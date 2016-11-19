package Geyang;

import java.util.*;


public class GoodNode_Runnable extends Node implements Runnable{
	

	
	
	public GoodNode_Runnable(int label, Vector<Node> vec, int numOfNds){
		this.label = label;
		this.connectID = label;
		this.neighbors = new Vector<>();
		GoodNode_Runnable.nodesGroup = vec;
		
		//randomMatrix(this.matrix);
		nodesGroup.add(this);
		//keyGeneration();
	}

	public void run(){
		while(true){
			try{
				Thread.sleep((int)( Math.random() * 10000));
				//ChangeContact(10);
					//sendMessage(randomNeighbors());
			}catch(Exception e){
				 e.printStackTrace();
			}
			
		}
	}
	
	protected Node randomNode(){
		int index = (int)(Math.random() * nodesGroup.size());
		return nodesGroup.elementAt(index) == this ? null: nodesGroup.elementAt(index);
	}
	
	protected Node randomNeighbors(){
		if(this.neighbors.size()==0) return null;
		int index = (int)(Math.random() * this.neighbors.size());
		return this.neighbors.elementAt(index) == this ? null: this.neighbors.elementAt(index);
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
			while(!this.getConnect(this.randomNode(), false))
				this.getConnect(this.randomNode(), false);
			isChanged = true;
		}
		return isChanged;
	}
}
