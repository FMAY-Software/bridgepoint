package org.xtuml.bp.debug.ui.session.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.common.InstanceList;

public class ExecutionModelRoot extends Ooaofooa {

	private static Map<String, ExecutionModelRoot> executionRoots = new HashMap<>();
	private String coreRootId = "";
	private ExecutionModelRoot parentRoot;
	private List<ExecutionModelRoot> childRoots = new ArrayList<ExecutionModelRoot>();
	private HashMap<Class<?>, InstanceList> executionInstanceLists = new HashMap<>();

	public ExecutionModelRoot(String aRootId) {
		super(aRootId);
	}

	@Override
	public Object getPersistenceFile() {
		// no persistence
		return null;
	}

	@Override
	public boolean isPersisting() {
		// no persistence
		return false;
	}

	@Override
	public SystemModel_c getRoot() {
		return null;
	}
	
	public HashMap<Class<?>, InstanceList> getExecutionLists() {
		return executionInstanceLists;
	}
	
	@Override
	public synchronized InstanceList getInstanceList(Class<?> type) {
		InstanceList list = executionInstanceLists.get(type);
		if(list == null) {
			list = new ForwardingDebugInstanceList(this, type);
			executionInstanceLists.put(type, list);
		}
		return list;
	}

	public ExecutionModelRoot getParent() {
		return parentRoot;
	}

	public void setCoreRoot(String coreRootId) {
		this.coreRootId = coreRootId;
	}

	public static ExecutionModelRoot getInstance(String id) {
		ExecutionModelRoot root = executionRoots.get(id);
		if (root == null) {
			root = new ExecutionModelRoot(id);
			/* setup any registered delta collectors */
			setupDeltaCollectors(root);
			executionRoots.put(id, root);
		}
		return root;
	}

	/**
	 * Setup any plug-in registered delta collectors for the given root
	 */
	private static void setupDeltaCollectors(ExecutionModelRoot root) {

	}

	public void dispose() {
		setParent(null);
		executionRoots.remove(getId());
		for(ExecutionModelRoot child : childRoots) {
			child.dispose();
		}
	}
	
	private void addChild(ExecutionModelRoot child) {
		childRoots.add(child);
	}

	public void setParent(ExecutionModelRoot launchRoot) {
		parentRoot = launchRoot;
		parentRoot.addChild(this);
	}

	public InstanceList getCoreList(Class<?> type) {
		if(getParent() != null) {
			return (Ooaofooa.getInstance(getParent().coreRootId)).getILMap().get(type);
		}
		return Ooaofooa.getInstance(coreRootId).getILMap().get(type);
	}

}
