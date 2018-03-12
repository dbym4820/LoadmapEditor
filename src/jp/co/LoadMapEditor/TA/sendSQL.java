//******** データベースにアクセスするためのクラス ********//
//何か変化があるたびに更新する？


package jp.co.LoadMapEditor.TA;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
 
public class sendSQL {

	public static ResultSet rs;//クエリ発行後の実行結果ID
	String res;//実行結果を１行分だけ保存しておく用
	Vector<String> resus = new Vector<String>();//実行結果をまとめて保存しておくよう。配列の数は予め宣言が必要。
	
	
	Connection connection = null;
    Statement statement = null;

    /**************
     * 
     * SQL送信サンプル
     *
     **************
    public static void main(String[] args){
    	String goal_node_id = "201512040018000000"; 
    	String que = "select project_XML from graph_xml_tbl where goal_node_id = \""+goal_node_id+"\"";
    	sendSQL sql = new sendSQL();
    	String back = sql.selectSQL(que);
    	System.out.println(back);
    }
    */
    
    //コンストラクタ
    //SQLiteとのコネクトをインスタンス化するためにだけ使う
    public sendSQL(){
    }
    
    //SELECT文を送信して一つのデータだけを取ってくるメソッド
    public Vector<String> getVectorSelectSQL(String query){
    	try {
    		
    		//JDBCクラスの呼び出し
    		Class.forName("org.sqlite.JDBC");
 
    		//ルートディレクトリをユーザ環境の絶対パスで取得(jar対策)
    		String curUrl = new File(".").getAbsoluteFile().getParent();
    		
    		//JDBCによるSQLiteデータベースへのアクセス
    		connection = DriverManager.getConnection("jdbc:sqlite:"+curUrl+"/database/LME.sqlite");
		
    		//コネクションに対するステートメント（SQLの大枠的なもの）
    		//現在は特にステートメントを作成してはいない
            statement = connection.createStatement();
            
            //クエリ実行・結果のリターン
    		rs = statement.executeQuery(query);
        
    		//文字列の取得
    		while (rs.next()) {
    			//ループ時のの1行を変数に格納
    			res = rs.getString(1);
    			
    			//各ループ時に取得した文字列をベクタ型オブジェクトに格納(後で配列的に参照できるように)
    			resus.addElement(res);
    		}
    	} catch (ClassNotFoundException e) {
    	} catch (SQLException e) {
    	} finally {
    		try {
    			//ステートメントを切断
    			if (statement != null) {
    				statement.close();
    			}
    		} catch (SQLException e) {
    			
    		}
    		try {
    			//SQLiteとのコネクションを切断
    			if (connection != null) {
    				connection.close();
    			}
    		} catch (SQLException e) {
    			
    		}
    	}

    	//クエリから返ってきたデータを文字列に保存(この時の型は文字列配列などではないので1つのデータしか取れないメソッドになっている）
    	//引数は取得されたデータの番号（何番目のデータか）を表しているが基本的には（正しくクエリが記述できていれば）1番目のデータ(getIndexNum==0)になるはず
    	return resus;
    }
    
    
    
    
    //SQLiteの操作だけで返り値がないクエリの送信は全てこれ
    //記述はほぼselectSQLメソッドと同じ。返り値なし
    public void operateSQL(String query){
    	try {
    		Class.forName("org.sqlite.JDBC");
        
        
    		String curUrl = new File(".").getAbsoluteFile().getParent();
    		
    		connection = DriverManager.getConnection("jdbc:sqlite:"+curUrl+"/database/LME.sqlite");
		
                   
    		statement = connection.createStatement();
    		rs = statement.executeQuery(query);
        
    		
    	} catch (ClassNotFoundException e) {
    	
    	} catch (SQLException e) {
    	
    	} finally {
    		try {
    			if (statement != null) {
    				statement.close();
    			}
    		} catch (SQLException e) {
    			
    		}
    		try {
    			if (connection != null) {
    				connection.close();
    			}
    		} catch (SQLException e) {
    		
    		}
    	}

    }
    
