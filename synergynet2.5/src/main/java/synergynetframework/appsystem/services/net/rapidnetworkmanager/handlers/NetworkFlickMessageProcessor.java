package synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers;

import java.util.concurrent.Callable;

import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.AnnounceTableMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.EnableFlickMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.RegisterTableMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.TransferableContentItem;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.UnregisterTableMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.utils.networkflick.TransferController;

import com.jme.util.GameTaskQueueManager;

/**
 * The Class NetworkFlickMessageProcessor.
 */
public class NetworkFlickMessageProcessor implements MessageProcessor {
	
	/** The transfer controller. */
	protected TransferController transferController;

	/**
	 * Instantiates a new network flick message processor.
	 *
	 * @param transferController
	 *            the transfer controller
	 */
	public NetworkFlickMessageProcessor(TransferController transferController) {
		this.transferController = transferController;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers
	 * .MessageProcessor#process(java.lang.Object)
	 */
	@Override
	public void process(final Object obj) {
		
		if (transferController != null) {
			if (obj instanceof EnableFlickMessage) {
				transferController
						.enableNetworkFlick(((EnableFlickMessage) obj)
								.isFlickEnabled());
			} else if (obj instanceof AnnounceTableMessage) {
				AnnounceTableMessage msg = (AnnounceTableMessage) obj;
				transferController.registerRemoteTable(msg.getTableInfo());
				transferController.sendRegistrationMessage(msg.getSender());
			} else if (obj instanceof RegisterTableMessage) {
				RegisterTableMessage msg = (RegisterTableMessage) obj;
				transferController.registerRemoteTable(msg.getTableInfo());
			} else if (obj instanceof UnregisterTableMessage) {
				transferController
						.cleanUpUnregisteredTable((UnregisterTableMessage) obj);
			} else if (obj instanceof TransferableContentItem) {
				GameTaskQueueManager.getManager().update(
						new Callable<Object>() {
							public Object call() throws Exception {
								transferController
										.applyTransferableContentItem((TransferableContentItem) obj);
								return null;
							}
						});
			}
		}
	}
	
}
