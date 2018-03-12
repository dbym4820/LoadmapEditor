//******************* データベースにグラフなんかを保存するクラス ********************//
//*******************　このクラスはプロジェクトの指定を確認した時に自動的に起動しててほしい *********//

package jp.co.LoadMapEditor.TA;


import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import org.w3c.dom.Node;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxUtils;



public class dataInsert {

	

	//コンストラクタ
	//パラメータは受け取ったグラフ
	//パラメータmodeはなにをインサートするかによって処理を分岐させるためのモード
	public dataInsert(){
		
		
		//XMLにエンコード
		mxCodec codec = new mxCodec();
		Node node = codec.encode(MyGraphicEditor.getGraph().getModel());
		StringBuffer GoalXml = new StringBuffer(mxUtils.getPrettyXml(node));
		String GoalXmlString = new String(GoalXml);
		
		
		
		mxCodec codec1 = new mxCodec();
		Node node1 = codec1.encode(MyNetworkModelEditor.getGraph().getModel());
		StringBuffer NetXml = new StringBuffer(mxUtils.getPrettyXml(node1));
		String NetXmlString = new String(NetXml);
		
		
		sendSQL sql = new sendSQL();
		
		//プロジェクトIDごとに目標XMLとネットワークXMLが保存
		sql.insertGraphTreeTable(loadMapEditor.getProjectId(), GoalXmlString, NetXmlString, frmInit.getProjectMode());
		
		
		//目標IDと学習プランXMLを関連付け
		 //MyLearningModelEditor.insertDataIntoDB();
		MyLearningModelEditor.getComponent().refresh();
		
	}

	//ユーザ環境の画面サイズを獲得するメソッド
	public Dimension getEnvironmentalSize(){
		Dimension screenSize = null;
		try{
			GraphicsEnvironment graphicsEnvironment =GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice graphicsDevice =graphicsEnvironment.getDefaultScreenDevice();
			Rectangle rect=graphicsDevice.getDefaultConfiguration().getBounds();
			screenSize=rect.getSize();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return screenSize;
	}
	
	
	
	
	
}
