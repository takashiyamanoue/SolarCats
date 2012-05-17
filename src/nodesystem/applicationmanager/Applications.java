package nodesystem.applicationmanager;
import java.awt.Choice;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import nodesystem.AMessage;
import nodesystem.CommunicationNode;

import controlledparts.ControlledFrame;


/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* *************************************
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
* for this machine, so Jigloo or this code cannot be used legally
* for any corporate or commercial purpose.
* *************************************
*/
public class Applications extends java.lang.Object
{
	Hashtable<String,String> commands;
	public void setEditable(boolean t){
		ApplicationID p=first;
		while(p!=null){
			ControlledFrame f=p.application;
			f.setEditable(t);
			p=p.next;
		}
		
	}
	
    public void removeApplication(String name)
    {
        this.applicationPaths.remove(name);
    }

    public String getApplicationPath(String name)
    {
        String rtn=(String)(this.applicationPaths.get(name));
//        System.out.println("applicationPath="+rtn);
        return rtn;
    }

    public String getApplicationFromCommand(String command){
    	String rtn=(String)(this.applicationCommand.get(command));
    	if(rtn==null){
    		String appliName=this.commands.get(command);
    		if(appliName==null) return null;
    		ControlledFrame f=this.spawnApplication2(appliName, "receive");
    		gui.setControlMode("receive");
    		if(f==null) return null;
    		rtn=(String)(this.applicationCommand.get(command));
    	}
    	return rtn;
    }

    public void addApplication(String name, String path, String command)
    {
        this.applicationPaths.put(name,path);
        this.commands.put(command, name);
    }

    Hashtable applicationPaths; //key: application-id content: path
    Hashtable applicationCommand; //key:command-name content: path

    public void kill(int id)
    {
        ApplicationID aid=getRunningApplication(id);
        if(aid==null) gui.messageArea.append("proc "+id+" not found while deleting it.\n");
        else this.deleteRunningApplication(id);
//        aid.application.exitThis();        
    }

    public ApplicationID getRunningApplication(int id)
    {
        ApplicationID p=first;
        while(p!=null){
            int pid=p.procID;
            if(pid==id) {
                return p;
            }
            p=p.next;
        }
        return null;
    }

    public void deleteRunningApplication(int pID)
    {
        ApplicationID p=first;
        ApplicationID q=first;
        int id=first.procID;
        if(id==pID){
//            first.application.dispose();
//            first=first.next;
            return;
        }
        q=p.next;
        while(q!=null){
            id=q.procID;
            if(id==pID) {
//               q.application.dispose();
//               p.next=q.next;
               return;
            }
            q=q.next;
            p=p.next;
        }
    }

    public void changeAllControlMode(String controlMode)
    {
        this.gui.setControlMode(controlMode);
    }

    public void deleteRunningApplication(String applicationName)
    {
        ApplicationID p=first;
        ApplicationID q=first;
        String name=first.name;
        if(name.equals(applicationName)){
//            first.application.dispose();
//            first=first.next;
            return;
        }
        q=p.next;
        while(q!=null){
            name=q.name;
            if(name.equals(applicationName)) {
//                p.application.dispose();
//               p.next=q.next;
               return;
            }
            q=q.next;
            p=p.next;
        }
    }

    public ApplicationID getRunningApplication(String applicationName)
    {
        ApplicationID p=first;
        while(p!=null){
            String name=p.name;
            if(name.equals(applicationName)) return p;
            p=p.next;
        }
        return null;
    }

    public void addNewRunningApplication(ApplicationID aid)
    {
        aid.next=null;
        //
        if(first==null) {
            last=aid;
            first=aid;
        }
        else{
            aid.next=first;
            first=aid;
        }
    }

