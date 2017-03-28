package com.jgkj.bxxc.tools;

import com.jgkj.bxxc.bean.HistoryView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangzhou on 2016/11/2.
 * 原生解析json数据，原来是用来解析聚合数据的，但是后台聚合数据更新了就没用了
 */
public class MyJson {
    public static List<HistoryView> jsonStringList(String jsonString) {
        // 创建一个list集合
        List<HistoryView> list = new ArrayList<HistoryView>();
        // 解析
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            // for循环遍历
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                HistoryView hisView = new HistoryView(null,(int)json.get("number"),(int)json.get("right_Answer"),
                        (int)json.get("user_Answer"),json.get("answerA").toString(),json.get("answerB").toString(),
                        json.get("answerC").toString(),json.get("answerD").toString(),json.get("explain").toString(),
                        json.get("question").toString(),json.get("picture").toString());
                list.add(hisView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
