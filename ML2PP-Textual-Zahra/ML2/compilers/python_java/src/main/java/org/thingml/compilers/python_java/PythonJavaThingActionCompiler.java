/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 */

package org.thingml.compilers.python_java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;


import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.thingml.compilers.Context;
import org.thingml.compilers.thing.common.CommonThingActionCompiler;
import org.thingml.xtext.constraints.ThingMLHelpers;
import org.thingml.xtext.constraints.Types;
import org.thingml.xtext.helpers.AnnotatedElementHelper;
import org.thingml.xtext.helpers.ConfigurationHelper;
import org.thingml.xtext.helpers.ThingHelper;
import org.thingml.xtext.helpers.TyperHelper;
import org.thingml.xtext.thingML.*;
import org.thingml.xtext.validation.TypeChecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


/**
 * Armin Moin, moin@in.tum.de, moin@arminmoin.de
 */
public class PythonJavaThingActionCompiler extends CommonThingActionCompiler {

	@Override
	public void generate(Increment action, StringBuilder builder, Context ctx) {
		if (action.getVar() instanceof Property) {
			builder.append("set" + ctx.firstToUpper(ctx.getVariableName(action.getVar())) + "(");
			builder.append("(" + PythonJavaHelper.getJavaType(action.getVar().getTypeRef().getType(),
					action.getVar().getTypeRef().getCardinality() != null, ctx) + ")");
			builder.append("(get" + ctx.firstToUpper(ctx.getVariableName(action.getVar())) + "()");
			builder.append(" + 1));\n");
		} else {
			super.generate(action, builder, ctx);
		}
	}

	@Override
	public void generate(Decrement action, StringBuilder builder, Context ctx) {
		if (action.getVar() instanceof Property) {
			builder.append("set" + ctx.firstToUpper(ctx.getVariableName(action.getVar())) + "(");
			builder.append("(" + PythonJavaHelper.getJavaType(action.getVar().getTypeRef().getType(),
					action.getVar().getTypeRef().getCardinality() != null, ctx) + ")");
			builder.append("(get" + ctx.firstToUpper(ctx.getVariableName(action.getVar())) + "()");
			builder.append(" - 1));\n");
		} else {
			super.generate(action, builder, ctx);
		}
	}

	@Override
	public void generate(EqualsExpression expression, StringBuilder builder, Context ctx) {
		final TypeRef leftType = TypeChecker.computeTypeOf(expression.getLhs());
		final TypeRef rightType = TypeChecker.computeTypeOf(expression.getRhs());
		if (TyperHelper.isA(leftType, Types.OBJECT_TYPEREF) && TyperHelper.isA(rightType, Types.OBJECT_TYPEREF)) {
			if (expression.getLhs() instanceof ExternExpression) {
				final ExternExpression extL = (ExternExpression) expression.getLhs();
				if (extL.getExpression().trim().equals("null")) {// we check for null pointer, should use ==
					super.generate(expression, builder, ctx);
					return;
				}
			}
			if (expression.getRhs() instanceof ExternExpression) {
				final ExternExpression extR = (ExternExpression) expression.getRhs();
				if (extR.getExpression().trim().equals("null")) {// we check for null pointer, should use ==
					super.generate(expression, builder, ctx);
					return;
				}
			}
			generate(expression.getLhs(), builder, ctx);
			builder.append(".equals(");
			generate(expression.getRhs(), builder, ctx);
			builder.append(")");
		} else {
			super.generate(expression, builder, ctx);
		}
	}

	@Override
	public void generate(NotEqualsExpression expression, StringBuilder builder, Context ctx) {
		final TypeRef leftType = TypeChecker.computeTypeOf(expression.getLhs());
		final TypeRef rightType = TypeChecker.computeTypeOf(expression.getRhs());
		if (TyperHelper.isA(leftType, Types.OBJECT_TYPEREF) || TyperHelper.isA(rightType, Types.OBJECT_TYPEREF)) {
			if (expression.getLhs() instanceof ExternExpression) {
				final ExternExpression extL = (ExternExpression) expression.getLhs();
				if (extL.getExpression().trim().equals("null")) {// we check for null pointer, should use ==
					super.generate(expression, builder, ctx);
					return;
				}
			}
			if (expression.getRhs() instanceof ExternExpression) {
				final ExternExpression extR = (ExternExpression) expression.getRhs();
				if (extR.getExpression().trim().equals("null")) {// we check for null pointer, should use ==
					super.generate(expression, builder, ctx);
					return;
				}
			}
			builder.append("!(");
			generate(expression.getLhs(), builder, ctx);
			builder.append(".equals(");
			generate(expression.getRhs(), builder, ctx);
			builder.append("))");
		} else {
			super.generate(expression, builder, ctx);
		}
	}

	@Override
	public void generate(SendAction action, StringBuilder builder, Context ctx) {
		builder.append(
				"send" + ctx.firstToUpper(action.getMessage().getName()) + "_via_" + action.getPort().getName() + "(");
		int i = 0;
		for (Expression p : action.getParameters()) {
			if (i > 0)
				builder.append(", ");
			int j = 0;
			for (Parameter fp : action.getMessage().getParameters()) {
				if (i == j) {// parameter p corresponds to formal parameter fp
					cast(fp.getTypeRef().getType(), fp.getTypeRef().isIsArray(), p, builder, ctx);
					break;
				}
				j++;
			}
			i++;
		}
		builder.append(");\n");
	}

	@Override
	public void generate(StartSession action, StringBuilder builder, Context ctx) {
		builder.append("final Component " + action.getSession().getName() + " = new "
				+ ctx.firstToUpper(ThingMLHelpers.findContainingThing(action.getSession()).getName()) + "()");
		for (Property p : ThingHelper.allPropertiesInDepth(ThingMLHelpers.findContainingThing(action.getSession()))) {
			builder.append(".init" + ctx.firstToUpper(ctx.getVariableName(p)) + "(");
			if (p.getTypeRef().isIsArray() || p.getTypeRef().getCardinality() != null) {
				builder.append("Arrays.copyOf("
						+ ctx.firstToUpper(ThingMLHelpers.findContainingThing(action.getSession()).getName()) + ".this."
						+ ctx.getVariableName(p) + ", "
						+ ctx.firstToUpper(ThingMLHelpers.findContainingThing(action.getSession()).getName()) + ".this."
						+ ctx.getVariableName(p) + ".length)");
			} else {
				builder.append(ctx.firstToUpper(ThingMLHelpers.findContainingThing(action.getSession()).getName())
						+ ".this." + ctx.getVariableName(p));
			}
			builder.append(")");
		}
		builder.append(".buildBehavior(\"" + action.getSession().getName() + "\", "
				+ ctx.firstToUpper(ThingMLHelpers.findContainingThing(action.getSession()).getName()) + ".this);\n");
		builder.append("final Component root = ("
				+ ctx.firstToUpper(ThingMLHelpers.findContainingThing(action.getSession()).getName())
				+ ".this.root == null)? "
				+ ctx.firstToUpper(ThingMLHelpers.findContainingThing(action.getSession()).getName()) + ".this : "
				+ ctx.firstToUpper(ThingMLHelpers.findContainingThing(action.getSession()).getName())
				+ ".this.root;\n");
		builder.append("root.addSession(" + action.getSession().getName() + ");\n");
	}

	@Override
	public void generate(FunctionCallStatement action, StringBuilder builder, Context ctx) {
		if (AnnotatedElementHelper.isDefined(action.getFunction(), "fork_thread", "true")
				&& action.getFunction().getTypeRef() != null) {
			System.err.println("function " + action.getFunction().getName()
					+ "cannot be called with @fork_thread, as its return type ("
					+ action.getFunction().getTypeRef().getType().getName() + ") is not void");
			throw new UnsupportedOperationException("function " + action.getFunction().getName()
					+ "cannot be called with @fork_thread, as its return type ("
					+ action.getFunction().getTypeRef().getType().getName() + ") is not void");
		}

		if (AnnotatedElementHelper.isDefined(action.getFunction(), "fork_thread", "true")) {
			builder.append("new Thread(new Runnable(){public void run() {\n");
		}

		builder.append(action.getFunction().getName() + "(");
		int i = 0;
		for (Expression p : action.getParameters()) {
			if (i > 0)
				builder.append(", ");
			int j = 0;
			for (Parameter fp : action.getFunction().getParameters()) {
				if (i == j) {// parameter p corresponds to formal parameter fp
					cast(fp.getTypeRef().getType(), fp.getTypeRef().isIsArray(), p, builder, ctx);
					break;
				}
				j++;
			}
			i++;
		}
		builder.append(");\n");

		if (AnnotatedElementHelper.isDefined(action.getFunction(), "fork_thread", "true")) {
			builder.append("}}).start();\n");
		}
	}

	@Override
	public void generate(LocalVariable action, StringBuilder builder, Context ctx) {
		if (action.isReadonly()) {
			builder.append("final ");
		}

		// Define the type of the variable
		builder.append(
				PythonJavaHelper.getJavaType(action.getTypeRef().getType(), action.getTypeRef().isIsArray(), ctx));
		builder.append(" ");

		builder.append(ctx.getVariableName(action));

		// Define the initial value for that variable
		if (action.getInit() != null) {
			builder.append(" = ");
			cast(action.getTypeRef().getType(), action.getTypeRef().isIsArray(), action.getInit(), builder, ctx);
			builder.append(";\n");
		} else {
			if (action.getTypeRef().getCardinality() != null) {
				builder.append(" = new " + PythonJavaHelper.getJavaType(action.getTypeRef().getType(), false, ctx)
						+ "[(int)(");
				generate(action.getTypeRef().getCardinality(), builder, ctx);
				builder.append(")];");
			} else {
				if (action.getTypeRef().getType() instanceof PrimitiveType) {
					builder.append(" = " + PythonJavaHelper.getDefaultValue(action.getTypeRef().getType()) + ";");
				} else {
					builder.append(" = null;");
				}
			}
		}
		builder.append("\n");
	}

	@Override
	public void generate(ErrorAction action, StringBuilder builder, Context ctx) {
		final Thing t = ThingMLHelpers.findContainingThing(action);
		if (AnnotatedElementHelper.isDefined(t, "stdout_sync", "true")) {
			builder.append("synchronized(System.err) {\n");
		}
		if (action.isLine())
			builder.append("System.err.println(\"\"");
		else
			builder.append("System.err.print(\"\"");
		for (Expression msg : action.getMsg()) {
			builder.append("+(");
			generate(msg, builder, ctx);
			builder.append(")");
		}
		builder.append(");\n");
		if (AnnotatedElementHelper.isDefined(t, "stdout_sync", "true")) {
			builder.append("}\n");
		}
	}

	@Override
	public void generate(PrintAction action, StringBuilder builder, Context ctx) {
		final Thing t = ThingMLHelpers.findContainingThing(action);
		if (AnnotatedElementHelper.isDefined(t, "stdout_sync", "true")) {
			builder.append("synchronized(System.out) {\n");
		}
		if (action.isLine())
			builder.append("System.out.println(\"\"");
		else
			builder.append("System.out.print(\"\"");
		for (Expression msg : action.getMsg()) {
			builder.append("+(");
			generate(msg, builder, ctx);
			builder.append(")");
		}
		builder.append(");\n");
		if (AnnotatedElementHelper.isDefined(t, "stdout_sync", "true")) {
			builder.append("}\n");
		}
	}

	@Override
	public void generate(PropertyReference expression, StringBuilder builder, Context ctx) {
		if (!ctx.getAtInitTimeLock()) {
			if (expression.getProperty() instanceof Property
					&& ((Property) expression.getProperty()).getTypeRef().getCardinality() == null)
				builder.append("get" + ctx.firstToUpper(ctx.getVariableName(expression.getProperty())) + "()");
			else
				builder.append(ctx.getVariableName(expression.getProperty()));
		} else {
			Property p = (Property) expression.getProperty();
			if (!p.isReadonly()) {
				System.out.println(
						"Error: non Read-only property (" + p.getName() + ") used in array cardinality definition.");
			}
			Expression e = ConfigurationHelper.initExpressions(ctx.getCurrentConfiguration(), ctx.currentInstance, p)
					.get(0);
			generate(e, builder, ctx);
		}
	}

	@Override
	public void generate(EnumLiteralRef expression, StringBuilder builder, Context ctx) {
		final EnumerationLiteral lit = expression.getLiteral();
		if (((Enumeration) lit.eContainer()).getTypeRef() != null) {
			generate(lit.getInit(), builder, ctx);
		} else {
			if (AnnotatedElementHelper.hasAnnotation(lit, "enum_val")) {
				final String value = AnnotatedElementHelper.annotation(lit, "enum_val").get(0);
				try {
					Double.parseDouble(value);
					builder.append(AnnotatedElementHelper.annotation(lit, "enum_val").get(0));
				} catch (NumberFormatException nfe) {
					builder.append("\"" + AnnotatedElementHelper.annotation(lit, "enum_val").get(0) + "\"");
				}
			} else {
				builder.append(ctx.firstToUpper(expression.getEnum().getName()) + "_ENUM."
						+ ((Enumeration) expression.getLiteral().eContainer()).getName().toUpperCase() + "_"
						+ expression.getLiteral().getName().toUpperCase());
			}
		}
	}

	@Override
	public void generate(FunctionCallExpression expression, StringBuilder builder, Context ctx) {
		if (ctx.currentInstance != null)
			builder.append(ctx.getInstanceName(ctx.currentInstance) + ".");
		builder.append(expression.getFunction().getName() + "(");

		int i = 0;
		for (Expression p : expression.getParameters()) {

			if (i > 0)
				builder.append(", ");
			int j = 0;
			for (Parameter fp : expression.getFunction().getParameters()) {
				if (i == j) {// parameter p corresponds to formal parameter fp
					cast(fp.getTypeRef().getType(), fp.getTypeRef().isIsArray(), p, builder, ctx);
					break;
				}
				j++;
			}
			i++;
		}
		builder.append(")");
	}

	@Override
	protected String castArrayIndex(String builder) {
		return "(int)(" + builder + ")";
	}

	@Override
	public void cast(Type type, boolean isArray, Expression exp, StringBuilder builder, Context ctx) {
		if (!(type instanceof Enumeration)) {
			if (AnnotatedElementHelper.hasAnnotation(type, "java_type")) {
				if (!isArray)
					builder.append("(" + AnnotatedElementHelper.annotation(type, "java_type").toArray()[0] + ") ");
				else
					builder.append("(" + AnnotatedElementHelper.annotation(type, "java_type").toArray()[0] + "[]) ");
			} else {
				if (!isArray)
					builder.append("(Object) ");
				else
					builder.append("(Object[]) ");
			}
		}
		builder.append("(");
		generate(exp, builder, ctx);
		builder.append(")");
	}

	@Override
	public void generate(EventReference expression, StringBuilder builder, Context ctx) {
		builder.append((((ReceiveMessage) expression.getReceiveMsg()).getMessage().getName()) + "."
				+ expression.getParameter().getName());
	}

	@Override
	public void generate(IntegerLiteral expression, StringBuilder builder, Context ctx) {
		super.generate(expression, builder, ctx);
		if (expression.getIntValue() > 2147483647 || expression.getIntValue() < -2147483647)
			builder.append("L");
	}

	@Override
	public void generate(ArrayInit expression, StringBuilder builder, Context ctx) {
		final EObject container = expression.eContainer();
		final TypeRef typeref = (TypeRef) container.eGet(ThingMLPackage.eINSTANCE.getVariable_TypeRef());
		final Type t = typeref.getType();
		builder.append("new " + PythonJavaHelper.getJavaType(t, false, ctx) + "[]");
		builder.append("{");
		for (Expression e : expression.getValues()) {
			if (expression.getValues().indexOf(e) > 0)
				builder.append(", ");
			generate(e, builder, ctx);
		}
		builder.append("}");
	}

	@Override
	public void generate(ForAction fa, StringBuilder builder, Context ctx) {
		if (fa.getIndex() != null) {
			builder.append("{\n");
			final String t = PythonJavaHelper.getJavaType(fa.getIndex().getTypeRef().getType(), false, ctx);
			builder.append(t + " " + ctx.getVariableName(fa.getIndex()) + " = 0;\n");
		}
		final String t = PythonJavaHelper.getJavaType(fa.getVariable().getTypeRef().getType(), false, ctx);
		builder.append("for(" + t + " " + ctx.getVariableName(fa.getVariable()) + " : "
				+ ctx.getVariableName(fa.getArray().getProperty()) + ") {\n");
		generate(fa.getAction(), builder, ctx);
		if (fa.getIndex() != null) {
			builder.append(ctx.getVariableName(fa.getIndex()) + "++;\n");
		}
		builder.append("}\n");
		if (fa.getIndex() != null) {
			builder.append("}\n");
		}
	}

	// ML2 Helper String first to upper-case (capitalize first letter)
	private String first_to_upper(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	private String boolToPython(BooleanLiteral boolLiteral) {
    return boolLiteral != null ? (boolLiteral.isBoolValue() ? "True" : "False") : "False";
}

	// ML2: da_save











@Override
public void generate(DASaveAction action, StringBuilder builder, Context ctx) {
    // Compute base path and output paths
    final String basePath = ctx.getOutputDirectory()
            .toPath()
            .toAbsolutePath()
            .normalize()
            .toString()
            .replace("\\", "/");
    final String OUT_TXT = basePath + "/src/python-scripts/outputs/prediction.txt";
    final String DATASET = action.getDataAnalytics().getDataset();

    // ------- Begin generated Java source -------
    builder.append("// -------- Start saving data --------\n")
           .append("System.out.println(\"📁 Saving data to dataset...\");\n\n")
           // create ./data folder if needed
           .append("File dataDir = new File(\"data\");\n")
           .append("if (!dataDir.exists()) {\n")
           .append("    System.out.println(\"📂 Creating directory: data\");\n")
           .append("    dataDir.mkdirs();\n")
           .append("}\n\n")
           .append("File datasetFile = new File(\"").append(DATASET).append("\");\n")
           .append("try (BufferedWriter br = new BufferedWriter(new FileWriter(datasetFile, true))) {\n");

    // Timestamp column (optional)
    if (action.getDataAnalytics().getTimestamps() == Timestamps.ON) {
        builder.append("    String ts = java.time.LocalDateTime.now()\n")
               .append("        .format(java.time.format.DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm:ss\"));\n")
               .append("    br.write(ts + \",\");\n")
               .append("    System.out.println(\"🕒 Timestamp saved: \" + ts);\n\n");
    }

    // Write each declared feature
    for (Variable f : action.getDataAnalytics().getFeatures()) {
        String vName = ctx.getVariableName(f);
        if (f.getTypeRef().isIsArray()) {
            builder.append("    String arrVal_").append(vName)
                   .append(" = java.util.Arrays.toString(").append(vName).append(").replace(\",\", \" \" );\n")
                   .append("    br.write(arrVal_").append(vName).append(" + \",\");\n")
                   .append("    System.out.println(\"✅ Array feature [").append(vName)
                   .append("] = \" + arrVal_").append(vName).append(");\n\n");
        } else {
            builder.append("    br.write(String.valueOf(").append(vName).append(") + \",\");\n")
                   .append("    System.out.println(\"✅ Feature [").append(vName)
                   .append("] = \" + ").append(vName).append(");\n\n");
        }
    }

    // ----- Read predictions from prediction.txt -----
    builder.append("    java.io.File predFile = new java.io.File(\"").append(OUT_TXT).append("\");\n")
           .append("    if (predFile.exists()) {\n")
           .append("        java.util.List<String> all = java.nio.file.Files.readAllLines(predFile.toPath());\n")
           .append("        String last = all.stream()\n")
           .append("            .filter(s -> s.contains(\"🔹 Prediction:\"))\n")
           .append("            .reduce((first, second) -> second)\n")
           .append("            .orElse(\"\");\n")
           .append("        if (!last.isEmpty()) {\n")
           .append("            String[] preds = last.substring(last.indexOf(\"[\") + 1, last.indexOf(\"]\"))\n")
           .append("                .split(\"\\\\s*,\\\\s*\");\n")
           // dynamically map to FlowAnalytics_predctionN_var
           .append("            for (int i = 0; i < preds.length; i++) {\n")
           .append("                int v = (int) Float.parseFloat(preds[i]);\n")
           .append("                try {\n")
           .append("                    java.lang.reflect.Field f = this.getClass()\n")
           .append("                        .getDeclaredField(\"FlowAnalytics_predction\" + (i+1) + \"_var\");\n")
           .append("                    f.setAccessible(true);\n")
           .append("                    f.setInt(this, v);\n")
           .append("                } catch (Exception e) {\n")
           .append("                    System.err.println(\"❌ Failed to set predction\" + (i+1) + \": \" + e.getMessage());\n")
           .append("                }\n")
           .append("            }\n")
           // append predictions to CSV
           .append("            for (int i = 0; i < preds.length; i++) {\n")
           .append("                br.write(preds[i]);\n")
           .append("                if (i < preds.length - 1) br.write(\",\");\n")
           .append("            }\n")
           .append("            System.out.println(\"📊 Predictions appended: \" + java.util.Arrays.toString(preds));\n")
           .append("        }\n")
           .append("    } else {\n")
           .append("        System.out.println(\"⚠️  Prediction file not found (\" + predFile + \") – skipping preds.\");\n")
           .append("    }\n")
           .append("    br.newLine();\n")
           .append("    br.flush();\n")
           .append("} catch (java.io.IOException e) {\n")
           .append("    System.err.println(\"❌ I/O error during save: \" + e.getMessage());\n")
           .append("    e.printStackTrace();\n")
           .append("}\n");
}

// ML2: da_preprocess

@Override
public void generate(DAPreprocessAction action, StringBuilder builder, Context ctx) {
    // Normalize output directory path
    Path path = ctx.getOutputDirectory().toPath().toAbsolutePath().normalize();
    
    
    
    final String basePath = ctx.getOutputDirectory()
                                .toPath()
                                .toAbsolutePath()
                                .normalize()
                                .toString()
                                .replace("\\", "/");

    String outputTxtPath = basePath + "/src/python-scripts/outputs/prediction.txt";


    // Prepare the plots directory
    Path plotsPath = path.resolve("src/python-scripts/plots");
    File plotsDir = plotsPath.toFile();
    if (!plotsDir.exists()) {
        boolean created = plotsDir.mkdirs();
        if (created) {
            System.out.println("Plots directory created successfully at: " + plotsDir.getAbsolutePath());
        } else {
            System.out.println("Failed to create plots directory at: " + plotsDir.getAbsolutePath());
        }
    } else {
        System.out.println("Plots directory already exists at: " + plotsDir.getAbsolutePath());
    }

    // Dataset path (fallback to tmp_dataset.csv)
    String dataset = path.resolve("tmp_dataset.csv").toString();
    if (action.getDataAnalytics().getDataset() != null) {
        dataset = action.getDataAnalytics().getDataset();
    } else {
        System.err.println("WARNING: No dataset specified in the data analytics section! Using tmp_dataset.csv as the default...\n");
    }

    // Handle AutoML
    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.NOT_SET_VALUE) {
        System.err.println("WARNING: Parameter automl not specified in the data analytics section. Thus, the default value OFF is assumed...\n");
        action.getDataAnalytics().setAutoML(AutoML.OFF);
    }

    // Handle Sequential
    String sequential = "";
    if (action.getDataAnalytics().getSequential().getValue() == Sequential.NOT_SET_VALUE) {
        if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
            if (action.getDataAnalytics().getTimestamps().getValue() == Timestamps.ON_VALUE) {
                System.err.println("WARNING: Parameter sequential not specified! Assuming TRUE due to AutoML ON and timestamps ON.\n");
                action.getDataAnalytics().setSequential(Sequential.TRUE);
            } else {
                System.err.println("WARNING: Parameter sequential not specified! Assuming FALSE.\n");
                action.getDataAnalytics().setSequential(Sequential.FALSE);
            }
        } else {
            System.err.println("WARNING: Parameter sequential not specified! Assuming FALSE.\n");
            action.getDataAnalytics().setSequential(Sequential.FALSE);
        }
    }
    sequential = action.getDataAnalytics().getSequential().getName();

    // Handle Timestamps
    String timestamps = "";
    if (action.getDataAnalytics().getTimestamps().getValue() == Timestamps.NOT_SET_VALUE) {
        if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
            if (action.getDataAnalytics().getSequential().getValue() == Sequential.TRUE_VALUE) {
                System.err.println("WARNING: Parameter timestamps not specified! Assuming ON due to AutoML ON and sequential TRUE.\n");
                action.getDataAnalytics().setTimestamps(Timestamps.ON);
            } else {
                System.err.println("WARNING: Parameter timestamps not specified! Assuming OFF.\n");
                action.getDataAnalytics().setTimestamps(Timestamps.OFF);
            }
        } else {
            System.err.println("WARNING: Parameter timestamps not specified! Assuming OFF.\n");
            action.getDataAnalytics().setTimestamps(Timestamps.OFF);
        }
    }
    timestamps = action.getDataAnalytics().getTimestamps().getName();

    // Prepare labels and features
    String labels = action.getDataAnalytics().getLabels().getName();
    EList<Property> features = action.getDataAnalytics().getFeatures();
    List<String> feature_types = new ArrayList<>();
    for (Property prop : features) {
        if (prop.getTypeRef().isIsArray()) {
            feature_types.add(prop.getTypeRef().getType().getName() + "[]");
        } else {
            feature_types.add(prop.getTypeRef().getType().getName());
        }
    }

    // Generate Python script
    generatePythonDAPreprocessingScript(path.resolve("src").toString(), action);

    // Prepare output folders in builder
    builder.append("File picklesDir = new File(\"" + path + "/src/python-scripts/pickles/\");\n");
    builder.append("if (!picklesDir.exists()) picklesDir.mkdirs();\n");

    builder.append("File plotsDir = new File(\"" + path + "/src/python-scripts/plots/\");\n");
    builder.append("if (!plotsDir.exists()) plotsDir.mkdirs();\n");

    // Make the script executable
    builder.append("List<String> chmodCmd = new ArrayList<>();\n");
    builder.append("chmodCmd.add(\"chmod\");\n");
    builder.append("chmodCmd.add(\"u+x\");\n");
    builder.append("chmodCmd.add(\"" + path + "/src/python-scripts/preprocess.py\");\n");
    builder.append("try {\n");
    builder.append("    new ProcessBuilder(chmodCmd).start();\n");
    builder.append("} catch(Exception e) { System.out.println(e); }\n");

    // Build Python execution command
    builder.append("List<String> list = new ArrayList<>();\n");
    builder.append("list.add(\"/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/scripts/venv/bin/python\");\n");
    builder.append("list.add(\"" + path + "/src/python-scripts/preprocess.py\");\n");
    builder.append("list.add(\"" + dataset + "\");\n");
    builder.append("list.add(\"" + sequential + "\");\n");
    builder.append("list.add(\"" + timestamps + "\");\n");

    String features_str = features.stream().map(Property::getName).collect(java.util.stream.Collectors.joining(","));
    String feature_types_str = feature_types.stream().collect(java.util.stream.Collectors.joining(","));

    builder.append("list.add(\"" + features_str + "\");\n");
    builder.append("list.add(\"" + feature_types_str + "\");\n");
    builder.append("list.add(\"" + labels + "\");\n");

    // Execute Python script and print output step-by-step
    builder.append("try {\n");
    builder.append("    ProcessBuilder pb = new ProcessBuilder(list);\n");
    builder.append("    pb.redirectErrorStream(true);\n");
    builder.append("    pb.environment().put(\"PYTHONIOENCODING\", \"utf-8\");\n");
    builder.append("    Process p = pb.start();\n");
    builder.append("    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));\n");
    builder.append("    String line;\n");
    builder.append("    while ((line = reader.readLine()) != null) {\n");
    builder.append("        System.out.println(\"[preprocess.py] \" + line);\n");
    builder.append("    }\n");
    builder.append("    int exitCode = p.waitFor();\n");
    builder.append("    System.out.println(\"preprocess.py exited with code: \" + exitCode);\n");
    builder.append("} catch(Exception e) {\n");
    builder.append("    System.out.println(\"Error running preprocess.py: \" + e.getMessage());\n");
    builder.append("    e.printStackTrace();\n");
    builder.append("}\n");
}


	// ML2: Code generator for the data analytics preprocessing Python script,
// preprocess.py
private void generatePythonDAPreprocessingScript(String path_str, DAPreprocessAction action) {
    System.out.println("📁 Starting script generation for preprocess.py...");
    
    StringBuilder pythonScriptStringBuilder = new StringBuilder();

    // STEP 0: Inject prediction result columns
    System.out.println("🔍 Adding prediction_columns...");
    StringBuilder predictionVars = new StringBuilder();
    if (action.getDataAnalytics().getPredictionResults() != null &&
        !action.getDataAnalytics().getPredictionResults().isEmpty()) {

        predictionVars.append("prediction_columns = [");
        for (int i = 0; i < action.getDataAnalytics().getPredictionResults().size(); i++) {
            String predName = action.getDataAnalytics().getPredictionResults().get(i).getName();
            predictionVars.append("\"").append(predName).append("\"");
            if (i < action.getDataAnalytics().getPredictionResults().size() - 1) {
                predictionVars.append(", ");
            }
            System.out.println("   ➕ Added prediction result: " + predName);
        }
        predictionVars.append("]\n");
    } else {
        predictionVars.append("prediction_columns = []\n");
        System.out.println("⚠️ No prediction results specified.");
    }
    pythonScriptStringBuilder.append(predictionVars.toString());
    System.out.println("✅ Prediction columns section added.");

    // STEP 1: Add imports
    System.out.println("📥 Adding required imports...");
    pythonScriptStringBuilder.append("import sys\n");
    pythonScriptStringBuilder.append("import os\n");
    pythonScriptStringBuilder.append("import pickle\n");
    pythonScriptStringBuilder.append("import time, datetime\n");
    pythonScriptStringBuilder.append("import pandas as pd\n");
    pythonScriptStringBuilder.append("import numpy as np\n");
    pythonScriptStringBuilder.append("from sklearn.preprocessing import LabelEncoder\n");
    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
    pythonScriptStringBuilder.append("import seaborn as sns\n");
    pythonScriptStringBuilder.append("from scipy import stats\n");
    pythonScriptStringBuilder.append("from sklearn.impute import KNNImputer\n\n");
    System.out.println("✅ Imports added.");

    // STEP 2: Parse input arguments
    System.out.println("📦 Adding input arguments parser...");
    pythonScriptStringBuilder.append("dataset = sys.argv[1]\n");
    pythonScriptStringBuilder.append("sequential = sys.argv[2]\n");
    pythonScriptStringBuilder.append("timestamps = sys.argv[3]\n");
    pythonScriptStringBuilder.append("features = sys.argv[4].split(',')\n");
    pythonScriptStringBuilder.append("feature_types = sys.argv[5].split(',')\n");
    pythonScriptStringBuilder.append("labels = sys.argv[6]\n\n");
    System.out.println("✅ Input arguments parser added.");

    //
    // STEP 3: PREPARE COLUMN NAMES
    //
    pythonScriptStringBuilder.append("from collections import Counter\n\n");
    pythonScriptStringBuilder.append("col_names = []\n");
    pythonScriptStringBuilder.append("num_col_names = []\n");
    pythonScriptStringBuilder.append("cat_col_names = []\n\n");

    pythonScriptStringBuilder.append("if timestamps.lower() == 'on':\n");
    pythonScriptStringBuilder.append("    col_names.append('timestamp')\n\n");

    pythonScriptStringBuilder.append("for i in range(len(features)):\n");
    pythonScriptStringBuilder.append("    feature = features[i]\n");
    pythonScriptStringBuilder.append("    feature_type = feature_types[i]\n");
    pythonScriptStringBuilder.append("    if 'String' in feature_type or 'Char' in feature_type:\n");
    pythonScriptStringBuilder.append("        cat_col_names.append(feature)\n");
    pythonScriptStringBuilder.append("    if 'Int' in feature_type or 'Double' in feature_type:\n");
    pythonScriptStringBuilder.append("        num_col_names.append(feature)\n");
    pythonScriptStringBuilder.append("    col_names.append(feature)\n\n");


    //
    // STEP 4: LOAD DATASET & SAVE ORIGINAL
    
    
    
  // STEP 4: LOAD DATASET, REMOVE PREDICTION COLUMNS & SAVE ORIGINAL
pythonScriptStringBuilder.append("raw_df = pd.read_csv(dataset, header=None, dtype=str, skip_blank_lines=True, on_bad_lines='skip')\n");
pythonScriptStringBuilder.append("raw_df.columns = col_names + [f'extra_col_{i}' for i in range(len(raw_df.columns) - len(col_names))]\n");
pythonScriptStringBuilder.append("df = raw_df.drop(columns=[col for col in raw_df.columns if col in prediction_columns], errors='ignore')\n");
pythonScriptStringBuilder.append("df = df[[col for col in df.columns if col in col_names]]\n\n");
pythonScriptStringBuilder.append("original_df = df.copy(deep=True)\n");

  
    pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/preprocess_original_df.pickle', 'wb') as pickle_file:\n");
    pythonScriptStringBuilder.append("    pickle.dump(original_df, pickle_file)\n\n");
    
    
    

    pythonScriptStringBuilder.append("if(timestamps.lower() == 'on'):\n");
pythonScriptStringBuilder.append("    timeformat = \"%d-%m-%Y %H:%M:%S\"\n");
pythonScriptStringBuilder.append("    with open('" + path_str + "/python-scripts/pickles/"
        + "preprocess_timeformat.pickle', 'wb') as pickle_file:\n");
pythonScriptStringBuilder.append("        pickle.dump(timeformat, pickle_file)\n");
pythonScriptStringBuilder.append("    df.timestamp = df.timestamp.apply(lambda x: datetime.datetime.strptime(x, timeformat))\n");
pythonScriptStringBuilder.append("    df.set_index('timestamp', inplace=True)\n\n");


// … after timestamp indexing …
Resampling resampling = action.getDataAnalytics().getResampling();
if (resampling != null && resampling != Resampling.NOT_SET) {
    String rule;
    switch (resampling) {
        case MINUTELY:
            rule = "'T'";       // 1-minute
            break;
        case HOURLY:
            rule = "'H'";       // 1-hour
            break;
        case DAILY:
            rule = "'D'";       // 1-day
            break;
        case WEEKLY:
            rule = "'W'";       // 1-week
            break;
        case MONTHLY:
            rule = "'M'";       // 1-month
            break;
        case YEARLY:
            rule = "'Y'";       // 1-year
            break;
        default:
            rule = null;        // should never happen
    }
    if (rule != null) {
        pythonScriptStringBuilder.append(
            "df = df.resample(" + rule + ").mean()\n"
          + "print('Data resampled to " + resampling.name().toLowerCase() + " frequency')\n\n"
        );
    }
}




    //
    // STEP 5: HANDLE TIMESTAMPS
    
   // pythonScriptStringBuilder.append("pickle_dir = '" + path_str + "/python-scripts/pickles'\n\n");
    //
  //  pythonScriptStringBuilder.append("if timestamps.lower() == 'on':\n");
   // pythonScriptStringBuilder.append("    df['timestamp'] = pd.to_datetime(df['timestamp'], errors='coerce')\n");
  //  pythonScriptStringBuilder.append("    df.dropna(subset=['timestamp'], inplace=True)\n");
  //  pythonScriptStringBuilder.append("    df.set_index('timestamp', inplace=True)\n");
 //   pythonScriptStringBuilder.append("    print('Timestamp set as index')\n\n");
    
    
//    pythonScriptStringBuilder.append("    # 💾 Save timestamp format\n");
 //   pythonScriptStringBuilder.append("    timeformat = \"%d-%m-%Y %H:%M:%S\"\n");
  //  pythonScriptStringBuilder.append("    with open(os.path.join(pickle_dir, 'preprocess_timeformat.pickle'), 'wb') as pickle_file:\n");
 //   pythonScriptStringBuilder.append("        pickle.dump(timeformat, pickle_file)\n");
 //   pythonScriptStringBuilder.append("    print(f\"✅ Timestamp format saved to {pickle_dir}/preprocess_timeformat.pickle\")\n\n");



    //
    // STEP 6: CONVERT NUMERIC COLUMNS
    //
    pythonScriptStringBuilder.append("for col in num_col_names:\n");
    pythonScriptStringBuilder.append("    df[col] = pd.to_numeric(df[col], errors='coerce')\n\n");


    //
    // STEP 7: PLOTS DIRECTORY
    //
    pythonScriptStringBuilder.append("plots_path = '" + path_str + "/python-scripts/plots/'\n\n");


    //
    // STEP 8: LABEL ENCODING
    //
    pythonScriptStringBuilder.append("if len(cat_col_names) != 0:\n");
    pythonScriptStringBuilder.append("    cat_col_names = [col for col in cat_col_names if col != 'timestamp']\n");
    pythonScriptStringBuilder.append("    le = LabelEncoder()\n");
    pythonScriptStringBuilder.append("    for col in cat_col_names:\n");
    pythonScriptStringBuilder.append("        df[col] = le.fit_transform(df[col])\n");
    pythonScriptStringBuilder.append("    with open('" + path_str + "/python-scripts/pickles/preprocess_label_encoder.pickle', 'wb') as pickle_file:\n");
    pythonScriptStringBuilder.append("        pickle.dump(le, pickle_file)\n\n");


    //
    // STEP 9: FILL MISSING VALUES
    //
    if (action.getDataAnalytics().getFill_missing_value() != null &&
       (action.getDataAnalytics().getFill_missing_value() == FillingMissingValue.BF ||
        action.getDataAnalytics().getFill_missing_value() == FillingMissingValue.FF ||
        action.getDataAnalytics().getFill_missing_value() == FillingMissingValue.INTERPOLATION ||
        action.getDataAnalytics().getFill_missing_value() == FillingMissingValue.MEAN)) 
    {
        switch (action.getDataAnalytics().getFill_missing_value()) {
            case MEAN:
                pythonScriptStringBuilder.append("df.fillna(df.mean(), inplace=True)\n");
                break;
            case INTERPOLATION:
                pythonScriptStringBuilder.append("df.interpolate(method='linear', inplace=True)\n");
                break;
            case FF:
                pythonScriptStringBuilder.append("df.fillna(method='ffill', inplace=True)\n");
                break;
            case BF:
                pythonScriptStringBuilder.append("df.fillna(method='bfill', inplace=True)\n");
                break;
            default:
                pythonScriptStringBuilder.append("df.fillna(0, inplace=True)\n");
                break;
        }
        pythonScriptStringBuilder.append("print('Missing values handled')\n\n");
    }


    //
    // STEP 10: OUTLIER DETECTION (Z-SCORE)
    //
    if (action.getDataAnalytics().getOutlier_detection() != null &&
        action.getDataAnalytics().getOutlier_detection() == OutlierDetection.ON) 
    {
        pythonScriptStringBuilder.append("# Outlier Detection using Z-score\n");
        pythonScriptStringBuilder.append("z_scores = np.abs(stats.zscore(df.select_dtypes(include=[np.number])))\n");
        pythonScriptStringBuilder.append("df = df[(z_scores < 3).all(axis=1)]\n");
        pythonScriptStringBuilder.append("print('Outliers removed')\n\n");
    }


    
    // STEP 11: COMBINE THRESHOLD FOR COMMON PERIODS
   

    long combineThreshold = action.getDataAnalytics().getCombine_threshold();

    if (combineThreshold > 0){
    pythonScriptStringBuilder.append("combine_threshold = " + combineThreshold + "\n");
    pythonScriptStringBuilder.append("print('Common Period Threshold set:', combine_threshold)\n\n");

    pythonScriptStringBuilder.append("# Function to accurately determine common periods with limited missing data\n");
    pythonScriptStringBuilder.append("def get_common_periods_sections(df, max_missing_threshold=10):\n");
    pythonScriptStringBuilder.append("    import numpy as np\n");
    pythonScriptStringBuilder.append("    df = df.replace('', np.nan)\n");
    pythonScriptStringBuilder.append("    common_periods = df.notna().all(axis=1)\n");
    pythonScriptStringBuilder.append("    first_complete_row = common_periods.idxmax()\n");
    pythonScriptStringBuilder.append("    common_periods = common_periods[first_complete_row:]\n");
    pythonScriptStringBuilder.append("    d = common_periods.diff().ne(0)\n");
    pythonScriptStringBuilder.append("    starts = d.index[d & common_periods].tolist()\n");
    pythonScriptStringBuilder.append("    ends = d.index[d & ~common_periods].tolist()\n");
    pythonScriptStringBuilder.append("    if len(starts) > len(ends):\n");
    pythonScriptStringBuilder.append("        ends.append(common_periods.index[-1])\n");
    pythonScriptStringBuilder.append("    filtered_periods = []\n");
    pythonScriptStringBuilder.append("    for start, end in zip(starts, ends):\n");
    pythonScriptStringBuilder.append("        period_data = df.loc[start:end]\n");
    pythonScriptStringBuilder.append("        missing_values = period_data.isnull().sum().sum()\n");
    pythonScriptStringBuilder.append("        if missing_values <= max_missing_threshold:\n");
    pythonScriptStringBuilder.append("            filtered_periods.append((start, end, missing_values))\n");
    pythonScriptStringBuilder.append("    return filtered_periods\n\n");

    pythonScriptStringBuilder.append("# Enhanced plot for common and missing periods\n");
    pythonScriptStringBuilder.append("def find_common_periods_with_threshold_and_plot(df, threshold, plots_path):\n");
    pythonScriptStringBuilder.append("    import matplotlib.pyplot as plt\n");
    pythonScriptStringBuilder.append("    import matplotlib.dates as mdates\n");
    pythonScriptStringBuilder.append("    common_periods_sections = get_common_periods_sections(df, max_missing_threshold=threshold)\n");
    pythonScriptStringBuilder.append("    print('Number of common periods:', len(common_periods_sections))\n\n");
    pythonScriptStringBuilder.append("    fig, axes = plt.subplots(nrows=len(df.columns), ncols=1, figsize=(15, 12), sharex=True)\n");
    pythonScriptStringBuilder.append("    if len(df.columns) == 1:\n");
    pythonScriptStringBuilder.append("        axes = [axes]\n\n");
    pythonScriptStringBuilder.append("    fig.suptitle('Feature Values with Common and Missing Periods', fontsize=14, y=0.97)\n\n");
    pythonScriptStringBuilder.append("    for ax, col in zip(axes, df.columns):\n");
    pythonScriptStringBuilder.append("        if pd.api.types.is_numeric_dtype(df[col]):\n");
    pythonScriptStringBuilder.append("            ax.plot(df.index, df[col], color='blue', linewidth=1.2, label='Data')\n");
    pythonScriptStringBuilder.append("            missing_mask = df[col].isnull()\n");
    pythonScriptStringBuilder.append("            if missing_mask.any():\n");
    pythonScriptStringBuilder.append("                ax.scatter(df.index[missing_mask], [df[col].min() - 1] * missing_mask.sum(), color='black', marker='x', s=20, label='Missing Points')\n");
    pythonScriptStringBuilder.append("            for start, end, _ in common_periods_sections:\n");
    pythonScriptStringBuilder.append("                ax.axvspan(start, end, color='green', alpha=0.2, label='Common Period')\n");
    pythonScriptStringBuilder.append("            missing_periods = df[col].isna()\n");
    pythonScriptStringBuilder.append("            if missing_periods.any():\n");
    pythonScriptStringBuilder.append("                starts = missing_periods.index[missing_periods & ~missing_periods.shift(1, fill_value=False)]\n");
    pythonScriptStringBuilder.append("                ends = missing_periods.index[missing_periods & ~missing_periods.shift(-1, fill_value=False)]\n");
    pythonScriptStringBuilder.append("                for start, end in zip(starts, ends):\n");
    pythonScriptStringBuilder.append("                    ax.axvspan(start, end, color='red', alpha=0.2, label='Missing Data')\n");
    pythonScriptStringBuilder.append("            ax.set_ylabel(col, fontsize=10)\n");
    pythonScriptStringBuilder.append("        else:\n");
    pythonScriptStringBuilder.append("            ax.text(0.5, 0.5, f'Skipping non-numeric column: {col}', horizontalalignment='center', verticalalignment='center', transform=ax.transAxes, fontsize=9)\n");
    pythonScriptStringBuilder.append("            ax.set_ylabel(col, fontsize=10)\n");
    pythonScriptStringBuilder.append("        if pd.api.types.is_datetime64_any_dtype(df.index):\n");
    pythonScriptStringBuilder.append("            ax.xaxis.set_major_formatter(mdates.DateFormatter('%Y-%m-%d'))\n");
    pythonScriptStringBuilder.append("            fig.autofmt_xdate()\n\n");
    pythonScriptStringBuilder.append("    handles, labels = [], []\n");
    pythonScriptStringBuilder.append("    for ax in axes:\n");
    pythonScriptStringBuilder.append("        h, l = ax.get_legend_handles_labels()\n");
    pythonScriptStringBuilder.append("        handles += h\n");
    pythonScriptStringBuilder.append("        labels += l\n");
    pythonScriptStringBuilder.append("    by_label = dict(zip(labels, handles))\n");
    pythonScriptStringBuilder.append("    fig.legend(by_label.values(), by_label.keys(), loc='upper center', ncol=5, bbox_to_anchor=(0.5, 0.02), fontsize=10, frameon=True)\n");
    pythonScriptStringBuilder.append("    plt.tight_layout(rect=[0, 0.05, 1, 0.95])\n");
    pythonScriptStringBuilder.append("    plt.savefig(plots_path + 'common_plot.png')\n");
    pythonScriptStringBuilder.append("    plt.show()\n\n");
    pythonScriptStringBuilder.append("find_common_periods_with_threshold_and_plot(df, combine_threshold,plots_path)\n\n");
    pythonScriptStringBuilder.append("# Fill missing values using interpolation\n");
    pythonScriptStringBuilder.append("df.interpolate(method='linear', inplace=True)\n");
    pythonScriptStringBuilder.append("print('Missing values filled using linear interpolation.')\n\n");
}


    //
    // STEP 12: ADVANCED IMPUTATION (KNN)
    //
    if (action.getDataAnalytics().getAdvanced_imputation() != null &&
        action.getDataAnalytics().getAdvanced_imputation() == AdvancedImputation.ON) 
    {
        pythonScriptStringBuilder.append("# Advanced Imputation using KNN\n");
        pythonScriptStringBuilder.append("imputer = KNNImputer(n_neighbors=5)\n");
        pythonScriptStringBuilder.append("df = pd.DataFrame(imputer.fit_transform(df), columns=df.columns)\n");
        pythonScriptStringBuilder.append("print('Advanced imputation applied')\n\n");
    }


    //
    // STEP 13: LAGGED FEATURES (GENERAL)
    //
    if (action.getDataAnalytics().getLagged_features() != null &&
        action.getDataAnalytics().getLagged_features() == LaggedFeatures.ON) 
    {
        pythonScriptStringBuilder.append("# Creating lagged features for each numeric column\n");
        pythonScriptStringBuilder.append("n_lag = 3\n");
        pythonScriptStringBuilder.append("for col in df.select_dtypes(include=[np.number]).columns:\n");
        pythonScriptStringBuilder.append("    for lag in range(1, n_lag + 1):\n");
        pythonScriptStringBuilder.append("        df[f'{col}_lag_{lag}'] = df[col].shift(lag)\n");
        pythonScriptStringBuilder.append("df.dropna(inplace=True)\n\n");
    }


    //
    // STEP 14: TIME SERIES CLUSTERING
    //
    if (action.getDataAnalytics().getTime_series_clustering() != null &&
        action.getDataAnalytics().getTime_series_clustering() == TimeSeriesClustering.ON) 
    {
        //pythonScriptStringBuilder.append("from sklearn.cluster import KMeans\n");
        pythonScriptStringBuilder.append("# Time Series Clustering with KMeans\n");
        pythonScriptStringBuilder.append("n_clusters = 3\n");
        pythonScriptStringBuilder.append("print('Number of clusters set to:', n_clusters)\n");
        pythonScriptStringBuilder.append("kmeans = KMeans(n_clusters=n_clusters, random_state=42)\n");
        pythonScriptStringBuilder.append("df['cluster'] = kmeans.fit_predict(df.select_dtypes(include=[np.number]))\n");
        pythonScriptStringBuilder.append("print('Time series clustering applied with', n_clusters, 'clusters')\n\n");
        pythonScriptStringBuilder.append("sns.pairplot(df, hue='cluster')\n");
        pythonScriptStringBuilder.append("plt.title('KMeans Clustering Results')\n");
        pythonScriptStringBuilder.append("plt.savefig(plots_path + '/clustering_results.png')\n");
        pythonScriptStringBuilder.append("plt.show()\n\n");
    }


    //
    // STEP 15: CREATE TIME SERIES LAGGED FEATURES
    //
    if (action.getDataAnalytics().getCreate_lagged_features() != null &&
        action.getDataAnalytics().getCreate_lagged_features() == CreateLaggedFeatures.ON) 
    {
        pythonScriptStringBuilder.append("# Creating lagged features for selected time series columns\n");
        pythonScriptStringBuilder.append("n_lag = 3\n");
        pythonScriptStringBuilder.append("time_series_cols = [col for col in df.columns if 'time_series' in col]\n");
        pythonScriptStringBuilder.append("for col in time_series_cols:\n");
        pythonScriptStringBuilder.append("    for lag in range(1, n_lag + 1):\n");
        pythonScriptStringBuilder.append("        df[f'{col}_lag_{lag}'] = df[col].shift(lag)\n");
        pythonScriptStringBuilder.append("df.dropna(inplace=True)\n\n");
    }


    //
    // STEP 16: ROLLING WINDOW FEATURES
    //
    if (action.getDataAnalytics().getRolling_window_features() != null &&
        action.getDataAnalytics().getRolling_window_features() == RollingWindowFeatures.ON) 
    {
        String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();
        pythonScriptStringBuilder.append("# Generate rolling window features\n");
        pythonScriptStringBuilder.append("df['rolling_mean'] = df['" + targetColumn + "'].rolling(window=3).mean()\n");
        pythonScriptStringBuilder.append("df['rolling_std'] = df['" + targetColumn + "'].rolling(window=3).std()\n");
        pythonScriptStringBuilder.append("df.dropna(inplace=True)\n");
        pythonScriptStringBuilder.append("print('Rolling window features created')\n\n");
    }


    //
    // STEP 17: DATA TRANSFORMATIONS
    //
    if (action.getDataAnalytics().getTransformations() != null &&
        !action.getDataAnalytics().getTransformations().isEmpty()) 
    {
        String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();
        pythonScriptStringBuilder.append("# Apply data transformations\n");
        for (DataTransformation transformation : action.getDataAnalytics().getTransformations()) {
            switch (transformation) {
                case LOG_TRANSFORMATION:
                    pythonScriptStringBuilder.append("df['" + targetColumn + "'] = np.log1p(df['" + targetColumn + "'])\n");
                    pythonScriptStringBuilder.append("print('Log transformation applied')\n");
                    break;
                case DIFFERENCING:
                    pythonScriptStringBuilder.append("df['" + targetColumn + "_diff'] = df['" + targetColumn + "'].diff()\n");
                    pythonScriptStringBuilder.append("df.dropna(inplace=True)\n");
                    pythonScriptStringBuilder.append("print('Differencing applied')\n");
                    break;
                case NORMALIZATION:
                    pythonScriptStringBuilder.append("from sklearn.preprocessing import Normalizer\n");
                    pythonScriptStringBuilder.append("normalizer = Normalizer()\n");
                    pythonScriptStringBuilder.append("df = pd.DataFrame(normalizer.fit_transform(df), columns=df.columns)\n");
                    pythonScriptStringBuilder.append("print('Normalization applied')\n");
                    break;
                case STANDARDIZATION:
                    pythonScriptStringBuilder.append("from sklearn.preprocessing import StandardScaler\n");
                    pythonScriptStringBuilder.append("scaler = StandardScaler()\n");
                    pythonScriptStringBuilder.append("df = pd.DataFrame(scaler.fit_transform(df), columns=df.columns)\n");
                    pythonScriptStringBuilder.append("print('Standardization applied')\n");
                    break;
                case SCALING:
                    pythonScriptStringBuilder.append("from sklearn.preprocessing import MinMaxScaler\n");
                    pythonScriptStringBuilder.append("scaler = MinMaxScaler()\n");
                    pythonScriptStringBuilder.append("df = pd.DataFrame(scaler.fit_transform(df), columns=df.columns)\n");
                    pythonScriptStringBuilder.append("print('Scaling applied')\n");
                    break;
                case SMOOTHING:
                    pythonScriptStringBuilder.append("df['target'] = df['target'].rolling(window=3).mean()\n");
                    pythonScriptStringBuilder.append("df.dropna(inplace=True)\n");
                    pythonScriptStringBuilder.append("print('Smoothing applied')\n");
                    break;
                case AGGREGATION:
                    pythonScriptStringBuilder.append("df = df.resample('M').mean()\n");
                    pythonScriptStringBuilder.append("print('Aggregation applied')\n");
                    break;
                case DOWNSAMPLING:
                    pythonScriptStringBuilder.append("df = df.iloc[::2, :]\n");
                    pythonScriptStringBuilder.append("print('Downsampling applied')\n");
                    break;
                case UPSAMPLING:
                    pythonScriptStringBuilder.append("df = df.reindex(pd.date_range(df.index.min(), df.index.max(), freq='D')).interpolate()\n");
                    pythonScriptStringBuilder.append("print('Upsampling applied')\n");
                    break;
            }
        }
        pythonScriptStringBuilder.append("\n");
    }


    //
    // STEP 18: PREPROCESS FEATURE SCALER
    //
    if (action.getDataAnalytics().getPreprocess_feature_scaler() == Preprocess_feature_scaler.NOT_SET) {
        if (action.getDataAnalytics().getAutoML() == AutoML.ON) {
            System.err.println("WARNING: preprocess_feature_scaler not specified. Defaulting to MIN_MAX_SCALER (AutoML=ON).");
            action.getDataAnalytics().setPreprocess_feature_scaler(Preprocess_feature_scaler.MIN_MAX_SCALER);
        } else {
            System.err.println("WARNING: preprocess_feature_scaler not specified. Defaulting to OFF (AutoML=OFF).");
            action.getDataAnalytics().setPreprocess_feature_scaler(Preprocess_feature_scaler.OFF);
        }
    }

    if (action.getDataAnalytics().getPreprocess_feature_scaler() != null &&
        action.getDataAnalytics().getPreprocess_feature_scaler() != Preprocess_feature_scaler.OFF) 
    {
        switch (action.getDataAnalytics().getPreprocess_feature_scaler()) {
            case STANDARD_SCALER:
                pythonScriptStringBuilder.append("from sklearn.preprocessing import StandardScaler\n");
                pythonScriptStringBuilder.append("scaler = StandardScaler()\n");
                pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
                pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
                break;
            case MIN_MAX_SCALER:
                pythonScriptStringBuilder.append("from sklearn.preprocessing import MinMaxScaler\n");
                pythonScriptStringBuilder.append("scaler = MinMaxScaler()\n");
                pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
                pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
                break;
            case ROBUST_SCALER:
                pythonScriptStringBuilder.append("from sklearn.preprocessing import RobustScaler\n");
                pythonScriptStringBuilder.append("scaler = RobustScaler()\n");
                pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
                pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
                break;
            default:
                break;
        }
    }


    //
    // STEP 19: PREPROCESS SAMPLE NORMALIZER
    //
    if (action.getDataAnalytics().getPreprocess_sample_normalizer() != null &&
        action.getDataAnalytics().getPreprocess_sample_normalizer() != Preprocess_sample_normalizer.NOT_SET &&
        action.getDataAnalytics().getPreprocess_sample_normalizer() != Preprocess_sample_normalizer.OFF) 
    {
        switch (action.getDataAnalytics().getPreprocess_sample_normalizer()) {
            case NORMALIZER_L2_NORM:
                pythonScriptStringBuilder.append("from sklearn.preprocessing import Normalizer\n");
                pythonScriptStringBuilder.append("scaler = Normalizer(norm='l2')\n");
                break;
            case NORMALIZER_L1_NORM:
                pythonScriptStringBuilder.append("from sklearn.preprocessing import Normalizer\n");
                pythonScriptStringBuilder.append("scaler = Normalizer(norm='l1')\n");
                break;
            case NORMALIZER_MAX_NORM:
                pythonScriptStringBuilder.append("from sklearn.preprocessing import Normalizer\n");
                pythonScriptStringBuilder.append("scaler = Normalizer(norm='max')\n");
                break;
            default:
                break;
        }
        pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
        pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
    }


    //
    // STEP 20: STATIONARITY CHECK (ADF)
    //
    if (action.getDataAnalytics().getStationary() != null &&
        action.getDataAnalytics().getStationary() == Stationary.ON) 
    {
        String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();
        pythonScriptStringBuilder.append("from statsmodels.tsa.stattools import adfuller\n");
        pythonScriptStringBuilder.append("try:\n");
        pythonScriptStringBuilder.append("    result = adfuller(df['" + targetColumn + "'])\n");
        pythonScriptStringBuilder.append("    if result[1] > 0.05:\n");
        pythonScriptStringBuilder.append("        print('Series is non-stationary')\n");
        pythonScriptStringBuilder.append("    else:\n");
        pythonScriptStringBuilder.append("        print('Series is stationary')\n");
        pythonScriptStringBuilder.append("except ValueError:\n");
        pythonScriptStringBuilder.append("    print('Stationarity check could not be performed on column " + targetColumn + "')\n\n");
    }


    //
    // STEP 21: SEASONALITY DETECTION
    //
    if (action.getDataAnalytics().getSeasonality_detection() != null &&
        action.getDataAnalytics().getSeasonality_detection() == SeasonalityDetection.ON) 
    {
        String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();
        pythonScriptStringBuilder.append("from statsmodels.tsa.seasonal import seasonal_decompose\n");
        pythonScriptStringBuilder.append("try:\n");
        pythonScriptStringBuilder.append("    decomposition = seasonal_decompose(df['" + targetColumn + "'], model='additive', period=12)\n");
        pythonScriptStringBuilder.append("    decomposition.plot()\n");
        pythonScriptStringBuilder.append("    plt.savefig(plots_path + '/seasonality_decomposition.png')\n");
        pythonScriptStringBuilder.append("    plt.close()\n");
        pythonScriptStringBuilder.append("    print('Seasonality detected and plot saved')\n");
        pythonScriptStringBuilder.append("except ValueError as e:\n");
        pythonScriptStringBuilder.append("    print('Seasonality detection failed:', e)\n\n");
    }


    //
    // STEP 22: CONTEXT-SPECIFIC ADJUSTMENTS
    //

  ContextArea contextArea = action.getDataAnalytics().getContextArea();
   if (contextArea != null && (contextArea == ContextArea.RIVER_FLOW ||
                            contextArea == ContextArea.SERVER_MONITORING ||
                            contextArea == ContextArea.IO_TDATA_CENTER ||
                            contextArea == ContextArea.OTHER)) {
	    pythonScriptStringBuilder.append("# Context-Specific Adjustments\n");
	    switch (contextArea) {
	        case SERVER_MONITORING:
	            pythonScriptStringBuilder.append("print('Applying context-specific preprocessing for Server Monitoring data')\n");
	            break;
	        case IO_TDATA_CENTER:
	            pythonScriptStringBuilder.append("print('Applying context-specific preprocessing for IoT Data Center')\n");
	            break;
	        case OTHER:
	            pythonScriptStringBuilder.append("print('No specific context adjustments required')\n");
	            break;
	    }
	    pythonScriptStringBuilder.append("\n");
}


    //
    // STEP 23: SLIDING WINDOW
    //
    if (action.getDataAnalytics().getSliding_window() != null &&
        action.getDataAnalytics().getSliding_window() == SlidingWindow.ON) 
    {
        pythonScriptStringBuilder.append("# Applying Sliding Window\n");
        pythonScriptStringBuilder.append("window_size = 5\n");
        pythonScriptStringBuilder.append("def create_sliding_windows(data, window_size):\n");
        pythonScriptStringBuilder.append("    X, y = [], []\n");
        pythonScriptStringBuilder.append("    for i in range(len(data) - window_size):\n");
        pythonScriptStringBuilder.append("        X.append(data[i:i+window_size])\n");
        pythonScriptStringBuilder.append("        y.append(data[i+window_size])\n");
        pythonScriptStringBuilder.append("    return np.array(X), np.array(y)\n\n");

        String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();
        pythonScriptStringBuilder.append("X, y = create_sliding_windows(df['" + targetColumn + "'].values, window_size)\n");
        pythonScriptStringBuilder.append("print('Sliding windows created with window size:', window_size)\n");
        pythonScriptStringBuilder.append("print('Shape of X:', X.shape)\n");
        pythonScriptStringBuilder.append("print('Shape of y:', y.shape)\n\n");
    } 


    //





// STEP 24: SUPERVISED LEARNING FORMAT
//edited    .......

boolean isSupervised = action.getDataAnalytics().getSupervised_learning() == SupervisedLearning.ON;

//int combineThreshold = action.getDataAnalytics().getCombine_threshold();

if (isSupervised && combineThreshold > 0) {
    long nLag = (action.getDataAnalytics().getLag()   != null)
              ? action.getDataAnalytics().getLag().getIntValue()
              : 3;
    long nSteps = (action.getDataAnalytics().getSteps() != null)
               ? action.getDataAnalytics().getSteps().getIntValue()
               : 1;

    Property outputFeature = action.getDataAnalytics()
                                   .getOutput_features()
                                   .get(0);
    String outputType = outputFeature
                          .getTypeRef()
                          .getType()
                          .getName();

    pythonScriptStringBuilder.append("# Common Period Threshold set: " 
        + combineThreshold + "\n");
    pythonScriptStringBuilder.append("combine_threshold = " 
        + combineThreshold + "\n");
    pythonScriptStringBuilder.append("print('Common Period Threshold set:', combine_threshold)\n\n");

    pythonScriptStringBuilder.append("# Convert series to supervised format with common periods filtering\n");
    pythonScriptStringBuilder.append("def series_to_supervised(data, n_in=1, n_out=1):\n");
    pythonScriptStringBuilder.append("    import pandas as pd\n");
    pythonScriptStringBuilder.append("    n_vars = data.shape[1]\n");
    pythonScriptStringBuilder.append("    df = pd.DataFrame(data)\n");
    pythonScriptStringBuilder.append("    cols, names = list(), list()\n");
    pythonScriptStringBuilder.append("    for i in range(n_in, 0, -1):\n");
    pythonScriptStringBuilder.append("        cols.append(df.shift(i))\n");
    pythonScriptStringBuilder.append("        names += [('var%d(t-%d)' % (j+1, i)) for j in range(n_vars)]\n");
    pythonScriptStringBuilder.append("    for i in range(0, n_out):\n");
    pythonScriptStringBuilder.append("        cols.append(df.iloc[:, -1].shift(-i))\n");
    pythonScriptStringBuilder.append("        names += ['var%d(t+%d)' % (n_vars, i)] if i > 0 else ['var%d(t)' % n_vars]\n");
    pythonScriptStringBuilder.append("    agg = pd.concat(cols, axis=1)\n");
    pythonScriptStringBuilder.append("    agg.columns = names\n");
    pythonScriptStringBuilder.append("    agg.dropna(inplace=True)\n");
    pythonScriptStringBuilder.append("    return agg\n\n");

    pythonScriptStringBuilder.append("all_supervised = []\n");
    pythonScriptStringBuilder.append("for start, end, _ in get_common_periods_sections(df):\n");
    pythonScriptStringBuilder.append("    period_data = df.loc[start:end].copy()\n");
    pythonScriptStringBuilder.append("    supervised_data = series_to_supervised(\n"
        + "        period_data, n_in=" + nLag + ", n_out=" + nSteps + ")\n");
    pythonScriptStringBuilder.append("    if supervised_data.shape[0] > 1:\n");
    pythonScriptStringBuilder.append("        all_supervised.append(supervised_data)\n\n");

    pythonScriptStringBuilder.append("combined_supervised_data = pd.concat(all_supervised, axis=0)\n");
    pythonScriptStringBuilder.append("X = combined_supervised_data.iloc[:, :-" 
        + nSteps + "]\n");
    pythonScriptStringBuilder.append("y = combined_supervised_data.iloc[:, -" 
        + nSteps + ":]\n\n");

    pythonScriptStringBuilder.append("from sklearn.model_selection import train_test_split\n");
    pythonScriptStringBuilder.append("X_train, X_test, y_train, y_test = train_test_split(\n"
        + "    X, y, test_size=0.2, shuffle=False)\n");
    pythonScriptStringBuilder.append("print('Training and testing sets created from common periods for supervised learning')\n\n");
}


DataAnalyticsModelAlgorithm modelAlgo     = action.getDataAnalytics().getModelAlgorithm();
	Property                        outProp      = action.getDataAnalytics().getOutput_features().get(0);
	String                          outFeat      = outProp.getName();
	boolean                         hasLabels    = action.getDataAnalytics().getLabels() == Labels.ON
	                                              || action.getDataAnalytics().getLabels() == Labels.SEMI;

    // STEP 25: LABELS FOR UNSUPERVISED MODE
   
// -----------------------------------------------------------------------------
// STEP 25  – unsupervised training-set logic
//           (only executes if SupervisedLearning == OFF **and**
//            the model is NOT ARIMA / SARIMA / HWES / Prophet)
// -----------------------------------------------------------------------------
	
	boolean isTimeSeriesAlgo = modelAlgo instanceof ARIMA
            || modelAlgo instanceof SARIMA
            || modelAlgo instanceof HWES
            || modelAlgo instanceof Prophet;
	
    StringBuilder python = new StringBuilder();

    SupervisedLearning sl = action.getDataAnalytics().getSupervised_learning();
    if (hasLabels && ((sl == null || sl != SupervisedLearning.ON) && !isTimeSeriesAlgo)) {
        pythonScriptStringBuilder.append("# ===== non-timeseries unsupervised learning logic (labels ON) =====\n");
        pythonScriptStringBuilder.append("if str(timestamps).strip().lower() == 'on':\n");
        pythonScriptStringBuilder.append("    X_train = df.loc[:, col_names[1:-1]]\n");
        pythonScriptStringBuilder.append("else:\n");
        pythonScriptStringBuilder.append("    X_train = df.loc[:, col_names[:-1]]\n");
        pythonScriptStringBuilder.append("y_train = df[features[-1]]\n\n");

        pythonScriptStringBuilder.append("print(f'Shape of X_train: {X_train.shape}')\n");
        pythonScriptStringBuilder.append("print(f'Shape of y_train: {y_train.shape}')\n\n");

        pythonScriptStringBuilder.append("from sklearn.model_selection import train_test_split\n");
        pythonScriptStringBuilder.append("X_train, X_test, y_train, y_test = train_test_split(\n");
        pythonScriptStringBuilder.append("    X_train, y_train, test_size=0.2, random_state=42\n");
        pythonScriptStringBuilder.append(")\n");
        pythonScriptStringBuilder.append("print('X_train, X_test, y_train, y_test created in unsupervised mode')\n");
        pythonScriptStringBuilder.append("print(f'Shape of X_train after split: {X_train.shape}')\n");
        pythonScriptStringBuilder.append("print(f'Shape of X_test: {X_test.shape}')\n");
        pythonScriptStringBuilder.append("print(f'Shape of y_train after split: {y_train.shape}')\n");
        pythonScriptStringBuilder.append("print(f'Shape of y_test: {y_test.shape}')\n\n");
    } else if (!isSupervised && !isTimeSeriesAlgo && !hasLabels) {
        pythonScriptStringBuilder.append("# ===== non-timeseries unsupervised learning logic (labels OFF) =====\n");
        pythonScriptStringBuilder.append("X_train = df\n");
        pythonScriptStringBuilder.append("y_train = []\n\n");

        pythonScriptStringBuilder.append("print(f'Shape of X_train: {X_train.shape}')\n");
        pythonScriptStringBuilder.append("print('y_train is empty')\n\n");

        pythonScriptStringBuilder.append("from sklearn.model_selection import train_test_split\n");
        pythonScriptStringBuilder.append("X_train, X_test = train_test_split(\n");
        pythonScriptStringBuilder.append("    X_train, test_size=0.2, random_state=42\n");
        pythonScriptStringBuilder.append(")\n");
        pythonScriptStringBuilder.append("y_train, y_test = [], []\n");
        pythonScriptStringBuilder.append("print('X_train, X_test created in unsupervised mode (no labels)')\n");
        pythonScriptStringBuilder.append("print(f'Shape of X_train after split: {X_train.shape}')\n");
        pythonScriptStringBuilder.append("print(f'Shape of X_test: {X_test.shape}')\n\n");
    }

//explode any array-typed features and then pickle X_train
//pythonScriptStringBuilder.append("array_features_indexes = [i for i, t in enumerate(feature_types) if '[' in t]\n");
//pythonScriptStringBuilder.append("if array_features_indexes:\n");
//pythonScriptStringBuilder.append("    for index in array_features_indexes:\n");
//pythonScriptStringBuilder.append("        col = features[index]\n");
//pythonScriptStringBuilder.append("        # split “[a b c]” → ['a','b','c'] and convert to columns\n");
//pythonScriptStringBuilder.append("        parts = (\n");
//pythonScriptStringBuilder.append("            X_train[col]\n");
//pythonScriptStringBuilder.append("    parts = (\n");
//pythonScriptStringBuilder.append("        X_train[col]\n");
//pythonScriptStringBuilder.append("        .apply(lambda x: x[1:-1])\n");
//pythonScriptStringBuilder.append("        .str.split(r\"\\s+\", expand=True)\n");
//pythonScriptStringBuilder.append("    )\n");

//pythonScriptStringBuilder.append("        parts.columns = [f\"%s_{i}\" % (col, i) for i in parts.columns]\n");
//pythonScriptStringBuilder.append("        X_train = pd.concat([X_train.drop(columns=[col]), parts.astype(float)], axis=1)\n");
//pythonScriptStringBuilder.append("    # now pickle the exploded X_train\n");
//pythonScriptStringBuilder.append("     with open('" + path_str + "/python-scripts/pickles/preprocess_X_train.pickle', 'wb') as pickle_file:\n");
//pythonScriptStringBuilder.append("        pickle.dump(X_train, pickle_file)\n");


	    // --- END array‑feature expansion ---
	  //  pythonScriptStringBuilder.append("    pickle.dump(y_test, pickle_file)\n\n");
	//}

	// Dynamically build and load algorithm‐specific pickles


	
// STEP 26: VISUALIZATION
// STEP 26: VISUALIZATION
if (action.getDataAnalytics().getPlots() != null && !action.getDataAnalytics().getPlots().isEmpty()) {
    for (PreprocessingPlots plot : action.getDataAnalytics().getPlots()) {
        switch (plot) {

            /* LINE PLOT */
            case LINE_PLOT:
                pythonScriptStringBuilder.append("num_cols = [c for c in df.columns if pd.api.types.is_numeric_dtype(df[c])]\n");
                pythonScriptStringBuilder.append("if not num_cols:\n");
                pythonScriptStringBuilder.append("    print('No numeric columns for line plot.')\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    for col in num_cols:\n");
                pythonScriptStringBuilder.append("        plt.figure(figsize=(10,4))\n");
                pythonScriptStringBuilder.append("        plt.plot(df.index, df[col])\n");
                pythonScriptStringBuilder.append("        plt.title(col)\n");
                pythonScriptStringBuilder.append("        plt.tight_layout()\n");
                pythonScriptStringBuilder.append("        plt.savefig(f'{plots_path}line_{col}.png')\n");
                pythonScriptStringBuilder.append("        plt.close()\n");
                pythonScriptStringBuilder.append("\n");
                break;

            /* HISTOGRAM */
            case HISTOGRAM:
                pythonScriptStringBuilder.append("num_cols = df.select_dtypes(include=[np.number]).columns\n");
                pythonScriptStringBuilder.append("if not len(num_cols):\n");
                pythonScriptStringBuilder.append("    print('No numeric columns for histogram.')\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    for col in num_cols:\n");
                pythonScriptStringBuilder.append("        plt.figure(figsize=(6,4))\n");
                pythonScriptStringBuilder.append("        df[col].hist(bins=30)\n");
                pythonScriptStringBuilder.append("        plt.title(f'Histogram – {col}')\n");
                pythonScriptStringBuilder.append("        plt.tight_layout()\n");
                pythonScriptStringBuilder.append("        plt.savefig(f'{plots_path}hist_{col}.png')\n");
                pythonScriptStringBuilder.append("        plt.close()\n");
                pythonScriptStringBuilder.append("\n");
                break;

            /* BOX PLOT */
            case BOX_PLOT:
                pythonScriptStringBuilder.append("num_cols = df.select_dtypes(include=[np.number]).columns\n");
                pythonScriptStringBuilder.append("if not len(num_cols):\n");
                pythonScriptStringBuilder.append("    print('No numeric columns for box plot.')\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    for col in num_cols:\n");
                pythonScriptStringBuilder.append("        plt.figure(figsize=(4,6))\n");
                pythonScriptStringBuilder.append("        df[[col]].plot(kind='box')\n");
                pythonScriptStringBuilder.append("        plt.title(f'Box – {col}')\n");
                pythonScriptStringBuilder.append("        plt.tight_layout()\n");
                pythonScriptStringBuilder.append("        plt.savefig(f'{plots_path}box_{col}.png')\n");
                pythonScriptStringBuilder.append("        plt.close()\n");
                pythonScriptStringBuilder.append("\n");
                break;

            /* SCATTER / PAIR PLOT */
            case SCATTER_PLOT:
            	
            	
       
                pythonScriptStringBuilder.append("# 📊 Scatter/Pair Plot\n");
                pythonScriptStringBuilder.append("num_df = df.select_dtypes(include=[np.number])\n");
                pythonScriptStringBuilder.append("if num_df.shape[1] < 2:\n");
                pythonScriptStringBuilder.append("    print('⚠️ Need at least 2 numeric columns for scatter/pair plot.')\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    try:\n");
                pythonScriptStringBuilder.append("        sns.pairplot(num_df)\n");
                pythonScriptStringBuilder.append("        plt.savefig(f'{plots_path}pair_plot.png')\n");
                pythonScriptStringBuilder.append("        plt.close()\n");
                pythonScriptStringBuilder.append("        print('✅ Pair plot saved successfully.')\n");
                pythonScriptStringBuilder.append("    except Exception as e:\n");
                pythonScriptStringBuilder.append("        print(f'❌ Error generating pair plot: {e}')\n");
                pythonScriptStringBuilder.append("\n");
                break;

            case PAIR_PLOT:
                pythonScriptStringBuilder.append("num_df = df.select_dtypes(include=[np.number])\n");
                pythonScriptStringBuilder.append("if num_df.shape[1] < 2:\n");
                pythonScriptStringBuilder.append("    print('Need ≥2 numeric columns for pair plot.')\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    sns.pairplot(num_df)\n");
                pythonScriptStringBuilder.append("    plt.savefig(f'{plots_path}pair_plot.png')\n");
                pythonScriptStringBuilder.append("    plt.close()\n");
                pythonScriptStringBuilder.append("\n");
                break;

            /* HEAT MAP / CORRELATION */
            case HEAT_MAP:
            	
                pythonScriptStringBuilder.append("# 🔥 Correlation Heatmap\n");
                pythonScriptStringBuilder.append("num_df = df.select_dtypes(include=[np.number])\n");
                pythonScriptStringBuilder.append("if num_df.shape[1] < 2:\n");
                pythonScriptStringBuilder.append("    print('⚠️ Need at least 2 numeric columns for heatmap.')\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    try:\n");
                pythonScriptStringBuilder.append("        plt.figure(figsize=(10, 8))\n");
                pythonScriptStringBuilder.append("        sns.heatmap(num_df.corr(), annot=True, fmt='.2f', cmap='coolwarm', square=True, cbar=True)\n");
                pythonScriptStringBuilder.append("        plt.title('Correlation Heatmap')\n");
                pythonScriptStringBuilder.append("        plt.tight_layout()\n");
                pythonScriptStringBuilder.append("        plt.savefig(f'{plots_path}heatmap.png')\n");
                pythonScriptStringBuilder.append("        plt.close()\n");
                pythonScriptStringBuilder.append("        print('✅ Heatmap saved successfully.')\n");
                pythonScriptStringBuilder.append("    except Exception as e:\n");
                pythonScriptStringBuilder.append("        print(f'❌ Error generating heatmap: {e}')\n");
                pythonScriptStringBuilder.append("\n");
                break;

            case CORRELATION_MATRIX_PLOT:
                pythonScriptStringBuilder.append("num_df = df.select_dtypes(include=[np.number])\n");
                pythonScriptStringBuilder.append("if num_df.shape[1] < 2:\n");
                pythonScriptStringBuilder.append("    print('Need ≥2 numeric columns for heat map.')\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    plt.figure(figsize=(8,6))\n");
                pythonScriptStringBuilder.append("    sns.heatmap(num_df.corr(), annot=True, cmap='coolwarm', square=True)\n");
                pythonScriptStringBuilder.append("    plt.title('Correlation Heatmap')\n");
                pythonScriptStringBuilder.append("    plt.tight_layout()\n");
                pythonScriptStringBuilder.append("    plt.savefig(f'{plots_path}heatmap.png')\n");
                pythonScriptStringBuilder.append("    plt.close()\n");
                pythonScriptStringBuilder.append("\n");
                break;

            /* AUTOCORRELATION / PACF */
            case AUTOCORRELATION_PLOT:
                String target = action.getDataAnalytics().getOutput_features().get(0).getName();
                pythonScriptStringBuilder.append("if '" + target + "' in df.columns and pd.api.types.is_numeric_dtype(df['" + target + "']):\n");
                pythonScriptStringBuilder.append("    plot_acf(df['" + target + "'])\n");
                pythonScriptStringBuilder.append("    plt.title('ACF – " + target + "')\n");
                pythonScriptStringBuilder.append("    plt.tight_layout()\n");
                pythonScriptStringBuilder.append("    plt.savefig(f'{plots_path}acf_" + target + ".png')\n");
                pythonScriptStringBuilder.append("    plt.close()\n");
                pythonScriptStringBuilder.append("    plot_pacf(df['" + target + "'])\n");
                pythonScriptStringBuilder.append("    plt.title('PACF – " + target + "')\n");
                pythonScriptStringBuilder.append("    plt.tight_layout()\n");
                pythonScriptStringBuilder.append("    plt.savefig(f'{plots_path}pacf_" + target + ".png')\n");
                pythonScriptStringBuilder.append("    plt.close()\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    print('Target column not numeric / missing for ACF-PACF.')\n");
                pythonScriptStringBuilder.append("\n");
                break;

            /* LAG_PLOT */
            case LAG_PLOT:
                target = action.getDataAnalytics().getOutput_features().get(0).getName();
                pythonScriptStringBuilder.append("if '" + target + "' in df.columns and pd.api.types.is_numeric_dtype(df['" + target + "']):\n");
                pythonScriptStringBuilder.append("    pd.plotting.lag_plot(df['" + target + "'])\n");
                pythonScriptStringBuilder.append("    plt.title('Lag – " + target + "')\n");
                pythonScriptStringBuilder.append("    plt.tight_layout()\n");
                pythonScriptStringBuilder.append("    plt.savefig(f'{plots_path}lag_" + target + ".png')\n");
                pythonScriptStringBuilder.append("    plt.close()\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    print('Target column not numeric / missing for lag plot.')\n");
                pythonScriptStringBuilder.append("\n");
                break;

            /* VIOLIN_PLOT */
            case VIOLIN_PLOT:
                pythonScriptStringBuilder.append("num_cols = df.select_dtypes(include=[np.number]).columns\n");
                pythonScriptStringBuilder.append("if not len(num_cols):\n");
                pythonScriptStringBuilder.append("    print('No numeric columns for violin plot.')\n");
                pythonScriptStringBuilder.append("else:\n");
                pythonScriptStringBuilder.append("    for col in num_cols:\n");
                pythonScriptStringBuilder.append("        plt.figure(figsize=(6,4))\n");
                pythonScriptStringBuilder.append("        sns.violinplot(x=df[col], inner='quartile')\n");
                pythonScriptStringBuilder.append("        plt.title(f'Violin – {col}')\n");
                pythonScriptStringBuilder.append("        plt.tight_layout()\n");
                pythonScriptStringBuilder.append("        plt.savefig(f'{plots_path}violin_{col}.png')\n");
                pythonScriptStringBuilder.append("        plt.close()\n");
                pythonScriptStringBuilder.append("\n");
                break;

            /* DEFAULT */
            default:
                pythonScriptStringBuilder.append("print('Plot type " + plot + " is not supported.')\n");
                pythonScriptStringBuilder.append("\n");
                break;
        }
    }
}




if (hasLabels || (!isSupervised && !isTimeSeriesAlgo)) {
    if (modelAlgo instanceof HWES || modelAlgo instanceof ARIMA || modelAlgo instanceof SARIMA) {
    
        // Time series models: Save only the output feature
        pythonScriptStringBuilder.append(
            "with open('" + path_str + "/python-scripts/pickles/df_timeseries.pickle', 'wb') as pf:\n" +
            "    pickle.dump(df['" + outFeat + "'], pf)\n\n"
        );
        
        
        
    } else if (modelAlgo instanceof Prophet) {
        // Prophet model: Needs ds and y
        pythonScriptStringBuilder.append(
            "prophet_df = df[['timestamp', '" + outFeat + "']]\n" +
            "prophet_df = prophet_df.rename(columns={'timestamp': 'ds', '" + outFeat + "': 'y'})\n" +
            "with open('" + path_str + "/python-scripts/pickles/df_prophet.pickle', 'wb') as pf:\n" +
            "    pickle.dump(prophet_df, pf)\n\n"
        );
        
    } else {
        // ML models: Save X_train and X_test always
        pythonScriptStringBuilder.append(
            "with open('" + path_str + "/python-scripts/pickles/preprocess_X_train.pickle', 'wb') as pickle_file:\n" +
            "    pickle.dump(X_train, pickle_file)\n\n"
        );
        pythonScriptStringBuilder.append(
            "with open('" + path_str + "/python-scripts/pickles/preprocess_X_test.pickle', 'wb') as pickle_file:\n" +
            "    pickle.dump(X_test, pickle_file)\n\n"
        );
        // Only save y_train and y_test if present
        if (hasLabels) {
            pythonScriptStringBuilder.append(
                "with open('" + path_str + "/python-scripts/pickles/preprocess_y_train.pickle', 'wb') as pickle_file:\n" +
                "    pickle.dump(y_train, pickle_file)\n\n"
            );
            pythonScriptStringBuilder.append(
                "with open('" + path_str + "/python-scripts/pickles/preprocess_y_test.pickle', 'wb') as pickle_file:\n" +
                "    pickle.dump(y_test, pickle_file)\n\n"
            );
        }
    }
}

  
     // --- BEGIN array‑feature expansion ---
   
// 2) All other (supervised) algorithms: dump train/test sets




    //
    // STEP 28: WRITE SCRIPT TO FILE
    //
    File pythonScriptsDir = new File(path_str + "/python-scripts");
    if (!pythonScriptsDir.exists()) {
        pythonScriptsDir.mkdirs();
    }

    File scriptFile = new File(path_str + "/python-scripts/preprocess.py");
    try {
        Files.deleteIfExists(Paths.get(scriptFile.toURI()));
    } catch (IOException e) {
        e.printStackTrace();
    }

    FileWriter fr = null;
    BufferedWriter br = null;
    try {
        fr = new FileWriter(scriptFile, true);
        br = new BufferedWriter(fr);
        br.append(pythonScriptStringBuilder);
    } catch (IOException e1) {
        e1.printStackTrace();
    } finally {
        try {
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}


	// ML2: da_train

	
	





@Override
public void generate(DATrainAction action, StringBuilder builder, Context ctx) {
    // 0) Helpers
    boolean hasLabels = action.getDataAnalytics().getLabels() == Labels.ON
                     || action.getDataAnalytics().getLabels() == Labels.SEMI;

    // 1) Prepare paths
    Path path = Paths.get(new File(ctx.getOutputDirectory().getAbsolutePath()).toURI());
    String base = path.toString() + "/src/python-scripts/pickles";

    // 2) Generate the Python training script (train.py)
    generatePythonDATrainScript(path.toString() + "/src", action);

    // 3) Determine algorithm & whether it's a time series (TS) model
    DataAnalyticsModelAlgorithm algo = action.getDataAnalytics().getModelAlgorithm();
    boolean isTS = algo instanceof ARIMA
                 || algo instanceof SARIMA
                 || algo instanceof HWES
                 || algo instanceof Prophet;

    // 4) Check pickles based on model type
    if (!isTS) {
        builder.append("System.out.println(\"🔍 Checking preprocessing pickles…\");\n")
               .append("File X_train = new File(\"").append(base)
               .append("/preprocess_X_train.pickle\");\n")
               .append("File X_test  = new File(\"").append(base)
               .append("/preprocess_X_test.pickle\");\n")
               .append("if (!X_train.exists() || !X_test.exists()) {\n")
               .append("  System.out.println(\"⚠️ Missing preprocess_X_{train,test}.pickle\");\n")
               .append("  return;\n")
               .append("}\n");

        if (hasLabels) {
            builder.append("File y_train = new File(\"").append(base)
                   .append("/preprocess_y_train.pickle\");\n")
                   .append("File y_test  = new File(\"").append(base)
                   .append("/preprocess_y_test.pickle\");\n")
                   .append("if (!y_train.exists() || !y_test.exists()) {\n")
                   .append("  System.out.println(\"⚠️ Missing preprocess_y_{train,test}.pickle\");\n")
                   .append("  return;\n")
                   .append("}\n");
        }
    } else {
        builder.append("System.out.println(\"ℹ️ TS model detected; skipping standard ML pickle checks.\");\n");
    }

    // 5) Check time series pickle if applicable
    String tsPickle = null;
    if (isTS) {
        if      (algo instanceof ARIMA)   tsPickle = "df_timeseries.pickle";
        else if (algo instanceof SARIMA)  tsPickle = "df_timeseries.pickle";
        else if (algo instanceof HWES)    tsPickle = "df_timeseries.pickle";
        else /* Prophet */                tsPickle = "df_prophet.pickle";

        builder.append("System.out.println(\"🔍 Checking existence of TS model pickle: ")
               .append(tsPickle).append("…\");\n")
               .append("File model_pickle = new File(\"").append(base)
               .append("/").append(tsPickle).append("\");\n")
               .append("if (!model_pickle.exists()) {\n")
               .append("  System.out.println(\"⚠️ Missing file: ").append(tsPickle).append("\");\n")
               .append("  return;\n")
               .append("}\n")
               .append("System.out.println(\"✅ All required pickle files found. Proceeding with training…\");\n");
    } else {
        builder.append("System.out.println(\"ℹ️ Non‑TS model detected; skipping TS pickle check.\");\n");
    }



    // 6) Launch train.py via ProcessBuilder
    builder.append("System.out.println(\"🚀 Starting train.py via Java ProcessBuilder...\");\n")
           // make executable
           .append("List<String> list0 = new ArrayList<String>();\n")
           .append("list0.add(\"chmod\");\n")
           .append("list0.add(\"u+x\");\n")
           .append("list0.add(\"").append(path.toString()).append("/src/python-scripts/train.py\");\n")
           .append("try{\n")
           .append("    ProcessBuilder pb0 = new ProcessBuilder(list0);\n")
           .append("    Process p0 = pb0.start();\n")
           .append("    int code0 = p0.waitFor();\n")
           .append("    System.out.println(\"✅ chmod exited with code \" + code0);\n")
           .append("} catch(Exception e){ System.out.println(\"❌ chmod failed: \" + e); }\n")

           // launch Python
           .append("System.out.println(\"📤 Launching the process...\");\n")
           .append("List<String> list = new ArrayList<String>();\n")
           .append("list.add(\"/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/scripts/venv/bin/python\");\n")
           .append("list.add(\"").append(path.toString()).append("/src/python-scripts/train.py\");\n")
           .append("try{\n")
           .append("    ProcessBuilder pb = new ProcessBuilder(list)\n")
           .append("        .redirectErrorStream(true);\n")
           .append("    Process p = pb.start();\n")
           .append("    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));\n")
           .append("    String line;\n")
           .append("    while ((line = reader.readLine()) != null) {\n")
           .append("        System.out.println(\"📄 [train.py] \" + line);\n")
           .append("    }\n")
           .append("    int exitCode = p.waitFor();\n")
           .append("    System.out.println(\"✅ train.py exited with code \" + exitCode);\n")
           .append("} catch(Exception e){ System.out.println(\"❌ Python execution failed: \" + e); }\n");
}

	// ML2: Code generator for the data analytics training Python script, train.py
	private void generatePythonDATrainScript(String path_str, DATrainAction action) {
	    DataAnalyticsModelAlgorithm dataAnalyticsModelAlgorithm = action.getDataAnalytics().getModelAlgorithm();
	    String dalib = "auto";
	    if (AnnotatedElementHelper.hasAnnotation(action.getDataAnalytics(), "dalib")) {
	        String dalibAnnotationValue = action.getDataAnalytics().getAnnotations().get(0).getValue();
	        if (dalibAnnotationValue.equals("scikit-learn")|| dalibAnnotationValue.equals("keras-tensorflow")
	        || dalibAnnotationValue.equals("pytorch") ||dalibAnnotationValue.equals("xgboost")||dalibAnnotationValue.equals("prophet")
	        		|| dalibAnnotationValue.equals("statsmodels") || dalibAnnotationValue.equals("pytorch")) {
	            dalib = dalibAnnotationValue;
	        } else if (dalibAnnotationValue.equals("weka")) {
	            System.err.println("ERROR: This compiler/code generator generates Java AND Python code. The data analytics / machine learning part should be generated in Python. However, weka is chosen as the library for data analytics / machine learning in the annotations of the model. Please either change the annotation @dalib to a Python library, e.g., scikit-learn or use the pure Java compiler/code generator!");
	            return;
	        } else {
	            dalib = "auto";
	        }
	    }

	    StringBuilder pythonScriptStringBuilder = new StringBuilder();
	    pythonScriptStringBuilder.append("import pickle\n\n");

	
 
 
 
 
 
DataAnalyticsModelAlgorithm modelAlgo     = action.getDataAnalytics().getModelAlgorithm();
Property                        outProp      = action.getDataAnalytics().getOutput_features().get(0);
String                          outFeat      = outProp.getName();
boolean                         hasLabels    = action.getDataAnalytics().getLabels() == Labels.ON
                                              || action.getDataAnalytics().getLabels() == Labels.SEMI;



// 1) Time‑series only cases:
// … after defining outFeat, hasLabels, modelAlgo, etc. …

if (hasLabels && modelAlgo instanceof ARIMA) {
    pythonScriptStringBuilder.append(
        "with open('" + path_str + "/python-scripts/pickles/df_timeseries.pickle', 'rb') as pf:\n" +
        "    " + outFeat + "_series = pickle.load(pf)\n"
    );
}
else if (hasLabels && modelAlgo instanceof SARIMA) {
    pythonScriptStringBuilder.append(
        "with open('" + path_str + "/python-scripts/pickles/df_timeseries.pickle', 'rb') as pf:\n" +
        "    " + outFeat + "_series = pickle.load(pf)\n"
    );
}
else if (hasLabels && modelAlgo instanceof HWES) {
    pythonScriptStringBuilder.append(
        "with open('" + path_str + "/python-scripts/pickles/df_timeseries.pickle', 'rb') as pf:\n" +
        "    " + outFeat + "_series = pickle.load(pf)\n"
    );
}
else if (hasLabels && modelAlgo instanceof Prophet) {
    pythonScriptStringBuilder.append(
        "prophet_df = df[['timestamp','" + outFeat + "']].rename(columns={'timestamp':'ds','" + outFeat + "':'y'})\n" +
        "with open('" + path_str + "/python-scripts/pickles/df_prophet.pickle', 'rb') as pf:\n" +
        "   " + outFeat + "_series = pickle.load(pf)\n"
    );
}
else {
    // fallback: train/test pickles
    pythonScriptStringBuilder.append(
        "with open('" + path_str + "/python-scripts/pickles/preprocess_X_train.pickle', 'rb') as pf:\n" +
        "    X_train = pickle.load(pf)\n" +
        "with open('" + path_str + "/python-scripts/pickles/preprocess_X_test.pickle',  'rb') as pf:\n" +
        "    X_test = pickle.load(pf)\n"
    );
    if (hasLabels) {
        pythonScriptStringBuilder.append(
            "with open('" + path_str + "/python-scripts/pickles/preprocess_y_train.pickle', 'rb') as pf:\n" +
            "    y_train = pickle.load(pf)\n" +
            "with open('" + path_str + "/python-scripts/pickles/preprocess_y_test.pickle',  'rb') as pf:\n" +
            "    y_test = pickle.load(pf)\n"
        );
    }
}


	    // Supervised ML
	    if (dataAnalyticsModelAlgorithm instanceof GRU) { // GRU
	        generatePythonDATrainScriptGRU(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof LinearClassifierLogisticRegression || dataAnalyticsModelAlgorithm instanceof LinearRegression) {
	        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	            if (dataAnalyticsModelAlgorithm instanceof LinearClassifierLogisticRegression) {
	                System.err.println("ERROR: The prediction data type is numeric. Therefore, please use the linear regression model for regression, instead of the logistic regression classifier.\n");
	            }
	            generatePythonDATrainScriptLinearRegression(dalib, path_str, action, pythonScriptStringBuilder, false);
	        } else {
	            if (dataAnalyticsModelAlgorithm instanceof LinearRegression) {
	                System.err.println("ERROR: The prediction data type is categorical. Therefore, please use the logistic regression classifier, instead of the linear regression model.\n");
	            }
	            generatePythonDATrainScriptLinearClassifierLogisticRegression(dalib, path_str, action, pythonScriptStringBuilder, false);
	        }
	    } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesGaussian) {
	        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
	        } else {
	            generatePythonDATrainScriptNaiveBayesGaussian(dalib, path_str, action, pythonScriptStringBuilder, false);
	        }
	        
	    } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesMultinomial) {
	        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
	        } else {
	            generatePythonDATrainScriptNaiveBayesMultinomial(dalib, path_str, action, pythonScriptStringBuilder, false);
	        }}
	        
	        else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesComplement) {
		        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
		            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
		        } else {
		        	generatePythonDATrainScriptNaiveBayesComplement(dalib, path_str, action, pythonScriptStringBuilder, false);
		        }}
		        
		        else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesBernoulli) {
			        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
			            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
			        } else {
			        	generatePythonDATrainScriptNaiveBayesBernoulli(dalib, path_str, action, pythonScriptStringBuilder, false);
			        } }
			        else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesCategorical) {
				        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
				            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
				        } else {
				        	generatePythonDATrainScriptNaiveBayesCategorical(dalib, path_str, action, pythonScriptStringBuilder, false);
				        }
			        
			        
			       
	        
	    } else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeRegressor) {
	        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	            generatePythonDATrainScriptDecisionTreeRegressor(dalib, path_str, action, pythonScriptStringBuilder, false);
	        } else {
	            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a classification ML algorithm, but a regression ML algorithm. However, it is used with a categorical type for the prediction results.\n");
	        }
	    } else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeClassifier) {
	        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
	        } else {
	            generatePythonDATrainScriptDecisionTreeClassifier(dalib, path_str, action, pythonScriptStringBuilder, false);
	        }
	    } else if (dataAnalyticsModelAlgorithm instanceof RandomForestRegressor) {
	        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	            generatePythonDATrainScriptRandomForestRegressor(dalib, path_str, action, pythonScriptStringBuilder, false);
	        } else {
	            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a classification ML algorithm, but a regression ML algorithm. However, it is used with a categorical type for the prediction results.\n");
	        }
	    } else if (dataAnalyticsModelAlgorithm instanceof RandomForestClassifier) {
	        if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	            System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName() + " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
	        } else {
	            generatePythonDATrainScriptRandomForestClassifier(dalib, path_str, action, pythonScriptStringBuilder, false);
	        }
	    } else if (dataAnalyticsModelAlgorithm instanceof NN_MultilayerPerceptron) {
	    	

	        generatePythonDATrainScriptNN_MultilayerPerceptron(dalib, path_str, action, pythonScriptStringBuilder, false);
	        

	    } else if (dataAnalyticsModelAlgorithm instanceof MLP) {

	    	 generatePythonDATrainScriptMLP(dalib, path_str, action, pythonScriptStringBuilder, false);   }
	    
	    else if (dataAnalyticsModelAlgorithm instanceof CNN) {
	        generatePythonDATrainScriptCNN(dalib, path_str, action, pythonScriptStringBuilder, false);
	        
	    } else if (dataAnalyticsModelAlgorithm instanceof LSTM) {

	        generatePythonDATrainScriptLSTM(dalib, path_str, action, pythonScriptStringBuilder, false);

	    } else if (dataAnalyticsModelAlgorithm instanceof RNN) {
	       generatePythonDATrainScriptRNN(dalib, path_str, action, pythonScriptStringBuilder, false);
	       
	    }
	    
	    else if (dataAnalyticsModelAlgorithm instanceof TCN) {
	        generatePythonDATrainScriptTCN(dalib, path_str, action, pythonScriptStringBuilder, false);
	    }
	    
	     else if (dataAnalyticsModelAlgorithm instanceof Transformer) {
	        generatePythonDATrainScriptTransformer(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof ARIMA) {
	        generatePythonDATrainScriptARIMA(dalib, path_str, action, pythonScriptStringBuilder, false);
	        
	    } else if (dataAnalyticsModelAlgorithm instanceof SARIMA) {
	        generatePythonDATrainScriptSARIMA(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof HWES) {
	        generatePythonDATrainScriptHWES(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof ETS) {
	        generatePythonDATrainScriptETS(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof StateSpaceModel) {
	        generatePythonDATrainScriptStateSpaceModel(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof SVR) {
	        generatePythonDATrainScriptSVR(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof RFR) {
	    
	        generatePythonDATrainScriptRFR(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof XGBoost) {
	        generatePythonDATrainScriptXGBoost(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } 
	    
	    
	
	    
	    
	    
	    

	    
	    else if (dataAnalyticsModelAlgorithm instanceof ARIMA_GARCH) {
	        generatePythonDATrainScriptARIMA_GARCH(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (dataAnalyticsModelAlgorithm instanceof Prophet) {
	        generatePythonDATrainScriptProphet(dalib, path_str, action, pythonScriptStringBuilder, false);
	    } else if (action.getDataAnalytics().getLabels() == Labels.OFF) {
	        // Unsupervised ML
	        if (dataAnalyticsModelAlgorithm instanceof KMeans) {
	            generatePythonDATrainScriptKMeans(dalib, path_str, action, pythonScriptStringBuilder);
	        } else if (dataAnalyticsModelAlgorithm instanceof MiniBatchKMeans) {
	            generatePythonDATrainScriptMiniBatchKMeans(dalib, path_str, action, pythonScriptStringBuilder);
	        } else if (dataAnalyticsModelAlgorithm instanceof DBSCAN) {
	            generatePythonDATrainScriptDBSCAN(dalib, path_str, action, pythonScriptStringBuilder);
	        } else if (dataAnalyticsModelAlgorithm instanceof SpectralClustering) {
	            generatePythonDATrainScriptSpectralClustering(dalib, path_str, action, pythonScriptStringBuilder);
	        } else if (dataAnalyticsModelAlgorithm instanceof GaussianMixture) {
	            generatePythonDATrainScriptGaussianMixture(dalib, path_str, action, pythonScriptStringBuilder);
	        }
	    } else if (action.getDataAnalytics().getLabels() == Labels.SEMI) {
	        // Semi-supervised ML
	        if (dataAnalyticsModelAlgorithm instanceof SelfTrainingClassifier) {
	            generatePythonDATrainScriptSelfTrainingClassifier(dalib, path_str, action, pythonScriptStringBuilder);
	        } else if (dataAnalyticsModelAlgorithm instanceof LabelPropagation) {
	            generatePythonDATrainScriptLabelPropagation(dalib, path_str, action, pythonScriptStringBuilder);
	        } else if (dataAnalyticsModelAlgorithm instanceof LabelSpreading) {
	            generatePythonDATrainScriptLabelSpreading(dalib, path_str, action, pythonScriptStringBuilder);
	        }
	    } else {
	        System.err.println("ERROR: In the data analytics section of the model, labels must be specified: ON (for supervised ML, i.e., labeled data), OFF (for unsupervised ML, i.e., unlabeled data) or SEMI (for semi-supervised ML, i.e., partially labeled data).\n");
	    }
	    
	    
	    
	      List<ForecastingPlots> forecastingPlotsList = new ArrayList<>();
	    List<OverfittingPlots> overfittingPlotsList = new ArrayList<>();
	    List<PredictionPlots> predictionPlotsList = new ArrayList<>();
	    // Identify model type and populate plot lists based on user selections
	    if (dataAnalyticsModelAlgorithm instanceof Time_series_ModelAlgorithm) {
	        if (dataAnalyticsModelAlgorithm instanceof MLP) {
	            MLP mlpModel = (MLP) dataAnalyticsModelAlgorithm;
	            if (mlpModel.getForecasting_plots() != null) forecastingPlotsList.add(mlpModel.getForecasting_plots());
	            if (mlpModel.getOverfitting_Plots() != null) overfittingPlotsList.add(mlpModel.getOverfitting_Plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof GRU) {
	            GRU gruModel = (GRU) dataAnalyticsModelAlgorithm;
	            if (gruModel.getForecasting_plots() != null) forecastingPlotsList.add(gruModel.getForecasting_plots());
	            if (gruModel.getOverfitting_Plots() != null) overfittingPlotsList.add(gruModel.getOverfitting_Plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof CNN) {
	            CNN cnnModel = (CNN) dataAnalyticsModelAlgorithm;
	            if (cnnModel.getForecasting_plots() != null) forecastingPlotsList.add(cnnModel.getForecasting_plots());
	            if (cnnModel.getOverfitting_Plots() != null) overfittingPlotsList.add(cnnModel.getOverfitting_Plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof LSTM) {
	            LSTM lstmModel = (LSTM) dataAnalyticsModelAlgorithm;
	            if (lstmModel.getForecasting_plots() != null) forecastingPlotsList.add(lstmModel.getForecasting_plots());
	            if (lstmModel.getOverfitting_Plots() != null) overfittingPlotsList.add(lstmModel.getOverfitting_Plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof RNN) {
	            RNN rnnModel = (RNN) dataAnalyticsModelAlgorithm;
	            if (rnnModel.getForecasting_plots() != null) forecastingPlotsList.add(rnnModel.getForecasting_plots());
	            if (rnnModel.getOverfitting_Plots() != null) overfittingPlotsList.add(rnnModel.getOverfitting_Plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof TCN) {
	            TCN tcnModel = (TCN) dataAnalyticsModelAlgorithm;
	            if (tcnModel.getForecasting_plots() != null) forecastingPlotsList.add(tcnModel.getForecasting_plots());
	            if (tcnModel.getOverfitting_Plots() != null) overfittingPlotsList.add(tcnModel.getOverfitting_Plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof Transformer) {
	            Transformer transformerModel = (Transformer) dataAnalyticsModelAlgorithm;
	            if (transformerModel.getForecasting_plots() != null) forecastingPlotsList.add(transformerModel.getForecasting_plots());
	            if (transformerModel.getOverfitting_Plots() != null) overfittingPlotsList.add(transformerModel.getOverfitting_Plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof ARIMA) {
	            ARIMA arimaModel = (ARIMA) dataAnalyticsModelAlgorithm;
	            if (arimaModel.getForecasting_plots() != null) forecastingPlotsList.add(arimaModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof SARIMA) {
	            SARIMA sarimaModel = (SARIMA) dataAnalyticsModelAlgorithm;
	            if (sarimaModel.getForecasting_plots() != null) forecastingPlotsList.add(sarimaModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof HWES) {
	            HWES hwesModel = (HWES) dataAnalyticsModelAlgorithm;
	            if (hwesModel.getForecasting_plots() != null) forecastingPlotsList.add(hwesModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof ETS) {
	            ETS etsModel = (ETS) dataAnalyticsModelAlgorithm;
	            if (etsModel.getForecasting_plots() != null) forecastingPlotsList.add(etsModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof StateSpaceModel) {
	            StateSpaceModel stateSpaceModel = (StateSpaceModel) dataAnalyticsModelAlgorithm;
	            if (stateSpaceModel.getForecasting_plots() != null) forecastingPlotsList.add(stateSpaceModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof SVR) {
	            SVR svrModel = (SVR) dataAnalyticsModelAlgorithm;
	            if (svrModel.getForecasting_plots() != null) forecastingPlotsList.add(svrModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof RFR) {
	            RFR rfrModel = (RFR) dataAnalyticsModelAlgorithm;
	            if (rfrModel.getForecasting_plots() != null) forecastingPlotsList.add(rfrModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof GBM) {
	            GBM gbmModel = (GBM) dataAnalyticsModelAlgorithm;
	            if (gbmModel.getForecasting_plots() != null) forecastingPlotsList.add(gbmModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof XGBoost) {
	            XGBoost xgBoostModel = (XGBoost) dataAnalyticsModelAlgorithm;
	            if (xgBoostModel.getForecasting_plots() != null) forecastingPlotsList.add(xgBoostModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof ARIMA_GARCH) {
	            ARIMA_GARCH arimaGarchModel = (ARIMA_GARCH) dataAnalyticsModelAlgorithm;
	            if (arimaGarchModel.getForecasting_plots() != null) forecastingPlotsList.add(arimaGarchModel.getForecasting_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof Prophet) {
	            Prophet prophetModel = (Prophet) dataAnalyticsModelAlgorithm;
	            if (prophetModel.getForecasting_plots() != null) forecastingPlotsList.add(prophetModel.getForecasting_plots());
	        }
	    }


	    // Only call generatePlots if there are plots specified in the respective lists
	    int predictionCount = action.getDataAnalytics().getPredictionResults().size();


// Generate plots only if needed
boolean hasForecasting   = forecastingPlotsList   != null && !forecastingPlotsList.isEmpty();
boolean hasOverfitting   = overfittingPlotsList   != null && !overfittingPlotsList.isEmpty();

if (hasForecasting || hasOverfitting) {
    generatePlots(
        pythonScriptStringBuilder,
        path_str,
        hasForecasting,          // forecastingPlots flag
        hasOverfitting,          // overfittingPlots flag
        forecastingPlotsList,
        overfittingPlotsList,
        predictionCount,
        modelAlgo,
        outFeat
    );
}


	    if (dataAnalyticsModelAlgorithm instanceof ML2_ModelAlgorithm) {
	        if (dataAnalyticsModelAlgorithm instanceof LinearClassifierLogisticRegression) {
	            LinearClassifierLogisticRegression model = (LinearClassifierLogisticRegression) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof LinearRegression) {
	            LinearRegression model = (LinearRegression) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesGaussian) {
	            NaiveBayesGaussian model = (NaiveBayesGaussian) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesMultinomial) {
	            NaiveBayesMultinomial model = (NaiveBayesMultinomial) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesComplement) {
	            NaiveBayesComplement model = (NaiveBayesComplement) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesBernoulli) {
	            NaiveBayesBernoulli model = (NaiveBayesBernoulli) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesCategorical) {
	            NaiveBayesCategorical model = (NaiveBayesCategorical) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeRegressor) {
	            DecisionTreeRegressor model = (DecisionTreeRegressor) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeClassifier) {
	            DecisionTreeClassifier model = (DecisionTreeClassifier) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof RandomForestRegressor) {
	            RandomForestRegressor model = (RandomForestRegressor) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof RandomForestClassifier) {
	            RandomForestClassifier model = (RandomForestClassifier) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof NN_MultilayerPerceptron) {
	            NN_MultilayerPerceptron model = (NN_MultilayerPerceptron) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof KMeans) {
	            KMeans model = (KMeans) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof MiniBatchKMeans) {
	            MiniBatchKMeans model = (MiniBatchKMeans) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof DBSCAN) {
	            DBSCAN model = (DBSCAN) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof SpectralClustering) {
	            SpectralClustering model = (SpectralClustering) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof GaussianMixture) {
	            GaussianMixture model = (GaussianMixture) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof SelfTrainingClassifier) {
	            SelfTrainingClassifier model = (SelfTrainingClassifier) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof LabelPropagation) {
	            LabelPropagation model = (LabelPropagation) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        } else if (dataAnalyticsModelAlgorithm instanceof LabelSpreading) {
	            LabelSpreading model = (LabelSpreading) dataAnalyticsModelAlgorithm;
	            if (model.getPrediction_plots() != null) predictionPlotsList.add(model.getPrediction_plots());
	        }
	    }

	    
	    
	    
	    // Generate prediction plots if they are specified
	    if (!predictionPlotsList.isEmpty()) {
    generatePredictionPlots(pythonScriptStringBuilder, path_str, predictionPlotsList);
	    }

	    
	    
	    
	    

	    // Save the generated script to a file
	    File pythonScriptsDir = new File(path_str + "/python-scripts");
	    if (!pythonScriptsDir.exists()) {
	        pythonScriptsDir.mkdirs();
	    }
	    File scriptFile = new File(path_str + "/python-scripts/train.py");
	    try {
	        Files.deleteIfExists(scriptFile.toPath());
	        try (FileWriter fr = new FileWriter(scriptFile, true); BufferedWriter br = new BufferedWriter(fr)) {
	            br.append(pythonScriptStringBuilder);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}



	    
	    
	    
	    
	    
	    
	    
	    

	



   
	  
	    // Identify model type and populate plot lists based on user selections



	private void generatePythonDATrainScriptLinearRegression(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptLinearRegression(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ "linear regression.\n" + "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptLinearRegression(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String fit_intercept_str = "";
		String normalize_str = "";
		String copy_X_str = "";
		String n_jobs_str = "";
		String positive_str = "";
		String params = "";
		boolean flag = false;

		if (((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getFit_intercept() != null) {
			fit_intercept_str = first_to_upper(
					String.valueOf(((LinearRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getFit_intercept().isBoolValue()));
			params += ("fit_intercept=" + fit_intercept_str);
			flag = true;
		}
		if (((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getNormalize() != null) {
			normalize_str = first_to_upper(String.valueOf(
					((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getNormalize().isBoolValue()));
			if (action.getDataAnalytics().getPreprocess_feature_scaler() != Preprocess_feature_scaler.OFF
					&& ((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getNormalize()
							.isBoolValue()) {
				System.err.println(
						"WARNING: In the linear regression model, parameter normalize is set to true. This will normalize by subtracting the mean and dividing by the l2-norm (i.e., equivalent to \"MEAN_NORMALIZATION_L2_NORM\").\n However, the data analytics section of the model also includes the preprocess_feature_scaling parameter. You need to make sure that they are consistent and in accordance with what is desired!\n");
			}
			if ((!(((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getFit_intercept()
					.isBoolValue()))
					&& (((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getNormalize()
							.isBoolValue())) {
				System.err.println(
						"WARNING: In the linear regression model, parameter normalize will be ignored, since parameter fit_intercept is set to false.\n Please refer to the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LinearRegression.html\n");
			}
			if (flag)
				params += ",";
			params += ("normalize=" + normalize_str);
			flag = true;
		}
		if (((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getCopy_X() != null) {
			copy_X_str = first_to_upper(String.valueOf(
					((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getCopy_X().isBoolValue()));
			if (flag)
				params += ",";
			params += ("copy_X=" + copy_X_str);
			flag = true;
		}
		if (((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getN_jobs() != null) {
			n_jobs_str = String.valueOf(
					((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getN_jobs().getIntValue());
			if (flag)
				params += ",";
			params += ("n_jobs=" + n_jobs_str);
			flag = true;
		}
		if (((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getPositive() != null) {
			positive_str = first_to_upper(String.valueOf(
					((LinearRegression) (action.getDataAnalytics().getModelAlgorithm())).getPositive().isBoolValue()));
			if (flag)
				params += ",";
			params += ("positive=" + positive_str);
			flag = true;
		}
		pythonScriptStringBuilder.append("from sklearn.linear_model import LinearRegression\n");
		pythonScriptStringBuilder.append("model = LinearRegression(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_lin_reg.pickle', 'wb') as pickle_file:\n");
			
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");
			
		}
	}

	private void generatePythonDATrainScriptLinearClassifierLogisticRegression(String dalib, String path_str,
			DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptLinearClassifierLogisticRegression(path_str, action,
					pythonScriptStringBuilder, isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR:" + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ "logistic regression.\n" + "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR:" + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptLinearClassifierLogisticRegression(String path_str,
			DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String penalty_str = "";
		String dual_str = "";
		String tol_str = "";
		String c_str = "";
		String fit_intercept_str = "";
		String intercept_scaling_str = "";
		String class_weight_str = "";
		String random_state_str = "";
		String solver_str = "";
		String max_iter_str = "";
		String multi_class_str = "";
		String verbose_str = "";
		String warm_start_str = "";
		String n_jobs_str = "";
		String l1_ratio_str = "";
		String params = "";
		String params_without_dual = "";
		String params_without_solver = "";
		String params_without_dual_solver = "";
		boolean flag = false;

		// Checking the solver & penalty compatibility...
		// See the API doc:
		// https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html
		boolean penalty_solver_incompatible = false;
		if (((((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
				.getValue() == Optimizer.NEWTON_CG_VALUE
				|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
						.getValue() == Optimizer.SAG_VALUE
				|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
						.getValue() == Optimizer.LBFGS_VALUE)
				&& (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getPenalty()
						.getValue() == Penalty.L1_VALUE
						|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
								.getPenalty().getValue() == Penalty.ELASTICNET_VALUE))
				|| (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getPenalty()
						.getValue() == Penalty.ELASTICNET_VALUE
						&& (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
								.getOptimizer().getValue() == Optimizer.NEWTON_CG_VALUE
								|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics()
										.getModelAlgorithm())).getOptimizer().getValue() == Optimizer.LBFGS_VALUE
								|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics()
										.getModelAlgorithm())).getOptimizer().getValue() == Optimizer.LIBLINEAR_VALUE
								|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics()
										.getModelAlgorithm())).getOptimizer().getValue() == Optimizer.SAG_VALUE))
				|| (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getPenalty()
						.getValue() == Penalty.NONE_VALUE
						&& ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
								.getOptimizer().getValue() == Optimizer.LIBLINEAR_VALUE)) {
			penalty_solver_incompatible = true;
		}

		// Checking the dual compatibility...
		// See the API doc:
		// https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html
		boolean ignore_dual = true; // By default true
		pythonScriptStringBuilder.append("dual_is_true = False\n");
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getPenalty()
				.getValue() == Penalty.L2_VALUE
				&& ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
						.getValue() == Optimizer.LIBLINEAR_VALUE) {
			ignore_dual = false;
		} else if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getDual() != null) {
			System.err.println(
					"WARNING: In the linear classification (logictic regression) model, dual is only implemented for l2 penalty with liblinear solver. Hence, it is ignored here.\n Please refer to the API documentation of Scikit-Learn for more information: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getDual() != null
				&& ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getDual()
						.isBoolValue()) {
			pythonScriptStringBuilder.append("dual_is_true = True\n");
			System.err.println(
					"WARNING: In the linear classification (logictic regression) model, the boolean parameter dual is set to true. However, if n_samples > n_features, this will be ignored, and automatically set to false.\n Please refer to the API documentation of Scikit-Learn for more information: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
		}

		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getPenalty()
				.getValue() != Penalty.NOT_SET_VALUE) {
			if (penalty_solver_incompatible) {
				System.err.println(
						"WARNING: In the linear classification (logictic regression) model, parameter penalty is ignored, since the chosen penalty and solver are incompatible.\n Please refer to the API documentation of Scikit-Learn for more information: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
			} else {
				penalty_str = ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
						.getPenalty().getLiteral();
				params += ("penalty=" + penalty_str);
				params_without_dual += ("penalty=" + penalty_str);
				flag = true;
			}
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getDual() != null
				&& !ignore_dual) {
			dual_str = first_to_upper(String
					.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getDual().isBoolValue()));
			if (flag) {
				params += ",";
			}
			params += ("dual=" + dual_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getTol() != null) {
			tol_str = String
					.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getTol().getDoubleValue());
			if (flag) {
				params += ",";
				params_without_dual += ",";
			}
			params += ("tol=" + tol_str);
			params_without_dual += ("tol=" + tol_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getC() != null) {
			if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getC()
					.getDoubleValue() <= 0) {
				System.err.println(
						"WARNING: In the linear classification (logictic regression) model, parameter C must be a positive float. However, this is currently not the case. Thus, it will be ignored.\n Please see the API documentation for more indormation on this: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
			} else {
				c_str = String
						.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
								.getC().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_dual += ",";
				}
				params += ("C=" + c_str);
				params_without_dual += ("C=" + c_str);
				flag = true;
			}
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getFit_intercept() != null) {
			fit_intercept_str = first_to_upper(String
					.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getFit_intercept().isBoolValue()));
			if (flag) {
				params += ",";
				params_without_dual += ",";
			}
			params += ("fit_intercept=" + fit_intercept_str);
			params_without_dual += ("fit_intercept=" + fit_intercept_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getIntercept_scaling() != null) {
			if (!(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
					.getFit_intercept().isBoolValue()
					&& ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getOptimizer().getValue() == Optimizer.LIBLINEAR_VALUE)) {
				System.err.println(
						"WARNING: In the linear classification (logictic regression) model, parameter intercept_scaling is useful only when the liblinear solver is used and fit_intercept is set to True. However, this is currently not the case. Thus, it will be ignored.\n Please see the API documentation for more indormation on this: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
			} else {
				intercept_scaling_str = String
						.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
								.getIntercept_scaling().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_dual += ",";
				}
				params += ("intercept_scaling=" + intercept_scaling_str);
				params_without_dual += ("intercept_scaling=" + intercept_scaling_str);
				flag = true;
			}
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getClass_weight() != null) {
			class_weight_str = ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
					.getClass_weight().toString();
			if (flag) {
				params += ",";
				params_without_dual += ",";
			}
			params += ("class_weight=" + class_weight_str);
			params_without_dual += ("class_weight=" + class_weight_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getRandom_state() != null) {
			if (!(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() == Optimizer.LIBLINEAR_VALUE
					|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getOptimizer().getValue() == Optimizer.SAG_VALUE
					|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getOptimizer().getValue() == Optimizer.SAGA_VALUE)) {
				System.err.println(
						"WARNING: In the linear classification (logictic regression) model, parameter random_state may only be used when one of the following solvers is employed: sag, saga or liblinear. However, this is currently not the case. Thus, it will be ignored.\n Please see the API documentation for more indormation on this: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
			} else {
				random_state_str = String
						.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
								.getRandom_state().getIntValue());
				if (flag) {
					params += ",";
					params_without_dual += ",";
				}
				params += ("random_state=" + random_state_str);
				params_without_dual += ("random_state=" + random_state_str);
				flag = true;
			}
		}

		params_without_solver = params;
		params_without_dual_solver = params_without_dual;

		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
				.getValue() != Optimizer.NOT_SET_VALUE) {
			if (penalty_solver_incompatible
					|| !(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getOptimizer().getValue() == Optimizer.NEWTON_CG_VALUE
							|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
									.getOptimizer().getValue() == Optimizer.LBFGS_VALUE
							|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
									.getOptimizer().getValue() == Optimizer.LIBLINEAR_VALUE
							|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
									.getOptimizer().getValue() == Optimizer.SAG_VALUE
							|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
									.getOptimizer().getValue() == Optimizer.SAGA_VALUE)) {
				if (penalty_solver_incompatible) {
					System.err.println(
							"WARNING: In the linear classification (logictic regression) model, parameter solver is ignored, since the chosen penalty and solver are incompatible.\n Please refer to the API documentation of Scikit-Learn for more information: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
				} else {
					System.err.println(
							"WARNING: In the linear classification (logictic regression) model, parameter solver is ignored, since the given solver (optimizer) is not among the supported ones: newton-cg, lbfgs, liblinear, sag and saga.\n Please refer to the API documentation of Scikit-Learn for more information: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
				}
			} else {

				if ((((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
						.getOptimizer().getValue() == Optimizer.SAG_VALUE
						|| ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
								.getOptimizer().getValue() == Optimizer.SAGA_VALUE)
						&& (action.getDataAnalytics().getPreprocess_feature_scaler() == Preprocess_feature_scaler.OFF)) {
					System.err.println(
							"WARNING: In the linear classification (logictic regression) model, the SAG and SAGA solvers fast convergence is only guaranteed on features with approximately the same scale. However, normalization/standardization is turned off (check parameter preprocess_feature_scaling).\n Please refer to the API documentation of Scikit-Learn for more information: https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html\n");
				}

				solver_str = "\'"
						+ ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
								.getOptimizer().getLiteral()
						+ "\'";
				if (flag) {
					params += ",";
					params_without_dual += ",";
				}
				params += ("solver=" + solver_str);
				params_without_dual += ("solver=" + solver_str);
				flag = true;
			}
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getMax_iter() != null) {
			max_iter_str = String
					.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getMax_iter().getIntValue());
			if (flag) {
				params += ",";
				params_without_dual += ",";
				params_without_solver += ",";
				params_without_dual_solver += ",";
			}
			params += ("max_iter=" + max_iter_str);
			params_without_dual += ("max_iter=" + max_iter_str);
			params_without_solver += ("max_iter=" + max_iter_str);
			params_without_dual_solver += ("max_iter=" + max_iter_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm())).getMulti_class()
				.getValue() != Multi_class.NOT_SET_VALUE) {
			multi_class_str = ((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
					.getMulti_class().getLiteral();
			if (flag) {
				params += ",";
				params_without_dual += ",";
				params_without_solver += ",";
				params_without_dual_solver += ",";
			}
			params += ("multi_class=" + multi_class_str);
			params_without_dual += ("multi_class=" + multi_class_str);
			params_without_solver += ("multi_class=" + multi_class_str);
			params_without_dual_solver += ("multi_class=" + multi_class_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getVerbose() != null) {
			verbose_str = String
					.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getVerbose().getIntValue());
			if (flag) {
				params += ",";
				params_without_dual += ",";
				params_without_solver += ",";
				params_without_dual_solver += ",";
			}
			params += ("verbose=" + verbose_str);
			params_without_dual += ("verbose=" + verbose_str);
			params_without_solver += ("verbose=" + verbose_str);
			params_without_dual_solver += ("verbose=" + verbose_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getWarm_start() != null) {
			warm_start_str = first_to_upper(String
					.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getWarm_start().isBoolValue()));
			if (flag) {
				params += ",";
				params_without_dual += ",";
				params_without_solver += ",";
				params_without_dual_solver += ",";
			}
			params += ("warm_start=" + warm_start_str);
			params_without_dual += ("warm_start=" + warm_start_str);
			params_without_solver += ("warm_start=" + warm_start_str);
			params_without_dual_solver += ("warm_start=" + warm_start_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getN_jobs() != null) {
			n_jobs_str = String
					.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getN_jobs().getIntValue());
			if (flag) {
				params += ",";
				params_without_dual += ",";
				params_without_solver += ",";
				params_without_dual_solver += ",";
			}
			params += ("n_jobs=" + n_jobs_str);
			params_without_dual += ("n_jobs=" + n_jobs_str);
			params_without_solver += ("n_jobs=" + n_jobs_str);
			params_without_dual_solver += ("n_jobs=" + n_jobs_str);
			flag = true;
		}
		if (((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
				.getL1_ratio() != null) {
			l1_ratio_str = String
					.valueOf(((LinearClassifierLogisticRegression) (action.getDataAnalytics().getModelAlgorithm()))
							.getL1_ratio().getDoubleValue());
			if (flag) {
				params += ",";
				params_without_dual += ",";
				params_without_solver += ",";
				params_without_dual_solver += ",";
			}
			params += ("l1_ratio=" + l1_ratio_str);
			params_without_dual += ("l1_ratio=" + l1_ratio_str);
			params_without_solver += ("l1_ratio=" + l1_ratio_str);
			params_without_dual_solver += ("l1_ratio=" + l1_ratio_str);
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.linear_model import LogisticRegression\n");
		pythonScriptStringBuilder.append("model = None\n");

		// AutoML
		if (action.getDataAnalytics().getAutoML() == AutoML.ON && action.getDataAnalytics()
				.getPreprocess_feature_scaler() != Preprocess_feature_scaler.OFF) {
			// SAGA requires scaling the numeric values. See the API doc:
			// https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html
			System.out.println(
					"INFO: The AutoML mode is on and the data are normalized/standardized/scaled. Hence, depending on the data, the selected solver/optimizer might be ignored and set to a more optimal choice.\n");
			// See the API doc:
			// https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html
			// For small datasets, ‘liblinear’ is a good choice, whereas ‘sag’ and ‘saga’
			// are faster for large ones.
			pythonScriptStringBuilder.append("if(X_train.shape[0]<1000):\n"); // "small" dataset
			pythonScriptStringBuilder.append("	if(dual_is_true and X_train.shape[0]>X_train.shape[1]):\n");
			pythonScriptStringBuilder.append("		model = LogisticRegression(" + params_without_dual_solver
					+ ",solver=\'" + Optimizer.LIBLINEAR + "\')\n");
			pythonScriptStringBuilder.append("	else:\n");
			pythonScriptStringBuilder.append("		model = LogisticRegression(" + params_without_solver + ",solver=\'"
					+ Optimizer.LIBLINEAR + "\')\n");
			pythonScriptStringBuilder.append("else:\n"); // "large" or "very large" dataset
			pythonScriptStringBuilder.append("	if(dual_is_true and X_train.shape[0]>X_train.shape[1]):\n");
			pythonScriptStringBuilder.append("		model = LogisticRegression(" + params_without_dual_solver
					+ ",solver=\'" + Optimizer.SAGA + "\')\n");
			pythonScriptStringBuilder.append("	else:\n");
			pythonScriptStringBuilder.append("		model = LogisticRegression(" + params_without_solver + ",solver=\'"
					+ Optimizer.SAGA + "\')\n");

		} else {
			pythonScriptStringBuilder.append("if(dual_is_true and X_train.shape[0]>X_train.shape[1]):\n");
			pythonScriptStringBuilder.append("	model = LogisticRegression(" + params_without_dual + ")\n");
			pythonScriptStringBuilder.append("else:\n");
			pythonScriptStringBuilder.append("	model = LogisticRegression(" + params + ")\n");
		}
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_lin_cl_log_reg.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
			
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

		}
	}

	private void generatePythonDATrainScriptNaiveBayesGaussian(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptNaiveBayesGaussian(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ "naive bayes.\n" + "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptNaiveBayesGaussian(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String priors_str = "";
		String var_smoothing_str = "";
		String params = "";
		boolean flag = false;

		if (((NaiveBayesGaussian) (action.getDataAnalytics().getModelAlgorithm())).getPriors() != null) {
			priors_str = ((NaiveBayesGaussian) (action.getDataAnalytics().getModelAlgorithm())).getPriors().toString();
			params += ("priors=" + priors_str);
			flag = true;
		}
		if (((NaiveBayesGaussian) (action.getDataAnalytics().getModelAlgorithm())).getVar_smoothing() != null) {
			var_smoothing_str = String.valueOf(((NaiveBayesGaussian) (action.getDataAnalytics().getModelAlgorithm()))
					.getVar_smoothing().getDoubleValue());
			if (flag)
				params += ",";
			params += ("var_smoothing=" + var_smoothing_str);
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.naive_bayes import GaussianNB\n");
		pythonScriptStringBuilder.append("model = GaussianNB(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_gnb.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		}
	}

	private void generatePythonDATrainScriptNaiveBayesMultinomial(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptNaiveBayesMultinomial(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ "naive bayes.\n" + "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptNaiveBayesMultinomial(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String alpha_str = "";
		String fit_prior_str = "";
		String class_prior_str = "";
		String params = "";
		boolean flag = false;

		if (((NaiveBayesMultinomial) (action.getDataAnalytics().getModelAlgorithm())).getAlpha() != null) {
			alpha_str = String.valueOf(((NaiveBayesMultinomial) (action.getDataAnalytics().getModelAlgorithm()))
					.getAlpha().getDoubleValue());
			params += ("alpha=" + alpha_str);
			flag = true;
		}
		if (((NaiveBayesMultinomial) (action.getDataAnalytics().getModelAlgorithm())).getFit_prior() != null) {
			fit_prior_str = first_to_upper(
					String.valueOf(((NaiveBayesMultinomial) (action.getDataAnalytics().getModelAlgorithm()))
							.getFit_prior().isBoolValue()));
			if (flag)
				params += ",";
			params += ("fit_prior=" + fit_prior_str);
			flag = true;
		}
		if (((NaiveBayesMultinomial) (action.getDataAnalytics().getModelAlgorithm())).getClass_prior() != null) {
			class_prior_str = ((NaiveBayesMultinomial) (action.getDataAnalytics().getModelAlgorithm())).getClass_prior()
					.toString();
			if (flag)
				params += ",";
			params += ("class_prior=" + class_prior_str);
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.naive_bayes import MultinomialNB\n");
		pythonScriptStringBuilder.append("model = MultinomialNB(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_mnb.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		}
	}
	

	private void generatePythonDATrainScriptNaiveBayesComplement(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptNaiveBayesComplement(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ "naive bayes.\n" + "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptNaiveBayesComplement(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String alpha_str = "";
		String fit_prior_str = "";
		String class_prior_str = "";
		String norm_str = "";
		String params = "";
		boolean flag = false;

		if (((NaiveBayesComplement) (action.getDataAnalytics().getModelAlgorithm())).getAlpha() != null) {
			alpha_str = String.valueOf(((NaiveBayesComplement) (action.getDataAnalytics().getModelAlgorithm()))
					.getAlpha().getDoubleValue());
			params += ("alpha=" + alpha_str);
			flag = true;
		}
		if (((NaiveBayesComplement) (action.getDataAnalytics().getModelAlgorithm())).getFit_prior() != null) {
			fit_prior_str = first_to_upper(
					String.valueOf(((NaiveBayesComplement) (action.getDataAnalytics().getModelAlgorithm()))
							.getFit_prior().isBoolValue()));
			if (flag)
				params += ",";
			params += ("fit_prior=" + fit_prior_str);
			flag = true;
		}
		if (((NaiveBayesComplement) (action.getDataAnalytics().getModelAlgorithm())).getClass_prior() != null) {
			class_prior_str = ((NaiveBayesComplement) (action.getDataAnalytics().getModelAlgorithm())).getClass_prior()
					.toString();
			if (flag)
				params += ",";
			params += ("class_prior=" + class_prior_str);
			flag = true;
		}
		if (((NaiveBayesComplement) (action.getDataAnalytics().getModelAlgorithm())).getNorm() != null) {
			norm_str = first_to_upper(String.valueOf(
					((NaiveBayesComplement) (action.getDataAnalytics().getModelAlgorithm())).getNorm().isBoolValue()));
			if (flag)
				params += ",";
			params += ("norm=" + norm_str);
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.naive_bayes import ComplementNB\n");
		pythonScriptStringBuilder.append("model = ComplementNB(" + params + ")\n");

		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_cnb.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

		}
	}

	private void generatePythonDATrainScriptNaiveBayesBernoulli(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptNaiveBayesBernoulli(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ "naive bayes.\n" + "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptNaiveBayesBernoulli(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String alpha_str = "";
		String binarize_str = "";
		String fit_prior_str = "";
		String class_prior_str = "";
		String params = "";
		boolean flag = false;

		if (((NaiveBayesBernoulli) (action.getDataAnalytics().getModelAlgorithm())).getAlpha() != null) {
			alpha_str = String.valueOf(((NaiveBayesBernoulli) (action.getDataAnalytics().getModelAlgorithm()))
					.getAlpha().getDoubleValue());
			params += ("alpha=" + alpha_str);
			flag = true;
		}
		if (((NaiveBayesBernoulli) (action.getDataAnalytics().getModelAlgorithm())).getBinarize() != null) {
			binarize_str = String.valueOf(((NaiveBayesBernoulli) (action.getDataAnalytics().getModelAlgorithm()))
					.getBinarize().getDoubleValue());
			if (flag)
				params += ",";
			params += ("binarize=" + binarize_str);
			flag = true;
		}
		if (((NaiveBayesBernoulli) (action.getDataAnalytics().getModelAlgorithm())).getFit_prior() != null) {
			fit_prior_str = first_to_upper(
					String.valueOf(((NaiveBayesBernoulli) (action.getDataAnalytics().getModelAlgorithm()))
							.getFit_prior().isBoolValue()));
			if (flag)
				params += ",";
			params += ("fit_prior=" + fit_prior_str);
			flag = true;
		}
		if (((NaiveBayesBernoulli) (action.getDataAnalytics().getModelAlgorithm())).getClass_prior() != null) {
			class_prior_str = ((NaiveBayesBernoulli) (action.getDataAnalytics().getModelAlgorithm())).getClass_prior()
					.toString();
			if (flag)
				params += ",";
			params += ("class_prior=" + class_prior_str);
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.naive_bayes import BernoulliNB\n");
		pythonScriptStringBuilder.append("model = BernoulliNB(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_bnb.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

		}
	}

	private void generatePythonDATrainScriptNaiveBayesCategorical(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptNaiveBayesCategorical(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ "naive bayes.\n" + "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptNaiveBayesCategorical(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String alpha_str = "";
		String fit_prior_str = "";
		String class_prior_str = "";
		String min_categories_str = "";
		String params = "";
		boolean flag = false;

		if (((NaiveBayesCategorical) (action.getDataAnalytics().getModelAlgorithm())).getAlpha() != null) {
			alpha_str = String.valueOf(((NaiveBayesCategorical) (action.getDataAnalytics().getModelAlgorithm()))
					.getAlpha().getDoubleValue());
			params += ("alpha=" + alpha_str);
			flag = true;
		}
		if (((NaiveBayesCategorical) (action.getDataAnalytics().getModelAlgorithm())).getFit_prior() != null) {
			fit_prior_str = first_to_upper(
					String.valueOf(((NaiveBayesCategorical) (action.getDataAnalytics().getModelAlgorithm()))
							.getFit_prior().isBoolValue()));
			if (flag)
				params += ",";
			params += ("fit_prior=" + fit_prior_str);
			flag = true;
		}
		if (((NaiveBayesCategorical) (action.getDataAnalytics().getModelAlgorithm())).getClass_prior() != null) {
			class_prior_str = ((NaiveBayesCategorical) (action.getDataAnalytics().getModelAlgorithm())).getClass_prior()
					.toString();
			if (flag)
				params += ",";
			params += ("class_prior=" + class_prior_str);
			flag = true;
		}
		if (((NaiveBayesCategorical) (action.getDataAnalytics().getModelAlgorithm())).getMin_categories() != null) {
			min_categories_str = ((NaiveBayesCategorical) (action.getDataAnalytics().getModelAlgorithm()))
					.getMin_categories().toString();
			if (flag)
				params += ",";
			params += ("min_categories=" + min_categories_str);
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.naive_bayes import CategoricalNB\n");
		pythonScriptStringBuilder.append("model = CategoricalNB(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_cat_nb.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

		}
	}

	private void generatePythonDATrainScriptDecisionTreeRegressor(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptDecisionTreeRegressor(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptDecisionTreeRegressor(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String criterion_str = "";
		String splitter_str = "";
		String max_depth_str = "";
		String min_samples_split_str = "";
		String min_samples_leaf_str = "";
		String min_weight_fraction_leaf_str = "";
		String max_features_str = "";
		String random_state_str = "";
		String max_leaf_nodes_str = "";
		String min_impurity_decrease_str = "";
		String min_impurity_split_str = "";
		String ccp_alpha_str = "";
		String params = "";
		boolean flag = false;

		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
				.getDecisionTreeRegressorCriterion().getValue() != DecisionTreeRegressorCriterion.NOT_SET_VALUE) {
			criterion_str = ((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getDecisionTreeRegressorCriterion().getLiteral();
			params += ("criterion=" + criterion_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getDecisionTreeSplitter()
				.getValue() != DecisionTreeSplitter.NOT_SET_VALUE) {
			splitter_str = ((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getDecisionTreeSplitter().getLiteral();
			if (flag)
			params += ("splitter=" + splitter_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_depth() != null) {
			max_depth_str = String.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_depth().getIntValue());
			if (flag)
				params += ",";
			params += ("max_depth=" + max_depth_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMin_samples_split() != null) {
			min_samples_split_str = String
					.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_samples_split().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_samples_split=" + min_samples_split_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMin_samples_leaf() != null) {
			min_samples_leaf_str = String
					.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_samples_leaf().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_samples_leaf=" + min_samples_leaf_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_weight_fraction_leaf() != null) {
			min_weight_fraction_leaf_str = String
					.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_weight_fraction_leaf().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_weight_fraction_leaf=" + min_weight_fraction_leaf_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_features_numeric() != null
				|| ((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_features_enum()
						.getValue() != Max_features_enum.NOT_SET_VALUE) {

			if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_features_numeric() != null) {
				max_features_str = String
						.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
								.getMax_features_numeric().getDoubleValue());
				if (flag)
					params += ",";
				params += ("max_features=" + max_features_str);
				flag = true;
			} else {
				max_features_str = ((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
						.getMax_features_enum().getLiteral();
				if (flag)
					params += ",";
				params += ("max_features=" + max_features_str);
				flag = true;
			}
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getRandom_state() != null) {
			random_state_str = String.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getRandom_state().getIntValue());
			if (flag)
				params += ",";
			params += ("random_state=" + random_state_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_leaf_nodes() != null) {
			max_leaf_nodes_str = String
					.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMax_leaf_nodes().getIntValue());
			if (flag)
				params += ",";
			params += ("max_leaf_nodes=" + max_leaf_nodes_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_impurity_decrease() != null) {
			min_impurity_decrease_str = String
					.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_impurity_decrease().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_impurity_decrease=" + min_impurity_decrease_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMin_impurity_split() != null) {
			System.err.println(
					"WARNING: In the decision tree regressor, parameter min_impurity_split is present. However, this has been deprecated. Use min_impurity_decrease instead.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeRegressor.html\n");
			min_impurity_split_str = String
					.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_impurity_split().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_impurity_split=" + min_impurity_split_str);
			flag = true;
		}
		if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getCcp_alpha() != null) {
			if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getCcp_alpha()
					.getDoubleValue() < 0) { // See the API documentation:
												// https://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeRegressor.html
				System.err.println(
						"ERROR: In the decision tree regressor, parameter ccp_alpha is not allowed to be negative.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeRegressor.html\n");
			} else {
				ccp_alpha_str = String.valueOf(((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm()))
						.getCcp_alpha().getDoubleValue());
				if (flag)
					params += ",";
				params += ("ccp_alpha=" + ccp_alpha_str);
				flag = true;
			}
		}

		pythonScriptStringBuilder.append("from sklearn.tree import DecisionTreeRegressor\n");
		pythonScriptStringBuilder.append("model = DecisionTreeRegressor(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_dtr.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

		}
	}

	private void generatePythonDATrainScriptDecisionTreeClassifier(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptDecisionTreeClassifier(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptDecisionTreeClassifier(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String criterion_str = "";
		String splitter_str = "";
		String max_depth_str = "";
		String min_samples_split_str = "";
		String min_samples_leaf_str = "";
		String min_weight_fraction_leaf_str = "";
		String max_features_str = "";
		String random_state_str = "";
		String max_leaf_nodes_str = "";
		String min_impurity_decrease_str = "";
		String min_impurity_split_str = "";
		String class_weight_str = "";
		String ccp_alpha_str = "";
		String params = "";
		boolean flag = false;

		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
				.getDecisionTreeClassifierCriterion().getValue() != DecisionTreeClassifierCriterion.NOT_SET_VALUE) {
			criterion_str = ((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getDecisionTreeClassifierCriterion().getLiteral();
			params += ("criterion=" + criterion_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getDecisionTreeSplitter()
				.getValue() != DecisionTreeSplitter.NOT_SET_VALUE) {
			splitter_str = ((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getDecisionTreeSplitter().getLiteral();
			if (flag)
				params += ",";
			params += ("splitter=" + splitter_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_depth() != null) {
			max_depth_str = String.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_depth().getIntValue());
			if (flag)
				params += ",";
			params += ("max_depth=" + max_depth_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMin_samples_split() != null) {
			min_samples_split_str = String
					.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_samples_split().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_samples_split=" + min_samples_split_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMin_samples_leaf() != null) {
			min_samples_leaf_str = String
					.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_samples_leaf().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_samples_leaf=" + min_samples_leaf_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_weight_fraction_leaf() != null) {
			min_weight_fraction_leaf_str = String
					.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_weight_fraction_leaf().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_weight_fraction_leaf=" + min_weight_fraction_leaf_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_features_numeric() != null
				|| ((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_features_enum()
						.getValue() != Max_features_enum.NOT_SET_VALUE) {

			if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_features_numeric() != null) {
				max_features_str = String
						.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
								.getMax_features_numeric().getDoubleValue());
				if (flag)
					params += ",";
				params += ("max_features=" + max_features_str);
				flag = true;
			} else {
				max_features_str = ((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
						.getMax_features_enum().getLiteral();
				if (flag)
					params += ",";
				params += ("max_features=" + max_features_str);
				flag = true;
			}
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getRandom_state() != null) {
			random_state_str = String.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getRandom_state().getIntValue());
			if (flag)
				params += ",";
			params += ("random_state=" + random_state_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_leaf_nodes() != null) {
			max_leaf_nodes_str = String
					.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMax_leaf_nodes().getIntValue());
			if (flag)
				params += ",";
			params += ("max_leaf_nodes=" + max_leaf_nodes_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_impurity_decrease() != null) {
			min_impurity_decrease_str = String
					.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_impurity_decrease().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_impurity_decrease=" + min_impurity_decrease_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_impurity_split() != null) {
			System.err.println(
					"WARNING: In the decision tree classifier, parameter min_impurity_split is present. However, this has been deprecated. Use min_impurity_decrease instead.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeClassifier.html\n");
			min_impurity_split_str = String
					.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_impurity_split().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_impurity_split=" + min_impurity_split_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getClass_weight() != null) {
			class_weight_str = ((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getClass_weight().toString();
			if (flag)
				params += ",";
			params += ("class_weight=" + class_weight_str);
			flag = true;
		}
		if (((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm())).getCcp_alpha() != null) {
			if (((DecisionTreeRegressor) (action.getDataAnalytics().getModelAlgorithm())).getCcp_alpha()
					.getDoubleValue() < 0) { // See the API documentation:
												// https://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeClassifier.html
				System.err.println(
						"ERROR: In the decision tree classifier, parameter ccp_alpha is not allowed to be negative.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeClassifier.html\n");
			} else {
				ccp_alpha_str = String
						.valueOf(((DecisionTreeClassifier) (action.getDataAnalytics().getModelAlgorithm()))
								.getCcp_alpha().getDoubleValue());
				if (flag)
					params += ",";
				params += ("ccp_alpha=" + ccp_alpha_str);
				flag = true;
			}
		}

		pythonScriptStringBuilder.append("from sklearn.tree import DecisionTreeClassifier\n");
		pythonScriptStringBuilder.append("model = DecisionTreeClassifier(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_dtc.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

		}
	}

	private void generatePythonDATrainScriptRandomForestRegressor(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptRandomForestRegressor(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptRandomForestRegressor(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String n_estimators_str = "";
		String criterion_str = "";
		String max_depth_str = "";
		String min_samples_split_str = "";
		String min_samples_leaf_str = "";
		String min_weight_fraction_leaf_str = "";
		String max_features_str = "";
		String max_leaf_nodes_str = "";
		String min_impurity_decrease_str = "";
		String min_impurity_split_str = "";
		String bootstrap_str = "";
		String oob_score_str = "";
		String n_jobs_str = "";
		String random_state_str = "";
		String verbose_str = "";
		String warm_start_str = "";
		String ccp_alpha_str = "";
		String max_samples_str = "";
		String params = "";
		boolean flag = false;

		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getN_estimators() != null) {
			n_estimators_str = String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getN_estimators().getIntValue());
			params += ("n_estimators=" + n_estimators_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
				.getRandomForestRegressorCriterion().getValue() != RandomForestRegressorCriterion.NOT_SET_VALUE) {
			criterion_str = ((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getRandomForestRegressorCriterion().getLiteral();
			if (flag)
				params += ",";
			params += ("criterion=" + criterion_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_depth() != null) {
			max_depth_str = String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_depth().getIntValue());
			if (flag)
				params += ",";
			params += ("max_depth=" + max_depth_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMin_samples_split() != null) {
			min_samples_split_str = String
					.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_samples_split().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_samples_split=" + min_samples_split_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMin_samples_leaf() != null) {
			min_samples_leaf_str = String
					.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_samples_leaf().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_samples_leaf=" + min_samples_leaf_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_weight_fraction_leaf() != null) {
			min_weight_fraction_leaf_str = String
					.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_weight_fraction_leaf().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_weight_fraction_leaf=" + min_weight_fraction_leaf_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_features_numeric() != null
				|| ((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_features_enum()
						.getValue() != Max_features_enum.NOT_SET_VALUE) {

			if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_features_numeric() != null) {
				max_features_str = String
						.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
								.getMax_features_numeric().getDoubleValue());
				if (flag)
					params += ",";
				params += ("max_features=" + max_features_str);
				flag = true;
			} else {
				max_features_str = ((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
						.getMax_features_enum().getLiteral();
				if (flag)
					params += ",";
				params += ("max_features=" + max_features_str);
				flag = true;
			}
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_leaf_nodes() != null) {
			max_leaf_nodes_str = String
					.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMax_leaf_nodes().getIntValue());
			if (flag)
				params += ",";
			params += ("max_leaf_nodes=" + max_leaf_nodes_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_impurity_decrease() != null) {
			min_impurity_decrease_str = String
					.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_impurity_decrease().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_impurity_decrease=" + min_impurity_decrease_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMin_impurity_split() != null) {
			System.err.println(
					"WARNING: In the random forest regressor, parameter min_impurity_split is present. However, this has been deprecated. Use min_impurity_decrease instead.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.ensemble.RandomForestRegressor.html\n");
			min_impurity_split_str = String
					.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_impurity_split().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_impurity_split=" + min_impurity_split_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getBootstrap() != null) {
			bootstrap_str = first_to_upper(
					String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getBootstrap().isBoolValue()));
			if (flag)
				params += ",";
			params += ("bootstrap=" + bootstrap_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getOob_score() != null) {
			oob_score_str = first_to_upper(
					String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getOob_score().isBoolValue()));
			if (flag)
				params += ",";
			params += ("oob_score=" + oob_score_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getN_jobs() != null) {
			n_jobs_str = String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getN_jobs().getIntValue());
			if (flag)
				params += ",";
			params += ("n_jobs=" + n_jobs_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getRandom_state() != null) {
			random_state_str = String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getRandom_state().getIntValue());
			if (flag)
				params += ",";
			params += ("random_state=" + random_state_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getVerbose() != null) {
			verbose_str = String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getVerbose().getIntValue());
			if (flag)
				params += ",";
			params += ("verbose=" + verbose_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getWarm_start() != null) {
			warm_start_str = first_to_upper(
					String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
							.getWarm_start().isBoolValue()));
			if (flag)
				params += ",";
			params += ("warm_start=" + warm_start_str);
			flag = true;
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getCcp_alpha() != null) {
			if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getCcp_alpha()
					.getDoubleValue() < 0) { // See the API documentation:
												// https://scikit-learn.org/stable/modules/generated/sklearn.ensemble.RandomForestRegressor.html
				System.err.println(
						"ERROR: In the random forest regressor, parameter ccp_alpha is not allowed to be negative.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.ensemble.RandomForestRegressor.html\n");
			} else {
				ccp_alpha_str = String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
						.getCcp_alpha().getDoubleValue());
				if (flag)
					params += ",";
				params += ("ccp_alpha=" + ccp_alpha_str);
				flag = true;
			}
		}
		if (((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm())).getMax_samples() != null) {
			max_samples_str = String.valueOf(((RandomForestRegressor) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_samples().getDoubleValue());
			if (flag)
				params += ",";
			params += ("max_samples=" + max_samples_str);
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.ensemble import RandomForestRegressor\n");
		pythonScriptStringBuilder.append("model = RandomForestRegressor(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_rfr.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

		}
	}

	private void generatePythonDATrainScriptRandomForestClassifier(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptRandomForestClassifier(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptRandomForestClassifier(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		String n_estimators_str = "";
		String criterion_str = "";
		String max_depth_str = "";
		String min_samples_split_str = "";
		String min_samples_leaf_str = "";
		String min_weight_fraction_leaf_str = "";
		String max_features_str = "";
		String max_leaf_nodes_str = "";
		String min_impurity_decrease_str = "";
		String min_impurity_split_str = "";
		String bootstrap_str = "";
		String oob_score_str = "";
		String n_jobs_str = "";
		String random_state_str = "";
		String verbose_str = "";
		String warm_start_str = "";
		String class_weight_str = "";
		String ccp_alpha_str = "";
		String max_samples_str = "";
		String params = "";
		boolean flag = false;

		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getN_estimators() != null) {
			n_estimators_str = String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getN_estimators().getIntValue());
			params += ("n_estimators=" + n_estimators_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
				.getRandomForestClassifierCriterion().getValue() != RandomForestClassifierCriterion.NOT_SET_VALUE) {
			criterion_str = ((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getRandomForestClassifierCriterion().getLiteral();
			if (flag)
				params += ",";
			params += ("criterion=" + criterion_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_depth() != null) {
			max_depth_str = String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_depth().getIntValue());
			if (flag)
				params += ",";
			params += ("max_depth=" + max_depth_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMin_samples_split() != null) {
			min_samples_split_str = String
					.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_samples_split().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_samples_split=" + min_samples_split_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMin_samples_leaf() != null) {
			min_samples_leaf_str = String
					.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_samples_leaf().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_samples_leaf=" + min_samples_leaf_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_weight_fraction_leaf() != null) {
			min_weight_fraction_leaf_str = String
					.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_weight_fraction_leaf().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_weight_fraction_leaf=" + min_weight_fraction_leaf_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_features_numeric() != null
				|| ((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_features_enum()
						.getValue() != Max_features_enum.NOT_SET_VALUE) {

			if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_features_numeric() != null) {
				max_features_str = String
						.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
								.getMax_features_numeric().getDoubleValue());
				if (flag)
					params += ",";
				params += ("max_features=" + max_features_str);
				flag = true;
			} else {
				max_features_str = ((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
						.getMax_features_enum().getLiteral();
				if (flag)
					params += ",";
				params += ("max_features=" + max_features_str);
				flag = true;
			}
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_leaf_nodes() != null) {
			max_leaf_nodes_str = String
					.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMax_leaf_nodes().getIntValue());
			if (flag)
				params += ",";
			params += ("max_leaf_nodes=" + max_leaf_nodes_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_impurity_decrease() != null) {
			min_impurity_decrease_str = String
					.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_impurity_decrease().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_impurity_decrease=" + min_impurity_decrease_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
				.getMin_impurity_split() != null) {
			System.err.println(
					"WARNING: In the random forest classifier, parameter min_impurity_split is present. However, this has been deprecated. Use min_impurity_decrease instead.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.ensemble.RandomForestClassifier.html\n");
			min_impurity_split_str = String
					.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getMin_impurity_split().getDoubleValue());
			if (flag)
				params += ",";
			params += ("min_impurity_split=" + min_impurity_split_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getBootstrap() != null) {
			bootstrap_str = first_to_upper(
					String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getBootstrap().isBoolValue()));
			if (flag)
				params += ",";
			params += ("bootstrap=" + bootstrap_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getOob_score() != null) {
			oob_score_str = first_to_upper(
					String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getOob_score().isBoolValue()));
			if (flag)
				params += ",";
			params += ("oob_score=" + oob_score_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getN_jobs() != null) {
			n_jobs_str = String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getN_jobs().getIntValue());
			if (flag)
				params += ",";
			params += ("n_jobs=" + n_jobs_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getRandom_state() != null) {
			random_state_str = String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getRandom_state().getIntValue());
			if (flag)
				params += ",";
			params += ("random_state=" + random_state_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getVerbose() != null) {
			verbose_str = String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getVerbose().getIntValue());
			if (flag)
				params += ",";
			params += ("verbose=" + verbose_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getWarm_start() != null) {
			warm_start_str = first_to_upper(
					String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
							.getWarm_start().isBoolValue()));
			if (flag)
				params += ",";
			params += ("warm_start=" + warm_start_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getClass_weight() != null) {
			class_weight_str = ((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getClass_weight().toString();
			if (flag)
				params += ",";
			params += ("class_weight=" + class_weight_str);
			flag = true;
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getCcp_alpha() != null) {
			if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getCcp_alpha()
					.getDoubleValue() < 0) { // See the API documentation:
												// https://scikit-learn.org/stable/modules/generated/sklearn.ensemble.RandomForestClassifier.html
				System.err.println(
						"ERROR: In the random forest classifier, parameter ccp_alpha is not allowed to be negative.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.ensemble.RandomForestClassifier.html\n");
			} else {
				ccp_alpha_str = String
						.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
								.getCcp_alpha().getDoubleValue());
				if (flag)
					params += ",";
				params += ("ccp_alpha=" + ccp_alpha_str);
				flag = true;
			}
		}
		if (((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm())).getMax_samples() != null) {
			max_samples_str = String.valueOf(((RandomForestClassifier) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_samples().getDoubleValue());
			if (flag)
				params += ",";
			params += ("max_samples=" + max_samples_str);
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.ensemble import RandomForestClassifier\n");
		pythonScriptStringBuilder.append("model = RandomForestClassifier(" + params + ")\n");
		if (!isForSemiSupervised) {
			pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "train_model_rfc.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

		}
	}
	
	

	private void generatePythonDATrainScriptNN_MultilayerPerceptron(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
			generatePythonScikitLearnDATrainScriptNN_MultilayerPerceptron(path_str, action, pythonScriptStringBuilder,
					isForSemiSupervised);
		} else if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
			generatePythonKerasTensorFlowDATrainScriptNN_MultilayerPerceptron(path_str, action,
					pythonScriptStringBuilder);
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}


	private void generatePythonScikitLearnDATrainScriptNN_MultilayerPerceptron(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder, boolean isForSemiSupervised) {
		if (action.getDataAnalytics().getPreprocess_feature_scaler() == Preprocess_feature_scaler.OFF) {
			System.err.println(
					"SEVERE WARNING: For the Neural Network (Multi-Layer Perceptron) model, normalization/standardization of the numerical features is highly recommneded.\n If you have any numerical features, try using the preprocess_feature_scaling parameter in the data analytics section of your model, e.g., preprocess_feature_scaling STANDARDIZATION_Z_SCORE_NORMALIZATION or you may set automl to ON: automl ON. AuoML will take care of that automatically.\n");
		}
		String hidden_layer_sizes_str = "";
		String activation_str = "";
		String optimizer_str = ""; // i.e., "solver" in scikit-learn
		String alpha_str = "";
		String batch_size_str = "";
		String learning_rate_mode_str = ""; // i.e., "learnig rate" in scikit-learn
		String learning_rate_init_str = "";
		String power_t_str = "";
		String max_iter_str = "";
		String shuffle_str = "";
		String random_state_str = "";
		String tol_str = "";
		String verbose_str = "";
		String warm_start_str = "";
		String momentum_str = "";
		String nesterovs_momentum_str = "";
		String early_stopping_str = "";
		String validation_fraction_str = "";
		String beta_1_str = "";
		String beta_2_str = "";
		String epsilon_str = "";
		String n_iter_no_change_str = "";
		String max_fun_str = "";
		String params = "";
		String params_without_optimizer = "";
		boolean flag = false;
		boolean set_optimizer_automatically = false;

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getHidden_layer_sizes() != null) {
			hidden_layer_sizes_str = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getHidden_layer_sizes().toString();
			params += ("hidden_layer_sizes=" + hidden_layer_sizes_str);
			params_without_optimizer += ("hidden_layer_sizes=" + hidden_layer_sizes_str);
			flag = true;
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
				.getValue() != Activation.NOT_SET_VALUE) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
					.getValue() != Activation.IDENTITY_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
							.getValue() != Activation.LOGISTIC_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
							.getValue() != Activation.TANH_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
							.getValue() != Activation.RELU_VALUE) {
				System.err.println(
						"ERROR: Since the chosen DA/ML library is scikit-learn, in the neural network multi-layer perceptron model, parameter activation may only be one of the following: identity, logisitc, tanh or relu.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html\n");
			} else {
				activation_str = "\'" + ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getActivation().getLiteral() + "\'";
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("activation=" + activation_str);
				params_without_optimizer += ("activation=" + activation_str);
				flag = true;
			}
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getHidden_layers_activation_functions() != null) {
			System.err.println(
					"ERROR: Since the chosen DA/ML library is scikit-learn, in the neural network multi-layer perceptron model, parameter hidden_layers_activation_functions may NOT exist. Scikit-learn supports only one type of activation functio for all hidden layers. Please use the activation parameter instead.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html\n");
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
				.getValue() != Optimizer.NOT_SET_VALUE) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.ADAM_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.LBFGS_VALUE) {
				System.err.println(
						"ERROR: Since the chosen DA/ML library is scikit-learn, in the neural network multi-layer perceptron model, parameter optimizer/solver may only be one of the following: lbfgs, sgd or adam.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html\n");
			} else {
				optimizer_str = "\'" + ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getOptimizer().getLiteral() + "\'";
				if (flag)
					params += ",";
				params += ("solver=" + optimizer_str); // In scikit-learn, optimizer is called "solver".
				flag = true;
			}
		} else {
			if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
				System.out.println(
						"INFO: In the neural network multilayer perceptron model, no optimizer/solver is specified. Since AutoML is on, depending on the size of the dataset, either lbfgs or adam will be automatically chosen.\n If no_of_samples < 1000 (\"small\" dataset), lbfgs will be chosen as that shall converge faster and perform better. However, for larger datasets, adam will be set, since that works pretty well in terms of both training time and validation score.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
				set_optimizer_automatically = true;
			}
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getAlpha() != null) {
			alpha_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getAlpha().getDoubleValue());
			if (flag) {
				params += ",";
				params_without_optimizer += ",";
			}
			params += ("alpha=" + alpha_str);
			params_without_optimizer += ("alpha=" + alpha_str);
			flag = true;
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBatch_size() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() == Optimizer.LBFGS_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter batch_size is specified. However, since the optimizer/solver is set to lbfgs, the batch size parameter will be ignored. You may use another optimizer/solver (a stochasitic one).\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				batch_size_str = String
						.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getBatch_size().getIntValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("batch_size=" + batch_size_str);
				params_without_optimizer += ("batch_size=" + batch_size_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getLearning_rate_mode()
				.getValue() != Learning_rate_mode.NOT_SET_VALUE) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getLearning_rate_mode()
					.getValue() != Learning_rate_mode.CONSTANT_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getLearning_rate_mode().getValue() != Learning_rate_mode.INVSCALING_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getLearning_rate_mode().getValue() != Learning_rate_mode.ADAPTIVE_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter learning_rate_mode is present, which may specify the learning rate schedule for weight updates. However, the chosen library for DA/ML (i.e., scikit-learn) only allowes constant, invscaling or adaptive.\n The chosen one is not supported. Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter learning_rate_mode is present, which may specify the learning rate schedule for weight updates. However, this is only supported for the SGD optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				learning_rate_mode_str = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getLearning_rate_mode().getLiteral();
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("learning_rate=" + learning_rate_mode_str); // In scikit-learn, learning_rate_mode is called
																		// "learning_rate".
				params_without_optimizer += ("learning_rate=" + learning_rate_mode_str); // In scikit-learn,
																							// learning_rate_mode is
																							// called "learning_rate".
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getLearning_rate_init() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter learning_rate_init is present, which may specify the initial learning rate. However, this is only supported for the SGD or adam optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				learning_rate_init_str = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getLearning_rate_init().toString();
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("learning_rate_init=" + learning_rate_init_str);
				params_without_optimizer += ("learning_rate_init=" + learning_rate_init_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getPower() != null) {
			System.err.println(
					"WARNING: In the neural network multilayer perceptron model, parameter power is present. However, the chosen library for DA/ML (i.e., scikit-learn) supports the power_t parameter, not power. Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getPower_t() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter power_t is present. However, this is only supported for the SGD optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getLearning_rate_mode().getValue() != Learning_rate_mode.INVSCALING_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter power_t is present. However, this is only supported if the learning_rate_mode is set to the invscaling schedule. Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				power_t_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getPower_t().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("power_t=" + power_t_str);
				params_without_optimizer += ("power_t=" + power_t_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMax_iter() != null) {
			max_iter_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getMax_iter().getIntValue());
			if (flag) {
				params += ",";
				params_without_optimizer += ",";
			}
			params += ("max_iter=" + max_iter_str);
			params_without_optimizer += ("max_iter=" + max_iter_str);
			flag = true;
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getShuffle() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getShuffle().isBoolValue()
					&& action.getDataAnalytics().getSequential().getValue() == Sequential.TRUE_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter shuffle is set to true. However, in the data analytics section of the model, it is mentioned that the data are sequential (i.e., sequential is set to true).\n It is not allowed to shuffle time series or any kind of sequential data, since the order of samples does matter! Thus, the shuffle parameter will be ignored.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter shuffle is present. However, in the chosen DA/ML library, i.e., scikit-learn, this is only supported for the SGD or adam solver/optimizer. Thus, it will be ignored.\n See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				shuffle_str = first_to_upper(
						String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getShuffle().isBoolValue()));
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("shuffle=" + shuffle_str);
				params_without_optimizer += ("shuffle=" + shuffle_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getRandom_state() != null) {
			random_state_str = String
					.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getRandom_state().getIntValue());
			if (flag) {
				params += ",";
				params_without_optimizer += ",";
			}
			params += ("random_state=" + random_state_str);
			params_without_optimizer += ("random_state=" + random_state_str);
			flag = true;
		} else {
			System.err.println(
					"WARNING: In the neural network multilayer perceptron model, no parameter random_state is provided. Pass an integer for reproducible results across multiple function calls!\n See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html.\n");
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getTol() != null) {
			if ((((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getEarly_stopping() == null)
					|| !(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEarly_stopping()
							.isBoolValue())) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter tol is present. However, the early_stopping parameter either does not exist or is False. Please either set early_stopping to True or remove tol.\n See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html.\n");
			} else {
				tol_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getTol().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("tol=" + tol_str);
				params_without_optimizer += ("tol=" + tol_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getVerbose() != null) {
			verbose_str = first_to_upper(
					String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getVerbose().isBoolValue()));
			if (flag) {
				params += ",";
				params_without_optimizer += ",";
			}
			params += ("verbose=" + verbose_str);
			params_without_optimizer += ("verbose=" + verbose_str);
			flag = true;
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getWarm_start() != null) {
			warm_start_str = first_to_upper(
					String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getWarm_start().isBoolValue()));
			if (flag) {
				params += ",";
				params_without_optimizer += ",";
			}
			params += ("warm_start=" + warm_start_str);
			params_without_optimizer += ("warm_start=" + warm_start_str);
			flag = true;
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMomentum() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter momentum is present. However, that is only supported for the SGD optimizer/solver in the chosen library for DA/ML, i.e., scikit-learn. Thus, it will be ignored.\n See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMomentum()
					.getDoubleValue() < 0
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMomentum()
							.getDoubleValue() > 1) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter momentum is present. However, its value may only be between 0 and 1. This is currently not the case. Thus, it will be ignored.\n See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html.\n");
			} else {
				momentum_str = String
						.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getMomentum().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("momentum=" + momentum_str);
				params_without_optimizer += ("momentum=" + momentum_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getNesterovs_momentum() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter nesterovs_momentum is present. However, that is only supported for the SGD optimizer/solver in the chosen library for DA/ML, i.e., scikit-learn. Thus, it will be ignored.\n See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMomentum() == null
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMomentum()
							.getDoubleValue() <= 0) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter nesterovs_momentum is present. However, that is only supported if the momentum parameter is given and has a value above 0. Thus, it will be ignored.\n See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html.\n");
			} else {
				nesterovs_momentum_str = first_to_upper(
						String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getNesterovs_momentum().isBoolValue()));
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("nesterovs_momentum=" + nesterovs_momentum_str);
				params_without_optimizer += ("nesterovs_momentum=" + nesterovs_momentum_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEarly_stopping() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter early_stopping is only supported for the SGD or adam optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				early_stopping_str = first_to_upper(
						String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getEarly_stopping().isBoolValue()));
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("early_stopping=" + early_stopping_str);
				params_without_optimizer += ("early_stopping=" + early_stopping_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getValidation_fraction() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getEarly_stopping() == null) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter validation_fraction is present. However, this is only supported if early_stopping is present and is set to true. Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else if (!((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEarly_stopping()
					.isBoolValue()) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter validation_fraction is present. However, this is only supported if early_stopping is set to true. This is currently not the case. Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getValidation_fraction().getDoubleValue() < 0
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getValidation_fraction().getDoubleValue() > 1) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter validation_fraction is present. However, its provided value is not between 0 and 1! Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				validation_fraction_str = String
						.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getValidation_fraction().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("validation_fraction=" + validation_fraction_str);
				params_without_optimizer += ("validation_fraction=" + validation_fraction_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_1() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter beta_1 is only supported for the adam optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_1()
					.getDoubleValue() < 0
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_1()
							.getDoubleValue() >= 1) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter beta_1 is present. However, its provided value is not in [0, 1)! Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				beta_1_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getBeta_1().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("beta_1=" + beta_1_str);
				params_without_optimizer += ("beta_1=" + beta_1_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_2() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter beta_2 is only supported for the adam optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_2()
					.getDoubleValue() < 0
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_2()
							.getDoubleValue() >= 1) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter beta_2 is present. However, its provided value is not in [0, 1)! Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				beta_2_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getBeta_2().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("beta_2=" + beta_2_str);
				params_without_optimizer += ("beta_2=" + beta_2_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEpsilon() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter epsilon is only supported for the adam optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				epsilon_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getEpsilon().getDoubleValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("epsilon=" + epsilon_str);
				params_without_optimizer += ("epsilon=" + epsilon_str);
				flag = true;
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getN_iter_no_change() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.ADAM_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.SGD_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter n_iter_no_change is only supported for the SGD or the adam optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				if ((((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getEarly_stopping() == null)
						|| !(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getEarly_stopping().isBoolValue())) {
					System.err.println(
							"WARNING: In the neural network multilayer perceptron model, parameter n_iter_no_change is present. However, the early_stopping parameter either does not exist or is False. Please either set early_stopping to True or remove n_iter_no_change.\n See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html.\n");
				} else {
					n_iter_no_change_str = String
							.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
									.getN_iter_no_change().getIntValue());
					if (flag) {
						params += ",";
						params_without_optimizer += ",";
					}
					params += ("n_iter_no_change=" + n_iter_no_change_str);
					params_without_optimizer += ("n_iter_no_change=" + n_iter_no_change_str);
					flag = true;
				}
			}
		}
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMax_fun() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.LBFGS_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter max_fun is only supported for the lbfgs optimizer/solver in the chosen library for DA/ML (i.e., scikit-learn). Thus, it will be ignored.\n Please see the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html.\n");
			} else {
				max_fun_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getMax_fun().getIntValue());
				if (flag) {
					params += ",";
					params_without_optimizer += ",";
				}
				params += ("max_fun=" + max_fun_str);
				params_without_optimizer += ("max_fun=" + max_fun_str);
				flag = true;
			}
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getLoss()
				.getValue() != Loss.NOT_SET_VALUE) {
			System.err.println(
					"ERROR: In the Neural Network (Multilayer Perceptron) model, parameter loss is present. However, the chosen library for DA/ML, i.e., scikit-learn does not accept this parameter, since its implemnetation of multilayer perceptron optimizes the log-loss function using LBFGS or stochastic gradient descent.\n Either remove it or try using another library, e.g., @dalib \"keras-tensorflow\".\n");
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEpochs() != null) {
			System.err.println(
					"ERROR: In the Neural Network (Multilayer Perceptron) model, parameter epochs is present. However, the chosen library for DA/ML, i.e., scikit-learn does not accept this parameter.\n Please use the max_iter parameter instead. See the API doc for more information: https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPRegressor.html or https://scikit-learn.org/stable/modules/generated/sklearn.neural_network.MLPClassifier.html.\n");
		}

		if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
			// NN Multi-Layer Perceptron (MLP) Regression
			pythonScriptStringBuilder.append("from sklearn.neural_network import MLPRegressor\n");
			if (set_optimizer_automatically) {
				pythonScriptStringBuilder.append("if(X_train.shape[0]<1000):\n"); // "small" dataset
				pythonScriptStringBuilder.append("	model = MLPRegressor(" + params_without_optimizer + ",solver=\'"
						+ Optimizer.LBFGS_VALUE + "\')\n");
				pythonScriptStringBuilder.append("else:\n");
				pythonScriptStringBuilder.append("	model = MLPRegressor(" + params_without_optimizer + ",solver=\'"
						+ Optimizer.ADAM + "\')\n");

			} else {
				pythonScriptStringBuilder.append("model = MLPRegressor(" + params + ")\n");
			}
			if (!isForSemiSupervised) {
				pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
				pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
						+ "train_model_nn_mlp_r.pickle', 'wb') as pickle_file:\n");
				pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
			}
		} else {
			// NN Multi-Layer Perceptron (MLP) Classification
			pythonScriptStringBuilder.append("from sklearn.neural_network import MLPClassifier\n");
			if (set_optimizer_automatically) {
				pythonScriptStringBuilder.append("if(X_train.shape[0]<1000):\n"); // "small" dataset
				pythonScriptStringBuilder.append("	model = MLPClassifier(" + params_without_optimizer + ",solver=\'"
						+ Optimizer.LBFGS_VALUE + "\')\n");
				pythonScriptStringBuilder.append("else:\n");
				pythonScriptStringBuilder.append("	model = MLPClassifier(" + params_without_optimizer + ",solver=\'"
						+ Optimizer.ADAM + "\')\n");
			} else {
				pythonScriptStringBuilder.append("model = MLPClassifier(" + params + ")\n");
			}
			if (!isForSemiSupervised) {
				pythonScriptStringBuilder.append("model = model.fit(X_train,y_train)\n");
				pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
						+ "train_model_nn_mlp_c.pickle', 'wb') as pickle_file:\n");
				pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
			    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

			}
		}
	}

	private void generatePythonKerasTensorFlowDATrainScriptNN_MultilayerPerceptron(String path_str,
			DATrainAction action, StringBuilder pythonScriptStringBuilder) {
		if (action.getDataAnalytics().getPreprocess_feature_scaler() == Preprocess_feature_scaler.OFF) {
			System.err.println(
					"SEVERE WARNING: For the Neural Network (Multi-Layer Perceptron) model, normalization/standardization of the numerical features is highly recommneded.\n If you have any numerical features, try using the preprocess_feature_scaling parameter in the data analytics section of your model, e.g., preprocess_feature_scaling STANDARDIZATION_Z_SCORE_NORMALIZATION or you may set automl to ON: automl ON. AuoML will take care of that automatically.\n");
		}
		int no_hidden_layers = 0;
		boolean hidden_layers_size_given = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getHidden_layer_sizes() == null) {
			System.err.println(
					"WARNING: In the neural network multilayer perceptron model, parameter hidden_layer_sizes, which may specify the number of hidden layers and the size of each hidden layer through a tuple, where the ith element represents the number of neurons in the ith hidden layer, is not present.\n Thus, only one hidden layer with the size of the mean of the input layer size and the output layer size will be considered.\n");
			no_hidden_layers = 1;
			pythonScriptStringBuilder.append("hidden_layers_size_not_given = True\n");
		} else {
			pythonScriptStringBuilder.append("hidden_layers_size_not_given = False\n");
			no_hidden_layers = ((NN_MultilayerPerceptron) action.getDataAnalytics().getModelAlgorithm())
					.getHidden_layers().size();
			hidden_layers_size_given = true;
		}

		boolean same_activation_all_hidden_layers = false;
		boolean one_activation_per_hidden_layer = false;

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getHidden_layers_activation_functions() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
					.getValue() != Activation.NOT_SET_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameters activation and hidden_layers_activation_functions cannot be specified simultaneously. Remove one of them. If you want to use the same activation for all hidden layers, use the activation parameter. Otherwise, use the hidden_layers_activation_functions parameter.\n In the latter case, you should specifiy them through a tuple, where the ith element represents the activation function for the ith hidden layer.\n Currently, your choices will be ignored. Relu will be used by default for all hidden layers.\n ");
				same_activation_all_hidden_layers = true;
			} else {
				if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivations()
						.size() != no_hidden_layers) {
					System.err.println(
							"WARNING: In the neural network multilayer perceptron model, parameter hidden_layers_activation_functions implies a different number of hidden layers than parameter hidden_layer_sizes.\n Either the hidden_layer_sizes parameter does not exist or the size of its tuple is not the same as the size of the tuple of hidden_layers_activation_functions!\n");
					same_activation_all_hidden_layers = true;
				} else {
					for (int i = 0; i < ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getActivations().size(); i++) {
						if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivations()
								.get(i).getValue() == Activation.NOT_SET_VALUE) {
							((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivations()
									.set(i, Activation.RELU);
							System.err.println(
									"WARNING: In the neural network multilayer perceptron model, parameter hidden_layers_activation_functions includes a NOT_SET activation function, which will be automatically set to RELU.\n");
						} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getActivations().get(i).getValue() == Activation.RELU_VALUE
								&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getValue() == Activation.SIGMOID_VALUE
								&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getValue() == Activation.SOFTMAX_VALUE
								&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getValue() == Activation.SOFTPLUS_VALUE
								&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getValue() == Activation.SOFTSIGN_VALUE
								&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getValue() == Activation.TANH_VALUE
								&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getValue() == Activation.SELU_VALUE
								&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getValue() == Activation.ELU_VALUE
								&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getValue() == Activation.EXPONENTIAL_VALUE) {
							((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivations()
									.set(i, Activation.RELU);
							System.err.println(
									"WARNING: In the neural network multilayer perceptron model, parameter hidden_layers_activation_functions includes an invalid activation function, which will be automatically set to RELU. See the API documentation for more information on the valid choices: https://keras.io/api/layers/activations/.\n");
						}
					}
					one_activation_per_hidden_layer = true;
				}
			}
		} else {
			same_activation_all_hidden_layers = true;
		}

		String activation_function = "";
		if (same_activation_all_hidden_layers) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
					.getValue() != Activation.NOT_SET_VALUE) {
				if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
						.getValue() != Activation.RELU_VALUE
						|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
								.getValue() != Activation.SIGMOID_VALUE
						|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
								.getValue() != Activation.SOFTMAX_VALUE
						|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
								.getValue() != Activation.SOFTPLUS_VALUE
						|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
								.getValue() != Activation.SOFTSIGN_VALUE
						|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
								.getValue() != Activation.TANH_VALUE
						|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
								.getValue() != Activation.SELU_VALUE
						|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
								.getValue() != Activation.ELU_VALUE
						|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getActivation()
								.getValue() != Activation.EXPONENTIAL_VALUE) {

					System.err.println(
							"WARNING: In the neural network multilayer perceptron model, parameter activation does not specify a valid activation function for the selected DA/ML library (keras-tensorflow).\n Thus, the default relu activation function will be used. See the API documentation for more information: https://keras.io/api/layers/activations/. \n");
					activation_function = Activation.RELU.getLiteral();
				} else {
					activation_function = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getActivation().getLiteral();
				}
			} else {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter activation is not present. Thus, the default relu activation function will be used.\n");
				activation_function = Activation.RELU.getLiteral();
			}
		}

		String optimizer = "";
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
				.getValue() != Optimizer.NOT_SET_VALUE) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.RMSPROP_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.ADAM_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.ADADELTA_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.ADAGRAD_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.ADAMAX_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.NADAM_VALUE
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
							.getValue() != Optimizer.FTRL_VALUE) {

				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter optimizer does not specify a valid optimizer for the selected DA/ML library (keras-tensorflow).\n Thus, the default adam optimizer will be used. See the API documentation for more information: https://keras.io/api/optimizers/. \n");
				optimizer = first_to_upper(Optimizer.ADAM.getLiteral());
			} else {
				if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
						.getValue() == Optimizer.SGD_VALUE) {
					optimizer = Optimizer.SGD.getLiteral().toUpperCase();
				} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
						.getValue() == Optimizer.ADAM_VALUE) {
					optimizer = first_to_upper(Optimizer.ADAM.getLiteral());
				} else {
					optimizer = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getOptimizer().getLiteral();
				}
			}
		} else {
			System.err.println(
					"WARNING: In the neural network multilayer perceptron model, parameter optimizer is not present. Thus, the default adam optimizer will be used.\n");
			optimizer = first_to_upper(Optimizer.ADAM.getLiteral());
		}

		double alpha = 0;
		boolean alpha_given = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getAlpha() != null) {
			alpha = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getAlpha()
					.getDoubleValue();
			alpha_given = true;
		}

		int batch_size = 0;
		boolean batch_size_not_given = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBatch_size() != null) {
			batch_size = (int) ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getBatch_size().getIntValue();
		} else {
			batch_size_not_given = true;
			System.err.println(
					"WARNING: In the neural network multilayer perceptron model, parameter batch_size is not present. Thus, the default value min(200, no_of_samples) will be used.\n");
		}

		String learning_rate_mode = "";
		boolean learning_rate_mode_given = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getLearning_rate_mode()
				.getValue() != Learning_rate_mode.NOT_SET_VALUE) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getLearning_rate_mode()
					.getValue() != Learning_rate_mode.EXPONENTIAL_DECAY_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getLearning_rate_mode().getValue() != Learning_rate_mode.PIECEWISE_CONSTANT_DECAY_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getLearning_rate_mode().getValue() != Learning_rate_mode.POLYNOMIAL_DECAY_VALUE
					&& ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
							.getLearning_rate_mode().getValue() != Learning_rate_mode.INVERSE_TIME_DECAY_VALUE) {
				System.err.println(
						"WARNING: The chosen library for DA/ML, i.e., keras-tensorflow, only acceptes one of the following choices for the learning_rate_mode (lr_schedule): ExponentialDecay, PiecewiseConstantDecay, PolynomialDecay or InverseTimeDecay.\n However, the chosen one is not supported. Please see the API doc for more information on this: https://keras.io/api/optimizers/learning_rate_schedules/.\n");
			}
			learning_rate_mode = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getLearning_rate_mode().getLiteral();
			learning_rate_mode_given = true;
		}

		String learning_rate_init = "";
		boolean learning_rate_init_given = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getLearning_rate_init() != null) {
			learning_rate_init = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getLearning_rate_init().toString();
			learning_rate_init_given = true;
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getPower_t() != null) {
			System.err.println(
					"WARNING: The chosen library for DA/ML, i.e., keras-tensorflow, does not accept parameter power_t. You may use the power parameter (not power_t) for the PolynomialDecay learning rate schedule.\n See the API doc for more information: https://keras.io/api/optimizers/learning_rate_schedules/polynomial_decay/.\n");
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMax_iter() != null) {
			System.err.println(
					"WARNING: The chosen library for DA/ML, i.e., keras-tensorflow, does not accept parameter max_iter. Use epochs instead. See the API doc for more information: https://keras.io/api/models/sequential/\n");
		}

		String shuffle_str = "";
		boolean shuffle_given = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getShuffle() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getShuffle().isBoolValue()
					&& action.getDataAnalytics().getSequential().getValue() == Sequential.TRUE_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter shuffle is set to true. However, in the data analytics section of the model, it is mentioned that the data are sequential (i.e., sequential is set to true).\n It is not allowed to shuffle time series or any kind of sequential data, since the order of samples does matter! Thus, the shuffle parameter will be ignored.\n");
			} else {
				shuffle_str = first_to_upper(
						String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getShuffle().isBoolValue()));
				shuffle_given = true;
			}
		}

		int random_state = 0;
		boolean random_state_given = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getRandom_state() != null) {
			random_state = (int) ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getRandom_state().getIntValue();
			random_state_given = true;
		}

		boolean tol_given = false;
		String tol_str = "";
		// The tolerance (tol) for the early stopping is called "min_delta" in Keras,
		// see: https://keras.io/api/callbacks/early_stopping/
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getTol() != null) {
			if ((((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getEarly_stopping() == null)
					|| !(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEarly_stopping()
							.isBoolValue())) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter tol is present. However, the early_stopping parameter either does not exist or is False. Please either set early_stopping to True or remove tol.\n");
			} else {
				tol_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getTol().getDoubleValue());
				tol_given = true;
			}
		}

		boolean verbose_given = false;
		boolean verbose_bool = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getVerbose() != null) {
			verbose_bool = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getVerbose()
					.isBoolValue();
			verbose_given = true;
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getWarm_start() != null) {
			System.err.println(
					"WARNING: The chosen library for DA/ML, i.e., keras-tensorflow, does not currently accept parameter warm_start. See the API documentation: https://keras.io/api/\n. Please either remove this or try using the scikit-learn library via the @dalib annotation.\n");
		}

		boolean momentum_given = false;
		String momentum_str = "";
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMomentum() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter momentum is present. However, that is only supported for the SGD optimizer/solver in the chosen library for DA/ML, i.e., keras-tensorflow. Thus, it will be ignored.\n See the API doc for more information: https://keras.io/api/optimizers/sgd/.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMomentum()
					.getDoubleValue() < 0) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter momentum is present. However, its value may not be negative in the chosen library for DA/ML, i.e., keras-tensorflow. Thus, it will be ignored.\n See the API doc for more information: https://keras.io/api/optimizers/sgd/.\n");
			} else {
				momentum_str = String
						.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getMomentum().getDoubleValue());
				momentum_given = true;
			}
		}

		boolean nesterovs_momentum_given = false;
		String nesterovs_momentum_str = "";
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getNesterovs_momentum() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.SGD_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter nesterovs_momentum is present. However, that is only supported for the SGD optimizer/solver in the chosen library for DA/ML, i.e., keras-tensorflow. Thus, it will be ignored.\n See the API doc for more information: https://keras.io/api/optimizers/sgd/.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMomentum()
					.getDoubleValue() < 0) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter nesterovs_momentum is present. However, parameter momentum has a negative value. This is not allowed in the chosen library for DA/ML, i.e., keras-tensorflow. Thus, it will be ignored.\n See the API doc for more information: https://keras.io/api/optimizers/sgd/.\n");
			} else {
				nesterovs_momentum_str = first_to_upper(
						String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getNesterovs_momentum().isBoolValue()));
				nesterovs_momentum_given = true;
			}
		}

		boolean early_stopping_given = false;
		boolean early_stopping_boolean = false;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEarly_stopping() != null) {
			early_stopping_boolean = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getEarly_stopping().isBoolValue();
			early_stopping_given = true;
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
				.getValidation_fraction() != null) {
			System.err.println(
					"WARNING: The chosen library for DA/ML, i.e., keras-tensorflow, does not currently accept parameter validation_fraction. See the API documentation: https://keras.io/api/\n. Please either remove this or try using the scikit-learn library via the @dalib annotation.\n");
		}

		boolean beta_1_given = false;
		String beta_1_str = "";
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_1() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter beta_1 is only supported for the adam optimizer/solver in the chosen library for DA/ML (i.e., keras-tensorflow). Thus, it will be ignored.\n Please see the API doc for more information: https://keras.io/api/optimizers/adam/.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_1()
					.getDoubleValue() < 0
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_1()
							.getDoubleValue() >= 1) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter beta_1 is present. However, its provided value is not in [0, 1)! Thus, it will be ignored.\n Please see the API doc for more information: https://keras.io/api/optimizers/adam/.\n");
			} else {
				beta_1_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getBeta_1().getDoubleValue());
				beta_1_given = true;
			}
		}

		boolean beta_2_given = false;
		String beta_2_str = "";
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_2() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter beta_2 is only supported for the adam optimizer/solver in the chosen library for DA/ML (i.e., keras-tensorflow). Thus, it will be ignored.\n Please see the API doc for more information: https://keras.io/api/optimizers/adam/.\n");
			} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_2()
					.getDoubleValue() < 0
					|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getBeta_2()
							.getDoubleValue() >= 1) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter beta_2 is present. However, its provided value is not in [0, 1)! Thus, it will be ignored.\n Please see the API doc for more information: https://keras.io/api/optimizers/adam/.\n");
			} else {
				beta_2_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getBeta_2().getDoubleValue());
				beta_2_given = true;
			}
		}

		boolean epsilon_given = false;
		String epsilon_str = "";
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEpsilon() != null) {
			if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getOptimizer()
					.getValue() != Optimizer.ADAM_VALUE) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter epsilon is only supported for the adam optimizer/solver in the chosen library for DA/ML (i.e., keras-tensorflow). Thus, it will be ignored.\n Please see the API doc for more information: https://keras.io/api/optimizers/adam/.\n");
			} else {
				epsilon_str = String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
						.getEpsilon().getDoubleValue());
				epsilon_given = true;
			}
		}

		boolean n_iter_no_change_given = false;
		String n_iter_no_change_str = "";
		// The n_iter_no_change parameter is called "patience" for the early stopping in
		// Keras, see: https://keras.io/api/callbacks/early_stopping/
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getN_iter_no_change() != null) {
			if ((((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
					.getEarly_stopping() == null)
					|| !(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEarly_stopping()
							.isBoolValue())) {
				System.err.println(
						"WARNING: In the neural network multilayer perceptron model, parameter n_iter_no_change is present. However, the early_stopping parameter either does not exist or is False. Please either set early_stopping to True or remove n_iter_no_change.\n");
			} else {
				n_iter_no_change_str = String
						.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getN_iter_no_change().getIntValue());
				n_iter_no_change_given = true;
			}
		}

		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getMax_fun() != null) {
			System.err.println(
					"WARNING: The chosen library for DA/ML, i.e., keras-tensorflow, does not currently accept parameter max_fun. See the API documentation: https://keras.io/api/\n. Please either remove this or try using the scikit-learn library via the @dalib annotation.\n");
		}

		if (random_state_given) {
			pythonScriptStringBuilder.append("from numpy.random import seed\n");
			pythonScriptStringBuilder.append("seed(" + String.valueOf(random_state) + ")\n");
			pythonScriptStringBuilder.append("from tensorflow import set_random_seed\n");
			pythonScriptStringBuilder.append("set_random_seed(" + String.valueOf(random_state) + ")\n");
		}
		pythonScriptStringBuilder.append("import pandas as pd\n");
		pythonScriptStringBuilder.append("import numpy as np\n");
		pythonScriptStringBuilder.append("from sklearn.preprocessing import LabelEncoder\n");
		pythonScriptStringBuilder.append("import re\n\n");

		pythonScriptStringBuilder.append("import logging, os, sys\n");
		pythonScriptStringBuilder.append("logging.disable(logging.WARNING)\n");
		pythonScriptStringBuilder.append("os.environ[\"TF_CPP_MIN_LOG_LEVEL\"] = \"3\"\n");
		pythonScriptStringBuilder.append("stderr = sys.stderr\n");
		pythonScriptStringBuilder.append("sys.stderr = open(os.devnull, 'w')\n");
		pythonScriptStringBuilder.append("import tensorflow as tf\n");
		pythonScriptStringBuilder.append("from tensorflow import keras\n");
		pythonScriptStringBuilder.append("from tensorflow.keras import layers\n");
		pythonScriptStringBuilder.append("from tensorflow.keras.optimizers import " + optimizer + "\n");
		pythonScriptStringBuilder.append("sys.stderr = stderr\n\n");

		pythonScriptStringBuilder.append("#Setting the correct data type, i.e., float32 for numerical features\n");
		pythonScriptStringBuilder.append("p1 = re.compile(r'\\d+(\\.\\d+)?$')\n");
		pythonScriptStringBuilder
				.append("tmp = X_train.iloc[:,1:].apply(lambda x: True if re.match(p1,str(x[0]))!=None else False)\n");
		pythonScriptStringBuilder.append("numeric_features_list = tmp.index[tmp]\n");
		pythonScriptStringBuilder
				.append("X_train[numeric_features_list] = X_train[numeric_features_list].astype(\'float32\')\n\n");

		pythonScriptStringBuilder.append("#Preparing the class labels\n");
		pythonScriptStringBuilder.append("le = LabelEncoder()\n");
		pythonScriptStringBuilder.append("le.fit(y_train)\n");
		pythonScriptStringBuilder.append("p2 = re.compile(r'[\\d+(\\.\\d+)]')\n");
		pythonScriptStringBuilder.append("p3 = re.compile(r'\\d+(\\.\\d+)')\n");
		pythonScriptStringBuilder.append("if(re.match(p2,str(y_train[0:1]))!=None):\n");
		pythonScriptStringBuilder.append("	y_train = y_train.apply(lambda x: le.transform([x]))\n");
		pythonScriptStringBuilder.append("if(re.match(p3,str(y_train[0:1]))!=None):\n");
		pythonScriptStringBuilder.append("	y_train = y_train.apply(lambda x: le.transform(x))\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "nn_nlp_y_train_le.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(le, pickle_file)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "nn_nlp_y_train_categorical.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(y_train, pickle_file)\n\n");

		pythonScriptStringBuilder.append("#Creating the model\n");
		if (alpha_given) {
			pythonScriptStringBuilder.append("from keras.regularizers import l2\n");
		}

		pythonScriptStringBuilder.append("model = keras.Sequential()\n");
		pythonScriptStringBuilder.append("model.add(layers.Input(shape=(X_train.shape[1],)))\n");

		if (hidden_layers_size_given) {
			for (int i = 0; i < no_hidden_layers; i++) {
				if (same_activation_all_hidden_layers) {
					if (alpha_given) {
						pythonScriptStringBuilder.append("model.add(layers.Dense("
								+ String.valueOf(
										((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getHidden_layers().get(i).getIntValue())
								+ ", activation='" + activation_function + "', kernel_regularizer=l2(" + alpha
								+ ")))\n");
					} else {
						pythonScriptStringBuilder.append("model.add(layers.Dense("
								+ String.valueOf(
										((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getHidden_layers().get(i).getIntValue())
								+ ", activation='" + activation_function + "'))\n");
					}
				} else if (one_activation_per_hidden_layer) {
					if (alpha_given) {
						pythonScriptStringBuilder.append("model.add(layers.Dense("
								+ String.valueOf(
										((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getHidden_layers().get(i).getIntValue())
								+ ", activation='"
								+ ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getLiteral()
								+ "', kernel_regularizer=l2(" + alpha + ")))\n");
					} else {
						pythonScriptStringBuilder.append("model.add(layers.Dense("
								+ String.valueOf(
										((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getHidden_layers().get(i).getIntValue())
								+ ", activation='"
								+ ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().get(i).getLiteral()
								+ "'))\n");
					}
				}
			}
		} else {
			pythonScriptStringBuilder.append("import math\n");
			pythonScriptStringBuilder
					.append("hidden_layer_size=math.ceil(float((X_train.shape[1])+(len(y_train)))/2.0)\n");
			if (same_activation_all_hidden_layers) {
				if (alpha_given) {
					pythonScriptStringBuilder.append("model.add(layers.Dense(hidden_layer_size, activation='"
							+ activation_function + "', kernel_regularizer=l2(" + alpha + ")))\n");
				} else {
					pythonScriptStringBuilder.append(
							"model.add(layers.Dense(hidden_layer_size, activation='" + activation_function + "'))\n");
				}
			} else if (one_activation_per_hidden_layer) {
				if (alpha_given) {
					pythonScriptStringBuilder.append("model.add(layers.Dense(hidden_layer_size, activation='"
							+ ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
									.getActivations().get(0).getLiteral()
							+ "', kernel_regularizer=l2(" + alpha + ")))\n");
				} else {
					pythonScriptStringBuilder.append("model.add(layers.Dense(hidden_layer_size, activation='"
							+ ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
									.getActivations().get(0).getLiteral()
							+ "'))\n");
				}
			}
		}

		pythonScriptStringBuilder.append("model.add(layers.Dense(len(y_train)))\n\n");

		pythonScriptStringBuilder.append("#Compiling the model\n");
		String loss_function = "";
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getLoss()
				.getValue() == Loss.NOT_SET_VALUE) {
			loss_function = Loss.SPARSE_CATEGORICAL_CROSSENTROPY.getLiteral();
		} else {
			loss_function = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getLoss()
					.getLiteral();
		}

		if (learning_rate_mode_given) {
			pythonScriptStringBuilder
					.append("from tensorflow.keras.optimizers.schedules import " + learning_rate_mode + "\n");
		}
		pythonScriptStringBuilder.append("\n");
		pythonScriptStringBuilder.append("model.compile(\n");

		pythonScriptStringBuilder.append("  optimizer=" + optimizer + "(\n");
		if (learning_rate_mode_given) {
			pythonScriptStringBuilder.append("    learning_rate=" + learning_rate_mode + "(\n");
			if (learning_rate_init_given) {
				pythonScriptStringBuilder.append("        initial_learning_rate=" + learning_rate_init + ")),\n");
			} else {
				pythonScriptStringBuilder.append("    ),\n");
			}
		} else {
			if (learning_rate_init_given) {
				pythonScriptStringBuilder.append("    learning_rate=" + learning_rate_init + ",\n");
			}
		}
		if (momentum_given) {
			pythonScriptStringBuilder.append("    momentum=" + momentum_str + ",\n");
		}
		if (nesterovs_momentum_given) {
			pythonScriptStringBuilder.append("    nesterov=" + nesterovs_momentum_str + ",\n");
		}
		if (beta_1_given) {
			pythonScriptStringBuilder.append("    beta_1=" + beta_1_str + ",\n");
		}
		if (beta_2_given) {
			pythonScriptStringBuilder.append("    beta_2=" + beta_2_str + ",\n");
		}
		if (epsilon_given) {
			pythonScriptStringBuilder.append("    epsilon=" + epsilon_str + ",\n");
		}
		pythonScriptStringBuilder.append("  ),\n");

		pythonScriptStringBuilder.append("  loss=keras.losses." + loss_function + "(),\n");
		pythonScriptStringBuilder.append("  metrics=['accuracy'],\n");
		pythonScriptStringBuilder.append(")\n\n");

		int no_epochs = 0;
		if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEpochs() == null) {
			System.err.println(
					"WARNING: In the neural network multilayer perceptron model, parameter epochs is not present. Thus, the default value 200 will be used.\n");
			no_epochs = 200;
		} else {
			no_epochs = (int) ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm())).getEpochs()
					.getIntValue();
		}

		pythonScriptStringBuilder.append("#Training the model\n");
		if (early_stopping_given && early_stopping_boolean) {
			pythonScriptStringBuilder.append("callback = tf.keras.callbacks.EarlyStopping(monitor='loss',");
			if (tol_given) {
				pythonScriptStringBuilder.append("min_delta=" + tol_str + ",");
			}
			if (n_iter_no_change_given) {
				pythonScriptStringBuilder.append("patience=" + n_iter_no_change_str + ",");
			}
			pythonScriptStringBuilder.append(")\n");
		}
		pythonScriptStringBuilder.append("model.fit(\n");
		pythonScriptStringBuilder.append("  X_train,\n");
		pythonScriptStringBuilder.append("  y_train,\n");
		pythonScriptStringBuilder.append("  epochs=" + no_epochs + ",\n");
		if (batch_size_not_given) {
			pythonScriptStringBuilder.append("  batch_size=min(200,X_train.shape[0]),\n");
		} else {
			pythonScriptStringBuilder.append("  batch_size=" + batch_size + ",\n");
		}
		if (early_stopping_given && early_stopping_boolean) {
			pythonScriptStringBuilder.append("  callbacks=[callback],\n");
		}
		if (verbose_given) {
			if (verbose_bool) {
				pythonScriptStringBuilder.append("  verbose=2,\n");
			} else {
				pythonScriptStringBuilder.append("  verbose=0,\n");
			}
		} else {
			pythonScriptStringBuilder.append("  verbose=0,\n");
		}
		if (shuffle_given) {
			pythonScriptStringBuilder.append("  shuffle=" + shuffle_str + ",\n");
		}
		pythonScriptStringBuilder.append(")\n\n");

		pythonScriptStringBuilder.append(
				"model.save_weights('" + path_str + "/python-scripts/pickles/" + "train_model_nn_mlp_weights.h5')\n\n");
		
	    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

	}


	private void generatePythonDATrainScriptKMeans(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1,
		
																	// https://scikit-learn.org/stable/modules/generated/sklearn.cluster.KMeans.html
			generatePythonScikitLearnDATrainScriptKMeans(path_str, action, pythonScriptStringBuilder);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptKMeans(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
			System.out.println(
					"INFO: The chosen algorithm for unsupervised ML in the data analytics section of the model is K-Means. However, if you have a dataset with more than 10 thousands samples (data instances), you may consider using MiniBatchKMeans that is probably much faster.\n");
		}

		String n_clusters_str = "";
		String init_str = "";
		String n_init_str = "";
		String max_iter_str = "";
		String tol_str = "";
		String verbose_str = "";
		String random_state_str = "";
		String copy_x_str = "";
		String n_jobs_str = "";
		String algorithm_str = "";
		String params = "";
		boolean flag = false;

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getN_clusters() != null) {
			if (flag) {
				params += ",";
			}
			n_clusters_str = String
					.valueOf(((KMeans) action.getDataAnalytics().getModelAlgorithm()).getN_clusters().getIntValue());
			params += "n_clusters=" + n_clusters_str;
			flag = true;

		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_enum() != null
				&& ((KMeans) action.getDataAnalytics().getModelAlgorithm())
						.getInit_enum() != KMeans_init_enum.NOT_SET) {
			if (flag) {
				params += ",";
			}
			init_str = ((KMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_enum().getLiteral();
			params += "init=" + init_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_str() != null) {
			if (flag) {
				params += ",";
			}
			init_str = ((KMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_str();
			params += "init=" + init_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getN_init() != null) {
			if (flag) {
				params += ",";
			}
			n_init_str = String
					.valueOf(((KMeans) action.getDataAnalytics().getModelAlgorithm()).getN_init().getIntValue());
			params += "n_init=" + n_init_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getMax_iter() != null) {
			if (flag) {
				params += ",";
			}
			max_iter_str = String
					.valueOf(((KMeans) action.getDataAnalytics().getModelAlgorithm()).getMax_iter().getIntValue());
			params += "max_iter=" + max_iter_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getTol() != null) {
			if (flag) {
				params += ",";
			}
			tol_str = String
					.valueOf(((KMeans) action.getDataAnalytics().getModelAlgorithm()).getTol().getDoubleValue());
			params += "tol=" + tol_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getVerbose() != null) {
			if (flag) {
				params += ",";
			}
			verbose_str = String
					.valueOf(((KMeans) action.getDataAnalytics().getModelAlgorithm()).getVerbose().getIntValue());
			params += "verbose=" + verbose_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getRandom_state() != null) {
			if (flag) {
				params += ",";
			}
			random_state_str = String
					.valueOf(((KMeans) action.getDataAnalytics().getModelAlgorithm()).getRandom_state().getIntValue());
			params += "random_state=" + random_state_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getCopy_x() != null) {
			if (flag) {
				params += ",";
			}
			copy_x_str = first_to_upper(
					String.valueOf(((KMeans) action.getDataAnalytics().getModelAlgorithm()).getCopy_x().isBoolValue()));
			params += "copy_x=" + copy_x_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getN_jobs() != null) {
			if (flag) {
				params += ",";
			}
			n_jobs_str = String
					.valueOf(((KMeans) action.getDataAnalytics().getModelAlgorithm()).getN_jobs().getIntValue());
			params += "n_jobs=" + n_jobs_str;
			flag = true;
		}

		if (((KMeans) action.getDataAnalytics().getModelAlgorithm()).getAlgorithm() != null
				&& ((KMeans) action.getDataAnalytics().getModelAlgorithm())
						.getAlgorithm() != KMeans_algorithm_enum.NOT_SET) {
			if (flag) {
				params += ",";
			}
			algorithm_str = ((KMeans) action.getDataAnalytics().getModelAlgorithm()).getAlgorithm().getLiteral();
			params += "algorithm=" + algorithm_str;
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.cluster import KMeans\n");
		pythonScriptStringBuilder.append("k_means_clustering = KMeans(" + params + ")\n");
		pythonScriptStringBuilder.append("k_means_clustering = k_means_clustering.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_k_means_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(k_means_clustering, pickle_file)\n\n");
	        pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

	}

	private void generatePythonDATrainScriptMiniBatchKMeans(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2,
																	// https://scikit-learn.org/stable/modules/generated/sklearn.cluster.MiniBatchKMeans.html
			generatePythonScikitLearnDATrainScriptMiniBatchKMeans(path_str, action, pythonScriptStringBuilder);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptMiniBatchKMeans(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		String n_clusters_str = "";
		String init_str = "";
		String max_iter_str = "";
		String batch_size_str = "";
		String verbose_str = "";
		String compute_labels_str = "";
		String random_state_str = "";
		String tol_str = "";
		String max_no_improvement_str = "";
		String init_size_str = "";
		String n_init_str = "";
		String reassignment_ratio_str = "";
		String params = "";
		boolean flag = false;

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getN_clusters() != null) {
			if (flag) {
				params += ",";
			}
			n_clusters_str = String.valueOf(
					((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getN_clusters().getIntValue());
			params += "n_clusters=" + n_clusters_str;
			flag = true;

		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_enum() != null
				&& ((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm())
						.getInit_enum() != KMeans_init_enum.NOT_SET) {
			if (flag) {
				params += ",";
			}
			init_str = ((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_enum().getLiteral();
			params += "init=" + init_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_str() != null) {
			if (flag) {
				params += ",";
			}
			init_str = ((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_str();
			params += "init=" + init_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getMax_iter() != null) {
			if (flag) {
				params += ",";
			}
			max_iter_str = String.valueOf(
					((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getMax_iter().getIntValue());
			params += "max_iter=" + max_iter_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getBatch_size() != null) {
			if (flag) {
				params += ",";
			}
			batch_size_str = String.valueOf(
					((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getBatch_size().getIntValue());
			params += "batch_size=" + batch_size_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getVerbose() != null) {
			if (flag) {
				params += ",";
			}
			verbose_str = String.valueOf(
					((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getVerbose().getIntValue());
			params += "verbose=" + verbose_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getCompute_labels() != null) {
			if (flag) {
				params += ",";
			}
			compute_labels_str = first_to_upper(
					String.valueOf(((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getCompute_labels()
							.isBoolValue()));
			params += "compute_labels=" + compute_labels_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getRandom_state() != null) {
			if (flag) {
				params += ",";
			}
			random_state_str = String.valueOf(
					((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getRandom_state().getIntValue());
			params += "random_state=" + random_state_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getTol() != null) {
			if (flag) {
				params += ",";
			}
			tol_str = String.valueOf(
					((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getTol().getDoubleValue());
			params += "tol=" + tol_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getMax_no_improvement() != null) {
			if (flag) {
				params += ",";
			}
			max_no_improvement_str = String.valueOf(((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm())
					.getMax_no_improvement().getIntValue());
			params += "max_no_improvement=" + max_no_improvement_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_size() != null) {
			if (flag) {
				params += ",";
			}
			init_size_str = String.valueOf(
					((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getInit_size().getIntValue());
			params += "init_size=" + init_size_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getN_init() != null) {
			if (flag) {
				params += ",";
			}
			n_init_str = String.valueOf(
					((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getN_init().getIntValue());
			params += "n_init=" + n_init_str;
			flag = true;
		}

		if (((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm()).getReassignment_ratio() != null) {
			if (flag) {
				params += ",";
			}
			reassignment_ratio_str = String.valueOf(((MiniBatchKMeans) action.getDataAnalytics().getModelAlgorithm())
					.getReassignment_ratio().getDoubleValue());
			params += "reassignment_ratio=" + reassignment_ratio_str;
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.cluster import MiniBatchKMeans\n");
		pythonScriptStringBuilder.append("mini_batch_k_means_clustering = MiniBatchKMeans(" + params + ")\n");
		pythonScriptStringBuilder
				.append("mini_batch_k_means_clustering = mini_batch_k_means_clustering.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_mini_batch_k_means_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(mini_batch_k_means_clustering, pickle_file)\n\n");
	    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

	}

	private void generatePythonDATrainScriptDBSCAN(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2,
																
	// https://scikit-learn.org/stable/modules/generated/sklearn.cluster.DBSCAN.html
			generatePythonScikitLearnDATrainScriptDBSCAN(path_str, action, pythonScriptStringBuilder);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptDBSCAN(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		String eps_str = "";
		String min_samples_str = "";
		String metric_str = "";
		String metric_params_str = "";
		String algorithm_str = "";
		String leaf_size_str = "";
		String p_str = "";
		String n_jobs_str = "";
		String params = "";
		boolean flag = false;

		if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getEps() != null) {
			if (flag) {
				params += ",";
			}
			eps_str = String
					.valueOf(((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getEps().getDoubleValue());
			params += "eps=" + eps_str;
			flag = true;

		} else {
			if (action.getDataAnalytics().getAutoML() == AutoML.ON) {
				System.out.println(
						"WARNING: The parameter eps in the DBSCAN unsupervised ML method is missing. However, this is the most important DBSCAN parameter to choose appropriately for your data set and distance function.\n See the API documentation for more information: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.DBSCAN.html");
			}
		}

		if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getMin_samples() != null) {
			if (flag) {
				params += ",";
			}
			min_samples_str = String
					.valueOf(((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getMin_samples().getIntValue());
			params += "min_samples=" + min_samples_str;
			flag = true;
		}

		if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getMetric() != null) {
			if (flag) {
				params += ",";
			}
			metric_str = ((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getMetric();
			params += "metric=" + metric_str;
			flag = true;
		}

		if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getMetric_params() != null) {
			if (flag) {
				params += ",";
			}
			metric_params_str = ((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getMetric_params();
			params += "metric_params=" + metric_params_str;
			flag = true;
		}

		if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getAlgorithm() != null
				&& ((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getAlgorithm() != KNN_Algorithms.NOT_SET) {
			if (flag) {
				params += ",";
			}
			algorithm_str = ((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getAlgorithm().getLiteral();
			params += "algorithm=" + algorithm_str;
			flag = true;
		}

		if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getLeaf_size() != null) {
			if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getAlgorithm() != KNN_Algorithms.BRUTE) {
				if (flag) {
					params += ",";
				}
				leaf_size_str = String
						.valueOf(((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getLeaf_size().getIntValue());
				params += "leaf_size=" + leaf_size_str;
				flag = true;
			} else {
				System.err.println(
						"WARNING: The parameter algorithm in the DBSCAN unsupervised ML method is set to the Brute Nearest Neighbor algorithm. However, the parameter leaf_size is also present. Leaf_size can only be used for the BallTree or KDTree algorithms. Thus, the parameter leaf_size will be ignored.\n For more information, see the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.DBSCAN.html");
			}
		}
		if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getP() != null) {
			if (flag) {
				params += ",";
			}
			p_str = String.valueOf(((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getP().getDoubleValue());
			params += "p=" + p_str;
			flag = true;
		}

		if (((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getN_jobs() != null) {
			if (flag) {
				params += ",";
			}
			n_jobs_str = String
					.valueOf(((DBSCAN) action.getDataAnalytics().getModelAlgorithm()).getN_jobs().getIntValue());
			params += "n_jobs=" + n_jobs_str;
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.cluster import DBSCAN\n");
		pythonScriptStringBuilder.append("dbscan_clustering = DBSCAN(" + params + ")\n");
		pythonScriptStringBuilder.append("dbscan_clustering = dbscan_clustering.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_dbscan_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(dbscan_clustering, pickle_file)\n\n");
		pythonScriptStringBuilder.append("y_pred = dbscan_clustering.labels_\n");
	}

	private void generatePythonDATrainScriptSpectralClustering(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2,
																	// https://scikit-learn.org/stable/modules/generated/sklearn.cluster.SpectralClustering.html
			generatePythonScikitLearnDATrainScriptSpectralClustering(path_str, action, pythonScriptStringBuilder);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptSpectralClustering(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		String n_clusters_str = "";
		String eigen_solver_str = "";
		String n_components_str = "";
		String random_state_str = "";
		String n_init_str = "";
		String gamma_str = "";
		String affinity_str = "";
		String n_neighbors_str = "";
		String eigen_tol_str = "";
		String assign_labels_str = "";
		String degree_str = "";
		String coef0_str = "";
		String kernel_params_str = "";
		String n_jobs_str = "";
		String verbose_str = "";
		String params = "";
		boolean flag = false;

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getN_clusters() != null) {
			if (flag) {
				params += ",";
			}
			n_clusters_str = String.valueOf(
					((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getN_clusters().getIntValue());
			params += "n_clusters=" + n_clusters_str;
			flag = true;

		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getEigen_solver() != null
				&& ((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
						.getEigen_solver() != Eigen_Solver.NOT_SET) {

			if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
					.getEigen_solver() == Eigen_Solver.AMG) {
				System.out.println(
						"INFO: In the Spectral Clustering ML model/algorithm, the parameter eigen_solver is set to AMG, which requires the Python package pyamg to be installed. Please make sure it is already installed.\n Moreover, note that AMG can be faster on very large, sparse problems, but may also lead to instabilities.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.SpectralClustering.html\n");
			}

			if (flag) {
				params += ",";
			}
			eigen_solver_str = ((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getEigen_solver()
					.getLiteral();
			params += "eigen_solver=" + eigen_solver_str;
			flag = true;
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getN_components() != null) {
			if (flag) {
				params += ",";
			}
			n_components_str = String.valueOf(((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
					.getN_components().getIntValue());
			params += "n_components=" + n_components_str;
			flag = true;
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getRandom_state() != null) {
			if (flag) {
				params += ",";
			}
			random_state_str = String.valueOf(((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
					.getRandom_state().getIntValue());
			params += "random_state=" + random_state_str;
			flag = true;
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getN_init() != null) {

			if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
					.getAssign_labels() == Laplacian_Embedding_Label_Assign_Strategy.DISCRETIZE) {
				System.err.println(
						"WARNING: In the Spectral Clustering ML model/algorithm, the parameter n_init is present. However, this cannot be used for the discretize strategy for assigning labels in the embedding space, i.e., when the parameter assign_labels is not set to the default kmeans strategy.\n Therefore, the parameter n_init will be ignored. For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.SpectralClustering.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				n_init_str = String.valueOf(
						((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getN_init().getIntValue());
				params += "n_init=" + n_init_str;
				flag = true;
			}
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getGamma() != null) {
			if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAffinity()
					.contains("nearest_neighbors")) {
				System.err.println(
						"WARNING: In the Spectral Clustering ML model/algorithm, the parameter gamma is present. However, this cannot be used for the nearest_neighbors strategy for constructing the affinity matrix, i.e., when the parameter affinity is set to nearest_neighbors.\n Therefore, the parameter gamma will be ignored. For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.SpectralClustering.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				gamma_str = String.valueOf(((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
						.getGamma().getDoubleValue());
				params += "gamma=" + gamma_str;
				flag = true;
			}
		}
		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAffinity() != null) {
			if (flag) {
				params += ",";
			}
			affinity_str = ((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAffinity();
			params += "affinity=" + affinity_str;
			flag = true;
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getN_neighbors() != null) {
			if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAffinity().contains("rbf")) {
				System.err.println(
						"WARNING: In the Spectral Clustering ML model/algorithm, the parameter n_neighbors is present. However, this cannot be used for the Radial Basis Function (RBF) kernel for constructing the affinity matrix, i.e., when the parameter affinity is set to rbf.\n Therefore, the parameter n_neighbors will be ignored. For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.SpectralClustering.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				n_neighbors_str = String.valueOf(((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
						.getN_neighbors().getIntValue());
				params += "n_neighbors=" + n_neighbors_str;
				flag = true;
			}
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getEigen_tol() != null) {
			if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
					.getEigen_solver() == Eigen_Solver.LOBPCG
					|| ((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
							.getEigen_solver() == Eigen_Solver.AMG) {
				System.err.println(
						"WARNING: In the Spectral Clustering ML model/algorithm, the parameter eigen_tol is present. However, this cannot be used for the lobpcg or amg eigen solvers.\n Thus, the parameter eigen_tol will be ignored. For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.SpectralClustering.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				eigen_tol_str = String.valueOf(((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
						.getEigen_tol().getDoubleValue());
				params += "eigen_tol=" + eigen_tol_str;
				flag = true;
			}
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAssign_labels() != null
				&& ((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
						.getAssign_labels() != Laplacian_Embedding_Label_Assign_Strategy.NOT_SET) {
			if (flag) {
				params += ",";
			}
			assign_labels_str = ((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAssign_labels()
					.getLiteral();
			params += "assign_labels=" + assign_labels_str;
			flag = true;
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getDegree() != null) {
			if (!((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAffinity().contains("poly")) {
				System.err.println(
						"WARNING: In the Spectral Clustering ML model/algorithm, the parameter degree is present. However, this is only relevant to the polynomial kernel for constructing the affinity matrix, i.e., when the parameter affinity is set to poly/polynomial.\n Therefore, the parameter degree will be ignored. For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.SpectralClustering.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				degree_str = String.valueOf(((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
						.getDegree().getDoubleValue());
				params += "degree=" + degree_str;
				flag = true;
			}
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getCoef0() != null) {
			if (!((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAffinity().contains("poly")
					&& !((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getAffinity()
							.contains("sigmoid")) {
				System.err.println(
						"WARNING: In the Spectral Clustering ML model/algorithm, the parameter coef0 is present. However, this is only relevant to the polynomial or signoid kernels for constructing the affinity matrix, i.e., when the parameter affinity is set to poly/polynomial or sigmoid.\n Therefore, the parameter coef0 will be ignored. For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.cluster.SpectralClustering.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				coef0_str = String.valueOf(((SpectralClustering) action.getDataAnalytics().getModelAlgorithm())
						.getCoef0().getDoubleValue());
				params += "coef0=" + coef0_str;
				flag = true;
			}
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getKernel_params() != null) {
			if (flag) {
				params += ",";
			}
			kernel_params_str = ((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getKernel_params();
			params += "kernel_params=" + kernel_params_str;
			flag = true;
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getN_jobs() != null) {
			if (flag) {
				params += ",";
			}
			n_jobs_str = String.valueOf(
					((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getN_jobs().getIntValue());
			params += "n_jobs=" + n_jobs_str;
			flag = true;
		}

		if (((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getVerbose() != null) {
			if (flag) {
				params += ",";
			}
			verbose_str = first_to_upper(String.valueOf(
					((SpectralClustering) action.getDataAnalytics().getModelAlgorithm()).getVerbose().isBoolValue()));
			params += "verbose=" + verbose_str;
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.cluster import SpectralClustering\n");
		pythonScriptStringBuilder.append("spectral_clustering = SpectralClustering(" + params + ")\n");
		pythonScriptStringBuilder.append("spectral_clustering = spectral_clustering.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_spectral_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(spectral_clustering, pickle_file)\n\n");
	    pythonScriptStringBuilder.append("y_pred = spectral_clustering.labels_\n");	}
	
	

	private void generatePythonDATrainScriptGaussianMixture(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2,
																	// https://scikit-learn.org/stable/modules/generated/sklearn.mixture.GaussianMixture.html
			generatePythonScikitLearnDATrainScriptGaussianMixture(path_str, action, pythonScriptStringBuilder);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptGaussianMixture(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		String n_components_str = "";
		String covariance_type_str = "";
		String tol_str = "";
		String reg_covar_str = "";
		String max_iter_str = "";
		String n_init_str = "";
		String init_params_str = "";
		String weights_init_str = "";
		String means_init_str = "";
		String precisions_init_str = "";
		String random_state_str = "";
		String warm_start_str = "";
		String verbose_str = "";
		String verbose_interval_str = "";
		String params = "";
		boolean flag = false;

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getN_components() != null) {
			if (flag) {
				params += ",";
			}
			n_components_str = String.valueOf(
					((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getN_components().getIntValue());
			params += "n_components=" + n_components_str;
			flag = true;

		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getCovariance_type() != null
				&& ((GaussianMixture) action.getDataAnalytics().getModelAlgorithm())
						.getCovariance_type() != Covariance_Type.NOT_SET) {
			if (flag) {
				params += ",";
			}
			covariance_type_str = ((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getCovariance_type()
					.getLiteral();
			params += "covariance_type=" + covariance_type_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getTol() != null) {
			if (flag) {
				params += ",";
			}
			tol_str = String.valueOf(
					((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getTol().getDoubleValue());
			params += "tol=" + tol_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getReg_covar() != null) {

			if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getReg_covar().getDoubleValue() < 0) {
				System.err.println(
						"WARNING: In the Gaussian Mixture Clustering model/algorithm, the parameter reg_covar is not allowed to be negative. Thus, it will be ignored.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.mixture.GaussianMixture.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				reg_covar_str = String.valueOf(((GaussianMixture) action.getDataAnalytics().getModelAlgorithm())
						.getReg_covar().getDoubleValue());
				params += "reg_covar=" + reg_covar_str;
				flag = true;
			}
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getMax_iter() != null) {
			if (flag) {
				params += ",";
			}
			max_iter_str = String.valueOf(
					((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getMax_iter().getIntValue());
			params += "max_iter=" + max_iter_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getN_init() != null) {
			if (flag) {
				params += ",";
			}
			n_init_str = String.valueOf(
					((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getN_init().getIntValue());
			params += "n_init=" + n_init_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getInit_params() != null
				&& ((GaussianMixture) action.getDataAnalytics().getModelAlgorithm())
						.getInit_params() != Init_Params_Methods.NOT_SET) {
			if (flag) {
				params += ",";
			}
			init_params_str = ((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getInit_params()
					.getLiteral();
			params += "init_params=" + init_params_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getWeights_init() != null) {
			if (flag) {
				params += ",";
			}
			weights_init_str = ((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getWeights_init();
			params += "weights_init=" + weights_init_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getMeans_init() != null) {
			if (flag) {
				params += ",";
			}
			means_init_str = ((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getMeans_init();
			params += "means_init=" + means_init_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getPrecisions_init() != null) {
			if (flag) {
				params += ",";
			}
			precisions_init_str = ((GaussianMixture) action.getDataAnalytics().getModelAlgorithm())
					.getPrecisions_init();
			params += "precisions_init=" + precisions_init_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getRandom_state() != null) {
			if (flag) {
				params += ",";
			}
			random_state_str = String.valueOf(
					((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getRandom_state().getIntValue());
			params += "random_state=" + random_state_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getWarm_start() != null) {
			if (flag) {
				params += ",";
			}
			warm_start_str = first_to_upper(String.valueOf(
					((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getWarm_start().isBoolValue()));
			params += "warm_start=" + warm_start_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getVerbose() != null) {
			if (flag) {
				params += ",";
			}
			verbose_str = String.valueOf(
					((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getVerbose().getIntValue());
			params += "verbose=" + verbose_str;
			flag = true;
		}

		if (((GaussianMixture) action.getDataAnalytics().getModelAlgorithm()).getVerbose_interval() != null) {
			if (flag) {
				params += ",";
			}
			verbose_interval_str = String.valueOf(((GaussianMixture) action.getDataAnalytics().getModelAlgorithm())
					.getVerbose_interval().getIntValue());
			params += "verbose_interval=" + verbose_interval_str;
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.mixture import GaussianMixture\n");
		pythonScriptStringBuilder.append("gaussian_mixture_clustering = GaussianMixture(" + params + ")\n");
		pythonScriptStringBuilder.append("gaussian_mixture_clustering = gaussian_mixture_clustering.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_gaussian_mixture_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(gaussian_mixture_clustering, pickle_file)\n\n");
	    pythonScriptStringBuilder.append("y_pred = gaussian_mixture_clustering.predict(X_train)\n");

	}

	private void generatePythonDATrainScriptSelfTrainingClassifier(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2,
																	// https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.SelfTrainingClassifier.html#sklearn.semi_supervised.SelfTrainingClassifier
			generatePythonScikitLearnDATrainScriptSelfTrainingClassifier(path_str, action, pythonScriptStringBuilder);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptSelfTrainingClassifier(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {

		ClassifierWithPredictionCertaintyProbabilities base_estimator = null;

		String threshold_str = "";
		String criterion_str = "";
		String k_best_str = "";
		String max_iter_str = "";
		String verbose_str = "";

		String params = "";
		boolean flag = false;

		if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getBase_estimator() != null) {
			base_estimator = ((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm())
					.getBase_estimator();
			if (base_estimator instanceof LinearClassifierLogisticRegression) {
				generatePythonScikitLearnDATrainScriptLinearClassifierLogisticRegression(path_str, action,
						pythonScriptStringBuilder, true);
			} else if (base_estimator instanceof NaiveBayesGaussian) {
				generatePythonScikitLearnDATrainScriptNaiveBayesGaussian(path_str, action, pythonScriptStringBuilder,
						true);
			} else if (base_estimator instanceof NaiveBayesMultinomial) {
				generatePythonScikitLearnDATrainScriptNaiveBayesMultinomial(path_str, action, pythonScriptStringBuilder,
						true);
			} else if (base_estimator instanceof NaiveBayesComplement) {
				generatePythonScikitLearnDATrainScriptNaiveBayesComplement(path_str, action, pythonScriptStringBuilder,
						true);
			} else if (base_estimator instanceof NaiveBayesBernoulli) {
				generatePythonScikitLearnDATrainScriptNaiveBayesBernoulli(path_str, action, pythonScriptStringBuilder,
						true);
			} else if (base_estimator instanceof NaiveBayesCategorical) {
				generatePythonScikitLearnDATrainScriptNaiveBayesCategorical(path_str, action, pythonScriptStringBuilder,
						true);
			} else if (base_estimator instanceof DecisionTreeClassifier) {
				generatePythonScikitLearnDATrainScriptDecisionTreeClassifier(path_str, action,
						pythonScriptStringBuilder, true);
			} else if (base_estimator instanceof NN_MultilayerPerceptron) {
				generatePythonScikitLearnDATrainScriptNN_MultilayerPerceptron(path_str, action,
						pythonScriptStringBuilder, true);
			} else {
				System.err.println(
						"ERROR: The specified base estimator (classifier) in the semi-supervised ML method self training is not supported.\n This should be on eof the supported estimator objects, e.g., the logistic regression classifier, implementing fit and predict_proba in the Scikit-Learn library.\n Please refer to the API doc for more info: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.SelfTrainingClassifier.html#sklearn.semi_supervised.SelfTrainingClassifier\n");
			}
		}

		if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getThreshold() != null) {
			if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm())
					.getCriterion() == Self_Training_Criterion.KBEST) {
				System.err.println(
						"ERROR: In the semi-supervised ML method self training, parameter threshold may only be specified if the criterion parameter is set to the default value threshold, not to the k_best value. Thus, it will be ignored.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.SelfTrainingClassifier.html#sklearn.semi_supervised.SelfTrainingClassifier\n");
			} else if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getThreshold()
					.getDoubleValue() < 0
					|| ((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getThreshold()
							.getDoubleValue() >= 1) {
				System.err.println(
						"ERROR: In the semi-supervised ML method self training, parameter threshold may have values in the interval [0,1). Since this is currently not the case, this parameter will be ignored.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.SelfTrainingClassifier.html#sklearn.semi_supervised.SelfTrainingClassifier\n");
			} else {
				if (flag) {
					params += ",";
				}
				threshold_str = String.valueOf(((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm())
						.getThreshold().getDoubleValue());
				params += "threshold=" + threshold_str;
				flag = true;
			}
		}

		if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getCriterion().equals(null)
				&& action.getDataAnalytics().getAutoML() == AutoML.ON) {
			// TODO: Check for a well-calibrated classifier:
			// https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.SelfTrainingClassifier.html#sklearn.semi_supervised.SelfTrainingClassifier
		}

		if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getCriterion() != null
				&& ((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm())
						.getCriterion() != Self_Training_Criterion.NOT_SET) {
			if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm())
					.getCriterion() == Self_Training_Criterion.THRESHOLD
					&& action.getDataAnalytics().getAutoML() == AutoML.ON) {
				// TODO: Check for a well-calibrated classifier:
				// https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.SelfTrainingClassifier.html#sklearn.semi_supervised.SelfTrainingClassifier
			}
			if (flag) {
				params += ",";
			}
			criterion_str = ((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getCriterion()
					.getLiteral();
			params += "criterion=" + criterion_str;
			flag = true;
		}

		if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getK_best() != null
				&& ((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm())
						.getCriterion() == Self_Training_Criterion.KBEST) {
			if (flag) {
				params += ",";
			}
			k_best_str = String.valueOf(
					((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getK_best().getIntValue());
			params += "k_best=" + k_best_str;
			flag = true;
		}

		if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getMax_iter() != null) {
			if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getMax_iter()
					.getIntValue() < 0) {
				System.err.println(
						"ERROR: In the semi-supervised ML method self training, parameter max_iter may not have a negative value. Since this is currently not the case, this parameter will be ignored.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.SelfTrainingClassifier.html#sklearn.semi_supervised.SelfTrainingClassifier\n");
			} else {
				if (flag) {
					params += ",";
				}
				max_iter_str = String.valueOf(((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm())
						.getMax_iter().getIntValue());
				params += "max_iter=" + max_iter_str;
				flag = true;
			}
		}
		if (((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getVerbose() != null) {
			if (flag) {
				params += ",";
			}
			verbose_str = first_to_upper(
					String.valueOf(((SelfTrainingClassifier) action.getDataAnalytics().getModelAlgorithm()).getVerbose()
							.isBoolValue()));
			params += "verbose=" + verbose_str;
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.semi_supervised import SelfTrainingClassifier\n");
		if (!params.equals(null)) {
			pythonScriptStringBuilder.append("self_training_model = SelfTrainingClassifier(model, " + params + ")\n");
		} else {
			pythonScriptStringBuilder.append("self_training_model = SelfTrainingClassifier(model)\n");
		}
		pythonScriptStringBuilder.append("self_training_model = self_training_model.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_self_training.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(self_training_model, pickle_file)\n\n");
		pythonScriptStringBuilder.append("y_pred = self_training_model.predict(X_test)\n");

	}

	private void generatePythonDATrainScriptLabelPropagation(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2,
																	// https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelPropagation.html
			generatePythonScikitLearnDATrainScriptLabelPropagation(path_str, action, pythonScriptStringBuilder);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptLabelPropagation(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {

		String kernel_str = "";
		String gamma_str = "";
		String n_neighbors_str = "";
		String max_iter_str = "";
		String tol_str = "";
		String n_jobs_str = "";
		String params = "";
		boolean flag = false;

		if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getKernel() != null
				&& ((LabelPropagation) action.getDataAnalytics().getModelAlgorithm())
						.getKernel() != Label_Propagation_Kernel.NOT_SET) {
			if (flag) {
				params += ",";
			}
			kernel_str = ((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getKernel().getLiteral();
			params += "kernel=" + kernel_str;
			flag = true;

		}

		if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getGamma() != null) {
			if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm())
					.getKernel() == Label_Propagation_Kernel.KNN) {
				System.err.println(
						"ERROR: In the semi-supervised ML method Label Propagation, parameter gamma is not defined for the KNN kernel.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelPropagation.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				gamma_str = String.valueOf(
						((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getGamma().getDoubleValue());
				params += "gamma=" + gamma_str;
				flag = true;
			}
		}

		if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getN_neighbors() != null) {
			if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getKernel().equals(null)
					|| ((LabelPropagation) action.getDataAnalytics().getModelAlgorithm())
							.getKernel() != Label_Propagation_Kernel.KNN) {
				System.err.println(
						"ERROR: In the semi-supervised ML method Label Propagation, parameter n_neighbors is only defined for the KNN kernel.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelPropagation.html\n");
			} else if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getN_neighbors()
					.getIntValue() <= 0) {
				System.err.println(
						"ERROR: In the semi-supervised ML method Label Propagation, parameter n_neighbors may only have a strictly positive value.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelPropagation.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				n_neighbors_str = String.valueOf(((LabelPropagation) action.getDataAnalytics().getModelAlgorithm())
						.getN_neighbors().getIntValue());
				params += "n_neighbors=" + n_neighbors_str;
				flag = true;
			}
		}

		if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getMax_iter() != null) {
			if (flag) {
				params += ",";
			}
			max_iter_str = String.valueOf(
					((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getMax_iter().getIntValue());
			params += "max_iter=" + max_iter_str;
			flag = true;
		}

		if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getTol() != null) {
			if (flag) {
				params += ",";
			}
			tol_str = String.valueOf(
					((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getTol().getDoubleValue());
			params += "tol=" + tol_str;
			flag = true;
		}

		if (((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getN_jobs() != null) {
			if (flag) {
				params += ",";
			}
			n_jobs_str = String.valueOf(
					((LabelPropagation) action.getDataAnalytics().getModelAlgorithm()).getN_jobs().getIntValue());
			params += "n_jobs=" + n_jobs_str;
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.semi_supervised import LabelPropagation\n");
		pythonScriptStringBuilder.append("model = LabelPropagation(" + params + ")\n");
		pythonScriptStringBuilder.append("model = model.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_label_propagation.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
		pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");

	}

	private void generatePythonDATrainScriptLabelSpreading(String dalib, String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {
		if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2,
																	// https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelSpreading.html
			generatePythonScikitLearnDATrainScriptLabelSpreading(path_str, action, pythonScriptStringBuilder);
		} else if (dalib.equals("keras-tensorflow")) {
			System.err.println("ERROR: " + dalib
					+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
					+ action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		} else {
			System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
					+ "You may try @dalib=\"auto\" instead.\n");
		}
	}

	private void generatePythonScikitLearnDATrainScriptLabelSpreading(String path_str, DATrainAction action,
			StringBuilder pythonScriptStringBuilder) {

		String kernel_str = "";
		String gamma_str = "";
		String n_neighbors_str = "";
		String alpha_str = "";
		String max_iter_str = "";
		String tol_str = "";
		String n_jobs_str = "";
		String params = "";
		boolean flag = false;

		if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getKernel() != null
				&& ((LabelSpreading) action.getDataAnalytics().getModelAlgorithm())
						.getKernel() != Label_Propagation_Kernel.NOT_SET) {
			if (flag) {
				params += ",";
			}
			kernel_str = ((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getKernel().getLiteral();
			params += "kernel=" + kernel_str;
			flag = true;

		}

		if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getGamma() != null) {
			if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm())
					.getKernel() == Label_Propagation_Kernel.KNN) {
				System.err.println(
						"ERROR: In the semi-supervised ML method Label Spreading, parameter gamma is not defined for the KNN kernel.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelPropagation.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				gamma_str = String.valueOf(
						((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getGamma().getDoubleValue());
				params += "gamma=" + gamma_str;
				flag = true;
			}
		}

		if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getN_neighbors() != null) {
			if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getKernel().equals(null)
					|| ((LabelSpreading) action.getDataAnalytics().getModelAlgorithm())
							.getKernel() != Label_Propagation_Kernel.KNN) {
				System.err.println(
						"ERROR: In the semi-supervised ML method Label Spreading, parameter n_neighbors is only defined for the KNN kernel.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelSpreading.html\n");
			} else if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getN_neighbors()
					.getIntValue() <= 0) {
				System.err.println(
						"ERROR: In the semi-supervised ML method Label Spreading, parameter n_neighbors may only have a strictly positive value.\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelSpreading.html\n");
			} else {
				if (flag) {
					params += ",";
				}
				n_neighbors_str = String.valueOf(((LabelSpreading) action.getDataAnalytics().getModelAlgorithm())
						.getN_neighbors().getIntValue());
				params += "n_neighbors=" + n_neighbors_str;
				flag = true;
			}
		}

		if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getAlpha() != null) {

			if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getAlpha().getDoubleValue() < 0
					|| ((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getAlpha()
							.getDoubleValue() > 1) {
				System.err.println(
						"ERROR: In the semi-supervised ML method Label Spreading, parameter alpha (i.e., the Clamping factor) may only have a value in [0,1] (or actually in (0,1) to make it useful!).\n For more info, please refer to the API doc: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelSpreading.html\n");
			} else {

				if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getAlpha().getDoubleValue() == 0
						&& action.getDataAnalytics().getAutoML() == AutoML.ON) {
					System.out.println(
							"WARNING: In the semi-supervised ML method Label Spreading, parameter alpha (i.e., the Clamping factor) is set to 0. Please be aware that alpha=0 means keeping all the initial label information.\n Usually, alpha is in (0,1). Please refer to the API doc for more info: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelSpreading.html\n");
				}

				if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getAlpha().getDoubleValue() == 1
						&& action.getDataAnalytics().getAutoML() == AutoML.ON) {
					System.out.println(
							"WARNING: In the semi-supervised ML method Label Spreading, parameter alpha (i.e., the Clamping factor) is set to 1. Please be aware that alpha=1 means replacing all initial information.\n Usually, alpha is in (0,1). Please refer to the API doc for more info: https://scikit-learn.org/stable/modules/generated/sklearn.semi_supervised.LabelSpreading.html\n");
				}

				if (flag) {
					params += ",";
				}
				alpha_str = String.valueOf(
						((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getAlpha().getDoubleValue());
				params += "alpha=" + alpha_str;
				flag = true;
			}
		}

		if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getMax_iter() != null) {
			if (flag) {
				params += ",";
			}
			max_iter_str = String.valueOf(
					((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getMax_iter().getIntValue());
			params += "max_iter=" + max_iter_str;
			flag = true;
		}

		if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getTol() != null) {
			if (flag) {
				params += ",";
			}
			tol_str = String.valueOf(
					((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getTol().getDoubleValue());
			params += "tol=" + tol_str;
			flag = true;
		}

		if (((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getN_jobs() != null) {
			if (flag) {
				params += ",";
			}
			n_jobs_str = String.valueOf(
					((LabelSpreading) action.getDataAnalytics().getModelAlgorithm()).getN_jobs().getIntValue());
			params += "n_jobs=" + n_jobs_str;
			flag = true;
		}

		pythonScriptStringBuilder.append("from sklearn.semi_supervised import LabelSpreading\n");
		pythonScriptStringBuilder.append("model = LabelSpreading(" + params + ")\n");
		pythonScriptStringBuilder.append("model = model.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_label_spreading.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");

	}

	//private void generatePythonDATrainScriptGRU(String dalib, String path_str, DATrainAction action,
		//	StringBuilder pythonScriptStringBuilder, boolean b) {
		// TODO Auto-generated method stub
		
	//}

    // ------------------------------------------------------------------
	static void generatePlots(
		    StringBuilder sb,
		    String pathStr,
		    boolean forecastingPlots,
		    boolean overfittingPlots,
		    List<ForecastingPlots> forecastingPlotsList,
		    List<OverfittingPlots>  overfittingPlotsList,
		    int predictionCount,
		    DataAnalyticsModelAlgorithm modelAlgo,
		    String outFeat) {

		    final String plotsPath = pathStr + "/python-scripts/plots/";

		    // ---------------- Overfitting Plots ----------------
		   // ---------------- Overfitting Plots ----------------
// ---------- Overfitting Plots (safe version – no blank first plot) ----------
boolean drawTrainingLoss   = overfittingPlotsList != null &&
                             overfittingPlotsList.contains(OverfittingPlots.TRAINING_LOSS);
boolean drawValidationLoss = overfittingPlotsList != null &&
                             overfittingPlotsList.contains(OverfittingPlots.VALIDATION_LOSS);
boolean drawLearningCurve  = overfittingPlotsList != null &&
                             overfittingPlotsList.contains(OverfittingPlots.LEARNING_CURVE);
boolean drawBiasVariance   = overfittingPlotsList != null &&
                             overfittingPlotsList.contains(OverfittingPlots.BIAS_VARIANCE_TRADEOFF);

boolean anyOverfitPlot     = drawTrainingLoss || drawValidationLoss ||
                             drawLearningCurve || drawBiasVariance;

if (anyOverfitPlot) {
    sb.append("\n# ---------------- Overfitting Plots ----------------\n")
      .append("plt.figure(figsize=(10,6))\n");

    if (drawTrainingLoss)
        sb.append("plt.plot(history.history['loss'], label='Training Loss')\n");
    if (drawValidationLoss)
        sb.append("plt.plot(history.history['val_loss'], label='Validation Loss')\n");
    if (drawLearningCurve)
        sb.append("plt.plot(history.epoch, history.history['loss'], label='Learning Curve')\n");
    if (drawBiasVariance)
        sb.append("# TODO: bias–variance trade-off plot\n");

    sb.append("plt.xlabel('Epochs')\n")
      .append("plt.ylabel('Loss')\n")
      .append("plt.legend()\n")
      .append("plt.title('Overfitting Plot')\n")
      .append("plt.tight_layout()\n")
      .append("plt.savefig('").append(plotsPath).append("overfitting_plot.png')\n")
      .append("plt.close()\n");
}


		    // ---------------- Forecasting Plots ----------------
		    boolean hasForecast = forecastingPlots && forecastingPlotsList != null && !forecastingPlotsList.isEmpty()
		            && !(modelAlgo instanceof ARIMA || modelAlgo instanceof SARIMA || modelAlgo instanceof HWES || modelAlgo instanceof Prophet);

		    if (hasForecast) {
		        sb.append("\n# ---------------- Forecasting Plots ----------------\n")
		          .append("import os\n")
		          .append("import numpy as np\n")
		          .append("y_pred = np.array(y_pred)\n")
		          .append("y_test = np.array(y_test)\n")
		          .append("# reshape to 2D if single-step forecast\n")
		          .append("if y_test.ndim == 1:\n    y_test = np.tile(y_test.reshape(-1, 1), (1, y_pred.shape[1]))\n")
		          .append("if y_pred.ndim == 1:\n    y_pred = y_pred.reshape(-1, 1)\n")
		          .append("nSteps = min(y_test.shape[1], y_pred.shape[1])\n");

		        if (forecastingPlotsList.contains(ForecastingPlots.FORECAST_VS_ACTUAL)) {
		            sb.append("# plot exactly nSteps forecast steps\n")
		              .append("steps = nSteps\n")
		              .append("titles = [f'Prediction {i+1} (t+{i})' for i in range(steps)]\n")
		              .append("filenames = [f'forecast_vs_actual_t{i}.png' for i in range(steps)]\n")
		              .append("for i in range(steps):\n")
		              .append("    plt.figure(figsize=(10,5))\n")
		              .append("    plt.plot(y_test[:, i], label='Actual')\n")
		              .append("    plt.plot(y_pred[:, i], label='Forecast', linestyle='--')\n")
		              .append("    plt.title(titles[i])\n")
		              .append("    plt.xlabel('Time Steps')\n")
		              .append("    plt.ylabel('Value')\n")
		              .append("    plt.legend()\n")
		              .append("    plt.tight_layout()\n")
		              .append("    plt.savefig(os.path.join('" + plotsPath + "', filenames[i]))\n")
		              .append("    plt.close()\n");
		        }

		        if (forecastingPlotsList.contains(ForecastingPlots.FORECAST_ERROR)) {
		            sb.append("forecast_error = y_test - y_pred\n")
		              .append("plt.figure(figsize=(10,5))\n")
		              .append("plt.hist(forecast_error.flatten(), bins=30)\n")
		              .append("plt.title('Forecast Error Distribution')\n")
		              .append("plt.xlabel('Error')\n")
		              .append("plt.ylabel('Frequency')\n")
		              .append("plt.tight_layout()\n")
		              .append("plt.savefig('" + plotsPath + "forecast_error.png')\n")
		              .append("plt.close()\n");
		        }

		        if (forecastingPlotsList.contains(ForecastingPlots.FORECAST_INTERVALS)) {
		            sb.append("forecast_std = y_pred.std(axis=0)\n")
		              .append("for i in range(y_test.shape[1]):\n")
		              .append("    ci = 1.96 * forecast_std[i]\n")
		              .append("    plt.figure(figsize=(10,5))\n")
		              .append("    plt.fill_between(range(len(y_test)), y_pred[:, i]-ci, y_pred[:, i]+ci, alpha=0.4, label='95% CI')\n")
		              .append("    plt.plot(y_test[:, i], label='Actual')\n")
		              .append("    plt.plot(y_pred[:, i], label='Forecast', linestyle='--')\n")
		              .append("    plt.title(f'Forecast w/ CI – Prediction {i+1}')\n")
		              .append("    plt.xlabel('Time Steps')\n")
		              .append("    plt.ylabel('Value')\n")
		              .append("    plt.legend()\n")
		              .append("    plt.tight_layout()\n")
		              .append("    plt.savefig(os.path.join('" + plotsPath + "', f'forecast_ci_t{i}.png'))\n")
		              .append("    plt.close()\n");
		        }
		    }

		    // ---------------- ARIMA / SARIMA / HWES ----------------
		    if (modelAlgo instanceof ARIMA || modelAlgo instanceof SARIMA) {
		        sb.append("\n# ---------------- Classical Statsmodels Forecast ----------------\n")
		          .append("steps = ").append(predictionCount).append("\n")
		          .append("forecast_res = model_fit.get_forecast(steps=steps)\n")
		          .append("y_pred_cls  = forecast_res.predicted_mean\n")
		          .append("ci          = forecast_res.conf_int(alpha=0.05)\n\n")
		          .append("plt.figure(figsize=(12,6))\n")
		          .append("plt.plot(").append(outFeat).append("_series.index, ").append(outFeat).append("_series, label='Actual')\n")
		          .append("plt.plot(y_pred_cls.index, y_pred_cls, label='Forecast', linestyle='--')\n")
		          .append("plt.fill_between(ci.index, ci.iloc[:,0], ci.iloc[:,1], alpha=0.4, label='95% CI')\n")
		          .append("plt.title('Forecast vs Actual (").append(modelAlgo.getClass().getSimpleName()).append(")')\n")
		          .append("plt.xlabel('Time')\nplt.ylabel('Value')\nplt.legend()\nplt.tight_layout()\n")
		          .append("plt.savefig(os.path.join('").append(plotsPath).append("', '")
		          .append(modelAlgo.getClass().getSimpleName().toLowerCase()).append("_forecast.png'))\nplt.close()\n");
		        
		        
		    } else if (modelAlgo instanceof HWES) {
		        sb.append("\n# ---------------- Classical Statsmodels Forecast (HWES) ----------------\n")
		          .append("steps = ").append(predictionCount).append("\n")
		          .append("y_pred_cls = model_fit.forecast(steps=steps)\n")
		          .append("future_index = pd.date_range(start=").append(outFeat).append("_series.index[-1], periods=steps+1, freq='D')[1:]\n\n")
		          .append("plt.figure(figsize=(12,6))\n")
		          .append("plt.plot(").append(outFeat).append("_series.index, ").append(outFeat).append("_series, label='Actual')\n")
		          .append("plt.plot(future_index, y_pred_cls, label='Forecast', linestyle='--')\n")
		          .append("plt.title('Forecast vs Actual (HWES)')\n")
		          .append("plt.xlabel('Time')\nplt.ylabel('Value')\nplt.legend()\nplt.tight_layout()\n")
		          .append("plt.savefig(os.path.join('").append(plotsPath).append("', 'hwes_forecast.png'))\nplt.close()\n");
		    }


		    // ---------------- Prophet Forecast ----------------
		    if (modelAlgo instanceof Prophet) {
		        sb.append("\n# ---------------- Prophet Forecast Plot ----------------\n")
		          .append("future   = model.make_future_dataframe(periods=").append(predictionCount).append(")\n")
		          .append("forecast = model.predict(future)\n\n")
		          .append("plt.figure(figsize=(12,6))\n")
		          .append("plt.plot(prophet_df['ds'], prophet_df['y'], label='Actual')\n")
		          .append("plt.plot(forecast['ds'], forecast['yhat'], label='Forecast', linestyle='--')\n")
		          .append("plt.fill_between(forecast['ds'], forecast['yhat_lower'], forecast['yhat_upper'], alpha=0.4, label='95% CI')\n")
		          .append("plt.title('Forecast vs Actual (Prophet)')\n")
		          .append("plt.xlabel('Time')\nplt.ylabel('Value')\nplt.legend()\nplt.tight_layout()\n")
		          .append("plt.savefig(os.path.join('")
		          .append(plotsPath)
		          .append("', 'prophet_forecast.png'))\nplt.close()\n");
		    }
		}

    
	private void generatePredictionPlots(
		    StringBuilder pythonScriptStringBuilder,
		    String path_str,
		    List<PredictionPlots> predictionPlotsList
		) {
		    if (predictionPlotsList == null || predictionPlotsList.isEmpty()) {
		        System.out.println("INFO: No prediction plots defined. Skipping prediction plot generation.");
		        return;
		    }

		    // Common imports and path
		    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
		    pythonScriptStringBuilder.append("import numpy as np\n");
		    pythonScriptStringBuilder.append("import seaborn as sns\n");
		    pythonScriptStringBuilder.append("from sklearn.metrics import confusion_matrix, roc_curve, auc, precision_recall_curve, average_precision_score\n\n");
		    pythonScriptStringBuilder.append("plots_path = '" + path_str + "/python-scripts/plots/'\n\n");

		    if (predictionPlotsList.contains(PredictionPlots.PREDICTION_VS_ACTUAL)) {
		        pythonScriptStringBuilder.append(
		            "plt.figure()\n" +
		            "plt.plot(y_train, label='Actual')\n" +
		            "plt.plot(y_pred, label='Predicted')\n" +
		            "plt.title('Prediction vs Actual')\n" +
		            "plt.legend()\n" +
		            "plt.savefig(plots_path + 'prediction_vs_actual.png')\n" +
		            "plt.close()\n\n"
		        );
		    }

		    if (predictionPlotsList.contains(PredictionPlots.RESIDUALS_PLOT)) {
		        pythonScriptStringBuilder.append(
		            "residuals = y_train - y_pred\n" +
		            "plt.figure()\n" +
		            "plt.hist(residuals, bins=30)\n" +
		            "plt.title('Residuals Plot')\n" +
		            "plt.savefig(plots_path + 'residuals_plot.png')\n" +
		            "plt.close()\n\n"
		        );
		    }

		    if (predictionPlotsList.contains(PredictionPlots.ERROR_DISTRIBUTION)) {
		        pythonScriptStringBuilder.append(
		            "errors = y_train - y_pred\n" +
		            "plt.figure()\n" +
		            "plt.hist(errors, bins=30, density=True)\n" +
		            "plt.title('Error Distribution')\n" +
		            "plt.xlabel('Error')\n" +
		            "plt.ylabel('Density')\n" +
		            "plt.savefig(plots_path + 'error_distribution.png')\n" +
		            "plt.close()\n\n"
		        );
		    }

		    if (predictionPlotsList.contains(PredictionPlots.CONFIDENCE_INTERVALS)) {
		        pythonScriptStringBuilder.append(
		            "predicted_mean = y_pred.mean()\n" +
		            "predicted_std = y_pred.std()\n" +
		            "ci_lower = predicted_mean - 1.96 * predicted_std\n" +
		            "ci_upper = predicted_mean + 1.96 * predicted_std\n" +
		            "plt.figure()\n" +
		            "plt.plot(y_pred, label='Predicted')\n" +
		            "plt.fill_between(range(len(y_pred)), ci_lower, ci_upper, alpha=0.2, label='95% CI')\n" +
		            "plt.title('Prediction with Confidence Intervals')\n" +
		            "plt.legend()\n" +
		            "plt.savefig(plots_path + 'confidence_intervals.png')\n" +
		            "plt.close()\n\n"
		        );
		    }

		    if (predictionPlotsList.contains(PredictionPlots.CONFUSION_MATRIX)) {
		        pythonScriptStringBuilder.append(
		            "cm = confusion_matrix(y_train, y_pred)\n" +
		            "plt.figure()\n" +
		            "sns.heatmap(cm, annot=True, fmt='d', cmap='Blues')\n" +
		            "plt.title('Confusion Matrix')\n" +
		            "plt.xlabel('Predicted')\n" +
		            "plt.ylabel('Actual')\n" +
		            "plt.savefig(plots_path + 'confusion_matrix.png')\n" +
		            "plt.close()\n\n"
		        );
		    }

		    if (predictionPlotsList.contains(PredictionPlots.ROC_CURVE)) {
		        pythonScriptStringBuilder.append(
		            "fpr, tpr, _ = roc_curve(y_train, y_pred)\n" +
		            "roc_auc = auc(fpr, tpr)\n" +
		            "plt.figure()\n" +
		            "plt.plot(fpr, tpr, label='ROC (AUC = %0.2f)' % roc_auc)\n" +
		            "plt.plot([0,1], [0,1], 'k--')\n" +
		            "plt.title('ROC Curve')\n" +
		            "plt.xlabel('False Positive Rate')\n" +
		            "plt.ylabel('True Positive Rate')\n" +
		            "plt.legend(loc='lower right')\n" +
		            "plt.savefig(plots_path + 'roc_curve.png')\n" +
		            "plt.close()\n\n"
		        );
		    }

		    if (predictionPlotsList.contains(PredictionPlots.PR_CURVE)) {
		        pythonScriptStringBuilder.append(
		            "precision, recall, _ = precision_recall_curve(y_train, y_pred)\n" +
		            "avg_prec = average_precision_score(y_train, y_pred)\n" +
		            "plt.figure()\n" +
		            "plt.step(recall, precision, where='post', label='Avg Precision = %0.2f' % avg_prec)\n" +
		            "plt.title('Precision-Recall Curve')\n" +
		            "plt.xlabel('Recall')\n" +
		            "plt.ylabel('Precision')\n" +
		            "plt.legend(loc='lower left')\n" +
		            "plt.savefig(plots_path + 'pr_curve.png')\n" +
		            "plt.close()\n\n"
		        );
		    }
		}

	
	////  Deep Learning Models
////////////////////////////////////////////////////////////////////////////	
	
	private void generatePythonDATrainScriptMLP(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
	        generatePythonKerasTensorFlowDATrainScriptMLP(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}
	
	
	
	private void generatePythonKerasTensorFlowDATrainScriptMLP(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    List<Integer> layerSizes = new ArrayList<>();
    MLP mlp = (MLP) action.getDataAnalytics().getModelAlgorithm();
    
    // Default hidden layer sizes (as a string to be used in the Python script)
    String hidden_layer_sizes_str = "64, 32";

    // Default parameter values (wrapped in quotes when appropriate)
    String input_activation_str = "'relu'";
    String hidden_activation_str = "'tanh'";
    String output_activation_str = "'linear'";
    String optimizer_str = "'adam'";
    String batch_size_str = "32";
    String epochs_str = "100";
    String dropout_str = "0.2";
    String metrics_str = "'mse'";
    // Default regularizer code: using l2(0.01)
    String regularizer_code = "l2(0.01)";
    boolean early_stopping = false;
    String rate_str = "0.001";

    pythonScriptStringBuilder.append("import pickle\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.models import Sequential\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.layers import Dense, Dropout\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.regularizers import l1, l2, l1_l2\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.callbacks import EarlyStopping\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.optimizers import Adam\n");
    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n\n");
    
    pythonScriptStringBuilder.append("if X_train.ndim == 3:\n");
    pythonScriptStringBuilder.append("    X_train = X_train.reshape(X_train.shape[0], -1)  # Flatten to (samples, timesteps * features)\n");
    pythonScriptStringBuilder.append("if X_test.ndim == 3:\n");
    pythonScriptStringBuilder.append("    X_test = X_test.reshape(X_test.shape[0], -1)  # Flatten to (samples, timesteps * features)\n");


    // Extract model configuration values from MLP (use defaults if NOT_SET or null)
    if (mlp.getRegularization() != null && !mlp.getRegularization().getLiteral().equals("NOT_SET")) {
        regularizer_code = mlp.getRegularization().getLiteral();
    }
    if (mlp.getInput_activation() != null && mlp.getInput_activation() != Activation.NOT_SET) {
        input_activation_str = "'" + mlp.getInput_activation().getLiteral() + "'";
    }
    if (mlp.getHidden_activation() != null && mlp.getHidden_activation() != Activation.NOT_SET) {
        hidden_activation_str = "'" + mlp.getHidden_activation().getLiteral() + "'";
    }
    if (mlp.getOutput_activation() != null && mlp.getOutput_activation() != Activation.NOT_SET) {
        output_activation_str = "'" + mlp.getOutput_activation().getLiteral() + "'";
    }

    // Handle hidden layer sizes with default fallback
    if (mlp.getHidden_layers() != null && mlp.getHidden_layers().size() > 0) {
        for (IntegerLiteral hiddenLayer : mlp.getHidden_layers()) {
            int size = (int) hiddenLayer.getIntValue();
            layerSizes.add(size);
        }
    } else {
        layerSizes.add(64);
        layerSizes.add(32);
    }
 
    // Handle optional parameters: Dropout rate
    Double dropoutRate = mlp.getDropout(); // Assuming getDropout() returns a Double
    if (dropoutRate != null) {
        dropout_str = String.valueOf(dropoutRate);
    } else {
        System.err.println("WARNING: Dropout rate not specified. Defaulting to 0.2.");
        dropout_str = "0.2";
    }

    // Manage Learning Rate
    Double learningRate = mlp.getRate(); // Assuming getRate() returns a Double
    if (learningRate != null) {
        rate_str = String.valueOf(learningRate);
    } else {
        System.err.println("WARNING: Learning rate not specified. Defaulting to 0.001.");
        rate_str = "0.001";
    }

    // Optimizer
    if (mlp.getOptimizer() != null && mlp.getOptimizer() != Optimizer.NOT_SET) {
        optimizer_str = "'" + mlp.getOptimizer().getLiteral() + "'";
    } else {
        System.err.println("WARNING: Optimizer not specified. Defaulting to 'adam'.");
        optimizer_str = "'adam'";
    }

    // Batch Size
    if (mlp.getBatch_size() != null) {
        batch_size_str = String.valueOf(mlp.getBatch_size().getIntValue());
    } else {
        System.err.println("WARNING: Batch size not specified. Defaulting to 32.");
        batch_size_str = "32";
    }

    // Epochs
    if (mlp.getEpochs() != null) {
        epochs_str = String.valueOf(mlp.getEpochs().getIntValue());
    } else {
        System.err.println("WARNING: Number of epochs not specified. Defaulting to 100.");
        epochs_str = "100";
    }

    // Metrics
    if (mlp.getMetrics() != null && mlp.getMetrics() != Metrics.NOT_SET) {
        metrics_str = "'" + mlp.getMetrics().getLiteral() + "'";
    } else {
        System.err.println("WARNING: Metrics not specified. Defaulting to 'mse'.");
        metrics_str = "'mse'";
    }

    if (mlp.getEarly_stopping() != null) {
        early_stopping = mlp.getEarly_stopping() == EarlyStopping.ENABLE;
    }

    // AutoML and Hyperparameter Tuning handling
    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        // Auto-select optimizer based on dataset size
        pythonScriptStringBuilder.append("if X_train.shape[0] < 1000:\n");
        pythonScriptStringBuilder.append("    optimizer_auto = Adam(learning_rate=0.01)  # or use a smaller learning rate for convergence\n");
        pythonScriptStringBuilder.append("    print('INFO: Small dataset detected, using lbfgs optimizer for faster convergence.')\n");
        pythonScriptStringBuilder.append("else:\n");
        pythonScriptStringBuilder.append("    optimizer_auto = Adam()\n");
        pythonScriptStringBuilder.append("    print('INFO: Large dataset detected, using adam optimizer for better performance.')\n");
        optimizer_str = "optimizer_auto";
        
        // AutoML tuning for hidden layers, batch size, and epochs using new criteria
        pythonScriptStringBuilder.append("hidden_layer_sizes_auto = (64, 32)\n");
        hidden_layer_sizes_str = "hidden_layer_sizes_auto";
        
        pythonScriptStringBuilder.append("if X_train.shape[0] < 500:\n");
        pythonScriptStringBuilder.append("    batch_size_auto = 8\n");
        pythonScriptStringBuilder.append("    epochs_auto = 30\n");
        pythonScriptStringBuilder.append("elif X_train.shape[0] < 2000:\n");
        pythonScriptStringBuilder.append("    batch_size_auto = 16\n");
        pythonScriptStringBuilder.append("    epochs_auto = 50\n");
        pythonScriptStringBuilder.append("else:\n");
        pythonScriptStringBuilder.append("    batch_size_auto = 32\n");
        pythonScriptStringBuilder.append("    epochs_auto = 100\n");
        batch_size_str = "batch_size_auto";
        epochs_str = "epochs_auto";
        
        // AutoML selection for activation functions
        pythonScriptStringBuilder.append("input_activation_auto = 'relu'\n");
        input_activation_str = "input_activation_auto";
        
        pythonScriptStringBuilder.append("hidden_activation_auto = 'tanh'\n");
        hidden_activation_str = "hidden_activation_auto";
        
        pythonScriptStringBuilder.append("output_activation_auto = 'linear'\n");
        output_activation_str = "output_activation_auto";
        
        // AutoML selection for dropout rate
        pythonScriptStringBuilder.append("dropout_rate_auto = 0.2\n");
        dropout_str = "dropout_rate_auto";
        
        // AutoML selection for regularization
        pythonScriptStringBuilder.append("regularizer_auto = l2(0.01)\n");
        regularizer_code = "regularizer_auto";
        
        // Correct the mse metric definition by quoting the string
        pythonScriptStringBuilder.append("metrics_auto = 'mse'\n");
        metrics_str = "metrics_auto";
        
    } else {
        // AutoML is OFF: Use user-specified or default parameters

        // Hidden Layer Sizes
        if (mlp.getHidden_layer_sizes() != null && !mlp.getHidden_layer_sizes().isEmpty()) {
            hidden_layer_sizes_str = mlp.getHidden_layer_sizes();
        } else {
            System.err.println("WARNING: Hidden layer sizes not specified. Defaulting to (64, 32).");
            hidden_layer_sizes_str = "(64, 32)";
        }

        // Activation Function for Input Layer
        if (mlp.getInput_activation() != null && mlp.getInput_activation() != Activation.NOT_SET) {
            input_activation_str = "'" + mlp.getInput_activation().getLiteral() + "'";
        } else {
            input_activation_str = "'relu'";
            System.err.println("WARNING: Input layer activation function not specified. Defaulting to 'relu'.");
        }

        // Activation Function for Hidden Layers
        if (mlp.getHidden_activation() != null && mlp.getHidden_activation() != Activation.NOT_SET) {
            hidden_activation_str = "'" + mlp.getHidden_activation().getLiteral() + "'";
        } else {
            hidden_activation_str = "'tanh'";
            System.err.println("WARNING: Hidden layer activation function not specified. Defaulting to 'tanh'.");
        }

        // Activation Function for Output Layer
        if (mlp.getOutput_activation() != null && mlp.getOutput_activation() != Activation.NOT_SET) {
            output_activation_str = "'" + mlp.getOutput_activation().getLiteral() + "'";
        } else {
            output_activation_str = "'linear'";
            System.err.println("WARNING: Output layer activation function not specified. Defaulting to 'linear'.");
        }

        // Optimizer
        if (mlp.getOptimizer() != null && mlp.getOptimizer() != Optimizer.NOT_SET) {
            optimizer_str = "'" + mlp.getOptimizer().getLiteral() + "'";
        } else {
            optimizer_str = "'adam'";
            System.err.println("WARNING: Optimizer not specified. Defaulting to 'adam'.");
        }

        // Batch Size
        if (mlp.getBatch_size() != null) {
            batch_size_str = String.valueOf(mlp.getBatch_size().getIntValue());
        } else {
            batch_size_str = "32";
            System.err.println("WARNING: Batch size not specified. Defaulting to 32.");
        }

        // Epochs
        if (mlp.getEpochs() != null) {
            epochs_str = String.valueOf(mlp.getEpochs().getIntValue());
        } else {
            epochs_str = "100";
            System.err.println("WARNING: Number of epochs not specified. Defaulting to 100.");
        }

        // Metrics
        if (mlp.getMetrics() != null && mlp.getMetrics() != Metrics.NOT_SET) {
            metrics_str = "'" + mlp.getMetrics().getLiteral() + "'";
        } else {
            metrics_str = "'mse'";
            System.err.println("WARNING: Metrics not specified. Defaulting to 'mse'.");
        }

        // Regularization
        if (mlp.getRegularization() != null && !mlp.getRegularization().getLiteral().equals("NOT_SET")) {
            regularizer_code = mlp.getRegularization().getLiteral();
        } else {
            regularizer_code = "l2(0.01)";
            System.err.println("WARNING: Regularization not specified. Defaulting to 'l2(0.01)'.");
        }
    }
    
    // Hyperparameter Tuning: Only apply if specified and AutoML is off
   // Hyperparameter Tuning: Only apply if specified and AutoML is off
boolean hyperTuningOn = (!autoML && action.getDataAnalytics().getHyperparameter_tuning() != null 
        && action.getDataAnalytics().getHyperparameter_tuning() != HyperparameterTuning.OFF);
if (hyperTuningOn) {
    HyperparameterTuning tuningMethod = action.getDataAnalytics().getHyperparameter_tuning();
    switch (tuningMethod) {
        case GRID_SEARCH:
            // Wrap the model in a KerasRegressor so that scikit-learn can work with it.
            pythonScriptStringBuilder.append("from tensorflow.keras.wrappers.scikit_learn import KerasRegressor\n");
            pythonScriptStringBuilder.append("def build_model(units, dropout):\n");
            pythonScriptStringBuilder.append("    model = Sequential()\n");
            pythonScriptStringBuilder.append("    model.add(Dense(units, activation=" + input_activation_str + ", input_dim=X_train.shape[1], kernel_regularizer=" + regularizer_code + "))\n");
            pythonScriptStringBuilder.append("    model.add(Dropout(dropout))\n");
            pythonScriptStringBuilder.append("    model.add(Dense(32, activation=" + hidden_activation_str + ", kernel_regularizer=" + regularizer_code + "))\n");
            pythonScriptStringBuilder.append("    model.add(Dropout(dropout))\n");
            pythonScriptStringBuilder.append("    model.add(Dense(3, activation=" + output_activation_str + "))\n");
            pythonScriptStringBuilder.append("    model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=[" + metrics_str + "])\n");
            pythonScriptStringBuilder.append("    return model\n\n");
            pythonScriptStringBuilder.append("model_wrapper = KerasRegressor(build_fn=build_model, verbose=0)\n");
            pythonScriptStringBuilder.append("param_grid = {\n");
            pythonScriptStringBuilder.append("    'units': [64, 128, 256],\n");
            pythonScriptStringBuilder.append("    'dropout': [0.2, 0.3, 0.5],\n");
            pythonScriptStringBuilder.append("    'batch_size': [16, 32, 64],\n");
            pythonScriptStringBuilder.append("    'epochs': [50, 100, 150]\n");
            pythonScriptStringBuilder.append("}\n");
            pythonScriptStringBuilder.append("from sklearn.model_selection import GridSearchCV\n");
            pythonScriptStringBuilder.append("grid = GridSearchCV(estimator=model_wrapper, param_grid=param_grid, scoring='neg_mean_squared_error')\n");
            pythonScriptStringBuilder.append("grid_result = grid.fit(X_train, y_train)\n\n");
            pythonScriptStringBuilder.append("best_model = grid_result.best_estimator_.model\n");
            System.out.println("INFO: Hyperparameter Tuning enabled using Grid Search.");
            break;

        case RANDOM_SEARCH:
            pythonScriptStringBuilder.append("from tensorflow.keras.wrappers.scikit_learn import KerasRegressor\n");
            pythonScriptStringBuilder.append("def build_model(units, dropout):\n");
            pythonScriptStringBuilder.append("    model = Sequential()\n");
            pythonScriptStringBuilder.append("    model.add(Dense(units, activation=" + input_activation_str + ", input_dim=X_train.shape[1], kernel_regularizer=" + regularizer_code + "))\n");
            pythonScriptStringBuilder.append("    model.add(Dropout(dropout))\n");
            pythonScriptStringBuilder.append("    model.add(Dense(32, activation=" + hidden_activation_str + ", kernel_regularizer=" + regularizer_code + "))\n");
            pythonScriptStringBuilder.append("    model.add(Dropout(dropout))\n");
            pythonScriptStringBuilder.append("    model.add(Dense(3, activation=" + output_activation_str + "))\n");
            pythonScriptStringBuilder.append("    model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=[" + metrics_str + "])\n");
            pythonScriptStringBuilder.append("    return model\n\n");
            pythonScriptStringBuilder.append("model_wrapper = KerasRegressor(build_fn=build_model, verbose=0)\n");
            pythonScriptStringBuilder.append("param_dist = {\n");
            pythonScriptStringBuilder.append("    'units': [64, 128, 256],\n");
            pythonScriptStringBuilder.append("    'dropout': [0.2, 0.3, 0.5],\n");
            pythonScriptStringBuilder.append("    'batch_size': [16, 32, 64],\n");
            pythonScriptStringBuilder.append("    'epochs': [50, 100, 150]\n");
            pythonScriptStringBuilder.append("}\n");
            pythonScriptStringBuilder.append("from sklearn.model_selection import RandomizedSearchCV\n");
            pythonScriptStringBuilder.append("random_search = RandomizedSearchCV(estimator=model_wrapper, param_distributions=param_dist, n_iter=10, scoring='neg_mean_squared_error')\n");
            pythonScriptStringBuilder.append("random_result = random_search.fit(X_train, y_train)\n\n");
            pythonScriptStringBuilder.append("best_model = random_result.best_estimator_.model\n");
            System.out.println("INFO: Hyperparameter Tuning enabled using Random Search.");
            break;

        case BAYESIAN_OPTIMIZATION:
            pythonScriptStringBuilder.append("import keras_tuner as kt\n");
            pythonScriptStringBuilder.append("def build_model(hp):\n");
            pythonScriptStringBuilder.append("    model = Sequential()\n");
            pythonScriptStringBuilder.append("    model.add(Dense(hp.Int('units', min_value=64, max_value=256, step=64), activation=" + input_activation_str + ", input_dim=X_train.shape[1], kernel_regularizer=" + regularizer_code + "))\n");
            pythonScriptStringBuilder.append("    model.add(Dropout(hp.Float('dropout', min_value=0.2, max_value=0.5, step=0.1)))\n");
            pythonScriptStringBuilder.append("    model.add(Dense(3, activation=" + output_activation_str + "))\n\n");
            pythonScriptStringBuilder.append("    model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=[" + metrics_str + "])\n");
            pythonScriptStringBuilder.append("    return model\n\n");
            pythonScriptStringBuilder.append("tuner = kt.BayesianOptimization(build_model, objective='val_loss', max_trials=5)\n");
            pythonScriptStringBuilder.append("tuner.search(X_train, y_train, epochs=" + epochs_str + ", validation_data=(X_test, y_test))\n");
            pythonScriptStringBuilder.append("best_model = tuner.get_best_models(num_models=1)[0]\n\n");
            System.out.println("INFO: Hyperparameter Tuning enabled using Bayesian Optimization.");
            break;
        default:
            break;
    }
} else {
    System.out.println("INFO: Hyperparameter Tuning is OFF or no parameters provided.");
}

    // Use best_model if hyperparameter tuning was enabled;
    // otherwise, build the model from scratch.
    if (hyperTuningOn) {
        pythonScriptStringBuilder.append("try:\n");
        pythonScriptStringBuilder.append("    model = best_model\n");
        pythonScriptStringBuilder.append("    print('INFO: Using best model from hyperparameter tuning for prediction.')\n");
        pythonScriptStringBuilder.append("except NameError:\n");
        pythonScriptStringBuilder.append("    print('WARNING: best_model not found, building model from scratch.')\n");
        pythonScriptStringBuilder.append("    model = Sequential()\n");
    } else {
        pythonScriptStringBuilder.append("model = Sequential()\n");
        pythonScriptStringBuilder.append("print('INFO: Building MLP model with hidden layers:', " + layerSizes + ")\n");
    
        // Input Layer
        pythonScriptStringBuilder.append("model.add(Dense(" + layerSizes.get(0) + ", activation=" + input_activation_str + ", input_dim=X_train.shape[1], kernel_regularizer=" + regularizer_code + "))\n");
        pythonScriptStringBuilder.append("model.add(Dropout(" + dropout_str + "))\n");
    
        // Hidden Layers
        for (int i = 1; i < layerSizes.size(); i++) {
            int layerSize = layerSizes.get(i);
            pythonScriptStringBuilder.append("model.add(Dense(" + layerSize + ", activation=" + hidden_activation_str + ", kernel_regularizer=" + regularizer_code + "))\n");
            pythonScriptStringBuilder.append("model.add(Dropout(" + dropout_str + "))\n");
        }
        
        long nSteps = action.getDataAnalytics().getSteps() != null ? action.getDataAnalytics().getSteps().getIntValue() : 1;
        // Output Layer
        pythonScriptStringBuilder.append("model.add(Dense(" + nSteps + ", activation=" + output_activation_str + "))\n\n");
    }
    
    // Compile the Model
    pythonScriptStringBuilder.append("model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=[" + metrics_str + "])\n");

    // Early Stopping Callback
    if (early_stopping) {
        pythonScriptStringBuilder.append("early_stopping = EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)\n");
        pythonScriptStringBuilder.append("callbacks = [early_stopping]\n");
    } else {
        pythonScriptStringBuilder.append("callbacks = []\n");
    }

    // Model Training
    pythonScriptStringBuilder.append("history = model.fit(X_train, y_train, epochs=" + epochs_str + ", batch_size=" + batch_size_str + ", validation_data=(X_test, y_test), callbacks=callbacks)\n");

    // Save the Model and Predict
    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");
  //  pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/mlp_model.h5', 'wb') as pickle_file:\n");
    //pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
    
pythonScriptStringBuilder.append("model.save('" + path_str + "/python-scripts/pickles/mlp_model.h5')\n\n");

    
    // Additional code for Overfitting and Forecasting Plot Conditions can be added here as needed.
}

	
	
	private void generatePythonDATrainScriptCNN(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
	        generatePythonKerasTensorFlowDATrainScriptCNN(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}
	

	

private void generatePythonKerasTensorFlowDATrainScriptCNN(String path_str,
                                                           DATrainAction action,
                                                           StringBuilder pythonScriptStringBuilder,
                                                           boolean autoML) {
    // 0. Grab the CNN node
    CNN cnn = (CNN) action.getDataAnalytics().getModelAlgorithm();

    // 1. Defaults
    String units_str      = cnn.getUnits()      != null ? String.valueOf(cnn.getUnits().getIntValue()) : "32";
    Double dropoutRate    = cnn.getDropout();   // may be null
    String dropout_str;
    if (dropoutRate != null) {
        dropout_str = String.valueOf(dropoutRate);
    } else {
        System.err.println("WARNING: Dropout rate not specified.  Using 0.2.");
        dropout_str = "0.2";
    }
    String batch_size_str = cnn.getBatch_size() != null ? String.valueOf(cnn.getBatch_size().getIntValue()) : "32";
    String epochs_str     = cnn.getEpochs()     != null ? String.valueOf(cnn.getEpochs().getIntValue())     : "100";
    String optimizer_str  = cnn.getOptimizer() != Optimizer.NOT_SET
                            ? "'" + cnn.getOptimizer().getLiteral() + "'" : "Adam()";
    String input_act_str  = cnn.getInput_activation()  != Activation.NOT_SET
                            ? "'" + cnn.getInput_activation().getLiteral() + "'" : "'relu'";
    String hidden_act_str = cnn.getHidden_activation() != Activation.NOT_SET
                            ? "'" + cnn.getHidden_activation().getLiteral() + "'" : "'relu'";
    String output_act_str = cnn.getOutput_activation() != Activation.NOT_SET
                            ? "'" + cnn.getOutput_activation().getLiteral() + "'" : "'linear'";
    String metrics_str    = "'mse'";

    // 2. Hidden‐dense sizes
    List<Integer> denseSizes = new ArrayList<>();
    if (cnn.getHidden_layers() != null && !cnn.getHidden_layers().isEmpty()) {
        for (IntegerLiteral il : cnn.getHidden_layers()) {
            denseSizes.add((int) il.getIntValue());
        }
    } else {
        denseSizes.add(64);
        denseSizes.add(32);
    }

    // 3. Early‐stop?
    boolean earlyStop = cnn.getEarly_stopping() != null &&
                        cnn.getEarly_stopping() == EarlyStopping.ENABLE;

    // STEP 1 · Imports
    pythonScriptStringBuilder.append("import os, pickle, numpy as np\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.models import Sequential\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.layers import InputLayer, Conv1D, MaxPooling1D, GlobalMaxPooling1D, Flatten, Dense, Dropout\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.optimizers import Adam\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.callbacks import EarlyStopping\n\n");
    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n\n");

   

    // Reshape if necessary
    pythonScriptStringBuilder.append("X_train, X_test = np.asarray(X_train), np.asarray(X_test)\n");
    pythonScriptStringBuilder.append("y_train, y_test = np.asarray(y_train), np.asarray(y_test)\n\n");
    pythonScriptStringBuilder.append("if X_train.ndim == 2:\n");
    pythonScriptStringBuilder.append("    X_train = X_train.reshape((X_train.shape[0], 1, X_train.shape[1]))\n");
    pythonScriptStringBuilder.append("    X_test  = X_test.reshape((X_test.shape[0],  1, X_test.shape[1]))\n");
    pythonScriptStringBuilder.append("print(f'✅ X_train shape after reshape: {X_train.shape}')\n\n");
    pythonScriptStringBuilder.append("timesteps   = X_train.shape[1]\n");
    pythonScriptStringBuilder.append("features    = X_train.shape[2]\n");
    pythonScriptStringBuilder.append("kernel_size = 3 if timesteps >= 3 else timesteps\n");
    pythonScriptStringBuilder.append("use_pool    = timesteps >= 2\n\n");

    // STEP 3 · AutoML overrides
    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        pythonScriptStringBuilder.append("# AutoML: tune filters, lr & dropout by dataset size\n");
        pythonScriptStringBuilder.append("if X_train.shape[0] < 1000:\n");
        pythonScriptStringBuilder.append("    filters = 32; learning_rate = 0.01; dropout_rate = 0.2\n");
        pythonScriptStringBuilder.append("    print('INFO: small dataset → filters=32, lr=0.01, dropout=0.2')\n");
        pythonScriptStringBuilder.append("else:\n");
        pythonScriptStringBuilder.append("    filters = 64; learning_rate = 0.001; dropout_rate = 0.3\n");
        pythonScriptStringBuilder.append("    print('INFO: large dataset → filters=64, lr=0.001, dropout=0.3')\n");
        pythonScriptStringBuilder.append("optimizer = Adam(learning_rate=learning_rate)\n\n");
        optimizer_str = "optimizer";
        dropout_str   = "dropout_rate";
        units_str     = "filters";
    } else {
        pythonScriptStringBuilder.append("# Fixed hyperparameters (AutoML OFF)\n");
        pythonScriptStringBuilder.append("filters = ").append(units_str).append("\n");
        pythonScriptStringBuilder.append("optimizer = ").append(optimizer_str).append("\n");
        pythonScriptStringBuilder.append("dropout_rate = ").append(dropout_str).append("\n\n");
        optimizer_str = "optimizer";
        dropout_str   = "dropout_rate";
        units_str     = "filters";
    }

    // STEP 4 · Build CNN
    pythonScriptStringBuilder.append("model = Sequential(name='cnn_ts')\n");
    pythonScriptStringBuilder.append("model.add(InputLayer(shape=(timesteps, features)))\n");
    pythonScriptStringBuilder.append("model.add(Conv1D(filters, kernel_size=kernel_size, activation=")
                              .append(input_act_str).append("))\n");
    pythonScriptStringBuilder.append("if use_pool:\n");
    pythonScriptStringBuilder.append("    model.add(MaxPooling1D(pool_size=2))\n");
    pythonScriptStringBuilder.append("else:\n");
    pythonScriptStringBuilder.append("    model.add(GlobalMaxPooling1D())\n");
    pythonScriptStringBuilder.append("model.add(Dropout(").append(dropout_str).append("))\n\n");

    pythonScriptStringBuilder.append("# Dense hidden layers\n");
    for (int sz : denseSizes) {
        pythonScriptStringBuilder.append("model.add(Dense(").append(sz)
                                  .append(", activation=").append(hidden_act_str).append("))\n");
        pythonScriptStringBuilder.append("model.add(Dropout(").append(dropout_str).append("))\n");
    }
    pythonScriptStringBuilder.append("\n");

    // STEP 5 · Output & compile
    long nSteps = action.getDataAnalytics().getSteps() != null
                  ? action.getDataAnalytics().getSteps().getIntValue() : 1;
    pythonScriptStringBuilder.append("model.add(Flatten())\n");
    pythonScriptStringBuilder.append("model.add(Dense(").append(nSteps)
                              .append(", activation=").append(output_act_str).append("))\n\n");
    pythonScriptStringBuilder.append("model.compile(optimizer=optimizer, loss='mse', metrics=[")
                              .append(metrics_str).append("])\n");
    pythonScriptStringBuilder.append("print(model.summary())\n\n");

    // STEP 6 · Callbacks & fit
    pythonScriptStringBuilder.append("callbacks = [EarlyStopping(monitor='val_loss', patience=10, restore_best_weights=True)]\n");
    pythonScriptStringBuilder.append("history = model.fit(\n");
    pythonScriptStringBuilder.append("    X_train, y_train,\n");
    pythonScriptStringBuilder.append("    epochs=").append(epochs_str).append(", batch_size=").append(batch_size_str).append(",\n");
    pythonScriptStringBuilder.append("    validation_data=(X_test, y_test), callbacks=callbacks\n");
    pythonScriptStringBuilder.append(")\n\n");

    // STEP 7 · Save
    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");
    pythonScriptStringBuilder.append("# Save CNN model structure and weights as pkl\n");
    pythonScriptStringBuilder.append("import pickle\n");
    pythonScriptStringBuilder.append("model_json = model.to_json()\n");
    pythonScriptStringBuilder.append("model_weights = model.get_weights()\n");
    pythonScriptStringBuilder.append("with open('")
                              .append(path_str)
                              .append("/python-scripts/pickles/cnn_model.pkl', 'wb') as f:\n");
    pythonScriptStringBuilder.append("    pickle.dump({'model_json': model_json, 'weights': model_weights}, f)\n");
    pythonScriptStringBuilder.append("print('✅ CNN model saved to cnn_model.pkl')\n\n");

}


	private void generatePythonDATrainScriptGRU(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
	        generatePythonKerasTensorFlowDATrainScriptGRU(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}
	
	
private void generatePythonKerasTensorFlowDATrainScriptGRU(String path_str,
            DATrainAction action,
            StringBuilder pythonScriptStringBuilder,
            boolean autoML) {
List<Integer> layerSizes = new ArrayList<>();
GRU gru = (GRU) action.getDataAnalytics().getModelAlgorithm();

// 1. Default parameter values
String return_sequences_str = "False";
String dropout_rate_str     = "0.2";
String optimizer_str        = "'adam'";
String batch_size_str       = "32";
String epochs_str           = "100";
String metrics_str          = "'mse'";
boolean early_stopping      = false;

String inputActStr  = "'tanh'";
String hiddenActStr = "'tanh'";
String outputActStr = "'linear'";

// Imports
// Imports
pythonScriptStringBuilder.append("import pickle\n")
.append("import numpy as np\n")
.append("from tensorflow.keras.models import Sequential\n")
.append("from tensorflow.keras.layers import GRU, Dense, Dropout\n")
.append("from tensorflow.keras.optimizers import Adam\n")
.append("from tensorflow.keras.callbacks import EarlyStopping\n\n")
.append("import matplotlib.pyplot as plt\n\n");

// Assert: Ensure data is 3D
pythonScriptStringBuilder.append("X_train = np.array(X_train)\n");
pythonScriptStringBuilder.append("X_test = np.array(X_test)\n");

pythonScriptStringBuilder.append("if X_train.ndim == 2:\n");
pythonScriptStringBuilder.append("    X_train = X_train.reshape(X_train.shape[0], 1, X_train.shape[1])\n");
pythonScriptStringBuilder.append("if X_test.ndim == 2:\n");
pythonScriptStringBuilder.append("    X_test = X_test.reshape(X_test.shape[0], 1, X_test.shape[1])\n\n");



// 2. AutoML vs. user‐specified
if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
pythonScriptStringBuilder.append("# AutoML Configuration\n")
.append("if X_train.shape[0] < 1000:\n")
.append("    optimizer_auto = Adam(learning_rate=0.01)\n")
.append("    print('INFO: small dataset → lr=0.01')\n")
.append("else:\n")
.append("    optimizer_auto = Adam()\n\n");
optimizer_str = "optimizer_auto";

pythonScriptStringBuilder.append("hidden_layer_sizes_auto = (64, 128)\n");
// we'll parse that in Python below if needed
// but for building we'll read layerSizes from the tuple...

pythonScriptStringBuilder.append("batch_size_auto = 16 if X_train.shape[0] < 1000 else 32\n");
batch_size_str = "batch_size_auto";
pythonScriptStringBuilder.append("epochs_auto = 50 if X_train.shape[0] < 10000 else 100\n");
epochs_str = "epochs_auto";

pythonScriptStringBuilder.append("dropout_rate_auto = 0.3\n");
dropout_rate_str = "dropout_rate_auto";

pythonScriptStringBuilder.append("metrics_auto = ['mse']\n");
metrics_str = "metrics_auto";

// when AutoML is on, we won't read gru.getHidden_layers() at all—
// we'll assume the Python tuple above
layerSizes.clear();
pythonScriptStringBuilder.append("\n");
} else {
// user‐specified (or defaults)




if (gru.getReturn_sequences() != null) {
    // BooleanLiteralImpl.has a booleanValue() we can use
    return_sequences_str = gru.getReturn_sequences().isBoolValue() ? "True" : "False";
}


if (gru.getHidden_layers() != null && !gru.getHidden_layers().isEmpty()) {
for (IntegerLiteral hl : gru.getHidden_layers()) {
layerSizes.add((int) hl.getIntValue());
}
} else {
layerSizes.add(64);
layerSizes.add(32);
}
Double dr = gru.getDropout();
if (dr != null) dropout_rate_str = dr.toString();

if (gru.getOptimizer() != null && gru.getOptimizer() != Optimizer.NOT_SET)
optimizer_str = "'" + gru.getOptimizer().getLiteral() + "'";

if (gru.getBatch_size() != null)
batch_size_str = String.valueOf(gru.getBatch_size().getIntValue());
if (gru.getEpochs() != null)
epochs_str = String.valueOf(gru.getEpochs().getIntValue());
if (gru.getMetrics() != null && gru.getMetrics() != Metrics.NOT_SET)
metrics_str = "'" + gru.getMetrics().getLiteral() + "'";
if (gru.getInput_activation() != null && gru.getInput_activation() != Activation.NOT_SET)
inputActStr = "'" + gru.getInput_activation().getLiteral() + "'";
if (gru.getHidden_activation() != null && gru.getHidden_activation() != Activation.NOT_SET)
hiddenActStr = "'" + gru.getHidden_activation().getLiteral() + "'";
if (gru.getOutput_activation() != null && gru.getOutput_activation() != Activation.NOT_SET)
outputActStr = "'" + gru.getOutput_activation().getLiteral() + "'";
if (gru.getEarly_stopping() == EarlyStopping.ENABLE)
early_stopping = true;
pythonScriptStringBuilder.append("\n");
}

// 3. Number of forecast steps
long nSteps = action.getDataAnalytics().getSteps() != null
? action.getDataAnalytics().getSteps().getIntValue()
: 1;

// 4. Build the GRU stack
boolean userSetReturnSeq = gru.getReturn_sequences() != null;
String returnSeqArg     = userSetReturnSeq
? "return_sequences=" + return_sequences_str + ", "
: "";

pythonScriptStringBuilder.append("model = Sequential()\n");
// first layer always needs input_shape
pythonScriptStringBuilder.append("model.add(GRU(")
.append(layerSizes.get(0)).append(", ")
.append(returnSeqArg)
.append("input_shape=(X_train.shape[1], X_train.shape[2])))\n")
.append("model.add(Dropout(")
.append(dropout_rate_str).append("))\n");

// any intermediate layers must return_sequences=True




for (int i = 1; i < layerSizes.size(); i++) {
    boolean isLast = (i == layerSizes.size() - 1);
    pythonScriptStringBuilder.append("model.add(GRU(").append(layerSizes.get(i));
    if (!isLast) {
    	pythonScriptStringBuilder.append(", return_sequences=True");
    }
    pythonScriptStringBuilder.append(", activation=").append(hiddenActStr).append("))\n")
      .append("model.add(Dropout(").append(dropout_rate_str).append("))\n");
}

// final Dense
pythonScriptStringBuilder.append("model.add(Dense(")
.append(nSteps)
.append(", activation=").append(outputActStr)
.append("))\n\n");

// 5. Compile
pythonScriptStringBuilder.append("model.compile(optimizer=")
.append(optimizer_str)
.append(", loss='mse', metrics=[")
.append(metrics_str)
.append("])\n")
.append("print(model.summary())\n\n");

// 6. Callbacks
if (early_stopping) {
pythonScriptStringBuilder.append("callbacks = [EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)]\n");
} else {
pythonScriptStringBuilder.append("callbacks = []\n");
}

// 7. Training + save
pythonScriptStringBuilder.append("history = model.fit(X_train, y_train, epochs=")
.append(epochs_str)
.append(", batch_size=")
.append(batch_size_str)
.append(", validation_data=(X_test, y_test), callbacks=callbacks)\n\n")
.append("y_pred = model.predict(X_test)\n")
.append("with open('")
.append(path_str)
.append("/python-scripts/pickles/gru_model.pickle', 'wb') as pickle_file:\n")
.append("    pickle.dump(model, pickle_file)\n\n");
}

	
	// The main method to generate the Python training script for an LSTM model
	private void generatePythonDATrainScriptLSTM(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
	        generatePythonKerasTensorFlowDATrainScriptLSTM(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}
	
	
		    // Initialize parameter strings with defaults
	
  private void generatePythonKerasTensorFlowDATrainScriptLSTM(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    // Retrieve LSTM configuration and the number of output steps
    List<Integer> layerSizes = new ArrayList<>();
    LSTM lstm = (LSTM) action.getDataAnalytics().getModelAlgorithm();
    Long nSteps = action.getDataAnalytics().getSteps() != null ? action.getDataAnalytics().getSteps().getIntValue() : 1L;
    
    // Default parameter values (with activations wrapped in quotes)
    String input_activation_str = "'relu'";
    String hidden_activation_str = "'tanh'";
    String output_activation_str = "'linear'";
    String optimizer_str = "'adam'"; // default optimizer is 'adam'
    String batch_size_str = "32";
    String epochs_str = "100";
    String dropout_rate_str = "0.2"; // temporary default; will be overwritten below
    String l2_reg_str = "l2(0.01)";
    String metrics_str = "'mse'";  // default metrics
    
    // Use the DSL dropout value if provided
    Double dropoutRate = lstm.getDropout();
    if (dropoutRate != null) {
        dropout_rate_str = String.valueOf(dropoutRate);
    } else {
        dropout_rate_str = "0.0"; // Default value if dropout is not set
    }
    
    // Use the DSL learning rate directly if provided (using > 0.0 as an indicator)
    if (lstm.getRate() > 0.0) {
        if (!autoML) {
            optimizer_str = "Adam(learning_rate=" + lstm.getRate() + ")";
        }
    }
    
    // In the non-AutoML branch, update optimizer_str if a valid optimizer is provided.
    if (!autoML && lstm.getOptimizer() != null && lstm.getOptimizer() != Optimizer.NOT_SET) {
        optimizer_str = "'" + lstm.getOptimizer().getLiteral() + "'";
    }
    
    // Determine layer sizes: use user-specified hidden layers, or use lstm.getUnits(), or default to [64, 32]
    if (lstm.getHidden_layers() != null && !lstm.getHidden_layers().isEmpty()) {
        for (IntegerLiteral hiddenLayer : lstm.getHidden_layers()) {
            int size = (int) hiddenLayer.getIntValue();
            layerSizes.add(size);
        }
    } else if (lstm.getUnits() != null) {
        int units = (int) lstm.getUnits().getIntValue();
        layerSizes.add(units);
    } else {
        layerSizes.add(64);
        layerSizes.add(32);
    }
    
    // Import necessary Python modules
    pythonScriptStringBuilder.append("import pickle\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.models import Sequential\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.layers import LSTM, Dense, Dropout\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.callbacks import EarlyStopping\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.optimizers import Adam\n");
    pythonScriptStringBuilder.append("from tensorflow.keras.regularizers import l2\n");
    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
    pythonScriptStringBuilder.append("import numpy as np\n\n");
    
    // Ensure input data is 3D. If it's 2D, add a singleton time dimension.
    
    pythonScriptStringBuilder.append("X_train = np.array(X_train)\n");
    pythonScriptStringBuilder.append("X_test = np.array(X_test)\n");

    pythonScriptStringBuilder.append("if X_train.ndim == 2:\n");
    pythonScriptStringBuilder.append("    X_train = X_train.reshape(X_train.shape[0], 1, X_train.shape[1])\n");
    pythonScriptStringBuilder.append("if X_test.ndim == 2:\n");
    pythonScriptStringBuilder.append("    X_test = X_test.reshape(X_test.shape[0], 1, X_test.shape[1])\n\n");
    
    // Early Stopping: Check DSL enum (NOT_SET, ENABLE, DISABLE)
    if (lstm.getEarly_stopping() != null && lstm.getEarly_stopping() == EarlyStopping.ENABLE) {
        pythonScriptStringBuilder.append("early_stopping = EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)\n");
        pythonScriptStringBuilder.append("callbacks = [early_stopping]\n");
    } else {
        pythonScriptStringBuilder.append("callbacks = []\n");
    }
    
    // AutoML adjustments: Only proceed if autoML flag is true AND the AutoML configuration is not null and set to ON.
    if (autoML && action.getDataAnalytics().getAutoML() != null && action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        // If a valid learning rate (> 0) is provided in the DSL, use it for optimizer creation.
        if (lstm.getRate() > 0.0) {
            pythonScriptStringBuilder.append("if X_train.shape[0] < 1000:\n");
            pythonScriptStringBuilder.append("    optimizer_auto = Adam(learning_rate=" + lstm.getRate() + ")\n");
            pythonScriptStringBuilder.append("else:\n");
            pythonScriptStringBuilder.append("    optimizer_auto = Adam(learning_rate=" + lstm.getRate() + ")\n");
            pythonScriptStringBuilder.append("    print('INFO: Large dataset detected, using Adam optimizer with provided learning rate.')\n");
        } else {
            pythonScriptStringBuilder.append("if X_train.shape[0] < 1000:\n");
            pythonScriptStringBuilder.append("    optimizer_auto = Adam(learning_rate=0.01)  # default for smaller datasets\n");
            pythonScriptStringBuilder.append("else:\n");
            pythonScriptStringBuilder.append("    optimizer_auto = Adam()\n");
            pythonScriptStringBuilder.append("    print('INFO: Large dataset detected, using default Adam optimizer.')\n");
        }
        optimizer_str = "optimizer_auto";
        
        // AutoML-specific tuning parameters for batch size and epochs.
        pythonScriptStringBuilder.append("hidden_layer_sizes_auto = (64, 32)\n");
        pythonScriptStringBuilder.append("if X_train.shape[0] < 500:\n");
        pythonScriptStringBuilder.append("    batch_size_auto = 8\n");
        pythonScriptStringBuilder.append("    epochs_auto = 30\n");
        pythonScriptStringBuilder.append("elif X_train.shape[0] < 2000:\n");
        pythonScriptStringBuilder.append("    batch_size_auto = 16\n");
        pythonScriptStringBuilder.append("    epochs_auto = 50\n");
        pythonScriptStringBuilder.append("else:\n");
        pythonScriptStringBuilder.append("    batch_size_auto = 32\n");
        pythonScriptStringBuilder.append("    epochs_auto = 100\n");
        batch_size_str = "batch_size_auto";
        epochs_str = "epochs_auto";
        
        // AutoML selection for activations, dropout, and regularization
        pythonScriptStringBuilder.append("input_activation_auto = 'relu'\n");
        input_activation_str = "input_activation_auto";
        pythonScriptStringBuilder.append("hidden_activation_auto = 'tanh'\n");
        hidden_activation_str = "hidden_activation_auto";
        pythonScriptStringBuilder.append("output_activation_auto = 'linear'\n");
        output_activation_str = "output_activation_auto";
        pythonScriptStringBuilder.append("dropout_rate_auto = " + dropout_rate_str + "\n");
        dropout_rate_str = "dropout_rate_auto";
        pythonScriptStringBuilder.append("regularizer_auto = l2(0.01)\n");
        l2_reg_str = "regularizer_auto";
        pythonScriptStringBuilder.append("metrics_auto = 'mse'\n");
        metrics_str = "metrics_auto";
    } else {
        // Non-AutoML branch: Use DSL-specified or default parameters.
        if (lstm.getHidden_layer_sizes() == null || lstm.getHidden_layer_sizes().isEmpty()) {
            System.err.println("WARNING: Hidden layer sizes not specified. Defaulting to (64, 32).");
        }
        if (lstm.getInput_activation() != null && lstm.getInput_activation() != Activation.NOT_SET) {
            input_activation_str = "'" + lstm.getInput_activation().getLiteral() + "'";
        } else {
            input_activation_str = "'relu'";
            System.err.println("WARNING: Input activation not specified. Defaulting to 'relu'.");
        }
        if (lstm.getHidden_activation() != null && lstm.getHidden_activation() != Activation.NOT_SET) {
            hidden_activation_str = "'" + lstm.getHidden_activation().getLiteral() + "'";
        } else {
            hidden_activation_str = "'tanh'";
            System.err.println("WARNING: Hidden activation not specified. Defaulting to 'tanh'.");
        }
        if (lstm.getOutput_activation() != null && lstm.getOutput_activation() != Activation.NOT_SET) {
            output_activation_str = "'" + lstm.getOutput_activation().getLiteral() + "'";
        } else {
            output_activation_str = "'linear'";
            System.err.println("WARNING: Output activation not specified. Defaulting to 'linear'.");
        }
        if (lstm.getOptimizer() != null && lstm.getOptimizer() != Optimizer.NOT_SET) {
            optimizer_str = "'" + lstm.getOptimizer().getLiteral() + "'";
        } else {
            optimizer_str = "'adam'";
            System.err.println("WARNING: Optimizer not specified. Defaulting to 'adam'.");
        }
        if (lstm.getBatch_size() != null) {
            batch_size_str = String.valueOf(lstm.getBatch_size().getIntValue());
        } else {
            batch_size_str = "32";
            System.err.println("WARNING: Batch size not specified. Defaulting to 32.");
        }
        if (lstm.getEpochs() != null) {
            epochs_str = String.valueOf(lstm.getEpochs().getIntValue());
        } else {
            epochs_str = "100";
            System.err.println("WARNING: Epochs not specified. Defaulting to 100.");
        }
        if (lstm.getMetrics() != null && lstm.getMetrics() != Metrics.NOT_SET) {
            metrics_str = "'" + lstm.getMetrics().getLiteral() + "'";
        } else {
            metrics_str = "'mse'";
            System.err.println("WARNING: Metrics not specified. Defaulting to 'mse'.");
        }
        if (lstm.getRegularization() != null && !lstm.getRegularization().getLiteral().equals("NOT_SET")) {
            l2_reg_str = lstm.getRegularization().getLiteral();
        } else {
            l2_reg_str = "l2(0.01)";
            System.err.println("WARNING: Regularization not specified. Defaulting to 'l2(0.01)'.");
        }
    }
    
    // Hyperparameter Tuning (only if tuning is enabled and AutoML is off)
    boolean hyperTuningOn = (!autoML && action.getDataAnalytics().getHyperparameter_tuning() != null 
                             && action.getDataAnalytics().getHyperparameter_tuning() != HyperparameterTuning.OFF);
    if (hyperTuningOn) {
        HyperparameterTuning tuningMethod = action.getDataAnalytics().getHyperparameter_tuning();
        switch (tuningMethod) {
            case GRID_SEARCH:
                pythonScriptStringBuilder.append("from tensorflow.keras.wrappers.scikit_learn import KerasRegressor\n");
                pythonScriptStringBuilder.append("def build_model(units, dropout):\n");
                pythonScriptStringBuilder.append("    model = Sequential()\n");
                pythonScriptStringBuilder.append("    model.add(LSTM(units, activation=" + input_activation_str + ", input_shape=(X_train.shape[1], X_train.shape[2]), return_sequences=True, kernel_regularizer=" + l2_reg_str + "))\n");
                pythonScriptStringBuilder.append("    model.add(Dropout(dropout))\n");
                pythonScriptStringBuilder.append("    model.add(Dense(" + nSteps + ", activation=" + output_activation_str + ", kernel_regularizer=" + l2_reg_str + "))\n");
                pythonScriptStringBuilder.append("    model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=['mse'])\n");
                pythonScriptStringBuilder.append("    return model\n\n");
                pythonScriptStringBuilder.append("model_wrapper = KerasRegressor(build_fn=build_model, verbose=0)\n");
                pythonScriptStringBuilder.append("param_grid = {'units': [64, 128, 256], 'dropout': [0.2, 0.3, 0.5], 'batch_size': [16, 32, 64], 'epochs': [50, 100, 150]}\n");
                pythonScriptStringBuilder.append("from sklearn.model_selection import GridSearchCV\n");
                pythonScriptStringBuilder.append("grid = GridSearchCV(estimator=model_wrapper, param_grid=param_grid, scoring='neg_mean_squared_error')\n");
                pythonScriptStringBuilder.append("grid_result = grid.fit(X_train, y_train)\n\n");
                pythonScriptStringBuilder.append("best_model = grid_result.best_estimator_.model\n");
                System.out.println("INFO: Hyperparameter Tuning enabled using Grid Search.");
                break;
            case RANDOM_SEARCH:
                pythonScriptStringBuilder.append("from tensorflow.keras.wrappers.scikit_learn import KerasRegressor\n");
                pythonScriptStringBuilder.append("def build_model(units, dropout):\n");
                pythonScriptStringBuilder.append("    model = Sequential()\n");
                pythonScriptStringBuilder.append("    model.add(LSTM(units, activation=" + input_activation_str + ", input_shape=(X_train.shape[1], X_train.shape[2]), return_sequences=True, kernel_regularizer=" + l2_reg_str + "))\n");
                pythonScriptStringBuilder.append("    model.add(Dropout(dropout))\n");
                pythonScriptStringBuilder.append("    model.add(Dense(" + nSteps + ", activation=" + output_activation_str + ", kernel_regularizer=" + l2_reg_str + "))\n");
                pythonScriptStringBuilder.append("    model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=['mse'])\n");
                pythonScriptStringBuilder.append("    return model\n\n");
                pythonScriptStringBuilder.append("model_wrapper = KerasRegressor(build_fn=build_model, verbose=0)\n");
                pythonScriptStringBuilder.append("param_dist = {'units': [64, 128, 256], 'dropout': [0.2, 0.3, 0.5], 'batch_size': [16, 32, 64], 'epochs': [50, 100, 150]}\n");
                pythonScriptStringBuilder.append("from sklearn.model_selection import RandomizedSearchCV\n");
                pythonScriptStringBuilder.append("random_search = RandomizedSearchCV(estimator=model_wrapper, param_distributions=param_dist, n_iter=10, scoring='neg_mean_squared_error')\n");
                pythonScriptStringBuilder.append("random_result = random_search.fit(X_train, y_train)\n\n");
                pythonScriptStringBuilder.append("best_model = random_result.best_estimator_.model\n");
                System.out.println("INFO: Hyperparameter Tuning enabled using Random Search.");
                break;
            case BAYESIAN_OPTIMIZATION:
                pythonScriptStringBuilder.append("import keras_tuner as kt\n");
                pythonScriptStringBuilder.append("def build_model(hp):\n");
                pythonScriptStringBuilder.append("    model = Sequential()\n");
                pythonScriptStringBuilder.append("    model.add(LSTM(hp.Int('units', min_value=32, max_value=512, step=32), activation=" + input_activation_str + ", input_shape=(X_train.shape[1], X_train.shape[2]), return_sequences=True, kernel_regularizer=" + l2_reg_str + "))\n");
                pythonScriptStringBuilder.append("    model.add(Dropout(hp.Float('dropout', min_value=0.0, max_value=0.5, step=0.1)))\n");
                pythonScriptStringBuilder.append("    model.add(Dense(" + nSteps + ", activation=" + output_activation_str + ", kernel_regularizer=" + l2_reg_str + "))\n\n");
                pythonScriptStringBuilder.append("    model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=['mse'])\n");
                pythonScriptStringBuilder.append("    return model\n\n");
                pythonScriptStringBuilder.append("tuner = kt.BayesianOptimization(build_model, objective='val_loss', max_trials=5)\n");
                pythonScriptStringBuilder.append("tuner.search(X_train, y_train, epochs=" + epochs_str + ", validation_data=(X_test, y_test))\n");
                pythonScriptStringBuilder.append("best_model = tuner.get_best_models(num_models=1)[0]\n\n");
                System.out.println("INFO: Hyperparameter Tuning enabled using Bayesian Optimization.");
                break;
            default:
                break;
        }
    }
    
    // Build final model: if hyperparameter tuning was applied, use best_model; otherwise, build from scratch.
    if (hyperTuningOn) {
        pythonScriptStringBuilder.append("try:\n");
        pythonScriptStringBuilder.append("    model = best_model\n");
        pythonScriptStringBuilder.append("    print('INFO: Using best model from hyperparameter tuning for prediction.')\n");
        pythonScriptStringBuilder.append("except NameError:\n");
        pythonScriptStringBuilder.append("    print('WARNING: best_model not found, building model from scratch.')\n");
        pythonScriptStringBuilder.append("    model = Sequential()\n");
    } else {
        pythonScriptStringBuilder.append("model = Sequential()\n");
        pythonScriptStringBuilder.append("print('INFO: Building LSTM model with hidden layers:', " + layerSizes + ")\n");
        for (int i = 0; i < layerSizes.size(); i++) {
            int layerSize = layerSizes.get(i);
            // Set return_sequences to True for all but the last LSTM layer.
            String returnSeq = (i < layerSizes.size() - 1) ? "True" : "False";
            if (i == 0) {
                pythonScriptStringBuilder.append("model.add(LSTM(" + layerSize + ", activation=" + input_activation_str + ", return_sequences=" + returnSeq + ", input_shape=(X_train.shape[1], X_train.shape[2]), kernel_regularizer=" + l2_reg_str + "))\n");
            } else {
                pythonScriptStringBuilder.append("model.add(LSTM(" + layerSize + ", activation=" + hidden_activation_str + ", return_sequences=" + returnSeq + ", kernel_regularizer=" + l2_reg_str + "))\n");
            }
            pythonScriptStringBuilder.append("model.add(Dropout(" + dropout_rate_str + "))\n");
        }
        pythonScriptStringBuilder.append("model.add(Dense(" + nSteps + ", activation=" + output_activation_str + ", kernel_regularizer=" + l2_reg_str + "))\n\n");
    }
    
    // Final model compilation and training; include the callbacks list.
    pythonScriptStringBuilder.append("model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=[" + metrics_str + "])\n");
    pythonScriptStringBuilder.append("history = model.fit(X_train, y_train, epochs=" + epochs_str + ", batch_size=" + batch_size_str + ", validation_data=(X_test, y_test), callbacks=callbacks)\n");
    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");
    
    // Save the model using Keras's built-in save method in HDF5 format.
    pythonScriptStringBuilder.append("model.save('" + path_str + "/python-scripts/pickles/lstm_model.h5')\n\n");
}
  
    
    
    
    
    
    
    

	private void generatePythonDATrainScriptRNN(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
	    	generatePythonKerasTensorFlowDATrainScriptRNN(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}
	
	
	
	private void generatePythonKerasTensorFlowDATrainScriptRNN(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    // Initialize parameter strings with defaults
	    String hidden_layer_sizes_str = "64, 32";  // Default hidden layer sizes
	    String activation_str = "relu";  // Default activation function for all layers
	    String optimizer_str = "adam";  // Default optimizer
	    String batch_size_str = "32";  // Default batch size
	    String epochs_str = "100";  // Default epochs
	    String dropout_rate_str = "0.2";  // Default dropout rate
	    String l2_reg_str = "l2(0.01)";  // Default L2 regularization
	    String metrics_str = "mse";  // Default metric for regression
	    boolean set_optimizer_automatically = false;
	    boolean forecasting_plots = false;
	    boolean overfitting_plots = false;

	    RNN rnn = (RNN) action.getDataAnalytics().getModelAlgorithm();
	    Long nStep = action.getDataAnalytics().getSteps() != null ? action.getDataAnalytics().getSteps().getIntValue() : 1L;

	    // AutoML Handling
	    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
	        System.out.println("INFO: AutoML is ON. Automatically tuning parameters based on data size and characteristics...\n");

	        // Automatically set optimizer based on dataset size
	        pythonScriptStringBuilder.append("if X_train.shape[0] < 1000:\n");
	        pythonScriptStringBuilder.append("    optimizer_auto = 'lbfgs'\n");
	        pythonScriptStringBuilder.append("    print('INFO: Small dataset detected, using lbfgs optimizer for faster convergence.')\n");
	        pythonScriptStringBuilder.append("else:\n");
	        pythonScriptStringBuilder.append("    optimizer_auto = 'adam'\n");
	        pythonScriptStringBuilder.append("    print('INFO: Large dataset detected, using adam optimizer for better performance.')\n");
	        optimizer_str = "optimizer_auto";
	        set_optimizer_automatically = true;

	        // Auto-tune other settings
	        pythonScriptStringBuilder.append("hidden_layer_sizes_auto = (64, 32)\n");
	        hidden_layer_sizes_str = "hidden_layer_sizes_auto";
	        pythonScriptStringBuilder.append("batch_size_auto = 16 if X_train.shape[0] < 1000 else 32\n");
	        batch_size_str = "batch_size_auto";
	        pythonScriptStringBuilder.append("epochs_auto = 50 if X_train.shape[0] < 10000 else 100\n");
	        epochs_str = "epochs_auto";
	    } else {
	        // AutoML is OFF: Use user-specified or default parameters

	        // Hidden Layer Sizes
	        if (rnn.getHidden_layer_sizes() != null && !rnn.getHidden_layer_sizes().isEmpty()) {
	            hidden_layer_sizes_str = rnn.getHidden_layer_sizes();
	        } else {
	            System.err.println("WARNING: Hidden layer sizes not specified. Defaulting to (64, 32).");
	        }

	        // Activation function
	        if (rnn.getActivation() != Activation.NOT_SET) {
	            activation_str = rnn.getActivation().getLiteral();
	        }

	        // Optimizer
	        if (rnn.getOptimizer() != Optimizer.NOT_SET) {
	            optimizer_str = rnn.getOptimizer().getLiteral();
	        } else {
	            System.err.println("WARNING: Optimizer not specified. Defaulting to 'adam'.");
	        }

	        // Batch Size
	        if (rnn.getBatch_size() != null) {
	            batch_size_str = String.valueOf(rnn.getBatch_size().getIntValue());
	        } else {
	            System.err.println("WARNING: Batch size not specified. Defaulting to 32.");
	        }

	        // Epochs
	        if (rnn.getEpochs() != null) {
	            epochs_str = String.valueOf(rnn.getEpochs().getIntValue());
	        } else {
	            System.err.println("WARNING: Number of epochs not specified. Defaulting to 100.");
	        }

	       

	        // L2 Regularization
	        if (rnn.getRegularization() != null) {
	            l2_reg_str = "l2(" + String.valueOf(rnn.getRegularization().getValue()) + ")";
	        } else {
	            System.err.println("WARNING: L2 regularization not specified. Defaulting to 0.01.");
	        }

	        // Forecasting and overfitting plots
	        forecasting_plots = rnn.getForecasting_plots() != null;
	        overfitting_plots = rnn.getOverfitting_Plots() != null;
	    }

	    // Build the Python code for training the RNN model
	    pythonScriptStringBuilder.append("from tensorflow.keras.models import Sequential\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.layers import SimpleRNN, Dense, Dropout\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.regularizers import " + l2_reg_str.split("\\(")[0] + "\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.optimizers import " + optimizer_str + "\n");
	    pythonScriptStringBuilder.append("import tensorflow.keras.backend as K\n");
	    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
	    pythonScriptStringBuilder.append("import pickle\n\n");

	    if (metrics_str.equals("rmse")) {
	        pythonScriptStringBuilder.append("def rmse(y_true, y_pred):\n");
	        pythonScriptStringBuilder.append("    return K.sqrt(K.mean(K.square(y_pred - y_true)))\n\n");
	        metrics_str = "rmse";
	    }
	    long nSteps = action.getDataAnalytics().getSteps() != null ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	    // Define the Sequential RNN model with the specified layers and regularization
	    pythonScriptStringBuilder.append("model = Sequential()\n");

	    // Input RNN layer with activation and regularization
	    pythonScriptStringBuilder.append("model.add(SimpleRNN(" + hidden_layer_sizes_str.split(",")[0] + ", activation='" + activation_str + "', input_shape=(X_train.shape[1], X_train.shape[2]), kernel_regularizer=" + l2_reg_str + ", return_sequences=True))\n");
	    pythonScriptStringBuilder.append("model.add(Dropout(" + dropout_rate_str + "))\n");

	    // Add hidden RNN layers with dropout
	    String[] hiddenLayers = hidden_layer_sizes_str.split(",");
	    for (int i = 1; i < hiddenLayers.length; i++) {
	        pythonScriptStringBuilder.append("model.add(SimpleRNN(" + hiddenLayers[i].trim() + ", activation='" + activation_str + "', return_sequences=" + (i < hiddenLayers.length - 1) + "))\n");
	        pythonScriptStringBuilder.append("model.add(Dropout(" + dropout_rate_str + "))\n");
	    }

	    // Output layer for regression
	    pythonScriptStringBuilder.append("model.add(Dense(" + nStep + ", activation='linear'))\n\n");

	    // Compile the model
	    pythonScriptStringBuilder.append("model.compile(optimizer=" + optimizer_str + ", loss='mse', metrics=[" + metrics_str + "])\n");

	    // Train the model
	    pythonScriptStringBuilder.append("history = model.fit(X_train, y_train, epochs=" + epochs_str + ", batch_size=" + batch_size_str + ", validation_data=(X_test, y_test))\n\n");

	    // Optionally, generate forecasting and overfitting plots
	    

	   
	    // Generate predictions and save the model using pickle
	    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");
	    pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/rnn_model.h5', 'wb') as pickle_file:\n");
	    pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
	    }
	

	
	private void generatePythonDATrainScriptTCN(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
	        generatePythonKerasTensorFlowDATrainScriptTCN(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	            + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	            + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	            + "You may try @dalib=\"auto\" instead.\n");
	    }
	}

	private void generatePythonKerasTensorFlowDATrainScriptTCN(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    List<Integer> hiddenLayerSizes = new ArrayList<>();
	    List<Integer> denseLayerSizes = new ArrayList<>();
	    TCN tcn = (TCN) action.getDataAnalytics().getModelAlgorithm();

	    // Default parameters
	    String nb_filters_str = "64";
	    String kernel_size_str = "2";
	    String dilations_str = "[1, 2, 4, 8]";
	    String nb_stacks_str = "1";
	    String dropout_rate_str = "0.1";
	    String activation_str = "'relu'";
	    String optimizer_str = "'adam'";
	    String batch_size_str = "32";
	    String epochs_str = "100";
	    String learning_rate_str = "0.001";
	    String loss_str = "'mse'";
	    String metrics_str = "'mse'";
	    boolean forecasting_plots = false;
	    boolean overfitting_plots = false;

	    if (autoML && action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
	        System.out.println("INFO: AutoML is ON. Automatically tuning TCN parameters...");
	        // Add AutoML logic here in future
	    } else {
	    	

	    	// Parse hidden_layers string
	    	if (tcn.getHidden_layers() != null) {
	    	    String[] hiddenStr = tcn.getHidden_layers().split(",");
	    	    for (String s : hiddenStr) {
	    	        hiddenLayerSizes.add(Integer.parseInt(s.trim()));
	    	    }
	    	} else {
	    	    hiddenLayerSizes.add(128);
	    	    hiddenLayerSizes.add(64);
	    	}

	    	// Parse dense_layers string
	    	if (tcn.getDense_layers() != null) {
	    	    String[] denseStr = tcn.getDense_layers().split(",");
	    	    for (String s : denseStr) {
	    	        denseLayerSizes.add(Integer.parseInt(s.trim()));
	    	    }
	    	} else {
	    	    denseLayerSizes.add(64);
	    	    denseLayerSizes.add(32);
	    	}


	        if (tcn.getNb_filters() != null) nb_filters_str = String.valueOf(tcn.getNb_filters());
	        if (tcn.getKernel_size() != null) kernel_size_str = String.valueOf(tcn.getKernel_size());
	        if (tcn.getDilations() != null) dilations_str = tcn.getDilations();
	        if (tcn.getNb_stacks() != null) nb_stacks_str = String.valueOf(tcn.getNb_stacks());
	        double dr = tcn.getDropout_rate();           // primitive, never null
	        if (dr != 0.1) {                             // 0.1 is your current default
	            dropout_rate_str = String.valueOf(dr);
	        }	        
	        if (tcn.getActivation() != null) activation_str = "'" + tcn.getActivation().getLiteral() + "'";
	        if (tcn.getOptimizer() != null) optimizer_str = "'" + tcn.getOptimizer().getLiteral() + "'";
	        if (tcn.getBatch_size() != null) batch_size_str = String.valueOf(tcn.getBatch_size());
	        if (tcn.getEpochs() != null) epochs_str = String.valueOf(tcn.getEpochs());
	        double lr = tcn.getLearning_rate(); // primitive
	        if (lr != 0.0) {
	            learning_rate_str = String.valueOf(lr);
	        }	        if (tcn.getLoss() != null) loss_str = "'" + tcn.getLoss().getLiteral() + "'";
	        if (tcn.getMetrics() != null) metrics_str = "'" + tcn.getMetrics().getLiteral() + "'";
	        forecasting_plots = tcn.getForecasting_plots() != null;
	        overfitting_plots = tcn.getOverfitting_Plots() != null;
	    }

	    long nSteps = action.getDataAnalytics().getSteps() != null ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	    pythonScriptStringBuilder.append("import pickle\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.models import Sequential\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.layers import Dense, Dropout, Conv1D\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.optimizers import Adam\n");
	    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n\n");

	    pythonScriptStringBuilder.append("model = Sequential()\n");
	    for (Integer size : hiddenLayerSizes) {
	        pythonScriptStringBuilder.append("model.add(Dense(" + size + ", activation=" + activation_str + "))\n");
	    }

	    pythonScriptStringBuilder.append("model.add(Conv1D(filters=" + nb_filters_str + ", kernel_size=" + kernel_size_str + ", activation=" + activation_str + ", dilation_rate=" + dilations_str + "))\n");
	    pythonScriptStringBuilder.append("model.add(Dropout(" + dropout_rate_str + "))\n");

	    for (Integer size : denseLayerSizes) {
	        pythonScriptStringBuilder.append("model.add(Dense(" + size + ", activation=" + activation_str + "))\n");
	    }

	    pythonScriptStringBuilder.append("model.add(Dense(" + nSteps + ", activation='linear'))\n\n");
	    pythonScriptStringBuilder.append("model.compile(optimizer=" + optimizer_str + ", loss=" + loss_str + ", metrics=[" + metrics_str + "])\n");
	    pythonScriptStringBuilder.append("history = model.fit(X_train, y_train, epochs=" + epochs_str + ", batch_size=" + batch_size_str + ", validation_data=(X_test, y_test))\n");
	    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");
	    pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/tcn_model.pickle', 'wb') as pickle_file:\n");
	    pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
	}

	    
	    
	    // Generate and save forecasting plots if enabled
	   
	


	

	private void generatePythonDATrainScriptTransformer(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
	        generatePythonKerasTensorFlowDATrainScriptTransformer(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}

	private void generatePythonKerasTensorFlowDATrainScriptTransformer(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    String hidden_layers_str = "128,64";
	    String num_layers_str = "4";
	    String d_model_str = "128";
	    String num_heads_str = "8";
	    String dff_str = "512";
	    String dropout_rate_str = "0.1";
	    String activation_str = "relu";
	    String optimizer_str = "adam";
	    String batch_size_str = "32";
	    String epochs_str = "100";
	    String learning_rate_str = "0.001";
	    String loss_str = "mse";
	    String metrics_str = "accuracy";
	    String dense_layers_str = "64,32";
	    boolean forecasting_plots = false;
	    boolean overfitting_plots = false;

	    Transformer transformer = (Transformer) action.getDataAnalytics().getModelAlgorithm();
	    long nSteps = action.getDataAnalytics().getSteps() != null ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	    if (autoML && action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
	        System.out.println("INFO: AutoML is ON. Automatically tuning Transformer parameters...");
	    } else {
	        hidden_layers_str = transformer.getHidden_layers() != null ? String.join(",", transformer.getHidden_layers()) : hidden_layers_str;
	        num_layers_str = transformer.getNum_layers() != null ? String.valueOf(transformer.getNum_layers()) : num_layers_str;
	        d_model_str = transformer.getD_model() != null ? String.valueOf(transformer.getD_model()) : d_model_str;
	        num_heads_str = transformer.getNum_heads() != null ? String.valueOf(transformer.getNum_heads()) : num_heads_str;
	        dff_str = transformer.getDff() != null ? String.valueOf(transformer.getDff()) : dff_str;
	        double dr = transformer.getDropout_rate();           // primitive, never null
	        if (dr != 0.1) {                             // 0.1 is your current default
	            dropout_rate_str = String.valueOf(dr);
	        }

	        
	        
	        activation_str = transformer.getActivation() != null ? transformer.getActivation().getLiteral() : activation_str;
	        optimizer_str = transformer.getOptimizer() != null ? transformer.getOptimizer().getLiteral() : optimizer_str;
	        batch_size_str = transformer.getBatch_size() != null ? String.valueOf(transformer.getBatch_size()) : batch_size_str;
	        epochs_str = transformer.getEpochs() != null ? String.valueOf(transformer.getEpochs()) : epochs_str;
	        double lr = transformer.getLearning_rate();          // cannot be null
	        if (lr != 0.0) {                             // 0.0 is your default
	            learning_rate_str = String.valueOf(lr);  // overwrite only if user set it
	        }	        
	        loss_str = transformer.getLoss() != null ? transformer.getLoss().getLiteral() : loss_str;
	        metrics_str = transformer.getMetrics() != null ? transformer.getMetrics().getLiteral() : metrics_str;
	        dense_layers_str = transformer.getDense_layers() != null ? String.join(",", transformer.getDense_layers()) : dense_layers_str;
	        forecasting_plots = transformer.getForecasting_plots() != null;
	        overfitting_plots = transformer.getOverfitting_Plots() != null;
	    }

	    pythonScriptStringBuilder.append("import tensorflow as tf\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.models import Sequential\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.layers import Dense, Dropout, LayerNormalization, MultiHeadAttention, Input\n");
	    pythonScriptStringBuilder.append("from tensorflow.keras.optimizers import " + optimizer_str + "\n");
	    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n\n");

	    pythonScriptStringBuilder.append("class CustomTransformerLayer(tf.keras.layers.Layer):\n");
	    pythonScriptStringBuilder.append("    def __init__(self, d_model, num_heads, dff, rate=0.1):\n");
	    pythonScriptStringBuilder.append("        super(CustomTransformerLayer, self).__init__()\n");
	    pythonScriptStringBuilder.append("        self.mha = MultiHeadAttention(num_heads=num_heads, key_dim=d_model)\n");
	    pythonScriptStringBuilder.append("        self.ffn = tf.keras.Sequential([\n");
	    pythonScriptStringBuilder.append("            Dense(dff, activation='relu'),\n");
	    pythonScriptStringBuilder.append("            Dense(d_model)\n");
	    pythonScriptStringBuilder.append("        ])\n");
	    pythonScriptStringBuilder.append("        self.layernorm1 = LayerNormalization(epsilon=1e-6)\n");
	    pythonScriptStringBuilder.append("        self.layernorm2 = LayerNormalization(epsilon=1e-6)\n");
	    pythonScriptStringBuilder.append("        self.dropout1 = Dropout(rate)\n");
	    pythonScriptStringBuilder.append("        self.dropout2 = Dropout(rate)\n\n");
	    pythonScriptStringBuilder.append("    def call(self, x):\n");
	    pythonScriptStringBuilder.append("        attn_output = self.mha(x, x)\n");
	    pythonScriptStringBuilder.append("        out1 = self.layernorm1(x + self.dropout1(attn_output))\n");
	    pythonScriptStringBuilder.append("        ffn_output = self.ffn(out1)\n");
	    pythonScriptStringBuilder.append("        return self.layernorm2(out1 + self.dropout2(ffn_output))\n\n");

	    pythonScriptStringBuilder.append("model = Sequential()\n");
	    for (String hiddenLayerSize : hidden_layers_str.split(",")) {
	        pythonScriptStringBuilder.append("model.add(Dense(" + hiddenLayerSize + ", activation='" + activation_str + "'))\n");
	    }
	    pythonScriptStringBuilder.append("model.add(tf.keras.layers.Reshape((1, -1)))\n");
	    pythonScriptStringBuilder.append("for _ in range(" + num_layers_str + "):\n");
	    pythonScriptStringBuilder.append("    model.add(CustomTransformerLayer(d_model=" + d_model_str + ", num_heads=" + num_heads_str + ", dff=" + dff_str + ", rate=" + dropout_rate_str + "))\n");
	    pythonScriptStringBuilder.append("model.add(tf.keras.layers.Flatten())\n");
	    for (String denseLayerSize : dense_layers_str.split(",")) {
	        pythonScriptStringBuilder.append("model.add(Dense(" + denseLayerSize + ", activation='" + activation_str + "'))\n");
	    }
	    pythonScriptStringBuilder.append("model.add(Dense(" + nSteps + ", activation='linear'))\n\n");

	    pythonScriptStringBuilder.append("model.compile(optimizer=" + optimizer_str + ", loss='" + loss_str + "', metrics=['" + metrics_str + "'])\n");

	    if (transformer.getEarly_stopping() != null && transformer.getEarly_stopping() == EarlyStopping.ENABLE) {
	        pythonScriptStringBuilder.append("early_stopping = tf.keras.callbacks.EarlyStopping(monitor='val_loss', patience=5, restore_best_weights=True)\n");
	        pythonScriptStringBuilder.append("callbacks = [early_stopping]\n");
	    } else {
	        pythonScriptStringBuilder.append("callbacks = []\n");
	    }

	    pythonScriptStringBuilder.append("history = model.fit(X_train, y_train, epochs=" + epochs_str + ", batch_size=" + batch_size_str + ", validation_data=(X_test, y_test), callbacks=callbacks)\n");
	    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");
	    pythonScriptStringBuilder.append("import pickle\n");
	    pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/transformer_model.h5', 'wb') as pickle_file:\n");
	    pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
	}
	
//////Statistical 

private void generatePythonDATrainScriptARIMA(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
 if (dalib.equals("auto") || dalib.equals("statsmodels")) {
     generatePythonStatsmodelsDATrainScriptARIMA(path_str, action, pythonScriptStringBuilder, autoML);
 } else {
     System.err.println("ERROR: " + dalib
             + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
             + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
             + "You may try @dalib=\"auto\" instead.\n");
 }
}

private void generatePythonStatsmodelsDATrainScriptARIMA(
    String path_str,
    DATrainAction action,
    StringBuilder pythonScriptStringBuilder,
    boolean autoML
) {
    // Initialize ARIMA parameters with default values
    String p_str = "1";
    String d_str = "1";
    String q_str = "1";
    String trend_str = "'c'";
    String seasonalP_str = "0";
    String seasonalD_str = "0";
    String seasonalQ_str = "0";
    String seasonality_str = "1"; // Default seasonality

    // Retrieve model configuration and target column
    ARIMA arima = (ARIMA) action.getDataAnalytics().getModelAlgorithm();
    long nSteps = action.getDataAnalytics().getSteps() != null
        ? action.getDataAnalytics().getSteps().getIntValue()
        : 1;
    String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();
    String outFeat = targetColumn; // alias for consistency

    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        // AutoML branch
        System.out.println("INFO: AutoML is ON. Automatically tuning ARIMA parameters...\n");
        pythonScriptStringBuilder.append("from pmdarima import auto_arima\n");
        pythonScriptStringBuilder.append("import pickle\n\n");

        // Dump and reload raw series
        //pythonScriptStringBuilder.append(
         //   "with open('" + path_str + "/python-scripts/pickles/df_arima.pickle', 'wb') as pf:\n" +
         //   "    pickle.dump(df['" + targetColumn + "'], pf)\n\n"
       // );
      //  pythonScriptStringBuilder.append(
         //   "with open('" + path_str + "/python-scripts/pickles/df_arima.pickle', 'rb') as pf:\n" +
           // "    " + outFeat + "_series = pickle.load(pf)\n\n"
      //  );

        // Auto-tune ARIMA
        pythonScriptStringBuilder.append(
            "model = auto_arima(" + outFeat + "_series, seasonal=True, m=" + seasonality_str + ","
            + " trace=True, error_action='ignore', suppress_warnings=True)\n"
        );
        pythonScriptStringBuilder.append("model_fit = model.fit(" + outFeat + "_series)\n\n");
        pythonScriptStringBuilder.append("print(model_fit.summary())\n\n");
        pythonScriptStringBuilder.append(
            "with open('" + path_str + "/python-scripts/pickles/auto_arima_model.pickle', 'wb') as pickle_file:\n" +
            "    pickle.dump(model_fit, pickle_file)\n\n"
        );

    } else {
        // Manual ARIMA parameters
        p_str = arima.getP() != null ? String.valueOf(arima.getP().getIntValue()) : p_str;
        d_str = arima.getDifferencing() != null ? String.valueOf(arima.getDifferencing().getIntValue()) : d_str;
        q_str = arima.getQ() != null ? String.valueOf(arima.getQ().getIntValue()) : q_str;
        trend_str = arima.getTrend() != null && !arima.getTrend().isEmpty() ? "'" + arima.getTrend() + "'" : trend_str;
        seasonalP_str = arima.getSeasonalP() != null ? String.valueOf(arima.getSeasonalP().getIntValue()) : seasonalP_str;
        seasonalD_str = arima.getSeasonalD() != null ? String.valueOf(arima.getSeasonalD().getIntValue()) : seasonalD_str;
        seasonalQ_str = arima.getSeasonalQ() != null ? String.valueOf(arima.getSeasonalQ().getIntValue()) : seasonalQ_str;
        seasonality_str = arima.getSeasonality() != null ? String.valueOf(arima.getSeasonality().getIntValue()) : seasonality_str;

        pythonScriptStringBuilder.append("from statsmodels.tsa.arima.model import ARIMA\n");
        pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
        pythonScriptStringBuilder.append("import pickle\n");
        pythonScriptStringBuilder.append("import os\n");
        pythonScriptStringBuilder.append("import numpy as np\n\n");

    

        // Define and fit ARIMA model using loaded series
        pythonScriptStringBuilder.append(
            "model = ARIMA(" + outFeat + "_series, order=(" + p_str + "," + d_str + "," + q_str + "), "
            + "seasonal_order=(" + seasonalP_str + "," + seasonalD_str + "," + seasonalQ_str + "," + seasonality_str + "), "
            + "trend=" + trend_str + ")\n"
        );
        pythonScriptStringBuilder.append("model_fit = model.fit()\n");
        pythonScriptStringBuilder.append("print(model_fit.summary())\n\n");

        // Plot the model's predictions
       // pythonScriptStringBuilder.append("model_fit.plot_predict(dynamic=False)\n");
    //    pythonScriptStringBuilder.append("plt.title('ARIMA Model Forecast vs Actual')\n");
      //  pythonScriptStringBuilder.append(
       //     "plt.savefig('" + path_str + "/arima_forecast_vs_actual.png')\n"
       // );
      //  pythonScriptStringBuilder.append("plt.show()\n\n");

        // Save fitted model
        pythonScriptStringBuilder.append(
            "with open('" + path_str + "/python-scripts/pickles/arima_model_fit.pickle', 'wb') as pickle_file:\n" +
            "    pickle.dump(model_fit, pickle_file)\n\n"
        );
    }
}






	
////////Statistical 


private void generatePythonDATrainScriptSARIMA(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    if (dalib.equals("auto") || dalib.equals("statsmodels")) {
        generatePythonStatsmodelsDATrainScriptSARIMA(path_str, action, pythonScriptStringBuilder, autoML);
    } else {
        System.err.println("ERROR: " + dalib
                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
                + "You may try @dalib=\"auto\" instead.\n");
    }
}


private void generatePythonStatsmodelsDATrainScriptSARIMA(
        String pathStr,
        DATrainAction action,
        StringBuilder sb,
        boolean autoML
) {
    // Default values
    String p = "1", d = "1", q = "1";
    String SP = "0", SD = "0", SQ = "0", m = "1";
    String trend = "'c'";

    SARIMA sarima = (SARIMA) action.getDataAnalytics().getModelAlgorithm();
    long nSteps = action.getDataAnalytics().getSteps() != null ? action.getDataAnalytics().getSteps().getIntValue() : 1;
    String outFeat = action.getDataAnalytics().getOutput_features().get(0).getName();

    sb.append("import os\n")
      .append("import pandas as pd\n")
      .append("from statsmodels.tsa.statespace.sarimax import SARIMAX\n")
      .append("import pickle\n\n")
      .append("import matplotlib.pyplot as plt\n\n")
      .append("os.makedirs('").append(pathStr).append("/python-scripts/pickles', exist_ok=True)\n\n")
      .append("series = ").append(outFeat).append("_series\n\n");

    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        sb.append("from pmdarima import auto_arima\n")
          .append("model = auto_arima(\n")
          .append("    series,\n")
          .append("    seasonal=True, m=").append(m).append(",\n")
          .append("    trace=True, error_action='ignore', suppress_warnings=True\n")
          .append(")\n")
          .append("model_fit = model.fit(series)\n")
          .append("print(model_fit.summary())\n")
          .append("with open('").append(pathStr).append("/python-scripts/pickles/auto_sarima_model_fit.pickle', 'wb') as f:\n")
          .append("    pickle.dump(model_fit, f)\n\n");
    } else {
        // Override defaults if user provided values
        if (sarima.getP()           != null) p     = sarima.getP().getIntValue() + "";
        if (sarima.getDifferencing()!= null) d     = sarima.getDifferencing().getIntValue() + "";
        if (sarima.getQ()           != null) q     = sarima.getQ().getIntValue() + "";
        if (sarima.getSeasonalP()   != null) SP    = sarima.getSeasonalP().getIntValue() + "";
        if (sarima.getSeasonalD()   != null) SD    = sarima.getSeasonalD().getIntValue() + "";
        if (sarima.getSeasonalQ()   != null) SQ    = sarima.getSeasonalQ().getIntValue() + "";
        if (sarima.getSeasonality() != null) m     = sarima.getSeasonality().getIntValue() + "";
        if (sarima.getTrend()       != null) trend = "'" + sarima.getTrend() + "'";
        
        
        sb.append("series = pd.to_numeric(").append(outFeat).append("_series, errors='coerce').dropna()\n\n");

        sb.append("model = SARIMAX(\n")
          .append("    series,\n")
          .append("    order=(").append(p).append(", ").append(d).append(", ").append(q).append("),\n")
          .append("    seasonal_order=(").append(SP).append(", ").append(SD).append(", ").append(SQ).append(", ").append(m).append("),\n")
          .append("    trend=").append(trend).append("\n")
          .append(")\n")
          .append("model_fit = model.fit()\n")
          .append("print(model_fit.summary())\n\n")
          .append("with open('").append(pathStr).append("/python-scripts/pickles/sarima_model_fit.pickle', 'wb') as f:\n")
          .append("    pickle.dump(model_fit, f)\n\n");
    }
}



////////Statistical 

private void generatePythonDATrainScriptHWES(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
if (dalib.equals("auto") || dalib.equals("statsmodels")) {
generatePythonStatsmodelsDATrainScriptHWES(path_str, action, pythonScriptStringBuilder, autoML);
} else {
System.err.println("ERROR: " + dalib
       + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
       + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
       + "You may try @dalib=\"auto\" instead.\n"); // Fixed error message
}
}



private void generatePythonStatsmodelsDATrainScriptHWES(
      String pathStr,
        DATrainAction action,
        StringBuilder sb,
        boolean autoML
) {
    // Default HWES params
    String trend = "'add'";
    String damped = "False";
    String seasonal = "'add'";
    String m = "12";  // seasonal_periods
    String initMethod = "None";
    String useBoxcox = "False";
    String removeBias = "False";
    String optimized = "True";

    // Grab model and parameters
    HWES hwes = (HWES) action.getDataAnalytics().getModelAlgorithm();
    long nSteps = action.getDataAnalytics().getSteps() != null
        ? action.getDataAnalytics().getSteps().getIntValue()
        : 1;
    String outFeat = action.getDataAnalytics()
                          .getOutput_features()
                          .get(0)
                          .getName();

    // Begin script: imports & ensure pickles dir exists
    sb.append("import os\n")
      .append("import pandas as pd\n")
      .append("from statsmodels.tsa.holtwinters import ExponentialSmoothing\n")
      .append("import pickle\n\n")
      .append("import matplotlib.pyplot as plt\n\n")
      .append("os.makedirs('").append(pathStr)
      .append("/python-scripts/pickles', exist_ok=True)\n\n")
      // load full series
      .append("series = ").append(outFeat).append("_series\n\n");

    // AutoML vs. manual branch
    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        // allow seasonal_periods override
        if (hwes.getSeasonal_periods() != null) {
            m = String.valueOf(hwes.getSeasonal_periods().getIntValue());
            if (Integer.parseInt(m) <= 0) { m = "12"; }
        }
        sb.append("from pmdarima import auto_arima\n")
          .append("model = auto_arima(\n")
          .append("    series,\n")
          .append("    seasonal=True, m=").append(m).append(",\n")
          .append("    trace=True, error_action='ignore', suppress_warnings=True\n")
          .append(")\n")
          .append("model_fit = model.fit(series)\n")
          .append("print(model_fit.summary())\n")
          .append("with open('").append(pathStr)
            .append("/python-scripts/pickles/auto_hwes_model_fit.pickle','wb') as f:\n")
          .append("    pickle.dump(model_fit, f)\n\n");

    } else {
        // override defaults if provided
        if (hwes.getTrend()               != null) trend       = "'" + hwes.getTrend() + "'";
        if (hwes.getDamped()              != null) damped      = hwes.getDamped().isBoolValue() ? "True" : "False";
        if (hwes.getSeasonal()            != null) seasonal    = "'" + hwes.getSeasonal() + "'";
        if (hwes.getSeasonal_periods()    != null) m           = String.valueOf(hwes.getSeasonal_periods().getIntValue());
        if (hwes.getInitialization_method()!= null) initMethod  = "'" + hwes.getInitialization_method() + "'";
        if (hwes.getUse_boxcox()          != null) useBoxcox   = hwes.getUse_boxcox().isBoolValue() ? "True" : "False";
        if (hwes.getRemove_bias()         != null) removeBias  = hwes.getRemove_bias().isBoolValue() ? "True" : "False";
        if (hwes.getOptimized()           != null) optimized    = hwes.getOptimized().isBoolValue() ? "True" : "False";

        // model definition
        
        sb.append("model = ExponentialSmoothing(\n")
        .append("    series,\n")
        .append("    trend=").append(trend)
        .append(", damped_trend=").append(damped)
        .append(", seasonal=").append(seasonal)
        .append(", seasonal_periods=").append(m);
      if (!"None".equals(initMethod)) {
          sb.append(", initialization_method=").append(initMethod);
      }
      sb.append(", use_boxcox=").append(useBoxcox)
        .append(", remove_bias=").append(removeBias)
        .append(")\n")
        .append("model_fit = model.fit(optimized=").append(optimized).append(")\n")
        .append("print(model_fit.summary())\n\n")
        // save the model
        .append("with open('").append(pathStr)
        .append("/python-scripts/pickles/hwes_model_fit.pickle', 'wb') as f:\n")
        .append("    pickle.dump(model_fit, f)\n\n");
  }
}


private void generatePythonDATrainScriptETS(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    if (dalib.equals("auto") || dalib.equals("statsmodels")) {
        generatePythonStatsmodelsDATrainScriptETS(path_str, action, pythonScriptStringBuilder, autoML);
    } else {
        System.err.println("ERROR: " + dalib
                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
                + "You may try @dalib=\"auto\" instead.\n");
    }
}

private void generatePythonStatsmodelsDATrainScriptETS(
        String pathStr,
        DATrainAction action,
        StringBuilder sb,
        boolean autoML
) {
    String errorComp = "'add'", trendComp = "'add'", seasonalComp = "'add'", dampedComp = "False";
    String m = "12", initMethod = "None", useBoxcox = "False", removeBias = "False", optimized = "True";

    ETS ets = (ETS) action.getDataAnalytics().getModelAlgorithm();
    long nSteps = action.getDataAnalytics().getSteps() != null ? action.getDataAnalytics().getSteps().getIntValue() : 1;
    String outFeat = action.getDataAnalytics().getOutput_features().get(0).getName();

    sb.append("import os\n")
      .append("import pandas as pd\n")
      .append("from statsmodels.tsa.holtwinters import ExponentialSmoothing\n")
      .append("import pickle\n\n")
      .append("import matplotlib.pyplot as plt\n\n")
      .append("os.makedirs('" + pathStr + "/python-scripts/pickles', exist_ok=True)\n\n")
      .append("series = df['" + outFeat + "']\n\n");

    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        sb.append("# AutoML not supported for ETS; using default optimized fit\n")
          .append("model = ExponentialSmoothing(\n")
          .append("    series,\n")
          .append("    error='add', trend='add', seasonal='add', seasonal_periods=").append(m).append("\n")
          .append(")\n")
          .append("model_fit = model.fit(optimized=True)\n")
          .append("print(model_fit.summary())\n\n")
          .append("with open('" + pathStr + "/python-scripts/pickles/auto_ets_model_fit.pickle','wb') as f:\n")
          .append("    pickle.dump(model_fit, f)\n\n");
    } else {
        if (ets.getError() != null && !ets.getError().isEmpty()) errorComp = "'" + ets.getError() + "'";
        if (ets.getTrend() != null && !ets.getTrend().isEmpty()) trendComp = "'" + ets.getTrend() + "'";
        if (ets.getSeasonal() != null && !ets.getSeasonal().isEmpty()) seasonalComp = "'" + ets.getSeasonal() + "'";
        if (ets.getDamped() != null) dampedComp = ets.getDamped().isBoolValue() ? "True" : "False";
        if (ets.getSeasonal_periods() != null) m = String.valueOf(ets.getSeasonal_periods().getIntValue());
        if (ets.getInitialization_method() != null && !ets.getInitialization_method().isEmpty()) initMethod = "'" + ets.getInitialization_method() + "'";
        if (ets.getUse_boxcox() != null) useBoxcox = ets.getUse_boxcox().isBoolValue() ? "True" : "False";
        if (ets.getRemove_bias() != null) removeBias = ets.getRemove_bias().isBoolValue() ? "True" : "False";
        if (ets.getOptimized() != null) optimized = ets.getOptimized().isBoolValue() ? "True" : "False";

        sb.append("model = ExponentialSmoothing(\n")
          .append("    series,\n")
          .append("    error=").append(errorComp)
          .append(", trend=").append(trendComp)
          .append(", damped_trend=").append(dampedComp)
          .append(", seasonal=").append(seasonalComp)
          .append(", seasonal_periods=").append(m);
        if (!"None".equals(initMethod)) {
            sb.append(", initialization_method=").append(initMethod);
        }
        sb.append(", use_boxcox=").append(useBoxcox)
          .append(", remove_bias=").append(removeBias)
          .append(")\n")
          .append("model_fit = model.fit(optimized=").append(optimized).append(")\n")
          .append("print(model_fit.summary())\n\n")
          .append("with open('" + pathStr + "/python-scripts/pickles/ets_model_fit.pickle','wb') as f:\n")
          .append("    pickle.dump(model_fit, f)\n\n");
    }
}

private void generatePythonDATrainScriptStateSpaceModel(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    if (dalib.equals("auto") || dalib.equals("statsmodels")) {
        generatePythonStatsmodelsDATrainScriptStateSpaceModel(path_str, action, pythonScriptStringBuilder, autoML);
    } else {
        System.err.println("ERROR: " + dalib
                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
                + "You may try @dalib=\"auto\" instead.\n");
    }
}

private void generatePythonStatsmodelsDATrainScriptStateSpaceModel(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    String nobs_str = "1";

    StateSpaceModel model = (StateSpaceModel) action.getDataAnalytics().getModelAlgorithm();
    String target = action.getDataAnalytics().getOutput_features().get(0).getName();

    pythonScriptStringBuilder.append("import os\n")
        .append("import numpy as np\n")
        .append("import pandas as pd\n")
        .append("from statsmodels.tsa.statespace.mlemodel import MLEModel\n")
        .append("import pickle\n\n")
        .append("import matplotlib.pyplot as plt\n\n")
        .append("os.makedirs(\"" + path_str + "/python-scripts/pickles\", exist_ok=True)\n\n")
        .append("series = df['" + target + "']\n\n");

    if (autoML && action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        if (model.getNobs() != null) {
            nobs_str = String.valueOf(model.getNobs().getIntValue());
        }

        pythonScriptStringBuilder.append("class CustomStateSpaceModel(MLEModel):\n")
            .append("    def __init__(self, endog, k_states):\n")
            .append("        super().__init__(endog, k_states=k_states)\n")
            .append("        self['transition'] = np.eye(k_states)\n")
            .append("        self['selection'] = np.eye(k_states)\n")
            .append("        self['state_cov'] = np.eye(k_states) * 0.1\n")
            .append("        self['obs_cov'] = np.eye(1) * 0.1\n")
            .append("        self['initial_state'] = np.zeros(k_states)\n")
            .append("        self['initial_state_cov'] = np.eye(k_states)\n\n")
            .append("    def update(self, params, **kwargs):\n")
            .append("        pass\n\n")
            .append("model = CustomStateSpaceModel(series, k_states=" + nobs_str + ")\n")
            .append("model_fit = model.fit(disp=False)\n")
            .append("print(model_fit.summary())\n\n")
            .append("with open('" + path_str + "/python-scripts/pickles/automl_statespace_model_fit.pickle', 'wb') as f:\n")
            .append("    pickle.dump(model_fit, f)\n\n");
    } else {
        if (model.getNobs() != null) {
            nobs_str = String.valueOf(model.getNobs().getIntValue());
        }

        String transition = model.getTransition_matrix() != null ? model.getTransition_matrix() : "np.eye(k_states)";
        String selection = model.getSelection_matrix() != null ? model.getSelection_matrix() : "np.eye(k_states)";
        String state_cov = model.getState_cov() != null ? model.getState_cov() : "np.eye(k_states) * 0.1";
        String obs_cov = model.getObs_cov() != null ? model.getObs_cov() : "np.eye(1) * 0.1";
        String initial_mean = model.getInitial_state_mean() != null ? model.getInitial_state_mean() : "np.zeros(k_states)";
        String initial_cov = model.getInitial_state_cov() != null ? model.getInitial_state_cov() : "np.eye(k_states)";

        pythonScriptStringBuilder.append("class CustomStateSpaceModel(MLEModel):\n")
            .append("    def __init__(self, endog, k_states):\n")
            .append("        super().__init__(endog, k_states=k_states)\n")
            .append("        self['transition'] = " + transition + "\n")
            .append("        self['selection'] = " + selection + "\n")
            .append("        self['state_cov'] = " + state_cov + "\n")
            .append("        self['obs_cov'] = " + obs_cov + "\n")
            .append("        self['initial_state'] = " + initial_mean + "\n")
            .append("        self['initial_state_cov'] = " + initial_cov + "\n\n")
            .append("    def update(self, params, **kwargs):\n")
            .append("        pass\n\n")
            .append("model = CustomStateSpaceModel(series, k_states=" + nobs_str + ")\n")
            .append("model_fit = model.fit(disp=False)\n")
            .append("print(model_fit.summary())\n\n")
            .append("with open('" + path_str + "/python-scripts/pickles/statespace_model_fit.pickle', 'wb') as f:\n")
            .append("    pickle.dump(model_fit, f)\n\n");
    }
}



//////////////////////////


/**
 * Generates Python script for training a Support Vector Regression (SVR) model using the specified data analytics library.
 *
 * @param dalib                     The data analytics library to use ("auto" or "scikit-learn").
 * @param path_str                  The path where scripts and models will be saved.
 * @param action                    The training action containing model parameters.
 * @param pythonScriptStringBuilder The StringBuilder to append the Python script.
 * @param autoML                    Flag indicating whether to use AutoML for parameter tuning.
 */
private void generatePythonDATrainScriptSVR(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    if (dalib.equals("auto") || dalib.equals("scikit-learn")) {
        generatePythonScikitLearnDATrainScriptSVR(path_str, action, pythonScriptStringBuilder, autoML);
    } else {
        System.err.println("ERROR: " + dalib
                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
                + "You may try @dalib=\"auto\" instead.\n");
    }
}

/**
 * Generates Python script for training a Support Vector Regression (SVR) model using the scikit-learn library.
 *
 * @param path_str                  The path where scripts and models will be saved.
 * @param action                    The training action containing model parameters.
 * @param pythonScriptStringBuilder The StringBuilder to append the Python script.
 * @param autoML                    Flag indicating whether to use AutoML for parameter tuning.
 */
private void generatePythonScikitLearnDATrainScriptSVR(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    // Initialize SVR parameters with default values
    String kernel_str = "'rbf'";          // Default kernel
    String degree_str = "3";               // Default degree
    String gamma_str = "'scale'";          // Default gamma
    String coef0_str = "0.0";              // Default coef0
    String tol_str = "1e-3";               // Default tolerance
    String c_str = "1.0";                   // Default C parameter
    String epsilon_str = "0.1";            // Default epsilon
    String shrinking_str = "True";         // Default shrinking
    String cache_size_str = "200";         // Default cache size
    String verbose_str = "False";          // Default verbosity
    String max_iter_str = "-1";            // Default max iterations
    boolean forecasting_plots = false;     // Default forecasting plots flag

    // Extract SVR model parameters from action
    SVR svr = (SVR) action.getDataAnalytics().getModelAlgorithm();

    // Dynamic target column retrieval
    String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();

    // Load the dataset and setup for SVR modeling
    pythonScriptStringBuilder.append("# Import necessary libraries\n");
    pythonScriptStringBuilder.append("import pandas as pd\n");
    pythonScriptStringBuilder.append("from sklearn.svm import SVR\n");
    pythonScriptStringBuilder.append("from sklearn.model_selection import train_test_split\n");
    pythonScriptStringBuilder.append("from sklearn.metrics import mean_squared_error, r2_score\n");
    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
    pythonScriptStringBuilder.append("import pickle\n\n");

    // Handle AutoML if enabled
    if  (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        System.out.println("INFO: AutoML is ON. Automatically tuning SVR parameters...");
        pythonScriptStringBuilder.append("# AutoML: Automatically tuning SVR parameters using GridSearchCV\n");
        pythonScriptStringBuilder.append("from sklearn.model_selection import GridSearchCV\n\n");

        // Define parameter grid for AutoML
        pythonScriptStringBuilder.append("param_grid = {\n");
        pythonScriptStringBuilder.append("    'kernel': ['linear', 'poly', 'rbf', 'sigmoid'],\n");
        pythonScriptStringBuilder.append("    'C': [0.1, 1, 10, 100],\n");
        pythonScriptStringBuilder.append("    'epsilon': [0.01, 0.1, 0.2, 0.5],\n");
        pythonScriptStringBuilder.append("    'gamma': ['scale', 'auto']\n");
        pythonScriptStringBuilder.append("}\n\n");

        // Initialize SVR model for GridSearchCV
        pythonScriptStringBuilder.append("svr = SVR()\n");
        pythonScriptStringBuilder.append("grid_search = GridSearchCV(estimator=svr, param_grid=param_grid, cv=5, scoring='neg_mean_squared_error', n_jobs=-1)\n");
        pythonScriptStringBuilder.append("grid_search.fit(X_train, y_train)\n\n");
        pythonScriptStringBuilder.append("best_model = grid_search.best_estimator_\n");
        pythonScriptStringBuilder.append("print('Best SVR Parameters:', grid_search.best_params_)\n\n");

        // Save the best SVR model using pickle
        pythonScriptStringBuilder.append("# Save the best SVR model using pickle\n");
        pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/automl_svr_model_fit.pickle', 'wb') as pickle_file:\n");
        pythonScriptStringBuilder.append("    pickle.dump(best_model, pickle_file)\n\n");

        // Generate forecasting plots for AutoML
        pythonScriptStringBuilder.append("# Generate forecasting plots for AutoML SVR model\n");
        pythonScriptStringBuilder.append("y_pred = best_model.predict(X_test)\n");
        pythonScriptStringBuilder.append("plt.figure(figsize=(10,6))\n");
        pythonScriptStringBuilder.append("plt.scatter(X_test, y_test, color='blue', label='Actual')\n");
        pythonScriptStringBuilder.append("plt.scatter(X_test, y_pred, color='red', label='Predicted')\n");
        pythonScriptStringBuilder.append("plt.legend()\n");
        pythonScriptStringBuilder.append("plt.title('SVR AutoML: Actual vs Predicted')\n");
        pythonScriptStringBuilder.append("plt.xlabel('X_test')\n");
        pythonScriptStringBuilder.append("plt.ylabel('y_test')\n");
        pythonScriptStringBuilder.append("plt.savefig('" + path_str + "/python-scripts/plots/svr_automl_actual_vs_predicted.png')\n");
        pythonScriptStringBuilder.append("plt.show()\n\n");
    } else {
        // Manual Parameter Handling

        // Set SVR parameters with validation and defaults
        if (svr.getKernel() != null && !svr.getKernel().isEmpty()) {
            kernel_str = "'" + svr.getKernel() + "'";
        } else {
            kernel_str = "'rbf'"; // Default kernel
            System.err.println("WARNING: Kernel not specified. Defaulting to 'rbf'.");
        }

        if (svr.getDegree() != null) {
            degree_str = String.valueOf(svr.getDegree().getIntValue());
        } else {
            degree_str = "3"; // Default degree
            System.err.println("WARNING: Degree not specified. Defaulting to 3.");
        }

        if (svr.getGamma() != null && !svr.getGamma().isEmpty()) {
            gamma_str = "'" + svr.getGamma() + "'";
        } else {
            gamma_str = "'scale'"; // Default gamma
            System.err.println("WARNING: Gamma not specified. Defaulting to 'scale'.");
        }

        coef0_str = svr.getCoef0() != 0.0 ? String.valueOf(svr.getCoef0()) : "0.0";
        if (svr.getCoef0() == 0.0) {
            System.err.println("WARNING: coef0 not specified. Defaulting to 0.0.");
        }

        tol_str = svr.getTol() != 0.0 ? String.valueOf(svr.getTol()) : "1e-3";
        if (svr.getTol() == 0.0) {
            System.err.println("WARNING: tol not specified. Defaulting to 1e-3.");
        }

        c_str = svr.getC() != 0.0 ? String.valueOf(svr.getC()) : "1.0";
        if (svr.getC() == 0.0) {
            System.err.println("WARNING: C not specified. Defaulting to 1.0.");
        }

        epsilon_str = svr.getEpsilon() != 0.0 ? String.valueOf(svr.getEpsilon()) : "0.1";
        if (svr.getEpsilon() == 0.0) {
            System.err.println("WARNING: epsilon not specified. Defaulting to 0.1.");
        }

        shrinking_str = svr.getShrinking() != null ? String.valueOf(svr.getShrinking().isBoolValue()) : "True";
        if (svr.getShrinking() == null) {
            System.err.println("WARNING: Shrinking not specified. Defaulting to True.");
        }

        cache_size_str = svr.getCache_size() != 0.0 ? String.valueOf(svr.getCache_size()) : "200";
        if (svr.getCache_size() == 0.0) {
            System.err.println("WARNING: cache_size not specified. Defaulting to 200.");
        }

        verbose_str = svr.getVerbose() != null ? String.valueOf(svr.getVerbose().isBoolValue()) : "False";
        if (svr.getVerbose() == null) {
            System.err.println("WARNING: verbose not specified. Defaulting to False.");
        }

        max_iter_str = svr.getMax_iter() != null ? String.valueOf(svr.getMax_iter().getIntValue()) : "-1";
        if (svr.getMax_iter() == null) {
            System.err.println("WARNING: max_iter not specified. Defaulting to -1 (no limit).");
        }

        // Handle forecasting plots
        if (svr.getForecasting_plots() != null) {
            forecasting_plots = true;
        }

        // Validate that all required parameters are set appropriately
        // For example, ensure C and epsilon are positive
        try {
            double C = Double.parseDouble(c_str);
            double epsilon = Double.parseDouble(epsilon_str);
            if (C <= 0) {
                System.err.println("ERROR: 'C' parameter must be a positive value.");
                return;
            }
            if (epsilon < 0) {
                System.err.println("ERROR: 'epsilon' parameter must be non-negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: 'C' and 'epsilon' parameters must be valid numbers.");
            return;
        }

        // Build Python code string for the SVR model
        pythonScriptStringBuilder.append("# Define SVR model with manual configurations\n");
        pythonScriptStringBuilder.append("model = SVR(kernel=" + kernel_str + ", degree=" + degree_str + ", gamma=" + gamma_str
                + ", coef0=" + coef0_str + ", tol=" + tol_str + ", C=" + c_str + ", epsilon=" + epsilon_str
                + ", shrinking=" + shrinking_str + ", cache_size=" + cache_size_str + ", verbose=" + verbose_str + ", max_iter=" + max_iter_str + ")\n");
        pythonScriptStringBuilder.append("model_fit = model.fit(X_train, y_train)\n\n");

        pythonScriptStringBuilder.append("print(model_fit)\n\n");

        // Save the SVR model using pickle
        pythonScriptStringBuilder.append("# Save the fitted SVR model using pickle\n");
        pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/svr_model_fit.pickle', 'wb') as pickle_file:\n");
        pythonScriptStringBuilder.append("    pickle.dump(model_fit, pickle_file)\n\n");

    
}}

	

private void generatePythonDATrainScriptRFR(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    if (dalib.equals("auto") || dalib.equals("scikit-learn")) {
        generatePythonScikitLearnDATrainScriptRFR(path_str, action, pythonScriptStringBuilder, autoML);
    } else {
        System.err.println("ERROR: " + dalib
                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
                + "You may try @dalib=\"auto\" instead.\n");
    }
}

/**
 * Generates Python script for training a Random Forest Regressor (RFR) model using the scikit-learn library.
 *
 * @param path_str                  The path where scripts and models will be saved.
 * @param action                    The training action containing model parameters.
 * @param pythonScriptStringBuilder The StringBuilder to append the Python script.
 * @param autoML                    Flag indicating whether to use AutoML for parameter tuning.
 */
private void generatePythonScikitLearnDATrainScriptRFR(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    // Initialize RFR parameters with default values
    String n_estimators_str = "100";                  // Default number of trees
    String criterion_str = "'mse'";
    String max_depth_str = "None";                     // Default maximum depth of the tree
    String min_samples_split_str = "2";                // Default minimum number of samples required to split an internal node
    String min_samples_leaf_str = "1";                 // Default minimum number of samples required to be at a leaf node
    String min_weight_fraction_leaf_str = "0.0";       // Default minimum weighted fraction of the sum total of weights required to be at a leaf node
    String max_features_str = "None";                   // Default number of features to consider when looking for the best split
    String max_leaf_nodes_str = "None";                // Default Grow trees with max_leaf_nodes in best-first fashion
    String min_impurity_decrease_str = "0.0";          // Default threshold for early stopping in tree growth
    String bootstrap_str = "True";                      // Default whether bootstrap samples are used when building trees
    String oob_score_str = "False";                     // Default whether to use out-of-bag samples to estimate the R^2
    String n_jobs_str = "None";                         // Default number of jobs to run in parallel
    String random_state_str = "None";                   // Default controls the randomness of the estimator
    String verbose_str = "False";                       // Default controls the verbosity when fitting and predicting
    String warm_start_str = "False";                    // Default when set to True, reuse the solution of the previous call to fit and add more estimators to the ensemble
    boolean forecasting_plots = false;                  // Default forecasting plots flag

    // Extract RFR model parameters from action
    RFR rfr = (RFR) action.getDataAnalytics().getModelAlgorithm();

    // Dynamic target column retrieval
    String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();

    // Load the dataset and setup for RFR modeling
    pythonScriptStringBuilder.append("# Import necessary libraries\n");
    pythonScriptStringBuilder.append("import pandas as pd\n");
    pythonScriptStringBuilder.append("from sklearn.ensemble import RandomForestRegressor\n");
    pythonScriptStringBuilder.append("from sklearn.metrics import mean_squared_error, r2_score\n");
    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
    pythonScriptStringBuilder.append("import pickle\n\n");

    // Handle AutoML if enabled
    if  (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        System.out.println("INFO: AutoML is ON. Automatically tuning RFR parameters...");
        pythonScriptStringBuilder.append("# AutoML: Automatically tuning RFR parameters using GridSearchCV\n\n");

        // Define parameter grid for AutoML
        pythonScriptStringBuilder.append("param_grid = {\n");
        pythonScriptStringBuilder.append("    'n_estimators': [100, 200, 300],\n");
        pythonScriptStringBuilder.append("    'criterion': ['mse', 'mae'],\n");
        pythonScriptStringBuilder.append("    'max_depth': [None, 10, 20, 30],\n");
        pythonScriptStringBuilder.append("    'min_samples_split': [2, 5, 10],\n");
        pythonScriptStringBuilder.append("    'min_samples_leaf': [1, 2, 4],\n");
        pythonScriptStringBuilder.append("    'max_features': ['auto', 'sqrt', 'log2'],\n");
        pythonScriptStringBuilder.append("    'bootstrap': [True, False]\n");
        pythonScriptStringBuilder.append("}\n\n");

        // Initialize RFR model for GridSearchCV
        pythonScriptStringBuilder.append("rfr = RandomForestRegressor()\n");
        pythonScriptStringBuilder.append("grid_search = GridSearchCV(estimator=rfr, param_grid=param_grid, cv=5, scoring='neg_mean_squared_error', n_jobs=-1)\n");
        pythonScriptStringBuilder.append("grid_search.fit(X_train, y_train)\n\n");
        pythonScriptStringBuilder.append("best_model = grid_search.best_estimator_\n");
        pythonScriptStringBuilder.append("print('Best RFR Parameters:', grid_search.best_params_)\n\n");

        // Save the best RFR model using pickle
        pythonScriptStringBuilder.append("# Save the best RFR model using pickle\n");
        pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/automl_rfr_model_fit.pickle', 'wb') as pickle_file:\n");
        pythonScriptStringBuilder.append("    pickle.dump(best_model, pickle_file)\n\n");

       
    } else {
        // Manual Parameter Handling

        // Set RFR parameters with validation and defaults
        if (rfr.getN_estimators() != null) {
            n_estimators_str = String.valueOf(rfr.getN_estimators().getIntValue());
        } else {
            n_estimators_str = "100"; // Default n_estimators
            System.err.println("WARNING: n_estimators not specified. Defaulting to 100.");
        }

        if (rfr.getCriterion() != null && !rfr.getCriterion().isEmpty()) {
            criterion_str = "'" + rfr.getCriterion() + "'";
        } else {
            criterion_str = "'mse'"; // Default criterion
            System.err.println("WARNING: criterion not specified. Defaulting to 'mse'.");
        }

        if (rfr.getMax_depth() != null) {
            max_depth_str = String.valueOf(rfr.getMax_depth().getIntValue());
        } else {
            max_depth_str = "None"; // Default max_depth
            System.err.println("WARNING: max_depth not specified. Defaulting to None.");
        }

        if (rfr.getMin_samples_split() != null) {
            min_samples_split_str = String.valueOf(rfr.getMin_samples_split().getIntValue());
        } else {
            min_samples_split_str = "2"; // Default min_samples_split
            System.err.println("WARNING: min_samples_split not specified. Defaulting to 2.");
        }

        if (rfr.getMin_samples_leaf() != null) {
            min_samples_leaf_str = String.valueOf(rfr.getMin_samples_leaf().getIntValue());
        } else {
            min_samples_leaf_str = "1"; // Default min_samples_leaf
            System.err.println("WARNING: min_samples_leaf not specified. Defaulting to 1.");
        }

        if (rfr.getMin_weight_fraction_leaf() != 0.0) {
            min_weight_fraction_leaf_str = String.valueOf(rfr.getMin_weight_fraction_leaf());
        } else {
            min_weight_fraction_leaf_str = "0.0"; // Default min_weight_fraction_leaf
            System.err.println("WARNING: min_weight_fraction_leaf not specified. Defaulting to 0.0.");
        }

        if (rfr.getMax_features() != null && !rfr.getMax_features().isEmpty()) {
            max_features_str = "'" + rfr.getMax_features() + "'";
        } else {
            max_features_str = "None"; // Default max_features
            System.err.println("WARNING: max_features not specified. Defaulting to None.");
        }

        if (rfr.getMax_leaf_nodes() != null) {
            max_leaf_nodes_str = String.valueOf(rfr.getMax_leaf_nodes().getIntValue());
        } else {
            max_leaf_nodes_str = "None"; // Default max_leaf_nodes
            System.err.println("WARNING: max_leaf_nodes not specified. Defaulting to None.");
        }

        if (rfr.getMin_impurity_decrease() != 0.0) {
            min_impurity_decrease_str = String.valueOf(rfr.getMin_impurity_decrease());
        } else {
            min_impurity_decrease_str = "0.0"; // Default min_impurity_decrease
            System.err.println("WARNING: min_impurity_decrease not specified. Defaulting to 0.0.");
        }

        if (rfr.getBootstrap() != null) {
            bootstrap_str = String.valueOf(rfr.getBootstrap().isBoolValue());
        } else {
            bootstrap_str = "True"; // Default bootstrap
            System.err.println("WARNING: bootstrap not specified. Defaulting to True.");
        }

        if (rfr.getOob_score() != null) {
            oob_score_str = String.valueOf(rfr.getOob_score().isBoolValue());
        } else {
            oob_score_str = "False"; // Default oob_score
            System.err.println("WARNING: oob_score not specified. Defaulting to False.");
        }

        if (rfr.getN_jobs() != null) {
            n_jobs_str = String.valueOf(rfr.getN_jobs().getIntValue());
        } else {
            n_jobs_str = "None"; // Default n_jobs
            System.err.println("WARNING: n_jobs not specified. Defaulting to None.");
        }

        if (rfr.getRandom_state() != null) {
            random_state_str = String.valueOf(rfr.getRandom_state().getIntValue());
        } else {
            random_state_str = "None"; // Default random_state
            System.err.println("WARNING: random_state not specified. Defaulting to None.");
        }

        if (rfr.getVerbose() != null) {
            verbose_str = String.valueOf(rfr.getVerbose().isBoolValue());
        } else {
            verbose_str = "False"; // Default verbose
            System.err.println("WARNING: verbose not specified. Defaulting to False.");
        }

        if (rfr.getWarm_start() != null) {
            warm_start_str = String.valueOf(rfr.getWarm_start().isBoolValue());
        } else {
            warm_start_str = "False"; // Default warm_start
            System.err.println("WARNING: warm_start not specified. Defaulting to False.");
        }

        // Handle forecasting plots
        if (rfr.getForecasting_plots() != null) {
            forecasting_plots = true;
        }

        // Validate critical parameters
        try {
            int n_estimators = Integer.parseInt(n_estimators_str);
            if (n_estimators <= 0) {
                System.err.println("ERROR: 'n_estimators' must be a positive integer.");
                return;
            }

            int min_samples_split = Integer.parseInt(min_samples_split_str);
            if (min_samples_split <= 0) {
                System.err.println("ERROR: 'min_samples_split' must be a positive integer.");
                return;
            }

            int min_samples_leaf = Integer.parseInt(min_samples_leaf_str);
            if (min_samples_leaf <= 0) {
                System.err.println("ERROR: 'min_samples_leaf' must be a positive integer.");
                return;
            }

            double min_weight_fraction_leaf = Double.parseDouble(min_weight_fraction_leaf_str);
            if (min_weight_fraction_leaf < 0.0) {
                System.err.println("ERROR: 'min_weight_fraction_leaf' must be non-negative.");
                return;
            }

            double min_impurity_decrease = Double.parseDouble(min_impurity_decrease_str);
            if (min_impurity_decrease < 0.0) {
                System.err.println("ERROR: 'min_impurity_decrease' must be non-negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: One or more RFR parameters are invalid.");
            return;
        }

        // Build Python code string for the RFR model
        pythonScriptStringBuilder.append("# Define RFR model with manual configurations\n");
        pythonScriptStringBuilder.append("model = RandomForestRegressor(n_estimators=" + n_estimators_str
                + ", criterion=" + criterion_str
                + ", max_depth=" + max_depth_str
                + ", min_samples_split=" + min_samples_split_str
                + ", min_samples_leaf=" + min_samples_leaf_str
                + ", min_weight_fraction_leaf=" + min_weight_fraction_leaf_str
                + ", max_features=" + max_features_str
                + ", max_leaf_nodes=" + max_leaf_nodes_str
                + ", min_impurity_decrease=" + min_impurity_decrease_str
                + ", bootstrap=" + bootstrap_str
                + ", oob_score=" + oob_score_str
                + ", n_jobs=" + n_jobs_str
                + ", random_state=" + random_state_str
                + ", verbose=" + verbose_str
                + ", warm_start=" + warm_start_str
                + ")\n");
        pythonScriptStringBuilder.append("model_fit = model.fit(X_train, y_train)\n\n");

        pythonScriptStringBuilder.append("print(model_fit)\n\n");

        // Save the RFR model using pickle
        pythonScriptStringBuilder.append("# Save the fitted RFR model using pickle\n");
        pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/rfr_model_fit.pickle', 'wb') as pickle_file:\n");
        pythonScriptStringBuilder.append("    pickle.dump(model_fit, pickle_file)\n\n");

        // Generate forecasting plots if enabled
      
    }
}



/**
 * Generates Python script for training a Gradient Boosting Regressor (GBM) model using the specified data analytics library.
 *
 * @param dalib                     The data analytics library to use ("auto" or "scikit-learn").
 * @param path_str                  The path where scripts and models will be saved.
 * @param action                    The training action containing model parameters.
 * @param pythonScriptStringBuilder The StringBuilder to append the Python script.
 * @param autoML                    Flag indicating whether to use AutoML for parameter tuning.
 */
private void generatePythonDATrainScriptGBM(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    if (dalib.equals("auto") || dalib.equals("scikit-learn")) {
        generatePythonScikitLearnDATrainScriptGBM(path_str, action, pythonScriptStringBuilder, autoML);
    } else {
        System.err.println("ERROR: " + dalib
                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
                + "You may try @dalib=\"auto\" instead.\n");
    }
}

/**
 * Generates Python script for training a Gradient Boosting Regressor (GBM) model using the scikit-learn library.
 *
 * @param path_str                  The path where scripts and models will be saved.
 * @param action                    The training action containing model parameters.
 * @param pythonScriptStringBuilder The StringBuilder to append the Python script.
 * @param autoML                    Flag indicating whether to use AutoML for parameter tuning.
 */
private void generatePythonScikitLearnDATrainScriptGBM(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    // Initialize GBM parameters with default values
    String loss_str = "'ls'";          
    String learning_rate_str = "0.1";                // Default learning rate
    String n_estimators_str = "100";                 // Default number of boosting stages
    String subsample_str = "1.0";                     // Default fraction of samples used for fitting the individual base learners
    String criterion_str = "'friedman_mse'";         // Default function to measure the quality of a split
    String min_samples_split_str = "2";               // Default minimum number of samples required to split an internal node
    String min_samples_leaf_str = "1";                // Default minimum number of samples required to be at a leaf node
    String min_weight_fraction_leaf_str = "0.0";      // Default minimum weighted fraction of the sum total of weights required to be at a leaf node
    String max_depth_str = "3";                        // Default maximum depth of the individual regression estimators
    String min_impurity_decrease_str = "0.0";         // Default threshold for early stopping in tree growth
    String init_str = "None";                          // Default estimator to use to compute the initial predictions
    String random_state_str = "None";                  // Default controls the randomness of the estimator
    String max_features_str = "None";                  // Default number of features to consider when looking for the best split
    String alpha_str = "0.9";                          // Default alpha-quantile of the huber loss function
    String verbose_str = "False";                      // Default verbosity level
    String max_leaf_nodes_str = "None";                // Default grow trees with max_leaf_nodes in best-first fashion
    String warm_start_str = "False";                   // Default reuse the solution of the previous call to fit and add more estimators to the ensemble
    String presort_str = "deprecated";                 // Presort parameter is deprecated in recent scikit-learn versions

    // Extract GBM model parameters from action
    GBM gbm = (GBM) action.getDataAnalytics().getModelAlgorithm();

    // Dynamic target column retrieval
    String targetColumn = action.getDataAnalytics().getOutput_features().get(0).getName();

    // Load the dataset and setup for GBM modeling
    pythonScriptStringBuilder.append("# Import necessary libraries\n");
    pythonScriptStringBuilder.append("import pandas as pd\n");
    pythonScriptStringBuilder.append("from sklearn.ensemble import GradientBoostingRegressor\n");
    pythonScriptStringBuilder.append("from sklearn.model_selection import train_test_split, GridSearchCV\n");
    pythonScriptStringBuilder.append("from sklearn.metrics import mean_squared_error, r2_score\n");
    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
    pythonScriptStringBuilder.append("import pickle\n\n");

    // Handle AutoML if enabled
    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        System.out.println("INFO: AutoML is ON. Automatically tuning GBM parameters...");
        pythonScriptStringBuilder.append("# AutoML: Automatically tuning GBM parameters using GridSearchCV\n\n");

        // Define parameter grid for AutoML
        pythonScriptStringBuilder.append("param_grid = {\n");
        pythonScriptStringBuilder.append("    'n_estimators': [100, 200, 300],\n");
        pythonScriptStringBuilder.append("    'learning_rate': [0.01, 0.1, 0.2],\n");
        pythonScriptStringBuilder.append("    'subsample': [0.8, 1.0],\n");
        pythonScriptStringBuilder.append("    'max_depth': [3, 5, 7],\n");
        pythonScriptStringBuilder.append("    'min_samples_split': [2, 5, 10],\n");
        pythonScriptStringBuilder.append("    'min_samples_leaf': [1, 2, 4],\n");
        pythonScriptStringBuilder.append("    'max_features': ['auto', 'sqrt', 'log2'],\n");
        pythonScriptStringBuilder.append("    'loss': ['ls', 'lad', 'huber']\n");
        pythonScriptStringBuilder.append("}\n\n");

        // Initialize GBM model for GridSearchCV
        pythonScriptStringBuilder.append("gbm = GradientBoostingRegressor()\n");
        pythonScriptStringBuilder.append("grid_search = GridSearchCV(estimator=gbm, param_grid=param_grid, cv=5, scoring='neg_mean_squared_error', n_jobs=-1)\n");
        pythonScriptStringBuilder.append("grid_search.fit(X_train, y_train)\n\n");
        pythonScriptStringBuilder.append("best_model = grid_search.best_estimator_\n");
        pythonScriptStringBuilder.append("print('Best GBM Parameters:', grid_search.best_params_)\n\n");

        // Save the best GBM model using pickle
        pythonScriptStringBuilder.append("# Save the best GBM model using pickle\n");
        pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/automl_gbm_model_fit.pickle', 'wb') as pickle_file:\n");
        pythonScriptStringBuilder.append("    pickle.dump(best_model, pickle_file)\n\n");

   
    } else {
        // Manual Parameter Handling

        // Set GBM parameters with validation and defaults
        if (gbm.getLoss() != null && !gbm.getLoss().isEmpty()) {
            loss_str = "'" + gbm.getLoss() + "'";
        } else {
            loss_str = "'ls'"; // Default loss
            System.err.println("WARNING: loss not specified. Defaulting to 'ls'.");
        }

        if (gbm.getLearning_rate() != 0.0) {
            learning_rate_str = String.valueOf(gbm.getLearning_rate());
        } else {
            learning_rate_str = "0.1"; // Default learning_rate
            System.err.println("WARNING: learning_rate not specified. Defaulting to 0.1.");
        }

        if (gbm.getN_estimators() != null) {
            n_estimators_str = String.valueOf(gbm.getN_estimators().getIntValue());
        } else {
            n_estimators_str = "100"; // Default n_estimators
            System.err.println("WARNING: n_estimators not specified. Defaulting to 100.");
        }

        if (gbm.getSubsample() != 0.0) {
            subsample_str = String.valueOf(gbm.getSubsample());
        } else {
            subsample_str = "1.0"; // Default subsample
            System.err.println("WARNING: subsample not specified. Defaulting to 1.0.");
        }

        if (gbm.getCriterion() != null && !gbm.getCriterion().isEmpty()) {
            criterion_str = "'" + gbm.getCriterion() + "'";
        } else {
            criterion_str = "'friedman_mse'"; // Default criterion
            System.err.println("WARNING: criterion not specified. Defaulting to 'friedman_mse'.");
        }

        if (gbm.getMin_samples_split() != null) {
            min_samples_split_str = String.valueOf(gbm.getMin_samples_split().getIntValue());
        } else {
            min_samples_split_str = "2"; // Default min_samples_split
            System.err.println("WARNING: min_samples_split not specified. Defaulting to 2.");
        }

        if (gbm.getMin_samples_leaf() != null) {
            min_samples_leaf_str = String.valueOf(gbm.getMin_samples_leaf().getIntValue());
        } else {
            min_samples_leaf_str = "1"; // Default min_samples_leaf
            System.err.println("WARNING: min_samples_leaf not specified. Defaulting to 1.");
        }

        if (gbm.getMin_weight_fraction_leaf() != 0.0) {
            min_weight_fraction_leaf_str = String.valueOf(gbm.getMin_weight_fraction_leaf());
        } else {
            min_weight_fraction_leaf_str = "0.0"; // Default min_weight_fraction_leaf
            System.err.println("WARNING: min_weight_fraction_leaf not specified. Defaulting to 0.0.");
        }

        if (gbm.getMax_depth() != null) {
            max_depth_str = String.valueOf(gbm.getMax_depth().getIntValue());
        } else {
            max_depth_str = "3"; // Default max_depth
            System.err.println("WARNING: max_depth not specified. Defaulting to 3.");
        }

        if (gbm.getMin_impurity_decrease() != 0.0) {
            min_impurity_decrease_str = String.valueOf(gbm.getMin_impurity_decrease());
        } else {
            min_impurity_decrease_str = "0.0"; // Default min_impurity_decrease
            System.err.println("WARNING: min_impurity_decrease not specified. Defaulting to 0.0.");
        }

        if (gbm.getInit() != null && !gbm.getInit().isEmpty()) {
            init_str = "'" + gbm.getInit() + "'";
        } else {
            init_str = "None"; // Default init
            System.err.println("WARNING: init not specified. Defaulting to None.");
        }

        if (gbm.getRandom_state() != null) {
            random_state_str = String.valueOf(gbm.getRandom_state().getIntValue());
        } else {
            random_state_str = "None"; // Default random_state
            System.err.println("WARNING: random_state not specified. Defaulting to None.");
        }

        if (gbm.getMax_features() != null && !gbm.getMax_features().isEmpty()) {
            max_features_str = "'" + gbm.getMax_features() + "'";
        } else {
            max_features_str = "None"; // Default max_features
            System.err.println("WARNING: max_features not specified. Defaulting to None.");
        }

        if (gbm.getAlpha() != 0.0) {
            alpha_str = String.valueOf(gbm.getAlpha());
        } else {
            alpha_str = "0.9"; // Default alpha
            System.err.println("WARNING: alpha not specified. Defaulting to 0.9.");
        }

        if (gbm.getVerbose() != null) {
            verbose_str = String.valueOf(gbm.getVerbose().isBoolValue());
        } else {
            verbose_str = "False"; // Default verbose
            System.err.println("WARNING: verbose not specified. Defaulting to False.");
        }

        if (gbm.getMax_leaf_nodes() != null) {
            max_leaf_nodes_str = String.valueOf(gbm.getMax_leaf_nodes().getIntValue());
        } else {
            max_leaf_nodes_str = "None"; // Default max_leaf_nodes
            System.err.println("WARNING: max_leaf_nodes not specified. Defaulting to None.");
        }

        if (gbm.getWarm_start() != null) {
            warm_start_str = String.valueOf(gbm.getWarm_start().isBoolValue());
        } else {
            warm_start_str = "False"; // Default warm_start
            System.err.println("WARNING: warm_start not specified. Defaulting to False.");
        }

        if (gbm.getPresort() != null) {
            presort_str = String.valueOf(gbm.getPresort().isBoolValue());
            // Note: 'presort' is deprecated and removed in newer scikit-learn versions. It's better to omit it.
            if (presort_str.equals("deprecated")) {
                presort_str = "False";
                System.err.println("WARNING: presort is deprecated. Defaulting to False.");
            }
        } else {
            presort_str = "False"; // Default presort
            System.err.println("WARNING: presort not specified. Defaulting to False.");
        }

        // Handle forecasting plots
        

        // Validate critical parameters
        try {
            double learning_rate = Double.parseDouble(learning_rate_str);
            if (learning_rate <= 0) {
                System.err.println("ERROR: 'learning_rate' must be a positive value.");
                return;
            }

            int n_estimators = Integer.parseInt(n_estimators_str);
            if (n_estimators <= 0) {
                System.err.println("ERROR: 'n_estimators' must be a positive integer.");
                return;
            }

            double subsample = Double.parseDouble(subsample_str);
            if (subsample <= 0.0 || subsample > 1.0) {
                System.err.println("ERROR: 'subsample' must be in the interval (0.0, 1.0].");
                return;
            }

            int min_samples_split = Integer.parseInt(min_samples_split_str);
            if (min_samples_split <= 0) {
                System.err.println("ERROR: 'min_samples_split' must be a positive integer.");
                return;
            }

            int min_samples_leaf = Integer.parseInt(min_samples_leaf_str);
            if (min_samples_leaf <= 0) {
                System.err.println("ERROR: 'min_samples_leaf' must be a positive integer.");
                return;
            }

            double min_weight_fraction_leaf = Double.parseDouble(min_weight_fraction_leaf_str);
            if (min_weight_fraction_leaf < 0.0) {
                System.err.println("ERROR: 'min_weight_fraction_leaf' must be non-negative.");
                return;
            }

            double min_impurity_decrease = Double.parseDouble(min_impurity_decrease_str);
            if (min_impurity_decrease < 0.0) {
                System.err.println("ERROR: 'min_impurity_decrease' must be non-negative.");
                return;
            }

            if (!max_depth_str.equals("None")) {
                int max_depth = Integer.parseInt(max_depth_str);
                if (max_depth <= 0) {
                    System.err.println("ERROR: 'max_depth' must be a positive integer or None.");
                    return;
                }
            }

            if (!max_leaf_nodes_str.equals("None")) {
                int max_leaf_nodes = Integer.parseInt(max_leaf_nodes_str);
                if (max_leaf_nodes <= 0) {
                    System.err.println("ERROR: 'max_leaf_nodes' must be a positive integer or None.");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: One or more GBM parameters are invalid.");
            return;
        }

        // Build Python code string for the GBM model
        pythonScriptStringBuilder.append("# Define GBM model with manual configurations\n");
        pythonScriptStringBuilder.append("model = GradientBoostingRegressor(loss=" + loss_str
                + ", learning_rate=" + learning_rate_str
                + ", n_estimators=" + n_estimators_str
                + ", subsample=" + subsample_str
                + ", criterion=" + criterion_str
                + ", min_samples_split=" + min_samples_split_str
                + ", min_samples_leaf=" + min_samples_leaf_str
                + ", min_weight_fraction_leaf=" + min_weight_fraction_leaf_str
                + ", max_depth=" + max_depth_str
                + ", min_impurity_decrease=" + min_impurity_decrease_str
                + ", init=" + init_str
                + ", random_state=" + random_state_str
                + ", max_features=" + max_features_str
                + ", alpha=" + alpha_str
                + ", verbose=" + verbose_str
                + ", max_leaf_nodes=" + max_leaf_nodes_str
                + ", warm_start=" + warm_start_str
                + ", presort=" + presort_str
                + ")\n");
        pythonScriptStringBuilder.append("model_fit = model.fit(X_train, y_train)\n\n");

        pythonScriptStringBuilder.append("print(model_fit)\n\n");

        // Save the GBM model using pickle
        pythonScriptStringBuilder.append("# Save the fitted GBM model using pickle\n");
        pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/gbm_model_fit.pickle', 'wb') as pickle_file:\n");
        pythonScriptStringBuilder.append("    pickle.dump(model_fit, pickle_file)\n\n");

     
    }
}


	


private void generatePythonDATrainScriptXGBoost(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("xgboost")) {
	        generatePythonXGBoostDATrainScript(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}

	
private void generatePythonXGBoostDATrainScript(String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
    // Initialize XGBoost parameters with defaults
    String max_depth_str = "6";
    String learning_rate_str = "0.1";
    String n_estimators_str = "100";
    String objective_str = "'reg:squarederror'";
    String booster_str = "'gbtree'";
    String gamma_str = "0";
    String min_child_weight_str = "1";
    String subsample_str = "1.0";
    String colsample_bytree_str = "1.0";
    String colsample_bylevel_str = "1.0";
    String colsample_bynode_str = "1.0";
    String reg_alpha_str = "0.0";
    String reg_lambda_str = "1.0";
    String scale_pos_weight_str = "1";
    String base_score_str = "0.5";
    String random_state_str = "42";
    String missing_str = "None";
    String importance_type_str = "'gain'";

    XGBoost xgboost = (XGBoost) action.getDataAnalytics().getModelAlgorithm();

    // Add basic imports
    pythonScriptStringBuilder.append("import pandas as pd\n");
    pythonScriptStringBuilder.append("import xgboost as xgb\n");
    pythonScriptStringBuilder.append("import pickle\n");
    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
    pythonScriptStringBuilder.append("import numpy as np\n\n");

    // Fix shape and type of input data
    pythonScriptStringBuilder.append("# ✅ Ensure X_train and y_train are valid numpy arrays\n");
    pythonScriptStringBuilder.append("if isinstance(X_train, list):\n");
    pythonScriptStringBuilder.append("    X_train = np.array(X_train)\n");
    pythonScriptStringBuilder.append("if isinstance(y_train, list):\n");
    pythonScriptStringBuilder.append("    y_train = np.array(y_train)\n");
    pythonScriptStringBuilder.append("if X_train.ndim == 1:\n");
    pythonScriptStringBuilder.append("    X_train = X_train.reshape(-1, 1)\n");
    pythonScriptStringBuilder.append("if y_train.ndim == 2 and y_train.shape[1] == 1:\n");
    pythonScriptStringBuilder.append("    y_train = y_train.ravel()\n\n");

    // Check data shape before fitting
    pythonScriptStringBuilder.append("# ⛑️ Data shape check before training\n");
    pythonScriptStringBuilder.append("print(\"🔍 X_train shape:\", getattr(X_train, \"shape\", None))\n");
    pythonScriptStringBuilder.append("print(\"🔍 y_train shape:\", getattr(y_train, \"shape\", None))\n");
    pythonScriptStringBuilder.append("if X_train is None or y_train is None or len(X_train) == 0 or len(y_train) == 0:\n");
    pythonScriptStringBuilder.append("    raise ValueError(\"❌ X_train or y_train is empty. Cannot proceed with training.\")\n\n");

    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        pythonScriptStringBuilder.append("# AutoML: XGBoost with GridSearchCV\n");
        pythonScriptStringBuilder.append("from sklearn.model_selection import GridSearchCV\n\n");

        pythonScriptStringBuilder.append("param_grid = {\n");
        pythonScriptStringBuilder.append("    'n_estimators': [100, 200, 300],\n");
        pythonScriptStringBuilder.append("    'learning_rate': [0.01, 0.1, 0.2],\n");
        pythonScriptStringBuilder.append("    'max_depth': [3, 5, 7],\n");
        pythonScriptStringBuilder.append("    'gamma': [0, 0.1, 0.2],\n");
        pythonScriptStringBuilder.append("    'min_child_weight': [1, 3, 5],\n");
        pythonScriptStringBuilder.append("    'subsample': [0.8, 1.0],\n");
        pythonScriptStringBuilder.append("    'colsample_bytree': [0.8, 1.0],\n");
        pythonScriptStringBuilder.append("    'reg_alpha': [0, 0.1, 1],\n");
        pythonScriptStringBuilder.append("    'reg_lambda': [1, 1.5, 2],\n");
        pythonScriptStringBuilder.append("    'booster': ['gbtree']\n");
        pythonScriptStringBuilder.append("}\n\n");

        pythonScriptStringBuilder.append("model = xgb.XGBRegressor(objective='reg:squarederror', random_state=42)\n");
        pythonScriptStringBuilder.append("grid_search = GridSearchCV(model, param_grid, cv=5, scoring='neg_mean_squared_error', verbose=1, n_jobs=-1)\n");
        pythonScriptStringBuilder.append("grid_search.fit(X_train, y_train)\n");
        pythonScriptStringBuilder.append("best_model = grid_search.best_estimator_\n");
        pythonScriptStringBuilder.append("print('Best Parameters:', grid_search.best_params_)\n\n");

        pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/automl_xgboost_model_fit.pickle', 'wb') as f:\n");
        pythonScriptStringBuilder.append("    pickle.dump(best_model, f)\n\n");

    } else {
        // Manual XGBoost params
        if (xgboost.getMax_depth() != null) max_depth_str = String.valueOf(xgboost.getMax_depth().getIntValue());
        if (xgboost.getLearning_rate() != 0.0) learning_rate_str = String.valueOf(xgboost.getLearning_rate());
        if (xgboost.getN_estimators() != null) n_estimators_str = String.valueOf(xgboost.getN_estimators().getIntValue());
        if (xgboost.getObjective() != null && !xgboost.getObjective().isEmpty()) objective_str = "'" + xgboost.getObjective() + "'";
        if (xgboost.getBooster() != null && !xgboost.getBooster().isEmpty()) booster_str = "'" + xgboost.getBooster() + "'";
        if (xgboost.getGamma() != 0.0) gamma_str = String.valueOf(xgboost.getGamma());
        if (xgboost.getMin_child_weight() != 0.0) min_child_weight_str = String.valueOf(xgboost.getMin_child_weight());
        if (xgboost.getSubsample() != 0.0) subsample_str = String.valueOf(xgboost.getSubsample());
        if (xgboost.getColsample_bytree() != 0.0) colsample_bytree_str = String.valueOf(xgboost.getColsample_bytree());
        if (xgboost.getColsample_bylevel() != 0.0) colsample_bylevel_str = String.valueOf(xgboost.getColsample_bylevel());
        if (xgboost.getColsample_bynode() != 0.0) colsample_bynode_str = String.valueOf(xgboost.getColsample_bynode());
        if (xgboost.getReg_alpha() != 0.0) reg_alpha_str = String.valueOf(xgboost.getReg_alpha());
        if (xgboost.getReg_lambda() != 0.0) reg_lambda_str = String.valueOf(xgboost.getReg_lambda());
        if (xgboost.getScale_pos_weight() != 0.0) scale_pos_weight_str = String.valueOf(xgboost.getScale_pos_weight());
        if (xgboost.getBase_score() != 0.0) base_score_str = String.valueOf(xgboost.getBase_score());
        if (xgboost.getRandom_state() != null) random_state_str = String.valueOf(xgboost.getRandom_state().getIntValue());
        if (xgboost.getImportance_type() != null && !xgboost.getImportance_type().isEmpty()) importance_type_str = "'" + xgboost.getImportance_type() + "'";

        pythonScriptStringBuilder.append("# Manual XGBoost Training\n");
        pythonScriptStringBuilder.append("model = xgb.XGBRegressor(max_depth=" + max_depth_str
                + ", learning_rate=" + learning_rate_str
                + ", n_estimators=" + n_estimators_str
                + ", objective=" + objective_str
                + ", booster=" + booster_str
                + ", gamma=" + gamma_str
                + ", min_child_weight=" + min_child_weight_str
                + ", subsample=" + subsample_str
                + ", colsample_bytree=" + colsample_bytree_str
                + ", colsample_bylevel=" + colsample_bylevel_str
                + ", colsample_bynode=" + colsample_bynode_str
                + ", reg_alpha=" + reg_alpha_str
                + ", reg_lambda=" + reg_lambda_str
                + ", scale_pos_weight=" + scale_pos_weight_str
                + ", base_score=" + base_score_str
                + ", random_state=" + random_state_str
                + ", missing=" + missing_str
                + ", importance_type=" + importance_type_str
                + ")\n");

        pythonScriptStringBuilder.append("model_fit = model.fit(X_train, y_train)\n\n");
        pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/xgboost_model_fit.pickle', 'wb') as f:\n");
        pythonScriptStringBuilder.append("    pickle.dump(model_fit, f)\n\n");
    }
}



private void generatePythonDATrainScriptProphet(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("prophet")) {
	        generatePythonProphetDATrainScript(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}
private void generatePythonProphetDATrainScript(
        String path_str,
        DATrainAction action,
        StringBuilder sb,
        boolean autoML
) {
    // Retrieve nSteps from action, defaulting to 1 if not specified
    long nSteps = action.getDataAnalytics().getSteps() != null
        ? action.getDataAnalytics().getSteps().getIntValue()
        : 1;

    // Extract Prophet model and output feature name
    Prophet prophet = (Prophet) action.getDataAnalytics().getModelAlgorithm();
    String targetColumn = action.getDataAnalytics()
                              .getOutput_features()
                              .get(0)
                              .getName();
    String outFeat = targetColumn; // alias for consistency

    // Set Prophet parameters with default values and validations
    String growth_str = (prophet.getGrowth() != null && !prophet.getGrowth().isEmpty())
        ? "'" + prophet.getGrowth() + "'" : "'linear'";
    String seasonality_mode_str = (prophet.getSeasonality_mode() != null && !prophet.getSeasonality_mode().isEmpty())
        ? "'" + prophet.getSeasonality_mode() + "'" : "'additive'";

    Double dsp = prophet.getSeasonality_prior_scale();
    String seasonality_prior_scale_str = (dsp != null && dsp != 0.0)
        ? String.valueOf(dsp) : "10.0";
    if (dsp == null || dsp == 0.0) {
        System.err.println("WARNING: Seasonality prior scale not specified. Defaulting to 10.0.");
    }

    Double dhp = prophet.getHolidays_prior_scale();
    String holidays_prior_scale_str = (dhp != null && dhp != 0.0)
        ? String.valueOf(dhp) : "10.0";
    if (dhp == null || dhp == 0.0) {
        System.err.println("WARNING: Holidays prior scale not specified. Defaulting to 10.0.");
    }

    Double dcp = prophet.getChangepoint_prior_scale();
    String changepoint_prior_scale_str = (dcp != null && dcp != 0.0)
        ? String.valueOf(dcp) : "0.05";
    if (dcp == null || dcp == 0.0) {
        System.err.println("WARNING: Changepoint prior scale not specified. Defaulting to 0.05.");
    }

    Double diw = prophet.getInterval_width();
    String interval_width_str = (diw != null && diw != 0.0)
        ? String.valueOf(diw) : "0.8";
    if (diw == null || diw == 0.0) {
        System.err.println("WARNING: Interval width not specified. Defaulting to 0.8.");
    }

    // Begin building the Python script
    sb.append("print('INFO: Starting Prophet training script...')\n\n")
      .append("import pandas as pd\n")
      .append("from prophet import Prophet\n\n")
      // wrap the existing outFeat_series
      .append("# Build DataFrame from pre-loaded series\n")
      .append("df = pd.DataFrame({\n")
      .append("    'ds': ").append(outFeat).append("_series.index,\n")
      .append("    'y':  ").append(outFeat).append("_series.values\n")
      .append("})\n\n");

    // AutoML vs manual branch
    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
        sb.append("print('INFO: AutoML is ON; tuning via Optuna...')\n")
          .append("import optuna\n")
          .append("from prophet.diagnostics import cross_validation, performance_metrics\n\n")
          .append("def objective(trial):\n")
          .append("    sp = trial.suggest_float('seasonality_prior_scale', 1.0, 20.0, log=True)\n")
          .append("    hp = trial.suggest_float('holidays_prior_scale',    1.0, 20.0, log=True)\n")
          .append("    cp = trial.suggest_float('changepoint_prior_scale', 0.001, 0.5, log=True)\n")
          .append("    iw = trial.suggest_float('interval_width',         0.5,   0.95)\n")
          .append("    m = Prophet(\n")
          .append("        growth=").append(growth_str).append(",\n")
          .append("        seasonality_mode=").append(seasonality_mode_str).append(",\n")
          .append("        seasonality_prior_scale=sp,\n")
          .append("        holidays_prior_scale=hp,\n")
          .append("        changepoint_prior_scale=cp,\n")
          .append("        interval_width=iw\n")
          .append("    )\n")
          .append("    m.fit(df)\n")
          .append("    cv = cross_validation(m, initial='90 days', period='15 days', horizon='")
          .append(nSteps).append(" days')\n")
          .append("    pm = performance_metrics(cv)\n")
          .append("    return pm['rmse'].mean()\n\n")
          .append("study = optuna.create_study(direction='minimize')\n")
          .append("study.optimize(objective, n_trials=20)\n")
          .append("best = study.best_params\n")
          .append("print('INFO: Best hyperparameters:', best)\n")
          .append("model = Prophet(\n")
          .append("    growth=").append(growth_str).append(",\n")
          .append("    seasonality_mode=").append(seasonality_mode_str).append(",\n")
          .append("    seasonality_prior_scale=best['seasonality_prior_scale'],\n")
          .append("    holidays_prior_scale=best['holidays_prior_scale'],\n")
          .append("    changepoint_prior_scale=best['changepoint_prior_scale'],\n")
          .append("    interval_width=best['interval_width']\n")
          .append(")\n");
    } else {
        sb.append("print('INFO: AutoML is OFF; using manual parameters')\n")
          .append("model = Prophet(\n")
          .append("    growth=").append(growth_str).append(",\n")
          .append("    seasonality_mode=").append(seasonality_mode_str).append(",\n")
          .append("    seasonality_prior_scale=").append(seasonality_prior_scale_str).append(",\n")
          .append("    holidays_prior_scale=").append(holidays_prior_scale_str).append(",\n")
          .append("    changepoint_prior_scale=").append(changepoint_prior_scale_str).append(",\n")
          .append("    interval_width=").append(interval_width_str).append("\n")
          .append(")\n");
    }

    // fit, make future, predict, save
    sb.append("model_fit = model.fit(df)\n\n")
      .append("future   = model_fit.make_future_dataframe(periods=").append(nSteps).append(", freq='D')\n")
      .append("forecast = model_fit.predict(future)\n\n")
      .append("import pickle\n")
      .append("pickle.dump(model_fit, open('").append(path_str)
      .append("/python-scripts/pickles/prophet_model_fit.pickle','wb'))\n")
      .append("pickle.dump(forecast,     open('").append(path_str)
      .append("print('INFO: Prophet training and forecast complete.')\n");
}







	private void generatePythonDATrainScriptARIMA_GARCH(String dalib, String path_str, DATrainAction action, StringBuilder pythonScriptStringBuilder, boolean autoML) {
	    if (dalib.equals("auto") || dalib.equals("statsmodels")) {
	        generatePythonStatsmodelsDATrainScriptARIMA_GARCH(path_str, action, pythonScriptStringBuilder, autoML);
	    } else {
	        System.err.println("ERROR: " + dalib
	                + " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
	                + action.getDataAnalytics().getModelAlgorithm().getName() + ".\n"
	                + "You may try @dalib=\"auto\" instead.\n");
	    }
	}

	private void generatePythonStatsmodelsDATrainScriptARIMA_GARCH(
	        String path_str,
	        DATrainAction action,
	        StringBuilder sb,
	        boolean autoML
	) {
	    // 1) pull nSteps, algorithm & outFeat
	    long nSteps = action.getDataAnalytics().getSteps() != null
	        ? action.getDataAnalytics().getSteps().getIntValue()
	        : 10;

	    ARIMA_GARCH ag = (ARIMA_GARCH) action.getDataAnalytics().getModelAlgorithm();
	    String targetColumn = action.getDataAnalytics()
	                                .getOutput_features()
	                                .get(0)
	                                .getName();
	    String outFeat = targetColumn;  // alias

	    // 2) defaults + warnings
	    String p_str = ag.getP() != null
	        ? String.valueOf(ag.getP().getIntValue()) : "1";
	    if (ag.getP() == null) System.err.println("WARNING: p not specified; defaulting to 1.");

	    String d_str = ag.getD() != null
	        ? String.valueOf(ag.getD().getIntValue()) : "1";
	    if (ag.getD() == null) System.err.println("WARNING: d not specified; defaulting to 1.");

	    String q_str = ag.getQ() != null
	        ? String.valueOf(ag.getQ().getIntValue()) : "1";
	    if (ag.getQ() == null) System.err.println("WARNING: q not specified; defaulting to 1.");

	    // seasonal_order: expects "p,d,q,m"
	    String seasonal_order_str = "(0,0,0,0)";
	    if (ag.getSeasonal_order() != null && !ag.getSeasonal_order().isEmpty()) {
	        String[] sp = ag.getSeasonal_order().split("\\s*,\\s*");
	        if (sp.length==4) {
	            seasonal_order_str = "(" +
	                sp[0] + "," + sp[1] + "," + sp[2] + "," + sp[3] + ")";
	        } else {
	            System.err.println("WARNING: seasonal_order must have 4 elements; defaulting to (0,0,0,0).");
	        }
	    }

	    String trend_str = (ag.getTrend()!=null && !ag.getTrend().isEmpty())
	        ? "'" + ag.getTrend() + "'" : "'c'";
	    if (ag.getTrend()==null||ag.getTrend().isEmpty())
	        System.err.println("WARNING: Trend not specified; defaulting to 'c'.");

	    // garch_order: expects "p,q"
	    String garch_order_str = "(1,1)";
	    if (ag.getGarch_order()!=null && !ag.getGarch_order().isEmpty()) {
	        String[] gp = ag.getGarch_order().split("\\s*,\\s*");
	        if (gp.length==2) {
	            garch_order_str = "(" + gp[0] + "," + gp[1] + ")";
	        } else {
	            System.err.println("WARNING: garch_order must have 2 elements; defaulting to (1,1).");
	        }
	    }

	    // 3) script header: imports, mkdir, load outFeat_series
	    sb.append("# --- ARIMA-GARCH training script ---\n")
	      .append("import os\n")
	      .append("import pickle\n")
	      .append("from statsmodels.tsa.arima.model import ARIMA\n")
	      .append("import matplotlib.pyplot as plt\n\n")
	      .append("from arch import arch_model\n")
	      .append("os.makedirs('").append(path_str)
	        .append("/python-scripts/pickles', exist_ok=True)\n\n")
	      .append("# load the pre-serialized series\n")
	      .append("with open('").append(path_str)
	        .append("/python-scripts/pickles/df_").append(outFeat)
	        .append(".pickle','rb') as pf:\n")
	      .append("    ").append(outFeat).append("_series = pickle.load(pf)\n\n");

	    // 4) AutoML stub vs manual
	    if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
	        sb.append("# AutoML block not yet implemented – will grid-search ARIMA+GARCH params here\n");
	        // … you can drop in your grid-search logic later …
	    } else {
	        // 5) fit ARIMA
	        sb.append("# fit ARIMA\n")
	          .append("arima = ARIMA(").append(outFeat)
	          .append("_series, order=(").append(p_str).append(",")
	          .append(d_str).append(",").append(q_str).append("), ")
	          .append("seasonal_order=").append(seasonal_order_str)
	          .append(", trend=").append(trend_str).append(")\n")
	          .append("arima_fit = arima.fit()\n\n")

	          // 6) extract residuals and fit GARCH
	          .append("# fit GARCH on ARIMA residuals\n")
	          .append("resid = arima_fit.resid\n")
	          .append("garch = arch_model(resid, vol='Garch', p=")
	          .append(garch_order_str.replaceAll("[()].*","")) // p
	          .append(", q=").append(garch_order_str.replaceAll(".*,|[()]","")) // q
	          .append(")\n")
	          .append("garch_fit = garch.fit(disp='off')\n\n");
	    }

	    // 7) summary & save
	    sb.append("print(garch_fit.summary())\n")
	      .append("pickle.dump(garch_fit, open('").append(path_str)
	      .append("/python-scripts/pickles/arima_garch_model_fit.pickle','wb'))\n")
	      .append("print('Done.')\n");
	}

	
		
	// predict.py
	
	// ML2: da_predict
	@Override
	public void generate(DAPredictAction action, StringBuilder builder, Context ctx) {
	    // Check for the blackbox ML (i.e., the hybrid/mixed MDSE/Non-MDSE) mode:
	    System.out.println("🔍 Checking for blackbox ML mode...");
boolean blackbox_ml = false;
if (action.getDataAnalytics().getBlackbox_ml() != null) {
    if (action.getDataAnalytics().getBlackbox_ml().isBoolValue()) {
        blackbox_ml = true;
    }
}
if (blackbox_ml) {
    System.err.println("❌ ERROR: blackbox-ml mode is not allowed in da_predict action.");
    return;
}

System.out.println("✅ Passed blackbox ML check.");

String dalib = "auto";
if (AnnotatedElementHelper.hasAnnotation(action.getDataAnalytics(), "dalib")) {
    String annotationValue = action.getDataAnalytics().getAnnotations().get(0).getValue();
    System.out.println("📌 Annotation @dalib = " + annotationValue);
    if (annotationValue.equals("scikit-learn") || annotationValue.equals("keras-tensorflow")
            || annotationValue.equals("pytorch") || annotationValue.equals("statsmodels")
            || annotationValue.equals("xgboost")) {
        dalib = annotationValue;
    } else if (annotationValue.equals("weka")) {
        System.err.println("❌ ERROR: weka is not supported in hybrid Java-Python generation.");
    } else {
        dalib = "auto";
    }
}
System.out.println("🔧 Using ML library: " + dalib);

Path path = null;
path = Paths.get(new File(ctx.getOutputDirectory().getAbsolutePath().toString()).toURI());
System.out.println("📂 Output path: " + path.toString());

builder.append("String prediction_timestamp = \"\";\n");

if (action.getDataAnalytics().getTimestamps().equals(Timestamps.ON)) {
    System.out.println("⏳ Timestamps are enabled.");
    
    String timestamp_pickle_path = path.toString() + "/src/python-scripts/pickles/preprocess_timeformat.pickle";
    File timeformat_pickle_file = new File(timestamp_pickle_path);
    
    if (timeformat_pickle_file.exists()) {
        System.out.println("✅ Found: " + timestamp_pickle_path);
    } else {
        System.err.println("❌ ERROR: Missing timestamp pickle file at " + timestamp_pickle_path);
    }

    // Appending the Java code that will be generated
    builder.append("File preprocess_timeformat_pickle = new File(\"" + timestamp_pickle_path + "\");\n");
    builder.append("if(!preprocess_timeformat_pickle.exists())\n");
    builder.append("    return;\n");
    builder.append("prediction_timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm:ss\")).toString();\n");

} else {
    System.out.println("⏳ Timestamps are disabled.");
}


String timestamps = action.getDataAnalytics().getTimestamps().getName();
System.out.println("📅 Timestamps option: " + timestamps);

EList<Property> features = action.getFeatures();
System.out.println("📋 Features size = " + features.size());

List<String> feature_types = new ArrayList<String>();
for (int i = 0; i < features.size(); i++) {
    String name = features.get(i).getName();
    String type = features.get(i).getTypeRef().getType().getName();
    boolean isArray = features.get(i).getTypeRef().isIsArray();
    System.out.println("   ➤ Feature[" + i + "] name: " + name + ", type: " + type + ", isArray: " + isArray);
    if (isArray) {
        feature_types.add(type + "[]");
    } else {
        feature_types.add(type);
    }
}



EList<Property> feature_values_for_prediction = action.getFeatures();
System.out.println("✅ Feature types collected: " + feature_types);
	    DataAnalyticsModelAlgorithm dataAnalyticsModelAlgorithm = action.getDataAnalytics().getModelAlgorithm();
		generatePythonDAPredictScript(path.toString() + "/src", action);

	    builder.append("File preprocess_original_df_pickle = new File(\"" + path.toString()
	            + "/src/python-scripts/pickles/preprocess_original_df.pickle" + "\");\n");
	    builder.append("if(!preprocess_original_df_pickle.exists())\n");
	    builder.append("    return;\n");

	    if (dataAnalyticsModelAlgorithm instanceof ML2_ModelAlgorithm) {
	        if (action.getDataAnalytics().getLabels() == Labels.ON) {
	            // Supervised ML

	            if (dataAnalyticsModelAlgorithm instanceof LinearRegression) { // Linear Regression
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    builder.append("File train_model_lin_reg_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_lin_reg.pickle" + "\");\n");
	                    builder.append("if(!train_model_lin_reg_pickle.exists())\n");
	                    builder.append("    return;\n");
	                } else {
	                    System.err.println(
	                            "ERROR: The prediction data type is categorical. Therefore, please use the logistic regression classifier, instead of the linear regression model.\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof LinearClassifierLogisticRegression) { // Linear
	                                                                                                    // Classifier:
	                                                                                                    // Logistic
	                                                                                                    // Regression
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    System.err.println(
	                            "ERROR: The prediction data type is numerical. Therefore, please use the linear regression model, instead of the logistic regression classifier.\n");
	                } else {
	                    builder.append("File train_model_lin_cl_log_reg_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_lin_cl_log_reg.pickle" + "\");\n");
	                    builder.append("if(!train_model_lin_cl_log_reg_pickle.exists())\n");
	                    builder.append("    return;\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesGaussian) { // Gaussian Naive Bayes (NB)
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    System.err.println(
	                            "ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
	                } else {
	                    builder.append("File train_model_gnb_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_gnb.pickle" + "\");\n");
	                    builder.append("if(!train_model_gnb_pickle.exists())\n");
	                    builder.append("    return;\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesMultinomial) { // Multinomial Naive Bayes
	                                                                                        // (NB)
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    System.err.println(
	                            "ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
	                } else {
	                    builder.append("File train_model_mnb_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_mnb.pickle" + "\");\n");
	                    builder.append("if(!train_model_mnb_pickle.exists())\n");
	                    builder.append("    return;\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesComplement) { // Complement Naive Bayes (NB)
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    System.err.println(
	                            "ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
	                } else {
	                    builder.append("File train_model_cnb_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_cnb.pickle" + "\");\n");
	                    builder.append("if(!train_model_cnb_pickle.exists())\n");
	                    builder.append("    return;\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesBernoulli) { // Bernoulli Naive Bayes (NB)
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    System.err.println(
	                            "ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
	                } else {
	                    builder.append("File train_model_bnb_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_bnb.pickle" + "\");\n");
	                    builder.append("if(!train_model_bnb_pickle.exists())\n");
	                    builder.append("    return;\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesCategorical) { // Categorical Naive Bayes
	                                                                                        // (NB)
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    System.err.println(
	                            "ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
	                } else {
	                    builder.append("File train_model_cat_nb_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_cat_nb.pickle" + "\");\n");
	                    builder.append("if(!train_model_cat_nb_pickle.exists())\n");
	                    builder.append("    return;\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeRegressor) {// Decision Tree (DT)
	                                                                                        // Regression
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    builder.append("File train_model_dtr_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_dtr.pickle" + "\");\n");
	                    builder.append("if(!train_model_dtr_pickle.exists())\n");
	                    builder.append("    return;\n");
	                } else {
	                    System.err.println(
	                            "ERROR: The prediction data type is categorical. Therefore, please use a classifier, instead of a regression model.\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeClassifier) {// Decision Tree (DT)
	                                                                                        // Classification
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    System.err.println(
	                            "ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
	                } else {
	                    builder.append("File train_model_dtc_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_dtc.pickle" + "\");\n");
	                    builder.append("if(!train_model_dtc_pickle.exists())\n");
	                    builder.append("    return;\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof RandomForestRegressor) { // Random Forest (RF)
	                                                                                        // Regression
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    builder.append("File train_model_rfr_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_rfr.pickle" + "\");\n");
	                    builder.append("if(!train_model_rfr_pickle.exists())\n");
	                    builder.append("    return;\n");
	                } else {
	                    System.err.println(
	                            "ERROR: The prediction data type is categorical. Therefore, please use a classifier, instead of a regression model.\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof RandomForestClassifier) { // Random Forest (RF)
	                                                                                        // Classification
	                if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                    System.err.println(
	                            "ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
	                } else {
	                    builder.append("File train_model_rfc_pickle = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_rfc.pickle" + "\");\n");
	                    builder.append("if(!train_model_rfc_pickle.exists())\n");
	                    builder.append("    return;\n");
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof NN_MultilayerPerceptron) { // NN Multi-Layer
	                                                                                            // Perceptron (MLP)

	                if (dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
	                    if (TyperHelper
	                            .isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
	                        // NN Multi-Layer Perceptron (MLP) Regression
	                        builder.append("File train_model_nn_mlp_r_pickle = new File(\"" + path.toString()
	                                + "/src/python-scripts/pickles/train_model_nn_mlp_r.pickle" + "\");\n");
	                        builder.append("if(!train_model_nn_mlp_r_pickle.exists())\n");
	                        builder.append("    return;\n");
	                    } else {
	                        // NN Multi-Layer Perceptron (MLP) Classification
	                        builder.append("File train_model_nn_mlp_c_pickle = new File(\"" + path.toString()
	                                + "/src/python-scripts/pickles/train_model_nn_mlp_c.pickle" + "\");\n");
	                        builder.append("if(!train_model_nn_mlp_c_pickle.exists())\n");
	                        builder.append("    return;\n");
	                    }
	                } else if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
	                    builder.append("File train_model_nn_mlp_weights = new File(\"" + path.toString()
	                            + "/src/python-scripts/pickles/train_model_nn_mlp_weights.h5" + "\");\n");
	                    builder.append("if(!train_model_nn_mlp_weights.exists())\n");
	                    builder.append("    return;\n");
	                } else {
	                    System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
	                            + "You may try @dalib=\"auto\" instead.\n");
	                }
	            } 

	        } else if (action.getDataAnalytics().getLabels() == Labels.OFF) {
	            // Unsupervised ML

	            if (dataAnalyticsModelAlgorithm instanceof KMeans) { // KMeans Clustering
	                builder.append("File train_model_k_means_clustering_pickle = new File(\"" + path.toString()
	                        + "/src/python-scripts/pickles/train_model_k_means_clustering.pickle" + "\");\n");
	                builder.append("if(!train_model_k_means_clustering_pickle.exists())\n");
	                builder.append("    return;\n");

	            } else if (dataAnalyticsModelAlgorithm instanceof MiniBatchKMeans) { // Mini-Batch KMeans Clustering
	                builder.append("File train_model_mini_batch_k_means_clustering = new File(\"" + path.toString()
	                        + "/src/python-scripts/pickles/train_model_mini_batch_k_means_clustering.pickle"
	                        + "\");\n");
	                builder.append("if(!train_model_mini_batch_k_means_clustering.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof DBSCAN) { // DBSCAN Clustering
	                builder.append("File train_model_dbscan_clustering = new File(\"" + path.toString()
	                        + "/src/python-scripts/pickles/train_model_dbscan_clustering.pickle" + "\");\n");
	                builder.append("if(!train_model_dbscan_clustering.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof SpectralClustering) { // Spectral Clustering
	                builder.append("File train_model_spectral_clustering = new File(\"" + path.toString()
	                        + "/src/python-scripts/pickles/train_model_spectral_clustering.pickle" + "\");\n");
	                builder.append("if(!train_model_spectral_clustering.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof GaussianMixture) { // Gaussian Mixture Clustering
	                builder.append("File train_model_gaussian_mixture_clustering = new File(\"" + path.toString()
	                        + "/src/python-scripts/pickles/train_model_gaussian_mixture_clustering.pickle" + "\");\n");
	                builder.append("if(!train_model_gaussian_mixture_clustering.exists())\n");
	                builder.append("    return;\n");
	            }

	        } else if (action.getDataAnalytics().getLabels() == Labels.SEMI) {
	            // Semi-supervised ML

	            if (dataAnalyticsModelAlgorithm instanceof SelfTrainingClassifier) { // Self Training Classifier for
	                                                                                    // Semi-supervised ML
	                builder.append("File train_model_self_training = new File(\"" + path.toString()
	                        + "/src/python-scripts/pickles/train_model_self_training.pickle" + "\");\n");
	                builder.append("if(!train_model_self_training.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof LabelPropagation) { // Label Propagation
	                builder.append("File train_model_label_propagation = new File(\"" + path.toString()
	                        + "/src/python-scripts/pickles/train_model_label_propagation.pickle" + "\");\n");
	                builder.append("if(!train_model_label_propagation.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof LabelSpreading) { // Label Spreading
	                builder.append("File train_model_label_spreading = new File(\"" + path.toString()
	                        + "/src/python-scripts/pickles/train_model_label_spreading.pickle" + "\");\n");
	                builder.append("if(!train_model_label_spreading.exists())\n");
	                builder.append("    return;\n");
	            }

	        } else {
	            // Nothing
	        }
	    }
	    else if (dataAnalyticsModelAlgorithm instanceof Time_series_ModelAlgorithm) {
	        // Handling Time_series_ModelAlgorithm subtypes directly
	        if (dataAnalyticsModelAlgorithm instanceof Deep_learning_ModelAlgorithm) {
	            // Handling Deep Learning Time Series Models
	            if (dataAnalyticsModelAlgorithm instanceof MLP) {
	                builder.append("File mlp_model= new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/mlp_model.h5\");\n");
	                builder.append("if(!mlp_model.exists())\n");
	                builder.append("    return;\n");
	                System.out.println("✅ Added check for mlp model pickle.");
	            } else if (dataAnalyticsModelAlgorithm instanceof GRU) {
	                builder.append("File gru_model = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/gru_model.h5\");\n");
	                builder.append("if(!gru_model.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof CNN) {
	                builder.append("File cnn_model = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/cnn_model.pkl\");\n");
	                builder.append("if(!cnn_model.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof LSTM) {
	                builder.append("File lstm_model = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/lstm_model.h5\");\n");
	                builder.append("if(!lstm_model.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof RNN) {
	                builder.append("File rnn_model = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/rnn_model.h5\");\n");
	                builder.append("if(!train_model_rnn_weights.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof TCN) {
	                builder.append("File tcn_model = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/tcn_model.h5\");\n");
	                builder.append("if(!tcn_model.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof Transformer) {
	                builder.append("File transformer_model = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/transformer_model.h5\");\n");
	                builder.append("if(!transformer_model.exists())\n");
	                builder.append("    return;\n");
	            } else {
	                System.err.println("ERROR: Unhandled Deep Learning Model: " +
	                                   dataAnalyticsModelAlgorithm.getClass().getSimpleName());
	            }
	        } else if (dataAnalyticsModelAlgorithm instanceof statistical_ModelAlgorithm) {
	            // Handling Statistical Time Series Models
	            if (dataAnalyticsModelAlgorithm instanceof ARIMA) {
	                builder.append("File arima_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/arima_model_fit.pickle\");\n");
	                builder.append("if(!arima_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof SARIMA) {
	                builder.append("File sarima_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/sarima_model_fit.pickle\");\n");
	                builder.append("if(!sarima_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof HWES) {
	                builder.append("File hwes_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/hwes_model_fit.pickle\");\n");
	                builder.append("if(!hwes_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof ETS) {
	                builder.append("File ets_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/ ets_model_fit.pickle\");\n");
	                builder.append("if(! ets_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof StateSpaceModel) {
	                builder.append("File statespace_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/statespace_model_fit.pickle\");\n");
	                builder.append("if(!statespace_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else {
	                System.err.println("ERROR: Unhandled Statistical Model: " +
	                                   dataAnalyticsModelAlgorithm.getClass().getSimpleName());
	            }
	        } else if (dataAnalyticsModelAlgorithm instanceof machine_learning_ModelAlgorithm) {
	            // Handling Machine Learning Time Series Models
	            if (dataAnalyticsModelAlgorithm instanceof SVR) {
	                builder.append("File svr_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/svr_model_fit.pickle\");\n");
	                builder.append("if(!svr_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof RFR) {
	                builder.append("File rfr_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/rfr_model_fit.pickle\");\n");
	                builder.append("if(!rfr_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof GBM) {
	                builder.append("File gbm_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/gbm_model_fit.pickle\");\n");
	                builder.append("if(!gbm_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof XGBoost) {
	                builder.append("File xgboost_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/xgboost_model_fit.pickle \");\n");
	                builder.append("if(!xgboost_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else {
	                System.err.println("ERROR: Unhandled Machine Learning Model: " +
	                                   dataAnalyticsModelAlgorithm.getClass().getSimpleName());
	            }
	        } else if (dataAnalyticsModelAlgorithm instanceof hybrid_ModelAlgorithm) {
	            // Handling Hybrid Time Series Models
	            if (dataAnalyticsModelAlgorithm instanceof ARIMA_GARCH) {
	                builder.append("File arima_garch_mode = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/arima_garch_model.pickle\");\n");
	                builder.append("if(!arima_garch_model.exists())\n");
	                builder.append("    return;\n");
	            } else if (dataAnalyticsModelAlgorithm instanceof Prophet) {
	                builder.append("File prophet_model_fit = new File(\"" + path.toString()
	                               + "/src/python-scripts/pickles/prophet_model_fit.pickle\");\n");
	                builder.append("if(! prophet_model_fit.exists())\n");
	                builder.append("    return;\n");
	            } else {
	                System.err.println("ERROR: Unhandled Hybrid Model: " +
	                                   dataAnalyticsModelAlgorithm.getClass().getSimpleName());
	            }
	        } else {
	            System.err.println("ERROR: Unsupported Time Series Model Algorithm type: " 
	                               + dataAnalyticsModelAlgorithm.getClass().getSimpleName());
	        }
	    }

	    
	   
	    // Make the generated Python script executable
	    // ********* ML2 *********

// Make the generated Python script executable

// Timestamp


// Execute the Python script and capture output
// Make the generated Python script executable
builder.append("List<String> list0 = new ArrayList<String>();\n");
builder.append("System.out.println(\"🔧 Creating chmod command list...\");\n");
builder.append("list0.add(\"chmod\");\n");
builder.append("list0.add(\"u+x\");\n");
builder.append("list0.add(\"" + path.toString() + "/src/python-scripts/predict.py\");\n");
builder.append("try {\n");
builder.append("    System.out.println(\"🔧 Executing chmod to make script executable...\");\n");
builder.append("    ProcessBuilder pb0 = new ProcessBuilder(list0);\n");
builder.append("    Process p0 = pb0.start();\n");
builder.append("    p0.waitFor();\n");
builder.append("    System.out.println(\"✅ Python script made executable.\");\n");
builder.append("} catch(Exception e) {\n");
builder.append("    System.out.println(\"❌ Error setting script as executable: \" + e);\n");
builder.append("}\n");

// Build the command to call predict.py
builder.append("List<String> list = new ArrayList<String>();\n");
builder.append("System.out.println(\"🔧 Creating command list for Python script...\");\n");
builder.append("list.add(\"/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/scripts/venv/bin/python\");\n");
builder.append("list.add(\"" + path.toString() + "/src/python-scripts/predict.py\");\n");
builder.append("list.add(\"" + timestamps + "\");\n");

// Features and types
String features_str = "";
String feature_types_str = "";
for (int i = 0; i < features.size(); i++) {
    features_str += features.get(i).getName();
    feature_types_str += feature_types.get(i);
    if (i < features.size() - 1) {
        features_str += ",";
        feature_types_str += ",";
    }
}
builder.append("System.out.println(\"📥 Adding features and types to list...\");\n");
builder.append("list.add(\"" + features_str + "\");\n");
builder.append("list.add(\"" + feature_types_str + "\");\n");

// Feature values
//new zahra

builder.append("System.out.println(\"📥 Adding feature values to list...\");\n");
builder.append("list.add(");
builder.append("\"");

for (int i = 0; i < feature_values_for_prediction.size(); i++) {
    if (feature_values_for_prediction.get(i).getTypeRef().isIsArray()) {
        builder.append("\" + Arrays.toString(" + ctx.getVariableName(feature_values_for_prediction.get(i)) + ").replaceAll(\",\",\"\") + \"");
    } else {
        builder.append("\" + String.valueOf(" + ctx.getVariableName(feature_values_for_prediction.get(i)) + ") + \"");
    }

    if (i < feature_values_for_prediction.size() - 1) {
        builder.append(",");
    }
}







builder.append("\");\n");

//Define paths first
String basePath = path.toString().replace("\\", "/");
String outputTxtPath = basePath + "/src/python-scripts/outputs/prediction.txt";

//Timestamp (added to command list)
builder.append("System.out.println(\"🕒 Adding timestamp to list...\");\n");
builder.append("list.add(\"'\" + prediction_timestamp + \"'\");\n");

//Execute the Python script and capture output
builder.append("StringBuilder output_string_builder = new StringBuilder();\n");
builder.append("try {\n");
builder.append("    System.out.println(\"🚀 Running the Python prediction script...\");\n");
builder.append("    ProcessBuilder pb = new ProcessBuilder(list);\n");
builder.append("    Process p = pb.start();\n");
builder.append("    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));\n");
builder.append("    String s = null;\n");
builder.append("    while ((s = stdInput.readLine()) != null) {\n");
builder.append("        System.out.println(\"📤 Python Output: \" + s);\n");
builder.append("        output_string_builder.append(s + \"\\n\");\n");
builder.append("    }\n");
builder.append("    p.waitFor();\n");
builder.append("    System.out.println(\"✅ Python script finished execution.\");\n");
builder.append("} catch(Exception e) {\n");
builder.append("    System.out.println(\"❌ Error running predict.py: \" + e);\n");
builder.append("}\n");

//Ensure outputs directory exists
builder.append("File outputsDir = new File(\"" + basePath + "/src/python-scripts/outputs/\");\n");
builder.append("if (!outputsDir.exists()) outputsDir.mkdirs();\n");

//Save prediction output to file
builder.append("try (java.io.FileWriter fileWriter = new java.io.FileWriter(\"" + outputTxtPath + "\", true)) {\n");
builder.append("    System.out.println(\"💾 Saving prediction to file...\");\n");
builder.append("    fileWriter.write(\"[\" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm:ss\")) + \"]\\n\");\n");
builder.append("    fileWriter.write(output_string_builder.toString() + \"\\n\");\n");
builder.append("    System.out.println(\"✅ Prediction saved to " + outputTxtPath + "\");\n");
builder.append("} catch (Exception e) {\n");
builder.append("    System.out.println(\"❌ Error writing prediction to file: \" + e);\n");
builder.append("}\n");




	    if (!(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType() instanceof Enumeration)) {
	        if (AnnotatedElementHelper.hasAnnotation(
	                action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(), "java_type")) {
	            if (!action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().isIsArray()) {

	                if (AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .equals("boolean")) {
	                    // boolean
	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                    + "= Boolean.parseBoolean(" + "(output_string_builder.toString());\n");

	                } else if (AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .equals("char")) {
	                    // char
	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                    + "= " + "output_string_builder.toString();\n");

	                } else if (AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .equals("String")) {
	                    // String
	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                    + "= " + "output_string_builder.toString();\n");

	                } else if (AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .equals("double")) {
	                    // double
	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                    + "= Double.valueOf(" + "output_string_builder.toString());\n");

	                } else if (AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .equals("byte")) {
	                    // byte
	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                    + "= Byte.valueOf((int) Math.round(Double.valueOf("
	                                    + "output_string_builder.toString())));\n");

	                } else if (AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .equals("short")) {
	                    // short
	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                    + "= Short.valueOf((int) Math.round(Double.valueOf("
	                                    + "output_string_builder.toString())));\n");

	                } else if (AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .equals("int")) {
	                    // int
	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                    + "= Integer.valueOf((int) Math.round(Double.valueOf("
	                                    + "output_string_builder.toString())));\n");

	                } else if (AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .equals("long")) {
	                    // long
	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                    + "= Long.valueOf((int) Math.round(Double.valueOf("
	                                    + "output_string_builder.toString())));\n");

	                }

	            } else {
	                builder.append(
	                        "String[] arrString = output_string_builder.toString().substring(1,output_string_builder.toString().length()-1).split(\" \");\n");
	                builder.append(AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .toArray()[0] + "[] ");
	                builder.append("arr = new ");
	                builder.append(AnnotatedElementHelper
	                        .annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
	                                "java_type")
	                        .toArray()[0]);
	                builder.append("[arrString.length];\n");
	                builder.append("for(int i=0; i<arrString.length; i++) {\n");
	                builder.append(
	                        "    arr[i] = "
	                                + ctx.firstToUpper(AnnotatedElementHelper
	                                        .annotation(action.getDataAnalytics().getPredictionResults().get(0)
	                                                .getTypeRef().getType(), "java_type")
	                                        .toArray()[0].toString())
	                                + ".parse"
	                                + ctx.firstToUpper(AnnotatedElementHelper.annotation(action.getDataAnalytics()
	                                        .getPredictionResults().get(0).getTypeRef().getType(), "java_type")
	                                        .toArray()[0].toString())
	                                + "(arrString[i]);\n");
	                builder.append("}\n");
	                builder.append(
	                        ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
	                                + "=arr;\n");
	            }
	        } else {
	            if (!action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().isIsArray()) {
	                // TODO
	                builder.append("(Object) (output_string_builder.toString());\n");
	            } else {
	                // TODO
	                builder.append("(Object[]) (output_string_builder.toString());\n");
	            }
	        }
	    }

	//  builder.append("//********* ML2 *********\n\n");
	}

	
	
	
	
	
	
	
	// ML2: Code generator for the data analytics predicting Python script,
	// predict.py
	public void generatePythonDAPredictScript(String path_str, DAPredictAction action) {
	    DataAnalyticsModelAlgorithm dataAnalyticsModelAlgorithm = action.getDataAnalytics().getModelAlgorithm();

	    String dalib = "auto";
	    if (AnnotatedElementHelper.hasAnnotation(action.getDataAnalytics(), "dalib")) {
	        if (action.getDataAnalytics().getAnnotations().get(0).getValue().equals("scikit-learn")
	                || action.getDataAnalytics().getAnnotations().get(0).getValue().equals("keras-tensorflow")
	                || action.getDataAnalytics().getAnnotations().get(0).getValue().equals("pytorch")
	                || action.getDataAnalytics().getAnnotations().get(0).getValue().equals("statsmodels")
	                || action.getDataAnalytics().getAnnotations().get(0).getValue().equals("xgboost")
	                || action.getDataAnalytics().getAnnotations().get(0).getValue().equals("prophet")) {
	            dalib = action.getDataAnalytics().getAnnotations().get(0).getValue();
	        } else if (action.getDataAnalytics().getAnnotations().get(0).getValue().equals("weka")) {
	            System.err.println(
	                "ERROR: This compiler/code generator generates Java AND Python code. The data analytics / machine learning part should be generated in Python. However, weka is chosen as the library for data analytics / machine learning in the annotations of the model. Please either change the annotation @dalib to a Python library, e.g., scikit-learn or use the pure Java compiler/code generator!"
	            );
	        } else {
	            dalib = "auto";
	        }
	    }

	    StringBuilder pythonScriptStringBuilder = new StringBuilder();
	    pythonScriptStringBuilder.append("import sys\n");
	    pythonScriptStringBuilder.append("import time, datetime\n");
	    pythonScriptStringBuilder.append("import pandas as pd\n");
	    pythonScriptStringBuilder.append("import numpy as np\n");
	    pythonScriptStringBuilder.append("import pickle\n\n");


	    pythonScriptStringBuilder.append("timestamps = sys.argv[1]\n");
	    pythonScriptStringBuilder.append("features = sys.argv[2].split(',')\n");
	    pythonScriptStringBuilder.append("feature_types = sys.argv[3].split(',')\n");
	    pythonScriptStringBuilder.append("feature_values_for_prediction = sys.argv[4].split(',')\n");
	    pythonScriptStringBuilder.append("timestamp_for_prediction = sys.argv[5]\n\n");
	    
	    
	    DataAnalyticsModelAlgorithm modelAlgo     = action.getDataAnalytics().getModelAlgorithm();
		Property                        outProp      = action.getDataAnalytics().getOutput_features().get(0);
		String                          outFeat      = outProp.getName();
		boolean                         hasLabels    = action.getDataAnalytics().getLabels() == Labels.ON
		                                              || action.getDataAnalytics().getLabels() == Labels.SEMI;
		
		
		
		boolean isTimeSeriesAlgo = modelAlgo instanceof ARIMA
	            || modelAlgo instanceof SARIMA
	            || modelAlgo instanceof HWES
	            || modelAlgo instanceof Prophet;
		
	    StringBuilder python = new StringBuilder();

	    SupervisedLearning sl = action.getDataAnalytics().getSupervised_learning();
	    
	 // ─────────────────────────────────────────────────────────────────────────────
	 // 1️⃣  Helpers
	 // ─────────────────────────────────────────────────────────────────────────────
	

	 // ─────────────────────────────────────────────────────────────────────────────
	 // 2️⃣  Always load the ORIGINAL DataFrame first – everyone needs it
	 // ─────────────────────────────────────────────────────────────────────────────
	 pythonScriptStringBuilder
	     .append("# Load full, un-scaled training frame\n")
	     .append("with open(r'" + path_str + "/python-scripts/pickles/preprocess_original_df.pickle','rb') as f:\n")
	     .append("    original_df = pickle.load(f)\n\n");

	 // ─────────────────────────────────────────────────────────────────────────────
	 // 3️⃣  Branch:  TS vs. non-TS
	 // ─────────────────────────────────────────────────────────────────────────────
	 if (isTimeSeriesAlgo) {

	     /* ── 3 A.  Time-series models – save ONLY the target column for later
	        ­──────── forecast-vs-truth plots etc.                              */
	     pythonScriptStringBuilder.append("# Persist target column for diagnostics\n")
	         .append("with open(r'" + path_str + "/python-scripts/pickles/df_timeseries.pickle','wb') as f:\n")
	         .append("    pickle.dump(original_df['" + outFeat + "'], f)\n\n");

	

	 } else {

	     /* ── 3 B.  All other ML models – you’ll usually need X_train (+ y_train) */
	     pythonScriptStringBuilder.append("# Load pre-processed design matrices\n")
	         .append("with open(r'" + path_str + "/python-scripts/pickles/preprocess_X_train.pickle','rb') as f:\n")
	         .append("    X_train = pickle.load(f)\n");

	     if (action.getDataAnalytics().getLabels() == Labels.ON ||
	         action.getDataAnalytics().getLabels() == Labels.SEMI) {

	         pythonScriptStringBuilder.append("with open(r'"
	                 + path_str + "/python-scripts/pickles/preprocess_y_train.pickle','rb') as f:\n")
	             .append("    y_train = pickle.load(f)\n");
	     }
	     pythonScriptStringBuilder.append("\n");

	     /* Load the non-TS model (example: CategoricalNB) */
	    // pythonScriptStringBuilder.append("# Load scikit-learn model\n")
	        // .append("with open(r'" + path_str + "/python-scripts/pickles/train_model_cat_nb.pickle','rb') as f:\n")
	        // .append("    model = pickle.load(f)\n\n");
	 }


	    pythonScriptStringBuilder.append(
	            "array_features_indexes = list(filter(lambda x: '[' in feature_types[x], range(len(feature_types))))\n");
	    pythonScriptStringBuilder.append("new_feature_values_for_prediction = []\n");
	    pythonScriptStringBuilder.append("for index in array_features_indexes:\n");
	    pythonScriptStringBuilder.append("    for item in feature_values_for_prediction[index][2:-2].split(' '):\n");
	    pythonScriptStringBuilder.append("        new_feature_values_for_prediction.append(item)\n");
	    pythonScriptStringBuilder.append("    feature_values_for_prediction.pop(index)\n");
	    pythonScriptStringBuilder.append("    feature_values_for_prediction.append(new_feature_values_for_prediction)\n");
	    pythonScriptStringBuilder.append("    feature_name = features[index]\n");
	    pythonScriptStringBuilder.append("    features.pop(index)\n");
	    pythonScriptStringBuilder.append("    i=index\n");
	    pythonScriptStringBuilder.append("    for item in range(len(new_feature_values_for_prediction)):\n");
	    pythonScriptStringBuilder.append("        features.insert(i,feature_name+'_'+str(item))\n");
	    pythonScriptStringBuilder.append("        i=i+1\n");
	    pythonScriptStringBuilder.append("if(len(array_features_indexes)!=0):\n");
	    pythonScriptStringBuilder.append("    feature_values_for_prediction = feature_values_for_prediction[0]\n\n");

	    pythonScriptStringBuilder.append("col_names = []\n");
	    pythonScriptStringBuilder.append("num_col_names = []\n");
	    pythonScriptStringBuilder.append("cat_col_names = []\n");
	    pythonScriptStringBuilder.append("if(timestamps.lower() == 'on'):\n");
	    pythonScriptStringBuilder.append("    col_names.append('timestamp')\n");
	    pythonScriptStringBuilder.append("for i in range(len(features)):\n");
	    pythonScriptStringBuilder.append("    feature=features[i]\n");
	    pythonScriptStringBuilder.append("    feature_type=feature_types[i]\n");
	    pythonScriptStringBuilder.append("    if((\"String\" in feature_type) or (\"Char\" in feature_type)):\n");
	    pythonScriptStringBuilder.append("        cat_col_names.append(feature)\n");
	    pythonScriptStringBuilder.append(
	            "    if((\"Int\" in feature_type) or (\"Long\" in feature_type) or (\"Double\" in feature_type)):\n");
	    pythonScriptStringBuilder.append("        num_col_names.append(feature)\n");
	    pythonScriptStringBuilder.append("    col_names.append(feature)\n\n");

	    pythonScriptStringBuilder.append("if(len(cat_col_names)!=0):\n");

	    pythonScriptStringBuilder.append("    from sklearn.preprocessing import LabelEncoder\n");
	    pythonScriptStringBuilder.append("    with open('" + path_str + "/python-scripts/pickles/"
	            + "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	    pythonScriptStringBuilder.append("        le = pickle.load(pickle_file)\n\n");

	    pythonScriptStringBuilder.append("flag = False\n");
	    pythonScriptStringBuilder.append("for i in range(len(features)):\n");
	    pythonScriptStringBuilder.append("    if features[i] in cat_col_names:\n");
	    pythonScriptStringBuilder.append(
	            "        if not np.isin([feature_values_for_prediction[i]],original_df[features[i]]).item(0):\n");
	    pythonScriptStringBuilder.append("            flag = True\n");
	    pythonScriptStringBuilder.append("            break\n\n");

	    pythonScriptStringBuilder.append("if(flag):\n");
	    pythonScriptStringBuilder.append("    print (False)\n");
	    pythonScriptStringBuilder.append("else:\n");
	    if (dataAnalyticsModelAlgorithm instanceof ML2_ModelAlgorithm) {
			if (action.getDataAnalytics().getLabels() == Labels.ON) {
				// Supervised ML

				if (dataAnalyticsModelAlgorithm instanceof LinearRegression
						|| dataAnalyticsModelAlgorithm instanceof LinearClassifierLogisticRegression) { // Linear Model
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						// Linear Regression
						if (dataAnalyticsModelAlgorithm instanceof LinearClassifierLogisticRegression) {
							System.err.println(
									"ERROR: The prediction data type is numeric. Therefore, please use the linear regression model for regression, instead of the logistic regression classifier.\n");
						}
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
							pythonScriptStringBuilder.append("	from sklearn.linear_model import LinearRegression\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_lin_reg.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");
							

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
						
		                    	
		                    	                 pythonScriptStringBuilder.append(
		                    	                                 "    output_txt_path = '" + outputTxtPath + "'\n"
		                    	                         );
		                    	                 pythonScriptStringBuilder.append(
		                    	                      "    output_csv_path = '" + outputCsvPath + "'\n\n"
		                    	                       );
							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					} else {
						// Linear Classification through Logistic Regression
						if (dataAnalyticsModelAlgorithm instanceof LinearRegression) {
							System.err.println(
									"ERROR: The prediction data type is categorical. Therefore, please use the logistic regression classifier, instead of the linear regression model.\n");
						}
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
							pythonScriptStringBuilder.append("	from sklearn.linear_model import LogisticRegression\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_lin_cl_log_reg.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "    output_txt_path = '" + outputTxtPath + "'\n"
		                    	                         );
		                    	                pythonScriptStringBuilder.append(
		                    	                      "    output_csv_path = '" + outputCsvPath + "'\n\n"
		                    	                       );
							
							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");		

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesGaussian) { // Gaussian Naive Bayes (NB)
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
								+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
					} else { // Classification
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
							pythonScriptStringBuilder.append("	from sklearn.naive_bayes import GaussianNB\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_gnb.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							
							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");	

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesMultinomial) { // Multinomial Naive Bayes
																							// (NB)
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
								+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
					} else { // Classification
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
							pythonScriptStringBuilder.append("	from sklearn.naive_bayes import MultinomialNB\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_mnb.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesComplement) { // Complement Naive Bayes (NB)
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
								+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
					} else { // Classification
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
							pythonScriptStringBuilder.append("	from sklearn.naive_bayes import ComplementNB\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_cnb.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesBernoulli) { // Bernoulli Naive Bayes (NB)
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
								+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
					} else { // Classification
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
							pythonScriptStringBuilder.append("	from sklearn.naive_bayes import BernoulliNB\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_bnb.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesCategorical) { // Categorical Naive Bayes
																							// (NB)
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
								+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
					} else { // Classification
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
							pythonScriptStringBuilder.append("	from sklearn.naive_bayes import CategoricalNB\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_cat_nb.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
							
							// 1) import and make sure output directories exist
	                    	                        pythonScriptStringBuilder.append("    import os, sys\n");
	                    	                        pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
	                    	                        pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
	                    	                        pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");

	                    

							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					}
				} else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeRegressor) { // Decision Tree (DT)
																							// Regression
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) {
							pythonScriptStringBuilder.append("	from sklearn.tree import DecisionTreeRegressor\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_dtr.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					} else {
						System.err.println(
								"ERROR: The prediction data type is categorical. Therefore, please use a classifier, instead of a regression model.\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeClassifier) { // Decision Tree (DT)
																							// Classification
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println(
								"ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
					} else {
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) {
							pythonScriptStringBuilder.append("	from sklearn.tree import DecisionTreeClassifier\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_dtc.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
		                    	                               
							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					}
				} else if (dataAnalyticsModelAlgorithm instanceof RandomForestRegressor) { // Random Forest (RF)
																							// Regression
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) {
							pythonScriptStringBuilder.append("	from sklearn.ensemble import RandomForestRegressor\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_rfr.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
							
							// 1) import and make sure output directories exist
	                    	pythonScriptStringBuilder.append("    import os, sys\n");
	                    	pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
	                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
	                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");


							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");
							
							

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					} else {
						System.err.println(
								"ERROR: The prediction data type is categorical. Therefore, please use a classifier, instead of a regression model.\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof RandomForestClassifier) { // Random Forest (RF)
																							// Classification
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println(
								"ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
					} else {
						if (dalib.equals("auto") || dalib.equals("scikit-learn")) {
							pythonScriptStringBuilder.append("	from sklearn.ensemble import RandomForestClassifier\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_rfc.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

							pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
							pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
							pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
							pythonScriptStringBuilder.append("		else:\n");
							pythonScriptStringBuilder.append(
									"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

							// The actual prediction of the DA/ML model
							pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
							
							pythonScriptStringBuilder.append("pred = model.predict(df)\n");
							pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

							// 3) write out the text file
							String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
							String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
							pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	                pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
							
							// 1) import and make sure output directories exist
	                    	pythonScriptStringBuilder.append("    import os, sys\n");
	                    	pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
	                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
	                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");


							pythonScriptStringBuilder.append("# --- Write text output ---\n");
							pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
							pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
							pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

							// 4) write out the CSV
							pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
							pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
							pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
							pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
							pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
							pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
							pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

						} else if (dalib.equals("keras-tensorflow")) {
							System.err.println("ERROR: " + dalib
									+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
									+ dataAnalyticsModelAlgorithm.getName() + ".\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						} else {
							System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
									+ "You may try @dalib=\"auto\" instead.\n");
						}
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NN_MultilayerPerceptron) { // NN Multi-Layer
																								// Perceptron (MLP)

					if (dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
						if (TyperHelper
								.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
							// NN Multi-Layer Perceptron (MLP) Regression
							pythonScriptStringBuilder.append("	from sklearn.neural_network import MLPRegressor\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_nn_mlp_r.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");
						} else {
							// NN Multi-Layer Perceptron (MLP) Classification
							pythonScriptStringBuilder.append("	from sklearn.neural_network import MLPClassifier\n");
							pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
									+ "train_model_nn_mlp_c.pickle', 'rb') as pickle_file:\n");
							pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");
						}

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						
						// The actual prediction of the DA/ML model

						pythonScriptStringBuilder.append("pred = model.predict(df)\n");
						pythonScriptStringBuilder.append("print(pred.item(0))\n\n");

						// 3) write out the text file
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append(
		                    	                                 "output_txt_path = '" + outputTxtPath + "'\n" );
		                    	         pythonScriptStringBuilder.append(
		                    	                               " output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	pythonScriptStringBuilder.append("    import os, sys\n");
                    	pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");


						pythonScriptStringBuilder.append("# --- Write text output ---\n");
						pythonScriptStringBuilder.append("formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
						pythonScriptStringBuilder.append(String.format("with open(r\"%s\", \"w\") as f:\n", outputTxtPath));
						pythonScriptStringBuilder.append("    f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
						pythonScriptStringBuilder.append("print(f\"🔹 Predictions saved to: " + outputTxtPath + "\")\n\n");

						// 4) write out the CSV
						pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
						pythonScriptStringBuilder.append("cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
						pythonScriptStringBuilder.append("df_out = pd.DataFrame([pred], columns=cols)\n");
						pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
						pythonScriptStringBuilder.append("    df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
						pythonScriptStringBuilder.append(String.format("df_out.to_csv(r\"%s\", index=False)\n", outputCsvPath));
						pythonScriptStringBuilder.append("print(f\"🔹 Predictions dataset saved to: " + outputCsvPath + "\")\n");

					} else if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) { // https://keras.io/api/
						pythonScriptStringBuilder.append("	with open('" + path_str
								+ "/python-scripts/pickles/nn_nlp_y_train_le.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		y_train_le = pickle.load(pickle_file)\n");
						pythonScriptStringBuilder.append("	with open('" + path_str
								+ "/python-scripts/pickles/nn_nlp_y_train_categorical.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		y_train_categorical = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	import logging, os, sys\n");
						pythonScriptStringBuilder.append("	logging.disable(logging.WARNING)\n");
						pythonScriptStringBuilder.append("	os.environ[\"TF_CPP_MIN_LOG_LEVEL\"] = \"3\"\n");
						pythonScriptStringBuilder.append("	stderr = sys.stderr\n");
						pythonScriptStringBuilder.append("	sys.stderr = open(os.devnull, 'w')\n");
						pythonScriptStringBuilder.append("	import tensorflow as tf\n");
						pythonScriptStringBuilder.append("	from tensorflow import keras\n");
						pythonScriptStringBuilder.append("	from tensorflow.keras import layers\n");
						pythonScriptStringBuilder.append("	sys.stderr = stderr\n\n");

						pythonScriptStringBuilder.append("	#Rebuilding the model\n");
						double alpha = 0;
						boolean alpha_given = false;
						if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getAlpha() != null) {
							alpha = ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
									.getAlpha().getDoubleValue();
							alpha_given = true;
						}
						int no_hidden_layers = 0;
						boolean hidden_layers_size_given = false;
						if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getHidden_layer_sizes() == null) {
							System.err.println(
									"WARNING: In the neural network multilayer perceptron model, parameter hidden_layer_sizes, which may specify the number of hidden layers and the size of each hidden layer through a tuple, where the ith element represents the number of neurons in the ith hidden layer, is not present.\n Thus, only one hidden layer with the size of the mean of the input layer size and the output layer size will be considered.\n");
							no_hidden_layers = 1;
							pythonScriptStringBuilder.append("hidden_layers_size_not_given = True\n");
						} else {
							pythonScriptStringBuilder.append("hidden_layers_size_not_given = False\n");
							no_hidden_layers = ((NN_MultilayerPerceptron) action.getDataAnalytics().getModelAlgorithm())
									.getHidden_layers().size();
							hidden_layers_size_given = true;
						}

						boolean same_activation_all_hidden_layers = false;
						boolean one_activation_per_hidden_layer = false;

						if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
								.getHidden_layers_activation_functions() != null) {
							if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
									.getActivation().getValue() != Activation.NOT_SET_VALUE) {
								System.err.println(
										"WARNING: In the neural network multilayer perceptron model, parameters activation and hidden_layers_activation_functions cannot be specified simultaneously. Remove one of them. If you want to use the same activation for all hidden layers, use the activation parameter. Otherwise, use the hidden_layers_activation_functions parameter.\n In the latter case, you should specifiy them through a tuple, where the ith element represents the activation function for the ith hidden layer.\n Currently, your choices will be ignored. Relu will be used by default for all hidden layers.\n ");
								same_activation_all_hidden_layers = true;
							} else {
								if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivations().size() != no_hidden_layers) {
									System.err.println(
											"WARNING: In the neural network multilayer perceptron model, parameter hidden_layers_activation_functions implies a different number of hidden layers than parameter hidden_layer_sizes.\n Either the hidden_layer_sizes parameter does not exist or the size of its tuple is not the same as the size of the tuple of hidden_layers_activation_functions!\n");
									same_activation_all_hidden_layers = true;
								} else {
									for (int i = 0; i < ((NN_MultilayerPerceptron) (action.getDataAnalytics()
											.getModelAlgorithm())).getActivations().size(); i++) {
										if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivations().get(i).getValue() == Activation.NOT_SET_VALUE) {
											((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
													.getActivations().set(i, Activation.RELU);
											System.err.println(
													"WARNING: In the neural network multilayer perceptron model, parameter hidden_layers_activation_functions includes a NOT_SET activation function, which will be automatically set to RELU.\n");
										} else if (((NN_MultilayerPerceptron) (action.getDataAnalytics()
												.getModelAlgorithm())).getActivations().get(i)
														.getValue() == Activation.RELU_VALUE
												&& ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i)
																.getValue() == Activation.SIGMOID_VALUE
												&& ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i)
																.getValue() == Activation.SOFTMAX_VALUE
												&& ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i)
																.getValue() == Activation.SOFTPLUS_VALUE
												&& ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i)
																.getValue() == Activation.SOFTSIGN_VALUE
												&& ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i)
																.getValue() == Activation.TANH_VALUE
												&& ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i)
																.getValue() == Activation.SELU_VALUE
												&& ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i)
																.getValue() == Activation.ELU_VALUE
												&& ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i)
																.getValue() == Activation.EXPONENTIAL_VALUE) {
											((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
													.getActivations().set(i, Activation.RELU);
											System.err.println(
													"WARNING: In the neural network multilayer perceptron model, parameter hidden_layers_activation_functions includes an invalid activation function, which will be automatically set to RELU. See the API documentation for more information on the valid choices: https://keras.io/api/layers/activations/.\n");
										}
									}
									one_activation_per_hidden_layer = true;
								}
							}
						} else {
							same_activation_all_hidden_layers = true;
						}

						String activation_function = "";
						if (same_activation_all_hidden_layers) {
							if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
									.getActivation().getValue() != Activation.NOT_SET_VALUE) {
								if (((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
										.getActivation().getValue() != Activation.RELU_VALUE
										|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivation().getValue() != Activation.SIGMOID_VALUE
										|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivation().getValue() != Activation.SOFTMAX_VALUE
										|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivation().getValue() != Activation.SOFTPLUS_VALUE
										|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivation().getValue() != Activation.SOFTSIGN_VALUE
										|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivation().getValue() != Activation.TANH_VALUE
										|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivation().getValue() != Activation.SELU_VALUE
										|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivation().getValue() != Activation.ELU_VALUE
										|| ((NN_MultilayerPerceptron) (action.getDataAnalytics().getModelAlgorithm()))
												.getActivation().getValue() != Activation.EXPONENTIAL_VALUE) {

									System.err.println(
											"WARNING: In the neural network multilayer perceptron model, parameter activation does not specify a valid activation function for the selected DA/ML library (keras-tensorflow).\n Thus, the default relu activation function will be used. See the API documentation for more information: https://keras.io/api/layers/activations/. \n");
									activation_function = Activation.RELU.getLiteral();
								} else {
									activation_function = ((NN_MultilayerPerceptron) (action.getDataAnalytics()
											.getModelAlgorithm())).getActivation().getLiteral();
								}
							} else {
								System.err.println(
										"WARNING: In the neural network multilayer perceptron model, parameter activation is not present. Thus, the default relu activation function will be used.\n");
								activation_function = Activation.RELU.getLiteral();
							}
						}

						if (alpha_given) {
							pythonScriptStringBuilder.append("	from keras.regularizers import l2\n");
						}

						pythonScriptStringBuilder.append("	model = keras.Sequential()\n");
						pythonScriptStringBuilder.append("	model.add(layers.Input(shape=(X_train.shape[1],)))\n");

						if (hidden_layers_size_given) {
							for (int i = 0; i < no_hidden_layers; i++) {
								if (same_activation_all_hidden_layers) {
									if (alpha_given) {
										pythonScriptStringBuilder.append("	model.add(layers.Dense("
												+ String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getHidden_layers().get(i).getIntValue())
												+ ", activation='" + activation_function + "', kernel_regularizer=l2("
												+ alpha + ")))\n");
									} else {
										pythonScriptStringBuilder.append("	model.add(layers.Dense("
												+ String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getHidden_layers().get(i).getIntValue())
												+ ", activation='" + activation_function + "'))\n");
									}
								} else if (one_activation_per_hidden_layer) {
									if (alpha_given) {
										pythonScriptStringBuilder.append("	model.add(layers.Dense("
												+ String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getHidden_layers().get(i).getIntValue())
												+ ", activation='"
												+ ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i).getLiteral()
												+ "', kernel_regularizer=l2(" + alpha + ")))\n");
									} else {
										pythonScriptStringBuilder.append("	model.add(layers.Dense("
												+ String.valueOf(((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getHidden_layers().get(i).getIntValue())
												+ ", activation='"
												+ ((NN_MultilayerPerceptron) (action.getDataAnalytics()
														.getModelAlgorithm())).getActivations().get(i).getLiteral()
												+ "'))\n");
									}
								}
							}
						} else {
							pythonScriptStringBuilder.append("	import math\n");
							pythonScriptStringBuilder.append(
									"	hidden_layer_size=math.ceil(float((X_train.shape[1])+(len(y_train)))/2.0)\n");
							if (same_activation_all_hidden_layers) {
								if (alpha_given) {
									pythonScriptStringBuilder
											.append("	model.add(layers.Dense(hidden_layer_size, activation='"
													+ activation_function + "', kernel_regularizer=l2(" + alpha
													+ ")))\n");
								} else {
									pythonScriptStringBuilder
											.append("	model.add(layers.Dense(hidden_layer_size, activation='"
													+ activation_function + "'))\n");
								}
							} else if (one_activation_per_hidden_layer) {
								if (alpha_given) {
									pythonScriptStringBuilder
											.append("	model.add(layers.Dense(hidden_layer_size, activation='"
													+ ((NN_MultilayerPerceptron) (action.getDataAnalytics()
															.getModelAlgorithm())).getActivations().get(0).getLiteral()
													+ "', kernel_regularizer=l2(" + alpha + ")))\n");
								} else {
									pythonScriptStringBuilder
											.append("	model.add(layers.Dense(hidden_layer_size, activation='"
													+ ((NN_MultilayerPerceptron) (action.getDataAnalytics()
															.getModelAlgorithm())).getActivations().get(0).getLiteral()
													+ "'))\n");
								}
							}
						}

						pythonScriptStringBuilder.append("	model.add(layers.Dense(len(y_train)))\n\n");

						pythonScriptStringBuilder.append("	model.load_weights('" + path_str
								+ "/python-scripts/pickles/" + "train_model_nn_mlp_weights.h5')\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder
								.append("	print(y_train_le.inverse_transform([int(model.predict(df))]).item(0))\n\n");
						
						pythonScriptStringBuilder.append("# --- Predict & decode ---\n");
						pythonScriptStringBuilder.append("pred = model.predict(df)\n");
						pythonScriptStringBuilder.append("pred_class = int(pred.argmax(axis=1)[0])  # best class index\n");
						pythonScriptStringBuilder.append("decoded    = y_train_le.inverse_transform([pred_class])[0]\n");
						pythonScriptStringBuilder.append("print(decoded)\n\n");

						/* ---------- 3) write out the text file ---------- */
					
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");

						pythonScriptStringBuilder.append("# --- Write text output ---\n");
						// `decoded` holds the single predicted label or numeric value
						pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
						pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {decoded}\\n')\n");
						pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

						/* ---------- 4) write out the CSV ---------- */
						pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
						pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Prediction': [decoded]})\n");
						pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
						pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
						pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
						pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");

						
						

					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				} else {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is currently not a supported algorithm for supervised ML (classification).\n");
				}
			} else if (action.getDataAnalytics().getLabels() == Labels.OFF) {
				// Unsupervised ML

				if (dataAnalyticsModelAlgorithm instanceof KMeans) { // KMeans Clustering
					if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.1
						pythonScriptStringBuilder.append("	from sklearn.cluster import KMeans\n");
						pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
								+ "train_model_k_means_clustering.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");					
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						pythonScriptStringBuilder.append("# --- Predict cluster ID ---\n");
					        pythonScriptStringBuilder.append("cluster_id = int(model.predict(df)[0])\n");
					        pythonScriptStringBuilder.append("print(cluster_id)\n\n");
						
						pythonScriptStringBuilder.append("# --- Write text output ---\n");
					    pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
					    pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {cluster_id}\\n')\n");
					    pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

					    /* ---------- 4) Write out the CSV ---------- */
					      pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
					      pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Cluster': [cluster_id]})\n");
					      pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
					      pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
					      pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
					      pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");
						
						
						

					} else if (dalib.equals("keras-tensorflow")) {
						System.err.println("ERROR: " + dalib
								+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
								+ dataAnalyticsModelAlgorithm.getName() + ".\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof MiniBatchKMeans) { // Mini-Batch KMeans Clustering
					if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2
						pythonScriptStringBuilder.append("	from sklearn.cluster import MiniBatchKMeans\n");
						pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
								+ "train_model_mini_batch_k_means_clustering.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");;
						
						
						pythonScriptStringBuilder.append("# --- Predict cluster ID ---\n");
					        pythonScriptStringBuilder.append("cluster_id = int(model.predict(df)[0])\n");
					        pythonScriptStringBuilder.append("print(cluster_id)\n\n");

					    /* ---------- 3) Write out the text file ---------- */
					        pythonScriptStringBuilder.append("# --- Write text output ---\n");
					        pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
					        pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {cluster_id}\\n')\n");
					        pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

					    /* ---------- 4) Write out the CSV ---------- */
					        pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
					        pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Cluster': [cluster_id]})\n");
					        pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
					        pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
					        pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
					        pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");

					} else if (dalib.equals("keras-tensorflow")) {
						System.err.println("ERROR: " + dalib
								+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
								+ dataAnalyticsModelAlgorithm.getName() + ".\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof DBSCAN) { // DBSCAN Clustering
					if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2
						pythonScriptStringBuilder.append("	from sklearn.cluster import DBSCAN\n");
						pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
								+ "train_model_dbscan_clustering.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");
						 // DBSCAN has no .predict(); if you’re using a custom wrapper keep this,
					    // otherwise switch to model.fit_predict(df) as needed.
					       pythonScriptStringBuilder.append("cluster_id = int(model.predict(df)[0])\n");
					       pythonScriptStringBuilder.append("print(cluster_id)\n\n");

					    /* ---------- 3) Write out the text file ---------- */
					       pythonScriptStringBuilder.append("# --- Write text output ---\n");
					       pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
					       pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {cluster_id}\\n')\n");
					       pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

					    /* ---------- 4) Write out the CSV ---------- */
					      pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
					      pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Cluster': [cluster_id]})\n");
					      pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
					      pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
					      pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
					      pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");

					} else if (dalib.equals("keras-tensorflow")) {
						System.err.println("ERROR: " + dalib
								+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
								+ dataAnalyticsModelAlgorithm.getName() + ".\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof SpectralClustering) { // Spectral Clustering
					if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2
						pythonScriptStringBuilder.append("	from sklearn.cluster import SpectralClustering\n");
						pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
								+ "train_model_spectral_clustering.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");
						pythonScriptStringBuilder.append("cluster_id = int(model.predict(df)[0])\n");
					        pythonScriptStringBuilder.append("print(cluster_id)\n\n");

					    /* ---------- 3) Write out the text file ---------- */
					        pythonScriptStringBuilder.append("# --- Write text output ---\n");
					        pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
					        pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {cluster_id}\\n')\n");
					        pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

					    /* ---------- 4) Write out the CSV ---------- */
					        pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
					        pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Cluster': [cluster_id]})\n");
					        pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
					        pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
					        pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
					        pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");
						
						

					} else if (dalib.equals("keras-tensorflow")) {
						System.err.println("ERROR: " + dalib
								+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
								+ dataAnalyticsModelAlgorithm.getName() + ".\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof GaussianMixture) { // Gaussian Mixture Clustering
					if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2
						pythonScriptStringBuilder.append("	from sklearn.cluster import GaussianMixture\n");
						pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
								+ "train_model_gaussian_mixture_clustering.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");
						
						// The actual prediction of the DA/ML model
						
						 // DBSCAN has no .predict(); if you’re using a custom wrapper keep this,
					    // otherwise switch to model.fit_predict(df) as needed.
					       pythonScriptStringBuilder.append("cluster_id = int(model.predict(df)[0])\n");
					       pythonScriptStringBuilder.append("print(cluster_id)\n\n");

					    /* ---------- 3) Write out the text file ---------- */
					      pythonScriptStringBuilder.append("# --- Write text output ---\n");
					      pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
					      pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {cluster_id}\\n')\n");
					      pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

					    /* ---------- 4) Write out the CSV ---------- */
					      pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
					      pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Cluster': [cluster_id]})\n");
					      pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
					      pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
					      pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
					      pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");

					} else if (dalib.equals("keras-tensorflow")) {
						System.err.println("ERROR: " + dalib
								+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
								+ dataAnalyticsModelAlgorithm.getName() + ".\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				}

			} else if (action.getDataAnalytics().getLabels() == Labels.SEMI) {
				// Semi-supervised ML

				if (dataAnalyticsModelAlgorithm instanceof SelfTrainingClassifier) { // Self Training Classifier for
																						// Semi-supervised ML
					if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2
						pythonScriptStringBuilder
								.append("	from sklearn.semi_supervised import SelfTrainingClassifier\n");
						pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
								+ "train_model_self_training.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						
						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");
						
						 // DBSCAN has no .predict(); if you’re using a custom wrapper keep this,
					    // otherwise switch to model.fit_predict(df) as needed.
					     pythonScriptStringBuilder.append("cluster_id = int(model.predict(df)[0])\n");
					     pythonScriptStringBuilder.append("print(cluster_id)\n\n");

					    /* ---------- 3) Write out the text file ---------- */
					       pythonScriptStringBuilder.append("# --- Write text output ---\n");
					       pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
					       pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {cluster_id}\\n')\n");
					       pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

					    /* ---------- 4) Write out the CSV ---------- */
					       pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
					       pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Cluster': [cluster_id]})\n");
					       pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
					       pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
					       pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
					       pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");

					} else if (dalib.equals("keras-tensorflow")) {
						System.err.println("ERROR: " + dalib
								+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
								+ dataAnalyticsModelAlgorithm.getName() + ".\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof LabelPropagation) { // Label Propagation for
																						// Semi-supervised ML
					if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2
						pythonScriptStringBuilder.append("	from sklearn.semi_supervised import LabelPropagation\n");
						pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
								+ "train_model_label_propagation.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						
						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");
						
						 // DBSCAN has no .predict(); if you’re using a custom wrapper keep this,
					    // otherwise switch to model.fit_predict(df) as needed.
					       pythonScriptStringBuilder.append("cluster_id = int(model.predict(df)[0])\n");
					       pythonScriptStringBuilder.append("print(cluster_id)\n\n");

					    /* ---------- 3) Write out the text file ---------- */
					      pythonScriptStringBuilder.append("# --- Write text output ---\n");
					      pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
					      pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {cluster_id}\\n')\n");
					      pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

					    /* ---------- 4) Write out the CSV ---------- */
					      pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
					      pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Cluster': [cluster_id]})\n");
					      pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
					      pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
					      pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
					      pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");

					} else if (dalib.equals("keras-tensorflow")) {
						System.err.println("ERROR: " + dalib
								+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
								+ dataAnalyticsModelAlgorithm.getName() + ".\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof LabelSpreading) { // Label Spreading for
																					// Semi-supervised ML
					if (dalib.equals("auto") || dalib.equals("scikit-learn")) { // scikit-learn v0.24.2
						pythonScriptStringBuilder.append("	from sklearn.semi_supervised import LabelSpreading\n");
						pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
								+ "train_model_label_spreading.pickle', 'rb') as pickle_file:\n");
						pythonScriptStringBuilder.append("		model = pickle.load(pickle_file)\n\n");

						pythonScriptStringBuilder.append("	df = pd.DataFrame(data={}, columns=[])\n");
						pythonScriptStringBuilder.append("	for i in range(len(feature_values_for_prediction)):\n");
						pythonScriptStringBuilder.append("		if features[i] in cat_col_names:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
						pythonScriptStringBuilder.append("		else:\n");
						pythonScriptStringBuilder.append(
								"			df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

						// The actual prediction of the DA/ML model
						
						// The actual prediction of the DA/ML model
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");
						String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
						String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
							
						pythonScriptStringBuilder.append("output_txt_path = '" + outputTxtPath + "'\n" );
		                    	        pythonScriptStringBuilder.append("output_csv_path = '" + outputCsvPath + "'\n\n");
						
						// 1) import and make sure output directories exist
                    	                        pythonScriptStringBuilder.append("import os, sys\n");
                    	                        pythonScriptStringBuilder.append("# Ensure output directories exist\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
                    	                        pythonScriptStringBuilder.append("os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");
						
						 // DBSCAN has no .predict(); if you’re using a custom wrapper keep this,
					    // otherwise switch to model.fit_predict(df) as needed.
					        pythonScriptStringBuilder.append("cluster_id = int(model.predict(df)[0])\n");
					        pythonScriptStringBuilder.append("print(cluster_id)\n\n");

					    /* ---------- 3) Write out the text file ---------- */
					       pythonScriptStringBuilder.append("# --- Write text output ---\n");
					       pythonScriptStringBuilder.append("with open(output_txt_path, 'w') as f:\n");
					       pythonScriptStringBuilder.append("    f.write(f'🔹 Prediction: {cluster_id}\\n')\n");
					       pythonScriptStringBuilder.append("print(f'🔹 Prediction saved to: {output_txt_path}')\n\n");

					    /* ---------- 4) Write out the CSV ---------- */
					       pythonScriptStringBuilder.append("# --- Write CSV output ---\n");
					       pythonScriptStringBuilder.append("df_out = pd.DataFrame({'Cluster': [cluster_id]})\n");
					       pythonScriptStringBuilder.append("if 'timestamp_for_prediction' in locals():\n");
					       pythonScriptStringBuilder.append("    df_out.insert(0, 'Timestamp', timestamp_for_prediction)\n");
					       pythonScriptStringBuilder.append("df_out.to_csv(output_csv_path, index=False)\n");
					       pythonScriptStringBuilder.append("print(f'🔹 Predictions dataset saved to: {output_csv_path}')\n");

					} else if (dalib.equals("keras-tensorflow")) {
						System.err.println("ERROR: " + dalib
								+ " is specified as the library for DA/ML. However, it does not support the chosen model/algorithm for DA/ML: "
								+ dataAnalyticsModelAlgorithm.getName() + ".\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					} else {
						System.err.println("ERROR: " + dalib + " not supported as the library for DA/ML.\n"
								+ "You may try @dalib=\"auto\" instead.\n");
					}
				}

			} else {
				System.err.println("ERROR: "
						+ "In the data analytics section of the model, labels must be specified: ON (for supervised ML, i.e., labeled data), OFF (for unsupervised ML, i.e., unlabeled data) or SEMI (for semi-supervised ML, i.e., partially labeled data).\n");
			
			
			
			
			
			
			}
			
			File pythonScriptsDir = new File(path_str + "/python-scripts");
			if (!pythonScriptsDir.exists()) {
			    pythonScriptsDir.mkdirs();
			}

			File scriptFile = new File(path_str + "/python-scripts/predict.py");

			try {
			    Files.deleteIfExists(Paths.get(scriptFile.toURI()));
			} catch (IOException e) {
			    e.printStackTrace();
			}

			try (FileWriter fr = new FileWriter(scriptFile, true);
			     BufferedWriter br = new BufferedWriter(fr)) {
			    br.append(pythonScriptStringBuilder);
			} catch (IOException e1) {
			    e1.printStackTrace();
			}

			// Close method brace
			}
			
			
			
		

 
	    else if (dataAnalyticsModelAlgorithm instanceof Time_series_ModelAlgorithm) {
	        if (action.getDataAnalytics().getLabels() == Labels.ON) {
	            if (dataAnalyticsModelAlgorithm instanceof Deep_learning_ModelAlgorithm) {
	                if (dataAnalyticsModelAlgorithm instanceof MLP) {
	                    if (dalib.equals("keras-tensorflow")) {
	                    	// Determine lag and step counts
	                    	long nLag = (action.getDataAnalytics().getLag() != null)
	                    	    ? action.getDataAnalytics().getLag().getIntValue() : 3;
	                    	long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	                    	// Compute file paths
	                    	String modelPath     = path_str + "/python-scripts/pickles/mlp_model.h5";
	                    	String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	                    	String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";

	                    	// Start fresh


	                    	// Inject paths at top of script
	                    	pythonScriptStringBuilder.append(
	                    	    "    # --- Paths ---\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    model_path      = '" + modelPath     + "'\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    output_txt_path = '" + outputTxtPath + "'\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    output_csv_path = '" + outputCsvPath + "'\n\n"
	                    	);

	                    	// Step 8: extract past window
	                    	pythonScriptStringBuilder.append(
	                    	    "    print(\"🔹 Step 8: Extracting past " + nLag + " records...\")\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    past_window = original_df[num_col_names].tail(" + nLag + ")\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    print(f\"   ✅ Past window shape: {past_window.shape}\")\n\n"
	                    	);

	                    	// Step 9: add current input
	                    	pythonScriptStringBuilder.append(
	                    	    "    print(\"🔹 Step 9: Adding current input for prediction...\")\n"
	                    	);
	                    	pythonScriptStringBuilder.append("    latest_data = {}\n");
	                    	pythonScriptStringBuilder.append("    for i in range(len(features)):\n");
	                    	pythonScriptStringBuilder.append("        if features[i] in cat_col_names:\n");
	                    	pythonScriptStringBuilder.append(
	                    	    "            latest_data[features[i]] = feature_values_for_prediction[i]\n"
	                    	);
	                    	pythonScriptStringBuilder.append("        else:\n");
	                    	pythonScriptStringBuilder.append(
	                    	    "            latest_data[features[i]] = float(feature_values_for_prediction[i])\n"
	                    	);
	                    	pythonScriptStringBuilder.append("    latest_df = pd.DataFrame([latest_data])\n");
	                    	pythonScriptStringBuilder.append(
	                    	    "    print(\"   📥 Latest input row:\")\n"
	                    	);
	                    	pythonScriptStringBuilder.append("    print(latest_df)\n\n");

	                    	// Combine past & latest
	                    	pythonScriptStringBuilder.append(
	                    	    "    combined_df = pd.concat([past_window, latest_df], ignore_index=True)\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    print(\"   🧊 Combined input for supervised conversion:\")\n"
	                    	);
	                    	pythonScriptStringBuilder.append("    print(combined_df)\n\n");

	                    	// Ensure numeric
	                    	pythonScriptStringBuilder.append(
	                    	    "    combined_df[num_col_names] = combined_df[num_col_names]"
	                    	  + ".apply(pd.to_numeric, errors='coerce')\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    combined_df.fillna(method='ffill', inplace=True)\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    combined_df.interpolate(method='linear', inplace=True)\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    combined_df.fillna(method='bfill', inplace=True)\n\n"
	                    	);

	                    	// Supervised helper
	                    	pythonScriptStringBuilder.append(
	                    	    "    def series_to_supervised(data, n_in=1, n_out=1, dropnan=True):\n"
	                    	);
	                    	pythonScriptStringBuilder.append("        n_vars = data.shape[1]\n");
	                    	pythonScriptStringBuilder.append("        df = pd.DataFrame(data)\n");
	                    	pythonScriptStringBuilder.append("        cols, names = list(), list()\n\n");
	                    	pythonScriptStringBuilder.append("        # input sequence (t-n ... t-1)\n");
	                    	pythonScriptStringBuilder.append("        for i in range(n_in, 0, -1):\n");
	                    	pythonScriptStringBuilder.append("            cols.append(df.shift(i))\n");
	                    	pythonScriptStringBuilder.append(
	                    	    "            names += [f'var{j+1}(t-{i})' for j in range(n_vars)]\n\n"
	                    	);
	                    	pythonScriptStringBuilder.append("        # forecast sequence (t ... t+n)\n");
	                    	pythonScriptStringBuilder.append("        for i in range(0, n_out):\n");
	                    	pythonScriptStringBuilder.append("            cols.append(df.iloc[:, 0].shift(-i))\n");
	                    	pythonScriptStringBuilder.append("            if i == 0:\n");
	                    	pythonScriptStringBuilder.append(
	                    	    "                names += ['var1(t)']\n"
	                    	);
	                    	pythonScriptStringBuilder.append("            else:\n");
	                    	pythonScriptStringBuilder.append(
	                    	    "                names += [f'var1(t+{i})']\n\n"
	                    	);
	                    	pythonScriptStringBuilder.append("        agg = pd.concat(cols, axis=1)\n");
	                    	pythonScriptStringBuilder.append("        agg.columns = names\n");
	                    	pythonScriptStringBuilder.append("        if dropnan:\n");
	                    	pythonScriptStringBuilder.append("            agg = agg.dropna()\n\n");
	                    	pythonScriptStringBuilder.append("        return agg\n\n");

	                    	// Apply nLag & nSteps
	                    	pythonScriptStringBuilder.append(
	                    	    "    n_in = " + nLag + "  # lag records count\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    n_out = " + nSteps + "  # forecast steps\n\n"
	                    	);

	                    	// Generate supervised DataFrame
	                    	pythonScriptStringBuilder.append(
	                    	    "    supervised_df = series_to_supervised(combined_df, n_in=n_in, n_out=n_out , dropnan=False)\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    print(\"🔹 Supervised data shape:\", supervised_df.shape)\n"
	                    	);
	                    	pythonScriptStringBuilder.append("    print(supervised_df.tail())\n\n");

	                    	// Build X_pred
	                    	pythonScriptStringBuilder.append(
	                    	    "    input_cols = len(features) * n_in\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    X_pred = supervised_df.iloc[-1, :input_cols].values.reshape(1, -1)\n"
	                    	);
	                    	pythonScriptStringBuilder.append(
	                    	    "    print(\"   📐 X_pred shape:\", X_pred.shape)\n\n"
	                    	);

	                    	// 1) import and make sure output directories exist
	                    	pythonScriptStringBuilder.append("    import os, sys\n");
	                    	pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
	                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
	                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");

	                    	// 2) check that the model file is there, then load & predict
	                    	pythonScriptStringBuilder.append("    if not os.path.exists(model_path):\n");
	                    	pythonScriptStringBuilder.append("        print(f\"❌ Model not found at {model_path}\")\n");
	                    	pythonScriptStringBuilder.append("        sys.exit(1)\n\n");

	                    	pythonScriptStringBuilder.append("    print(f\"✅ Loading model from {model_path}...\")\n");
	                    	pythonScriptStringBuilder.append("    import tensorflow as tf\n");
	                    	pythonScriptStringBuilder.append("    from tensorflow.keras.models import load_model\n");
	                    	pythonScriptStringBuilder.append("    custom_objs = {'mse': tf.keras.losses.get('mean_squared_error')}\n");
	                    	pythonScriptStringBuilder.append("    model = load_model(model_path, custom_objects=custom_objs)\n");
	                    	pythonScriptStringBuilder.append("    pred = model.predict(X_pred).flatten()\n\n");

	                    	// 3) write out the text file
	                    	pythonScriptStringBuilder.append("    # --- Write text output ---\n");
	                    	pythonScriptStringBuilder.append("    formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
	                    	pythonScriptStringBuilder.append("    with open(output_txt_path, \"w\") as f:\n");
	                    	pythonScriptStringBuilder.append("        f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
	                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions saved to: {output_txt_path}\")\n\n");

	                    	// 4) write out the CSV
	                    	pythonScriptStringBuilder.append("    # --- Write CSV output ---\n");
	                    	pythonScriptStringBuilder.append("    import pandas as pd\n");
	                    	pythonScriptStringBuilder.append("    cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
	                    	pythonScriptStringBuilder.append("    df_out = pd.DataFrame([pred], columns=cols)\n");
	                    	pythonScriptStringBuilder.append("    if 'timestamp_for_prediction' in locals():\n");
	                    	pythonScriptStringBuilder.append("        df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
	                    	pythonScriptStringBuilder.append("    df_out.to_csv(output_csv_path, index=False)\n");
	                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions dataset saved to: {output_csv_path}\")\n");


	                    }
	                } else if (dataAnalyticsModelAlgorithm instanceof GRU) {
	                    if (dalib.equals("keras-tensorflow")) {
	                    	long nLag = (action.getDataAnalytics().getLag() != null)
		                    	    ? action.getDataAnalytics().getLag().getIntValue() : 3;
		                    	long nSteps = (action.getDataAnalytics().getSteps() != null)
		                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

		                    	// Compute file paths
		                    	String modelPath     = path_str + "/python-scripts/pickles/gru_model.h5";
		                    	String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
		                    	String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";

		                    	// Start fresh


		                    	// Inject paths at top of script
		                    	pythonScriptStringBuilder.append(
		                    	    "    # --- Paths ---\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    model_path      = '" + modelPath     + "'\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    output_txt_path = '" + outputTxtPath + "'\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    output_csv_path = '" + outputCsvPath + "'\n\n"
		                    	);

		                    	// Step 8: extract past window
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Step 8: Extracting past " + nLag + " records...\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    past_window = original_df[num_col_names].tail(" + nLag + ")\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(f\"   ✅ Past window shape: {past_window.shape}\")\n\n"
		                    	);

		                    	// Step 9: add current input
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Step 9: Adding current input for prediction...\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    latest_data = {}\n");
		                    	pythonScriptStringBuilder.append("    for i in range(len(features)):\n");
		                    	pythonScriptStringBuilder.append("        if features[i] in cat_col_names:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            latest_data[features[i]] = feature_values_for_prediction[i]\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        else:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            latest_data[features[i]] = float(feature_values_for_prediction[i])\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    latest_df = pd.DataFrame([latest_data])\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   📥 Latest input row:\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(latest_df)\n\n");

		                    	// Combine past & latest
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df = pd.concat([past_window, latest_df], ignore_index=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   🧊 Combined input for supervised conversion:\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(combined_df)\n\n");

		                    	// Ensure numeric
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df[num_col_names] = combined_df[num_col_names]"
		                    	  + ".apply(pd.to_numeric, errors='coerce')\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.fillna(method='ffill', inplace=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.interpolate(method='linear', inplace=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.fillna(method='bfill', inplace=True)\n\n"
		                    	);

		                    	// Supervised helper
		                    	pythonScriptStringBuilder.append(
		                    	    "    def series_to_supervised(data, n_in=1, n_out=1, dropnan=True):\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        n_vars = data.shape[1]\n");
		                    	pythonScriptStringBuilder.append("        df = pd.DataFrame(data)\n");
		                    	pythonScriptStringBuilder.append("        cols, names = list(), list()\n\n");
		                    	pythonScriptStringBuilder.append("        # input sequence (t-n ... t-1)\n");
		                    	pythonScriptStringBuilder.append("        for i in range(n_in, 0, -1):\n");
		                    	pythonScriptStringBuilder.append("            cols.append(df.shift(i))\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            names += [f'var{j+1}(t-{i})' for j in range(n_vars)]\n\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        # forecast sequence (t ... t+n)\n");
		                    	pythonScriptStringBuilder.append("        for i in range(0, n_out):\n");
		                    	pythonScriptStringBuilder.append("            cols.append(df.iloc[:, 0].shift(-i))\n");
		                    	pythonScriptStringBuilder.append("            if i == 0:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "                names += ['var1(t)']\n"
		                    	);
		                    	pythonScriptStringBuilder.append("            else:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "                names += [f'var1(t+{i})']\n\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        agg = pd.concat(cols, axis=1)\n");
		                    	pythonScriptStringBuilder.append("        agg.columns = names\n");
		                    	pythonScriptStringBuilder.append("        if dropnan:\n");
		                    	pythonScriptStringBuilder.append("            agg = agg.dropna()\n\n");
		                    	pythonScriptStringBuilder.append("        return agg\n\n");

		                    	// Apply nLag & nSteps
		                    	pythonScriptStringBuilder.append(
		                    	    "    n_in = " + nLag + "  # lag records count\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    n_out = " + nSteps + "  # forecast steps\n\n"
		                    	);

		                    	// Generate supervised DataFrame
		                    	pythonScriptStringBuilder.append(
		                    	    "    supervised_df = series_to_supervised(combined_df, n_in=n_in, n_out=n_out , dropnan=False)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Supervised data shape:\", supervised_df.shape)\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(supervised_df.tail())\n\n");

		                    	// Build X_pred
		                    	pythonScriptStringBuilder.append(
		                    	    "    input_cols = len(features) * n_in\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    X_pred = supervised_df.iloc[-1, :input_cols].values.reshape(1, -1)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   📐 X_pred shape:\", X_pred.shape)\n\n"
		                    	);

		                    	// 1) import and make sure output directories exist
		                    	pythonScriptStringBuilder.append("    import os, sys\n");
		                    	pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
		                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
		                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");

		                    	// 2) check that the model file is there, then load & predict
		                    	pythonScriptStringBuilder.append("    if not os.path.exists(model_path):\n");
		                    	pythonScriptStringBuilder.append("        print(f\"❌ Model not found at {model_path}\")\n");
		                    	pythonScriptStringBuilder.append("        sys.exit(1)\n\n");

		                    	pythonScriptStringBuilder.append("    print(f\"✅ Loading model from {model_path}...\")\n");
		                    	pythonScriptStringBuilder.append("    import tensorflow as tf\n");
		                    	pythonScriptStringBuilder.append("    from tensorflow.keras.models import load_model\n");
		                    	pythonScriptStringBuilder.append("    custom_objs = {'mse': tf.keras.losses.get('mean_squared_error')}\n");
		                    	pythonScriptStringBuilder.append("    model = load_model(model_path, custom_objects=custom_objs)\n");
		                    	pythonScriptStringBuilder.append("    pred = model.predict(X_pred).flatten()\n\n");

		                    	// 3) write out the text file
		                    	pythonScriptStringBuilder.append("    # --- Write text output ---\n");
		                    	pythonScriptStringBuilder.append("    formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
		                    	pythonScriptStringBuilder.append("    with open(output_txt_path, \"w\") as f:\n");
		                    	pythonScriptStringBuilder.append("        f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
		                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions saved to: {output_txt_path}\")\n\n");

		                    	// 4) write out the CSV
		                    	pythonScriptStringBuilder.append("    # --- Write CSV output ---\n");
		                    	pythonScriptStringBuilder.append("    import pandas as pd\n");
		                    	pythonScriptStringBuilder.append("    cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
		                    	pythonScriptStringBuilder.append("    df_out = pd.DataFrame([pred], columns=cols)\n");
		                    	pythonScriptStringBuilder.append("    if 'timestamp_for_prediction' in locals():\n");
		                    	pythonScriptStringBuilder.append("        df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
		                    	pythonScriptStringBuilder.append("    df_out.to_csv(output_csv_path, index=False)\n");
		                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions dataset saved to: {output_csv_path}\")\n");


	                    }
	                } else if (dataAnalyticsModelAlgorithm instanceof CNN) {
	                    if (dalib.equals("keras-tensorflow")) {
	                    	long nLag = (action.getDataAnalytics().getLag() != null)
		                    	    ? action.getDataAnalytics().getLag().getIntValue() : 3;
		                    	long nSteps = (action.getDataAnalytics().getSteps() != null)
		                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

		                    	// Compute file paths
		                    	String modelPath     = path_str + "/python-scripts/pickles/cnn_model.pkl";
		                    	String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
		                    	String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";

		                    	// Start fresh


		                    	// Inject paths at top of script
		                    	pythonScriptStringBuilder.append(
		                    	    "    # --- Paths ---\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    model_path      = '" + modelPath     + "'\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    output_txt_path = '" + outputTxtPath + "'\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    output_csv_path = '" + outputCsvPath + "'\n\n"
		                    	);

		                    	// Step 8: extract past window
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Step 8: Extracting past " + nLag + " records...\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    past_window = original_df[num_col_names].tail(" + nLag + ")\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(f\"   ✅ Past window shape: {past_window.shape}\")\n\n"
		                    	);

		                    	// Step 9: add current input
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Step 9: Adding current input for prediction...\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    latest_data = {}\n");
		                    	pythonScriptStringBuilder.append("    for i in range(len(features)):\n");
		                    	pythonScriptStringBuilder.append("        if features[i] in cat_col_names:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            latest_data[features[i]] = feature_values_for_prediction[i]\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        else:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            latest_data[features[i]] = float(feature_values_for_prediction[i])\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    latest_df = pd.DataFrame([latest_data])\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   📥 Latest input row:\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(latest_df)\n\n");

		                    	// Combine past & latest
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df = pd.concat([past_window, latest_df], ignore_index=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   🧊 Combined input for supervised conversion:\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(combined_df)\n\n");

		                    	// Ensure numeric
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df[num_col_names] = combined_df[num_col_names]"
		                    	  + ".apply(pd.to_numeric, errors='coerce')\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.fillna(method='ffill', inplace=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.interpolate(method='linear', inplace=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.fillna(method='bfill', inplace=True)\n\n"
		                    	);

		                    	// Supervised helper
		                    	pythonScriptStringBuilder.append(
		                    	    "    def series_to_supervised(data, n_in=1, n_out=1, dropnan=True):\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        n_vars = data.shape[1]\n");
		                    	pythonScriptStringBuilder.append("        df = pd.DataFrame(data)\n");
		                    	pythonScriptStringBuilder.append("        cols, names = list(), list()\n\n");
		                    	pythonScriptStringBuilder.append("        # input sequence (t-n ... t-1)\n");
		                    	pythonScriptStringBuilder.append("        for i in range(n_in, 0, -1):\n");
		                    	pythonScriptStringBuilder.append("            cols.append(df.shift(i))\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            names += [f'var{j+1}(t-{i})' for j in range(n_vars)]\n\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        # forecast sequence (t ... t+n)\n");
		                    	pythonScriptStringBuilder.append("        for i in range(0, n_out):\n");
		                    	pythonScriptStringBuilder.append("            cols.append(df.iloc[:, 0].shift(-i))\n");
		                    	pythonScriptStringBuilder.append("            if i == 0:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "                names += ['var1(t)']\n"
		                    	);
		                    	pythonScriptStringBuilder.append("            else:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "                names += [f'var1(t+{i})']\n\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        agg = pd.concat(cols, axis=1)\n");
		                    	pythonScriptStringBuilder.append("        agg.columns = names\n");
		                    	pythonScriptStringBuilder.append("        if dropnan:\n");
		                    	pythonScriptStringBuilder.append("            agg = agg.dropna()\n\n");
		                    	pythonScriptStringBuilder.append("        return agg\n\n");

		                    	// Apply nLag & nSteps
		                    	pythonScriptStringBuilder.append(
		                    	    "    n_in = " + nLag + "  # lag records count\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    n_out = " + nSteps + "  # forecast steps\n\n"
		                    	);

		                    	// Generate supervised DataFrame
		                    	pythonScriptStringBuilder.append(
		                    	    "    supervised_df = series_to_supervised(combined_df, n_in=n_in, n_out=n_out , dropnan=False)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Supervised data shape:\", supervised_df.shape)\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(supervised_df.tail())\n\n");

		                    	// Build X_pred
		                    	pythonScriptStringBuilder.append(
		                    	    "    input_cols = len(features) * n_in\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    X_pred = supervised_df.iloc[-1, :input_cols].values.reshape(1, -1)\n"
		                    	);
		                    	
		                    	pythonScriptStringBuilder.append("    if X_pred.ndim == 2:\n");
		                    	pythonScriptStringBuilder.append("        X_pred = X_pred.reshape((X_pred.shape[0], 1, X_pred.shape[1]))\n");
		                    	pythonScriptStringBuilder.append("    print(f\"✅ X_pred reshaped to: {X_pred.shape}\")\n\n");

		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   📐 X_pred shape:\", X_pred.shape)\n\n"
		                    	);

		                    	// 1) import and make sure output directories exist
		                    	pythonScriptStringBuilder.append("    import os, sys\n");
		                    	pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
		                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
		                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");

		                    	// 2) check that the model file is there, then load & predict
		                    	pythonScriptStringBuilder.append("    if not os.path.exists(model_path):\n");
		                    	pythonScriptStringBuilder.append("        print(f\"❌ Model not found at {model_path}\")\n");
		                    	pythonScriptStringBuilder.append("        sys.exit(1)\n\n");

		                    	pythonScriptStringBuilder.append("    print(f\"✅ Loading model from {model_path}...\")\n");
		                    	pythonScriptStringBuilder.append("    import pickle\n");
		                    	pythonScriptStringBuilder.append("    from tensorflow.keras.models import model_from_json\n");
		                    	pythonScriptStringBuilder.append("    with open(model_path, 'rb') as f:\n");
		                    	pythonScriptStringBuilder.append("        data = pickle.load(f)\n");
		                    	pythonScriptStringBuilder.append("        model = model_from_json(data['model_json'])\n");
		                    	pythonScriptStringBuilder.append("        model.set_weights(data['weights'])\n");
		                    	pythonScriptStringBuilder.append("    model.compile(optimizer='adam', loss='mse', metrics=['mse'])\n");
		                    	pythonScriptStringBuilder.append("    print('✅ CNN model loaded from .pkl and compiled')\n\n");

		                    	pythonScriptStringBuilder.append("    pred = model.predict(X_pred).flatten()\n\n");


		                    	// 3) write out the text file
		                    	pythonScriptStringBuilder.append("    # --- Write text output ---\n");
		                    	pythonScriptStringBuilder.append("    formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
		                    	pythonScriptStringBuilder.append("    with open(output_txt_path, \"w\") as f:\n");
		                    	pythonScriptStringBuilder.append("        f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
		                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions saved to: {output_txt_path}\")\n\n");

		                    	// 4) write out the CSV
		                    	pythonScriptStringBuilder.append("    # --- Write CSV output ---\n");
		                    	pythonScriptStringBuilder.append("    import pandas as pd\n");
		                    	pythonScriptStringBuilder.append("    cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
		                    	pythonScriptStringBuilder.append("    df_out = pd.DataFrame([pred], columns=cols)\n");
		                    	pythonScriptStringBuilder.append("    if 'timestamp_for_prediction' in locals():\n");
		                    	pythonScriptStringBuilder.append("        df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
		                    	pythonScriptStringBuilder.append("    df_out.to_csv(output_csv_path, index=False)\n");
		                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions dataset saved to: {output_csv_path}\")\n");


	                    }
	                } else if (dataAnalyticsModelAlgorithm instanceof LSTM) {
	                    if (dalib.equals("keras-tensorflow")) {
	                    	long nLag = (action.getDataAnalytics().getLag() != null)
		                    	    ? action.getDataAnalytics().getLag().getIntValue() : 3;
		                    	long nSteps = (action.getDataAnalytics().getSteps() != null)
		                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

		                    	// Compute file paths
		                    	String modelPath     = path_str + "/python-scripts/pickles/lstm_model.h5";
		                    	String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
		                    	String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";

		                    	// Start fresh


		                    	// Inject paths at top of script
		                    	pythonScriptStringBuilder.append(
		                    	    "    # --- Paths ---\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    model_path      = '" + modelPath     + "'\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    output_txt_path = '" + outputTxtPath + "'\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    output_csv_path = '" + outputCsvPath + "'\n\n"
		                    	);

		                    	// Step 8: extract past window
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Step 8: Extracting past " + nLag + " records...\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    past_window = original_df[num_col_names].tail(" + nLag + ")\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(f\"   ✅ Past window shape: {past_window.shape}\")\n\n"
		                    	);

		                    	// Step 9: add current input
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Step 9: Adding current input for prediction...\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    latest_data = {}\n");
		                    	pythonScriptStringBuilder.append("    for i in range(len(features)):\n");
		                    	pythonScriptStringBuilder.append("        if features[i] in cat_col_names:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            latest_data[features[i]] = feature_values_for_prediction[i]\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        else:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            latest_data[features[i]] = float(feature_values_for_prediction[i])\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    latest_df = pd.DataFrame([latest_data])\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   📥 Latest input row:\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(latest_df)\n\n");

		                    	// Combine past & latest
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df = pd.concat([past_window, latest_df], ignore_index=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   🧊 Combined input for supervised conversion:\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(combined_df)\n\n");

		                    	// Ensure numeric
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df[num_col_names] = combined_df[num_col_names]"
		                    	  + ".apply(pd.to_numeric, errors='coerce')\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.fillna(method='ffill', inplace=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.interpolate(method='linear', inplace=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.fillna(method='bfill', inplace=True)\n\n"
		                    	);

		                    	// Supervised helper
		                    	pythonScriptStringBuilder.append(
		                    	    "    def series_to_supervised(data, n_in=1, n_out=1, dropnan=True):\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        n_vars = data.shape[1]\n");
		                    	pythonScriptStringBuilder.append("        df = pd.DataFrame(data)\n");
		                    	pythonScriptStringBuilder.append("        cols, names = list(), list()\n\n");
		                    	pythonScriptStringBuilder.append("        # input sequence (t-n ... t-1)\n");
		                    	pythonScriptStringBuilder.append("        for i in range(n_in, 0, -1):\n");
		                    	pythonScriptStringBuilder.append("            cols.append(df.shift(i))\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            names += [f'var{j+1}(t-{i})' for j in range(n_vars)]\n\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        # forecast sequence (t ... t+n)\n");
		                    	pythonScriptStringBuilder.append("        for i in range(0, n_out):\n");
		                    	pythonScriptStringBuilder.append("            cols.append(df.iloc[:, 0].shift(-i))\n");
		                    	pythonScriptStringBuilder.append("            if i == 0:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "                names += ['var1(t)']\n"
		                    	);
		                    	pythonScriptStringBuilder.append("            else:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "                names += [f'var1(t+{i})']\n\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        agg = pd.concat(cols, axis=1)\n");
		                    	pythonScriptStringBuilder.append("        agg.columns = names\n");
		                    	pythonScriptStringBuilder.append("        if dropnan:\n");
		                    	pythonScriptStringBuilder.append("            agg = agg.dropna()\n\n");
		                    	pythonScriptStringBuilder.append("        return agg\n\n");

		                    	// Apply nLag & nSteps
		                    	pythonScriptStringBuilder.append(
		                    	    "    n_in = " + nLag + "  # lag records count\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    n_out = " + nSteps + "  # forecast steps\n\n"
		                    	);

		                    	// Generate supervised DataFrame
		                    	pythonScriptStringBuilder.append(
		                    	    "    supervised_df = series_to_supervised(combined_df, n_in=n_in, n_out=n_out , dropnan=False)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Supervised data shape:\", supervised_df.shape)\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(supervised_df.tail())\n\n");

		                    	// Build X_pred
		                    	
		                    	
		                    	pythonScriptStringBuilder.append(
		                    		    "    input_cols  = len(features) * n_in\n");
		                    	pythonScriptStringBuilder.append(
		                    		    "    n_features  = len(features)\n");
		                    	pythonScriptStringBuilder.append(
		                    		    "    X_flat      = supervised_df.iloc[-1, :input_cols].values.astype(float)\n");
		                    	pythonScriptStringBuilder.append(
		                    		    "    # LSTM was trained on a single-timestep tensor (1, 1, input_cols)\n");
		                    	pythonScriptStringBuilder.append(
		                    		    "    X_pred      = X_flat.reshape(1, 1, input_cols)\n");
		                    	pythonScriptStringBuilder.append(
		                    		    "    print(f\"   📐 X_pred shape: {X_pred.shape}\")\n\n");
		                    

		                    	// 1) import and make sure output directories exist
		                    	pythonScriptStringBuilder.append("    import os, sys\n");
		                    	pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
		                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
		                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");

		                    	// 2) check that the model file is there, then load & predict
		                    	pythonScriptStringBuilder.append("    if not os.path.exists(model_path):\n");
		                    	pythonScriptStringBuilder.append("        print(f\"❌ Model not found at {model_path}\")\n");
		                    	pythonScriptStringBuilder.append("        sys.exit(1)\n\n");

		                    	pythonScriptStringBuilder.append("    print(f\"✅ Loading model from {model_path}...\")\n");
		                    	pythonScriptStringBuilder.append("    import tensorflow as tf\n");
		                    	pythonScriptStringBuilder.append("    from tensorflow.keras.models import load_model\n");
		                    	pythonScriptStringBuilder.append("    custom_objs = {'mse': tf.keras.losses.get('mean_squared_error')}\n");
		                    	pythonScriptStringBuilder.append("    model = load_model(model_path, custom_objects=custom_objs)\n");
		                    	pythonScriptStringBuilder.append("    pred = model.predict(X_pred).flatten()\n\n");

		                    	// 3) write out the text file
		                    	pythonScriptStringBuilder.append("    # --- Write text output ---\n");
		                    	pythonScriptStringBuilder.append("    formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
		                    	pythonScriptStringBuilder.append("    with open(output_txt_path, \"w\") as f:\n");
		                    	pythonScriptStringBuilder.append("        f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
		                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions saved to: {output_txt_path}\")\n\n");

		                    	// 4) write out the CSV
		                    	pythonScriptStringBuilder.append("    # --- Write CSV output ---\n");
		                    	pythonScriptStringBuilder.append("    import pandas as pd\n");
		                    	pythonScriptStringBuilder.append("    cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
		                    	pythonScriptStringBuilder.append("    df_out = pd.DataFrame([pred], columns=cols)\n");
		                    	pythonScriptStringBuilder.append("    if 'timestamp_for_prediction' in locals():\n");
		                    	pythonScriptStringBuilder.append("        df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
		                    	pythonScriptStringBuilder.append("    df_out.to_csv(output_csv_path, index=False)\n");
		                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions dataset saved to: {output_csv_path}\")\n");


	                    }
	                } else if (dataAnalyticsModelAlgorithm instanceof RNN) {
	                    if (dalib.equals("keras-tensorflow")) {
	                    	long nLag = (action.getDataAnalytics().getLag() != null)
		                    	    ? action.getDataAnalytics().getLag().getIntValue() : 3;
		                    	long nSteps = (action.getDataAnalytics().getSteps() != null)
		                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

		                    	// Compute file paths
		                    	String modelPath     = path_str + "/python-scripts/pickles/rnn_model.h5";
		                    	String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
		                    	String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";

		                    	// Start fresh


		                    	// Inject paths at top of script
		                    	pythonScriptStringBuilder.append(
		                    	    "    # --- Paths ---\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    model_path      = '" + modelPath     + "'\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    output_txt_path = '" + outputTxtPath + "'\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    output_csv_path = '" + outputCsvPath + "'\n\n"
		                    	);

		                    	// Step 8: extract past window
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Step 8: Extracting past " + nLag + " records...\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    past_window = original_df[num_col_names].tail(" + nLag + ")\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(f\"   ✅ Past window shape: {past_window.shape}\")\n\n"
		                    	);

		                    	// Step 9: add current input
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Step 9: Adding current input for prediction...\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    latest_data = {}\n");
		                    	pythonScriptStringBuilder.append("    for i in range(len(features)):\n");
		                    	pythonScriptStringBuilder.append("        if features[i] in cat_col_names:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            latest_data[features[i]] = feature_values_for_prediction[i]\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        else:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            latest_data[features[i]] = float(feature_values_for_prediction[i])\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    latest_df = pd.DataFrame([latest_data])\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   📥 Latest input row:\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(latest_df)\n\n");

		                    	// Combine past & latest
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df = pd.concat([past_window, latest_df], ignore_index=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   🧊 Combined input for supervised conversion:\")\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(combined_df)\n\n");

		                    	// Ensure numeric
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df[num_col_names] = combined_df[num_col_names]"
		                    	  + ".apply(pd.to_numeric, errors='coerce')\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.fillna(method='ffill', inplace=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.interpolate(method='linear', inplace=True)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    combined_df.fillna(method='bfill', inplace=True)\n\n"
		                    	);

		                    	// Supervised helper
		                    	pythonScriptStringBuilder.append(
		                    	    "    def series_to_supervised(data, n_in=1, n_out=1, dropnan=True):\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        n_vars = data.shape[1]\n");
		                    	pythonScriptStringBuilder.append("        df = pd.DataFrame(data)\n");
		                    	pythonScriptStringBuilder.append("        cols, names = list(), list()\n\n");
		                    	pythonScriptStringBuilder.append("        # input sequence (t-n ... t-1)\n");
		                    	pythonScriptStringBuilder.append("        for i in range(n_in, 0, -1):\n");
		                    	pythonScriptStringBuilder.append("            cols.append(df.shift(i))\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "            names += [f'var{j+1}(t-{i})' for j in range(n_vars)]\n\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        # forecast sequence (t ... t+n)\n");
		                    	pythonScriptStringBuilder.append("        for i in range(0, n_out):\n");
		                    	pythonScriptStringBuilder.append("            cols.append(df.iloc[:, 0].shift(-i))\n");
		                    	pythonScriptStringBuilder.append("            if i == 0:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "                names += ['var1(t)']\n"
		                    	);
		                    	pythonScriptStringBuilder.append("            else:\n");
		                    	pythonScriptStringBuilder.append(
		                    	    "                names += [f'var1(t+{i})']\n\n"
		                    	);
		                    	pythonScriptStringBuilder.append("        agg = pd.concat(cols, axis=1)\n");
		                    	pythonScriptStringBuilder.append("        agg.columns = names\n");
		                    	pythonScriptStringBuilder.append("        if dropnan:\n");
		                    	pythonScriptStringBuilder.append("            agg = agg.dropna()\n\n");
		                    	pythonScriptStringBuilder.append("        return agg\n\n");

		                    	// Apply nLag & nSteps
		                    	pythonScriptStringBuilder.append(
		                    	    "    n_in = " + nLag + "  # lag records count\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    n_out = " + nSteps + "  # forecast steps\n\n"
		                    	);

		                    	// Generate supervised DataFrame
		                    	pythonScriptStringBuilder.append(
		                    	    "    supervised_df = series_to_supervised(combined_df, n_in=n_in, n_out=n_out , dropnan=False)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"🔹 Supervised data shape:\", supervised_df.shape)\n"
		                    	);
		                    	pythonScriptStringBuilder.append("    print(supervised_df.tail())\n\n");

		                    	// Build X_pred
		                    	pythonScriptStringBuilder.append(
		                    	    "    input_cols = len(features) * n_in\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    X_pred = supervised_df.iloc[-1, :input_cols].values.reshape(1, -1)\n"
		                    	);
		                    	pythonScriptStringBuilder.append(
		                    	    "    print(\"   📐 X_pred shape:\", X_pred.shape)\n\n"
		                    	);

		                    	// 1) import and make sure output directories exist
		                    	pythonScriptStringBuilder.append("    import os, sys\n");
		                    	pythonScriptStringBuilder.append("    # Ensure output directories exist\n");
		                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n");
		                    	pythonScriptStringBuilder.append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n\n");

		                    	// 2) check that the model file is there, then load & predict
		                    	pythonScriptStringBuilder.append("    if not os.path.exists(model_path):\n");
		                    	pythonScriptStringBuilder.append("        print(f\"❌ Model not found at {model_path}\")\n");
		                    	pythonScriptStringBuilder.append("        sys.exit(1)\n\n");

		                    	pythonScriptStringBuilder.append("    print(f\"✅ Loading model from {model_path}...\")\n");
		                    	pythonScriptStringBuilder.append("    import tensorflow as tf\n");
		                    	pythonScriptStringBuilder.append("    from tensorflow.keras.models import load_model\n");
		                    	pythonScriptStringBuilder.append("    custom_objs = {'mse': tf.keras.losses.get('mean_squared_error')}\n");
		                    	pythonScriptStringBuilder.append("    model = load_model(model_path, custom_objects=custom_objs)\n");
		                    	pythonScriptStringBuilder.append("    pred = model.predict(X_pred).flatten()\n\n");

		                    	// 3) write out the text file
		                    	pythonScriptStringBuilder.append("    # --- Write text output ---\n");
		                    	pythonScriptStringBuilder.append("    formatted = \", \".join(f\"{p:.2f}\" for p in pred)\n");
		                    	pythonScriptStringBuilder.append("    with open(output_txt_path, \"w\") as f:\n");
		                    	pythonScriptStringBuilder.append("        f.write(f\"🔹 Prediction: [{formatted}]\\n\")\n");
		                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions saved to: {output_txt_path}\")\n\n");

		                    	// 4) write out the CSV
		                    	pythonScriptStringBuilder.append("    # --- Write CSV output ---\n");
		                    	pythonScriptStringBuilder.append("    import pandas as pd\n");
		                    	pythonScriptStringBuilder.append("    cols = [f\"Forecast_t+{i+1}\" for i in range(len(pred))]\n");
		                    	pythonScriptStringBuilder.append("    df_out = pd.DataFrame([pred], columns=cols)\n");
		                    	pythonScriptStringBuilder.append("    if 'timestamp_for_prediction' in locals():\n");
		                    	pythonScriptStringBuilder.append("        df_out.insert(0, \"Timestamp\", timestamp_for_prediction)\n");
		                    	pythonScriptStringBuilder.append("    df_out.to_csv(output_csv_path, index=False)\n");
		                    	pythonScriptStringBuilder.append("    print(f\"🔹 Predictions dataset saved to: {output_csv_path}\")\n");


	                    }
	                } else if (dataAnalyticsModelAlgorithm instanceof TCN) {
	                    if (dalib.equals("keras-tensorflow")) {
	                        pythonScriptStringBuilder.append("from tensorflow.keras.models import load_model\n");
	                        pythonScriptStringBuilder.append("import pandas as pd\n");
	                        pythonScriptStringBuilder.append("model = load_model('" + path_str + "/python-scripts/pickles/train_model_tcn_weights.h5')\n");
	                        pythonScriptStringBuilder.append("df = pd.DataFrame(data={}, columns=[])\n");
	                        pythonScriptStringBuilder.append("for i in range(len(feature_values_for_prediction)):\n");
	                        pythonScriptStringBuilder.append("    if features[i] in cat_col_names:\n");
	                        pythonScriptStringBuilder.append("        df.insert(i, features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
	                        pythonScriptStringBuilder.append("    else:\n");
	                        pythonScriptStringBuilder.append("        df.insert(i, features[i], pd.Series(feature_values_for_prediction[i]))\n");
	                        pythonScriptStringBuilder.append("prediction = model.predict(df)\n");
	                        pythonScriptStringBuilder.append("print(prediction[0][0])\n\n");
	                    }
	                } else if (dataAnalyticsModelAlgorithm instanceof Transformer) {
	                    if (dalib.equals("keras-tensorflow")) {
	                        pythonScriptStringBuilder.append("from tensorflow.keras.models import load_model\n");
	                        pythonScriptStringBuilder.append("import pandas as pd\n");
	                        pythonScriptStringBuilder.append("model = load_model('" + path_str + "/python-scripts/pickles/train_model_transformer_weights.h5')\n");
	                        pythonScriptStringBuilder.append("df = pd.DataFrame(data={}, columns=[])\n");
	                        pythonScriptStringBuilder.append("for i in range(len(feature_values_for_prediction)):\n");
	                        pythonScriptStringBuilder.append("    if features[i] in cat_col_names:\n");
	                        pythonScriptStringBuilder.append("        df.insert(i, features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
	                        pythonScriptStringBuilder.append("    else:\n");
	                        pythonScriptStringBuilder.append("        df.insert(i, features[i], pd.Series(feature_values_for_prediction[i]))\n");
	                        pythonScriptStringBuilder.append("prediction = model.predict(df)\n");
	                        pythonScriptStringBuilder.append("print(prediction[0][0])\n\n");
	                    }
	                }
	            } else if (dataAnalyticsModelAlgorithm instanceof statistical_ModelAlgorithm) {
	                // ARIMA
	            	if (dataAnalyticsModelAlgorithm instanceof ARIMA) {

	            	    String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";

	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	            	                  ? action.getDataAnalytics().getSteps().getIntValue()
	            	                  : 1;

	            	    pythonScriptStringBuilder
	            	        /* ── imports & constants ───────────────────────────────────────── */
	            	        .append("    import os, pickle\n")
	            	        .append("    import pandas as pd\n")
	            	        .append("\n")
	            	        .append("    n_out = ").append(nSteps).append("\n")      // ← constant
	            	        .append("\n")
	            	        .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
	            	        .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
	            	        .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
	            	        .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
	            	        .append("\n")
	            	        /* ── load model & forecast ─────────────────────────────────────── */
	            	        .append("    with open(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/arima_model_fit.pickle', 'rb') as f:\n")
	            	        .append("        model = pickle.load(f)\n")
	            	        .append("    prediction = model.forecast(steps=n_out)\n")
	            	        .append("\n")
	            	        /* ── TXT output ───────────────────────────────────────────────── */
	            	        .append("    formatted = \", \".join(f\"{p:.2f}\" for p in prediction)\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction (next {n_out}): [{formatted}]\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV output ──────────────────────────────────────────────── */
	            	        .append("    cols = [f'ARIMA_t+{i+1}' for i in range(n_out)]\n")
	            	        .append("    df = pd.DataFrame([prediction], columns=cols)\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode='a',\n")
	            	        .append("              header=not os.path.exists(output_csv_path),\n")
	            	        .append("              index=False)\n")
	            	        .append("\n");
	            	}


	                // SARIMA
	            	if (dataAnalyticsModelAlgorithm instanceof SARIMA) {

	            	    // 1 – Where the Python script should deposit its results
	            	    String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & forecast ──────────────────────────────────────── */
	            	        .append("    with open(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/sarima_model_fit.pickle', 'rb') as f:\n")
	            	        .append("        model = pickle.load(f)\n")
	            	        .append("    prediction = model.forecast(steps = n_out)\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = \", \".join(f\"{p:.2f}\" for p in prediction)\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction (next {n_out}): [{formatted}]\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    cols = [f'SARIMA_t+{i + 1}' for i in range(n_out)]\n")
	            	        .append("    df = pd.DataFrame([prediction], columns = cols)\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	                // Holt-Winters Exponential Smoothing
	            	else if (dataAnalyticsModelAlgorithm instanceof HWES) {

	            	    // 1 – Where the Python script should deposit its results
	            	    String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & forecast ──────────────────────────────────────── */
	            	        .append("    with open(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/hwes_model_fit.pickle', 'rb') as f:\n")
	            	        .append("        model = pickle.load(f)\n")
	            	        .append("    prediction = model.forecast(steps = n_out)\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = \", \".join(f\"{p:.2f}\" for p in prediction)\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction (next {n_out}): [{formatted}]\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    cols = [f'HWES_t+{i + 1}' for i in range(n_out)]\n")
	            	        .append("    df = pd.DataFrame([prediction], columns = cols)\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	                // ETS
	            	else if (dataAnalyticsModelAlgorithm instanceof ETS) {

	            	    // 1 – Where the Python script should deposit its results
	            		String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & forecast ──────────────────────────────────────── */
	            	        .append("    with open(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/ets_model_fit.pickle', 'rb') as f:\n")
	            	        .append("        model = pickle.load(f)\n")
	            	        .append("    prediction = model.forecast(steps = n_out)\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = \", \".join(f\"{p:.2f}\" for p in prediction)\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction (next {n_out}): [{formatted}]\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    cols = [f'ETS_t+{i + 1}' for i in range(n_out)]\n")
	            	        .append("    df = pd.DataFrame([prediction], columns = cols)\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	                // State Space Model
	            	else if (dataAnalyticsModelAlgorithm instanceof StateSpaceModel) {

	            	    // 1 – Where the Python script should deposit its results
	            		String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & forecast ──────────────────────────────────────── */
	            	        .append("    with open(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/statespace_model_fit.pickle', 'rb') as f:\n")
	            	        .append("        model = pickle.load(f)\n")
	            	        .append("    prediction = model.forecast(steps = n_out)\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = \", \".join(f\"{p:.2f}\" for p in prediction)\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction (next {n_out}): [{formatted}]\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    cols = [f'StateSpace_t+{i + 1}' for i in range(n_out)]\n")
	            	        .append("    df = pd.DataFrame([prediction], columns = cols)\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	            }

	          else if (dataAnalyticsModelAlgorithm instanceof machine_learning_ModelAlgorithm) {
	            	
	          
	                // SVR
	            	 if (dataAnalyticsModelAlgorithm instanceof SVR) {

	            	    // 1 – Where the Python script should deposit its results
	            		String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & predict ───────────────────────────────────────── */
	            	        .append("    model = joblib.load(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/svr_model_fit.pickle')\n")
	            	        .append("    prediction = model.predict([feature_values_for_prediction])\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = str(prediction[0])\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction: {formatted}\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    df = pd.DataFrame([prediction], columns = ['SVR_pred'])\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	                // RFR
	            	else if (dataAnalyticsModelAlgorithm instanceof RFR) {

	            	    // 1 – Where the Python script should deposit its results
	            		String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & predict ───────────────────────────────────────── */
	            	        .append("    model = joblib.load(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/rfr_model_fit.pickle')\n")
	            	        .append("    prediction = model.predict([feature_values_for_prediction])\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = str(prediction[0])\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 RFR Prediction: {formatted}\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    df = pd.DataFrame([prediction], columns = ['RFR_pred'])\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	                // GBM
	            	else if (dataAnalyticsModelAlgorithm instanceof GBM) {

	            	    // 1 – Where the Python script should deposit its results
	            		String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & predict ───────────────────────────────────────── */
	            	        .append("    model = joblib.load(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/gbm_model.pkl')\n")
	            	        .append("    prediction = model.predict([feature_values_for_prediction])\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = str(prediction[0])\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction: {formatted}\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    df = pd.DataFrame([prediction], columns = ['GBM_pred'])\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	                // XGBoost
	            	else if (dataAnalyticsModelAlgorithm instanceof XGBoost) {

	            	    // 1 – Where the Python script should deposit its results
	            	   String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & predict ───────────────────────────────────────── */
	            	        .append("    model = xgb.Booster()\n")
	            	        .append("    model.load_model(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/xgboost_model_fit.pickle')\n")
	            	        .append("    dmatrix = xgb.DMatrix([feature_values_for_prediction])\n")
	            	        .append("    prediction = model.predict(dmatrix)\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = str(prediction[0])\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction: {formatted}\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    df = pd.DataFrame([prediction], columns = ['XGBoost_pred'])\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	            } 	
	                // ARIMA‑GARCH
	            	else if (dataAnalyticsModelAlgorithm instanceof ARIMA_GARCH) {

	            	    // 1 – Where the Python script should deposit its results
	            		String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (4-space indent on every line)
	            	    pythonScriptStringBuilder
	            	    /* ── imports & constants ───────────────────────────────────────── */
            	            .append("    import os, pickle\n")
            	            .append("    import pandas as pd\n")
            	            .append("\n")
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
            	            .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
            	            .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
            	            .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok=True)\n")
            	            .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok=True)\n")
            	            .append("\n")
	            	        /* ── load model & forecast ──────────────────────────────────────── */
	            	        .append("    with open(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/arima_garch_model.pickle', 'rb') as f:\n")
	            	        .append("        model = pickle.load(f)\n")
	            	        .append("    prediction = model.forecast(steps = n_out)\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = \", \".join(f\"{p:.2f}\" for p in prediction)\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction (next {n_out}): [{formatted}]\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    cols = [f'ARIMA_GARCH_t+{i + 1}' for i in range(n_out)]\n")
	            	        .append("    df = pd.DataFrame([prediction], columns = cols)\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}

	                // Prophet
	            	else if (dataAnalyticsModelAlgorithm instanceof Prophet) {

	            	    // 1 – Where the Python script should deposit its results
	            	    String outputTxtPath = path_str + "/python-scripts/outputs/prediction.txt";
	            	    String outputCsvPath = path_str + "/python-scripts/outputs/prediction.csv";
	            	    long nSteps = (action.getDataAnalytics().getSteps() != null)
	                    	    ? action.getDataAnalytics().getSteps().getIntValue() : 1;

	            	    // 2 – Build the Python script (every line indented four spaces)
	            	    pythonScriptStringBuilder
	            	        /* ── imports & constants ────────────────────────────────────────── */
	            	        .append("    import os, sys, pickle\n")
	            	        .append("    import pandas as pd\n")
	            	        .append("    try:\n")
	            	        .append("        from prophet import Prophet            # v1.x and up\n")
	            	        .append("    except ImportError:\n")
	            	        .append("        from fbprophet import Prophet         # legacy name\n")
	            	        .append("\n")
	            	        
	            	       
            	            .append("    n_out = ").append(nSteps).append("\n")      // ← constant
            	            .append("\n")
	            	        .append("    output_txt_path = r'").append(outputTxtPath.replace("\\", "/")).append("'\n")
	            	        .append("    output_csv_path = r'").append(outputCsvPath.replace("\\", "/")).append("'\n")
	            	        .append("    os.makedirs(os.path.dirname(output_txt_path), exist_ok = True)\n")
	            	        .append("    os.makedirs(os.path.dirname(output_csv_path), exist_ok = True)\n")
	            	        .append("\n")
	            	        /* ── load model & forecast ──────────────────────────────────────── */
	            	        .append("    with open(r'").append(path_str.replace("\\", "/"))
	            	        .append("/python-scripts/pickles/prophet_model.pkl', 'rb') as f:\n")
	            	        .append("        model = pickle.load(f)\n")
	            	        .append("    future = model.make_future_dataframe(periods = n_out)\n")
	            	        .append("    forecast = model.predict(future)\n")
	            	        .append("    prediction = forecast['yhat'].tail(n_out).values\n")
	            	        .append("\n")
	            	        /* ── human-readable TXT output ──────────────────────────────────── */
	            	        .append("    formatted = \", \".join(f\"{p:.2f}\" for p in prediction)\n")
	            	        .append("    with open(output_txt_path, 'a') as f:\n")
	            	        .append("        f.write(f\"🔹 Prediction (next {n_out}): [{formatted}]\\n\")\n")
	            	        .append("\n")
	            	        /* ── CSV log (append-friendly) ──────────────────────────────────── */
	            	        .append("    cols = [f'Prophet_t+{i + 1}' for i in range(n_out)]\n")
	            	        .append("    df = pd.DataFrame([prediction], columns = cols)\n")
	            	        .append("    df.to_csv(output_csv_path,\n")
	            	        .append("              mode   = 'a',\n")
	            	        .append("              header = not os.path.exists(output_csv_path),\n")
	            	        .append("              index  = False)\n")
	            	        .append("\n");
	            	}}

	           

	         else {
	            System.err.println("ERROR: Labels must be ON for supervised time series prediction models.\n");
	        }
	   
	        
	        File pythonScriptsDir = new File(path_str + "/python-scripts");
	        if (!pythonScriptsDir.exists()) {
	            pythonScriptsDir.mkdirs();
	        }
	        File scriptFile = new File(path_str + "/python-scripts/predict.py");
	        try {
	            Files.deleteIfExists(Paths.get(scriptFile.toURI()));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try (FileWriter fr = new FileWriter(scriptFile, true);
	             BufferedWriter br = new BufferedWriter(fr)) {
	            br.append(pythonScriptStringBuilder);
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        }
	    }

	  }
	    
	    
	    
	    
	    
	    
	    
	            // Continue appending more models and algorithms here...
	    
	    

	       

	
	

	// ML2: da_pre_trained_predict
	@Override
	public void generate(DAPreTrainedPredictAction action, StringBuilder builder, Context ctx) {

		// Check for the blackbox ML (i.e., the hybrid/mixed MDSE/Non-MDSE) mode:
		boolean blackbox_ml = false;
		if (action.getDataAnalytics().getBlackbox_ml() != null) {
			if (action.getDataAnalytics().getBlackbox_ml().isBoolValue()) {
				blackbox_ml = true;
			}
		}
		if (!blackbox_ml) {
			System.err.println(
					"[ERROR] The da_pre_trained_predict action in the statechart may only be used in the blackbox-ml mode. Either disable blackbox-ml in the data analytics or use the da_predict action instead!\n");
			return;
		}

		Path path = null;
		path = Paths.get(new File(ctx.getOutputDirectory().getAbsolutePath().toString()).toURI());

		String dalib = "auto";
		if (AnnotatedElementHelper.hasAnnotation(action.getDataAnalytics(), "dalib")) {
			if (action.getDataAnalytics().getAnnotations().get(0).getValue().equals("scikit-learn")
					|| action.getDataAnalytics().getAnnotations().get(0).getValue().equals("keras-tensorflow")
					|| action.getDataAnalytics().getAnnotations().get(0).getValue().equals("pytorch")) {
				dalib = action.getDataAnalytics().getAnnotations().get(0).getValue();
			} else if (action.getDataAnalytics().getAnnotations().get(0).getValue().equals("weka")) {
				System.err.println(
						"ERROR: This compiler/code generator generates Java AND Python code. The data analytics / machine learning part should be generated in Python. However, weka is chosen as the library for data analytics / machine learning in the annotations of the model. Please either change the annotation @dalib to a Python library, e.g., scikit-learn or use the pure Java compiler/code generator!");
			} else {
				dalib = "auto";
			}
		}

		EList<Property> features = action.getFeatures();
		List<String> feature_types = new ArrayList<String>();
		for (int i = 0; i < features.size(); i++) {
			if (action.getFeatures().get(i).getTypeRef().isIsArray()) {
				feature_types.add(action.getFeatures().get(i).getTypeRef().getType().getName() + "[]");
			} else {
				feature_types.add(action.getFeatures().get(i).getTypeRef().getType().getName());
			}

		}
		EList<Property> feature_values_for_prediction = action.getFeatures();

//		builder.append("\n//********* ML2 *********\n");
		generatePythonDAPreTrainedPredictScript(path.toString() + "/src", action);

		// Make the generated Python script executable
		builder.append("List<String> list0 = new ArrayList<String>();\n");
		builder.append("list0.add(\"chmod\");\n");
		builder.append("list0.add(\"u+x\");\n");
		builder.append("list0.add(\"" + path.toString() + "/src/python-scripts/pre_trained_predict.py\");\n");
		builder.append("try{\n");
		builder.append("	ProcessBuilder pb0 = new ProcessBuilder(list0);\n");
		builder.append("	Process p0 = pb0.start();\n");
		builder.append("} catch(Exception e){System.out.println(e);}\n");

		builder.append("List<String> list = new ArrayList<String>();\n");
		builder.append("list.add(\"/home/mlpluspy/appservers/apache-tomcat-10.1.26/webapps/ROOT/scripts/venv/bin/python\");\n");
		builder.append("list.add(\"" + path.toString() + "/src/python-scripts/pre_trained_predict.py\");\n");

		String features_str = "";
		String feature_types_str = "";
		for (int i = 0; i < features.size(); i++) {
			features_str += features.get(i).getName();
			feature_types_str += feature_types.get(i);
			if (i < features.size() - 1) {
				features_str += ",";
				feature_types_str += ",";
			}
		}
		builder.append("list.add(\"" + features_str + "\");\n");
		builder.append("list.add(\"" + feature_types_str + "\");\n");

		builder.append("list.add(\"");
		String feature_values_for_prediction_str = "";
		for (int i = 0; i < feature_values_for_prediction.size(); i++) {
			if (feature_values_for_prediction.get(i).getTypeRef().isIsArray()) {
				builder.append("\"\'\" + Arrays.toString(" + ctx.getVariableName(feature_values_for_prediction.get(i))
						+ ").replaceAll(\",\",\"\") + \"\'\"");
			} else {
				builder.append(ctx.getVariableName(feature_values_for_prediction.get(i)).toString());
			}

			if (i < feature_values_for_prediction.size() - 1) {
				builder.append(",");
			}
		}
		builder.append("\");\n");

		builder.append("StringBuilder output_string_builder = new StringBuilder();\n");
		builder.append("try{\n");
		builder.append("	ProcessBuilder pb = new ProcessBuilder(list);\n");
		builder.append("	Process p = pb.start();\n");
		builder.append("	p.waitFor();\n");
		builder.append(
				"	BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));\n");
		builder.append("	String s = null;\n");
		builder.append("	while ((s = stdInput.readLine()) != null) {\n");
		builder.append("		output_string_builder.append(s);\n");
		builder.append("	}\n");
		builder.append("} catch(Exception e){System.out.println(e);}\n");

		if (!(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType() instanceof Enumeration)) {
			if (AnnotatedElementHelper.hasAnnotation(
					action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(), "java_type")) {
				if (!action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().isIsArray()) {

					if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("boolean")) {
						// boolean
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Boolean.parseBoolean(" + "(output_string_builder.toString());\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("char")) {
						// char
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= " + "output_string_builder.toString();\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("String")) {
						// String
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= " + "output_string_builder.toString();\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("double")) {
						// double
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Double.valueOf(" + "output_string_builder.toString());\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("byte")) {
						// byte
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Byte.valueOf((int) Math.round(Double.valueOf("
										+ "output_string_builder.toString())));\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("short")) {
						// short
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Short.valueOf((int) Math.round(Double.valueOf("
										+ "output_string_builder.toString())));\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("int")) {
						// int
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Integer.valueOf((int) Math.round(Double.valueOf("
										+ "output_string_builder.toString())));\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("long")) {
						// long
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Long.valueOf((int) Math.round(Double.valueOf("
										+ "output_string_builder.toString())));\n");

					}

				} else {
					builder.append(
							"String[] arrString = output_string_builder.toString().substring(1,output_string_builder.toString().length()-1).split(\" \");\n");
					builder.append(AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.toArray()[0] + "[] ");
					builder.append("arr = new ");
					builder.append(AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.toArray()[0]);
					builder.append("[arrString.length];\n");
					builder.append("for(int i=0; i<arrString.length; i++) {\n");
					builder.append(
							"	arr[i] = "
									+ ctx.firstToUpper(AnnotatedElementHelper
											.annotation(action.getDataAnalytics().getPredictionResults().get(0)
													.getTypeRef().getType(), "java_type")
											.toArray()[0].toString())
									+ ".parse"
									+ ctx.firstToUpper(AnnotatedElementHelper.annotation(action.getDataAnalytics()
											.getPredictionResults().get(0).getTypeRef().getType(), "java_type")
											.toArray()[0].toString())
									+ "(arrString[i]);\n");
					builder.append("}\n");
					builder.append(
							ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
									+ "=arr;\n");
				}
			} else {
				if (!action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().isIsArray()) {
					// TODO
					builder.append("(Object) (output_string_builder.toString());\n");
				} else {
					// TODO
					builder.append("(Object[]) (output_string_builder.toString());\n");
				}
			}
		}

//		builder.append("//********* ML2 *********\n\n");
	}

	// ML2: Code generator for the Python script regarding prediction using a
	// pre-trained ML model, pre_trained_predict.py
	private void generatePythonDAPreTrainedPredictScript(String path_str, DAPreTrainedPredictAction action) {

		String dalib = "auto";
		if (AnnotatedElementHelper.hasAnnotation(action.getDataAnalytics(), "dalib")) {
			if (action.getDataAnalytics().getAnnotations().get(0).getValue().equals("scikit-learn")
					|| action.getDataAnalytics().getAnnotations().get(0).getValue().equals("keras-tensorflow")
					|| action.getDataAnalytics().getAnnotations().get(0).getValue().equals("pytorch")) {
				dalib = action.getDataAnalytics().getAnnotations().get(0).getValue();
			} else if (action.getDataAnalytics().getAnnotations().get(0).getValue().equals("weka")) {
				System.err.println(
						"ERROR: This compiler/code generator generates Java AND Python code. The data analytics / machine learning part should be generated in Python. However, weka is chosen as the library for data analytics / machine learning in the annotations of the model. Please either change the annotation @dalib to a Python library, e.g., scikit-learn or use the pure Java compiler/code generator!");
			} else {
				dalib = "auto";
			}
		}

		StringBuilder pythonScriptStringBuilder = new StringBuilder();
//		pythonScriptStringBuilder.append("#********* ML2 *********\n\n");
		pythonScriptStringBuilder.append("import sys\n");
		pythonScriptStringBuilder.append("import pandas as pd\n");
		pythonScriptStringBuilder.append("import numpy as np\n");
		pythonScriptStringBuilder.append("import pickle\n\n");

		pythonScriptStringBuilder.append("features = sys.argv[1].split(',')\n");
		pythonScriptStringBuilder.append("feature_types = sys.argv[2].split(',')\n");
		pythonScriptStringBuilder.append("feature_values_for_prediction = sys.argv[3].split(',')\n\n");

		pythonScriptStringBuilder.append(action.getDataAnalytics().getBlackbox_import_algorithm() + "\n\n");

		pythonScriptStringBuilder.append("with open('" + path_str + File.separator
				+ action.getDataAnalytics().getBlackbox_ml_model() + "', 'rb') as pickle_file:\n");
		pythonScriptStringBuilder.append("	model = pickle.load(pickle_file)\n\n");

		pythonScriptStringBuilder.append(
				"array_features_indexes = list(filter(lambda x: '[' in feature_types[x], range(len(feature_types))))\n");
		pythonScriptStringBuilder.append("new_feature_values_for_prediction = []\n");
		pythonScriptStringBuilder.append("for index in array_features_indexes:\n");
		pythonScriptStringBuilder.append("	for item in feature_values_for_prediction[index][2:-2].split(' '):\n");
		pythonScriptStringBuilder.append("		new_feature_values_for_prediction.append(item)\n");
		pythonScriptStringBuilder.append("	feature_values_for_prediction.pop(index)\n");
		pythonScriptStringBuilder.append("	feature_values_for_prediction.append(new_feature_values_for_prediction)\n");
		pythonScriptStringBuilder.append("	feature_name = features[index]\n");
		pythonScriptStringBuilder.append("	features.pop(index)\n");
		pythonScriptStringBuilder.append("	i=index\n");
		pythonScriptStringBuilder.append("	for item in range(len(new_feature_values_for_prediction)):\n");
		pythonScriptStringBuilder.append("		features.insert(i,feature_name+'_'+str(item))\n");
		pythonScriptStringBuilder.append("		i=i+1\n");
		pythonScriptStringBuilder.append("if(len(array_features_indexes)!=0):\n");
		pythonScriptStringBuilder.append("		feature_values_for_prediction = feature_values_for_prediction[0]\n\n");

		pythonScriptStringBuilder.append("col_names = []\n");
		pythonScriptStringBuilder.append("num_col_names = []\n");
		pythonScriptStringBuilder.append("cat_col_names = []\n");
		pythonScriptStringBuilder.append("if(timestamps.lower() == 'on'):\n");
		pythonScriptStringBuilder.append("    col_names.append('timestamp')\n");
		pythonScriptStringBuilder.append("for i in range(len(features)):\n");
		pythonScriptStringBuilder.append("    feature=features[i]\n");
		pythonScriptStringBuilder.append("    feature_type=feature_types[i]\n");
		pythonScriptStringBuilder.append("    if((\"String\" in feature_type) or (\"Char\" in feature_type)):\n");
		pythonScriptStringBuilder.append("        cat_col_names.append(feature)\n");
		pythonScriptStringBuilder.append(
				"    if((\"Int\" in feature_type) or (\"Long\" in feature_type) or (\"Double\" in feature_type)):\n");
		pythonScriptStringBuilder.append("        num_col_names.append(feature)\n");
		pythonScriptStringBuilder.append("    col_names.append(feature)\n\n");

		pythonScriptStringBuilder.append("if(len(cat_col_names)!=0):\n");
		pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
		pythonScriptStringBuilder.append("	with open('" + path_str
				+ action.getDataAnalytics().getBlackbox_label_encoder() + "', 'rb') as pickle_file:\n");
		pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");

		pythonScriptStringBuilder.append("df = pd.DataFrame(data={}, columns=[])\n");
		pythonScriptStringBuilder.append("for i in range(len(feature_values_for_prediction)):\n");
		pythonScriptStringBuilder.append("	if features[i] in cat_col_names:\n");
		pythonScriptStringBuilder.append(
				"		df.insert(i,features[i], pd.Series(le.transform([feature_values_for_prediction[i]])))\n");
		pythonScriptStringBuilder.append("	else:\n");
		pythonScriptStringBuilder
				.append("		df.insert(i,features[i], pd.Series(feature_values_for_prediction[i]))\n\n");

		pythonScriptStringBuilder.append("print (model.predict(df).item(0))\n\n");

//		pythonScriptStringBuilder.append("#********* ML2 *********\n\n");
		File pythonScriptsDir = new File(path_str + "/python-scripts");
		if (!pythonScriptsDir.exists()) {
			pythonScriptsDir.mkdirs();
		}
		File scriptFile = new File(path_str + "/python-scripts/pre_trained_predict.py");
		try {
			Files.deleteIfExists(Paths.get(scriptFile.toURI()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileWriter fr = null;
		BufferedWriter br = null;
		try {
			fr = new FileWriter(scriptFile, true);
			br = new BufferedWriter(fr);
			br.append(pythonScriptStringBuilder);
			br.close();
			fr.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

}
