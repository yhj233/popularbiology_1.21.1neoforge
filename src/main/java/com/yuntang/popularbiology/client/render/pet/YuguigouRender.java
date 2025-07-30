package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.YuguigouPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class YuguigouRender extends BaseRender<YuguigouPet> {
   public YuguigouRender(Context ctx) {
      super(ctx, new BaseModel());
   }
}
