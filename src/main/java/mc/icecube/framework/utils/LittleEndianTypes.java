package mc.icecube.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static mc.icecube.framework.utils.ByteStreamHelper.readBytes;
import static mc.icecube.framework.utils.LittleEndian.fromLE;
import static mc.icecube.framework.utils.LittleEndian.toLE;

public class LittleEndianTypes {
    // uint16
    public static int readShortLE(DataInputStream stream) throws IOException {
        return (int) fromLE(readBytes(stream, 2));
    }
    // uint24
    public static int readTriadLE(DataInputStream dataInputStream) throws IOException {
        return (int) fromLE(readBytes(dataInputStream, 3));
    }
    // uint32
    public static int readIntLE(DataInputStream stream) throws IOException {
        return (int) fromLE(readBytes(stream, 4));
    }
    // write uint32
    public static void writeIntLE(ByteArrayOutputStream stream, int intToWrite) throws IOException {
        stream.write(toLE(intToWrite, 4));
    }
    // length prefixed string: length (uint32) + bytes
    // read bytes by readed length and convert it to string
    // if length 1 you read 1 byte more after length and covert this byte to string
    public static String readStringUTF8(DataInputStream stream) throws IOException {
        int length = readIntLE(stream);
        byte[] strBytes = new byte[length];
        stream.readFully(strBytes);
        return new String(strBytes, StandardCharsets.UTF_8);
    }
    // length prefixed string
    // same thing, we encode string length + string (in bytes)
    public static void writeStringUTF8(ByteArrayOutputStream stream, String str) throws IOException {
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        writeIntLE(stream, str.length());
        stream.write(strBytes);
    }
    // float (8 bytes)
    public static float readFloatLE(DataInputStream dataInputStream) throws IOException {
        return (float) fromLE(readBytes(dataInputStream, 4));
    }
    // double (8 bytes)
    public static double readDoubleLE(DataInputStream dataInputStream) throws IOException {
        return (double) fromLE(readBytes(dataInputStream, 8));
    }

}
