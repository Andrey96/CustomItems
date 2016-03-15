package ru.andrey96.customItems;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.ResourcePackFileNotFoundException;

@SideOnly(Side.CLIENT)
public class CustomItemsResourcePack extends FMLFileResourcePack {

	private final String id;
	private final ZipFile zip;
	
	public CustomItemsResourcePack(ModContainer container) {
		super(container);
		CustomItemsModContainer cir = (CustomItemsModContainer)container;
		id = cir.id;
		zip = cir.zip;
	}
	
	@Override
    protected InputStream getInputStreamByName(String resourceName) throws IOException
    {
        ZipEntry zipentry = zip.getEntry(resourceName);
        try {
            if ("pack.mcmeta".equals(resourceName)) {
                return new ByteArrayInputStream(("{ \"pack\": { \"description\": \"CustomItems "+id+" resource pack\", \"pack_format\": 1 }}").getBytes());
            } else {
                return zip.getInputStream(zipentry);
            }
        } catch (IOException e) {
            throw new ResourcePackFileNotFoundException(resourcePackFile, resourceName);
        }
    }
	
    @Override
    public boolean hasResourceName(String resourceName)
    {
        return zip.getEntry(resourceName) != null;
    }
}
