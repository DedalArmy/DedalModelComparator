package fr.imt.mines.ales;

import java.io.IOException;
import java.util.List;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;

import fr.imt.mines.ales.comparators.ProjectComparator;
import fr.imt.mines.ales.utils.CSVReader;

public class Main {

	public static void main(String[] args) {
		
		CSVReader csvReader = new CSVReader();
		List<String[]> rows = csvReader.readCsv("/home/quentin/broadleaf-versions/csvtest.csv");
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

		
		for (int i = 2; i < rows.size(); i++) {
			ProjectComparator projectComparator = new ProjectComparator();
			try {
				projectComparator.loadResources(
						"/home/quentin/broadleaf-versions/" + rows.get(i-1)[1],
						"/home/quentin/broadleaf-versions/" + rows.get(i)[1],
						"/home/quentin/broadleaf-versions/"
						);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			projectComparator.createDifferences();
			projectComparator = null;
			JavaParserFacade.clearInstances();
		}
	}

}