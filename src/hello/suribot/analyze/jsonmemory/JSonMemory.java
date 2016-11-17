package hello.suribot.analyze.jsonmemory;

import java.io.FileInputStream;
import java.io.FileReader;
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

public class JSonMemory {
	
	static String EXTENSION_FILE = ".json";
	static JSONParser parser = new JSONParser();
	
	////////////////// GETTERS /////////////////////////
	public static String getIdContrat(String idUser){
		return getValueFromJson(idUser+EXTENSION_FILE, "contract");
	}
	
	public static String getPrenom(String idUser){
		return getValueFromJson(idUser+EXTENSION_FILE, "firstname");
	}
	
	public static String getNom(String idUser){
		return getValueFromJson(idUser+EXTENSION_FILE, "lastname");
	}
	
	public static String getLastIntents(String idUser) {
		return getValueFromJson(idUser+EXTENSION_FILE, "intents");
	}
	
	///////////////// SETTERS  ///////////////////////////
	
	public static void putNom(String idUser, String nom){
		putValueInJson(idUser, "nom", nom);
	}
	
	public static void putPrenom(String idUser, String prenom){
		putValueInJson(idUser, "prenom", prenom);
	}
	
	public static void putLastIntents(String idUser, String intents){
		putValueInJson(idUser, "intents", intents);
	}
	
	public static void putIdContrat(String idUser, String idContrat){
		putValueInJson(idUser, "contrat", idContrat);
	}
	
	///////////////// REMOVE  ///////////////////////////
	
	public static void removeLastIntents(String idUser){
		removeKeyInJson(idUser, "intents");
	}
	
	//////////////// UTILS //////////////
	private static String getValueFromJson(String fileName, String name){
		try {
			Object object = parser.parse(new FileReader(fileName));
			JSONObject json = (JSONObject) object;
			return (String) json.get(name);	
		} catch (IOException | JSONException | ParseException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void putValuesInJson(String idUser, Map<String, String> values){
		if(idUser==null || idUser.isEmpty() || values==null || values.isEmpty()) return;
		
		String fileName = idUser + EXTENSION_FILE;
		createJsonFileIfNotExists(idUser);
		
		Object object;
		JSONObject json=null;
		try {
			object = parser.parse(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

			json = (JSONObject) object;
			for(String key : values.keySet()){
				json.put((String) key, (String) values.get(key));
			}
		} catch (IOException | ParseException e2) { // empty file
			try (FileWriter file = new FileWriter(fileName)){
				JSONObject jsonObject = new JSONObject();
				
				for(String key : values.keySet()){
					jsonObject.put(key, values.get(key));
					System.out.println(values.get(key));
				}
				file.write(jsonObject.toString());
			} catch (IOException e1) {
				// impossible d'écrire dans le fichier qui était vide
				e1.printStackTrace();
			}
		}
		
		// cas où fichier non vide et contient déjà du JSON
		try (FileWriter file = new FileWriter(fileName)){
			file.write(json.toJSONString());
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
			if(!Files.exists(Paths.get(idUser+EXTENSION_FILE))){
				Files.createFile(Paths.get(idUser+EXTENSION_FILE));
			}
		} catch (Exception e){
			System.out.println("Cannot create "+idUser+EXTENSION_FILE+" file");
		}
	}
	
}
