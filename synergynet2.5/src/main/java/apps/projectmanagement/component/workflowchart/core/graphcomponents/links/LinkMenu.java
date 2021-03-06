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

package apps.projectmanagement.component.workflowchart.core.graphcomponents.links;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import apps.projectmanagement.component.workflowchart.core.GraphManager;
import apps.projectmanagement.component.workflowchart.core.MessageFactory;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.GraphComponent.OptionMessageListener;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.OptionMessage;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.GraphNode;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.KeyboardNode;
import apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes.KeyboardNode.KeyboardListener;

/**
 * The Class LinkMenu.
 */
public class LinkMenu {
	
	/** The font size. */
	private static final int FONT_SIZE = 10;

	/** The bg colour. */
	private Color bgColour = Color.ORANGE;

	/** The border colour. */
	private Color borderColour = Color.LIGHT_GRAY;

	/** The border size. */
	private int borderSize = 4;

	/** The container. */
	private OrthoContainer container;

	/** The content system. */
	ContentSystem contentSystem;

	/** The distance between items. */
	private int distanceBetweenItems = 0;

	/** The font. */
	private Font font = new Font("Arial", Font.PLAIN, FONT_SIZE);

	/** The graph manager. */
	protected GraphManager graphManager;

	/** The is visible. */
	private boolean isVisible;

	/** The item height. */
	private int itemHeight = 20;

	/** The item width. */
	private int itemWidth = 100;

	/** The location_y. */
	private float location_x, location_y;
	
	/** The menu buttons. */
	private ArrayList<SimpleButton> menuButtons = new ArrayList<SimpleButton>();

	/** The text colour. */
	private Color textColour = Color.black;
	
	/**
	 * Instantiates a new link menu.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param gManager
	 *            the g manager
	 * @param link
	 *            the link
	 */
	public LinkMenu(ContentSystem contentSystem, GraphManager gManager,
			GraphLink link) {
		this.contentSystem = contentSystem;
		this.graphManager = gManager;
		link.setArrowMode(LineItem.ARROW_TO_TARGET);
		container = (OrthoContainer) contentSystem
				.createContentItem(OrthoContainer.class);
		menuButtons = createLinkMenu(link);
		if (menuButtons != null) {
			for (SimpleButton btn : menuButtons) {
				container.addSubItem(btn);
			}
		}
	}

	/**
	 * Creates the link menu.
	 *
	 * @param item
	 *            the item
	 * @return the array list
	 */
	private ArrayList<SimpleButton> createLinkMenu(GraphLink item) {
		final GraphLink link = item;
		final SimpleButton closeBtn = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		closeBtn.setText("Cancel");
		
		closeBtn.addButtonListener(new SimpleButtonAdapter() {
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (LinkMenu.this.isVisible()) {
					LinkMenu.this.setVisible(false);
				}
			}
		});

		final SimpleButton btn4 = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		btn4.setText("Delete");
		
