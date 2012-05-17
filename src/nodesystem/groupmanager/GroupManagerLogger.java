package nodesystem.groupmanager;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;
public class GroupManagerLogger {
	   /**
	    * ���O�ݒ�v���p�e�B�t�@�C���̃t�@�C����
	    */
	    protected static final String LOGGING_PROPERTIES 
	        = "gmlog.properties";
	    /*
	     * �v���p�e�B�t�@�C�����Ȃ��ꍇ�̐ݒ�
	     */
	    protected static final String LOGGING_SETTINGS
	           = "handlers=java.util.logging.ConsoleHandler\n"
	           + ".level=INFO\n"
	           + "java.util.logging.ConsoleHandler.level=INFO\n"
	           + "java.util.logging.ConsoleHandler.formatter"
	           + "=java.util.logging.SimpleFormatter\n"
	           + "java.util.logging.FileHandler.limit=1000000\n"
	           + "java.util.logging.FileHandler.pattern=GMLogging%u.log\n"
	           + "java.util.logging.FileHandler.formatter=java.util.logging.SimpleFormatter\n"
	           + "java.util.logging.FileHandler.append=true\n"
	           + "java.util.logging.FileHandler.count=10\n";

	    /**
	    * static initializer �ɂ�郍�O�ݒ�̏�����
	    */
	    /*
	    static {
	        final Logger logger = Logger.getLogger("NaraLogging");
		        
	        // �N���X�p�X�̒����� ���O�ݒ�v���p�e�B�t�@�C�����擾
	        logger.fine("���O�ݒ�: " + LOGGING_PROPERTIES 
	            + " �����ƂɃ��O��ݒ肵�܂��B");
//	        InputStream inStream = NaraLogger.class
//	            .getClassLoader().getResourceAsStream(
//         LOGGING_PROPERTIES);
	        InputStream inStream =null;
	        try{
 	        inStream=new FileInputStream(LOGGING_PROPERTIES);
	        }
	        catch(Exception e){}
	        if (inStream == null) {
	            logger.info("���O�ݒ�: " + LOGGING_PROPERTIES 
	                 + " �̓N���X�p�X��Ɍ�����܂���ł����B");
	            
//	            InputStream inStream = null;
	            try {
	                inStream = new ByteArrayInputStream(
	                    LOGGING_SETTINGS.getBytes("UTF-8"));
	                try {
	                    LogManager.getLogManager().readConfiguration(
	                        inStream);
	                    logger.config(
	                        "���O�ݒ�: LogManager��ݒ肵�܂����B");
	                } catch (IOException e) {
	                    logger.warning("���O�ݒ�: LogManager�ݒ�̍ۂ�"
	                        + "��O���������܂����B:" + e.toString());
	                }
	            } catch (UnsupportedEncodingException e) {
	                logger.severe("���O�ݒ�: UTF-8�G���R�[�f�B���O��"
	                    + "�T�|�[�g����Ă��܂���B:" + e.toString());
	            } finally {
	                try {
	                    if (inStream != null) inStream.close(); 
	                } catch (IOException e) {
	                    logger.warning(
	                        "���O�ݒ�: ���O�ݒ�v���p�e�B�t�@�C���̃X�g"
	                        + "���[���N���[�Y���ɗ�O���������܂����B:"
	                        + e.toString());
	                }
	            }
	            
	           Properties p=new Properties();
	           p.setProperty("handlers", "java.util.logging.ConsoleHandler");
	           p.setProperty(".level", "FINEST");
	           p.setProperty("java.util.logging.ConsoleHandler.level", "FINEST");
	           p.setProperty("java.util.logging.ConsoleHandler.formatter",
	                 "java.util.logging.SimpleFormatter");
	 	       try {
		           FileOutputStream saveS = new FileOutputStream(LOGGING_PROPERTIES);
		           p.store(saveS,"--- naralog ---");
                saveS.close();
		        } catch( Exception e){
		           System.err.println(e);
		        } 
	            
	            
	        } else {
	            try {
	                LogManager.getLogManager().readConfiguration(
	                    inStream);
	                logger.config(
	                    "���O�ݒ�: LogManager��ݒ肵�܂����B");
	            } catch (IOException e) {
	                logger.warning("���O�ݒ�: LogManager�ݒ�̍ۂ�"
	                    +"��O���������܂����B:"+ e.toString());
	            } finally {
	                try {
	                    if (inStream != null) inStream.close(); 
	                } catch (IOException e) {
	                    logger.warning("���O�ݒ�: ���O�ݒ�v���p�e�B"
	                        +"�t�@�C���̃X�g���[���N���[�Y���ɗ�O��"
	                        +"�������܂����B:"+ e.toString());
	                }
	            }
	        }
	    }
	    */
	    
