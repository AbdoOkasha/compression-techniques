import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;




public class HuffmanJPEG {

	String data;
	TreeMap<String,Integer> table = new TreeMap<String,Integer>();
	TreeMap<String,String> code= new TreeMap<String,String>();
	
	Comparator<Pair<String,Integer>> compareBy = new Comparator<Pair<String,Integer>>(){

		@Override
		public int compare(Pair<String, Integer> p1, Pair<String, Integer> p2) {
			if(p1.getValue()==p2.getValue()) return 0;
			else return (p1.getValue()<p2.getValue())?1:-1;
		}
		
		
	};
	
	
	public String encode(String data,String path) throws IOException {
		this.data=data;
		data=data.replaceAll(",", " ");
		String[] info=data.split(" ");
//		System.out.println("info length "+info.length);
//		
//		for(int i=0;i<info.length;++i) System.out.println(info[i]);
		
		ArrayList<String> res=split(info);
//		System.out.println(res.size());
		res=getDescriptor(res);
		fillTable(res);
		
		ArrayList<Pair<String,Integer>> val= new ArrayList<Pair<String,Integer>>();
		for(Map.Entry<String, Integer> mp :table.entrySet()) {
//			System.out.println(mp.getKey() +" => " +mp.getValue());
			String symb= mp.getKey();
			int freq = mp.getValue();
			Pair<String,Integer> p =new Pair<String, Integer>(symb, freq);
			val.add(p);
			
		}

		Collections.sort(val,compareBy);
		
		huffmanEncoding(val);
		
		String out=writoToFile(res,path);
		code.clear();
		table.clear();
		return out;
	}
	
	public String decode(String path) throws IOException {
		ArrayList<Pair<String,String>> info =readFromFile(path);
		String out ="";
		for(Pair<String,String> p :info) {
			String tab=p.getKey();
			String num=p.getValue();
			System.out.println(tab+" " +num);
			
			boolean neg=false;
			if(num.startsWith("0")) {
				neg=true;
				num=convertToOnes(num);
//				System.out.println(num);
			}
			int n=Integer.parseInt(num,2);
			if(neg) n*=-1;
//			System.out.println(n+ " " +tab);
			tab=code.get(tab);
			
			tab=tab.substring(0,tab.indexOf('/'));
//			System.out.println(tab);
			int zero=Integer.parseInt(tab);
			
			for(int i=0;i<zero;++i) {
				out+="0,";
			}
			out+=n;
			out+=",";
		}
		
		BufferedWriter bf = new BufferedWriter(new FileWriter(path));
		bf.write(out);
		bf.close();
		code.clear();
		table.clear();
		return out;
	}
	
	
	private ArrayList<Pair<String, String>> readFromFile(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path));
		String tableData= sc.nextLine();
		String compressed = sc.nextLine();
		
