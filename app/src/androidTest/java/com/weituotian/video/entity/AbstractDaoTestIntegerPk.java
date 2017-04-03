package com.weituotian.video.entity;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.test.AbstractDaoTestSinglePk;

/**
 * Created by ange on 2017/4/2.
 */

public abstract class AbstractDaoTestIntegerPk <D extends AbstractDao<T, Integer>, T> extends AbstractDaoTestSinglePk<D, T, Integer> {

    public AbstractDaoTestIntegerPk(Class<D> daoClass) {
        super(daoClass);
    }

    @Override
    protected Integer createRandomPk() {
        return random.nextInt();
    }

}
