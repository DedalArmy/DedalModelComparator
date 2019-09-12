package fr.imt.mines.ales.structure;

public interface DiffConnection extends DiffDedal{
	public void setServerDiffComponent(DiffComponent diffComponent);
	public void setClientDiffComponent(DiffComponent diffComponent);
	public DiffComponent getServerDiffComponent();
	public DiffComponent getClientDiffComponent();
	public void setServerDiffInterface(DiffInterface diffInterface);
	public void setClientDiffInterface(DiffInterface diffInterface);
	public DiffInterface getServerDiffInterface();
	public DiffInterface getClientDiffInterface();
}
