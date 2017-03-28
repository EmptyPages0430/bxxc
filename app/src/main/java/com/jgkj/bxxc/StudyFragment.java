package com.jgkj.bxxc;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.jgkj.bxxc.adapter.SimpleFragmentPagerAdapter;

/**
 * 我的资料Fragment
 * 学习中心的页面
 */
public class StudyFragment extends Fragment {
    private View view;
    private SimpleFragmentPagerAdapter pagerAdapter;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    /**
     * 注意：在fragment中嵌套fragment，当外部fragment切换时，内部fragment会出现空白情况
     * 这是因为外部fragment切换后，内部fragment会走destroy生命周期，当外部fragment切换回来后自然
     * 内部fragment就不存在了，因此我们可以利用tag值保存当前fragment的视图view，当内部fragment已
     * 被创建过了后保存tag，再次切换回来后调用tag值
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(container.getTag()==null){
            view = inflater.inflate(R.layout.study, container, false);
            init();
            container.setTag(view);
        }else{
            view = (View) container.getTag();
        }

        return view;
    }

    //控件实例化及监听
    private void init() {
        pagerAdapter = new SimpleFragmentPagerAdapter(getFragmentManager(),getActivity());
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        //设置可以滑动
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

}
