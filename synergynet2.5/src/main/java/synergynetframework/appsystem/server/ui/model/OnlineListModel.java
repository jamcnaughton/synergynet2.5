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

package synergynetframework.appsystem.server.ui.model;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import synergynetframework.appsystem.server.ServerMonitor;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.server.TableCommsServerService;

public class OnlineListModel extends DefaultListModel implements ServerMonitor {
	private static final long serialVersionUID = -1549976236904153875L;
	private TableCommsServerService server;

	public OnlineListModel(TableCommsServerService server) {
		this.server = server;
		server.registerServerMonitor(this);
	}
	
	public Object getElementAt(int index) {
		if(server.getReceivers().size() == 0) return null;
		java.util.List<TableIdentity> l = new ArrayList<TableIdentity>();
		l.addAll(server.getReceivers().keySet());
		return l.get(index);
	}
	
	public int getSize() {
		return server.getReceivers().size();
	}

	public void tableConnected(TableIdentity identity) {
		this.fireContentsChanged(this, 0, getSize());
	}

	public void tableDisconnected(TableIdentity identity) {
		this.fireContentsChanged(this, 0, getSize());
	}
	
	public void serverReceivedMessage(Object obj) {}
}