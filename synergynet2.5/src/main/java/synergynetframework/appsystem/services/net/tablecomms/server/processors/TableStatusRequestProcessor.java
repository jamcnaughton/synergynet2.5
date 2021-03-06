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

package synergynetframework.appsystem.services.net.tablecomms.server.processors;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromserver.TableStatusResponse;
import synergynetframework.appsystem.services.net.tablecomms.server.ServerMessageProcessor;
import synergynetframework.appsystem.services.net.tablecomms.server.TableCommsServerService;

/**
 * The Class TableStatusRequestProcessor.
 */
public class TableStatusRequestProcessor implements ServerMessageProcessor {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(TableStatusRequestProcessor.class.getName());
	
	/**
	 * Creates the table status response.
	 *
	 * @param server
	 *            the server
	 * @return the table status response
	 */
	public TableStatusResponse createTableStatusResponse(
			TableCommsServerService server) {
		TableStatusResponse response = new TableStatusResponse();
		for (TableIdentity id : server.getReceivers().keySet()) {
			response.addStatus(id, TableStatusResponse.STATUS_ONLINE);
		}
		return response;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.server.
	 * ServerMessageProcessor
	 * #handle(synergynetframework.appsystem.services.net.tablecomms
	 * .server.TableCommsServerService,
	 * synergynetframework.appsystem.services.net
	 * .objectmessaging.connections.ConnectionHandler,
	 * synergynetframework.appsystem
	 * .services.net.tablecomms.messages.TableMessage)
	 */
	public void handle(TableCommsServerService server,
			ConnectionHandler fromHandler, TableMessage obj) throws IOException {
		sendTableStatusResponse(server, fromHandler);
		log.info("handle() " + obj);
	}

	/**
	 * Send table status response.
	 *
	 * @param server
	 *            the server
	 * @param handlers
	 *            the handlers
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void sendTableStatusResponse(TableCommsServerService server,
			Collection<ConnectionHandler> handlers) throws IOException {
		TableStatusResponse response = createTableStatusResponse(server);
		for (ConnectionHandler h : handlers) {
			h.sendMessage(response);
		}
	}
	
	/**
	 * Send table status response.
	 *
	 * @param server
	 *            the server
	 * @param handlers
	 *            the handlers
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void sendTableStatusResponse(TableCommsServerService server,
			ConnectionHandler... handlers) throws IOException {
		TableStatusResponse response = createTableStatusResponse(server);

		for (ConnectionHandler h : handlers) {
			h.sendMessage(response);
		}
	}
}
