package org.xtuml.bp.debug.ui.actions;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

public interface IParameterConfigurator {
	void configureParameters(IStructuredContentProvider provider, IStructuredSelection selection);
}
