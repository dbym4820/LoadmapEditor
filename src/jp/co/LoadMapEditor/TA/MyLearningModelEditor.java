//*********************************
//******* このクラスファイルは学習プランエディタを作成するためのもの
//***********************************


package jp.co.LoadMapEditor.TA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;


public class MyLearningModelEditor extends JPanel{	
	
	private static final long serialVersionUID = 1L;

	//インスタンス化された回数を記憶する変数
	@SuppressWarnings("unused")
	private static int instanceCount = 0;
	
	//貼り付けるパネル
	//本体
	private JPanel graphicEditor = new JPanel();

	//表示しているグラフオブジェクト
	private static MyGraph grf;
	
	//パネルに貼り付けるコンポーネント
	private static mxGraphComponent component;
	
	
	
	//おそらく台紙的なもの
	@SuppressWarnings("unused")
	private static Object parent;
	
	//セレクト中のノード名
	private String nodeName;
	
	//一時オブジェクト
	private Object obj1 = new Object();
	private Object obj2 = new Object();
	@SuppressWarnings("unused")
	private Object obj3 = new Object();
	
	//選択中のノードを把握するやつ
	private static mxCell SelectedCell;
	
	//デフォルトの画面表示位置を設定する変数
	private int defViewPos_x = 1800;
	private int defViewPos_y = 2300;
	
	//右下のダミーノードの位置を設定
	//デフォルトの画面の大きさになる
	private int grfEditX = 5000;
	private int grfEditY = 5000;
	
	//多分ノード数をカウントする変数
	private int obj_num = 0;
	
	//編集中の学習プランがどの分割目標ノードに関するものかを保存しておくための変数
	private static String editTargetId;
	
	
	//右クリックポップアップ設定
	JPopupMenu popup;
	
	//水平スクロールバー
	@SuppressWarnings("unused")
	private JScrollBar holScroll;
	
	//その位置
	private int holPos;
	//ボタンが押された最初のx位置
	private int firstHol;
	
	//垂直スクロールバー
	@SuppressWarnings("unused")
	private JScrollBar verScroll;
	//その位置
	private int verPos;
	//ボタンが押された最初のy位置
	private int firstVer;
	
	
	//*********************************************
	//******************************
	//***************************** グラフを書き換えるための変数群
	//******************************
	//***********************************************
	//学習プラングラフのXML
	private String xml;
	private String LearnXmlString = new String();	
	//書き換え用の新たなXML
	private String newXml = new String();
	//表示中ノードのIDが格納されるベクトル
	private Vector<String> ids = new Vector<String>();
	//各ノードのスタイルが格納されるベクトル
	private Vector<String> styles = new Vector<String>();
	//各ノードの中身の文字列が格納されるベクトル
	private Vector<String> values = new Vector<String>();
	//各ノードの期限が格納されるベクトル
	@SuppressWarnings("unused")
	private Vector<String> deadlines = new Vector<String>();
	//取得するベクトルのXML上での行数
	private Vector<Integer> lines = new Vector<Integer>();
	//位置情報が格納されるベクトル
	private Vector<String> geoXs = new Vector<String>();
	private Vector<String> geoYs = new Vector<String>();
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
	@SuppressWarnings("unused")
	private int j;
	private int k;
	@SuppressWarnings("unused")
	private int l;
	
	sendSQL sql = new sendSQL();
	String planQuery = new String();
	String textSQL = new String();
	
