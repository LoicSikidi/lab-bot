package test.hello.suribot.analyze.jsonmemory;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import hello.suribot.analyze.jsonmemory.JSONMemory;

public class JSONMemoryTests {
	
	private static String testUser = "testUser";
	
	////////////////// GETTERS /////////////////////////
	@Test
	public void getIdContrat(){
		assertEquals(null, JSONMemory.getIdContrat(testUser));
		
		JSONMemory.putIdContrat(testUser, "7613");
		assertEquals("7613", JSONMemory.getIdContrat(testUser));
		JSONMemory.deleteFile(testUser);
	}
	
	@Test
	public void getPrenom(){
		JSONMemory.deleteFile(testUser);
		assertEquals(null, JSONMemory.getPrenom(testUser));
		
		JSONMemory.putPrenom(testUser, "testPrenom");
		assertEquals("testPrenom", JSONMemory.getPrenom(testUser));
		JSONMemory.deleteFile(testUser);
	}
	
	@Test
	public void getNom(){
		JSONMemory.deleteFile(testUser);
		assertEquals(null, JSONMemory.getNom(testUser));
		
		JSONMemory.putNom(testUser, "testNom");
		assertEquals("testNom", JSONMemory.getNom(testUser));
		JSONMemory.deleteFile(testUser);
	}
	
	@Test
	public void getIntents(){
		JSONMemory.deleteFile(testUser);
		assertEquals(null, JSONMemory.getLastEntities(testUser));
		
		JSONMemory.putLastEntities(testUser, "{\"testUser\":\"felix\",\"CONTEXTE\":\"demande\",\"IDENTIFICATION\":\"ID-5935697\"}");
		assertEquals("{\"testUser\":\"felix\",\"CONTEXTE\":\"demande\",\"IDENTIFICATION\":\"ID-5935697\"}", JSONMemory.getLastEntities(testUser));
		JSONMemory.deleteFile(testUser);
	}
	
	
	///////////////// SETTERS  ///////////////////////////
	
	///////////////// REMOVE  ///////////////////////////
	
	
	
	@Before
	public void cleanFile(){
		JSONMemory.deleteFile(testUser);
	}
	
	@AfterClass
	public static void cleanFileAfter(){
		JSONMemory.deleteFile(testUser);
	}
}
