package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.LizimantouPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class LizimantouRender extends BaseRender<LizimantouPet> {
   public LizimantouRender(Context ctx) {
      super(ctx, new BaseModel());
   }
}
