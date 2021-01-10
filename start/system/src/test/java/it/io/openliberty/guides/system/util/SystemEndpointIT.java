package it.io.openliberty.guides.system.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SystemEndpointIT {

	protected static String authHeadAdmin;
	protected static String authHeadUser;
	protected static String baseURL;
	protected static String urlOS;
	protected static String urlUsername;
	protected static String urlRoles;
	
	@BeforeAll
	public static void setup() throws Exception {
		
		baseURL = StringUtils.join(
				"http://",
				System.getProperty("hostname"),
				":",
				System.getProperty("http.port"),
				"/system/properties" );
		
		urlOS = StringUtils.join( baseURL , "/os" );
		urlUsername = StringUtils.join( baseURL , "/username" );
		urlRoles = StringUtils.join( baseURL , "jwtroles" );
		
		authHeadAdmin = "Bearer " + new JwtBuilder().createAdminJwt("testUser");
		authHeadUser = "Bearer " + new JwtBuilder().createUserJwt("testUser");
		
	}
	
	@Test
	public void testOSEndpoint() {
		
		Response response = makeRequest( urlOS , authHeadAdmin );
		
		assertEquals( 
				200 , 
				response.getStatus() , 
				StringUtils.join( "Incorrect response code from " , urlOS )  );
		
		assertEquals( 
				System.getProperty("os.name") , 
				response.readEntity(String.class) , 
				"The system property for the local and remote JVM should match" );
		
		response = makeRequest( urlOS , authHeadUser );
		
		assertEquals( 
				403 , 
				response.getStatus() , 
				StringUtils.join( "Incorrect response code from" , urlOS ));

		response = makeRequest( urlOS , null );
				
		assertEquals( 
				401 , 
				response.getStatus() , 
				StringUtils.join( "Incorrect response code from" , urlOS ));
		
		response.close();
		
	}
	
	@Test
	public void testUsernameEndpoint() {
		
		Response response = makeRequest( urlUsername , authHeadUser );
		
		assertEquals( 
				200 , 
				response.getStatus() , 
				StringUtils.join( "Incorrect response code from " , urlUsername ) );
		
		response = makeResquest( urlUsername , authHeadUser );
		
		assertEquals( 
				200 , 
				response.getStatus() , 
				StringUtils.join( "Incorrect response code from " , urlUsername ) );


		response = makeResquest( urlUsername , null );
		
		assertEquals( 
				401 , 
				response.getStatus() , 
				StringUtils.join( "Incorrect response code from " , urlUsername ) );
		
		
	}
	
}
