package Geyang;

import java.util.*;

public class MutiThreadsTst {
	public static void main(String[] args){
		MutiThreadsTst test = new MutiThreadsTst();
		//test.MutiTreadsNodes();
		//test.tstNodesManger();
		//test.tstSign();
		test.tstNodesManger_Security();
	}
	
	
	
	
	
	public void tstNodesManger_Security(){
		NodesManger ndsMg= new NodesManger(6,6,true,true);
		//NodesManger ndsMg= new NodesManger(10);
		GoodNode_Security_Runnable badNode = (GoodNode_Security_Runnable) ndsMg.getNodesGroup().get(5);
		//badNode.matrix[5][1].setTimes(10);
		//badNode.matrix[5][2].setTimes(10);
		//badNode.matrix[5][3].setTimes(10);
		//badNode.matrix[5][4].setTimes(10);
		
		ndsMg.testSecrity();
	}
	
}
