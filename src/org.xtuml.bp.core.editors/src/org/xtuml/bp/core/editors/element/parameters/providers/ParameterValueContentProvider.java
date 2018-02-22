package org.xtuml.bp.core.editors.element.parameters.providers;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.xtuml.bp.core.BridgeParameter_c;
import org.xtuml.bp.core.Bridge_c;
import org.xtuml.bp.core.ExecutableProperty_c;
import org.xtuml.bp.core.FunctionParameter_c;
import org.xtuml.bp.core.Function_c;
import org.xtuml.bp.core.LocalValue_c;
import org.xtuml.bp.core.Local_c;
import org.xtuml.bp.core.OperationParameter_c;
import org.xtuml.bp.core.Operation_c;
import org.xtuml.bp.core.PropertyParameter_c;
import org.xtuml.bp.core.ProvidedExecutableProperty_c;
import org.xtuml.bp.core.ProvidedOperation_c;
import org.xtuml.bp.core.ProvidedSignal_c;
import org.xtuml.bp.core.RequiredExecutableProperty_c;
import org.xtuml.bp.core.RequiredOperation_c;
import org.xtuml.bp.core.RequiredSignal_c;
import org.xtuml.bp.core.RuntimeValue_c;
import org.xtuml.bp.core.SimpleCoreValue_c;
import org.xtuml.bp.core.SimpleValue_c;
import org.xtuml.bp.core.editors.providers.MetaModelContentProvider;

public class ParameterValueContentProvider extends MetaModelContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IStructuredSelection) {
			return ((IStructuredSelection) parentElement).toArray();
		}
		if (parentElement instanceof Operation_c) {
			return OperationParameter_c.getManyO_TPARMsOnR117((Operation_c) parentElement);
		} else if (parentElement instanceof Function_c) {
			return FunctionParameter_c.getManyS_SPARMsOnR24((Function_c) parentElement);
		} else if (parentElement instanceof Bridge_c) {
			return BridgeParameter_c.getManyS_BPARMsOnR21((Bridge_c) parentElement);
		} else if (parentElement instanceof ProvidedOperation_c) {
			return PropertyParameter_c.getManyC_PPsOnR4006(ExecutableProperty_c.getManyC_EPsOnR4501(
					ProvidedExecutableProperty_c.getManySPR_PEPsOnR4503((ProvidedOperation_c) parentElement)));
		} else if (parentElement instanceof ProvidedSignal_c) {
			return PropertyParameter_c.getManyC_PPsOnR4006(ExecutableProperty_c.getManyC_EPsOnR4501(
					ProvidedExecutableProperty_c.getManySPR_PEPsOnR4503((ProvidedSignal_c) parentElement)));
		} else if (parentElement instanceof RequiredOperation_c) {
			return PropertyParameter_c.getManyC_PPsOnR4006(ExecutableProperty_c.getManyC_EPsOnR4500(
					RequiredExecutableProperty_c.getManySPR_REPsOnR4502((RequiredOperation_c) parentElement)));
		} else if (parentElement instanceof RequiredSignal_c) {
			return PropertyParameter_c.getManyC_PPsOnR4006(ExecutableProperty_c.getManyC_EPsOnR4500(
					RequiredExecutableProperty_c.getManySPR_REPsOnR4502((RequiredSignal_c) parentElement)));
		}
		SimpleCoreValue_c simpleCoreValue = getSimpleCoreValue(parentElement);
		if (simpleCoreValue != null) {
			return new Object[] { simpleCoreValue };
		}
		return new Object[0];
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}
	
	public static SimpleCoreValue_c getSimpleCoreValue(Object element) {
		if(element instanceof FunctionParameter_c) {
			return SimpleCoreValue_c.getOneRV_SCVOnR3308(SimpleValue_c.getOneRV_SMVOnR3300(
					RuntimeValue_c.getOneRV_RVLOnR3306(Local_c.getOneL_LCLOnR3007((FunctionParameter_c) element))));
		} else if(element instanceof BridgeParameter_c) {
			return SimpleCoreValue_c.getOneRV_SCVOnR3308(SimpleValue_c.getOneRV_SMVOnR3300(
					RuntimeValue_c.getOneRV_RVLOnR3306(Local_c.getOneL_LCLOnR3009((BridgeParameter_c) element))));
		} else if(element instanceof OperationParameter_c) {
			return SimpleCoreValue_c.getOneRV_SCVOnR3308(SimpleValue_c.getOneRV_SMVOnR3300(
					RuntimeValue_c.getOneRV_RVLOnR3306(Local_c.getOneL_LCLOnR3008((OperationParameter_c) element))));
		} else if(element instanceof PropertyParameter_c) {
			return SimpleCoreValue_c.getOneRV_SCVOnR3308(SimpleValue_c.getOneRV_SMVOnR3300(RuntimeValue_c.getOneRV_RVLOnR3306(
					Local_c.getOneL_LCLOnR3001((LocalValue_c.getOneL_LVLOnR3017((PropertyParameter_c) element))))));
		}
		return null;
	}
	
}
