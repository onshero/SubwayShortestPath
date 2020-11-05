package control;

import java.util.List;
import java.util.Map;

import org.w3c.dom.ls.LSException;

import model.BeanLine;
import model.BeanPath;
import model.BeanStation;
import util.MyUtil;

public class StationManager {
	
	//判断站点是否已存在线路上
	public boolean isExist(BeanLine line,BeanStation station) {
		return line.getStationList().contains(station);	
	}
	
	//查询站点在线路上的下标
	public int index(BeanLine line, BeanStation station) {
		return line.getStationList().indexOf(station);
	}
	
	//查询两站点在线路上的距离
	public int distanceBetwwenTwoStations(BeanLine line, BeanStation s1, BeanStation s2) {
		int index1=index(line, s1);
		int index2=index(line, s2);
		return line.getGraph()[index1][index2];
	}
	
	//添加站点所在线路
	public boolean addLine(BeanStation station, BeanLine line) {
		station.getOnLine().add(line);
		return true;
	}
	
	//根据路线和起始站、终点站确定所经站点
	public BeanPath thronghStations(BeanPath path,Map<String, BeanStation> nameToStation,Map<Integer, BeanLine> idToLine){
		if(path.getBeginStation().equals(path.getEndStation())) {
			path.getaStations().add(nameToStation.get(path.getEndStation()));
			path.setSumSations(0);
			return path;
		}
		else if (path.getaList().size()==1) {
			int l=distanceBetwwenTwoStations(path.getaList().get(0), nameToStation.get(path.getBeginStation()), nameToStation.get(path.getEndStation()));
			path.getaStations().add(nameToStation.get(path.getEndStation()));
			path.getaStations().add(nameToStation.get(path.getBeginStation()));
			path.setSumSations(l);
			return path;
		}
		else {
			BeanStation nowStation= nameToStation.get(path.getBeginStation());
			List<BeanLine> list = path.getaList().subList(0, path.getaList().size()-1);
			for(BeanStation station: path.getaList().get(path.getaList().size()-1).getStationList()) {
				if(!station.getStationName().equals(path.getBeginStation())&&station.isTransferStation()) {
					if(station.getOnLine().contains(list.get(list.size()-1))) {
						BeanPath newPath = new BeanPath(station.getStationName(), path.getEndStation());
						newPath.setaList(list);
						BeanPath newPath2  = thronghStations(newPath, nameToStation, idToLine);
						int l=distanceBetwwenTwoStations(path.getaList().get(path.getaList().size()-1), nameToStation.get(path.getBeginStation()), station);
						if(path.getaStations().isEmpty()) {
							path.setaStations(newPath2.getaStations());
							path.getaStations().add(nowStation);
							path.setSumSations(newPath2.getSumSations()+l);
						}else if(l+newPath2.getSumSations()<path.getSumSations()) {
							path.setaStations(newPath2.getaStations());
							path.getaStations().add(nowStation);
							path.setSumSations(newPath2.getSumSations()+l);
						}
					}
				}
			}
			return path;
		}
	}
}
