package pw.stego.network.container.steganography;

import pw.stego.network.container.Sign;
import pw.stego.network.container.util.FileMagic;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Stego lib wrapper
 * Created by lina on 01.01.17.
 */
public class Stego implements Steganography {
    private Random r = new Random();

    @Override
    public Boolean isSignedWith(Sign sign, File container) {
        try {
            return pw.Stego.tryKey(sign.getValue(), container);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean isAcceptableContainer(File container) {
        switch (FileMagic.getFileFormat(container)) {
            case "PNG": case "GIF": case "BMP":
                return true;

            default:
                return false;
        }
    }

    @Override
    public byte[] extract(Sign sign, File container) throws IOException {
        return pw.Stego.decode(sign.getValue(), container);
    }

    @Override
    public File insert(Sign sign, byte[] message, File container) throws IOException {
        return pw.Stego.encode(sign.getValue(), message, pw.stego.util.Patterns.Type.SIMPLE, container);
    }

    /**
     * @param message to insert into container
     * @return {X, Y} - image size
     */
    @Override
    public String[] getOptimalContainerParams(byte[] message) {
        int blocks = message.length * 4;

        int dim = (int) Math.sqrt(blocks);
        dim += r.nextInt(dim);

        return new String[]{String.valueOf(dim), String.valueOf(blocks / dim + blocks * 4 % dim + r.nextInt(dim))};
    }
}
