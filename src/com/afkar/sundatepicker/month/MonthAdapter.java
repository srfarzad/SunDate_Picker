package com.afkar.sundatepicker.month;

import java.text.ParseException;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.afkar.sundatepicker.DatePickerDialog;
import com.afkar.sundatepicker.R;
import com.afkar.sundatepicker.tool.Date;
import com.afkar.sundatepicker.tool.JDF;
import com.afkar.sundatepicker.tool.Util;

/*
 * Created by Alireza Afkar - 24/10/14
 */

public class MonthAdapter extends BaseAdapter {
	private Context context;
	int month;
	JDF today;
	int startDay;
	Typeface typeface;

	public MonthAdapter(Context context, int month) {
		this.context = context;
		this.month = month;
		today = new JDF();
		typeface = DatePickerDialog.getTypeFace();

		try {
			startDay = new JDF().getIranianDay(Date.getYear(), month + 1, 1);
		} catch (ParseException e) {
		}

	}

	@Override
	public int getCount() {
		if (month < 6)
			return 31 + 7 + startDay;
		else
			return 30 + 7 + startDay;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		position -= 7;

		if (v == null) {
			v = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.days_text_layout, null);
		}

		final TextView tv = (TextView) v.findViewById(R.id.day);

		if (typeface != null)
			tv.setTypeface(typeface);

		tv.setBackgroundColor(context.getResources().getColor(
				android.R.color.transparent));
		tv.setTextColor(DatePickerDialog.getGrayColor());

		if (position >= 0 && position - startDay >= 0) {
			position -= startDay;

			tv.setText(String.valueOf(position + 1));

			if (month + 1 == today.getIranianMonth()
					&& position + 1 == today.getIranianDay()
					&& Date.getYear() == today.getIranianYear()) {
				tv.setBackgroundColor(context.getResources().getColor(
						android.R.color.transparent));
				tv.setTextColor(DatePickerDialog.getBlueColor());
				Date.setTodayText(tv);
			}

			if (Date.getMonth() == month + 1 && Date.getDay() == position + 1) {
				Date.setDayText(tv);
				tv.setBackgroundDrawable(DatePickerDialog.getCircle());
				tv.setTextColor(DatePickerDialog.getGrayColor());
			}

			final int day = position + 1;
			tv.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View arg0) {

					if (Date.getDayText() != null) {
						Date.getDayText().setBackgroundColor(
								context.getResources().getColor(
										android.R.color.transparent));
						Date.getDayText().setTextColor(
								DatePickerDialog.getGrayColor());
					}

					if (Date.getTodayText() != null) {
						Date.getTodayText().setBackgroundColor(
								context.getResources().getColor(
										android.R.color.transparent));
						Date.getTodayText().setTextColor(
								DatePickerDialog.getBlueColor());
					}

					Date.setDay(day);
					Date.setMonth(month + 1);
					Date.setDayText(tv);
					Date.updateUI();

					tv.setBackgroundDrawable(DatePickerDialog.getCircle());
					tv.setTextColor(DatePickerDialog.getGrayColor());

					// Util.clickAnimation(context, tv, true);
					Util.tryVibrate(context);
				}
			});
		} else if (position < 0) {
			tv.setText(JDF.iranianDayNames[position + 7].substring(0, 1));
		}

		return v;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

}