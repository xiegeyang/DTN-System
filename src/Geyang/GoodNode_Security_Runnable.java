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
	public Node insecurityNode;
	
	
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

	
	public GoodNode_Security_Runnable(int label, Vector<Node> vec, int numOfNds) {
		super(label, vec, numOfNds);
		keyGeneration();
		//insecurityNode = new GoodNode_Runnable(label, )
		// TODO Auto-generated constructor stub
	}
	
	
	
	public boolean sendMessage(Node nodeB){
		if(nodeB == null){
			return false;
		}
		this.insecurityNode.sendMessage(((GoodNode_Security_Runnable)nodeB).insecurityNode);
		this.insecurityNode.sendMatrix(((GoodNode_Security_Runnable)nodeB).insecurityNode);
		boolean isSuccess = true;
		System.out.println("Secuity Node " + this.label + " send message to Security Node " + nodeB.label);
		Message message = ((GoodNode_Security_Runnable)this).createMessage(this.matrix, nodeB, true);
		//message.setTimes(10);
		if(message == null) return false;
		isSuccess = ((GoodNode_Security_Runnable)nodeB).receiveMessage(this, message);
		if(!isSuccess) return false;
		message = ((GoodNode_Security_Runnable)nodeB).createMessage(nodeB.matrix, this, false);
		if(message == null) return false;
		isSuccess = ((GoodNode_Security_Runnable)this).receiveMessage(nodeB, message);
		if(!isSuccess) return false;
		
		return true;
	}
	
	public Message createMessage(HistoryObj[][] matrix, Node desNode, boolean first){
		if(matrix == null || desNode == null) return null;
		HistoryObj historyObj = matrix[this.label][desNode.label];
		//System.out.println(" Create Message Label are " + this.label + " and " + desNode.label);		
		//matrix[this.label][desNode.label] = null;
		if(historyObj == null){
			historyObj = new HistoryObj(0);
		}
		
		int times = historyObj.getTimes();
		//System.out.println(times);
		if(!((verify(desNode, historyObj.signatureA, historyObj.data) && 
				verify(this, historyObj.signatureB, historyObj.data))||
				(verify(desNode, historyObj.signatureB, historyObj.data) && 
				verify(this, historyObj.signatureA, historyObj.data)))&& 
				(historyObj.signatureA !=null || historyObj.signatureB!=null)){
			System.out.println("Create message Attack detact" + " Node " + this.label + " and node " + desNode.label);
			setHistory(this, desNode,-1);
			return null;
		}
		
		
		Message message = new Message(this.label, desNode.label, times);
		//System.out.println("Message Created success, times is " + message.getTimes());
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
			setHistory(this,sourceNode,-1);
			return false;
			
		}
		if(!verify(sourceNode, message.signature_times, message.data_times)){
			System.out.println("potential Attack detect!");
			setHistory(this,sourceNode,-1);
			return false;
		}
		if(matrix[this.label][sourceNode.label].getTimes()!=matrix[sourceNode.label][this.label].getTimes()){
			System.out.println("1. Malicious intent Attack detect!");
			setHistory(this,sourceNode,-1);
			return false;
		}
		
		if(matrix[this.label][sourceNode.label].getTimes()!= message.getTimes()){
			System.out.println("2. Malicious intent Attack detect!");
			setHistory(this,sourceNode,-1);
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
		
		/*for(int i = 0; i< matrix.length;i++){
			for(int j =0;j<matrix[0].length;j++){
				System.out.print(matrix[i][j].getTimes() + " ");
			}
			System.out.println();
		}*/
		return true;
	}
	
	public boolean sendMatrix(GoodNode_Security_Runnable desNode){
		if(desNode == null) return false;
		int hash_Matrix = this.matrix.hashCode();
		byte[] data_Matrix = ByteBuffer.allocate(4).putInt(hash_Matrix).array();
		byte[] signature = this.sign(data_Matrix);
		
		if(!desNode.reseiveMatrix(this, signature, data_Matrix)) return false;
		sp.setMatrix(this.label, desNode.label, this.matrix[this.label][desNode.label].getTimes());
		return true;
		
	}
	
	public boolean reseiveMatrix(Node neighber, byte[] signature, byte[] data_Matrix){
		//System.out.println(signature.length);
		if(!this.verify(neighber, signature, data_Matrix)){
			System.out.println("potential Attack Detect");
			setHistory(this,neighber,-1);
			return false;
		}
		//System.out.println(this.label + "--------------" + neighber.label);
		return compare(neighber);
		
	}
	
	private boolean compare(Node node){
		for(int i =0;i<matrix.length; i++){
			for(int j =0;j<matrix[i].length;j++){
				//HistoryObj ch =  verifyHistoryObj()
				HistoryObj obj1 = this.matrix[i][j];
				HistoryObj obj2 = node.matrix[i][j];
				//if(!(obj1.signatureA == null && obj1.signatureB == null && obj1.getTimes()==0))
				//if()
				if(obj1.getTimes()==-1 && obj2.getTimes()==-1){
					
				}else if(obj1.getTimes()==-1 && obj2.getTimes()!=-1){
					HistoryObj temp = new HistoryObj(-1);
					HistoryObj temp2 = new HistoryObj(-1);
					node.matrix[i][j] = temp;
					node.matrix[j][i] = temp2;
				}else if(obj1.getTimes()!=-1 && obj2.getTimes()==-1){
					HistoryObj temp = new HistoryObj(-1);
					HistoryObj temp2 = new HistoryObj(-1);
					this.matrix[i][j] = temp;
					this.matrix[j][i] = temp2;
				}
				else{
					if(obj1.getTimes()!=0 && !((verify(nodesGroup.get(i), obj1.signatureA, obj1.data) 
							&& verify(nodesGroup.get(j), obj1.signatureB, obj1.data)) ||
							(verify(nodesGroup.get(i), obj1.signatureB, obj1.data) && 
							verify(nodesGroup.get(j), obj1.signatureA, obj1.data)))){
							System.out.println(" Attack on the integrity detect [i, j] : [" + i + " , " + j + "], from label " + this.label + "'s matrix" );
							//printMatrix(this);
							setHistory(this,node,-1);
							return false;
					}
					if(obj2.getTimes()!=0 && !((verify(nodesGroup.get(i), obj2.signatureA, obj2.data) 
							&& verify(nodesGroup.get(j), obj2.signatureB, obj2.data)) ||
							(verify(nodesGroup.get(i), obj2.signatureB, obj2.data) && 
							verify(nodesGroup.get(j), obj2.signatureA, obj2.data)))){
							System.out.println(" Attack on the integrity detect [i, j] : [" + i + " , " + j + "], from label " + node.label + "'s matrix"  );
							//printMatrix(node);
							setHistory(this,node,-1);
							return false;
							
					}
					if(this.matrix[i][j].getTimes() > node.matrix[i][j].getTimes()) {
						System.out.println("Node : " + label + " has changed the matrix " + i + " " + j
								+ " from " + matrix[i][j].getTimes() + " to " + node.matrix[i][j].getTimes());
						this.matrix[i][j] = null;
						HistoryObj temp_obj = node.matrix[i][j];
						this.matrix[i][j] = temp_obj.clone();
					}	
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
		if(neignberNode == null ||signature ==null ||data == null){
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
	
	public void printMatrix(Node node){
		System.out.println("Node : " + node.label +"'s matirx print : ");
		for(int i = 0; i< node.matrix.length;i++){
			for(int j =0;j<node.matrix[0].length;j++){
				System.out.print(node.matrix[i][j].getTimes());
			}
			System.out.println();
		}
	}
	
	public void setHistory(Node a , Node b,int times){
		//HistoryObj newHistory = new HistoryObj(times);
		//HistoryObj newHistory2 = newHistory.clone();
		//this.matrix[a.label][b.label] = newHistory;
		//this.matrix[b.label][a.label] = newHistory2;
		//this.sp.setMatrix(a.label, b.label, times);
	}
	
	protected boolean ChangeContact(int probability){
		//probability from 0 - 100;
		boolean isChanged = false;
		if(probability <0) probability = 0;
		if(probability >100) probability = 100;
		int pro = (int)(Math.random() * 100);
		if(pro <= probability ) {
			this.disConnect(this.randomNeighbors());
			isChanged = true;
		}
		pro = (int)(Math.random() * 100);
		if(pro <= probability){
			while(!((GoodNode_Security_Runnable)this).getConnect((GoodNode_Security_Runnable)this.randomNode(), false))
				this.getConnect((GoodNode_Security_Runnable)this.randomNode(), false);
			isChanged = true;
		}
		return isChanged;
	}
	
	public void run(){
		//int i =0; 
		GoodNode_Security_Runnable randomNode = null;
		while(true){
			try{
				Thread.sleep((int)( Math.random() * 10000));
				if(true){
					//i++;
					randomNode = (GoodNode_Security_Runnable)randomNode();
					
					if(randomNode!=null && randomNode!=this){
						//if(!ChangeContact(10))
						if(sendMessage(randomNode))
							sendMatrix(randomNode);
							
						
					}
				}
					
			}catch(Exception e){
				 e.printStackTrace();
			}
			
		}
	}
}
