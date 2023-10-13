.//====================================================================
.//
.// File:      $RCSfile: gen_stream_export.arc,v $
.// Version:   $Revision: 1.31.16.1 $
.// Modified:  $Date: 2013/07/26 10:13:36 $
.//
.// (c) Copyright 2007-2014 by Mentor Graphics Corp.  All rights reserved.
.//
.//====================================================================
.//
.// Generate the Java code that exports the model in a format
.// that Model Builder can read to an output stream.
.//
.//====================================================================
.//
.assign io_core = "../org.xtuml.bp.io.core"
.assign class_name = "ExportModelStream"
.assign tool = "7.1"
.include "${io_core}/arc/export_functions.inc"
//========================================================================
//
// File: ${class_name}.java
//
// WARNING:      Do not edit this generated file
// Generated by: ${info.arch_file_name}
// Version:      $$Revision: 1.31.16.1 $$
//
// (c) Copyright 2007-2014 by Mentor Graphics Corp.  All rights reserved.
//
//========================================================================
// Licensed under the Apache License, Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License.  You may obtain a copy 
// of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   See the 
// License for the specific language governing permissions and limitations under
// the License.
//========================================================================
//
package org.xtuml.bp.io.mdl;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.xtuml.bp.core.common.PersistableModelComponent;
import org.xtuml.bp.core.common.PersistenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.HashMap;
import java.util.Locale;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.dnd.Clipboard;

import org.xtuml.bp.core.*;
import org.xtuml.bp.core.CorePlugin;
import org.xtuml.bp.ui.canvas.*;
import org.xtuml.bp.io.core.*;
import org.xtuml.bp.core.common.ClassQueryInterface_c;
import org.xtuml.bp.core.common.NonRootModelElement;
import org.xtuml.bp.core.common.IdAssigner;

public class ${class_name} extends CoreExport {
  NonRootModelElement m_inst;

  protected PrintStream m_fh;

  protected IPath m_path;

  protected boolean m_export_graphics = true;
  protected boolean m_fileExportInProgress = false;

  OutputStream m_outStream;

  protected boolean	m_is_fully_derived = false;

  public ${class_name}(Ooaofooa modelRoot, OutputStream outStream,
      boolean export_graphics, NonRootModelElement[] instances,
      boolean file_export_in_progress) {
    super(modelRoot, CoreExport.NO);
        m_instances = instances;
    m_outStream = outStream;
    m_fh = new PrintStream(m_outStream);
    m_fileExportInProgress = file_export_in_progress;
  }

  Set<String> savedRTOSystems = new HashSet<String>();
  Set<Object> writtenProxies = new HashSet<Object>();
  List<ProxyPath> collectedProxies = new ArrayList<ProxyPath>();
  Set<Object> writtenInstances = new HashSet<Object>();
  NonRootModelElement[] m_instances = null;

  public Set<String> getSavedRTOSystems() { return savedRTOSystems; }
  
