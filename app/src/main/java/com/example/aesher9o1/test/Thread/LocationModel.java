package com.example.aesher9o1.test.Thread;

public class LocationModel {
    private String location;
    private String address;
    private Double lat;
    private Double lng;

    LocationModel(String location,String address, Double lat, Double lng){
        this.lat = lat;
        this.lng= lng;
        this.location = location;
        this.address= address;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }
}
