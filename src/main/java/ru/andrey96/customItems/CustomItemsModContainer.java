package ru.andrey96.customItems;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import ru.andrey96.customItems.json.CustomItemsPack;

@SideOnly(Side.CLIENT)
public class CustomItemsModContainer extends DummyModContainer {
	
	public final String id, modid;
	public final File dir, source;
	public ZipFile zip;
	
	CustomItemsModContainer(CustomItemsPack pack) {
		super(new ModMetadata());
		
		this.id = pack.name;
		this.dir = new File(CustomItemsMod.instance.dir+id);
		this.source = new File(CustomItemsMod.instance.dir+id+".zip");
		this.modid = "ci_"+id;
		
		ModMetadata meta = getMetadata();
        meta.modId = this.modid;
        meta.name = "CustomItems resources [" + id + "]";
        meta.description = "Used to load custom textures and lang files for " + id + " items";
    }
	
	boolean load() {
		if (dir.exists() && dir.isDirectory()) {
			try {
				FMLLog.info("Compressing directory into zip file for custom items pack '%s'", id);
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(source, false));
				int start = dir.toString().length()+1;
				for(File f : FileUtils.listFiles(dir, null, true))
				{
					out.putNextEntry(new ZipEntry(new StringBuilder("assets/").append(CustomItemsMod.MODID.toLowerCase()).append('/').append(f.toString().substring(start).replace('\\', '/')).toString()));
					IOUtils.copy(new FileInputStream(f), out);
					out.closeEntry();
				}
				out.close();
			} catch(Exception ex) {
				FMLLog.log(Level.ERROR, "Directory compression failed with %s: %s", ex.getClass().getSimpleName(), ex.getMessage());
				return false;
			}
		}
		if (source.exists() && source.isFile()) {
			try {
				zip = new ZipFile(source);
				FMLClientHandler.instance().addModAsResource(this);
		        ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).reloadResourcePack(FMLClientHandler.instance().getResourcePackFor(modid));
		        return true;
			} catch (Exception ex) { }
        }
		return false;
    }
	
	@Override
	public Class<?> getCustomResourcePackClass() {
		return CustomItemsResourcePack.class;
	}
	
	@Override
	public File getSource() {
		return source;
	}
}
