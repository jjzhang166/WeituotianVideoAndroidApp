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

import com.weituotian.video.entity.HistoryVideo;
import com.weituotian.video.entity.HistoryVideoDao;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;

public class RxTestHelper {
    static <T> TestSubscriber<T> awaitTestSubscriber(Observable<T> observable) {
        TestSubscriber<T> testSubscriber = new TestSubscriber<>();
        observable.subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        return testSubscriber;
    }

    static HistoryVideo insertEntity(HistoryVideoDao dao, String simpleStringNotNull) {
        HistoryVideo entity = createEntity(simpleStringNotNull);
        dao.insert(entity);
        return entity;
    }

    static HistoryVideo createEntity(String simpleStringNotNull) {
        HistoryVideo entity = new HistoryVideo();
        return entity;
    }
}
