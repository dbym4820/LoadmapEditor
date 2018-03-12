//******************** ユーザーの環境でのスクリーンサイズを計測するクラス ***********************//

package jp.co.LoadMapEditor.TA;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class DefaultScreenSize {

	    public static void main(String[] s){

	        @SuppressWarnings("unused")
			Dimension screenSize=new Dimension(800,600);// set default
	        try{

	            GraphicsEnvironment graphicsEnvironment =GraphicsEnvironment.getLocalGraphicsEnvironment();
	            GraphicsDevice graphicsDevice =graphicsEnvironment.getDefaultScreenDevice();
	            Rectangle rect=graphicsDevice.getDefaultConfiguration().getBounds();
	            screenSize=rect.getSize();
	        }
	        catch(Exception ex){
	            ex.printStackTrace();
	        }

	        
    }
}
