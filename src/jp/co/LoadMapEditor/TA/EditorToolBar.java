//******* 目標エディタと学習プランエディタ用のツールバー設定用クラス ******************//

package jp.co.LoadMapEditor.TA;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;


public class EditorToolBar extends JToolBar implements ActionListener{

	
	private static final long serialVersionUID = 1L;

	
	//********
	//**** 画像へのパス取得
	//**** Jarファイルにした時もEclipseのときも動作するように
	//****
	//写真フォルダへの絶対パスの取得
	String rootPath = new File(".").getAbsoluteFile().getParent()+"/picts/";
	//各写真ファイルへのパス
	String connectImagePath = rootPath + "connect.png";
	String lineImagePath = rootPath + "cut.png";
	String homeImagePath = rootPath + "home.png";
	String bookmarkImagePath = rootPath + "bookmark.png";
	String cloudImagePath = rootPath + "cloud.png";
	String saveImagePath = rootPath + "save.png";
	String undoImagePath = rootPath + "undo.png";
	String redoImagePath = rootPath + "redo.png";
	String zoomInImagePath = rootPath + "zoom_in.png";
	String zoomOutImagePath = rootPath + "zoom_out.png";
	String importImagePath = rootPath + "import.png";
	String clearImagePath = rootPath + "clear.png";
	
	
	//ツールバーをセット
    JToolBar toolBar = new JToolBar(SwingConstants.HORIZONTAL);
    JButton draw_node = new JButton(new ImageIcon(cloudImagePath));//ノード追加用ボタン
    JButton clear = new JButton(new ImageIcon(clearImagePath));//クリアボタン
    JButton save = new JButton(new ImageIcon(saveImagePath));//保存用ボタン
    JButton undo = new JButton(new ImageIcon(undoImagePath));//アンドゥ用ボタン
    JButton redo = new JButton(new ImageIcon(redoImagePath));//リドゥ用ボタン
    JButton zoomIn = new JButton(new ImageIcon(zoomInImagePath));//拡大用ボタン
    JButton zoomOut = new JButton(new ImageIcon(zoomOutImagePath));//縮小用ボタン
    JButton imp = new JButton(new ImageIcon(importImagePath));//グラフ挿入用ボタン
  
    
    //ドロップダウンリスト
    dropDown dD = new dropDown();
    
    sendSQL SQL = new sendSQL();
	
    

    //分割目標エディタの宣言
	MyGraphicEditor MGE = new MyGraphicEditor();;
	//学習プランツリーの宣言
	MyLearningModelEditor MLME = new MyLearningModelEditor();

	
	//******
	//**** コンストラクタ
	//*******
	public EditorToolBar() {
		
		setInheritsPopupMenu(true);
		//setoolBar();
		
		//ツールバーのSWING設定
		toolBar.setPreferredSize(new Dimension(10, 30));
		toolBar.setFloatable(false);

	
		
	}
	
	//*****
	//****ツールバーの設定
	//******
	public JToolBar setoolBar(){
		
		//新規ノード追加用ボタン設定
		draw_node.setToolTipText("新規ノードの追加");
		draw_node.setHorizontalAlignment(SwingConstants.LEADING);
		draw_node.setSize(15,15);
		draw_node.setPreferredSize(new Dimension(15, 15));
		draw_node.setBorderPainted(false);
		draw_node.addActionListener(this);
		toolBar.add(draw_node);
		

		//セパレータ
		toolBar.addSeparator();
		
		//保存用ボタン設定
		save.setToolTipText("保存");
		save.setHorizontalAlignment(SwingConstants.LEADING);
		save.setPreferredSize(new Dimension(15, 15));
		save.setBorderPainted(false);
		save.addActionListener(this);
		toolBar.add(save);
		
		//セパレータ
		toolBar.addSeparator();
		
		//保存用ボタン設定
		undo.setToolTipText("UnDo");
		undo.setHorizontalAlignment(SwingConstants.LEADING);
		undo.setPreferredSize(new Dimension(15, 15));
		undo.setBorderPainted(false);
		undo.addActionListener(this);
		toolBar.add(undo);
				
		//セパレータ
		toolBar.addSeparator();
				
		//ReDoボタン設定
		redo.setToolTipText("ReDo");
		redo.setHorizontalAlignment(SwingConstants.LEADING);
		redo.setPreferredSize(new Dimension(15, 15));
		redo.setBorderPainted(false);
		redo.addActionListener(this);
		toolBar.add(redo);
		
		//セパレータ
		toolBar.addSeparator();
		//拡大ボタン設定
		zoomIn.setToolTipText("ZoomIn");
		zoomIn.setHorizontalAlignment(SwingConstants.LEADING);
		zoomIn.setPreferredSize(new Dimension(15, 15));
		zoomIn.setBorderPainted(false);
		zoomIn.addActionListener(this);
		toolBar.add(zoomIn);
				
		//セパレータ
		toolBar.addSeparator();

		
		//縮小ボタン設定
		zoomOut.setToolTipText("ZoomOut");
		zoomOut.setHorizontalAlignment(SwingConstants.LEADING);
		zoomOut.setPreferredSize(new Dimension(15, 15));
		zoomOut.setBorderPainted(false);
		zoomOut.addActionListener(this);
		toolBar.add(zoomOut);
		
		//セパレータ
		toolBar.addSeparator();
		
		
		//インポートボタン設定
		imp.setToolTipText("グラフのインポート");
		imp.setHorizontalAlignment(SwingConstants.LEADING);
		imp.setPreferredSize(new Dimension(15, 15));
		imp.setBorderPainted(false);
		imp.addActionListener(this);
		toolBar.add(imp);

		//セパレータ
		toolBar.addSeparator();
		
		//新規リレーション追加用ボタン設定
		clear.setToolTipText("グラフをクリアする");
		clear.setHorizontalAlignment(SwingConstants.LEADING);
		clear.setPreferredSize(new Dimension(15, 15));
		clear.setBorderPainted(false);
		clear.addActionListener(this);
		toolBar.add(clear);
		
	
		
	      
		return toolBar;
	}

