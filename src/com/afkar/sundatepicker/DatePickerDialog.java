package com.afkar.sundatepicker;

import java.util.Calendar;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afkar.sundatepicker.month.MonthMainFragement;
import com.afkar.sundatepicker.tool.Date;
import com.afkar.sundatepicker.tool.JDF;
import com.afkar.sundatepicker.tool.Util;
import com.afkar.sundatepicker.year.YearMainFragement;

/*
 * Created by Alireza Afkar - 24/10/14
 */

public class DatePickerDialog extends DialogFragment implements OnClickListener {

	private static TextView dayTV;
	private static TextView monthTV;
	private static TextView yearTV;
	private static TextView dayNameTV;
	LinearLayout dayMonth;
	TextView doneTV;

	private static Typeface mTypeFace = null;

	FrameLayout frameLayout;
	FragmentManager fragmentManager;

	private static int mBlue = 0;
	private static int mGry = 0;
	static boolean mDarkTheme;

	static int minYear;
	static int maxYear;

	private static GradientDrawable circle;
	private static Boolean mVibrate;

	private static OnDateSetListener mCallBack;
	static int id;

	public DatePickerDialog() {

	}

	public static DatePickerDialog newInstance(
			OnDateSetListener onDateSetListener, int requestID,
			boolean darkTheme) {

		JDF jdf = new JDF();
		return newInstance(onDateSetListener, requestID, jdf.getIranianYear(),
				jdf.getIranianMonth(), jdf.getIranianDay(), darkTheme);
	}

	public static DatePickerDialog newInstance(
			OnDateSetListener onDateSetListener, boolean darkTheme) {
		return newInstance(onDateSetListener, 0, darkTheme);
	}

	public static DatePickerDialog newInstance(
			OnDateSetListener onDateSetListener, int requestID, int year,
			int month, int day, boolean darkTheme) {

		DatePickerDialog datePickerDialog = new DatePickerDialog();

		mDarkTheme = darkTheme;
		if (mDarkTheme)
			datePickerDialog.setStyle(
					android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
					android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth);
		else
			datePickerDialog
					.setStyle(
							android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
							android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);

		mCallBack = onDateSetListener;
		Date.setDate(year, month, day, false);
		minYear = new JDF().getIranianYear();
		maxYear = minYear + 2;
		mVibrate = true;
		id = requestID;
		mBlue = 0;
		mGry = 0;
		mTypeFace = null;

		return datePickerDialog;
	}

	@Override
	public void onStart() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int screenWidth = (int) (metrics.widthPixels * 0.75);
		int screenHeight = (int) (metrics.heightPixels * 0.90);
		getDialog().getWindow().setLayout(screenWidth, screenHeight);
		setRetainInstance(true);
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater layoutInflater,
			ViewGroup container, Bundle savedInstanceState) {

		final View view = layoutInflater.inflate(R.layout.main_layout, null);

		if (mBlue == 0)
			mBlue = getResources().getColor(R.color.blue);

		if (mGry == 0)
			mGry = getResources().getColor(R.color.gray);

		circle = new GradientDrawable();
		circle.setCornerRadius(getResources().getDimension(
				R.dimen.circle_radius));
		circle.setColor(mBlue);
		circle.setAlpha(80);

		frameLayout = (FrameLayout) view.findViewById(R.id.frame_container);
		fragmentManager = getChildFragmentManager();

		dayTV = (TextView) view.findViewById(R.id.day);
		monthTV = (TextView) view.findViewById(R.id.month);
		yearTV = (TextView) view.findViewById(R.id.year);
		dayNameTV = (TextView) view.findViewById(R.id.dayName);
		doneTV = (TextView) view.findViewById(R.id.done);
		dayMonth = (LinearLayout) view.findViewById(R.id.dayMonthBack);

		if (mTypeFace != null) {
			dayTV.setTypeface(mTypeFace);
			monthTV.setTypeface(mTypeFace);
			yearTV.setTypeface(mTypeFace);
			dayNameTV.setTypeface(mTypeFace);
			doneTV.setTypeface(mTypeFace);
		}

		if (mDarkTheme) {
			dayNameTV.setTextColor(getResources().getColor(R.color.black));
			dayNameTV.setBackgroundColor(getResources().getColor(
					R.color.transparent_white));
		} else {
			dayNameTV.setTextColor(getResources().getColor(R.color.white));
			dayNameTV.setBackgroundColor(getResources().getColor(R.color.gray));
		}

		dayMonth.setOnClickListener(this);
		yearTV.setOnClickListener(this);
		doneTV.setOnClickListener(this);

		updateDisplay(Date.getYear(), Date.getMonth(), Date.getDay());

		((LinearLayout) view.findViewById(R.id.dayMonthBack)).performClick();

		return view;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.year) {
			// Util.clickAnimation(getActivity(), v, false);
			yearTV.setTextColor(mBlue);
			dayTV.setTextColor(mGry);
			monthTV.setTextColor(mGry);
			switchFragment(new YearMainFragement(minYear, maxYear));
		} else if (v.getId() == R.id.dayMonthBack) {
			// Util.clickAnimation(getActivity(), v, false);
			yearTV.setTextColor(mGry);
			dayTV.setTextColor(mBlue);
			monthTV.setTextColor(mBlue);
			switchFragment(new MonthMainFragement());
		} else if (v.getId() == R.id.done) {
			if (mCallBack != null) {
				Calendar calendar = Calendar.getInstance();
				JDF j = new JDF();
				j.setIranianDate(Date.getYear(), Date.getMonth(), Date.getDay());
				calendar.set(j.getGregorianYear(), j.getGregorianMonth(),
						j.getGregorianDay());
				mCallBack.onDateSet(id, calendar, Date.getYear(),
						Date.getMonth(), Date.getDay());
				Util.tryVibrate(getActivity());
			}
			dismiss();
		}
	}

	void switchFragment(final Fragment fragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		transaction.replace(R.id.frame_container, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/*
	 * Setters
	 */
	public void setMainColor(int color) {
		mBlue = color;
	}

	public void setSecondColor(int color) {
		mGry = color;
	}

	public void setYearRange(int _minYear, int _maxYear) {
		minYear = _minYear;
		maxYear = _maxYear;
	}

	public void setInitialDate(int year, int month, int day) {
		Date.setDate(year, month, day, false);
	}

	public void setVibrate(boolean vibrate) {
		mVibrate = vibrate;
	}

	public void setTypeFace(Typeface typeface) {
		mTypeFace = typeface;
	}

	public void setRequestID(int requestID) {
		id = requestID;
	}

	/*
	 * End of Setters
	 */

	/*
	 * Getters
	 */
	public static GradientDrawable getCircle() {
		return circle;
	}

	public static int getBlueColor() {
		return mBlue;
	}

	public static int getGrayColor() {
		return mGry;
	}

	public static boolean checkVibrate() {
		return mVibrate;
	}

	public static Typeface getTypeFace() {
		return mTypeFace;
	}

	public static int getRequestID() {
		return id;
	}

	/*
	 * End of Getters
	 */

	public static void updateDisplay(int year, int month, int day) {
		try {
			DatePickerDialog.dayTV.setText("" + day);
			DatePickerDialog.monthTV.setText(JDF.monthNames[month - 1]);
			DatePickerDialog.yearTV.setText("" + year);
			DatePickerDialog.dayNameTV.setText(new JDF().getIranianDayName(
					year, month, day));
		} catch (Exception e) {
		}
	}

	public static abstract interface OnDateSetListener {
		public abstract void onDateSet(int id, Calendar calendar, int year,
				int month, int day);
	}

}
