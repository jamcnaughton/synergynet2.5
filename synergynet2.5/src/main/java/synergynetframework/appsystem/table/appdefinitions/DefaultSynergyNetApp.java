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

package synergynetframework.appsystem.table.appdefinitions;

import java.util.logging.Logger;

import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;

import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;

public abstract class DefaultSynergyNetApp extends SynergyNetApp {

	protected Camera cam;
	private static final Logger log = Logger.getLogger(DefaultSynergyNetApp.class.getName());
	
	public DefaultSynergyNetApp(ApplicationInfo info) {
		super(info);	
	}
	
	@Override
	public void init() {
		super.init();		
	}

	@Override
	protected Camera getCamera() {
		if(cam == null) {
			cam = createDefaultCamera();
		}		
		return cam;
	}

	private Camera createDefaultCamera() {
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		Camera cam = display.getRenderer().createCamera(display.getWidth(), display.getHeight());
        Vector3f loc = new Vector3f( 0.0f, 0.0f, 25f );
        Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
        Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
        Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
        cam.setFrame(loc, left, up, dir);   
		cam.setFrustumPerspective( 45.0f, (float) DisplaySystem.getDisplaySystem().getWidth() / (float) DisplaySystem.getDisplaySystem().getHeight(), 1, 1000 );
		cam.setParallelProjection(false);
		cam.update();
		log.info("Default Camera is created");
		return cam;
	}

	@Override
	protected void initInput() {
		input = new InputHandler();		
		SynergyNetAppUtils.addEscapeKeyToExit(this.input);
	}

	@Override
	protected void stateUpdate(float tpf) {
		input.update(tpf);
	}

}
