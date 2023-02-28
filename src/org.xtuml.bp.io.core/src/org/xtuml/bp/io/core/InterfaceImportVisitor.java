package org.xtuml.bp.io.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.xtuml.bp.core.DataType_c;
import org.xtuml.bp.core.Elementtypeconstants_c;
import org.xtuml.bp.core.ExecutableProperty_c;
import org.xtuml.bp.core.Ifdirectiontype_c;
import org.xtuml.bp.core.InterfaceOperation_c;
import org.xtuml.bp.core.InterfaceSignal_c;
import org.xtuml.bp.core.Interface_c;
import org.xtuml.bp.core.Ooaofooa;
import org.xtuml.bp.core.Package_c;
import org.xtuml.bp.core.PackageableElement_c;
import org.xtuml.bp.core.PropertyParameter_c;
import org.xtuml.bp.core.Visibility_c;
import org.xtuml.bp.core.util.DimensionsUtil;
import org.xtuml.bp.io.core.XtumlParser.Interface_definitionContext;
import org.xtuml.bp.io.core.XtumlParser.Message_definitionContext;
import org.xtuml.bp.io.core.XtumlParser.ParameterContext;
import org.xtuml.bp.io.core.XtumlParser.Parameter_listContext;

public class InterfaceImportVisitor extends XtumlImportVisitor {

	public InterfaceImportVisitor(Ooaofooa modelRoot) {
		super(modelRoot);
	}

	@Override
	public Interface_c visitInterface_definition(Interface_definitionContext ctx) {
		final Package_c parent_pkg = (Package_c) currentRoot;

		// find or create interface
		final String iface_name = (String) visit(ctx.iface_name);
		final Interface_c iface = Interface_c.resolveInstance(modelRoot, UUID.randomUUID(), parent_pkg.getPackage_id(),
				iface_name, "", parent_pkg.getPath() + "::" + iface_name);
		final PackageableElement_c pe = new PackageableElement_c(modelRoot);
		pe.relateAcrossR8001To(iface);
		pe.setVisibility(Visibility_c.Public);
		pe.setType(Elementtypeconstants_c.INTERFACE);
		currentRoot = iface;

		// set interface description
		if (ctx.description() != null) {
			iface.setDescrip(ctx.description().getText().lines().map(line -> line.replace("//!", "").strip())
					.collect(Collectors.joining(System.lineSeparator())));
		}

		// link executable properties to interface
		final List<ExecutableProperty_c> c_eps = ctx.message_definition().stream()
				.map(m -> (ExecutableProperty_c) visit(m)).collect(Collectors.toList());
		for (ExecutableProperty_c c_ep : c_eps) {
			c_ep.relateAcrossR4003To(iface);
		}

		// link individual operations and signals together
		final List<InterfaceOperation_c> c_ios = c_eps.stream()
				.map(c_ep -> InterfaceOperation_c.getOneC_IOOnR4004(c_ep)).filter(c_io -> c_io != null)
				.collect(Collectors.toList());
		for (int i = 0; i + 1 < c_ios.size(); i++) {
			c_ios.get(i).relateAcrossR4019ToPrecedes(c_ios.get(i + 1));
		}
		final List<InterfaceSignal_c> c_ass = c_eps.stream().map(c_ep -> InterfaceSignal_c.getOneC_ASOnR4004(c_ep))
				.filter(c_as -> c_as != null).collect(Collectors.toList());
		for (int i = 0; i + 1 < c_ass.size(); i++) {
			c_ass.get(i).relateAcrossR4020ToPrecedes(c_ass.get(i + 1));
		}

		// link to the parent package last to prevent getting selected before messages
		// are loaded
		pe.relateAcrossR8000To(parent_pkg);

		return iface;
	}

