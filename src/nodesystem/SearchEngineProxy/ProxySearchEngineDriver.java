package nodesystem.SearchEngineProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import nodesystem.StringIO;

import nodesystem.MessageReceiver;

public class ProxySearchEngineDriver {
	
    public MessageReceiver mreceiver;
    public String page;
	StringIO sio;
	MessageReceiver message;

    public ProxySearchEngineDriver(StringIO s, MessageReceiver m){
    	sio=s;
    	mreceiver=m;
    }
    
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
        try{
        	if(this.sio!=null){
                this.sio.writeString("return "+ page);
        	}
        }
        catch(Exception e){
        	Thread.dumpStack();
        }
        try{
        	dinstream.close();
        	dinstream=null;
        }
        catch(Exception e){
        	Thread.dumpStack();
        }
        mreceiver.receiveMessage("connection closed.\n");
        
        /*
        try{
           dinstream.close();
        }
        catch(IOException e) {mreceiver.receveMessageText("close error.\n");}
        */
    }

}
