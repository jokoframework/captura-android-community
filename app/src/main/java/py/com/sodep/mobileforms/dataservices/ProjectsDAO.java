package py.com.sodep.mobileforms.dataservices;

import java.util.List;

import py.com.sodep.mf.exchange.objects.metadata.Project;

/**
 * An implementation of ProjectsDAO (Data Access Object) provides the means for
 * retrieving, listing and saving instances of Project and/or data related to
 * them
 * 
 * @author Miguel
 * 
 */
public interface ProjectsDAO {

	List<Project> listProjects(Long applicationId);

	void updateProject(Project project);
	
	void saveProject(Project project);

	void deleteProject(Long projectId);
	
	Project getProject(Long projectId);

}