	 Logger logger=null;
	 public GroupManagerLogger(){
	        logger = Logger.getLogger("GMLogging");
	        
	        // �N���X�p�X�̒����� ���O�ݒ�v���p�e�B�t�@�C�����擾
	        logger.fine("���O�ݒ�: " + LOGGING_PROPERTIES 
	            + " �����ƂɃ��O��ݒ肵�܂��B");
//	        InputStream inStream = NaraLogger.class
//	            .getClassLoader().getResourceAsStream(
//         LOGGING_PROPERTIES);
	        InputStream inStream =null;
	        try{
 	        inStream=new FileInputStream(LOGGING_PROPERTIES);
	        }
	        catch(Exception e){}
	        if (inStream == null) {
	            logger.info("���O�ݒ�: " + LOGGING_PROPERTIES 
	                 + " �̓N���X�p�X��Ɍ�����܂���ł����B");
	            
//	            InputStream inStream = null;
	            try {
	                inStream = new ByteArrayInputStream(
	                    LOGGING_SETTINGS.getBytes("UTF-8"));
	                try {
	                    LogManager.getLogManager().readConfiguration(
	                        inStream);
	                    logger.config(
	                        "���O�ݒ�: LogManager��ݒ肵�܂����B");
	                } catch (IOException e) {
	                    logger.warning("���O�ݒ�: LogManager�ݒ�̍ۂ�"
	                        + "��O���������܂����B:" + e.toString());
	                }
	            } catch (UnsupportedEncodingException e) {
	                logger.severe("���O�ݒ�: UTF-8�G���R�[�f�B���O��"
	                    + "�T�|�[�g����Ă��܂���B:" + e.toString());
	            } finally {
	                try {
	                    if (inStream != null) inStream.close(); 
	                } catch (IOException e) {
	                    logger.warning(
	                        "���O�ݒ�: ���O�ݒ�v���p�e�B�t�@�C���̃X�g"
	                        + "���[���N���[�Y���ɗ�O���������܂����B:"
	                        + e.toString());
	                }
	            }
	            
	           Properties p=new Properties();
	           
	           p.setProperty("handlers", "java.util.logging.ConsoleHandler, "
	                                   + "java.util.logging.FileHandler");
	           p.setProperty(".level", "INFO");
	           p.setProperty("java.util.logging.ConsoleHandler.level", "INFO");
	           p.setProperty("java.util.logging.ConsoleHandler.formatter",
	                 "java.util.logging.SimpleFormatter");
	           p.setProperty("java.util.logging.FileHandler.limit","1000000");
	           p.setProperty("java.util.logging.FileHandler.pattern","GMLogging%u.log");
	           p.setProperty("java.util.logging.FileHandler.formatter",
	        		   "java.util.logging.SimpleFormatter");
	           p.setProperty("java.util.logging.FileHandler.append", "true");
	           p.setProperty("java.util.logging.FileHandler.count","10");
	                 
	 	       try {
		           FileOutputStream saveS = new FileOutputStream(LOGGING_PROPERTIES);
		           p.store(saveS,"--- gmlog ---");
                saveS.close();
		        } catch( Exception e){
		           System.err.println(e);
		        } 
	            
	            
	        } else {
	            try {
	                LogManager.getLogManager().readConfiguration(
	                    inStream);
	                logger.config(
	                    "���O�ݒ�: LogManager��ݒ肵�܂����B");
	            } catch (IOException e) {
	                logger.warning("���O�ݒ�: LogManager�ݒ�̍ۂ�"
	                    +"��O���������܂����B:"+ e.toString());
	            } finally {
	                try {
	                    if (inStream != null) inStream.close(); 
	                } catch (IOException e) {
	                    logger.warning("���O�ݒ�: ���O�ݒ�v���p�e�B"
	                        +"�t�@�C���̃X�g���[���N���[�Y���ɗ�O��"
	                        +"�������܂����B:"+ e.toString());
	                }
	            }
	        }
	        //logger = Logger.getLogger("NaraLogging");

	 }
  public static void main(final String[] args) {
     final Logger logger = Logger.getLogger("NaraLogging");

     logger.finest("�ׂ̋q�͂悭�`�H���q���B");
     logger.finer("�����s�������ǁB");
     logger.fine("������҂傱�҂傱�B");
     logger.config("��ɂ͓�H�{���B");
     logger.info("�������Đ����B");
     logger.warning("�����܂����݁B");
     logger.severe("�������������������̂����B");
 }
  
  public void finest(String x){
 	 logger.finest(x);
  }
  public void finer(String x){
 	 logger.finer(x);
  }     
  public void fine(String x){
 	 logger.fine(x);
  }
  public void config(String x){
 	 logger.config(x);
  }
  public void info(String x){
 	 logger.info(x);
  }
  public void warning(String x){
 	 logger.warning(x);
  }
  public void severe(String x){
 	 logger.severe(x);
  }
}
