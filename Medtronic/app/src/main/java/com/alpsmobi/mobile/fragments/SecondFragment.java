package com.alpsmobi.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alpsmobi.mobile.R;
import com.alpsmobi.mobile.utils.Constants;

public class SecondFragment extends BaseFragment {

    private final String TAG = SecondFragment.class.getSimpleName();
    private EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_second, container, false);
        Button button = view.findViewById(R.id.second_button);
        mEditText = view.findViewById(R.id.second_editText);
        button.setOnClickListener(onClickListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mOnActivityCallBacks != null) {
            mEditText.setText(String.valueOf(mOnActivityCallBacks.getValue()));
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            printLog("Input Value : "+ mEditText.getText());
            if(mEditText.getText() == null || mEditText.getText().toString().equals("")) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.empty_field_error, Toast.LENGTH_SHORT).show();
            } else {
                if (mOnActivityCallBacks != null) {
                    Fragment fragment = new FirstFragment();
                    mOnActivityCallBacks.addFragment(fragment);
                    mOnActivityCallBacks.setValue(Integer.parseInt(mEditText.getText().toString()) / 2);
                }
            }
        }
    };

    private void printLog(String message){
        if(Constants.DBG){
            Log.d(TAG,message);
        }
    }
}
