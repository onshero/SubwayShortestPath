package model;

import java.util.ArrayList;
import java.util.List;

public class BeanStation {
    private int id; //站点id
    private String StationName; //站点名
    private boolean isTransferStation; //是否是换乘站点
    private List<BeanLine> onLine = new ArrayList<>(); //所在线路
    public BeanStation(){
        isTransferStation=false;
    }

    public BeanStation(int id, String name){
        this();
        this.id=id;
        this.StationName=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public boolean isTransferStation() {
        return isTransferStation;
    }

    public void setTransferStation(boolean isTransferStation) {
        this.isTransferStation = isTransferStation;
    }

    public List<BeanLine> getOnLine() {
        return onLine;
    }

    public void setOnLine(List<BeanLine> onLine) {
        this.onLine = onLine;
    }

    public String toString(){
        return StationName;
    }
}
