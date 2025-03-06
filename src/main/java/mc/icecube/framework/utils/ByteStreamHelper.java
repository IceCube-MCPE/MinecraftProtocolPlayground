package mc.icecube.framework.utils;

import java.io.DataInputStream;
import java.io.IOException;

public class ByteStreamHelper {
    // Just additional class because DataInputStream.readFully in Android implemented only at 13+
    static byte[] readBytes(DataInputStream dis, int numBytes) throws IOException {
        byte[] bytes = new byte[numBytes];
        dis.readFully(bytes);
        return bytes;
    }

    // We don't need any Little Endian or Big Endian for ONLY ONE BYTE XD
    public static boolean readBool(DataInputStream stream) throws IOException {
        return stream.readByte() != 0;
    }
}
