package py.com.sodep.mobileforms.net.sync;

import java.util.List;

import py.com.sodep.mf.exchange.objects.metadata.Application;
import py.com.sodep.mf.exchange.objects.metadata.Form;
import py.com.sodep.mf.exchange.objects.metadata.Project;
import py.com.sodep.mobileforms.dataservices.ApplicationsDAO;
import py.com.sodep.mobileforms.dataservices.FormsDAO;
import py.com.sodep.mobileforms.dataservices.ProjectsDAO;

public class MetadataSynchronizationHelpers {

	public static void saveProjects(ProjectsDAO projectsDAO, Application app) {
		for (Project p : app.getProjects()) {
			boolean exists = projectsDAO.getProject(p.getId()) != null;
			if (exists) {
				projectsDAO.updateProject(p);
			} else {
				projectsDAO.saveProject(p);
			}
		}

		removeNotListedProjects(projectsDAO, app);
	}

	private static void removeNotListedProjects(ProjectsDAO projectsDAO, Application app) {
		List<Project> newProjects = app.getProjects();
		List<Project> existingProjects = projectsDAO.listProjects(app.getId());

		for (Project existingProject : existingProjects) {
			boolean listed = false;
			for (Project project : newProjects) {
				if (project.getId().equals(existingProject.getId())) {
					listed = true;
					break;
				}
			}
			if (!listed) {
				projectsDAO.deleteProject(existingProject.getId());
			}
		}
	}

	public static void saveApps(ApplicationsDAO appDAO, List<Application> applications) {
		for (Application app : applications) {
			boolean exists = appDAO.getApplication(app.getId()) != null;
			if (exists) {
				appDAO.updateApplication(app);
			} else {
				appDAO.saveApplication(app);
			}
		}

		removeNotListedApps(appDAO, applications);
	}

	private static void removeNotListedApps(ApplicationsDAO appDAO, List<Application> newApps) {
		List<Application> existingApps = appDAO.listApplications();
		for (Application existingApp : existingApps) {
			boolean listed = false;
			for (Application app : newApps) {
				if (app.getId().equals(existingApp.getId())) {
					listed = true;
					break;
				}
			}
			if (!listed) {
				appDAO.deleteApplication(existingApp.getId());
			}
		}
	}
	
	public static void saveForms(FormsDAO formsDAO, List<Form> forms) {
		for (Form f : forms) {
			boolean exists = formsDAO.getForm(f.getId(), f.getVersion()) != null;
			if (exists) {
				formsDAO.updateForm(f);
			} else {
				formsDAO.saveForm(f);
			}
		}

		removeNotListedForms(formsDAO, forms);
	}

	private static void removeNotListedForms(FormsDAO formsDAO, List<Form> newForms) {
		List<Form> existingForms = formsDAO.listAllForms();

		for (Form existingForm : existingForms) {
			boolean listed = false;
			for (Form form : newForms) {
				if (form.getId().equals(existingForm.getId())
				/* && form.getVersion().equals(existingForm.getVersion()) */) {
					// Keep the current version & old versions also
					listed = true;
					break;
				}
			}
			if (!listed) {
				// If there's no version of the form in the synced list of forms
				// delete them
				formsDAO.deleteForm(existingForm.getId(), existingForm.getVersion());
			}
		}
	}

}
