package mc.icecube.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static mc.icecube.framework.utils.ByteStreamHelper.readBytes;
import static mc.icecube.framework.utils.LittleEndian.fromLE;
import static mc.icecube.framework.utils.LittleEndian.toLE;

public class LittleEndianTypes {
    public static String readStringAscii(DataInputStream stream, int lenBytes) throws IOException {
        byte[] lenBytesArray = new byte[lenBytes];
        stream.readFully(lenBytesArray);
        int length = (int) fromLE(lenBytesArray);
        byte[] strBytes = new byte[length];
        stream.readFully(strBytes);
        return new String(strBytes, StandardCharsets.US_ASCII);
    }
    public static int readShortLE(DataInputStream stream) throws IOException {
        return (int) fromLE(readBytes(stream, 2));
    }
    public static int readIntLE(DataInputStream stream) throws IOException {
        return (int) fromLE(readBytes(stream, 4));
    }
    public static void writeStringAscii(ByteArrayOutputStream stream, String str, int lenBytes) throws IOException {
        byte[] strBytes = str.getBytes(StandardCharsets.US_ASCII);
        byte[] len = toLE(strBytes.length, lenBytes);
        stream.write(len);
        stream.write(strBytes);
    }
    public static String readStringUTF8(DataInputStream stream) throws IOException {
        int length = readIntLE(stream);
        byte[] strBytes = new byte[length];
        stream.readFully(strBytes);
        return new String(strBytes, StandardCharsets.UTF_8);
    }
    public static void writeStringUTF8(ByteArrayOutputStream stream, String str, int lenBytes) throws IOException {
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] len = toLE(strBytes.length, lenBytes);
        stream.write(len);
        stream.write(strBytes);
    }

}
