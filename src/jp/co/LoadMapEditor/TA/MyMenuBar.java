//****** メニューバーを操作するためのクラスファイル


package jp.co.LoadMapEditor.TA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyMenuBar extends JMenuBar {
	
	
	private static final long serialVersionUID = 1L;

	public MyMenuBar(){
	
	}
	
	public JMenuBar setMenu(){
		JMenuBar menuBar = new JMenuBar();
		
		
		//ファイル操作に関するメニュー
		JMenu menuFile = new JMenu("ファイル");
		menuBar.add(menuFile);
		
		JMenuItem menuItemNew = new JMenuItem("新規作成");
		menuItemNew.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//ダイアログの起動
				selectDialog sd = new selectDialog();
		
				sd.setBounds(100, 100, 500, 500);
				sd.setVisible(true);
				sd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
				//モーダルをセット。この画面が開いてる間はアプリケーションは動かない
				sd.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
			}
		});
		
		menuFile.add(menuItemNew);
		
		JMenuItem menuItemRead = new JMenuItem("開く");
		menuItemRead.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//ダイアログの起動
				selectDialog sd = new selectDialog();
		
				sd.setBounds(100, 100, 500, 500);
				sd.setVisible(true);
				sd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
				//モーダルをセット。この画面が開いてる間はアプリケーションは動かない
				sd.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
			}
		});
		menuFile.add(menuItemRead);
		
		JMenuItem menuItemPrint = new JMenuItem("印刷");
		menuFile.add(menuItemPrint);
		
		
		
		//画面編集に関する操作のメニュー
		JMenu menuEdit = new JMenu("編集");
		menuBar.add(menuEdit);
		
		JMenuItem menuItemUndo = new JMenuItem("元に戻す");
		menuEdit.add(menuItemUndo);
		
		JMenuItem menuItemRedo = new JMenuItem("やり直し");
		menuEdit.add(menuItemRedo);
		
		
		//ヘルプに関するメニュー
		//htmlを表示させて使い方とかを見せる？
		JMenu menuHelp = new JMenu("ヘルプ");
		menuBar.add(menuHelp);
		
		JMenuItem menuItemDocument = new JMenuItem("操作法に関するドキュメントを見る");
		menuHelp.add(menuItemDocument);
		
		
		return menuBar;
	}
	
	
	
	
}
