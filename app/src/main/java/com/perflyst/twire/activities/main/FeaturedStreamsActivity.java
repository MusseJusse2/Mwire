package com.perflyst.twire.activities.main;

import android.util.Log;

import com.perflyst.twire.R;
import com.perflyst.twire.adapters.MainActivityAdapter;
import com.perflyst.twire.adapters.StreamsAdapter;
import com.perflyst.twire.model.StreamInfo;
import com.perflyst.twire.service.JSONService;
import com.perflyst.twire.service.Service;
import com.perflyst.twire.views.recyclerviews.AutoSpanRecyclerView;
import com.perflyst.twire.views.recyclerviews.auto_span_behaviours.AutoSpanBehaviour;
import com.perflyst.twire.views.recyclerviews.auto_span_behaviours.StreamAutoSpanBehaviour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FeaturedStreamsActivity extends LazyMainActivity<StreamInfo> {

    @Override
    protected int getActivityIconRes() {
        return R.drawable.ic_favorite;
    }

    @Override
    protected int getActivityTitleRes() {
        return R.string.featured_activity_title;
    }

    @Override
    protected AutoSpanBehaviour constructSpanBehaviour() {
        return new StreamAutoSpanBehaviour();
    }

    @Override
    protected void customizeActivity() {
        super.customizeActivity();
        setLimit(10);
        setMaxElementsToFetch(200);
    }

    @Override
    protected MainActivityAdapter<StreamInfo, ?> constructAdapter(AutoSpanRecyclerView recyclerView) {
        return new StreamsAdapter(recyclerView, this);
    }

    @Override
    public void addToAdapter(List<StreamInfo> aObjectList) {
        mOnScrollListener.checkForNewElements(mRecyclerView);
        mAdapter.addList(aObjectList);
        Log.i(LOG_TAG, "Adding Featured Streams: " + aObjectList.size());
    }

    /**
     * Methods for functionality and for controlling the SwipeRefreshLayout
     */

    @Override
    public List<StreamInfo> getVisualElements() throws JSONException, MalformedURLException {
        List<StreamInfo> resultList = new ArrayList<>();

        JSONArray streams = settings.loadJSONArray("NAMES");

        final String BASE_URL = "https://api.twitch.tv/kraken/streams/";
        final String STREAM_OBJECT = "stream";

        if (streams.length() > 0) {

            //THIS IS A TEST
            //JOE IS A CUNT

            for (int i = 0; i < streams.length(); i++) {
                try {
                    JSONObject topObject = new JSONObject(Service.urlToJSONString(BASE_URL + streams.get(i)));
                    JSONObject streamObject = topObject.getJSONObject(STREAM_OBJECT);

                    StreamInfo mStreamInfo = JSONService.getStreamInfo(getBaseContext(), streamObject, null, false);
                    resultList.add(mStreamInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultList;

    }
}
