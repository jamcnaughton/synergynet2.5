package synergynetframework.appsystem.contentsystem.items.utils.vnc;

//
// Copyright (C) 2001 HorizonLive.com, Inc. All Rights Reserved.
// Copyright (C) 1999 AT&T Laboratories Cambridge. All Rights Reserved.
//
// This is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This software is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this software; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
// USA.
//

//
// Clipboard frame.
//

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * The Class ClipboardFrame.
 */
class ClipboardFrame extends Frame implements WindowListener, ActionListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5190334595915692477L;
	
	/** The close button. */
	Button clearButton, closeButton;
	
	/** The selection. */
	String selection;
	
	/** The text area. */
	TextArea textArea;
	
	/** The viewer. */
	VncViewer viewer;
	
	//
	// Constructor.
	//
	
	/**
	 * Instantiates a new clipboard frame.
	 *
	 * @param v
	 *            the v
	 */
	ClipboardFrame(VncViewer v) {
		super("TightVNC Clipboard");
		
		viewer = v;
		
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		
		textArea = new TextArea(5, 40);
		gridbag.setConstraints(textArea, gbc);
		add(textArea);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridwidth = 1;
		
		clearButton = new Button("Clear");
		gridbag.setConstraints(clearButton, gbc);
		add(clearButton);
		clearButton.addActionListener(this);
		
		closeButton = new Button("Close");
		gridbag.setConstraints(closeButton, gbc);
		add(closeButton);
		closeButton.addActionListener(this);
		
		pack();
		
		addWindowListener(this);
	}
	
	//
	// Set the cut text from the RFB server.
	//
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == clearButton) {
			textArea.setText("");
		} else if (evt.getSource() == closeButton) {
			setVisible(false);
		}
	}
	
	//
	// When the focus leaves the window, see if we have new cut text and
	// if so send it to the RFB server.
	//
	
	/**
	 * Sets the cut text.
	 *
	 * @param text
	 *            the new cut text
	 */
	void setCutText(String text) {
		selection = text;
		textArea.setText(text);
		if (isVisible()) {
			textArea.selectAll();
		}
	}
	
	//
	// Close our window properly.
	//
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent evt) {
	}
	
	//
	// Ignore window events we're not interested in.
	//
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent evt) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent evt) {
		setVisible(false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent
	 * )
	 */
	public void windowDeactivated(WindowEvent evt) {
		if ((selection != null) && !selection.equals(textArea.getText())) {
			selection = textArea.getText();
			viewer.setCutText(selection);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent
	 * )
	 */
	public void windowDeiconified(WindowEvent evt) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent evt) {
	}
	
	//
	// Respond to button presses
	//
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent evt) {
	}
}
