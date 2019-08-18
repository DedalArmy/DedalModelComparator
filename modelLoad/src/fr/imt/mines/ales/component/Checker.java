package fr.imt.mines.ales.component;

import fr.imt.ales.redoc.type.hierarchy.build.HierarchyBuilder;

public abstract class Checker{
	private HierarchyBuilder hierarchyBuilderOld;
	private HierarchyBuilder hierarchyBuilderNew;
	
	public Checker(HierarchyBuilder hierarchyBuilderOld, HierarchyBuilder hierarchyBuilderNew) {
		this.hierarchyBuilderOld = hierarchyBuilderOld;
		this.hierarchyBuilderNew = hierarchyBuilderNew;
	}
	
	public HierarchyBuilder getHierarchyBuilderOld() {
		return hierarchyBuilderOld;
	}
	
	public HierarchyBuilder getHierarchyBuilderNew() {
		return hierarchyBuilderNew;
	}
}
