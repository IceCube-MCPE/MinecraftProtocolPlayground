package mc.icecube.framework.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LittleEndian {
    // Преобразование в little-endian
    public static byte[] toLE(long n, int c) {
        ByteBuffer buffer = ByteBuffer.allocate(c).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < c; i++) {
            buffer.put((byte) ((n >> (8 * i)) & 0xFF));
        }
        return buffer.array();
    }
    // Преобразование из little-endian
    static long fromLE(byte[] b) {
        long result = 0;
        for (int i = 0; i < b.length; i++) {
            result |= ((long) b[i] & 0xFF) << (8 * i);
        }
        return result;
    }
}
