package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.event.KeyEvent;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MTKeyboard;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMTKeyboardImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.items.utils.KeyboardUtility;

public class JMEMTKeyboard extends JMEWindow implements IMTKeyboardImplementation{

	protected MTKeyboard keyboard;
	protected LightImageLabel keypadImage;

	private int initialBorderSize = 100;
	private KeyboardUtility utility;
	
	public JMEMTKeyboard(ContentItem contentItem) {
		super(contentItem);
		keyboard = (MTKeyboard) contentItem;
		utility = new KeyboardUtility(keyboard);
		keypadImage = (LightImageLabel)keyboard.getContentSystem().createContentItem(LightImageLabel.class);
		keypadImage.setAutoFitSize(true);
		keypadImage.drawImage(JMEMTKeyboard.class.getResource("utils/Keypad.png"));
		keypadImage.addItemListener(new ItemListener(){

			@Override
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {
				KeyEvent evt = utility.getKeyEvent(x,y);
				keyboard.fireKeyPressed(evt);
			}

			public void cursorChanged(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				KeyEvent evt = utility.getKeyEvent(x,y);
				keyboard.fireKeyReleased(evt);
			}
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {}
			public void cursorRightClicked(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorLongHeld(ContentItem item, long id, float x,	float y, float pressure) {}
			public void cursorDoubleClicked(ContentItem item, long id, float x,	float y, float pressure) {}
		});
	}
	
	@Override
	public void init(){
		super.init();
		keyboard.addSubItem(keypadImage);
		keyboard.setBorderSize(initialBorderSize);
		resize();
		keypadImage.setRotateTranslateScalable(false);
	}
	
	protected void resize(){
		keyboard.setWidth((int)(keypadImage.getWidth())+keyboard.getBorderSize());
		keyboard.setHeight((int)(keypadImage.getHeight())+keyboard.getBorderSize());
	}
	
	@Override
	public void setBorder(Border border){
		super.setBorder(border);
		this.resize();
	}
	
}