//**************** アプリケーション起動時にポップアップしプロジェクトの選択をするダイアログ ***************//
//このダイアログが開いている間アプリケーション本体はロック
//ここでプロジェクトの選択をしないと編集に進めないようにする

package jp.co.LoadMapEditor.TA;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class selectDialog extends JDialog{

	
	private static final long serialVersionUID = 1L;
	
	//プロジェクトが新規か編集かを判定する変数.TRUEなら新規プロジェクト
	private static boolean isNewProject;
	
	//ハリボテ用のラベルとテキストボックス
	private JTextField textField;
	private JLabel label;
	
	//プロジェクト選択用のコンボボックス
	private JComboBox<String> projectBox;
	
	//SQL
	private sendSQL SQL = new sendSQL();
	
	//エラー表示を行うためのラベル
	private JLabel errorLabel;

	//コンストラクタ
	public selectDialog(){
		
		this.setDialog();
		this.setProject();
		
	}
	
	//画面構成をセットするメソッド
	public void setDialog(){
			
		

		//ボタンをのせるパネル
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		//起動ボタン
		JButton boot = new JButton("起動");
		buttonPanel.add(boot);
		//キャンセルボタン
		JButton cancel = new JButton("キャンセル");
		buttonPanel.add(cancel);
		//レイアウト
		
		
		//後で使う
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{170, 62, 47, 150, 0};
		gbl_panel_1.rowHeights = new int[]{37, 0, 36, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JPanel panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_6.insets = new Insets(0, 0, 5, 5);
		gbc_panel_6.gridx = 1;
		gbc_panel_6.gridy = 1;
		panel_1.add(panel_6, gbc_panel_6);
		
		
		
		
		JLabel lblNewLabel = new JLabel("プロジェクトを選択");
		panel_6.add(lblNewLabel);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_5.anchor = GridBagConstraints.NORTH;
		gbc_panel_5.insets = new Insets(0, 0, 5, 5);
		gbc_panel_5.gridx = 1;
		gbc_panel_5.gridy = 2;
		panel_1.add(panel_5, gbc_panel_5);
		
		projectBox = new JComboBox<String>();
		panel_5.add(projectBox);
		projectBox.addItem("");
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 4;
		panel_1.add(panel, gbc_panel);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		
		label = new JLabel("新規にプロジェクトを作成");
		label.setHorizontalTextPosition(SwingConstants.LEFT);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(label);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_4.insets = new Insets(0, 0, 5, 5);
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 5;
		panel_1.add(panel_4, gbc_panel_4);
		
		textField = new JTextField("");
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(textField);
		textField.setColumns(10);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.gridwidth = 4;
		gbc_panel_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_8.insets = new Insets(0, 0, 5, 5);
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 6;
		panel_1.add(panel_8, gbc_panel_8);
		
		
		//何も選択するものがないときにエラーを表示するためのダミー
		errorLabel = new JLabel("");
		panel_8.add(errorLabel);
		
		
			
			
		//起動ボタンでアプリケーション起動
		boot.addMouseListener(new MouseAdapter(){
				
			@Override
			public void mouseClicked(MouseEvent e){
				
				
				loadMapEditor LME = new loadMapEditor();
				
				//新規にプロジェクトを作成するときはテキスト欄に入力
				String project_name = textField.getText();
				
				//入力欄が空白以外なら新規プロジェクトを立ち上げる
				if(!project_name.equals("")){ 
										
					//新規プロジェクトであることを示す
					setIsNewProject(true);
					//編集するプロジェクトのIDを設定
					loadMapEditor.setProjectId(project_name);
					//プロジェクト選択済みフラグを立てる
					LME.setSelectFlag(true);
					//本体の起動
					LME.startApplication();
					//新規登録時は初期状態をデータベースに登録しておく
					new dataInsert();
					
					
					//ダイアログウィンドウを閉じる
					closeWindow();	
					
				} else if(projectBox.getSelectedItem().toString().equals("")){
					setErrorLabel("新規プロジェクト名を入力するか<br />既存のプロジェクトを選択してください");
					
				//入力欄が空白の時選択されているプロジェクトが編集される
				} else if(!(projectBox.getSelectedItem().toString().equals(""))){
	
				
					//編集プロジェクトであることを表す
					setIsNewProject(false);
					//選択中のコンボボックス内の値をテキストとして拾ってくる
					project_name = projectBox.getSelectedItem().toString();
					//編集中のプロジェクトIDをセット
					loadMapEditor.setProjectId(project_name);
					//プロジェクト選択フラグを立てる
					LME.setSelectFlag(true);
					//本体の起動
					LME.startApplication();
				
					//ダイアログウィンドウを閉じる
					closeWindow();	
				}
			}	
		});
			
		//キャンセルボタンで閉じる
		cancel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				closeWindow();	
			}
		});
	}
	
	//プロジェクト選択画面を閉じるためのメソッド
	public void closeWindow(){
		this.dispose();
	}

	
	//プロジェクトをデータベースから調べてコンボボックスに全部突っ込むためのメソッド
	public void setProject(){
		Vector<String> projects = new Vector<String>();
		String query = "select project_id from graph_xml_tbl";
		projects = SQL.getVectorSelectSQL(query);
		int i;
		for(i=0; i<projects.size(); i++){
			projectBox.addItem(projects.elementAt(i));
			
		}
		projectBox.setSelectedIndex(projectBox.getItemCount()-1);
	}
	
	//プロジェクトが新規か編集かの値を操作するメソッド
	public static void setIsNewProject(boolean bool){
		isNewProject = bool;
	}
	
	//プロジェクトの新規・編集を設定するメソッド
	public static boolean getIsNewProject(){
		return isNewProject;
	}
	
	//エラーラベルを貼り付けるところ
	public void setErrorLabel(String errorText){
		errorLabel.setText("<html><body><p><br /><br /><br /><span style='color:red;'><strong><I>"+errorText+"</I></strong></span></p></body></html>");
	}
	
}
