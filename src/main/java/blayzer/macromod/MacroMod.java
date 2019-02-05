package blayzer.macromod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "MacroMod", acceptableRemoteVersions = "*")
public class MacroMod {
	
	public static KeyBinding keyMacros = new KeyBinding("Edit Macros", Keyboard.KEY_P, "Macros");
	public static Map<String, String> listMacros = new HashMap<String, String>();
	public static File config;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventsHandler());
		FMLCommonHandler.instance().bus().register(new EventsHandler());
		
		ClientRegistry.registerKeyBinding(keyMacros);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new File(event.getModConfigurationDirectory().getAbsolutePath() + "/MacrosMod/macros.txt");
		
		try {
			if(!config.exists()) {
				config.getParentFile().mkdirs();
				config.createNewFile();
			}
			FileReader fr = new FileReader(config);
	        BufferedReader br = new BufferedReader(fr);
	        String line = null;
	        while ((line = br.readLine()) != null) {
	        	String[] map = line.split("@");
	            listMacros.put(map[0], map[1]);
	        }
	        fr.close();
	        br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
