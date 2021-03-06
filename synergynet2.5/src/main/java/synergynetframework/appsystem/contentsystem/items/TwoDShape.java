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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.ITwoDShape;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry;

/**
 * The Class TwoDShape.
 */
public class TwoDShape extends OrthoContentItem implements Cloneable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7358045606444271667L;

	/** The shape geometry. */
	private TwoDShapeGeometry shapeGeometry;

	/**
	 * Instantiates a new two d shape.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public TwoDShape(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/**
	 * Gets the shape geometry.
	 *
	 * @return the shape geometry
	 */
	public TwoDShapeGeometry getShapeGeometry() {
		return shapeGeometry;
	}
	
	/**
	 * Sets the colours.
	 *
	 * @param colours
	 *            the new colours
	 */
	public void setColours(Color[] colours) {
		ITwoDShape impl = (ITwoDShape) contentItemImplementation;
		impl.setColours(colours);
	}

	/**
	 * Sets the shape geometry.
	 *
	 * @param geom
	 *            the new shape geometry
	 */
	public void setShapeGeometry(TwoDShapeGeometry geom) {
		this.shapeGeometry = geom;
		ITwoDShape impl = (ITwoDShape) contentItemImplementation;
		impl.setShapeGeometry(geom);
	}
}
