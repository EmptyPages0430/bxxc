package com.jgkj.bxxc;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jgkj.bxxc.adapter.MyCoachAdapter;
import com.jgkj.bxxc.bean.CoachDetailAction;
import com.jgkj.bxxc.bean.SchoolPlaceTotal;
import com.jgkj.bxxc.tools.RefreshLayout;
import com.jgkj.bxxc.tools.SelectPopupWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CoachFragment extends Fragment implements OnClickListener, AdapterView.OnItemClickListener
        , SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener {
    //教练排序展示
    private ListView listView;
    private MyCoachAdapter adapter;
    //上拉刷新
    private RefreshLayout swipeLayout;
    //读取本地信息
    private SharedPreferences sp1;

    //选择排序的条件等
    private View view;
    private Button sort_btn1, sort_btn2, sort_btn3, sort_btn4;
    private Dialog dialog;
    private int count = 0;
    private SelectPopupWindow mPopupWindowSub = null;
    private SelectPopupWindow mPopupWindowCampus = null;
    private SelectPopupWindow mPopupWindowSort = null;
    private SelectPopupWindow mPopupWindowClassType = null;
    private String tag;
    private String[] sub = {"科目二", "科目三"};
    private String[] sortStr = {"综合", "信用", "通过率", "好评率"};
    private String[] classType = {"贵宾班", "VIP班"};

    private String parentStr = null;
    private String childrenStr = null;
    private String class_type = "";
    private String sortString = "";
    private String class_class = "";
    private int schId = 0;

    private String[] city;
    private String[][] datialPlace;
    private SharedPreferences sp;
    private String string;

    //获取场地
    private String placePath = "http://www.baixinxueche.com/index.php/Home/Apiupdata/Apiarea";

    //新版本教练中心
    private String coachShowUrl = "http://www.baixinxueche.com/index.php/Home/Apitoken/Apiarea";
    //新版本排序
    private String sortPath = "http://www.baixinxueche.com/index.php/Home/Apitoken/chooseinfo";
    private int page = 1;
    private int searchPage = 1;
    private String token;
    private List<CoachDetailAction.Result> totalList;

    private List<CoachDetailAction.Result> coachList = new ArrayList<>();

    private SchoolPlaceTotal schoolPlaceTotal;
    private int sendTag = 0;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.coach, container, false);
        init();
        getPlace();
        getBundle();
        return view;
    }

    private void getBundle() {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            String string = bundle.getString("SEARCH");
            Gson gson = new Gson();
            CoachDetailAction coachDetailAction = gson.fromJson(string, CoachDetailAction.class);
            adapter = new MyCoachAdapter(getActivity(), coachDetailAction.getResult());
            listView.setAdapter(adapter);
        } else {
            check();
            sort(class_type, schId + "", sortString, page + "", class_class);
        }
    }

    /**
     * 获取地址信息
     */
    private void getPlace() {
        OkHttpUtils
                .get()
                .url(placePath)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(getActivity(), "网络状态不佳，请检查网络", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        listView.setTag(s);
                        if (listView.getTag() != null) {
                            addAdapter();
                        }
                    }
                });
    }

    /**
     * 解析服务器返回的json数据
     */
    private void addAdapter() {
        Gson gson = new Gson();
        String str = listView.getTag().toString();
        schoolPlaceTotal = gson.fromJson(str, SchoolPlaceTotal.class);
        setPup(schoolPlaceTotal.getResult());
    }

    /**
     * 设置pupweindow，下拉框值
     */
    private void setPup(List<SchoolPlaceTotal.Result> results) {
        city = new String[results.size()];
        datialPlace = new String[results.size()][];
        for (int i = 0; i < results.size(); i++) {
            city[i] = results.get(i).getSchool_aera();
            if (results.get(i) != null) {
                List<SchoolPlaceTotal.Result.Res> listSch = results.get(i).getResult();
                datialPlace[i] = new String[listSch.size()];
                for (int j = 0; j < listSch.size(); j++) {
                    datialPlace[i][j] = listSch.get(j).getSname();
                }
            }
        }
    }

    //条件查询
    private void sort(String class_type, String school, String sort, String page, String class_class) {
        OkHttpUtils
                .post()
                .url(sortPath)
                .addParams("school_id", school)
                .addParams("class_type", class_type)
                .addParams("sort", sort)
                .addParams("page", page)
                .addParams("class_class", class_class)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onResponse(String s, int i) {
                        listView.setTag(s);
                        if (listView.getTag() != null) {
                            setAdapter();
                        }
                    }
                });
    }

    /**
     * 设置adapter
     * 区分是刷新还是加载
     */
    private void setAdapter() {
        String swiTag="";
        String tag = "";
        try {
            swiTag = swipeLayout.getTag().toString();
            tag = listView.getTag().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        swipeLayout.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        CoachDetailAction coachDetailAction = gson.fromJson(tag, CoachDetailAction.class);
        switch (swiTag) {
            case "UNENABLE":
                coachList.clear();
                setMyAdapter();
                break;
            case "REFRESH":
                coachList.clear();
                setMyAdapter();
                break;
            case "ONLOAD":
                if (coachDetailAction.getCode() == 200) {
                    coachList.addAll(coachDetailAction.getResult());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), coachDetailAction.getReason(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 设置adapter
     */
    private void setMyAdapter() {
        String tag = listView.getTag().toString();
        Gson gson = new Gson();
        CoachDetailAction coachDetailAction = gson.fromJson(tag, CoachDetailAction.class);
        if (coachDetailAction.getCode() == 200) {
            coachList.addAll(coachDetailAction.getResult());
            adapter = new MyCoachAdapter(getActivity(), coachList);
            listView.setAdapter(adapter);
        } else {
            swipeLayout.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), coachDetailAction.getReason(), Toast.LENGTH_SHORT).show();
        }
    }

    //初始化控件
    private void init() {
        //排序按钮实例化并添加点击监听事件
        sort_btn1 = (Button) view.findViewById(R.id.coach_sort_btn1);
        sort_btn2 = (Button) view.findViewById(R.id.coach_sort_btn2);
        sort_btn3 = (Button) view.findViewById(R.id.coach_sort_btn3);
        sort_btn4 = (Button) view.findViewById(R.id.coach_sort_btn4);
        sort_btn1.setOnClickListener(this);
        sort_btn2.setOnClickListener(this);
        sort_btn3.setOnClickListener(this);
        sort_btn4.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.widget_layout_item);
        listView.setOnItemClickListener(this);
        listView.setFocusable(false);
        textView = (TextView) view.findViewById(R.id.textView);
        //上拉刷新
        swipeLayout = (RefreshLayout) view.findViewById(R.id.swipeCoach);
        swipeLayout.setColorSchemeResources(R.color.color_bule2, R.color.color_bule, R.color.color_bule2, R.color.color_bule3);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);
        swipeLayout.setTag("UNENABLE");
        sp1 = getActivity().getSharedPreferences("token",
                Activity.MODE_PRIVATE);
        token = sp1.getString("token", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.coach_sort_btn1:
                tag = "sort_btn1";
                if (mPopupWindowSub == null) {
                    mPopupWindowSub = new SelectPopupWindow(sub, null, getActivity(),
                            selectCategory);
                }
                mPopupWindowSub.showAsDropDown(sort_btn1, -5, 1);
                break;
            case R.id.coach_sort_btn2:
                coachList.clear();
                adapter = new MyCoachAdapter(getActivity(), coachList);
                listView.setAdapter(adapter);
                tag = "sort_btn2";
                if (datialPlace == null) {
                    Toast.makeText(getActivity(), "网络状态不佳，请稍后再试", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mPopupWindowCampus == null) {
                        mPopupWindowCampus = new SelectPopupWindow(city, datialPlace, getActivity(),
                                selectCategory);
                    }
                    mPopupWindowCampus.showAsDropDown(sort_btn2, -5, 1);
                }
                break;
            case R.id.coach_sort_btn3:
                tag = "sort_btn3";
                if (mPopupWindowSort == null) {
                    mPopupWindowSort = new SelectPopupWindow(sortStr, null, getActivity(),
                            selectCategory);
                }
                mPopupWindowSort.showAsDropDown(sort_btn3, -5, 1);
                break;
            case R.id.coach_sort_btn4:
                tag = "sort_btn4";
                if (mPopupWindowClassType == null) {
                    mPopupWindowClassType = new SelectPopupWindow(classType, null, getActivity(),
                            selectCategory);
                }
                mPopupWindowClassType.showAsDropDown(sort_btn4, -5, 1);
                break;
        }
    }

    private void check() {
        if (sort_btn4.getText().toString().trim().equals("班型")) {
            class_class = "";
        } else {
            class_class = sort_btn4.getText().toString().trim();
        }
        if (!sort_btn1.getText().toString().trim().equals("科目")) {
            class_type = sort_btn1.getText().toString().trim() + "教练";
        }
        if (sort_btn3.getText().toString().trim().equals("综合")) {
            sortString = "zonghe";
        } else {
            String string = sort_btn3.getText().toString().trim();
            if (string.equals("综合")) {
                sortString = "zonghe";
            } else if (string.equals("信用")) {
                sortString = "credit";
            } else if (string.equals("好评率")) {
                sortString = "haopin";
            } else if (string.equals("通过率")) {
                sortString = "tguo";
            }
        }
    }

    /**
     * 选择完成回调接口
     */
    private SelectPopupWindow.SelectCategory selectCategory = new SelectPopupWindow.SelectCategory() {
        @Override
        public void selectCategory(Integer parentSelectposition, Integer childrenSelectposition) {
            if (tag.equals("sort_btn1")) {
                sort_btn1.setText(sub[parentSelectposition]);
            } else if (tag.equals("sort_btn2")) {
                schId = schoolPlaceTotal.getResult().get(parentSelectposition).getResult().get(childrenSelectposition).getId();
                sort_btn2.setText(datialPlace[parentSelectposition][childrenSelectposition]);
            } else if (tag.equals("sort_btn3")) {
                sortString = sortStr[parentSelectposition];
                sort_btn3.setText(sortStr[parentSelectposition]);
            } else if (tag.equals("sort_btn4")) {
                class_class = classType[parentSelectposition];
                sort_btn4.setText(classType[parentSelectposition]);
            }
            check();
            page = 1;
            swipeLayout.setTag("REFRESH");
            sort(class_type, schId + "", sortString, page + "", class_class);
        }
    };

    //ListView的item项的监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView coachId = (TextView) view.findViewById(R.id.coachId);
        Intent intent = new Intent();
        intent.setClass(getActivity(), ReservationActivity.class);
        intent.putExtra("coachId", coachId.getText().toString().trim());
        intent.putExtra("token", token);
        startActivity(intent);
    }


    //上拉加载
    @Override
    public void onLoad() {
        swipeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                page++;
                swipeLayout.setTag("ONLOAD");
                check();
                sort(class_type, schId + "", sortString, page + "", class_class);
                swipeLayout.setLoading(false);
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        listView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        swipeLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                page = 1;
                swipeLayout.setTag("ONFRESH");
                sort_btn1.setText("科目");
                sort_btn2.setText("全城");
                sort_btn3.setText("综合");
                sort_btn4.setText("班型");
                check();
                sort(class_type, schId + "", sortString, page + "", class_class);
                swipeLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
