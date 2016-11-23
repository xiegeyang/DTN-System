package Geyang;

public class ShortPath {
	private int matrix[][];
	private int originalMatrix[][];
	int originalDelay[][];
	int fakeDelay[][];
	int RealDelay[][];
	int size;
	
	public void setMatrix(int a , int b, int time){
		matrix[a][b] = time;
		matrix[b][a] = time;
		CaluShortPath(fakeDelay,RealDelay);
	}
	
	public int[][] getMatrix(){
		return this.matrix;
	}
	
	public ShortPath(int size, HistoryObj _matrix[][]){
		matrix = new int[size][size];
		originalMatrix = new int[size][size];
		fakeDelay = new int[size][size];
		RealDelay = new int[size][size];
		originalDelay = new int[size][size];
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
	
	public void CaluShortPath(int[][] delayMatrix, int[][] realMatrix){
		System.out.println("--------------------------------");
		int[][] prv= new int[size][size];
		int[][] prv2= new int[size][size];
		int[][] cur= new int[size][size];
		int[][] cur2= new int[size][size];
		for(int i =0;i<size;i++){
			for(int j =0;j<size;j++){
				prv[i][j] = matrix[i][j];
				prv2[i][j] = originalMatrix[i][j];
				cur[i][j] = matrix[i][j];
				cur2[i][j] = originalMatrix[i][j];
				System.out.print(matrix[i][j] +" ");
			}
			System.out.println();
		}
		System.out.println("----------Security Version------------");
		for(int i =1;i<size;i++){
			for(int x = 0; x<size;x++){
				for(int y = 0;y<size;y++){
					if(x!=y){
						int min = Integer.MAX_VALUE;
						int min2 = Integer.MAX_VALUE;
						for(int j = 0;j<size;j++){
							int temp = prv[x][j] + this.matrix[j][y];
							int temp2 = prv2[x][j] + this.originalMatrix[j][y];
							if(min>temp){
								min = temp;
								min2 = temp2;
							}
						}
						cur[x][y] = min;
						cur2[x][y] = min2;
					}
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
				System.out.print(cur[i][j] + " ");
			}
			System.out.println();
		}
	}
}
