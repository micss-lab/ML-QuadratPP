package org.thingml.xtext.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Builds (with Maven wrapper if available) and runs the generated JAR for a given user.
 *
 * <p><strong>Fix:</strong> The original implementation assumed that <code>mvn</code>
 * was available on the server PATH.  We now first look for the Maven Wrapper
 * <code>mvnw</code> in <code>codeDir</code>; if it exists we use that.  If not, we
 * fall back to a system Maven install, and if neither is available we stream a
 * descriptive error to the browser and abort the build.</p>
 */
@WebServlet(name = "RunCodeServlet", urlPatterns = "/run-code")
public class RunCodeServlet extends HttpServlet {

    private static final int BASE_PORT  = 9100;
    private static final int PORT_RANGE = 1000;
    private static final Map<String, Object> buildLocks = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            /* 1) Identify user */
            String userID = (String) request.getSession().getAttribute("userID");
            if (userID == null || !userID.matches("^[a-zA-Z0-9]+$")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("data: ❌ Invalid or missing userID\n\n");
                return;
            }

            /* 2) Config & dataset */
            String configName  = (String) request.getSession().getAttribute("configName");
            if (configName == null || configName.isEmpty()) configName = "River11FlowCfg";
            String datasetName = (String) request.getSession().getAttribute("datasetName");
            if (datasetName == null || datasetName.isEmpty()) datasetName = "flow_dataset.csv";

            writer.printf("data: ▶ Running JAR `%s-1.0.0-jar-with-dependencies.jar` for user=%s on dataset=%s\n\n",
                          configName, userID, datasetName);
            writer.flush();

            /* 3) Paths */
            Path baseDir   = Paths.get("/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/GeneratedCode", userID);
            Path codeDir   = baseDir.resolve("python_java");
            Path targetDir = codeDir.resolve("target");
            Path jarPath   = targetDir.resolve(configName + "-1.0.0-jar-with-dependencies.jar");

            /* 4) Build section guarded by lock */
            Object lock = buildLocks.computeIfAbsent(userID, k -> new Object());
            synchronized (lock) {
                if (isBuildNeeded(codeDir, jarPath)) {
                    writer.write("data: 🔨 Running Maven build…\n\n");
                    writer.flush();

                    String mvnCmd;
                    if (Files.exists(codeDir.resolve("mvnw"))) {
                        mvnCmd = "./mvnw -B clean package";   // Wrapper present: use it
                    } else if (systemMvnAvailable()) {
                        mvnCmd = "mvn -B clean package";       // Fallback to system Maven
                    } else {
                        writer.write("data: ❌ Maven not found (neither mvnw nor mvn). Build aborted.\n\n");
                        writer.flush();
                        return;
                    }

                    ProcessBuilder mvnPb = new ProcessBuilder("sh", "-c", "cd " + codeDir + " && " + mvnCmd);
                    mvnPb.redirectErrorStream(true);
                    Process mvn = mvnPb.start();
                    streamProcess(mvn, writer, "❌ Maven build timed out");
                    if (mvn.exitValue() != 0) {
                        writer.write("data: ❌ Maven build failed, aborting.\n\n");
                        writer.flush();
                        return;
                    }
                    updateBuildTimestamp(codeDir);
                    writer.write("data: ✅ Build succeeded.\n\n");
                    writer.flush();
                } else {
                    writer.write("data: ⚙ Using cached build.\n\n");
                    writer.flush();
                }
            }

            /* 5) Copy dataset */
            writer.write("data: 📂 Copying dataset `" + datasetName + "` into JAR folder…\n\n");
            writer.flush();
            Path src = Paths.get("/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/Datasets", datasetName);
            Path dst = targetDir.resolve(datasetName);
            runShell("cp " + src + " " + dst, writer, "❌ Copying dataset timed out");

            /* 6) Launch the JAR */
            int userPort = BASE_PORT + (Math.abs(userID.hashCode()) % PORT_RANGE);
            writer.write("data: 🚀 Launching JAR on port " + userPort + "…\n\n");
            writer.flush();

            ProcessBuilder jarPb = new ProcessBuilder(
                    "java",
                    "-Dprometheus.port=" + userPort,
                    "-jar",
                    jarPath.toString());
            jarPb.directory(targetDir.toFile());
            jarPb.redirectErrorStream(true);

            /* Prepend venv38 to PATH for any embedded python calls */
            Map<String, String> env = jarPb.environment();
            String venvBin = "/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/scripts/venv38/bin";
            env.put("PATH", venvBin + ":" + env.getOrDefault("PATH", System.getenv("PATH")));

            Process jarProc = jarPb.start();
            streamProcess(jarProc, writer, "❌ Application timed out");

            if (jarProc.exitValue() == 0) {
                writer.write("data: ✅ JAR execution completed successfully!\n\n");
            } else {
                writer.write("data: ❌ JAR exited with code " + jarProc.exitValue() + "\n\n");
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /* ——————————————————— utilities ——————————————————— */

    private boolean systemMvnAvailable() {
        try {
            Process p = new ProcessBuilder("mvn", "-v").start();
            return p.waitFor(5, TimeUnit.SECONDS) && p.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isBuildNeeded(Path codeDir, Path jar) throws IOException {
        if (!Files.exists(jar)) return true;
        Path stamp = codeDir.resolve(".lastbuild");
        if (!Files.exists(stamp)) return true;
        long jarTime  = Files.getLastModifiedTime(jar).toMillis();
        long lastTime = Long.parseLong(Files.readString(stamp).trim());
        return jarTime < lastTime;
    }

    private void updateBuildTimestamp(Path codeDir) throws IOException {
        Files.writeString(codeDir.resolve(".lastbuild"),
                          String.valueOf(System.currentTimeMillis()),
                          StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void runShell(String cmd, PrintWriter w, String timeoutMsg) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("sh", "-c", cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        streamProcess(p, w, timeoutMsg);
    }

    private void streamProcess(Process proc, PrintWriter w, String timeoutMsg)
            throws IOException, InterruptedException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            long start = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                w.write("data: " + line + "\n\n");
                w.flush();
                if (System.currentTimeMillis() - start > 10 * 60_000) {
                    w.write("data: " + timeoutMsg + "\n\n");
                    w.flush();
                    proc.destroyForcibly();
                    return;
                }
            }
            proc.waitFor(1, TimeUnit.MINUTES);
        }
    }
}
