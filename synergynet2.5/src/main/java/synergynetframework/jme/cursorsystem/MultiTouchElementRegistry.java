/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.jme.cursorsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import synergynetframework.jme.pickingsystem.data.PickResultData;

import com.jme.scene.Spatial;

/**
 * Whenever a Spatial is to be interacted with, using the multitouch interface,
 * a MultiTouchElement is created. Creating a MultiTouchElement, or any of its
 * subclasses, will cause that element to be registered with
 * MultiTouchElementRegistry. This registry is responsible for tracking
 * registered elements, allowing for elements to be registered and
 * de-registered. Further, this registry maintains state on which cursors are
 * associated with a particular MultiTouchElement. For example, if two fingers
 * are placed over a single Spatial object, then the cursor system will call
 * associateCursorIDToName twice, for each cursor.
 * getRegisteredElementsForCursorID will return the MultiTouchElements of all
 * Spatials associated with a single cursor.
 *
 * @author dcs0ah1
 */

public class MultiTouchElementRegistry {

	/** The instance. */
	private static MultiTouchElementRegistry instance;

	/**
	 * Gets the single instance of MultiTouchElementRegistry.
	 *
	 * @return single instance of MultiTouchElementRegistry
	 */
	public static MultiTouchElementRegistry getInstance() {
		if (instance == null) {
			instance = new MultiTouchElementRegistry();
		}
		return instance;
	}
	
	/** The cursor id to names. */
	protected Map<Long, Set<String>> cursorIDToNames;
	
	/** The name to multi touch element. */
	protected Map<String, List<MultiTouchElement>> nameToMultiTouchElement;

	/**
	 * Instantiates a new multi touch element registry.
	 */
	private MultiTouchElementRegistry() {
		cursorIDToNames = new HashMap<Long, Set<String>>();
		nameToMultiTouchElement = new HashMap<String, List<MultiTouchElement>>();
	}

	/**
	 * Associate cursor id to name.
	 *
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 */
	public void associateCursorIDToName(long id, String name) {
		if (nameToMultiTouchElement.containsKey(name)) {
			Set<String> s = cursorIDToNames.get(id);
			if (s == null) {
				s = new HashSet<String>();
			}
			s.add(name);
			cursorIDToNames.put(id, s);
		}
	}

	/**
	 * Associate cursor id to name.
	 *
	 * @param pickResultData
	 *            the pick result data
	 */
	public void associateCursorIDToName(PickResultData pickResultData) {
		associateCursorIDToName(pickResultData.getOriginatingCursorID(),
				pickResultData.getPickedSpatialName());
	}

	/**
	 * Gets the element by target spatial.
	 *
	 * @param s
	 *            the s
	 * @return the element by target spatial
	 */
	public MultiTouchElement getElementByTargetSpatial(Spatial s) {
		for (String key : nameToMultiTouchElement.keySet()) {
			List<MultiTouchElement> list = nameToMultiTouchElement.get(key);
			for (MultiTouchElement e : list) {
				if (e.getTargetSpatial() == s) {
					return e;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the registered elements for cursor id.
	 *
	 * @param id
	 *            the id
	 * @return the registered elements for cursor id
	 */
	public List<MultiTouchElement> getRegisteredElementsForCursorID(long id) {
		Set<String> elementNames = cursorIDToNames.get(id);
		List<MultiTouchElement> nodes = new ArrayList<MultiTouchElement>();
		if (elementNames != null) {
			Iterator<String> i = elementNames.iterator();
			while (i.hasNext()) {
				String elementName = i.next();
				List<MultiTouchElement> elements = nameToMultiTouchElement
						.get(elementName);
				if (elements != null) {
					nodes.addAll(elements);
				}
			}
		}
		return nodes;
	}

	/**
	 * Checks if is registered.
	 *
	 * @param e
	 *            the e
	 * @return true, if is registered
	 */
	public boolean isRegistered(MultiTouchElement e) {
		List<MultiTouchElement> registeredElements = nameToMultiTouchElement
				.get(e.getName());
		if (registeredElements == null) {
			return false;
		}
		return registeredElements.contains(e);
	}

	/**
	 * Checks if is registered.
	 *
	 * @param s
	 *            the s
	 * @return true, if is registered
	 */
	public boolean isRegistered(Spatial s) {
		return nameToMultiTouchElement.containsKey(s.getName());
	}

	/**
	 * Register.
	 *
	 * @param mrs
	 *            the mrs
	 */
	public void register(MultiTouchElement mrs) {
		List<MultiTouchElement> registeredElements = nameToMultiTouchElement
				.get(mrs.getName());
		if (registeredElements == null) {
			registeredElements = new ArrayList<MultiTouchElement>();
			nameToMultiTouchElement.put(mrs.getName(), registeredElements);
		}
		
		registeredElements.add(mrs);
	}

	/**
	 * Removes the cursor id to names associations.
	 *
	 * @param id
	 *            the id
	 */
	public void removeCursorIDToNamesAssociations(long id) {
		cursorIDToNames.remove(id);
	}

	/**
	 * Unregister.
	 *
	 * @param e
	 *            the e
	 */
	public void unregister(MultiTouchElement e) {
		List<MultiTouchElement> registeredElements = nameToMultiTouchElement
				.get(e.getName());
		registeredElements.remove(e);
	}
	
	/**
	 * Unregister elements for spatial.
	 *
	 * @param s
	 *            the s
	 */
	public void unregisterElementsForSpatial(Spatial s) {
		nameToMultiTouchElement.remove(s.getName());
	}

}
