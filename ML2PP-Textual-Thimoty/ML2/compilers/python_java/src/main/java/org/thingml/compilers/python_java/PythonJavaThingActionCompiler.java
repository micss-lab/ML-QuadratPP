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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Desktop.Action;
import java.io.BufferedWriter;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	// ML2: da_save
	@Override
	public void generate(DASaveAction action, StringBuilder builder, Context ctx) { // ML2
		// builder.append("\n//********* ML2 *********\n");
		builder.append("File dataDir = new File(\"data\");\n");
		builder.append("if(!dataDir.exists())\n");
		builder.append("dataDir.mkdirs();\n");
		builder.append("File datasetFile = new File(\"" + action.getDataAnalytics().getDataset() + "\");\n");
		builder.append("FileWriter fr = null;\n");
		builder.append("BufferedWriter br = null;\n");
		builder.append("try{\n");
		builder.append("	fr = new FileWriter(datasetFile, true);\n");
		builder.append("	br = new BufferedWriter(fr);\n");

		// Writing the timestamp to the CSV file
		String str0 = "";
		if (action.getDataAnalytics().getTimestamps().equals(Timestamps.ON)) {
			str0 = "LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm:ss\")).toString()";
			builder.append("	br.write(" + str0 + " + \",\");\n");
		}

		// Writing the attribute value of the feature to the CSV file
		String str1 = "";
		for (int i = 0; i < action.getDataAnalytics().getFeatures().size() - 1; i++) {
			if (!(action.getDataAnalytics().getFeatures().get(i).getTypeRef().getType() instanceof Enumeration)) {
				if (AnnotatedElementHelper.hasAnnotation(
						action.getDataAnalytics().getFeatures().get(i).getTypeRef().getType(), "java_type")) { // Java
																												// Type
					if (!action.getDataAnalytics().getFeatures().get(i).getTypeRef().isIsArray()) { // This feature is
																									// not an array
						str1 = "String.valueOf(" + ctx.getVariableName(action.getDataAnalytics().getFeatures().get(i))
								+ ")";
						builder.append("	br.write(" + str1 + ");\n");
					} else { // This feature is an array
						builder.append("	String array_var_val_str1 = \"[\";\n");
						builder.append("	boolean flag1 = false;\n");
						builder.append("	for(int i = 0; i < "
								+ ctx.getVariableName(action.getDataAnalytics().getFeatures().get(i))
								+ ".length; i++) {\n");
						builder.append("		if(!flag1) {\n");
						builder.append("			array_var_val_str1 = array_var_val_str1 + String.valueOf("
								+ ctx.getVariableName(action.getDataAnalytics().getFeatures().get(i)) + "[i]);\n");
						builder.append("			flag1 = true;\n");
						builder.append("		} else {\n");
						builder.append("			array_var_val_str1 = array_var_val_str1 + \" \" + String.valueOf("
								+ ctx.getVariableName(action.getDataAnalytics().getFeatures().get(i)) + "[i]);\n");
						builder.append("		}\n");
						builder.append("	}\n");
						builder.append("	array_var_val_str1 = array_var_val_str1 + \"]\";\n");
						builder.append("	br.write(" + "array_var_val_str1 + \",\");\n");
					}
				} else { // Not a Java Type
					System.err.println("ERROR: Property " + action.getDataAnalytics().getFeatures().get(i).getName()
							+ " has type "
							+ action.getDataAnalytics().getFeatures().get(i).getTypeRef().getType().getName()
							+ ", which is not registered as a Java type. Please add the proper annotation: @java_type... ");
				}
			}

			builder.append("	br.write(\",\");\n");

		}

		// Writing the prediction results to the CSV file
		String str2 = "";
		if (!(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType() instanceof Enumeration)) {
			if (AnnotatedElementHelper.hasAnnotation(
					action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(), "java_type")) { // Java
																													// type
				if (!action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().isIsArray()) { // We are not
																											// predicting
																											// an array
					str2 = "String.valueOf("
							+ ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)) + ")";
					builder.append("	br.write(" + str2 + "+ \"\\n\");\n");
				} else { // We are predicting an array
					builder.append("	String array_var_val_str2 = \"[\";\n");
					builder.append("	boolean flag2 = false;\n");
					builder.append("	for(int i = 0; i < "
							+ ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0))
							+ ".length; i++) {\n");
					builder.append("		if(!flag2) {\n");
					builder.append("			array_var_val_str2 = array_var_val_str2 + String.valueOf("
							+ ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)) + "[i]);\n");
					builder.append("			flag2 = true;\n");
					builder.append("		} else {\n");
					builder.append("			array_var_val_str2 = array_var_val_str2 + \" \" + String.valueOf("
							+ ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)) + "[i]);\n");
					builder.append("		}\n");
					builder.append("	}\n");
					builder.append("	array_var_val_str2 = array_var_val_str2 + \"]\";\n");
					// Write the prediction to the CSV file
					builder.append("	br.write(" + "array_var_val_str2 + \"\\n\");\n");
				}
			} else { // Not a Java Type
				System.err.println("ERROR: Property "
						+ action.getDataAnalytics().getPredictionResults().get(0).getName() + " has type "
						+ action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType().getName()
						+ ", which is not registered as a Java type. Please add the proper annotation: @java_type... ");
			}
		}

		builder.append("	br.close();\n");
		builder.append("	fr.close();\n");
		builder.append("}  catch (IOException e1) {\n");
		builder.append("	e1.printStackTrace();\n");
		builder.append("} finally {\n");
		builder.append("		try {\n");
		builder.append("			br.close();\n");
		builder.append("			fr.close();\n");
		builder.append("		} catch (IOException e2) {\n");
		builder.append("			e2.printStackTrace();\n");
		builder.append("		}\n");
		builder.append("}\n");
		// builder.append("//********* ML2 *********\n\n");
	}

	// ML2: da_preprocess
	@Override
	public void generate(DAPreprocessAction action, StringBuilder builder, Context ctx) {
		Path path = null;
		path = Paths.get(new File(ctx.getOutputDirectory().getAbsolutePath().toString()).toURI());
    Path pythonPlotsPath = path.resolve("src/python-scripts/plots");
    File pythonPlotsDirectory = pythonPlotsPath.toFile();
    pythonPlotsDirectory.mkdirs();
    
		String dataset = path.toString() + "tmp_dataset.csv";
		if (action.getDataAnalytics().getDataset() != null) {
			dataset = action.getDataAnalytics().getDataset();
		} else {
			System.err.println(
					"WARNING: No dataset specified in the data analytics section! Using tmp_dataset.csv as the default...\n");
		}

		// AutoML is by default off
		if (action.getDataAnalytics().getAutoML().getValue() == AutoML.NOT_SET_VALUE) {
			System.err.println(
					"WARNING: Parameter automl not specified in the data analytics section. Thus, the default value off is assumed...\n");
			action.getDataAnalytics().setAutoML(AutoML.OFF);
		}

		String sequential = "";
		if (action.getDataAnalytics().getSequential().getValue() == Sequential.NOT_SET_VALUE) {
			if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
				if (action.getDataAnalytics().getTimestamps().getValue() == Timestamps.ON_VALUE) {
					System.err.println(
							"WARNING: Parameter sequential not specified in the data analytics section! Since AutoML is on, and the timestamps parameter is also set to on, the boolean value TRUE will be assumed for parameter sequential...\n");
					action.getDataAnalytics().setSequential(Sequential.TRUE);
				} else if (action.getDataAnalytics().getTimestamps().getValue() == Timestamps.OFF_VALUE) {
					System.err.println(
							"WARNING: Parameter sequential not specified in the data analytics section! Since AutoML is on, and the timestamps parameter is set to off, the boolean value FALSE will be assumed for parameter sequential...\n");
					action.getDataAnalytics().setSequential(Sequential.FALSE);
				} else {
					System.err.println(
							"WARNING: Parameter sequential not specified in the data analytics section! Using FALSE as the default value...\n");
					action.getDataAnalytics().setSequential(Sequential.FALSE);
				}
			} else {
				System.err.println(
						"WARNING: Parameter sequential not specified in the data analytics section! Using FALSE as the default value...\n");
				action.getDataAnalytics().setSequential(Sequential.FALSE);
			}
		}
		sequential = action.getDataAnalytics().getSequential().getName();

		String timestamps = "";
		if (action.getDataAnalytics().getTimestamps().getValue() == Timestamps.NOT_SET_VALUE) {
			if (action.getDataAnalytics().getAutoML().getValue() == AutoML.ON_VALUE) {
				if (action.getDataAnalytics().getSequential().getValue() == Sequential.TRUE_VALUE) {
					System.err.println(
							"WARNING: Parameter timestamps not specified in the data analytics section! Since AutoML is on, and the sequential parameter is set to TRUE, the value ON will be assumed for parameter timestamps...\n");
					action.getDataAnalytics().setTimestamps(Timestamps.ON);
				} else if (action.getDataAnalytics().getSequential().getValue() == Sequential.FALSE_VALUE) {
					System.err.println(
							"WARNING: Parameter timestamps not specified in the data analytics section! Since AutoML is on, and the sequential parameter is set to FALSE, the value OFF will be assumed for parameter timestamps...\n");
					action.getDataAnalytics().setTimestamps(Timestamps.OFF);
				} else {
					System.err.println(
							"WARNING: Parameter timestamps not specified in the data analytics section! Using OFF as the default value...\n");
					action.getDataAnalytics().setTimestamps(Timestamps.OFF);
				}
			} else {
				System.err.println(
						"WARNING: Parameter timestamps not specified in the data analytics section! Using OFF as the default value...\n");
				action.getDataAnalytics().setTimestamps(Timestamps.OFF);
			}
		}
		timestamps = action.getDataAnalytics().getTimestamps().getName();

		String labels = action.getDataAnalytics().getLabels().getName();

		EList<Property> features = action.getDataAnalytics().getFeatures();
		List<String> feature_types = new ArrayList<String>();
		for (int i = 0; i < features.size(); i++) {
			if (action.getDataAnalytics().getFeatures().get(i).getTypeRef().isIsArray()) {
				feature_types
						.add(action.getDataAnalytics().getFeatures().get(i).getTypeRef().getType().getName() + "[]");
			} else {
				feature_types.add(action.getDataAnalytics().getFeatures().get(i).getTypeRef().getType().getName());
			}

		}

		// builder.append("\n//********* ML2 *********\n");
		generatePythonDAPreprocessingScript(path.toString() + "/src", action);

		builder.append("File dataDir = new File(\"data\");\n");
		builder.append("if(!dataDir.exists()) {\n");
		builder.append("	dataDir.mkdirs();\n");
		builder.append("}\n");
		builder.append("File picklesDir = new File(\"" + path.toString() + "/src/python-scripts/pickles/" + "\");\n");
		builder.append("if(!picklesDir.exists()) {\n");
		builder.append("	picklesDir.mkdirs();\n");
		builder.append("}\n");

		// Make the generated Python script executable
		builder.append("List<String> list0 = new ArrayList<String>();\n");
		builder.append("list0.add(\"chmod\");\n");
		builder.append("list0.add(\"u+x\");\n");
		builder.append("list0.add(\"" + path.toString() + "/src/python-scripts/preprocess.py\");\n");
		builder.append("try{\n");
		builder.append("	ProcessBuilder pb0 = new ProcessBuilder(list0);\n");
		builder.append("	Process p0 = pb0.start();\n");
		builder.append("} catch(Exception e){System.out.println(e);}\n");

		builder.append("List<String> list = new ArrayList<String>();\n");
		builder.append("list.add(\"python\");\n");
		builder.append("list.add(\"" + path.toString() + "/src/python-scripts/preprocess.py\");\n");
		builder.append("list.add(\"" + dataset + "\");\n");
		builder.append("list.add(\"" + sequential + "\");\n");
		builder.append("list.add(\"" + timestamps + "\");\n");
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
		builder.append("list.add(\"" + labels + "\");\n");
		builder.append("try{\n");
		builder.append("	ProcessBuilder pb = new ProcessBuilder(list);\n");
		builder.append("	Process p = pb.start();\n");
		builder.append("	p.waitFor();\n");
		builder.append("} catch(Exception e){System.out.println(e);}\n\n");
		// builder.append("//********* ML2 *********\n\n");
	}

	// ML2: Code generator for the data analytics preprocessing Python script,
	// preprocess.py
	private void generatePythonDAPreprocessingScript(String path_str, DAPreprocessAction action) {
		// Note: Regardless of the chosen library via da_lib, the preprocessing is
		// always carried out using the Scikit-Learn library to make it efficient for
		// large datasets.

		StringBuilder pythonScriptStringBuilder = new StringBuilder();
		// pythonScriptStringBuilder.append("#********* ML2 *********\n\n");
		pythonScriptStringBuilder.append("import sys\n");
		pythonScriptStringBuilder.append("import pickle\n");
		pythonScriptStringBuilder.append("import time, datetime\n");
		pythonScriptStringBuilder.append("import pandas as pd\n");
		pythonScriptStringBuilder.append("import numpy as np\n");
		pythonScriptStringBuilder.append("from sklearn.preprocessing import LabelEncoder\n\n");

		pythonScriptStringBuilder.append("dataset = sys.argv[1]\n");
		pythonScriptStringBuilder.append("sequential = sys.argv[2]\n");
		pythonScriptStringBuilder.append("timestamps = sys.argv[3]\n");
		pythonScriptStringBuilder.append("features = sys.argv[4].split(',')\n");
		pythonScriptStringBuilder.append("feature_types = sys.argv[5].split(',')\n");
		pythonScriptStringBuilder.append("labels = sys.argv[6]\n\n");
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
		pythonScriptStringBuilder
				.append("df = pd.read_csv(dataset, lineterminator='\\n', names=col_names, header=None)\n");
		pythonScriptStringBuilder.append("original_df = df.copy(deep=True)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_original_df.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(original_df, pickle_file)\n\n");
		pythonScriptStringBuilder.append("if(timestamps.lower() == 'on'):\n");
		pythonScriptStringBuilder.append("	timeformat = \"%d-%m-%Y %H:%M:%S\"\n");
		pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_timeformat.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("		pickle.dump(timeformat, pickle_file)\n");
		pythonScriptStringBuilder.append(
				"	df.timestamp = df.timestamp.apply(lambda x: datetime.datetime.strptime(x, timeformat))\n\n");

		// Handling the categorical values: one-hot encoding
		pythonScriptStringBuilder.append("if(len(cat_col_names)!=0):\n");
		pythonScriptStringBuilder.append("	le = LabelEncoder()\n");
		pythonScriptStringBuilder.append("	le.fit(df[cat_col_names])\n");
		pythonScriptStringBuilder.append("	df[cat_col_names] = df[cat_col_names].apply(lambda x: le.transform(x))\n");
		pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_label_encoder.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("		pickle.dump(le, pickle_file)\n\n");

		// Handling the numerical values: possible standardization (Note:
		// Standardization is always per feature, i.e., column-wise)
		if (action.getDataAnalytics().getPreprocess_feature_scaler() == Preprocess_feature_scaler.NOT_SET) {
			if (action.getDataAnalytics().getAutoML() == AutoML.ON) {
				System.err.println(
						"WARNING: Parameter preprocess_feature_scaler is not specified in the data analytics section! Since AutoML is on, preprocess_feature_scaler is automatically set to the Standrad Scaler...\n");
				action.getDataAnalytics().setPreprocess_feature_scaler(Preprocess_feature_scaler.STANDARD_SCALER);
			} else {
				System.err.println(
						"WARNING: Parameter preprocess_feature_scaler is not specified in the data analytics section! Since AutoML is off, preprocess_feature_scaler is also set to off (i.e., disabled)...\n");
				action.getDataAnalytics().setPreprocess_feature_scaler(Preprocess_feature_scaler.OFF);
			}
		}
		if (action.getDataAnalytics().getPreprocess_feature_scaler() != null
				&& action.getDataAnalytics().getPreprocess_feature_scaler() != Preprocess_feature_scaler.OFF) {

			// StandardScaler, see
			// https://scikit-learn.org/stable/modules/generated/sklearn.preprocessing.StandardScaler.html
			if (action.getDataAnalytics().getPreprocess_feature_scaler() == Preprocess_feature_scaler.STANDARD_SCALER) {
				pythonScriptStringBuilder.append("from sklearn.preprocessing import StandardScaler\n");
				pythonScriptStringBuilder.append("scaler = StandardScaler()\n");
				pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
				pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
			}

			// MinMaxScaler, see
			// https://scikit-learn.org/stable/modules/generated/sklearn.preprocessing.MinMaxScaler.html#sklearn.preprocessing.MinMaxScaler
			if (action.getDataAnalytics().getPreprocess_feature_scaler() == Preprocess_feature_scaler.MIN_MAX_SCALER) {
				pythonScriptStringBuilder.append("from sklearn.preprocessing import MinMaxScaler\n");
				pythonScriptStringBuilder.append("scaler = MinMaxScaler()\n");
				pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
				pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
			}

			// RobustScaler, see
			// https://scikit-learn.org/stable/modules/generated/sklearn.preprocessing.RobustScaler.html#sklearn.preprocessing.RobustScaler
			if (action.getDataAnalytics().getPreprocess_feature_scaler() == Preprocess_feature_scaler.ROBUST_SCALER) {
				pythonScriptStringBuilder.append("from sklearn.preprocessing import RobustScaler\n");
				pythonScriptStringBuilder.append("scaler = RobustScaler()\n");
				pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
				pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
			}
		}

		// Handling the numerical values: possible normalization (Note: Normalization is
		// always per sample, i.e., row-wise)
		if (action.getDataAnalytics().getPreprocess_sample_normalizer() != null
				&& action.getDataAnalytics().getPreprocess_sample_normalizer() != Preprocess_sample_normalizer.NOT_SET
				&& action.getDataAnalytics().getPreprocess_sample_normalizer() != Preprocess_sample_normalizer.OFF) {

			// NORMALIZER_L2_NORM, see
			// https://scikit-learn.org/stable/modules/generated/sklearn.preprocessing.Normalizer.html#sklearn.preprocessing.Normalizer
			if (action.getDataAnalytics()
					.getPreprocess_sample_normalizer() == Preprocess_sample_normalizer.NORMALIZER_L2_NORM) {
				pythonScriptStringBuilder.append("from sklearn.preprocessing import Normalizer\n");
				pythonScriptStringBuilder.append("scaler = Normalizer(norm=\'l2\')\n");
				pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
				pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
			}

			// NORMALIZER_L1_NORM, see
			// https://scikit-learn.org/stable/modules/generated/sklearn.preprocessing.Normalizer.html#sklearn.preprocessing.Normalizer
			if (action.getDataAnalytics()
					.getPreprocess_sample_normalizer() == Preprocess_sample_normalizer.NORMALIZER_L1_NORM) {
				pythonScriptStringBuilder.append("from sklearn.preprocessing import Normalizer\n");
				pythonScriptStringBuilder.append("scaler = Normalizer(norm=\'l1\')\n");
				pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
				pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
			}

			// NORMALIZER_MAX_NORM, see
			// https://scikit-learn.org/stable/modules/generated/sklearn.preprocessing.Normalizer.html#sklearn.preprocessing.Normalizer
			if (action.getDataAnalytics()
					.getPreprocess_sample_normalizer() == Preprocess_sample_normalizer.NORMALIZER_MAX_NORM) {
				pythonScriptStringBuilder.append("from sklearn.preprocessing import Normalizer\n");
				pythonScriptStringBuilder.append("scaler = Normalizer(norm=\'max\')\n");
				pythonScriptStringBuilder.append("scaler.fit(df[num_col_names])\n");
				pythonScriptStringBuilder.append("df[num_col_names] = scaler.transform(df[num_col_names])\n\n");
			}
		}

		pythonScriptStringBuilder.append("if(labels.lower() == 'on'):\n");
		pythonScriptStringBuilder.append("	if(timestamps.lower() == 'on'):\n");
		pythonScriptStringBuilder.append("		X_train =  df.loc[:,col_names[1:-1]]\n");
		pythonScriptStringBuilder.append("	else:\n");
		pythonScriptStringBuilder.append("		X_train =  df.loc[:,col_names[:-1]]\n");
		pythonScriptStringBuilder.append("	y_train = df[features[-1]]\n");
		pythonScriptStringBuilder.append("else:\n");
		pythonScriptStringBuilder.append("	if(timestamps.lower() == 'on'):\n");
		pythonScriptStringBuilder.append("		X_train =  df.loc[:,col_names[1:]]\n");
		pythonScriptStringBuilder.append("	else:\n");
		pythonScriptStringBuilder.append("		X_train =  df.loc[:,col_names[:]]\n");
		pythonScriptStringBuilder.append("	y_train = []\n\n");
		// Handling features, which have the array type
		pythonScriptStringBuilder.append(
				"array_features_indexes = list(filter(lambda x: '[' in feature_types[x], range(len(feature_types))))\n");
		pythonScriptStringBuilder.append("for index in array_features_indexes[:-1]:\n");
		pythonScriptStringBuilder.append("	col = '' + features[array_features_indexes[index]]\n");
		pythonScriptStringBuilder.append("	tmp_df_1 = X_train.loc[:, X_train.columns != col]\n");
		pythonScriptStringBuilder.append(
				"	tmp_df_2 = pd.DataFrame(X_train[col].apply(lambda x: x[1:-1]).str.split(' ', expand=True).values).rename(columns=lambda x : col + '_' + str(x))\n");

		pythonScriptStringBuilder.append("	X_train = pd.concat([tmp_df_1,tmp_df_2],axis=1)\n\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_X_train.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("	pickle.dump(X_train, pickle_file)\n");

		if (action.getDataAnalytics().getLabels() == Labels.ON
				|| action.getDataAnalytics().getLabels() == Labels.SEMI) {
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "preprocess_y_train.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("	pickle.dump(y_train, pickle_file)\n");
			
			pythonScriptStringBuilder.append("from sklearn.model_selection import train_test_split \n");
			
			pythonScriptStringBuilder.append("x_train, x_test, y_train, y_test = train_test_split(X_train, y_train) \n");
			
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_X_train.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("	pickle.dump(x_train, pickle_file)\n");
			
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_X_test.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("	pickle.dump(x_test, pickle_file)\n");
			
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_y_train.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("	pickle.dump(y_train, pickle_file)\n");
			
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_y_test.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("	pickle.dump(y_test, pickle_file)\n");
			
			
		} else {
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_X_train.pickle', 'wb') as pickle_file:\n");
			pythonScriptStringBuilder.append("	pickle.dump(X_train, pickle_file)\n");
		}
    generatePreProcessPlots(pythonScriptStringBuilder,path_str,action.getDataAnalytics());


		// pythonScriptStringBuilder.append("#********* ML2 *********\n\n");
		File pythonScriptsDir = new File(path_str + "/python-scripts");
		if (!pythonScriptsDir.exists()) {
			pythonScriptsDir.mkdirs();
		}
		File scriptFile = new File(path_str + "/python-scripts/preprocess.py");
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

	// ML2: da_train
	@Override
	public void generate(DATrainAction action, StringBuilder builder, Context ctx) {
		Path path = null;
		path = Paths.get(new File(ctx.getOutputDirectory().getAbsolutePath().toString()).toURI());
		// builder.append("\n//********* ML2 *********\n");
		generatePythonDATrainScript(path.toString() + "/src", action);

		builder.append("File X_train_pickle = new File(\"" + path.toString()+ "/src/python-scripts/pickles/preprocess_X_train.pickle" + "\");\n");
		builder.append("File y_train_pickle = new File(\"" + path.toString()+ "/src/python-scripts/pickles/preprocess_y_train.pickle" + "\");\n");
		builder.append("if(!X_train_pickle.exists())\n");
		builder.append("	return;\n");

		if (action.getDataAnalytics().getLabels() == Labels.ON
				|| action.getDataAnalytics().getLabels() == Labels.SEMI) {
			builder.append("if(!X_train_pickle.exists() || !y_train_pickle.exists())\n");
			builder.append("	return;\n");
		}
		// Make the generated Python script executable
		builder.append("List<String> list0 = new ArrayList<String>();\n");
		builder.append("list0.add(\"chmod\");\n");
		builder.append("list0.add(\"u+x\");\n");
		builder.append("list0.add(\"" + path.toString() + "/src/python-scripts/train.py\");\n");
		builder.append("try{\n");
		builder.append("	ProcessBuilder pb0 = new ProcessBuilder(list0);\n");
		builder.append("	Process p0 = pb0.start();\n");
		builder.append("} catch(Exception e){System.out.println(e);}\n");

		builder.append("List<String> list = new ArrayList<String>();\n");
		builder.append("list.add(\"python\");\n");
		builder.append("list.add(\"" + path.toString() + "/src/python-scripts/train.py\");\n");
		builder.append("try{\n");
		builder.append("	ProcessBuilder pb = new ProcessBuilder(list);\n");
		builder.append("	Process p = pb.start();\n");
		builder.append("	p.waitFor();\n");
		builder.append("} catch(Exception e){System.out.println(e);}\n\n");

		// Save the timestamp of training in the training_results text file
		builder.append("File dataDir = new File(\"data\");\n");
		builder.append("if(!dataDir.exists())\n");
		builder.append("dataDir.mkdirs();\n");
		builder.append(
				"File training_results_file = new File(\"" + action.getDataAnalytics().getTrainingResults() + "\");\n");
		builder.append("FileWriter fr = null;\n");
		builder.append("BufferedWriter br = null;\n");
		builder.append("try{\n");
		builder.append("	fr = new FileWriter(training_results_file, true);\n");
		builder.append("	br = new BufferedWriter(fr);\n");
		String str0 = "LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm:ss\")).toString()";
		String str1 = action.getDataAnalytics().getModelAlgorithm().getName();
		builder.append("	br.write(" + "\"INFO: \" + " + str0 + "+ \", \" + \"" + str1
				+ "\" + \", training done\" + \"\\n\");\n");
		builder.append("	br.close();\n");
		builder.append("	fr.close();\n");
		builder.append("}  catch (IOException e1) {\n");
		builder.append("	e1.printStackTrace();\n");
		builder.append("} finally {\n");
		builder.append("		try {\n");
		builder.append("			br.close();\n");
		builder.append("			fr.close();\n");
		builder.append("		} catch (IOException e2) {\n");
		builder.append("			e2.printStackTrace();\n");
		builder.append("		}\n");
		builder.append("}\n");
		// builder.append("//********* ML2 *********\n\n");
	}

	// ML2: Code generator for the data analytics training Python script, train.py
	private void generatePythonDATrainScript(String path_str, DATrainAction action) {
		DataAnalyticsModelAlgorithm dataAnalyticsModelAlgorithm = action.getDataAnalytics().getModelAlgorithm();

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
		// pythonScriptStringBuilder.append("#********* ML2 *********\n\n");
		pythonScriptStringBuilder.append("import pickle\n\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_X_train.pickle', 'rb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    X_train = pickle.load(pickle_file)\n");

		if (action.getDataAnalytics().getLabels() == Labels.ON
				|| action.getDataAnalytics().getLabels() == Labels.SEMI) {
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "preprocess_y_train.pickle', 'rb') as pickle_file:\n");
			pythonScriptStringBuilder.append("    y_train = pickle.load(pickle_file)\n\n");
	
	    pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
	    + "preprocess_X_test.pickle', 'rb') as pickle_file:\n");
	    pythonScriptStringBuilder.append("    X_test = pickle.load(pickle_file)\n\n");
	    
	    pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
	    + "preprocess_y_test.pickle', 'rb') as pickle_file:\n");
	    pythonScriptStringBuilder.append("    y_test = pickle.load(pickle_file)\n\n");
			
		}

		if (action.getDataAnalytics().getLabels() == Labels.ON) {
			// Supervised ML

			if (dataAnalyticsModelAlgorithm instanceof LinearClassifierLogisticRegression
					|| dataAnalyticsModelAlgorithm instanceof LinearRegression) { // Linear Model
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					// Linear Regression
					if (dataAnalyticsModelAlgorithm instanceof LinearClassifierLogisticRegression) {
						System.err.println(
								"ERROR: The prediction data type is numeric. Therefore, please use the linear regression model for regression, instead of the logistic regression classifier.\n");
					}
					generatePythonDATrainScriptLinearRegression(dalib, path_str, action, pythonScriptStringBuilder,
							false);
				} else {
					// Linear Classification through Logistic Regression
					if (dataAnalyticsModelAlgorithm instanceof LinearRegression) {
						System.err.println(
								"ERROR: The prediction data type is categorical. Therefore, please use the logistic regression classifier, instead of the linear regression model.\n");
					}
					generatePythonDATrainScriptLinearClassifierLogisticRegression(dalib, path_str, action,
							pythonScriptStringBuilder, false);
				}
			} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesGaussian) { // Gaussian Naive Bayes (NB)
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
				} else { // Classification
					generatePythonDATrainScriptNaiveBayesGaussian(dalib, path_str, action, pythonScriptStringBuilder,
							false);
				}

			} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesMultinomial) { // Multinomial NB
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
				} else { // Classification
					generatePythonDATrainScriptNaiveBayesMultinomial(dalib, path_str, action, pythonScriptStringBuilder,
							false);
				}

			} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesComplement) { // Complement NB
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
				} else { // Classification
					generatePythonDATrainScriptNaiveBayesComplement(dalib, path_str, action, pythonScriptStringBuilder,
							false);
				}

			} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesBernoulli) { // Bernoulli NB
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
				} else { // Classification
					generatePythonDATrainScriptNaiveBayesBernoulli(dalib, path_str, action, pythonScriptStringBuilder,
							false);
				}

			} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesCategorical) { // Categorical NB
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
				} else { // Classification
					generatePythonDATrainScriptNaiveBayesCategorical(dalib, path_str, action, pythonScriptStringBuilder,
							false);
				}

			} else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeRegressor) { // Decision Tree (DT) Regression
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					generatePythonDATrainScriptDecisionTreeRegressor(dalib, path_str, action, pythonScriptStringBuilder,
							false);
				} else {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a classification ML algorithm, but a regression ML algorithm. However, it is used with a categorical type for the prediction results.\n");
				}
			} else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeClassifier) { // Decision Tree (DT)
																						// Classification
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
				} else {
					generatePythonDATrainScriptDecisionTreeClassifier(dalib, path_str, action,
							pythonScriptStringBuilder, false);
				}
			} else if (dataAnalyticsModelAlgorithm instanceof RandomForestRegressor) { // Random Forest (RF) Regression
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					generatePythonDATrainScriptRandomForestRegressor(dalib, path_str, action, pythonScriptStringBuilder,
							false);
				} else {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a classification ML algorithm, but a regression ML algorithm. However, it is used with a categorical type for the prediction results.\n");
				}
			} else if (dataAnalyticsModelAlgorithm instanceof RandomForestClassifier) { // Random Forest (RF)
																						// Classification
				if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
					System.err.println("ERROR: " + dataAnalyticsModelAlgorithm.getName()
							+ " is a not a regression ML algorithm, but a classification ML algorithm. However, it is used with a numerical type for the prediction results.\n");
				} else {
					generatePythonDATrainScriptRandomForestClassifier(dalib, path_str, action,
							pythonScriptStringBuilder, false);
				}
			} else if (dataAnalyticsModelAlgorithm instanceof NN_MultilayerPerceptron) { // NN Multi-Layer Perceptron
																							// (MLP)
				generatePythonDATrainScriptNN_MultilayerPerceptron(dalib, path_str, action, pythonScriptStringBuilder,
						false);
			} else if (action.getDataAnalytics().getLabels() == Labels.OFF) {
				// Unsupervised ML

				if (dataAnalyticsModelAlgorithm instanceof KMeans) { // KMeans Clustering
					generatePythonDATrainScriptKMeans(dalib, path_str, action, pythonScriptStringBuilder);
				} else if (dataAnalyticsModelAlgorithm instanceof MiniBatchKMeans) { // Mini-Batch K-Means Clustering
					generatePythonDATrainScriptMiniBatchKMeans(dalib, path_str, action, pythonScriptStringBuilder);
				} else if (dataAnalyticsModelAlgorithm instanceof DBSCAN) { // DBSCAN Clustering
					generatePythonDATrainScriptDBSCAN(dalib, path_str, action, pythonScriptStringBuilder);
				} else if (dataAnalyticsModelAlgorithm instanceof SpectralClustering) { // Spectral Clustering
					generatePythonDATrainScriptSpectralClustering(dalib, path_str, action, pythonScriptStringBuilder);
				} else if (dataAnalyticsModelAlgorithm instanceof GaussianMixture) { // Gaussian Mixture Clustering
					generatePythonDATrainScriptGaussianMixture(dalib, path_str, action, pythonScriptStringBuilder);
				}
			} else if (action.getDataAnalytics().getLabels() == Labels.SEMI) {
				// Semi-supervised ML

				if (dataAnalyticsModelAlgorithm instanceof SelfTrainingClassifier) { // Self Training Classifier for
																						// Semi-supervised ML
					generatePythonDATrainScriptSelfTrainingClassifier(dalib, path_str, action,
							pythonScriptStringBuilder);
				} else if (dataAnalyticsModelAlgorithm instanceof LabelPropagation) { // Semi-supervised ML through
																						// Label Propagation
					generatePythonDATrainScriptLabelPropagation(dalib, path_str, action, pythonScriptStringBuilder);
				} else if (dataAnalyticsModelAlgorithm instanceof LabelSpreading) { // Semi-supervised ML through Label
																					// Spreading
					generatePythonDATrainScriptLabelSpreading(dalib, path_str, action, pythonScriptStringBuilder);
				}
			} else {
				System.err.println("ERROR: "
						+ "In the data analytics section of the model, labels must be specified: ON (for supervised ML, i.e., labeled data), OFF (for unsupervised ML, i.e., unlabeled data) or SEMI (for semi-supervised ML, i.e., partially labeled data).\n");
			}
      generateTrainPlotsMetrics(pythonScriptStringBuilder,path_str,action.getDataAnalytics());
      generateDAReport(pythonScriptStringBuilder,path_str,action.getDataAnalytics());
			// pythonScriptStringBuilder.append("#********* ML2 *********\n\n");
			File pythonScriptsDir = new File(path_str + "/python-scripts");
			if (!pythonScriptsDir.exists()) {
				pythonScriptsDir.mkdirs();
			}
			File scriptFile = new File(path_str + "/python-scripts/train.py");
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
		} else {
			System.err.println("ERROR: " + action.getDataAnalytics().getModelAlgorithm().getName()
					+ " is currently not a supported algorithm for supervised ML (classification).\n");
		}
	}

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
				params += ",";
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
		pythonScriptStringBuilder.append("model = KMeans(" + params + ")\n");
		pythonScriptStringBuilder.append("model = model.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_k_means_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
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
		pythonScriptStringBuilder.append("model = MiniBatchKMeans(" + params + ")\n");
		pythonScriptStringBuilder
				.append("model = model.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_mini_batch_k_means_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
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
		pythonScriptStringBuilder.append("model = DBSCAN(" + params + ")\n");
		pythonScriptStringBuilder.append("model = dbscan_clustering.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_dbscan_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");

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
		pythonScriptStringBuilder.append("model = SpectralClustering(" + params + ")\n");
		pythonScriptStringBuilder.append("model = spectral_clustering.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_spectral_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");
	}

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
		pythonScriptStringBuilder.append("model = GaussianMixture(" + params + ")\n");
		pythonScriptStringBuilder.append("model = gaussian_mixture_clustering.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_gaussian_mixture_clustering.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");

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
			pythonScriptStringBuilder.append("model = SelfTrainingClassifier(model, " + params + ")\n");
		} else {
			pythonScriptStringBuilder.append("model = SelfTrainingClassifier(model)\n");
		}
		pythonScriptStringBuilder.append("model = self_training_model.fit(X_train)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "train_model_self_training.pickle', 'wb') as pickle_file:\n");
		pythonScriptStringBuilder.append("    pickle.dump(model, pickle_file)\n\n");

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

	// ML2: da_predict
	@Override
	public void generate(DAPredictAction action, StringBuilder builder, Context ctx) {

		// Check for the blackbox ML (i.e., the hybrid/mixed MDSE/Non-MDSE) mode:
		boolean blackbox_ml = false;
		if (action.getDataAnalytics().getBlackbox_ml() != null) {
			if (action.getDataAnalytics().getBlackbox_ml().isBoolValue()) {
				blackbox_ml = true;
			}
		}
		if (blackbox_ml) {
			System.err.println(
					"ERROR: The blackbox-ml mode of the data analytics may not be used with any da_predict action in the statechart. Either use the da_pre_trained_predict action instead, or disable the blackbox ML mode.\n");
			return;
		}

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

		Path path = null;
		path = Paths.get(new File(ctx.getOutputDirectory().getAbsolutePath().toString()).toURI());
//		builder.append("\n//********* ML2 *********\n");
		builder.append("String prediction_timestamp = \"\";\n");
		if (action.getDataAnalytics().getTimestamps().equals(Timestamps.ON)) {
			builder.append("File preprocess_timeformat_pickle = new File(\"" + path.toString()
					+ "/src/python-scripts/pickles/preprocess_timeformat.pickle" + "\");\n");
			builder.append("if(!preprocess_timeformat_pickle.exists())\n");
			builder.append("	return;\n");
			builder.append(
					"prediction_timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm:ss\")).toString();\n");
		}
		String timestamps = action.getDataAnalytics().getTimestamps().getName();
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

		DataAnalyticsModelAlgorithm dataAnalyticsModelAlgorithm = action.getDataAnalytics().getModelAlgorithm();
		generatePythonDAPredictScript(path.toString() + "/src", action);

		builder.append("File preprocess_original_df_pickle = new File(\"" + path.toString()
				+ "/src/python-scripts/pickles/preprocess_original_df.pickle" + "\");\n");
		builder.append("if(!preprocess_original_df_pickle.exists())\n");
		builder.append("	return;\n");

		if (dataAnalyticsModelAlgorithm instanceof ML2_ModelAlgorithm) {
			if (action.getDataAnalytics().getLabels() == Labels.ON) {
				// Supervised ML

				if (dataAnalyticsModelAlgorithm instanceof LinearRegression) { // Linear Regression
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						builder.append("File train_model_lin_reg_pickle = new File(\"" + path.toString()
								+ "/src/python-scripts/pickles/train_model_lin_reg.pickle" + "\");\n");
						builder.append("if(!train_model_lin_reg_pickle.exists())\n");
						builder.append("	return;\n");
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
						builder.append("	return;\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesGaussian) { // Gaussian Naive Bayes (NB)
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println(
								"ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
					} else {
						builder.append("File train_model_gnb_pickle = new File(\"" + path.toString()
								+ "/src/python-scripts/pickles/train_model_gnb.pickle" + "\");\n");
						builder.append("if(!train_model_gnb_pickle.exists())\n");
						builder.append("	return;\n");
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
						builder.append("	return;\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesComplement) { // Complement Naive Bayes (NB)
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println(
								"ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
					} else {
						builder.append("File train_model_cnb_pickle = new File(\"" + path.toString()
								+ "/src/python-scripts/pickles/train_model_cnb.pickle" + "\");\n");
						builder.append("if(!train_model_cnb_pickle.exists())\n");
						builder.append("	return;\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof NaiveBayesBernoulli) { // Bernoulli Naive Bayes (NB)
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						System.err.println(
								"ERROR: The prediction data type is numerical. Therefore, please use a regression model, instead of a classifier.\n");
					} else {
						builder.append("File train_model_bnb_pickle = new File(\"" + path.toString()
								+ "/src/python-scripts/pickles/train_model_bnb.pickle" + "\");\n");
						builder.append("if(!train_model_bnb_pickle.exists())\n");
						builder.append("	return;\n");
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
						builder.append("	return;\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof DecisionTreeRegressor) {// Decision Tree (DT)
																							// Regression
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						builder.append("File train_model_dtr_pickle = new File(\"" + path.toString()
								+ "/src/python-scripts/pickles/train_model_dtr.pickle" + "\");\n");
						builder.append("if(!train_model_dtr_pickle.exists())\n");
						builder.append("	return;\n");
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
						builder.append("	return;\n");
					}
				} else if (dataAnalyticsModelAlgorithm instanceof RandomForestRegressor) { // Random Forest (RF)
																							// Regression
					if (TyperHelper.isNumeric(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef())) {
						builder.append("File train_model_rfr_pickle = new File(\"" + path.toString()
								+ "/src/python-scripts/pickles/train_model_rfr.pickle" + "\");\n");
						builder.append("if(!train_model_rfr_pickle.exists())\n");
						builder.append("	return;\n");
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
						builder.append("	return;\n");
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
							builder.append("	return;\n");
						} else {
							// NN Multi-Layer Perceptron (MLP) Classification
							builder.append("File train_model_nn_mlp_c_pickle = new File(\"" + path.toString()
									+ "/src/python-scripts/pickles/train_model_nn_mlp_c.pickle" + "\");\n");
							builder.append("if(!train_model_nn_mlp_c_pickle.exists())\n");
							builder.append("	return;\n");
						}
					} else if (dalib.equals("auto") || dalib.equals("keras-tensorflow")) {
						builder.append("File train_model_nn_mlp_weights = new File(\"" + path.toString()
								+ "/src/python-scripts/pickles/train_model_nn_mlp_weights.h5" + "\");\n");
						builder.append("if(!train_model_nn_mlp_weights.exists())\n");
						builder.append("	return;\n");
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
					builder.append("	return;\n");

				} else if (dataAnalyticsModelAlgorithm instanceof MiniBatchKMeans) { // Mini-Batch KMeans Clustering
					builder.append("File train_model_mini_batch_k_means_clustering = new File(\"" + path.toString()
							+ "/src/python-scripts/pickles/train_model_mini_batch_k_means_clustering.pickle"
							+ "\");\n");
					builder.append("if(!train_model_mini_batch_k_means_clustering.exists())\n");
					builder.append("	return;\n");
				} else if (dataAnalyticsModelAlgorithm instanceof DBSCAN) { // DBSCAN Clustering
					builder.append("File train_model_dbscan_clustering = new File(\"" + path.toString()
							+ "/src/python-scripts/pickles/train_model_dbscan_clustering.pickle" + "\");\n");
					builder.append("if(!train_model_dbscan_clustering.exists())\n");
					builder.append("	return;\n");
				} else if (dataAnalyticsModelAlgorithm instanceof SpectralClustering) { // Spectral Clustering
					builder.append("File train_model_spectral_clustering = new File(\"" + path.toString()
							+ "/src/python-scripts/pickles/train_model_spectral_clustering.pickle" + "\");\n");
					builder.append("if(!train_model_spectral_clustering.exists())\n");
					builder.append("	return;\n");
				} else if (dataAnalyticsModelAlgorithm instanceof GaussianMixture) { // Gaussian Mixture Clustering
					builder.append("File train_model_gaussian_mixture_clustering = new File(\"" + path.toString()
							+ "/src/python-scripts/pickles/train_model_gaussian_mixture_clustering.pickle" + "\");\n");
					builder.append("if(!train_model_gaussian_mixture_clustering.exists())\n");
					builder.append("	return;\n");
				}

			} else if (action.getDataAnalytics().getLabels() == Labels.SEMI) {
				// Semi-supervised ML

				if (dataAnalyticsModelAlgorithm instanceof SelfTrainingClassifier) { // Self Training Classifier for
																						// Semi-supervised ML
					builder.append("File train_model_self_training = new File(\"" + path.toString()
							+ "/src/python-scripts/pickles/train_model_self_training.pickle" + "\");\n");
					builder.append("if(!train_model_self_training.exists())\n");
					builder.append("	return;\n");
				} else if (dataAnalyticsModelAlgorithm instanceof LabelPropagation) { // Label Propagation
					builder.append("File train_model_label_propagation = new File(\"" + path.toString()
							+ "/src/python-scripts/pickles/train_model_label_propagation.pickle" + "\");\n");
					builder.append("if(!train_model_label_propagation.exists())\n");
					builder.append("	return;\n");
				} else if (dataAnalyticsModelAlgorithm instanceof LabelSpreading) { // Label Spreading
					builder.append("File train_model_label_spreading = new File(\"" + path.toString()
							+ "/src/python-scripts/pickles/train_model_label_spreading.pickle" + "\");\n");
					builder.append("if(!train_model_label_spreading.exists())\n");
					builder.append("	return;\n");
				}

			} else {
				// Nothing
			}
		}

		// Make the generated Python script executable
		builder.append("List<String> list0 = new ArrayList<String>();\n");
		builder.append("list0.add(\"chmod\");\n");
		builder.append("list0.add(\"u+x\");\n");
		builder.append("list0.add(\"" + path.toString() + "/src/python-scripts/predict.py\");\n");
		builder.append("try{\n");
		builder.append("	ProcessBuilder pb0 = new ProcessBuilder(list0);\n");
		builder.append("	Process p0 = pb0.start();\n");
		builder.append("} catch(Exception e){System.out.println(e);}\n");

		builder.append("List<String> list = new ArrayList<String>();\n");
		builder.append("list.add(\"python\");\n");
		builder.append("list.add(\"" + path.toString() + "/src/python-scripts/predict.py\");\n");

		builder.append("list.add(\"" + timestamps + "\");\n");
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
				builder.append("\" + String.valueOf(" + ctx.getVariableName(feature_values_for_prediction.get(i)) + ") + \"");
			}

			if (i < feature_values_for_prediction.size() - 1) {
				builder.append(",");
			}
		}
		builder.append("\");\n");

		builder.append("list.add(\"\'\" + prediction_timestamp + \"\'\");\n");

		builder.append("StringBuilder output_string_builder = new StringBuilder();\n");
		builder.append("List<String> pythonOutput = new ArrayList<>();\n");

		builder.append("try{\n");
		builder.append("	ProcessBuilder pb = new ProcessBuilder(list);\n");
		builder.append("	Process p = pb.start();\n");
		builder.append("	p.waitFor();\n");
		builder.append(
				"	BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));\n");
		builder.append("	String s = null;\n");
		builder.append("	while ((s = stdInput.readLine()) != null) {\n");
		builder.append("		output_string_builder.append(s);\n");
		builder.append("		pythonOutput.add(s.trim());\n");
		builder.append("	}\n");
		builder.append("} catch(Exception e){System.out.println(e);}\n");

    for (int i = 0; i < action.getDataAnalytics().getPredictionResults().size(); i++) {
      builder.append("if(!pythonOutput.isEmpty() && " + i +"< pythonOutput.size()){\n");
	    if (!(action.getDataAnalytics().getPredictionResults().get(i).getTypeRef().getType() instanceof Enumeration)) {

	        if (AnnotatedElementHelper.hasAnnotation(
	                action.getDataAnalytics().getPredictionResults().get(i).getTypeRef().getType(), "java_type")) {

	            if (!action.getDataAnalytics().getPredictionResults().get(i).getTypeRef().isIsArray()) {
	                List<String> da_predict_annotations = AnnotatedElementHelper.annotation(action.getDataAnalytics().getPredictionResults().get(i).getTypeRef().getType(),"java_type");
	                String annotationHelperOutput = da_predict_annotations.get(da_predict_annotations.size()-1);

	                if (annotationHelperOutput.equals("boolean")) {
	                    // boolean

	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(i)).toString()
	                                    + "= Boolean.parseBoolean(" + "pythonOutput.get(" + i + ").toString());\n");

	                } else if (annotationHelperOutput.equals("char")) {
	                    // char

	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(i)).toString()
	                                    + "= " + "pythonOutput.get(" + i + ").toString();\n");

	                } else if (annotationHelperOutput.equals("String")) {
	                    // String

	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(i)).toString()
	                                    + "= " + "pythonOutput.get(" + i + ").toString();\n");

	                } else if (annotationHelperOutput.equals("double")) {
	                    // double

	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(i)).toString()
	                                    + "= Double.valueOf(" + "pythonOutput.get(" + i + ").toString());\n");

	                } else if (annotationHelperOutput.equals("byte")) {
	                    // byte

	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(i)).toString()
	                                    + "= Byte.valueOf((int) Math.round(Double.valueOf("
	                                    + "pythonOutput.get(" + i + ").toString())));\n");

	                } else if (annotationHelperOutput.equals("short")) {
	                    // short

	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(i)).toString()
	                                    + "= Short.valueOf((int) Math.round(Double.valueOf("
	                                    + "pythonOutput.get(" + i + ").toString())));\n");

	                } else if (annotationHelperOutput.equals("int")) {
	                    // int

	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(i)).toString()
	                                    + "= Integer.valueOf((int) Math.round(Double.valueOf("
	                                    + "pythonOutput.get(" + i + ").toString())));\n");

	                } else if (annotationHelperOutput.equals("long")) {
	                    // long

	                    builder.append(
	                            ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(i)).toString()
	                                    + "= Long.valueOf((int) Math.round(Double.valueOf("
	                                    + "pythonOutput.get(" + i + ").toString())));\n");

	                }

	            } else {

	                builder.append(
	                        "String[] arrString = pythonOutput.get(" + i + ").toString().substring(1,pythonOutput.get(" + i + ").toString().length()-1).split(\" \");\n");
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
	                builder.append("(Object) (pythonOutput[i].toString());\n");
	            } else {
	                // TODO
	                builder.append("(Object[]) (pythonOutput[i].toString());\n");
	            }
	        }
	    }
      builder.append("}\n");
    }
