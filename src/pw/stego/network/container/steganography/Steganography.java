package pw.stego.network.container.steganography;

import pw.stego.network.container.Sign;

import java.io.File;
import java.io.IOException;

/**
 * Steganography algorithms base interface
 * Implement it if you want to use your algorithm in network
 * Created by lina on 07.11.16.
 */
public interface Steganography {
    /**
     * Check if container signed with provided sign
     * @param sign to check
     * @param container which could be signed
     * @return true if signature found
     */
    Boolean isSignedWith(Sign sign, File container);

    /**
     * Checks if it can work with provided file
     * @param container to check
     * @return true if algorithm can work with provided container
     */
    Boolean isAcceptableContainer(File container);

    /**
     * Extracts message from container with provided signature
     * @param sign is signature to use
     * @param container to extract frm
     * @return extracted message
     */
    byte[] extract(Sign sign, File container) throws IOException;

    /**
     * Inserts message in container with provided signature
     * @param sign to sign
     * @param message to insert
     * @param container to insert to
     * @return container
     */
    File insert(Sign sign, byte[] message, File container) throws IOException;
}
