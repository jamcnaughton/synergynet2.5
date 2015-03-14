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

package apps.mathpadapp.controllerapp.tablecontroller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.BlockTableMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.totable.UnicastEnableRemoteDesktopMessage;
import apps.mathpadapp.util.MTListManager;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class TableListControlPanel {
	
	protected OrthoContainer container;
	private MTListManager tablePanelManager;
	public static final int controlTablePanelHeight = 140;
	private ControllerManager controllerManager;
	
	public TableListControlPanel(ContentSystem contentSystem, final MTListManager tablePanelManager, ControllerManager controllerManager){
		container  = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		this.tablePanelManager = tablePanelManager;
		this.controllerManager = controllerManager;
		
		List<String> buttonActions = new ArrayList<String>();
		buttonActions.add("Block");
		buttonActions.add("Unblock");
		buttonActions.add("Delete");
		buttonActions.add("Select All");
		buttonActions.add("Deselect All");
		buttonActions.add("Watch");
		buttonActions.add("Send Message");
		
		int panelWidth = TableControllerWindow.windowWidth- 20;
		int shiftX = -panelWidth/2;
		int shiftY = 0;
		for(int i=0; i<buttonActions.size(); i++){
			SimpleButton button = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
			button.setText(buttonActions.get(i));
			button.addButtonListener(new ButtonAction());
			button.setLocalLocation(shiftX + button.getWidth()/2, shiftY);
			shiftX+= button.getWidth()+ 5;
			if((shiftX+ button.getWidth()/2)> panelWidth/2){
				shiftX = -panelWidth/2;
				shiftY-= button.getHeight() + 5;
			}
			container.addSubItem(button);
		}
	}
	
	class ButtonAction implements SimpleButtonListener{

		@Override
		public void buttonClicked(SimpleButton b, long id, float x, float y,
				float pressure) {
			if(b.getText().equalsIgnoreCase("Select All")){
				tablePanelManager.selectAllItems();
			}else if(b.getText().equalsIgnoreCase("Deselect All")){
				tablePanelManager.deselectAllItems();
			}else if(b.getText().equalsIgnoreCase("Delete")){
				tablePanelManager.deleteSelectedItems();
			}else if(b.getText().equalsIgnoreCase("Block")){
				for(Object item: tablePanelManager.getSelectedItems()){
					TableIdentity tableId = (TableIdentity) item;
					if(controllerManager != null){	
						BlockTableMessage message = new BlockTableMessage(MathPadClient.class, tableId, true);
						controllerManager.sendMessage(message);
					}
				}
				for(Object item: tablePanelManager.getSelectedItems()){
					tablePanelManager.setIcon(item, MathPadResources.class.getResource("tablestatus/blocked.jpg"));
				}
			}else if(b.getText().equalsIgnoreCase("Unblock")){
				for(Object item: tablePanelManager.getSelectedItems()){
					TableIdentity tableId = (TableIdentity) item;
					if(controllerManager != null){	
						BlockTableMessage message = new BlockTableMessage(MathPadClient.class, tableId, false);
						controllerManager.sendMessage(message);
					}
				}
				for(Object item: tablePanelManager.getSelectedItems()){
					tablePanelManager.setIcon(item, MathPadResources.class.getResource("tablestatus/online.jpg"));
				}
			}else if(b.getText().equalsIgnoreCase("Watch")){
				for(Object item: tablePanelManager.getSelectedItems()){
					TableIdentity tableId = (TableIdentity) item;
					if(controllerManager != null){
						MathPadRemoteDesktop rd = controllerManager.getRemoteDesktopManager().createRemoteDesktopNode(tableId);
						if(rd != null) rd.getDesktopWindow().setAsTopObject();
						UnicastEnableRemoteDesktopMessage msg = new UnicastEnableRemoteDesktopMessage(MathPadClient.class, tableId, true);
						controllerManager.sendMessage(msg);
					}
				}
			}
		}

		@Override
		public void buttonDragged(SimpleButton b, long id, float x, float y, float pressure) {
		}

		@Override
		public void buttonPressed(SimpleButton b, long id, float x, float y, float pressure) {
			Color bgColor = b.getBackgroundColour();
			b.setBackgroundColour(b.getTextColour());
			b.setTextColour(bgColor);
		}

		@Override
		public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
			Color bgColor = b.getBackgroundColour();
			b.setBackgroundColour(b.getTextColour());
			b.setTextColour(bgColor);
		}
		
	}
	
	public OrthoContainer getContainer(){
		return container;
	}
}