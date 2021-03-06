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

package synergynetframework.appsystem.table.animationsystem.animelements;

import synergynetframework.appsystem.table.animationsystem.AnimationElement;
import synergynetframework.appsystem.table.appregistry.ApplicationControlError;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.ApplicationTaskManager;

/**
 * The Class ApplicationActivator.
 */
public class ApplicationActivator extends AnimationElement {
	
	/** The ai. */
	private ApplicationInfo ai;
	
	/**
	 * Instantiates a new application activator.
	 *
	 * @param ai
	 *            the ai
	 */
	public ApplicationActivator(ApplicationInfo ai) {
		this.ai = ai;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.animationsystem.AnimationElement#
	 * elementStart(float)
	 */
	@Override
	public void elementStart(float tpf) {
		try {
			ApplicationTaskManager.getInstance().setActiveApplication(ai);
		} catch (ApplicationControlError e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.animationsystem.AnimationElement#
	 * isFinished()
	 */
	@Override
	public boolean isFinished() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.animationsystem.AnimationElement#
	 * reset()
	 */
	@Override
	public void reset() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.animationsystem.AnimationElement#
	 * updateAnimationState(float)
	 */
	@Override
	public void updateAnimationState(float tpf) {

	}
	
}
