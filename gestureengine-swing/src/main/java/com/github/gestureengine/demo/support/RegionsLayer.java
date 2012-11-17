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

package com.github.gestureengine.demo.support;

import com.github.gestureengine.api.flow.Bounds;
import com.github.gestureengine.api.flow.Cursor;
import com.github.gestureengine.api.flow.CursorPerRegionProcessor;
import com.github.gestureengine.api.region.TouchableRegion;
import com.github.gestureengine.base.region.DefaultCursorToRegionDispatcher;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;

public class RegionsLayer implements Layer, CursorPerRegionProcessor {

	private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

	private final Canvas canvas;

	private DefaultCursorToRegionDispatcher cursorToRegionDispatcher = null;

	private final Map<TouchableRegion, Collection<Cursor>> cursorsForRegions =
			new HashMap<TouchableRegion, Collection<Cursor>>();

	public RegionsLayer(final Canvas canvas) {
		this.canvas = canvas;
	}

	public DefaultCursorToRegionDispatcher getRegionProvider() {
		return cursorToRegionDispatcher;
	}

	public void setRegionProvider(final DefaultCursorToRegionDispatcher cursorToRegionDispatcher) {
		this.cursorToRegionDispatcher = cursorToRegionDispatcher;
	}

	@Override
	public void process(final TouchableRegion region, final Collection<Cursor> cursors) {
		cursorsForRegions.put(region, cursors);
		canvas.repaint();
	}

	@Override
	public void paint(final Graphics2D g2d) {
		g2d.setColor(UIManager.getColor("nimbusGreen"));

		// Draw all regions
		if (cursorToRegionDispatcher != null) {
			for (final TouchableRegion region : cursorToRegionDispatcher.getRegions()) {
				final Bounds bounds = convertScreenBoundsToCanvas(((DummyRegion) region).getTouchableBounds());
				g2d.drawRect(bounds.getX(), bounds.getY(), bounds.getWidth() - 1, bounds.getHeight() - 1);
			}
		}

		// Fill all touched regions
		for (final Map.Entry<TouchableRegion, Collection<Cursor>> entry : cursorsForRegions.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				if (!DefaultCursorToRegionDispatcher.SCREEN_REGION.equals(entry.getKey())) {
					final Bounds bounds =
							convertScreenBoundsToCanvas(((DummyRegion) entry.getKey()).getTouchableBounds());
					g2d.fillRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
				}
			}
		}
	}

	private Bounds convertScreenBoundsToCanvas(final Bounds screenBounds) {
		final int canvasX = screenBounds.getX() * canvas.getWidth() / SCREEN_SIZE.width;
		final int canvasY = screenBounds.getY() * canvas.getHeight() / SCREEN_SIZE.height;
		final int canvasWidth = screenBounds.getWidth() * canvas.getWidth() / SCREEN_SIZE.width;
		final int canvasHeight = screenBounds.getHeight() * canvas.getHeight() / SCREEN_SIZE.height;

		return new Bounds(screenBounds.getId(), canvasX, canvasY, canvasWidth, canvasHeight);
	}
}