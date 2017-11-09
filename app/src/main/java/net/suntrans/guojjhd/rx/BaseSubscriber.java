package net.suntrans.guojjhd.rx;

import android.content.Context;


import net.suntrans.guojjhd.util.UiUtils;
import net.suntrans.guojjhd.api.ApiErrorCode;
import net.suntrans.guojjhd.api.ApiErrorHelper;
import net.suntrans.guojjhd.api.ApiException;

import rx.Subscriber;

/**
 * Created by Looney on 2017/9/6.
 */

public class BaseSubscriber<T> extends Subscriber<T> {

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        if (!UiUtils.isNetworkAvailable(context)) {
            this.onError(new ApiException(ApiErrorCode.ERROR_NO_INTERNET, "network interrupt"));
            return;
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        ApiErrorHelper.handleCommonError(context,e);
    }

    @Override
    public void onNext(T t) {

    }
}
