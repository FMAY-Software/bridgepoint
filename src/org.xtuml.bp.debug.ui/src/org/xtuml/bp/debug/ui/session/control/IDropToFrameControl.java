package org.xtuml.bp.debug.ui.session.control;

import org.eclipse.debug.core.DebugException;
import org.xtuml.bp.debug.ui.model.BPThread;

public interface IDropToFrameControl {
	boolean canDropToFrame(BPThread thread);
	public void dropToFrame(BPThread thread) throws DebugException;
}
