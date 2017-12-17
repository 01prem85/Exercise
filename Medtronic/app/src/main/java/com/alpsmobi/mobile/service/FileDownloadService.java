package com.alpsmobi.mobile.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.alpsmobi.mobile.model.FileDownload;
import com.alpsmobi.mobile.R;
import com.alpsmobi.mobile.listeners.DownloadAPI;
import com.alpsmobi.mobile.utils.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FileDownloadService extends IntentService {

    private final String TAG = FileDownloadService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 101;
    private static final int MAX_LIMIT = 100;
    //UPDATE_DURATION_MILLIS - This can be configured to one minute if large file
    private static final int UPDATE_DURATION_MILLIS = 1000;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public FileDownloadService() {
        super("FileDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String baseUrl = intent.getStringExtra(Constants.DOWNLOAD_BASE_URL);
            String filePath = intent.getStringExtra(Constants.DOWNLOAD_FILE_PATH);
            printLog("onHandleIntent baseUrl: "+ baseUrl);
            printLog("onHandleIntent filePath: "+ filePath);
            handleFileDownloadAction(baseUrl, filePath);
        }
    }

    private void handleFileDownloadAction(String baseUrl, String filePath) {
        buildNotification();
        try {
            initFileDownload(baseUrl, filePath);
        } catch (Exception e) {
            printLog("handleFileDownloadAction: "+ e.getMessage());
            displayToast(e.getMessage());
        }
    }

    private void initFileDownload(String baseUrl, String filePath){
        final String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        printLog( "To save fileName : " + fileName);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();

        DownloadAPI downloadAPI = retrofit.create(DownloadAPI.class);

        Call<ResponseBody> request = downloadAPI.downloadFileByUrl(baseUrl + filePath);

        Callback<ResponseBody> downloadCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                printLog("call connected at url: " + call.request().url());
                try {
                    if(response.isSuccessful()){
                        readAndSaveFile(response.body(), fileName);
                    } else {
                        displayToast(response.message());
                        printLog( "Response unsuccessful : " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                printLog("call failed at url: " + call.request().url());
                displayToast(t.getMessage());
            }
        };

        try {
            request.enqueue(downloadCallback);
        } catch (Exception  e){
            printLog("initFileDownload: "+ e.getMessage());
            displayToast(e.getMessage());
        }
    }

    private void readAndSaveFile(ResponseBody body, String fileName) throws IOException {
        int count;
        InputStream bis = null;
        OutputStream output = null;
        byte data[] = new byte[1024 * 4];
        long total = 0;
        int timeCount = 1;
        int totalFileSize;
        long startTime = System.currentTimeMillis();
        try {
            long fileSize = body.contentLength();
            bis = new BufferedInputStream(body.byteStream(), 1024 * 8);

            File outputFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName);
            output = new FileOutputStream(outputFile);

            while ((count = bis.read(data)) != -1) {

                total += count;
                totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
                double current = Math.round(total / (Math.pow(1024, 2)));

                int progress = (int) ((total * 100) / fileSize);
                long currentTime = System.currentTimeMillis() - startTime;

                FileDownload fileDownload = new FileDownload();
                fileDownload.setTotalFileSize(totalFileSize);

                if (currentTime > UPDATE_DURATION_MILLIS * timeCount) {
                    fileDownload.setCurrentFileSize((int) current);
                    fileDownload.setDownloadProgress(progress);
                    printLog("onHandleIntent: progress " + progress);
                    updateProgressNotification(fileDownload);
                    timeCount++;
                }

                output.write(data, 0, count);
            }
            downloadCompleteNotification();
        }catch (Exception e){
            displayToast(e.getMessage());
        } finally {
            if(output != null) {
                output.flush();
                output.close();
            }
            if(bis != null) {
                bis.close();
            }
        }
    }

    private void buildNotification(){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(getString(R.string.download))
                .setContentText(getString(R.string.download_start_text))
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void updateProgressNotification(FileDownload fileDownload){
        notificationBuilder.setProgress(MAX_LIMIT, fileDownload.getDownloadProgress(),false);
        notificationBuilder.setContentText(String.format(Locale.getDefault(),
                getString(R.string.download_progress_text),
                fileDownload.getCurrentFileSize(),
                fileDownload.getTotalFileSize()));
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void downloadCompleteNotification(){
        notificationManager.cancel(NOTIFICATION_ID);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        notificationBuilder.setContentText(getString(R.string.download_finished_text));
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void downloadErrorNotification(){
        notificationManager.cancel(NOTIFICATION_ID);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        notificationBuilder.setContentText(getString(R.string.download_error_text));
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void printLog(String message){
        if(Constants.DBG){
            Log.d(TAG, message);
        }
    }

    private void displayToast(String msg){
        Toast.makeText(this, "Error while downloading "+msg, Toast.LENGTH_SHORT).show();
        downloadErrorNotification();
    }
}
