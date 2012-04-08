package com.bourke.finch;

import android.util.Log;

import com.bourke.finch.common.TwitterTask;
import com.bourke.finch.common.TwitterTaskCallback;
import com.bourke.finch.common.TwitterTaskParams;

import twitter4j.Paging;

import twitter4j.ResponseList;

import twitter4j.TwitterException;

import twitter4j.TwitterResponse;

public class ConnectionsFragment extends BaseFinchFragment {

    private static final String TAG = "Finch/ConnectionsFragment";

    @Override
    public void loadNextPage() {
        TwitterTaskCallback pullUpRefreshCallback =
                new TwitterTaskCallback<TwitterTaskParams,
                                        TwitterException>() {
            public void onSuccess(TwitterTaskParams payload) {
                /* Append responses to list adapter */
                ResponseList<TwitterResponse> res = (
                        ResponseList<TwitterResponse>)payload.result;
                mMainListAdapter.appendResponses(res);
                mMainListAdapter.notifyDataSetChanged();
                mLoadingPage = false;
            }
            public void onFailure(TwitterException e) {
                e.printStackTrace();
            }
        };
        Paging paging = new Paging(++mPage);
        Log.d(TAG, "Fetching page " + mPage);
        TwitterTaskParams getTimelineParams =
             new TwitterTaskParams(TwitterTask.GET_MENTIONS,
                 new Object[] {getSherlockActivity(), mMainListAdapter,
                     mMainList, paging});
        new TwitterTask(getTimelineParams, pullUpRefreshCallback,
            mTwitter).execute();
    }

    @Override
    public void refresh() {
        /* Fetch user's mentions */
        TwitterTaskCallback mentionsCallback = new TwitterTaskCallback<
                TwitterTaskParams, TwitterException>() {
            public void onSuccess(TwitterTaskParams payload) {
                ResponseList<TwitterResponse> res = (
                        ResponseList<TwitterResponse>)payload.result;
                mMainListAdapter.prependResponses(res);
                mMainListAdapter.notifyDataSetChanged();
            }
            public void onFailure(TwitterException e) {
                e.printStackTrace();
            }
        };
        TwitterTaskParams getMentionsParams =
            new TwitterTaskParams(TwitterTask.GET_MENTIONS,
                    new Object[] {getSherlockActivity(), mMainListAdapter,
                        mMainList, new Paging(1)});
        new TwitterTask(getMentionsParams, mentionsCallback,
                mTwitter).execute();
    }

    public void setupActionMode() {
        // TODO
    }
}