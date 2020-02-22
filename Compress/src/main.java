import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class main {

	static HashMap<String,Integer> stringMap = new HashMap<String,Integer>();
	static Scanner sc=new Scanner(System.in);
	static String comp="";
	
	static void compress(String words,String path, boolean state) throws IOException {
		
		BufferedWriter out = new BufferedWriter( 
                new FileWriter(path, state)); //true for append 
		
		for(int i=0,t=0;i<words.length();++i) {
			String chars="";
			chars+=words.charAt(i);
			
			if(stringMap.get(chars)==null) {
				out.write("0" + " " + chars);
				out.write("\n");
				stringMap.put(chars, t+1);
				++t;
			}
			
			else {
				int j=i+1;
				if(j<words.length()) {
					while(j<words.length()) {
						chars+=words.charAt(j);
						
						if(stringMap.get(chars)==null) { //p_   t
							int res;
							
							chars=chars.substring(0,chars.length()-1);
							
							res= stringMap.get(chars);
							
							out.write(res + " " + words.charAt(j));// + " " + chars+words.charAt(j)+" " +""+(t+1));

							out.write("\n");
							
							stringMap.put((chars+words.charAt(j)),t+1);
							
							i=j;
							++t;
							break;
						}
						++j;
						if(j==words.length()){
							out.write(stringMap.get(chars) + " -1");

							out.write("\n");
							i=j-1;
							break;
						}
					}
				}
				else if(j==words.length()) {
					out.write(stringMap.get(chars)+(""+" -1"));
					out.write("\n");
					i=j;
					break;
				}
				
				
			}
		}
		out.write((-2) + " " + (-2));
		out.newLine();
		out.close();
		stringMap.clear();
	}
	
	
	static HashMap<Integer,String> deMap=new HashMap<Integer,String>();
	
	public static String decompress(String path) throws IOException {
		String result="";
		
		File file = new File(path);

		Scanner sc= new Scanner (file);
		
		String input,values[] = new String[2];
		int num,t=1;
		while(sc.hasNext()) {
			input=sc.nextLine();
			int flag=0;
			/*
			 *split string at the first space only to handle spaces in the middle of string
			 */
			for(int i=0;i<input.length();++i) {
				if(input.charAt(i)==' ') {
					flag=i;
					break;
				}
			}
			values[0]=input.substring(0,flag);
			values[1]=input.substring(flag+1,input.length());
			num=Integer.parseInt(values[0]);
			
			if(num==0) {
				deMap.put(t, values[1]);
				result+=deMap.get(t);
			}
			else if(num==-2){
				result+="\n";
				deMap.clear();
				t=0;
			}
			else {
				if(values[1].equals("-1")) {
					result+=deMap.get(num);
					deMap.clear();
				}
				else {
					deMap.put(t, deMap.get(num)+values[1]);
					result+=deMap.get(t);
				}
			}
			++t;
		}
		sc.close();
		return result;
	}
	
}
