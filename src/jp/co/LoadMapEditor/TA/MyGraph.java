//********* インサートするノードのスタイルなどを変更してから呼び出すためのクラス **********//

package jp.co.LoadMapEditor.TA;


import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;


/*
 *
 * 使い方
 * //**************************
 * //期限などの条件によって色を変更する
 * new checkNodes(MyGraphicEditor.getGraph());
 * //**************************
 * 
 */




public class MyGraph extends mxGraph{
	
	//新規ノードの出現場所
	private int pos_x = 2500;
	private int pos_y = 2500;
	
	//ノードのサイズ
	private final int node_size_x = 150;
	private final int node_size_y = 65;
	
	//オブジェクトの数やけどいまは使わない
	private int obj_num = 0;
	
	//ダミーオブジェクトの個数
	//これがマイト２つ目のダミーのIDが数値になる
	private int dammy_num = 0;
	
	private Object obj;
	
	//コンストラクタ
	//特になんの処理もしない
	public MyGraph(){
		
	}
	
	
	//独自ノードを生成するメソッド
	public Object insertMyVertex(Object parent, mxGraph grf, String mode, String index, String uniqueID){
		//インサートするグラフオブジェクト
		obj = new Object();
		
		//スタイル文字列
		String styleString = null;
		
		//ノードのモードによってスタイルを変化させる
		/*
		if(mode.equals("none")){
			styleString = "shape=none;";
		} else if(mode.equals("default")){
			styleString = "shape=doubleEllipse;strokeColor=blue;fillColor=lightblue;";
		} else if(mode.equals("mode1")){
			styleString = "rounded=1;fillColor=pink;";
		} else if(mode.equals("mode2")){
			styleString = "shape=ellipse;perimeter=ellipsePerimeter;strokeColor=red;fillColor=pink;";
		} else {
			return 0;
		}
		*/
		styleString = "shape=ellipse;perimeter=ellipsePerimeter;strokeColor=blue;fillColor=lightblue;";
		
		
		//文字列長が一定以上の時にOOO...で表示するための処理
		String indexOutput = null;
		if(index.length() >= 9){
			indexOutput = index.substring(0,9)+"...";
			
			//********
			//**** ノード状態を表す特殊ノード
			//*********
			//モード状態ノードのID
			String mode_id = "mode_"+uniqueID;
			//DBへの挿入
			sendSQL SQL = new sendSQL();
			String insertModeNodeQuery = "insert into goal_node_tbl values(\""+mode_id+"\", \"未設定\", \"\", \"未設定\", , \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";
			SQL.operateSQL(insertModeNodeQuery);
			//モード状態ノードのスタイル
			String mode_style = "rounded=1;fillColor=pink;";
			

			MyGraphicEditor.setYesUpdate(false);
			
			//第二引数を変更するとが変わる
			obj = grf.insertVertex(parent, uniqueID, indexOutput, pos_x, pos_y, node_size_x, node_size_y, styleString);
						
			MyGraphicEditor.setYesUpdate(true);
			
			//モード状態ノードのグラフへの挿入
			grf.insertVertex(parent, mode_id, "未設定", pos_x+70, pos_y-10, 100, 22, mode_style);
			
			
		} else if(index.length() <= 9){
			
			
			//********
			//**** ノード状態を表す特殊ノード
			//*********
			//モード状態ノードのID
			String mode_id = "mode_"+uniqueID;
			
			//DBへの挿入
			sendSQL SQL = new sendSQL();
			String insertModeNodeQuery = "insert into goal_node_tbl values(\""+mode_id+"\", \"未設定\", \"\", \"未設定\", , \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";
			SQL.operateSQL(insertModeNodeQuery);
			
			//モード状態ノードのスタイル
			String mode_style = "rounded=1;fillColor=pink;";
			
			

			MyGraphicEditor.setYesUpdate(false);
			
			obj = grf.insertVertex(parent, uniqueID, index, pos_x, pos_y, node_size_x, node_size_y, styleString);
			

			MyGraphicEditor.setYesUpdate(true);
			//モード状態ノードのグラフへの挿入
			grf.insertVertex(parent, mode_id, "未設定", pos_x+70, pos_y-10, 100, 22, mode_style);
			
			
			
		} else if(frmInit.getRightUpTabIndex() >= 1){
			
		} else {
			obj = grf.insertVertex(parent, uniqueID, index, pos_x, pos_y, node_size_x, node_size_y, styleString);
		}
		
		//これが追加されるオブジェクト
		return obj;
	}
	
	
	//ノード生成ポリモーフィズム
	//座標指定可能メソッド
	//独自ノードを生成するメソッド
	public Object insertMyVertex(Object parent, mxGraph grf, String mode, String index, String uniqueID, int POSX, int POSY){
			
		//インサートするグラフオブジェクト
		Object obj = new Object();
		
		//スタイル文字列
		String styleString = "shape=ellipse;perimeter=ellipsePerimeter;strokeColor=blue;fillColor=lightblue;";
		
		
		
		//ノードのモードによってスタイルを変化させる
		/*
			if(mode.equals("none")){
				styleString = "shape=none;";
			} else if(mode.equals("default")){
				styleString = "shape=doubleEllipse;strokeColor=blue;fillColor=lightblue;";
			} else if(mode.equals("mode1")){
				styleString = "rounded=1;fillColor=pink;";
			} else if(mode.equals("mode2")){
				styleString = "shape=ellipse;perimeter=ellipsePerimeter;strokeColor=red;fillColor=pink;";
			} else {
				styleString = "shape=triangle;strokeColor=blue;fillColor=lightblue;";
			}
		 */
			
		//文字列長が一定以上の時にOOO...で表示するための処理
		String indexOutput = null;
		if(index.length() >= 9){
			indexOutput = index.substring(0,9)+"...";
			
			//********
			//**** ノード状態を表す特殊ノード
			//*********
			//モード状態ノードのID
			String mode_id = "mode_"+uniqueID;
		
			//DBへの挿入
			sendSQL modeSQL = new sendSQL();
			String insertModeNodeQuery = "insert into goal_node_tbl values(\""+mode_id+"\", \"未設定\", \"未設定\", \"未設定\", \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";
			modeSQL.operateSQL(insertModeNodeQuery);	
			//モード状態ノードのスタイル
			String mode_style = "rounded=1;fillColor=pink;";
				
			

			MyGraphicEditor.setYesUpdate(false);
			//第二引数を変更するとが変わる
			//データベースを参照して現在使われていないIDを指定する(被らないIDを作成する)
			obj = grf.insertVertex(parent, uniqueID, indexOutput, POSX, POSY, node_size_x, node_size_y, styleString);	
			
			

			MyGraphicEditor.setYesUpdate(true);
			//モード状態ノードのグラフへの挿入
			grf.insertVertex(parent, mode_id, "未設定", POSX+70, POSY-10, 100, 22, mode_style);	
				
			
			
			
			
			//挿入が終わったらデータベースに保存する
		} else if(index.length() <= 9){
			
				
			//********
			//**** ノード状態を表す特殊ノード
			//*********
			//モード状態ノードのID
			String mode_id = "mode_"+uniqueID;
			
			//DBへの挿入
			sendSQL modeSQL = new sendSQL();
			String insertModeNodeQuery = "insert into goal_node_tbl values(\""+mode_id+"\", \"未設定\", \"未設定\", \"未設定\", \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";
			modeSQL.operateSQL(insertModeNodeQuery);
			
			//モード状態ノードのスタイル
			String mode_style = "rounded=1;fillColor=pink;";
			
			

			MyGraphicEditor.setYesUpdate(false);
			//モード状態ノードのグラフへの挿入
			obj = grf.insertVertex(parent, uniqueID, index, POSX, POSY, node_size_x, node_size_y, styleString);
			

			MyGraphicEditor.setYesUpdate(true);
			grf.insertVertex(parent, mode_id, "未設定", POSX+70, POSY-10, 100, 22, mode_style);
			
			
			
		} else if(frmInit.getRightUpTabIndex() >= 1){
				
		} else {
			//obj = grf.insertVertex(parent, uniqueID, index, POSX, POSY, node_size_x, node_size_y, styleString);
		}
			
		//これが追加されるオブジェクト
		return obj;
	}
	
