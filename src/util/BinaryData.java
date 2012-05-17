
/*
 * 作成日: 2005/01/23
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package util;

/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */

import java.util.StringTokenizer;

public class BinaryData {

    byte binaryArray[];
    int length;
	
	char i2a[]={
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','i','j',
			'k','l','m','n','o','p','q','r','s','t',
			'u','v','w','x','y','z','!','#','$','%',
			'A','B','C','D','E','F','G','H','I','J',
			'K','L','M','N','O','P','Q','R','S','T',
			'U','V','W','X'};
	int a2i[];
	public void init()
	{
			a2i=new int[256];
			a2i[(int)'0']=0;a2i[(int)'1']=1;a2i[(int)'2']=2;a2i[(int)'3']=3; a2i[(int)'4']=4;
			a2i[(int)'5']=5;a2i[(int)'6']=6;a2i[(int)'7']=7;a2i[(int)'8']=8; a2i[(int)'9']=9;

			a2i[(int)'a']=10;a2i[(int)'b']=11;a2i[(int)'c']=12;a2i[(int)'d']=13;a2i[(int)'e']=14;
			a2i[(int)'f']=15;a2i[(int)'g']=16;a2i[(int)'h']=17;a2i[(int)'i']=18;a2i[(int)'j']=19;
			a2i[(int)'k']=20;a2i[(int)'l']=21;a2i[(int)'m']=22;a2i[(int)'n']=23;a2i[(int)'o']=24;
			a2i[(int)'p']=25;a2i[(int)'q']=26;a2i[(int)'r']=27;a2i[(int)'s']=28;a2i[(int)'t']=29;
			a2i[(int)'u']=30;a2i[(int)'v']=31;a2i[(int)'w']=32;a2i[(int)'x']=33;a2i[(int)'y']=34;
			a2i[(int)'z']=35;a2i[(int)'!']=36;a2i[(int)'#']=37;a2i[(int)'$']=38;a2i[(int)'%']=39;

			a2i[(int)'A']=40;a2i[(int)'B']=41;a2i[(int)'C']=42;a2i[(int)'D']=43;a2i[(int)'E']=44;
			a2i[(int)'F']=45;a2i[(int)'G']=46;a2i[(int)'H']=47;a2i[(int)'I']=48;a2i[(int)'J']=49;
			a2i[(int)'K']=50;a2i[(int)'L']=51;a2i[(int)'M']=52;a2i[(int)'N']=53;a2i[(int)'O']=54;
			a2i[(int)'P']=55;a2i[(int)'Q']=56;a2i[(int)'R']=57;a2i[(int)'S']=58;a2i[(int)'T']=59;
			a2i[(int)'U']=60;a2i[(int)'V']=61;a2i[(int)'W']=62;a2i[(int)'X']=63;a2i[(int)'Y']=64;

	}
	
	int MaxLength=800;

	public BinaryData(){
		this.binaryArray=new byte[MaxLength];
		init();
	}

    public void setDataShallow(byte x[]){
    	this.binaryArray=x;
    }
    
    public byte[] getByteArray(){
    	return this.binaryArray;
    }
    
    public void setDataDeep(byte x[]){
    	int l=x.length;
		this.binaryArray=new byte[l];
    	for(int i=0;i<l;i++){
    		binaryArray[i]=x[i];
    	}
    }

    public void setLength(int x){
    	this.length=x;
    }
    
    public int getLength(){
    	return length;
    }

	public String bin2str()
//	コードを文字列に直す
//	4つ集めてtoStringを呼び出す
	{

		int n=0;
		int m=0;
		int padding=0;
		String buf=int2String(length);
		if(length%4!=0) padding=1;
		for(m=0;m<length/4+padding;m++){
			int k;
			k=m*4;
			int a=0;
			for(n=0;n<4;n++){
				byte c=binaryArray[k];
				a=a<<8;
				a=a | (c & 0x000000ff);
				k++;
			}
			String st=int2String(a); // this can be more faster
			buf=buf+st;  // this can be more faster
		}
		
		return buf;
	}

	public String bin2str(int from, int len)
//	コードを文字列に直す
//	4つ集めてtoStringを呼び出す
	{

		int n=0;
		int m=0;
		int padding=0;
		String buf=int2String(len);
		if(len%4!=0) padding=1;
		for(m=0;m<len/4+padding;m++){
			int k;
			k=m*4;
			int a=0;
			for(n=0;n<4;n++){
				byte c=0;
				if(k+from<length)
				  c=binaryArray[k+from];
				a=a<<8;
				a=a | (c & 0x000000ff);
				k++;
			}
			String st=int2String(a); // this can be more faster
			buf=buf+st;  // this can be more faster
		}
		
		return buf;
	}

