package fr.imt.mines.ales.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonWriterSingleton {

	private JsonWriterSingleton() {}
	
	private static JsonWriterSingleton INSTANCE;
	private JSONObject jsonObjectListDiffs;
	private JSONArray jsonArrayListDiffs;
	
	public static JsonWriterSingleton getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new JsonWriterSingleton();
		}
		return INSTANCE;
	}
	
	public void setJsonObject(Map<MapPathKeys, String> mapPathsOldProject, Map<MapPathKeys, String> mapPathsNewProject) {
		jsonArrayListDiffs = new JSONArray();
		jsonObjectListDiffs = new JSONObject();
		jsonObjectListDiffs.put("pathOldVersion", mapPathsOldProject.get(MapPathKeys.PROJECT_PATH));
		jsonObjectListDiffs.put("pathNewVersion", mapPathsNewProject.get(MapPathKeys.PROJECT_PATH));
		jsonObjectListDiffs.put("nameOldVersion", mapPathsOldProject.get(MapPathKeys.PROJECT_NAME));
		jsonObjectListDiffs.put("nameNewVersion", mapPathsNewProject.get(MapPathKeys.PROJECT_NAME));
	}
	
	public void addDiffToJsonArray(JSONObject jsonObject) {
		jsonArrayListDiffs.put(jsonObject);
	}

	public void writeJson(String pathToJsonFile) {
		jsonObjectListDiffs.put("diffs", jsonArrayListDiffs);
		try (FileWriter file = new FileWriter(pathToJsonFile)) {
 
            file.write(jsonObjectListDiffs.toString(2));
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
