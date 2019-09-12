package fr.imt.mines.ales.structure;

import java.util.Set;

public interface DiffComponent extends DiffDedal{
	public void addDiffInterface(DiffInterface diffInterface);
	public DiffInterface getDiffInterface(DiffInterface diffInterface);
	public DiffInterface getDiffInterface(String name);
	public Set<DiffInterface> getDiffInterfaces();
	Boolean containsDiffInterface(String name);
	Boolean containsDiffInterface(DiffInterface diffInterface);
}
