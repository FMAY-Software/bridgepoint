/*
 * generated by Xtext 2.33.0
 */
package org.xtuml.bp.xtext.masl.ui.tests;

import com.google.inject.Injector;
import org.eclipse.xtext.testing.IInjectorProvider;
import org.xtuml.bp.xtext.masl.ui.internal.MaslActivator;

public class MASLUiInjectorProvider implements IInjectorProvider {

	@Override
	public Injector getInjector() {
		return MaslActivator.getInstance().getInjector("org.xtuml.bp.xtext.masl.MASL");
	}

}
