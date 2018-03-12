//*************** ノードの状態に合わせてスタイルを変更するルールを記述するクラス *******************//

package jp.co.LoadMapEditor.TA;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.w3c.dom.Node;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxUtils;

public class checkNodes {

	//目標数の取得
	@SuppressWarnings("unused")
	private String TargetNum;
	
	//プロジェクトのモードのドロップダウンボックスのインデックス取得
	private int ProjectModeIndex;
	
	
	//目標グラフのXML
	private String xml;
	
	//書き換え後の目標XMLを保存するための変数
	private String GoalXmlString;
	
	//知識ネットワークグラフのデータ
	private String NetXmlString;
	
	//書き換え用の新たなXML
	private String newXml = new String();
	
	
	//表示中ノードのIDが格納されるベクトル
	private Vector<String> ids = new Vector<String>();
	//各ノードのスタイルが格納されるベクトル
	private Vector<String> styles = new Vector<String>();
	//各ノードの中身の文字列が格納されるベクトル
	private Vector<String> values = new Vector<String>();
	//各ノードの期限が格納されるベクトル
	private Vector<String> deadlines = new Vector<String>();
	//取得するベクトルのXML上での行数
	private Vector<Integer> lines = new Vector<Integer>();
	//位置情報が格納されるベクトル
	private Vector<String> geoXs = new Vector<String>();
	private Vector<String> geoYs = new Vector<String>();
	
	//********** エッジの情報に関するパラメータ ***************//
	//エッジの元を格納するベクトル（エッジが出ているところで、こっちが親ノードのID）
	private Vector<String> edgesIdSource = new Vector<String>();
	//エッジの先を格納するベクトル（エッジが入っているところで、こっちが子ノードのID）
	private Vector<String> edgesIdTarget = new Vector<String>();
		
	//********* 複合情報パラメータ ***************//
	//特定の親ノードにたいする子ノードの数
	private Vector<Integer> childNums = new Vector<Integer>();
	
	
	
	
	//ノードの情報を取得するのに必要な文字中の位置情報
	private int indexAfter;
	private int indexBefore;
	private int indexStyleBefore;
	private int indexStyleAfter;
	private int indexValuesBefore;
	private int indexValuesAfter;
	private int indexGeoXBefore;
	private int indexGeoXAfter;
	private int indexGeoYBefore;
	private int indexGeoYAfter;
	
	//ループ用パラメータ
	private int i;
	private int j;
	private int k;
	private int l;
	
	//一時的な子ノード数
	private int node_cnt = 0;
		
	//期限と現在の日時の差を格納する変数
	private int dist;
	
	
	//コンストラクタ
	public checkNodes(MyGraph grf){
		//目標数取得
		//けどこれはnode_cntがあるから使わんかも
		TargetNum = frmInit.getTargetNum();
		
		//プロジェクトのモードを取得
		ProjectModeIndex = frmInit.getProjectMode();
		
		
		//目標グラフのXML取得 into xml
		this.setGoalXml(grf);
		
		//メインの書き換えメソッド
		this.changeNewXml();

		//目標数の再セット
		this.setNewTargetNum(ids.size());
		
		//保存
		this.saveNewXmls();
		
		//アラート表示
		this.AlertPopup();
	}
	
	//*********
	//***** メインの処理
	//*********
	public void changeNewXml(){
		//目標グラフXMLを一行ごとに保存
		String[] xmlDash = xml.split("\n");
								
		
		//表示中のXMLの行数分だけループ
		for(i=0; i<xmlDash.length; i++){
			
			
			
			
			//************
			//******** ノード情報の取得
			//************
			this.setXmlTexts(xmlDash, i);
			
			

		}
		
		
		//*********
		//***** 期限の取得
		//*********
		j=0;
		for(i=0; i<ids.size(); i++){
			this.getDeadLine(i, j);
		}
		
		
		
		//***********
		//****** ノードの情報を書き換える
		//***********
		j=0;
		k=0;
		for(i=0; i<ids.size(); i++){
			
			//************
			//****** 期限による書き換え
			//************
			if(j < deadlines.size()){
				//２つの日付（期限と今の時間）の差
				this.getDateDist(xmlDash, i);
				j++;
			}
			
			//************
			//******** 状態ノードの位置の書き換え
			//************
			
			if(k < geoXs.size()){
				this.getGeoSet(xmlDash, i, k);
				
			}
			k++;
		}
		
		for(i=0; i<ids.size(); i++){
			this.setChildNums(i);
		}
		
		//新しいXMLテキストを作成
		for(i=0; i<xmlDash.length; i++){
			newXml += xmlDash[i]+"\n";
		}
		
		//表示を切り替える
		GoalXmlString = new String(newXml);
	}
	
	/////////******************** データの取得に必要なメソッド（いらんかも） ***************************//////////
	
	//*********
	//**** 特定IDに対するデータの取得
	//*********
	
	//IDに対する子ノードの数を取得するメソッド
	public int getChildsFromId(String ID){
		int nums = childNums.elementAt(getIndexFromId(ID));
		return nums;
	}
	
