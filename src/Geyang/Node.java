package Geyang;


import java.io.IOException;
import java.security.*;
import java.util.*;

public abstract class Node {
	protected int label;
	protected Vector<Node> neighbors;
	protected Message msg;
	//protected HashMap<Node, Integer> frequency;
	//protected HashMap<Node, HashMap<Node, Integer>> matrix;
	protected HistoryObj matrix[][];
	protected Vector<Node> nodesGroup;
	protected int connectID;
	public ShortPath sp;
	public int preference;
	
	
	public void setMsg(Message msg){
		if(msg == null) return;
		encriptMsg(msg);
	}
	
	
	
	
	public void disConnect(Node newNode){
		
		if(newNode == null) return;
		if(this == newNode) return;
		if(newNode.connectID != this.connectID){
			return;
		}
		System.out.println("Node " + this.label +" disconnect with node " + newNode.label);
		if(newNode.neighbors.contains(this)) newNode.neighbors.remove(this);
		if(this.neighbors.contains(newNode)) this.neighbors.remove(newNode);
		syncConnectID(this, this.label);
		syncConnectID(newNode, newNode.label);
		
		//this.setTimes(this, newNode, 0);
		//System.out.println("Connection ID of each node: ");
		//for(int i =0; i<nodesGroup.size() ;i++){
		//	Node goodNode = nodesGroup.elementAt(i);
		//	System.out.println("Node " + nodesGroup.elementAt(i).label+ " : " + goodNode.connectID );
		//}
	}
	
	/*public void getConnect(Node newNode){
		if(newNode == null) return;
		if(this == newNode) return;
		if(this.neighbors.contains(newNode)) return;
		if(newNode.connectID == this.connectID){
			System.out.println("Warning! Node : " + label + " has already connected to the node : "
					+ newNode.label);
			return;
		}
		if(!keyMap.containsKey(newNode)){
			keyMap.put(newNode, newNode.getPub());
			System.out.println("Node " + this.label + " got key from node :" + newNode.label + " , key is"  +keyMap.get(newNode).toString());
		}
		if(!newNode.keyMap.containsKey(this)){
			newNode.keyMap.put(this, this.getPub());
			System.out.println("Node " + newNode.label +" got key from node :" + this.label + " , key is"  +newNode.keyMap.get(this).toString());
		}
		if(!this.neighbors.contains(newNode)) this.neighbors.add(newNode);
		if(!newNode.neighbors.contains(this)) newNode.neighbors.add(this);
		syncConnectID(this, newNode);
		System.out.println("Node " + this.label +" connect with node " + newNode.label);
		//System.out.println("Connection ID of each node: ");
		//for(int i =0; i<nodesGroup.size() ;i++){
		//	Node goodNode = nodesGroup.elementAt(i);
		//	System.out.println("Node " + nodesGroup.elementAt(i).label+ " : " + goodNode.connectID );
		//}
	}*/
	
	public boolean getConnect(Node newNode, boolean isOneGroup){
		System.out.println("Node " + this.label +" connect with node " + newNode.label);
		if(newNode == null) return false;
		if(this.neighbors.contains(newNode)) return false;
		if(isOneGroup && newNode.connectID == this.connectID){
			return false;
		}
		if(this == newNode) return false;
		
		if(!this.neighbors.contains(newNode)) this.neighbors.add(newNode);
		if(!newNode.neighbors.contains(this)) newNode.neighbors.add(this);
		syncConnectID(this, newNode);
		this.setTimes(this, newNode, 1);
		//System.out.println("Connection ID of each node: ");
		//for(int i =0; i<nodesGroup.size() ;i++){
		//	Node goodNode = nodesGroup.elementAt(i);
		//	System.out.println("Node " + nodesGroup.elementAt(i).label+ " : " + goodNode.connectID );
		//}
		return true;
	}
	
	protected void syncConnectID(Node origiNode, Node newNode){
		if(origiNode == null || newNode == null) return;
		if(origiNode.connectID == newNode.connectID) return;
		origiNode.connectID = newNode.connectID;
		for(int i = 0;i<origiNode.neighbors.size();i++){
			syncConnectID(origiNode.neighbors.elementAt(i), newNode);
		}
		return;
	}
	
	private void syncConnectID(Node origiNode, int connectedID){
		if(origiNode == null || connectedID < 0) return;
		if(origiNode.connectID == connectedID) return;
		origiNode.connectID = connectedID;
		for(int i = 0;i<origiNode.neighbors.size();i++){
			syncConnectID(origiNode.neighbors.elementAt(i), connectedID);
		}
		return;
	}
	
	public boolean sendMessage(Node desNode){
		if(desNode == null){
			return false;
		}
		
			System.out.println("Insecurity Node : " + label + ", sending message : " + this.msg 
					+ ", to Insecurity Node : " + desNode.label);
			SendContactHis(this.matrix, desNode);
			//sendMatrix(desNode);
			//System.out.println(this.matrix[this.label][desNode.label].getTimes()+"-----------------------");
			
			
		return true;
	}
	
