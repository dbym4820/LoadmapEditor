//******* ツールバー設定用クラス

package jp.co.LoadMapEditor.TA;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;


public class NetworkEditorToolBar extends JToolBar implements ActionListener{

	
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
    //dropDown dD = new dropDown();
	
	//知識ネットワークモデルエディタの宣言
	MyNetworkModelEditor MNME = new MyNetworkModelEditor();
	
	sendSQL SQL = new sendSQL();
	
	//******
	//**** コンストラクタ
	//*******
	public NetworkEditorToolBar() {
		
		setInheritsPopupMenu(true);

		
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

		//ネットワークエディタのインスタンス化
		//MNME = new MyNetworkModelEditor();
		
		
		//ノードを追加するボタン
		if(e.getSource() == draw_node){
		
			MNME.setMyVertex();
			
			
			//クリアボタン
		} else if(e.getSource() == clear){
			MyNetworkModelEditor.getGraph().refresh();
			
			//セーブボタン
		} else if(e.getSource() == save){
			
			//表示されているグラフを保存する
			//保存先を変えるためにここのsaveGraph()の引数に保存先ファイルを入れることになると思う
			//ただセーブする前にアラートするから別のコンポーネントにこの処理は移す
			
			MNME.saveGraph();
			

			//アンドゥボタン
		} else if(e.getSource() == undo){
			

			MNME.getUndoManager().undo();
			
			
			//Redo
		} else if(e.getSource() == redo){
			MNME.getUndoManager().redo();
			
			//拡大
		} else if(e.getSource() == zoomIn){
			MyNetworkModelEditor.getComponent().zoomIn();
			
			//縮小
		} else if(e.getSource() == zoomOut){
			MyNetworkModelEditor.getComponent().zoomOut();
				
				
			
			//グラフの挿入ボタン
			//これは多分いらん
			//この処理をプロジェクト選択とかですると思う
		} else if(e.getSource() == imp){
			
			//ここで読み込むデータのIDを選択するポップアップフレームを出力する
			MNME.setImportGraph(loadMapEditor.getProjectId());
			
		}
		
		
	}
	
	
}
