//********************
//****** このクラスファイルは画面全体の構成を設定するためのもの 
//*********************

package jp.co.LoadMapEditor.TA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.w3c.dom.Node;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxUtils;


public class frmInit extends JFrame implements ActionListener{

	
	private static final long serialVersionUID = 1L;
	
	//ノード名を表示するフィールド
	private static JTextField nameField;
	
	//右上のタブペイン
	private static JTabbedPane tabbedPane_rightUp;
	
	//右下のタブペイン
	private static JTabbedPane tabbedPane_rightDown;
	
	
	
	@SuppressWarnings("unused")
	private static String NameFieldString;
	
	
	//ドロップダウンボックスの表示
	//これは後で消すことになるかも
	@SuppressWarnings("unused")
	private static dropDown modeBox;
	
	//ユーザーのプロジェクト内の情況モードを表示するドロップダウンメニュー
	private static dropDown modeBox2;
	
	private static JTextArea textArea;
	
	//全体を構成する左右を分割するスプリットペイン
	private JSplitPane splitPane;
	
	//右側を構成するエディタの上下を分割するスプリットペイン
	private JSplitPane splitPane_top;
	
	//分割目標エディタのパネル
	private MyGraphicEditor	objectEditor = new MyGraphicEditor();
	
	//学習プランエディタの
	private static MyLearningModelEditor learnModelEditor;
	
	//知識ネットワークエディタのパネル
	private MyNetworkModelEditor semanticNetworkEditor;
	
	//カレンダーのパネル
	private calenderPanel calender;
	
	//目標数を表示するエリア
	private static JTextField targetNum;

	//******
	//*** コンストラクタ
	//*******
	public frmInit() {
		//ユーザの環境の画面の大きさ
		double width = this.getEnvironmentalSize().getWidth();
		double height = this.getEnvironmentalSize().getHeight();
		int widthInt = (int)width;
		int heightInt = (int)height;
		
		
		
		
		
		//基本画面情報
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(new Rectangle(41, 23, widthInt, heightInt));
		
		//****
		//** ここからトップメニュー
		//****
		MyMenuBar menuBar = new MyMenuBar();
		setJMenuBar(menuBar.setMenu());
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		
		
		
		
		//****
		//** ここから画面分割設定
		//****
		splitPane = new JSplitPane();
		splitPane.setPreferredSize(new Dimension(200, 33));
		splitPane.setMinimumSize(new Dimension(21, 21));
		getContentPane().add(splitPane);
		
		splitPane_top = new JSplitPane();
		splitPane_top.setPreferredSize(new Dimension(205, 200));
		splitPane_top.setMinimumSize(new Dimension(205, 100));
		splitPane_top.setDividerSize(10);
		splitPane_top.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_top);
		
		
		//***********
		//****** 右上
		//***********
		tabbedPane_rightUp = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_rightUp.setDoubleBuffered(true);
		tabbedPane_rightUp.setOpaque(true);
		tabbedPane_rightUp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		tabbedPane_rightUp.setPreferredSize(new Dimension(21, 500));
		splitPane_top.setLeftComponent(tabbedPane_rightUp);
		
		
		
		if(selectDialog.getIsNewProject() == true){
			//新規プロジェクトを選択した時だけって言う制約をつける
			objectEditor.setFirstGraph();
		} else if(selectDialog.getIsNewProject() == false){
			//選択したプロジェクトの読み込み
			objectEditor.setImportGraph(loadMapEditor.getProjectId());
		}
		
		
		tabbedPane_rightUp.addTab("達成目標編集エリア", null, objectEditor.setEditor(), null);
		

		//**********
		//***** 右下
		//**********
		tabbedPane_rightDown = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_rightDown.setMinimumSize(new Dimension(21, 100));
		tabbedPane_rightDown.setPreferredSize(new Dimension(21, 300));
		splitPane_top.setRightComponent(tabbedPane_rightDown);

		//知識ネットワークエディタのインスタンス化
		semanticNetworkEditor = new MyNetworkModelEditor();
		
