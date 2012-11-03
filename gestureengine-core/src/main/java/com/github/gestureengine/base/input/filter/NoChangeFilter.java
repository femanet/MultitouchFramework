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

package com.github.gestureengine.base.input.filter;

import com.github.gestureengine.api.flow.Cursor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NoChangeFilter extends AbstractInputFilter {

	private Set<Cursor> lastCursors = new HashSet<Cursor>();

	@Override
	public void process(final Collection<Cursor> cursors) {
		boolean changed = false;

		if (cursors.size() == lastCursors.size()) {
			// Same number of cursors as last event, so check if all cursors are the same
			for (final Cursor cursor : cursors) {
				if (!lastCursors.contains(cursor)) {
					changed = true;
					break;
				}
			}
		} else {
			// Not the same number of cursors as last event, so process them
			changed = true;
		}

		if (changed) {
			lastCursors = new HashSet<Cursor>(cursors);
			forwardToNextBlocks(cursors);
		}
	}
}