	//************************
	//*************
	//********** アンドゥマネージャー
	//*************
	//*************************
	private mxUndoManager undoManager = new mxUndoManager();
	private mxIEventListener undoHandler = new mxIEventListener(){
		@Override
		public void invoke(Object source, mxEventObject evt)
		{
			undoManager.undoableEditHappened((mxUndoableEdit)evt.getProperty("edit"));
		}
	};
	
	
	//コンストラクタ
	public MyLearningModelEditor() {
		grf = new MyGraph();
		component = new mxGraphComponent(grf);
		holScroll = component.getHorizontalScrollBar();
		verScroll = component.getVerticalScrollBar();
		parent = getParent();
		
		//アンドゥハンドラ
		grf.getModel().addListener(mxEvent.UNDO, undoHandler);
		grf.getView().addListener(mxEvent.UNDO, undoHandler);

		//リドゥハンドラ
		grf.getModel().addListener(mxEvent.REDO, undoHandler);
		grf.getView().addListener(mxEvent.REDO, undoHandler);
		
		
		
		this.setInheritsPopupMenu(true);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		this.setAutoscrolls(true);
		this.setLayout(new BorderLayout(0, 0));
		
		grf.setAllowLoops(false);
		//grf.setAllowDanglingEdges(false);
		//grf.setCellsResizable(false);
		//grf.setAllowNegativeCoordinates(false);
		//grf.setMultigraph(true);
		//grf.setCellsCloneable(true);
		//grf.setCellsMovable(false);
		//grf.setAutoOrigin(false);
		//grf.setCellsBendable(false);
		grf.setCellsCloneable(true);
		grf.setGridEnabled(true);
		grf.setGridSize(20);
		//grf.setConnectableEdges(false);
		//grf.setEdgeLabelsMovable(false);
		//grf.setResetViewOnRootChange(true);
		//grf.setHtmlLabels(true);
		grf.setExtendParents(true);
		grf.setExtendParentsOnAdd(true);
		//grf.setCloneInvalidEdges(false);
		//grf.setLabelsVisible(false);
		//grf.setResetEdgesOnConnect(false);
		grf.setSplitEnabled(false);
		//grf.setCellsLocked(false);
		//grf.setAutoSizeCells(true);
		//grf.setCellsDisconnectable(false);
		//grf.setDefaultParent(parent);
		component.setImportEnabled(true);
		component.setAntiAlias(true);
		
		
		//画面を拡大するためのダミーノード
		
		obj1 = grf.insertDammyVertex(getParent(), grf, 0, 0);	
		obj1 = grf.insertDammyVertex(getParent(), grf, grfEditX, grfEditY);
		
		
				
		this.add(addPopup());
	}
	
	
	public JPanel setEditor(){

		graphicEditor.setLayout(new BorderLayout(0, 0));
		
		//****
		//*** ここからツールバー
		//****
		EditorToolBar ETB = new EditorToolBar();
		ETB.setFloatable(false);
		graphicEditor.add(ETB.setoolBar(), BorderLayout.NORTH);

		
		
		
		//*******
		//**** 描画
		//*******
		//コンポーネント作成・設定
		component.getViewport().setBackground(Color.WHITE);
		component.getViewport().setViewPosition(new Point(defViewPos_x, defViewPos_y));
		
		
		//ノードのないところに矢印を持って行っても矢印が消える
		component.getConnectionHandler().setCreateTarget(false);
		
		//マウスホイールリスナ
		component.getGraphControl().addMouseWheelListener(new MouseAdapter(){
			@Override
			public void mouseWheelMoved(MouseWheelEvent e){
				//コントロールマスクがある時
				if(e.isControlDown()){
					//ホイールが下に行った時
					if(e.getWheelRotation() > 0 ){
						component.zoomOut();
					}
					//ホイールが上に行った時
					if(e.getWheelRotation() < 0 ){
						component.zoomIn();
					}
				} else {
					//コントロールが押されていない場合
					firstHol = component.getHorizontalScrollBar().getValue();
					firstVer = component.getVerticalScrollBar().getValue();
					//ホイールが下に行った時
					if(e.isShiftDown()){
						holPos = component.getHorizontalScrollBar().getValue();
						component.getHorizontalScrollBar().setValue(holPos-e.getWheelRotation());
					} else {					 
						verPos = component.getVerticalScrollBar().getValue();
						component.getVerticalScrollBar().setValue(verPos-e.getWheelRotation());
					}						 
				}
			}
		});
				
		//マウスリスナ
		component.getGraphControl().addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent e){
				//クリックしたところにノードがあるか判定
				Object cell = component.getCellAt(e.getX(), e.getY());
				if (cell != null){
					nodeName = grf.getLabel(cell);
					
					//メイン画面の左側のノード名のフィールドにノード名を入れる
					//のちのちノードIDからデータベースを探して元のノード名を取得してそれを表示させる
					//理由はOOO...になったときそのまま表示されてしまうから
					frmInit.setNameField(nodeName);
					SelectedCell = (mxCell)cell;
					

				} else {
					//クリックしたとこに何もない時の処理
				}
			
			
				//右クリック処理
				if((e.getModifiers() & java.awt.event.MouseEvent.BUTTON3_MASK) != 0){
					
					//ポップアップメニューの表示
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			
			//マウスが押された時の処理
			@Override
			public void mousePressed(MouseEvent e){
				
				firstHol = e.getX();
				firstVer = e.getY();
			}
			
			//マウスが離れた時の処理
			@Override
			public void mouseReleased(MouseEvent e){}
		});
		
