package hello.suribot.analyze.jsonmemory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Classe de mémorisation des données des utilisateurs (similaire à une BDD), sous forme de fichier ".json".
 */
public class JSONMemory {

	private static final String DIR = System.getProperty("user.dir")+File.separator+"JsonMemory"+File.separator;
	private static final String EXTENSION_FILE = ".json";
	private static JSONParser parser = new JSONParser();

	public static final String CONTRACT = "contract";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String ENTITIES = "entities";
	public static final String CONTEXTE = "contexte";
	public static final String LANGUAGE = "language";

	////////////////// GETTERS /////////////////////////
	public static String getIdContrat(String idUser){
		return getValueFromJson(DIR+idUser+EXTENSION_FILE, CONTRACT);
	}

	public static String getPrenom(String idUser){
		return getValueFromJson(DIR+idUser+EXTENSION_FILE, FIRSTNAME);
	}

	public static String getNom(String idUser){
		return getValueFromJson(DIR+idUser+EXTENSION_FILE, LASTNAME);
	}

	public static String getLastEntities(String idUser) {
		return getValueFromJson(DIR+idUser+EXTENSION_FILE, ENTITIES);
	}
	
	public static String getLanguage(String idUser) {
		return getValueFromJson(DIR+idUser+EXTENSION_FILE, LANGUAGE);
	}

	///////////////// SETTERS  ///////////////////////////

	public static void putNom(String idUser, String nom){
		putValueInJson(idUser, LASTNAME, nom);
	}

	public static void putPrenom(String idUser, String prenom){
		putValueInJson(idUser, FIRSTNAME, prenom);
	}

	public static void putLastEntities(String idUser, String entities){
		putValueInJson(idUser, ENTITIES, entities);
	}

	public static void putContext(String idUser, String contexte){
		putValueInJson(idUser, CONTEXTE, contexte);
	}

	public static void putIdContrat(String idUser, String idContrat){
		putValueInJson(idUser, CONTRACT, idContrat);
	}
	
	public static void putLanguage(String idUser, String language){
		putValueInJson(idUser, CONTRACT, language);
	}

	///////////////// REMOVE  ///////////////////////////

	public static void removeLastEntities(String idUser){
		removeKeyInJson(idUser, ENTITIES);
	}

	public static void removeLastContext(String idUser){
		removeKeyInJson(idUser, CONTEXTE);
	}

	public static void deleteFile(String idUser){
		deleteJsonFileIfExists(idUser);
	}

	public static void cleanFile(String idUser){
		cleanJsonFileIfExists(idUser);
	}

	//////////////// UTILS //////////////
	private static String getValueFromJson(String fileName, String name){

		FileInputStream fileInput = null;
		InputStreamReader isr = null;
		try {
			fileInput = new FileInputStream(fileName);
			isr = new InputStreamReader(fileInput, "UTF-8");
			Object object = parser.parse(isr);
			JSONObject json = (JSONObject) object;
			return (String) json.get(name);	
		} catch (IOException | JSONException | ParseException e) {
			return null;
		}finally{
			try {
				if(isr!=null)isr.close();
				if(fileInput!=null)fileInput.close();
			} catch (IOException e) {
				return null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void putValuesInJson(String idUser, Map<String, String> values){
		if(idUser==null || idUser.isEmpty() || values==null || values.isEmpty()) return;

		String fileName = DIR + idUser + EXTENSION_FILE;
		createJsonFileIfNotExists(idUser);

		Object object;
		JSONObject json=null;
		FileInputStream fileInput = null;
		InputStreamReader isr = null;
		try {
			fileInput = new FileInputStream(fileName);
			isr = new InputStreamReader(fileInput, "UTF-8");
			object = parser.parse(isr);

			json = (JSONObject) object;
			for(String key : values.keySet()){
				if(values.get(key)==null){
					if(json.containsKey(key)) json.remove(key);
				} else {
					json.put((String) key, (String) values.get(key));
				}
			}
		} catch (IOException | ParseException e2) { // empty file
			try (FileWriter file = new FileWriter(fileName)){
				json = new JSONObject();

				for(String key : values.keySet()){
					if(values.get(key)==null){
						if(json.containsKey(key)) json.remove(key);
					} else {
						json.put((String) key, (String) values.get(key));
					}
				}
				if(json.isEmpty()) file.write("");
				else file.write(json.toJSONString());
				file.flush();
				file.close();
				return;
			} catch (IOException e1) {
				// impossible d'écrire dans le fichier qui était vide
				e1.printStackTrace();
			}
		}finally{
			try {
				if(isr!=null)isr.close();
				if(fileInput!=null)fileInput.close();
			} catch (IOException e) {}
		}

		// cas où fichier non vide et contient déjà du JSON
		FileWriter file = null;
		try{
			file = new FileWriter(fileName);
			file.write(json.toJSONString());
			file.flush();
			file.close();
		} catch (IOException | JSONException e) {
			// ecriture du fichier existant NOK
		}
	}

	private static void putValueInJson(String idUser, String key, String value){
		if(idUser==null || idUser.isEmpty() || key==null || key.isEmpty() 
				|| value==null || value.isEmpty()) return;
		Map<String, String> map = new HashMap<>(1);
		map.put(key, value);
		putValuesInJson(idUser, map);
	}

	private static void removeKeyInJson(String idUser, String key){
		if(idUser==null || idUser.isEmpty() || key==null || key.isEmpty()) return;
		Map<String, String> map = new HashMap<>(1);
		map.put(key, null);
		putValuesInJson(idUser, map);
	}

	private static void createJsonFileIfNotExists(String idUser) {
		try{
			if (!Files.exists(Paths.get(DIR))) { // Création du dossier des fichiers json pour le tout premier utilisateur
				new File(DIR).mkdir();
			}
			if(!Files.exists(Paths.get(DIR+idUser+EXTENSION_FILE))){
				Files.createFile(Paths.get(DIR+idUser+EXTENSION_FILE));
			}
		} catch (Exception e){
			// TODO: Log System ?
			System.out.println("Cannot create "+idUser+EXTENSION_FILE+" file");
			e.printStackTrace();
		}
	}

	private static boolean deleteJsonFileIfExists(String idUser) {
		try{
			return Files.deleteIfExists(Paths.get(DIR+idUser+EXTENSION_FILE));
		} catch (Exception e){
			// on le vide si impossible à supprimer (https://github.com/fflewddur/archivo/issues/95)
			cleanJsonFileIfExists(idUser);
		}
		return false;
	}

	private static void cleanJsonFileIfExists(String idUser) {
		Map<String, String> nothing = new HashMap<>(4);
		nothing.put(CONTRACT, null);
		nothing.put(FIRSTNAME, null);
		nothing.put(LASTNAME, null);
		nothing.put(ENTITIES, null);
		nothing.put(LANGUAGE, null);
		putValuesInJson(idUser, nothing);
	}

}
