package jp.co.LoadMapEditor.TA;

import javax.swing.JDialog;

public class loadMapEditor {
	

	//選択されたプロジェクトID
	private static String project_id = new String("");
		
	//プロジェクトが選択されている華道家のフラグ
	private static boolean select_project_flag = false;


	
	
	//アプリケーション起動時のメインメソッド
	public static void main(String[] args) {
		
		//Mac OS X用のメニューバー
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		loadMapEditor LME = new loadMapEditor();
		
		//ダイアログを起動してプロジェクトを選択する
		LME.checkFlagDialog();
	}



	//コンストラクタ
	public loadMapEditor(){
	}
	
	//アプリケーション本体起動
	public void startApplication(){
		//アプリケーションタイトル
		String title = new String("ロードマップエディタプランエディタ - "+loadMapEditor.getProjectId());
		//アプリケーションイニシャライザ
		frmInit frm = new frmInit();
		//タイトル表示
		frm.setTitle(title);
		//アプリケーション表示
		frm.setVisible(true);
	}
	
	//プロジェクト選択ダイアログの起動
	public void checkFlagDialog(){
		if(project_id != ""){
			//ダイアログの起動
			selectDialog sd = new selectDialog();
			
			sd.setBounds(100, 100, 500, 500);
			sd.setVisible(true);
			sd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			
			//モーダルをセット。この画面が開いてる間はアプリケーションは動かない
			sd.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
				
				
			//この値をダイアログの方で切り替える
			setSelectFlag(false);
		} else {
			
		}
	}
	

	
	//プロジェクト選択フラグのセッター
	public void setSelectFlag(boolean flag){
		select_project_flag = flag;
	}
	
	//プロジェクト選択フラグゲッター
	public boolean getSelectFlag(){
		return select_project_flag;
	}

	public static void setProjectId(String pro_id){
		project_id = pro_id;
	}
	
	public static String getProjectId(){
		return project_id;
	}

	
}
