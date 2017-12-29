package net.suntrans.bieshuhd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.bieshuhd.api.RetrofitHelper;
import net.suntrans.bieshuhd.bean.LoginEntity;
import net.suntrans.bieshuhd.rx.BaseSubscriber;
import net.suntrans.bieshuhd.util.UiUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelcomeActivity extends BasedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setUpFullScreen();
        init();
    }

    private void init() {
        String account = App.getSharedPreferences().getString("account", "");
        String password = App.getSharedPreferences().getString("password", "");
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            handler.postDelayed(loginRunable, 2000);
            return;
        }
        login(account,password);
    }

    private void login(String accounts, String passwords) {

        if (TextUtils.isEmpty(accounts)) {
            UiUtils.showToast(getApplicationContext(), "请输入账号");

            return;
        }
        if (TextUtils.isEmpty(passwords)) {
            UiUtils.showToast(getApplicationContext(), "请输入密码");
            return;
        }

        accounts = accounts.replace(" ", "");
        passwords = passwords.replace(" ", "");
        final String finalAccounts = accounts;
        final String finalPasswords = passwords;
        RetrofitHelper.getApi().login(accounts, passwords,"1","009eb687a4fcafdabe991c320172fcc9","password")
                .compose(this.<LoginEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<LoginEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.postDelayed(loginRunable, 2000);

                    }

                    @Override
                    public void onNext(LoginEntity loginResult) {
                        if (loginResult != null) {
                            if (loginResult.data.token != null) {
                                App.getSharedPreferences().edit().putString("access_token", loginResult.data.token.access_token)
                                        .putString("account", finalAccounts)
                                        .putString("password", finalPasswords)
                                        .putLong("firsttime", System.currentTimeMillis())
                                        .putLong("envRefreshTime",loginResult.data.timer.sensus )
                                        .putLong("energyRefreshTime",loginResult.data.timer.ammeter )
                                        .putLong("lightRefreshTime",loginResult.data.timer.light )
                                        .putString("familyname",loginResult.data.user.family_name )
                                        .commit();

                                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                finish();
                            } else {
                                handler.postDelayed(loginRunable, 2000);
                            }

                        } else {
                            handler.postDelayed(loginRunable, 2000);
                        }
                    }
                });
    }

    private Runnable loginRunable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }
    };
    private Runnable mainRunable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(loginRunable);
        super.onDestroy();
    }

    Handler handler = new Handler();

    private void setUpFullScreen() {
        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);


//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
