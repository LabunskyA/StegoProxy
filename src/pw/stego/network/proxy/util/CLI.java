package pw.stego.network.proxy.util;

import pw.stego.network.container.util.ContainerFactory;
import pw.stego.network.container.util.EmptyFilesFactory;
import pw.stego.network.container.util.RandomPNGFactory;

/**
 * Command Line Interface
 * Created by lina on 06.01.17.
 */
public class CLI {
    public static void help() {
        System.out.println("Available steganography algorithms:");
        System.out.println("stego - lossless image steganography;");
        System.out.println("nostego - raw files without steganography;");

        System.out.println();

        System.out.println("Available container factories:");
        System.out.println("randompngfactory - random pngs as containers;");
        System.out.println("emptyfilesfactory - empty container files");
    }

    public static ContainerFactory getContainerFactory(String name) {
        switch (name.toLowerCase()) {
            case "randompngfactory":
                return new RandomPNGFactory();

            case "emptyfilesfactory":
                return new EmptyFilesFactory();

            default:
                System.out.println("Wrong factory");
                return null;
        }
    }
}
