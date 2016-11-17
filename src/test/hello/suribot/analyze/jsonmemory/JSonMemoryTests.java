package test.hello.suribot.analyze.jsonmemory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hello.suribot.analyze.jsonmemory.JSonMemory;

public class JSonMemoryTests {
	
	////////////////// GETTERS /////////////////////////
	@Test
	public void getIdContrat(){
		String iduser = "iduser";
		JSonMemory.deleteFile(iduser);
		assertEquals(null, JSonMemory.getIdContrat(iduser));
		
		JSonMemory.putIdContrat(iduser, "7613");
		assertEquals("7613", JSonMemory.getIdContrat(iduser));
		JSonMemory.deleteFile(iduser);
	}
	
	@Test
	public void getPrenom(){
		String iduser = "iduser";
		JSonMemory.deleteFile(iduser);
		assertEquals(null, JSonMemory.getPrenom(iduser));
		
		JSonMemory.putPrenom(iduser, "testPrenom");
		assertEquals("testPrenom", JSonMemory.getPrenom(iduser));
		JSonMemory.deleteFile(iduser);
	}
	
	@Test
	public void getNom(){
		String iduser = "iduser";
		JSonMemory.deleteFile(iduser);
		assertEquals(null, JSonMemory.getNom(iduser));
		
		JSonMemory.putNom(iduser, "testNom");
		assertEquals("testNom", JSonMemory.getNom(iduser));
		JSonMemory.deleteFile(iduser);
	}
	
	@Test
	public void getIntents(){
		String iduser = "iduser";
		JSonMemory.deleteFile(iduser);
		assertEquals(null, JSonMemory.getLastIntents(iduser));
		
		JSonMemory.putLastIntents(iduser, "{\"IDUSER\":\"felix\",\"CONTEXTE\":\"demande\",\"IDENTIFICATION\":\"ID-5935697\"}");
		assertEquals("{\"IDUSER\":\"felix\",\"CONTEXTE\":\"demande\",\"IDENTIFICATION\":\"ID-5935697\"}", JSonMemory.getLastIntents(iduser));
		JSonMemory.deleteFile(iduser);
	}
	
	///////////////// SETTERS  ///////////////////////////
	
	///////////////// REMOVE  ///////////////////////////
	
}
