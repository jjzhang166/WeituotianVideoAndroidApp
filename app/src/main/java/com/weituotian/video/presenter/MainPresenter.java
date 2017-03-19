package com.weituotian.video.presenter;

import android.content.Context;
import android.util.Log;

import com.weituotian.video.activity.MainActivity;
import com.weituotian.video.entity.RetInfo;
import com.weituotian.video.entity.User;
import com.weituotian.video.factory.RetrofitFactory;
import com.weituotian.video.http.LoginContext;
import com.weituotian.video.mvpview.IMainView;
import com.weituotian.video.presenter.base.BasePresenter;
import com.weituotian.video.presenter.func1.ResultToRetInfoFun1;
import com.weituotian.video.utils.UIUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.weituotian.video.http.LoginContext.logout;

/**
 * Created by ange on 2017/3/19.
 */

public class MainPresenter extends BasePresenter<IMainView> {

    public void touch() {
        RetrofitFactory.getUserService().touch()
                .flatMap(new ResultToRetInfoFun1<User>())
                .compose(this.<RetInfo<User>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RetInfo<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().onLoginError(e);
                        Log.e("logincontext", e.getMessage());
                    }

                    @Override
                    public void onNext(RetInfo<User> retInfo) {
                        if (retInfo.isSuccess()) {
                            LoginContext.saveUser(retInfo.getObj());
                            getView().setLogin();
                        } else {
                            getView().setNoLogin(retInfo.getMsg());
                            logout();
                        }
                    }
                });
    }

}
