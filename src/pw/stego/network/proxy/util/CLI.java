package pw.stego.network.proxy.util;

import java.sql.Timestamp;


/**
 * Command Line Interface
 * Created by lina on 06.01.17.
 */
public class CLI {
    public static boolean process(String arg) {
        arg = arg.toLowerCase().replaceAll("[^a-z]", "");
        switch (arg) {
            case "printHelp":
            case "h":
                printHelp();
                return true;

            case "format":
            case "fmt":
                printConfFormat();
                return true;
        }

        return false;
    }

    public static void printHelp() {
        System.out.println("Use .conf files in config folder to configure proxy.");
        System.out.println("If you want to use different config file - use path to it as a program argument.");
        System.out.println("If you want to see .conf file format, variables and values - use \"--format\" key.");
    }

    public static void printConfFormat() {
        System.out.println("Config file format:");
        System.out.println("variable=value");
        System.out.println();

        System.out.println("\"tunnel\" - describes tunnel that will be used to translate traffic.");
        System.out.println("Different tunnels requires different \"destination\" variable values. Available tunnels for now:");
        System.out.println("SocketTunnel - communication through sockets. Destination should be \"ip:port\".");
        System.out.println();

        System.out.println("\"destination\" - where tunnel will be connected to or where traffic from tunnel will be redirected to.");
        System.out.println("Format depends on tunnel type used.");
        System.out.println();

        System.out.println("\"acceptor_port\" - (for proxy client) port to accept connections as proxy server.");
        System.out.println();

        System.out.println("\"acceptor\" - (for proxy server) is how tunnel will be waiting for tunnel incoming connections.");
        System.out.println("It depends on tunnel type used.");

        System.out.println("\"algorithm\" - steganography algorithm to use.");
        System.out.println("Different algorithms requires different \"container_factory\" variable values. Available algorithms for now:");
        System.out.println("Stego - steganography in lossless images (like png, bmp, gif, etc);");
        System.out.println("NoStego - plain data in files;");

        System.out.println("\"container_factory\" - factory which will produce containers to transmit data through tunnel.");
        System.out.println("Depends on \"algorithm\" variable, should produce compatible containers. Available factories:");
        System.out.println("lorempixelpng - random png images from lorempixel.com;");
        System.out.println("emptyfilesfactory - empty files in system temp folder;");
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    private CLI() {}
}
