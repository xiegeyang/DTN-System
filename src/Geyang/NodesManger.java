package Geyang;

import java.util.*;

public class NodesManger {
	private int size;
	private Vector<Node> nodesGroup;
	private Vector<Node> insecurityGroup;
	private int lines;
	private HistoryObj[][] matrix;
	
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

	
	public NodesManger(int size, int lines, boolean isOneGroup, boolean security){
		setSize(size);
		setLines(lines);
		nodesGroup = new Vector<>();
		insecurityGroup = new Vector<>();
		matrix = new HistoryObj[size][size];
		for(int i =0; i<size; i++){
			
			GoodNode_Security_Runnable node = new GoodNode_Security_Runnable(i, this.nodesGroup, size);
			nodesGroup.add(node);
			GoodNode_Runnable inseNode = new GoodNode_Runnable(i, this.insecurityGroup,size);
			insecurityGroup.add(inseNode);
		}
		randomMatrix(matrix);
		for(int i =0;i<matrix.length;i++){
			for(int j =0;j<matrix[0].length;j++){
				HistoryObj obj = matrix[i][j];
				//HistoryObj obj2 = new HistoryObj(matrix[i][j].getTimes());
				System.out.print(obj.getTimes()+" ");
			}
			System.out.println();
		}
		for(int i =0;i<size;i++){
			GoodNode_Security_Runnable node = (GoodNode_Security_Runnable)this.nodesGroup.get(i);
			node.matrix = new HistoryObj[size][size];
			for(int j = 0;j<matrix.length;j++){
				for(int z = 0;z<matrix[0].length;z++){
					HistoryObj temp = matrix[j][z].clone();
					node.matrix[j][z] = temp;
				}
			}
			
		}
		
		for(int i =0;i<size;i++){
			GoodNode_Runnable node = (GoodNode_Runnable)this.insecurityGroup.get(i);
			node.matrix = new HistoryObj[size][size];
			for(int j = 0;j<matrix.length;j++){
				for(int z = 0;z<matrix[0].length;z++){
					HistoryObj temp = new HistoryObj(matrix[j][z].getTimes());
					node.matrix[j][z] = temp;
				}
			}
			
		}
		
		ShortPath sp = new ShortPath(size,matrix,1);
		ShortPath inseSp = new ShortPath(size, matrix,2);
		for(int i =0;i<size;i++){
			GoodNode_Security_Runnable node = (GoodNode_Security_Runnable)this.nodesGroup.get(i);
			node.sp = sp;
		}
		for(int i =0;i<size;i++){
			GoodNode_Runnable node = (GoodNode_Runnable)this.insecurityGroup.get(i);
			node.sp = inseSp;
		}
		for(int i =0;i<size;i++){
			GoodNode_Security_Runnable node = (GoodNode_Security_Runnable)this.nodesGroup.get(i);
			GoodNode_Runnable node2= (GoodNode_Runnable)this.insecurityGroup.get(i);
			node.insecurityNode = node2;
		}
		//makeConnection_Security(lines, isOneGroup);
	}
	
	public void randomMatrix(HistoryObj matrix[][]){
		for(int i = 0 ;i<matrix.length;i++){
			for(int j =0;j<matrix[0].length;j++){
				GoodNode_Security_Runnable nodeA = (GoodNode_Security_Runnable)nodesGroup.get(i);
				GoodNode_Security_Runnable nodeB = (GoodNode_Security_Runnable)nodesGroup.get(j);
				Random random = new Random();
				int ranTime = random.nextInt(100);
				if(i==j) ranTime = 0;
				HistoryObj history = new HistoryObj(ranTime);
				history.signatureA = nodeA.sign(history.data);
				history.signatureB = nodeB.sign(history.data);
				HistoryObj history2 = history.clone();
				matrix[i][j] = history;
				matrix[j][i] = history2;
			}
		}
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
	
	public void testSecrity(){
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
	
	
	
}
