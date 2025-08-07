package com.educaflow.apps.seguridad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConditionBuilder {

    private final StringBuilder conditionBuilder;
    private final List<String> conditionParams;

    private ConditionBuilder() {
        this.conditionBuilder = new StringBuilder();
        this.conditionParams = new ArrayList<>();
    }

    private ConditionBuilder(String condition, String conditionParams) {
        this();
        if (condition != null) {
            this.conditionBuilder.append(condition);
        }
        if (conditionParams != null && !conditionParams.isBlank()) {
            this.conditionParams.addAll(Arrays.asList(conditionParams.split("\\s*,\\s*")));
        }
    }

    public static ConditionBuilder create(String condition, String conditionParams) {
        return new ConditionBuilder(condition, conditionParams);
    }

    public static ConditionBuilder create() {
        return new ConditionBuilder();
    }

    public ConditionBuilder setCondition(String condition) {
        this.conditionBuilder.setLength(0);
        this.conditionBuilder.append(condition);
        return this;
    }

    public ConditionBuilder setConditionParams(String conditionParams) {
        this.conditionParams.clear();
        if (conditionParams != null && !conditionParams.isBlank()) {
            this.conditionParams.addAll(Arrays.asList(conditionParams.split("\\s*,\\s*")));
        }
        return this;
    }

    public ConditionBuilder addConditionParams(String conditionParams) {
        if (conditionParams != null && !conditionParams.isBlank()) {
            this.conditionParams.addAll(Arrays.asList(conditionParams.split("\\s*,\\s*")));
        }
        return this;
    }

    public ConditionBuilder append(String text) {
        this.conditionBuilder.append(text);
        return this;
    }

    public ConditionBuilder addAND() {
        this.conditionBuilder.append(" AND ");
        return this;
    }

    public ConditionBuilder addOR() {
        this.conditionBuilder.append(" OR ");
        return this;
    }

    public ConditionBuilder addOpenParenthesis() {
        this.conditionBuilder.append(" ( ");
        return this;
    }

    public ConditionBuilder addCloseParenthesis() {
        this.conditionBuilder.append(" ) ");
        return this;
    }

    public ConditionBuilder addCondition(String condition) {
        this.conditionBuilder.append(condition);
        return this;
    }

    public String getCondition() {
        return this.conditionBuilder.toString().trim();
    }

    public String getConditionParams() {
        return String.join(", ", this.conditionParams);
    }
}

