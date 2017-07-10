package com.example.rzhu.counter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
	@BindView(R.id.counter_view)
	CounterView mCounterView;

	@BindView(R.id.tv)
	TextView tv;

	@OnClick(R.id.random_button)
	public void random()
	{
		int number = (int) (Math.random() * 1000000);
		tv.setText(String.valueOf(number));
		mCounterView.setCount(number);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}
}