	@Override
	public ExecutableProperty_c visitMessage_definition(Message_definitionContext ctx) {
		final Interface_c iface = (Interface_c) currentRoot;

		// parse message info
		final String name = (String) visit(ctx.msg_name);
		final int direction = "to".equals(ctx.direction.getText()) ? Ifdirectiontype_c.ClientServer
				: Ifdirectiontype_c.ServerClient;

		// find or create executable property
		final ExecutableProperty_c c_ep = ExecutableProperty_c.resolveInstance(modelRoot, UUID.randomUUID(),
				iface.getId(), direction, name, "", 0, iface.getPath() + "::" + name);
		if (ctx.type_reference() != null) {
			c_ep.unrelateAcrossR4004From(InterfaceOperation_c.getOneC_IOOnR4004(c_ep));
			final InterfaceOperation_c c_io = new InterfaceOperation_c(modelRoot);
			c_io.relateAcrossR4008To(voidType);
			c_ep.relateAcrossR4004To(c_io);
		} else {
			c_ep.unrelateAcrossR4004From(InterfaceSignal_c.getOneC_ASOnR4004(c_ep));
			c_ep.relateAcrossR4004To(new InterfaceSignal_c(modelRoot));
		}

		// get the message description
		final String messageDescription = ctx.description() != null ? ctx.description().getText().lines()
				.map(line -> line.replace("//!", "").strip()).collect(Collectors.joining(System.lineSeparator())) : "";

		// process marks
		// TODO eventually, this should tie in with the marking editor, but for
		// now it is just used to get the message number
		@SuppressWarnings("unchecked")
		final Map<String, Mark> marks = ctx.marks() != null ? (Map<String, Mark>) visit(ctx.marks())
				: Collections.emptyMap();
		if (marks.containsKey(MESSAGE_NUM)) {
			c_ep.setNumb(marks.get(MESSAGE_NUM).getInteger());
		}

		// set subtype specific info
		final InterfaceOperation_c c_io = InterfaceOperation_c.getOneC_IOOnR4004(c_ep);
		final InterfaceSignal_c c_as = InterfaceSignal_c.getOneC_ASOnR4004(c_ep);
		if (c_io != null) {
			c_io.setName(name);
			c_io.setDescrip(messageDescription);
			c_io.setDirection(direction);

			// set return type
			c_io.unrelateAcrossR4008From(DataType_c.getOneS_DTOnR4008(c_io));
			c_io.relateAcrossR4008To((DataType_c) Optional.ofNullable(visit(ctx.type_reference())).orElse(voidType));

			// set return dimensions
			String dim_string = getDimString(ctx.type_reference().array_type_reference());
			c_io.setReturn_dimensions(dim_string);
			List<Integer> dims = DimensionsUtil.getDimensionsData(dim_string, c_io);
			for (int i = 0; i < dims.size(); i++) {
				c_io.Resizereturn_dimensions(i, dims.get(i), dims.size());
			}
		} else {
			c_as.setName(name);
			c_as.setDescrip(messageDescription);
			c_as.setDirection(direction);
		}

		// process parameters
		if (ctx.parameter_list() != null) {
			currentRoot = c_ep;
			visit(ctx.parameter_list());
			currentRoot = iface;
		}

		return c_ep;
	}

	@Override
	public PropertyParameter_c visitParameter_list(Parameter_listContext ctx) {
		// link parameters to each other in order
		PropertyParameter_c prevPp = null;
		for (ParameterContext paramCtx : ctx.parameter()) {
			final PropertyParameter_c c_pp = (PropertyParameter_c) visit(paramCtx);
			if (prevPp != null) {
				c_pp.relateAcrossR4021ToPrecedes(prevPp);
			}
			prevPp = c_pp;
		}
		return prevPp;
	}

	@Override
	public PropertyParameter_c visitParameter(ParameterContext ctx) {
		final ExecutableProperty_c c_ep = (ExecutableProperty_c) currentRoot;

		// create a new parameter
		final PropertyParameter_c c_pp = new PropertyParameter_c(modelRoot);
		c_pp.setName((String) visit(ctx.param_name));

		// set by value/ref
		c_pp.setBy_ref("in".equals(ctx.by_ref.getText()) ? 0 : 1);

		// link the data type
		c_pp.relateAcrossR4007To((DataType_c)visit(ctx.type_reference()));

		// set the array dimensions
		final String dimString = getDimString(ctx.type_reference().array_type_reference());
		c_pp.setDimensions(dimString);
		List<Integer> dims = DimensionsUtil.getDimensionsData(dimString, c_pp);
		for (int i = 0; i < dims.size(); i++) {
			c_pp.Resizedimensions(i, dims.get(i), dims.size());
		}
		
		// link to message
		c_pp.relateAcrossR4006To(c_ep);

		return c_pp;
	}

}
