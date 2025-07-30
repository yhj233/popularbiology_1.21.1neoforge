package com.yuntang.popularbiology.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class XiaobaTaoFaBnag extends SwordItem {
   public XiaobaTaoFaBnag(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
      super(tier, properties);
   }

   public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
      if (pStack.getItem() == this) {
         pTooltipComponents.add(Component.translatable("tooltip.popularbiology.xiaoba_tao_fa_bang").withStyle((style) -> {
            return style.withColor(TextColor.fromRgb(62975));
         }));
      }

   }
}
