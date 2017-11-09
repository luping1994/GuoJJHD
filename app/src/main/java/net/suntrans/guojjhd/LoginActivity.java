package net.suntrans.guojjhd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import net.suntrans.guojjhd.api.RetrofitHelper;
import net.suntrans.guojjhd.bean.LoginEntity;
import net.suntrans.guojjhd.rx.BaseSubscriber;
import net.suntrans.guojjhd.util.LogUtil;
import net.suntrans.guojjhd.util.UiUtils;
import net.suntrans.looney.widgets.EditView;
import net.suntrans.looney.widgets.LoadingDialog;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/21.
 */

public class LoginActivity extends BasedActivity implements View.OnClickListener {
    private EditView account;
    private EditView password;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = (EditView) findViewById(R.id.account);
        password = (EditView) findViewById(R.id.password);
        String usernames = App.getSharedPreferences().getString("account","");
        String passwords = App.getSharedPreferences().getString("password","");

        account.setText(usernames);
        password.setText(passwords);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            login();
        }
    }

    private void login() {
        String accounts = account.getText().toString();
        String passwords = password.getText().toString();
        if (TextUtils.isEmpty(accounts)) {
            UiUtils.showToast(getApplicationContext(),"请输入账号");

            return;
        }
        if (TextUtils.isEmpty(passwords)) {
            UiUtils.showToast(getApplicationContext(),"请输入密码");
            return;
        }
        if (dialog== null){
            dialog= new LoadingDialog(this);
            dialog.setWaitText("登录中...");
        }
        dialog.show();
        accounts = accounts.replace(" ","");
        passwords =passwords.replace(" ","");
        final String finalAccounts = accounts;
        final String finalPasswords = passwords;
        RetrofitHelper.getApi().login(accounts, passwords)
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
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(LoginEntity loginResult) {
                        dialog.dismiss();
                        if (loginResult != null) {
                            LogUtil.i(loginResult.toString());
                            if (loginResult.data.token != null) {
                                App.getSharedPreferences().edit().putString("access_token", loginResult.data.token.access_token)
                                        .putString("account", finalAccounts)
                                        .putString("password", finalPasswords)
                                        .putLong("firsttime", System.currentTimeMillis())
                                        .commit();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                            } else {
                                UiUtils.showToast(getApplicationContext(),"服务器错误!登录失败");

                            }

                        } else {

                            UiUtils.showToast(getApplicationContext(),"服务器错误!登录失败");

                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }

    }
}
