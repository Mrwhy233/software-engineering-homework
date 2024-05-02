package com.example.vhr.http;

/**
 * 请求回调
 *
 * @author Tellsea
 * @date 2021-11-24
 */
public interface OnResponseListener {

    void onSuccess(String response);

    void onError(String error);
}
