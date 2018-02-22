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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.xtuml.bp.core.editors.ErrorToolTip;
import org.xtuml.bp.core.editors.ITabErrorSupport;
import org.xtuml.bp.core.editors.editing.ElementEditingSupport;
import org.xtuml.bp.core.editors.providers.MetaModelLabelProvider;

public class ElementEditorTab extends Composite implements IEditorElementTab {

	private TableViewer fTableViewer;
	public TableViewer getTableViewer() {
		return fTableViewer;
	}

	private ErrorToolTip tip;
	public ErrorToolTip getTip() {
		return tip;
	}

	private Button formalizeButton;
	public Button getFormalizeButton() {
		return formalizeButton;
	}

	private Button fEditDescripButton;

	public Button getEditDescripButton() {
		return fEditDescripButton;
	}

	public ElementEditorTab(Composite parent, Object inputObject, IStructuredContentProvider provider) {
		super(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		setLayout(layout);
		setLayoutData(new GridData(GridData.FILL_BOTH));
		createTableViewer(this, inputObject, provider);
		pack();
		fTableViewer.getTable().setFocus();
		if (fTableViewer.getElementAt(0) != null) {
			fTableViewer.editElement(fTableViewer.getElementAt(0), 0);
		}
	}


	class TabTableViewer extends TableViewer implements ITabErrorSupport {

		public TabTableViewer(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void setErrorMessage(String errorMessage) {
			if (tip == null) {
				tip = new ErrorToolTip(getShell());
			}
			if ((errorMessage != null) && !errorMessage.isEmpty()) {
				tip.setVisible(false);
				TableItem treeItem = fTableViewer.getTable().getSelection()[0];
				TableColumn column = fTableViewer.getTable().getColumn(0);
				tip.setText(errorMessage);
				Point location = new Point(column.getWidth(), treeItem.getBounds().y);
				Point controlPoint = fTableViewer.getTable().toDisplay(location);
				tip.autoSize();
				tip.setLocation(controlPoint.x, controlPoint.y - tip.getHeight() - 5);
				tip.setVisible(true);
			} else {
				tip.setVisible(false);
			}
		}

	}

	private void createTableViewer(Composite parent, final Object input, IStructuredContentProvider provider) {
		fTableViewer = new TabTableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		fTableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		fTableViewer.setUseHashlookup(true);
		fTableViewer.getTable().setHeaderVisible(true);
		fTableViewer.getTable().setLinesVisible(true);
		createInitialColumns(input);
		fTableViewer.getTable().pack();
		fTableViewer.setContentProvider(provider);
		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(fTableViewer,
				new FocusCellOwnerDrawHighlighter(fTableViewer));
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(fTableViewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		TableViewerEditor.create(fTableViewer, focusCellManager, actSupport,
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
				fTableViewer.refresh(cell.getElement());
				fTableViewer.getTable().update();
			}

			@Override
			public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {
				// do nothing
			}
		};
		fTableViewer.getColumnViewerEditor().addEditorActivationListener(activationListener);
		fTableViewer.setInput(input);
	}

	CellLabelProvider cellLabelProvider = new CellLabelProvider() {

		@Override
		public void update(ViewerCell cell) {
			MetaModelLabelProvider mmProvider = getMetaModelLabelProvider();
			int index = cell.getColumnIndex();
			Object cellObject = cell.getElement();
			Object[] children = ((IStructuredContentProvider) fTableViewer.getContentProvider())
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
		columnEditingSupport[0] = createColumn("Element", 135, false, 0);
		columnEditingSupport[1] = createColumn("Value", 135, true, 1);
	}
	
	public ElementEditingSupport[] getEditingSupport() {
		return columnEditingSupport;
	}
	
	public ElementEditingSupport createColumn(String name, int width, boolean supportsEditing, int index) {
		ElementEditingSupport editingSupport = null;
		TableViewerColumn column = new TableViewerColumn(fTableViewer, SWT.LEAD);
		column.getColumn().setText(name);
		column.getColumn().setWidth(width);
		column.setLabelProvider(cellLabelProvider);
		if (supportsEditing) {
			editingSupport = createEditingSupport(column.getViewer(), column.getColumn());
			column.setEditingSupport(editingSupport);
		}
		column.getColumn().setData("index", new Integer(index));
		return editingSupport;
	}

	protected ElementEditingSupport createEditingSupport(ColumnViewer viewer, Item item) {
		return new ElementEditingSupport(viewer, item);
	}
	public Object getTableSelection() {
		return ((IStructuredSelection) fTableViewer.getSelection()).getFirstElement();
	}

	@Override
	public ColumnViewer getViewer() {
		return fTableViewer;
	}

}
