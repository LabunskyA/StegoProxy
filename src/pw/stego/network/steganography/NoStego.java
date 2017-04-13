package pw.stego.network.steganography;

import pw.stego.network.container.Container;
import pw.stego.network.container.FileBytesContainer;
import pw.stego.network.container.Sign;

import java.io.IOException;

/**
 * Not really a steganography
 * Created by lina on 26.12.16.
 */
public class NoStego implements Steganography {
    @Override
    public boolean isSignedWith(Sign sign, Container container) {
        return true;
    }

    @Override
    public boolean isAcceptableContainer(Container container) {
        return true;
    }

    @Override
    public byte[] extract(Sign sign, Container container) throws IOException {
        return container.getBytes();
    }

    @Override
    public FileBytesContainer insert(Sign sign, byte[] message, Container container) throws IOException {
        return newContainer(message);
    }

    @Override
    public String[] getOptimalContainerParams(byte[] message) {
        return new String[]{String.valueOf(message.length)};
    }

    @Override
    public FileBytesContainer newContainer(byte[] container) throws IOException {
        return new FileBytesContainer(container);
    }
}
