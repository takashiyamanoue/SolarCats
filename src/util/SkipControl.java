package util;
import nodesystem.eventrecorder.*;

public class SkipControl {
	// 2008 12/03 t.yamanoue
	// TCP 通信による音声・画像通信の蓄積遅延を緩和するため、
	// 読み飛ばし間隔(updateInterval)ごとに、読み飛ばし時間(skipduration)だけ読み飛ばす。
	int minimumInterval=1000; // 読み飛ばし間隔の最小値(msec)
	int updateInterval=10000; // 読み飛ばし間隔(変化する)
	int initialUpdateInterval=10000; //読み飛ばし間隔の初期値
	int lastUpdate=0; //最後に読み飛ばしを行った時間
	Timer timer;
	double updateDecreasingFactor=2.0; //読み飛ばし間隔を短くする割合
	double updateIncreasingFactor=2.0; //読み飛ばし間隔を長くする割合
    int skipDuration=50; //読み飛ばし時間
    int lastLatency=0; // 前の読み飛ばし間隔のときの遅延
    int currentLatency=0; // 現在の遅延
	double skiptermIncreasingFactor=2.0;//読み飛ばし時間を長くする割合
	int initialSkipterm=50; //読み飛ばし時間の初期値
	int gapBetweenDecreaseToIncrease=10; //読み飛ばし間隔を長くしているときから
	                                     //短くするときの、遅延のギャップ
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
     * 読み飛ばし方法... 2008 12/3
     *   TCP で音声・画像を受信すると、データの信頼性があるため、データ処理（再生）
     *   のわずかな時間が重なっていき、再生の遅延が大きくなっていく。これを
     *   解消するため、以下を行う。
     *   
     * 　一定時間ごと、送信元の送信時間とここで受信した時間の差を計測する。
     * 　その差を一定時間ごと比較し（前の差と現在の差）、差の増加傾向を把握する。
     *   増加傾向が大きいとき、データ読み飛ばし(largeDelar がtrue )の間隔
     *   を短くし、読み飛ばし時間を長くする。
     *   増加傾向が小さい時、データ読み飛ばしの間隔を長くし、読み飛ばし時間を
     *   短くする。
     */
    public boolean largeDelay(int sentTime){
//    	return false;
    	
		int time=this.timer.getMilliTime(); //time=現時刻
		int ud=time-lastUpdate;
		int dt=time-sentTime; //wx=現時刻-送信時間=遅延時間
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
