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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IFrameImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.items.utils.ImageInfo;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;

import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class JMEFrame.
 */
public class JMEFrame extends JMEQuadContentItem implements
		IFrameImplementation {

	/** The gfx. */
	protected ImageGraphics gfx;

	/** The graphics image quad. */
	protected GraphicsImageQuad graphicsImageQuad;

	/** The item. */
	protected Frame item;

	/**
	 * Instantiates a new JME frame.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMEFrame(ContentItem contentItem) {
		super(contentItem, new GraphicsImageQuad(contentItem.getName(), 250,
				250, 250, 250));
		graphicsImageQuad = (GraphicsImageQuad) this.spatial;
		gfx = graphicsImageQuad.getImageGraphics();
		item = (Frame) contentItem;
		graphicsImageQuad.setLocalTranslation(0, 0, 0);
	}

	/**
	 * Draw.
	 */
	protected void draw() {

	}
	
	/**
	 * Draw background.
	 */
	protected void drawBackground() {
		gfx.setColor(item.getBackgroundColour());
		gfx.fillRect(0, 0, item.getWidth(), item.getHeight());
		
	}
	
	/**
	 * Draw border.
	 */
	protected void drawBorder() {
		int borderSize = item.getBorderSize();
		gfx.setColor(item.getBorderColour());

		// draw bottom border
		gfx.fillRect(0, 0, item.getWidth(), borderSize);
		// draw left border
		gfx.fillRect(0, 0, borderSize, item.getHeight());
		// draw top border
		gfx.fillRect(0, item.getHeight() - borderSize, item.getWidth(),
				borderSize);
		// draw right border
		gfx.fillRect(item.getWidth() - borderSize, 0, borderSize,
				item.getHeight());

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#drawImage(java.net.URL)
	 */
	@Override
	public void drawImage(URL imageResource) {
		render();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#drawImage(java.net.URL, int, int, int, int)
	 */
	@Override
	public void drawImage(URL imageResource, int x, int y, int width, int height) {
		render();
	}
	
	/**
	 * Draw images.
	 */
	protected void drawImages() {
		// draw images
		if (item.getImageResources() != null) {
			for (ImageInfo imgInfo : item.getImageResources().values()) {
				if (imgInfo.getImageResource() != null) {
					Image image = new ImageIcon(imgInfo.getImageResource())
							.getImage();
					gfx.drawImage(image, imgInfo.getX(), imgInfo.getY(),
							imgInfo.getWidth(), imgInfo.getHeight(), null);
				}
			}
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#flushGraphics()
	 */
	@Override
	public void flushGraphics() {
		graphicsImageQuad.updateGraphics();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#getGraphicsContext()
	 */
	@Override
	public Graphics2D getGraphicsContext() {
		return gfx;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#getImages()
	 */
	@Override
	public HashMap<URL, ImageInfo> getImages() {
		return item.getImages();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#
	 * init()
	 */
	@Override
	public void init() {
		super.init();
		resize();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#removeAllImages()
	 */
	@Override
	public void removeAllImages() {
		render();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#removeImage(java.net.URL)
	 */
	@Override
	public void removeImage(URL imageResource) {
		render();
	}
	
	/**
	 * Render.
	 */
	protected void render() {

		// draw background
		this.drawBackground();

		// draw content
		draw();
		
		// draw border
		drawBorder();

		// draw images
		drawImages();

		this.graphicsImageQuad.updateGeometricState(0f, false);
		graphicsImageQuad.updateGraphics();
	}

	/**
	 * Resize.
	 */
	protected void resize() {

		int w = item.getWidth();
		int h = item.getHeight();
		this.graphicsImageQuad.updateGeometry(w, h);

		if (w < 2) {
			w = 2;
		}
		if (h < 2) {
			h = 2;
		}
		this.graphicsImageQuad.recreateImageForSize(w, h);
		gfx = graphicsImageQuad.getImageGraphics();

		render();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem
	 * #setAutoFitSize(boolean)
	 */
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		render();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #setBackGround(synergynetframework.appsystem.contentsystem.items.utils.
	 * Background)
	 */
	@Override
	public void setBackGround(Background backGround) {
		render();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #
	 * setBorder(synergynetframework.appsystem.contentsystem.items.utils.Border)
	 */
	@Override
	public void setBorder(Border border) {
		render();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem
	 * #setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
		resize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem
	 * #setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		resize();
	}
	
}