	public String binary2StringWithLength(int from, int length)
	{

       int size=this.binaryArray.length;

       int strLength=length*4/3;
       if(strLength*3!=length*4) strLength=strLength+1;

	   char subimg[]=new char[strLength];
	   String rtn;

       int psub=0;
       int pc=0;
       int i=0;
       while(i<length){
	   	   if(pc==0){ // xxxx xx-- , pattern 0
	   	   	  int tmp=binaryArray[from+i] & 0x000000fc;
	   	   	  subimg[psub]=i2a[(tmp>>2)];
	          pc++;
	   	   }
	   	   else
	   	   if(pc==1){ // ---- --xx xxxx ---- , pattern 1
	   	   	  int tmp=binaryArray[from+i] & 0x00000003;
	   	   	  i++;
	   	   	  if(from+i<size){
	   	   	  	int tmp2=binaryArray[from+i+1]&0x000000f0;
				subimg[psub]=i2a[((tmp<<4)|(tmp2>>4))];
	   	   	  }
	   	   	  else{ 
	   	   	  	subimg[psub]=i2a[((tmp<<4)|0x000000)];
	   	   	  }
	   	   	  pc++;
	   	   }
	   	   else
	   	   if(pc==2){ // ---- xxxx xx-- ----, pattern 2
	   	      int tmp=binaryArray[from+i] & 0x0000000f;
	   	      i++;
	   	      if(from+i<size){
	   	      	int tmp2=binaryArray[from+i+1] & 0x000000c0;
	   	      	subimg[psub]=i2a[((tmp<<2) | (tmp2>>6))];
	   	      }
	   	      else{
	   	      	subimg[psub]=i2a[(tmp<<2)];
	   	      }
	   	      pc++;
	   	   }
	   	   else
	   	   if(pc==3){ // --xx xxxx, pattern 3
	   	   	  int tmp=binaryArray[from+i] & 0x0000003f;
	   	   	  subimg[psub]=i2a[tmp];
	   	   	  i++;
	   	   	  pc++;
	   	   }
	   	   if(pc>3){
	   	   	  pc=0;
	   	   }
	   	   psub++;
	   }
		rtn=new String(subimg);
		return ""+from+" "+length+" "+rtn;

	}

	public void stringWithLength2bin(String x)
	{
		StringTokenizer st=new StringTokenizer(x," ");
		String frmstr=st.nextToken();
		String lenstr=st.nextToken();
		String data=st.nextToken();
		int dataLength=data.length();
		int length=(new Integer(lenstr)).intValue();
		int from=(new Integer(frmstr)).intValue();
 
		int i=0;
		int pstr=0;
		int pc=0;
		while(i<length){
			int a=0;
			int b=0;
			int c=0;
			int d=0;
		   if(pc==0){ // aaaa aabb , pattern 0, char a
					  // bbbb cccc  , pattern 1, char b
			  a=a2i[data.charAt(pstr)] ;
			  pstr++;
			  b=a2i[data.charAt(pstr)];
			  pstr++;
			  this.binaryArray[from+i]=(byte)((a<<2)|(b>>6));
			  i++;
			  pc++;
		   }
		   else
		   if(pc==1){ // bbbb cccc , pattern 1, char b
					  // ccdd dddd , pattern 2, char c
			  c=a2i[data.charAt(pstr)];
			  this.binaryArray[from+i]=(byte)((b<<4)|(c>>2));
			  i++;
			  pc++;
		   }
		   else
		   if(pc==2){ // ccdd dddd , pattern2 char d;
			  d=a2i[data.charAt(pstr)];
			  pstr++;
			  this.binaryArray[from+i]=(byte)((c<<6)|d);
			  i++;
			  pc=0;
		   }

	   }
	}

	public int string2Int(String x)
	{
		int index;
		int rtn=0;
		int mlt=64;
		int xmlt=1;
		for(int i=0;i<6;i++){
			rtn=rtn | a2i[(int)(x.charAt(i))]*xmlt;
			xmlt=xmlt*mlt;
		}
		return rtn;
	}
	public String int2String(int x)
	{
		int strSize=6;

		char subimg[]=new char[strSize];
		String rtn;

		 int psub=0;
		 int indexOfthisSubimg=0;
        
		 int px=x;
		 for(int j=0;j<6;j++){
			   char c=i2a[px & 0x0000003F];
			   subimg[psub]=c; psub++;
			   px=px>>6;
		 }
		 rtn=new String(subimg);
		 return rtn;
		 /*
		String rtn;
		rtn="";
		for(int i=0;i<6;i++){
			rtn=rtn+i2a[x & 0x0000003F];
			x=x>>6;
		}
		return rtn;
         */
	}
	
	public void string2bin(String x)
	{
		int index;
		int rtn=0;
		int mlt=64;
		int xmlt=1;
		int padding=0;
		for(int i=0;i<6;i++){
			rtn=rtn | a2i[(int)(x.charAt(i))]*xmlt;
			xmlt=xmlt*mlt;
		}
		int l;
		if(rtn>MaxLength) return;
		length = rtn;
		if(length%4!=0)padding=1;
		for(int m=0;m<length/4+padding;m++){
			int k;
			k=m*6;
			int xmlt2=1;
			rtn=0;
			for(int s=0;s<6;s++){
				int c=a2i[(int)(x.charAt(k+6+s))];
				rtn=rtn | c*xmlt2;
				xmlt2=xmlt2<<6;
			}
			binaryArray[m*4+3] = (byte)  (rtn & 0x000000ff);
			binaryArray[m*4+2] = (byte) ((rtn & 0x0000ff00)>>8);
			binaryArray[m*4+1] = (byte) ((rtn & 0x00ff0000)>>16);
			binaryArray[m*4]   = (byte) ((rtn & 0xff000000)>>24);
		}
		
//		return buf;		
		
	}


	
	public boolean isAllTheSame(int from, int length)
	{
		byte x=this.binaryArray[from];
		for(int i=1;i<length;i++){
			if(x!=binaryArray[from+i]){
				return false;
			}
		}
		return true;

	}

}
