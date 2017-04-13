package pw.stego.network.proxy.tunnel;

import pw.stego.network.container.Container;
import pw.stego.network.container.Sign;
import pw.stego.network.steganography.Steganography;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Tunnel through socket
 * Created by lina on 21.12.16.
 */
public class SocketTunnel<S extends Steganography> extends Tunnel<Socket, S> {
    private final DataInputStream is;
    private final DataOutputStream os;

    public SocketTunnel(Sign sign, S algo, String ipOut, int portOut) throws IOException {
        super(sign, new Socket(ipOut, portOut), algo);
        connection.setSoTimeout(1000 * 60);

        is = new DataInputStream(connection.getInputStream());
        os = new DataOutputStream(connection.getOutputStream());
    }

    public SocketTunnel(Sign sign, S algo, Socket socket) throws IOException {
        super(sign, socket, algo);

        is = new DataInputStream(connection.getInputStream());
        os = new DataOutputStream(connection.getOutputStream());
    }

    @Override
    public void send(byte[] data) throws IOException {
        Container container = algo.insert(sign, data, getNextContainer());
        byte[] stegoBytes = container.getBytes();

        os.writeInt(stegoBytes.length);
        os.write(stegoBytes);

        os.flush();
    }

    @Override
    public byte[] receive() throws IOException {
        byte[] data = new byte[is.readInt()];

        int i = 0, count, len = data.length;
        while ((count = is.read(data, i, len)) != -1) {
            i += count;
            if ((len -= count) == 0)
                break;
        }

        Container container = algo.newContainer(data);
        if (!algo.isSignedWith(sign, container))
            throw new IOException("Wrong signature");
        return algo.extract(sign, container);
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }

    @Override
    public String toString() {
        return connection.getInetAddress()+":"+connection.getPort();
    }
}
