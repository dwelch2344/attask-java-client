package com.mcubedlabs.libs.attask;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.mcubedlabs.libs.attask.model.Project;
import com.mcubedlabs.libs.attask.model.Task;

/**
 * A simple test to verify we're reading from the API correctly. Expects to have a file
 * named 'attask-credentials.properties' in the same folder.
 * 
 * @author dwelch2344
 *
 */
public class AtTaskClientTest {

	private AtTaskClient client;

	@Before
	public void setUp() throws Exception {
		Properties props = new Properties();
		props.load( getClass().getResourceAsStream("attask-credentials.properties") );
		client = new AtTaskClient(props.getProperty("host"), props.getProperty("user"), props.getProperty("pass"));
	}
	
	@Test
	public void test() {
		List<Project> projects = client.getProjects();
		
		for(Project project : projects){
			List<Task> tasks = client.getTasksByProject(project.getId());
			System.out.println(project.getName() + " has " + tasks.size() + " tasks");
		}
	}

}
