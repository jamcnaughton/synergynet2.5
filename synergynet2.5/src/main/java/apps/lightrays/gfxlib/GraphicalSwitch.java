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

package apps.lightrays.gfxlib;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class GraphicalSwitch.
 */
public class GraphicalSwitch extends DrawableElement {
	
	/** The current_item. */
	DrawableElement current_item;

	/** The current_item_name. */
	String current_item_name;

	/** The items. */
	Map<String, DrawableElement> items;

	/**
	 * Instantiates a new graphical switch.
	 */
	public GraphicalSwitch() {
		items = new HashMap<String, DrawableElement>();
	}

	/**
	 * Adds the.
	 *
	 * @param name
	 *            the name
	 * @param el
	 *            the el
	 */
	public void add(String name, DrawableElement el) {
		this.items.put(name, el);
	}

	/*
	 * (non-Javadoc)
	 * @see apps.lightrays.gfxlib.DrawableElement#draw(java.awt.Graphics2D,
	 * long)
	 */
	public void draw(Graphics2D gfx, long tick_count) {
		current_item.draw(gfx, tick_count);
	}

	/**
	 * Sets the current.
	 *
	 * @param name
	 *            the new current
	 */
	public void setCurrent(String name) {
		current_item_name = name;
		current_item = items.get(name);
	}
	
}
