package com.baidu.fis.server.launch;

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
    public static String WEBAPP_DIR = "./web";

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        options.addOption("h", "help", false, "output usage information" );
        options.addOption("p", "port", true, "server listen port");
        options.addOption("root", true, "document root" );

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

        if (cmd.hasOption("root")) {
            WEBAPP_DIR = cmd.getOptionValue("root");
        }

        startServer();
    }

    protected static void startServer() throws Exception{
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(PORT);
        tomcat.addContext("/", new File(WEBAPP_DIR).getAbsolutePath());

        System.out.println(new File(WEBAPP_DIR).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
