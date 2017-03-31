package com.weituotian.video.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.weituotian.video.GlobalConstant;
import com.weituotian.video.R;
import com.weituotian.video.mvpview.IRegView;
import com.weituotian.video.presenter.RegPresenter;
import com.weituotian.video.utils.SystemBarHelper;
import com.weituotian.video.utils.UIUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ange on 2017/3/29.
 */

public class RegActivity extends BaseMvpActivity<IRegView, RegPresenter> implements IRegView {


    @BindView(R.id.toolbar_simple)
    Toolbar mToolbar;
    @BindView(R.id.et_username)
    TextInputEditText mEtUsername;
    @BindView(R.id.wrapper_username)
    TextInputLayout mWrapperUsername;
    @BindView(R.id.et_name)
    TextInputEditText mEtName;
    @BindView(R.id.wrapper_name)
    TextInputLayout mWrapperName;
    @BindView(R.id.et_email)
    TextInputEditText mEtEmail;
    @BindView(R.id.wrapper_email)
    TextInputLayout mWrapperEmail;
    @BindView(R.id.et_password)
    TextInputEditText mEtPassword;
    @BindView(R.id.wrapper_password)
    TextInputLayout mWrapperPassword;
    @BindView(R.id.et_password2)
    TextInputEditText mEtPassword2;
    @BindView(R.id.wrapper_password2)
    TextInputLayout mWrapperPassword2;
    @BindView(R.id.btn_reg)
    Button mBtnReg;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, RegActivity.class);
        activity.startActivityForResult(intent, GlobalConstant.REQUEST_CODE_LOGIN);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_reg;
    }

    @NonNull
    @Override
    public RegPresenter createPresenter() {
        return new RegPresenter();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initToolBar();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean verifyEmail(String email) {
        Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private String getTextInputText(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString();
    }

    private boolean checkRegForm() {

        //验证用户名
        if (getTextInputText(mEtUsername).length() >= 6) {//verifyEmail()
            mWrapperUsername.setErrorEnabled(false);
        } else {
            mWrapperUsername.setErrorEnabled(true);
            mWrapperUsername.setError("用户名长度不能小于6");
            return false;
        }

        //验证昵称
        if (getTextInputText(mEtName).length() >= 2) {//verifyEmail()
            mWrapperUsername.setErrorEnabled(false);
        } else {
            mWrapperUsername.setErrorEnabled(true);
            mWrapperUsername.setError("昵称长度不能小于2");
            return false;
        }

        //验证邮箱
        if (verifyEmail(getTextInputText(mEtEmail))) {
            mWrapperEmail.setErrorEnabled(false);
        } else {
            mWrapperEmail.setErrorEnabled(true);
            mWrapperEmail.setError("邮箱格式不正确");
            return false;
        }

        //验证密码是否相同
        if (getTextInputText(mEtPassword).equals(getTextInputText(mEtPassword2))) {
            mWrapperEmail.setErrorEnabled(false);
        } else {
            mWrapperEmail.setErrorEnabled(true);
            mWrapperEmail.setError("两次输入的密码不相同");
            return false;
        }

        return true;
    }

    /**
     * 注册按钮点击
     */
    @OnClick(R.id.btn_reg)
    public void onClick() {
        String username = getTextInputText(mEtUsername);
        String name = getTextInputText(mEtName);
        String email = getTextInputText(mEtEmail);
        String password = getTextInputText(mEtPassword);
        if (checkRegForm()) {
            presenter.doreg(username, name, email, password);
        }
    }

    /* 以下实现iview的接口 */

    @Override
    public void showRegFail(Throwable throwable) {
        UIUtil.showToast(this, throwable.getMessage());
    }

    @Override
    public void showRegSuccess() {
        UIUtil.showToast(this, "登录成功");
        Intent intent = new Intent();
        intent.putExtra("a", "a");
        setResult(3, intent);//返回给mainactivity使用
        this.finish();
    }
}
