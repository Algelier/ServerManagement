package net.algelier.servermanagement.utils.ping;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MinecraftPing {

    public MinecraftPingReply getPing(final String hostName) throws IOException {
        return this.getPing(new MinecraftPingOptions().setHostName(hostName));
    }

    private MinecraftPingReply getPing(final MinecraftPingOptions options) throws IOException{
        MinecraftPingUtil.validate(options.getHostName(),"Hostname cannot be null");
        MinecraftPingUtil.validate(options.getPort(), "Port cannot be null");

        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress(options.getHostName(), options.getPort()), options.getTimeout());

        final DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshake_bytes);

        handshake.writeByte(MinecraftPingUtil.PACKET_HANDSHAKE);
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.PROTOCOL_VERSION);
        MinecraftPingUtil.writeVarInt(handshake, options.getHostName().length());

        handshake.writeBytes(options.getHostName());
        handshake.writeShort(options.getPort());

        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.STATUS_HANDSHAKE);

        MinecraftPingUtil.writeVarInt(outputStream, handshake_bytes.size());
        outputStream.write(handshake_bytes.toByteArray());

        outputStream.writeByte(0x01);
        outputStream.writeByte(MinecraftPingUtil.PACKET_STATUSREQUEST);

        MinecraftPingUtil.readVarInt(inputStream);
        int id = MinecraftPingUtil.readVarInt(inputStream);

        MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
        MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_STATUSREQUEST, "Server returned invalid packet.");

        int lenght = MinecraftPingUtil.readVarInt(inputStream);
        MinecraftPingUtil.io(lenght == -1,"Server prematurely ended stream.");
        MinecraftPingUtil.io(lenght == 0, "Server returned unexpected  value.");

        byte[] data = new byte[lenght];
        inputStream.readFully(data);
        String json = new String(data, options.getCharset());

        outputStream.writeByte(0x09);
        outputStream.writeByte(MinecraftPingUtil.PACKET_PING);
        outputStream.writeLong(System.currentTimeMillis());

        MinecraftPingUtil.readVarInt(inputStream);
        id = MinecraftPingUtil.readVarInt(inputStream);

        MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
        MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_PING, "Server returned invalid packet.");

        handshake.close();
        handshake_bytes.close();
        outputStream.close();
        inputStream.close();
        socket.close();

        try {
            return new Gson().fromJson(json, MinecraftPingReply.class);

        } catch (Exception e) {
            //useless
        }
        return null;
    }
}
