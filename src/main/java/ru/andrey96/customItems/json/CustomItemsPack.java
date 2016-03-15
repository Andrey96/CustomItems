package ru.andrey96.customItems.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.InstanceCreator;

public class CustomItemsPack {
	
	public static class Creator implements InstanceCreator<CustomItemsPack> {
		
		public String name;
		
		@Override
		public CustomItemsPack createInstance(Type type) {
			return new CustomItemsPack(name);
		}
		
	} 
	
	public final String name;
	public String creativeTabIcon = null;
	public List<CustomItemData> items = new ArrayList<CustomItemData>();
	
	public CustomItemsPack(String name) {
		this.name = name;
	}
	
	public boolean hasValidCreativeTab() {
		return creativeTabIcon != null && creativeTabIcon.indexOf(':') != -1;
	}
	
}
