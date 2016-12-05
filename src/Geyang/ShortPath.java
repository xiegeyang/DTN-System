package Geyang;

import java.text.DecimalFormat;

public class ShortPath {
	private static DecimalFormat df2 = new DecimalFormat("#.######");
	public int label;
	private int matrix[][];
	private int originalMatrix[][];
	double originalDelay[][];
	double fakeDelay[][];
	double RealDelay[][];
	int size;
	
	public void setMatrix(int a , int b, int time){
		//System.out.println("!!!!!!!!!!!!!!!" + time);
		matrix[a][b] = time;
		matrix[b][a] = time;
		CaluShortPath(fakeDelay,RealDelay);
	}
	
	public int[][] getMatrix(){
		return this.matrix;
	}
	
	public ShortPath(int size, HistoryObj _matrix[][], int label){
		matrix = new int[size][size];
		originalMatrix = new int[size][size];
		fakeDelay = new double[size][size];
		RealDelay = new double[size][size];
		originalDelay = new double[size][size];
		this.label = label;
		this.size = size;
		for(int i =0;i<size;i++){
			for(int j=0;j<size;j++){
				matrix[i][j] = _matrix[i][j].getTimes();
				originalMatrix[i][j] = _matrix[i][j].getTimes();
			}
		}
		CaluShortPath(originalDelay,null);
		CaluShortPath(fakeDelay,RealDelay);
	}
	
	public void CaluShortPath(double[][] delayMatrix, double[][] realMatrix){
		System.out.println("----------" + this.label + "-----------");
		double[][] prv= new double[size][size];
		double[][] prv2= new double[size][size];
		double[][] cur= new double[size][size];
		double[][] cur2= new double[size][size];
		for(int i =0;i<size;i++){
			for(int j =0;j<size;j++){
				prv[i][j] = matrix[i][j];
				prv2[i][j] = originalMatrix[i][j];
				cur[i][j] = matrix[i][j];
				cur2[i][j] = originalMatrix[i][j];
				//System.out.print(matrix[i][j] +" ");
			}
			//System.out.println();
		}
		//System.out.println("----------new delay------------");
		for(int i =1;i<size;i++){
			for(int x = 0; x<size;x++){
				for(int y = 0;y<size;y++){
					//if(x!=y){
						double min = prv[x][y];
						double min2 = prv2[x][y];
						for(int j = 0;j<size;j++){
							double temp = (double)1.0/(double)prv[x][j] + ((double)1.0/(double)this.matrix[j][y]);
							//if(x==2&&j==2&&y==2) System.out.println(temp);
							double temp2 = (double)1.0/(double)prv2[x][j] + ((double)1.0/(double)this.originalMatrix[j][y]);
							if(min>temp){
								min = temp;
								min2 = temp2;
							}
						}
						cur[x][y] = min;
						cur2[x][y] = min2;
					//}
				}
			}
			for(int z =0;z<size;z++){
				for(int q = 0;q<size;q++){
					prv[z][q] = cur[z][q];
					prv2[z][q] = cur2[z][q];
				}
			}
		}
		for(int i =0;i<size;i++){
			for(int j =0;j<size;j++){
				delayMatrix[i][j] = cur[i][j];
				if(realMatrix!=null)
					realMatrix[i][j] = cur2[i][j];
				//System.out.print(df2.format(cur[i][j]) + " ");
			}
			//System.out.println();
		}
		System.out.println("----------contact table------------");
		for(int i =0;i<size;i++){
			for(int j =0;j<size;j++){
				System.out.print(df2.format(matrix[i][j]) + " ");
			}
			System.out.println();
		}
		System.out.println("----------real delay------------");
		double ave = 0;
		for(int i =0;i<size;i++){
			for(int j =0;j<size;j++){
				System.out.print(df2.format(cur2[i][j]) + " ");
				ave +=cur2[i][j];
			}
			System.out.println();
		}
		System.out.println("Ave is : " + ave / (double)(size *size));
	}
}
