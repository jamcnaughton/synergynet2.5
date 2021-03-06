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

package apps.dissection;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.jme.gfx.JMEGfxUtils;

import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;

/**
 * The Class SolutionFinder.
 */
public class SolutionFinder implements ItemListener {
	
	/** The log. */
	private LogWriter log;

	/** The quadril vertex. */
	private Vector3f quadrilVertex;
	
	/** The quadril vertices. */
	private Vector3f[] quadrilVertices;
	
	/** The shape names. */
	private String[] shapeNames = { "Pentagon1", "Right-angle-Triangle",
			"Trapezium", "Parallelogram", "Square", "Quadrilateral",
			"Pentagon2", "Pentagon3" };
	
	/** The solutions. */
	private DissectionSolution[] solutions;
	
	/** The tq. */
	private TriMesh tq;
	
	/** The triangle vertices. */
	private Vector3f[] triangleVertices;
	
	/** The tt. */
	private TriMesh tt;
	
	/** The user shapes. */
	private boolean[] userShapes = new boolean[shapeNames.length];
	
	// private static AudioTrack solutionSound;
	//
	// static {
	// AudioSystem as = AudioSystem.getSystem();
	// solutionSound =
	// as.createAudioTrack(DissectionResources.class.getResource("16235__dreamoron__turkkilasirumpu_boing5.ogg"),
	// true);
	// solutionSound.setLooping(false);
	// }
	//
	/**
	 * Instantiates a new solution finder.
	 *
	 * @param logName
	 *            the log name
	 */
	public SolutionFinder(String logName) {

		for (int i = 0; i < shapeNames.length; i++) {
			userShapes[i] = false;
		}
		
		log = new LogWriter(logName, false);
		log.write("Application started, users are trying to find the solutions...");
	}
	
	/**
	 * Calculate quadrilateral vertices.
	 */
	public void calculateQuadrilateralVertices() {
		Vector3f[] vertices = JMEGfxUtils.getWorldVertices(tq, 0);
		Vector3f[] vertices1 = JMEGfxUtils.getWorldVertices(tq, 1);
		setQuadrilVertices(vertices, vertices1[2]);
	}
	
	/**
	 * Calculate triangle vertices.
	 */
	public void calculateTriangleVertices() {
		Vector3f[] vertices = JMEGfxUtils.getWorldVertices(tt, 0);
		setTriangleVertices(vertices);
	}
	
	/**
	 * Check for shapes.
	 */
	public void checkForShapes() {

		float t = 10;
		int dis = 100;
		
		float disT0Q0 = JMEGfxUtils.distance(triangleVertices[0].x,
				quadrilVertices[0].x, triangleVertices[0].y,
				quadrilVertices[0].y);
		float disT0Q1 = JMEGfxUtils.distance(triangleVertices[0].x,
				quadrilVertices[1].x, triangleVertices[0].y,
				quadrilVertices[1].y);
		float disT0Q2 = JMEGfxUtils.distance(triangleVertices[0].x,
				quadrilVertices[2].x, triangleVertices[0].y,
				quadrilVertices[2].y);
		float disT0Q3 = JMEGfxUtils.distance(triangleVertices[0].x,
				quadrilVertex.x, triangleVertices[0].y, quadrilVertex.y);
		
		float disT1Q0 = JMEGfxUtils.distance(triangleVertices[1].x,
				quadrilVertices[0].x, triangleVertices[1].y,
				quadrilVertices[0].y);
		float disT1Q1 = JMEGfxUtils.distance(triangleVertices[1].x,
				quadrilVertices[1].x, triangleVertices[1].y,
				quadrilVertices[1].y);
		// float disT1Q2 = JMEGfxUtils.distance(triangleVertices[1].x,
		// quadrilVertices[2].x, triangleVertices[1].y, quadrilVertices[2].y);
		float disT1Q3 = JMEGfxUtils.distance(triangleVertices[1].x,
				quadrilVertex.x, triangleVertices[1].y, quadrilVertex.y);
		
		float disT2Q0 = JMEGfxUtils.distance(triangleVertices[2].x,
				quadrilVertices[0].x, triangleVertices[2].y,
				quadrilVertices[0].y);
		float disT2Q1 = JMEGfxUtils.distance(triangleVertices[2].x,
				quadrilVertices[1].x, triangleVertices[2].y,
				quadrilVertices[1].y);
		float disT2Q2 = JMEGfxUtils.distance(triangleVertices[2].x,
				quadrilVertices[2].x, triangleVertices[2].y,
				quadrilVertices[2].y);
		float disT2Q3 = JMEGfxUtils.distance(triangleVertices[2].x,
				quadrilVertex.x, triangleVertices[2].y, quadrilVertex.y);
		
		// user shape id: 0
		if ((disT0Q1 <= t) && (disT1Q0 <= t) && (disT2Q2 >= ((2 * dis) - t))) {
			checkTheShape(0);
		}

		// user shape id: 1
		if ((disT0Q0 <= t) && (disT1Q1 <= t) && (disT2Q2 >= (dis - t))) {
			checkTheShape(1);
		}
		
		// user shape id: 2
		if ((disT0Q2 <= t) && (disT2Q0 <= t) && (disT1Q3 >= ((3 * dis) - t))) {
			checkTheShape(2);
		}
		
		// user shape id: 3
		if ((disT0Q0 <= t) && (disT2Q2 <= t) && (disT1Q1 >= ((2 * dis) - t))) {
			checkTheShape(3);
		}
		
		// user shape id: 4
		if ((disT1Q1 <= t) && (disT2Q3 <= t) && (disT0Q0 >= ((2 * dis) - t))) {
			checkTheShape(4);
		}
		
		// user shape id: 5
		if ((disT0Q2 <= t) && (disT2Q3 <= t) && (disT1Q0 >= ((3 * dis) - t))) {
			checkTheShape(5);
		}

		// user shape id: 6
		if ((disT0Q3 <= t) && (disT2Q2 <= t) && (disT1Q1 >= ((2 * dis) - t))) {
			checkTheShape(6);
		}
		
		// user shape id: 7
		if ((disT1Q3 <= t) && (disT2Q1 <= t) && (disT0Q2 >= ((2 * dis) - t))) {
			checkTheShape(7);
		}
		
	}
	
