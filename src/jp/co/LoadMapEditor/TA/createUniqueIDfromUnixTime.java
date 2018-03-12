//************ Unix時間から文字列型のユニークIDを生成するクラス ナノ秒まで計算しているので人間が使う限りで重複はないと言える ***************//

package jp.co.LoadMapEditor.TA;

import java.text.SimpleDateFormat;
import java.util.Date;

public class createUniqueIDfromUnixTime {
	
	public createUniqueIDfromUnixTime(){
	}
	
	public String getUniqueID(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMddHHmmss");
		String unixTime = dateFormat.format(date);
		
		long timeNow = System.nanoTime();
		String now = String.valueOf(timeNow);
		
		String uniqueID = unixTime+String.valueOf(now);
		
		return uniqueID;
	}

}
