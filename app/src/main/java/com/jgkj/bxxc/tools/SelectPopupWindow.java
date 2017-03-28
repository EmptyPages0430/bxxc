package com.jgkj.bxxc.tools;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jgkj.bxxc.R;
import com.jgkj.bxxc.adapter.ChildrenCategoryAdapter;
import com.jgkj.bxxc.adapter.ParentCategoryAdapter;


/**
 * PopupWindow 下拉框
 *
 * @author fangzhou
 */
public class SelectPopupWindow extends PopupWindow {
    private SelectCategory selectCategory;

    private String[] parentStrings;
    private String[][] childrenStrings;

    private ListView lvParentCategory = null;
    private ListView lvChildrenCategory = null;
    private ParentCategoryAdapter parentCategoryAdapter = null;
    private ChildrenCategoryAdapter childrenCategoryAdapter = null;
    //回调数据
    private String string;

    /**
     * @param parentStrings  字类别数据
     * @param activity
     * @param selectCategory 回调接口注入
     */
    public SelectPopupWindow(String[] parentStrings, String[][] childrenStrings, Activity activity, SelectCategory selectCategory) {
        this.selectCategory = selectCategory;
        this.parentStrings = parentStrings;
        this.childrenStrings = childrenStrings;

        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_quyu_choose_view, null);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        this.setContentView(contentView);
        this.setWidth(dm.widthPixels);
        this.setHeight(dm.heightPixels * 7 / 10);

		/* 设置背景显示 */
        setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.pop_bg));
		/* 设置触摸外面时消失 */
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true); /*设置点击menu以外其他地方以及返回键退出 */

        /**
         * 1.解决再次点击MENU键无反应问题
         */
        contentView.setFocusableInTouchMode(true);

        //父类别适配器
        lvParentCategory = (ListView) contentView.findViewById(R.id.lv_parent_category);
        parentCategoryAdapter = new ParentCategoryAdapter(activity, parentStrings);
        lvParentCategory.setAdapter(parentCategoryAdapter);

        //子类别适配器
        lvChildrenCategory = (ListView) contentView.findViewById(R.id.lv_children_category);
        childrenCategoryAdapter = new ChildrenCategoryAdapter(activity);
        lvChildrenCategory.setAdapter(childrenCategoryAdapter);

        lvParentCategory.setOnItemClickListener(parentItemClickListener);
        lvChildrenCategory.setOnItemClickListener(childrenItemClickListener);
    }


    /**
     * 子类别点击事件
     */
    private OnItemClickListener childrenItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (selectCategory != null) {
                selectCategory.selectCategory(parentCategoryAdapter.getPos(), position);
            }
            dismiss();
        }
    };
    /**
     * 父类别点击事件
     */
    private OnItemClickListener parentItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(childrenStrings!= null){
                childrenCategoryAdapter.setDatas(childrenStrings[position]);
                childrenCategoryAdapter.notifyDataSetChanged();
                parentCategoryAdapter.setSelectedPosition(position);
                parentCategoryAdapter.notifyDataSetChanged();
            }else{
                if (selectCategory != null) {
                    selectCategory.selectCategory(position, null);
                }
                parentCategoryAdapter.setSelectedPosition(position);
                parentCategoryAdapter.notifyDataSetChanged();
                dismiss();
            }


        }
    };

    /**
     * 选择成功回调
     *
     * @author apple
     */
    public interface SelectCategory {
        /**
         * 把选中的下标通过方法回调回来
         *
         * @param parentSelectposition 父类别选中下标
         */
        public void selectCategory(Integer parentSelectposition, Integer childrenSelectposition);
    }

}