		if(selectDialog.getIsNewProject() == true){
			//新規プロジェクトを選択した時だけって言う制約をつける
			semanticNetworkEditor.setFirstGraph();
		} else if(selectDialog.getIsNewProject() == false){
			//選択したプロジェクトの読み込み
			semanticNetworkEditor.setImportGraph(loadMapEditor.getProjectId());
		}
		
		tabbedPane_rightDown.addTab("知識ネットワーク編集エリア", null, semanticNetworkEditor.setEditor(), null);
		
		
		//***********
		//******* 左側
		//**********
		JTabbedPane tabbedPane_left = new JTabbedPane(SwingConstants.TOP);
		tabbedPane_left.setMinimumSize(new Dimension(100, 21));
		tabbedPane_left.setPreferredSize(new Dimension(300, 1000));
		splitPane.setLeftComponent(tabbedPane_left);
		
		JScrollPane inputArea = new JScrollPane();
		tabbedPane_left.addTab("詳細入力エリア", null, inputArea, null);
		
		//パネル貼り付けとボックスレイアウト
		JPanel inputPanel = new JPanel();
		inputPanel.setPreferredSize(new Dimension(20, 10));
		inputArea.setViewportView(inputPanel);
		GridBagLayout gbl_inputPanel = new GridBagLayout();
		gbl_inputPanel.columnWidths = new int[]{0, 0};
		gbl_inputPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_inputPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_inputPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		inputPanel.setLayout(gbl_inputPanel);
		
