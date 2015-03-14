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

import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.RequestAllTableIdsMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;
import apps.mathpadapp.util.MTFrame;
import apps.mathpadapp.util.MTList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;

public class TableControllerWindow extends MTFrame implements ControllerNetworkListener{
	
	public static final int windowWidth = 600;
	public static final int windowHeight = 380;
	
	private TableListControlPanel controlPanel;
	private ControllerManager controllerManager;

	public MTList tableListPanel;
	
	public TableControllerWindow(final ContentSystem contentSystem, final ControllerManager controllerManager){
		super(contentSystem);
		this.controllerManager = controllerManager;
		this.setWidth(windowWidth);
		this.setHeight(windowHeight);

		tableListPanel = new MTList(contentSystem);
		this.getWindow().addSubItem(tableListPanel.getContainer());
		tableListPanel.getContainer().setLocalLocation(0, -20);

		controlPanel = new TableListControlPanel(contentSystem, tableListPanel.getManager(), controllerManager);
		controlPanel.getContainer().setLocalLocation(controlPanel.getContainer().getLocalLocation().x, -165);

		this.getWindow().addSubItem(controlPanel.getContainer());
		this.getWindow().setOrder(OrthoBringToTop.bottomMost);
		tableListPanel.getManager().addItem("temp", "temp");
		tableListPanel.getManager().deleteAllItems();
		this.setTitle("Online Tables");
		if(controllerManager != null){
			controllerManager.addNetworkListener(this);
			RequestAllTableIdsMessage msg = new RequestAllTableIdsMessage(MathPadClient.class);
			controllerManager.sendMessage(msg);
		}
	}

	public MTList getTableList() {
		return tableListPanel;
	}

	@Override
	public void close(){
		if(controllerManager != null) controllerManager.removeNetworkListener(this);
		super.close();
	}
	
	@Override
	public void projectorFound(TableIdentity tableId, boolean isLeaseSuccessful) {
		 
		
	}

	@Override
	public void remoteDesktopContentReceived(TableIdentity tableId,	HashMap<UserIdentity, MathToolInitSettings> items) {}

	@Override
	public void resultsReceivedFromUser(TableIdentity tableId,	UserIdentity userId, AssignmentInfo assignInfo) {}

	@Override
	public void syncDataReceived(TableIdentity sender,	HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData) {}

	@Override
	public void userIdsReceived(TableIdentity tableId,	List<UserIdentity> userIds) {}

	@Override
	public void userMathPadReceived(TableIdentity tableId, UserIdentity userId,	MathToolInitSettings mathToolSettings) {}

	@Override
	public void userRegistrationReceived(TableIdentity tableId,	UserIdentity userId) {	}

	@Override
	public void userUnregistrationReceived(TableIdentity tableId,	UserIdentity userId) {	}

	@Override
	public void tableIdReceived(TableIdentity tableId) {
		if(!tableListPanel.getManager().getAllItems().contains(tableId)){
			tableListPanel.getManager().addItem(tableId.toString(), tableId);
			tableListPanel.getManager().setIcon(tableId, MathPadResources.class.getResource("tablestatus/online.jpg"));
		}
	}

}