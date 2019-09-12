package fr.imt.mines.ales.structure;

import java.util.Set;

public interface DiffInterfaceType extends DiffDedal{
	public void addDiffSignature(DiffSignature diffSignature);
	public DiffSignature getDiffSignature(DiffSignature diffSignature);
	public DiffSignature getDiffSignature(String name);
	public Set<DiffSignature> getDiffSignatures();
	String getName();
	Boolean containsDiffSignature(DiffSignature diffSignature);
	Boolean containsDiffSignature(String name);
}
