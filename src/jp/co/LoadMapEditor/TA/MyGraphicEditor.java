//*********************************
//******* このクラスファイルはグラフィックエディタを作成するためのもの
//***********************************

package jp.co.LoadMapEditor.TA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.mxgraph.examples.swing.editor.BasicGraphEditor;
import com.mxgraph.examples.swing.editor.EditorPopupMenu;
import com.mxgraph.io.mxCodec;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.util.mxUndoManager;

import java.awt.Cursor;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFormattedTextField;
import javax.swing.JMenu;



@SuppressWarnings("unused")
public class MyGraphicEditor extends JPanel{	
	
	private static final long serialVersionUID = 1L;

	//貼り付けるパネル
	//本体
	private static JPanel graphicEditor = new JPanel();
		
	//インスタンス化された回数を記憶する変数
	private static int instanceCount = 0;
	
	
	//ツールバー
	private EditorToolBar ETB;
	
	//表示しているグラフオブジェクト
	private static final MyGraph grf = new MyGraph();
	

	//パネルに貼り付けるコンポーネント
	private static final mxGraphComponent component = new mxGraphComponent(grf);
	
	//おそらく台紙的なもの
	private static Object parent = grf.getDefaultParent();
	
	//セレクト中のノード名
	private static String nodeName;
		
	//一時オブジェクト
	private static Object obj1 = new Object();
	private Object obj2 = new Object();
	private Object obj3 = new Object();
	
	//アンドノードオブジェクト
	Object o = new Object();
	
	//選択中のノードを把握するやつ
	private static mxCell SelectedCell;
	
	//デフォルトの画面表示位置を設定する変数
	private static final int defViewPos_x = 1800;
	private static final int defViewPos_y = 2300;
	
	//次に挿入されるグラフの座標
	private static int newGrfPosX;
	private static int newGrfPosY;
	
	//右下のダミーノードの位置を設定
	//デフォルトの画面の大きさになる
	private static final int grfEditX = 5000;
	private static final int grfEditY = 5000;
	
	//多分ノード数をカウントする変数
	private int obj_num = 0;
	
	//アンドノードの数
	private static int andNum = 0;
	
	//SQL
	private sendSQL SQL = new sendSQL();
	
	//右クリックポップアップ設定
	private JPopupMenu popup;
	
	//ノード状態選択ポップアップ
	private JPopupMenu modePopup;
	
	
	
	//水平スクロールバー
	private JScrollBar holScroll = component.getHorizontalScrollBar();
	//その位置
	private int holPos;
	//ボタンが押された最初のx位置
	private int firstHol;
	
	//垂直スクロールバー
	private JScrollBar verScroll = component.getVerticalScrollBar();
	//その位置
	private int verPos;
	//ボタンが押された最初のy位置
	private int firstVer;
	

	//状態ノードポップアップクリック時にノードの中身を変更する文字列
	private String intoSelectedCellsValue = null; 
	

	//最後に追加されたノード
	private static mxGraph lastInsertCell;
	
	//XMLを更新するかどうかの変数
	private static boolean yes_update = true;
	
	
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
	public MyGraphicEditor() {
		//アンドゥハンドラ
		grf.getModel().addListener(mxEvent.UNDO, undoHandler);
		grf.getView().addListener(mxEvent.UNDO, undoHandler);

		//リドゥハンドラ
		grf.getModel().addListener(mxEvent.REDO, undoHandler);
		grf.getView().addListener(mxEvent.REDO, undoHandler);
		
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
		if(instanceCount == 0){
			obj1 = grf.insertDammyVertex(getParent(), grf, 0, 0);
			obj1 = grf.insertDammyVertex(getParent(), grf, grfEditX, grfEditY);
		}
		instanceCount++;
		
		
		parent = getParent();
		
		this.add(addPopup());
		
		this.add(addModePopup());
		
		
		
	}
	
