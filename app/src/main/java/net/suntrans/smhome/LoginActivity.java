package net.suntrans.smhome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.pgyersdk.update.PgyUpdateManager;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.smhome.api.RetrofitHelper;
import net.suntrans.smhome.bean.LoginEntity;
import net.suntrans.smhome.rx.BaseSubscriber;
import net.suntrans.smhome.util.LogUtil;
import net.suntrans.smhome.util.UiUtils;
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

        PgyUpdateManager.register(this,"net.suntrans.smhome.fileProvider");
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
                        super.onError(e);
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
                                        .putLong("envRefreshTime",loginResult.data.timer.sensus )
                                        .putLong("auto",loginResult.data.timer.auto )
                                        .putLong("energyRefreshTime",loginResult.data.timer.ammeter )
                                        .putLong("lightRefreshTime",loginResult.data.timer.light )
                                        .putString("familyname",loginResult.data.user.family_name )
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
