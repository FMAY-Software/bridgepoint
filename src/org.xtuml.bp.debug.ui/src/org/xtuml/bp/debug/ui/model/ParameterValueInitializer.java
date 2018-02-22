package org.xtuml.bp.debug.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.xtuml.bp.core.BridgeParameter_c;
import org.xtuml.bp.core.DataType_c;
import org.xtuml.bp.core.FunctionParameter_c;
import org.xtuml.bp.core.LocalValue_c;
import org.xtuml.bp.core.Local_c;
import org.xtuml.bp.core.OperationParameter_c;
import org.xtuml.bp.core.PropertyParameter_c;
import org.xtuml.bp.core.RuntimeValue_c;
import org.xtuml.bp.core.SimpleValue_c;
import org.xtuml.bp.core.common.NonRootModelElement;

public class ParameterValueInitializer {

	public static List<RuntimeValue_c> initializeRuntimeValues(Object[] children) {
		List<RuntimeValue_c> initializedValues = new ArrayList<RuntimeValue_c>();
		for (Object child : children) {
			if(child instanceof NonRootModelElement) {
				NonRootModelElement nrme = (NonRootModelElement) child;
				Local_c local = new Local_c(nrme.getModelRoot());
				RuntimeValue_c rtv = new RuntimeValue_c(nrme.getModelRoot());
				local.relateAcrossR3306To(rtv);
				LocalValue_c lvl = new LocalValue_c(nrme.getModelRoot());
				lvl.relateAcrossR3001To(local);
				DataType_c dt = null;
				if(child instanceof OperationParameter_c) {
				    dt = DataType_c.getOneS_DTOnR118((OperationParameter_c) child);
				    local.relateAcrossR3008To((OperationParameter_c) child);
				} else if(child instanceof BridgeParameter_c) {
					dt = DataType_c.getOneS_DTOnR22((BridgeParameter_c) child);
					local.relateAcrossR3009To((BridgeParameter_c) child);
				} else if(child instanceof PropertyParameter_c) {
					dt = DataType_c.getOneS_DTOnR4007((PropertyParameter_c) child);
					lvl.relateAcrossR3017To((PropertyParameter_c) child);
				} else if(child instanceof FunctionParameter_c) {
					dt = DataType_c.getOneS_DTOnR26((FunctionParameter_c) child);
					local.relateAcrossR3007To((FunctionParameter_c) child);
				}
				rtv.relateAcrossR3307To(dt);
				local.relateAcrossR3001To(lvl);
				rtv.Createsimplevalue();
				SimpleValue_c simple = SimpleValue_c.getOneRV_SMVOnR3300(rtv);
				rtv.Setdefault(0);
				simple.Initialize();
				initializedValues.add(rtv);
			}
		}
		return initializedValues;
	}

}