	//これでfrmInitにエディタをセットする
	public JPanel setEditor(){

		
		
		
		
		graphicEditor.setLayout(new BorderLayout(0, 0));
		
		//****
		//*** ここからツールバー
		//****
		ETB = new EditorToolBar();
		ETB.setFloatable(false);
		graphicEditor.add(ETB.setoolBar(), BorderLayout.NORTH);
		
	
		
		//*******
		//**** 描画
		//*******
		//コンポーネント作成・設定
		component.getViewport().setBackground(Color.WHITE);
		component.getViewport().setBackground(Color.WHITE);
		component.getViewport().setViewPosition(new Point(defViewPos_x, defViewPos_y));
		
		//ツールチップ設定
		component.setToolTips(true);
		//エンターキーで編集修了フラグ
		component.setEnterStopsCellEditing(true);
		
		
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
					firstHol = holScroll.getValue();
					firstVer = verScroll.getValue();
					//ホイールが下に行った時
					if(e.isShiftDown()){
						
						holPos = holScroll.getValue();
						holScroll.setValue(holPos-e.getWheelRotation());
					} else {					 
						verPos = verScroll.getValue();
						verScroll.setValue(verPos-e.getWheelRotation());
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

					SelectedCell = (mxCell)cell;
					
					//指定したグラフをXMLにエンコード
					mxCodec codec = new mxCodec();
					Node node = codec.encode(grf.getModel());
					StringBuffer GoalXml = new StringBuffer(mxUtils.getPrettyXml(node));
					String xml = new String(GoalXml);
					String newXml = new String();
					//SelectedCell.insert(grf.);
					//System.out.println(SelectedCell.getValue());
					//System.out.println(SelectedCell.getChildCount());
					
					
					//ノード名をデータベースから取ってくる。鍵はID
					sendSQL getSQL = new sendSQL();
					String getNameQuery;
					getNameQuery = "select goal_node_name from goal_node_tbl where goal_node_id=\""+SelectedCell.getId()+"\";";
					if(frmInit.getRightUpTabIndex() == 0){
						
					} else if(frmInit.getRightUpTabIndex() >= 1){
						getNameQuery = "select network_node_name from network_node_tbl";
					}
					nodeName = getSQL.getVectorSelectSQL(getNameQuery).elementAt(0);
					
					
					//メイン画面の左側のノード名のフィールドにノード名を入れる
					frmInit.setNameField(nodeName);
					
					//ノードのコンテンツ（テキスト）を表示する
					sendSQL getContQuery = new sendSQL();
					String getContentQuery = "select goal_node_content from goal_node_tbl where goal_node_id=\""+SelectedCell.getId()+"\";";
					String nodeContent = getContQuery.getVectorSelectSQL(getContentQuery).elementAt(0);
					frmInit.setTextAreaContents(nodeContent);
					
					//ノードの期限を表示する
					sendSQL getDeadLine = new sendSQL();
					String getDeadLineQuery = "select deadline from goal_node_tbl where goal_node_id=\""+SelectedCell.getId()+"\";";
					String deadline = getDeadLine.getVectorSelectSQL(getDeadLineQuery).elementAt(0);
					calenderPanel.setCalenderContents(deadline);
					
					//選択中のノードの状態を知る
					MyGraph gDash = new MyGraph();
					System.out.println(gDash.getNodeModeFromId(SelectedCell.getId()));
				} else {
					//クリックしたとこに何もない時の処理
					
					
					frmInit.setNameField("");
					SelectedCell = null;
					frmInit.setTextAreaContents("");
					calenderPanel.setCalenderContents("");
				}
			
			
				//右クリック処理
				if(((e.getModifiers() & java.awt.event.MouseEvent.BUTTON3_MASK) != 0) && !(SelectedCell.getId().startsWith("mode_"))){
					
					popup.show(e.getComponent(), e.getX(), e.getY());
				
				
				
					//モード表示ボタンの上で左クリックした時だけこれ
					//ここを何とかするためにモードノードの何かを設定するようにしたい
				} else if(((e.getModifiers() & java.awt.event.MouseEvent.BUTTON1_MASK) != 0) && (SelectedCell.getId().startsWith("mode_"))){
					
						modePopup.show(e.getComponent(), e.getX(), e.getY());

						SelectedCell.setValue(intoSelectedCellsValue);
						
				}
			}
			
