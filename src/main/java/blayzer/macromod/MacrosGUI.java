package blayzer.macromod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;

public class MacrosGUI extends GuiScreen {
	private int macrosCount = 10;
	
	private GuiButton[] buttons = new GuiButton[macrosCount];
	private GuiTextField[] fields = new GuiTextField[macrosCount];
	ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	
	private int typedKey = -1;
	
	public static String emptyMessage = "Type a message or command here";
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		this.drawDefaultBackground();
		
		for(GuiTextField field : fields) {
			field.drawTextBox();
	    	if(field.isFocused() && field.getText().equals(emptyMessage))
	    		field.setText("");
	    	else if(!field.isFocused() && field.getText().equals(""))
	    		field.setText(emptyMessage);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        
        for(GuiTextField field : fields)
        	field.textboxKeyTyped(par1, par2);
    }
	
	@Override
	protected void mouseClicked(int x, int y, int btn) {
	    super.mouseClicked(x, y, btn);
	    
	    for(GuiTextField field : fields) {
	    	field.mouseClicked(x, y, btn);
	    }
	}
	
	@Override
    public void updateScreen()
    {
        super.updateScreen();
        
        for(GuiTextField field : fields)
        	field.updateCursorCounter();
    }

	@Override
	public void initGui() {
		
		int heightValue = 24;
		
		int textHeight = 20;
		int textWidth = 237;
        int textX = ((scale.getScaledWidth() - textWidth) / 2);
        int textY = ((scale.getScaledHeight() - textHeight) / 2) + 60;
        
        
		int butHeight = textHeight;
		int butWidth = 50;
        int butX = ((scale.getScaledWidth() - butWidth) / 2);
        int butY = ((scale.getScaledHeight() - butHeight) / 2) + 60;
		
		for(int i = 0; i < macrosCount; i++) {
			fields[i] = new GuiTextField(this.fontRendererObj, textX, textY - heightValue, textWidth, textHeight);
			//fields[i].setMaxStringLength(16);
			fields[i].setText(emptyMessage); //Проверку на сохраненность, и дефолт если не сохранено
			//fields[i].setFocused(true);
			this.buttonList.add(buttons[i] = new GuiButton(1, butX - 154, butY - heightValue, butWidth, butHeight, "None"));
			heightValue += 24;
		}
		
		load();
	}

	@Override
	protected void actionPerformed(GuiButton typedButton) {
		
		for(GuiButton button : buttons) {
			if (typedButton == button) {
				button.displayString = "Key?";
				new KeyThread(button).start();
			}
		}

	}
	
	private void save() {
		//Сохранение текущих настроек
		
		try {
			FileWriter fw = new FileWriter(MacroMod.config);
			
			for(int i = 0; i < macrosCount; i++) {
				MacroMod.listMacros.put(buttons[i].displayString, fields[i].getText());
				fw.write(buttons[i].displayString + "@" + fields[i].getText() + "\n");
			}
			
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void load() {
		//Загрузка текущих настроек
		
		int index = 0;
		Iterator iter = MacroMod.listMacros.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry map = (Map.Entry)iter.next();
			buttons[index].displayString = (String) map.getKey();
			fields[index].setText((String) map.getValue());
			index++;
		}
	}
	
	@Override
	public void onGuiClosed() {
		save();
	}
	
	private class KeyThread extends Thread {
		private GuiButton button;
		
		public KeyThread(GuiButton button) {
			this.button = button;
		}
		
		@Override
		public void run() {
			while(typedKey == -1) {
				if (Keyboard.getEventKeyState())
					typedKey = Keyboard.getEventKey();
			}
			if(typedKey != 1)
				button.displayString = Keyboard.getKeyName(typedKey);
			typedKey = -1;
			this.interrupt();
		}
	}
}
