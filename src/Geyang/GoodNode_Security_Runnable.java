package Geyang;

import java.io.IOException;
import java.nio.ByteBuffer;
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
	protected HashMap<Integer, PublicKey> keyMap = new HashMap<>();
	
	
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

	public HashMap<Integer, PublicKey> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(HashMap<Integer, PublicKey> keyMap) {
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
			keyMap.put(newNode.label, newNode.getPublicKey());
			System.out.println("Node " + this.label + " got key from node :" + newNode.label + " , key is"  +keyMap.get(newNode.label).toString());
		}
		if(!newNode.keyMap.containsKey(this)){
			newNode.keyMap.put(this.label, this.getPublicKey());
			System.out.println("Node " + newNode.label +" got key from node " + this.label + " , key is "  +newNode.keyMap.get(this.label).toString());
		}
		sendMessage(newNode);
		sendMatrix(newNode);
		return true;
	}
	
	public boolean sendMessage(Node nodeB){
		Message message = ((GoodNode_Security_Runnable)this).createMessage(this.matrix, nodeB, true);
		//message.setTimes(10);
		((GoodNode_Security_Runnable)nodeB).receiveMessage(this, message);
		message = ((GoodNode_Security_Runnable)nodeB).createMessage(nodeB.matrix, this, false);
		((GoodNode_Security_Runnable)this).receiveMessage(nodeB, message);
		return true;
	}
	
	public Message createMessage(HistoryObj[][] matrix, Node desNode, boolean first){
		
		HistoryObj historyObj = matrix[this.label][desNode.label];
		//matrix[this.label][desNode.label] = null;
		if(historyObj == null){
			historyObj = new HistoryObj(0);
		}
		
		if(!((verify(desNode, historyObj.signatureA, historyObj.data) && 
				verify(this, historyObj.signatureB, historyObj.data))||
				(verify(desNode, historyObj.signatureB, historyObj.data) && 
				verify(this, historyObj.signatureA, historyObj.data)))&& 
				historyObj.signatureA !=null && historyObj.signatureB!=null){
			System.out.println("Attack detact");
			return null;
		}
		int times = historyObj.getTimes();
		//System.out.println(times);
		if(first)
			times++;
		
		Message message = new Message(this.label, desNode.label, times);
		System.out.println("Message Created success, times is " + message.getTimes());
		message.signature_Sting = sign(message.data_String);
		message.signature_times = sign(message.data_times);
		//historyObj.getContactHis().setTimes(times+1);
		//signHistoryObj(historyObj);
		//matrix[this.label][desNode.label] = historyObj;
		//matrix[desNode.label][this.label] = historyObj;
		//desNode.receiveContactHis(this);
		return message;
	}
	
	public boolean receiveMessage(Node sourceNode,Message message){
		System.out.println("Node " + this.label + " received the message from Node " + sourceNode.label);
		if(!verify(sourceNode, message.signature_Sting, message.data_String)){
			System.out.println("ID spoofing Attack detect!");
			return false;
			
		}
		if(!verify(sourceNode, message.signature_times, message.data_times)){
			System.out.println("potential Attack detect!");
			return false;
		}
		if(matrix[this.label][sourceNode.label].getTimes()!=matrix[sourceNode.label][this.label].getTimes()
				|| matrix[this.label][sourceNode.label].getTimes()+1!= message.getTimes()){
			System.out.println("Malicious intent Attack detect!");
			return false;
		}
		//matrix[this.label][sourceNode.label] = null;
		HistoryObj historyObj = new HistoryObj(message.getTimes());
		//System.out.println("historyObj.times is " + historyObj.times + " message.times is " + message.times);
		historyObj.data = message.data_times;
		historyObj.signatureA = message.signature_times;
		historyObj.signatureB = sign(message.data_times);
		HistoryObj historyObj2 = historyObj.clone();
		matrix[this.label][sourceNode.label] = null;
		matrix[this.label][sourceNode.label] = historyObj;
		matrix[sourceNode.label][this.label] = null;
		matrix[sourceNode.label][this.label] = historyObj2;
		
		for(int i = 0; i< matrix.length;i++){
			for(int j =0;j<matrix[0].length;j++){
				System.out.print(matrix[i][j].getTimes());
			}
			System.out.println();
		}
		return true;
	}
	
	public boolean sendMatrix(GoodNode_Security_Runnable desNode){
		int hash_Matrix = this.matrix.hashCode();
		byte[] data_Matrix = ByteBuffer.allocate(4).putInt(hash_Matrix).array();
		byte[] signature = this.sign(data_Matrix);
		
		desNode.reseiveMatrix(this.matrix, this, signature, data_Matrix);
		return true;
	}
	
	public boolean reseiveMatrix(HistoryObj[][] matrix, Node neighber, byte[] signature, byte[] data_Matrix){
		//System.out.println(signature.length);
		if(!this.verify(neighber, signature, data_Matrix)){
			System.out.println("potential Attack Detect");
			return false;
		}
		System.out.println(this.label + "--------------" + neighber.label);
		compare(matrix);	
		
		return true;
	}
	
	private boolean compare(HistoryObj[][] neiMatrix){
		for(int i =0;i<matrix.length; i++){
			for(int j =0;j<matrix[i].length;j++){
				//HistoryObj ch =  verifyHistoryObj()
				HistoryObj obj1 = this.matrix[i][j];
				HistoryObj obj2 = neiMatrix[i][j];
				//if(!(obj1.signatureA == null && obj1.signatureB == null && obj1.getTimes()==0))
				//if()
				if(obj1.getTimes()!=0 && !((verify(nodesGroup.get(i), obj1.signatureA, obj1.data) 
						&& verify(nodesGroup.get(j), obj1.signatureB, obj1.data)) ||
						(verify(nodesGroup.get(i), obj1.signatureB, obj1.data) && 
						verify(nodesGroup.get(j), obj1.signatureA, obj1.data)))){
						System.out.println(" Attack on the integrity detect [i, j] : [" + i + " , " + j + "]" );
						return false;
					
				}
				if(obj2.getTimes()!=0 && !((verify(nodesGroup.get(i), obj2.signatureA, obj2.data) 
						&& verify(nodesGroup.get(j), obj2.signatureB, obj2.data)) ||
						(verify(nodesGroup.get(i), obj2.signatureB, obj2.data) && 
						verify(nodesGroup.get(j), obj2.signatureA, obj2.data)))){
					
						System.out.println(" Attack on the integrity detect [i, j] : [" + i + " , " + j + "]"  );
						return false;
						
				}
				if(this.matrix[i][j].getTimes() < neiMatrix[i][j].getTimes()) {
					System.out.println("Node : " + label + " has changed the matrix " + i + " " + j
							+ " from " + matrix[i][j].getTimes() + " to " + neiMatrix[i][j].getTimes());
					this.matrix[i][j] = null;
					HistoryObj temp_obj = neiMatrix[i][j];
					this.matrix[i][j] = temp_obj.clone();
				}	
			}
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
			keyMap.put(this.label, publicKey);
	        } catch (Exception e) {
	            System.err.println("Caught exception " + e.toString());
	        }
	}
	
	public byte[] sign(byte[] data){
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
	
	public boolean verify(Node neignberNode, byte[] signature, byte[] data){
		if(neignberNode == null ||signature ==null ||data == null ||signature.length==0 || data.length == 0){
			return false;
		}
		try {
			Signature sign = Signature.getInstance("SHA1withRSA");
			sign.initVerify(((GoodNode_Security_Runnable)neignberNode).getPublicKey());
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
	
	public void run(){
		GoodNode_Security_Runnable randomNode = null;
		while(true){
			try{
				Thread.sleep((int)( Math.random() * 10000));
				if(!ChangeContact(10)){
					randomNode = (GoodNode_Security_Runnable)randomNeighbors();
					sendMessage(randomNode);
					sendMatrix(randomNode);
				}
					
			}catch(Exception e){
				 e.printStackTrace();
			}
			
		}
	}
}
