import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.Raster;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;

public class VectorQuantization {
	
	int vecSize=20,nVec=16;
	Raster img ;
	ArrayList<int[][]> list= new ArrayList<int[][]>();
	
	public VectorQuantization() {
		
	}
	
	public VectorQuantization(int vecSize,int nVec) {
		this.vecSize=vecSize;
		this.nVec=nVec;
	}
	
	public void encode(String path,String deCompress) throws IOException {
		BufferedImage img=readImage(path);
		Raster result=img.getData();
		
		for(int i=0;i<result.getWidth()-vecSize;i+=vecSize) {
			for(int j=0;j<result.getHeight()-vecSize;j+=vecSize) {
				
				int[][]tmp=new int[vecSize][vecSize];
				for(int k=i;k<i+vecSize;++k) {
					for(int s=j;s<j+vecSize;++s) {
//						System.out.println(k%vecSize+" "+s%vecSize);
						tmp[k%vecSize][s%vecSize]=result.getSample(k, s,0);
					}
				}
				list.add(tmp);
			}
		}
		
		
		
		double[][]tmp=getAverage(img);
		System.out.println("average ");
		print(tmp);
		split(tmp,deCompress);
//		readFromFile(deCompress);
		
	}
	
	public BufferedImage readImage(String path) throws IOException {
		File file = new File(path);
		
		BufferedImage img = ImageIO.read(file);
	
		
		BufferedImage result = new BufferedImage(
                img.getWidth(),
                img.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphic = result.createGraphics();
        graphic.drawImage(img, 0, 0, Color.WHITE, null);
		
        for (int i = 0; i < result.getHeight(); i++) {
            for (int j = 0; j < result.getWidth(); j++) {
                Color c = new Color(result.getRGB(j, i));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);
                Color newColor = new Color(
                        red + green + blue,
                        red + green + blue,
                        red + green + blue);
                result.setRGB(j, i,newColor.getRGB());
            }
        }
        ImageIO.write(result, "png",new File(file+"tst.png"));
		
        
		return result;
		
	}
	
	public double[][] getAverage(BufferedImage img) {
		double [][]average=new double[vecSize][vecSize];
		
		double mod=(img.getHeight()/(vecSize*1.0))*(img.getWidth()/(vecSize*1.0));
		System.out.println(mod);
		Raster raster=img.getData();
		this.img=raster;
		
		for(int i=0;i<img.getWidth()-vecSize;++i) {
			for(int j=0;j<img.getHeight()-vecSize;++j) {
				average[i%vecSize][j%vecSize]+=raster.getSampleDouble(i, j, 0)/mod;
			}
		}
		
		
		
		return average;
	}
	
	
	public void split(double [][]average,String path) throws IOException {
		ArrayList<int[][]> fSplit=new ArrayList<int[][]>();
		ArrayList<double[][]> sSplit=new ArrayList<double[][]>();
		
		int [][]tmpCeil  =getCeil(average);
		int [][]tmpFloor =getFloor(average);
		
		fSplit.add(tmpFloor);
		fSplit.add(tmpCeil);
		
//		System.out.println("flooR ");
//		print2(tmpFloor);
		
		ArrayList<Integer>[] arrangement=new ArrayList[nVec*2];
		do{
			ArrayList<Integer> arr[]=new ArrayList[nVec*2];
			for(int i=0;i<nVec*2;++i){
				arr[i]=new ArrayList<Integer>();
			}
			for(int s=0;s<list.size();++s) {
				int res=Integer.MAX_VALUE,ans,loc=0;
				for(int i=0;i<fSplit.size();++i) {
					ans=diference(list.get(s),fSplit.get(i));
					if(ans<res) {
						loc=i;
						res=ans;
					}
					
				}
				arr[loc].add(s);
			}
			arrangement=arr;
			sSplit=Split(arr,fSplit);
			fSplit=add(sSplit);
		}
		while(fSplit.size()<nVec);

		
		
		writeToFile(arrangement,fSplit,path);
	}
	
