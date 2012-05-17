package webleap;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import nodesystem.AMessage;
import nodesystem.StringIO;
public class PageLoader1 implements java.lang.Runnable  
{
	String proxyKind;
	String passWord;
	
    public PageLoader1(String url, MessageReceiver p, MessageReceiver m, String pname, int pport, String pkind, String pw)
    {
        setProxy(pname,pport,pkind,pw);
        objectURL=url;
        receiver=p;
        mreceiver=m;
        start();
    }
    
    public void setProxy(String pname, int pport, String pkind, String pw)
    {
        this.proxyName=pname;
        this.proxyPort=pport;
        this.proxyKind=pkind;
        this.passWord=pw;
    }

    public int proxyPort;

    public String proxyName;

    public void loadURLfromProxy(String targetURL, String proxyAddress, int proxyPort)
    {
        int maxbuff=1024;
        byte buff[]=new byte[maxbuff];;
        String dataLine;
        page="";
        int bl=0;
		URL url=null;
		InputStream instream=null;
		BufferedReader dinstream=null;
//		CQueue inqueue;
		int rl=0;
		int x=0;
		
		Socket proxySocket=null;
		try{
		    proxySocket=new Socket(proxyAddress,proxyPort);
		}
		catch(UnknownHostException e){
		    mreceiver.receiveMessage("proxy host unknown:"+proxyAddress+":"+proxyPort+"\n");
		}
		catch(IOException e){
		    mreceiver.receiveMessage("IOException while connecting the proxy:"+proxyAddress+"\n");
		}
		BufferedOutputStream outs=null;
		InputStream ins=null;
		try{
    		outs=new BufferedOutputStream(proxySocket.getOutputStream());
		    ins=proxySocket.getInputStream();
    	}
    	catch(IOException e){
    	    mreceiver.receiveMessage("IOException while getting output stream\n");
    	}
		
		try{
		    byte b[]=(new String("GET "+targetURL+" HTTP/1.1\n\n")).getBytes();
    		outs.write(b);
    		outs.flush();
		}
		catch(IOException e){
		    mreceiver.receiveMessage("IOException while output the GET command\n");
		}
		

       int buffersize=60000;

            dinstream=new BufferedReader(new InputStreamReader(ins),buffersize);


            String line=null;
            try{
            line=dinstream.readLine();
            }
            catch(IOException e){
                mreceiver.receiveMessage("error at readLine.\n");
            }
            catch(NullPointerException e){
                mreceiver.receiveMessage("null pointer exception.\n");
            }
            while(line!=null)
            {
                page=page+line+"\n";
                try{
                line=dinstream.readLine();
                }
                catch(IOException e){
                    mreceiver.receiveMessage("error at readLine.\n");
                    break;
                }
                catch(ArrayIndexOutOfBoundsException e){
                    mreceiver.receiveMessage("error at readLine, arrayIndexOutOf bounds.\n");
                    mreceiver.receiveMessage("buffer size "+buffersize+"seems small.");
                    break;
                }
                catch(NullPointerException e){
                    mreceiver.receiveMessage("error at readLine, null pointer exception.\n");
                    break;
                }
                catch(Exception e){
                    mreceiver.receiveMessage("error at readLine, something wrong:"+e+".\n");
                    break;
                }
            }
  //      }
//        resultArea.repaint();
        mreceiver.receiveMessage("connection closed.\n");
        
        try{
           dinstream.close();
           outs.close();
           proxySocket.close();
        }
        catch(IOException e) {mreceiver.receiveMessage("close error.\n");}
       
    }
    public void setPassword(String x){
    	this.passWord=x;
    }
    public void loadURLfromWebLeapProxy(String targetURL, String proxyAddress, int proxyPort)
    {
    	StringIO sio=new StringIO(proxyAddress,proxyPort,null);
    	if(sio==null ) {
    		page="";
    		return;
    	}
    	try{
        	sio.writeString("get-"+passWord+" "+targetURL);
        	AMessage m=sio.readString();
        	String rtn=m.getHead();
        	if(rtn.startsWith("return ")){
        		String x=rtn.substring("return ".length());
        	    this.page=x;
        	}
    	}
    	catch(Exception e){
    		System.out.println(e.toString());
    		Thread.dumpStack();
    	}

        mreceiver.receiveMessage("connection closed.\n");
        
        try{
        	sio.close();
        }
        catch(IOException e) {mreceiver.receiveMessage("close error.\n");}
       
    }

    public MessageReceiver mreceiver;

    public String objectURL;

