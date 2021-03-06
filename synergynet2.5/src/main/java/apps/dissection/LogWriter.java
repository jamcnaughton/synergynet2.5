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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * The Class LogWriter.
 */
public class LogWriter {

	/** The enabled. */
	private boolean enabled = true;
	
	/** The file name. */
	private String fileName;
	
	/** The log file. */
	private FileWriter logFile;
	
	/** The out. */
	private BufferedWriter out;
	
	/**
	 * Instantiates a new log writer.
	 *
	 * @param groupName
	 *            the group name
	 * @param enabled
	 *            the enabled
	 */
	public LogWriter(String groupName, boolean enabled) {
		if (enabled) {
			try {
				fileName = groupName + "_Log.txt";
				logFile = new FileWriter(fileName);
				out = new BufferedWriter(logFile);
			} catch (IOException io) {
				System.out.println("IO Exception");
			}
			
		}
	}
	
	/**
	 * Close.
	 */
	public synchronized void close() {
		if (!enabled) {
			return;
		}
		try {
			out.close();
		} catch (IOException io) {
			System.out.println("IO Exception");
		}
	}
	
	/**
	 * Delete.
	 */
	public synchronized void delete() {
		if (!enabled) {
			return;
		}
		File file = new File(fileName);
		file.delete();
	}
	
	/**
	 * Write.
	 *
	 * @param stringToWrite
	 *            the string to write
	 */
	public synchronized void write(String stringToWrite) {
		if (!enabled) {
			return;
		}
		try {
			out.write(new Date().toString() + " : ");
			out.write(stringToWrite + "\r\n");
			out.flush();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
}
