package br.com.zup.realwavegeolocation.model.exceptions;

import java.net.SocketTimeoutException;

import br.com.zup.realwavegeolocation.RWGApplication;
import br.com.zup.realwavegeolocation.model.enums.ERWGeoResponses;

/**
 * Created by wisle on 01/08/2017.
 */

public class NetworkStatusException extends SocketTimeoutException {
    /**
     *
     */
    private static final long serialVersionUID = 2103853211476439926L;

    private final ERWGeoResponses response = ERWGeoResponses.M01;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ERWGeoResponses getResponse() {
        return response;
    }

    public NetworkStatusException() {
        super(ERWGeoResponses.M01.getMessage(RWGApplication.sInstance.getApplicationContext()));

    }
}
