package util;
import nodesystem.eventrecorder.*;

public class SkipControl {
	// 2008 12/03 t.yamanoue
	// TCP �ʐM�ɂ�鉹���E�摜�ʐM�̒~�ϒx�����ɘa���邽�߁A
	// �ǂݔ�΂��Ԋu(updateInterval)���ƂɁA�ǂݔ�΂�����(skipduration)�����ǂݔ�΂��B
	int minimumInterval=1000; // �ǂݔ�΂��Ԋu�̍ŏ��l(msec)
	int updateInterval=10000; // �ǂݔ�΂��Ԋu(�ω�����)
	int initialUpdateInterval=10000; //�ǂݔ�΂��Ԋu�̏����l
	int lastUpdate=0; //�Ō�ɓǂݔ�΂����s��������
	Timer timer;
	double updateDecreasingFactor=2.0; //�ǂݔ�΂��Ԋu��Z�����銄��
	double updateIncreasingFactor=2.0; //�ǂݔ�΂��Ԋu�𒷂����銄��
    int skipDuration=50; //�ǂݔ�΂�����
    int lastLatency=0; // �O�̓ǂݔ�΂��Ԋu�̂Ƃ��̒x��
    int currentLatency=0; // ���݂̒x��
	double skiptermIncreasingFactor=2.0;//�ǂݔ�΂����Ԃ𒷂����銄��
	int initialSkipterm=50; //�ǂݔ�΂����Ԃ̏����l
	int gapBetweenDecreaseToIncrease=10; //�ǂݔ�΂��Ԋu�𒷂����Ă���Ƃ�����
	                                     //�Z������Ƃ��́A�x���̃M���b�v
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
     * �ǂݔ�΂����@... 2008 12/3
     *   TCP �ŉ����E�摜����M����ƁA�f�[�^�̐M���������邽�߁A�f�[�^�����i�Đ��j
     *   �̂킸���Ȏ��Ԃ��d�Ȃ��Ă����A�Đ��̒x�����傫���Ȃ��Ă����B�����
     *   �������邽�߁A�ȉ����s���B
     *   
     * �@��莞�Ԃ��ƁA���M���̑��M���ԂƂ����Ŏ�M�������Ԃ̍����v������B
     * �@���̍�����莞�Ԃ��Ɣ�r���i�O�̍��ƌ��݂̍��j�A���̑����X����c������B
     *   �����X�����傫���Ƃ��A�f�[�^�ǂݔ�΂�(largeDelar ��true )�̊Ԋu
     *   ��Z�����A�ǂݔ�΂����Ԃ𒷂�����B
     *   �����X�������������A�f�[�^�ǂݔ�΂��̊Ԋu�𒷂����A�ǂݔ�΂����Ԃ�
     *   �Z������B
     */
    public boolean largeDelay(int sentTime){
//    	return false;
    	
		int time=this.timer.getMilliTime(); //time=������
		int ud=time-lastUpdate;
		int dt=time-sentTime; //wx=������-���M����=�x������
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
