
import java.io.*;
/*import java.util.Scanner;
import java.util.Stack; 
import java.util.Queue;
import java.util.LinkedList;
*/
import java.util.*;
import java.util.HashMap;
import java.sql.*;

public class Main{
	public static void main(String[] args) throws Exception{

		 	Hashmap h = new Hashmap();
		 	h.hashmap();
			String url="jdbc:mysql://localhost:3306/hashmap";
			String Uname="root";
			String Password="$iddh@13M";
			String query="select cname, id from components where id=";
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con =DriverManager.getConnection(url,Uname,Password);
			Statement st=con.createStatement();
			
		//	System.out.println(name);
	//		st.close();
//			con.close();
			
			 int i, j = -1, k, number = 0;
			
			
			
			
        Circuit g = new Circuit(0);
        System.out.print(g.toString());
       
        try {  
            // Create f1 object of the file to read data  
            File f1 = new File("src/info");    
            Scanner dataReader = new Scanner(f1);  
            String fileData = dataReader.nextLine();
            number = Integer.parseInt(fileData);
            System.out.println(number);
            g = new Circuit(number);
            for(i = 0; i < number; i++){
            	k = 0;
            	fileData = dataReader.nextLine();
           	for(j = 0; j < number*2-1;j++){
           		
           		if(fileData.charAt(j) == '0')
           			k++;
           		else if(fileData.charAt(j) == '1'){
           			g.addEdge(i, k);
           			k++;
           		}
           		else
           			continue;
           			
           	} 	
        	}
        dataReader.close();
      }
      catch (FileNotFoundException exception) {  
            System.out.println("Unexcpected error occurred!");  
            exception.printStackTrace();  
        }
        
        for(i = 1; i <= number; i++) {
        	String str1 = Integer.toString(i);
			String query1 = query + str1;
			ResultSet rs=st.executeQuery(query1);
			rs.next();
			String name=rs.getString("cname");
			int idey = rs.getInt("id");
	//		int ID = Integer.parseInt(idey);
			h.insert(name, idey-1);
		}  
        st.close();
		con.close();
     System.out.print(g.toString()); 
     g.segregateComponents();
     boolean b;
//     g.display();
//     g.chromaticNumber();
     	if(args[0].equals("DFS")){
     		k = h.map(args[1]);
     		System.out.print(g.DFS(k));
    		}
    		
    		if(args[0].equals("BFS")){
     		k = h.map(args[1]);
     		System.out.print(g.BFS(k));
    		}
    		
    		if(args[0].equals("components")){
     	//	k = h.map(args[1]);
     		System.out.print(g.getComponents());
    		}
    		
    		if(args[0].equals("chromatic_number")){
     	//	k = h.map(args[1]);
     		System.out.print(g.chromaticNumber());
    		}
    		
    		if(args[0].equals("segregate")){
     	//	k = h.map(args[1]);
     		g.segregateComponents();
     		g.display();
    		}
    		
    		if(args[0].equals("planarity")){
     	//	k = h.map(args[1]);
     		g.segregateComponents();
     		b = g.isPlaner();
     	//	System.out.print(b);
     		if(b)
     			System.out.print("\n Circuit possible");
     		else
     			System.out.print("\n Circuit not possible");
    		}
    		h.displaymap();
     }
}


class Circuit{
	
	protected boolean adjMatrix[][];
	protected boolean graphComponents[][][];
	protected int dimensions[]; 
	protected int numVertices;
	protected int components;
	protected int chnum[];
	protected int max;
	
	public Circuit(int num){
		this.numVertices = num;
		adjMatrix = new boolean[numVertices][numVertices];
	}
	
	public void addEdge(int i, int j){
		adjMatrix[i][j] = true;
		adjMatrix[j][i] = true;
	}
	
	public void removeEdge(int i, int j){
		adjMatrix[i][j] = false;
		adjMatrix[j][i] = false;
	}
	
