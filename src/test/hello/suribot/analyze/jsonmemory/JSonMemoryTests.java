package test.hello.suribot.analyze.jsonmemory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hello.suribot.analyze.jsonmemory.JSonMemory;

public class JSonMemoryTests {
	
	////////////////// GETTERS /////////////////////////
	@Test
	public void getIdContrat(){
		String testUser = "testUser";
		JSonMemory.deleteFile(testUser);
		assertEquals(null, JSonMemory.getIdContrat(testUser));
		
		JSonMemory.putIdContrat(testUser, "7613");
		assertEquals("7613", JSonMemory.getIdContrat(testUser));
		JSonMemory.deleteFile(testUser);
	}
	
	@Test
	public void getPrenom(){
		String testUser = "testUser";
		JSonMemory.deleteFile(testUser);
		assertEquals(null, JSonMemory.getPrenom(testUser));
		
		JSonMemory.putPrenom(testUser, "testPrenom");
		assertEquals("testPrenom", JSonMemory.getPrenom(testUser));
		JSonMemory.deleteFile(testUser);
	}
	
	@Test
	public void getNom(){
		String testUser = "testUser";
		JSonMemory.deleteFile(testUser);
		assertEquals(null, JSonMemory.getNom(testUser));
		
		JSonMemory.putNom(testUser, "testNom");
		assertEquals("testNom", JSonMemory.getNom(testUser));
		JSonMemory.deleteFile(testUser);
	}
	
	@Test
	public void getIntents(){
		String testUser = "testUser";
		JSonMemory.deleteFile(testUser);
		assertEquals(null, JSonMemory.getLastIntents(testUser));
		
		JSonMemory.putLastIntents(testUser, "{\"testUser\":\"felix\",\"CONTEXTE\":\"demande\",\"IDENTIFICATION\":\"ID-5935697\"}");
		assertEquals("{\"testUser\":\"felix\",\"CONTEXTE\":\"demande\",\"IDENTIFICATION\":\"ID-5935697\"}", JSonMemory.getLastIntents(testUser));
		JSonMemory.deleteFile(testUser);
	}
	
	///////////////// SETTERS  ///////////////////////////
	
	///////////////// REMOVE  ///////////////////////////
	
}
