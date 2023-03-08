package org.xtuml.bp.io.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.xtuml.bp.core.Actiondialect_c;
import org.xtuml.bp.core.Component_c;
import org.xtuml.bp.core.DataType_c;
import org.xtuml.bp.core.Elementtypeconstants_c;
import org.xtuml.bp.core.InterfaceReference_c;
import org.xtuml.bp.core.Interface_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.Parsestatus_c;
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
import org.xtuml.bp.core.Visibility_c;
import org.xtuml.bp.core.common.IdAssigner;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.util.DimensionsUtil;
import org.xtuml.bp.io.core.ProxyUtil.ProxyInstance;
import org.xtuml.bp.io.core.XtumlParser.Component_definitionContext;
import org.xtuml.bp.io.core.XtumlParser.Message_definitionContext;
import org.xtuml.bp.io.core.XtumlParser.ParameterContext;
import org.xtuml.bp.io.core.XtumlParser.Parameter_listContext;
import org.xtuml.bp.io.core.XtumlParser.Port_definitionContext;

public class ComponentImportVisitor extends XtumlImportVisitor {

	public ComponentImportVisitor(Ooaofooa modelRoot) {
		super(modelRoot);
	}

	@Override
	public Component_c visitComponent_definition(Component_definitionContext ctx) {
		// TODO consider components defined directly within other components
		final Package_c parent_pkg = (Package_c) currentRoot;

		// find or create component
		final String comp_name = (String) visit(ctx.comp_name);
		final Component_c comp = Component_c.resolveInstance(modelRoot, UUID.randomUUID(), IdAssigner.NULL_UUID,
				IdAssigner.NULL_UUID, comp_name, "", 0, IdAssigner.NULL_UUID, false, "", "",
				parent_pkg.getPath() + "::" + comp_name);
		final PackageableElement_c pe = new PackageableElement_c(modelRoot);
		pe.relateAcrossR8000To(parent_pkg);
		pe.relateAcrossR8001To(comp);
		pe.setVisibility(Visibility_c.Public);
		pe.setType(Elementtypeconstants_c.COMPONENT);
		currentRoot = comp;

		// set component description
		if (ctx.description() != null) {
			comp.setDescrip(ctx.description().getText().lines().map(line -> line.replace("//!", "").strip())
					.collect(Collectors.joining(System.lineSeparator())));
		}

		// process marks
		@SuppressWarnings("unchecked")
		final Map<String, Mark> marks = ctx.marks() != null ? (Map<String, Mark>) visit(ctx.marks())
				: Collections.emptyMap();
		if (marks.containsKey(MULTIPLICITY) && marks.get(MULTIPLICITY).getString().equals("many")) {
			comp.setMult(1);
		}
		if (marks.containsKey(KEY_LETTERS)) {
			comp.setKey_lett(marks.get(KEY_LETTERS).getString());
		}
		if (marks.containsKey(REALIZED)) {
			comp.setIsrealized(true);
			comp.setRealized_class_path(marks.get(REALIZED).getString());

		}

		// load all component items
		// TODO ensure packages load first?
		ctx.component_item().forEach(this::visit);

		return comp;
	}

	@Override
	public Port_c visitPort_definition(Port_definitionContext ctx) {
		final Component_c comp = (Component_c) currentRoot;

		// find the formalized interface
		Interface_c iface = null;
		if (ctx.iface_name != null) {
			try {
				iface = (Interface_c) executor.callAndWait(() -> {
					List<Interface_c> ifaces = findVisibleElements(searchRoot, Elementtypeconstants_c.INTERFACE)
							.stream().map(Interface_c::getOneC_IOnR8001)
							.filter(ifc -> ifc.getPath().endsWith((String) visit(ctx.iface_name)))
							.collect(Collectors.toList());
					if (ifaces.isEmpty()) {
						return Optional.empty();
					} else {
						if (ifaces.size() > 1) {
							throw new IllegalArgumentException(
									"The given path corresponds to more than one unique element");
						}
						return Optional.of(ifaces.get(0));
					}
				});
			} catch (Exception e) {
				throw new CoreImport.XtumlLoadException(
						"Failed to find interface '" + visit(ctx.iface_name) + "' for port definition.", e);
			}
		}

		// find or create port
		final String port_name = (String) visit(ctx.port_name);
		final Port_c port = Port_c.resolveInstance(modelRoot, UUID.randomUUID(), comp.getId(), port_name, 0, false, "",
				comp.getPath() + "::" + port_name);
		port.relateAcrossR4010To(comp);
		final NonRootModelElement oldRoot = currentRoot;
		currentRoot = port;

		// process marks
		@SuppressWarnings("unchecked")
		final Map<String, Mark> marks = ctx.marks() != null ? (Map<String, Mark>) visit(ctx.marks())
				: Collections.emptyMap();
		if (marks.containsKey(MULTIPLICITY) && marks.get(MULTIPLICITY).getString().equals("many")) {
			port.setMult(1);
		}
		if (marks.containsKey(KEY_LETTERS)) {
			port.setKey_lett(marks.get(KEY_LETTERS).getString());
		}
		if (marks.containsKey(HIDE_GRAPHIC)) {
			port.setDonotshowportoncanvas(true);
		}

		// create the interface reference
		final String informalName = marks.containsKey(INFORMAL_NAME) ? marks.get(INFORMAL_NAME).getString()
				: "Unnamed Interface";
		final InterfaceReference_c ir = InterfaceReference_c.resolveInstance(modelRoot, UUID.randomUUID(),
				IdAssigner.NULL_UUID, IdAssigner.NULL_UUID, port.getId(),
				port.getPath() + "::" + (iface != null ? iface.getName() : informalName));
		ir.relateAcrossR4016To(port);
		if (iface != null) {
			ir.relateAcrossR4012To(iface);
		}
		Provision_c c_p = null;
		Requirement_c c_r = null;
		if (ctx.direction.getText().equals("provided")) {
			c_p = Provision_c.resolveInstance(modelRoot, UUID.randomUUID(), "", informalName, "", "", ir.getPath());
			c_p.relateAcrossR4009To(ir);
		} else {
			c_r = Requirement_c.resolveInstance(modelRoot, UUID.randomUUID(), "", "", informalName, "", ir.getPath());
			c_r.relateAcrossR4009To(ir);
		}

		// set port description
		if (ctx.description() != null) {
			if (c_p != null) {
				c_p.setDescrip(ctx.description().getText().lines().map(line -> line.replace("//!", "").strip())
						.collect(Collectors.joining(System.lineSeparator())));
			} else if (c_r != null) {
				c_r.setDescrip(ctx.description().getText().lines().map(line -> line.replace("//!", "").strip())
						.collect(Collectors.joining(System.lineSeparator())));
			} else {
				CorePlugin.getDefault().getLog().warn("Could not set description for port: " + port.getName());
			}
		}

		// process all message definitions
		ir.Synchronizesignalsandoperations();
		ctx.message_definition().forEach(this::visit);

		currentRoot = oldRoot;
		return port;
	}

