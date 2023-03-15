package org.xtuml.bp.io.core;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.SystemModel_c;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.io.core.XtumlParser.Package_definitionContext;

public class SystemModelImportVisitor extends XtumlImportVisitor {

	public SystemModelImportVisitor(Ooaofooa modelRoot) {
		super(modelRoot);
	}

	@Override
	public NonRootModelElement visitPackage_definition(Package_definitionContext ctx) {
		// If we encounter the "system_model" mark, this is a system model
		// instance. Otherwise it is a regular package.
		final Map<String, Mark> marks = ctx.marks() != null ? visitMarks(ctx.marks())
				: Collections.emptyMap();
		if (marks.containsKey(SYSTEM_MODEL)) {
			// create a system model
			final SystemModel_c s_sys = SystemModel_c.resolveInstance(modelRoot, UUID.randomUUID(),
					visitName(ctx.pkg_name), false, visitName(ctx.pkg_name));
			// look for "use_globals=true" in the "@system_model" mark
			s_sys.setUseglobals(marks.get(SYSTEM_MODEL).getBoolean(USE_GLOBALS));
			return s_sys;
		} else {
			return null;
		}
	}

}