		//マウスモーションリスナ
		component.getGraphControl().addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){}
			@Override
			public void mouseDragged(MouseEvent e){
				//コントロールキーが押されていたとき
				//画面のスクロールをする
				if(e.isControlDown()){
					verPos = component.getVerticalScrollBar().getValue();
					holPos = component.getHorizontalScrollBar().getValue();
					int moveHol = e.getX() - firstHol;
					int moveVer = e.getY() - firstVer;
					component.getHorizontalScrollBar().setValue(holPos-moveHol);
					component.getVerticalScrollBar().setValue(verPos-moveVer);
				}
			}
		});
		
		
		graphicEditor.setMinimumSize(new Dimension(1200, 2000));
		
		//詳細設定
		grf.setMultigraph(false);
		grf.setAllowDanglingEdges(false);
		component.setConnectable(true);
		component.setToolTips(true);
		
		//範囲選択の設定
		new mxRubberband(component);
		//デリートキーとかを使えるように設定
		new mxKeyboardHandler(component);
		//グリッドラインを見えるように設定
		component.setGridVisible(true);
		
		//グラフが何か変化した時の処理
		grf.getModel().addListener(mxEvent.CHANGE, new mxIEventListener(){
							
			@Override
			public void invoke(Object sender, mxEventObject evt){

				
				new dataInsert();
				
				insertDataIntoDB();
				M();
				
				//編集処理をした時にノードの情報をDBに保存するための処理
				/*
				String updateQuery = "update learning_node_tbl set plan_node_name=\""+SelectedCell.getValue()+"\" where plan_node_id=\""+SelectedCell.getId()+"\"";
				sendSQL SQL = new sendSQL();
				SQL.operateSQL(updateQuery);
				*/
				
			}
		});
		


		graphicEditor.add(component);
		
		return graphicEditor;
	}
	
	

	//ノードもしくはエッジをセットするメソッド。のちのちこれを分割してノードのメソッドとエッジのメソッドを造る。もしくは別クラスを用意する
	public Object setMyVertex(){
		//ID用の文字列生成
		//UNIX時間のナノ秒をString型に直したものを利用(人間が使ってほぼ一意になる)
		createUniqueIDfromUnixTime createID = new createUniqueIDfromUnixTime();
		String uniqueID = createID.getUniqueID();		
		
		getGraphModel().beginUpdate();

		try	{
			if(obj_num  == 0){
				obj1 =grf.insertMyVertex(getParent(), grf, "default", "mode1", uniqueID);
				obj_num++;
			} else {
			
				obj2 = grf.insertMyVertex(getParent(), grf, "mode2", "mode2aaaaaaaaaaa", uniqueID);
			
				obj3 = grf.insertMyEdge(getParent(), grf, "type1", "", obj1, obj2);
				obj_num++;
				obj1 = obj2;
			}
		} finally {

			getGraphModel().endUpdate();
		}
		return obj1;
	}
	
	
	//ポリモーフィズム
	public Object setMyVertex(String mode, String node_name){
		
		//ID用の文字列生成
		//UNIX時間のナノ秒をString型に直したものを利用(人間が使ってほぼ一意になる)
		createUniqueIDfromUnixTime createID = new createUniqueIDfromUnixTime();
		String uniqueID = createID.getUniqueID();
		
		getGraphModel().beginUpdate();

		try	{
			if(obj_num  == 0){
				obj1 =grf.insertMyVertex(getParent(), grf, mode, node_name, uniqueID);
				obj_num++;
			} else {
			
				obj2 = grf.insertMyVertex(getParent(), grf, mode, node_name, uniqueID);
			
				obj3 = grf.insertMyEdge(getParent(), grf, "type1", null, obj1, obj2);
				obj_num++;
				obj1 = obj2;
			}
			
		} finally {

			getGraphModel().endUpdate();
		}
		return obj1;
	}
	
	
	
	//エッジを挿入するためのメソッド
	public Object setMyEdge(Object par, Object chi){
		getGraphModel().beginUpdate();

		try	{
				grf.insertMyEdge(getParent(), grf, null, null, par, chi);
			
		} finally {

			getGraphModel().endUpdate();
		}
		return obj1;
	}
	


	//編集画面を保存するメソッド
	public void saveGraph(){

		saveComp frm = new saveComp();
		frm.setSaveFrame();
	    frm.setBounds(300, 100, 300, 200);
	    frm.setTitle("画像の保存");
	    frm.setVisible(true);
	}
	
	//ノード名を返すメソッド
	public String getNodeName(){
		return nodeName;
	}
	
	//グラフオブジェクトを渡すメソッド
	public static MyGraph getGraph(){
		return grf;
	}
	
	//現在のグラフモデルを返すメソッド
	public static mxIGraphModel getGraphModel(){
		return grf.getModel();
	}
			
	//コンポーネントごと渡すメソッド
	public static mxGraphComponent getComponent(){
		
		return component;
	}
	

	
	//グラフオブジェクトを渡すメソッド
	public Object getPar(){
		return getParent();
	}
	
	
	//現在編集中の学習プランに関する分割目標ノードのIDをセットするメソッド
	public static void setEditTargetId(String targetId){
		editTargetId = targetId;
	}
	
	//現在編集中の学習プランに関する分割目標ノードのIDを取得するメソッド
	//これは使わないかもしれない
	public static String getEditTargetId(){
		return editTargetId;
	}
	
	
	
	//選択中のノードをmxCell型でリターンするメソッド
	public static mxCell getSelectedCell(){
		if(SelectedCell != null){
			return SelectedCell;	
		} else {
			return null;
		}
	}
	
	//多分ノードの数を変えるメソッド
	public int getObjectNum(){
		return obj_num;
	}
	
	
	
		
	//XMLファイルからのインポートを処理するメソッド
	//できれば外部クラスに置きたい処理
	//XMLファイルからのインポートを処理するメソッド
	public Object setImportGraph(){
			
			
		
		new checkLearningNodes();
		//MyLearningModelEditor MLME = new MyLearningModelEditor();
		//MyLearningModelEditor.getComponent().refresh();
		
		return obj1;
	}
	


	//右クリックで出るポップアップメニュー設定
	private JPopupMenu addPopup() {
		

		popup = new JPopupMenu();
		
		JMenuItem menuItem1 = new JMenuItem("タブを閉じる");
		menuItem1.addMouseListener(new MouseListener(){
		
			public void mouseEntered(MouseEvent e){}
		  
			public void mouseExited(MouseEvent e){}

			public void mousePressed(MouseEvent e){
				@SuppressWarnings("unused")
				MyLearningModelEditor MLME = new MyLearningModelEditor();
				frmInit.getTabbed_rightUp().removeTabAt(frmInit.getRightUpTabIndex());

			}

			public void mouseReleased(MouseEvent e){}

			public void mouseClicked(MouseEvent e){}
		  });
	
		popup.add(menuItem1);
		//JMenuItem menuItem2 = new JMenuItem("以降の木を折りたたむ");
		//popup.add(menuItem2);
		JMenuItem menuItem3 = new JMenuItem("コピー");
		menuItem3.addMouseListener(new MouseListener(){
		
			public void mouseEntered(MouseEvent e){}
		  
			public void mouseExited(MouseEvent e){}

			public void mousePressed(MouseEvent e){
				M();
			}

			public void mouseReleased(MouseEvent e){}

			public void mouseClicked(MouseEvent e){}
		  });
	    popup.add(menuItem3);
	    JMenuItem menuItem4 = new JMenuItem("貼り付け");
		popup.add(menuItem4);
		
	    
	    return popup;
	}
	
	//目標ノードIDと学習プランXMLを関連付けるテーブルにデータを保存するメソッド
		public void insertDataIntoDB(){

			
			String targetId = getEditTargetId();
			
			//すでに同じ目標ノードに関する関連がセットされていないかテェック
			//されていればアップデート
			sendSQL sqlUP = new sendSQL();
			String checkExist = "select count(*) from goal_to_plan_xml_tbl where goal_node_id=\""+targetId+"\"";
			String c = sqlUP.getVectorSelectSQL(checkExist).elementAt(0);
			
			//目標ノードに対する学習プランXMLを保存
			//XMLにエンコード
			mxCodec codec2 = new mxCodec();
			Node node2 = codec2.encode(getGraph().getModel());
			StringBuffer LearnXml = new StringBuffer(mxUtils.getPrettyXml(node2));
			String LXmlString = new String(LearnXml);
			
			//場合分けをしてDBに保存
			//過去にその目標に対して学習プランが編集されていなければ新しい編集を始める
			//アップデートするときは、いつ？
			if(c.equals("0")){
				String insertLearnToPlan = "insert into goal_to_plan_xml_tbl values (\'"+targetId+"\', \'"+LXmlString+"\')";
				sqlUP.operateSQL(insertLearnToPlan);
			} else if(targetId != null){
				String insertLearnToPlan = "update goal_to_plan_xml_tbl set plan_xml=\'"+LXmlString+"\' where goal_node_id=\""+targetId+"\"";
				sqlUP.operateSQL(insertLearnToPlan);
			
			}

			
			//getComponent().refresh();
			
		}


		//エディタ作成時のグラフを挿入するメソッド
		public void setFirstGraph(String ID){
			sendSQL SQL = new sendSQL();
			String getOldGraph = "select plan_xml from goal_to_plan_xml_tbl where goal_node_id = \""+ID+"\"";
			String oldGraph = SQL.getVectorSelectSQL(getOldGraph).elementAt(0);
			
			try{
				Document doc = mxXmlUtils.parseXml(oldGraph);
				mxCodec codec = new mxCodec(doc);
				codec.decode(doc.getDocumentElement(), getGraphModel());
			} catch (Exception ex){
				ex.printStackTrace();
			}
			grf.setModel(getGraphModel());
		}
		
		//******************
		//*********
		//***** アンドゥマネージャ　
		//**********
		//*******************
		public mxUndoManager getUndoManager()
		{
			return undoManager;
		}
	
		
		//****************************
		//****************
		//****************以降XMLの書き換え
		//****************
		//*****************************
		public void M(){
			//目標グラフのXML取得 into xml
			this.setLearnXml();	
			//メインの書き換えメソッド
			this.changeNewXml();	
			//保存
			this.saveNewXmls();
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
			/*
			j=0;
			for(i=0; i<ids.size(); i++){
				this.getDeadLine(i, j);
			}
			*/
			
			
			//***********
			//****** ノードの情報を書き換える
			//***********
			j=0;
			k=0;
			for(i=0; i<ids.size(); i++){
				/*
				//************
				//****** 期限による書き換え
				//************
				if(j < deadlines.size()){
					//２つの日付（期限と今の時間）の差
					this.getDateDist(xmlDash, i);
					j++;
				}
				*/
				//************
				//******** 状態ノードの位置の書き換え
				//************
				
				if(k < geoXs.size()){
					this.getGeoSet(xmlDash, i, k);
					
				}
				k++;
			}
			
			//新しいXMLテキストを作成
			for(i=0; i<xmlDash.length; i++){
				
				newXml += xmlDash[i]+"\n";
			}
			
			//表示を切り替える
			LearnXmlString = new String(newXml);
			
			newXml = "";
		}
		
		/////////******************** コンストラクタに絶対必要なメソッド ***************************//////////
		
		
		//***********
		//***** 指定した目標グラフをXMLにエンコードするメソッド
		//************
		private void setLearnXml(){
			mxCodec codec = new mxCodec();
			Node node = codec.encode(getGraph().getModel());
			StringBuffer LearnXml = new StringBuffer(mxUtils.getPrettyXml(node));
			xml = new String(LearnXml);
		}
		
		
		
		//*********
		//******* DBに新しいXMLを保存するメソッド
		//*********
		private void saveNewXmls(){
			
			
			sendSQL input = new sendSQL();
			//目標IDごとに学習プランXMLが保存
			String inputLearn = "update goal_to_plan_xml_tbl set plan_xml=\'"+LearnXmlString+"\' where goal_node_id=\""+getEditTargetId()+"\"";
			input.operateSQL(inputLearn);
			System.out.println(inputLearn);
			
			this.setImportGraph();
			//component.refresh();
			//MyLearningModelEditor.getComponent().refresh();
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
		/*
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
		}*/
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
			
			
			/*
			System.out.println("ここから今週");
			System.out.println(xmlDash[lines.elementAt(i)]);
			System.out.println(xmlDash[lines.elementAt(i)+1]);
			System.out.println(xmlDash[lines.elementAt(i)+3]);
			System.out.println(xmlDash[lines.elementAt(i)+4]);
			*/
			xmlDash[lines.elementAt(i)+4] = newGeoLine;
		}
}



