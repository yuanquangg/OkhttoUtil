package com.tcl.adplayer.OKHttp;

import android.text.TextUtils;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;


public class RequestUtil {
    private String mMethodType;//请求方式，目前只支持get和post
    private String mUrl;//接口
    private Map<String, String> mParamsMap;//键值对类型的参数
    private String mJsonStr;//json类型的参数，post方式
    private Map<String, String> mHeaderMap;//头参数
    private Request mOkHttpRequest;//请求对象
    private Request.Builder mRequestBuilder;//请求对象的构建者

    RequestUtil(String methodType, String url, Map<String, String> paramsMap, Map<String, String> headerMap) {
        this(methodType, url, null, paramsMap, headerMap);
    }

    RequestUtil(String methodType, String url, String jsonStr, Map<String, String> headerMap) {
        this(methodType, url, jsonStr, null, headerMap);
    }

    private RequestUtil(String methodType, String url, String jsonStr, Map<String, String> paramsMap, Map<String, String> headerMap) {
        mMethodType = methodType;
        mUrl = url;
        mJsonStr = jsonStr;
        mParamsMap = paramsMap;
        mHeaderMap = headerMap;
    }

    /**
     * 创建Request
     */
    public Request getOkHttpRequest() {
        mRequestBuilder = new Request.Builder();
        //设置参数
        switch (mMethodType) {
            case OkhttpUtil.METHOD_GET:
                setGetParams();
                break;
            case OkhttpUtil.METHOD_POST:
                mRequestBuilder.post(getRequestBody());
                break;

        }

        mRequestBuilder.url(mUrl);
        if (mHeaderMap != null) {
            setHeader();
        }
        mOkHttpRequest = mRequestBuilder.build();
        return mOkHttpRequest;
    }

    /**
     * 得到body对象
     */
    private RequestBody getRequestBody() {
        /**
         * 首先判断mJsonStr是否为空，由于mJsonStr与mParamsMap不可能同时存在，所以先判断mJsonStr
         */
        if (!TextUtils.isEmpty(mJsonStr)) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
            return RequestBody.create(JSON, mJsonStr);//json数据，
        }

        FormBody.Builder formBody = new FormBody.Builder();
        if (mParamsMap != null) {
            for (String key : mParamsMap.keySet()) {
                formBody.add(key, mParamsMap.get(key));
            }
        }
        return formBody.build();
    }

    /**
     * get请求，只有键值对参数
     */
    private void setGetParams() {
        if (mParamsMap != null) {
            mUrl = mUrl + "?";
            for (String key : mParamsMap.keySet()) {
                mUrl = mUrl + key + "=" + mParamsMap.get(key) + "&";
            }
            mUrl = mUrl.substring(0, mUrl.length() - 1);
        }
    }

    /**
     * 设置头参数
     */
    private void setHeader() {
        if (mHeaderMap != null) {
            for (String key : mHeaderMap.keySet()) {
                mRequestBuilder.addHeader(key, mHeaderMap.get(key));
            }
        }
    }

}