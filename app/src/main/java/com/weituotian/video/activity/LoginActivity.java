package com.weituotian.video.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.weituotian.video.R;
import com.weituotian.video.mvpview.ILoginView;
import com.weituotian.video.presenter.LoginPresenter;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
        if (verifyEmail()) {
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

}
