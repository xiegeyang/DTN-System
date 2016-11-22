package Geyang;

public class ShortPath {
	private int matrix[][];
	int DynamicMatrix[][];
	int size;
	
	public void setMatrix(int a , int b, int time){
		matrix[a][b] = time;
		matrix[b][a] = time;
		CaluShortPath();
	}
	
	public int[][] getMatrix(){
		return this.matrix;
	}
	
	public ShortPath(int size, HistoryObj _matrix[][]){
		matrix = new int[size][size];
		DynamicMatrix = new int[size][size];
		this.size = size;
		for(int i =0;i<size;i++){
			for(int j=0;j<size;j++){
				matrix[i][j] = _matrix[i][j].getTimes();
			}
		}
		CaluShortPath();
		
	}
	
	public void CaluShortPath(){
		System.out.println("--------------------------------");
		int[][] prv= new int[size][size];
		int[][] cur= new int[size][size];
		for(int i =0;i<size;i++){
			for(int j =0;j<size;j++){
				prv[i][j] = matrix[i][j];
				cur[i][j] = matrix[i][j];
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
						for(int j = 0;j<size;j++){
							int temp = 0;
							if(prv[x][j] == -1 || matrix[j][y] == -1){
								temp = Integer.MAX_VALUE;
							}
							else{
								temp = prv[x][j] + this.matrix[j][y];
							}
							if(min>temp) min = temp;
						}
						if(min == Integer.MAX_VALUE) cur[x][y] = -1;
						else cur[x][y] = min;
					}
				}
			}
			for(int z =0;z<size;z++){
				for(int q = 0;q<size;q++){
					prv[z][q] = cur[z][q];
				}
			}
		}
		for(int i =0;i<size;i++){
			for(int j =0;j<size;j++){
				DynamicMatrix[i][j] = cur[i][j];
				System.out.print(cur[i][j] + " ");
			}
			System.out.println();
		}
	}
}
