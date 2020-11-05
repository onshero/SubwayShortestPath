package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanLine {
    private int id; //线路id
    private String LineName; //线路名
    private boolean isLoop; //是否是回环线路
    private List<BeanStation> stationList = new ArrayList<>(); //所经站点
    private Set<BeanLine> connectedLine = new HashSet<>(); //相连线路
    private int[][] Graph; //线路内站到站的最短距离
    public BeanLine(){
        isLoop=false;
    }
    public BeanLine(int id, String name){
        this();
        this.id=id;
        this.LineName=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public List<BeanStation> getStationList() {
        return stationList;
    }

    public void setStationList(List<BeanStation> stationList) {
        this.stationList = stationList;
    }

    public Set<BeanLine> getConnectedLine() {
        return connectedLine;
    }

    public void setConnectedLine(Set<BeanLine> connectedLine) {
        this.connectedLine = connectedLine;
    }

    public int[][] getGraph() {
        return Graph;
    }

    public void setGraph(int[][] graph) {
    	Graph = new int[stationList.size()][stationList.size()];
        Graph = graph;
    }

    public String tosString() {
    	return LineName;
    }
    
  //创建图
  	public void createGraph(){
          int length = stationList.size();
          Graph = new int[length][length];
          for(int i=0;i<length;i++){                                      //初始化图
              for(int j=0;j<=i;j++){
                  Graph[i][j]=Graph[j][i]=i-j;
              }
          }
          if(isLoop==true){                                        //当线路为环路时要修改站与站的距离
              Graph[0][length-1]=Graph[length-1][0]=1;
              for(int i=0;i<length;i++){
                  for(int k=0;k<length;k++){
                      for(int j=0;j<length;j++){
                          if(i!=j&&i!=k&&j!=k){
                              if(Graph[i][j]>Graph[i][k]+Graph[j][k])
                                  Graph[i][j]=Graph[j][i]=Graph[i][k]+Graph[j][k];
                          }
                      }
                  }
              }
          }
      }
}

