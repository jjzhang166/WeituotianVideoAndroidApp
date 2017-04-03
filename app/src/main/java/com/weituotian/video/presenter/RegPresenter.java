package com.weituotian.video.presenter;

import com.weituotian.video.entity.User;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.IRegView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import rx.functions.Action1;

/**
 * 注册presenter
 * Created by ange on 2017/3/29.
 */

public class RegPresenter extends BasePresenter<IRegView> {

    private boolean sending = false;

    public void doreg(String username, String name, String email, String password) {
        if (sending) {
            return;
        }
        sending = true;
        RetrofitFactory.getUserService().doreg(username, name, email, password, true)
                .flatMap(new ResultToEntityFunc1<User>())
                .compose(this.<User>bindToLifecycle())
                .compose(this.<User>applySchedulers())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        getView().showRegSuccess();
                        LoginContext.saveUser(user);
                        sending = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().showRegFail(throwable);
                        sending = false;
                    }
                });
    }

}
