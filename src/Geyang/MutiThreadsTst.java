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
	
	public void tstNodesManger(){
		NodesManger ndsMg= new NodesManger(5,5,true);
		//NodesManger ndsMg= new NodesManger(10);
		ndsMg.test();
	}
	
	public void tstSign(){
		NodesManger ndsMg= new NodesManger();
		//ndsMg.testSignature();
		ndsMg.testMatrix();
	}
	
	public void tstNodesManger_Security(){
		NodesManger ndsMg= new NodesManger(3,3,true,true);
		//NodesManger ndsMg= new NodesManger(10);
		ndsMg.testSecrity();
	}
	
}