//		System.out.println(tableData+"\n" + compressed);
		String []tableInfo= tableData.split(" ");
		for(int i=0;i<tableInfo.length;++i) {
			String ans[]=tableInfo[i].split(",");
//			System.out.println(ans[1] +" " + ans[0]);
			code.put(ans[1], ans[0]);
		}
		ArrayList<Pair<String,String>> in=readCodeTable(compressed);
		sc.close();
		return in;
	}

	private ArrayList<Pair<String,String>> readCodeTable(String compressed) throws FileNotFoundException {
		String res="";
		ArrayList<Pair<String,String>> chars = new ArrayList<Pair<String,String>>();
		for(int i=0;i<compressed.length();++i) {
			res+=compressed.charAt(i);
			if(code.get(res)!=null) {
				String siz= code.get(res);
				siz=siz.substring(siz.indexOf('/')+1);
				boolean flag=false;
				if(siz.startsWith("0")) {
					flag=true;
					siz=convertToOnes(siz);
				}
				int num = Integer.parseInt(siz);
				if (flag) num*=-1;
				siz="";
				for(int j=0;j<num;++j) {
					siz+=compressed.charAt(j);
				}
				i+=num;
				Pair<String,String> p = new Pair<String,String>(res,siz);
				chars.add(p);
				res="";
				System.out.println(p.getKey() + " " + p.getValue());
			}
		}
		return chars;
	}

	private String writoToFile(ArrayList<String> res,String path) throws IOException {
//		System.out.println(res.size());
		String out=writeTable(path);
		BufferedWriter bf = new BufferedWriter(new FileWriter(path,true));
		for(int i=0;i<res.size();++i) {
			String tmp = res.get(i).substring(0,res.get(i).indexOf('/')+1);
			int last=res.get(i).substring(res.get(i).indexOf('/')+1).length();
			tmp+=last;
//			System.out.println(code.get(tmp)+","+res.get(i).substring(res.get(i).indexOf('/')+1));
			bf.write(code.get(tmp)+res.get(i).substring(res.get(i).indexOf('/')+1));
			out+=(code.get(tmp)+res.get(i).substring(res.get(i).indexOf('/')+1));
		}
		bf.newLine();
		bf.close();
		out+='\n';
		return out;
	}

	private String writeTable(String path) throws IOException {
		String out="";
		BufferedWriter bf = new BufferedWriter(new FileWriter(path));
		for(Map.Entry<String, String>mp : code.entrySet()) {
			if(mp.getKey().indexOf('/')==mp.getKey().lastIndexOf('/')) {
				bf.write(mp.getKey()+","+mp.getValue()+" ");
				out+=(mp.getKey()+","+mp.getValue()+" ");
			}
		}
		bf.newLine();
		bf.close();
		out+="\n";
		return out;
	}

	private void fillTable(ArrayList<String> res) {
		for(int i=0;i<res.size();++i) {
			int idx=res.get(i).indexOf('/');
			String val=res.get(i).substring(0,idx+1);
			String size=res.get(i).substring(idx+1);
			val+=size.length();
//			System.out.println(val);
			if(table.containsKey(val)) {
				table.replace(val, table.get(val)+1);
			}
			else
				table.put(val, 1);
		}
		
	}

	private ArrayList<String> getDescriptor(ArrayList<String> res) {
		ArrayList<String> ans= new ArrayList<String>();
		for(int i=0;i<res.size();++i) {
			String val=res.get(i);
			val=getLastZero(val);
//			System.out.println(val);
			ans.add(val);
		}
		return ans;
	}

	private String getLastZero(String val) {
		int size=0;
		for(int i=0;i<val.length();++i) {
			if(val.charAt(i)!='0') {
				size=i;
				break;
			}
		}
		val=val.substring(size);
		int num=Integer.parseInt(val);
		boolean flag=num<0;
		if(flag) num*=-1;
		String res=Integer.toBinaryString(num);
		if(flag) res=convertToOnes(res);
		val=size+"/"+res;

		return val;
	}

	private String convertToOnes(String res) {
		res=res.replace('1', '2');
		res=res.replace('0', '1');
		res=res.replace('2', '0');
		
		return res;
	}

	public ArrayList<String> split(String[] info) {
		ArrayList<String>res = new ArrayList<String>();
		String val="";
		for(int i=0;i<info.length;++i) {
			val+=info[i];
			if(!info[i].equals("0")) {
				res.add(val);
				val="";
			}
		}
		
//		for(String i:res) System.out.println(i);
		return res;
	}
	
	public void huffmanEncoding(ArrayList<Pair<String,Integer>> val) {
		if(val.size()==2) {
			code.put(val.get(0).getKey(), "0");
			code.put(val.get(1).getKey(), "1");
			
//			System.out.println(code.get(val.get(0).getKey())+ "   " +val.get(0));
//			System.out.println(code.get(val.get(1).getKey())+ "   " +val.get(1));
			return;
		}
		ArrayList<Pair<String,Integer>> tmp = new ArrayList<Pair<String,Integer>>();
		
		for(int i=0;i<val.size()-2;++i) {
			tmp.add(val.get(i));
		}
		
		String n="";
		int siz=val.size();
//		System.out.println(val.get(siz-1).getKey()+ " " +val.get(siz-2).getKey());
		Pair<String,Integer>p = new Pair<String, Integer>(val.get(siz-1).getKey()+val.get(siz-2).getKey(),table.get(val.get(siz-2).getKey())+table.get(val.get(siz-1).getKey()));
		n=p.getKey();
		tmp.add(p);
		table.put(tmp.get(tmp.size()-1).getKey(), tmp.get(tmp.size()-1).getValue());

		
		Collections.sort(tmp,compareBy);
		
		huffmanEncoding(tmp);
		
		for(int i=2;i>0;--i) {
			code.put(val.get(val.size()-i).getKey() , code.get(n)+(2-i));
//			System.out.println(code.get(val.get(val.size()-i).getKey()) + " " +val.get(val.size()-i).getKey());
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		
		HuffmanJPEG tst = new HuffmanJPEG();
		String data="-2,0,0,2,0,0,3,2,0,1,0,0,-2,0,-1,0,0,1,0,0,-1,";
		String path="E:\\Downloads\\FCI\\Multi\\tst.txt";
		tst.encode(data, path);
//		System.out.println(tst.decode(path));

	}

}
