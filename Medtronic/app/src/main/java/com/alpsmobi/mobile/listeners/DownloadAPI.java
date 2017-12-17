package com.alpsmobi.mobile.listeners;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadAPI {

    @Streaming
    @GET
    Call<ResponseBody> downloadFileByUrl(@Url String fileUrl);
}
