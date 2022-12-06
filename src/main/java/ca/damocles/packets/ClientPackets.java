package ca.damocles.packets;

import ca.damocles.proto.Packets;
import com.google.protobuf.Any;
import com.google.protobuf.Message;

public enum ClientPackets {
    LOGIN(Packets.ClientPacket.LoginPacket.class),
    CLOSE(Packets.ClientPacket.ClosePacket.class),
    CREATE_ACCOUNT(Packets.ClientPacket.CreateAccountPacket.class),
    PONG(Packets.ClientPacket.PongPacket.class),
    NONE(null);

    public Class<? extends Message> clazz;
    ClientPackets(Class<? extends Message> clazz){
        this.clazz = clazz;
    }

    public static ClientPackets inferType(Any packet){
        for(ClientPackets type : ClientPackets.values()){
            if(type == NONE) break;
            if(packet.is(type.clazz)) return type;
        }
        return NONE;
    }
}
