package Geyang;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Vector;

public class GoodNode_Security_Runnable extends GoodNode_Runnable implements Security{
	
	protected KeyPair pair;
	protected PrivateKey privateKey;
	protected PublicKey publicKey;
	protected HashMap<Node, PublicKey> keyMap = new HashMap<>();
	
	
	public KeyPair getPair() {
		return pair;
	}

	public void setPair(KeyPair pair) {
		this.pair = pair;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public HashMap<Node, PublicKey> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(HashMap<Node, PublicKey> keyMap) {
		this.keyMap = keyMap;
	}

	
	
	
	public GoodNode_Security_Runnable(int label) {
		super(label);
		keyGeneration();
		// TODO Auto-generated constructor stub
	}
	
	public GoodNode_Security_Runnable(int label, Vector<Node> vec, int numOfNds) {
		super(label, vec, numOfNds);
		keyGeneration();
		// TODO Auto-generated constructor stub
	}
	
	public void disConnect(GoodNode_Security_Runnable newNode){
		super.disConnect(newNode);
		if(this.keyMap.containsKey(newNode)) this.keyMap.remove(newNode);
		if(newNode.keyMap.containsKey(this)) newNode.keyMap.remove(this);
	}
	
	public boolean getConnect(GoodNode_Security_Runnable newNode, boolean isOneGroup){
		super.getConnect(newNode, isOneGroup);
		if(!keyMap.containsKey(newNode)){
			keyMap.put(newNode, newNode.getPublicKey());
			System.out.println("Node " + this.label + " got key from node :" + newNode.label + " , key is"  +keyMap.get(newNode).toString());
		}
		if(!newNode.keyMap.containsKey(this)){
			newNode.keyMap.put(this, this.getPublicKey());
			System.out.println("Node " + newNode.label +" got key from node " + this.label + " , key is "  +newNode.keyMap.get(this).toString());
		}
		return true;
	}
	
	public void keyGeneration() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		    kpg.initialize(1024);
		    KeyPair keyPair = kpg.genKeyPair();
			
			this.pair = keyPair;
			this.privateKey = keyPair.getPrivate();
			this.publicKey = keyPair.getPublic();
	        } catch (Exception e) {
	            System.err.println("Caught exception " + e.toString());
	        }
	}
	
	public void sign(HistoryObj historyObj){
		try {
			Signature sign = Signature.getInstance("SHA1withRSA");
			try {
				sign.initSign(this.privateKey);
				try {
					sign.update(historyObj.getData());
					byte[] signature = sign.sign();
					if(historyObj.getSignatureA()== null){
						historyObj.setSignatureA(signature);
					}
					if(historyObj.getSignatureB()== null){
						historyObj.setSignatureB(signature);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean verify(Node neignberNode, HistoryObj historyObj){
		
		try {
			Signature sign = Signature.getInstance("SHA1withRSA");
			sign.initVerify(((GoodNode_Security_Runnable)neignberNode).publicKey);
			sign.update(historyObj.getData());
			if(sign.verify(historyObj.getSignatureA())){
				historyObj.setSignatureA(null);
				return true;
			}
			if(sign.verify(historyObj.getSignatureB())){
				historyObj.setSignatureB(null);
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
