package fr.imt.mines.ales.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {
	private static String password;
	private static final String PATH_DEDAL_FILE = "generated/globalDeployment.sdsl_genDedalDiag.dedal";
	private static final String PATH_DEPEDENCIES = ".m2";

	
	public static Map<MapPathKeys, String> createMapPaths(String projectPath) {
		
		if(projectPath.lastIndexOf("/") != projectPath.length() - 1) {
			projectPath += "/";
		}
		
		String pathDedalFile = projectPath + PATH_DEDAL_FILE;
		String pathDepenciesString = projectPath + PATH_DEPEDENCIES;
		String projectNameString = projectPath.split("/")[projectPath.split("/").length-1];
		
		Map<MapPathKeys, String> mapPtahs = new HashMap<>();
		mapPtahs.put(MapPathKeys.DEDAL_FILE_PATH, pathDedalFile);
		mapPtahs.put(MapPathKeys.DEPENDENCIES_PATH, pathDepenciesString);
		mapPtahs.put(MapPathKeys.PROJECT_PATH, projectPath);
		mapPtahs.put(MapPathKeys.PROJECT_NAME, projectNameString);

		return mapPtahs;
	}
	
	public static List<File> listFileInDirectory(String directoryPath) {
		File f = null;
		List<File> paths = new ArrayList<File>() ;
		try {

			// create new file
			//f = new File("/home/quentin/Documents/These/experiences/ADL/versionning/adl_files");
			f = new File(directoryPath);
			
			// returns pathnames for files and directory
			paths = Arrays.asList(f.listFiles());

		} catch (Exception e) {
			// if any error occurs
			e.printStackTrace();
		}
		return paths;
	}
	
	public static void copyFilesFromServer(String filePathServer, String filePathDest) {
		readUsernamePassword();
		Process process;
		try {
			Runtime rt = Runtime.getRuntime();
			System.out.println("Launch script shell for scp : source=" + filePathServer + " dest=" + filePathDest);
			process = rt.exec("sh /home/quentin/eclipse-workspace-dedal/modelLoad/scp_script.sh " + password + " " + filePathServer + " " + filePathDest);
			int returnCode = process.waitFor();
			System.out.println(returnCode);
			if(returnCode != 0) {
				System.out.println("Scp OK !");
			}else {
				System.out.println("Error during scp");
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		password = null;
	}
	
	private static void readUsernamePassword() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/quentin/password.txt"));
			password = bufferedReader.readLine();
			
			bufferedReader.close();
			bufferedReader = null;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void cleanDirectory(String directoryPath) {
		File f = new File(directoryPath);
		File[] files = f.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File file: files) {
	            if(file.isDirectory()) {
	            	cleanDirectory(file.getAbsolutePath());
	            } else {
	                file.delete();
	            }
	        }
	    }
	    f.delete();
	    System.out.println("Clean directory " + directoryPath + " done !");
	}
}
