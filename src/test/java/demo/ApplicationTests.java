package demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.cisco.touchworksConnector.proxyapp.AllscriptsConnector;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = AllscriptsConnector.class)
//@AutoConfigureMockMvc
//@TestPropertySource(locations = {"classpath:application.properties","classpath:apiconfig.properties"})
public class ApplicationTests {

	static Logger log = Logger.getLogger(ApplicationTests.class);
	
	@LocalServerPort
	private int port;
	
	@Autowired
    private AllscriptsConnector ct;

	//@Autowired
    //private MockMvc mvc;

	private TestRestTemplate template = new TestRestTemplate();
/*
	@Test
	public void twTest() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/tw/GetSchedule?Parameter1=01/01/2017|01/01/2018", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		log.info(response);
	}
*/
	
	@Test
	public void testTW() {
		
		List<String> twActions = ct.getActions(ct.getTwActionMap());
		//List<String> pmActions = ct.getActions(ct.getPmActionMap());
		
		for(String s: twActions) {
			ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/tw/" + s, String.class);
			//assertEquals(HttpStatus.OK, response.getStatusCode());
			log.info(response);
		}
	}

	/*
	@Test
	public void testPM() {
		
		//List<String> twActions = ct.getActions(ct.getTwActionMap());
		List<String> pmActions = ct.getActions(ct.getPmActionMap());
		
		for(String s: pmActions) {
			ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/pm/" + s, String.class);
			//assertEquals(HttpStatus.OK, response.getStatusCode());
			log.info(response);
		}
	}
	*/
	/*
	mvc.perform(get("/getSchedule")
		      .contentType(MediaType.APPLICATION_JSON))
		      .andExpect(status().isOk())
		      .andExpect(content()
		      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		     
		      
	 */
}