	private void writeToFile(ArrayList<Integer>[] arrangement, ArrayList<int[][]> fSplit,String path) throws IOException {
		DataOutputStream os = new DataOutputStream(new FileOutputStream(path));
		
		//TODO write as int
		os.writeInt(vecSize);
		os.writeChars("\n");
		os.writeInt(nVec);
		os.writeChars("\n");
		os.writeInt(img.getWidth());
		os.writeChars("\n");
		os.writeInt(img.getHeight());
		os.writeChars("\n");
		for(int i=0;i<fSplit.size();++i) {
			int[][] tmp=fSplit.get(i);
			for(int j=0;j<vecSize;j++) {
				for(int k=0;k<vecSize;++k) {
					os.writeFloat(tmp[j][k]);
					os.writeChars(" ");
				}
				os.writeChars("\n");
			}
//			os.writeInt(arrangement[i].size());
			for(int j:arrangement[i]) {
				os.write(j);
				os.writeChars(" ");
			}
		}
		os.close();
	}

	public void readFromFile(String path) throws FileNotFoundException {
		File file= new File(path);
		Scanner sc= new Scanner (file);
		vecSize=sc.nextInt();
		nVec=sc.nextInt();
		int height=sc.nextInt();
		int width=sc.nextInt();
		System.out.println(vecSize+" "+nVec+" " +height+" "+width);
		
		
	}
	
	private int[][] getCeil(double[][] average) {
		int[][] tmpCeil = new int[vecSize][vecSize];
		for(int i=0;i<vecSize;++i) {
			for(int j=0;j<vecSize;++j) {
				tmpCeil[i][j]=(int) Math.floor(average[i][j]+1);
			}
		}
		return tmpCeil;
	}
	
	private int[][] getFloor(double[][] average) {
		int[][] tmpFloor = new int[vecSize][vecSize];
		for(int i=0;i<vecSize;++i) {
			for(int j=0;j<vecSize;++j) {
				tmpFloor[i][j]=(int) Math.ceil(average[i][j]-1);
			}
		}
		return tmpFloor;
	}

	private ArrayList<int[][]> add(ArrayList<double[][]> in) {
		ArrayList<int[][]> fSplit= new ArrayList<int[][]>();
		for(double[][] i:in) {
			fSplit.add(getFloor(i));
			fSplit.add(getCeil(i));
		}
		return fSplit;
	}

	private ArrayList<double[][]> Split(ArrayList<Integer>[] arr, ArrayList<int[][]> in) {
		ArrayList<double[][]> fSplit=new ArrayList<double[][]>();
		for(int i=0;i<in.size();++i) {
//			System.out.println("siz "+arr[i].size());
			fSplit.add(construct(arr[i]));
			print(fSplit.get(i));
		}
		return fSplit;
	}

	private double[][] construct(ArrayList<Integer> valid) {
		double [][] tmp = new  double[vecSize][vecSize];
		
		for(int s:valid) {
			for(int i=0;i<vecSize;++i) {
				for(int j=0;j<vecSize;++j) {
					tmp[i][j]+=list.get(s)[i][j];
				}
			}
//			print2(list.get(s));
		}
		for(int i=0;i<vecSize;++i) {
			for(int j=0;j<vecSize;++j) {
				if(valid.size()>0) tmp[i][j]/=(valid.size()*1.0);
				else tmp[i][j]=0;
			}
		}
		
//		print(tmp);
		return tmp;
	}

	private int diference(int[][] ks,int [][]tmp) {
		int res=0;
		for(int i=0;i<vecSize;++i) {
			for(int j=0;j<vecSize;++j) {
				res+=Math.abs(ks[i][j]-tmp[i][j]);
			}
		}
		return res;
	}
	
	public void print (double[][] tmp) {
		for(int i=0;i<vecSize;++i) {
			for(int j=0;j<vecSize;++j) {
				System.out.print(tmp[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public void print2 (int[][] tmp) {
		for(int i=0;i<vecSize;++i) {
			for(int j=0;j<vecSize;++j) {
				System.out.print(tmp[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	
	
	public static void main(String[] args) throws IOException {
		VectorQuantization tst = new VectorQuantization() ;
		tst.encode("E:\\Downloads\\FCI\\Multi\\imgTst.png","E:\\Downloads\\FCI\\Multi\\tst");

	}

}
