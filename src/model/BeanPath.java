package model;

import java.util.ArrayList;
import java.util.List;

public class BeanPath {
	private String beginStation; //��ʼվ
    private String endStation; //�յ�վ
    private List<BeanStation> aStations = new ArrayList<>(); //����վ��
    private int sumSations;
    private List<BeanLine> aList = new ArrayList<>(); //������·

    public BeanPath() {
    	
    }
    
    public BeanPath(String beginStation, String endStation){
        this.beginStation=beginStation;
        this.endStation=endStation;
    }

    public String getBeginStation() {
        return beginStation;
    }

    public void setBeginStation(String beginStation) {
        this.beginStation = beginStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public List<BeanStation> getaStations() {
        return aStations;
    }

    public void setaStations(List<BeanStation> aStations) {
        this.aStations = aStations;
    }

    public int getSumSations() {
        return sumSations;
    }

    public void setSumSations(int sumSations) {
        this.sumSations = sumSations;
    }

    public List<BeanLine> getaList() {
        return aList;
    }

    public void setaList(List<BeanLine> aList) {
        this.aList = aList;
    }
    
    public String toString() {
    	String string="";
        string+= beginStation+" "+endStation+" ����վ����"+sumSations;
        int stNum=aStations.size()-1;
        for(int i=stNum;i>=0;i--) {
            if(i==stNum) string+="  ·����"+aStations.get(i);
            else {
                string+="-->("+aList.get(i).getLineName()+")-->"+aStations.get(i);
            }
        }
        return string;
	}
}
