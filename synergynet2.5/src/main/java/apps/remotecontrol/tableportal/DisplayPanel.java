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

package apps.remotecontrol.tableportal;

// import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
// import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
// import com.jme.math.Vector3f;
import apps.remotecontrol.networkmanager.managers.syncmanager.SyncRenderer;
import apps.remotecontrol.tableportal.TablePortal.OperationMode;
import apps.remotecontrol.tableportal.TablePortal.TraceMode;

import com.jme.renderer.ColorRGBA;
// import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.BlendState;
// import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;

/**
 * The Class DisplayPanel.
 */
public class DisplayPanel {

	/** The alpha state. */
	protected BlendState alphaState;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The line colour. */
	protected ColorRGBA lineColour = ColorRGBA.white;

	/** The line width. */
	protected float lineWidth = 2f;

	/** The online items list. */
	protected Map<String, ContentItem> onlineItemsList = new HashMap<String, ContentItem>();

	/** The portal. */
	protected TablePortal portal;

	/** The sync renderer. */
	protected SyncRenderer syncRenderer;

	/** The traces. */
	protected Queue<Line> traces = new ConcurrentLinkedQueue<Line>();

	/** The window. */
	protected Window window;
	
	/**
	 * Instantiates a new display panel.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param portal
	 *            the portal
	 */
	public DisplayPanel(ContentSystem contentSystem, TablePortal portal) {
		this.contentSystem = contentSystem;
		this.portal = portal;
		syncRenderer = new SyncRenderer();
		window = (Window) contentSystem.createContentItem(Window.class);
		window.getBackgroundFrame().setVisible(false);
		window.getBackgroundFrame().setRotateTranslateScalable(false);
		
		alphaState = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		alphaState.setBlendEnabled(true);
		alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaState
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaState.setTestEnabled(false);
		alphaState.setTestFunction(BlendState.TestFunction.GreaterThan);
		alphaState.setEnabled(true);
	}

	/**
	 * Clear traces.
	 */
	public void clearTraces() {
		Iterator<Line> iter = traces.iterator();
		while (iter.hasNext()) {
			iter.next().removeFromParent();
			iter.remove();
		}
	}
	
	/**
	 * Gets the online items.
	 *
	 * @return the online items
	 */
	public List<ContentItem> getOnlineItems() {
		return new ArrayList<ContentItem>(onlineItemsList.values());
	}

	/**
	 * Gets the window.
	 *
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}
	
	/**
	 * Register content item.
	 *
	 * @param item
	 *            the item
	 */
	public void registerContentItem(ContentItem item) {
		boolean isVisible = item.isVisible();
		
		if (!onlineItemsList.containsKey(portal.id + "@" + portal.remoteTableId
				+ "@" + item.getName())) {
			contentSystem.addContentItem(item);
			String name = item.getName();
			if (item.getName().contains("@")) {
				name = name.substring(name.lastIndexOf("@"));
			}
			item.setName(portal.id + "@" + portal.remoteTableId + "@"
					+ item.getName());
			window.addSubItem(item);
			if (item instanceof OrthoContentItem) {
				((OrthoContentItem) item).setLocation(((OrthoContentItem) item)
						.getLocalLocation());
			}
			onlineItemsList.put(item.getName(), item);
			portal.syncManager.addSyncListeners(item);
			this.getWindow().setOrder(9999);
			if (((OrthoContentItem) item).isRotateTranslateScaleEnabled()) {
				((OrthoContentItem) item).setRotateTranslateScalable(true,
						false, this.getWindow().getBackgroundFrame());
			}
			if (((OrthoContentItem) item).isBringToTopEnabled()) {
				((OrthoContentItem) item)
						.addBringToTopListener(new BringToTopListener() {
							
							@Override
							public void itemBringToToped(ContentItem item) {
								DisplayPanel.this.getWindow().setTopItem(item);
								DisplayPanel.this.getWindow().setOrder(9999);
							}
						});
			}
			((OrthoContentItem) item)
					.addScreenCursorListener(new ScreenCursorListener() {
						
						@Override
						public void screenCursorChanged(ContentItem item,
								long id, float x, float y, float pressure) {
						}
						
						@Override
						public void screenCursorClicked(ContentItem item,
								long id, float x, float y, float pressure) {
						}
						
						@Override
						public void screenCursorPressed(ContentItem item,
								long id, float x, float y, float pressure) {
							
						}
						
						@Override
						public void screenCursorReleased(ContentItem item,
								long id, float x, float y, float pressure) {
						}
					});
			
			WorkspaceManager.getInstance().addWorkspaceListener(item);
			portal.cullManager.registerItemForClipping(item);
			item.setVisible(isVisible);
		}
	}
	
