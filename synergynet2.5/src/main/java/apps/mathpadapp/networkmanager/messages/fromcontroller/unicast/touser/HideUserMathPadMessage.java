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
 * The Class HideUserMathPadMessage.
 */
public class HideUserMathPadMessage extends ControllerToUserMessage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1558455400434548461L;
	
	/** The is hide. */
	protected boolean isHide = false;

	/**
	 * Instantiates a new hide user math pad message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param tableId
	 *            the table id
	 * @param userId
	 *            the user id
	 * @param isHide
	 *            the is hide
	 */
	public HideUserMathPadMessage(Class<?> targetClass, TableIdentity tableId,
			UserIdentity userId, boolean isHide) {
		super(targetClass);
		this.setRecipient(tableId);
		this.setRecipientUser(userId);
		this.isHide = isHide;
	}

	/**
	 * Hide pad.
	 *
	 * @param isShown
	 *            the is shown
	 */
	public void hidePad(boolean isShown) {
		this.isHide = isShown;
	}

	/**
	 * Checks if is hide pad.
	 *
	 * @return true, if is hide pad
	 */
	public boolean isHidePad() {
		return isHide;
	}
}
