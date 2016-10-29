package Geyang;

import java.io.Serializable;

public class HistoryObj {
	private ContactHis contactHis;
	public byte[] signatureA; 
	public byte[] signatureB; 
	private byte[] data;
	

	public HistoryObj(int time){
		contactHis = new ContactHis(time);
	}
	
	
	public ContactHis getContactHis() {
		return contactHis;
	}


	public void setContactHis(ContactHis contactHis) {
		this.contactHis = contactHis;
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
}

class ContactHis implements Serializable {
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	private int times;
	
	public int getTimes() {
		return times;
	}
	
	public void setTimes(int time) {
		this.times = time;
	}
	
	public ContactHis(int time){
		this.times = time;
	}
}



