package mc.icecube.framework.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteStreamHelper {
    // Just additional class because DataInputStream.readFully in Android implemented only at 13+
    public static byte[] readBytes(DataInputStream dis, int numBytes) throws IOException {
        byte[] bytes = new byte[numBytes];
        dis.readFully(bytes);
        return bytes;
    }

    // We don't need any Little Endian or Big Endian for ONLY ONE BYTE XD
    public static boolean readBool(DataInputStream stream) throws IOException {
        return stream.readByte() != 0;
    }

    // one byte[] array equal to another byte[] array
    // a = b check
    public static void assertEqual(byte[] a, byte[] b) throws Exception {
        if (a.length != b.length) {
            throw new Exception("First byte[] array length not equals to second byte[] array");
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                throw new Exception("Error in assertEqual... \nExpected: " + Arrays.toString(a) + "\nBut got: " + Arrays.toString(b));
            }
        }
    }
}
