package com.xsbz;

import android.os.Bundle;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.xsbz.IndexActivity;
import com.xsbz.PaiPanActivity;

import MyBZ.ActivitiesCollector;
import MyBZ.Libs;

import com.xsbz.R;
import com.xsbz.views.ArcMenu;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class IndexActivity extends Activity {

	private int gender = 1, isSolar = 1;// �Ա�Ĭ��Ϊ�У�����Ĭ��Ϊ����
	private static int BASEYEAR = 1900;
	private static Boolean isExit = false;

	private Button confirm;
	private EditText etName;
	private RadioButton rbGender, rbSolar;
	private Spinner YearSpinner, MonthSpinner, DaySpinner, HourSpinner, MinuteSpinner;

	private ArcMenu mArcMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.index);
		ActivitiesCollector.addActivity(this);

		mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
		etName = (EditText) this.findViewById(R.id.etName);
		rbGender = (RadioButton) this.findViewById(R.id.rbMen);
		rbSolar = (RadioButton) this.findViewById(R.id.rbSolar);
		confirm = (Button) this.findViewById(R.id.bConfirm);
		YearSpinner = (Spinner) this.findViewById(R.id.sYear);
		HourSpinner = (Spinner) this.findViewById(R.id.sHour);
		MinuteSpinner = (Spinner) this.findViewById(R.id.sMinute);
		// ����ĸ�����������
		LoadYearAdapter();
		LoadMonthAdapter();
		LoadDayAdapter();
		LoadHourAdapter(HourSpinner);
		LoadMinuteAdapter();
		// ͨ���������ı�RadioButton����ѡֵ
		rbGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				gender = (rbGender.isChecked()) ? 1 : 0;
			}
		});
		rbSolar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				isSolar = (rbSolar.isChecked()) ? 1 : 0;
				// ֻҪ�ı䡰������������ѡ���ֵ�������¼����º��յ�����
				LoadMonthAdapter();
				LoadDayAdapter();
			}
		});

		// �����Spinner��Ҫ�������ݵı仯
		YearSpinner.setOnItemSelectedListener(new yearOnItemSelectedListener());
		MonthSpinner.setOnItemSelectedListener(new monthOnItemSelectedListener());

		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// ��ȡ����ֵ
				String name = etName.getText().toString();
				String selectYear = YearSpinner.getSelectedItem().toString();
				String selectMonth = MonthSpinner.getSelectedItem().toString();
				String selectDay = DaySpinner.getSelectedItem().toString();
				String selectHour = HourSpinner.getSelectedItem().toString();
				String selectMinute = MinuteSpinner.getSelectedItem().toString();

				Calendar calInput = null;
				if (isSolar == 1) {
					calInput = Libs.getInputCalendar(selectYear, selectMonth, selectDay, selectHour, selectMinute);
				} else {
					calInput = Libs.getInputSolarCalendar(selectYear, selectMonth, selectDay, selectHour, selectMinute);
				}

				String date = calInput.get(Calendar.YEAR) + "-" + (calInput.get(Calendar.MONTH) + 1) + "-"
						+ calInput.get(Calendar.DATE) + " " + calInput.get(Calendar.HOUR_OF_DAY) + ":"
						+ calInput.get(Calendar.MINUTE) + ":00";
				Intent intent = new Intent(IndexActivity.this, PaiPanActivity.class);
				name = etName.getText().toString().length() == 0 ? "����" : etName.getText().toString();
				intent.putExtra("name", name);
				intent.putExtra("gender", gender);
				intent.putExtra("date", date);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mArcMenu.isOpen())
			mArcMenu.toggleMenu(200);
		return super.onTouchEvent(event);
	}

	private void LoadMinuteAdapter() {
		// ��ȡ��ǰʱ��ƫ��ֵ
		Calendar indexCalendar = Calendar.getInstance();
		int indexMinute = indexCalendar.get(Calendar.MINUTE);

		List<String> listMinute = Libs.getMinuteAdapterData();
		ArrayAdapter<String> adapterMinute = new ArrayAdapter<String>(IndexActivity.this,
				android.R.layout.simple_spinner_dropdown_item, listMinute);
		MinuteSpinner.setAdapter(adapterMinute);
		MinuteSpinner.setSelection(indexMinute);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitByTwoClick();
		}
		return false;
	}

	private void exitByTwoClick() {

		Timer timer = null;
		if (!isExit) {
			isExit = true;
			Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			finish();
			System.exit(0);
		}

	}

	/**
	 * @author Alex ѡ����ļ�����
	 */
	private class yearOnItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {

			LoadMonthAdapter();
			LoadDayAdapter();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	/**
	 * @author Alex ѡ���µļ����¼�
	 */
	private class monthOnItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {

			LoadDayAdapter();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	/**
	 * �������������
	 */
	public void LoadYearAdapter() {

		// ��ȡ��ǰʱ��ƫ��ֵ
		Calendar indexCalendar = Calendar.getInstance();
		int indexYear = indexCalendar.get(Calendar.YEAR);

		YearSpinner = (Spinner) this.findViewById(R.id.sYear);
		List<String> listYear = Libs.getYearAdapterData();
		ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(IndexActivity.this,
				android.R.layout.simple_spinner_dropdown_item, listYear);
		YearSpinner.setAdapter(adapterYear);
		YearSpinner.setSelection(indexYear - BASEYEAR);
	}

	/**
	 * �����µ�������
	 */
	public void LoadMonthAdapter() {
		// ��ȡ��ǰʱ��ƫ��ֵ
		Calendar indexCalendar = Calendar.getInstance();

		int indexMonth = indexCalendar.get(Calendar.MONTH);

		YearSpinner = (Spinner) this.findViewById(R.id.sYear);
		MonthSpinner = (Spinner) this.findViewById(R.id.sMonth);
		rbSolar = (RadioButton) this.findViewById(R.id.rbSolar);

		boolean isSolarNow = (rbSolar.isChecked()) ? true : false;

		List<String> listMonth = Libs.getMonthAdapterData(isSolarNow, YearSpinner.getSelectedItemPosition() + BASEYEAR);
		ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(IndexActivity.this,
				android.R.layout.simple_spinner_dropdown_item, listMonth);
		MonthSpinner.setAdapter(adapterMonth);
		MonthSpinner.setSelection(indexMonth);
	}

	/**
	 * �������������
	 */
	public void LoadDayAdapter() {
		// ��ȡ��ǰʱ��ƫ��ֵ
		Calendar indexCalendar = Calendar.getInstance();

		int indexDay = indexCalendar.get(Calendar.DATE);

		YearSpinner = (Spinner) this.findViewById(R.id.sYear);
		MonthSpinner = (Spinner) this.findViewById(R.id.sMonth);
		DaySpinner = (Spinner) this.findViewById(R.id.sDay);
		rbSolar = (RadioButton) this.findViewById(R.id.rbSolar);

		boolean isSolarNow = (rbSolar.isChecked()) ? true : false;

		List<String> listDay = Libs.getDayAdapterData(isSolarNow, YearSpinner.getSelectedItemPosition() + BASEYEAR,
				MonthSpinner.getSelectedItemPosition() + 1);
		ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(IndexActivity.this,
				android.R.layout.simple_spinner_dropdown_item, listDay);
		DaySpinner.setAdapter(adapterDay);
		DaySpinner.setSelection(indexDay - 1);

	}

	/**
	 * ����ʱ��������
	 */
	public void LoadHourAdapter(Spinner HourSpinner) {
		// ��ȡ��ǰʱ��ƫ��ֵ
		Calendar indexCalendar = Calendar.getInstance();
		int indexHour = indexCalendar.get(Calendar.HOUR_OF_DAY);

		List<String> listHour = Libs.getHourAdapterData();
		ArrayAdapter<String> adapterHour = new ArrayAdapter<String>(IndexActivity.this,
				android.R.layout.simple_spinner_dropdown_item, listHour);
		HourSpinner.setAdapter(adapterHour);
		HourSpinner.setSelection(indexHour);
	}

}
