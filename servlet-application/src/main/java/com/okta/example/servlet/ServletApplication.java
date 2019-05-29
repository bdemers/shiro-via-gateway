package com.okta.example.servlet;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.io.File;

public class ServletApplication {

    public static void main(String[] args) throws Exception {

        WebAppContext webapp = new WebAppContext();
        webapp.setBaseResource(Resource.newResource(new File( "src/main/webapp")));
        webapp.setContextPath("/");
        webapp.setParentLoaderPriority(true);
        webapp.setConfigurations(new Configuration[] {

            new AnnotationConfiguration(),
            new WebXmlConfiguration(),
            new WebInfConfiguration(),
            new PlusConfiguration(),
            new MetaInfConfiguration(),
            new FragmentConfiguration(),
            new EnvConfiguration()
        });

        // scan everything for annotations, jstl, web fragments, etc
        webapp.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");
        webapp.setParentLoaderPriority(true);

        Server server = new Server(8000);
        server.setHandler(webapp);
        server.start();
        server.join();
    }
}