	//*******
	//**** イベントリスナー処理
	//*******
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		//ノードを追加するボタン
		if(e.getSource() == draw_node){
			

			//ID用の文字列生成
			//UNIX時間のナノ秒をString型に直したものを利用(人間が使ってほぼ一意になる)
			createUniqueIDfromUnixTime createID = new createUniqueIDfromUnixTime();
			String uniqueID = createID.getUniqueID();

			@SuppressWarnings("unused")
			String query = null;
			String query0 = null;
			//上のエディタがアクティブなとき
			if(frmInit.getRightUpTabIndex() == 0){
				
				
				
				new updateInsertPosition();
				
				//ノード追加時にデータベースに保存
				query0 = "select count(*) from goal_node_tbl where goal_node_id=\""+uniqueID+"\"";
				String flag = SQL.getVectorSelectSQL(query0).elementAt(0);
				int flagInt = Integer.parseInt(flag);
				
				MGE.setMyVertex();
				
				//現在の日時を取得（最終更新日）
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				@SuppressWarnings("unused")
				String dateDash = format.format(date);
				
				if(flagInt == 0){
					//query = "insert into goal_node_tbl values(\""+uniqueID+"\", \""+MyGraphicEditor.getSelectedCell().getValue().toString()+"\", \"default\", \""+calenderPanel.getCalenderConetnts()+"\",  \""+dateDash+"\", \""+dateDash+"\", \""+loadMapEditor.getProjectId()+"\")";
					//SQL.operateSQL(query);
				}
				
				
					
			} else if(frmInit.getRightUpTabIndex() >= 1){
				
				
				query0 = "select count(*) from learning_node_tbl where plan_node_id=\""+uniqueID+"\"";
				String flag = SQL.getVectorSelectSQL(query0).elementAt(0);
				
				MLME.setMyVertex();
				int flagInt = Integer.parseInt(flag);
				if(flagInt == 0){
					//query = "insert into learning_node_tbl values(\""+uniqueID+"\", \""+MyLearningModelEditor.getSelectedCell().getValue().toString()+"\", \"a\", \"a\", \"a\", \"a\", \"a\", \""+loadMapEditor.getProjectId()+"\")";
				}
			}

			SQL.operateSQL(query0);
		

			
			//クリアボタン
		} else if(e.getSource() == clear){
			if(frmInit.getRightUpTabIndex() == 0){
				
				
				
				
			} else {
				
				MyLearningModelEditor.getGraph().refresh();
			}
			
			//セーブボタン
		} else if(e.getSource() == save){
			
			
			//表示されているグラフを保存する
			if(frmInit.getRightUpTabIndex() == 0){
				
				MGE.saveGraph();

			} else {
				MLME.saveGraph();
			}

			//アンドゥボタン
		} else if(e.getSource() == undo){
			MGE.getUndoManager().undo();
			//Redo
		} else if(e.getSource() == redo){
			MGE.getUndoManager().redo();
			//拡大
		} else if(e.getSource() == zoomIn){
			MyGraphicEditor.getComponent().zoomIn();
			
			//縮小
		} else if(e.getSource() == zoomOut){
			MyGraphicEditor.getComponent().zoomOut();
			
			
			
			//グラフの挿入ボタン
			//これは多分いらん
			//この処理をプロジェクト選択とかですると思う
		} else if(e.getSource() == imp){
			MyGraphicEditor.getSelectedCell().setStyle("shape=ellipse;perimeter=ellipsePerimeter;strokeColor=red;fillColor=pink;");
			MyGraphicEditor.getComponent().refresh();
			//ここで読み込むデータのIDを選択するポップアップフレームを出力する
			//MGE.setImportGraph(loadMapEditor.getProjectId());
			
		}
		
		
	}
	
	
}

