package org.xtuml.bp.core.editors.element.parameters;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.xtuml.bp.core.DataType_c;
import org.xtuml.bp.core.EnumerationDataType_c;
import org.xtuml.bp.core.Enumerator_c;
import org.xtuml.bp.core.RuntimeValue_c;
import org.xtuml.bp.core.SimpleCoreValue_c;
import org.xtuml.bp.core.SimpleValue_c;
import org.xtuml.bp.core.UserDataType_c;
import org.xtuml.bp.core.editors.editing.ElementEditingSupport;
import org.xtuml.bp.core.editors.element.parameters.providers.ParameterValueContentProvider;
import org.xtuml.bp.core.ui.cells.editors.BooleanCellEditor;
import org.xtuml.bp.core.ui.cells.editors.EnumCellEditor;
import org.xtuml.bp.core.ui.cells.editors.FloatCellEditor;
import org.xtuml.bp.core.ui.cells.editors.IntegerCellEditor;

public class ParameterValueEditingSupport extends ElementEditingSupport {

	public ParameterValueEditingSupport(ColumnViewer viewer, Item tableColumn) {
		super(viewer, tableColumn);
	}

	@Override
	public void setValue(Object element, Object value) {
		SimpleCoreValue_c scv = ParameterValueContentProvider.getSimpleCoreValue(element); 
		DataType_c type = DataType_c
				.getOneS_DTOnR3307(RuntimeValue_c.getOneRV_RVLOnR3300(SimpleValue_c.getOneRV_SMVOnR3308(scv)));
		EnumerationDataType_c edt = EnumerationDataType_c.getOneS_EDTOnR17(type);
		if(edt != null) {
			if(value instanceof Integer && (int) value >= 0) {
				scv.setValue(getEnumerations(edt)[(int) value]);
			} else {
				scv.setValue("Unknown");
			}
		} else {
			scv.setValue(value.toString());
		}
	}

	@Override
	public Object getValue(Object element) {
		return ParameterValueContentProvider.getSimpleCoreValue(element).getValue();
	}

	@Override
	public boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected CellEditor getCoreCellEditor(Object element) {
		SimpleCoreValue_c scv = ParameterValueContentProvider.getSimpleCoreValue(element);
		if(scv == null) {
			return null;
		}
		DataType_c type = DataType_c
				.getOneS_DTOnR3307(RuntimeValue_c.getOneRV_RVLOnR3300(SimpleValue_c.getOneRV_SMVOnR3308(scv)));
		DataType_c core = type;
		UserDataType_c udt = UserDataType_c.getOneS_UDTOnR17(type);
		if (udt != null) {
			core = DataType_c.getOneS_DTOnR18(udt);
		}
		EnumerationDataType_c edt = EnumerationDataType_c.getOneS_EDTOnR17(type);
		if (edt != null) {
			EnumCellEditor editor = new EnumCellEditor((Composite) getViewer().getControl(), getEnumerations(edt));
			return editor;
		} else {
			if (core.getName().equals("integer")) {
				IntegerCellEditor editor = new IntegerCellEditor((Composite) getViewer().getControl(),
						Integer.MIN_VALUE, Integer.MAX_VALUE);
				return editor;
			} else if (core.getName().equals("boolean")) {
				BooleanCellEditor editor = new BooleanCellEditor((Composite) getViewer().getControl());
				return editor;
			} else if (core.getName().equals("string")) {
				TextCellEditor editor = new TextCellEditor((Composite) getViewer().getControl());
				return editor;
			} else if (core.getName().equals("real")) {
				FloatCellEditor editor = new FloatCellEditor((Composite) getViewer().getControl());
				return editor;
			}
		}
		return null;
	}

	private String[] getEnumerations(EnumerationDataType_c type) {
		Enumerator_c[] enumerators = Enumerator_c.getManyS_ENUMsOnR27(type);
		String[] enums = new String[enumerators.length];
		for(int i = 0; i < enumerators.length; i++) {
			enums[i] = type.getName() + "::" + enumerators[i].getName();
		}
		return enums;
	}

}
