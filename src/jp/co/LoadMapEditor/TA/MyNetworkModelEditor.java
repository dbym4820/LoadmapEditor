//*********************************
//******* このクラスファイルは知識意味ネットワークエディタを作成するためのもの
//***********************************


package jp.co.LoadMapEditor.TA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.undo.UndoManager;

import org.w3c.dom.Document;

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

import java.awt.Cursor;
import java.awt.Component;
import javax.swing.JFormattedTextField;


@SuppressWarnings("unused")
public class MyNetworkModelEditor extends JPanel{	
	
	private static final long serialVersionUID = 1L;
	
	
	//インスタンス化された回数を記憶する変数
	private static int instanceCount = 0;

	//貼り付けるパネル
	//本体
	private static JPanel graphicNetEditor = new JPanel();
	
	
	//表示しているグラフオブジェクト
	private static MyGraph grf = new MyGraph();

	//パネルに貼り付けるコンポーネント
	private static mxGraphComponent component = new mxGraphComponent(grf);
	
	//おそらく台紙的なもの
	private static Object parent = grf.getDefaultParent();
	
	//セレクト中のノード名
	private static String nodeName;
	
	
	//一時オブジェクト
	private Object obj1 = new Object();
	private Object obj2 = new Object();
	private Object obj3 = new Object();
	
	//選択中のノードを把握するやつ
	private static mxCell SelectedCell;
	
	//デフォルトの画面表示位置を設定する変数
	private static final int defViewPos_x = 1800;
	private static final int defViewPos_y = 2300;
	
	//右下のダミーノードの位置を設定
	//デフォルトの画面の大きさになる
	private static final int grfEditX = 5000;
	private static final int grfEditY = 5000;
	
	//多分ノード数をカウントする変数
	private int obj_num = 0;

	//最後に追加されたノード
	private static Object lastInsertCell;
	
	//追加されたノードのヒストリー
	private static Vector<String> addGraphIdHist = new Vector<String>();
	
	//SQLite
	private sendSQL SQL = new sendSQL();
	
	
	
	//右クリックポップアップ設定
	JPopupMenu popup;
	
	//水平スクロールバー
	JScrollBar holScroll = component.getHorizontalScrollBar();
	//その位置
	int holPos;
	//ボタンが押された最初のx位置
	int firstHol;
	
	//垂直スクロールバー
	JScrollBar verScroll = component.getVerticalScrollBar();
	//その位置
	int verPos;
	//ボタンが押された最初のy位置
	int firstVer;
	

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
	public MyNetworkModelEditor() {
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
		grf.setAllowDanglingEdges(true);
		//grf.setCellsResizable(false);
		//grf.setAllowNegativeCoordinates(false);
		grf.setMultigraph(true);
		grf.setCellsCloneable(true);
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
		
		this.add(addPopup());
		
		
		
		
	}
	
	
	public JPanel setEditor(){
		graphicNetEditor.setLayout(new BorderLayout(0, 0));
		
		//****
		//*** ここからツールバー
		//****
		NetworkEditorToolBar NETB = new NetworkEditorToolBar();
		NETB.setFloatable(false);
		graphicNetEditor.add(NETB.setoolBar(), BorderLayout.NORTH);
		
		
		
		//画面を拡大するためのダミーノード
		if(instanceCount == 0){
			obj1 = grf.insertDammyVertex(parent, grf, 0, 0);
			obj1 = grf.insertDammyVertex(parent, grf, grfEditX, grfEditY);
		}

		instanceCount++;
		
		
		//*******
		//**** 描画
		//*******
		//コンポーネント作成・設定
		component.getViewport().setBackground(Color.WHITE);
		component.getViewport().setViewPosition(new Point(defViewPos_x, defViewPos_y));
		
		
		//ノードのないところに矢印を持って行っても矢印が消える
		component.getConnectionHandler().setCreateTarget(false);
		

		//エンターキーで編集修了フラグ
		component.setEnterStopsCellEditing(true);
		
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
					
					SelectedCell = (mxCell)cell;
					
					//ノード名をデータベースから取ってくる。鍵はID
					sendSQL getName = new sendSQL();
					String getNameQuery;
					getNameQuery = "select network_node_name from network_node_tbl where network_node_id=\""+SelectedCell.getId()+"\";";
					
					String nodeName = getName.getVectorSelectSQL(getNameQuery).elementAt(0);
					
							
					//メイン画面の左側のノード名のフィールドにノード名を入れる
					frmInit.setNameField(nodeName);
							
				} else {
					//クリックしたとこに何もない時の処理

					frmInit.setNameField("");
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
				if(e.isControlDown()){
					firstHol = e.getX();
					firstVer = e.getY();
				}
			}
			
