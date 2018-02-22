package org.xtuml.bp.core.editors.element;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class ElementTableDialog extends SelectionStatusDialog {
	
	String title = "Edit Element";
	protected IStructuredSelection initialElements;
	public IEditorElementTab createdTab;
	private String buttonLabel;
	protected IStructuredContentProvider provider;
	private Object selected;
	private ISelectionStatusValidator selectionValidator;

	public ElementTableDialog(Shell shell, IStructuredSelection initialElements,
			IStructuredContentProvider provider, String title, String buttonLabel) {
		super(shell);
		this.title = title;
		this.initialElements = initialElements;
		this.buttonLabel = buttonLabel;
		this.provider = provider;
		setHelpAvailable(false);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createEditorTab(composite);
		createdTab.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStatus valid = Status.OK_STATUS;
				if(selectionValidator != null) {
					valid = selectionValidator.validate(((IStructuredSelection) event.getSelection()).toArray());
					updateStatus(valid);
				}
				if(valid.getMessage().equals("")) {
					selected = event.getSelection();
				}
			}
		});
		composite.pack();
		return composite;
	}

	@Override
	protected Point getInitialSize() {
		return super.getInitialSize();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, buttonLabel, true);
	}

	@Override
	protected void okPressed() {
		// apply any current editor values
		createdTab.getViewer().applyEditorValue();
		super.okPressed();
	}

	protected void createEditorTab(Composite composite) {
		createdTab = new ElementEditorTab(composite, initialElements, provider);
	}

	public Object getSelection() {
		if(selected == null) return null;
		return ((IStructuredSelection) selected).getFirstElement();
	}

	@Override
	protected void computeResult() {
		
	}

	public void setValidator(ISelectionStatusValidator selectionValidator) {
		this.selectionValidator = selectionValidator;
	}

}
