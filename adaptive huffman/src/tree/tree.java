package tree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;


public class tree {
	
	
	
	private int NYT=-1,lastNum=256;
	node root= new node();
	private HashMap<Character,String> shortCode= new HashMap<Character,String>();
	private HashMap<Character,Integer> mp=new HashMap<Character,Integer>();
	private HashMap<String,Character> reversedShortCode=new HashMap<String,Character>();
	
	
	tree(){
		
	}

	node[] list;
	
	public String readFromFile(String path) throws FileNotFoundException{
		File file = new File(path);
		Scanner sc = new Scanner(file);
		String out="";
		while(sc.hasNextLine()) out+=sc.nextLine();

		sc.close();
		return out;
		
	}

	public void WriteToFile(String path,String out) throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter(path));
		file.write(out);
		file.close();
		
	}
	
	public void setup() {
		
		NYT=-1;

		mp.clear();
		shortCode.clear();
		reversedShortCode.clear();
		list = new node[257];
		
		for(int i=0;i<128;++i) {
			String ans= Integer.toBinaryString(i);
			while(ans.length()<8) {
				ans="0"+ans;
			}
			
			shortCode.put((char)i,ans);
			reversedShortCode.put(ans, (char)i);
		}
		root.number=256;
		list[256]=root;
		mp.put(root.symb,256);
	}
	
	public String compress(String input , String path) throws IOException {

		BufferedWriter file = new BufferedWriter(new FileWriter(path));
		String out="";
		
		setup();
		
		for(int i=0;i<input.length();++i) {
			if(NYT==-1) {
				
				node symbol=new node() , nyt= new node();
				
				file.write(shortCode.get(input.charAt(i)));
				out+=shortCode.get(input.charAt(i));
				
				
				perform(symbol , root.number-1 , nyt , root.number-2 , root,input.charAt(i));

				shortCode.replace(input.charAt(i), symbol.code);
				mp.put(input.charAt(i), NYT+1);
			}
			
			else {
				if(mp.containsKey(input.charAt(i))) {
					int loc=mp.get(input.charAt(i));
					file.write(shortCode.get(input.charAt(i)));
					out+=shortCode.get(input.charAt(i));
						

					moreSwap(loc);
				}
				else {
					file.write(list[NYT].code+shortCode.get(input.charAt(i))); 
					out+=list[NYT].code+shortCode.get(input.charAt(i));
					
					int symb=NYT-1 , nyt= NYT-2;
					node symbol=new node() , gab= new node();
					
					
					perform(symbol , symb, gab , nyt ,list[NYT] , input.charAt(i));

					shortCode.replace(input.charAt(i), symbol.code);
					mp.put(input.charAt(i), symb);

					int loc=list[NYT].parent.parent.number;

					moreSwap(loc);
				}
				
			}
		}
		file.close();
		return out;
	}
	
	public void perform(node symbol ,int symbolLoc, node nyt, int nytLoc,node parent ,char ch) {
		symbol.symb=ch;
		symbol.freq=1;
		symbol.number=symbolLoc;
		symbol.code=parent.code+"1";
		
		nyt.number=nytLoc;
		nyt.code=parent.code+"0";
		
		parent.right=symbol;
		parent.left=nyt;
		parent.freq=1;
		
		symbol.parent=parent;
		nyt.parent=parent;
		
		list[symbolLoc]= symbol;
		list[nytLoc]=nyt;
		
		NYT = nytLoc;
		
		
	}

	public int swapWith(node n1,node n2) {
	
		if(n1.freq>=n2.freq && n1.number<n2.number && n2 !=root && !n1.code.startsWith(n2.code) /*n2!=n1.parent*/) {

			return n2.number;
		}
		
		if(n2.left != null && n2.right != null ) {
			return Math.max(swapWith(n1,n2.left), swapWith(n1,n2.right));
		}
		
		
		
		return -1;
	}

	public void swap(node n1 , node n2) {
		
		
		int tmpFreq= n1.freq;
		char tmpSymb=n1.symb;
		node tmpLeft=new node();
		tmpLeft=n1.left;
		node tmpRight=new node();
		tmpRight=n1.right;
		
		
		n1.left=n2.left;
		n1.right=n2.right;
		n1.symb=n2.symb;
		n1.freq=n2.freq;
		
		
		n2.freq=tmpFreq;
		n2.symb=tmpSymb;
		n2.left=tmpLeft;
		n2.right=tmpRight;
		
		list[n1.number]=n1;
		list[n2.number]=n2;
		
		if(n1.left!=null) {
			n1.left.parent=n1;
			n1.right.parent=n1;
		}
		if(n2.left!=null) {
			n2.left.parent=n2;
			n2.right.parent=n2;
		}
		
		shortCode.put(n1.symb, n1.code);
		shortCode.put(n2.symb, n2.code);
		
		
		reversedShortCode.put(n1.code, n1.symb);
		reversedShortCode.put(n2.code, n2.symb);
		
		mp.replace(n1.symb,n1.number);
		mp.replace(n2.symb, n2.number);

		
		updateChildren(n1, n1.code);
		updateChildren(n2, n2.code);
	}
	
	public void updateChildren(node n,String code) {
		if(n.left != null || n.right !=null) {
			updateChildren(n.left, code+"0");
			updateChildren(n.right, code+"1");
		}
		n.code=code;
		list[n.number].code=code;
		shortCode.replace(n.symb, code);
		reversedShortCode.put(code,n.symb);

		return ;
	}
	
	public void tst(node n,BufferedWriter out) throws IOException {
		char t=(n.symb==' ')?'7':n.symb;
		out.write("node code "+n.code +" "+ t+" freq "+n.freq+ " "+n.number +"\n left "+n.left + "    right "+n.right+"\n");
		if(n.right != null) {
			tst(n.right,out);
		}
		if(n.left != null) {
			tst(n.left,out);
		}
		
	}

	public void moreSwap(int loc) {
		while(list[loc]!=root) {
			int res=swapWith(list[loc], root);
			if(res != -1) {
				
				swap(list[loc],list[res]);
				loc=res;
			}
			list[loc].freq++;
			loc=list[loc].parent.number;
			
		}
	}
	
	public String deCompress(String path) throws FileNotFoundException {
		
		File file = new File(path);
		Scanner sc = new Scanner (file);

		String input ="";
		while(sc.hasNextLine()) input += sc.nextLine();
		
		String out="";
		String lastNYT="";
		
		setup();
		String data="";
		
		for(int i=0;i<input.length();++i) {
			data+=input.charAt(i);
			
			if(reversedShortCode.get(data) != null && reversedShortCode.get(data)!= '\u0000') {
				char newChar=reversedShortCode.get(data);
				if(NYT==-1) {
						
					node symbol=new node() , nyt= new node();
					
					out+=newChar;
					
					
					perform(symbol , root.number-1 , nyt , root.number-2 , root,newChar);

					reversedShortCode.put(symbol.code, newChar);
					mp.put(newChar, NYT+1);
					
				
				}
				else {
					if(mp.containsKey(newChar)) {
						int loc=mp.get(newChar);
						out+=(newChar);
							

						moreSwap(loc);
					}
					
				}
				data="";
				lastNYT=list[NYT].code;
			}
			
			else if(data.equals(lastNYT)) {
				data="";
				for(int j=i+1;j<i+9;++j) {
					data+=input.charAt(j);
				}
				i+=8;
				char newChar=reversedShortCode.get(data);
				
				out+=newChar; 
				
				int symb=NYT-1 , nyt= NYT-2;
				node symbol=new node() , gab= new node();
				
				
				perform(symbol , symb, gab , nyt ,list[NYT] , newChar);
				
				reversedShortCode.put(symbol.code, newChar);
				mp.put(newChar, symb);

				int loc=list[NYT].parent.parent.number;

				moreSwap(loc);
				lastNYT=list[NYT].code;
				data="";
			}
		}
			return out;
			
	}
	
	
	
	public static void main(String[] args) throws IOException {
		
		String tt="01100010 0 01100101 00 01100100 100 00100000 000 01110011 1100 01110000 1000 01110010 00 0100 01100001 100 00 1111 001 001 100 1001 101 00 10001 100 011 111 010 110 00 1011 100 111 011 10100 01101111 110000 01101110 011 0000 110 100 110";
		String bo="01100010 0 01100101 00 01100100 100 00100000 000 01110011 0100 01110000 0000 01110010 00 0100 01100001 000 11 1011 1001 110 001 000 110 10 01001 110 000 011 111 111 01 0101 000 100 010 01000 01101111 011000 01101110 010 10000 11 111 100";
		tt=tt.replaceAll(" ", "");
		tree t= new tree();
		t.compress("bed spreaders spread spreads on beds","E:\\Downloads\\FCI\\Compress.txt");//+ "\n"+tt+"\n"+bo);
		System.out.println(t.deCompress("E:\\Downloads\\FCI\\Compress.txt"));
		
		
		
	}

}
