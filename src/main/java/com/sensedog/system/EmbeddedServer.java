package com.sensedog.system;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class EmbeddedServer {

    private Server server;

    private EmbeddedServer(Builder builder) {
        try {
            ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(ResourceConfig.forApplicationClass(builder.resourceConfig)));
            jerseyServlet.setInitOrder(0);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/" + builder.contextPath);
            context.addServlet(jerseyServlet, "/*");

            server = new Server(builder.port);
            server.setHandler(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.destroy();
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int port;
        private String contextPath;
        private Class<? extends ResourceConfig> resourceConfig;

        private Builder() {
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setContextPath(String contextPath) {
            this.contextPath = contextPath;
            return this;
        }

        public Builder setResourceConfig(Class<? extends ResourceConfig> resourceConfig) {
            this.resourceConfig = resourceConfig;
            return this;
        }

        public EmbeddedServer build() {
            return new EmbeddedServer(this);
        }
    }
}
