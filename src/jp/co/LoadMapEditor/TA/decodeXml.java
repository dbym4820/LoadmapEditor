//***************** XMLテキストをデコードしてグラフに変換するクラス ****************//

package jp.co.LoadMapEditor.TA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.mxgraph.view.mxGraph;

public class decodeXml {

	//台紙的ななにか
	Object parent = new Object();
	
	//貼り付けるグラフ格納用
	mxGraph graph = new mxGraph();
	
	//読み込んだXMLコード
	String xmlCode = null;
	
	//コンストラクタ
	decodeXml(){
		
		//読み込むファイルの選択
		//本番はデータベースから選択
		File xmlFile = new  File("CreatePicts/sample.xml");
		
		
		try {
			
			//ファイルの読み込み
			FileReader fR = new FileReader(xmlFile);
			xmlCode = new String();
			int ch;
			while((ch = fR.read()) != -1){
				xmlCode += (char)ch;
			}	

			fR.close();
		} catch (FileNotFoundException e) {
			
		} catch (IOException e){
			
		}
		

		
		parent = graph.getDefaultParent();
		

	}
	

	public static void main(String[] args) {
		new decodeXml();
	}

}
