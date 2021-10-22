package com.perflyst.twire.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.perflyst.twire.service.Service;
import com.perflyst.twire.service.Settings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian Rask on 26-06-2016.
 */
public class GetStreamsCountTask extends AsyncTask<Void, Void, Integer> {
    private final Settings settings;
    private final Delegate delegate;

    public GetStreamsCountTask(Context context, Delegate delegate) {
        this.settings = new Settings(context);
        this.delegate = delegate;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            final String URL = "https://api.twitch.tv/kraken/streams/followed?oauth_token=" + settings.getGeneralTwitchAccessToken() + "&offset=0&stream_type=live";
            final String STREAMS_ARRAY = "streams";

            String jsonString = Service.urlToJSONString(URL);
            JSONObject fullDataObject = new JSONObject(jsonString);

            /*String[] test = new String[]{};
            JSONArray Test = new JSONArray(Arrays.asList(test));

            settings.saveJSONArray("NAMES", Test);*/

            JSONArray Test2 = settings.loadJSONArray("NAMES");

            Log.d("Testing", Test2.toString());

            List<String> list = new ArrayList<>();
            for (int i = 0; i < Test2.length(); i++) {
                list.add( Test2.getString(i) );
            }

            JSONArray streams = fullDataObject.getJSONArray(STREAMS_ARRAY);

            int count = 0;
            for (int i = 0; i < streams.length(); i++) {
                JSONObject stream = streams.getJSONObject(i);
                JSONObject streamer = stream.getJSONObject("channel");
                String name = streamer.getString("_id").toLowerCase();

                if (list.contains(name)) {
                    count++;
                }
            }

            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        delegate.TaskFinished(integer);
    }

    public interface Delegate {
        void TaskFinished(int count);
    }
}
