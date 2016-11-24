package hello.suribot.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IJsonDecoder {
	
	default String getString(){
		return null;
	}
	
	default String getInteger(){
		return null;
	}
	
	default JSONArray getJSonArray(){
		return null;
	}
	
	default JSONObject getJSonObject(){
		return null;
	}
	
}
