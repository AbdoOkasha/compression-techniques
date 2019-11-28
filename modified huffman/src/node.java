
public class node implements Comparable<node>{
	public node parent;
	public String symbol="";
	public String code="";
	public int freq=0;
	@Override
	public int compareTo(node n) {
		if(this.freq==n.freq) return 0;
		return (this.freq < n.freq)? 1:-1;
	}
	
}
