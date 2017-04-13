package pw.stego.network.steganography;

import pw.stego.coders.KeyNotFoundException;
import pw.stego.network.container.Container;
import pw.stego.network.container.LosslessImageContainer;
import pw.stego.network.container.Sign;
import pw.stego.network.container.util.FileMagic;
import pw.stego.util.Patterns;
import pw.stego.util.StegoImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Stego lib wrapper
 * Created by lina on 01.01.17.
 */
public class Stego implements Steganography<BufferedImage> {
    private Random r = new Random();

    private StegoImage extractImage(Container container) throws IOException {
        if (container instanceof LosslessImageContainer)
            return new StegoImage((BufferedImage) container.asObject());

        if (container.asObject() instanceof BufferedImage)
            return new StegoImage((BufferedImage) container.asObject());

        try (ByteArrayInputStream in = new ByteArrayInputStream(container.getBytes())) {
            return new StegoImage(ImageIO.read(in));
        }
    }

    @Override
    public boolean isSignedWith(Sign sign, Container container) throws IOException {
        return pw.Stego.tryKey(sign.getValue(), extractImage(container));
    }

    @Override
    public boolean isAcceptableContainer(Container container) throws IOException {
        return container instanceof LosslessImageContainer ||
                container.asObject() instanceof StegoImage ||
                container.asObject() instanceof BufferedImage ||
                FileMagic.isLosslessImage(container.getBytes());

    }

    @Override
    public byte[] extract(Sign sign, Container container) throws IOException {
        try {
            return pw.Stego.decode(sign.getValue(), extractImage(container));
        } catch (KeyNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public Container<BufferedImage> insert(Sign sign, byte[] message, Container container) throws IOException {
        return new LosslessImageContainer(
                pw.Stego.encode(
                        sign.getValue(), message,
                        Patterns.Type.SIMPLE, extractImage(container)
                ).toBufferedImage()
        );
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

        return new String[]{String.valueOf(dim), String.valueOf(blocks / dim + blocks % dim + r.nextInt(dim) + 1)};
    }

    @Override
    public Container<BufferedImage> newContainer(byte[] container) throws IOException {
        return new LosslessImageContainer(container);
    }
}
