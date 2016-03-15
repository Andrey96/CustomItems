package ru.andrey96.customItems.json;

public class CustomItemData {
	
	public int maxStackSize = 64;
	public String name = null, texture = null;
	public boolean hasEnchantEffect = false;
	
	public boolean isValid() {
		return name != null;
	}
}
