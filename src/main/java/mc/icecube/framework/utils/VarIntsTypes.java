package mc.icecube.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class VarIntsTypes {
    // We read length prefix (VarInt) and read bytes based on prefix to get string
    // Unsigned VarInt because we cannot have length less than zero yes?
    public static void writeVarStringUTF8(ByteArrayOutputStream stream, String str) throws IOException {
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        VarInts.writeUnsignedInt(stream, strBytes.length);
        stream.write(strBytes);
    }
    public static String readVarStringUTF8(DataInputStream stream) throws IOException {
        int length = VarInts.readUnsignedInt(stream);
        byte[] strBytes = new byte[length];
        stream.readFully(strBytes);
        return new String(strBytes, StandardCharsets.UTF_8);
    }
}
