/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

/**
 * The Class UnicastSyncWithUserMessage.
 */
public class UnicastSyncWithUserMessage extends ControllerToUserMessage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4567182029942298153L;

	/** The is synchronisation on. */
	private boolean isSynchronisationOn;

	/**
	 * Instantiates a new unicast sync with user message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param tableId
	 *            the table id
	 * @param userId
	 *            the user id
	 * @param isSynchronisation
	 *            the is synchronisation
	 */
	public UnicastSyncWithUserMessage(Class<?> targetClass,
			TableIdentity tableId, UserIdentity userId,
			boolean isSynchronisation) {
		super(targetClass);
		this.isSynchronisationOn = isSynchronisation;
		this.setRecipient(tableId);
		this.setRecipientUser(userId);
	}
	
	/**
	 * Checks if is synchronisation on.
	 *
	 * @return true, if is synchronisation on
	 */
	public boolean isSynchronisationOn() {
		return isSynchronisationOn;
	}
	
	/**
	 * Sets the synchronisation on.
	 *
	 * @param isSynchronisationOn
	 *            the new synchronisation on
	 */
	public void setSynchronisationOn(boolean isSynchronisationOn) {
		this.isSynchronisationOn = isSynchronisationOn;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Enable Synchronisation Message";
	}
	
}
