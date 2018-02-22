package org.xtuml.bp.core.editors.element;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.TreeViewerFocusCellManager;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.xtuml.bp.core.editors.ErrorToolTip;
import org.xtuml.bp.core.editors.ITabErrorSupport;
import org.xtuml.bp.core.editors.editing.ElementEditingSupport;
import org.xtuml.bp.core.editors.element.parameters.ParameterValueEditingSupport;
import org.xtuml.bp.core.editors.providers.MetaModelLabelProvider;

public class ElementTreeColumnEditorTab extends Composite implements IEditorElementTab {

	private TreeViewer fTreeViewer;
	
	public TreeViewer getTreeViewer() {
		return fTreeViewer;
	}

	private ErrorToolTip tip;
	public ErrorToolTip getTip() {
		return tip;
	}

	public ElementTreeColumnEditorTab(Composite parent, Object inputObject, IStructuredContentProvider provider) {
		super(parent, SWT.None);
		GridLayout layout = new GridLayout(1, true);
		setLayout(layout);
		setLayoutData(new GridData(GridData.FILL_BOTH));
		createTreeViewer(this, inputObject, provider);
		pack();
		fTreeViewer.getTree().setFocus();
	}


	class TabTreeViewer extends TreeViewer implements ITabErrorSupport {

		public TabTreeViewer(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void setErrorMessage(String errorMessage) {
			if (tip == null) {
				tip = new ErrorToolTip(getShell());
			}
			if ((errorMessage != null) && !errorMessage.isEmpty()) {
				tip.setVisible(false);
				TreeItem treeItem = fTreeViewer.getTree().getSelection()[0];
				TreeColumn column = fTreeViewer.getTree().getColumn(0);
				tip.setText(errorMessage);
				Point location = new Point(column.getWidth(), treeItem.getBounds().y);
				Point controlPoint = fTreeViewer.getTree().toDisplay(location);
				tip.autoSize();
				tip.setLocation(controlPoint.x, controlPoint.y - tip.getHeight() - 5);
				tip.setVisible(true);
			} else {
				tip.setVisible(false);
			}
		}

	}

	private void createTreeViewer(Composite parent, final Object input, IStructuredContentProvider provider) {
		fTreeViewer = new TabTreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		fTreeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		fTreeViewer.setUseHashlookup(true);
		fTreeViewer.getTree().setHeaderVisible(true);
		fTreeViewer.getTree().setLinesVisible(true);
		createInitialColumns(input);
		fTreeViewer.getTree().pack();
		fTreeViewer.setContentProvider(provider);
		TreeViewerFocusCellManager focusCellManager = new TreeViewerFocusCellManager(fTreeViewer,
				new FocusCellOwnerDrawHighlighter(fTreeViewer));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(fTreeViewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		TreeViewerEditor.create(fTreeViewer, focusCellManager, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);
		ColumnViewerEditorActivationListener activationListener = new ColumnViewerEditorActivationListener() {

			@Override
			public void beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
				// do nothing
			}

			@Override
			public void beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
				// do nothing
			}

			@Override
			public void afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
				ViewerCell cell = (ViewerCell) event.getSource();
				fTreeViewer.refresh(cell.getElement());
				fTreeViewer.getTree().update();
			}

			@Override
			public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {
				// do nothing
			}
		};
		fTreeViewer.getColumnViewerEditor().addEditorActivationListener(activationListener);
		fTreeViewer.setInput(input);
	}

	CellLabelProvider cellLabelProvider = new CellLabelProvider() {

		@Override
		public void update(ViewerCell cell) {
			MetaModelLabelProvider mmProvider = getMetaModelLabelProvider();
			int index = cell.getColumnIndex();
			Object cellObject = cell.getElement();
			Object[] children = ((IStructuredContentProvider) fTreeViewer.getContentProvider())
					.getElements(cellObject);
			if(index != 0 && children.length == 1) {
				cellObject = children[0];
			}
			if(index == 0) {
				cell.setImage(mmProvider.getColumnImage(cellObject, index));
			}
			cell.setText(mmProvider.getColumnText(cellObject, index));
		}
	};
	
	public CellLabelProvider getCellLabelProvider() {
		return cellLabelProvider;
	}
	
	protected MetaModelLabelProvider getMetaModelLabelProvider() {
		return new MetaModelLabelProvider();
	}

	ElementEditingSupport[] columnEditingSupport = new ElementEditingSupport[2];
	private void createInitialColumns(Object input) {
		columnEditingSupport[0] = createColumn("Element", 300, false, 0);
		columnEditingSupport[1] = createColumn("Value", 300, true, 1);
	}
	
	public ElementEditingSupport[] getEditingSupport() {
		return columnEditingSupport;
	}
	
	public ElementEditingSupport createColumn(String name, int width, boolean supportsEditing, int index) {
		ElementEditingSupport editingSupport = null;
		TreeViewerColumn column = new TreeViewerColumn(fTreeViewer, SWT.LEAD);
		column.getColumn().setText(name);
		column.getColumn().setWidth(width);
		column.setLabelProvider(cellLabelProvider);
		if (supportsEditing) {
			editingSupport = new ParameterValueEditingSupport(column.getViewer(), column.getColumn());
			column.setEditingSupport(editingSupport);
		}
		column.getColumn().setData("index", new Integer(index));
		return editingSupport;
	}

	public Object getTreeSelection() {
		return ((IStructuredSelection) fTreeViewer.getSelection()).getFirstElement();
	}

	@Override
	public ColumnViewer getViewer() {
		return fTreeViewer;
	}

}
