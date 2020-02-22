import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Scanner;

import javafx.util.Pair;


public class floatingArthmetic {
	HashMap<Character,charProp> charInfo = new HashMap<Character,charProp>();
	
	public void init(String input) {
		BigDecimal upper= new BigDecimal(0);
		for(int i=0;i<26;++i) {
			if(input.contains(""+(char)('a'+i))) {
				charProp tmp = charInfo.get((char)('a'+i));
				tmp.lower=upper;
				upper=upper.add(tmp.prop);
				tmp.upper=upper;
				charInfo.put((char)('a'+i),tmp);
			}
		}
	}
	
	public Pair<String,Integer> readInput(Scanner sc,String path,boolean encoding) throws IOException {
		int n =sc.nextInt();
		char symb='\u0000';
		String data="",chars="";
		data+=n;
		data+="\n";
		for(int i=0;i<n;++i) {
			charProp prob= new charProp();
			data+=symb=sc.next().charAt(0);
			chars+=symb;
			data+=" ";
			data+=prob.prop=sc.nextBigDecimal();
			data+="\n";
			charInfo.put(symb, prob);
		}
		sc.nextLine();
		if(!encoding) {
			n=sc.nextInt();
			sc.nextLine();
		}
		String input=sc.nextLine();
		init(chars);
		writeToFile(path,data,false);
		Pair<String,Integer> out = new Pair<String,Integer>(input,n);
		if(encoding) writeToFile(path,input.length()+"\n",true);
		return out;
	}
	
	public BigDecimal encode(String path) throws IOException {
		Scanner sc = new Scanner (new File(path));
		String input=readInput(sc,path,true).getKey();
		
		char prev= input.charAt(0),current;
		for(int i=1;i<input.length();++i) {
			current=input.charAt(i);
			charProp prevData= new charProp();
			prevData.equal(charInfo.get(prev));
			changeUpper_Lower(prevData, current);
			prev=current;
		}
		char lastChar=input.charAt(input.length()-1);
		BigDecimal lower=charInfo.get(lastChar).lower;
		BigDecimal upper=charInfo.get(lastChar).upper;
		
		BigDecimal result = generateResult(lower,upper);
		
		writeToFile(path,result.toString(),true);
		sc.close();
		return result;
	}
	
	public void writeToFile(String path , String result,boolean flag) throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter(path,flag));
		file.write(result);
		file.close();
	}
	
	public String decode(String path) throws IOException {
		Scanner sc = new Scanner (new File(path));
		Pair<String,Integer> out =readInput(sc,path,false);
		int symbolCounter = out.getValue();
		String data=out.getKey();
		BigDecimal code = new BigDecimal(data);
		String result="";
		BigDecimal input = code;
		boolean firstChar=true;
		charProp prev=new charProp();
		char prevKey='\u0000';
		for(int j=0;j<symbolCounter;++j) {
			char currentKey=getEquivlentChar(input);
			charProp current= charInfo.get(currentKey);
				if(firstChar) {
					firstChar=false;
					prev=current;
					prevKey=currentKey;
					result+=currentKey;
					input=changeCode(prev,code);
				}
				else {
					charProp prevData= new charProp();
					prevData.equal(charInfo.get(prevKey));
					changeUpper_Lower(prevData, currentKey);
					current=charInfo.get(currentKey);
					prev=current;
					prevKey=currentKey;
					input=changeCode(current,code);
					result+=prevKey;
				}
			}
		writeToFile(path,result,true);
		return result;
	}
	
	public char getEquivlentChar(BigDecimal input) {
		for(HashMap.Entry<Character,charProp> val:charInfo.entrySet()) {
			charProp current= val.getValue();
			char currentKey=val.getKey();
			if(current.lower.compareTo(input)<1 && current.upper.compareTo(input)>-1) {
				return currentKey;
			}
			
		}
		return '\u0000';
		
		
	}
	
	public void changeUpper_Lower(charProp prev , char current) {
		charProp currentData = charInfo.get(current);
		currentData=add(prev,currentData);
		
		charInfo.put(current, currentData);
	}
	
	public charProp add(charProp prev ,charProp current) {
		charProp tst= new charProp();
		tst.equal(prev);
		current.lower=calculate(tst, current.lower);
		current.upper=calculate(tst, current.upper);
		return current;
	}
	
	public BigDecimal calculate(charProp last, BigDecimal current) {
		BigDecimal lower=new BigDecimal(last.lower.toString());
		BigDecimal upper=new BigDecimal(last.upper.toString());
		BigDecimal mul=upper.subtract(lower);
		mul=mul.multiply(current);
		lower=lower.add(mul);
		return lower;
	}
	public BigDecimal generateResult (BigDecimal lower, BigDecimal upper) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal tow = new BigDecimal(2);
		BigDecimal lastAdded = new BigDecimal(0.5);
		while(result.compareTo(upper) >0 || result.compareTo(lower)<0) {
			if(result.compareTo(lower)==-1) {
				result=result.add(lastAdded);
				lastAdded=lastAdded.divide(tow);
			}
			
			if(result.compareTo(upper)==1) {
				result=result.subtract(lastAdded.multiply(tow));
			}
		}
		return result;
	}
	
	public BigDecimal changeCode(charProp state , BigDecimal code) {
		BigDecimal upper=state.upper;
		BigDecimal lower=state.lower;
		
		BigDecimal left,right,result;
		left=code.subtract(lower);
		right=upper.subtract(lower);
	
		
		result= left.divide(right,MathContext.DECIMAL128);
		return result;
	}
	
	public static void main(String []args) throws IOException {
		
	}
	
}
