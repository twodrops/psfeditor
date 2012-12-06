/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.ui.psf.editor.reader;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.team.core.ProjectSetCapability;
import org.eclipse.team.core.ProjectSetSerializationContext;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.RepositoryProviderType;
import org.eclipse.team.core.TeamException;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.XMLMemento;
import org.eclipselabs.team.ui.psf.editor.change.service.ContentChangedProvider;
import org.eclipselabs.team.ui.psf.editor.change.service.ContentDelta;
import org.eclipselabs.team.ui.psf.editor.change.service.IContentChangedProvider;
import org.eclipselabs.team.ui.psf.editor.change.service.IContentDelta;
import org.eclipselabs.team.ui.psf.editor.core.IProjectSet;
import org.eclipselabs.team.ui.psf.editor.core.IProvider;
import org.eclipselabs.team.ui.psf.editor.core.ProjectSetFactory;
import org.eclipselabs.team.ui.psf.editor.exceptions.ProjectSetException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author Nirmal Sasidharan
 */
public class ProjectSetHandler extends ContentChangedProvider implements IContentChangedProvider {

  /**
   * 
   */
  public static final ProjectSetHandler INSTANCE = new ProjectSetHandler();


  private static int counter;


  /**
   * @param projectSet
   * @param projects
   * @throws ProjectSetException
   * @throws TeamException
   */
  public void createProjects(final IProjectSet projectSet, final IProject[] projects) throws ProjectSetException {

    Set<org.eclipselabs.team.ui.psf.editor.core.IProject> projectsAdded =
        new HashSet<org.eclipselabs.team.ui.psf.editor.core.IProject>();
    Map<String, Set<IProject>> projectsHash = hashProjectsByProvider(projects);

    ProjectSetSerializationContext context = new ProjectSetSerializationContext();

    Iterator<String> it = projectsHash.keySet().iterator();

    while (it.hasNext()) {

      String id = it.next();

      IProvider provider = ProjectSetFactory.INSTANCE.createProvider(projectSet, id);

      Set<IProject> projectsList = projectsHash.get(id);
      IProject[] projectArray = projectsList.toArray(new IProject[projectsList.size()]);
      RepositoryProviderType providerType = RepositoryProviderType.getProviderType(id);
      ProjectSetCapability projectSetCapability = providerType.getProjectSetCapability();
      ProjectSetCapability.ensureBackwardsCompatible(providerType, projectSetCapability);

      if (projectSetCapability != null) {
        String[] references;
        try {
          references = projectSetCapability.asReference(projectArray, context, new NullProgressMonitor());
          for (String reference : references) {
            org.eclipselabs.team.ui.psf.editor.core.IProject newProject = ProjectSetFactory.INSTANCE.createProject(provider);
            projectsAdded.add(newProject);
            newProject.getReferences().addAll(Arrays.asList(reference.split(",")));
          }
        }

        catch (TeamException e) {
          throw new ProjectSetException(e);
        }
      }
    }

    if (projectsAdded.size() > 0) {
      setContentDelta(new ContentDelta(projectsAdded, IContentDelta.ADDED));
    }

  }

  /**
   * @param projectSet
   * @param projects
   * @param projectsAdded
   */
  public void createWorkingSets(final IProjectSet projectSet, final IProject[] projects) {


    for (IProject project : projects) {

      Set<IWorkingSet> matchingWorkingSets = getWorkingSets(project);

      Map<String, String> properties = null;

      for (IWorkingSet matchingWorkingSet : matchingWorkingSets) {

        org.eclipselabs.team.ui.psf.editor.core.IWorkingSet workingSet =
            ProjectSetFactory.INSTANCE.createWorkingSet(projectSet, getUniqueId(), matchingWorkingSet.getId(),
                matchingWorkingSet.getLabel(), matchingWorkingSet.getName());
        IPersistableElement persistable = (IPersistableElement) project.getAdapter(IPersistableElement.class);
        if (persistable != null) {
          if (properties == null) {
            properties = getWorkingSetItemProperties(persistable);
          }
          ProjectSetFactory.INSTANCE.createWorkingSetItem(workingSet, persistable.getFactoryId(), properties);
        }
      }
    }
  }

  /**
   * @param persistable
   * @return
   */
  private Map<String, String> getWorkingSetItemProperties(final IPersistableElement persistable) {
    Map<String, String> properties;
    IMemento memento = getXMLMementoRoot();
    persistable.saveState(memento);
    String[] attributeKeys = memento.getAttributeKeys();
    properties = new HashMap<String, String>();
    for (String attributeKey : attributeKeys) {
      String attributeValue = memento.getString(attributeKey);
      properties.put(attributeKey, attributeValue);
    }
    return properties;
  }

  private IMemento getXMLMementoRoot() {
    Document document;
    try {
      document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element element = document.createElement("psf"); //$NON-NLS-1$
      document.appendChild(element);
      XMLMemento xmlMemento = new XMLMemento(document, element);
      return xmlMemento.createChild("item");//$NON-NLS-1$
    }
    catch (ParserConfigurationException e) {
      throw new Error(e.getMessage());
    }
  }


  protected Set<IWorkingSet> getWorkingSets(final IProject project) {

    IWorkingSet[] platformWorkingSets = PlatformUI.getWorkbench().getWorkingSetManager().getWorkingSets();

    Set<IWorkingSet> workingSets = new HashSet<IWorkingSet>();
    IProject platformProject = null;
    for (IWorkingSet platformWorkingSet : platformWorkingSets) {
      IAdaptable[] elements = platformWorkingSet.getElements();
      for (IAdaptable element : elements) {
        platformProject = (IProject) element.getAdapter(IProject.class);
        if (platformProject != null) {
          if (project.equals(platformProject)) {
            workingSets.add(platformWorkingSet);
          }
        }
      }
    }
    return workingSets;
  }

  /**
   * @param projects
   * @return
   * @throws ProjectSetException
   */
  protected Map<String, Set<IProject>> hashProjectsByProvider(final IProject[] projects) throws ProjectSetException {
    Map<String, Set<IProject>> projectsHash = new HashMap<String, Set<IProject>>();
    for (IProject project : projects) {
      RepositoryProvider provider = RepositoryProvider.getProvider(project);
      if (provider != null) {

        String id = provider.getID();
        Set<IProject> projectsList = projectsHash.get(id);

        if (projectsList == null) {
          projectsList = new TreeSet<IProject>(new Comparator<IProject>() {

            @Override
            public int compare(final IProject o1, final IProject o2) {
              return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
          });
          projectsHash.put(id, projectsList);
        }

        projectsList.add(project);
      }
      else {
        throw new ProjectSetException("Project '" + project.getName() +
            "' is not part of any repository. Cannot add this to ProjectSet");
      }
    }

    return projectsHash;
  }

  /**
   * @param projectSet
   * @param projects
   */
  public void removeProjects(final IProjectSet projectSet,
      final Set<org.eclipselabs.team.ui.psf.editor.core.IProject> projects) {
    for (IProvider provider : projectSet.getProviders()) {
      provider.getProjects().removeAll(projects);
    }
    setContentDelta(new ContentDelta(projects, IContentDelta.REMOVED));
  }

  protected String getUniqueId() {
    return Long.toString(System.currentTimeMillis()) + "_" + counter++; //$NON-NLS-1$

  }

}