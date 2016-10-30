package Geyang;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class HistoryObj implements Cloneable{
	
	private int times;
	public byte[] signatureA; 
	public byte[] signatureB; 
	public byte[] data;
	

	public HistoryObj(int time){
		this.times = time;
		this.data = ByteBuffer.allocate(4).putInt(times).array();
	}
	
	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
		this.data = ByteBuffer.allocate(4).putInt(times).array();
	}
	
	
	public byte[] getSignatureB() {
		return signatureB;
	}

	public void setSignatureB(byte[] signatureB) {
		this.signatureB = signatureB;
	}

	public byte[] getSignatureA(){
		return this.signatureA;
	}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setSignatureA(byte[] signature) {
		this.signatureA = signature;
	}
	
	public HistoryObj clone(){  
	    try{  
	        return (HistoryObj)super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
}




