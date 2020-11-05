import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import control.LineManager;
import model.BeanLine;
import model.BeanPath;
import model.BeanStation;
import util.MyUtil;

public class Starter {
	List<BeanStation> stations = new ArrayList<>(); //存放所有站
	List<BeanStation> transferStations = new ArrayList<>();  //存放换乘站点
	List<BeanLine> lines = new ArrayList<>(); //存放所有线路
	Map<String, BeanStation> nameToStationMap =  new HashMap<>(); //由站名映射站
	Map<Integer, BeanLine> idToLineMap = new HashMap<>(); //由线路id映射线路
	Map<String, BeanLine> nameToLineMap = new HashMap<>(); //由线路名映射线路
	
	//载入地铁线路信息
	public void loadInformation() {
		String pathName = "地铁线路信息.txt";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(pathName),"UTF-8"));
			String string;
			while((string=br.readLine())!=null) {
				String[] a = string.split(" ");
				int lineId = lines.size();
				String lineName = a[0];
				BeanLine line = new BeanLine(lineId, lineName);
				idToLineMap.put(lineId, line);
				nameToLineMap.put(lineName, line);
				for(int i=1;i<a.length;i++) {
					if(nameToStationMap.containsKey(a[i])) {	//查看站点是否曾出现过
						BeanStation s = nameToStationMap.get(a[i]);
						if(!s.isTransferStation()) s.setTransferStation(true);			//如果不是换乘站点就设置成换乘站点
						if(!transferStations.contains(s)) transferStations.add(s);
						s.getOnLine().add(line);
						line.getStationList().add(s);
					}
					else {
						int stationId=stations.size();
						BeanStation station = new BeanStation(stationId, a[i]);
						nameToStationMap.put(a[i], station);
						stations.add(station);
						line.getStationList().add(station);
						station.getOnLine().add(line);
					}
				}
				if(a[1].equals(a[a.length-1])) line.setLoop(true);
				line.createGraph();
				lines.add(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//查询已确定的最短路线中是否已存在需求路线
	public boolean shortestPath(String startStation, String endStation) {
		String pathName = "已确定的最短路径.txt";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(pathName),"UTF-8"));
			String string;
			while((string=br.readLine())!=null) {
				String[] a = string.split(" ");
				if(a[0].equals(startStation)&&a[1].equals(endStation)) {
					System.out.println(string);
					return true;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//显示路径并写入文件
	public void writeFile(BeanPath path) {
		try {
			File file = new File("已确定的最短路径.txt");
			if (!file.exists()) 
			    file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(file,true));
	        System.out.println(path.toString());
	        out.write(path.toString()+"\r\n");
	        out.flush(); // 把缓存区内容压入文件
	        out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Starter starter = new Starter();
		starter.loadInformation();
		for(BeanStation s: starter.transferStations) {
			for(BeanLine l: s.getOnLine()) {
				MyUtil.lineManager.addConnectedLines(s.getOnLine(), l);
			}
		}
		Scanner input = new Scanner(System.in);
		System.out.print("请输入起始站：");
		String begin = input.nextLine().trim();
		System.out.print("请输入终点站：");
		String end = input.nextLine().trim();
		if(!(starter.nameToStationMap.containsKey(begin)&&starter.nameToStationMap.containsKey(end))) {
			System.out.println("站名不存在");
			System.exit(0);
		}
		if(!starter.shortestPath(begin, end)) {
			BeanStation s1=starter.nameToStationMap.get(begin);
			BeanStation s2=starter.nameToStationMap.get(end);
			List<List<BeanLine>> allLines = new ArrayList<>();
			for(BeanLine l1: s1.getOnLine()) {
				for(BeanLine l2: s2.getOnLine()) {
					allLines.addAll(MyUtil.lineManager.LineToLine(l1, l2,23));
				}
			}
			List<BeanPath> allPaths = new ArrayList<>();
			for(List<BeanLine> line: allLines) {
				BeanPath path = new BeanPath(begin, end);
				path.setaList(line);
				path = MyUtil.stationManager.thronghStations(path, starter.nameToStationMap, starter.idToLineMap);
				allPaths.add(path);
			}
			Comparator<BeanPath> com = new MyComparator();
			Collections.sort(allPaths, com);
			if(allPaths.isEmpty()) {
				System.out.println("无可选路线");
			}else {
				starter.writeFile(allPaths.get(0));
				for(int i=1;i<allPaths.size();i++) {
					if(allPaths.get(i).getSumSations()==allPaths.get(0).getSumSations()&&allPaths.get(i).getaList().size()==allPaths.get(0).getaList().size()) {
						starter.writeFile(allPaths.get(i));
					}
				}
			}
		}
			
	}
}
//自定义函数，按所经站点从少到多排序，如相同，则按所经线路从少到多排序
class MyComparator implements Comparator<BeanPath> {

	public int compare(BeanPath p1,BeanPath p2) {
		if(p1.getSumSations()!=p2.getSumSations()) return p2.getSumSations()-p1.getSumSations();
		else {
			return p2.getaList().size()-p1.getaList().size();
		}
	}
}
