package Geyang;

import java.util.*;

public class NodesManger {
	private int size;
	private Vector<Node> nodesGroup;
	private int lines;
	
	private void setSize(int size){
		if(size <= 0){
			System.out.println("Error : Nodes Group size cannot less than 1, We will make the size with 1");
			this.size = 1;
			return;
		}
		this.size = size;
	}
	
	private void setLines(int lines){
		if(size <= 0){
			System.out.println("Error : Nodes Group lines cannot less than 1, We will make the lines with 0");
			this.lines = 0;
			return;
		}
		this.lines = lines;
	}
	
	public Vector<Node> getNodesGroup() {
		return nodesGroup;
	}
	
	public NodesManger(){
		
	}

	public NodesManger(int size){
		setSize(size);
		nodesGroup = new Vector<>();
		for(int i =0; i<size; i++){
			GoodNode_Runnable node = new GoodNode_Runnable(i, this.nodesGroup, size);
		}
		Random ran = new Random();
		this.lines = ran.nextInt(size * 2 - size) + size;
		makeConnection(lines,false);
		
	}
	
	public NodesManger(int size, int lines){
		setSize(size);
		setLines(lines);
		nodesGroup = new Vector<>();
		for(int i =0; i<size; i++){
			GoodNode_Runnable node = new GoodNode_Runnable(i, this.nodesGroup, size);
		}
		makeConnection(lines,false);
	}
	
	public NodesManger(int size, int lines, boolean isOneGroup){
		setSize(size);
		setLines(lines);
		nodesGroup = new Vector<>();
		for(int i =0; i<size; i++){
			GoodNode_Runnable node = new GoodNode_Runnable(i, this.nodesGroup, size);
		}
		makeConnection(lines, isOneGroup);
	}
	
	
	
	/*public void makeConnection(int lines){
		System.out.println("The number of lines are : "+lines);
		Random ran = new Random();
		for(int i =0;i<lines;i++){
			Node nodeA = nodesGroup.elementAt(ran.nextInt(size)); 
			Node nodeB = nodesGroup.elementAt(ran.nextInt(size)); 
			if(nodeA != nodeB){
				nodeA.getConnect(nodeB, false);
			}
		}
	}*/
	
	public void makeConnection(int lines, boolean isOneGroup){
		System.out.println("The number of lines are : "+lines);
		Random ran = new Random();
		if(isOneGroup){
			for(int i =0;i<size-1;i++){
				Node nodeA = nodesGroup.elementAt(ran.nextInt(size)); 
				Node nodeB = nodesGroup.elementAt(ran.nextInt(size)); 
				if(!nodeA.getConnect(nodeB, isOneGroup)) i--;
			}
			for(int i =0 ; i<lines-size+1;i++){
				Node nodeA = nodesGroup.elementAt(ran.nextInt(size)); 
				Node nodeB = nodesGroup.elementAt(ran.nextInt(size)); 
				if(!nodeA.getConnect(nodeB, false)) i--;
			}
		}else{
			for(int i =0;i<lines;i++){
				Node nodeA = nodesGroup.elementAt(ran.nextInt(size)); 
				Node nodeB = nodesGroup.elementAt(ran.nextInt(size)); 
				if(nodeA != nodeB){
					nodeA.getConnect(nodeB,false);
				}
			}
		}
	}
	
	public void test(){
		for(int i =0; i<nodesGroup.size() ;i++){
			Node goodNode = nodesGroup.elementAt(i);
			System.out.println(goodNode.label + " : ");
			for(int j =0; j<goodNode.neighbors.size();j++){
				System.out.print(goodNode.neighbors.elementAt(j).label +" ");
			}
			System.out.println();
		}
		System.out.println("Connection ID of each node: ");
		for(int i =0; i<nodesGroup.size() ;i++){
			Node goodNode = nodesGroup.elementAt(i);
			System.out.println("Node " + nodesGroup.elementAt(i).label+ " : " + goodNode.connectID );
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		/*for(int i =0;i<vec.size();i++){
			GoodNode goodNode = vec.elementAt(i);
			for(int j=0;j<goodNode.nodesGroup.size();j++){
				System.out.println(goodNode.nodesGroup.elementAt(j).label+"");
			}
		}*/
		
		for(int i =0;i<nodesGroup.size();i++ ){
			new Thread((GoodNode_Runnable)nodesGroup.elementAt(i)).start();
		}
	}
	
	
	public void testSignature(){
		GoodNode_Security_Runnable goodNode1 = new GoodNode_Security_Runnable(1);
		GoodNode_Security_Runnable goodNode2 = new GoodNode_Security_Runnable(2);
		HistoryObj historyObj = new HistoryObj(1);
		goodNode1.sign(historyObj);
		System.out.println(goodNode2.verify(goodNode1, historyObj));
	}
}
