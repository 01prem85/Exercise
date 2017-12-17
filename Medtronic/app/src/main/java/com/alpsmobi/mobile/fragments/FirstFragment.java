package com.alpsmobi.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alpsmobi.mobile.R;
import com.alpsmobi.mobile.utils.Constants;


public class FirstFragment extends BaseFragment {

    private final String TAG = FirstFragment.class.getSimpleName();
    private EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        Button button = view.findViewById(R.id.first_button);
        mEditText = view.findViewById(R.id.first_editText);
        button.setOnClickListener(onClickListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mOnActivityCallBacks != null) {
            mEditText.setText(String.valueOf(mOnActivityCallBacks.getValue()));
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            printLog("Input Value : "+ mEditText.getText());
            if(mEditText.getText() == null || mEditText.getText().toString().equals("")) {
                Snackbar.make(view, R.string.empty_field_error, Snackbar.LENGTH_LONG).show();
            } else {
                if (mOnActivityCallBacks != null) {
                    Fragment fragment = new SecondFragment();
                    mOnActivityCallBacks.addFragment(fragment);
                    mOnActivityCallBacks.setValue(Integer.parseInt(mEditText.getText().toString()) * 2);
                }
            }
        }
    };

    private void printLog(String message){
        if(Constants.DBG){
            Log.d(TAG, message);
        }
    }
}
