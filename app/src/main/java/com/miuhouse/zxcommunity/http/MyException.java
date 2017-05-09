package com.miuhouse.zxcommunity.http;

import android.text.TextUtils;

/**
 * User: Jiang Qi
 * Date: 12-8-14
 */
public class MyException extends Exception {

    
     /**error_code
      * 
      * 
      */

    private String error;
    //this error string is from sina weibo request return
    private String oriError;
    private int error_code;

    public String getError() {

        String result=null;

        if (!TextUtils.isEmpty(error)) {
            result = error;
        } 
//        else {
//
//            String name = "code" + error_code;
//            int i = GlobalContext.getInstance().getResources()
//                    .getIdentifier(name, "string", GlobalContext.getInstance().getPackageName());
//
//            try {
//                result = GlobalContext.getInstance().getString(i);
//
//            } catch (Resources.NotFoundException e) {
//
//                if (!TextUtils.isEmpty(oriError)) {
//                    result = oriError;
//                } else {
//
//                    result = GlobalContext.getInstance().getString(R.string.unknown_error_error_code) + error_code;
//                }
//            }
//        }

        return result;
    }

    @Override
    public String getMessage() {
        return getError();
    }


    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getError_code() {
        return error_code;
    }

    public MyException() {

    }

    public MyException(String detailMessage) {
        error = detailMessage;
    }

    public MyException(String detailMessage, Throwable throwable) {
        error = detailMessage;
    }


    public void setOriError(String oriError) {
        this.oriError = oriError;
    }

}
