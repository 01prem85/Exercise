package com.alpsmobi.mobile.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alpsmobi.mobile.R;
import com.alpsmobi.mobile.fragments.FirstFragment;
import com.alpsmobi.mobile.listeners.OnActivityCallBacks;
import com.alpsmobi.mobile.utils.Constants;

public class CalculationActivity extends AppCompatActivity implements OnActivityCallBacks {

    private final String TAG = CalculationActivity.class.getSimpleName();
    private int mValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcutation);
        mValue = Constants.DEFAULT_VALUE;
        addFragment(new FirstFragment());
    }

    @Override
    public void addFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, fragment);
            fragmentTransaction.commit();
        } catch (Exception ex){
            printLog(ex.getMessage());
        }
    }

    @Override
    public void popFragment() {
        if(getFragmentManager() != null) {
            try {
                getFragmentManager().popBackStack();
            } catch (Exception ex){
                printLog(ex.getMessage());
            }
        }
    }

    @Override
    public void setValue(int value) {
        mValue = value;
    }

    @Override
    public int getValue() {
        return mValue;
    }

    private void printLog(String message){
        if(Constants.DBG){
            Log.d(TAG,message);
        }
    }
}
