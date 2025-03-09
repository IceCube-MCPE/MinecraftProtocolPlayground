package mc.icecube.framework.nethernet.codec;

import mc.icecube.framework.nethernet.discovery.*;
import mc.icecube.framework.nethernet.encryption.*;
import mc.icecube.framework.utils.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class NetherNetCodec {
    private static String TAG = NetherNetCodec.class.getSimpleName();

    public static byte[] nethernetEncode(Object packet) {
        ByteArrayOutputStream p = new ByteArrayOutputStream();
        int type = determinePacketType(packet);
        try {
            // Запись типа пакета и ID
            p.write(LittleEndian.toLE(type, 2));
            p.write(LittleEndian.toLE(((DiscoveryPacket) packet).getId(), 8));
            // works too new byte[8] for padding ?
            p.write(LittleEndian.toLE(0, 8)); // padding; I think we can just write zero 8 times, but it doesn't matter
            if (packet instanceof DiscoveryRequestPacket) {
                // Нет дополнительной информации для кодирования
                // this packet contains only id
                // I think I can remove this if method, but to explain how the protocol works, let it remain
            } else if (packet instanceof DiscoveryResponsePacket) {
                encodeDiscoveryResponsePacket(p, (DiscoveryResponsePacket) packet);
            } else if (packet instanceof DiscoveryMessagePacket) {
                encodeDiscoveryMessagePacket(p, (DiscoveryMessagePacket) packet);
            }
            // Запись длины пакета, он идёт самым первым
            int length = p.size() + 2;
            byte[] pBytes = p.toByteArray();
            ByteArrayOutputStream encoded = new ByteArrayOutputStream();
            encoded.write(length & 0xFF);
            encoded.write((length >> 8) & 0xFF);
            encoded.write(pBytes);
            // Добавление контрольной суммы и шифрование
            byte[] encodedBytes = encoded.toByteArray();
            byte[] checksum = DecryptorCipher.checksum(encodedBytes);
            byte[] encrypted = DecryptorCipher.encrypt(encodedBytes);
            // Объединение контрольной суммы и зашифрованных данных
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            result.write(checksum);
            result.write(encrypted);
            return result.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static int determinePacketType(Object packet) {
        // pretty simple, yes?
        if (packet instanceof DiscoveryRequestPacket) {
            return 0;
        } else if (packet instanceof DiscoveryResponsePacket) {
            return 1;
        } else if (packet instanceof DiscoveryMessagePacket) {
            return 2;
        } else {
            throw new IllegalArgumentException("Unknown packet type for Object: " + packet.getClass().getSimpleName());
        }
    }
    private static void encodeDiscoveryResponsePacket(ByteArrayOutputStream p, DiscoveryResponsePacket packet) throws IOException {
        ByteArrayOutputStream h = new ByteArrayOutputStream();
        h.write(LittleEndian.toLE(packet.version, 1));
        VarIntsTypes.writeVarStringUTF8(h, packet.server_name);
        VarIntsTypes.writeVarStringUTF8(h, packet.level_name);
        h.write(LittleEndian.toLE(packet.game_type, 4));
        h.write(LittleEndian.toLE(packet.player_count, 4));
        h.write(LittleEndian.toLE(packet.max_player_count, 4));
        h.write(LittleEndian.toLE(packet.is_editor_world ? 1 : 0, 1));
        if(packet.version > 2) {
            h.write(LittleEndian.toLE(packet.hardcore ? 1 : 0, 1));
        }
        h.write(LittleEndian.toLE(packet.transport_layer, 4));
        String hexString = Hexer.bytesToHex(h.toByteArray());
        LittleEndianTypes.writeStringUTF8(p, hexString);
    }
    private static void encodeDiscoveryMessagePacket(ByteArrayOutputStream p, DiscoveryMessagePacket packet) throws IOException {
        p.write(LittleEndian.toLE(packet.recipient_id, 8));
        LittleEndianTypes.writeStringUTF8(p, packet.data);
    }

    public static DiscoveryPacket nethernetDecode(byte[] packet) {
        try {
            if(packet == null) {
                SimpleLog.log(TAG, "We got bytes to decode null. Returning null");
                return null;
            }
            byte[] checksum = Arrays.copyOfRange(packet, 0, 32);
            byte[] encrypted = Arrays.copyOfRange(packet, 32, packet.length);
            byte[] decrypted = DecryptorCipher.decrypt(encrypted);
            if(decrypted == null) {
                SimpleLog.log(TAG, "We can't decrypt packet after readed checksum. Returning null");
                return null;
            }
            ByteArrayOutputStream dec = new ByteArrayOutputStream();
            dec.write(decrypted);
            if (!Arrays.equals(checksum, DecryptorCipher.checksum(decrypted))) {
                SimpleLog.log(TAG, "Saved checksum not equals decrypted checksum. Returning null");
                return null;
            }
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(decrypted));
            int packetLength = LittleEndianTypes.readShortLE(dis);
            int type = LittleEndianTypes.readShortLE(dis);
            long senderId = dis.readLong();
            long padding = dis.readLong();
            if (type == 0) {
                return new DiscoveryRequestPacket(senderId);
            } else if (type == 1) {
                String hexData = LittleEndianTypes.readStringUTF8(dis);
                byte[] dataBytes = Hexer.hexToBytes(hexData);
                DataInputStream dataDis = new DataInputStream(new ByteArrayInputStream(dataBytes));
                int version = dataDis.readUnsignedByte();
                String serverName = VarIntsTypes.readVarStringUTF8(dataDis);
                String levelName = VarIntsTypes.readVarStringUTF8(dataDis);
                int gameType = LittleEndianTypes.readIntLE(dataDis);
                int playerCount = LittleEndianTypes.readIntLE(dataDis);
                int maxPlayerCount = LittleEndianTypes.readIntLE(dataDis);
                boolean isEditor = ByteStreamHelper.readBool(dataDis);
                if(version > 2) {
                    boolean hardcore = ByteStreamHelper.readBool(dataDis);
                    int transportLayer = LittleEndianTypes.readIntLE(dataDis);
                    return new DiscoveryResponsePacket(senderId, version, serverName, levelName, gameType, playerCount, maxPlayerCount, isEditor, hardcore, transportLayer);
                }
                int transportLayer = LittleEndianTypes.readIntLE(dataDis);
                // return class without hardcore, btw we return with Response with hardcore in if above
                return new DiscoveryResponsePacket(senderId, version, serverName, levelName, gameType, playerCount, maxPlayerCount, isEditor, transportLayer);
            } else if (type == 2) {
                long recipientId = dis.readLong();
                String data = LittleEndianTypes.readStringUTF8(dis);
                return new DiscoveryMessagePacket(senderId, recipientId, data);
            } else {
                throw new RuntimeException("Invalid packet type: " + type);
            }
        } catch (Exception e) {
            SimpleLog.log(TAG, "Returning null. Reached error: " + e);
            return null;
        }
    }
}