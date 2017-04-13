package pw.stego.network.steganography;

import pw.stego.network.container.Container;
import pw.stego.network.container.Sign;

import java.io.IOException;

/**
 * Steganography algorithms base interface
 * Implement it if you want to use your algorithm in network
 *
 * @param <T> is class of object you want to use as container.
 * You need to implement class for it, though. If you don't want to do it,
 * you can not use it and go for plain bytes container API.
 */
public interface Steganography<T> {
    /**
     * Check if container signed with some signature
     * @param sign to check
     * @param container which could be signed
     * @return true if signature found
     */
    boolean isSignedWith(Sign sign, Container container) throws IOException;

    /**
     * Checks if it can work with provided container
     * @param container to check
     * @return can algorithm work with provided container or not
     * @apiNote You should use both
     */
    boolean isAcceptableContainer(Container container) throws IOException;

    /**
     * Extracts message from container with provided signature
     * @param sign is signature to use
     * @param container to extract frm
     * @return extracted message
     */
    byte[] extract(Sign sign, Container container) throws IOException;

    /**
     * Inserts message in container with provided signature and returns it (for your comfort)
     * Note, that there is no standard, is function should modificate container and return it
     * or it should return another new one. Basically, container parameter could be ignored.
     *
     * @param sign to sign
     * @param message to insert
     * @param container to insert to
     * @return container
     */
    Container<T> insert(Sign sign, byte[] message, Container container) throws IOException;

    /**
     * Returns optimal container parameters which will be used to create container with ContainerFactory
     *
     * @param message to insert into container
     * @return String array or parameters. Strings class is used because it can really represent anything.
     * @apiNote Please notice, that array format should be associated with at least one ContainerFactory.
     *
     * @apiNote If you don't want to implement it or you're not planning to use special parameters,
     * just return empty array (new String[0]) from this function.
     */
    String[] getOptimalContainerParams(byte[] message);

    /**
     * Creates new compatible with algorithm container from full container file in byte array.
     * This method will be called from receiver class when file is accepted via network.
     *
     * @param container full file in byte array
     * @return compatible container from bytes
     * @throws IOException if provided container bytes are not valid.
     */
    Container<T> newContainer(byte[] container) throws IOException;
}
