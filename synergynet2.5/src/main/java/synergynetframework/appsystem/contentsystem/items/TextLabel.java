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
import java.awt.Font;
import java.io.Serializable;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ITextLabelImplementation;

/**
 * The Class TextLabel.
 */
public class TextLabel extends Frame implements Serializable, Cloneable {

	/**
	 * The Enum Alignment.
	 */
	public enum Alignment {
		/** The center. */
		CENTER,
		/** The left. */
		LEFT,
		/** The right. */
		RIGHT
	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8279443252885792979L;

	/** The font. */
	protected Font font = new Font("Arial", Font.PLAIN, 16);

	/** The text. */
	protected String text = "";

	/** The text alignment. */
	protected Alignment textAlignment = Alignment.CENTER;;

	/** The text colour. */
	protected Color textColour = Color.black;

	/**
	 * Instantiates a new text label.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public TextLabel(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.QuadContentItem#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		TextLabel clonedItem = (TextLabel) super.clone();
		clonedItem.text = text;
		clonedItem.font = font;
		clonedItem.textColour = textColour;
		return clonedItem;

	}

	/**
	 * Gets the alignment.
	 *
	 * @return the alignment
	 */
	public Alignment getAlignment() {
		return textAlignment;
	}
	
	/**
	 * Gets the font.
	 *
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the text colour.
	 *
	 * @return the text colour
	 */
	public Color getTextColour() {
		return textColour;
	}

	/**
	 * Sets the alignment.
	 *
	 * @param alignment
	 *            the new alignment
	 */
	public void setAlignment(Alignment alignment) {
		textAlignment = alignment;
		((ITextLabelImplementation) this.contentItemImplementation)
				.setAlignment(textAlignment);
		
	}
	
	/**
	 * Sets the font.
	 *
	 * @param font
	 *            the new font
	 */
	public void setFont(Font font) {
		this.font = font;
		((ITextLabelImplementation) this.contentItemImplementation)
				.setFont(font);
	}

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
		((ITextLabelImplementation) this.contentItemImplementation)
				.setText(text);
	}

	/**
	 * Sets the text colour.
	 *
	 * @param textColour
	 *            the new text colour
	 */
	public void setTextColour(Color textColour) {
		this.textColour = textColour;
		((ITextLabelImplementation) this.contentItemImplementation)
				.setTextColour(textColour);
	}
}
