package Geyang;

public interface Security {
	public byte[] sign(byte[] data);
	public boolean verify(Node neignberNode, byte[] signature, byte[] data);
	void keyGeneration();
}
