package fr.imt.mines.ales.structure;

import java.util.Set;

public interface DiffSignature  extends DiffDedal{
	public void addDiffParameter(DiffParameter diffParameter);
	public DiffParameter getDiffParameter(DiffParameter diffParameter);
	public DiffParameter getDiffParameter(String name);
	public Set<DiffParameter> getDiffParameters();
	public Boolean containsDiffParameter(DiffParameter diffParameter);
	Boolean containsDiffParameter(String name);
	String getName();
}
