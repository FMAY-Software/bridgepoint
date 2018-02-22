package org.xtuml.bp.debug.ui.launch;

//====================================================================
//
//File:      $RCSfile: VerifiableElementInitializerDialog.java,v $
//Version:   $Revision: 1.3 $
//Modified:  $Date: 2013/01/10 23:17:46 $
//
//(c) Copyright 2005-2014 by Mentor Graphics Corp.  All rights reserved.
//
//====================================================================
// Licensed under the Apache License, Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License.  You may obtain a copy 
// of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the 
// License for the specific language governing permissions and limitations under
// the License.
//======================================================================== 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.xtuml.bp.core.BridgeParameter_c;
import org.xtuml.bp.core.ComponentReference_c;
import org.xtuml.bp.core.Component_c;
import org.xtuml.bp.core.DataType_c;
import org.xtuml.bp.core.EnumerationDataType_c;
import org.xtuml.bp.core.ExecutableProperty_c;
import org.xtuml.bp.core.FunctionParameter_c;
import org.xtuml.bp.core.ImportedProvision_c;
import org.xtuml.bp.core.ImportedRequirement_c;
import org.xtuml.bp.core.InterfaceReference_c;
import org.xtuml.bp.core.LocalValue_c;
import org.xtuml.bp.core.Local_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.OperationParameter_c;
import org.xtuml.bp.core.Port_c;
import org.xtuml.bp.core.PropertyParameter_c;
import org.xtuml.bp.core.ProvidedExecutableProperty_c;
import org.xtuml.bp.core.ProvidedOperation_c;
import org.xtuml.bp.core.ProvidedSignal_c;
import org.xtuml.bp.core.Provision_c;
import org.xtuml.bp.core.RequiredExecutableProperty_c;
import org.xtuml.bp.core.RequiredOperation_c;
import org.xtuml.bp.core.RequiredSignal_c;
import org.xtuml.bp.core.Requirement_c;
import org.xtuml.bp.core.RuntimeValue_c;
import org.xtuml.bp.core.SimpleCoreValue_c;
import org.xtuml.bp.core.SimpleValue_c;
import org.xtuml.bp.core.UserDataType_c;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.MultipleOccurrenceElement;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.editors.element.ElementTableDialog;
import org.xtuml.bp.core.editors.element.ElementTreeColumnEditorTab;
import org.xtuml.bp.core.editors.element.parameters.providers.ParameterValueContentProvider;
import org.xtuml.bp.core.editors.element.parameters.providers.ParameterValueLabelProvider;
import org.xtuml.bp.core.editors.providers.MetaModelLabelProvider;
import org.xtuml.bp.debug.ui.BPDebugUIPlugin;
import org.xtuml.bp.debug.ui.model.ParameterValueInitializer;
import org.xtuml.bp.ui.explorer.ModelContentProvider;
import org.xtuml.bp.ui.explorer.ModelLabelProvider;

public class VerifiableElementInitializerDialog extends DialogCellEditor {
	private static final String PARAMETER_INITIALIZATION_SEPARATOR = "_";
	private static final String PARAMETER_INITIALIZATION_CONFIGURATION_SEPARATOR = "&";
	VerifiableElementComposite parent = null;

	/**
	 * @Override
	 */
	public VerifiableElementInitializerDialog(VerifiableElementComposite p_parent) {
		super(p_parent.getTreeViewer().getTree());
		parent = p_parent;
	}

	/**
	 * When the user makes a selection from the tree in the initializer
	 * selection dialog, this class is used to validate and return the selection
	 * information.
	 */
	class VerifiableElementSelectionStatusValidator implements ISelectionStatusValidator {
		/**
		 * @implements
		 */
		public IStatus validate(Object[] selection) {
			Status okStatus = new Status(IStatus.OK, BPDebugUIPlugin.getUniqueIdentifier(), IStatus.OK, "", null);
			Status infoStatus = new Status(IStatus.ERROR, BPDebugUIPlugin.getUniqueIdentifier(), IStatus.WARNING,
					"Please select None, or a valid message.", null);
			if (selection.length == 0) {
				return infoStatus;
			}
			if (selection[0] instanceof Port_c) {
				return infoStatus;
			}
			if (selection[0] instanceof Requirement_c) {
				return infoStatus;
			}
			if (selection[0] instanceof Provision_c) {
				return infoStatus;
			}
			if (selection[0] instanceof PropertyParameter_c) {
				return infoStatus;
			}
			return okStatus;
		}
	}

