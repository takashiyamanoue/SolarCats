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
	    * ログ設定プロパティファイルのファイル名
	    */
	    protected static final String LOGGING_PROPERTIES 
	        = "gmlog.properties";
	    /*
	     * プロパティファイルがない場合の設定
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
	    * static initializer によるログ設定の初期化
	    */
	    /*
	    static {
	        final Logger logger = Logger.getLogger("NaraLogging");
		        
	        // クラスパスの中から ログ設定プロパティファイルを取得
	        logger.fine("ログ設定: " + LOGGING_PROPERTIES 
	            + " をもとにログを設定します。");
//	        InputStream inStream = NaraLogger.class
//	            .getClassLoader().getResourceAsStream(
//         LOGGING_PROPERTIES);
	        InputStream inStream =null;
	        try{
 	        inStream=new FileInputStream(LOGGING_PROPERTIES);
	        }
	        catch(Exception e){}
	        if (inStream == null) {
	            logger.info("ログ設定: " + LOGGING_PROPERTIES 
	                 + " はクラスパス上に見つかりませんでした。");
	            
//	            InputStream inStream = null;
	            try {
	                inStream = new ByteArrayInputStream(
	                    LOGGING_SETTINGS.getBytes("UTF-8"));
	                try {
	                    LogManager.getLogManager().readConfiguration(
	                        inStream);
	                    logger.config(
	                        "ログ設定: LogManagerを設定しました。");
	                } catch (IOException e) {
	                    logger.warning("ログ設定: LogManager設定の際に"
	                        + "例外が発生しました。:" + e.toString());
	                }
	            } catch (UnsupportedEncodingException e) {
	                logger.severe("ログ設定: UTF-8エンコーディングが"
	                    + "サポートされていません。:" + e.toString());
	            } finally {
	                try {
	                    if (inStream != null) inStream.close(); 
	                } catch (IOException e) {
	                    logger.warning(
	                        "ログ設定: ログ設定プロパティファイルのスト"
	                        + "リームクローズ時に例外が発生しました。:"
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
	                    "ログ設定: LogManagerを設定しました。");
	            } catch (IOException e) {
	                logger.warning("ログ設定: LogManager設定の際に"
	                    +"例外が発生しました。:"+ e.toString());
	            } finally {
	                try {
	                    if (inStream != null) inStream.close(); 
	                } catch (IOException e) {
	                    logger.warning("ログ設定: ログ設定プロパティ"
	                        +"ファイルのストリームクローズ時に例外が"
	                        +"発生しました。:"+ e.toString());
	                }
	            }
	        }
	    }
	    */
	    
	 Logger logger=null;
	 public GroupManagerLogger(){
	        logger = Logger.getLogger("GMLogging");
	        
	        // クラスパスの中から ログ設定プロパティファイルを取得
	        logger.fine("ログ設定: " + LOGGING_PROPERTIES 
	            + " をもとにログを設定します。");
//	        InputStream inStream = NaraLogger.class
//	            .getClassLoader().getResourceAsStream(
//         LOGGING_PROPERTIES);
	        InputStream inStream =null;
	        try{
 	        inStream=new FileInputStream(LOGGING_PROPERTIES);
	        }
	        catch(Exception e){}
	        if (inStream == null) {
	            logger.info("ログ設定: " + LOGGING_PROPERTIES 
	                 + " はクラスパス上に見つかりませんでした。");
	            
//	            InputStream inStream = null;
	            try {
	                inStream = new ByteArrayInputStream(
	                    LOGGING_SETTINGS.getBytes("UTF-8"));
	                try {
	                    LogManager.getLogManager().readConfiguration(
	                        inStream);
	                    logger.config(
	                        "ログ設定: LogManagerを設定しました。");
	                } catch (IOException e) {
	                    logger.warning("ログ設定: LogManager設定の際に"
	                        + "例外が発生しました。:" + e.toString());
	                }
	            } catch (UnsupportedEncodingException e) {
	                logger.severe("ログ設定: UTF-8エンコーディングが"
	                    + "サポートされていません。:" + e.toString());
	            } finally {
	                try {
	                    if (inStream != null) inStream.close(); 
	                } catch (IOException e) {
	                    logger.warning(
	                        "ログ設定: ログ設定プロパティファイルのスト"
	                        + "リームクローズ時に例外が発生しました。:"
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
	                    "ログ設定: LogManagerを設定しました。");
	            } catch (IOException e) {
	                logger.warning("ログ設定: LogManager設定の際に"
	                    +"例外が発生しました。:"+ e.toString());
	            } finally {
	                try {
	                    if (inStream != null) inStream.close(); 
	                } catch (IOException e) {
	                    logger.warning("ログ設定: ログ設定プロパティ"
	                        +"ファイルのストリームクローズ時に例外が"
	                        +"発生しました。:"+ e.toString());
	                }
	            }
	        }
	        //logger = Logger.getLogger("NaraLogging");

	 }
  public static void main(final String[] args) {
     final Logger logger = Logger.getLogger("NaraLogging");

     logger.finest("隣の客はよく柿食う客だ。");
     logger.finer("東京都特許許可局。");
     logger.fine("かえるぴょこぴょこ。");
     logger.config("庭には二羽鶏が。");
     logger.info("生麦生米生卵。");
     logger.warning("あかまきがみ。");
     logger.severe("すもももももももものうち。");
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
