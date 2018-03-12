//******************** 挿入するグラフの位置を更新するためのクラス **********************//
//******************** グラフの状態を監視する **********************//


package jp.co.LoadMapEditor.TA;

public class updateInsertPosition {

	@SuppressWarnings("unused")
	private static MyGraph grf = new MyGraph();
	
	//引数なしコンストラクタ
	public updateInsertPosition(){
		grf = MyGraphicEditor.getGraph();
		setNewPosition();
	}
	
	//引数ありコンストラクタ
	public updateInsertPosition(int dx, int dy){
		grf = MyGraphicEditor.getGraph();
		setNewPosition(dx, dy);
	}
	
	//引数なしの時
	public void setNewPosition(){
		
		//もし挿入しようとしたところにノードがすでにあればも一個ずらす処理を追記する
		
		
		//入力するグラフの座標を変更する				
		int x = (int)MyGraphicEditor.getSelectedCellPosition().getX();
		int y = (int)MyGraphicEditor.getSelectedCellPosition().getY();
		MyGraphicEditor.updateNewPos(x, y+80);
	}
	
	//引数ありの時
	public void setNewPosition(int dx, int dy){
		//入力するグラフの座標を変更する				
		int x = (int)MyGraphicEditor.getSelectedCellPosition().getX();
		int y = (int)MyGraphicEditor.getSelectedCellPosition().getY();
		MyGraphicEditor.updateNewPos(x+dx, y+dy);
		
	}
	
}
