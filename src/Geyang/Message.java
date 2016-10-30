package Geyang;

import java.nio.ByteBuffer;
import java.util.Random;

public class Message {
	public String A;
	public int id_i;
	public int id_j;
	public byte[] data_String;
	public byte[] signature_Sting;
	private int times;
	public byte[] data_times;
	public byte[] signature_times;
	
	public Message(int id_i, int id_j,int times){
		char[] text = new char[32];
		String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rng = new Random();
	    for (int i = 0; i < 32; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    this.A = new String(text);
		this.id_i = id_i;
		this.id_j = id_j;
		this.times = times;
		String temp = A + this.id_i + this.id_j;
		this.data_String = temp.getBytes();
		this.data_times = ByteBuffer.allocate(4).putInt(times).array();
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
		this.data_times = ByteBuffer.allocate(4).putInt(times).array();
	}
	
	
}
