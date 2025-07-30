package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.FeishuPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class FeishuRender extends BaseRender<FeishuPet> {
   public FeishuRender(Context ctx) {
      super(ctx, new BaseModel());
   }
}
