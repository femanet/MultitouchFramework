/*
 * Copyright (c) 2012, Patrick Moawad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.gestureengine.base.input.controller;

import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Input controller making use of a TUIO client to provide touch points received from a TUIO server.
 *
 * @see AbstractInputController
 */
public class TuioController extends AbstractInputController {

	private class TuioInputAdapter implements TuioListener {

		private int nextTouchPointId = 0;

		/**
		 * @see TuioListener#addTuioObject(TuioObject)
		 */
		@Override
		public void addTuioObject(TuioObject tuioObject) {
			// Nothing to be done
		}

		/**
		 * @see TuioListener#updateTuioObject(TuioObject)
		 */
		@Override
		public void updateTuioObject(TuioObject tuioObject) {
			// Nothing to be done
		}

		/**
		 * @see TuioListener#removeTuioObject(TuioObject)
		 */
		@Override
		public void removeTuioObject(TuioObject tuioObject) {
			// Nothing to be done
		}

		/**
		 * @see TuioListener#addTuioCursor(TuioCursor)
		 */
		@Override
		public void addTuioCursor(TuioCursor tuioCursor) {
			// TODO
			System.out.println("TuioController$TuioInputAdapter.addTuioCursor: " + tuioCursor);
		}

		/**
		 * @see TuioListener#updateTuioCursor(TuioCursor)
		 */
		@Override
		public void updateTuioCursor(TuioCursor tuioCursor) {
			// TODO
			System.out.println("TuioController$TuioInputAdapter.updateTuioCursor: " + tuioCursor);
		}

		/**
		 * @see TuioListener#removeTuioCursor(TuioCursor)
		 */
		@Override
		public void removeTuioCursor(TuioCursor tuioCursor) {
			// TODO
			System.out.println("TuioController$TuioInputAdapter.removeTuioCursor: " + tuioCursor);
		}

		/**
		 * @see TuioListener#refresh(TuioTime)
		 */
		@Override
		public void refresh(TuioTime tuioTime) {
			// TODO
			System.out.println("TuioController$TuioInputAdapter.refresh: " + tuioTime);
		}
	}

//	private class TuioClientAdapter implements TUIOEvent {
//
//		/**
//		 * @see TUIOEvent#newCursorEvent(TUIOCursor)
//		 */
//		@Override
//		public void newCursorEvent(TUIOCursor tuioCursor) {
//			// TODO
//			System.out.println("TuioController$TuioClientAdapter.newCursorEvent: " + tuioCursor);
//		}
//
//		/**
//		 * @see TUIOEvent#removeCursorEvent(TUIOCursor)
//		 */
//		@Override
//		public void removeCursorEvent(TUIOCursor tuioCursor) {
//			// TODO
//			System.out.println("TuioController$TuioClientAdapter.removeCursorEvent: " + tuioCursor);
//		}
//
//		/**
//		 * @see TUIOEvent#moveCursorEvent(TUIOCursor)
//		 */
//		@Override
//		public void moveCursorEvent(TUIOCursor tuioCursor) {
//			// TODO
//			System.out.println("TuioController$TuioClientAdapter.moveCursorEvent: " + tuioCursor);
//		}
//	}

	/**
	 * Logger for this class.
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(TuioController.class);

	/**
	 * Default port to be used to connect to the TUIO server.
	 */
	private final static short DEFAULT_TUIO_PORT = 3333;

	/**
	 * Port to be used to connect to the TUIO server.
	 */
	private final short tuioPort;

	/**
	 * TUIO client receiving touch input from to the TUIO server.
	 */
	private TuioClient tuioClient;
//	private SingleThreadTUIOWrapper tuioClient;

	//	/**
//	 * Listener to the TUIO client, adapting the input events into {@link TouchPoint}s.
//	 */
//	private TUIOEvent tuioClientAdapter = new TuioClientAdapter();
	private TuioListener tuioClientAdapter = new TuioInputAdapter();

	/**
	 * Default constructor making use of the default TUIO port number to connect to the TUIO server.
	 *
	 * @see #DEFAULT_TUIO_PORT
	 */
	public TuioController() {
		this(DEFAULT_TUIO_PORT);
	}

	/**
	 * Constructor specifying the TUIO port number to connect to the TUIO server.
	 *
	 * @param tuioPort TUIO port number to connect to the TUIO server.
	 */
	public TuioController(final short tuioPort) {
		this.tuioPort = tuioPort;
	}

	/**
	 * @see AbstractInputController#start()
	 */
	@Override
	public void start() {
		// Ignore behavior of parent class
		if (isStarted()) {
			LOGGER.warn("TUIO input controller is already started");
		} else {
			// Connect to TUIO server if not already done before
			tuioClient = new TuioClient(tuioPort);
			tuioClient.connect();
//			final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			if (tuioClient == null) {
//				try {
//					tuioClient = new SingleThreadTUIOWrapper(new TUIOReceiver(screenSize.width, screenSize.height,
//																			  tuioPort));
//				} catch (SocketException e) {
//					LOGGER.error("Could not connect to TUIO server", e);
//				}
//			}

			// Add TUIO client adapter
//			if (tuioClient != null) {
//				tuioClient.addHandler(tuioClientAdapter);
			tuioClient.addTuioListener(tuioClientAdapter);

			// Everything went fine
			super.start();
//			}
		}
	}

	/**
	 * @see AbstractInputController#stop()
	 */
	@Override
	public void stop() {
		// Remove TUIO client adapter
		if (tuioClient != null) {
//			tuioClient.removeHandler(tuioClientAdapter);
			tuioClient.disconnect();
		}
		super.stop();
	}
}