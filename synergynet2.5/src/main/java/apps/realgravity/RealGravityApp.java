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

package apps.realgravity;

import java.awt.Point;

import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.jme.cursorsystem.elements.twod.CursorEventDispatcher;

import com.jme.system.DisplaySystem;
// import javax.swing.text.DateFormatter;
import com.jme.bounding.OrthogonalBoundingBox;

/**
 * The Class RealGravityApp.
 */
public class RealGravityApp extends DefaultSynergyNetApp {

	/** The gravity app running. */
	public static boolean gravityAppRunning = false;

	/** The us. */
	protected UniverseSimulator us;
	
	/**
	 * Instantiates a new real gravity app.
	 *
	 * @param info
	 *            the info
	 */
	public RealGravityApp(ApplicationInfo info) {
		super(info);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent
	 * ()
	 */
	@Override
	public void addContent() {

		setMenuController(new HoldTopRightConfirmVisualExit(this));

		Universe.G = Universe.G / 1e1;
		final Universe u = new Universe();
		us = new UniverseSimulator(u, "universequad", DisplaySystem
				.getDisplaySystem().getRenderer().getWidth(), DisplaySystem
				.getDisplaySystem().getRenderer().getHeight());

		MassEntity earth = MassEntity.EARTH;
		u.add(earth);

		MassEntity mars = MassEntity.MARS;
		mars.setPos(new Point.Double(6.37814e7, 6.37814e7));
		mars.setVel(new Point.Double(-1e3, 1e3));
		u.add(mars);

		us.setModelBound(new OrthogonalBoundingBox());
		us.updateModelBound();
		us.setLocalTranslation(DisplaySystem.getDisplaySystem().getWidth() / 2,
				DisplaySystem.getDisplaySystem().getHeight() / 2, 0);
		orthoNode.attachChild(us);

		new CursorEventDispatcher(us, 1);
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #stateUpdate(float)
	 */
	@Override
	protected void stateUpdate(float tpf) {
		us.draw();
	}
	
}
