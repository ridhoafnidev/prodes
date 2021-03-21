package com.mrii.prodes.api;

public class CombineApi {
    public static final String BASE_URL = "http://ngebugcode.com/ps-buysell-admin/index.php/";
    public static PSApiService getApiService(){
        return ApiClient.getApiClient(BASE_URL).create(PSApiService.class);
    }
}