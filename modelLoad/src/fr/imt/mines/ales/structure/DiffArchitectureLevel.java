package fr.imt.mines.ales.structure;

import java.util.Set;

public interface DiffArchitectureLevel extends DiffDedal {
	public void addDiffComponent(DiffComponent diffComponent);
	public DiffComponent getDiffComponent(DiffComponent diffComponent);
	public DiffComponent getDiffComponent(String componentName);
	public Set<DiffComponent> getDiffComponents();
	public void addDiffConnection(DiffConnection diffConnection);
	public DiffConnection getDiffConnection(DiffConnection diffConnection);
	public DiffConnection getDiffConnection(String refID);
	public Set<DiffConnection> getDiffConnections();
}
