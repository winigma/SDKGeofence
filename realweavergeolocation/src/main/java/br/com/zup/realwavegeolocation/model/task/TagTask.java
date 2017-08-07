package br.com.zup.realwavegeolocation.model.task;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.net.MalformedURLException;
import java.util.List;

import br.com.zup.realwavegeolocation.connectorcallback.interfaces.ITagConnectorCallback;
import br.com.zup.realwavegeolocation.model.ERWGMesages;
import br.com.zup.realwavegeolocation.model.Tag;
import br.com.zup.realwavegeolocation.model.TagBO;
import br.com.zup.realwavegeolocation.model.conn.TagConn;
import br.com.zup.realwavegeolocation.model.enums.ERWGeoResponses;
import br.com.zup.realwavegeolocation.model.exceptions.NetworkStatusException;
import br.com.zup.realwavegeolocation.model.params.TagParam;

/**
 * Created by wisle on 02/08/2017.
 */

public class TagTask extends AbstractAsyncTask<TagParam>{


    private List<Tag> tags;
    private ERWGMesages mErrorMsg= null;
    private String mResultJson = null;
    private Gson mGson = null;
    private ITagConnectorCallback mCallback;



    public TagTask(final ITagConnectorCallback listener) {
       this.mCallback = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mCallback.notifyStartListener();
    }

    @Override
    protected Boolean doInBackground(TagParam... params) {

        if(!isCancelled()) {
            final TagConn conn =  new TagConn(params[0]);
            this.mGson = new Gson();

            try {
                conn.createUrl();
                conn.execute();

                mResultJson = conn.getDataTag();

                tags = mGson.fromJson(mResultJson, new TypeToken<List<Tag>>(){}.getType());

            } catch (MalformedURLException e) {
                mErrorMsg = new ERWGMesages();
                mErrorMsg.setError(ERWGeoResponses.M01.getCode());

                e.printStackTrace();
            } catch (NetworkStatusException e) {
                mErrorMsg = new ERWGMesages();
                mErrorMsg.setError(e.getResponse().getCode());
                e.printStackTrace();
            }catch (JsonSyntaxException e){
                mErrorMsg = mGson.fromJson(mResultJson, ERWGMesages.class);
            }
        }
        return (mErrorMsg != null);
    }

    @Override
    protected void onPostExecute(Boolean hasError) {
        super.onPostExecute(hasError);
        if(!isCancelled()) {

            if(hasError) {
                mCallback.notifyFailureListener(ERWGeoResponses.getErrorResponse(mErrorMsg));
            }else{
                mCallback.notifySuccessListener(this.tags);
            }

        }
    }
}
