//********* ドロップダウンメニューを作成するクラス *********//
//******** 現在はまだ使われていない *********//


package jp.co.LoadMapEditor.TA;

import java.awt.Dimension;

import javax.swing.JComboBox;

public class dropDown extends JComboBox<Object> {
	
	
	private static final long serialVersionUID = 1L;

	//ドロップダウンコンポーネントの宣言
	JComboBox<Object> cBox;
	
	//中身を追加
	Object[] Items = {"追加するノードのモードを選択してください", "sample1", "sample2"};
	
	//コンストラクタ
	public dropDown(){
		
	}
	
	//ドロップダウンメニューを取得するメソッド
	public JComboBox<Object> setComboBox(){
		
		cBox = new JComboBox<Object>(Items);
		cBox.addItem("aaaaaa");
	
		return cBox;
	}
	
	@Override
	public void setPreferredSize(Dimension d){
		cBox.setPreferredSize(d);
	}
	
	
}
