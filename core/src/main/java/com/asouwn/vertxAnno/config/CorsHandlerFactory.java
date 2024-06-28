package com.asouwn.vertxAnno.config;

import io.vertx.ext.web.handler.CorsHandler;

public class CorsHandlerFactory {
    private final static CorsHandler handler;

    static {
        handler = CorsHandler.create();
    }

    public static CorsHandler getInstance() {
        return handler;
    }
    public static void setHandler(){

    }
}