			//マウスが押された時の処理
			@Override
			public void mousePressed(MouseEvent e){
				if(e.isControlDown()){
					firstHol = e.getX();
					firstVer = e.getY();
					
									
				}
			}
			
			
			//マウスが離れた時の処理
			@Override
			public void mouseReleased(MouseEvent e){
			//	MyGraphicEditor.getComponent().refresh();
				
			}
			
			
		});
		
		//マウスモーションリスナ
		component.getGraphControl().addMouseMotionListener(new MouseAdapter(){
			//マウスが動いた時の処理
			@Override
			public void mouseMoved(MouseEvent e){
				//MyGraphicEditor.getComponent().refresh();
				//new checkNodes(MyGraphicEditor.getGraph());
			}
			@Override
			public void mouseDragged(MouseEvent e){
				//コントロールキーが押されていたとき
				//画面のスクロールをする
				if(e.isControlDown()){
					verPos = verScroll.getValue();
					holPos = holScroll.getValue();
					int moveHol = e.getX() - firstHol;
					int moveVer = e.getY() - firstVer;
					holScroll.setValue(holPos-moveHol);
					verScroll.setValue(verPos-moveVer);
				}
				
			}
		});
		
		
		//詳細設定
		grf.setMultigraph(true);
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
		grf.getModel().addListener(mxEvent.CHANGE, new mxIEventListener()
		{
			
			@Override
			public void invoke(Object sender, mxEventObject evt)
			{
				//component.refresh();
				new dataInsert();
				
				
				
				
				
				if(SelectedCell == null){
					//System.out.println("noCELL");
				} else {
										
					
					//現在の日時を取得（最終更新日）
					Date date = new Date();
					SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
					String dateDash = format.format(date);
					//編集処理をした時にノードの情報をDBに保存するための処理
					String updateQuery = "update goal_node_tbl set goal_node_name=\""+SelectedCell.getValue().toString()+"\", goal_node_content=\""+frmInit.getTextAreaContents()+"\", deadline=\""+calenderPanel.getCalenderConetnts()+"\", last_edit_date=\""+dateDash+"\" where goal_node_id=\""+SelectedCell.getId()+"\"";
					SQL.operateSQL(updateQuery);
					if(SelectedCell.getValue().toString().length() >= 100){
						String newString = SelectedCell.getValue().toString().substring(0,15)+"...";
						SelectedCell.setValue(newString);
					}
					
					
				}
				
				if(yes_update == true){
					//期限などの条件によって色を変更する
					new checkNodes(grf);
				}
				
				
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
		String query;
		
		getGraphModel().beginUpdate();

		try	{
			if(obj_num  == 0){
				newGrfPosX = 2500;
				newGrfPosY = 2600;
				
				query = "insert into goal_node_tbl values(\""+uniqueID+"\", \""+frmInit.getNameField()+"\", \""+frmInit.getTextAreaContents()+"\", \"default\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+loadMapEditor.getProjectId()+"\")";
				

				SQL.operateSQL(query);
				obj1 =grf.insertMyVertex(getParent(), grf, "default", "default", uniqueID, newGrfPosX, newGrfPosY);
				MyGraphicEditor.setLastInsertedNode(grf);
				obj_num++;
			} else {
				
				query = "insert into goal_node_tbl values(\""+uniqueID+"\", \""+frmInit.getNameField()+"\", \""+frmInit.getTextAreaContents()+"\", \"default\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+loadMapEditor.getProjectId()+"\")";
				SQL.operateSQL(query);
				
				obj2 = grf.insertMyVertex(getParent(), grf, "mode2", "default2", uniqueID, newGrfPosX, newGrfPosY);
				obj3 = grf.insertMyEdge(parent, grf, "type1", "", obj1, obj2);
				obj_num++;
				
				obj1 = obj2;
			}
			
		} finally {

			getGraphModel().endUpdate();
		}
		return obj1;
	}
	
	//And素子を入力するメソッド
	public Object setAndNode(mxCell selectedcell){
		System.out.println(selectedcell.getId());
		if(selectedcell.getId().indexOf("and_") != -1){
			return obj1;
		}
		
		getGraphModel().beginUpdate();
		try	{

			String q = "insert into goal_node_tbl values(\"and_"+MyGraphicEditor.getSelectedCell().getId()+"\", \"\", \"\", \"\", \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";
			SQL.operateSQL(q);
			new updateInsertPosition(40, 60);
			obj1 =grf.insertAndVertex(parent, grf, andNum, newGrfPosX, newGrfPosY);
			grf.insertEdge(parent, null, null, obj1, selectedcell);
			MyGraphicEditor.setLastInsertedNode(grf);

			
			andNum++;
		} finally {
			getGraphModel().endUpdate();
		}
		
		return obj1;
	}
	
	
	
	
	
	private static void setLastInsertedNode(MyGraph grf2) {
		// TODO 自動生成されたメソッド・スタブ
		
	}


	//ポリモーフィズム
	public Object setMyVertex(String mode, String node_name){
		//ID用の文字列生成
		//UNIX時間のナノ秒をString型に直したものを利用(人間が使ってほぼ一意になる)
		createUniqueIDfromUnixTime createID = new createUniqueIDfromUnixTime();
		String uniqueID = createID.getUniqueID();
		String query;
				
		getGraphModel().beginUpdate();

		new updateInsertPosition();
		try	{
			if(obj_num  == 0){

				
				query = "insert into goal_node_tbl values(\""+uniqueID+"\", \""+node_name+"\", \""+frmInit.getTextAreaContents()+"\", \"default\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+loadMapEditor.getProjectId()+"\")";

				SQL.operateSQL(query);
				
				obj1 =grf.insertMyVertex(parent, grf, mode, node_name, uniqueID, newGrfPosX, newGrfPosY);
				
				//最終追加ノードを更新する
				MyGraphicEditor.setLastInsertedNode(grf);
				obj_num++;
			} else {

				
				query = "insert into goal_node_tbl values(\""+uniqueID+"\", \""+node_name+"\", \""+frmInit.getTextAreaContents()+"\", \"default\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+loadMapEditor.getProjectId()+"\")";
				SQL.operateSQL(query);
				
				obj2 = grf.insertMyVertex(parent, grf, mode, node_name, uniqueID, newGrfPosX, newGrfPosY);
			
				obj3 = grf.insertMyEdge(parent, grf, "type1", null, obj1, obj2);
				
				
				//最終追加ノードを更新する
				MyNetworkModelEditor.setLastInsertedNode(grf);
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
				grf.insertMyEdge(parent, grf, null, null, par, chi);
			
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
	public static String getNodeName(){
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
	public static Object getPar(){
		return parent;
	}
	
	
	
	//選択中のノードをmxCell型でリターンするメソッド
	public static mxCell getSelectedCell(){
		if(SelectedCell != null){
			return SelectedCell;	
		} else {
			mxCell cell = new mxCell();
			return cell;
		}
	}
	
	//多分ノードの数を変えるメソッド
	public int getObjectNum(){
		return obj_num;
	}
	
	//最後に追加されたノードを取得するメソッド
	public static mxGraph getLastInsertedNode(){
		return lastInsertCell;
	}
		
	//最後に追加されたノードを変更するメソッド
	public static void setLastInsertedNode(mxGraph grf){
		lastInsertCell = grf;
	}

	//次に挿入されるグラフの座標を指定するメソッド
	public static void updateNewPos(int newPosX, int newPosY){
		newGrfPosX = newPosX;
		newGrfPosY = newPosY;
	}
	
	//選択されたセルの座標をmxPoint型で返すメソッド
	public static Point getSelectedCellPosition(){
		mxPoint pos = new mxPoint();
		if(SelectedCell != null){
			pos = SelectedCell.getGeometry();
		} else {
			pos.setX((double)grfEditX/2.0);
			pos.setY((double)grfEditY/2.0);
		}
		Point point = new Point();
		point.x = (int)pos.getX();
		point.y = (int)pos.getY();
		return point;
	}
	

	//XMLファイルからのインポートを処理するメソッド
	public Object setImportGraph(String project_id){
		
		
		sendSQL sql = new sendSQL();
		String textSQL = sql.getProjectXmlByID(project_id);
		
		try{
			Document doc = mxXmlUtils.parseXml(textSQL);
			mxCodec codec = new mxCodec(doc);
			codec.decode(doc.getDocumentElement(), getGraphModel());
		} catch (Exception ex){
			ex.printStackTrace();
		}
		grf.setModel(getGraphModel());
		
		return obj1;
	}
	


	//ポップアップメニュー設定
	private JPopupMenu addPopup() {
		
		//*************
		//****** 右クリックポップアップの設定
		//*************
		popup = new JPopupMenu();
		
		JMenu menuPop1 = new JMenu("このノードの学習プランを編集する");
	    popup.add(menuPop1);
		
	    JMenuItem menuItem10 = new JMenuItem("編集する");
		menuItem10.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				//どの目標ノードと学習プランXMLが関連してるのかを判断するために学習プランエディタに編集中の目標プランノードIDをセットする
				

				MyLearningModelEditor.setEditTargetId(SelectedCell.getId());
				MyLearningModelEditor newEditor = new MyLearningModelEditor();
				newEditor = frmInit.setNewLearningModelEditorTab(frmInit.getNameField());
				
				frmInit.setTabbedIndex(frmInit.getTabbed_rightUp().getTabCount()-1);
				//もしおなじIDのノードの学習プランができてるならそっちを開くようにする
				sendSQL SQL = new sendSQL();
				String checkLearningModel = "select count(*) from goal_to_plan_xml_tbl where goal_node_id = \""+SelectedCell.getId()+"\"";
				String res = SQL.getVectorSelectSQL(checkLearningModel).elementAt(0);
				
				if(res.equals("0")){
					newEditor.setMyVertex();
				} else {
					newEditor.setFirstGraph(SelectedCell.getId());
				}
				//どの目標ノードと学習プランXMLが関連してるのかを判断するために学習プランエディタに編集中の目標プランノードIDをセットする
				MyLearningModelEditor.setEditTargetId(SelectedCell.getId());
			}
		});
		
		/*
		JMenuItem menuItem1 = new JMenuItem("新規に編集する");
		menuItem1.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				//どの目標ノードと学習プランXMLが関連してるのかを判断するために学習プランエディタに編集中の目標プランノードIDをセットする
				MyLearningModelEditor.setEditTargetId(SelectedCell.getId());
				MyLearningModelEditor newEditor = new MyLearningModelEditor();
				newEditor = frmInit.setNewLearningModelEditorTab(frmInit.getNameField());
				
				frmInit.setTabbedIndex(frmInit.getTabbed_rightUp().getTabCount()-1);
				
				//もしおなじIDのノードの学習プランができてるならそっちを開くようにする
				sendSQL SQL = new sendSQL();
				String checkLearningModel = "select count(*) from goal_to_plan_xml_tbl where goal_node_id = \""+SelectedCell.getId()+"\"";
				String res = SQL.getVectorSelectSQL(checkLearningModel).elementAt(0);
				if(res.equals("0")){
					newEditor.setMyVertex();
				} else {
					newEditor.setFirstGraph(SelectedCell.getId());
				}
				//どの目標ノードと学習プランXMLが関連してるのかを判断するために学習プランエディタに編集中の目標プランノードIDをセットする
				MyLearningModelEditor.setEditTargetId(SelectedCell.getId());
			}
		});
		 
		
		JMenuItem menuItemp = new JMenuItem("継承して編集する");
		menuItemp.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				//どの目標ノードと学習プランXMLが関連してるのかを判断するために学習プランエディタに編集中の目標プランノードIDをセットする
				MyLearningModelEditor.setEditTargetId(SelectedCell.getId());
				MyLearningModelEditor newEditor = new MyLearningModelEditor();
				newEditor = frmInit.setNewLearningModelEditorTab(frmInit.getNameField());
				
				frmInit.setTabbedIndex(frmInit.getTabbed_rightUp().getTabCount()-1);
				
				//もしおなじIDのノードの学習プランができてるならそっちを開くようにする
				sendSQL SQL = new sendSQL();
				String checkLearningModel = "select count(*) from goal_to_plan_xml_tbl where goal_node_id = \""+SelectedCell.getId()+"\"";
				String res = SQL.getVectorSelectSQL(checkLearningModel).elementAt(0);
				if(res.equals("0")){
					newEditor.setMyVertex();
				} else {
					newEditor.setFirstGraph(SelectedCell.getId());
				}
				//どの目標ノードと学習プランXMLが関連してるのかを判断するために学習プランエディタに編集中の目標プランノードIDをセットする
				MyLearningModelEditor.setEditTargetId(SelectedCell.getId());
			}
		});
		*/
		menuPop1.add(menuItem10);
		//menuPop1.add(menuItem1);
		//menuPop1.add(menuItemp);
		
		
			
		
		
		JMenuItem menuItem = new JMenuItem("このノードの下に新規ノードを生成する");
		menuItem.addMouseListener(new MouseListener(){
			
			public void mouseEntered(MouseEvent e){
			}
		  
			public void mouseExited(MouseEvent e){
			}

			public void mousePressed(MouseEvent e){
				
				new updateInsertPosition(0, 160);
				createUniqueIDfromUnixTime createID = new createUniqueIDfromUnixTime();
				String uniqueID = createID.getUniqueID();
				
				
				String query = "insert into goal_node_tbl values(\""+uniqueID+"\", \""+getSelectedCell().getValue()+"\", \""+frmInit.getTextAreaContents()+"\", \"default\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+calenderPanel.getCalenderConetnts()+"\", \""+loadMapEditor.getProjectId()+"\")";
				
				SQL.operateSQL(query);
				//DBへの挿入
				sendSQL modeSQL = new sendSQL();
				
				//モード状態ノードのスタイル
				String mode_style = "rounded=1;fillColor=pink;";
				//モード状態ノードのグラフへの挿入
				
				
				Object chi = grf.insertMyVertex(getParent(), grf, "default", "default", uniqueID, newGrfPosX, newGrfPosY);
				
				o = setAndNode(getSelectedCell());
				grf.insertMyEdge(getParent(), grf, null, null, o, chi);
			}

			public void mouseReleased(MouseEvent e){
			}

			public void mouseClicked(MouseEvent e){
			}
		  });
		popup.add(menuItem);
		
		
		JMenu menuItem3 = new JMenu("色を変える");
	    popup.add(menuItem3);
	    
	    
	    
	    //色変えの入れ子
	    JMenuItem changeColorBlue = new JMenuItem("青");
	    changeColorBlue.addMouseListener(new MouseListener(){
	    	public void mousePressed(MouseEvent e){
	    		MyGraphicEditor.getSelectedCell().setStyle("shape=ellipse;perimeter=ellipsePerimeter;strokeColor=red;fillColor=lightblue;");
	    		MyGraphicEditor.getComponent().refresh();
	    	}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
	    });
	    JMenuItem changeColorRed = new JMenuItem("赤");
	    changeColorRed.addMouseListener(new MouseListener(){
	    	public void mousePressed(MouseEvent e){
	    		MyGraphicEditor.getSelectedCell().setStyle("shape=ellipse;perimeter=ellipsePerimeter;strokeColor=red;fillColor=red;");
	    		MyGraphicEditor.getComponent().refresh();
	    	}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
	    });
	    JMenuItem changeColorYellow = new JMenuItem("黄");
	    changeColorYellow.addMouseListener(new MouseListener(){
	    	public void mousePressed(MouseEvent e){
	    		MyGraphicEditor.getSelectedCell().setStyle("shape=ellipse;perimeter=ellipsePerimeter;strokeColor=red;fillColor=yellow;");
	    		MyGraphicEditor.getComponent().refresh();
	    	}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
	    });
	    JMenuItem changeColorPink = new JMenuItem("ピンク");
	    changeColorPink.addMouseListener(new MouseListener(){
	    	public void mousePressed(MouseEvent e){
	    		MyGraphicEditor.getSelectedCell().setStyle("shape=ellipse;perimeter=ellipsePerimeter;strokeColor=red;fillColor=pink;");
	    		MyGraphicEditor.getComponent().refresh();
	    	}
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
	    });
	    
	    
	    menuItem3.add(changeColorBlue);
	    menuItem3.add(changeColorRed);
	    menuItem3.add(changeColorYellow);
	    menuItem3.add(changeColorPink);
	    
	    
	    
	    
	    JMenuItem menuItem4 = new JMenuItem("予備スロット");
	    menuItem4.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
	    });
	    
	    
	    popup.add(menuItem4);
		
	    
	    return popup;
	}
	
	
	
	//モード状態ノードをクリックした時のポップアップ
	private JPopupMenu addModePopup(){
		
		//*************
		//****** 右クリックポップアップの設定
		//*************
		modePopup = new JPopupMenu();
		//SelectedCell = null;
		JMenuItem mode1 = new JMenuItem("学習プラン未設計");
		mode1.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				intoSelectedCellsValue = "学習プラン未設計";
				
				//DBに挿入する文字列を書く
				sendSQL updateMode = new sendSQL();
				String updateModeQuery = "update goal_node_tbl set goal_node_name=\"学習プラン未設計\" where goal_node_id=\""+SelectedCell.getId().toString()+"\"";
				updateMode.operateSQL(updateModeQuery);
				
				
				//SelectedCell = null;
				SelectedCell.setValue(intoSelectedCellsValue);
				MyGraphicEditor.getComponent().refresh();
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
	    });
		modePopup.add(mode1);
		
		JMenuItem mode2 = new JMenuItem("学習プラン設計中");
		mode2.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				intoSelectedCellsValue = "学習プラン設計中";
				//DBに挿入する文字列を書く
				sendSQL updateMode = new sendSQL();
				String updateModeQuery = "update goal_node_tbl set goal_node_name=\"学習プラン設計中\" where goal_node_id=\""+SelectedCell.getId().toString()+"\"";
				updateMode.operateSQL(updateModeQuery);
				
				//SelectedCell = null;
				SelectedCell.setValue(intoSelectedCellsValue);
				MyGraphicEditor.getComponent().refresh();
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
	    });
		modePopup.add(mode2);
		
		JMenuItem mode2_1 = new JMenuItem("学習プラン設計済");
		mode2_1.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				intoSelectedCellsValue = "学習プラン設計済";
				//DBに挿入する文字列を書く
				sendSQL updateMode = new sendSQL();
				String updateModeQuery = "update goal_node_tbl set goal_node_name=\"学習プラン設計済\" where goal_node_id=\""+SelectedCell.getId().toString()+"\"";
				updateMode.operateSQL(updateModeQuery);
				
				//SelectedCell = null;
				SelectedCell.setValue(intoSelectedCellsValue);
				MyGraphicEditor.getComponent().refresh();
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
	    });
		modePopup.add(mode2_1);
		
		JMenuItem mode3 = new JMenuItem("学習中");
		mode3.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				intoSelectedCellsValue = "学習中";
				//DBに挿入する文字列を書く
				sendSQL updateMode = new sendSQL();
				String updateModeQuery = "update goal_node_tbl set goal_node_name=\"学習中\" where goal_node_id=\""+SelectedCell.getId().toString()+"\"";
				updateMode.operateSQL(updateModeQuery);
				
				//SelectedCell = null;
				SelectedCell.setValue(intoSelectedCellsValue);
				MyGraphicEditor.getComponent().refresh();
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
	    });
		modePopup.add(mode3);
		
		JMenuItem mode4 = new JMenuItem("問題解決準備完了");
		mode4.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				intoSelectedCellsValue = "問題解決準備完了";
				//DBに挿入する文字列を書く
				sendSQL updateMode = new sendSQL();
				String updateModeQuery = "update goal_node_tbl set goal_node_name=\"問題解決準備完了\" where goal_node_id=\""+SelectedCell.getId().toString()+"\"";
				updateMode.operateSQL(updateModeQuery);
				
				//SelectedCell = null;
				SelectedCell.setValue(intoSelectedCellsValue);
				MyGraphicEditor.getComponent().refresh();
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
	    });
		modePopup.add(mode4);
		
		JMenuItem mode5 = new JMenuItem("問題解決中");
		mode5.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				intoSelectedCellsValue = "問題解決中";
				//DBに挿入する文字列を書く
				sendSQL updateMode = new sendSQL();
				String updateModeQuery = "update goal_node_tbl set goal_node_name=\"問題解決中\" where goal_node_id=\""+SelectedCell.getId().toString()+"\"";
				updateMode.operateSQL(updateModeQuery);
				
				//SelectedCell = null;
				SelectedCell.setValue(intoSelectedCellsValue);
				MyGraphicEditor.getComponent().refresh();
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
	    });
		modePopup.add(mode5);
		
		JMenuItem mode6 = new JMenuItem("問題解決済");
		mode6.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				intoSelectedCellsValue = "問題解決済";
				//DBに挿入する文字列を書く
				sendSQL updateMode = new sendSQL();
				String updateModeQuery = "update goal_node_tbl set goal_node_name=\"問題解決済\" where goal_node_id=\""+SelectedCell.getId().toString()+"\"";
				updateMode.operateSQL(updateModeQuery);
				
				//SelectedCell = null;
				SelectedCell.setValue(intoSelectedCellsValue);
				MyGraphicEditor.getComponent().refresh();
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseClicked(MouseEvent e){}
	    });
		modePopup.add(mode6);
		
		
		
		
		
		return modePopup;
	}
	
	//タブを新規で開いた時に初期配置として存在するノードの設定
	public void setFirstGraph(){
		//ID用の文字列生成
		//UNIX時間のナノ秒をString型に直したものを利用(人間が使ってほぼ一意になる)
		createUniqueIDfromUnixTime createID = new createUniqueIDfromUnixTime();
		String uniqueID = createID.getUniqueID();
		
		obj1 = grf.insertMyVertex(getParent(), grf, "default", "Start", uniqueID, 2500, 2500);
		//現在の日時を取得（最終更新日）
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String dateDash = format.format(date);
		
		String query = "insert into goal_node_tbl values(\""+uniqueID+"\", \"Start\", \"default\", \""+dateDash+"\", \""+dateDash+"\", \""+dateDash+"\", \""+dateDash+"\", \""+loadMapEditor.getProjectId()+"\")";
		sendSQL sql = new sendSQL();
		sql.operateSQL(query);
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
	
	//********
	//**** XMLの更新をしていいかどうかの変数を更新するメソッド
	//********
	public static void setYesUpdate(boolean bool){
		yes_update = bool;
	}

		
	
	
	
}



