/*
 * Copyright 2010-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.samples;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

import com.amazonaws.codegen.model.intermediate.Example;
import com.amazonaws.codegen.model.intermediate.IntermediateModel;
import com.amazonaws.codegen.model.intermediate.ListModel;
import com.amazonaws.codegen.model.intermediate.MapModel;
import com.amazonaws.codegen.model.intermediate.MemberModel;
import com.amazonaws.codegen.model.intermediate.Metadata;
import com.amazonaws.codegen.model.intermediate.OperationModel;
import com.amazonaws.codegen.model.intermediate.ShapeModel;
import com.amazonaws.samples.exception.SampleGenerationException;
import com.amazonaws.codegen.emitters.JavaCodeFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Produces Java generated sample code from generic sample content included with
 * a service intermediate model.
 */
public class SamplesGenerator {

    private final IntermediateModel model;
    private final StringBuilder sb;
    private final JavaCodeFormatter formatter;

    /**
     * Ctor.
     *
     * @param model
     *            service intermediate model containing generic samples
     */
    public SamplesGenerator(final IntermediateModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Intermediate model must be provided");
        }

        this.model = model;
        this.sb = new StringBuilder();

        Map formatterOptions = new HashMap();
        formatterOptions.put(DefaultCodeFormatterConstants.FORMATTER_COMMENT_FORMAT_LINE_COMMENT, false);
        this.formatter = new JavaCodeFormatter(formatterOptions);
    }

    /**
     * Produces Java generated samples content.
     *
     * @return generated samples content
     */
    public String getGeneratedSamples() throws SampleGenerationException {
        String serviceName = model.getMetadata().getSyncInterface();

        String packageName = model.getMetadata().getPackageName();
        writeLine(String.format("import %s.*;", packageName));
        writeLine(String.format("import %s.model.*;\n", packageName));
        writeLine(String.format("public class %sGeneratedSamples {", serviceName));

        model.getExamples().getOperationExamples().forEach((k, v) -> emitCode(serviceName, k, v));

        writeLine("}");

        return formatter.format(sb.toString());
    }

    private void emitCode(final String serviceName, final String operationName, final List<Example> examples) {
        Metadata metadata = model.getMetadata();
        OperationModel operationModel = model.getOperations().get(operationName);

        if (operationModel == null) {
            throw new IllegalArgumentException("Could not retrieve operation model for operation " + operationName);
        }

        String operationMethodName = operationModel.getMethodName();
        String inputType = operationModel.getInput().getVariableType();
        ShapeModel inputModel = model.getShapes().get(inputType);

        for (int i=0; i<examples.size(); i++) {
            Example example = examples.get(i);
            writeLine(String.format("public void %s_%s() {", operationName, i+1));
            writeLine(String.format("//BEGIN_SAMPLE:%s.%s", serviceName, operationName));
            writeLine("//TITLE:" + example.getTitle());
            writeLine("//DESCRIPTION:" + example.getDescription());

            writeLine(String.format("%s client = new %s();", metadata.getSyncInterface(), metadata.getSyncClient()));
            writeLine(String.format("%1$s request = new %1$s()", inputType));

            getRequestAssignments(example.getInput(), inputModel, example.getComments().getInputComments());

            if (operationModel.getReturnType() != null) {
                String returnType = operationModel.getReturnType().getReturnType();
                writeLine(String.format("%s response = client.%s(request);", returnType,
                                        operationMethodName));
            } else {
                writeLine(String.format("client.%s(request);", operationMethodName));
            }
            writeLine("//END_SAMPLE");
            writeLine("}");
        }
    }

    private void getRequestAssignments(final JsonNode node, final ShapeModel inputModel,
            final Map<String, String> comments) {
        Iterator<String> iter = node.fieldNames();
        while (iter.hasNext()) {
            String fieldName = iter.next();
            MemberModel memberModel = inputModel.findMemberModelByC2jName(fieldName);

            StringBuilder assignmentValue = new StringBuilder();
            getAssignmentValue(assignmentValue, node.get(fieldName), memberModel);

            if (comments.containsKey(fieldName)) {
                writeLine("//" + comments.get(fieldName));
            }

            if (memberModel.isList()) {
                sb.append(String.format(".with%s(%s)", firstCharToUpper(fieldName), assignmentValue.toString()));
            } else if (memberModel.isMap()) {
                sb.append(assignmentValue.toString());
            } else {
                sb.append(String.format(".with%s(%s)", firstCharToUpper(fieldName), assignmentValue.toString()));
            }
        }
        sb.append(";\n");
    }

    private void getAssignmentValue(final StringBuilder sb, final JsonNode node, MemberModel memberModel) {
        if (memberModel.isSimple()) {
            sb.append(formatPrimitiveValue(memberModel.getSetterModel().getSimpleType(), node.asText()));
        } else if (memberModel.isList()) {
            ListModel listModel = memberModel.getListModel();
            Iterator<JsonNode> iter = node.iterator();

            while (iter.hasNext()) {
                JsonNode entry = iter.next();

                if (listModel.getListMemberModel() == null) {
                    sb.append(formatPrimitiveValue(listModel.getMemberType(), entry.asText()));
                } else {
                    getAssignmentValue(sb, entry, listModel.getListMemberModel());
                }

                if (iter.hasNext()) {
                    sb.append(", ");
                }
            }
        } else if (memberModel.isMap()) {
            MapModel mapModel = memberModel.getMapModel();
            Iterator<Entry<String, JsonNode>> iter = node.fields();

            while (iter.hasNext()) {
                Entry<String, JsonNode> field = iter.next();
                JsonNode curNode = field.getValue();
                sb.append(String.format(".add%sEntry(", firstCharToUpper(memberModel.getC2jName())));

                sb.append(formatPrimitiveValue(mapModel.getKeyType(), field.getKey()));
                sb.append(", ");

                if (mapModel.getValueModel() == null) {
                    sb.append(formatPrimitiveValue(mapModel.getValueType(), curNode.asText()));
                } else {
                    getAssignmentValue(sb, curNode, mapModel.getValueModel());
                }

                sb.append(")");
            }
        } else {
            sb.append(String.format("new %s()", memberModel.getC2jShape()));

            Iterator<Entry<String, JsonNode>> iter = node.fields();
            ShapeModel memberShape = model.getShapes().get(memberModel.getC2jShape());

            while (iter.hasNext()) {
                Entry<String, JsonNode> field = iter.next();
                MemberModel fieldMemberModel = memberShape.getMemberByC2jName(field.getKey());

                if (fieldMemberModel.isMap()) {
                    getAssignmentValue(sb, field.getValue(), fieldMemberModel);
                } else {
                    sb.append(String.format(".with%s(", firstCharToUpper(field.getKey())));
                    getAssignmentValue(sb, field.getValue(), fieldMemberModel);
                    sb.append(")");
                }
            }
        }
    }

    private String formatPrimitiveValue(final String type, final String value) {
        if (type.equals("String")) {
            return String.format("\"%s\"", StringEscapeUtils.escapeJava(value));
        } else if (type.equals("Boolean")) {
            return value.toLowerCase();
        } else if (type.equals("Long")) {
            return value + "L";
        } else {
            return value;
        }
    }

    private void writeLine(final String line) {
        sb.append(line + "\n");
    }

    private String firstCharToUpper(final String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