	//IDに対する各情報のindexを取得するメソッド
	protected int getIndexFromId(String ID){
		return ids.lastIndexOf(ID);
	}
	

	
	
	/////////******************** コンストラクタに絶対必要なメソッド ***************************//////////
	
	
	//***********
	//***** 指定した目標グラフをXMLにエンコードするメソッド
	//************
	protected void setGoalXml(MyGraph grf){
		mxCodec codec = new mxCodec();
		Node node = codec.encode(grf.getModel());
		StringBuffer GoalXml = new StringBuffer(mxUtils.getPrettyXml(node));
		xml = new String(GoalXml);
	}
	
	
	
	//*********
	//******* DBに新しいXMLを保存するメソッド
	//*********
	protected void saveNewXmls(){
		//ネットワークグラフのコーデック
		mxCodec codec1 = new mxCodec();
		Node node1 = codec1.encode(MyNetworkModelEditor.getGraph().getModel());
		StringBuffer NetXml = new StringBuffer(mxUtils.getPrettyXml(node1));
		NetXmlString = new String(NetXml);
		
		sendSQL input = new sendSQL();
		//プロジェクトIDごとに目標XMLとネットワークXMLが保存
		input.insertGraphTreeTable(loadMapEditor.getProjectId(), GoalXmlString, NetXmlString, frmInit.getProjectMode());
		MyGraphicEditor MGE = new MyGraphicEditor();
		MGE.setImportGraph(loadMapEditor.getProjectId());
		MyGraphicEditor.getComponent().refresh();
	}
	
	//***********
	//******* 変数xmlDashに文字列を格納するメソッド
	//***********
	protected void setXmlTexts(String[] xmlDash, Integer i){
		String idBefore = null;
		String GeoBefore = null;
		
		//****** ダミーノード、状態表示ノード、アンドノード、謎の第１、第２ノードの排除 ********//
		if(xmlDash[i].startsWith("    <mxCell id=") && !(xmlDash[i].startsWith("    <mxCell id=\"0\"")) && !(xmlDash[i].startsWith("    <mxCell id=\"1\"")) && !(xmlDash[i].startsWith("    <mxCell id=\"dammy_")) && !(xmlDash[i].startsWith("    <mxCell id=\"and_")) && !(xmlDash[i].startsWith("    <mxCell id=\"mode_"))){
			
			
			//ID部分の取り出し
			indexBefore = xmlDash[i].indexOf("id");
			indexAfter = xmlDash[i].indexOf("\" parent");
			//IDの行格納
			idBefore = xmlDash[i];
			//ジオメトリの行格納
			GeoBefore = xmlDash[i+1];
				
			//スタイルに関するINDEX取得
			indexStyleBefore = xmlDash[i].indexOf("style=\"");
			indexStyleAfter = xmlDash[i].indexOf("\" value=");
			
			//中身の文字列に関する取得
			indexValuesBefore = xmlDash[i].indexOf("\" value=\"");
			indexValuesAfter = xmlDash[i].indexOf("\" vertex=");
			
			//Geometoryに関するINDEX取得
			indexGeoXBefore = xmlDash[i+1].indexOf("x=\"");
			indexGeoXAfter = xmlDash[i+1].indexOf("\" y=\"");
			indexGeoYBefore = xmlDash[i+1].indexOf("y=\"");
			indexGeoYAfter = xmlDash[i+1].indexOf("\"/>");
			
			
			//********* よくわからないミスったノードを排除 **********//
			if(!(Double.valueOf(idBefore.substring(indexBefore+4, indexAfter)) <= Double.valueOf("100000000000000000"))){
				
				//ID格納
				ids.addElement(idBefore.substring(indexBefore+4, indexAfter));
				
				//スタイル格納
				styles.addElement(idBefore.substring(indexStyleBefore+7, indexStyleAfter));
				
				//文字列格納
				values.addElement(idBefore.substring(indexValuesBefore+9, indexValuesAfter));
				
				//位置情報格納
				geoXs.addElement(GeoBefore.substring(indexGeoXBefore+3, indexGeoXAfter));
				geoYs.addElement(GeoBefore.substring(indexGeoYBefore+3, indexGeoYAfter));
				
				//i番目のデータが、XML上で何行目を指しているのかの情報を格納
				lines.addElement(i);
			}
		}
	}
	
	
	//***********
	//******* 与えられた日時文字列と現在の日時を比較しその差を出すメソッド
	//***********
	protected void getDateDist(String[] xmlDash, Integer i){
		//書き換え前の前半部分
		String before = new String();
		//書き換え前の後半部分
		String after = new String();
		//新しく書き込むスタイル文字列
		String newStyleLine = new String();
		
		
		//現在の日時を取得（最終更新日）
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String dateDash = format.format(date);	
		
		//差を求める日時
		String nowDate = deadlines.elementAt(i);
		
		//日付を表す文字列を、求めるDate型に
		Date formatDateNow = null;
		Date formatDate = null;		
		try {
			formatDateNow = format.parse(dateDash.toString());
			formatDate = format.parse(nowDate);
		} catch (ParseException e) {}	
	
		
		
		long date1 = formatDate.getTime();
		long date2 = formatDateNow.getTime();	
		long one_date_time = 1000 * 60 * 60 * 24;
		long distTimes = (date2 - date1) / one_date_time;
		dist = (int)distTimes;
		
		if(dist <= 3 && dist >= 1){	
			
			indexStyleBefore = xmlDash[lines.elementAt(i)].indexOf("style=\"");
			indexStyleAfter = xmlDash[lines.elementAt(i)].indexOf("\" value=");
			
			before = xmlDash[lines.elementAt(i)].substring(0, indexStyleBefore+7);
			after = xmlDash[lines.elementAt(i)].substring(indexStyleAfter, xmlDash[lines.elementAt(i)].length());
			newStyleLine = before+"rounded=1;strokeColor=red;fillColor=yellow;"+after;
			
			xmlDash[lines.elementAt(i)] = newStyleLine;
		} else {
			indexStyleBefore = xmlDash[lines.elementAt(i)].indexOf("style=\"");
			indexStyleAfter = xmlDash[lines.elementAt(i)].indexOf("\" value=");
			
			before = xmlDash[lines.elementAt(i)].substring(0, indexStyleBefore+7);
			after = xmlDash[lines.elementAt(i)].substring(indexStyleAfter, xmlDash[lines.elementAt(i)].length());
			newStyleLine = before+"shape=ellipse;strokeColor=red;fillColor=lightblue;"+after;
			
			xmlDash[lines.elementAt(i)] = newStyleLine;
			
		}
	}
	
	
	
