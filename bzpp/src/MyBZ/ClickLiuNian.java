package MyBZ;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ClickLiuNian implements OnClickListener {

	private TextView tv;
	private int i;
	private ArrayList<TextView> liunian;
	private ArrayList<TextView> liuyue;
	private String[] liunianStrings;
	
	public ClickLiuNian(TextView tv,ArrayList<TextView> liunian,ArrayList<TextView> liuyue,String[] liunianStrings,int i) {
		// TODO Auto-generated constructor stub
		this.tv=tv;
		this.i=i;
		this.liunian=liunian;
		this.liuyue=liuyue;
		this.liunianStrings=liunianStrings;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// ��������ɫ��Ϊ��ɫ����������ɫ��Ϊ��ɫ
		Libs.removeColor(liunian);
		tv.setTextColor(0xFFFF0000);

		// ���ÿ��TextView
		Libs.setTextViewText(liuyue, Libs.getLiuYueStrings(String.valueOf(liunianStrings[i].charAt(0))));
	}
}
