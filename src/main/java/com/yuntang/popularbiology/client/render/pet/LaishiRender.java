package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.LaishiPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class LaishiRender extends BaseRender<LaishiPet> {
   public LaishiRender(Context ctx) {
      super(ctx, new BaseModel());
   }
}
