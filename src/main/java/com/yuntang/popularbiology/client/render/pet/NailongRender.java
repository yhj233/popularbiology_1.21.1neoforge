package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.NailongPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class NailongRender extends BaseRender<NailongPet> {
   public NailongRender(Context ctx) {
      super(ctx, new BaseModel());
   }
}
