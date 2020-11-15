package py.com.sodep.mobileforms.dataservices;

import java.util.List;

import py.com.sodep.mf.exchange.objects.metadata.Application;

public interface ApplicationsDAO {

	List<Application> listApplications();

	Application getApplication(Long appId);

	void updateApplication(Application application);

	void saveApplication(Application application);

	void deleteApplication(Long applicationId);

	void deleteAllData();

	void setDeviceVerified(Long applicationId, boolean verified);

	boolean isDeviceVerified(Long applicationId);
}
