package org.thingml.xtext.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONObject;

@WebServlet(name = "SaveModelServlet", urlPatterns = "/save-model")
public class SaveModelServlet extends HttpServlet {

    private static final String MODELS_BASE_DIR =
        "/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/models";

    // Pattern to pull dataset from data_analytics → data { … dataset "…" }
    private static final Pattern DATASET_PAT = Pattern.compile(
        "data_analytics\\s+\\w+[^\\{]*\\{[^\\}]*?\\bdata\\s*\\{[^\\}]*?\\bdataset\\s+\"([^\"]+)\"",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    // Pattern to capture configuration name, allowing an optional @compiler after it
    private static final Pattern CONFIG_PAT = Pattern.compile(
        "\\bconfiguration\\s+(\\w+)\\s*(?:@compiler\\s+[^\\s\\{]+)?\\s*\\{",
        Pattern.CASE_INSENSITIVE
    );

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println("🔧 [SaveModelServlet] POST /save-model");

        // 1) Retrieve userID from session
        String userID = (String) request.getSession().getAttribute("userID");
        if (userID == null || userID.trim().isEmpty()) {
            System.out.println("⛔ Missing userID");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false}");
            return;
        }
        System.out.println("✅ userID: " + userID);

        // 2) Read JSON body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        JSONObject json;
        try {
            json = new JSONObject(sb.toString());
            System.out.println("✅ JSON parsed");
        } catch (Exception e) {
            System.out.println("⛔ JSON parse error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false}");
            return;
        }

        // 3) Extract your ThingML model text
        String modelContent = json.optString("modelContent", "").trim();
        if (modelContent.isEmpty()) {
            System.out.println("⛔ Empty modelContent");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\":false}");
            return;
        }

        // 4) Extract datasetName from the data_analytics → data block
        String datasetName;
        Matcher m1 = DATASET_PAT.matcher(modelContent);
        if (m1.find()) {
            datasetName = m1.group(1).trim();
        } else {
            datasetName = "flow_dataset.csv";  // fallback default
            System.out.println("🔍 Defaulting datasetName=" + datasetName);
        }

        // 5) Extract configName from the configuration declaration
        String configName;
        Matcher m2 = CONFIG_PAT.matcher(modelContent);
        if (m2.find()) {
            configName = m2.group(1).trim();
        } else {
            configName = "RiverFlowCfg";  // fallback default
            System.out.println("🔍 Defaulting configName=" + configName);
        }

        // 6) Parse features …
        Pattern featPat = Pattern.compile(
            "features\\s+(.+?)\\s+(?=output_features|timestamps|\\})",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );
        Matcher featM = featPat.matcher(modelContent);
        List<String> featuresList = new ArrayList<>();
        if (featM.find()) {
            featuresList = Arrays.stream(featM.group(1).split(","))
                                 .map(String::trim)
                                 .filter(s -> !s.isEmpty())
                                 .collect(Collectors.toList());
        }
        System.out.println("🔍 Parsed features: " + featuresList);

        // 7) Infer feature types …
        Pattern propPat = Pattern.compile(
            "property\\s+(\\w+)\\s*:\\s*(\\w+)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher propM = propPat.matcher(modelContent);
        Map<String,String> propTypes = new LinkedHashMap<>();
        while (propM.find()) {
            String name = propM.group(1);
            String type = propM.group(2);
            if (featuresList.contains(name)) {
                propTypes.put(name, type);
            }
        }
        List<String> featureTypesList = featuresList.stream()
            .map(f -> propTypes.getOrDefault(f, "String"))
            .collect(Collectors.toList());
        System.out.println("🔍 Inferred feature_types: " + featureTypesList);

        // 8) Parse prediction columns …
        Pattern predPat = Pattern.compile(
            "prediction_results\\s+(.+?)\\s+(?=metrics|\\})",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );
        Matcher predM = predPat.matcher(modelContent);
        List<String> predictionsList = new ArrayList<>();
        if (predM.find()) {
            predictionsList = Arrays.stream(predM.group(1).split(","))
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(Collectors.toList());
        }
        System.out.println("🔍 Parsed prediction_columns: " + predictionsList);

        // 9) Flags sequential, timestamps & labels
        String sequential  = json.optString("sequential", "TRUE").trim().toUpperCase();
        String timestamps  = json.optString("timestamps", "ON").trim().toUpperCase();
        String labelsFlag  = json.optString("labels", "ON").trim().toUpperCase();
        System.out.println("🔍 Parsed sequential=" + sequential
                         + ", timestamps=" + timestamps
                         + ", labels=" + labelsFlag);

        // 10) Store in session for downstream usage
        request.getSession().setAttribute("datasetName",       datasetName);
        request.getSession().setAttribute("configName",        configName);
        request.getSession().setAttribute("features",          String.join(",", featuresList));
        request.getSession().setAttribute("feature_types",     String.join(",", featureTypesList));
        request.getSession().setAttribute("predictionColumns", String.join(",", predictionsList));
        request.getSession().setAttribute("sequential",        sequential);
        request.getSession().setAttribute("timestamps",        timestamps);
        request.getSession().setAttribute("labels",            labelsFlag);

        System.out.println("🗄️ Session => datasetName=" + datasetName
                         + ", configName="  + configName
                         + ", features="    + featuresList
                         + ", feature_types="+ featureTypesList
                         + ", predictionColumns=" + predictionsList
                         + ", sequential="  + sequential
                         + ", timestamps="  + timestamps
                         + ", labels="      + labelsFlag);

        // 11) Persist the ThingML model file
        File userDir = new File(MODELS_BASE_DIR, userID);
        if (!userDir.exists()) userDir.mkdirs();
        File modelFile = new File(userDir, userID + "_model.thingml");
        try (FileWriter fw = new FileWriter(modelFile)) {
            fw.write(modelContent);
            System.out.println("✅ Model saved: " + modelFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("⛔ Write error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false}");
            return;
        }

        // 12) Success
        response.getWriter().write("{\"success\":true}");
        System.out.println("🎉 SaveModelServlet complete");
    }
}
