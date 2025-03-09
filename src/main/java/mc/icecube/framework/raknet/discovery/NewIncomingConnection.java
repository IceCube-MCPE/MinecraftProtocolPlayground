package mc.icecube.framework.raknet.discovery;

import java.util.Arrays;

public class NewIncomingConnection extends Packet {

    private long id;
    private String serverAddress;
    private String[] systemAddreses;
    private long requestTimestamp, replyTimestamp;

    public NewIncomingConnection(long id, String serverAddress, String[] systemAddreses, long requestTimestamp, long replyTimestamp) {
        this.id = id;
        this.serverAddress = serverAddress;
        this.systemAddreses = systemAddreses;
        this.requestTimestamp = requestTimestamp;
        this.replyTimestamp = replyTimestamp;
    }

    @Override
    public String toString() {
        return "NewIncomingConnection{" +
                "id=" + id +
                ", serverAddress='" + serverAddress + '\'' +
                ", systemAddreses=" + Arrays.toString(systemAddreses) +
                ", requestTimestamp=" + requestTimestamp +
                ", replyTimestamp=" + replyTimestamp +
                '}';
    }
}