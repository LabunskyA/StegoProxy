package pw.stego.network.container;

import java.util.Arrays;

/**
 * Signature wrapper
 * Created by lina on 07.10.16.
 */
public class Sign {
    private final byte[] value;
    private final byte[] copy;

    private final int hash;

    public Sign(byte[] value) {
        this.value = new byte[value.length];
        this.copy = new byte[value.length];

        System.arraycopy(value, 0, this.value, 0, value.length);
        System.arraycopy(value, 0, this.copy, 0, value.length);

        hash = Arrays.hashCode(value);
    }

    /**
     * @return copy of sign bytes (for security reasons)
     */
    public byte[] getValue() {
        if (Arrays.hashCode(copy) == hash)
            return copy;

        System.arraycopy(value, 0, copy, 0, value.length);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sign) && !(o instanceof byte[]))
            return false;

        byte[] oValue;
        if (o instanceof Sign)
            oValue = ((Sign) o).value;
        else oValue = (byte[]) o;

        if (value.length != oValue.length)
            return false;

        for (int i = 0; i < value.length; i++)
            if (value[i] != oValue[i])
                return false;

        return true;
    }
}
