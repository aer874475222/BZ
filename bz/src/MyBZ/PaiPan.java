package MyBZ;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class PaiPan {

	private int DAYUN_COUNT = 8;// ��Ҫ�Ĵ��˵���Ŀ
	// ==========��֧����===================
	final String[] Gan = new String[] { "��", "��", "��", "��", "��", "��", "��", "��", "��", "��" };
	final String[] Zhi = new String[] { "��", "��", "��", "î", "��", "��", "��", "δ", "��", "��", "��", "��" };
	final String[] NaYin = new String[] { "���н�", "���н�", "¯�л�", "¯�л�", "����ľ", "����ľ", "·����", "·����", "�����", "�����", "ɽͷ��",
			"ɽͷ��", "����ˮ", "����ˮ", "��ͷ��", "��ͷ��", "������", "������", "����ľ", "����ľ", "Ȫ��ˮ", "Ȫ��ˮ", "������", "������", "������", "������",
			"�ɰ�ľ", "�ɰ�ľ", "����ˮ", "����ˮ", "ɳ�н�", "ɳ�н�", "ɽ�»�", "ɽ�»�", "ƽ��ľ", "ƽ��ľ", "������", "������", "�𲭽�", "�𲭽�", "��ƻ�",
			"��ƻ�", "���ˮ", "���ˮ", "������", "������", "���˽�", "���˽�", "ɣ��ľ", "ɣ��ľ", "��Ϫˮ", "��Ϫˮ", "ɳ����", "ɳ����", "���ϻ�", "���ϻ�",
			"ʯ��ľ", "ʯ��ľ", "��ˮ", "��ˮ" };

	// �����֧��ƫ��ֵ
	private int yearCyl, dayCyl, hourCyl;
	private String monthGanZhi = null;
	// ����ʱ���ʽ����ʽ
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH");
	static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * ���캯��
	 * 
	 * @param cal
	 */
	public PaiPan(Calendar cal) {

		Date baseDate = null;
		try {
			baseDate = sdf1.parse("1900-1-31");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		yearCyl = getCheckedYear(cal) - 1864;

		// ����µĸ�֧
		monthGanZhi = getMonthGanZhiString(cal, getCheckedYear(cal));
		// ����պ�ʱ��ƫ��ֵ
		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
		dayCyl = offset + 40;
		hourCyl = (int) ((cal.getTime().getTime() - baseDate.getTime() + 3300000L) / 7200000L);

	}

	/**
	 * �������
	 * 
	 * @return
	 */
	public String getSiZhuString() {// ��������
		return cyclicalm(yearCyl) + monthGanZhi + cyclicalm(dayCyl) + cyclicalm(hourCyl);
	}

	/**
	 * ���� ���յ�offset ���ظ�֧, 0=����
	 * 
	 * @param num
	 * @return
	 */
	final private String cyclicalm(int num) {
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	/**
	 * �����ո�֧�����ؿ���
	 * 
	 * @param rigan
	 * @return
	 */
	public String getKWString(String rigan) {
		int gan = 0, zhi = 0, kong = 0;
		gan = getGanPosition(String.valueOf(rigan.charAt(0)));
		zhi = getZhiPosition(String.valueOf(rigan.charAt(1)));

		kong = (10 - gan - (12 - zhi) + 12) % 12;
		String kw = Zhi[kong] + Zhi[kong + 1];
		return kw;
	}

	/**
	 * �����Ա����ɣ������Ŵ��˵ķ���
	 * 
	 * @param gender
	 * @param yeargan
	 * @return
	 */
	public int getPaiDaYunDir(int gender, String yeargan) {

		int dir = 1;
		int yg = getGanPosition(yeargan);
		if ((yg % 2 == 0 && gender == 1) || (yg % 2 == 1 && gender == 0)) {// ����/Ů��
			dir = 1;
		} else if ((yg % 2 == 1 && gender == 1) || (yg % 2 == 0 && gender == 0)) {// ����/Ů��
			dir = -1;
		}
		return dir;
	}

	/**
	 * �Ŵ��˷���
	 * 
	 * @param gender
	 * @param yearganzhi
	 * @param monthganzhi
	 * @return
	 */
	public String[] getDaYunString(int gender, String yearganzhi, String monthganzhi) {

		int dir = 1, monthgan = 0, i, monthzhi = 0;
		String[] daYun = new String[DAYUN_COUNT];

		dir = getPaiDaYunDir(gender, String.valueOf(yearganzhi.charAt(0)));

		monthgan = getGanPosition(String.valueOf(monthganzhi.charAt(0)));
		monthzhi = getZhiPosition(String.valueOf(monthganzhi.charAt(1)));
		// Log.e("tag", "transfer-->"+monthzhi);
		// Log.e("tag", "transfer-->"+monthganzhi.charAt(1));

		if (dir == 1) {
			for (i = 0; i < DAYUN_COUNT; i++) {
				daYun[i] = Gan[(++monthgan) % 10].toString() + (Zhi[(++monthzhi) % 12].toString());
			}
		} else {
			for (i = 0; i < DAYUN_COUNT; i++) {
				daYun[i] = Gan[((--monthgan) + 10) % 10].toString() + (Zhi[((--monthzhi) + 12) % 12].toString());
				// Log.e("tag", "dayundizhi-->"+(Zhi[((monthzhi) + 12) %
				// 12].toString()));
			}
		}

		return daYun;
	}

	/**
	 * ��������ĵ�֧����Ӧ��λ��
	 * 
	 * @param gan
	 * @return
	 */
	public int getGanPosition(String gan) {// ����������������Ӧ��λ��

		int ganBack = 0, i = 0;
		for (i = 0; i < 10; i++) {
			if (Gan[i].toString().equals(gan)) {
				ganBack = i;
				break;
			} else {
				continue;
			}
		}
		return ganBack;
	}

	/**
	 * ��������ĵ�֧����Ӧ��λ��
	 * 
	 * @param zhi
	 * @return
	 */
	public int getZhiPosition(String zhi) {

		int zhiBack = 0, i = 0;
		for (i = 0; i < 12; i++) {
			if (Zhi[i].toString().equals(zhi)) {
				zhiBack = i;
				break;
			} else {
				continue;
			}
		}
		return zhiBack;
	}

	/**
	 * �������ڣ������¸�֧
	 * 
	 * @param cal
	 * @param year
	 * @return
	 */
	public String getMonthGanZhiString(Calendar cal, int year) {

		String monthGan = "������ɼ�";
		String monBeginGan = String.valueOf(monthGan.charAt((year - 1864) % 5));

		int gan = getGanPosition(monBeginGan);
		int zhi = getZhiPosition("��");

		String[] monthDate = getWholeYearJieQis(year);

		// ��������������µĸ�֧�ַ�����monthGanZhis
		String[] monthGanZhis = new String[12];
		for (int i = 0; i < 12; i++) {
			monthGanZhis[i] = Gan[(gan++) % 10] + Zhi[(zhi++) % 12];
		}

		return monthGanZhis[getPostionOfTheYear(cal, monthDate)];

	}

	/**
	 * ����У�Ժ����ݣ����ص�������н��������ڣ�����~�󺮣�
	 * 
	 * @param year
	 * @return
	 */
	public String[] getWholeYearJieQis(int year) {

		SolarTerm solarTerm = new SolarTerm();
		String[] nowYear = solarTerm.getLunarJieQisDateOfTheYear(year);
		String[] nextYear = solarTerm.getLunarJieQisDateOfTheYear(year + 1);

		String[] monthDate = new String[12];
		for (int i = 2; i < 12; i++) {
			monthDate[i - 2] = nowYear[i];
		}
		monthDate[10] = nextYear[0];
		monthDate[11] = nextYear[1];

		return monthDate;
	}

	/**
	 * ������������ʱ��
	 * 
	 * @param year
	 * @return
	 */
	public Calendar getLiChunCalendar(int year) {
		SolarTerm stLiChun = new SolarTerm();
		Calendar calLiChun = Calendar.getInstance();
		try {
			calLiChun.setTime(sdf3.parse(stLiChun.getLiChunString(year)));// �ҳ���������ʱ��

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calLiChun;
	}

	/**
	 * �����������ں͵�����������飬�������ڽ�����λ��
	 * 
	 * @param calInput
	 * @param jqStrings
	 * @return
	 */
	public int getPostionOfTheYear(Calendar calInput, String[] jqStrings) {

		int outPut = 0, i = 0;
		String timeString = null;
		Calendar calTime = Calendar.getInstance();// һ��һ��ȡ�����������е����ڽ��бȽ�
		long inputTime = calInput.getTimeInMillis();
		long jqTime = 0;// һ��һ��ȡ�����������е����ڱ�Ϊ���������ݽ��бȽ�

		for (i = 0; i < jqStrings.length; i++) {

			timeString = jqStrings[i];
			try {
				calTime.setTime(sdf4.parse(timeString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			jqTime = calTime.getTimeInMillis();

			if (jqTime < inputTime) {
				continue;
			} else {
				break;
			}
		}
		outPut = i - 1;

		return outPut;

	}

	/**
	 * �������ڣ�����У��������
	 * 
	 * @param cal
	 * @return
	 */
	final public int getCheckedYear(Calendar cal) {

		Calendar calLiChun = getLiChunCalendar(cal.get(Calendar.YEAR));
		return (cal.getTimeInMillis() > calLiChun.getTimeInMillis()) ? calLiChun.get(Calendar.YEAR)
				: (calLiChun.get(Calendar.YEAR) - 1);
	}

	/**
	 * �������ڷ�����Ӧ������
	 * 
	 * @param cal
	 * @return
	 */
	final public String getNaYinString(Calendar cal) {

		Calendar calLiChun = getLiChunCalendar(cal.get(Calendar.YEAR));
		int year = (cal.getTimeInMillis() > calLiChun.getTimeInMillis()) ? calLiChun.get(Calendar.YEAR)
				: (calLiChun.get(Calendar.YEAR) - 1);

		return NaYin[(year - 1864) % 60];
	}

	// //=============================TestArea=========================================//
	// public static void main(String[] arg) {
	//
	// String birthday = 2014 + "-" + 12 + "-" + 12 + " " + 12;
	// dataCalc.SolarTerm stLiChun = new SolarTerm();
	//
	// Calendar calInput = Calendar.getInstance();
	// try {
	// calInput.setTime(sdf2.parse(birthday));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// PaiPan pp=new PaiPan(calInput);
	//
	// SolarTerm solarTerm=new SolarTerm();
	// String [] test=solarTerm.getLunarMonthsDateOfTheYear(2014);
	// for(int j=0;j<test.length;j++){
	// System.out.println("Here:"+test[j]);
	// }
	// }

}
