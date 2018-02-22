package org.xtuml.bp.debug.ui.session.control;

import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.common.InstanceList;
import org.xtuml.bp.core.common.ModelRoot;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.common.RuntimeInstanceList;

/**
 * This class forwards access calls to the core instance lists associated, it
 * allows for communication between execution instances and core instances
 * 
 * @author travislondon
 *
 */
class ForwardingDebugInstanceList extends RuntimeInstanceList {

	public ForwardingDebugInstanceList(ModelRoot aRoot, Class<?> aType) {
		super(aRoot, aType);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public synchronized Object get(Object key) {
		Object object = super.get(key);
		InstanceList coreList = ((ExecutionModelRoot) getRoot()).getCoreList(getType());
		if (object == null && coreList != null) {
			object = coreList.get(key);
		}
		return object;
	}

	@Override
	public Object getGlobal(Object key) {
		Object object = super.getGlobal(key);
		InstanceList coreList = ((ExecutionModelRoot) getRoot()).getCoreList(getType());
		if (object == null && coreList != null) {
			object = coreList.getGlobal(key);
		}
		return object;
	}

	@Override
	public Object getGlobal(Object key, boolean includeProxies) {
		Object object = super.getGlobal(key, includeProxies);
		InstanceList coreList = ((ExecutionModelRoot) getRoot()).getCoreList(getType());
		if (object == null && coreList != null) {
			object = coreList.getGlobal(key, includeProxies);
		}
		return object;
	}

	@Override
	public Object getGlobal(SystemModel_c src_system, Object key) {
		Object object = super.getGlobal(src_system, key);
		InstanceList coreList = ((ExecutionModelRoot) getRoot()).getCoreList(getType());
		if (object == null && coreList != null) {
			object = coreList.getGlobal(src_system, key);
		}
		return object;
	}

	@Override
	public Object getGlobal(SystemModel_c src_system, Object key, boolean includeProxies) {
		Object object = super.getGlobal(src_system, key, includeProxies);
		InstanceList coreList = ((ExecutionModelRoot) getRoot()).getCoreList(getType());
		if (object == null && coreList != null) {
			object = coreList.getGlobal(src_system, key, includeProxies);
		}
		return object;
	}

	@Override
	public synchronized NonRootModelElement get(int index) {
		NonRootModelElement object = super.size() == 0 ? null : super.get(index);
		InstanceList coreList = ((ExecutionModelRoot) getRoot()).getCoreList(getType());
		if (object == null && coreList != null) {
			object = coreList.get(index);
		}
		return object;
	}

	@Override
	public synchronized int size() {
		InstanceList instanceList = ((ExecutionModelRoot) getRoot()).getExecutionLists().get(getType());
		if(instanceList == null || super.size() == 0) {
			InstanceList coreList = ((ExecutionModelRoot) getRoot()).getCoreList(getType());
			if(coreList != null) {
				/* dealing with a core list */
				return coreList.size();
			}
		}
		return super.size();
	}

}
