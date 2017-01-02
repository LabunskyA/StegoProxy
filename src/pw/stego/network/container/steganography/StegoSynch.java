package pw.stego.network.container.steganography;

import pw.stego.network.container.Sign;
import pw.stego.network.container.util.FileMagic;

import java.io.File;
import java.io.IOException;

/**
 * StegoSynch
 * Created by lina on 15.12.16.
 */
public class StegoSynch implements Steganography {
    private static synchronized Boolean isSignedWithSynch(Sign sign, File container) {
        try {
            return pw.Stego.tryKey(sign.getValue(), container);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static synchronized Boolean isAcceptableContainerSynch(File container) {
        switch (FileMagic.getFileFormat(container)) {
            case "PNG": case "GIF": case "BMP":
                return true;

            default:
                return false;
        }
    }

    private static synchronized byte[] extractSynch(Sign sign, File container) throws IOException {
        return pw.Stego.decode(sign.getValue(), container);
    }

    private static synchronized File insertSynch(Sign sign, byte[] message, File container) throws IOException {
        return pw.Stego.encode(sign.getValue(), message, pw.stego.util.Patterns.Type.SIMPLE, container);
    }

    @Override
    public Boolean isSignedWith(Sign sign, File container) {
        return isSignedWithSynch(sign, container);
    }

    @Override
    public Boolean isAcceptableContainer(File container) {
        return isAcceptableContainerSynch(container);
    }

    @Override
    public byte[] extract(Sign sign, File container) throws IOException {
        return extractSynch(sign, container);
    }

    @Override
    public File insert(Sign sign, byte[] message, File container) throws IOException {
        return insertSynch(sign, message, container);
    }
}
