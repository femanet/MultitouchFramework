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

package com.github.multitouchframework.base.gesture.recognition.tap;

import com.github.multitouchframework.api.input.Cursor;
import com.github.multitouchframework.api.region.Region;
import com.github.multitouchframework.base.gesture.recognition.AbstractGestureRecognizer;

import java.util.Collection;

/**
 * Entity responsible for recognizing a single-/multiple-tab gesture.<br>The recognition is made on a per-region
 * basis.<br>Note that this recognizer works best after filtering the input and limiting the number of input touch
 * events.
 *
 * @see AbstractGestureRecognizer
 * @see TapEvent
 */
public class TapRecognizer extends AbstractGestureRecognizer<TapRecognizer.RegionContext, TapEvent> {

    /**
     * Context storing the state of recognition of the gesture for a single region.<br>The recognition is based on the
     * variation of touch points.
     */
    protected static class RegionContext {

        public int previousCursorCount = 0;

        public int previousTapCount = 0;

        public long previousTapTimestamp = 0;
    }

    /**
     * Default minimum number of cursors needed to perform the gesture.
     */
    public static final int DEFAULT_MIN_CURSOR_COUNT = 1;

    /**
     * Default maximum number of cursors allowed to perform the gesture.
     */
    public static final int DEFAULT_MAX_CURSOR_COUNT = Integer.MAX_VALUE;

    /**
     * Default maximum duration in milliseconds between two taps to consider them consecutive and being part of a
     * same series.
     */
    public static final int DEFAULT_CONSECUTIVE_TAP_TIMEOUT = 750; // ms

    private long consecutiveTapTimeout = 0;

    /**
     * Default constructor.<br>By default, 1 cursor is the minimum required to perform the gesture, and there is no
     * maximum. Also, the default timeout for consecutive taps is 750 ms.
     */
    public TapRecognizer() {
        this(DEFAULT_MIN_CURSOR_COUNT, DEFAULT_MAX_CURSOR_COUNT, DEFAULT_CONSECUTIVE_TAP_TIMEOUT);
    }

    /**
     * Constructor specifying the minimum and maximum number of cursors needed to perform the gesture.<br>The default
     * timeout for consecutive taps is 750 ms.
     *
     * @param minCursorCount Minimum number of cursors needed for the gesture.
     * @param maxCursorCount Maximum number of cursors allowed for the gesture.
     */
    public TapRecognizer(final int minCursorCount, final int maxCursorCount) {
        this(minCursorCount, maxCursorCount, DEFAULT_CONSECUTIVE_TAP_TIMEOUT);
    }

    /**
     * Constructor specifying the minimum and maximum number of cursors needed to perform the gesture, as well as the
     * timeout for consecutive taps.
     *
     * @param minCursorCount        Minimum number of cursors needed for the gesture.
     * @param maxCursorCount        Maximum number of cursors allowed for the gesture.
     * @param consecutiveTapTimeout Consecutive tap timeout in milliseconds.
     */
    public TapRecognizer(final int minCursorCount, final int maxCursorCount, final long consecutiveTapTimeout) {
        super(minCursorCount, maxCursorCount);
    }

    /**
     * Gets the consecutive tap timeout.
     *
     * @return Consecutive tap timeout in milliseconds.
     */
    public long getConsecutiveTapTimeout() {
        return consecutiveTapTimeout;
    }

    /**
     * Sets the consecutive tap timeout.
     *
     * @param consecutiveTapTimeout Consecutive tap timeout in milliseconds.
     */
    public void setConsecutiveTapTimeout(final long consecutiveTapTimeout) {
        this.consecutiveTapTimeout = consecutiveTapTimeout;
    }

    /**
     * @see AbstractGestureRecognizer#createContext(Region)
     */
    @Override
    protected RegionContext createContext(final Region region) {
        return new RegionContext();
    }

    /**
     * @see AbstractGestureRecognizer#process(Object, Region, Collection)
     */
    @Override
    protected void process(final RegionContext context, final Region region, final Collection<Cursor> cursors) {
        final int cursorCount = cursors.size();
        final long tapTimestamp = System.currentTimeMillis();

        // Check if at least 1 cursor is still on the region
        if (isGestureStillArmed(region, cursors)) {
            if (!isCursorCountValid(context.previousCursorCount) && isCursorCountValid(cursorCount)) {
                // Just starting a new tap (e.g. some fingers down)
                if ((tapTimestamp - context.previousTapTimestamp) > consecutiveTapTimeout) {
                    // The tap is the beginning of a new series
                    context.previousTapCount = 1;
                } else {
                    // The tap is consecutive to a previous tap
                    context.previousTapCount++;
                }
                context.previousTapTimestamp = tapTimestamp;
                context.previousCursorCount = cursorCount;

                // Notify listeners
                fireGestureEvent(new TapEvent(TapEvent.State.ARMED, region, context.previousTapCount,
                        context.previousCursorCount));
            } else if (isCursorCountValid(context.previousCursorCount) && !isCursorCountValid(cursorCount)) {
                // Just finishing to tap (e.g. all fingers up)
                context.previousTapTimestamp = System.currentTimeMillis();
                context.previousCursorCount = cursorCount;

                // Notify listeners
                fireGestureEvent(new TapEvent(TapEvent.State.PERFORMED, region, context.previousTapCount,
                        context.previousCursorCount));
                fireGestureEvent(new TapEvent(TapEvent.State.UNARMED, region, context.previousTapCount,
                        context.previousCursorCount));
            }
        } else {
            // All cursors are now out of the region, gesture should be unarmed
            context.previousCursorCount = 0;

            // TODO Do not fire all the time
            fireGestureEvent(new TapEvent(TapEvent.State.UNARMED, region, context.previousTapCount,
                    context.previousCursorCount));
        }
    }

    private boolean isGestureStillArmed(final Region region, final Collection<Cursor> cursors) {
        boolean stillArmed = false;

        if (cursors.isEmpty()) {
            stillArmed = true;
        } else for (final Cursor cursor : cursors) {
            if (region.isTouched(cursor)) {
                stillArmed = true;
                break;
            }
        }

        return stillArmed;
    }
}
