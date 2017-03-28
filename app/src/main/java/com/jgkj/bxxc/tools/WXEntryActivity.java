package com.jgkj.bxxc.tools;

import android.app.AlertDialog;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * 微信回调,不过官方文档上说这个类一定要放在wxapi下，但是这里也能正常运行
 */
public class WXEntryActivity extends WXCallbackActivity {
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.i("Action", "onPayFinish,errCode=" + resp.errCode);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("这是title");
        }
    }
}
