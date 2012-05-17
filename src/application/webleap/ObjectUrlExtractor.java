package application.webleap;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTextArea;

import language.HtmlPatternMatcher;

public class ObjectUrlExtractor extends java.lang.Object
{
    public int urlNo;
    public WebLEAPFrame gui;
    public String textOfTheURL;
    public void loadURL(String urlName)
    {
        int bl=0;
		URL url=null;
		InputStream instream=null;
		DataInputStream dinstream=null;
//		CQueue inqueue;
		int rl=0;
		int x=0;
		try{
    		url=new URL(urlName);
    	}
    	catch(MalformedURLException e){
            gui.addMessage("URL Format Error\n");
    	}
try{
      URLConnection urlConnection = url.openConnection();

      gui.addMessage(""+urlConnection.getURL()+"\n");

      //Use the connection to get and display the date last
      // modified.
      Date lastModified = new Date(
                          urlConnection.getLastModified());
//      System.out.println(lastModified);
      gui.addMessage(lastModified+"\n");

      //Use the connection to get and display the content
      // type.
//      System.out.println(urlConnection.getContentType());
      gui.addMessage(""+urlConnection.getContentType()+"\n");


    }//end try
    catch(UnknownHostException e){
      System.out.println(e);
      System.out.println(
                        "Must be online to run properly.");
    }//end catch
    catch(MalformedURLException e){System.out.println(e);}
    catch(IOException e){System.out.println(e);}


        try{
//            instream=url.openStream();
              dinstream= new DataInputStream(url.openStream());
        }
        catch(IOException e){
            gui.addMessage("URL Open Error\n");
        }

            String line=null;
            textOfTheURL="";
            try{
                /*
               line=dinstream.readLine();
               */
               line=dinstream.readUTF();
            }
            catch(IOException e){
                gui.addMessage("error at readLine.\n");
            }
            while(line!=null)
            {
                textOfTheURL=textOfTheURL+line+"\n";
                try{
                    /*
                line=dinstream.readLine();
                    */
                    line=dinstream.readUTF();
                }
                catch(IOException e){
                    gui.addMessage("error at readLine.\n");
                    break;
                }
            }
  //      }
        JTextArea ra=gui.getResultArea();
        ra.repaint();
        gui.addMessage("connection closed.\n");

    }
    public Vector results;
    public webleap.PatternMatcher pm;
    public StringTokenizer st;
    public String html;
    public String next()
    {
        urlNo++;
        Vector result=new Vector();
        pm.pattern("<dl><dt><b>"+urlNo+"."+" *</b>",result);
        return (String)(result.elementAt(0));
    }
    public ObjectUrlExtractor(String ht, WebLEAPFrame g)
    {
        html=ht;
        gui=g;
        pm=new HtmlPatternMatcher(g);
        pm.init(ht);
        urlNo=0;
    }
}

