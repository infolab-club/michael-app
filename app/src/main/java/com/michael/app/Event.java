package com.michael.app;

public class Event {
    public int id;
    public int type; // 0 - холодная вода, 1 - горячая, 2 - дтп, 3 - пожар
    public String time;
    public String district;
    public double lat;
    public double lon;
    public int idAddress;
    public int idBuilding;
    public int danger = 0;

    public Event(int id, int type, String time, String district, double lat, double lon, int idAddress, int idBuilding) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.district = district;
        this.lat = lat;
        this.lon = lon;
        this.idAddress = idAddress;
        this.idBuilding = idBuilding;
    }
}