	//***********
	//****** 与えられた順目の位置情報をセットするメソッド
	//**********
	protected void getGeoSet(String[] xmlDash, Integer i, Integer k){
		//書き換え前の前半部分
		String beforeG = new String();
		//書き換え前の後半部分
		String afterG = new String();
		//新しく書き込む位置情報文字列
		String newGeoLine = new String();
		double tmpGeoX = 0;
		double tmpGeoY = 0;
		
		
		
		indexGeoXBefore = xmlDash[lines.elementAt(k)+1].indexOf("x=\"");
		
		indexGeoXAfter = xmlDash[lines.elementAt(k)+1].indexOf("\" y=\"");
		indexGeoYBefore = xmlDash[lines.elementAt(k)+1].indexOf("y=\"");
		indexGeoYAfter = xmlDash[lines.elementAt(k)+1].indexOf("\"/>");
	
		
		
		beforeG = xmlDash[lines.elementAt(k)+4].substring(0, indexGeoXBefore+3);
		afterG = xmlDash[lines.elementAt(k)+4].substring(indexGeoYAfter, xmlDash[lines.elementAt(k)+4].length());
		tmpGeoX = Double.valueOf(geoXs.elementAt(k)) + 80.0;
		tmpGeoY = Double.valueOf(geoYs.elementAt(k)) - 15.0;
		newGeoLine = beforeG+String.valueOf(tmpGeoX)+"\" y=\""+String.valueOf(tmpGeoY)+afterG;
	
		
		
		
		xmlDash[lines.elementAt(i)+4] = newGeoLine;
	}
	
	
	//**********
	//**** 与えられたIDの期限をから取得と格納を行うメソッド
	//**********
	protected void getDeadLine(Integer i, Integer j){

		sendSQL SQL = new sendSQL();
		String Query = "select deadline from goal_node_tbl where goal_node_id=\""+ids.elementAt(i)+"\"";
	
		if(j < deadlines.size()){
			//DBから各IDに対応する期限を取得する
			deadlines.addElement(SQL.getVectorSelectSQL(Query).elementAt(0));
			SQL.getResus().clear();
			
			j++;
		} else {
			deadlines.addElement("0000/00/00");
			SQL.getResus().clear();
			j++;
		}
	}
	
	//**********
	//****** 子ノードの数をカウントする
	//**********
	protected void setChildNums(Integer i){
		for(l=0; l<edgesIdTarget.size(); l++){
			
			//特定の親ノードIDに対する子ノードIDを発見すればカウント
			if(ids.elementAt(i).equals(edgesIdSource.elementAt(l))){
				node_cnt++;
			}
		}
		childNums.addElement(node_cnt);
		node_cnt = 0;
	}
	
	//**********
	//***** 目標数の再セット
	//**********
	//目標数が変わったら、数を再セット
	protected void setNewTargetNum(Integer nums){
		frmInit.setTargetNum(String.valueOf(nums));
	}
	
	
	//***********
	//****** 条件によってアラート出力
	//***********
	protected void AlertPopup(){
		if((Integer.valueOf(TargetNum) >= 5) && (ProjectModeIndex == 2)){
			String warningText = "<html><span style=\"color: black; font-size:20px;\">「今の状況は把握できている」と宣言していますが，<br />十分に計画が立てられていない可能性があり，<br />場当たり的に問題解決を行っている可能性があります．<br />一度計画の見直しをしてみてはいかがでしょうか？</span></html>";
			warningDialog WD = new warningDialog();
			WD.setWarningText(warningText);
			WD.setBounds(100, 100, 800, 500);
			WD.setVisible(true);
		}
		
		
	}
}
