package com.weituotian.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.weituotian.video.GlobalConstant;
import com.weituotian.video.R;
import com.weituotian.video.mvpview.ILoginView;
import com.weituotian.video.presenter.LoginPresenter;
import com.weituotian.video.utils.SystemBarHelper;
import com.weituotian.video.utils.UIUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yifeng on 16/11/25.
 */

public class LoginActivity extends MvpActivity<ILoginView, LoginPresenter> implements ILoginView {

    @BindView(R.id.et_username)
    TextInputEditText etUsername;

    @BindView(R.id.til_mobile)
    TextInputLayout tilMobile;

    @BindView(R.id.til_password)
    TextInputLayout tilPassword;

    @BindView(R.id.bt_login)
    Button btLogin;

    @BindView(R.id.et_password)
    TextInputEditText etPassword;

    @BindView(R.id.toolbar_simple)
    Toolbar mToolbar;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, GlobalConstant.REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initToolBar();

        Intent intent = getIntent();//获得从mainactivity传来的数据
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置StatusBar透明
        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setHeightAndPadding(this, mToolbar);
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    /**
     * 登录按钮onclick
     *
     * @param v
     */
    @OnClick(R.id.bt_login)
    public void onClickLogin(View v) {
        if ((etUsername).getText().toString().length() >= 6) {//verifyEmail()
            tilMobile.setErrorEnabled(false);
            presenter.doLogin(etUsername.getText().toString(), etPassword.getText().toString());
//            UIUtil.showToast(this, "Success");
        } else {
            tilMobile.setErrorEnabled(true);
            tilMobile.setError("手机号格式错误");
        }
    }

    public boolean verifyEmail() {
        Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
        Matcher matcher = pattern.matcher((etUsername).getText().toString());
        return matcher.matches();
    }

    @Override
    public void showLoginFail(String msg) {
        UIUtil.showToast(this, msg);
    }

    @Override
    public void showLoginSuccess() {
        UIUtil.showToast(this, "登录成功");
        Intent intent = new Intent();
        intent.putExtra("a", "a");
        setResult(2, intent);//返回给mainactivity使用
        this.finish();
    }
}
