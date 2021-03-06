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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Rectangle;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDocViewerImplementation;

/**
 * The Class DocViewer.
 */
public class DocViewer extends Frame implements IDocViewerImplementation {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2278233963786197814L;
	
	/** The current page index. */
	protected int currentPageIndex = 0;

	/** The double click delay. */
	private int DOUBLE_CLICK_DELAY = 1000;

	/** The page count. */
	protected int pageCount;

	/** The click count. */
	private int pressCount = 0, clickCount = 0;

	/** The time. */
	private long time = System.currentTimeMillis();

	/**
	 * Instantiates a new doc viewer.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	protected DocViewer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IDocViewerImplementation#changePage()
	 */
	public void changePage() {
		((IDocViewerImplementation) this.contentItemImplementation)
				.changePage();
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * cursorClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorClicked(ContentItem item, long id, float x, float y,
			float pressure) {
		super.cursorClicked(item, id, x, y, pressure);
		/*
		 * DocViewer contentItem = (DocViewer)item; Rectangle center = new
		 * Rectangle(contentItem.getWidth() / 2 - 20, 0, 40, 40);
		 * if(center.contains(x, y)) { makeFullScreen(); }else if(x
		 * >contentItem.getWidth() / 2) { this.nextPage(); }else{
		 * this.previousPage(); }
		 */
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * cursorPressed
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorPressed(ContentItem item, long id, float x, float y,
			float pressure) {
		super.cursorPressed(item, id, x, y, pressure);
		pressCount++;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * cursorReleased
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorReleased(ContentItem item, long id, float x, float y,
			float pressure) {
		super.cursorReleased(item, id, x, y, pressure);
		pressCount--;
		if (pressCount != 0) {
			return;
		}
		clickCount++;
		if (((System.currentTimeMillis() - time) < DOUBLE_CLICK_DELAY)
				&& (clickCount == 2)) {
			DocViewer contentItem = (DocViewer) item;
			Rectangle center = new Rectangle((contentItem.getWidth() / 2) - 20,
					0, 40, 40);
			if (center.contains(x, y)) {
				makeFullScreen();
			} else if (x > (contentItem.getWidth() / 2)) {
				this.nextPage();
			} else {
				this.previousPage();
			}
		}
		if (clickCount == 2) {
			clickCount = 0;
		}
		time = System.currentTimeMillis();
	}

	/**
	 * First page.
	 */
	public void firstPage() {
		currentPageIndex = 0;
		changePage();
	}

	/**
	 * Gets the current page index.
	 *
	 * @return the current page index
	 */
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	/**
	 * Gets the page count.
	 *
	 * @return the page count
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * Goto page.
	 *
	 * @param pageIndex
	 *            the page index
	 */
	public void gotoPage(int pageIndex) {
		currentPageIndex = pageIndex;
		changePage();
	}
	
	/**
	 * Last page.
	 */
	public void lastPage() {
		currentPageIndex = this.getPageCount() - 1;
		changePage();
	}
	
	/**
	 * Make full screen.
	 */
	private void makeFullScreen() {
		
		int w = contentSystem.getScreenWidth();
		int h = contentSystem.getScreenHeight();

		this.setWidth(w);
		this.setHeight(h);
		this.setAngle(0f);
		this.setScale(1f);
		this.centerItem();

	}

	/**
	 * Next page.
	 */
	public void nextPage() {
		currentPageIndex = (currentPageIndex + 1) % pageCount;
		changePage();
	}
	
	/**
	 * Previous page.
	 */
	public void previousPage() {
		currentPageIndex = currentPageIndex - 1;
		if (currentPageIndex < 0) {
			currentPageIndex = this.getPageCount() - 1;
		}
		changePage();
	}
	
	/**
	 * Sets the page count.
	 *
	 * @param pageCount
	 *            the new page count
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}
