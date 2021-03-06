/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.appsystem.contentsystem.items;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContainerImplementation;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;

import com.jme.scene.Node;

/**
 * The Class OrthoContainer.
 */
public class OrthoContainer extends OrthoContentItem implements
		IOrthoContainerImplementation {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2782161111250663419L;

	/** The sub content items. */
	private List<ContentItem> subContentItems = new ArrayList<ContentItem>();

	/**
	 * Instantiates a new ortho container.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public OrthoContainer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContainerImplementation#addSubItem(synergynetframework.appsystem.
	 * contentsystem.items.ContentItem)
	 */
	public void addSubItem(ContentItem contentItem) {

		if (!subContentItems.contains(contentItem)) {
			subContentItems.add(contentItem);
			((OrthoContentItem) contentItem).parent = this;
			((IOrthoContainerImplementation) this.contentItemImplementation)
					.addSubItem(contentItem);
			
			// update the multitouch element status of all the sub items
			this.setRotateTranslateScalable(this
					.isRotateTranslateScaleEnabled());
			this.setBringToTopable(this.isBringToTopEnabled());
		}
	}

	/**
	 * Contains.
	 *
	 * @param contentItem
	 *            the content item
	 * @return true, if successful
	 */
	public boolean contains(ContentItem contentItem) {
		return subContentItems.contains(contentItem);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContainerImplementation#detachSubItem(synergynetframework.appsystem
	 * .contentsystem.items.ContentItem)
	 */
	@Override
	public void detachSubItem(ContentItem contentItem) {
		if (subContentItems.contains(contentItem)) {
			this.subContentItems.remove(contentItem);
			((OrthoContentItem) contentItem).parent = null;
			((IOrthoContainerImplementation) this.contentItemImplementation)
					.detachSubItem(contentItem);
		}
	}

	/**
	 * Gets the all items include system items.
	 *
	 * @return the all items include system items
	 */
	public List<ContentItem> getAllItemsIncludeSystemItems() {
		return this.subContentItems;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContainerImplementation#getNode()
	 */
	@Override
	public Node getNode() {
		return ((IOrthoContainerImplementation) this.contentItemImplementation)
				.getNode();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IOrthoContainerImplementation#removeSubItem(synergynetframework.appsystem
	 * .contentsystem.items.ContentItem)
	 */
	public void removeSubItem(ContentItem contentItem) {
		this.removeSubItem(contentItem, true);
	}

	/**
	 * Removes the sub item.
	 *
	 * @param contentItem
	 *            the content item
	 * @param releaseTextures
	 *            the release textures
	 */
	public void removeSubItem(ContentItem contentItem, boolean releaseTextures) {
		if (subContentItems.contains(contentItem)) {
			this.subContentItems.remove(contentItem);
			((OrthoContentItem) contentItem).parent = null;
			((IOrthoContainerImplementation) this.contentItemImplementation)
					.removeSubItem(contentItem);
			contentSystem.removeContentItem(contentItem, releaseTextures);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * setBringToTopable(boolean)
	 */
	public void setBringToTopable(boolean isEnabled) {
		super.setBringToTopable(isEnabled);

		for (ContentItem item : subContentItems) {
			((OrthoContentItem) item).setBringToTopable(isEnabled);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * setRotateTranslateScalable(boolean)
	 */
	public void setRotateTranslateScalable(boolean isEnabled) {
		super.setRotateTranslateScalable(isEnabled);

		for (ContentItem item : subContentItems) {
			((OrthoContentItem) item).setRotateTranslateScalable(isEnabled);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * setScaleLimit(float, float)
	 */
	public void setScaleLimit(float min, float max) {
		for (ContentItem item : subContentItems) {
			((OrthoContentItem) item).setScaleLimit(min, max);
		}
	}

	/**
	 * Sets the top item.
	 *
	 * @param item
	 *            the new top item
	 */
	public void setTopItem(ContentItem item) {
		if (!subContentItems.contains(item)) {
			return;
		}
		
		// Subtract1 from all orders
		for (ContentItem subItem : subContentItems) {
			OrthoContentItem orthoItem = (OrthoContentItem) subItem;
			/* if(orthoItem.getOrder() > 0) */orthoItem.setOrder(orthoItem
					.getOrder() - 1);
		}

		((OrthoContentItem) item).setOrder(OrthoBringToTop.topMost);
		((IOrthoContainerImplementation) this.contentItemImplementation)
				.updateOrder(OrthoBringToTop.topMost);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IOrthoContainerImplementation#updateOrder(int)
	 */
	public void updateOrder(int order) {
		((IOrthoContainerImplementation) this.contentItemImplementation)
				.updateOrder(order);
	}
}