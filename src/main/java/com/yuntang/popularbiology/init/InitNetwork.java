package com.yuntang.popularbiology.init;

import com.yuntang.popularbiology.network.SyncDataC2SPacket;
//import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.bus.api.IEventBus;


public class InitNetwork {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(InitNetwork::onRegisterPayloadHandlers);
    }

    private static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        // 创建注册器并指定协议版本
        PayloadRegistrar registrar = event.registrar("popularbiology")
                .versioned("1.0"); // 协议版本
        
        // 注册客户端到服务器的数据包
        registrar.playToServer(
            SyncDataC2SPacket.TYPE,    // 数据包类型
            SyncDataC2SPacket.CODEC,   // 数据包编解码器
            (payload, context) -> {    // 处理函数
                context.enqueueWork(() -> 
                    SyncDataC2SPacket.handle(payload, context)
                );            
            }
        );
    }
}