	/**
	 * This is the class used to provide the content to the tree that is in the
	 * initializer selection dialog.
	 *
	 */
	class ElementInitilizerContentProvider implements ITreeContentProvider {
		ITreeContentProvider provider;

		public ElementInitilizerContentProvider(ITreeContentProvider p_provider) {
			provider = p_provider;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		/**
		 * Populate the selection tree for the given element
		 */
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof String)
				return new Object[0];
			if (inputElement instanceof IStructuredSelection) {
				inputElement = ((IStructuredSelection) inputElement).getFirstElement();
			}
			List<Object> elementList = new ArrayList<Object>();
			Object[] elements = provider.getElements(inputElement);
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] instanceof Port_c) {
					elementList.add((NonRootModelElement) elements[i]);
				} else if (elements[i] instanceof Provision_c) {
					elementList.add((NonRootModelElement) elements[i]);
				} else if (elements[i] instanceof Requirement_c) {
					elementList.add((NonRootModelElement) elements[i]);
				} else if (elements[i] instanceof RequiredOperation_c) {
					elementList.add((NonRootModelElement) elements[i]);
				} else if (elements[i] instanceof RequiredSignal_c) {
					elementList.add((NonRootModelElement) elements[i]);
				} else if (elements[i] instanceof ProvidedOperation_c) {
					elementList.add((NonRootModelElement) elements[i]);
				} else if (elements[i] instanceof ProvidedSignal_c) {
					elementList.add((NonRootModelElement) elements[i]);
				} else if (elements[i] instanceof ImportedProvision_c) {
					elementList.add((NonRootModelElement) elements[i]);
				} else if (elements[i] instanceof ImportedRequirement_c) {
					elementList.add((NonRootModelElement) elements[i]);
				}
			}
			if (inputElement instanceof Component_c || inputElement instanceof ComponentReference_c) {
				elementList.add(0, VerifierLaunchConfiguration.ConfigurationAttribute.DefaultInitializer);
			}
			List<PropertyParameter_c> parameters = new ArrayList<>();
			if (inputElement instanceof ProvidedSignal_c) {
				parameters = Arrays
						.asList(PropertyParameter_c.getManyC_PPsOnR4006(ExecutableProperty_c.getManyC_EPsOnR4501(
								ProvidedExecutableProperty_c.getManySPR_PEPsOnR4503((ProvidedSignal_c) inputElement))));
			} else if (inputElement instanceof RequiredSignal_c) {
				parameters = Arrays
						.asList(PropertyParameter_c.getManyC_PPsOnR4006(ExecutableProperty_c.getManyC_EPsOnR4500(
								RequiredExecutableProperty_c.getManySPR_REPsOnR4502((RequiredSignal_c) inputElement))));
			} else if (inputElement instanceof ProvidedOperation_c) {
				parameters = Arrays.asList(PropertyParameter_c
						.getManyC_PPsOnR4006(ExecutableProperty_c.getManyC_EPsOnR4501(ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4503((ProvidedOperation_c) inputElement))));
			} else if (inputElement instanceof RequiredOperation_c) {
				parameters = Arrays.asList(PropertyParameter_c
						.getManyC_PPsOnR4006(ExecutableProperty_c.getManyC_EPsOnR4500(RequiredExecutableProperty_c
								.getManySPR_REPsOnR4502((RequiredOperation_c) inputElement))));
			}
			if (parameters.size() > 0) {
				return parameters.toArray();
			}
			return elementList.toArray();
		}

		public boolean hasChildren(Object element) {
			return getElements(element).length != 0;
		}

		public Object getParent(Object element) {
			return provider.getParent(element);
		}

		public Object[] getChildren(Object parentElement) {
			return getElements(parentElement);
		}
	}

	/**
	 * Provide the labels for the tree nodes in the initializer selection tree
	 *
	 */
	class ElementInitilizerTreeLabelProvider extends MetaModelLabelProvider {
		ModelLabelProvider provider = new ModelLabelProvider();
		ParameterValueLabelProvider parameterProvider = new ParameterValueLabelProvider();

		public ElementInitilizerTreeLabelProvider() {
		}

		public void removeListener(ILabelProviderListener listener) {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void dispose() {
		}

		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof String) {
				return null;
			} else {
				if (columnIndex == 0) {
					return provider.getImage(element);
				}
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof String) {
				return (String) element;
			} else {
				if (columnIndex == 0) {
					return provider.getText(element);
				} else if (columnIndex == 1) {
					return parameterProvider.getColumnText(ParameterValueContentProvider.getSimpleCoreValue(element),
							columnIndex);
				}
			}
			return "";
		}

	}

	/**
	 * @Override
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		Object currentSelection = parent.getTreeViewer().getCurrentSelection();
		List<NonRootModelElement> modelParameters = getModelParameters(currentSelection);
		List<RuntimeValue_c> runtimeValues = ParameterValueInitializer
				.initializeRuntimeValues(modelParameters.toArray());
		String initializationString = getParameterInitializationString(currentSelection);
		runtimeValues.forEach(runtimeValue -> initializeParameterValue(initializationString, runtimeValue));
		ElementTableDialog initializerSelectionDialog = new ElementTableDialog(cellEditorWindow.getShell(),
				new StructuredSelection(currentSelection),
				new ElementInitilizerContentProvider(new ModelContentProvider()), "Select the initializer", "Choose") {

			@Override
			protected Point getInitialSize() {
				return new Point(750, 500);
			}

			@Override
			protected void createEditorTab(Composite composite) {
				createdTab = new ElementTreeColumnEditorTab(composite, initialElements, provider) {

					@Override
					protected MetaModelLabelProvider getMetaModelLabelProvider() {
						return new ElementInitilizerTreeLabelProvider();
					}

				};
			}

		};
		initializerSelectionDialog.setValidator(new VerifiableElementSelectionStatusValidator());
		int result = initializerSelectionDialog.open();
		if (result == 0) {
			Object element = initializerSelectionDialog.getSelection();
			if (element != null && element instanceof NonRootModelElement) {
				String id = getInitializerUUIDString((NonRootModelElement) element);
				String parameterInitializition = configureParameterInitializationString(runtimeValues);
				runtimeValues.forEach(
						runtimeValue -> {
							disconnectPropertyParameterFromRuntimValue(getPropertyParameterFromRuntimeValue(runtimeValue));
							runtimeValue.Dispose();
							});
				setInitializerForElement(currentSelection, id, parameterInitializition);
			} else if (element != null && element instanceof String) {
				setInitializerForElement(currentSelection, (String) element, "");
			}
		}
		return initializerSelectionDialog.getSelection();
	}

	private List<NonRootModelElement> getModelParameters(Object currentSelection) {
		List<NonRootModelElement> children = new ArrayList<>();
		getParameterChildren(currentSelection, children);
		return children;
	}

	private void getParameterChildren(Object parent, List<NonRootModelElement> childList) {
		ModelContentProvider contentProvider = new ModelContentProvider();
		Object[] children = contentProvider.getChildren(parent);
		for(Object child : children) {
			if(!considerChildAsParameter(child, childList)) {
				getParameterChildren(child, childList);
			}
		}
	}

	private boolean considerChildAsParameter(Object child, List<NonRootModelElement> childList) {
		if(child instanceof OperationParameter_c) {
			childList.add((NonRootModelElement) child);
			return true;
		}
		if(child instanceof FunctionParameter_c) {
			childList.add((NonRootModelElement) child);
			return true;
		}
		if(child instanceof BridgeParameter_c) {
			childList.add((NonRootModelElement) child);
			return true;
		}
		if(child instanceof MultipleOccurrenceElement) {
			NonRootModelElement realElement = ((MultipleOccurrenceElement) child).getElement();
			return considerChildAsParameter(realElement, childList);
		}
		if(child instanceof PropertyParameter_c) {
			childList.add((NonRootModelElement) child);
			return true;
		}
		return false;
	}
	
	static class RuntimeConfiguration {
		RuntimeValue_c runtimeValue;
		PropertyParameter_c parameter;
		Object value;
		
		@Override
		public int hashCode() {
			return parameter.hashCode();
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof RuntimeConfiguration) {
				if(parameter == ((RuntimeConfiguration) obj).parameter) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	public static void initializeParameterValue(String runtimeConfigurationString, RuntimeValue_c runtimeValue) {
		RuntimeConfiguration configuration = getRuntimeConfiguration(runtimeConfigurationString, runtimeValue);
		if(configuration != null) {
			VerifiableElementInitializerDialog.finalizeConfiguration(configuration);
		}
	}

	private static void finalizeConfiguration(RuntimeConfiguration configuration) {
		RuntimeValue_c runtimeValue = configuration.runtimeValue;
		PropertyParameter_c parameter = configuration.parameter;
		Object value = configuration.value;
		LocalValue_c localValue = LocalValue_c.getOneL_LVLOnR3001(Local_c.getOneL_LCLOnR3306(runtimeValue));
		parameter.relateAcrossR3017To(localValue);
		SimpleCoreValue_c simpleCoreValue = SimpleCoreValue_c.getOneRV_SCVOnR3308(SimpleValue_c.getOneRV_SMVOnR3300(runtimeValue));
		simpleCoreValue.setValue(value);
	}
	
	private static RuntimeConfiguration getRuntimeConfiguration(String runtimeConfigurationString, RuntimeValue_c runtimeValue) {
		String[] configurations = runtimeConfigurationString.split(PARAMETER_INITIALIZATION_SEPARATOR);
		for(String configuration : configurations) {
			String[] items = configuration.split(PARAMETER_INITIALIZATION_CONFIGURATION_SEPARATOR);
			if(items.length != 2) {
				continue;
			}
			UUID elementId = UUID.fromString(items[0]);
			PropertyParameter_c parameter = (PropertyParameter_c) Ooaofooa.getDefaultInstance()
					.getInstanceList(PropertyParameter_c.class).getGlobal(elementId);
			PropertyParameter_c runtimeParameter = PropertyParameter_c
					.getOneC_PPOnR3017(LocalValue_c.getOneL_LVLOnR3001(Local_c.getOneL_LCLOnR3306(runtimeValue)));
			if(parameter == runtimeParameter) {
				RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration();
				runtimeConfiguration.parameter = parameter;
				runtimeConfiguration.runtimeValue = runtimeValue;
				DataType_c type = DataType_c.getOneS_DTOnR3307(runtimeValue);
				runtimeConfiguration.value = convertFromString(type, items[1]);
				return runtimeConfiguration;
			}
		}
		return null;
	}

	private static Object convertFromString(DataType_c type, String string) {
		if(type.getName().equals("integer")) { //$NON-NLS-1$
			return Integer.valueOf(string);
		} else if(type.getName().equals("real")) { //$NON-NLS-1$
			return Float.valueOf(string);
		} else if(type.getName().equals("boolean")) { //$NON-NLS-1$
			return Boolean.valueOf(string);
		} else if(type.getName().equals("unique_id")) { //$NON-NLS-1$
			return UUID.fromString(string);
		} else {
			UserDataType_c udt = UserDataType_c.getOneS_UDTOnR17(type);
			if(udt != null) {
				return convertFromString(DataType_c.getOneS_DTOnR18(udt), string);
			}
		}
		return string;
	}

	private static String configureParameterInitializationString(List<RuntimeValue_c> runtimeValues) {
		String toConfigure = "";
		for(RuntimeValue_c value : runtimeValues) {
			toConfigure += getInitializtaionValueString(value);
			toConfigure += PARAMETER_INITIALIZATION_SEPARATOR;
		}
		return toConfigure;
	}

	private static String getInitializtaionValueString(RuntimeValue_c runtimeValue) {
		PropertyParameter_c parameter = getPropertyParameterFromRuntimeValue(runtimeValue);
		if(parameter == null) {
			return "";
		}
		SimpleCoreValue_c simpleCoreValue = SimpleCoreValue_c
				.getOneRV_SCVOnR3308(SimpleValue_c.getOneRV_SMVOnR3300(runtimeValue));
		return parameter.getPp_id().toString() + PARAMETER_INITIALIZATION_CONFIGURATION_SEPARATOR + simpleCoreValue.getValue().toString();
	}

	private static PropertyParameter_c getPropertyParameterFromRuntimeValue(RuntimeValue_c runtimeValue) {
		return PropertyParameter_c
				.getOneC_PPOnR3017(LocalValue_c.getOneL_LVLOnR3001(Local_c.getOneL_LCLOnR3306(runtimeValue)));
	}

	private static void disconnectPropertyParameterFromRuntimValue(PropertyParameter_c parameter) {
		if(parameter != null) {
			LocalValue_c localValue = LocalValue_c.getOneL_LVLOnR3017(parameter);
			parameter.unrelateAcrossR3017From(localValue);
		}
	}

	private String getParameterInitializationString(Object element) {
		if (element instanceof NonRootModelElement) {
			Vector<String> vector = parent.getElementVector(BPDebugUtils.getElementsSystem(element).getName());
			if (vector != null) {
				Iterator<String> iterator = vector.iterator();
				while (iterator.hasNext()) {
					String current = (String) iterator.next();
					if (current.startsWith(((NonRootModelElement) element).Get_ooa_id().toString())) {
						String parameterInitialization = VerifierLaunchConfiguration.getInternalElement(current,
								VerifierLaunchConfiguration.ConfigurationAttribute.ParameterInitialization);
						return parameterInitialization;
					}
				}
			}
		}
		return "";
	}
	
	protected NonRootModelElement getInitializerMessageElement(Object element) {
		if (element instanceof NonRootModelElement) {
			Vector<String> vector = parent.getElementVector(BPDebugUtils.getElementsSystem(element).getName());
			if (vector != null) {
				Iterator<String> iterator = vector.iterator();
				while (iterator.hasNext()) {
					String current = (String) iterator.next();
					if (current.startsWith(((NonRootModelElement) element).Get_ooa_id().toString())) {
						String id = VerifierLaunchConfiguration.getInternalElement(current,
								VerifierLaunchConfiguration.ConfigurationAttribute.Initializer);
						if (!id.equals(VerifierLaunchConfiguration.ConfigurationAttribute.DefaultInitializer)) {
							Component_c component = null;
							if (element instanceof Component_c) {
								component = (Component_c) element;
							} else if (element instanceof ComponentReference_c) {
								component = Component_c.getOneC_COnR4201((ComponentReference_c) element);
							}
							return getInitializerMessageFromUUIDString(component, id);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param element
	 * @return The current initializer string associated with the specified
	 *         element.
	 */
	protected String getInitializerForElement(Object element) {
		String resultInitializer = VerifierLaunchConfiguration.ConfigurationAttribute.DefaultInitializer;
		if (element instanceof NonRootModelElement) {
			Vector<String> modelVector = parent.getElementVector(BPDebugUtils.getElementsSystem(element).getName());
			if (modelVector != null) {
				Iterator<String> modelIterator = modelVector.iterator();
				while (modelIterator.hasNext()) {
					String currentLaunchConfigModel = (String) modelIterator.next();
					if (currentLaunchConfigModel.startsWith(((NonRootModelElement) element).Get_ooa_id().toString())) {
						String initializerID = VerifierLaunchConfiguration.getInternalElement(currentLaunchConfigModel,
								VerifierLaunchConfiguration.ConfigurationAttribute.Initializer);
						NonRootModelElement initializerElement = getInitializerMessageFromUUIDString(
								(NonRootModelElement) element, initializerID);
						if (initializerElement != null) {
							resultInitializer = initializerElement.getName();
						} else {
							resultInitializer = initializerID;
						}
						break;
					}
				}
			}
		}
		return resultInitializer;
	}

	public static NonRootModelElement getInitializerMessageFromUUIDString(NonRootModelElement parent, final String id) {
		Component_c component = null;
		if (parent instanceof Component_c) {
			component = (Component_c) parent;
		} else if (parent instanceof ComponentReference_c) {
			component = Component_c.getOneC_COnR4201((ComponentReference_c) parent);
		} else {
			return null;
		}
		RequiredOperation_c requiredOp = RequiredOperation_c.getOneSPR_ROOnR4502(
				RequiredExecutableProperty_c.getManySPR_REPsOnR4500(Requirement_c.getManyC_RsOnR4009(
						InterfaceReference_c.getManyC_IRsOnR4016(Port_c.getManyC_POsOnR4010(component)))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((RequiredOperation_c) candidate).getId().toString().equals(id);
					}

				});
		if (requiredOp != null) {
			return requiredOp;
		}
		RequiredSignal_c requiredSig = RequiredSignal_c.getOneSPR_RSOnR4502(
				RequiredExecutableProperty_c.getManySPR_REPsOnR4500(Requirement_c.getManyC_RsOnR4009(
						InterfaceReference_c.getManyC_IRsOnR4016(Port_c.getManyC_POsOnR4010(component)))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((RequiredSignal_c) candidate).getId().toString().equals(id);
					}

				});
		if (requiredSig != null) {
			return requiredSig;
		}
		ProvidedOperation_c providedOp = ProvidedOperation_c.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c.getManySPR_PEPsOnR4501(Provision_c.getManyC_PsOnR4009(
						InterfaceReference_c.getManyC_IRsOnR4016(Port_c.getManyC_POsOnR4010(component)))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate).getId().toString().equals(id);
					}

				});
		if (providedOp != null) {
			return providedOp;
		}
		ProvidedSignal_c providedSig = ProvidedSignal_c.getOneSPR_PSOnR4503(
				ProvidedExecutableProperty_c.getManySPR_PEPsOnR4501(Provision_c.getManyC_PsOnR4009(
						InterfaceReference_c.getManyC_IRsOnR4016(Port_c.getManyC_POsOnR4010(component)))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedSignal_c) candidate).getId().toString().equals(id);
					}

				});
		if (providedSig != null) {
			return providedSig;
		}
		return null;
	}

	protected void setInitializerForElement(Object element, String initializer, String parameterInitialization) {
		String mult = VerifierLaunchConfiguration.ConfigurationAttribute.DefaultMultiplicity;
		String enablement = VerifierLaunchConfiguration.DISABLED_STATE;
		String match = "";
		if (element instanceof NonRootModelElement) {
			Vector<String> vector = parent.getElementVector(BPDebugUtils.getElementsSystem(element).getName());

			// remove the old values
			Iterator<String> iterator = vector.iterator();
			while (iterator.hasNext()) {
				String current = (String) iterator.next();
				if (current.startsWith(((NonRootModelElement) element).Get_ooa_id().toString())) {
					match = current;
					break;
				}
			}
			if (!match.equals("")) {
				vector.remove(match);
				mult = VerifierLaunchConfiguration.getInternalElement(match,
						VerifierLaunchConfiguration.ConfigurationAttribute.Multiplicity);
				enablement = VerifierLaunchConfiguration.getInternalElement(match,
						VerifierLaunchConfiguration.ConfigurationAttribute.State);
			}

			String newResult = VerifierLaunchConfiguration.getComponentSelectionString(
					((NonRootModelElement) element).Get_ooa_id().toString(), mult, initializer, enablement,
					parameterInitialization);
			vector.add(newResult);

			parent.getTreeViewer().refresh(element);
			if (!match.equals(newResult)) {
				parent.updateControls();
			}
		}
	}

	protected String getInitializerUUIDString(NonRootModelElement element) {
		if (element instanceof RequiredOperation_c) {
			return ((RequiredOperation_c) element).getId().toString();
		} else if (element instanceof ProvidedOperation_c) {
			return ((ProvidedOperation_c) element).getId().toString();
		} else if (element instanceof RequiredSignal_c) {
			return ((RequiredSignal_c) element).getId().toString();
		} else if (element instanceof ProvidedSignal_c) {
			return ((ProvidedSignal_c) element).getId().toString();
		}
		return "";
	}

}
