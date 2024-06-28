package com.asouwn.vertxAnno.config;

import io.vertx.core.http.HttpMethod;

import java.util.HashSet;
import java.util.Set;

public class CorsConfig {
    private static final Set<String> allowedHeaders = new HashSet<>();
    private static final Set<HttpMethod> allowedMethods = new HashSet<>();

    public CorsConfig(Set<String> allowedHeaders, Set<HttpMethod> allowedMethods) {
        this.allowedHeaders.addAll(allowedHeaders);
        this.allowedMethods.addAll(allowedMethods);
    }

    public Set<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public Set<HttpMethod> getAllowedMethods() {
        return allowedMethods;
    }
}
