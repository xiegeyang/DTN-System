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
		NodesManger ndsMg= new NodesManger(10);
		//NodesManger ndsMg= new NodesManger(10);
		ndsMg.randomAttacks(0);
		//GoodNode_Security_Runnable badNode = (GoodNode_Security_Runnable) ndsMg.getNodesGroup().get(2);
		///badNode.matrix[5][1].setTimes(1000);
		//badNode.matrix[5][2].setTimes(1000);
		//badNode.matrix[5][3].setTimes(1000);
		//badNode.matrix[3][4].setTimes(1000);
		//badNode.matrix[2][1].setTimes(1000);
		//badNode.insecurityNode.matrix[5][1].setTimes(1000);
		//badNode.insecurityNode.matrix[5][2].setTimes(1000);
		//badNode.insecurityNode.matrix[5][3].setTimes(1000);
		//badNode.insecurityNode.matrix[3][4].setTimes(1000);
		//badNode.insecurityNode.matrix[2][1].setTimes(1000);
		
		
		ndsMg.testSecrity();
	}
	
}
