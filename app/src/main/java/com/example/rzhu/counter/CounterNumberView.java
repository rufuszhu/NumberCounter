package com.example.rzhu.counter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create this custom view so that it will be easier to add animation later
 * Created by rzhu on 5/31/2017.
 */

public class CounterNumberView extends RelativeLayout
{
	@BindView(R.id.number_old)
	TextView mOldNumber;
	@BindView(R.id.number_new)
	TextView mNewNumber;
	@BindView(R.id.number_behind)
	TextView mNumberBehind;
	@BindView(R.id.upper_cover)
	ImageView mUpperCover;
	@BindView(R.id.lower_cover)
	ImageView mLowerCover;


	private AnimatorSet mAnimateOut;
	private AnimatorSet mAnimateIn;

	public CounterNumberView(@NonNull Context context, @Nullable AttributeSet attrs)
	{
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.counter_number_view, this, true);
		ButterKnife.bind(this);
		int distance = 1000;
		float scale = getResources().getDisplayMetrics().density * distance;
		mOldNumber.setCameraDistance(scale);
		mNewNumber.setCameraDistance(scale);
		mAnimateOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.out_animation);
		mAnimateIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.in_animation);
	}

	/**
	 * @param newNumber has to be [0,9] inclusive
	 */
	public void setNumber(int newNumber)
	{
		if (mNewNumber.getText().toString().isEmpty())
		{
			mNewNumber.setText(String.valueOf(newNumber));
		}

		int oldNumber = Integer.valueOf(mNewNumber.getText().toString());
		if (oldNumber != newNumber)
		{
			mOldNumber.setVisibility(VISIBLE);
			mNewNumber.setVisibility(VISIBLE);

			mOldNumber.setText(String.valueOf(oldNumber));
			int numberHeight = mOldNumber.getMeasuredHeight();
			int numberWidth = mOldNumber.getMeasuredWidth();

			//take snapshot of upper half of number view
			Bitmap upperHalfNewNumber = Bitmap.createBitmap(numberWidth, numberHeight/2, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(upperHalfNewNumber);
			mNewNumber.layout(0, 0, numberWidth, numberHeight / 2);
			mNewNumber.draw(c);

			Bitmap lowerHalfOldNumber = Bitmap.createBitmap(numberWidth, numberHeight/2, Bitmap.Config.ARGB_8888);
			Canvas lowerCanvas = new Canvas(lowerHalfOldNumber);
			mOldNumber.layout(0, numberHeight / 2, numberWidth, numberHeight);
			mOldNumber.draw(lowerCanvas);

			mUpperCover.setImageBitmap(upperHalfNewNumber);
			mLowerCover.setImageBitmap(lowerHalfOldNumber);


			mOldNumber.setVisibility(GONE);
			mNewNumber.setVisibility(GONE);

//			mAnimateOut.setTarget(mOldNumber);
//			mAnimateIn.setTarget(mNewNumber);
//			mAnimateOut.start();
//			mAnimateIn.start();
		}
	}
}
