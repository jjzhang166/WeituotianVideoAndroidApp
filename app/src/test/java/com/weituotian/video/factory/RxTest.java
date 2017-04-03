package com.weituotian.video.factory;

import org.junit.Test;

import java.io.Serializable;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by ange on 2017/4/2.
 */

public class RxTest {

    @Test
    public void concat() {
        Observable.concat(Observable.just(1, 2, 3), Observable.just("a", "b", "c"))
                .subscribe(new Action1<Serializable>() {
                    @Override
                    public void call(Serializable serializable) {
                        if (serializable instanceof Integer) {
                            System.out.println("integer:" + serializable.toString());
                        } else if (serializable instanceof String) {
                            System.out.println("string:" + serializable.toString());
                        }
                    }
                });
    }

    @Test
    public void concat2() {
        Observable.concat(Observable.just(1, 2, 3).doOnNext(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("doOnNext:" + integer);
            }
        }), Observable.just("a", "b", "c").doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("doOnNext:" + s);
            }
        })).subscribe(new Action1<Serializable>() {
            @Override
            public void call(Serializable serializable) {
                System.out.println("serializable");
            }
        });

    }

    @Test
    public void concat3() {
        Observable.just(1, 2, 3).concatMap(new Func1<Integer, Observable<?>>() {
            @Override
            public Observable<?> call(Integer integer) {
                return Observable.just(integer * integer);
            }
        }).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                System.out.println(o);
            }
        });
    }

    @Test
    public void concat4() {
        Observable.zip(
                Observable.just(1, 2, 3),
                Observable.just("a", "b", "c"),
                new Func2<Integer, String, Object>() {
                    @Override
                    public Object call(Integer integer, String s) {
                        System.out.println(integer);
                        System.out.println(s);
                        return integer * integer;
                    }
                }).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                System.out.println(o);
            }
        });
    }
}
