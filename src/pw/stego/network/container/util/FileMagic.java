package pw.stego.network.container.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * File magic number extractor
 * Created by lina on 15.12.16.
 */
public class FileMagic {
    public static String getFileFormat(File file) {
        byte[] magic = new byte[4];
        try {
            System.arraycopy(Files.readAllBytes(file.toPath()), 0, magic, 0, 4);
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
