package com.jgkj.bxxc.tools;

import com.jgkj.bxxc.bean.ItemEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangzhou on 2016/10/29.
 */
public class JsonTool  {
    public static List<ItemEntity> jsonStringList(String jsonString) {
        // 创建一个list集合
        List<ItemEntity> list = new ArrayList<ItemEntity>();
        // 解析
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            // for循环遍历
            for (int i = 0; i < jsonArray.length(); i++) {
                // 获取JSONObject对象
                JSONObject item_jsonObject = jsonArray.getJSONObject(i);
                // 创建实体对象
                ItemEntity item = new ItemEntity();
                // 放值
                item.setId(item_jsonObject.getInt("id"));
                item.setAnswer(item_jsonObject.getInt("answer"));
                item.setQuestion(item_jsonObject.getString("question"));
                item.setItem1(item_jsonObject.getString("item1"));
                item.setItem2(item_jsonObject.getString("item2"));
                item.setItem3(item_jsonObject.getString("item3"));
                item.setItem4(item_jsonObject.getString("item4"));
                item.setExplains(item_jsonObject.getString("explains"));
                item.setUrl(item_jsonObject.getString("url"));
                // 添加到集合里
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
