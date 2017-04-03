package com.weituotian.video;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.weituotian.video.entity.DaoMaster;
import com.weituotian.video.entity.DaoSession;
import com.weituotian.video.entity.HistoryVideoTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ange on 2017/4/2.
 */
@RunWith(AndroidJUnit4.class)
public class GreenDaoTest {

    private DaoSession daoSession;
    HistoryVideoTest historyVideoTest;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(appContext, null, null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
//        minimalEntityDao = daoSession.getMinimalEntityDao();
    }

    @Test
    public void count(){
        historyVideoTest.testCount();
    }

}
