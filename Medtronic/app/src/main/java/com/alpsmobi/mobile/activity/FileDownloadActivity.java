package com.alpsmobi.mobile.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alpsmobi.mobile.R;
import com.alpsmobi.mobile.service.FileDownloadService;
import com.alpsmobi.mobile.utils.Constants;
import com.alpsmobi.mobile.utils.Utility;

public class FileDownloadActivity extends AppCompatActivity {

    private final String TAG = FileDownloadActivity.class.getSimpleName();
    private static final int WRITE_REQUEST_CODE = 1001;
    private EditText mHostNameEditText, mUrlPathEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_download);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button = findViewById(R.id.download_button);
        mHostNameEditText = findViewById(R.id.hostname_editText);
        mUrlPathEditText = findViewById(R.id.url_path_editText);
        button.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            printLog("Input HostName Value : "+ mHostNameEditText.getText());
            printLog("Input UrlPath Value : "+ mUrlPathEditText.getText());
            if(mHostNameEditText.getText() == null || mHostNameEditText.getText().toString().equals("")
                    || mUrlPathEditText.getText() == null || mUrlPathEditText.getText().toString().equals("") ) {
                Snackbar.make(view, R.string.empty_field_error, Snackbar.LENGTH_LONG).show();
            } else {
                initDownloadFileRequest();
            }
        }
    };

    private void initDownloadFileRequest(){
        if(Utility.checkWritePermission(this)){
            startFileDownloadService();
        } else {
            requestPermission();
        }
    }

    private void startFileDownloadService() {
        if(Utility.isNetWorkConnectionAvailable(this)){
            Intent intent = new Intent(this, FileDownloadService.class);
            printLog("Input url Value : "+ mHostNameEditText.getText() + mUrlPathEditText.getText());
            intent.putExtra(Constants.DOWNLOAD_BASE_URL, mHostNameEditText.getText().toString());
            intent.putExtra(Constants.DOWNLOAD_FILE_PATH, mUrlPathEditText.getText().toString());
            startService(intent);
            // Empty Value once start download
            mHostNameEditText.setText("");
            mUrlPathEditText.setText("");
        } else {
            Snackbar.make(findViewById(R.id.coordinatorLayout),
                    R.string.no_network_error, Snackbar.LENGTH_LONG).show();
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFileDownloadService();
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            R.string.write_permission_error, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void printLog(String message){
        if(Constants.DBG){
            Log.d(TAG,message);
        }
    }
}