			//マウスが離れた時の処理
			@Override
			public void mouseReleased(MouseEvent e){
			}
		});
		
		//マウスモーションリスナ
		component.getGraphControl().addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e){
				
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
		
		
		
		graphicNetEditor.setMinimumSize(new Dimension(1200, 2000));
		
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
		grf.getModel().addListener(mxEvent.CHANGE, new mxIEventListener()
		{
					
			@Override
			public void invoke(Object sender, mxEventObject evt){
				new dataInsert();
				
				//編集をしないとバグるから異端消しとく
				/*
				//編集処理をした時にノードの情報をDBに保存するための処理
				String updateQuery = "update network_node_tbl set network_node_name=\""+SelectedCell.getValue().toString()+"\" where network_node_id=\""+SelectedCell.getId()+"\"";
				sendSQL SQL = new sendSQL();
				SQL.operateSQL(updateQuery);
				*/
			}
		});

		

		graphicNetEditor.add(component);
		
		
		return graphicNetEditor;
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
				obj1 =grf.insertMyVertex(getParent(), grf, "default", "default", uniqueID);
				query = "insert into network_node_tbl values(\""+uniqueID+"\", \"default\", \"\", \"default\", \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";
				

				SQL.operateSQL(query);
				MyNetworkModelEditor.setLastInsertedNode(obj1);
				obj_num++;
			} else {
				
				obj2 = grf.insertMyVertex(getParent(), grf, "mode2", "default2", uniqueID);
				query = "insert into network_node_tbl values(\""+uniqueID+"\", \"default2\", \"\", \"default\", \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";

				obj3 = grf.insertMyEdge(parent, grf, "type1", "", obj1, obj2);

				
				SQL.operateSQL(query);
				//最終追加ノードを更新する
				MyNetworkModelEditor.setLastInsertedNode(obj2);
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
		String query;
		
		getGraphModel().beginUpdate();

		try	{
			if(obj_num  == 0){
				obj1 =grf.insertMyVertex(getParent(), grf, mode, node_name, uniqueID);
				query = "insert into network_node_tbl values(\""+uniqueID+"\", \""+node_name+"\", \"\", \"\", \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";

				//最終追加ノードを更新する
				MyNetworkModelEditor.setLastInsertedNode(obj1);

				SQL.operateSQL(query);
				obj_num++;
			} else {
			
				obj2 = grf.insertMyVertex(getParent(), grf, mode, node_name, uniqueID);
				
				obj3 = grf.insertMyEdge(getParent(), grf, "type1", null, obj1, obj2);
				query = "insert into network_node_tbl values(\""+uniqueID+"\", \""+node_name+"\", \"\", \"\", \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";

				SQL.operateSQL(query);
				
				//最終追加ノードを更新する
				MyNetworkModelEditor.setLastInsertedNode(obj2);

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
	
	//最後に追加されたノードを取得するメソッド
	public static Object getLastInsertedNode(){
		return lastInsertCell;
	}
	
	//最後に追加されたノードを変更するメソッド
	public static void setLastInsertedNode(Object grf){
		lastInsertCell = grf;
		mxCell grfObject = (mxCell)grf;

		addGraphIdHist.addElement(grfObject.getId());
		
		
	}
	
	//アンドゥ用のヒストリーを設定するメソッド
	public static Vector<String> getAddGraphHistory(){
				
		return addGraphIdHist;
	}
	
	//選択中のノードをmxCell型でリターンするメソッド
	public static mxCell getSelectedCell(){
		if(SelectedCell != null){
			return SelectedCell;	
		} else {
			return null;
		}
	}
	


	//XMLファイルからのインポートを処理するメソッド
	//できれば外部クラスに置きたい処理
	public Object setImportGraph(String project_id){
		
		
		sendSQL sql = new sendSQL();
		String textSQL = sql.getNetworkXmlByID(project_id);
		
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
	
	


	//右クリックで出るポップアップメニュー設定
	private JPopupMenu addPopup() {
			

		popup = new JPopupMenu();
			
			
		//JMenuItem menuItem2 = new JMenuItem("以降の木を折りたたむ");
		//popup.add(menuItem2);
		JMenuItem menuItem3 = new JMenuItem("コピー");
		popup.add(menuItem3);
		JMenuItem menuItem4 = new JMenuItem("貼り付け");
		popup.add(menuItem4);
		JMenuItem menuItem5 = new JMenuItem("ノードの削除");
		menuItem5.addMouseListener(new MouseListener(){
			
			public void mouseEntered(MouseEvent e){
			}
			
			public void mouseExited(MouseEvent e){
			}
			
			public void mousePressed(MouseEvent e){
				grf.removeCells();
			}

			public void mouseReleased(MouseEvent e){
			}

			public void mouseClicked(MouseEvent e){
			}	
		});	
		popup.add(menuItem5);
		

			
		    
		return popup;
	}
	
	//タブを新規で開いた時に初期配置として存在するノードの設定
	public void setFirstGraph(){
		//ID用の文字列生成
		//UNIX時間のナノ秒をString型に直したものを利用(人間が使ってほぼ一意になる)
		createUniqueIDfromUnixTime createID = new createUniqueIDfromUnixTime();
		String uniqueID = createID.getUniqueID();
		
		obj1 = grf.insertMyVertex(parent, grf, "default", "Start", uniqueID);
			
		String query = "insert into network_node_tbl values(\""+uniqueID+"\", \"Start\", \"\", \"\", \"\", \"\", \"\", \""+loadMapEditor.getProjectId()+"\")";
		sendSQL sql = new sendSQL();
		sql.operateSQL(query);
		//最終追加ノードを更新する
		MyNetworkModelEditor.setLastInsertedNode(obj1);
		
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
	
	
	
}



