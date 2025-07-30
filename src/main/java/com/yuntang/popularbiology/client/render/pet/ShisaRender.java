package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.ShisaPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class ShisaRender extends BaseRender<ShisaPet> {
   public ShisaRender(Context ctx) {
      super(ctx, new BaseModel());
   }
}
