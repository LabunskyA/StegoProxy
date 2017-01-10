package pw.stego.network.container.steganography;

import javafx.util.Pair;
import pw.stego.network.container.Sign;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Not a steganography, just for testing
 * Created by lina on 26.12.16.
 */
public class NoStego implements Steganography {
    @Override
    public Boolean isSignedWith(Sign sign, File container) {
        return true;
    }

    @Override
    public Boolean isAcceptableContainer(File container) {
        return true;
    }

    @Override
    public byte[] extract(Sign sign, File container) {
        try {
            return Files.readAllBytes(container.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    @Override
    public File insert(Sign sign, byte[] message, File container) {
        try {
            Files.write(container.toPath(), message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return container;
    }

    @Override
    public String[] getOptimalContainerParams(byte[] message) {
        return new String[]{String.valueOf(message.length)};
    }
}
