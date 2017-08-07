package br.com.zup.realwavegeolocation.model.conn;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.zup.realwavegeolocation.model.exceptions.NetworkStatusException;
import br.com.zup.realwavegeolocation.model.params.TagParam;
import br.com.zup.realwavegeolocation.model.util.Constants;

/**
 * Created by wisle on 02/08/2017.
 */

public class TagConn extends AbstractConn {


    private static final String URL_TAG_LIST = "tags/v1/tags/geolocation?gw-app-key=7de3ec809f980134b80e021e75abe44c&";
    private static final String LAT = "latitude=";
    private static final String LNG = "longitude=";
    private  final Double mLatitude;
    private  final Double mLongitude;


    public TagConn(final TagParam tagParam) {
        this.mLatitude = tagParam.getLatitude();
        this.mLongitude =tagParam.getLongitude();

    }

    @Override
    public void createUrl() throws MalformedURLException {
        final String sURL = Constants.BASE_URL + URL_TAG_LIST+LAT+this.mLatitude+"&"+LNG+this.mLongitude;
        url = new URL(sURL);

    }

    @Override
    public void execute() throws NetworkStatusException {
        this.connect(METHOD_GET, MEDIUM_TIMEOUT);

    }

    public String getDataTag() throws NetworkStatusException {
        return this.getData();
    }
}
