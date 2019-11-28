import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class modifiedHuff {
	static ArrayList<node> info= new ArrayList<node>();
	static HashMap<String,String> code= new HashMap<String,String>();
	static ArrayList<String> Others=new ArrayList<String>();
	
	
	public static void encode() {
		makeCode(info);
		
		
		
	}
	
	public static void makeCode(ArrayList<node> n) {
		if(n.size()==2) {
			n.get(0).code="1";
			n.get(1).code="0";
			for(int i=0;i<2;++i) {
				if(n.get(i).symbol.length()>1 && !n.get(i).symbol.equals("other")) continue;
				code.put(n.get(i).symbol, n.get(i).code);
				
				code.put(n.get(i).code, n.get(i).symbol);
				
				System.out.println(n.get(i).symbol + " "+n.get(i).code);
			
			}
			
			return;
		}
		
		ArrayList<node> tmp = new ArrayList<node>();
		for(int i=0;i<n.size()-2;++i) {
			tmp.add(n.get(i));
		}
		node extra = new node();
		extra.freq=n.get(n.size()-1).freq + n.get(n.size()-2).freq;
		extra.symbol=n.get(n.size()-1).symbol+"+"+ n.get(n.size()-2).symbol;
		tmp.add(extra);
		
		n.get(n.size()-1).parent=extra;
		n.get(n.size()-2).parent=extra;
		
		Collections.sort(tmp);

		makeCode(tmp);


		for(int i=1;i<3;++i) {
		n.get(n.size()-i).code =n.get(n.size()-i).parent.code+(i-1);
		
		if(n.get(n.size()-i).symbol.length()==1 ) {
				code.put(n.get(n.size()-i).code,n.get(n.size()-i).symbol );
		
				code.put(n.get(n.size()-i).symbol,n.get(n.size()-i).code );
				System.out.println(n.get(n.size()-i).symbol + " " +n.get(n.size()-i).code);
			}
		}
		for(int i=1;i<3;++i) {
			if(n.get(n.size()-1).symbol.equals("other")) {
				for(int j=0;j<Others.size();++j) {
					node newN=new node();
					newN.symbol=Others.get(j);
					newN.code=n.get(n.size()-i).code+code.get(newN.symbol);
					System.out.println(newN.symbol+" "+newN.code);
					code.put(newN.code, newN.symbol);
					code.put(newN.symbol, newN.code);
				}
				return;
			}
		}
		
		return;
	}
	
	public static String decode(String input) {
		
		String symb="",res="";
		for(int i=0;i<input.length();++i) {
			symb+=input.charAt(i);
			if(code.get(symb)!=null) {
				if(code.get(symb).equals("other") ) {
					for(int j=1;j<4;++j) {
						symb+=input.charAt(i+j);
					}
					i+=2;
				}
				else if(code.get(symb).length()>1) continue;
				res+=code.get(symb);
				symb="";
			}
			
		}
		return res;
	}
	
	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		
		int counter=0;
		
		int number;
		number=sc.nextInt();
		
		for(int i=0;i<number;++i) {
			node n = new node();
			
			char symbChar=sc.next().charAt(0);
			n.symbol+=symbChar;
			n.freq=sc.nextInt();
			
			if(n.freq >1) {
				info.add(n);
			}
			else if(n.freq==1) {
				counter++;
				Others.add(n.symbol);
			}
			if(i==number-1) {
				n.freq=counter;
				n.symbol="other";
				info.add(n);
			}
		}
		
		
		
		code.put("d", "000");
		code.put("f","111");
		Collections.sort(info);
		Collections.sort(Others);
		encode();
		
		
		String s="000000000010101010101010111111111111111110000110111";
		System.out.println(decode(s));
	}
}