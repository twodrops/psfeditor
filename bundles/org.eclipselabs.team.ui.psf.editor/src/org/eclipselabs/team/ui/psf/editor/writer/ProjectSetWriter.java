/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.ui.psf.editor.writer;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;
import org.eclipselabs.team.ui.psf.editor.core.IProjectSet;
import org.eclipselabs.team.ui.psf.editor.core.IProvider;
import org.eclipselabs.team.ui.psf.editor.core.IWorkingSet;
import org.eclipselabs.team.ui.psf.editor.core.IWorkingSetItem;
import org.eclipselabs.team.ui.psf.editor.core.Project;
import org.eclipselabs.team.ui.psf.editor.exceptions.ProjectSetException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author Nirmal Sasidharan
 */
public class ProjectSetWriter {

  public static final ProjectSetWriter INSTANCE = new ProjectSetWriter();

  /**
   * @param projectSet
   * @param fFile
   * @param monitor
   * @throws ProjectSetException
   */
  public void save(final IProjectSet projectSet, final IFile fFile, final IProgressMonitor monitor)
      throws ProjectSetException {

    StringWriter writer = null;

    try {

      writer = new StringWriter();

      XMLMemento xmlMemento = getXMLMementoRoot();
      Set<IProvider> providers = projectSet.getProviders();
      monitor.beginTask(null, 1000 * providers.size());

      for (IProvider provider : providers) {
        String id = provider.getId();
        IMemento memento = xmlMemento.createChild("provider"); //$NON-NLS-1$
        memento.putString("id", id); //$NON-NLS-1$
        Set<Project> projects = provider.getProjects();
        for (Project project : projects) {
          IMemento proj = memento.createChild("project"); //$NON-NLS-1$
          List<String> references = project.getReferences();
          StringBuilder referencesBuffer = new StringBuilder();
          for (Iterator iterator = references.iterator(); iterator.hasNext();) {
            String reference = (String) iterator.next();
            referencesBuffer.append(reference);
            if (iterator.hasNext()) {
              referencesBuffer.append(",");
            }
          }
          proj.putString("reference", referencesBuffer.toString()); //$NON-NLS-1$
        }
      }

      Set<IWorkingSet> workingSets = projectSet.getWorkingSets();
      for (IWorkingSet workingSet : workingSets) {
        IMemento ws = xmlMemento.createChild("workingSets"); //$NON-NLS-1$

        String editPageId = workingSet.getEditPageId();
        ws.putString("editPageId", editPageId);//$NON-NLS-1$

        String id = workingSet.getId();
        ws.putString("id", id);//$NON-NLS-1$

        String label = workingSet.getLabel();
        ws.putString("label", label);//$NON-NLS-1$

        String name = workingSet.getName();
        ws.putString("name", name);//$NON-NLS-1$

        Set<IWorkingSetItem> workingSetItems = workingSet.getWorkingSetItems();
        for (IWorkingSetItem workingSetItem : workingSetItems) {
          IMemento item = ws.createChild("item"); //$NON-NLS-1$
          item.putString("factoryId", workingSetItem.getFactoryId());//$NON-NLS-1$
          Map<String, String> properties = workingSetItem.getProperties();
          for (String key : properties.keySet()) {
            item.putString(key, properties.get(key));
          }

        }
      }
      xmlMemento.save(writer);

      ByteArrayInputStream stream = new ByteArrayInputStream(writer.toString().getBytes());
      if (!fFile.exists()) {
        fFile.create(stream, false, null);
      }
      else {
        // validate edit
        if (fFile.isReadOnly()) {
          IStatus status = ResourcesPlugin.getWorkspace().validateEdit(new IFile[] { fFile }, null);
          if (!status.isOK()) {
            throw new CoreException(status);
          }
        }
        fFile.setContents(stream, true, false, null);
      }

    }

    catch (UnsupportedEncodingException e) {
      throw new ProjectSetException(e);
    }
    catch (FileNotFoundException e) {
      throw new ProjectSetException(e);
    }
    catch (IOException e) {
      throw new ProjectSetException(e);
    }
    catch (CoreException e) {
      throw new ProjectSetException(e);
    }


  }

  private XMLMemento getXMLMementoRoot() {
    Document document;
    try {
      document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element element = document.createElement("psf"); //$NON-NLS-1$
      element.setAttribute("version", "2.0"); //$NON-NLS-1$ //$NON-NLS-2$
      document.appendChild(element);
      return new XMLMemento(document, element);
    }
    catch (ParserConfigurationException e) {
      throw new Error(e.getMessage());
    }
  }


}