		btn4.setBorderSize(borderSize);
		btn4.addButtonListener(new SimpleButtonAdapter() {
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (LinkMenu.this.isVisible()) {
					System.out.println("Button clicked : Delete!");
					
					OptionMessage msg = MessageFactory
							.getInstance()
							.createOptionMessage(
									contentSystem,
									graphManager,
									link,
									"Are you sure you want to delete this link?",
									MessageFactory.OK_CANCEL_MESSAGE);
					msg.setLocation(link.getLinkPoint().getLocation().x, link
							.getLinkPoint().getLocation().y);
					msg.setOrder(LinkMenu.this.getOrder() + 1);
					link.addOptionMessageListener(new OptionMessageListener() {
						
						@Override
						public void messageProcessed(OptionMessage msg) {
							if (msg.getParentComponent().getName()
									.equals(link.getName())) {
								if (msg.getSelectedOption() == 0) {
									msg.remove();
									graphManager.detachGraphNode(msg);
									link.remove();
									graphManager.detachGraphLink(link);
									LinkMenu.this.remove();
									if (link.getKeyboardNode() != null) {
										link.getKeyboardNode().remove();
										graphManager.detachGraphNode(link
												.getKeyboardNode());
									}
								} else if (msg.getSelectedOption() == 1) {
									msg.remove();
									graphManager.detachGraphNode(msg);
								}
							}

						}
					});
				}
			}
		});

		menuButtons.add(btn4);
		menuButtons.add(closeBtn);
		
		this.setLocation(location_x, location_y);
		this.setTextColour(textColour);
		this.setBorderColour(borderColour);
		this.setBgColour(bgColour);
		this.setFont(font);
		this.setMenuItemSize(itemWidth, itemHeight);
		this.setVisible(false);
		
		return menuButtons;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return new Location(location_x, location_y, 0);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return container.getName();
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return container.getOrder();
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Removes the.
	 */
	public void remove() {
		contentSystem.removeContentItem(container);
	}
	
	/**
	 * Sets the bg colour.
	 *
	 * @param bgColour
	 *            the new bg colour
	 */
	public void setBgColour(Color bgColour) {
		this.bgColour = bgColour;
		for (SimpleButton btn : menuButtons) {
			btn.setBackgroundColour(bgColour);
		}
	}

	/**
	 * Sets the border colour.
	 *
	 * @param borderColour
	 *            the new border colour
	 */
	public void setBorderColour(Color borderColour) {
		this.borderColour = borderColour;
		for (SimpleButton btn : menuButtons) {
			btn.setBorderColour(borderColour);
		}
	}

	/**
	 * Sets the font.
	 *
	 * @param font
	 *            the new font
	 */
	public void setFont(Font font) {
		this.font = font;
		for (SimpleButton btn : menuButtons) {
			btn.setFont(font);
		}
	}

	/**
	 * Sets the location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setLocation(float x, float y) {
		location_x = x;
		location_y = y;
		float shift = 0;
		for (SimpleButton btn : menuButtons) {
			btn.setLocalLocation(x, y - shift);
			shift += itemHeight + distanceBetweenItems;
		}
	}
	
	/**
	 * Sets the menu item size.
	 *
	 * @param itemWidth
	 *            the item width
	 * @param itemHeight
	 *            the item height
	 */
	public void setMenuItemSize(int itemWidth, int itemHeight) {
		this.itemWidth = itemWidth;
		this.itemHeight = itemHeight;
		for (SimpleButton btn : menuButtons) {
			btn.setAutoFitSize(false);
			btn.setWidth(itemWidth);
			btn.setHeight(itemHeight);
		}
	}

	/**
	 * Sets the order.
	 *
	 * @param zOrder
	 *            the new order
	 */
	public void setOrder(int zOrder) {
		container.setOrder(zOrder);
	}
	
	/**
	 * Sets the text colour.
	 *
	 * @param textColour
	 *            the new text colour
	 */
	public void setTextColour(Color textColour) {
		this.textColour = textColour;
		for (SimpleButton btn : menuButtons) {
			btn.setTextColour(textColour);
		}
	}
	
	/**
	 * Sets the visible.
	 *
	 * @param isVisible
	 *            the new visible
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
		this.container.setVisible(isVisible);
	}

	/**
	 * Show and link keyboard.
	 *
	 * @param link
	 *            the link
	 */
	@SuppressWarnings("unused")
	private void showAndLinkKeyboard(final GraphLink link) {

		if (link.getKeyboardNode() == null) {
			final KeyboardNode keyboardNode = new KeyboardNode(contentSystem,
					graphManager);
			link.setKeyboardNode(keyboardNode);
			keyboardNode.setLinkButtonLocation(GraphNode.MIDDLE);
			keyboardNode.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
			keyboardNode.setLocation(link.getLinkPoint().getLocation().x, link
					.getLinkPoint().getLocation().y);
			keyboardNode.getLinkButton().setVisible(false);
			keyboardNode.setLinkable(false);
			keyboardNode.setScale(0.5f);
			keyboardNode.setOrder(link.getOrder() + 1);
			keyboardNode.getCloseButton().addButtonListener(
					new SimpleButtonAdapter() {
						public void buttonClicked(SimpleButton b, long id,
								float x, float y, float pressure) {
							link.setKeyboardNode(null);
						}
					});
			final GraphLink linkToKeyboard = new GraphLink(contentSystem,
					graphManager);
			GraphNode linkPoint = link.getLinkPoint();
			linkToKeyboard.setSourceNode(linkPoint);
			linkToKeyboard.setTargetNode(keyboardNode);
			linkToKeyboard.setLinkMode(LineItem.SEGMENT_LINE);
			linkToKeyboard.setArrowMode(LineItem.NO_ARROWS);
			linkToKeyboard.setMenuEnabled(false);
			LinkMenu.this.setVisible(false);
			keyboardNode.addKeyListener(new KeyboardListener() {
				
				public void keyPressed(KeyEvent evt) {
				}
				
				public void keyReleased(KeyEvent evt) {
					String text = link.getText();
					if (text == null) {
						text = "";
					}
					if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
						keyboardNode.remove();
						linkToKeyboard.remove();
						link.setMenuEnabled(true);
					}

					if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
						if (text.length() > 0) {
							text = text.substring(0, text.length() - 1);
							link.setText(text);
						}
					} else if (evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK) {
						link.setText(text + evt.getKeyChar());
					}
				}
			});
			link.removeMenu();
		}
	}
}
