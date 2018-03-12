//******************* 子ノードを追加するボタンを定義するクラス ***************************//

package jp.co.LoadMapEditor.TA;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class addChildNodeButton extends JButton{

	private static final long serialVersionUID = 7249216181647037058L;
	
	//このクラスの本体
	JButton button;
	
	//コンストラクタ
	public addChildNodeButton(){
		button = new JButton("ノード追加");
		this.setAct();
	}
	
	//このボタンを返すクラス
	public JButton MyButton(){
		return button;
	}
	
	//このボタンクラスにアクションリスナをセットするクラス
	private void setAct(){
	
		button.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){

				new updateInsertPosition();
				
				
			//if(右上のエディタがアクティブの時){
				//目標モデルエディタがアクティブの時
				if(frmInit.getTabbed_rightUp().getSelectedIndex() == 0){
					
					MyGraphicEditor MGE = new MyGraphicEditor();
					String mes = frmInit.getDropDownMenuItem();
					String NodeName = frmInit.getNameField();

					if(mes == "モードを選択してください"){
						mes = "default";
					}
					//どれかのノードが選択されていれば以下の処理
					if(MyGraphicEditor.getSelectedCell() != null){

					
						//オブジェクトの挿入
						Object obje = MGE.setMyVertex(mes, NodeName);
					
						//選択されている時なので、親とのリンクを結ぶ
						MGE.setMyEdge(MyGraphicEditor.getSelectedCell(), obje);
					
						//なんのノードも選択されていない時
					} else {
						//今はデフォルト設定で挿入してるけど、画面の左上とかに表示させたい
						MGE.setMyVertex(mes, NodeName);
					}
				
					//3番目以降のタブがアクティブの時
				} else if(frmInit.getTabbed_rightUp().getSelectedIndex() >= 1){
					
					MyLearningModelEditor MLME = new MyLearningModelEditor();
					String mes = frmInit.getDropDownMenuItem();
					String NodeName = frmInit.getNameField();

					if(mes == "モードを選択してください"){
						mes = "default";
					}	
					//どれかのノードが選択されていれば以下の処理
					if(MyLearningModelEditor.getSelectedCell() != null){
					
						//オブジェクトの挿入
						Object obje = MLME.setMyVertex(mes, NodeName);
					
						//選択されている時なので、親とのリンクを結ぶ
						MLME.setMyEdge(MyLearningModelEditor.getSelectedCell(), obje);
					}
				}
		
				
				
					
				MyNetworkModelEditor MNME = new MyNetworkModelEditor();
				MNME.setMyVertex();
				
			
			}
		});
		
	}
}
