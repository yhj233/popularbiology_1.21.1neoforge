package com.yuntang.popularbiology.client.render.pet;

import com.yuntang.popularbiology.client.model.BaseModel;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.pet.XiaobaPet;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class XiaobaRender extends BaseRender<XiaobaPet> {
   public XiaobaRender(Context ctx) {
      super(ctx, new BaseModel());
   }
}
