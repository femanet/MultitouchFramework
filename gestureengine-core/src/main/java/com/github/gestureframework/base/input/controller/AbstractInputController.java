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

package com.github.gestureframework.base.input.controller;

import java.util.ArrayList;
import java.util.List;

import com.github.gestureframework.api.flow.TouchPointProcessor;
import com.github.gestureframework.api.input.controller.InputController;

public class AbstractInputController implements InputController {

	protected boolean started = false;

	protected final List<TouchPointProcessor> outputProcessors = new ArrayList<TouchPointProcessor>();

	@Override
	public void addNextBlock(final TouchPointProcessor outputProcessor) {
		outputProcessors.add(outputProcessor);
	}

	@Override
	public void removeNextBlock(final TouchPointProcessor outputProcessor) {
		outputProcessors.remove(outputProcessor);
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void start() {
		if (!started) {
			started = true;
		}
	}

	@Override
	public void stop() {
		started = false;
	}
}
