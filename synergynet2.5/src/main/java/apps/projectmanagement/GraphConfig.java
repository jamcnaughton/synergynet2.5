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

package apps.projectmanagement;

import java.awt.Color;
import java.awt.Font;
// import java.io.InputStream;
import java.net.URL;

import synergynetframework.appsystem.contentsystem.items.LineItem;

/**
 * The Class GraphConfig.
 */
public class GraphConfig {

	/** The arrow mode. */
	public static int arrowMode = LineItem.BIDIRECTIONAL_ARROWS;

	/** The link color. */
	public static Color linkColor = Color.white;

	/** The link mode. */
	public static int linkMode = LineItem.CONNECTED_LINE;

	/** The link text color. */
	public static Color linkTextColor = Color.white;

	/** The link text font. */
	public static Font linkTextFont = new Font("Arial", Font.BOLD, 16);

	/** The link width. */
	public static float linkWidth = 1f;

	/** The message background color. */
	public static Color messageBackgroundColor = Color.black;

	/** The message border color. */
	public static Color messageBorderColor = Color.white;

	/** The message text color. */
	public static Color messageTextColor = Color.white;

	/** The message text font. */
	public static Font messageTextFont = new Font("Arial", Font.PLAIN, 16);

	/** The node background color. */
	public static Color nodeBackgroundColor = Color.white;

	/** The node border color. */
	public static Color nodeBorderColor = Color.white;

	/** The node close image resource. */
	public static URL nodeCloseImageResource = ProjectManagementApp.class
			.getResource("close.png");

	/** The node default border size. */
	public static int nodeDefaultBorderSize = 20;

	/** The node edit image resource. */
	public static URL nodeEditImageResource = ProjectManagementApp.class
			.getResource("edit.png");

	/** The node foreground color. */
	public static Color nodeForegroundColor = Color.black;

	/** The node keyboard image resource. */
	public static URL nodeKeyboardImageResource = ProjectManagementApp.class
			.getResource("keyboard.jpg");

	/** The node link image resource. */
	public static URL nodeLinkImageResource = ProjectManagementApp.class
			.getResource("Link24.gif");

	/** The node next page image resource. */
	public static URL nodeNextPageImageResource = ProjectManagementApp.class
			.getResource("Forward24.gif");

	/** The node point bg color. */
	public static Color nodePointBgColor = Color.white;

	/** The node point border color. */
	public static Color nodePointBorderColor = Color.black;

	/** The node point border size. */
	public static int nodePointBorderSize = 2;

	/** The node point size. */
	public static int nodePointSize = 20;

	/** The node previous page image resource. */
	public static URL nodePreviousPageImageResource = ProjectManagementApp.class
			.getResource("Back24.gif");

	/** The node text font. */
	public static Font nodeTextFont = new Font("Arial", Font.PLAIN, 20);

	/** The node top border size. */
	public static int nodeTopBorderSize = 30;
	
	// public static InputStream keyboardDef =
	// CommonResources.class.getResourceAsStream("keyboard.def");

}
