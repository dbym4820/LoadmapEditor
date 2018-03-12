//******************* 変更を確定するボタンを定義するクラス ***************************//

package jp.co.LoadMapEditor.TA;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;

public class cnfBtn extends JButton{

	private static final long serialVersionUID = 7249216181647037058L;
	
	//このクラスの本体
	JButton button;
	
	//コンストラクタ
	public cnfBtn(){
		button = new JButton("変更を確定する");
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

				new dataInsert();
				
				
				
				
				sendSQL SQL = new sendSQL();
				if(MyGraphicEditor.getSelectedCell() == null){
				} else {
					
					//現在の日時を取得（最終更新日）
					Date date = new Date();
					SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
					String dateDash = format.format(date);
					//編集処理をした時にノードの情報をDBに保存するための処理
					String updateQuery = "update goal_node_tbl set goal_node_name=\""+MyGraphicEditor.getSelectedCell().getValue().toString()+"\", goal_node_content=\""+frmInit.getTextAreaContents()+"\", deadline=\""+calenderPanel.getCalenderConetnts()+"\", last_edit_date=\""+dateDash+"\" where goal_node_id=\""+MyGraphicEditor.getSelectedCell().getId()+"\"";
					SQL.operateSQL(updateQuery);
					if(MyGraphicEditor.getSelectedCell().getValue().toString().length() >= 100){
						String newString = MyGraphicEditor.getSelectedCell().getValue().toString().substring(0,15)+"...";
						MyGraphicEditor.getSelectedCell().setValue(newString);
						MyGraphicEditor.getComponent().refresh();
					}
				}
				
				//期限などの条件によって色を変更する
				new checkNodes(MyGraphicEditor.getGraph());
			}
		});
		
	}
}
