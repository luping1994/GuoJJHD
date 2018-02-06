package net.suntrans.smhome.api;

import android.content.Context;
import android.content.Intent;


import net.suntrans.smhome.AlertActivity;
import net.suntrans.smhome.rx.RxBus;
import net.suntrans.smhome.util.UiUtils;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Looney on 2017/9/6.
 */

public class ApiErrorHelper {
    public static void handleCommonError(final Context context, Throwable e) {
        if (e instanceof HttpException) {
            UiUtils.showToast(context, "服务暂不可用");
        } else if (e instanceof IOException) {
//            UiUtils.showToast("连接服务器失败");
        } else if (e instanceof ApiException) {

            int code = ((ApiException) e).code;
//            System.out.println("sbbbbbbbbbbbbbbbbbbbbbbbbbb="+code);

            if (code == ApiErrorCode.UNAUTHORIZED) {
//                showAlertDialog((BasedActivity) context);
                UiUtils.showToast(context, "您的身份已过期,请重新登录");
                Intent intent =new Intent(context, AlertActivity.class);
                RxBus.getInstance().post(intent);
//                startAlertActivity(context);
//                ActivityUtils.showLoginOutDialogFragmentToActivity(((BasedActivity)context).getSupportFragmentManager(),"Alert");
//                AlertDialog.Builder builder = new AlertDialog.Builder(App.getApplication());
//                AlertDialog dialog = builder.setMessage("您的身份已过期,请重新登录")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ((BasedActivity)context).killAll();
//                            }
//                        }).create();
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
//                if (!dialog.isShowing()) {//此时提示框未显示
//                    dialog.show();
//                }
            } else if (code == ApiErrorCode.ERROR_NO_INTERNET) {
                UiUtils.showToast(context, "网络连接不可用");
            } else {
                UiUtils.showToast(context, ((ApiException) e).msg);
            }
        } else {
            UiUtils.showToast(context, "服务器错误");
        }
    }

}
