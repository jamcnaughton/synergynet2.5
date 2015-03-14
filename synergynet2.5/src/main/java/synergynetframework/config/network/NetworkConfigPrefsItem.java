/*
 * Copyright (c) 2012 University of Durham, England
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

package synergynetframework.config.network;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import core.ConfigurationSystem;


import synergynetframework.config.PreferencesItem;

public class NetworkConfigPrefsItem implements PreferencesItem {
	
	private static final Preferences prefs = ConfigurationSystem.getPreferences(NetworkConfigPrefsItem.class);
	
	private static final String HTTP_PROXY_HOST = "HTTP_PROXY_HOST";
	private static final String HTTP_PROXY_PORT = "HTTP_PROXY_PORT";
	private static final String HTTP_PROXY_ENABLED = "HTTP_PROXY_ENABLED";

	@Override
	public JPanel getConfigurationPanel() {
		return new NetworkConfigPrefsPanel(this);
	}

	@Override
	public String getName() {
		return "Network";
	}

	public String getProxyHost() {
		return prefs.get(HTTP_PROXY_HOST, "");
	}
	
	public void setProxyHost(String host) {
		prefs.put(HTTP_PROXY_HOST, host);
	}
	
	public int getProxyPort() {
		return prefs.getInt(HTTP_PROXY_PORT, 8080);
	}

	public void setProxyPort(int port) {
		prefs.putInt(HTTP_PROXY_PORT, port);
	}
	
	public void setProxyEnabled(boolean b) {
		prefs.putBoolean(HTTP_PROXY_ENABLED, b);
	}
	
	public boolean getProxyEnabled() {
		return prefs.getBoolean(HTTP_PROXY_ENABLED, false);
	}
}
