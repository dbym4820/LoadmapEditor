//**************** アプリケーション起動時にポップアップしプロジェクトの選択をするダイアログ ***************//

package jp.co.LoadMapEditor.TA;

public class test{
	public static void main(String args[]){
		sendSQL getSQL = new sendSQL();
		String getContentQuery = "select goal_node_content from goal_node_tbl where goal_node_id=\"20160102141515104334324033360\";";
		String nodeContent = getSQL.getVectorSelectSQL(getContentQuery).elementAt(0);
		System.out.println(nodeContent);
		
	}
}