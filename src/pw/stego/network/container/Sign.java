package pw.stego.network.container;

import java.util.Arrays;

/**
 * Signature wrapper
 * Created by lina on 07.10.16.
 */
public class Sign {
    private final byte[] value;

    public Sign(byte[] value) {
        this.value = new byte[value.length];
        System.arraycopy(value, 0, this.value, 0, value.length);
    }

    /**
     * @return copy of sign bytes (for security reasons)
     */
    public byte[] getValue() {
        return Arrays.copyOf(value, value.length);
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
