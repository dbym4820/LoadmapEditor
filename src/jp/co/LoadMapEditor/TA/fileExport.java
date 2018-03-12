//******** 中央画面で編集したグラフをファイルとしてエクスポートするクラス *********//


package jp.co.LoadMapEditor.TA;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;



public class fileExport {

	@SuppressWarnings("unused")
	private Object parent = new Object();
	
	//コンストラクタ
	public fileExport(){
		
	}
	
	
	//コンストラクタ
	//パラメータは受け取ったグラフ
	@SuppressWarnings("unused")
	public fileExport(mxGraph graph){
		
		//XMLにエンコード
		parent = graph.getDefaultParent();
		mxCodec codec = new mxCodec();
		Node node = codec.encode(graph.getModel());
		StringBuffer nodeXml = new StringBuffer(mxUtils.getPrettyXml(node));
		String NodeXmlString = new String(nodeXml);
	
		
		
		mxDomUtils htmlXml = new mxDomUtils();
		mxDomUtils.createHtmlDocument();
		
		 
		//ファイルに書き込み
		try {
			File file =  new File("CreatePicts/sample12.xml");
			FileWriter filewriter = new FileWriter(file);
			filewriter.write(NodeXmlString);
			filewriter.close();
		} catch (IOException e) {
		}

		//ファイルから読みだしついでに書き換え
		//ファイルの中から４~10行目までを消す
		String htmlXmlDash = new String();
		FileReader fr = null;
		BufferedReader br = null;
		int i = 0;
		try{
		    fr = new FileReader(new File("CreatePicts/sample12.xml"));
		    br = new BufferedReader(fr);
	    	
		    String line;
	        while ((line = br.readLine()) != null) {
	            if(((i>=4) && (i<=9))){
	            	
	            	i++;
	            	continue;
	            }
	            System.out.println(line);
            	htmlXmlDash += line+"\n";
	            i++;
	        }
	    	
	    } catch (FileNotFoundException e) {
	       // e.printStackTrace();
	    } catch (IOException e) {
	        //e.printStackTrace();
	    } finally {
	        try {
	            br.close();
	            fr.close();
	        } catch (IOException e) {
	           // e.printStackTrace();
	        }
	    }
		
		
		
		mxGraph ggg = new mxGraph();
		
		try{
			Document doc2 = mxXmlUtils.parseXml(htmlXmlDash);
			mxCodec codec2 = new mxCodec(doc2);
			codec2.decode(doc2.getDocumentElement(), MyGraphicEditor.getGraphModel());
		} catch (Exception ex){
			ex.printStackTrace();
		}
		ggg.setModel(MyGraphicEditor.getGraphModel());
		
		
		//PNGイメージに出力
		BufferedImage image = mxCellRenderer.createBufferedImage(ggg, null, 1, Color.WHITE, true, null);
		try {
			boolean result = ImageIO.write(image, "png", new File("CreatePicts/target.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		
	
	
}
