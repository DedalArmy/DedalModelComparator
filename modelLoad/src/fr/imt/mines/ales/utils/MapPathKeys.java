package fr.imt.mines.ales.utils;

public enum MapPathKeys {
	DEDAL_FILE_PATH ("dedal_file_path"),
	DEPENDENCIES_PATH("dependencies_file_path"),
	PROJECT_PATH ("project_path"),
	PROJECT_NAME ("project_name");
	
	private String name = "";
		   
	MapPathKeys(String name){
		this.name = name;
	}
		   
	public String toString(){
		return name;
	}
}
