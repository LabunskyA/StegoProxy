package pw.stego.network.steganography;

import pw.barcodestego.coders.Encoder;
import pw.barcodestego.util.StegoImage;
import pw.stego.network.container.Container;
import pw.stego.network.container.LosslessImageContainer;
import pw.stego.network.container.Sign;
import pw.stego.network.container.util.FileMagic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * LSB steganography algorithm based on QR codes
 * Implementation uses Stego sign model - it
 * Created by lina on 30.01.17.
 */
public class BarcodeStego implements Steganography<BufferedImage> {
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
    public boolean isSignedWith(Sign sign, Container container) {
        return true;
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
        return pw.barcodestego.BarcodeStego.decode(sign.getValue(), extractImage(container));
    }

    @Override
    public Container<BufferedImage> insert(Sign sign, byte[] message, Container container) throws IOException {
        try {
            return new LosslessImageContainer(
                    pw.barcodestego.BarcodeStego.encode(
                            sign.getValue(), message, extractImage(container)
                    ).toBufferedImage()
            );
        } catch (Encoder.WrongSizeException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public String[] getOptimalContainerParams(byte[] message) {
        return new String[]{};
    }

    @Override
    public LosslessImageContainer newContainer(byte[] container) throws IOException {
        return new LosslessImageContainer(container);
    }
}
