/*
 * Copyright (C) 2011-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * This file is part of greenDAO Generator.
 *
 * greenDAO Generator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * greenDAO Generator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with greenDAO Generator.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.weituotian.video.rx;

import android.util.Log;

import com.weituotian.video.entity.HistoryVideo;
import com.weituotian.video.entity.HistoryVideoDao;

import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.test.AbstractDaoTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

@SuppressWarnings("unchecked")
public class RxDaoTest extends AbstractDaoTest<HistoryVideoDao, HistoryVideo, Long> {

    private RxDao rxDao;

    public RxDaoTest() {
        super(HistoryVideoDao.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        rxDao = dao.rx();
    }

    public void testScheduler() {
        TestSubscriber<List<HistoryVideo>> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.loadAll());
        Thread lastSeenThread = testSubscriber.getLastSeenThread();
        assertNotSame(lastSeenThread, Thread.currentThread());
    }

    public void testNoScheduler() {
        RxDao<HistoryVideo, Long> rxDaoNoScheduler = dao.rxPlain();
        TestSubscriber<List<HistoryVideo>> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDaoNoScheduler.loadAll());
        Thread lastSeenThread = testSubscriber.getLastSeenThread();
        assertSame(lastSeenThread, Thread.currentThread());
    }

    /*public void testLoadAll() {
        insertEntity("foo");
        insertEntity("bar");

        TestSubscriber<List<HistoryVideo>> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.loadAll());
        assertEquals(1, testSubscriber.getValueCount());
        List<HistoryVideo> entities = testSubscriber.getOnNextEvents().get(0);

        // Order of entities is unspecified
        int foo = 0, bar = 0;
        for (HistoryVideo entity : entities) {
            String value = entity.getSimpleStringNotNull();
            if (value.equals("foo")) {
                foo++;
            } else if (value.equals("bar")) {
                bar++;
            } else {
                fail(value);
            }
        }
        assertEquals(1, foo);
        assertEquals(1, bar);
    }

    public void testLoad() {
        HistoryVideo foo = insertEntity("foo");
        TestSubscriber<HistoryVideo> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.load(foo.getId()));
        assertEquals(1, testSubscriber.getValueCount());
        HistoryVideo foo2 = testSubscriber.getOnNextEvents().get(0);
        assertEquals(foo.getSimpleStringNotNull(), foo2.getSimpleStringNotNull());
    }

    public void testLoad_noResult() {
        TestSubscriber<HistoryVideo> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.load(42));
        assertEquals(1, testSubscriber.getValueCount());
        // Should we really propagate null through Rx?
        assertNull(testSubscriber.getOnNextEvents().get(0));
    }

    public void testRefresh() {
        HistoryVideo entity = insertEntity("foo");
        entity.setSimpleStringNotNull("temp");
        RxTestHelper.awaitTestSubscriber(rxDao.refresh(entity));
        assertEquals("foo", entity.getSimpleStringNotNull());
    }

    public void testInsert() {
        HistoryVideo foo = RxTestHelper.createEntity("foo");
        TestSubscriber<HistoryVideo> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.insert(foo));
        assertEquals(1, testSubscriber.getValueCount());
        HistoryVideo foo2 = testSubscriber.getOnNextEvents().get(0);
        assertSame(foo, foo2);

        List<HistoryVideo> all = dao.loadAll();
        assertEquals(1, all.size());
        assertEquals(foo.getSimpleStringNotNull(), all.get(0).getSimpleStringNotNull());
    }

    public void testInsertInTx() {
        HistoryVideo foo = RxTestHelper.createEntity("foo");
        HistoryVideo bar = RxTestHelper.createEntity("bar");
        TestSubscriber<Object[]> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.insertInTx(foo, bar));
        assertEquals(1, testSubscriber.getValueCount());
        Object[] array = testSubscriber.getOnNextEvents().get(0);
        assertSame(foo, array[0]);
        assertSame(bar, array[1]);

        List<HistoryVideo> all = dao.loadAll();
        assertEquals(2, all.size());
        assertEquals(foo.getSimpleStringNotNull(), all.get(0).getSimpleStringNotNull());
        assertEquals(bar.getSimpleStringNotNull(), all.get(1).getSimpleStringNotNull());
    }

    public void testInsertInTxList() {
        HistoryVideo foo = RxTestHelper.createEntity("foo");
        HistoryVideo bar = RxTestHelper.createEntity("bar");
        List<HistoryVideo> list = new ArrayList<>();
        list.add(foo);
        list.add(bar);
        TestSubscriber<List<HistoryVideo>> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.insertInTx(list));
        assertEquals(1, testSubscriber.getValueCount());
        List<HistoryVideo> result = testSubscriber.getOnNextEvents().get(0);
        assertSame(foo, result.get(0));
        assertSame(bar, result.get(1));

        List<HistoryVideo> all = dao.loadAll();
        assertEquals(2, all.size());
        assertEquals(foo.getSimpleStringNotNull(), all.get(0).getSimpleStringNotNull());
        assertEquals(bar.getSimpleStringNotNull(), all.get(1).getSimpleStringNotNull());
    }

    public void testInsertOrReplace() {
        HistoryVideo foo = insertEntity("foo");

        foo.setSimpleStringNotNull("bar");

        assertUpdatedEntity(foo, rxDao.insertOrReplace(foo));
    }

    public void testInsertOrReplaceInTx() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");

        foo.setSimpleStringNotNull("foo2");

        assertUpdatedEntities(foo, bar, rxDao.insertOrReplaceInTx(foo, bar));
    }

    public void testInsertOrReplaceInTxList() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");

        foo.setSimpleStringNotNull("foo2");

        List<HistoryVideo> list = new ArrayList<>();
        list.add(foo);
        list.add(bar);

        assertUpdatedEntities(list, rxDao.insertOrReplaceInTx(list));
    }

    public void testSave() {
        HistoryVideo foo = insertEntity("foo");

        foo.setSimpleStringNotNull("bar");

        assertUpdatedEntity(foo, rxDao.save(foo));
    }

    public void testSaveInTx() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");

        foo.setSimpleStringNotNull("foo2");

        assertUpdatedEntities(foo, bar, rxDao.saveInTx(foo, bar));
    }

    public void testSaveInTxList() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");

        foo.setSimpleStringNotNull("foo2");

        List<HistoryVideo> list = new ArrayList<>();
        list.add(foo);
        list.add(bar);

        assertUpdatedEntities(list, rxDao.saveInTx(list));
    }

    public void testUpdate() {
        HistoryVideo foo = insertEntity("foo");
        foo.setSimpleString("foofoo");
        TestSubscriber testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.update(foo));
        assertEquals(1, testSubscriber.getValueCount());
        assertSame(foo, testSubscriber.getOnNextEvents().get(0));
        List<HistoryVideo> testEntities = dao.loadAll();
        assertEquals(1, testEntities.size());
        assertNotSame(foo, testEntities.get(0));
        assertEquals("foofoo", testEntities.get(0).getSimpleString());
    }

    public void testUpdateInTx() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");

        foo.setSimpleStringNotNull("foo2");
        bar.setSimpleStringNotNull("bar2");

        assertUpdatedEntities(foo, bar, rxDao.updateInTx(foo, bar));
    }

    public void testUpdateInTxList() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");

        foo.setSimpleStringNotNull("foo2");
        bar.setSimpleStringNotNull("bar2");

        List<HistoryVideo> list = new ArrayList<>();
        list.add(foo);
        list.add(bar);

        assertUpdatedEntities(list, rxDao.updateInTx(list));
    }

    private void assertUpdatedEntity(HistoryVideo foo, Observable<HistoryVideo> observable) {
        TestSubscriber<HistoryVideo> testSubscriber = RxTestHelper.awaitTestSubscriber(observable);
        assertEquals(1, testSubscriber.getValueCount());
        HistoryVideo bar = testSubscriber.getOnNextEvents().get(0);
        assertSame(foo, bar);

        List<HistoryVideo> all = dao.loadAll();
        assertEquals(1, all.size());
        assertEquals(foo.getSimpleStringNotNull(), all.get(0).getSimpleStringNotNull());
    }

    private void assertUpdatedEntities(HistoryVideo foo, HistoryVideo bar, Observable<Object[]> observable) {
        TestSubscriber<Object[]> testSubscriber = RxTestHelper.awaitTestSubscriber(observable);
        assertEquals(1, testSubscriber.getValueCount());
        Object[] array = testSubscriber.getOnNextEvents().get(0);
        assertSame(foo, array[0]);
        assertSame(bar, array[1]);

        List<HistoryVideo> all = dao.loadAll();
        assertEquals(2, all.size());
        assertEquals(foo.getSimpleStringNotNull(), all.get(0).getSimpleStringNotNull());
        assertEquals(bar.getSimpleStringNotNull(), all.get(1).getSimpleStringNotNull());
    }

    private void assertUpdatedEntities(List<HistoryVideo> entities, Observable<List<HistoryVideo>> observable) {
        HistoryVideo foo = entities.get(0);
        HistoryVideo bar = entities.get(1);

        TestSubscriber<List<HistoryVideo>> testSubscriber = RxTestHelper.awaitTestSubscriber(observable);
        assertEquals(1, testSubscriber.getValueCount());
        List<HistoryVideo> result = testSubscriber.getOnNextEvents().get(0);
        assertSame(foo, result.get(0));
        assertSame(bar, result.get(1));

        List<HistoryVideo> all = dao.loadAll();
        assertEquals(2, all.size());
        assertEquals(foo.getSimpleStringNotNull(), all.get(0).getSimpleStringNotNull());
        assertEquals(bar.getSimpleStringNotNull(), all.get(1).getSimpleStringNotNull());
    }*/

    public void testDelete() {
        HistoryVideo foo = insertEntity("foo");
        assertDeleted(rxDao.delete(foo));
    }

    public void testDeleteByKey() {
        HistoryVideo foo = insertEntity("foo");
        assertDeleted(rxDao.deleteByKey(foo.getId()));
    }

    public void testDeleteAll() {
        insertEntity("foo");
        insertEntity("bar");
        assertDeleted(rxDao.deleteAll());
    }

    public void testDeleteInTx() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");
        assertDeleted(rxDao.deleteInTx(foo, bar));
    }

    public void testDeleteInTxList() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");

        List<HistoryVideo> list = new ArrayList<>();
        list.add(foo);
        list.add(bar);

        assertDeleted(rxDao.deleteInTx(list));
    }

    public void testDeleteByKeyInTx() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");
        assertDeleted(rxDao.deleteByKeyInTx(foo.getId(), bar.getId()));
    }

    public void testDeleteByKeyInTxList() {
        HistoryVideo foo = insertEntity("foo");
        HistoryVideo bar = insertEntity("bar");

        List<Long> list = new ArrayList<>();
        list.add(foo.getId());
        list.add(bar.getId());

        assertDeleted(rxDao.deleteByKeyInTx(list));
    }

    private void assertDeleted(Observable<Void> observable) {
        TestSubscriber testSubscriber = RxTestHelper.awaitTestSubscriber(observable);
        assertEquals(1, testSubscriber.getValueCount());
        assertNull(testSubscriber.getOnNextEvents().get(0));
        assertEquals(0, dao.count());
    }

    public void testCount() {
        insertEntity("foo");
        TestSubscriber<Long> testSubscriber = RxTestHelper.awaitTestSubscriber(rxDao.count());
        assertEquals(1, testSubscriber.getValueCount());
        Long count = testSubscriber.getOnNextEvents().get(0);
        assertEquals(1L, (long) count);
    }

    public void testInsert() {
        final HistoryVideo historyVideo = new HistoryVideo();
        historyVideo.setId(1L);
        historyVideo.setClick(1);
        historyVideo.setCollect(1);
        historyVideo.setCover("");
        historyVideo.setCreateTime(new Date());
        historyVideo.setDescript("asd");
        historyVideo.setMemberName("asd");
        historyVideo.setPartitionName("sdf");
        historyVideo.setTotalTime(1);
        historyVideo.setPlay(1);

        Log.i("testInsert", historyVideo.toString());
        System.out.println(historyVideo);

        //观看时间
        historyVideo.setViewTime(new Date());
        rxDao.insert(historyVideo)
                .flatMap(new Func1<HistoryVideo, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(HistoryVideo historyVideo) {
                        System.out.println(historyVideo);
                        return Observable.never();
                    }
                }).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                System.out.println(o);
            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected HistoryVideo insertEntity(String simpleStringNotNull) {
        return RxTestHelper.insertEntity(dao, simpleStringNotNull);
    }
}
