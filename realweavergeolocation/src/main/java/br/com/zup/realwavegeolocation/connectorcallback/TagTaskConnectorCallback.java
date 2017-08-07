package br.com.zup.realwavegeolocation.connectorcallback;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.realwavegeolocation.connectorcallback.interfaces.ITagConnectorCallback;
import br.com.zup.realwavegeolocation.ijoin.interfaces.ITagServiceIntegration;
import br.com.zup.realwavegeolocation.model.Tag;
import br.com.zup.realwavegeolocation.model.TagBO;
import br.com.zup.realwavegeolocation.model.TagPoint;
import br.com.zup.realwavegeolocation.model.enums.ERWGeoResponses;
import br.com.zup.realwavegeolocation.model.params.TagParam;
import br.com.zup.realwavegeolocation.model.task.TagTask;

import android.location.Location;
import android.os.AsyncTask.Status;


/**
 * Created by wisle on 02/08/2017.
 */

public class TagTaskConnectorCallback implements ITagConnectorCallback{

    private TagTask mTask;
    private ITagServiceIntegration mCallIntegration;


    public TagTaskConnectorCallback(final ITagServiceIntegration mCallback) {
        this.mCallIntegration = mCallback;
    }

    public void notifyStartPresenter(final Location location) {
        startNewsTask(location);
    }

    private void startNewsTask(final Location location) {
        cancelTask();
        mTask = new TagTask (this);
        if(mTask.getStatus() != Status.RUNNING) {
            mTask.execute(new TagParam(location.getLatitude(), location.getLongitude()));
        }
    }
    @Override
    public void notifyStartListener() {

        mCallIntegration.notifyStartListener();
    }

    @Override
    public void notifySuccessListener(List<Tag> list) {

        List<TagBO> mBOs = new ArrayList<>();
        for (Tag tag:list) {
            for (TagPoint point: tag.getPoints() ) {
                TagBO bo = new TagBO();
                bo.setName(point.getName());
                bo.setDescription(point.getDescription());
                bo.setLiveTime(tag.getLiveTime());
                bo.setLatitude(point.getLatitude());
                bo.setLongitude(point.getLongitude());
                mBOs.add(bo);
            }
        }
        mCallIntegration.notifySuccessListener(mBOs);
    }

    @Override
    public void notifyFailureListener(ERWGeoResponses response) {

        mCallIntegration.notifyFailureListener(response);
    }


    public void cancelTask() {
        if(mTask != null && mTask.getStatus() == Status.RUNNING) {
            mTask.cancel(true);
        }
    }
}
