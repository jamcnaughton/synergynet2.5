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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Point2D.Float;
import java.util.HashMap;
import java.util.Map;

import synergynetframework.appsystem.table.gfx.FullScreenCanvas;
import synergynetframework.jme.gfx.twod.DrawableSpatialImage;

import com.jme.scene.Spatial;
import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class UniverseSimulator.
 */
public class UniverseSimulator extends FullScreenCanvas implements
		DrawableSpatialImage {

	/** The Constant metersPerPixel. */
	public static final double metersPerPixel = 5e5;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1398525147529274059L;

	/** The Constant timeCompressionSeconds. */
	public static final double timeCompressionSeconds = 13600; // seconds per
																// second

	/** The elapsed time. */
	protected double elapsedTime;

	/** The forming entities. */
	protected Map<Long, FormingEntity> formingEntities = new HashMap<Long, FormingEntity>();

	/** The gfx. */
	private ImageGraphics gfx;

	/** The prev time. */
	private double prevTime;
	
	/** The u. */
	private Universe u;

	/** The height. */
	protected int width, height;
	
	/**
	 * Instantiates a new universe simulator.
	 *
	 * @param u
	 *            the u
	 * @param name
	 *            the name
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public UniverseSimulator(Universe u, String name, int width, int height) {
		super(name);
		this.u = u;
		gfx = getGraphics();
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Adds the new entity.
	 *
	 * @param pos
	 *            the pos
	 * @param vel
	 *            the vel
	 */
	private void addNewEntity(Float pos, Point.Double vel) {
		MassEntity me = new MassEntity("" + System.nanoTime(),
				MassEntity.EARTH.getMass(), MassEntity.EARTH.getRadius());
		me.getPos().x = pos.x * metersPerPixel;
		me.getPos().y = pos.y * metersPerPixel;
		me.getVel().x = vel.x;
		me.getVel().y = vel.y;
		u.add(me);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorClicked(long,
	 * int, int)
	 */
	@Override
	public void cursorClicked(long cursorID, int x, int y) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorDragged(long,
	 * int, int)
	 */
	public void cursorDragged(long id, int x, int y) {
		FormingEntity fe;
		synchronized (formingEntities) {
			fe = formingEntities.get(id);
		}
		if (fe != null) {
			Point pos = new Point(x, y);
			fe.dragx = pos.x;
			fe.dragy = pos.y;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorPressed(long,
	 * int, int)
	 */
	public void cursorPressed(long cursorID, int x, int y) {
		FormingEntity fe = new FormingEntity();
		fe.id = cursorID;
		Point pos = new Point(x, y);
		fe.posx = pos.x;
		fe.posy = pos.y;
		fe.dragx = pos.x;
		fe.dragy = pos.y;
		synchronized (formingEntities) {
			formingEntities.put(fe.id, fe);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorReleased(
	 * long, int, int)
	 */
	public void cursorReleased(long cursorID, int x, int y) {
		FormingEntity fe;
		synchronized (formingEntities) {
			fe = formingEntities.get(cursorID);
		}
		if (fe != null) {
			Point.Double vel = new Point.Double();
			vel.x = (-(fe.dragx - fe.posx) * metersPerPixel)
					/ timeCompressionSeconds;
			vel.y = (-(fe.dragy - fe.posy) * metersPerPixel)
					/ timeCompressionSeconds;
			addNewEntity(new Float(fe.dragx - (width / 2), fe.dragy
					- (height / 2)), vel);
			synchronized (formingEntities) {
				formingEntities.remove(fe.id);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#draw()
	 */
	public void draw() {
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gfx.setColor(Color.DARK_GRAY);
		gfx.fillRect(0, 0, width, pixelHeight);
		
		gfx.setColor(Color.LIGHT_GRAY);
		gfx.drawString(u.getEntityCount() + "", 20, 20);
		updateTimeDelta();
		u.updateMotion(elapsedTime);
		
		u.cullFarAway(metersPerPixel * width);
		u.render(gfx, width, height, metersPerPixel);
		renderFormingEntities(gfx);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#getSpatial()
	 */
	public Spatial getSpatial() {
		return this;
	}

	/**
	 * Render forming entities.
	 *
	 * @param g2d
	 *            the g2d
	 */
	private void renderFormingEntities(Graphics2D g2d) {
		for (FormingEntity fe : formingEntities.values()) {
			fe.render(g2d);
		}
	}
	
	/**
	 * Update time delta.
	 */
	private void updateTimeDelta() {
		double currentTime = System.currentTimeMillis();
		if (prevTime == 0) {
			prevTime = currentTime - 1;
		}
		elapsedTime = ((currentTime - prevTime) / 1000)
				* timeCompressionSeconds;
		prevTime = currentTime;
	}
	
}
