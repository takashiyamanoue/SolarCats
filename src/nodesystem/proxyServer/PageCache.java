package nodesystem.proxyServer;

import java.util.*;

public class PageCache {
	
	Hashtable cache;
	int timeout=1000;
	ProxyServer server;
	public PageCache(ProxyServer s){
		this.cache=new Hashtable();
		server=s;
	}
	
	public void clear(){
		if(this.cache!=null)
		this.cache.clear();
	}
    public synchronized void put(String url, AContent c){
    	if(this.cache!=null)
    	this.cache.put(url,c);
    }
    public int getSize(){
    	return this.cache.size();
    }
    public boolean containsKey(String x){
    	if(this.cache.containsKey(x)) return true;
    	else return false;
    }
    public AContent get(String url, URL_Loader l){
    	AContent c=(AContent)(this.cache.get(url));
    	if(c==null){
    		AContent nc=new AContent(url,"getting",null);
    		this.cache.put(url,nc);
//    		l.load(url);
    	}
    	else
    	if(c.kind=="getting"){
    		
    	}
    	else{
    		return c;
    	}
    	return null;
    }

}
