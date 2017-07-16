package com.udacity.gradle.builditbigger;

import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by patrick keogh on 2017-07-15.
 *
 */

public class Test_JokeService extends InstrumentationTestCase {

    private final CountDownLatch signal = new CountDownLatch(1);

    public void testAsyncTask() throws Throwable {

        final Task_GetJoke task = new Task_GetJoke(InstrumentationRegistry.getTargetContext(), null) {

            @Override
            protected String doInBackground(Void... voids) {
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(String result) {
                assertNotNull(result);
                signal.countDown();
            }
        };

        runTestOnUiThread(new Runnable() {

            @Override
            public void run() {
                task.execute();
            }
        });
        signal.await(30, TimeUnit.SECONDS);
    }
}