    public PageLoader1(String url, MessageReceiver p, MessageReceiver m)
    {
        objectURL=url;
        receiver=p;
        mreceiver=m;
        start();
    }

//    public AckReceiver receiver;
      public MessageReceiver receiver;
    public void loadURL(String urlName)
    {
        int maxbuff=1024;
        byte buff[]=new byte[maxbuff];
        String dataLine;
        page="";
        int bl=0;
		URL url=null;
		InputStream instream=null;
		InputStreamReader isr=null;
		BufferedReader dinstream=null;
		URLConnection urlConnection=null;
//        HttpURLConnection urlConnection=null;
/*
	getContentEncoding 
	getContentLength 
	getContentType 
	getDate 
	getExpiration 
	getLastModified
*/
        String contentEncoding="";
        int contentLength=0;
        String contentType="";
        long date=0;
        long expiration=0;
        long lastModified=0;
//		CQueue inqueue;
		int rl=0;
		int x=0;
		try{
    		url=new URL(urlName);
    	}
    	catch(MalformedURLException e){
            mreceiver.receiveMessage("URL Format Error\n");
    	}

        try{
              urlConnection = url.openConnection();
//              urlConnection = (HttpURLConnection)(url.openConnection());

              mreceiver.receiveMessage(""+urlConnection.getURL()+":");
/*
//  the following coment outed code is for the debugging.

              urlConnection.setAllowUserInteraction(false);
              if(urlConnection.getAllowUserInteraction()){
                System.out.println("urlConnection is allowed user interaction.");
              }
              else{
                System.out.println("urlConnection is not allowed user interaction.");
              }
              urlConnection.setUseCaches(false);
              if(urlConnection.getUseCaches()){
                System.out.println("urlConnection is using caches.");
              }
              else{
                System.out.println("urlConnection is not using caches.");
              }
//              urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//              urlConnection.setDoInput(true);
//              urlConnection.setDoOutput(true);
              urlConnection.connect();

            //Use the connection to get and display the date last
            // modified.
            lastModified=urlConnection.getLastModified();
            Date lastModifiedDate = new Date(lastModified);
            //      System.out.println(lastModified);
            mreceiver.receiveMessage("lastmodified:"+lastModifiedDate+"\n");

            //Use the connection to get and display the content
            // type.
            //      System.out.println(urlConnection.getContentType());
            contentType=urlConnection.getContentType();
            mreceiver.receiveMessage("type:"+contentType+"\n");

            //
            contentEncoding=urlConnection.getContentEncoding();
            mreceiver.receiveMessage("encoding:"+contentEncoding+"\n");
            
            //
            date=urlConnection.getDate();
            mreceiver.receiveMessage("date:"+(new Date(date))+"\n");
 */           
    
            //
//            Object xx=urlConnection.getContent();
            /*
            contentLength=urlConnection.getContentLength();
            mreceiver.receiveMessage(" length:"+contentLength+"\n");
            if(contentLength<0){
                mreceiver.receiveMessage("this url seems wrong.\n");
            }
            */

        }//end try
        catch(UnknownHostException e){
            System.out.println(e);
            System.out.println(
                        "Must be online to run properly.");
        }//end catch
        catch(MalformedURLException e){System.out.println(e);}
        catch(IOException e){System.out.println(e);}


// 
// start loading.
//
        try{
            //Use the connection to get an InputStream object.
            // Use the InputStream object to instantiate a
            // DataInputStream object.
              instream=urlConnection.getInputStream();
              isr=new InputStreamReader(instream);
        }
        catch(Exception e){
            mreceiver.receiveMessage("the url:"+urlName+" may be wrong.\n");
            mreceiver.receiveMessage(""+e.toString()+" while new InputStreamReader\n");
        }
        
        int buffersize=60000; //60000
        try{
              dinstream= new BufferedReader(isr);
          //    dinstream= new BufferedReader(isr,buffersize);
        }
        catch(Exception e){
              mreceiver.receiveMessage(""+e.toString()+" while new BufferedReader\n");
        }


        String line=null;
        try{
            line=dinstream.readLine();
        }
        catch(IOException e){
            mreceiver.receiveMessage("error at readLine.\n");
        }
        catch(NullPointerException e){
            mreceiver.receiveMessage("null pointer exception.\n");
        }
        while(line!=null) {
                page=page+line+"\n";
                try{
                line=dinstream.readLine();
                }
                catch(IOException e){
                    mreceiver.receiveMessage("error at readLine.\n");
                    break;
                }
                catch(ArrayIndexOutOfBoundsException e){
                    mreceiver.receiveMessage("error at readLine, arrayIndexOutOf bounds.\n");
                    mreceiver.receiveMessage("buffer size "+buffersize+"seems small.");
                    break;
                }
                catch(NullPointerException e){
                    mreceiver.receiveMessage("error at readLine, null pointer exception.\n");
                    break;
                }
                catch(Exception e){
                    mreceiver.receiveMessage("error at readLine, something wrong:"+e+".\n");
                    break;
                }
        }
        mreceiver.receiveMessage("connection closed.\n");
        
        
        try{
           dinstream.close();
        }
        catch(IOException e) {
        	mreceiver.receiveMessage("close error.\n");
        }
        
    }

    public void stop()
    {
        if(me!=null){
//            me.stop();
            me=null;
        }
    }

    public Thread me;

    public void start()
    {
        if(me==null){
            me=new Thread(this);
            me.start();
        }
    }

    public String page;

    public void run()
    {
        // This method is derived from interface java.lang.Runnable
        // to do: code goes here
        if(me!=null){
            if(proxyName==null){
                   loadURL(objectURL);
            }
            else   {
            	if(this.proxyKind.equals("general"))
            	    loadURLfromProxy(objectURL,proxyName,proxyPort);
            	else
            	if(this.proxyKind.equals("webleap"))
            		loadURLfromWebLeapProxy(objectURL,proxyName,proxyPort);
            	else
            		loadURL(objectURL);
            }
//            receiver.pageLoadingDone(page);
            receiver.receiveMessage(page);
            stop();
        }
    }

}