package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.programming.kantech.app.backend.myApi.MyApi;
import com.programming.kantech.app.jokedisplay.Activity_DisplayJoke;
import com.udacity.gradle.builditbigger.IdlingResource.SimpleIdlingResource;

import java.io.IOException;




/**
 * Created by patrick keogh on 2017-07-12.
 * Async Task that fetches a joke from Google End Point and
 * opens an activity to display the result
 */

class Task_GetJoke extends AsyncTask<Void, Void, String> {

    private static MyApi myApiService = null;
    private Context mContext;

    private SimpleIdlingResource mIdlingResource;

    public Task_GetJoke(Context context, @Nullable final SimpleIdlingResource idlingResource) {
        this.mContext = context;
        this.mIdlingResource = idlingResource;
    }


    @Override
    protected String doInBackground(Void... voids) {

        /**
         * The IdlingResource is null in production as set by the @Nullable annotation which means
         * the value is allowed to be null.
         *
         * If the idle state is true, Espresso can perform the next action.
         * If the idle state is false, Espresso will wait until it is true before
         * performing the next action.
         */
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    //.setRootUrl("http://192.168.0.27:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();

        ((MainActivity) mContext).hideSpinner();

        Intent intent = new Intent(mContext, Activity_DisplayJoke.class);

        intent.putExtra(Activity_DisplayJoke.JOKE_KEY, result);
        mContext.startActivity(intent);
    }
}
