package br.com.zup.realwavegeolocation.model.enums;

import android.content.Context;


import com.example.realwavegeolocation.R;

import br.com.zup.realwavegeolocation.model.ERWGMesages;

/**
 * Created by wisle on 01/08/2017.
 */

public enum ERWGeoResponses {

    NO_ERROR ("NO_ERROR", R.string.NO_ERROR),
    //mobile messages
    M01("M01",R.string.M01);

    private String code;

    public String getCode() {
        return code;
    }

    public int getResponseId() {
        return responseId;
    }

    private int responseId;

    ERWGeoResponses(String code,int responseId) {
        this.code = code;
        this.responseId = responseId;
    }

    public String getMessage(final Context ctx) {
        return ctx.getString(getResponseId());
    }

    public String getMessage(final Context ctx, final int intParam) {
        return ctx.getString(getResponseId(), intParam);
    }

    public String getMessageWithErrorCode(final Context ctx, int codeError) {
        return ctx.getString(getResponseId(), codeError);
    }

    public static ERWGeoResponses getResponse(final String code) {
        return ERWGeoResponses.valueOf(code);
    }

    public static ERWGeoResponses getErrorResponse(final ERWGMesages message) {
        return ERWGeoResponses.valueOf(message.getError());
    }

    public static ERWGeoResponses getInfoResponse(final ERWGMesages message) {
        return ERWGeoResponses.valueOf(message.getInfo());
    }
}