//		builder.append("//********* ML2 *********\n\n");
	}

	// ML2: Code generator for the data analytics predicting Python script,
	// predict.py
	private void generatePythonDAPredictScript(String path_str, DAPredictAction action) {
		DataAnalyticsModelAlgorithm dataAnalyticsModelAlgorithm = action.getDataAnalytics().getModelAlgorithm();

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
		pythonScriptStringBuilder.append("import time, datetime\n");
		pythonScriptStringBuilder.append("import pandas as pd\n");
		pythonScriptStringBuilder.append("import numpy as np\n");
		pythonScriptStringBuilder.append("import pickle\n\n");

		pythonScriptStringBuilder.append("timestamps = sys.argv[1]\n");
		pythonScriptStringBuilder.append("features = sys.argv[2].split(',')\n");
		pythonScriptStringBuilder.append("feature_types = sys.argv[3].split(',')\n");
		pythonScriptStringBuilder.append("feature_values_for_prediction = sys.argv[4].split(',')\n");
		pythonScriptStringBuilder.append("timestamp_for_prediction = sys.argv[5]\n\n");

		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_original_df.pickle', 'rb') as pickle_file:\n");
		pythonScriptStringBuilder.append("	original_df = pickle.load(pickle_file)\n");
		pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_X_train.pickle', 'rb') as pickle_file:\n");
		pythonScriptStringBuilder.append("	X_train = pickle.load(pickle_file)\n");

		if (action.getDataAnalytics().getLabels() == Labels.ON
				|| action.getDataAnalytics().getLabels() == Labels.SEMI) {
			pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"
					+ "preprocess_y_train.pickle', 'rb') as pickle_file:\n");
			pythonScriptStringBuilder.append("	y_train = pickle.load(pickle_file)\n\n");
		}

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
		pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"
				+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
		pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");

		pythonScriptStringBuilder.append("flag = False\n");
		pythonScriptStringBuilder.append("for i in range(len(features)):\n");
		pythonScriptStringBuilder.append("	if features[i] in cat_col_names:\n");
		pythonScriptStringBuilder.append(
				"		if not np.isin([feature_values_for_prediction[i]],original_df[features[i]]).item(0):\n");
		pythonScriptStringBuilder.append("			flag = True\n");
		pythonScriptStringBuilder.append("			break\n\n");

		pythonScriptStringBuilder.append("if(flag):\n");
		pythonScriptStringBuilder.append("	print (False)\n");
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
					    pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
	            pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	            pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
	            pythonScriptStringBuilder.append("	pred = le.inverse_transform(model.predict(df))\n");
	            pythonScriptStringBuilder.append("	for prediction in pred:\n");
	            pythonScriptStringBuilder.append("		print(str(prediction))\n\n");

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
					    pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
	            pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	            pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
	            pythonScriptStringBuilder.append("	pred = le.inverse_transform(model.predict(df))\n");
	            pythonScriptStringBuilder.append("	for prediction in pred:\n");
	            pythonScriptStringBuilder.append("		print(str(prediction))\n\n");

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
					    pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
	            pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	            pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
	            pythonScriptStringBuilder.append("	pred = le.inverse_transform(model.predict(df))\n");
	            pythonScriptStringBuilder.append("	for prediction in pred:\n");
	            pythonScriptStringBuilder.append("		print(str(prediction))\n\n");

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
					    pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
	            pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	            pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
	            pythonScriptStringBuilder.append("	pred = le.inverse_transform(model.predict(df))\n");
	            pythonScriptStringBuilder.append("	for prediction in pred:\n");
	            pythonScriptStringBuilder.append("		print(str(prediction))\n\n");

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
					    pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
	            pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	            pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
	            pythonScriptStringBuilder.append("	pred = le.inverse_transform(model.predict(df))\n");
	            pythonScriptStringBuilder.append("	for prediction in pred:\n");
	            pythonScriptStringBuilder.append("		print(str(prediction))\n\n");
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
					    pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
	            pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	            pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
	            pythonScriptStringBuilder.append("	pred = le.inverse_transform(model.predict(df))\n");
	            pythonScriptStringBuilder.append("	for prediction in pred:\n");
	            pythonScriptStringBuilder.append("		print(str(prediction))\n\n");

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
					    pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
	            pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	            pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
	            pythonScriptStringBuilder.append("	pred = le.inverse_transform(model.predict(df))\n");
	            pythonScriptStringBuilder.append("	for prediction in pred:\n");
	            pythonScriptStringBuilder.append("		print(str(prediction))\n\n");

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
					    pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
	            pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	            pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
	            pythonScriptStringBuilder.append("	pred = le.inverse_transform(model.predict(df))\n");
	            pythonScriptStringBuilder.append("	for prediction in pred:\n");
	            pythonScriptStringBuilder.append("		print(str(prediction))\n\n");

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
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");

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
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");

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
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");

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
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");

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
						pythonScriptStringBuilder.append("	print (model.predict(df).item(0))\n\n");

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
		}

