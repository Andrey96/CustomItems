package ru.andrey96.customItems;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.classloading.FMLForgePlugin;
import ru.andrey96.customItems.json.CustomItemData;
import ru.andrey96.customItems.json.CustomItemsPack;

@Mod(modid=CustomItemsMod.MODID)
public class CustomItemsMod {
	
	public static class CreativeTab extends CreativeTabs {
		
		public Item tabIconItem;
		
		public CreativeTab(String id) {
			super("ci_" + id);
		}

		@Override
		public Item getTabIconItem() {
			return tabIconItem;
		}
		
	}
	
	public static final String MODID = "CustomItems";
	@Instance(MODID)
	public static CustomItemsMod instance;
	
	public String dir;
	public final List<CustomItemsPack> packs = new ArrayList<CustomItemsPack>();
	private CreativeTab currentCreativeTab = null;
	
	@EventHandler()
	public void init(FMLInitializationEvent e) {
		Side side = e.getSide();
		dir = FilenameUtils.normalizeNoEndSeparator(side.isClient() ? Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + File.separator : "");
		dir += "custom-items" + File.separator;
		File cdir = new File(dir);
		if (cdir.exists() && cdir.isDirectory()) {
			loadPacks(cdir);
		}
		registerItems(side);
		if (side.isClient()) {
			loadResourcePacks();
		} else {
			loadLanguageFiles();
		}
	}
	
	private void loadPacks(File dir) {
		File[] packFiles = dir.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File file, String name) {
				return name.endsWith(".json");
			}
		});
		CustomItemsPack.Creator creator = new CustomItemsPack.Creator();
		Gson gson = new GsonBuilder().registerTypeAdapter(CustomItemsPack.class, creator).create();
		for (File packFile : packFiles) {
			creator.name = packFile.getName();
			creator.name = creator.name.substring(0, creator.name.length() - 5);
			try {
				CustomItemsPack pack = gson.fromJson(FileUtils.readFileToString(packFile), CustomItemsPack.class);
				if (pack.items.isEmpty()) {
					FMLLog.info("Custom items pack '%s' contains no items", creator.name);
				}
				packs.add(pack);
			} catch (Exception ex) {
				FMLLog.log(Level.ERROR, "Failed to load custom items pack '%s'", creator.name);
			}
		}
	}

	private void registerItems(Side side) {
		for (CustomItemsPack pack : packs) {
			if (side.isClient()) {
				currentCreativeTab = pack.hasValidCreativeTab() ? new CreativeTab(pack.name) : null;
			}
			for (CustomItemData data : pack.items) {
				if (data.isValid()) {
					CustomItem item = new CustomItem(pack, data);
					GameRegistry.registerItem(item, item.unlocalizedName);
					if (side.isClient()) {
						item.setTextureName(data.texture == null ? (MODID + ':' + item.unlocalizedName) : data.texture);
						if (currentCreativeTab != null) {
							item.setCreativeTab(currentCreativeTab);
						}
					}
				} else {
					FMLLog.log(Level.ERROR, "Custom item '%s' from pack '%s' is not valid, it will not be registered", data.name==null?"<null>":data.name, pack.name);
				}
			}
			if (side.isClient() && currentCreativeTab != null) {
				String[] iconItem = pack.creativeTabIcon.split(":", 2);
				currentCreativeTab.tabIconItem = GameRegistry.findItem(iconItem[0], iconItem[1]);
			}
		}
		currentCreativeTab = null;
	}
	
	@SideOnly(Side.CLIENT)
	private void loadResourcePacks() {
		for (CustomItemsPack pack : packs) {
			CustomItemsModContainer mod = new CustomItemsModContainer(pack);
			if (!mod.load()) {
				FMLLog.warning("Failed to load custom items resource pack '%s'", pack.name);
			}
		}
	}
	
	private void loadLanguageFiles() {
		for (CustomItemsPack pack : packs) {
			File zip = new File(dir + pack.name + ".zip");
			if(zip.exists() && zip.isFile()) {
				try {
					Method method = LanguageRegistry.class.getDeclaredMethod("searchZipForLanguages", File.class, Side.class); //Why is this private?
					method.setAccessible(true);
					method.invoke(LanguageRegistry.instance(), zip, Side.SERVER);
				} catch (Exception ex) {
					FMLLog.log(Level.ERROR, "Loading language files for custom items pack '%s' failed with %s: %s", pack.name, ex.getClass().getSimpleName(), ex.getMessage());
				}
			}
		}
	}
}
