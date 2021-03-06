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

package synergynetframework.jme.gfx.twod.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.jme.image.Texture;
import com.jme.image.Texture.WrapMode;
import com.jme.image.Texture2D;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.shape.Disk;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class GraphicsImageDisc.
 */
public class GraphicsImageDisc extends Disk {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 64623554610677568L;

	/** The concentric circles. */
	protected int concentricCircles;

	/** The graphics. */
	protected ImageGraphics graphics;

	/** The radial divisions. */
	protected int radialDivisions;

	/** The radius. */
	protected float radius;

	/** The texture. */
	protected Texture texture;

	/** The ts. */
	protected TextureState ts;
	
	/**
	 * Instantiates a new graphics image disc.
	 *
	 * @param name
	 *            the name
	 * @param radius
	 *            the radius
	 */
	public GraphicsImageDisc(String name, float radius) {
		this(name, 16, 32, radius);
	}

	/**
	 * Instantiates a new graphics image disc.
	 *
	 * @param name
	 *            the name
	 * @param concentricCircles
	 *            the concentric circles
	 * @param radialDivisions
	 *            the radial divisions
	 * @param radius
	 *            the radius
	 */
	public GraphicsImageDisc(String name, int concentricCircles,
			int radialDivisions, float radius) {
		super(name, concentricCircles, radialDivisions, radius);
		updateGeometry(concentricCircles, radialDivisions, radius);
		this.radius = radius;
		this.concentricCircles = concentricCircles;
		this.radialDivisions = radialDivisions;
		init();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.jme.scene.TriMesh#draw(com.jme.renderer.Renderer)
	 */
	public void draw(Renderer r) {
		if (graphics.isDirty()) {
			if (graphics != null) {
				graphics.update(texture, false);
			}
		}
		super.draw(r);
	}

	/**
	 * Enable anti alias.
	 *
	 * @param graphics
	 *            the graphics
	 */
	private void enableAntiAlias(Graphics2D graphics) {
		RenderingHints hints = graphics.getRenderingHints();
		if (hints == null) {
			hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			hints.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		graphics.setRenderingHints(hints);
	}

	/**
	 * Gets the image graphics.
	 *
	 * @return the image graphics
	 */
	public ImageGraphics getImageGraphics() {
		return graphics;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.jme.scene.shape.Disk#getRadius()
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Inits the.
	 */
	private void init() {
		recreateImageForSize(this.radius);
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		alpha.setEnabled(true);
		alpha.setBlendEnabled(true);
		alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled(true);
		alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
		this.setRenderState(alpha);
		
		this.updateRenderState();
		
	}
	
	/**
	 * Recreate image for size.
	 *
	 * @param radius
	 *            the radius
	 */
	public void recreateImageForSize(float radius) {
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = new Texture2D();
		texture.setMinificationFilter(Texture.MinificationFilter.NearestNeighborNoMipMaps);
		texture.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		
		int widthPixels = (int) (radius * 2);
		int heightPixels = (int) (radius * 2);

		graphics = ImageGraphics.createInstance(widthPixels, heightPixels, 0);

		enableAntiAlias(graphics);

		texture.setImage(graphics.getImage());
		texture.setScale(new Vector3f(1, -1, 1));
		
		ts.setTexture(texture);
		ts.apply();
		setRenderState(ts);
		updateRenderState();
	}
	
	/**
	 * Update geometry.
	 *
	 * @param r
	 *            the r
	 */
	public void updateGeometry(float r) {
		this.updateGeometry(this.concentricCircles, this.radialDivisions, r);
	}
	
	/**
	 * Update graphics.
	 */
	public void updateGraphics() {
		if (graphics != null) {
			graphics.update(texture, false);
		}
	}
	
}
