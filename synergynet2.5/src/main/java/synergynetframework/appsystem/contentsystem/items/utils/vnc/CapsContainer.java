package synergynetframework.appsystem.contentsystem.items.utils.vnc;

//
// Copyright (C) 2003 Constantin Kaplinsky. All Rights Reserved.
//
// This is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This software is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this software; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
// USA.
//

//
// CapsContainer.java - A container of capabilities as used in the RFB
// protocol 3.130
//

import java.util.Hashtable;
import java.util.Vector;

/**
 * The Class CapsContainer.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
class CapsContainer {
	
	// Public methods
	
	/** The info map. */
	protected Hashtable infoMap;
	
	/** The ordered list. */
	protected Vector orderedList;
	
	/**
	 * Instantiates a new caps container.
	 */
	public CapsContainer() {
		infoMap = new Hashtable(64, (float) 0.25);
		orderedList = new Vector(32, 8);
	}
	
	/**
	 * Adds the.
	 *
	 * @param capinfo
	 *            the capinfo
	 */
	public void add(CapabilityInfo capinfo) {
		Integer key = new Integer(capinfo.getCode());
		infoMap.put(key, capinfo);
	}
	
	/**
	 * Adds the.
	 *
	 * @param code
	 *            the code
	 * @param vendor
	 *            the vendor
	 * @param name
	 *            the name
	 * @param desc
	 *            the desc
	 */
	public void add(int code, String vendor, String name, String desc) {
		Integer key = new Integer(code);
		infoMap.put(key, new CapabilityInfo(code, vendor, name, desc));
	}
	
	/**
	 * Enable.
	 *
	 * @param other
	 *            the other
	 * @return true, if successful
	 */
	public boolean enable(CapabilityInfo other) {
		Integer key = new Integer(other.getCode());
		CapabilityInfo capinfo = (CapabilityInfo) infoMap.get(key);
		if (capinfo == null) {
			return false;
		}
		
		boolean enabled = capinfo.enableIfEquals(other);
		if (enabled) {
			orderedList.addElement(key);
		}
		
		return enabled;
	}
	
	/**
	 * Gets the by order.
	 *
	 * @param idx
	 *            the idx
	 * @return the by order
	 */
	public int getByOrder(int idx) {
		int code;
		try {
			code = ((Integer) orderedList.elementAt(idx)).intValue();
		} catch (ArrayIndexOutOfBoundsException e) {
			code = 0;
		}
		return code;
	}
	
	/**
	 * Gets the description.
	 *
	 * @param code
	 *            the code
	 * @return the description
	 */
	public String getDescription(int code) {
		CapabilityInfo capinfo = (CapabilityInfo) infoMap
				.get(new Integer(code));
		if (capinfo == null) {
			return null;
		}
		
		return capinfo.getDescription();
	}
	
	/**
	 * Gets the info.
	 *
	 * @param code
	 *            the code
	 * @return the info
	 */
	public CapabilityInfo getInfo(int code) {
		return (CapabilityInfo) infoMap.get(new Integer(code));
	}
	
	/**
	 * Checks if is enabled.
	 *
	 * @param code
	 *            the code
	 * @return true, if is enabled
	 */
	public boolean isEnabled(int code) {
		CapabilityInfo capinfo = (CapabilityInfo) infoMap
				.get(new Integer(code));
		if (capinfo == null) {
			return false;
		}
		
		return capinfo.isEnabled();
	}
	
	// Protected data
	
	/**
	 * Checks if is known.
	 *
	 * @param code
	 *            the code
	 * @return true, if is known
	 */
	public boolean isKnown(int code) {
		return infoMap.containsKey(new Integer(code));
	}
	
	/**
	 * Num enabled.
	 *
	 * @return the int
	 */
	public int numEnabled() {
		return orderedList.size();
	}
}
