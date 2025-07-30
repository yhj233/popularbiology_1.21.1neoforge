package com.yuntang.popularbiology.network;

import com.yuntang.popularbiology.entity.BasePet;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncDataC2SPacket(int entityId, Map<DataType, Object> updatedData) implements CustomPacketPayload {
    
    public static final Type<SyncDataC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("popularbiology", "sync_data"));
    
    public static final StreamCodec<FriendlyByteBuf, SyncDataC2SPacket> CODEC = StreamCodec.of(
        SyncDataC2SPacket::write,
        SyncDataC2SPacket::read
    );

    public SyncDataC2SPacket(int entityId, Map<DataType, Object> updatedData) {
        this.entityId = entityId;
        this.updatedData = new EnumMap<>(updatedData);
    }

    public static SyncDataC2SPacket read(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int size = buf.readInt();
        Map<DataType, Object> updatedData = new EnumMap<>(DataType.class);

        for(int i = 0; i < size; ++i) {
            DataType type = buf.readEnum(DataType.class);
            switch(type) {
                case PET_TEXTURE:
                    updatedData.put(type, buf.readInt());
                    break;
                case IS_CHAT_STATE:
                    updatedData.put(type, buf.readBoolean());
                    break;
                case CHATBUBBLE_MESSAGE:
                    updatedData.put(type, buf.readUtf(32767));
                    break;
                case CHATBUBBLE_MESSAGE_TIME:
                    updatedData.put(type, buf.readInt());
                    break;
            }
        }

        return new SyncDataC2SPacket(entityId, updatedData);
    }

    public static void write(FriendlyByteBuf buf, SyncDataC2SPacket packet) {
        buf.writeInt(packet.entityId);
        buf.writeInt(packet.updatedData.size());

        for(Entry<DataType, Object> entry : packet.updatedData.entrySet()) {
            buf.writeEnum(entry.getKey());
            switch(entry.getKey()) {
                case PET_TEXTURE:
                    buf.writeInt((Integer)entry.getValue());
                    break;
                case IS_CHAT_STATE:
                    buf.writeBoolean((Boolean)entry.getValue());
                    break;
                case CHATBUBBLE_MESSAGE:
                    buf.writeUtf((String)entry.getValue());
                    break;
                case CHATBUBBLE_MESSAGE_TIME:
                    buf.writeInt((Integer)entry.getValue());
                    break;
            }
        }
    }

    public static void handle(SyncDataC2SPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player != null) {
                Entity entity = player.level().getEntity(packet.entityId);
                if (entity instanceof BasePet pet) {
                    for(Entry<DataType, Object> entry : packet.updatedData.entrySet()) {
                        switch(entry.getKey()) {
                            case PET_TEXTURE:
                                pet.setPetTexture((Integer)entry.getValue());
                                break;
                            case IS_CHAT_STATE:
                                pet.setChatState((Boolean)entry.getValue());
                                break;
                            case CHATBUBBLE_MESSAGE:
                                pet.setChatbubbleMessage((String)entry.getValue());
                                break;
                            case CHATBUBBLE_MESSAGE_TIME:
                                pet.setChatbubbleMessageTime((Integer)entry.getValue());
                                break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum DataType {
        PET_TEXTURE,
        IS_CHAT_STATE,
        CHATBUBBLE_MESSAGE,
        CHATBUBBLE_MESSAGE_TIME;
    }
}
