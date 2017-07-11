package com.example.rzhu.counter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create this custom view so that it will be easier to add animation later
 * Created by rzhu on 5/31/2017.
 */

public class CounterNumberView extends FrameLayout
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
	private int mHeight;
	private int mWidth;
	private Handler mHandler;


	public CounterNumberView(@NonNull Context context, @Nullable AttributeSet attrs)
	{
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.counter_number_view, this, true);

		mAnimateOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.out_animation);
		mAnimateIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.in_animation);
		mHandler = new Handler();
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		ButterKnife.bind(this);

		int distance = 8000;
		float scale = getResources().getDisplayMetrics().density * distance;
		mOldNumber.setCameraDistance(scale);
		mNewNumber.setCameraDistance(scale);

	}

	/**
	 * @param newNumber has to be [0,9] inclusive
	 */
	public void setNumber(int newNumber)
	{
		if (mNewNumber.getText().toString().isEmpty())
		{
			mNewNumber.setText(String.valueOf(newNumber));
			return;
		}

		final int oldNumber = Integer.valueOf(mNewNumber.getText().toString());
		if (oldNumber != newNumber)
		{
			mNumberBehind.setText(String.valueOf(newNumber));
			mOldNumber.setText(String.valueOf(oldNumber));
			mNewNumber.setText(String.valueOf(newNumber));

			if (mWidth == 0 || mHeight == 0)
			{
				mHeight = mOldNumber.getMeasuredHeight();
				mWidth = mOldNumber.getMeasuredWidth();
			}

			//take snapshot of upper half of new number
			Bitmap upperHalfNewNumber = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
			Canvas upperCanvas = new Canvas(upperHalfNewNumber);
			upperCanvas.clipRect(0, 0, mWidth, mHeight / 2);
			mNewNumber.draw(upperCanvas);

			//take snapshot of lower half of old number
			Bitmap lowerHalfOldNumber = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
			Canvas lowerCanvas = new Canvas(lowerHalfOldNumber);
			lowerCanvas.clipRect(0, mHeight / 2, mWidth, mHeight);
			mOldNumber.draw(lowerCanvas);

			mUpperCover.setImageBitmap(upperHalfNewNumber);
			mLowerCover.setImageBitmap(lowerHalfOldNumber);

			mUpperCover.setVisibility(GONE);
			mLowerCover.setVisibility(VISIBLE);

			mHandler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					mNumberBehind.setText(String.valueOf(oldNumber));
					mUpperCover.setVisibility(VISIBLE);
					mLowerCover.setVisibility(GONE);
				}
			}, getResources().getInteger(R.integer.counter_animation_half_length));


			mAnimateOut.setTarget(mOldNumber);
			mAnimateIn.setTarget(mNewNumber);
			mAnimateOut.start();
			mAnimateIn.start();
		}
	}
}
