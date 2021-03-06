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

package synergynetframework.mtinput.tuio;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import synergynetframework.config.table.TableConfigPrefsItem;
import synergynetframework.mtinput.ClickDetector;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputSource;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;
import synergynetframework.mtinput.tuio.tuioobjects.TUIOFiducialObject;
import synergynetframework.mtinput.tuio.tuioobjects.TUIOFingerCursor;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

/**
 * Support for tables which use the TUIO protocol. This implementation supports
 * both cursors and objects/fiducials.
 *
 * @author dcs0ah1
 */
public class TUIOMultiTouchInput implements IMultiTouchInputSource,
		TuioListener {
	
	/** The calling list. */
	protected List<Callable<Object>> callingList = new ArrayList<Callable<Object>>();
	
	/** The click detector. */
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);

	/** The fiducials. */
	protected Map<Long, TUIOFiducialObject> fiducials = new HashMap<Long, TUIOFiducialObject>();
	
	/** The finger cursors. */
	protected Map<Long, TUIOFingerCursor> fingerCursors = new HashMap<Long, TUIOFingerCursor>();

	/** The listeners. */
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();
	
	/** The network client. */
	protected TuioClient networkClient;
	
	/**
	 * Instantiates a new TUIO multi touch input.
	 */
	public TUIOMultiTouchInput() {
		start();
	}
	
	/*
	 * (non-Javadoc)
	 * @see TUIO.TuioListener#addTuioCursor(TUIO.TuioCursor)
	 */
	public void addTuioCursor(final TuioCursor tuioCursor) {
		final long sessionID = tuioCursor.getSessionID();
		TUIOFingerCursor fingerCursor = fingerCursors.get(sessionID);
		if (fingerCursor == null) {
			fingerCursor = new TUIOFingerCursor();
			fingerCursors.put(sessionID, fingerCursor);
			
			Callable<Object> c = new Callable<Object>() {

				float x_speed = tuioCursor.getXSpeed();
				float xpos = tuioCursor.getX();
				float y_speed = tuioCursor.getYSpeed();
				float ypos = tuioCursor.getY();
				
				@Override
				public Object call() throws Exception {
					final TUIOFingerCursor fingerCursor = fingerCursors
							.get(sessionID);
					if (fingerCursor != null) {
						fingerCursor.setPosition(new Point2D.Float(xpos,
								1 - ypos));
						fingerCursor.setVelocity(new Point2D.Float(x_speed,
								y_speed));
						for (IMultiTouchEventListener listener : listeners) {
							clickDetector.newCursorPressed(
									fingerCursor.getId(),
									fingerCursor.getPosition());
							MultiTouchCursorEvent evt = new MultiTouchCursorEvent(
									fingerCursor.getId(),
									fingerCursor.getPosition(),
									fingerCursor.getVelocity());
							listener.cursorPressed(evt);
						}
					}
					return null;
				}
			};
			synchronized (callingList) {
				callingList.add(c);
			}
			
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see TUIO.TuioListener#addTuioObject(TUIO.TuioObject)
	 */
	public void addTuioObject(TuioObject tuioObject) {
		long sessionID = tuioObject.getSessionID();
		int fiducialID = tuioObject.getSymbolID();
		TUIOFiducialObject fiducial = fiducials.get(sessionID);
		if (fiducial == null) {
			fiducial = new TUIOFiducialObject(sessionID, fiducialID);
			fiducials.put(sessionID, fiducial);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see TUIO.TuioListener#refresh(TUIO.TuioTime)
	 */
	public void refresh(TuioTime tuioTime) {
		// unused
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#
	 * registerMultiTouchEventListener
	 * (synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void registerMultiTouchEventListener(
			IMultiTouchEventListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#
	 * registerMultiTouchEventListener
	 * (synergynetframework.mtinput.IMultiTouchEventListener, int)
	 */
	public void registerMultiTouchEventListener(
			IMultiTouchEventListener listener, int index) {
		if (!listeners.contains(listener)) {
			listeners.add(index, listener);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see TUIO.TuioListener#removeTuioCursor(TUIO.TuioCursor)
	 */
	public void removeTuioCursor(final TuioCursor tuioCursor) {
		Callable<Object> c = new Callable<Object>() {

			long sessionID = tuioCursor.getSessionID();
			
			@Override
			public Object call() throws Exception {
				final TUIOFingerCursor fingerCursor = fingerCursors
						.get(sessionID);
				
				if (fingerCursor != null) {
					
					for (IMultiTouchEventListener l : listeners) {
						MultiTouchCursorEvent event = new MultiTouchCursorEvent(
								fingerCursor.getId(),
								fingerCursor.getPosition(),
								fingerCursor.getVelocity());
						int clickCount = clickDetector
								.cursorReleasedGetClickCount(
										fingerCursor.getId(),
										fingerCursor.getPosition());
						if (clickCount > 0) {
							event.setClickCount(clickCount);
							l.cursorClicked(event);
						}
						l.cursorReleased(event);
					}
					fingerCursors.remove(sessionID);
					
				}
				return null;
			}
		};
		synchronized (callingList) {
			callingList.add(c);
		}
		
	}
	
	// TuioListener methods
	
	/*
	 * (non-Javadoc)
	 * @see TUIO.TuioListener#removeTuioObject(TUIO.TuioObject)
	 */
	public void removeTuioObject(final TuioObject tuioObject) {
		Callable<Object> c = new Callable<Object>() {

			long sessionID = tuioObject.getSessionID();
			
			@Override
			public Object call() throws Exception {
				TUIOFiducialObject fiducial = fiducials.get(sessionID);
				if (fiducial != null) {
					for (IMultiTouchEventListener listener : listeners) {
						MultiTouchObjectEvent event = new MultiTouchObjectEvent(
								fiducial.getId(), fiducial.getPosition(),
								fiducial.getVelocity());
						listener.objectRemoved(event);
					}
					fiducials.remove(sessionID);
				}
				return null;
			}
			
		};
		synchronized (callingList) {
			callingList.add(c);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.mtinput.IMultiTouchInputSource#setClickSensitivity
	 * (long, float)
	 */
	public void setClickSensitivity(long time, float distance) {
		this.clickDetector = new ClickDetector(time, distance);
	}
	
	/**
	 * Start.
	 */
	public void start() {
		synchronized (this) {
			TableConfigPrefsItem tablePrefs = new TableConfigPrefsItem();
			networkClient = new TuioClient(tablePrefs.getTuioPort());
			networkClient.addTuioListener(this);
			networkClient.connect();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					networkClient.disconnect();
				}
			});
		}
	}
	
	/**
	 * Stop.
	 */
	public void stop() {
		networkClient.removeTuioListener(this);
		networkClient.disconnect();
		networkClient = null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#
	 * unregisterMultiTouchEventListener
	 * (synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void unregisterMultiTouchEventListener(
			IMultiTouchEventListener listener) {
		listeners.remove(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#update(float)
	 */
	public void update(float tpf) {
		synchronized (callingList) {
			for (Callable<Object> c : callingList) {
				try {
					c.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			callingList.clear();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see TUIO.TuioListener#updateTuioCursor(TUIO.TuioCursor)
	 */
	public void updateTuioCursor(final TuioCursor tuioCursor) {
		Callable<Object> c = new Callable<Object>() {

			long sessionID = tuioCursor.getSessionID();
			float x_speed = tuioCursor.getXSpeed();
			float xpos = tuioCursor.getX();
			float y_speed = tuioCursor.getYSpeed();
			float ypos = tuioCursor.getY();

			@Override
			public Object call() throws Exception {
				final TUIOFingerCursor fingerCursor = fingerCursors
						.get(sessionID);
				if (fingerCursor != null) {
					fingerCursor.setPosition(new Point2D.Float(xpos, 1 - ypos));
					fingerCursor
							.setVelocity(new Point2D.Float(x_speed, y_speed));
					for (IMultiTouchEventListener listener : listeners) {
						MultiTouchCursorEvent event = new MultiTouchCursorEvent(
								fingerCursor.getId(),
								fingerCursor.getPosition(),
								fingerCursor.getVelocity());
						listener.cursorChanged(event);
					}
				}
				return null;
			}
		};
		synchronized (callingList) {
			callingList.add(c);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see TUIO.TuioListener#updateTuioObject(TUIO.TuioObject)
	 */
	public void updateTuioObject(final TuioObject tuioObject) {
		Callable<Object> c = new Callable<Object>() {

			float angle = tuioObject.getAngle();
			float r_accel = tuioObject.getRotationAccel();
			float r_speed = tuioObject.getRotationSpeed();
			long sessionID = tuioObject.getSessionID();
			float x_speed = tuioObject.getXSpeed();
			float xpos = tuioObject.getX();
			float y_speed = tuioObject.getYSpeed();
			float ypos = tuioObject.getY();
			
			@Override
			public Object call() throws Exception {
				TUIOFiducialObject fiducial = fiducials.get(sessionID);
				if (fiducial != null) {
					fiducial.setPosition(new Point2D.Float(xpos, 1 - ypos));
					fiducial.setVelocity(new Point2D.Float(x_speed, y_speed));
					fiducial.setAngle(angle);
					fiducial.setAngleVelocity(r_speed);
					fiducial.setAngleAcceleration(r_accel);
					for (IMultiTouchEventListener listener : listeners) {
						MultiTouchObjectEvent event = new MultiTouchObjectEvent(
								fiducial.getId(), fiducial.getPosition(),
								fiducial.getVelocity());
						listener.objectChanged(event);
					}
				}
				return null;
			}
			
		};
		synchronized (callingList) {
			callingList.add(c);
		}
		
	}
	
}