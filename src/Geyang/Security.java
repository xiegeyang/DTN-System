package Geyang;

public interface Security {
	public byte[] signHistoryObj(byte[] data);
	public boolean verifyHistoryObj(Node neignberNode, byte[] signature, byte[] data);
	void keyGeneration();
}
