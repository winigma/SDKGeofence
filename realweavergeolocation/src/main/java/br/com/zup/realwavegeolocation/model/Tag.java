package br.com.zup.realwavegeolocation.model;

import java.io.Serializable;
import java.util.List;

/**
 * Class lod data of point of interest in GeoFence
 */

public class Tag implements Serializable {

    private String name;

    private int liveTime;

    private List<TagPoint> points;


    public Tag(String name, int liveTime, List<TagPoint> points) {
        this.name = name;
        this.liveTime = liveTime;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(int liveTime) {
        this.liveTime = liveTime;
    }

    public List<TagPoint> getPoints() {
        return points;
    }

    public void setPoints(List<TagPoint> points) {
        this.points = points;
    }
}