  /* (non-Javadoc)
   * @see org.eclipse.core.resources.IWorkspaceRunnable#run(org.eclipse.core.runtime.IProgressMonitor)
   */
  public void run(IProgressMonitor pm) throws InvocationTargetException {
    try {
      savedRTOSystems.clear();
      Ooaofgraphics graphicsModelRoot = getGraphicsModelRoot();
      pm.beginTask("Exporting data...", Model_c.ModelInstances(graphicsModelRoot).length);
      // setup root types contained string
      String rootTypes = "-- root-types-contained: ";
      ArrayList<String> types = new ArrayList<String>();
      for(int i = 0; i < m_instances.length; i++) {
        if(m_instances[i] instanceof GraphicalElement_c) continue;
        if(!types.contains(m_instances[i].getClass().getSimpleName())) {
          types.add(m_instances[i].getClass().getSimpleName());
        }
      }
      for (int i = 0; i < types.size(); i++) {
        if(i == types.size() - 1) {
          rootTypes += types.get(i);
        } else {
          rootTypes += types.get(i) + ",";
        }
      }
      m_fh.print(rootTypes + "\n");
      m_fh.print(get_file_header("--", "StreamData", "7.1", CorePlugin.getPersistenceVersion())); //$$NON-NLS-1$$ //$$NON-NLS-2$$
      for(int i = 0; i < m_instances.length; i++) {
          m_inst = m_instances[i];
          PersistableModelComponent component = PersistenceManager.getComponent(m_inst);
          if(component != null) {
          	m_path = component.getFile().getLocation();
          }
          if(m_inst.getModelRoot() instanceof Ooaofooa)
            m_modelRoot = (Ooaofooa) m_inst.getModelRoot();
.select many eos from instances of EO where (selected.writePosition != "none")
.for each eo in eos
  .select any aep related by eo->AEP[R10]
  .if(empty aep)
    .select many otherEOs from instances of EO where (selected.Name == eo.Name)
    .for each otherEO in otherEOs
      .select any otherAep related by otherEO->AEP[R10]
      .if(not_empty otherAep)
        .assign aep = otherAep 
      .end if
    .end for
  .end if
  .select one table related by eo->EI[R3]->T[R4]
  .if(not_empty table)
    .invoke className = get_class_name(table)
        if(m_inst instanceof ${className.body}) {
    .if(empty aep)
          export_${className.body}((${className.body})m_inst, pm, false, true );
    .else
          export_${className.body}((${className.body})m_inst, pm, false, true, true);
    .end if
          continue;
        }
  .end if
.end for
      }
      writeCollectedProxies();
      m_fh.close();
      pm.done();
    } catch (IOException e) {
      // do nothing
    }
    writtenProxies.clear();
    collectedProxies.clear();
    writtenInstances.clear();
  }

.invoke result = gen_export_methods(true, true, "", tool, true)
${result.body}
.invoke result = gen_sql_write_statements("NBP", true, true)
${result.body}
.invoke result = gen_diagram_export_statements()
${result.body}

  class ProxyPath {
    NonRootModelElement fProxyElement;
    NonRootModelElement fLocalElement;
    public ProxyPath(NonRootModelElement p_proxyElement, NonRootModelElement p_localElement) {
      fProxyElement = p_proxyElement;
      fLocalElement = p_localElement;
    }
  }

  /**
   * @param  path content path relative to the current project
   * @return String name of the system/project if the path points outside the
   *         current system, or empty string if it does not.
   */
  protected String getReferredToSystem(IPath path) {
    String previousSegment = "";
    for (int i = 0; i < path.segmentCount(); ++i) {
      if (path.segment(i).equals("models")) {
        if (!previousSegment.isEmpty()) {
          return previousSegment;
        }
      }
      previousSegment = path.segment(i);
    }
    return previousSegment;
  }

  protected void writeCollectedProxies() throws IOException {
    for(int i = 0; i < collectedProxies.size(); i++) {
      ProxyPath proxyPath = collectedProxies.get(i);;
      NonRootModelElement element = proxyPath.fProxyElement;
      if(element instanceof DataType_c) {
    	  // skip if global
    	  GlobalElementInSystem_c geis = GlobalElementInSystem_c.getOneG_EISOnR9100(PackageableElement_c.getManyPE_PEsOnR8001((DataType_c) element));
    	  if(geis != null) {
    		  continue;
    	  }
      }
      if(proxyPath.fLocalElement.getPersistableComponent() != null) {
      m_path = proxyPath.fLocalElement.getPersistableComponent().getFullPath();
      }
      m_inst = proxyPath.fLocalElement;
      if(element != null && !writtenInstances.contains(element)) {
        	Method method;
        	try {
        	    if(!element.getModelRoot().isCompareRoot()) {
	        	    IPath rtoPath = new Path(element.getContent(m_path));
	                String rtoSystem = getReferredToSystem(rtoPath);
	                if (!rtoSystem.isEmpty()) {
	                  savedRTOSystems.add(rtoSystem);
	                }
	            }
        		method = getClass().getMethod("write_" + element.getClass().getSimpleName() + "_proxy_sql",
        				new Class[] { element.getClass() });
        		method.invoke(this, new Object[] { element });
        	} catch (SecurityException e) {
        		CorePlugin.logError("Unable to write proxy entries.", e);
        	} catch (NoSuchMethodException e) {
        		CorePlugin.logError("Unable to write proxy entries.", e);
        	} catch (IllegalArgumentException e) {
        		CorePlugin.logError("Unable to write proxy entries.", e);
        	} catch (IllegalAccessException e) {
        		CorePlugin.logError("Unable to write proxy entries.", e);
        	} catch (InvocationTargetException e) {
        		CorePlugin.logError("Unable to write proxy entries.", e);
        	}     
      }
    }
  }

}
.//
.emit to file "src/org/xtuml/bp/io/mdl/${class_name}.java"
.//
