package Geyang;

public interface Security {
	public void sign(HistoryObj historyObj);
	public boolean verify(Node neignberNode, HistoryObj historyObj);
	void keyGeneration();
}
