//************************* グラフを保存していいかどうか尋ねるフレームのクラス ************************//

package jp.co.LoadMapEditor.TA;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class saveComp extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//表示するフレーム
	private JFrame frm = new JFrame();
	
	//OKボタン
	private JButton btnOk;
	//キャンセルボタン
	JButton btnCancel;
	
	//コンストラクタ
	public saveComp(){
		
	}

	//表示設定
	public JFrame setSaveFrame(){
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(splitPane);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane.setRightComponent(splitPane_1);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(this);
		splitPane_1.setLeftComponent(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		splitPane_1.setRightComponent(btnCancel);
		splitPane_1.setDividerLocation(150);
		
		JPanel panel = new JPanel();
		panel.setLocation(new Point(300, 200));
		panel.setPreferredSize(new Dimension(500, 500));
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblOoj = new JLabel("モデルを保存します");
		panel.add(lblOoj, BorderLayout.CENTER);
		splitPane.setDividerLocation(70);
		
		return frm;
	}
	
	
	
	//アクションリスナ
	//OKボタンならファイルエクスポート
	//この辺りの保存先をデータベースに移す
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnOk){
			//new fileExport(MyGraphicEditor.getGraph());
			//編集中のグラフを編集中のプロジェクトIDで格納
			new dataInsert();
			dispose();
		} else if(e.getSource() == btnCancel){
			dispose();
		}
	}
}
