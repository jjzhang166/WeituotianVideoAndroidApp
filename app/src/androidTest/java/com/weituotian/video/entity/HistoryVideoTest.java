package com.weituotian.video.entity;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;
import org.junit.Test;

public class HistoryVideoTest extends AbstractDaoTestLongPk<HistoryVideoDao, HistoryVideo> {

    public HistoryVideoTest() {
        super(HistoryVideoDao.class);
    }

    @Override
    protected HistoryVideo createEntity(Long key) {
        HistoryVideo entity = new HistoryVideo();
        entity.setId(key);
        return entity;
    }

    @Test
    public void count(){
        dao.count();
    }

}
