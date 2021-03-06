package synergynetframework.appsystem.contentsystem.items.utils.vnc;

//
// Copyright (C) 2001-2004 HorizonLive.com, Inc. All Rights Reserved.
// Copyright (C) 2002 Constantin Kaplinsky. All Rights Reserved.
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
// VncViewer.java - the VNC viewer applet. This class mainly just sets up the
// user interface, leaving it to the VncCanvas to do the actual rendering of
// a VNC desktop.
//

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/**
 * The Class VncViewer.
 */
public class VncViewer extends JFrame implements java.lang.Runnable,
		WindowListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5802365296549275285L;
	/** The button panel. */
	ButtonPanel buttonPanel;
	
	/** The clipboard. */
	ClipboardFrame clipboard;
	
	/** The conn status label. */
	Label connStatusLabel;
	
	/** The cursor updates def. */
	String cursorUpdatesDef;
	
	/** The debug stats exclude updates. */
	int debugStatsExcludeUpdates = 0;
	
	/** The debug stats measure updates. */
	int debugStatsMeasureUpdates = 0;
	
	/** The defer cursor updates. */
	int deferCursorUpdates = 10;
	
	/** The defer screen updates. */
	int deferScreenUpdates = 20;
	
	/** The defer update requests. */
	int deferUpdateRequests = 0;
	
	/** The desktop scroll pane. */
	ScrollPane desktopScrollPane;
	
	/** The eight bit colors def. */
	String eightBitColorsDef;
	
	/** The encodings saved. */
	int[] encodingsSaved;
	
	/** The gridbag. */
	GridBagLayout gridbag;
	
	/** The host. */
	String host;// = mainArgs[1];
	
	// 192.168.137.1
	/** The in an applet. */
	boolean inAnApplet = false;
	
	/** The in separate frame. */
	boolean inSeparateFrame = true;
	
	/** The main args. */
	public String[] mainArgs = { "HOST", "", "PORT", "", "PASSWORD", "" };
	
	/** The n encodings saved. */
	int nEncodingsSaved;
	
	/** The offer relogin. */
	boolean offerRelogin = true;
	
	/** The options. */
	OptionsFrame options;
	
	/** The password param. */
	String passwordParam;// = mainArgs[5];
	
	/** The port. */
	int port;// = Integer.parseInt(mainArgs[3]);
	
	/** The rec. */
	RecordingFrame rec;
	
	/** The recording active. */
	boolean recordingActive;
	
	/** The recording status changed. */
	boolean recordingStatusChanged;
	
	// Control session recording.
	/** The recording sync. */
	Object recordingSync;
	
	/** The rfb. */
	RfbProto rfb;
	
	/** The rfb thread. */
	Thread rfbThread;
	
	/** The session file name. */
	String sessionFileName;
	
	/** The show controls. */
	boolean showControls = true;
	
	/** The show offline desktop. */
	boolean showOfflineDesktop = false;
	
	// Variables read from parameter values.
	/** The socket factory. */
	String socketFactory;
	
	// Reference to this applet for inter-applet communication.
	// public static java.applet.Applet refApplet;
	
	//
	// init()
	//
	
	/** The vc. */
	public VncCanvas vc;
	
	// Frame vncFrame;
	/** The vnc container. */
	Container vncContainer;
	
	//
	// run() - executed by the rfbThread to deal with the RFB socket.
	//
	
	/**
	 * Instantiates a new vnc viewer.
	 *
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @param password
	 *            the password
	 */
	public VncViewer(String host, int port, String password) {
		System.out.println("Hello");
		mainArgs[1] = host;
		mainArgs[3] = String.valueOf(port);
		mainArgs[5] = password;
		host = mainArgs[1];
		port = Integer.parseInt(mainArgs[3]);
		passwordParam = mainArgs[5];
		
		// refApplet = this;
		
		if (inSeparateFrame) {
			this.setTitle("Hello World!!!");
			if (!inAnApplet) {
				// vncFrame.add("Center", this);
			}
			vncContainer = this;
		} else {
			vncContainer = this;
		}
		
		recordingSync = new Object();
		
		options = new OptionsFrame(this);
		clipboard = new ClipboardFrame(this);
		
		if (RecordingFrame.checkSecurity()) {
			rec = new RecordingFrame(this);
		}
		
		sessionFileName = null;
		recordingActive = false;
		recordingStatusChanged = false;
		cursorUpdatesDef = null;
		eightBitColorsDef = null;
		
		// if (inSeparateFrame)
		// this.addWindowListener(this);
		
		rfbThread = new Thread(this);
		rfbThread.start();
		
	}
	
	//
	// Create a VncCanvas instance.
	//
	
	/**
	 * Ask password.
	 *
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	String askPassword() throws Exception {
		showConnectionStatus(null);
		
		AuthPanel authPanel = new AuthPanel(this);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.ipadx = 100;
		gbc.ipady = 50;
		gridbag.setConstraints(authPanel, gbc);
		vncContainer.add(authPanel);
		
		if (inSeparateFrame) {
			this.pack();
		} else {
			validate();
		}
		
		authPanel.moveFocusToDefaultField();
		String pw = authPanel.getPassword();
		vncContainer.remove(authPanel);
		
		return pw;
	}
	
	//
	// Process RFB socket messages.
	// If the rfbThread is being stopped, ignore any exceptions,
	// otherwise rethrow the exception so it can be handled.
	//
	
	/**
	 * Auto select encodings.
	 */
	void autoSelectEncodings() {
		setEncodings(true);
	}
	
	/**
	 * Check recording status.
	 *
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	boolean checkRecordingStatus() throws IOException {
		synchronized (recordingSync) {
			if (recordingStatusChanged) {
				recordingStatusChanged = false;
				if (sessionFileName != null) {
					startRecording();
					return true;
				} else {
					stopRecording();
				}
			}
		}
		return false;
	}
	
	//
	// Show a message describing the connection status.
	// To hide the connection status label, use (msg == null).
	//
	
	/**
	 * Connect and authenticate.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("deprecation")
	//
	// Connect to the RFB server and authenticate the user.
	//
	void connectAndAuthenticate() throws Exception {
		showConnectionStatus("Initializing...");
		if (inSeparateFrame) {
			this.pack();
			// this.show();
			this.hide();
		} else {
			validate();
		}
		
		showConnectionStatus("Connecting to " + host + ", port " + port + "...");
		
		host = mainArgs[1];
		port = Integer.parseInt(mainArgs[3]);
		passwordParam = mainArgs[5];
		
		rfb = new RfbProto(host, port, this);
		showConnectionStatus("Connected to server");
		
		rfb.readVersionMsg();
		showConnectionStatus("RFB server supports protocol version "
				+ rfb.serverMajor + "." + rfb.serverMinor);
		
		rfb.writeVersionMsg();
		showConnectionStatus("Using RFB protocol version " + rfb.clientMajor
				+ "." + rfb.clientMinor);
		
		int secType = rfb.negotiateSecurity();
		int authType;
		if (secType == RfbProto.SecTypeTight) {
			showConnectionStatus("Enabling TightVNC protocol extensions");
			rfb.setupTunneling();
			authType = rfb.negotiateAuthenticationTight();
		} else {
			authType = secType;
		}
		
		switch (authType) {
			case RfbProto.AuthNone:
				showConnectionStatus("No authentication needed");
				rfb.authenticateNone();
				break;
			case RfbProto.AuthVNC:
				showConnectionStatus("Performing standard VNC authentication");
				if (passwordParam != null) {
					rfb.authenticateVNC(passwordParam);
				} else {
					String pw = askPassword();
					rfb.authenticateVNC(pw);
				}
				break;
			default:
				throw new Exception("Unknown authentication scheme " + authType);
		}
	}
	
	//
	// Show an authentication panel.
	//
	
	/**
	 * Creates the canvas.
	 *
	 * @param maxWidth
	 *            the max width
	 * @param maxHeight
	 *            the max height
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("rawtypes")
	void createCanvas(int maxWidth, int maxHeight) throws IOException {
		// Determine if Java 2D API is available and use a special
		// version of VncCanvas if it is present.
		vc = null;
		try {
			// This throws ClassNotFoundException if there is no Java 2D API.
			Class<?> cl = Class.forName("java.awt.Graphics2D");
			// If we could load Graphics2D class, then we can use VncCanvas2D.
			cl = Class.forName("VncCanvas");
			Class[] argClasses = { this.getClass(), Integer.TYPE, Integer.TYPE };
			java.lang.reflect.Constructor cstr = cl.getConstructor(argClasses);
			Object[] argObjects = { this, new Integer(maxWidth),
					new Integer(maxHeight) };
			vc = (VncCanvas) cstr.newInstance(argObjects);
		} catch (Exception e) {
			System.out.println("Warning: Java 2D API is not available");
		}
		
		// If we failed to create VncCanvas2D, use old VncCanvas.
		if (vc == null) {
			vc = new VncCanvas(this, maxWidth, maxHeight);
		}
	}
	
	//
	// Do the rest of the protocol initialisation.
	//
	
	/**
	 * Destroy.
	 */
	public void destroy() {
		System.out.println("Destroying applet");
		
		vncContainer.removeAll();
		options.dispose();
		clipboard.dispose();
		if (rec != null) {
			rec.dispose();
		}
		if ((rfb != null) && !rfb.closed()) {
			rfb.close();
		}
		if (inSeparateFrame) {
			this.dispose();
		}
	}
	
	//
	// Send current encoding list to the RFB server.
	//
	
	/**
	 * Disconnect.
	 */
	synchronized public void disconnect() {
		System.out.println("Disconnecting");
		
		if (vc != null) {
			double sec = (System.currentTimeMillis() - vc.statStartTime) / 1000.0;
			double rate = Math.round((vc.statNumUpdates / sec) * 100) / 100.0;
			int nRealRects = vc.statNumPixelRects;
			int nPseudoRects = vc.statNumTotalRects - vc.statNumPixelRects;
			System.out.println("Updates received: " + vc.statNumUpdates + " ("
					+ nRealRects + " rectangles + " + nPseudoRects
					+ " pseudo), " + rate + " updates/sec");
			int numRectsOther = nRealRects - vc.statNumRectsTight
					- vc.statNumRectsZRLE - vc.statNumRectsHextile
					- vc.statNumRectsRaw - vc.statNumRectsCopy;
			System.out.println("Rectangles:" + " Tight=" + vc.statNumRectsTight
					+ "(JPEG=" + vc.statNumRectsTightJPEG + ") ZRLE="
					+ vc.statNumRectsZRLE + " Hextile="
					+ vc.statNumRectsHextile + " Raw=" + vc.statNumRectsRaw
					+ " CopyRect=" + vc.statNumRectsCopy + " other="
					+ numRectsOther);
			
			int raw = vc.statNumBytesDecoded;
			int compressed = vc.statNumBytesEncoded;
			if (compressed > 0) {
				double ratio = Math.round(((double) raw / compressed) * 1000) / 1000.0;
				System.out.println("Pixel data: " + vc.statNumBytesDecoded
						+ " bytes, " + vc.statNumBytesEncoded
						+ " compressed, ratio " + ratio);
			}
		}
		
		if ((rfb != null) && !rfb.closed()) {
			rfb.close();
		}
		options.dispose();
		clipboard.dispose();
		if (rec != null) {
			rec.dispose();
		}
		
		if (inAnApplet) {
			showMessage("Disconnected");
		} else {
			// System.exit(0);
		}
	}
	
	/**
	 * Do protocol initialisation.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	void doProtocolInitialisation() throws IOException {
		rfb.writeClientInit();
		rfb.readServerInit();
		
		System.out.println("Desktop name is " + rfb.desktopName);
		System.out.println("Desktop size is " + rfb.framebufferWidth + " x "
				+ rfb.framebufferHeight);
		
		setEncodings();
		
		showConnectionStatus(null);
	}
	
	/**
	 * Enable input.
	 *
	 * @param enable
	 *            the enable
	 */
	public void enableInput(boolean enable) {
		vc.enableInput(enable);
	}
	
	/**
	 * Fatal error.
	 *
	 * @param str
	 *            the str
	 */
	@SuppressWarnings("deprecation")
	synchronized public void fatalError(String str) {
		System.out.println(str);
		
		if (inAnApplet) {
			// vncContainer null, applet not inited,
			// can not present the error to the user.
			Thread.currentThread().stop();
		} else {
			System.exit(1);
		}
	}
	
	/**
	 * Fatal error.
	 *
	 * @param str
	 *            the str
	 * @param e
	 *            the e
	 */
	synchronized public void fatalError(String str, Exception e) {
		
		if ((rfb != null) && rfb.closed()) {
			// Not necessary to show error message if the error was caused
			// by I/O problems after the rfb.close() method call.
			System.out.println("RFB thread finished");
			return;
		}
		
		System.out.println(str);
		e.printStackTrace();
		
		if (rfb != null) {
			rfb.close();
		}
		
		if (inAnApplet) {
			showMessage(str);
		} else {
			System.exit(1);
		}
	}
	
	//
	// setCutText() - send the given cut text to the RFB server.
	//
	
	/**
	 * Move focus to desktop.
	 */
	void moveFocusToDesktop() {
		if (vncContainer != null) {
			if ((vc != null) && vncContainer.isAncestorOf(vc)) {
				vc.requestFocus();
			}
		}
	}
	
	//
	// Order change in session recording status. To stop recording, pass
	// null in place of the fname argument.
	//
	
	/**
	 * Process normal protocol.
	 *
	 * @throws Exception
	 *             the exception
	 */
	void processNormalProtocol() throws Exception {
		try {
			vc.processNormalProtocol();
		} catch (Exception e) {
			if (rfbThread == null) {
				System.out.println("Ignoring RFB socket exceptions"
						+ " because applet is stopping");
			} else {
				throw e;
			}
		}
	}
	
	//
	// Start or stop session recording. Returns true if this method call
	// causes recording of a new session.
	//
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		gridbag = new GridBagLayout();
		vncContainer.setLayout(gridbag);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		if (showControls) {
			buttonPanel = new ButtonPanel(this);
			gridbag.setConstraints(buttonPanel, gbc);
			vncContainer.add(buttonPanel);
		}
		
		try {
			connectAndAuthenticate();
			doProtocolInitialisation();
			
			// FIXME: Use auto-scaling not only in a separate frame.
			if (options.autoScale && inSeparateFrame) {
				Dimension screenSize;
				try {
					screenSize = vncContainer.getToolkit().getScreenSize();
				} catch (Exception e) {
					screenSize = new Dimension(0, 0);
				}
				createCanvas(screenSize.width / 4, screenSize.height / 4);
			} else {
				createCanvas(0, 0);
			}
			
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			
			if (inSeparateFrame) {
				
				// Create a panel which itself is resizeable and can hold
				// non-resizeable VncCanvas component at the top left corner.
				Panel canvasPanel = new Panel();
				canvasPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				canvasPanel.add(vc);
				
				// Create a ScrollPane which will hold a panel with VncCanvas
				// inside.
				desktopScrollPane = new ScrollPane(
						ScrollPane.SCROLLBARS_AS_NEEDED);
				gbc.fill = GridBagConstraints.BOTH;
				gridbag.setConstraints(desktopScrollPane, gbc);
				desktopScrollPane.add(canvasPanel);
				
				// Finally, add our ScrollPane to the Frame window.
				this.add(desktopScrollPane);
				this.setTitle(rfb.desktopName);
				this.pack();
				vc.resizeDesktopFrame();
				
			} else {
				
				// Just add the VncCanvas component to the Applet.
				gridbag.setConstraints(vc, gbc);
				add(vc);
				validate();
			}
			
			if (showControls) {
				buttonPanel.enableButtons();
			}
			
			moveFocusToDesktop();
			processNormalProtocol();
			
		} catch (NoRouteToHostException e) {
			fatalError("Network error: no route to server: " + host, e);
		} catch (UnknownHostException e) {
			fatalError("Network error: server name unknown: " + host, e);
		} catch (ConnectException e) {
			fatalError("Network error: could not connect to server: " + host
					+ ":" + port, e);
		} catch (EOFException e) {
			if (showOfflineDesktop) {
				e.printStackTrace();
				System.out
						.println("Network error: remote side closed connection");
				if (vc != null) {
					vc.enableInput(false);
				}
				if (inSeparateFrame) {
					this.setTitle(rfb.desktopName + " [disconnected]");
				}
				if ((rfb != null) && !rfb.closed()) {
					rfb.close();
				}
				if (showControls && (buttonPanel != null)) {
					buttonPanel.disableButtonsOnDisconnect();
					if (inSeparateFrame) {
						this.pack();
					} else {
						validate();
					}
				}
			} else {
				fatalError("Network error: remote side closed connection", e);
			}
		} catch (IOException e) {
			String str = e.getMessage();
			if ((str != null) && (str.length() != 0)) {
				fatalError("Network Error: " + str, e);
			} else {
				fatalError(e.toString(), e);
			}
		} catch (Exception e) {
			String str = e.getMessage();
			if ((str != null) && (str.length() != 0)) {
				fatalError("Error: " + str, e);
			} else {
				fatalError(e.toString(), e);
			}
		}
	}
	
	//
	// Start session recording.
	//
	
	/**
	 * Sets the cut text.
	 *
	 * @param text
	 *            the new cut text
	 */
	void setCutText(String text) {
		try {
			if ((rfb != null) && rfb.inNormalProtocol) {
				rfb.writeClientCutText(text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//
	// Stop session recording.
	//
	
	/**
	 * Sets the encodings.
	 */
	void setEncodings() {
		setEncodings(false);
	}
	
	//
	// readParameters() - read parameters from the html source or from the
	// command line. On the command line, the arguments are just a sequence of
	// param_name/param_value pairs where the names and values correspond to
	// those expected in the html applet tag source.
	//
	
	/**
	 * Sets the encodings.
	 *
	 * @param autoSelectOnly
	 *            the new encodings
	 */
	void setEncodings(boolean autoSelectOnly) {
		if ((options == null) || (rfb == null) || !rfb.inNormalProtocol) {
			return;
		}
		
		int preferredEncoding = options.preferredEncoding;
		if (preferredEncoding == -1) {
			long kbitsPerSecond = rfb.kbitsPerSecond();
			if (nEncodingsSaved < 1) {
				// Choose Tight or ZRLE encoding for the very first update.
				System.out.println("Using Tight/ZRLE encodings");
				preferredEncoding = RfbProto.EncodingTight;
			} else if ((kbitsPerSecond > 2000)
					&& (encodingsSaved[0] != RfbProto.EncodingHextile)) {
				// Switch to Hextile if the connection speed is above 2Mbps.
				System.out.println("Throughput " + kbitsPerSecond
						+ " kbit/s - changing to Hextile encoding");
				preferredEncoding = RfbProto.EncodingHextile;
			} else if ((kbitsPerSecond < 1000)
					&& (encodingsSaved[0] != RfbProto.EncodingTight)) {
				// Switch to Tight/ZRLE if the connection speed is below 1Mbps.
				System.out.println("Throughput " + kbitsPerSecond
						+ " kbit/s - changing to Tight/ZRLE encodings");
				preferredEncoding = RfbProto.EncodingTight;
			} else {
				// Don't change the encoder.
				if (autoSelectOnly) {
					return;
				}
				preferredEncoding = encodingsSaved[0];
			}
		} else {
			// Auto encoder selection is not enabled.
			if (autoSelectOnly) {
				return;
			}
		}
		
		int[] encodings = new int[20];
		int nEncodings = 0;
		
		encodings[nEncodings++] = preferredEncoding;
		if (options.useCopyRect) {
			encodings[nEncodings++] = RfbProto.EncodingCopyRect;
		}
		
		if (preferredEncoding != RfbProto.EncodingTight) {
			encodings[nEncodings++] = RfbProto.EncodingTight;
		}
		if (preferredEncoding != RfbProto.EncodingZRLE) {
			encodings[nEncodings++] = RfbProto.EncodingZRLE;
		}
		if (preferredEncoding != RfbProto.EncodingHextile) {
			encodings[nEncodings++] = RfbProto.EncodingHextile;
		}
		if (preferredEncoding != RfbProto.EncodingZlib) {
			encodings[nEncodings++] = RfbProto.EncodingZlib;
		}
		if (preferredEncoding != RfbProto.EncodingCoRRE) {
			encodings[nEncodings++] = RfbProto.EncodingCoRRE;
		}
		if (preferredEncoding != RfbProto.EncodingRRE) {
			encodings[nEncodings++] = RfbProto.EncodingRRE;
		}
		
		if ((options.compressLevel >= 0) && (options.compressLevel <= 9)) {
			encodings[nEncodings++] = RfbProto.EncodingCompressLevel0
					+ options.compressLevel;
		}
		if ((options.jpegQuality >= 0) && (options.jpegQuality <= 9)) {
			encodings[nEncodings++] = RfbProto.EncodingQualityLevel0
					+ options.jpegQuality;
		}
		
		if (options.requestCursorUpdates) {
			encodings[nEncodings++] = RfbProto.EncodingXCursor;
			encodings[nEncodings++] = RfbProto.EncodingRichCursor;
			if (!options.ignoreCursorUpdates) {
				encodings[nEncodings++] = RfbProto.EncodingPointerPos;
			}
		}
		
		encodings[nEncodings++] = RfbProto.EncodingLastRect;
		encodings[nEncodings++] = RfbProto.EncodingNewFBSize;
		
		boolean encodingsWereChanged = false;
		if (nEncodings != nEncodingsSaved) {
			encodingsWereChanged = true;
		} else {
			for (int i = 0; i < nEncodings; i++) {
				if (encodings[i] != encodingsSaved[i]) {
					encodingsWereChanged = true;
					break;
				}
			}
		}
		
		if (encodingsWereChanged) {
			try {
				rfb.writeSetEncodings(encodings, nEncodings);
				if (vc != null) {
					vc.softCursorFree();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			encodingsSaved = encodings;
			nEncodingsSaved = nEncodings;
		}
	}
	
	//
	// disconnect() - close connection to server.
	//
	
	/**
	 * Sets the recording status.
	 *
	 * @param fname
	 *            the new recording status
	 */
	void setRecordingStatus(String fname) {
		synchronized (recordingSync) {
			sessionFileName = fname;
			recordingStatusChanged = true;
		}
	}
	
	//
	// fatalError() - print out a fatal error message.
	// FIXME: Do we really need two versions of the fatalError() method?
	//
	
	/**
	 * Show connection status.
	 *
	 * @param msg
	 *            the msg
	 */
	void showConnectionStatus(String msg) {
		if (msg == null) {
			if (vncContainer.isAncestorOf(connStatusLabel)) {
				vncContainer.remove(connStatusLabel);
			}
			return;
		}
		
		System.out.println(msg);
		
		if (connStatusLabel == null) {
			connStatusLabel = new Label("Status: " + msg);
			connStatusLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));
		} else {
			connStatusLabel.setText("Status: " + msg);
		}
		
		if (!vncContainer.isAncestorOf(connStatusLabel)) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.insets = new Insets(20, 30, 20, 30);
			gridbag.setConstraints(connStatusLabel, gbc);
			vncContainer.add(connStatusLabel);
		}
		
		if (inSeparateFrame) {
			this.pack();
		} else {
			validate();
		}
	}
	
	/**
	 * Show message.
	 *
	 * @param msg
	 *            the msg
	 */
	void showMessage(String msg) {
		vncContainer.removeAll();
		
		Label errLabel = new Label(msg, Label.CENTER);
		errLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));
		
		if (offerRelogin) {
			
			Panel gridPanel = new Panel(new GridLayout(0, 1));
			Panel outerPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
			outerPanel.add(gridPanel);
			vncContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 16));
			vncContainer.add(outerPanel);
			Panel textPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
			textPanel.add(errLabel);
			gridPanel.add(textPanel);
			gridPanel.add(new ReloginPanel(this));
			
		} else {
			
			vncContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 30));
			vncContainer.add(errLabel);
			
		}
		
		if (inSeparateFrame) {
			this.pack();
		} else {
			validate();
		}
	}
	
	//
	// Show message text and optionally "Relogin" and "Close" buttons.
	//
	
	/**
	 * Start recording.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void startRecording() throws IOException {
		synchronized (recordingSync) {
			if (!recordingActive) {
				// Save settings to restore them after recording the session.
				cursorUpdatesDef = options.choices[options.cursorUpdatesIndex]
						.getSelectedItem();
				eightBitColorsDef = options.choices[options.eightBitColorsIndex]
						.getSelectedItem();
				// Set options to values suitable for recording.
				options.choices[options.cursorUpdatesIndex].select("Disable");
				options.choices[options.cursorUpdatesIndex].setEnabled(false);
				options.setEncodings();
				options.choices[options.eightBitColorsIndex].select("No");
				options.choices[options.eightBitColorsIndex].setEnabled(false);
				options.setColorFormat();
			} else {
				rfb.closeSession();
			}
			
			System.out.println("Recording the session in " + sessionFileName);
			rfb.startSession(sessionFileName);
			recordingActive = true;
		}
	}
	
	//
	// Stop the applet.
	// Main applet thread will terminate on first exception
	// after seeing that rfbThread has been set to null.
	//
	
	/**
	 * Stop.
	 */
	public void stop() {
		System.out.println("Stopping applet");
		rfbThread = null;
	}
	
	//
	// This method is called before the applet is destroyed.
	//
	
	/**
	 * Stop recording.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void stopRecording() throws IOException {
		synchronized (recordingSync) {
			if (recordingActive) {
				// Restore options.
				options.choices[options.cursorUpdatesIndex]
						.select(cursorUpdatesDef);
				options.choices[options.cursorUpdatesIndex].setEnabled(true);
				options.setEncodings();
				options.choices[options.eightBitColorsIndex]
						.select(eightBitColorsDef);
				options.choices[options.eightBitColorsIndex].setEnabled(true);
				options.setColorFormat();
				
				rfb.closeSession();
				System.out.println("Session recording stopped.");
			}
			sessionFileName = null;
			recordingActive = false;
		}
	}
	
	//
	// Start/stop receiving mouse events.
	//
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JFrame#update(java.awt.Graphics)
	 */
	public void update(Graphics g) {
	}
	
	//
	// Close application properly on window close event.
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
	@SuppressWarnings("deprecation")
	public void windowClosing(WindowEvent evt) {
		System.out.println("Closing window");
		if (rfb != null) {
			disconnect();
		}
		
		vncContainer.hide();
		
		if (!inAnApplet) {
			System.exit(0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent
	 * )
	 */
	public void windowDeactivated(WindowEvent evt) {
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
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent evt) {
	}
}
