package Geyang;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
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
	protected String A;
	
	public KeyPair getPair() {
		return pair;
	}
	public String getA() {
		return this.A;
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
		RandomString rsGenarator = new RandomString(32);
		this.A = rsGenarator.nextString();
		// TODO Auto-generated constructor stub
	}
	
	public GoodNode_Security_Runnable(int label, Vector<Node> vec, int numOfNds) {
		super(label, vec, numOfNds);
		keyGeneration();
		RandomString rsGenarator = new RandomString(32);
		this.A = rsGenarator.nextString();
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
	
	public boolean SendContactHis(HistoryObj[][] matrix, Node desNode){
		HistoryObj historyObj = matrix[this.label][desNode.label];
		if(historyObj == null){
			return false;
		}
		int times = historyObj.getContactHis().getTimes();
		historyObj.getContactHis().setTimes(times+1);
		//signHistoryObj(historyObj);
		matrix[this.label][desNode.label] = historyObj;
		matrix[desNode.label][this.label] = historyObj;
		desNode.receiveContactHis(this);
		return true;
	}
	
	public boolean receiveContactHis(Node sourceNode){
		HistoryObj historyObj = matrix[this.label][sourceNode.label];
		if(historyObj == null){
			return false;
		}
		//verifyHistoryObj(sourceNode, historyObj);
		int times = historyObj.getContactHis().getTimes()+1;
		historyObj.getContactHis().setTimes(times+1);
		matrix[this.label][sourceNode.label] = historyObj;
		matrix[sourceNode.label][this.label] = historyObj;
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
	
	public byte[] signHistoryObj(byte[] data){
		try {
			Signature sign = Signature.getInstance("SHA1withRSA");
			try {
				sign.initSign(this.privateKey);
				try {
					sign.update(data);
					byte[] signature = sign.sign();
					return signature;
					
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
		return new byte[0];
	}
	
	public boolean verifyHistoryObj(Node neignberNode, byte[] signature, byte[] data){
		try {
			Signature sign = Signature.getInstance("SHA1withRSA");
			sign.initVerify(this.keyMap.get(neignberNode));
			sign.update(data);
			if(sign.verify(signature)){
				return true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//
		}
		return false;
	}
	public boolean verifyRandomString(Message m, Node j) {
		String message = m.A + m.id_i + m.id_j;
		byte[] digest = getHash(message);
		try {
			Signature sign = Signature.getInstance("SHA1withRSA");
			sign.initVerify(keyMap.get(j));
			sign.update(digest);
			if(sign.verify(m.signature_String)){
				return true;
			}
		} catch  (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public byte[] signRandomString(int jLabel){
		String message = this.A + this.label + jLabel;
		Signature sign = null;
		byte[] digest = getHash(message);
		byte[] res = null;
		try {
			sign = Signature.getInstance("SHA1withRSA");
			sign.initSign(this.privateKey);
			
			sign.update(digest);
			res = sign.sign();
//				return sign;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	private byte[] getHash(String s) {
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(s.getBytes("UTF-8"));
			digest = md.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return digest;
	}
}
