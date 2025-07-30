package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.WusaqiPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class WusaqiRender extends BaseRender<WusaqiPet> {
   public WusaqiRender(Context context) {
      super(context, new BaseModel());
   }
}
