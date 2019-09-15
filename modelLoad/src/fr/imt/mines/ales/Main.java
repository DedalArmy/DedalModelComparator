package fr.imt.mines.ales;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.google.common.io.Files;

import fr.imt.mines.ales.comparators.ProjectComparator;
import fr.imt.mines.ales.structure.manager.DiffManager;
import fr.imt.mines.ales.utils.CSVReader;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String pathToCsvFile = args[0];
		String pathToDirectoryForJsonFiles = args[1];
		
		CSVReader csvReader = new CSVReader();
		List<String[]> rows = csvReader.readCsv(pathToCsvFile);//"/home/quentin/broadleaf-versions/csvtest.csv");
		//List<String[]> rows = csvReader.readCsv("/home/quentin/broadleaf-versions/versions_adl_2.csv");
		int gapVersions = 0;
		boolean error = false;
		loadRessourcesAndCreateDiff(gapVersions, rows, pathToDirectoryForJsonFiles);
				
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
	
	public static void loadRessourcesAndCreateDiff(int gapVersions, List<String[]> rows, String pathToDirectoryForJsonFiles) throws IOException {
		
		for (int i = 2; i < rows.size(); i++) {
			ProjectComparator projectComparator = new ProjectComparator();
			try {
				projectComparator.loadResources(
						rows.get(i-1)[1],
						rows.get(i)[1],
						pathToDirectoryForJsonFiles
						);
				projectComparator.createDifferences();

				DiffManager dm = DiffManager.getInstance();
				dm.init(projectComparator);
				System.out.println("Sorting diffs OK!");
				String export = dm.export(rows.get(i-1)[0], rows.get(i)[0]);
				Path path = Paths.get(pathToDirectoryForJsonFiles, rows.get(i-1)[0]+"---"+rows.get(i)[0]+".csv");
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));) {
					writer.write(export);
				}

			} catch (Exception e) {
				gapVersions++;
				try {
					projectComparator.loadResources(
							rows.get(i-1)[1],
							rows.get(i)[1],
							pathToDirectoryForJsonFiles
							);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				projectComparator.createDifferences();

				DiffManager dm = DiffManager.getInstance();
				dm.init(projectComparator);
				System.out.println("Sorting diffs OK!");
				String export = dm.export(rows.get(i-1)[0], rows.get(i)[0]);
				Path path = Paths.get(pathToDirectoryForJsonFiles, rows.get(i-1)[0]+"---"+rows.get(i)[0]+".csv");
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));) {
					writer.write(export);
				}
			}
			projectComparator = null;
			JavaParserFacade.clearInstances();
		}
	}

}