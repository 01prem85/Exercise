package com.alpsmobi.mobile.fragments;

import android.app.Fragment;
import android.content.Context;

import com.alpsmobi.mobile.listeners.OnActivityCallBacks;

public class BaseFragment extends Fragment {

    public OnActivityCallBacks mOnActivityCallBacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivityCallBacks) {
            mOnActivityCallBacks = (OnActivityCallBacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnActivityCallBacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnActivityCallBacks = null;
    }
}