    public  // synchronized 
    ControlledFrame spawnApplication2(String s, String controlMode)
    {
         StringTokenizer st=new StringTokenizer(s," ");
         String cname=st.nextToken();
         String args=s.substring(cname.length());
         Class theClass;
         ControlledFrame obj=null;
         ApplicationID aid=getRunningApplication(cname);
//         ApplicationID aid=null;
         this.gui.lockTimer=20;
         
         if(aid!=null) {
//            aid.application.controlMode=controlMode;
            aid.application.setVisible(true);
            aid.application.clearAll();
            return aid.application;
         }
         
         try{
            String path=this.getApplicationPath(cname);
           	theClass=null;
        	if(gui.isApplet()){
        		String x=gui.getAppletBase();
        		System.out.println("x="+x);
        		if(x.startsWith("http:")) {
        			try{
        				System.out.println("applet-base="+x+" path="+path);
                	   URLClassLoader loader = new URLClassLoader(new URL[] { new URL(x) });
                	   theClass=Class.forName(path,true,loader);       			}
        			catch(Exception e){
        				System.out.println(e.toString());
        			}
        		}
        		else
        			theClass=Class.forName(path);
       	    }
        	else{
                  theClass=Class.forName(path);
        	}
         }
         catch(ClassNotFoundException e){
            gui.appendMessage("class not found exception occurd while loading "
            +s+".\n");
            return null;
         }
        
         try{
               obj=(ControlledFrame)(theClass.newInstance());
         }
         catch(InstantiationException e)
         {
            gui.appendMessage("Instatiation Exception occurd while spawning "+s+".\n");
            return null;
         }
         catch(IllegalAccessException e)
         {
            gui.appendMessage("illegal access exception occurd while spawning "+s+".\n");
            return null;
         }
         catch(Exception e)
         {
            gui.appendMessage("exception " + e + " is occured while spawning " + s + ".\n");
            Thread.dumpStack();
            return null;
         }
//        obj.dispose();
         ControlledFrame sobj=obj.spawnMain(gui,args,procID,controlMode);
         sobj.setIsApplet(gui.isApplet());
         String commandName=obj.getCommandName()+" ";
         this.applicationCommand.put(commandName,cname);
         sobj.clearAll();
         String mode=gui.getControlMode();
         if(mode.equals("remote")||mode.equals("receive")){
        	sobj.setEditable(false);
         }
         else{
        	sobj.setEditable(true);
         }
//        Spawnable sobj=theClass.spawnMain(gui,args,procID);
         aid=new ApplicationID(cname,s,controlMode,procID,sobj);
         this.addNewRunningApplication(aid);
         procID++;
         return sobj;
    }

    public void sendAll()
    {
        ApplicationID p=first;
        while(p!=null){
            ControlledFrame obj=p.application;
            obj.sendAll();
            p=p.next;
        }
    }
    
    public ApplicationID last;
    public ApplicationID first;
    public boolean isRunning(String s)
    {
        ApplicationID aid=getRunningApplication(s);
        if(aid!=null) return true;
        else return false;
    }
    public void kill(String applicationName)
    {
        ApplicationID aid=getRunningApplication(applicationName);
        if(aid==null) gui.messageArea.append(applicationName+" not found while deleting it.\n");
        else this.deleteRunningApplication(applicationName);
//        aid.application.exitThis();        
    }
    public int procID;
    public void killProcess(String s)
    {
    }
    public CommunicationNode gui;
    public Applications(CommunicationNode gui)
    {
    	this.keyFrame=new Vector();
        this.gui=gui;
        this.commands=new Hashtable();
        init();
    }
    public void spawnApplication(String s, String controlMode)
    {
        ControlledFrame obj=spawnApplication2(s,controlMode);
    }
    public Choice applicationChoice;

    public void init()
    {
        procID=1;
        first=null;
        last=null;
        
        this.applicationPaths=new Hashtable();
        this.applicationCommand=new Hashtable();
    }
    Vector <AMessage> keyFrame;
    public Vector getKeyFrame(){
        ApplicationID p=first;
        while(p!=null){
            Vector<AMessage> appliKeyFrame=p.getKeyFrame();
            addKeyFrames(keyFrame,appliKeyFrame);
            p=p.next;
        }
        return keyFrame;
    }
    
    public void setCurrentStatus(Vector<AMessage> x){
    	for(int i=0;i<x.size();i++){
    		AMessage m=x.elementAt(i);
    		
    	}
    }
    public void addKeyFrames(Vector<AMessage> x, Vector<AMessage> y){
    	for(int i=0;i<y.size();i++){
    		x.add(y.elementAt(i));
    	}
    }
}

