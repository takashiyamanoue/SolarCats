package util;
import nodesystem.eventrecorder.*;

public class SkipControl {
	// 2008 12/03 t.yamanoue
	// TCP ’ÊM‚É‚æ‚é‰¹ºE‰æ‘œ’ÊM‚Ì’~Ï’x‰„‚ðŠÉ˜a‚·‚é‚½‚ßA
	// “Ç‚Ý”ò‚Î‚µŠÔŠu(updateInterval)‚²‚Æ‚ÉA“Ç‚Ý”ò‚Î‚µŽžŠÔ(skipduration)‚¾‚¯“Ç‚Ý”ò‚Î‚·B
	int minimumInterval=1000; // “Ç‚Ý”ò‚Î‚µŠÔŠu‚ÌÅ¬’l(msec)
	int updateInterval=10000; // “Ç‚Ý”ò‚Î‚µŠÔŠu(•Ï‰»‚·‚é)
	int initialUpdateInterval=10000; //“Ç‚Ý”ò‚Î‚µŠÔŠu‚Ì‰Šú’l
	int lastUpdate=0; //ÅŒã‚É“Ç‚Ý”ò‚Î‚µ‚ðs‚Á‚½ŽžŠÔ
	Timer timer;
	double updateDecreasingFactor=2.0; //“Ç‚Ý”ò‚Î‚µŠÔŠu‚ð’Z‚­‚·‚éŠ„‡
	double updateIncreasingFactor=2.0; //“Ç‚Ý”ò‚Î‚µŠÔŠu‚ð’·‚­‚·‚éŠ„‡
    int skipDuration=50; //“Ç‚Ý”ò‚Î‚µŽžŠÔ
    int lastLatency=0; // ‘O‚Ì“Ç‚Ý”ò‚Î‚µŠÔŠu‚Ì‚Æ‚«‚Ì’x‰„
    int currentLatency=0; // Œ»Ý‚Ì’x‰„
	double skiptermIncreasingFactor=2.0;//“Ç‚Ý”ò‚Î‚µŽžŠÔ‚ð’·‚­‚·‚éŠ„‡
	int initialSkipterm=50; //“Ç‚Ý”ò‚Î‚µŽžŠÔ‚Ì‰Šú’l
	int gapBetweenDecreaseToIncrease=10; //“Ç‚Ý”ò‚Î‚µŠÔŠu‚ð’·‚­‚µ‚Ä‚¢‚é‚Æ‚«‚©‚ç
	                                     //’Z‚­‚·‚é‚Æ‚«‚ÌA’x‰„‚ÌƒMƒƒƒbƒv
	Parameters parameters;
	public void setTimer(Timer t){
		this.timer=t;
	}
    public SkipControl(Timer t, Parameters p){
    	this.timer=t;
    	this.parameters=p;
    	this.setParameters(p);
    	System.out.println("minimumInterval="+minimumInterval);
    	System.out.println("updateInterval="+updateInterval);
    	System.out.println("initialSkipterm="+this.initialSkipterm);
    	System.out.println("gapBetweenDecreaseToIncrease="+this.gapBetweenDecreaseToIncrease);
    	System.out.println("updateDedreasingFactor="+this.updateDecreasingFactor);
    	System.out.println("updateIncreasingFactor="+this.updateIncreasingFactor);
    	System.out.println("skiptermIncreasingFactor="+this.skiptermIncreasingFactor);
    	
    }
    public void setParameters(Parameters p){
    	if(p==null) return;
    	this.minimumInterval=p.getInt("minimumInterval");
    	this.updateInterval=p.getInt("initialInterval");
    	this.skipDuration=p.getInt("initialSkipterm");
    	this.gapBetweenDecreaseToIncrease=p.getInt("gap");
    	this.updateDecreasingFactor=p.getDouble("updDecFact");
    	this.updateIncreasingFactor=p.getDouble("updIncFact");
    	this.skiptermIncreasingFactor=p.getDouble("skipIncFact");
    }
    public boolean skipTerm(int timeFromUpdate, int dt){
    	boolean skip;
    	currentLatency=dt;
    	if(timeFromUpdate>this.updateInterval){
    		skip=true;
        	if(timeFromUpdate>this.updateInterval+skipDuration){
        		skip=false;
        	}
        	else{
        		skip=true;
        	}
        	if(currentLatency>lastLatency){
//        		System.out.println("latency is increasing.");
        		if(this.updateInterval>minimumInterval)
        		this.updateInterval=(int)((double)this.updateInterval/this.updateDecreasingFactor);
        		this.skipDuration=(int)((double)this.skipDuration*this.skiptermIncreasingFactor);
        	}
        	else
        	if(currentLatency+10<lastLatency){
//        		System.out.println("latency is decreasing");
        		this.updateInterval=(int)((double)this.updateInterval*this.updateIncreasingFactor);
        		this.skipDuration=this.initialSkipterm;
        	}
        	this.lastLatency=dt;
    	}
    	else{
    		skip=false;
    	}
    	return skip;
    }
    /*
     * “Ç‚Ý”ò‚Î‚µ•û–@... 2008 12/3
     *   TCP ‚Å‰¹ºE‰æ‘œ‚ðŽóM‚·‚é‚ÆAƒf[ƒ^‚ÌM—Š«‚ª‚ ‚é‚½‚ßAƒf[ƒ^ˆ—iÄ¶j
     *   ‚Ì‚í‚¸‚©‚ÈŽžŠÔ‚ªd‚È‚Á‚Ä‚¢‚«AÄ¶‚Ì’x‰„‚ª‘å‚«‚­‚È‚Á‚Ä‚¢‚­B‚±‚ê‚ð
     *   ‰ðÁ‚·‚é‚½‚ßAˆÈ‰º‚ðs‚¤B
     *   
     * @ˆê’èŽžŠÔ‚²‚ÆA‘—MŒ³‚Ì‘—MŽžŠÔ‚Æ‚±‚±‚ÅŽóM‚µ‚½ŽžŠÔ‚Ì·‚ðŒv‘ª‚·‚éB
     * @‚»‚Ì·‚ðˆê’èŽžŠÔ‚²‚Æ”äŠr‚µi‘O‚Ì·‚ÆŒ»Ý‚Ì·jA·‚Ì‘‰ÁŒXŒü‚ð”cˆ¬‚·‚éB
     *   ‘‰ÁŒXŒü‚ª‘å‚«‚¢‚Æ‚«Aƒf[ƒ^“Ç‚Ý”ò‚Î‚µ(largeDelar ‚ªtrue )‚ÌŠÔŠu
     *   ‚ð’Z‚­‚µA“Ç‚Ý”ò‚Î‚µŽžŠÔ‚ð’·‚­‚·‚éB
     *   ‘‰ÁŒXŒü‚ª¬‚³‚¢ŽžAƒf[ƒ^“Ç‚Ý”ò‚Î‚µ‚ÌŠÔŠu‚ð’·‚­‚µA“Ç‚Ý”ò‚Î‚µŽžŠÔ‚ð
     *   ’Z‚­‚·‚éB
     */
    public boolean largeDelay(int sentTime){
//    	return false;
    	
		int time=this.timer.getMilliTime(); //time=Œ»Žž
		int ud=time-lastUpdate;
		int dt=time-sentTime; //wx=Œ»Žž-‘—MŽžŠÔ=’x‰„ŽžŠÔ
		if(ud>updateInterval){
	    	this.lastUpdate=time;
//	    	this.setParameters(this.parameters);
	    	if(this.skipTerm(ud, dt)){
	    		return true;
	    	}
	    	else{
	    		return false;
	    	}
		}
		return false;
    }
}
