package synergynetframework.appsystem.table.appregistry.menucontrol;

/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.concurrent.ConcurrentHashMap;


import com.jme.system.DisplaySystem;
import common.CommonResources;
import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;
import synergynetframework.jme.cursorsystem.CursorRegistry;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

public class HoldTopRightConfirmVisualExit extends MenuController implements IMultiTouchEventListener{
	
	private final long INTERVAL = 1000, VISIBLE_INTERVAL = 3000, EMERGENCY_INTERVAL = 4000;
	
	protected float cornerDistance = 70f;
	protected ConcurrentHashMap <Long,Long> cursorTiming = new ConcurrentHashMap<Long,Long>();
	protected ConcurrentHashMap <Long,Long> emergencyCursorTiming = new ConcurrentHashMap<Long,Long>();
	private boolean enabled;
	private ImageTextLabel confirmExitButton;
	private ImageTextLabel cornerButton;
	private ContentSystem contentSystem;	
	private long startVisibleTime = -1;
	private CursorRegistry cursorRegistry;
	
	public HoldTopRightConfirmVisualExit(SynergyNetApp app) {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(app);
		createButtons();
		cursorRegistry = CursorRegistry.getInstance();
	}	
	
	public void createButtons(){
		createCornerButton();
		createConfirmButton();
	}
	
	private void createCornerButton(){
		cornerButton = (ImageTextLabel)contentSystem.createContentItem(ImageTextLabel.class);
		cornerButton.setAutoFitSize(false);	
		cornerButton.setWidth(40);
		cornerButton.setHeight(40);				
		cornerButton.setRotateTranslateScalable(false);			
		cornerButton.setBorderSize(0);
		cornerButton.setManipulate(false);
		cornerButton.centerItem();
		cornerButton.setLocalLocation(DisplaySystem.getDisplaySystem().getWidth()-35, DisplaySystem.getDisplaySystem().getHeight()-35);
		cornerButton.removable = false;			
		buttonOff();
		cornerButton.addItemListener(new ItemEventAdapter() {						
			public void cursorPressed(ContentItem item, long id, float x, float y, float pressure) {
				if(confirmExitButton.isVisible()){
					confirmExitButton.setVisible(false);			
					buttonOff();
				}
			}
			
		});
	}
	
	private void buttonOn(){
		setButtonImage("corner_button_on");
		cornerButton.setAsTopObject();		
	}
	
	private void buttonOff(){
		setButtonImage("corner_button_off");
		cornerButton.setAsBottomObject();
	}
	
	private void setButtonImage(String resource){
		if (cornerButton == null) return;
		cornerButton.setImageInfo(CommonResources.class.getResource(resource + ".png"));
	}
	
	private void createConfirmButton() {
		confirmExitButton = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		confirmExitButton.setAutoFitSize(false);		
		confirmExitButton.setRotateTranslateScalable(false);	
		confirmExitButton.setWidth(100);
		confirmExitButton.setHeight(50);	
		confirmExitButton.setBorderSize(0);
		confirmExitButton.setVisible(false);
		confirmExitButton.setLocalLocation(DisplaySystem.getDisplaySystem().getWidth()-confirmExitButton.getWidth()/2-45, 
				DisplaySystem.getDisplaySystem().getHeight()-confirmExitButton.getHeight()/2-45);
		confirmExitButton.removable = false;
		confirmExitButton.setImageInfo(CommonResources.class.getResource("exit_button.png"));
		confirmExitButton.addItemListener(new ItemEventAdapter() {
			public void cursorReleased(ContentItem item, long id, float x, float y, float pressure) {
				exitApp();
			}			
		});		
	}
	
	private void exitApp(){
		try {
			confirmExitButton.setVisible(false);
			SynergyNetDesktop.getInstance().showMainMenu();
		} catch (InstantiationException e) {					 
			e.printStackTrace();
		} catch (IllegalAccessException e) {					 
			e.printStackTrace();
		} catch (ClassNotFoundException e) {					 
			e.printStackTrace();
		}
	}

	@Override
	public void enableForApplication(SynergyNetApp app) {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().registerMultiTouchEventListener(this);
		setEnabled(true);	
		buttonOff();
	}
	
	@Override
	public void applicationFinishing() {
		if(confirmExitButton != null) confirmExitButton.setVisible(false);
		setEnabled(false);
	}	
	
	
	public boolean isEnabled() {
		return enabled;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void cursorChanged(MultiTouchCursorEvent event) {
	}

	public void cursorClicked(MultiTouchCursorEvent event) {
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		if(!enabled)return;
		if(isInCorner(event)) {
			cursorTiming.put(event.getCursorID(), System.currentTimeMillis());
			buttonOn();
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		if(!enabled)return;
		if (cursorTiming.containsKey(event.getCursorID())){
			cursorTiming.remove(event.getCursorID());
			if(!confirmExitButton.isVisible())buttonOff();					
		}
		if (emergencyCursorTiming.containsKey(event.getCursorID()))emergencyCursorTiming.remove(event.getCursorID());
	}

	public void objectAdded(MultiTouchObjectEvent event) {
	}

	public void objectChanged(MultiTouchObjectEvent event) {
	}

	public void objectRemoved(MultiTouchObjectEvent event) {
	}

	protected boolean isInCorner(MultiTouchCursorEvent event) {
		int x = SynergyNetDesktop.getInstance().tableToScreenX(event.getPosition().x);
		int y = SynergyNetDesktop.getInstance().tableToScreenY(event.getPosition().y);
		return isInCorner(x, y);
	}
	
	protected boolean isInCorner(float x, float y) {
		return x > SynergyNetDesktop.getInstance().getDisplayWidth() - cornerDistance  && y > SynergyNetDesktop.getInstance().getDisplayHeight() - cornerDistance;
	}

	public void update(float interpolation) {
		if(!enabled)return;
		if(confirmExitButton.isVisible()){
			if((System.currentTimeMillis() - startVisibleTime)> VISIBLE_INTERVAL){
				confirmExitButton.setVisible(false);
				buttonOff();
			}
		}
		long endTime = System.currentTimeMillis();
		for(long id : cursorTiming.keySet()){
			long startTime = cursorTiming.get(id);
			if(endTime - startTime > INTERVAL ) {
				if (cursorRegistry.containsKey(id)){
					try{
						if (cursorRegistry.getCursor(id).getCurrentCursorScreenPosition() == null ||
								isInCorner(cursorRegistry.getCursor(id).getCurrentCursorScreenPosition().x, cursorRegistry.getCursor(id).getCurrentCursorScreenPosition().y)){
							emergencyCursorTiming.put(id, cursorTiming.get(id));
							cursorTiming.remove(id);			
							confirmExitButton.setVisible(true);
							confirmExitButton.setAsTopObject();
							startVisibleTime = System.currentTimeMillis();
						}
					}catch (NullPointerException e){}
				}
			}
		}

		for(long id : emergencyCursorTiming.keySet()){
			long startTime = emergencyCursorTiming.get(id);
			if(endTime - startTime > EMERGENCY_INTERVAL ) {
				if (cursorRegistry.containsKey(id)){
					if (cursorRegistry.getCursor(id).getCurrentCursorScreenPosition() == null ||
							isInCorner(cursorRegistry.getCursor(id).getCurrentCursorScreenPosition().x, cursorRegistry.getCursor(id).getCurrentCursorScreenPosition().y)){
						emergencyCursorTiming.remove(id);
						exitApp();
					}
				}
			}
		}
		
	}

}