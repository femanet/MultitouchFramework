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

package com.github.gestureengine.swing.flow;

import com.github.gestureengine.api.flow.Cursor;
import com.github.gestureengine.api.flow.CursorProcessor;
import com.github.gestureengine.api.flow.CursorProcessorBlock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Cursor processor block forwarding the cursors on the Event Dispatch Thread.
 */
public class EDTCursorProcessorBlock implements CursorProcessorBlock<CursorProcessor> {

	/**
	 * Blocks that are queued to this block.
	 */
	private final List<CursorProcessor> nextBlocks = Collections.synchronizedList(new ArrayList<CursorProcessor>());

	/**
	 * Default constructor.
	 */
	public EDTCursorProcessorBlock() {
		// Nothing to be done
	}

	/**
	 * Constructor specifying the first black to be queued to this block.
	 *
	 * @param firstNextBlock First block to be queued.
	 *
	 * @see #queue(CursorProcessor)
	 */
	public EDTCursorProcessorBlock(final CursorProcessor firstNextBlock) {
		nextBlocks.add(firstNextBlock);
	}

	/**
	 * @see CursorProcessorBlock#queue(Object)
	 */
	@Override
	public void queue(final CursorProcessor nextBlock) {
		nextBlocks.add(nextBlock);
	}

	/**
	 * @see CursorProcessorBlock#dequeue(Object)
	 */
	@Override
	public void dequeue(final CursorProcessor nextBlock) {
		nextBlocks.remove(nextBlock);
	}

	/**
	 * Forwards the specified cursors to the next blocks on the EDT.
	 *
	 * @see CursorProcessorBlock#process(Collection)
	 */
	@Override
	public void process(final Collection<Cursor> cursors) {
		// Just put the cursors in a new list, no need to clone them
		final Collection<Cursor> copiedData = new ArrayList<Cursor>(cursors);

		final Runnable edtRunnable = new Runnable() {
			@Override
			public void run() {
				synchronized (nextBlocks) {
					for (final CursorProcessor nextBlock : nextBlocks) {
						nextBlock.process(copiedData);
					}
				}
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {
			edtRunnable.run();
		} else {
			SwingUtilities.invokeLater(edtRunnable);
		}
	}
}
