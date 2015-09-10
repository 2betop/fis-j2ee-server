package com.baidu.fis.server.launch;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.File;

/**
 * Created by 2betop on 5/9/14.
 */
public class Main {


    public static int PORT = 8080;
    public static String BASE_DIR = null;
    public static String WEBAPP_DIR = "./web";
    public static String WEBAPP_PATH = "";
    public static Boolean HTTPS = false;

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");

        Options options = new Options();

        options.addOption("h", "help", false, "output usage information" );
        options.addOption("p", "port", true, "server listen port");
        options.addOption("base", true, "tomcat base dir" );
        options.addOption("root", true, "document root" );
        options.addOption("webapp", true, "webapp path" );
        options.addOption("type", false, "useless just ignore it." );
        options.addOption("https", false, "https");

        HelpFormatter help = new HelpFormatter();
        BasicParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("help")) {
            help.printHelp("assemble", options);
            return;
        }

        if (cmd.hasOption("port")) {
            PORT = Integer.valueOf(cmd.getOptionValue("port"));
        }

        if (cmd.hasOption("base")) {
            BASE_DIR = cmd.getOptionValue("base");
        } else if (BASE_DIR==null) {
            BASE_DIR = System.getProperty("java.io.tmpdir");
        }

        if (cmd.hasOption("root")) {
            WEBAPP_DIR = cmd.getOptionValue("root");
        }

        if (cmd.hasOption("webapp")) {
            WEBAPP_PATH = cmd.getOptionValue("webapp");

            if (!WEBAPP_PATH.isEmpty() && !WEBAPP_PATH.startsWith("/")) {
                WEBAPP_PATH = "/" + WEBAPP_PATH;
            }
        }

        if (cmd.hasOption("https")) {
            HTTPS = true;
        }


        startServer();
    }

    protected static void startServer() {
        Tomcat tomcat = new Tomcat();

        try {
            String base = new File(BASE_DIR).getCanonicalPath();
            String webapp = new File(WEBAPP_DIR).getCanonicalPath();

            // System.out.println(base);


            tomcat.setBaseDir(base);
            Connector defaultConnector = tomcat.getConnector();

            defaultConnector.setPort(PORT);

            if (HTTPS) {
                System.out.println("Use HTTPS");

                defaultConnector.setScheme("https");
                defaultConnector.setSecure(true);
                defaultConnector.setAttribute("keystoreFile", System.getProperty("user.dir") + "/../fis.keystore");
                defaultConnector.setAttribute("keystorePass", "123456");
                defaultConnector.setAttribute("clientAuth", "false");
                defaultConnector.setAttribute("sslProtocol", "TLS");
                defaultConnector.setAttribute("SSLEnabled", true);
            }

            tomcat.addWebapp(WEBAPP_PATH, webapp);

            tomcat.start();

            tomcat.getServer().await();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
