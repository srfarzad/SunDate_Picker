package com.afkar.sundatepicker.tool;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.afkar.sundatepicker.DatePickerDialog;
import com.afkar.sundatepicker.R;

/*
 * Created by Alireza Afkar - 24/10/14
 */

public class Util {

	private static Vibrator vibrator = null;
	private static Animation click_anim = null;

	public static void clickAnimation(final Context context, final View v,
			boolean vibrate) {

		if (click_anim == null) {
			click_anim = AnimationUtils.loadAnimation(context,
					R.anim.click_animation);
		}

		v.startAnimation(click_anim);

		if (vibrate)
			tryVibrate(context);

	}

	public static void tryVibrate(Context context) {
		if (vibrator == null) {
			vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
		}

		if (DatePickerDialog.checkVibrate() && vibrator != null)
			vibrator.vibrate(20);
	}
}
