package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.JiyiPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class JiyiRender extends BaseRender<JiyiPet> {
   public JiyiRender(Context ctx) {
      super(ctx, new BaseModel());
   }
}
