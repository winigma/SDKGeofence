package br.com.zup.realwavegeolocation.connectorcallback.interfaces;

import java.util.List;

import br.com.zup.realwavegeolocation.model.ERWGMesages;
import br.com.zup.realwavegeolocation.model.Tag;
import br.com.zup.realwavegeolocation.model.TagBO;
import br.com.zup.realwavegeolocation.model.enums.ERWGeoResponses;

/**
 * Created by wisle on 02/08/2017.
 */

public interface ITagConnectorCallback {


    void notifyStartListener();

    void notifySuccessListener(final List<Tag> list);

    void notifyFailureListener(final ERWGeoResponses response);
}