	/**
	 * Register content items.
	 *
	 * @param items
	 *            the items
	 */
	public void registerContentItems(List<ContentItem> items) {
		for (ContentItem item : items) {
			registerContentItem(item);
			if (item instanceof Frame) {
				((Frame) item).init();
			}
		}
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode
	 *            the new mode
	 */
	protected void setMode(OperationMode mode) {
		for (ContentItem item : onlineItemsList.values()) {
			if (mode == OperationMode.WRITE) {
				((OrthoContentItem) item).setRotateTranslateScalable(true,
						false, this.getWindow().getBackgroundFrame());
				((OrthoContentItem) item).setBringToTopable(true);
				((OrthoContentItem) item)
						.addBringToTopListener(new BringToTopListener() {
							
							@Override
							public void itemBringToToped(ContentItem item) {
								DisplayPanel.this.getWindow().setTopItem(item);
							}
						});
				
			} else {
				((OrthoContentItem) item).setRotateTranslateScalable(false);
				((OrthoContentItem) item).setBringToTopable(false);
				((OrthoContentItem) item).removeBringToTopListeners();
			}
		}
	}

	/**
	 * Sync content.
	 *
	 * @param itemSyncDataMap
	 *            the item sync data map
	 */
	protected void syncContent(Map<String, Map<Short, Object>> itemSyncDataMap) {
		if (onlineItemsList.isEmpty()) {
			return;
		}
		for (String name : itemSyncDataMap.keySet()) {
			if (!onlineItemsList.containsKey(portal.id + "@"
					+ portal.remoteTableId + "@" + name)) {
				continue;
			}
			OrthoContentItem item = (OrthoContentItem) onlineItemsList
					.get(portal.id + "@" + portal.remoteTableId + "@" + name);
			// float old_x = item.getLocalLocation().x;
			// float old_y = item.getLocalLocation().y;
			syncRenderer.renderSyncData(item, itemSyncDataMap.get(name));
			/*
			 * Line line = new Line(UUID.randomUUID().toString(), new
			 * Vector3f[]{new Vector3f(old_x, old_y,0), new
			 * Vector3f(item.getLocalLocation().x,
			 * item.getLocalLocation().y,0)}, null, new ColorRGBA[]{lineColour,
			 * lineColour}, null); line.setLineWidth(lineWidth);
			 * //line.setLightCombineMode(LightCombineMode.Off); Color r =
			 * portal.getWindow().getBackgroundColour(); line.setSolidColor(new
			 * ColorRGBA(r.getRed(), r.getGreen(), r.getBlue(),r.getAlpha()));
			 * if(portal.getTraceMode().equals(TraceMode.ITEMS_ONLY))
			 * line.setCullHint(CullHint.Always);
			 * line.setRenderState(alphaState);
			 * line.setRenderQueueMode(Renderer.QUEUE_ORTHO); ZBufferState zbs =
			 * DisplaySystem
			 * .getDisplaySystem().getRenderer().createZBufferState();
			 * zbs.setEnabled(false); line.setRenderState(zbs);
			 * this.getWindow().getNode().attachChild(line);
			 * line.setZOrder(999999); line.updateGeometricState(0, true);
			 * this.getWindow().getNode().updateGeometricState(0, true);
			 * line.updateRenderState(); traces.add(line);
			 */
		}
	}
	
	/**
	 * Unregister all items.
	 */
	public void unregisterAllItems() {
		for (ContentItem item : onlineItemsList.values()) {
			portal.syncManager.unregisterContentItem(item);
			WorkspaceManager.getInstance().unregisterContentItem(item);
			window.removeSubItem(item);
		}
		onlineItemsList.clear();
	}

	/**
	 * Unregister item.
	 *
	 * @param itemName
	 *            the item name
	 */
	public void unregisterItem(String itemName) {
		ContentItem contentItem = onlineItemsList.remove(itemName);
		window.removeSubItem(contentItem);
	}
	
	/**
	 * Update trace mode.
	 *
	 * @param currentTraceMode
	 *            the current trace mode
	 */
	public void updateTraceMode(TraceMode currentTraceMode) {
		if (currentTraceMode.equals(TraceMode.ITEMS_ONLY)) {
			for (Line l : traces) {
				l.setCullHint(CullHint.Always);
			}
		} else {
			for (Line l : traces) {
				l.setCullHint(CullHint.Never);
			}
		}
	}
}
