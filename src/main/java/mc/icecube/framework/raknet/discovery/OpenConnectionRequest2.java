package mc.icecube.framework.raknet.discovery;

public class OpenConnectionRequest2 extends Packet {
    public long id;
    public byte[] buffer;
    public int serverCookie;
    public boolean supportsSecurity;
    public String clientAddress;
    public int mtuSize;
    public long clientGUID;

    public OpenConnectionRequest2(long id, byte[] buffer, int serverCookie, boolean supportsSecurity, String clientAddress, int mtuSize, long clientGUID) {
        this.id = id;
        this.buffer = buffer;
        this.serverCookie = serverCookie;
        this.supportsSecurity = supportsSecurity;
        this.clientAddress = clientAddress;
        this.mtuSize = mtuSize;
        this.clientGUID = clientGUID;
    }

    @Override
    public String toString() {
        return "OpenConnectionRequest2{" +
                "id=" + id +
                ", serverCookie=" + serverCookie +
                ", supportsSecurity=" + supportsSecurity +
                ", clientAddress='" + clientAddress + '\'' +
                ", mtuSize=" + mtuSize +
                ", clientGUID=" + clientGUID +
                '}';
    }
}