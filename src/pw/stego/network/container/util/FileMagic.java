package pw.stego.network.container.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * File magic number extractor
 * Created by lina on 15.12.16.
 */
public class FileMagic {
    private static byte[] getFileMagic(File file) throws IOException {
        byte[] magic = new byte[4];
        try (FileInputStream fis = new FileInputStream(file)) {
            if (fis.read(magic) != magic.length)
                throw new IOException("Read not expected bytes count");
        }

        return magic;
    }

    public static String getFileFormat(byte[] bytes) {
        byte[] magic = new byte[]{bytes[0], bytes[1], bytes[2], bytes[3]};
        String format = new String(magic);

        if (format.contains("PNG"))
            return "PNG";

        if (format.contains("GIF"))
            return "GIF";

        if (format.contains("BM"))
            return "BMP";

        return "DUNNO";
    }

    public static String getFileFormat(File file) throws IOException {
        byte[] magic = getFileMagic(file);
        return getFileFormat(magic);
    }

    public static boolean isLosslessImage(byte[] bytes) {
        switch (getFileFormat(bytes)) {
            case "PNG": case "GIF": case "BMP":
                return true;

            default:
                return false;
        }
    }

    public static boolean isLosslessImage(File file) throws IOException {
        return isLosslessImage(getFileMagic(file));
    }
}
