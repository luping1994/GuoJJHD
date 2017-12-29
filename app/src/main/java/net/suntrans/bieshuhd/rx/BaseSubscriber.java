package net.suntrans.bieshuhd.rx;

import android.content.Context;


import net.suntrans.bieshuhd.util.UiUtils;
import net.suntrans.bieshuhd.api.ApiErrorCode;
import net.suntrans.bieshuhd.api.ApiErrorHelper;
import net.suntrans.bieshuhd.api.ApiException;

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
