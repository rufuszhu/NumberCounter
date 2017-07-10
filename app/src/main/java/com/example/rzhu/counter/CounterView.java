package com.example.rzhu.counter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import java.util.List;

/**
 * Created by rzhu on 5/31/2017.
 * A custom view that shows as current number of conversation in landing page
 */

public class CounterView extends LinearLayout
{
	private static final int DEFAULT_AND_MIN_COUNT = 174986;
	private static final int MAX_COUNT = 999999;
	@BindViews({R.id.first_digit, R.id.second_digit, R.id.third_digit, R.id.forth_digit, R.id.fifth_digit, R.id.sixth_digit})
	List<CounterNumberView> mDigitList;

	public CounterView(Context context, @Nullable AttributeSet attrs)
	{
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.counter_view, this, true);
		ButterKnife.bind(this);
		setGravity(Gravity.CENTER);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	/**
	 * @param number has to be between [DEFAULT_AND_MIN_COUNT,MAX_COUNT] inclusive, should never go above 6 digits
	 *               if server returns a value smaller than DEFAULT_AND_MIN_COUNT, just show DEFAULT_AND_MIN_COUNT
	 */
	public void setCount(int number)
	{
		//set default number for invalid input
		if (number < DEFAULT_AND_MIN_COUNT || number > MAX_COUNT)
		{
			setDefaultCount();
			return;
		}

		for (int i = 0; i < mDigitList.size(); i++)
		{
			int currentDigit = number % 10;
			CounterNumberView counterNumberView = mDigitList.get(i);
			counterNumberView.setNumber(currentDigit);
			number /= 10;
		}
	}

	public void setDefaultCount()
	{
		setCount(DEFAULT_AND_MIN_COUNT);
	}

}