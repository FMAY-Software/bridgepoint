package org.xtuml.bp.core.editors.element.parameters.providers;

import org.xtuml.bp.core.SimpleCoreValue_c;
import org.xtuml.bp.core.editors.providers.MetaModelLabelProvider;

public class ParameterValueLabelProvider extends MetaModelLabelProvider {

	@Override
	public String getColumnText(Object element, int index) {
		if(element instanceof SimpleCoreValue_c) {
			return ((SimpleCoreValue_c) element).getValue().toString();
		}
		return super.getColumnText(element, index);
	}

}
