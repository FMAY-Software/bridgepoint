package org.xtuml.bp.xtext.masl.ui;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.PlatformUI;

import com.google.inject.Injector;

public class MASLExecutableExtensionFactorySafe extends MASLExecutableExtensionFactory {
	
	private Future<Injector> asyncInjector() {
		CompletableFuture<Injector> future = CompletableFuture.supplyAsync(() -> {
			// schedule a job that we will join in this asynchronous task
			// which will allow the Xtext recovery to complete before we
			// start loading any Xtext editor
			Job nextInLine = WorkspaceJob.create("MASL Wait", (monitor) -> {
				// only using for determination of when its safe to run
			});
			nextInLine.schedule();
			try {
				nextInLine.join();
			} catch (InterruptedException e) {
			}
			return super.getInjector();
		});
		return future;
	}

	@Override
	protected Injector getInjector() {
//		// we see a conflict between the Xtext recovery thread
//		// and the UI thread if opening a MASL editor while the
//		// recovery thread is working
//		//
//		// This issue can be hard to hit but when it does the workspace
//		// is locked
//		//
//		Future<Injector> asyncInjector = asyncInjector();
//		while(!asyncInjector.isDone()) {
//			// while waiting allow the UI thread to continue
//			PlatformUI.getWorkbench().getDisplay().readAndDispatch();
//		}
//		try {
//			return asyncInjector.get();
//		} catch (InterruptedException | ExecutionException e) {
//			// on any exception just return the injector
			return super.getInjector();
//		}
	}

}
