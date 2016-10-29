package Geyang;

import java.util.*;

public class MutiThreadsTst {
	public static void main(String[] args){
		MutiThreadsTst test = new MutiThreadsTst();
		//test.MutiTreadsNodes();
		test.tstNodesManger();
		//test.tstSign();
	}
	
	public void tstNodesManger(){
		NodesManger ndsMg= new NodesManger(5,5,true);
		//NodesManger ndsMg= new NodesManger(10);
		ndsMg.test();
	}
	
	public void tstSign(){
		NodesManger ndsMg= new NodesManger();
		ndsMg.testSignature();
	}
}