	//AND素子を入力するメソッド
	public Object insertAndVertex(Object parent, mxGraph grf, int id, int POSX, int POSY){
		
		//インサートするグラフオブジェクト
		Object obj = new Object();
			
		//スタイル文字列
		String styleString = "style=rounded=1;";	
			
		//文字列長が一定以上の時にOOO...で表示するための処理
		String indexOutput = null;
				
		obj = grf.insertVertex(parent, "and_"+MyGraphicEditor.getSelectedCell().getId(), indexOutput, POSX+20, POSY+25, 10, 10, styleString);	
		
		
		return obj;
	}
	
	
	
	//画面を広げるためのダミーオブジェクトを設置するためのメソッド
	public Object insertDammyVertex(Object parent, mxGraph grf, int x, int y){
		Object obj = new Object();
		String styleString = "shape=none;";
		obj = grf.insertVertex(parent, "dammy_"+dammy_num, "", x, y, node_size_x, node_size_y, styleString);
		dammy_num++;
		return obj;	
	}
	
	
	
	//独自矢印を追加するメソッド
	public Object insertMyEdge(Object parent, mxGraph grf, String type, String index, Object par, Object chi){
		Object obj = new Object();
		String id = new String("ARROW_"+obj_num);
		if(type == "type1"){
			
		} else if(type == "type2"){
			
		}
		String style = mxConstants.STYLE_DASHED;
		obj = grf.insertEdge(parent, id, index, chi, par, style);
		
		return obj;
	}


	

	//多分ノードの数を返してくれる
	//今は全く機能していない
	public void setObjectNumber(int num){
		obj_num = num; 
	}
	
	//インサートするノードのX座標を指定するためのメソッド
	public void setX(int x){
		pos_x = x;
	}
	
	//インサートするノードY座標を指定するためのメソッド
	public void setY (int y){
		pos_y = y;
	}
	
	//引数で指定されたノードIDの状態ノードのvalue、つまりそのノードの状態を取得するメソッド
	public String getNodeModeFromId(String ID){
		
		//指定されたノードの状態ノードのID
		String searchNodeId = "mode_"+ID;
		sendSQL q = new sendSQL();
		
		if(!(ID.startsWith("mode_"))){
			String searchNodeQuery = "select goal_node_name from goal_node_tbl where goal_node_id=\""+searchNodeId+"\"";
			String Mode = q.getVectorSelectSQL(searchNodeQuery).elementAt(0);
			return Mode;
		}
		return "";
	}



	
}
