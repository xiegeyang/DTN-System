package Geyang;

public interface Security {
	public void signHistoryObj(HistoryObj historyObj);
	public boolean verifyHistoryObj(Node neignberNode, HistoryObj historyObj);
	void keyGeneration();
}
