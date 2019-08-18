package fr.imt.mines.ales.component;

import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;

public abstract class Checker4DedalInterface extends Checker implements IChecker4DedalInterface {

	public Checker4DedalInterface(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		super(hierarchyBuilderOld, hierarchyBuilderNew);
	}

}
