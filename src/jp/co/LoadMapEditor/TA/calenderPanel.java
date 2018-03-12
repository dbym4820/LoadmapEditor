//****************　期限を選択させるためのカレンダークラス ***************//

package jp.co.LoadMapEditor.TA;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import airy.jpopupcalendar.CalendarSelectedEvent;
import airy.jpopupcalendar.CalendarSelectedListener;
import airy.jpopupcalendar.JPopupCalendar;
import airy.sample.DateTextField;
 
public class calenderPanel extends JFrame {
 
    
	private static final long serialVersionUID = 1L;

	//選択した日にちを取得し、文字列にする
	private static DateTextField field = null;
     
	//カレンダーを読み込む
    private JPopupCalendar calendar = null;
     
    //コンストラクタ
    public calenderPanel() {
    	
    	//デフォルトカレンダーのコンストラクタを起動
        super("JPopupCalendar");
        
        //カレンダーのサイズを指定
        this.setSize(new Dimension(400, 80));
        
        //カレンダーを閉じられるように設定
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //多分表示位置
        this.setLocationRelativeTo(null);
        
        //コンテナペインの取得
        Container c = this.getContentPane();
        
        //カレンダーのレイアウト設定
        c.setLayout(new BorderLayout());
        
        //コンテナにパネルに貼り付け
        c.add(getBasePanel(), BorderLayout.CENTER);
        
        
        //カレンダーインスタンス化
        calendar = new JPopupCalendar(this);
        //カレンダーリスナ        
        calendar.setCalendarSelectedListener(new CalendarSelectedListener() {
            @Override
            public void calendarSelected(CalendarSelectedEvent e) {
                field.setText(e.getCalendarDate());
            }
        });
        //不明
        this.setVisible(false);
        
        
    }
     
    //カレンダー表示用のパネル設定
    public JPanel getBasePanel() {
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
        panel.setLayout(layout);
        field = new DateTextField();
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                calendar.clearOptions();
                calendar.setTitle("デフォルトカレンダー");
                calendar.setDate(field.getStartDate());
                calendar.setInvoker(field);
                calendar.setPosition(JPopupCalendar.POSITION_RIGHT);
                calendar.showCalendar();
            }
        });
        
        panel.add(field);
        return panel;
    }
    
    //選択したカレンダーの日付を返すメソッド
    public  static String getCalenderConetnts(){
    	return field.getText();
    }
    
    public static void setCalenderContents(String date){
    	field.setText(date);
    }
}