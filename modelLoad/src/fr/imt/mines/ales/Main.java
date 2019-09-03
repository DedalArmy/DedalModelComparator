package fr.imt.mines.ales;

import java.io.IOException;
import java.util.List;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import fr.imt.mines.ales.comparators.ProjectComparator;
import fr.imt.mines.ales.utils.CSVReader;

public class Main {

	public static void main(String[] args) {
		
		String pathToCsvFile = args[0];
		String pathToDirectoryForJsonFiles = args[1];
		
		CSVReader csvReader = new CSVReader();
		List<String[]> rows = csvReader.readCsv(pathToCsvFile);//"/home/quentin/broadleaf-versions/csvtest.csv");
		int gapVersions = 0;
		
		loadRessourcesAndCreateDiff(gapVersions, rows, pathToDirectoryForJsonFiles);
		
//		for (int i = 2; i < rows.size(); i++) {
//			ProjectComparator projectComparator = new ProjectComparator();
//			try {
//				projectComparator.loadResources(
//						rows.get(i-1)[1],
//						rows.get(i)[1],
//						pathToDirectoryForJsonFiles
//						);
//				projectComparator.createDifferences();
//
//			} catch (Exception e) {
//				gapVersions++;
//				try {
//					projectComparator.loadResources(
//							rows.get(i-1)[1],
//							rows.get(i)[1],
//							pathToDirectoryForJsonFiles
//							);
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				projectComparator.createDifferences();
//			}
//			projectComparator = null;
//			JavaParserFacade.clearInstances();
//		}
		
		
		//ProjectComparator projectComparator = new ProjectComparator();
//		String path = "/home/quentin/broadleaf-versions/broadleaf-5.0.0-RC1";
//		try {
//			projectComparator.loadResources(
//					path ,
//					"/home/quentin/broadleaf-versions/broadleaf-4.0.26-GA",
//					"/home/quentin/broadleaf-versions/"
//					);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		projectComparator.createDifferences();
	}
	
	public static void loadRessourcesAndCreateDiff(int gapVersions, List<String[]> rows, String pathToDirectoryForJsonFiles) {
		
		for (int i = 2; i+gapVersions < rows.size(); i++) {
			ProjectComparator projectComparator = new ProjectComparator();
			try {
				projectComparator.loadResources(
						rows.get(i+gapVersions-1)[1],
						rows.get(i+gapVersions)[1],
						pathToDirectoryForJsonFiles
						);
				projectComparator.createDifferences();

			} catch (Exception e) {
				gapVersions++;
				loadRessourcesAndCreateDiff(gapVersions, rows, pathToDirectoryForJsonFiles);
			}
			projectComparator = null;
			JavaParserFacade.clearInstances();
		}
	}

}