	@Override
	public NonRootModelElement visitMessage_definition(Message_definitionContext ctx) {

		// TODO Consider validating that the signature matches. At the moment, this
		// method simply matches by name. Note that this also assumes no overloading.

		final Port_c port = (Port_c) currentRoot;
		final Provision_c c_p = Provision_c.getOneC_POnR4009(InterfaceReference_c.getOneC_IROnR4016(port));
		final Requirement_c c_r = Requirement_c.getOneC_ROnR4009(InterfaceReference_c.getOneC_IROnR4016(port));
		final NonRootModelElement[] spr_pos = ProvidedOperation_c
				.getManySPR_POsOnR4503(ProvidedExecutableProperty_c.getManySPR_PEPsOnR4501(c_p));
		final NonRootModelElement[] spr_pss = ProvidedSignal_c
				.getManySPR_PSsOnR4503(ProvidedExecutableProperty_c.getManySPR_PEPsOnR4501(c_p));
		final NonRootModelElement[] spr_ros = RequiredOperation_c
				.getManySPR_ROsOnR4502(RequiredExecutableProperty_c.getManySPR_REPsOnR4500(c_r));
		final NonRootModelElement[] spr_rss = RequiredSignal_c
				.getManySPR_RSsOnR4502(RequiredExecutableProperty_c.getManySPR_REPsOnR4500(c_r));

		@SuppressWarnings("unchecked")
		final Map<String, Mark> marks = ctx.marks() != null ? (Map<String, Mark>) visit(ctx.marks())
				: Collections.emptyMap();

		try {
			final PortMessage msg = ProxyUtil.newProxy(PortMessage.class,
					Stream.of(spr_pos, spr_pss, spr_ros, spr_rss).flatMap(a -> Stream.of(a))
							.filter(m -> visit(ctx.msg_name).equals(m.getName())).findAny().orElseThrow());

			msg.setDialect(Actiondialect_c.oal); // TODO set dialect to OAL
			if (marks.containsKey(MESSAGE_NUM)) {
				msg.setNumb(marks.get(MESSAGE_NUM).getInteger());
			}
			msg.setSuc_pars(marks.containsKey(NOPARSE) ? Parsestatus_c.doNotParse : Parsestatus_c.parseInitial);
			msg.setAction_semantics_internal((String) visit(ctx.action_body()));
			return (NonRootModelElement) msg.getBasisObject();
		} catch (NoSuchElementException e) {
			CorePlugin.logError("Could not find message in interface with name: " + visit(ctx.msg_name), e);
			return null;
		}

	}

	@Override
	public PropertyParameter_c visitParameter_list(Parameter_listContext ctx) {
		// link parameters to each other in order
		PropertyParameter_c prevPp = null;
		for (ParameterContext paramCtx : ctx.parameter()) {
			final PropertyParameter_c c_pp = (PropertyParameter_c) visit(paramCtx);
			if (prevPp != null) {
				prevPp.relateAcrossR4021ToPrecedes(c_pp);
			}
			prevPp = c_pp;
		}
		return prevPp;
	}

	@Override
	public PropertyParameter_c visitParameter(ParameterContext ctx) {
		// create a new parameter
		PropertyParameter_c c_pp = new PropertyParameter_c(modelRoot);
		c_pp.setName((String) visit(ctx.param_name));
		c_pp.relateAcrossR4007To(integerType);

		// set by value/ref
		c_pp.setBy_ref("in".equals(ctx.by_ref.getText()) ? 0 : 1);

		// link the data type
		c_pp.unrelateAcrossR4007From(DataType_c.getOneS_DTOnR4007(c_pp));
		c_pp.relateAcrossR4007To((DataType_c) Optional.ofNullable(visit(ctx.type_reference())).orElse(integerType));

		// set the array dimensions
		String dim_string = getDimString(ctx.type_reference().array_type_reference());
		c_pp.setDimensions(dim_string);
		List<Integer> dims = DimensionsUtil.getDimensionsData(dim_string, c_pp);
		for (int i = 0; i < dims.size(); i++) {
			c_pp.Resizedimensions(i, dims.get(i), dims.size());
		}

		return c_pp;
	}

	private interface PortMessage extends ProxyInstance {
		String getName();

		void setSuc_pars(int value);

		void setNumb(int value);

		void setDialect(int value);

		void setAction_semantics_internal(String value);
	}

}