	/**
	 * Check for solution.
	 */
	public void checkForSolution() {
		if (checkIfAllSolutionsAreFound()) {
			log.write("All Shapes have been constructed...");
			log.close();
		} else {
			calculateTriangleVertices();
			calculateQuadrilateralVertices();
			checkForShapes();
		}
	}
	
	/**
	 * Check if all solutions are found.
	 *
	 * @return true, if successful
	 */
	public boolean checkIfAllSolutionsAreFound() {
		for (int i = 0; i < shapeNames.length; i++) {
			if (userShapes[i] == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check the shape.
	 *
	 * @param id
	 *            the id
	 */
	public void checkTheShape(int id) {
		if (!userShapes[id]) {
			log.write(shapeNames[id] + " have been constructed...");
			userShapes[id] = true;
			solutions[id].getGraphic().setLocalLocation((id + 1) * 90, 70, 0);
			
			// solutionSound.play();

		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorChanged
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorChanged(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorClicked(ContentItem item, long id, float x, float y,
			float pressure) {
		checkForSolution();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorDoubleClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorDoubleClicked(ContentItem item, long id, float x,
			float y, float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorLongHeld
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorLongHeld(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorPressed
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorPressed(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorReleased
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorReleased(ContentItem item, long id, float x, float y,
			float pressure) {
		checkForSolution();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorRightClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorRightClicked(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/**
	 * Gets the shape names.
	 *
	 * @return the shape names
	 */
	public String[] getShapeNames() {
		return shapeNames;
	}
	
	/**
	 * Gets the tt.
	 *
	 * @return the tt
	 */
	public TriMesh getTt() {
		return tt;
	}

	/**
	 * Sets the quadril vertices.
	 *
	 * @param quadrilVertices
	 *            the quadril vertices
	 * @param vert3
	 *            the vert3
	 */
	public void setQuadrilVertices(Vector3f[] quadrilVertices, Vector3f vert3) {
		this.quadrilVertices = quadrilVertices;
		this.quadrilVertex = vert3;
	}

	/**
	 * Sets the solutions.
	 *
	 * @param sol
	 *            the new solutions
	 */
	public void setSolutions(DissectionSolution[] sol) {
		this.solutions = sol;
	}
	
	/**
	 * Sets the tq.
	 *
	 * @param tq
	 *            the new tq
	 */
	public void setTq(TriMesh tq) {
		this.tq = tq;
	}
	
	/**
	 * Sets the triangle vertices.
	 *
	 * @param triangleVertices
	 *            the new triangle vertices
	 */
	public void setTriangleVertices(Vector3f[] triangleVertices) {
		this.triangleVertices = triangleVertices;
	}
	
	/**
	 * Sets the tt.
	 *
	 * @param tt
	 *            the new tt
	 */
	public void setTt(TriMesh tt) {
		this.tt = tt;
	}
	
}