    //複数回答(Vector)を返すためのクエリを受け取り実行するクラス
    public String selectSQL(String query){
    	try {
    		
    		//JDBCクラスの呼び出し
    		Class.forName("org.sqlite.JDBC");
 
    		//ルートディレクトリをユーザ環境の絶対パスで取得(jar対策)
    		String curUrl = new File(".").getAbsoluteFile().getParent();
    		
    		//JDBCによるSQLiteデータベースへのアクセス
    		connection = DriverManager.getConnection("jdbc:sqlite:"+curUrl+"/database/LME.sqlite");
		
    		//コネクションに対するステートメント（SQLの大枠的なもの）
    		//現在は特にステートメントを作成してはいない
            statement = connection.createStatement();
            
            //クエリ実行・結果のリターン
    		rs = statement.executeQuery(query);
        
    		//文字列の取得
    		while (rs.next()) {
    			//ループ時のの1行を変数に格納
    			res = rs.getString(1);
    			
    			//各ループ時に取得した文字列をベクタ型オブジェクトに格納(後で配列的に参照できるように)
    			resus.addElement(res);
    		}
    	} catch (ClassNotFoundException e) {
    	} catch (SQLException e) {
    	} finally {
    		try {
    			//ステートメントを切断
    			if (statement != null) {
    				statement.close();
    			}
    		} catch (SQLException e) {
    			
    		}
    		try {
    			//SQLiteとのコネクションを切断
    			if (connection != null) {
    				connection.close();
    			}
    		} catch (SQLException e) {

    		}
    	}

    	//クエリから返ってきたデータを文字列に保存(この時の型は文字列配列などではないので1つのデータしか取れないメソッドになっている）
    	//引数は取得されたデータの番号（何番目のデータか）を表しているが基本的には（正しくクエリが記述できていれば）1番目のデータ(getIndexNum==0)になるはず
    	String ret = resus.elementAt(0); 
    	return ret;
    }
    
    
    
    
    
    
    
    
    //ノードIDから分割目標のXMLテキストを取得するメソッド
	public String getProjectXmlByID(String goal_node_id){
		String xmlText = "";
		String selectQuery = "select project_xml from graph_xml_tbl where project_id = \""+goal_node_id+"\"";
		xmlText = this.selectSQL(selectQuery);
		
		return xmlText;
	}
	
	//ノードIDから知識ネットワークのXMLテキストを取得するメソッド
	public String getNetworkXmlByID(String network_node_id){
		String xmlText = "";
		String selectQuery = "select network_xml from graph_xml_tbl where project_id = \""+network_node_id+"\"";
		xmlText = this.getVectorSelectSQL(selectQuery).elementAt(0);
			
		return xmlText;
	}
	
	//特定プロジェクトのIDに対してそのプロジェクトのグラフと知識ネットワークのグラフを挿入するメソッド
	public void insertGraphTreeTable(String project_id, String project_XML, String network_XML, int project_mode){
			
		//指定したプロジェクトIDと同じIDをもつデータの数を返す
		//テーブルの制約としてプロジェクトIDはプライマリキーなので実質返り値の可能性は０か１しかない。それ以外はバグ
		String sql = "select count(*) from graph_xml_tbl where project_id=\'"+project_id+"\'";
		sendSQL count = new sendSQL();
		String cn = count.selectSQL(sql);
		int cnInt = Integer.parseInt(cn); 
		String insertQuery;
		//プロジェクトIDが存在していないならinsert、あるならupdate
		if(cnInt == 0){
			 insertQuery= "insert into graph_xml_tbl values(\""+project_id+"\", \'"+project_XML+"\', \'"+network_XML+"\', \'"+project_mode+"\')";
			this.operateSQL(insertQuery);
		} else {
			insertQuery = "update graph_xml_tbl set project_id=\'"+project_id+"\', project_xml=\'"+project_XML+"\', network_xml=\'"+network_XML+"\', project_mode=\'"+project_mode+"\' where project_id=\""+project_id+"\"";
			this.operateSQL(insertQuery);	
		}	
		
	}
	
	//ベクトルのゲッターメソッド
	public Vector<String> getResus(){
		return resus;
	}
}