		//ノード名表示
		JLabel nodeNameLabel = new JLabel("ノード名");
		GridBagConstraints gbc_nodeNameLabel = new GridBagConstraints();
		gbc_nodeNameLabel.insets = new Insets(0, 0, 5, 0);
		gbc_nodeNameLabel.gridx = 0;
		gbc_nodeNameLabel.gridy = 1;
		inputPanel.add(nodeNameLabel, gbc_nodeNameLabel);
		
		
		//ノード名が表示されるフィールド
		nameField = new JTextField();
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 0);
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 0;
		gbc_nameField.gridy = 2;
		inputPanel.add(nameField, gbc_nameField);
		nameField.setColumns(10);
		
		
		
		
		//ノード内容表示
		JLabel nodeINdexLabel = new JLabel("達成するのに必要なもの・こと");
		GridBagConstraints gbc_nodeINdexLabel = new GridBagConstraints();
		gbc_nodeINdexLabel.insets = new Insets(0, 0, 5, 0);
		gbc_nodeINdexLabel.gridx = 0;
		gbc_nodeINdexLabel.gridy = 3;
		inputPanel.add(nodeINdexLabel, gbc_nodeINdexLabel);
		
		//ノードの内容などが表示されることになるであろう(テキスト)エリア
		textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 10, 5, 10);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 6;
		inputPanel.add(textArea, gbc_textArea);
		

		//後々このフィールドはなくなってモード制御は自動で行うようにする
		/*
		//制御モード表示
		JLabel modeLabel = new JLabel("モード");
		GridBagConstraints gbc_modeLabel = new GridBagConstraints();
		gbc_modeLabel.insets = new Insets(0, 0, 5, 0);
		gbc_modeLabel.gridx = 0;
		gbc_modeLabel.gridy = 7;
		inputPanel.add(modeLabel, gbc_modeLabel);

		//ドロップダウンメニューを設定
		
		modeBox = new dropDown();
		modeBox.setComboBox();
		String[] modeMenu = {"モードを選択してください", "mode1", "mode2"};
		for(String menuList: modeMenu){
			modeBox.addItem(menuList);
		}
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 8;
		inputPanel.add(modeBox, gbc_comboBox);
		*/
		
		//ノードの期限を設定する用途
		JLabel deadLineDay = new JLabel("期限");
		GridBagConstraints gbc_deadLineDay = new GridBagConstraints();
		gbc_deadLineDay.insets = new Insets(0, 0, 5, 0);
		gbc_deadLineDay.gridx = 0;
		gbc_deadLineDay.gridy = 10;
		inputPanel.add(deadLineDay, gbc_deadLineDay);
		
		
		//カレンダーをインスタンス化
		calender = new calenderPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 50, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 14;
		inputPanel.add(calender.getBasePanel(), gbc_panel);
		
		
		
		//追加ボタン押下時の処理
		addChildNodeButton confirmBtn = new addChildNodeButton();
		GridBagConstraints gbc_confirmBtn = new GridBagConstraints();
		gbc_confirmBtn.insets = new Insets(0, 0, 5, 0);
		gbc_confirmBtn.gridx = 0;
		gbc_confirmBtn.gridy = 17;
		inputPanel.add(confirmBtn.MyButton(), gbc_confirmBtn);

		
		//追加ボタン押下時の処理
		cnfBtn cnfBtn = new cnfBtn();
		GridBagConstraints gbc_cnfBtn = new GridBagConstraints();
		gbc_cnfBtn.insets = new Insets(0, 0, 5, 0);
		gbc_cnfBtn.gridx = 0;
		gbc_cnfBtn.gridy = 20;
		inputPanel.add(cnfBtn.MyButton(), gbc_cnfBtn);
		
		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.EAST);
		
		//目標数ラベル
		JLabel projectFieldLabel = new JLabel("目標数：");
		projectFieldLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		projectFieldLabel.setForeground(Color.RED);
		panel_1.add(projectFieldLabel);
		projectFieldLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		projectFieldLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		projectFieldLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		//目標数を表示するテキストストフィールド
		targetNum = new JTextField();
		panel_1.add(targetNum);
		targetNum.setEditable(false);
		targetNum.setHorizontalAlignment(SwingConstants.CENTER);
		targetNum.setPreferredSize(new Dimension(5, 26));
		targetNum.setColumns(4);
		
		
		//目標数ラベル
		JLabel projectFieldLabel2 = new JLabel(", 現在の自分の状態：");
		projectFieldLabel2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		projectFieldLabel2.setForeground(Color.RED);
		panel_1.add(projectFieldLabel2);
		projectFieldLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);
		projectFieldLabel2.setAlignmentX(Component.RIGHT_ALIGNMENT);
		projectFieldLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
			
		
		//ドロップダウンメニューを設定
		modeBox2 = new dropDown();
		panel_1.add(modeBox2);
		modeBox2.setAlignmentX(Component.RIGHT_ALIGNMENT);
		modeBox2.setToolTipText("COCOMにおける制御モードの選択");
		modeBox2.setMaximumSize(new Dimension(200, 20));
		modeBox2.setOpaque(true);
		modeBox2.setMinimumSize(new Dimension(10, 10));
		modeBox2.setComboBox();
		modeBox2.setPreferredSize(new Dimension(100, 15));
		
		
		//プロジェクトの情況とかを表示する
		String[] modeMenu2 = {"特に何も考えていない"/*混乱状態制御モード*/, "行き当たりばったり"/*機会主義的制御モード*/, "今の状況は把握できている"/*戦術的制御モード*/, "将来的なことまで把握・計画済み"/*戦略的制御モード*/};
		for(String menuList: modeMenu2){
			modeBox2.addItem(menuList);
		}
		
		
		//プロジェクトモードを設定する
		//モードを取得
		//新規プロジェクトの時はモード選択できないので例外処理
		if(selectDialog.getIsNewProject() == false){
			String getModeQuery = "select project_mode from graph_xml_tbl where project_id = \'"+loadMapEditor.getProjectId()+"\'";
			sendSQL sql = new sendSQL();
			String mode = sql.getVectorSelectSQL(getModeQuery).elementAt(0);
			int modeIndex = Integer.parseInt(mode);
			frmInit.setProjectMode(modeIndex);
		}
		

		//目標数取得
		mxCodec codec = new mxCodec();
		Node node = codec.encode(MyGraphicEditor.getGraph().getModel());
		StringBuffer GoalXml = new StringBuffer(mxUtils.getPrettyXml(node));
		String xml = new String(GoalXml);
		//表示中のノードのIDを全取得
		String[] xmlDash = xml.split("\n");
		int indexAfter;
		int indexBefore;
		int i;
		String idBefore = null;
		//表示中ノードのIDが格納されたベクトル
		Vector<String> ids = new Vector<String>();
		for(i=0; i<xmlDash.length; i++){
			if(xmlDash[i].startsWith("    <mxCell id=") && !(xmlDash[i].startsWith("    <mxCell id=\"0\"")) && !(xmlDash[i].startsWith("    <mxCell id=\"1\"")) && !(xmlDash[i].startsWith("    <mxCell id=\"dammy_")) && !(xmlDash[i].startsWith("    <mxCell id=\"and_")) && !(xmlDash[i].startsWith("    <mxCell id=\"mode_"))){
				indexAfter = xmlDash[i].indexOf("\" parent");
				indexBefore = xmlDash[i].indexOf("id");
				idBefore = xmlDash[i];
				if(!(Double.valueOf(idBefore.substring(indexBefore+4, indexAfter)) <= Double.valueOf("100000000000000000"))){
					ids.addElement(idBefore.substring(indexBefore+4, indexAfter));
				}
			}
		}
		
		//目標数が変わったら、数を再セット
		setTargetNum(String.valueOf(ids.size()));
		
		

	}

	//ノード名表示のフィールドに文字を格納するためのメソッド
	public static void setNameField(String name){
		NameFieldString = name;
		nameField.setText(name);
	}
	
	//ノード名フィールドの文字列を取得するメソッド
	public static String getNameField(){
		return nameField.getText();
	}


	
	//アクティブな右上のタブを変更するメソッド
	public static void setTabbedIndex(int index){
		tabbedPane_rightUp.setSelectedIndex(index);
	}
	
	//アクティブな右上のエディタタブ自体のインスタンスを取得するメソッド
	public static JTabbedPane getTabbed_rightUp(){
		return tabbedPane_rightUp;
	}
	
	//アクティブな右上のタブのインデックスを取得するためのメソッド
	public static int getRightUpTabIndex(){
		return tabbedPane_rightUp.getSelectedIndex();
	}
	
	//アクティブな右下のエディタタブ自体のインスタンスを取得するメソッド
	public static JTabbedPane getTabbed_rightDwon(){
		return tabbedPane_rightDown;
	}
	
	//アクティブな右上のタブのインデックスを取得するためのメソッド
	public static int getRightDownTabIndex(){
		return tabbedPane_rightDown.getSelectedIndex();
		//return 1;
	}
	
	//テキストエリアの文字列を取得するメソッド
	public static String getTextAreaContents(){
		return textArea.getText();
	}
	
	//テキストエリアに文字列を追加するメソッド
	public static void setTextAreaContents(String text){
		textArea.setText(text);
	}

	
	//選択されたドロップダウンメニューの値を取得するメソッド
	public static String getDropDownMenuItem(){
		//return (String)modeBox.getSelectedItem();
		return "default";
	}
	
	//選択中のプロジェクト内状態モードを取得するメソッド
	public static int getProjectMode(){
		return modeBox2.getSelectedIndex();
	}
	
	//オブジェクトのモードを設定するメソッド
	public static void setProjectMode(int modeIndex){
		modeBox2.setSelectedIndex(modeIndex);
	}
	
	//目標数をセットするメソッド
	public static void setTargetNum(String num){
		targetNum.setText(num);
		targetNum.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		targetNum.setForeground(Color.RED);
		
	}

	//目標数を取得するメソッド
	public static String getTargetNum(){
		return targetNum.getText();
	}
	
	
	//動的に学習モプランエディタのタブを生成するメソッド
	public static MyLearningModelEditor setNewLearningModelEditorTab(String tabName){
		learnModelEditor = new MyLearningModelEditor();
		tabbedPane_rightUp.addTab(tabName+"の学習プラン編集エリア", null, learnModelEditor.setEditor(), null);
		return learnModelEditor;
	}
	
	
	
	//ユーザ環境の画面サイズを獲得するメソッド
	public Dimension getEnvironmentalSize(){
		Dimension screenSize = null;
		try{
            GraphicsEnvironment graphicsEnvironment =GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice graphicsDevice =graphicsEnvironment.getDefaultScreenDevice();
            Rectangle rect=graphicsDevice.getDefaultConfiguration().getBounds();
            screenSize=rect.getSize();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
		return screenSize;
	}
	
	//アクティブなコンポーネントを取得するメソッド
	public JPanel getActiveComponent(){
		return semanticNetworkEditor;
	}
	
	
	
	//アクションリスナー
	@Override
	public void actionPerformed(ActionEvent e){
	}
}