	public String toString() {
	int i, j, k;
		StringBuilder s = new StringBuilder();
		for(i = 0; i < numVertices; i++){
			s.append(i+": ");
			for(j = 0; j < numVertices; j++){
				if(adjMatrix[i][j])
					k = 1;
				else
					k = 0;
				
				s.append(k + " ");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	public String DFS(int num){
		int[] visited = new int[numVertices];
		Stack <Integer> stk = new Stack<>();
		int i, j;
		for(i = 0; i < numVertices; i++)
			visited[i] = 0;
		stk.push(num);
		visited[num] = 1;
		StringBuilder s = new StringBuilder();
		s.append(num + " ");
		while(stk.empty() != true){
			int k = stk.peek();
			int flag = 0;
			for(j = 0; j < numVertices; j++){
				if(visited[j] == 0 && adjMatrix[k][j] == true){
					visited[j] = 1;
					flag = 1;
					s.append(j + " ");
					stk.push(j);
					break;
				}
			}
			if(flag == 0)
				stk.pop(); 
		}
		return s.toString(); 
	}
	
	public String BFS(int num){
		
		boolean[] visited = new boolean[numVertices];
		Queue <Integer> q = new LinkedList<>();
		int i, j;
		q.add(num);
		visited[num] = true;
		StringBuilder s = new StringBuilder();
		s.append(num + " ");
		while(q.size() != 0){
			i = q.poll();
			for(j = 0; j < numVertices; j++){
				if(visited[j] == true)
					continue;
				if(adjMatrix[i][j] == false)
					continue;
				else{
					q.add(j);
					visited[j] = true;
					s.append(j + " ");
				}
			}
		}
		return s.toString();
	}	
	
	public int getComponents(){
		
		boolean[] visited = new boolean[numVertices];
		int k = 0, components = 0;
		int num = 0;
		while(k < numVertices){
			Stack <Integer> stk = new Stack<>();
			int i, j;	
			
			for(i = 0; i < numVertices; i++){
				if(visited[i] == false){
					num = i;
					break;
				}
			}
			if(i > numVertices)
				break;
			visited[num] = true;
			stk.push(num);
			k++;
			while(stk.empty() != true){
				num = stk.peek();
				int flag = 0;
				for(j = 0; j < numVertices; j++){
					if(visited[j] == false && adjMatrix[num][j] == true){
						flag = 1;
						visited[j] = true;
						stk.push(j);
						k++;
						break;
					}
				}
				if(flag == 0)
					stk.pop();
					
			}
			
			components++;
			
		}
		
		return components;
	}
	
	public void segregateComponents(){
	
		int d_3 = getComponents();
		int[][] visited = new int[3][numVertices];
		int k = 0, num = 0, components = 0;
		dimensions = new int[d_3];
		int c;
		for(c = 0; c < d_3; c++)
			dimensions[c] = 0;
		
		while(k < numVertices){
		
			int i, j, start = 0;
			for(i = 0; i < numVertices; i++){
				if(visited[0][i] == 0){
					start = i;
					break;
				}
			}
			Stack <Integer> stk = new Stack<>();
			stk.push(start);
			visited[0][start] = 1;
			visited[1][start] = components;
			dimensions[components]++;
			k++;
			while(stk.empty() != true){
				num = stk.peek();
				int flag = 0;
				for(j = 0; j < numVertices; j++){
					if(visited[0][j] == 0 && adjMatrix[num][j] == true){
						flag = 1;
						stk.push(j);
						visited[0][j] = 1;
						visited[1][j] = components;
						dimensions[components]++;
						k++;
						break;
					}
				}
				if(flag == 0)
					stk.pop();
			}
			components++;	
		}
		
		graphComponents = new boolean[d_3][][];
	//	System.out.print(components);
		
		for(int i = 0; i < d_3; i++)
			graphComponents[i] = new boolean[dimensions[i]][dimensions[i]];
		
		for(c = 0; c < d_3; c++){
			int count = 0;
			for(int j = 0;j < numVertices; j++){
				if(c == visited[1][j]){
					visited[2][j] = count;
					count++;
				}
			}
		}
		int n1, n2, n3;
		for(c = 0; c < numVertices; c++){
			for(int j = 0; j < numVertices; j++){
				if(adjMatrix[c][j] == false)
					continue;
				else{
					n1 = visited[1][c];
					n2 = visited[2][c];
					n3 = visited[2][j];
					graphComponents[n1][n2][n3] = true;
					graphComponents[n1][n3][n2] = true;
				}
			}
		}
		
	//	g.display();
	}
	
	public void display() {
		for(int i=0;i<getComponents();i++)
		{
			for(int j=0;j<dimensions[i];j++)
			{
				for(int k=0;k<dimensions[i];k++) {
					if(graphComponents[i][j][k]==true)
						System.out.print(1+" ");
					else
						System.out.print(0+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public int chromaticNumber(){
		
		int components = getComponents();
		chnum = new int[components];
		
		int i, j, k;
		
		for(i = 0;i < components; i++)
			chnum[i] = 0;
		
		for(k = 0; k < components; k++){
			
			int[] vertexColor = new int[dimensions[k]];
			for(i = 0; i < dimensions[k] ; i++)
				vertexColor[i] = -1;
			vertexColor[0] = 0;
			for(i = 1; i < dimensions[k]; i++){
				boolean[] color = new boolean[dimensions[k]];
				for(j = 0; j < dimensions[k]; j++){
					if(graphComponents[k][i][j] == true && vertexColor[j] != -1)
						color[j] = true;	
				}
				for(j = 0; j < dimensions[k]; j++){
					if(color[j] == false){
						vertexColor[i] = j;
						break;
					}
				}
			}
			int max = vertexColor[0];
			for(i = 0; i < dimensions[k]; i++)
				if(vertexColor[i] > max)
					max = vertexColor[i];
			chnum[k] = max + 1;
			System.out.print(chnum[k] + "\n");
		}
		
		int chr = chnum[0];
		for(i = 0;i < components; i++){
			if(chnum[i] > chr)
				chr = chnum[i];
		}
		
		return chr;
	}
	
	public boolean isPlaner(){
	
		int chromatic_number = chromaticNumber();
		if(chromatic_number < 5)
			return true;
		return false;
		
	} 
	
	public void findIC(){
		
		max = 0;
		int i, j, k, components = getComponents();
		segregateComponents();
		int[] ic_number = new int[components+5];
		for(i = 0; i < components; i++)
			ic_number[i] = 0;
		for(k = 0; k < components; k++){
			for(j = 0; j < dimensions[k]; j++){
				for(i = 0; i < dimensions[k]; i++)  { 
					if(dimensions[k] < 3)
						break;
					int[] array = new int[dimensions[k]];
					ic_number[j] = ic_number[j] +	combinations(j,dimensions[k]-1, i+1, array, k, i, 0);
			
				}		
			}
		}
		
		for(i = 0; i < components; i++){
			if(ic_number[i] > 0){
				System.out.print("IC present.\n");
				return;
			}
		}
		
		System.out.print("IC absent.\n");
	
	}
		
	public int combinations(int start, int end, int level, int[] array, int k, int max_arr_size, int arr_size){
	
		if(start > end)
			return 0;
		
		int stat = 0;
		if(level == 0 || arr_size >= 3){
			// find combinations
			stat = isComplete(k, array, arr_size);
			return stat;
		}
	
		array[arr_size++] = start;
		int i, j, max = 0;
//		for(i = 0; i < arr_size; i++)
//			System.out.print(array[i] + " ");
//		System.out.print("\n");
		
		for(i = start+1; i <= end; i++){
			k = combinations(i, end, level - 1, array, k, max_arr_size, arr_size);
			if(k > max)
				max = k;
		} 
	
		return max;
	}
	
	public int isComplete(int k, int[] array, int size){
	
		int i, j;
		if(size < 3)
			return 0;
		for(i = 0; i < size; i++){
			for(j = 0;j < size; j++){
				int a = array[i];
				int b = array[j];
				if(a == b)
					continue;
				if(graphComponents[k][a][b] != true){
			//		System.out.print("IC absent " + size +"\n");
					return 0;
				}
			}
		}
	//	*n = true;
		
		return 1;
	}
	
	public boolean isCktPossible(){
		
		int components = getComponents(), i, j, k;
		for(k = 0; k < components; k++){
			// target graph = graphComponents[k][][] 
			for(i = 0; i < dimensions[k] ;i++){
				int one_count = 0;
				for(j = 0; j < dimensions[k]; j++){
					if(i == j)
						continue;
					if(graphComponents[k][i][j])
						one_count++;
				}
				if(one_count <= 1)
					return false;
			} 	
	
		}
		
		return true;
		
	}
	
	public boolean isSerial(int num){
		
		int start = num;
		int i, j,k = 0;
		for(i = 0; i < numVertices; i++)
			if(adjMatrix[num][i] == true)
				break;
		int prev = start, curr = i;
		while(k < numVertices){
			if(curr == start)
				return true;
			int count = 0, I = 0;
			for(i = 0; i < numVertices; i++){
				if(adjMatrix[curr][i] == true && i != prev){
					count++;
					I = i;
				}
			}
			if(count != 1)
				return false;
			prev = curr;
			curr = I;
			k++;
		}
		
		return false;
	}
	
	
	
}


class Hashmap{

	private HashMap<String, Integer> map;


	public void hashmap(){
		
		map = new HashMap<>();
		
	/*	try{
			File f1 = new File("src/key");    
     		Scanner dataReader = new Scanner(f1);  
     	     String fileData = dataReader.nextLine();
      	     int number = Integer.parseInt(fileData);
      	     System.out.println(number);
      	     int i, j;
      	     for(i = 0; i < number; i++){
      	     	
      	     	String hole_id = dataReader.nextLine();
      	     	int id = Integer.parseInt(hole_id);
      	     	String holeName = dataReader.nextLine();
      	    // 	int name = Integer.parseInt(hole);	
      	     	map.put(holeName, id);
      	     
      	     }
      	     System.out.println(map);
		}
		catch (FileNotFoundException exception) {  
            System.out.println("Unexcpected error occurred!");  
            exception.printStackTrace();  
        }*/
	}

	public int map(String str){
	
		int a = map.get(str);
		return  a;

	}
	
	public void insert(String s, int id ) {
		map.put(s, id);
	}

	public void displaymap() {
		System.out.println(map);
	}

}








