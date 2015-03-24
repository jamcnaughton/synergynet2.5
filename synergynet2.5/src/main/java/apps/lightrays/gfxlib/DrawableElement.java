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

package apps.lightrays.gfxlib;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;


/**
 * The Class DrawableElement.
 */
public abstract class DrawableElement {
	
	/** The opacity. */
	protected float opacity = 1.0f;
	
	/** The x. */
	protected int x;
	
	/** The y. */
	protected int y;
	
	/** The z. */
	protected int z;
	
	/** The colour. */
	protected Color colour;

	/**
	 * Instantiates a new drawable element.
	 */
	public DrawableElement() {}
	
	/**
	 * Draw.
	 *
	 * @param gfx the gfx
	 * @param tick_count the tick_count
	 */
	public abstract void draw(Graphics2D gfx, long tick_count);
	

	/**
	 * * Utility Methods **.
	 *
	 * @return the alpha composite
	 */
	protected Composite getAlphaComposite() {
		return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
	}
	
	/**
	 * * GETTERS AND SETTERS ***.
	 *
	 * @return the opacity
	 */
	
	public float getOpacity() {
		return opacity;
	}

	/**
	 * Sets the opacity.
	 *
	 * @param opacity the new opacity
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Sets the z.
	 *
	 * @param z the new z
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * Gets the c.
	 *
	 * @return the c
	 */
	public Color getC() {
		return colour;
	}

	/**
	 * Sets the c.
	 *
	 * @param colour the new c
	 */
	public void setC(Color colour) {
		this.colour = colour;
	}	
}
