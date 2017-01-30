package pw.stego.network.container.util;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;

/**
 * File magic number extractor
 * Created by lina on 15.12.16.
 */
public class FileMagic {
    public static String getFileFormat(File file) {
        byte[] magic = new byte[4];
        try {
            FileInputStream fis = new FileInputStream(file);
            if (fis.read(magic) != magic.length)
                throw new IOException("Read not expected bytes count");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        String format = new String(magic);

        if (format.contains("PNG"))
            return "PNG";

        if (format.contains("GIF"))
            return "GIF";

        if (format.contains("BM"))
            return "BMP";

        return "DUNNO";
    }
}
