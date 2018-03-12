//*************** ノードの状態に合わせてスタイルを変更するルールを記述するクラス *******************//

package jp.co.LoadMapEditor.TA;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxXmlUtils;

public class checkLearningNodes {

	//コンストラクタ
	public checkLearningNodes(){
		String planQuery = "select plan_xml from goal_to_plan_xml_tbl where goal_node_id=\""+MyLearningModelEditor.getEditTargetId()+"\"";
		sendSQL sql = new sendSQL();
		String textSQL = sql.getVectorSelectSQL(planQuery).elementAt(0);
			
		try{
			Document doc = mxXmlUtils.parseXml(textSQL);
			mxCodec codec = new mxCodec(doc);
			codec.decode(doc.getDocumentElement(), MyLearningModelEditor.getGraphModel());
		} catch (Exception ex){
			ex.printStackTrace();
		}
		MyLearningModelEditor.getGraph().setModel(MyLearningModelEditor.getGraphModel());
		
	}
}
	