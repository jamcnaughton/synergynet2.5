/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mapapp;


import java.io.File;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;

public class MapApp extends DefaultSynergyNetApp {
	
	private ContentSystem contentSystem;
	private static final Logger log = Logger.getLogger(TableCommsClientService.class.getName());

	public MapApp(ApplicationInfo info) {
		super(info);		
	}

	@Override
	public void addContent() {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);		
		setMenuController(new HoldTopRightExit());
		
		File dir = new File("apps/mapapp/images");
		String[] children = dir.list();
		if (children == null) {
			log.severe("Could not read folder content");
		} else {
		    for (int i=0; i<children.length; i++) {
		    	String filename = children[i];
		    	if(!filename.endsWith("jpg") && !filename.endsWith("jpeg") && !filename.endsWith("png") && !filename.endsWith("bmp") && !filename.endsWith("gif")) continue;
		    	ImageTextLabel imageFrame = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		        imageFrame.setBorderSize(3);
		        imageFrame.setImageInfo(MapApp.class.getResource(filename));
		        ImageIcon image = new ImageIcon(MapApp.class.getResource(filename));
		        imageFrame.setAutoFit(false);
		        imageFrame.setWidth(image.getIconWidth());
		        imageFrame.setHeight(image.getIconHeight());
		        imageFrame.setScale(0.2f);
		        imageFrame.drawImage(MapApp.class.getResource(filename));		 
		        imageFrame.rotateRandom();
		        imageFrame.placeRandom();
		    }
		}
		
        Frame background = (Frame) contentSystem.createContentItem(Frame.class);
        background.setWidth(contentSystem.getScreenWidth());
        background.setHeight(contentSystem.getScreenHeight());
        background.centerItem();
        background.drawImage(MapApp.class.getResource("Map.jpg"));
        background.setAsBottomObject();
        background.setRotateTranslateScalable(false);
        background.setBringToTopable(false);
	}
	
	@Override
	protected void onDeactivate() {}

	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
	}

}

