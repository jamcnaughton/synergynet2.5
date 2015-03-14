package synergynetframework.appsystem.contentsystem.items.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import synergynetframework.appsystem.contentsystem.items.MTKeyboard;

public class KeyboardUtility {
	private Point firstLineStart = new Point(5,9);
	private Point secondLineStart = new Point(5,86);
	private Point thirdLineStart = new Point(39,161);
	private Point fourthLineStart = new Point(5,236);
	private Point fifthLineStart = new Point(12,312);

	private int separator = 12;
	private int buttonWidth = 70;
	private int buttonHeight = 66;
	private JFrame fakeComponent;
	
	private MTKeyboard keyboard;
	
	private int[] line1 = {KeyEvent.VK_1,KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_0, KeyEvent.VK_BACK_SPACE};
	private int[] line2 = {KeyEvent.VK_Q,KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U, KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_CAPS_LOCK};
	private int[] line3 = {KeyEvent.VK_A,KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_ENTER};
	private int[] line4 = {KeyEvent.VK_SHIFT, KeyEvent.VK_Z,KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M, KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD, KeyEvent.VK_SHIFT};
	private int[] line5 = {KeyEvent.VK_SPACE};

	private Map<Rectangle, Integer> keys = new HashMap<Rectangle, Integer>();
	
	public KeyboardUtility(MTKeyboard keyboard){
		this.keyboard = keyboard;
		Point point = firstLineStart; 
		for(int key: line1){
			keys.put(new Rectangle(point.x-separator/2, point.y-separator/2, buttonWidth+separator, buttonHeight+separator), key);
			point.x+= buttonWidth + separator;
		}
		
		point = secondLineStart; 
		for(int key: line2){
			keys.put(new Rectangle(point.x-separator/2, point.y-separator/2, buttonWidth+separator, buttonHeight+separator), key);
			point.x+= buttonWidth + separator;
		}
		
		point = thirdLineStart;
		separator = 11;
		for(int i=0; i<line3.length-1; i++){
			keys.put(new Rectangle(point.x-separator/2, point.y-separator/2, buttonWidth+separator, buttonHeight+separator), line3[i]);
			point.x+= buttonWidth + separator;
		}
		keys.put(new Rectangle(point.x-separator/2, point.y-separator/2, 180+separator, buttonHeight+separator), line3[line3.length-1]);

		point = fourthLineStart;
		separator = 10;
		for(int i=0; i<line4.length-1; i++){
			keys.put(new Rectangle(point.x-separator/2, point.y-separator/2, buttonWidth+separator, buttonHeight+separator), line4[i]);
			point.x+= buttonWidth + separator;
		}
		keys.put(new Rectangle(point.x-separator/2, point.y-separator/2, 140+separator, buttonHeight+separator), line4[line4.length-1]);
		
		point = fifthLineStart; 
		for(int key: line5){
			keys.put(new Rectangle(point.x-separator/2, point.y-separator/2, (buttonWidth+separator)*11, buttonHeight+separator), key);
			point.x+= buttonWidth + separator;
		}
		
		fakeComponent = new JFrame();
		fakeComponent.setSize(10,10);
		fakeComponent.setVisible(false);
		fakeComponent.setUndecorated(true);
	}
	
	public KeyEvent getKeyEvent(float x, float y){
		for(Rectangle rec: keys.keySet()){
			if(rec.contains(x,y)){
				int key = keys.get(rec);
				KeyEvent keyEvent = new KeyEvent(fakeComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), getModifiers(), key, getKeyText(key) );
				return keyEvent;
			}
		}
		return null;
	}
	
	private int getModifiers() {
		int modifiers = 0;
		if(keyboard.isKeyPressed(KeyEvent.VK_SHIFT)){
			modifiers &= KeyEvent.VK_SHIFT;
		}
		return modifiers;
	}
	
	private char getKeyText(int key) {
		char c = (char) key;
		
		if(keyboard.isCapsLockEnabled())
			c = Character.toUpperCase(c);
		else
			c = Character.toLowerCase(c);
		
		return c;
	}
}
