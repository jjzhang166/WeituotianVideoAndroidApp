package com.weituotian.video.presenter;

import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.ILoginView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToEntityFunc1;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ange on 2017/3/16.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {

    public void doLogin(String username, String password) {

        RetrofitFactory.getUserService().doLogin(username, password)
                .flatMap(new ResultToEntityFunc1<User>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override

                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showLoginFail(e.getMessage());
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        getView().showLoginSuccess();
                        LoginContext.saveUser(user);
                        System.out.println(user);
                    }
                });
/*
        RetrofitFactory.getUserService().doLogin(username, password).map(new Func1<Result<User>, User>() {
            @Override
            public User call(Result<User> userResult) {
                return null;
            }
        });
*/
    }

}
