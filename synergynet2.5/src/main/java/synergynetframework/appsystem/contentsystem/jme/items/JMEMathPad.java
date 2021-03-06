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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Point;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mit.ai.nl.core.Box;
import mit.ai.nl.core.Engine;
import mit.ai.nl.core.Expression;
import mit.ai.nl.core.Symbol;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MathPad.MathHandwritingListener;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMathPadImplementation;

import com.sun.java.util.collections.HashSet;
import com.sun.java.util.collections.Iterator;
import com.sun.java.util.collections.Set;

/**
 * The Class JMEMathPad.
 */
public class JMEMathPad extends JMESketchPad implements IMathPadImplementation {
	
	/** The Constant RATIO_THRESH. */
	protected static final double RATIO_THRESH = 0.8416663033577378;

	/** The selected symbol. */
	private static Symbol selectedSymbol = null;

	/** The engine. */
	protected Engine engine;

	/** The expressions. */
	private List<Expression> expressions = new ArrayList<Expression>();

	/** The last. */
	protected final java.util.List<Point> last = new ArrayList<Point>(64);

	/** The listeners. */
	private transient List<MathHandwritingListener> listeners = new ArrayList<MathHandwritingListener>();

	/** The math engine enabled. */
	protected boolean mathEngineEnabled = false;

	/**
	 * Instantiates a new JME math pad.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMEMathPad(ContentItem contentItem) {
		super(contentItem);

		try {
			engine = new Engine();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMathPadImplementation#addMathHandwritingListener(synergynetframework.
	 * appsystem.contentsystem.items.MathPad.MathHandwritingListener)
	 */
	@Override
	public void addMathHandwritingListener(MathHandwritingListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMESketchPad#clearAll
	 * ()
	 */
	@Override
	public void clearAll() {
		super.clearAll();
		this.clearMathExpressions();
		drawLineGrid();
		drawingFinished();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMathPadImplementation#clearMathExpressions()
	 */
	@Override
	public void clearMathExpressions() {
		engine.clear();
		expressions.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMESketchPad#
	 * cursorClicked(long, int, int)
	 */
	@Override
	public void cursorClicked(long cursorID, int x, int y) {
		Point p = new Point(x, y);
		if (mathEngineEnabled) {
			selectedSymbol = getSelectedSymbol(p);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMESketchPad#
	 * cursorDragged(long, int, int)
	 */
	@Override
	public void cursorDragged(long id, int x, int y) {
		Point p = lastPoint.get(id);
		if (p == null) {
			p = new Point(x, y);
		}
		super.cursorDragged(id, x, y);
		if (mathEngineEnabled) {
			last.add(new Point(x, y));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMESketchPad#
	 * cursorReleased(long, int, int)
	 */
	@Override
	public void cursorReleased(long cursorID, int x, int y) {
		if (!mathEngineEnabled || last.isEmpty()) {
			return;
		}
		int pc = last.size();
		float[][] points = new float[2][pc];
		for (int i = 0; i < pc; i++) {
			points[0][i] = last.get(i).x;
			points[1][i] = last.get(i).y;
		}
		
		try {
			mit.ai.nl.core.Stroke stroke = new mit.ai.nl.core.Stroke(points);
			last.clear();
			if ((Math.abs(stroke.getAspectRatio()) < RATIO_THRESH)
					&& ((stroke.getLength() / Math.sqrt(stroke.getArea())) > 3D)) {
				Set set = engine.getStrokes();
				HashSet hashset = new HashSet(15);
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					mit.ai.nl.core.Stroke stroke1 = (mit.ai.nl.core.Stroke) iterator
							.next();
					if (Box.edgeType(stroke, stroke1) == 1) {
						hashset.add(stroke1);
					}
				}
				
				if (hashset.size() != 0) {
					engine.deleteStrokes(hashset);
				} else {
					engine.addStroke(stroke);
				}
			} else {
				engine.addStroke(stroke);
			}
			if (expressions.isEmpty()) {
				expressions.add(engine.getExpression());
			}
			expressions.remove(expressions.size() - 1);
			expressions.add(engine.getExpression());
			
			for (MathHandwritingListener listener : listeners) {
				listener.expressionsWritten(expressions);
			}
		} catch (Exception exp) {
			engine.clear();
		}
	}

	/**
	 * Delete symbol.
	 */
	@SuppressWarnings("unused")
	private void deleteSymbol() {
		if (selectedSymbol != null) {
			
			mit.ai.nl.core.Stroke[] strokes = null;
			Field fields[] = selectedSymbol.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getName().equals("strokes")) {
					fields[i].setAccessible(true);
					try {
						strokes = (mit.ai.nl.core.Stroke[]) fields[i]
								.get(selectedSymbol);
					} catch (IllegalArgumentException e) {
						
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						
						e.printStackTrace();
					}
				}
			}
			
			HashSet selectedStrokes = new HashSet();
			for (int i = 0; i < strokes.length; i++) {
				selectedStrokes.add(strokes[i]);
			}
			engine.deleteStrokes(selectedStrokes);
			selectedSymbol = null;
		}
	}

	/**
	 * Draw line grid.
	 */
	private void drawLineGrid() {
		/*
		 * if(drawGfx != null){ drawGfx.setColor(Color.LIGHT_GRAY);
		 * drawGfx.setStroke(new BasicStroke(1.0f)); for(int y=0;
		 * y<heightPixels- item.getBorderSize(); y+=25)
		 * drawGfx.drawLine(item.getBorderSize(), y, widthPixels-
		 * item.getBorderSize(), y); }
		 */
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMathPadImplementation#getCurrentExpression()
	 */
	@Override
	public Expression getCurrentExpression() {
		return engine.getExpression();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMathPadImplementation#getMathExpressions()
	 */
	@Override
	public List<Expression> getMathExpressions() {
		return expressions;
	}
	
	/**
	 * Gets the selected symbol.
	 *
	 * @param p
	 *            the p
	 * @return the selected symbol
	 */
	private Symbol getSelectedSymbol(Point p) {
		if ((engine.getSymbols() == null) || engine.getSymbols().isEmpty()) {
			return null;
		}
		Iterator it = engine.getSymbols().iterator();
		while (it.hasNext()) {
			Box box = (Box) it.next();
			if ((p.x >= box.getLx()) && (p.x <= box.getUx())
					&& (p.y >= box.getLy()) && (p.y <= box.getUy())) {
				return (Symbol) box;
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMathPadImplementation#removeHandwritingListener(synergynetframework.
	 * appsystem.contentsystem.items.MathPad.MathHandwritingListener)
	 */
	@Override
	public void removeHandwritingListener(MathHandwritingListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMathPadImplementation#removeHandwritingListeners()
	 */
	@Override
	public void removeHandwritingListeners() {
		listeners.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMESketchPad#
	 * renderSketch()
	 */
	public void renderSketch() {
		super.renderSketch();
		this.drawLineGrid();
		super.redrawContent();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMathPadImplementation#setMathEngineEnabled(boolean)
	 */
	@Override
	public void setMathEngineEnabled(boolean isEnabled) {
		mathEngineEnabled = isEnabled;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMathPadImplementation#startNewExpression()
	 */
	@Override
	public void startNewExpression() {
		if (engine.getExpression() != null) {
			expressions.add(engine.getExpression());
		}
		engine.clear();
	}
}
