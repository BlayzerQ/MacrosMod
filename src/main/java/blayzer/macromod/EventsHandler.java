package blayzer.macromod;

import java.util.Iterator;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventsHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(KeyInputEvent event)
	{
	    if (MacroMod.keyMacros.isPressed()) 
	    {
	    	Minecraft.getMinecraft().displayGuiScreen(new MacrosGUI());
	    }
	    
		Iterator iter = MacroMod.listMacros.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry map = (Map.Entry) iter.next();
			if(Keyboard.getKeyIndex((String) map.getKey()) == Keyboard.getEventKey() && Keyboard.getEventKeyState()) {
				String message = (String) map.getValue();
				if(message != MacrosGUI.emptyMessage)
					Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
			}
		}
		
	}
	
}
