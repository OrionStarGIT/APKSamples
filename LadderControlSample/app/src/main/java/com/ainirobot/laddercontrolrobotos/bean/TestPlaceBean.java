package com.ainirobot.laddercontrolrobotos.bean;

/**
 * Data: 2023/5/24 16:13
 * Author: hgx
 * Description: TestPlaceBean
 */
public class TestPlaceBean {

    public TestPlaceBean() {
    }

    public TestPlaceBean(String placeName, int floorIndex) {
        this.placeName = placeName;
        this.floorIndex = floorIndex;
    }

    private String placeName;

    private int floorIndex;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getFloorIndex() {
        return floorIndex;
    }

    public void setFloorIndex(int floorIndex) {
        this.floorIndex = floorIndex;
    }

    @Override
    public String toString() {
        return "{" +
                "placeName='" + placeName + '\'' +
                ", floorIndex=" + floorIndex +
                '}';
    }
}
