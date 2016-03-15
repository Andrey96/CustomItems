package ru.andrey96.customItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.andrey96.customItems.json.CustomItemData;
import ru.andrey96.customItems.json.CustomItemsPack;

public class CustomItem extends Item{
	
	private boolean hasEnchantEffect;
	public String unlocalizedName;
	
	public CustomItem(CustomItemsPack pack, CustomItemData data) {
		this.hasEnchantEffect = data.hasEnchantEffect;
		this.setMaxStackSize(data.maxStackSize);
		this.setUnlocalizedName(unlocalizedName = (pack.name + "_" + data.name));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack) {
		return hasEnchantEffect;
	}
}
