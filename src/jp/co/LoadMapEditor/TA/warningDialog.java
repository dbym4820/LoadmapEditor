//**************** アプリケーション起動時にポップアップしプロジェクトの選択をするダイアログ ***************//
//このダイアログが開いている間アプリケーション本体はロック
//ここでプロジェクトの選択をしないと編集に進めないようにする

package jp.co.LoadMapEditor.TA;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class warningDialog extends JDialog{

	
	private static final long serialVersionUID = 1L;
	
	private JLabel warningLabel = new JLabel(); 
	
	
	//SQL
	@SuppressWarnings("unused")
	private sendSQL SQL = new sendSQL();

	
	
	//コンストラクタ
	public warningDialog(){
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
		panel.add(warningLabel, BorderLayout.CENTER);
		
		JButton okBtn = new JButton("OK");
		okBtn.addMouseListener(new MouseAdapter(){
				
			@Override
			public void mouseClicked(MouseEvent e){
				closeWindow();
			}
		});
		
		panel.add(okBtn, BorderLayout.SOUTH);

	}
	
	//プロジェクト選択画面を閉じるためのメソッド
	public void closeWindow(){
		this.dispose();
	}
	
	//ダイアログのテキストをセットするメソッド
	public void setWarningText(String str){
		warningLabel.setText("<html><span style=\"color: red\">"+str+"</span></html>");
	}

	

	
}

