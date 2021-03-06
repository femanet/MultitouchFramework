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

package com.github.multitouchframework.demo.feedback;

import com.github.multitouchframework.base.cursor.Cursor;
import com.github.multitouchframework.base.cursor.CursorUpdateEvent;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CursorsLayer extends AbstractFeedbackLayer<CursorUpdateEvent> {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -8344669485004582702L;

    private static final Color CURSOR_COLOR = UIManager.getColor("nimbusInfoBlue");

    private static final int CURSOR_SIZE = 6;

    private Collection<Cursor> cursors = null;

    /**
     * @see AbstractFeedbackLayer#processTouchEvent(com.github.multitouchframework.api.TouchEvent)
     */
    @Override
    public void processTouchEvent(CursorUpdateEvent event) {
        this.cursors = event.getCursors();
        triggerRepaint();
    }

    /**
     * @see AbstractFeedbackLayer#paintComponent(Graphics)
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if ((cursors != null) && !cursors.isEmpty()) {
            // Prepare for painting
            List<Point> canvasPoints = new ArrayList<Point>();
            for (Cursor cursor : cursors) {
                Point canvasPoint = convertCursorToComponent(cursor);
                canvasPoints.add(canvasPoint);
            }

            // Paint cursors
            graphics.setColor(CURSOR_COLOR);
            for (Point canvasPoint : canvasPoints) {
                graphics.fillOval(canvasPoint.x - CURSOR_SIZE / 2, canvasPoint.y - CURSOR_SIZE / 2, CURSOR_SIZE,
                        CURSOR_SIZE);
            }
        }
    }
}
