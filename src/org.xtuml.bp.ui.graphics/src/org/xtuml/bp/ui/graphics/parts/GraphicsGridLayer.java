package org.xtuml.bp.ui.graphics.parts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.editparts.GridLayer;
import org.xtuml.bp.ui.graphics.Activator;

public class GraphicsGridLayer extends GridLayer {
	public GraphicsGridLayer() {
		super();
	}

	@Override
	protected void paintGrid(Graphics g) {
		g.setForegroundColor(Activator.getDefault().getColor(88, 88, 88));
		super.paintGrid(g);
	}
	
}
