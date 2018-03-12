//************* 目標数をXMLを基にカウントするクラス **************//

package jp.co.LoadMapEditor.TA;

import org.w3c.dom.Node;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxUtils;

public class countGoalNode {
	
	//コンストラクタ
	public countGoalNode(){
	}
	
	
	
	public static int count(){
		//xmlのデコード
		mxCodec codec = new mxCodec();
		Node node = codec.encode(MyGraphicEditor.getGraph().getModel());
		StringBuffer GoalXml = new StringBuffer(mxUtils.getPrettyXml(node));
		String GoalXmlString = new String(GoalXml);
		
		//一行ごとに取り出し
		String[] xml = GoalXmlString.split("\n");
		String checkSum = "      <mxGeometry as=\"geometry\" height=";
		String checkSum2 = "    <mxCell id=\"and_";
		//当てはまる行が何行あるか
		int count = 0;
		//and素子の数を数える（後で総数からマイナスする）
		
		int andcount = 0;
		
		int i;
		for(i = 0; i < xml.length; i++){
			
			if(xml[i].startsWith(checkSum)){
				count++;
			}
			
			if(xml[i].startsWith(checkSum2)){
				andcount++;
			}
		}

		count -= andcount;
		return count;
	}
	
}
