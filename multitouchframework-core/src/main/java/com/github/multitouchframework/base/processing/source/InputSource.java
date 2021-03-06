/*
 * Copyright (c) 2013, Patrick Moawad
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

package com.github.multitouchframework.base.processing.source;

import com.github.multitouchframework.api.Chainable;
import com.github.multitouchframework.api.TouchListener;
import com.github.multitouchframework.base.cursor.CursorUpdateEvent;

/**
 * Interface to be implemented by input controllers.
 * <p/>
 * Input controllers are the starting block of the whole chain of touch input processing. They provide cursors to one or
 * several cursor processors, typically {@link com.github.multitouchframework.base.processing.filter.Filter}s.
 *
 * @see Chainable
 * @see TouchListener
 * @see CursorUpdateEvent
 */
public interface InputSource extends Chainable<TouchListener<CursorUpdateEvent>> {

    /**
     * States whether the input controller is started and is able to provide cursors or not.
     *
     * @return True if the controller is started, false otherwise.
     */
    boolean isStarted();

    /**
     * Starts the input controller so that it can provide cursors.
     */
    void start();

    /**
     * Stops the input controller so that it no longer provide cursors.
     */
    void stop();
}