	public boolean sendMatrix(Node desNode){
		System.out.println("!!!!!!!!!!!!!!!!!!");
		if(desNode == null){
			return false;
		}
		if(!desNode.reseiveMatrix(this.matrix, this)) return false;
		sp.setMatrix(this.label, desNode.label, this.matrix[this.label][desNode.label].getTimes());
		return true;
	}
	
	private boolean reseiveMatrix(HistoryObj[][] matrix, Node neighber){
		if(matrix.length == 0) return false;
		if(matrix[0].length == 0) return false;
		
		System.out.println("Node : " + label + " has reseived matrix from node : " + neighber.label 
				+ ", compare begins!! ");
		if(!compare(matrix)) return false;
		return true;
	}
	
	public boolean SendContactHis(HistoryObj[][] matrix, Node desNode){
		
		HistoryObj his1 = new HistoryObj(this.matrix[this.label][desNode.label].getTimes()+1);
		HistoryObj his2 = new HistoryObj(this.matrix[desNode.label][this.label].getTimes()+1);
		matrix[this.label][desNode.label] = his1;
		matrix[desNode.label][this.label] = his2;
		desNode.receiveContactHis(this);
		return true;
	}
	
	public boolean receiveContactHis(Node sourceNode){
		
		matrix[this.label][sourceNode.label] = new HistoryObj(this.matrix[this.label][sourceNode.label].getTimes()+1);
		matrix[sourceNode.label][this.label] = new HistoryObj(this.matrix[sourceNode.label][this.label].getTimes()+1);
		return true;
	}
	
	
	private boolean compare(HistoryObj[][] neiMatrix){
		if(matrix.length == 0) return false;
		if(matrix[0].length == 0) return false;
		for(int i =0;i<matrix.length; i++){
			for(int j =0;j<matrix[i].length;j++){
				//HistoryObj ch =  verifyHistoryObj()
				if(this.matrix[i][j].getTimes() < neiMatrix[i][j].getTimes()) {
					//System.out.println("Node : " + label + " has changed the matrix " + i + " " + j
						//	+ " from " + matrix[i][j].getTimes() + " to " + neiMatrix[i][j].getTimes());
					this.matrix[i][j].setTimes(neiMatrix[i][j].getTimes());
					//sp.setMatrix(i,j, this.matrix[i][j].getTimes());
				}	
			}
		}
		return true;
	}
	
	public boolean isConnect(Node des){
		if(des == null) return false;
		if(des.connectID !=this.connectID){
			System.out.println("Error! Node : " + label + " does not connect to the node : " + des.label);
			return false;
		}
		return true;
	}
	
	private boolean encriptMsg(Message msg){
		if(msg == null ) return false;
		this.msg = msg;
		return true;
	}
	
	protected void randomMatrix(HistoryObj[][] matrix){
		for(int i =0;i<matrix.length;i++){
			for(int j =0;j<matrix[0].length;j++){
				//Random ran = new Random();
				matrix[i][j] = new HistoryObj(0);
			}
		}
	}
	
	
	/*protected SignedObject signHistoryObj(HistoryObj history, PrivateKey key) {
		if (history==null || key ==null) {
			System.err.println("signHistoryObj error: history or private key can't be null");
			return null;
		}
		SignedObject so = null;
		try{
			Signature signingEngine = Signature.getInstance("SHA-1/RSA");
			
			so = new SignedObject(history, key, signingEngine);
		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
		}
		return so;
		
	}
	
	public HistoryObj verifyHistoryObj(SignedObject so, PublicKey pKey) {
		if (so ==null || pKey == null) {
			System.err.println("verifyHistoryObj error: so or public key can't be null");
			return null;
		}
		HistoryObj history = null;
		try {
			Signature verificationEngine =
				     Signature.getInstance("SHA-1/RSA");
				 if (so.verify(pKey, verificationEngine))
				     try {
				    	 history = (HistoryObj) so.getObject();
				     } catch (java.lang.ClassNotFoundException e) {
				    	 System.err.println("Caught exception " + e.toString());
				     };
		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
		}
		return history;
	}*/
	
	private boolean setTimes(Node nodeA, Node nodeB, int i){
		nodeA.matrix[nodeB.label][nodeA.label] = null;
		nodeA.matrix[nodeB.label][nodeA.label] = new HistoryObj(i);
		nodeA.matrix[nodeA.label][nodeB.label] = null;
		nodeA.matrix[nodeA.label][nodeB.label] = new HistoryObj(i);
		nodeB.matrix[nodeB.label][nodeA.label] = null;
		nodeB.matrix[nodeB.label][nodeA.label] = new HistoryObj(i);
		nodeB.matrix[nodeA.label][nodeB.label] = null;
		nodeB.matrix[nodeA.label][nodeB.label] = new HistoryObj(i);
		return true;
	}
	
}
