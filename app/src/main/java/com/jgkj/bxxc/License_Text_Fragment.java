package com.jgkj.bxxc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class License_Text_Fragment extends Fragment implements View.OnClickListener{
	private View view;
	private Button orderTest,error_Sub,randomTest,examTest;
	private int index;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.subject1, container, false);
		init();
		return view;
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		//顺序练题
		orderTest = (Button) view.findViewById(R.id.orderTest);
		orderTest.setOnClickListener(this);
		//错题
		error_Sub = (Button) view.findViewById(R.id.error_Sub);
		error_Sub.setOnClickListener(this);
		//随机练题
		randomTest = (Button) view.findViewById(R.id.randomTest);
		randomTest.setOnClickListener(this);
		//模拟考试
		examTest = (Button) view.findViewById(R.id.examTest);
		examTest.setOnClickListener(this);
		Bundle bundle = getArguments();

		index = bundle.getInt("index");
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()){
			case R.id.orderTest:
				if(index==1){
					intent.setClass(getActivity(),SubTestActivity.class);
				}else if(index==4){
					intent.setClass(getActivity(),SubFourTestActivity.class);
				}
				break;
			case R.id.error_Sub://SubfourErrorTestActivity
				if(index==1){
					intent.setClass(getActivity(),SubErrorTestActivity.class);
				}else if(index==4){
					intent.setClass(getActivity(),SubfourErrorTestActivity.class);
				}
				break;
			case R.id.randomTest:
				if(index==1) {
					intent.setClass(getActivity(), SubRandTestActivity.class);
				}else if(index==4){
					intent.setClass(getActivity(),SubFourRandTestActivity.class);
				}
				break;
			case R.id.examTest:
				if(index==1) {
					intent.setClass(getActivity(),SubExamTestActivity.class);
				}else if(index==4){
					intent.setClass(getActivity(),SubFourExamTestActivity.class);
				}
				break;
		}
		startActivity(intent);
	}
}
