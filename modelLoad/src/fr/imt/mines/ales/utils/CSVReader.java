package fr.imt.mines.ales.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	private BufferedReader csvReader;

	public CSVReader() {
	}
	
	public List<String[]> readCsv(String csvFilePath) {
		List<String[]> listRowSplitted = new ArrayList<String[]>();
		
		try {
			String row;
			csvReader = new BufferedReader(new FileReader(csvFilePath));
			
			while ((row = csvReader.readLine()) != null) {
			    String[] data = row.split(",");
			    listRowSplitted.add(data);
			}
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listRowSplitted;
	}

//	public List<String[]> readCsvValues(String csvFilePath, String... headers) {
//		List<String[]> listRowSplitted = new ArrayList<String[]>();
//		
//		int[] indexHeaders = new int[headers.length];
//		int nbLine = 0;
//		try {
//			String row;
//			csvReader = new BufferedReader(new FileReader(csvFilePath));
//			
//			
//			while ((row = csvReader.readLine()) != null) {
//			    String[] data = row.split(",");
//			    
//			    if(nbLine <= 0) {
//			    	for (int i = 0; i < data.length; i++) {
//						
//					}
//			    }
//			    
//			    listRowSplitted.add(data);
//			    nbLine++;
//			}
//			csvReader.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return listRowSplitted;
//	}

}
