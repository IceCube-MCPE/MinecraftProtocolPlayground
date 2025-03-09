package mc.icecube.framework.raknet.codec;

import mc.icecube.framework.raknet.discovery.*;
import mc.icecube.framework.raknet.framesetpacket.Frame;
import mc.icecube.framework.raknet.gamepacket.GamePacket;
import mc.icecube.framework.utils.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;


public class RakNetCodec {

    private boolean security;
    private KeyPair generatedPrivate;
    private int protocolVersion, protocolVersionRakNet;
    private int minPacketCompression;

    public RakNetCodec() {
    }


    protected static String readAddress(DataInputStream dataInputStream) throws IOException {
        int addressVersion = dataInputStream.readUnsignedByte();
        if (addressVersion == 4) {
            int[] ipBytes = new int[4];
            for (int i = 0; i < 4; i++) {
                ipBytes[i] =~ dataInputStream.readUnsignedByte() & 0xFF;
            }
            int port = dataInputStream.readUnsignedShort();
            return String.format("%d.%d.%d.%d:%d", ipBytes[0], ipBytes[1], ipBytes[2], ipBytes[3], port);
        } else if (addressVersion == 6) {
            // don't know about IPv6 (not tested)
            dataInputStream.readUnsignedShort();
            int port = dataInputStream.readUnsignedShort(); // Чтение порта
            byte[] flowInfoBytes = new byte[4];
            dataInputStream.readFully(flowInfoBytes);
            byte[] addressBytes = new byte[16];
            dataInputStream.readFully(addressBytes);
            byte[] scopeIdBytes = new byte[4];
            dataInputStream.readFully(scopeIdBytes);
            try {
                InetAddress inetAddress = InetAddress.getByAddress(addressBytes);
                return "[" + inetAddress.getHostAddress() + "]:" + port;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            return "Error in decoding.";
        }
        return "Error in decoding.";
    }

    public byte[] raknetEncode(Packet packet) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            if (packet instanceof UnconnectedPing unconnectedPing) {
                dataOutputStream.write(0x01);
                dataOutputStream.writeLong(unconnectedPing.uptime);
                dataOutputStream.write(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT);
                dataOutputStream.writeLong(unconnectedPing.id);
            } else if (packet instanceof UnconnectedPong unconnectedPong) {
                dataOutputStream.write(0x1c);
                dataOutputStream.write(BigEndian.toBE(unconnectedPong.uptime, 8));
                dataOutputStream.write(BigEndian.toBE(unconnectedPong.id, 8));
                dataOutputStream.write(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT);
                String responseS = String.join(";",
                        unconnectedPong.edition,
                        unconnectedPong.serverName,
                        unconnectedPong.protocolVersion,
                        unconnectedPong.versionNumber,
                        unconnectedPong.playerCount,
                        unconnectedPong.maxPlayerCount,
                        unconnectedPong.serverId,
                        unconnectedPong.levelName,
                        unconnectedPong.gameType,
                        unconnectedPong.gameTypeNumeric,
                        unconnectedPong.portv4,
                        unconnectedPong.portv6
                ).replaceAll(";{2,}$", ";");
                dataOutputStream.write(BigEndian.toBE(responseS.length(), 2));
                dataOutputStream.write(responseS.getBytes());
            }
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public int getPacketType(byte[] packet) {
        try (DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(packet))) {
            return dataInputStream.readUnsignedByte();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Packet raknetDecode(byte[] packet) {
        try (DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(packet))) {
            long ptype = dataInputStream.readUnsignedByte();
            if (ptype == 0x01 || ptype == 0x02) {
                long uptime = dataInputStream.readLong();
                ByteStreamHelper.assertEqual(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, ByteStreamHelper.readBytes(dataInputStream, 16));
                long uid = dataInputStream.readLong();
                return new UnconnectedPing(uid, uptime);
            } else if (ptype == 0x1c) {
                long uptime = dataInputStream.readLong();
                long uid = dataInputStream.readLong();
                ByteStreamHelper.assertEqual(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, ByteStreamHelper.readBytes(dataInputStream, 16));
                short strlen = dataInputStream.readShort();
                String s = new String(ByteStreamHelper.readBytes(dataInputStream, strlen));
                String[] splitS = s.split(";", -1);

                return new UnconnectedPong(uid, uptime, RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, splitS.length > 0 ? splitS[0] : "", splitS.length > 1 ? splitS[1] : "", splitS.length > 2 ? splitS[2] : "", splitS.length > 3 ? splitS[3] : "", splitS.length > 4 ? splitS[4] : "", splitS.length > 5 ? splitS[5] : "", splitS.length > 6 ? splitS[6] : "", splitS.length > 7 ? splitS[7] : "", splitS.length > 8 ? splitS[8] : "", splitS.length > 9 ? splitS[9] : "", splitS.length > 10 ? splitS[10] : "", splitS.length > 11 ? splitS[11] : "");
            } else if (ptype == 0xfe) {
                return handleGamePacket(ptype, packet);
            } else if (ptype == 0x00) {
                long time = dataInputStream.readLong();
                return new ConnectedPing(ptype, time);
            } else if (ptype == 0x03) {
                long pingTime = dataInputStream.readLong();
                long pongTime = dataInputStream.readLong();
                return new ConnectedPong(ptype, pingTime, pongTime);
            } else if (ptype == 0x04) {
                return new LostConnection(ptype, packet);
            } else if (ptype == 0x05) {
                ByteStreamHelper.assertEqual(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, ByteStreamHelper.readBytes(dataInputStream, 16));
                int protocolVersion = dataInputStream.readUnsignedByte();
                int mtu = dataInputStream.available() + 46;
                return new OpenConnectionRequest1(ptype, packet, RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, protocolVersion, mtu);
            } else if (ptype == 0x06) {
                ByteStreamHelper.assertEqual(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, ByteStreamHelper.readBytes(dataInputStream, 16));
                long serverGUID = dataInputStream.readLong();
                security = ByteStreamHelper.readBool(dataInputStream);
                int cookie = 0;
                if (security) {
                    cookie = BigEndianTypes.readIntBE(dataInputStream);
                }
                int mtuSize = BigEndianTypes.readShortBE(dataInputStream);
                return new OpenConnectionReply1(ptype, packet, RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, serverGUID, security, cookie, mtuSize);
            } else if (ptype == 0x07) {
                ByteStreamHelper.assertEqual(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, ByteStreamHelper.readBytes(dataInputStream, 16));
                int serverCookie = 0;
                boolean supportsSecurity = false;
                // if security was enabled in OpenConnectionReply1 so we read serverCookie
                if (security) {
                    serverCookie = BigEndianTypes.readIntBE(dataInputStream);
                    supportsSecurity = ByteStreamHelper.readBool(dataInputStream);
                }
                String serverAddress = readAddress(dataInputStream);
                int mtuSize = BigEndianTypes.readShortBE(dataInputStream);
                long clientGUID = dataInputStream.readLong();
                return new OpenConnectionRequest2(ptype, packet, serverCookie, supportsSecurity, serverAddress, mtuSize, clientGUID);
            } else if (ptype == 0x08) {
                ByteStreamHelper.assertEqual(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, ByteStreamHelper.readBytes(dataInputStream, 16));
                long serverGUID = dataInputStream.readLong();
                String address = readAddress(dataInputStream);
                int mtu = BigEndianTypes.readShortBE(dataInputStream);
                boolean encryption = ByteStreamHelper.readBool(dataInputStream);
                return new OpenConnectionReply2(ptype, packet, serverGUID, address, mtu, encryption);
            } else if (ptype == 0x09) {
                long clientGUID = dataInputStream.readLong();
                long requestTimestamp = dataInputStream.readLong();
                boolean useSecurity = ByteStreamHelper.readBool(dataInputStream);
                return new ConnectionRequest(ptype, packet, clientGUID, requestTimestamp, useSecurity);
            } else if (ptype == 0x10) {
                String clientAddress = readAddress(dataInputStream);
                int systemIndex = BigEndianTypes.readShortBE(dataInputStream);
                String[] systemAddresses = new String[10];
                // create 10 empty array
                // like this: ['', '', '', '', '', '', '', '', '', '']
                // read one address and put one to our array until it 10
                for (int i = 0; i < 10; i++) {
                    systemAddresses[i] = readAddress(dataInputStream);
                }
                long pingTime = dataInputStream.readLong();
                long pongTime = dataInputStream.readLong();
                return new ConnectionRequestAccepted(ptype, packet, clientAddress, systemIndex, systemAddresses, pingTime, pongTime);
            } else if (ptype == 0x13) {
                String serverAddress = readAddress(dataInputStream);
                String[] systemAddresses = new String[20];
                for (int i = 0; i < 20; i++) {
                    systemAddresses[i] = readAddress(dataInputStream);
                }
                // same as above, but 20
                long requestTimestamp = dataInputStream.readLong();
                long replyTimestamp = dataInputStream.readLong();
                return new NewIncomingConnection(ptype, serverAddress, systemAddresses, requestTimestamp, replyTimestamp);
            } else if (ptype == 0x15) {
                return new Disconnect(ptype, packet);
            } else if (ptype == 0x17) {
                ByteStreamHelper.assertEqual(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, ByteStreamHelper.readBytes(dataInputStream, 16));
                long serverGUID = dataInputStream.readLong();
                return new ConnectionBanned(ptype, packet, RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, serverGUID);
            } else if (ptype == 0x19) {
                int protocol = dataInputStream.readUnsignedByte();
                ByteStreamHelper.assertEqual(RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, ByteStreamHelper.readBytes(dataInputStream, 16));
                long serverGUID = dataInputStream.readLong();
                return new IncompatibleProtocol(ptype, packet, protocol, RakNetConstans.RAKNET_MAGIC_FIRST_CONSTANT, serverGUID);
            } else if (ptype >= 0x80 && ptype <= 0x8D) {
                int sequenceNumber = LittleEndianTypes.readTriadLE(dataInputStream);
                ArrayList<Frame> frames = new ArrayList<>();
                while (dataInputStream.available() > 0) {
                    // Frame
                    int flags = dataInputStream.readUnsignedByte();
                    int reliability = (flags & 0xe0) >> 5;
                    boolean isFragmented = (flags & 0x10) > 0;
                    // readUnsignedShortBE(dataInputStream)  in bits, so to get bytes we >> 3 ( bits / 2^3 = bits / 8) => bytes
                    // BigEndianTypes.readShortBE(dataInputStream) => bits
                    // to read bytes we need bytes. big logic, yeah?
                    int streamSize = BigEndianTypes.readShortBE(dataInputStream) >> 3;
                    int reliableFrameIndex = 0;
                    int sequencedFrameIndex = 0;
                    int orderedFrameIndex = 0;
                    int orderChannel = 0;
                    int compoundSize = 0;
                    int compoundID = 0;
                    int compoundEntryIndex = 0;
                    if (ReliabilityHelper.isReliable(reliability)) {
                        reliableFrameIndex = LittleEndianTypes.readTriadLE(dataInputStream);
                    }
                    if (ReliabilityHelper.isSequenced(reliability)) {
                        sequencedFrameIndex = LittleEndianTypes.readTriadLE(dataInputStream);
                    }
                    if (ReliabilityHelper.isOrdered(reliability)) {
                        orderedFrameIndex = LittleEndianTypes.readTriadLE(dataInputStream);
                        orderChannel = dataInputStream.readUnsignedByte();
                    }
                    if (isFragmented) {
                        compoundSize = BigEndianTypes.readIntBE(dataInputStream);
                        compoundID = BigEndianTypes.readShortBE(dataInputStream);
                        compoundEntryIndex = BigEndianTypes.readIntBE(dataInputStream);
                    }
                    byte[] body = new byte[streamSize];
                    dataInputStream.readFully(body);
                    // as I said body it's just bytes, but I want convert it to ByteBuffer
                    ByteBuffer stream = ByteBuffer.wrap(body);
                    Frame frame = new Frame(reliability, packet, isFragmented, reliableFrameIndex, sequencedFrameIndex, orderedFrameIndex, orderChannel, compoundSize, compoundID, compoundEntryIndex, stream);
                    frames.add(frame);
                }
                return new FrameSetPacket(ptype, sequenceNumber, frames);
            } else if (ptype == 0xc0) {
                int recordCount = BigEndianTypes.readShortBE(dataInputStream);
                boolean singleSequenceNumber = ByteStreamHelper.readBool(dataInputStream);
                int sequenceNumber = 0;
                int startSequenceNumber = 0;
                int endSequenceNumber = 0;
                if (singleSequenceNumber) {
                    sequenceNumber = LittleEndianTypes.readTriadLE(dataInputStream);
                } else {
                    startSequenceNumber = LittleEndianTypes.readTriadLE(dataInputStream);
                    endSequenceNumber = LittleEndianTypes.readTriadLE(dataInputStream);
                }
                return new AckPacket(recordCount, singleSequenceNumber, sequenceNumber, startSequenceNumber, endSequenceNumber);
            } else if (ptype == 0xa0) {
                int recordCount = BigEndianTypes.readShortBE(dataInputStream);
                boolean singleSequenceNumber = ByteStreamHelper.readBool(dataInputStream);
                int sequenceNumber = 0;
                int startSequenceNumber = 0;
                int endSequenceNumber = 0;
                if (singleSequenceNumber) {
                    sequenceNumber = LittleEndianTypes.readTriadLE(dataInputStream);
                } else {
                    startSequenceNumber = LittleEndianTypes.readTriadLE(dataInputStream);
                    endSequenceNumber = LittleEndianTypes.readTriadLE(dataInputStream);
                }
                return new NAckPacket(recordCount, singleSequenceNumber, sequenceNumber, startSequenceNumber, endSequenceNumber);
            } else {
                // weird packets or unknown packet that should be documented and implemented here
                return new UnknownPacket(ptype, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decompress(byte[] compressedData) throws IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (Exception e) {
            throw new IOException("Failed to decompress data", e);
        } finally {
            inflater.end();
        }

        return outputStream.toByteArray();
    }

    public byte[] compressZlib(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            byteArrayOutputStream.write(buffer, 0, count);
        }
        deflater.end();

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] compressGZip(byte[] data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(data);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] decompressZlib(byte[] compressedData) throws IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[compressedData.length * 2];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (Exception e) {
            throw new IOException("Failed to decompress data", e);
        } finally {
            inflater.end();
        }

        return outputStream.toByteArray();
    }

    public byte[] decompressGZip(byte[] compressedData) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedData))) {
            byte[] decompress = new byte[1024];
            int len;
            while ((len = gis.read(decompress)) != -1) {
                baos.write(decompress, 0, len);
            }
        }
        return baos.toByteArray();
    }


   /* boolean handledNetworkSettings, handledAcceptNetworkSettings;
    private GamePacket checkForPreLogin(long id, byte[] buffer) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(buffer));
        assertEqual(new byte[]{(byte) 0xfe}, readBytes(dataInputStream, 1));
        int length = VarInts.readUnsignedInt(dataInputStream);
        int header = VarInts.readUnsignedInt(dataInputStream);
        int packetID = header & 0x3FF;
        int senderId = (header >> 10) & 0x03;
        int targetId = (header >> 12) & 0x03;
        Log.d("RakNetCodec", "Check Pre-login, Buffer Length  " + buffer.length +
                ", Readed size of packet: " + length +
                ", Calculated size (to check readed): " + (buffer.length - 2) +
                ", " + Arrays.toString(buffer) +
                ", stringBuffer: " + new String(buffer) +
                "\nPre-login Packet ID: " + packetID +
                "\nBuffer HEX: " + bytesToHex(buffer));

        switch (packetID) {
            case 143:
                int compressionThreshold = readUnsignedShortLE(dataInputStream);
                int compressionAlgorithm = readUnsignedShortLE(dataInputStream);
                boolean clientThrottleEnabled = readBool(dataInputStream);
                int clientThrottleThreshold = dataInputStream.readUnsignedByte();
                float clientThrottleScalar = readFloat(dataInputStream);
                Log.d("RakNetCodec", "After NetworkSettingsPacket, available bytes: " + dataInputStream.available());
                handledNetworkSettings = true;
                return new NetworkSettingsPacket(packetID, buffer, compressionThreshold, compressionAlgorithm, clientThrottleEnabled, clientThrottleThreshold, clientThrottleScalar);
            case 193:
                handledAcceptNetworkSettings = true;
                int ClientNetworkSettingsVersion = readUnsignedIntBE(dataInputStream);
                Log.d("RakNetCodec", "After RequestNetworkSettingsPacket, available bytes: " + dataInputStream.available());
                return new RequestNetworkSettingsPacket(packetID, buffer, ClientNetworkSettingsVersion);
        }
        return null;
    }

    private GamePacket checkForLogin(long id, byte[] buffer) throws Exception {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(buffer));
        long pingTime = dataInputStream.readLong();
        int packetID = dataInputStream.readUnsignedByte();
        Log.d("RakNetCodec", "Check Login, Buffer Length  " + buffer.length +
                ", pingTime: " + pingTime +
                ", packetID: " + packetID +
                ", " + Arrays.toString(buffer) +
                ", stringBuffer: " + new String(buffer) +
                "\nBuffer HEX: " + bytesToHex(buffer));

        switch (packetID) {
            case 1:
                int ClientNetworkVersion = readUnsignedIntBE(dataInputStream);
                byte[] connectionRequest = new byte[dataInputStream.available()];
                dataInputStream.readFully(connectionRequest);
                String connectionString = new String(connectionRequest);
                return new LoginPacket(packetID, buffer, ClientNetworkVersion, connectionString);
            case 2:
                int status = readUnsignedIntBE(dataInputStream);
                return new PlayStatusPacket(packetID, buffer, status);
            case 3:
                Log.d("RakNetCodec", "Before ServerToClientHandshakePacket, available bytes: " + dataInputStream.available());
                String webToken = readVarString(dataInputStream);
                Log.d("RakNetCodec", "After reading webToken: " + webToken);
                String salt = "";
                try {
                    SignedJWT signedJWT = SignedJWT.parse(webToken);
                    ECPublicKey publicKey = parsePublicKey((String) signedJWT.getHeader().toJSONObject().get("x5u"));
                    JWSVerifier verifier = new ECDSAVerifier(publicKey);
                    if (signedJWT.verify(verifier)) {
                        Log.d("RakNetCodec", "Signature is valid.");
                        Log.d("RakNetCodec", "Claims: " + signedJWT.getJWTClaimsSet().toJSONObject());
                        salt = (String) signedJWT.getJWTClaimsSet().toJSONObject().get("salt");
                        if(salt != null && !salt.isEmpty()) {
                            byte[] randomKeyToken = Base64.getDecoder().decode(salt);
                            generatedPrivate = getKeyPair();
                            SecretKey secretKey = Encryption.getSecretKey(generatedPrivate.getPrivate(), publicKey, randomKeyToken);
                            encryptionCodecEncrypt = Encryption.createCipher(true, true, secretKey);
                            encryptionCodecDecrypt = encryptionCodecEncrypt = Encryption.createCipher(true, false, secretKey);
                            encryption = true;
                        }
                    } else {
                        Log.d("RakNetCodec", "Signature is invalid.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // here need decryptor key for SHA-256 (Encryption class)
                return new ServerToClientHandshakePacket(packetID, buffer, webToken, salt);
            case 4:
                // dont know what it, maybe again some string
                Log.d("RakNetCodec", "Before ServerToClientHandshakePacket, available bytes: " + dataInputStream.available());
                String stringCheck = readVarString(dataInputStream);
                Log.d("RakNetCodec", "After reading stringCheck: " + stringCheck);

                return new ClientToServerHandshakePacket(packetID, buffer);
            case 6:
                Log.d("RakNetCodec", "Before ResourcePacksInfoPacket, available bytes: " + dataInputStream.available());
                boolean required = dataInputStream.readBoolean();
                Log.d("RakNetCodec", "After reading required: " + required);
                boolean hasAddonPacks = dataInputStream.readBoolean();
                Log.d("RakNetCodec", "After reading hasAddonPacks: " + hasAddonPacks);
                boolean hasScripts = dataInputStream.readBoolean();
                Log.d("RakNetCodec", "After reading hasScripts: " + hasScripts);
                boolean forceServerPacksEnabled = dataInputStream.readBoolean();
                Log.d("RakNetCodec", "After reading forceServerPacksEnabled: " + forceServerPacksEnabled);
                // List<TexturePackInfo> texturePackInfos = readTexturePacks(dataInputStream);
                return new ResourcePacksInfoPacket(packetID, buffer, required, hasAddonPacks, hasScripts, forceServerPacksEnabled);
            case 7:
                return new ResourcePackStackPacket(packetID, buffer);
            case 8:
                return new ResourcePackClientResponsePacket(packetID, buffer);
        }
        return null;
    }



    private KeyPair getKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(new ECGenParameterSpec("secp384r1"));
        return keyPairGenerator.generateKeyPair();
    }

    private ECPublicKey parsePublicKey(String keyStr) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(keyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return (ECPublicKey) keyFactory.generatePublic(keySpec);
    }
    */


    private GamePacket handleGamePacket(long id, byte[] buffer) throws Exception {
        // in dev.
      /*  DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(buffer));
        assertEqual(new byte[]{(byte) 0xfe}, readBytes(dataInputStream, 1));
        // честно я хер его ебу чё вообще он отправляет.
        if(encryptionCodecDecrypt != null) {
            byte[] bufferDecrypted = encryptionCodecDecrypt.doFinal(Arrays.copyOfRange(buffer, 1, buffer.length));
            if(bufferDecrypted != null) {
                Log.d("RakNetCodec", "Encrypted buffer: " + Arrays.toString(buffer) + "\nDecrypted buffer: " + Arrays.toString(bufferDecrypted));
                buffer = bufferDecrypted;
            }
            // skip 8 bytes => long (maybe it's padding? dont know.)
            dataInputStream.readLong();
        }

        GamePacket preLogin = checkForPreLogin(id, buffer);
        if(preLogin != null) {
            return preLogin;
        }
        GamePacket login = checkForLogin(id, buffer);
        if(login != null) {
            return login;
        }







        Log.d("RakNetCodec", "GamePacket Length  " + buffer.length + ", " + Arrays.toString(buffer) + ", stringBuffer: " + new String(buffer));

        //int totalLength = VarInts.readUnsignedInt(dataInputStream);

        int header = VarInts.readUnsignedInt(dataInputStream);
        int packetID = header & 0x3FF;
        int senderId = (header >> 10) & 0x03;
        int targetId = (header >> 12) & 0x03;

        // ибо я для начала его закончу ок. а то я свихнусь нахуй
        Log.d("RakNetCodec", "totalLength: " + header + " header: " + header + " packetId: " + packetID + " senderId: " + senderId + " targetId: " + targetId);
        switch (packetID) {
            case 5:
                Log.d("RakNetCodec", "Before DisconnectPacket, available bytes: " + dataInputStream.available());
                int reason = VarInts.readInt(dataInputStream);
                logRemainingBytes(dataInputStream, "After Reason DisconnectPacket: " + reason);
                boolean skipMessage = readBool(dataInputStream);
                logRemainingBytes(dataInputStream, "After skipMessage DisconnectPacket: " + skipMessage);
                String message = "";
                if (!skipMessage) {
                    message = readVarString(dataInputStream);
                    logRemainingBytes(dataInputStream, "After message DisconnectPacket" + message);
                }
                return new DisconnectPacket(packetID, buffer, skipMessage, reason, message);
            case 9:
                return new TextPacket(packetID, buffer);
            case 10:
                return new SetTimePacket(packetID, buffer);
            case 11:
                return new StartGamePacket(packetID, buffer);
            case 12:
                return new AddPlayerPacket(packetID, buffer);
            case 13:
                return new AddEntityPacket(packetID, buffer);
            case 14:
                return new RemoveEntityPacket(packetID, buffer);

            // дальше ну очень лень. давай с компрессией разберёмся?
            default:
                byte[] unknownBody = new byte[dataInputStream.available()];
                dataInputStream.readFully(unknownBody);
                return new GamePacket(id, unknownBody);
        }


    }


       */
        return new GamePacket(id, buffer);
    }
}