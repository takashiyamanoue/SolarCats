package application.pen;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class IntVFileIO {
	private Hashtable OpenFileTable = new Hashtable();	// 開いているファイルを管理するテーブル
	private String LineSeparator =System.getProperty("line.separator");	// System に使用されている改行コードの取得
	
	public IntVFileIO(){
		// ファイルテーブルの初期化
		
		OpenFileTable.put(new Integer(0),  new Object[]{"input", "標準入力"});
		OpenFileTable.put(new Integer(1),  new Object[]{"output", "標準出力"});
		OpenFileTable.put(new Integer(2),  new Object[]{"error", "エラー出力"});
	}
	
	public Integer FileOpenRead(String FilePath){
		// 読み込みモード でファイルを開く
		// ファイルが存在しなかった場合、エラーを吐き停止
		
		File file = new File(FilePath);
		try {
			// ファイルテーブルに追加
			Integer ID = FileID();
			
			String code = getCharSet(file);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), code);
			BufferedReader reader = new BufferedReader(isr);
			
			OpenFileTable.put(ID, new Object[]{ "Read", reader });
			return ID;
		} catch (FileNotFoundException e) {
			// 指定されたパス名で示されるファイルが開けなかった時の処理
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		} catch (UnsupportedEncodingException e) {
			// エンコード処理できなかった場合
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		}
	}
	
	public Integer FileOpenWrite(String FilePath){
		// 書き込みモード でファイルをオープンする
		// ファイルが存在した場合、オーバーライトする
		
		File file = new File(FilePath);
		try {
			// ファイルテーブルに追加
			Integer ID = FileID();
			OpenFileTable.put(ID, new Object[]{ "Write", new BufferedWriter(new FileWriter(file))});
			return ID;
		} catch (IOException e) {
			// 入出力処理の失敗、または割り込みの発生
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		}
	}
	
	public Integer FileOpenAppend(String FilePath){
		// 追記モード でファイルをオープンする
		// ファイルが存在しなかった場合、ファイルを作成する
		
		File file = new File(FilePath);
		try {
			BufferedWriter bf;
			if( file.isFile() ) {
				// ファイルの内容を add に退避
				String code = getCharSet(file);
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file), code);
				BufferedReader reader = new BufferedReader(isr);
				String read = "";
				String add	= "";
				while(true){
					read = reader.readLine();
					if(read != null){
						add += read + LineSeparator;
					}else{
						reader.close();
						break;
					}
				}
				
				// FileWriter でファイルを開き、退避させたデータの書き込み
				bf = new BufferedWriter(new FileWriter(file));
				bf.write(add);
				bf.flush();
			} else {
				bf = new BufferedWriter(new FileWriter(file));
			}
			// ファイルテーブルに追加
			Integer ID = FileID();
			OpenFileTable.put(ID, new Object[]{ "Append", bf});
			return ID;
		} catch (UnsupportedEncodingException e) {
			// エンコード処理できなかった場合
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		} catch (IOException e) {
			// 入出力処理の失敗、または割り込みの発生
			// e.printStackTrace();
			Error(e.getLocalizedMessage());
			return null;
		}
	}
	
	public String FileIsFile(String FilePath){
		// ファイルが存在する場合	: true
		// ファイルが存在しない場合	: false
		
		File file = new File(FilePath);
		Boolean flag = new Boolean(file.isFile());
		
		return flag.toString();
	}
	
	public void FileReName(String FilePath1, String FilePath2){
		// ファイルのリネーム
		
		File file1 = new File(FilePath1);
		File file2 = new File(FilePath2);
		if( file1.isFile() ){
			if( file2.isFile() ){
				// リネーム先にファイルが存在する場合
				Error("リネーム先にファイルが存在します");
			} else {
				if( file1.renameTo(file2) ){
					// ファイルのリネームが成功した場合
				} else {
					// ファイルのリネームが失敗した場合
					Error("ファイルのリネームが失敗しました");
				}
			}
		} else {
			// リネーム元のファイルが存在しなかった場合
			Error("リネーム元のファイルが存在しません");
		}
	}
	
	public void FileRemove(String FilePath){
		// ファイルの削除
		
		File file = new File(FilePath);
		if( file.isFile() ){
			if( file.delete() ) {
				// ファイルの削除に成功した場合
				
			} else {
				// ファイルの削除が失敗した場合
				Error("ファイルの削除に失敗しました");
			}
		} else {
			// リネーム元のファイルが存在しなかった場合
			Error("削除するファイルが存在しません");
		}
	}
	
	public String FileGetStr(Integer ID, Integer n){
		// ID で指定されたファイルからデータを n文字 読み込む
		// Windows の場合、改行コードに注意 -> "\r\n" なので [ 2文字 ] として扱われる
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Read") ){
				BufferedReader bf = (BufferedReader) obj[1];
				int c;
				String str = "";
				try {
					// n文字分のデータ読み込み
					for( int i = 0; i < n.intValue(); i++) {
						c = bf.read();
						if( c != -1){
							str += new Character((char) c).toString();
						} else if( str.equals("")){
							return new String(new Character((char) c).toString());
						} else {
							break;
						}
					}
					return str;
				} catch (IOException e) {
					// 入出力処理の失敗、または割り込みの発生
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
					return null;
				}
			} else if( obj[0].equals("input") ){
				// コンソール画面から入力する処理をここに追加
				return null;
			} else {
				// ファイル状態が [ Write ] なファイルを読み込もうとした時の処理
				Error("ファイルID [ " + ID + " ] は WriteOnly です");
				return null;
			}
		} else {
			// 指定されたIDが存在しない場合の処理
			Error("ファイルID [ " + ID + " ] は存在しません");
			return null;
		}
	}
	
	public String FileGetLine(Integer ID){
		// ID で指定されたファイルからデータを1行読み込む
		// なお、改行コードは [ 付加してない ] 状態で返しています
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Read") ){
				BufferedReader bf = (BufferedReader) obj[1];
				try {
					// 一行分のデータを読み込み
					String str = bf.readLine();
					if( str == null ){
						int c = -1;
						return new String(new Character((char) c).toString());
					}
					return str;
				} catch (IOException e) {
					// 入出力処理の失敗、または割り込みの発生
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
					return null;
				}
			} else if( obj[0].equals("input") ){
				// コンソール画面から入力する処理をここに追加
				return null;
			} else {
				// ファイル状態が [ Write ] なファイルを読み込もうとした時の処理
				Error("ファイルID [ " + ID + " ] は WriteOnly です");
				return null;
			}
		} else {
			// 指定されたIDが存在しない場合の処理
			Error("ファイルID [ " + ID + " ] は存在しません");
			return null;
		}
	}
	
	public void FilePutLine(Integer ID, String str){
		// ID で指定されたファイルへ データ(str) に改行込みで書き込む
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Write") || obj[0].equals("Append") ){
				BufferedWriter bf = ( BufferedWriter ) obj[1];
				try {
					// ファイルへの書き込み
					bf.write(str);
					bf.newLine();
				} catch (IOException e) {
					// 入出力処理の失敗、または割り込みの発生
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
				}
			} else if( obj[0].equals("output") ){
				// コンソール画面へ出力する処理をここに追加
			} else if( obj[0].equals("error") ){
				// コンソール画面へ出力する処理をここに追加
			} else {
				// ファイル状態が [ Read ] なファイルに書き込もうとした時の処理
				Error("ファイルID [ " + ID + " ] は ReadOnly です");
			}
		} else {
			// 指定されたIDが存在しない場合の処理
			Error("ファイルID [ " + ID + " ] は存在しません");
		}
	}
	
	public void FilePutString(Integer ID, String str){
		// ID で指定されたファイルへ データ(str) を書き込む
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Write") || obj[0].equals("Append") ){
				BufferedWriter bf = ( BufferedWriter ) obj[1];
				try {
					// ファイルへの書き込み
					bf.write(str);
				} catch (IOException e) {
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
					e.printStackTrace();
				}
			} else if( obj[0].equals("output") ){
				// コンソール画面へ出力する処理をここに追加
			} else if( obj[0].equals("error") ){
				// コンソール画面へ出力する処理をここに追加
			} else {
				// ファイル状態が [ Read ] なファイルに書き込もうとした時の処理
				Error("ファイルID [ " + ID + " ] は ReadOnly です");
			}
		} else {
			// 指定されたIDが存在しない場合の処理
			Error("ファイルID [ " + ID + " ] は存在しません");
		}
	}
	
	public void FileFlush(Integer ID){
		// ストリームをフラッシュ

		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			if( obj[0].equals("Write") || obj[0].equals("Append") ){
				BufferedWriter bf = ( BufferedWriter ) obj[1];
				try {
					// ストリームをフラッシュ
					bf.flush();
				} catch (IOException e) {
					// 入出力処理の失敗、または割り込みの発生
					// e.printStackTrace();
					Error(e.getLocalizedMessage());
				}
			} else {
				// ファイル状態が [ Read ] なファイルに書き込もうとした時の処理
				Error("ファイルID [ " + ID + " ] は ReadOnly です");
			}
		} else {
			// 指定されたIDが存在しない場合の処理
			Error("ファイルID [ " + ID + " ] は存在しません");
		}
	}
	
	public void FileClose(Integer ID){
		// ファイルのクローズ処理
		// FileIDRemove(ID) で閉じたファイルを FileTable から削除
		
		if( OpenFileTable.get(ID) != null){
			Object[] obj = (Object[]) OpenFileTable.get(ID);
			try {
				// ファイルのクローズ処理
				// ファイルテーブルからIDを削除
				if( obj[0].equals("Read") ){
					BufferedReader bf = ( BufferedReader ) obj[1];
					bf.close();
					FileIDRemove(ID);
				} else if( obj[0].equals("Write") || obj[0].equals("Append")){
					BufferedWriter bf = ( BufferedWriter ) obj[1];
					bf.flush();
					bf.close();
					FileIDRemove(ID);
				} else {
					FileIDRemove(ID);
				}
			} catch (IOException e) {
				// 入出力処理の失敗、または割り込みの発生
				// e.printStackTrace();
				Error(e.getLocalizedMessage());
			}
		} else {
			// 指定されたIDが存在しない場合の処理
			Error("ファイルID [ " + ID + " ] は存在しません");
		}
	}
	
	public Integer FileID(){
		// 空いている FileID 番号の若い順から調べる

		int i;
		for( i = 3; true; i++){
			if( OpenFileTable.get(new Integer(i)) == null ){
				break;
			}
		}
		return new Integer(i);
	}
	
	public void FileIDRemove(Integer ID){
		// FileTable から FileID を開放する処理

		OpenFileTable.remove(ID);
	}
	
	public void FileAllClose(){
		// オープンされているファイルをすべて閉じる
		
		Object[] obj = OpenFileTable.keySet().toArray();
		
		for(int i = 0; i < obj.length; i++){
			Integer ID = ( Integer ) obj[i];
			FileClose(ID);
		}
	}
	
	public void Error(String msg){
		MainGUI.Run_flag = false;
		new ConsoleAppend("### " + MainGUI.run_point.getLineCount() + "行目でエラーです\n");
		new ConsoleAppend("### " + msg + "\n");
		FileAllClose();
		throw new ThreadRunStop();
	}

	public static String getCharSet(File file) {
		String code = "EUC_JP";
		FileInputStream in = null;
		int nch;

		try {
			in = new FileInputStream(file);
			while((nch=in.read())!=-1){
				if(nch==0x1B){
					code = "JIS";
					break;
				} else if(nch>0x80 && nch<0xA1){
					code = "Shift_JIS";
					break;
				}
			}
			in.close();
		} catch(IOException ex){
		} 
		return code;
	}
}