//		pythonScriptStringBuilder.append("#********* ML2 *********\n\n");
		File pythonScriptsDir = new File(path_str + "/python-scripts");
		if (!pythonScriptsDir.exists()) {
			pythonScriptsDir.mkdirs();
		}
		File scriptFile = new File(path_str + "/python-scripts/predict.py");
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
		builder.append("list.add(\"python\");\n");
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
										+ "= Boolean.parseBoolean(" + "output_string_builder.toString());\n");

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
										+ "= Byte.valueOf((int) Math.round(Double.valueOf("+ "output_string_builder.toString())));\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("short")) {
						// short
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Short.valueOf((int) Math.round(Double.valueOf("+ "output_string_builder.toString())));\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("int")) {
						// int
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Integer.valueOf((int) Math.round(Double.valueOf("+ "output_string_builder.toString())));\n");

					} else if (AnnotatedElementHelper
							.annotation(action.getDataAnalytics().getPredictionResults().get(0).getTypeRef().getType(),
									"java_type")
							.equals("long")) {
						// long
						builder.append(
								ctx.getVariableName(action.getDataAnalytics().getPredictionResults().get(0)).toString()
										+ "= Long.valueOf((int) Math.round(Double.valueOf("+ "output_string_builder.toString())));\n");

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
  
  static void generatePreProcessPlots(StringBuilder pythonScriptStringBuilder,String path_str, DataAnalytics da) {
      pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");

	    pythonScriptStringBuilder.append("plots_path = '" + path_str + "/python-scripts/plots/'\n");
    	pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_original_df.pickle', 'rb') as pickle_file:\n");
	    pythonScriptStringBuilder.append("    original_df = pickle.load(pickle_file)\n\n");

      for(Plots plot : da.getPlots()){      
        switch (plot){
            case HEAT_MAP:
            	pythonScriptStringBuilder.append("from seaborn import heatmap\n");
	            pythonScriptStringBuilder.append("try:\n");
	            pythonScriptStringBuilder.append("    heatmap(original_df.select_dtypes(include=\"number\").corr())\n");
	            pythonScriptStringBuilder.append("    plt.savefig(f\"{plots_path}heatmap.png\")\n");
	            pythonScriptStringBuilder.append("    plt.clf()\n");
              pythonScriptStringBuilder.append("    print('Heatmap saved')\n");
	            pythonScriptStringBuilder.append("except:\n");
	            pythonScriptStringBuilder.append("    print(\"Failed to produce Heatmap\")\n\n");
	            break;
            case BOX_PLOT:
            	pythonScriptStringBuilder.append("from seaborn import boxplot\n");
	            pythonScriptStringBuilder.append("try:\n");
	            pythonScriptStringBuilder.append("    nums=original_df.select_dtypes(include=\"number\")\n");
	            pythonScriptStringBuilder.append("    for num in nums.columns:\n");
	            pythonScriptStringBuilder.append("        boxplot(x=nums[num],width=0.5,fliersize=5)\n");
	            pythonScriptStringBuilder.append("        plt.title(f\"Boxplot of {num}\")\n");
	            pythonScriptStringBuilder.append("        plt.savefig(f\"{plots_path}boxplot_{num}.png\")\n");
	            pythonScriptStringBuilder.append("        plt.clf()\n");
              pythonScriptStringBuilder.append("    print('Box plots saved')\n");
	            pythonScriptStringBuilder.append("except:\n");
	            pythonScriptStringBuilder.append("    print(\"Failed to produce Boxplot\")\n\n");
              break;
            case CLASS_IMBALANCE:
            	pythonScriptStringBuilder.append("try:\n");
            	pythonScriptStringBuilder.append("    for series_name, series in original_df.items():\n");
            	pythonScriptStringBuilder.append("        if series_name in cat_col_names:\n");
            	pythonScriptStringBuilder.append("            series.value_counts().plot(kind='bar', title=f\"Count {series_name}\")\n");
              pythonScriptStringBuilder.append("            plt.tight_layout()\n");
            	pythonScriptStringBuilder.append("            plt.savefig(f\"{plots_path}count_{series_name}.png\")\n");
            	pythonScriptStringBuilder.append("            plt.close()\n\n\n");
              pythonScriptStringBuilder.append("    print('Class Imbalance saved')\n");

            	pythonScriptStringBuilder.append("except:\n");
              pythonScriptStringBuilder.append("    print(f'Failed to produce Class Imbalance')\n");
              break;
            case PAIR_PLOT:
            	pythonScriptStringBuilder.append("import seaborn as sns\n");
            	pythonScriptStringBuilder.append("try:\n");
            	pythonScriptStringBuilder.append("    sns.pairplot(df[num_col_names])\n");
              pythonScriptStringBuilder.append("    plt.tight_layout()\n");
            	pythonScriptStringBuilder.append("    plt.savefig(f\"{plots_path}pair_plot.png\")\n");
            	pythonScriptStringBuilder.append("    plt.close()\n");
              pythonScriptStringBuilder.append("    print('Pair Plot saved')\n");
            	pythonScriptStringBuilder.append("except:\n");
              pythonScriptStringBuilder.append("    print(f'Failed to produce Pair Plot')\n");
              break;
            default: 
              break;
        }
      }
    }
  
  	static void generateTrainPlotsMetrics(StringBuilder pythonScriptStringBuilder,String path_str, DataAnalytics da) {
	    pythonScriptStringBuilder.append("import matplotlib.pyplot as plt\n");
  		pythonScriptStringBuilder.append("import numpy as np\n");
	    pythonScriptStringBuilder.append("plots_path = '" + path_str + "/python-scripts/plots/'\n");
    	pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_original_df.pickle', 'rb') as pickle_file:\n");
	pythonScriptStringBuilder.append("    original_df = pickle.load(pickle_file)\n\n");
	    pythonScriptStringBuilder.append("from sklearn.preprocessing import LabelEncoder\n");
	    pythonScriptStringBuilder.append("with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
	    pythonScriptStringBuilder.append("    le = pickle.load(pickle_file)\n\n");
	    if (da.getLabels() == Labels.ON
				|| da.getLabels() == Labels.SEMI) {
			    pythonScriptStringBuilder.append("y_pred = model.predict(X_test)\n");
			    
          for(Plots plot : da.getPlots()){      
            switch (plot){
                case CONFUSION_MATRIX:
                		pythonScriptStringBuilder.append("from sklearn.metrics import confusion_matrix, ConfusionMatrixDisplay\n");
		                pythonScriptStringBuilder.append("try:\n");
		                pythonScriptStringBuilder.append("    confusion_matrix = confusion_matrix(y_test, y_pred)\n");
		                pythonScriptStringBuilder.append("    cm_display = ConfusionMatrixDisplay(confusion_matrix = confusion_matrix, display_labels=le.classes_)\n");
		                pythonScriptStringBuilder.append("    cm_display.plot()\n");
		                pythonScriptStringBuilder.append("    plt.savefig(f\"{plots_path}confusion_matrix.png\")\n");
		                pythonScriptStringBuilder.append("    plt.clf()\n");
		                pythonScriptStringBuilder.append("    print('Print Confusion Matrix saved')\n");
		                pythonScriptStringBuilder.append("except:\n");
		                pythonScriptStringBuilder.append("    print(\"Failed to produce Confusion Matrix\")\n\n");
		                break;
                case PRECISION_RECALL:
                		pythonScriptStringBuilder.append("from sklearn.metrics import precision_recall_curve\n");
                		pythonScriptStringBuilder.append("from sklearn.preprocessing import label_binarize \n");
		                pythonScriptStringBuilder.append("if len(le.classes_) == 2:\n");
		                pythonScriptStringBuilder.append("    try:\n");
		                if(da.getModelAlgorithm() instanceof LinearClassifierLogisticRegression){
  		                  pythonScriptStringBuilder.append("        y_scores = model.decision_function(X_test)\n");
		                } else {
  		                  pythonScriptStringBuilder.append("        y_scores = model.predict_proba(X_test)[:,1]\n");
		                }
		                pythonScriptStringBuilder.append("        precision, recall, thresholds = precision_recall_curve(y_test, y_scores)\n");
		                pythonScriptStringBuilder.append("        plt.plot(recall, precision)\n");
		                pythonScriptStringBuilder.append("        plt.xlabel('Recall')\n");
		                pythonScriptStringBuilder.append("        plt.ylabel('Precision')\n");
		                pythonScriptStringBuilder.append("        plt.savefig(f\"{plots_path}precision_recall.png\")\n");
		                pythonScriptStringBuilder.append("        plt.clf()\n");
		                pythonScriptStringBuilder.append("        print('Precision-Recall saved')\n");
		                pythonScriptStringBuilder.append("    except:\n");
		                pythonScriptStringBuilder.append("        print(\"Failed to produce Precision-Recall Graph\")\n\n");
		                
		                
		                pythonScriptStringBuilder.append("else:\n");
		                pythonScriptStringBuilder.append("    try:\n");
		                if(da.getModelAlgorithm() instanceof LinearClassifierLogisticRegression){
  		                  pythonScriptStringBuilder.append("        y_scores = model.decision_function(X_test)\n");
		                } else {
  		                  pythonScriptStringBuilder.append("        y_scores = model.predict_proba(X_test)\n");
		                }
	                  pythonScriptStringBuilder.append("        y_test_bin = label_binarize(y_test, classes=range(len(le.classes_)))\n");
	                  pythonScriptStringBuilder.append("        for i, class_label in enumerate(le.classes_):\n");	                  
	                  pythonScriptStringBuilder.append("            precision, recall, _ = precision_recall_curve(y_test_bin[:, i], y_scores[:, i])\n");
		                pythonScriptStringBuilder.append("            plt.plot(recall, precision)\n");
		                pythonScriptStringBuilder.append("            plt.xlabel('Recall')\n");
		                pythonScriptStringBuilder.append("            plt.ylabel('Precision')\n");
		                pythonScriptStringBuilder.append("            plt.title(f\"Precision-Recall - {class_label}\")\n");
		                pythonScriptStringBuilder.append("            plt.savefig(f\"{plots_path}precision_recall_{class_label}.png\")\n");
		                pythonScriptStringBuilder.append("            plt.clf()\n");
		                pythonScriptStringBuilder.append("        print('Precision-Recall saved')\n");
		                pythonScriptStringBuilder.append("    except:\n");
		                pythonScriptStringBuilder.append("        print(\"Failed to produce Precision-Recall Graph\")\n\n");
		                break;
                case LEARNING_CURVE:
                		pythonScriptStringBuilder.append("from sklearn.model_selection import learning_curve\n");		
		                pythonScriptStringBuilder.append("try:\n");
		                pythonScriptStringBuilder.append("    train_size_abs, train_scores, test_scores = learning_curve(model, X_train, y_train, train_sizes=[0.3, 0.6, 0.9])\n");
		                pythonScriptStringBuilder.append("    plt.figure()\n");
		                pythonScriptStringBuilder.append("    plt.plot(train_size_abs, train_scores.mean(axis=1), label = 'Training error')\n");
		                pythonScriptStringBuilder.append("    plt.plot(train_size_abs, test_scores.mean(axis=1), label = 'Test error')\n");
		                pythonScriptStringBuilder.append("    plt.xlabel('Training set size')\n");
		                pythonScriptStringBuilder.append("    plt.ylabel('Score')\n");
		                pythonScriptStringBuilder.append("    plt.legend()\n");
		                pythonScriptStringBuilder.append("    plt.savefig(f\"{plots_path}learning_curve.png\")\n");
		                pythonScriptStringBuilder.append("    plt.clf()\n");
		                pythonScriptStringBuilder.append("    print(\"Learning Curve Saved\")\n\n");
		                pythonScriptStringBuilder.append("except:\n");
		                pythonScriptStringBuilder.append("    print(\"Failed to produce Learning Curve\")\n\n");
		                break;
		            case ROC_CURVE:
		            		pythonScriptStringBuilder.append("from sklearn.metrics import roc_curve, auc\n");
		                pythonScriptStringBuilder.append("if len(le.classes_) <= 2:\n");
		                pythonScriptStringBuilder.append("    try:\n");
		                if(da.getModelAlgorithm() instanceof LinearClassifierLogisticRegression){
  		                  pythonScriptStringBuilder.append("        y_proba = model.decision_function(X_test)\n");
		                } else {
  		                  pythonScriptStringBuilder.append("        y_proba = model.predict_proba(X_test)[:,1]\n");
		                }
		                pythonScriptStringBuilder.append("        fpr, tpr, thresholds = roc_curve(y_test, y_proba)\n");
		                pythonScriptStringBuilder.append("        roc_auc = auc(fpr, tpr)\n");
		                pythonScriptStringBuilder.append("        plt.plot(fpr, tpr, label='ROC curve')\n");
		                pythonScriptStringBuilder.append("        plt.plot([0, 1], [0, 1], 'k--', label='No Skill')\n");
		                pythonScriptStringBuilder.append("        plt.xlim([0.0, 1.0])\n");
		                pythonScriptStringBuilder.append("        plt.ylim([0.0, 1.05])\n");
		                pythonScriptStringBuilder.append("        plt.xlabel(\'False Positive Rate\')\n");
		                pythonScriptStringBuilder.append("        plt.ylabel(\'True Positive Rate\')\n");
		                pythonScriptStringBuilder.append("        plt.title(\'ROC\')\n");	
		                pythonScriptStringBuilder.append("        plt.legend(loc=\"lower right\")\n");
		                pythonScriptStringBuilder.append("        plt.savefig(f\"{plots_path}ROC.png\")\n");	
                    pythonScriptStringBuilder.append("        print(\"ROC-Curve Saved\")\n\n");
		                pythonScriptStringBuilder.append("    except:\n");
		                pythonScriptStringBuilder.append("        print(\"Failed to produce ROC-Curve\")\n\n");
		                pythonScriptStringBuilder.append("else:\n");
		                pythonScriptStringBuilder.append("    from sklearn.preprocessing import label_binarize \n");
		                pythonScriptStringBuilder.append("    try:\n");
		                if(da.getModelAlgorithm() instanceof LinearClassifierLogisticRegression){
  		                  pythonScriptStringBuilder.append("        y_proba = model.decision_function(X_test)\n");
		                } else {
  		                  pythonScriptStringBuilder.append("        y_proba = model.predict_proba(X_test)[:,1]\n");
		                }
		                pythonScriptStringBuilder.append("        y_test_binarized = label_binarize(y_test, classes=np.unique(y_test))\n");
		                pythonScriptStringBuilder.append("        fpr = dict()\n");
		                pythonScriptStringBuilder.append("        tpr = dict()\n");
		                pythonScriptStringBuilder.append("        roc_auc = dict()\n");
		                pythonScriptStringBuilder.append("        for i in range(len(le.classes_)):\n");
		                pythonScriptStringBuilder.append("            fpr[i], tpr[i], _ = roc_curve(y_test_binarized[:, i], y_proba[:, i])\n");
		                pythonScriptStringBuilder.append("            roc_auc[i] = auc(fpr[i], tpr[i])\n");
		                pythonScriptStringBuilder.append("            plt.plot(fpr[i], tpr[i], linestyle=\'--\', label=f'{le.classes_[i]} vs Rest AUC ={roc_auc[i]}')\n\n");
		                pythonScriptStringBuilder.append("        plt.plot([0, 1], [0, 1], 'b--')\n");
		                pythonScriptStringBuilder.append("        plt.xlim([0.0, 1.0])\n");
		                pythonScriptStringBuilder.append("        plt.ylim([0.0, 1.05])\n");
		                pythonScriptStringBuilder.append("        plt.xlabel(\'False Positive Rate\')\n");
		                pythonScriptStringBuilder.append("        plt.ylabel(\'True Positive Rate\')\n");
		                pythonScriptStringBuilder.append("        plt.title(\'ROC\')\n");	
		                pythonScriptStringBuilder.append("        plt.legend(loc=\"lower right\")\n");
		                pythonScriptStringBuilder.append("        plt.savefig(f\"{plots_path}ROC.png\")\n");	
		                pythonScriptStringBuilder.append("        print(\"ROC-Curve Saved\")\n\n");
		                pythonScriptStringBuilder.append("    except:\n");
		                pythonScriptStringBuilder.append("        print(\"Failed to produce ROC-Curve\")\n\n");
		                break;
                default: 
                  break;
            }
          }
			} else {
		      DataAnalyticsModelAlgorithm dataAnalyticsModelAlgorithm = da.getModelAlgorithm();
			    if (da.getPlots().contains(Plots.CLUSTERING)) {
		      	if (dataAnalyticsModelAlgorithm instanceof KMeans || dataAnalyticsModelAlgorithm instanceof MiniBatchKMeans) {
      		    pythonScriptStringBuilder.append("try:\n");
		          pythonScriptStringBuilder.append(
		              "		plt.figure()\n" +
		              "		plt.scatter(X_test[:, 0], X_test[:, 1], c=y_pred, s=50, cmap='Tab10')\n" +
		              "		centers = model.cluster_centers_\n" +
		              "		plt.scatter(centers[:, 0], centers[:, 1], c='red', s=200,marker='o')\n"+
		              "		plt.title('Clustering Visualization')\n" +
		              "		plt.savefig(plots_path + 'kmeans_clustering.png')\n" +
		              "		plt.clf()\n\n"
		          );
		          pythonScriptStringBuilder.append(
                  "except:\n" +
                  "		print(\"Error Creating KMeans clustering plot\")\n\n"
		          );
	          } else if (dataAnalyticsModelAlgorithm instanceof DBSCAN) {
      		    pythonScriptStringBuilder.append("try:\n");
		          pythonScriptStringBuilder.append(
		              "		labels = model.labels_\n" +
		              "		n_clusters_ = len(set(labels)) - (1 if -1 in labels else 0)\n" +
		              "		unique_labels = set(labels)\n" +
		              "		colors = [plt.cm.Spectral(each) for each in np.linspace(0, 1, len(unique_labels))]\n" +
		              "		plt.figure()\n" +
		              "		for k, col in zip(unique_labels, colors):\n" +
		              "				if k == -1:\n" +
		              "						col = [0, 0, 0, 1]\n" +
		              "				class_member_mask = (labels == k)\n" +
		              "				xy = X[class_member_mask]\n" +
		              "				plt.plot(xy[:, 0], xy[:, 1], 'o', markerfacecolor=tuple(col),markeredgecolor='k', markersize=6)\n\n" +
		              "		plt.title('DBSCAN Clustering Visualization')\n" +
		              "		plt.savefig(plots_path + 'dbscan_clustering.png')\n" +
		              "		plt.clf()\n\n"
		          );
		          pythonScriptStringBuilder.append(
                  "except:\n" +
                  "		print(\"Error Creating DBSCAN plot\")\n\n"
		          );
	          } else if (dataAnalyticsModelAlgorithm instanceof SpectralClustering) {
      		    pythonScriptStringBuilder.append("try:\n");
		          pythonScriptStringBuilder.append(
		              "		plt.figure()\n" +
		              "		plt.scatter(X_test[:, 0], X_test[:, 1], c=y_pred, s=50, cmap='Tab10')\n" +
		              "		plt.title('Spectral Clustering Visualization')\n" +
		              "		plt.savefig(plots_path + 'spectral_clustering.png')\n" +
		              "		plt.clf()\n\n"
		          );
		          pythonScriptStringBuilder.append(
                  "except:\n" +
                  "		print(\"Error Creating SpectralClustering clustering plot\")\n\n"
		          );
	          } else if (dataAnalyticsModelAlgorithm instanceof GaussianMixture) {
      		    pythonScriptStringBuilder.append("try:\n");
		          pythonScriptStringBuilder.append(
		              "		plt.scatter(X_test[:, 0], X_test[:, 1], c=y_pred, s=50, cmap='Tab10')\n" +
		              "		centers = model.means_\n" +
		              "		plt.scatter(centers[:, 0], centers[:, 1], c='red', s=200, marker='o')\n" +
		              "		plt.figure()\n" +
		              "		plt.title('Gaussian Mixture Clustering Visualization')\n" +
		              "		plt.savefig(plots_path + 'kmeans_clustering.png')\n" +
		              "		plt.clf()\n\n"
		          );
		          pythonScriptStringBuilder.append(
                  "except:\n" +
                  "		print(\"Error Creating GaussianMixture clustering plot\")\n\n"
		          );
            }
	      }
			}
    }
    
private void generateDAReport(StringBuilder pythonScriptStringBuilder, String path_str, DataAnalytics da) {
	  pythonScriptStringBuilder.append("plots_path = '" + path_str + "/python-scripts/plots/'\n");

		pythonScriptStringBuilder.append("import numpy as np\n");
		pythonScriptStringBuilder.append("import pickle\n");
		
		pythonScriptStringBuilder.append("with open(r'" + path_str + "/python-scripts/pickles/preprocess_original_df.pickle','rb') as f:\n");
	  pythonScriptStringBuilder.append("    original_df = pickle.load(f)\n\n");
		
		pythonScriptStringBuilder.append("html = f\'\'\'\n<html>\n<head>\n<title> Data Analytics Report </title>\n</head>\n<body>\n<h1>Data Analytics Report</h1>\n\'\'\'\n");

		if (da.getLabels() == Labels.ON) {
			pythonScriptStringBuilder.append("learning_type = \"Supervised\"\n");
		} else if (da.getLabels() == Labels.SEMI) {
			pythonScriptStringBuilder.append("learning_type = \"Semi-supervised\"\n");
		} else {
			pythonScriptStringBuilder.append("learning_type = \"Unsupervised\"\n");
		}
		
		pythonScriptStringBuilder.append("model_type = \"" + da.getModelAlgorithm().eClass().getName().toString() +"\"\n");
				
		pythonScriptStringBuilder.append("model_name = \"" + da.getModelAlgorithm().getName() +"\"\n");
		
		pythonScriptStringBuilder.append("library = \"" + 	da.getAnnotations().get(0).getValue() +"\"\n");
		
		pythonScriptStringBuilder.append("html += f\'\'\'<h2>Model Information </h2>\n<p>Model Type: {model_type} </p>\n<p>Learning Type: {learning_type} </p>\n<p>Model Name: {model_name}</p>\n<p>Library: {library}</p>\\n <h3>Model Parameters</h3>\\n<table>\\n<tr>\\n<th>Parameter</th>\\n<th>Value</th>\\n</tr>\\n{\"\".join(\"<tr><td>\" + str(key) + \"</td>\" + \"<td>\" + str(value) + \"</td></tr>\" for key, value in model.get_params().items())}\n</table>\n \'\'\'\n");
		
		pythonScriptStringBuilder.append("features = X_train.columns.values.tolist() \n");
		pythonScriptStringBuilder.append("features.append(y_train.name) \n");

		pythonScriptStringBuilder.append("feature_types = list(X_train.dtypes.map(lambda x : x.name))\n");
		pythonScriptStringBuilder.append("feature_types.append(y_train.dtype) \n");
		pythonScriptStringBuilder.append("data_size = original_df[original_df.columns[0]].count()\n");
		
		pythonScriptStringBuilder.append("dataset_location = \"" + da.getDataset() +"\"\n");
		pythonScriptStringBuilder.append("training_results_location = \"" + da.getTrainingResults() +"\"\n");
		pythonScriptStringBuilder.append("html += f\'\'\'\n<h2>Data Information</h2><p>Dataset Size: {data_size} </p>\n<p>Dataset Location: {dataset_location}</p>\n <p>Training Results Location: {training_results_location} </p>\n <p>Target Features:" + da.getPredictionResults().stream().map(Property::getName).collect(Collectors.joining(",")) +  "</p>\n <h3>Features</h3>\n<table>\n<tr>\n<th>Feature</th>\n<th>Type</th>\n</tr>\n{\"\".join(\"<tr><td>\" + str(feature) + \"</td>\" + \"<td>\" + str(type) + \"</td></tr>\" for feature, type in zip(features,feature_types))}\n</table>\n<h2>Performance Metrics</h2>\n\'\'\'\n");

		EList<Metrics> metrics = da.getMetrics();
		for (Metrics metric : metrics) {
			pythonScriptStringBuilder.append("try:\n");
			switch (metric) {
				case RMSE:
					pythonScriptStringBuilder.append("	RMSE = np.sqrt(((y_pred - y_test) ** 2).mean())\n");
					pythonScriptStringBuilder.append("	html += f\'\'\'<p>RMSE: {RMSE}</p>\n\'\'\'\n");
					break;
				case MAE:
					pythonScriptStringBuilder.append("	from sklearn.metrics import mean_absolute_error as mae\n");
					pythonScriptStringBuilder.append("	MAE = mae(y_test, y_pred)\n");
					pythonScriptStringBuilder.append("	html += f\'\'\'<p>MAE: {MAE}</p>\n\'\'\'\n");
					break;
				case MSE:
					pythonScriptStringBuilder.append("	MSE = np.square(np.subtract(y_test, y_pred)).mean()\n");
					pythonScriptStringBuilder.append("	html += f\'\'\'<p>MSE: {MSE}</p>\n\'\'\'\n");
					break;
				case R2:
					pythonScriptStringBuilder.append("	from sklearn.metrics import r2_score\n");
					pythonScriptStringBuilder.append("	R2 = r2_score(y_test, y_pred)\n");
					pythonScriptStringBuilder.append("	html += f\'\'\'<p>R-Squared: {R2}</p>\n\'\'\'\n");
					break;
				case ACCURACY:
					pythonScriptStringBuilder.append("	from sklearn.metrics import accuracy_score\n");
					pythonScriptStringBuilder.append("	accuracy = accuracy_score(y_test, y_pred)\n");
					pythonScriptStringBuilder.append("	html += f\'\'\'<p>Accuracy: {accuracy}</p>\n \'\'\'\n");
					break;
				case PRECISION:
          pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
          pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
          pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
          
					pythonScriptStringBuilder.append("	from sklearn.metrics import classification_report\n");
					pythonScriptStringBuilder.append("	classif = classification_report(le.inverse_transform(y_test), le.inverse_transform(y_pred), output_dict=True)\n");
					pythonScriptStringBuilder.append("	for key, value in classif.items():\n");
					pythonScriptStringBuilder.append("		if key != 'accuracy' and key != 'macro avg' and key != 'weighted avg':\n");
					pythonScriptStringBuilder.append("			html += f\'\'\'<p>Class: {str(key)}</p>\n \'\'\'\n");
					pythonScriptStringBuilder.append("			html += f\'\'\'<p>Precision: {value[\'precision\']}</p>\n \'\'\'\n");
					break;
				case RECALL:
          pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
          pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
          pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
          
					pythonScriptStringBuilder.append("	from sklearn.metrics import classification_report\n");
					pythonScriptStringBuilder.append("	classif = classification_report(le.inverse_transform(y_test), le.inverse_transform(y_pred), output_dict=True)\n");
					pythonScriptStringBuilder.append("	for key, value in classif.items():\n");
					pythonScriptStringBuilder.append("		if key != 'accuracy' and key != 'macro avg' and key != 'weighted avg':\n");
					pythonScriptStringBuilder.append("			html += f\'\'\'<p>Class: {str(key)}</p>\n \'\'\'\n");
					pythonScriptStringBuilder.append("			html += f\'\'\'<p>Recall: {value[\'recall\']}</p>\n \'\'\'\n");
					break;
				case F1_SCORE:
          pythonScriptStringBuilder.append("	from sklearn.preprocessing import LabelEncoder\n");
          pythonScriptStringBuilder.append("	with open('" + path_str + "/python-scripts/pickles/"+ "preprocess_label_encoder.pickle', 'rb') as pickle_file:\n");
          pythonScriptStringBuilder.append("		le = pickle.load(pickle_file)\n\n");
          
					pythonScriptStringBuilder.append("	from sklearn.metrics import classification_report\n");
					pythonScriptStringBuilder.append("	classif = classification_report(le.inverse_transform(y_test), le.inverse_transform(y_pred), output_dict=True)\n");
					pythonScriptStringBuilder.append("	for key, value in classif.items():\n");
					pythonScriptStringBuilder.append("		if key != 'accuracy' and key != 'macro avg' and key != 'weighted avg':\n");
					pythonScriptStringBuilder.append("			html += f\'\'\'<p>Class: {str(key)}</p>\n \'\'\'\n");
					pythonScriptStringBuilder.append("			html += f\'\'\'<p>F1-Score: {value[\'f1-score\']}</p>\n \'\'\'\n\n\n");
					break;
				default:
		            break;
				}
			pythonScriptStringBuilder.append("except:\n");
			pythonScriptStringBuilder.append("	print(\"Failed to produce metric: " + metric + "\")\n\n");
		}
		pythonScriptStringBuilder.append("html += f\'\'\'</body>\n</html>\n\'\'\'\n");
		pythonScriptStringBuilder.append("with open(plots_path + 'html_report.html', 'w') as f:\n");
		pythonScriptStringBuilder.append("	f.write(html)\n");
		pythonScriptStringBuilder.append("	f.close()\n\n");
	}    
}
