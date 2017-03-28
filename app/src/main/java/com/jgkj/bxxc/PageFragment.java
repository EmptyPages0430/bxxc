package com.jgkj.bxxc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import static android.R.attr.fragment;


/**
 * Created by fangzhou on 2017/1/4.
 *
 * 展示所带所有学员状态
 */

public class PageFragment extends Fragment  {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    private View view;
    private boolean isCreate;
    private int mCurrentView;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("ARG_PAGE", page);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取page页数
        mPage = getArguments().getInt(ARG_PAGE);
        switch (mPage) {
            case 0:
                mCurrentView = R.layout.sub1;
                break;
            case 1:
                mCurrentView = R.layout.sub2;
                break;
            case 2:
                mCurrentView = R.layout.sub3;
                break;
            case 3:
                mCurrentView = R.layout.sub4;
                break;
        }
    }

    /**
     * 初始化布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(!isCreate){
            view = inflater.inflate(mCurrentView, container, false);
            container.setTag(view);
        }
        isCreate = true;
        return view;
    }
}
