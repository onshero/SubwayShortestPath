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
	List<BeanStation> stations = new ArrayList<>(); //�������վ
	List<BeanStation> transferStations = new ArrayList<>();  //��Ż���վ��
	List<BeanLine> lines = new ArrayList<>(); //���������·
	Map<String, BeanStation> nameToStationMap =  new HashMap<>(); //��վ��ӳ��վ
	Map<Integer, BeanLine> idToLineMap = new HashMap<>(); //����·idӳ����·
	Map<String, BeanLine> nameToLineMap = new HashMap<>(); //����·��ӳ����·
	
	//���������·��Ϣ
	public void loadInformation() {
		String pathName = "������·��Ϣ.txt";
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
					if(nameToStationMap.containsKey(a[i])) {	//�鿴վ���Ƿ������ֹ�
						BeanStation s = nameToStationMap.get(a[i]);
						if(!s.isTransferStation()) s.setTransferStation(true);			//������ǻ���վ������óɻ���վ��
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
	
	//��ѯ��ȷ�������·�����Ƿ��Ѵ�������·��
	public boolean shortestPath(String startStation, String endStation) {
		String pathName = "��ȷ�������·��.txt";
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
	
	//��ʾ·����д���ļ�
	public void writeFile(BeanPath path) {
		try {
			File file = new File("��ȷ�������·��.txt");
			if (!file.exists()) 
			    file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(file,true));
	        System.out.println(path.toString());
	        out.write(path.toString()+"\r\n");
	        out.flush(); // �ѻ���������ѹ���ļ�
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
		System.out.print("��������ʼվ��");
		String begin = input.nextLine().trim();
		System.out.print("�������յ�վ��");
		String end = input.nextLine().trim();
		if(!(starter.nameToStationMap.containsKey(begin)&&starter.nameToStationMap.containsKey(end))) {
			System.out.println("վ��������");
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
				System.out.println("�޿�ѡ·��");
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
//�Զ��庯����������վ����ٵ�����������ͬ����������·���ٵ�������
class MyComparator implements Comparator<BeanPath> {

	public int compare(BeanPath p1,BeanPath p2) {
		if(p1.getSumSations()!=p2.getSumSations()) return p2.getSumSations()-p1.getSumSations();
		else {
			return p2.getaList().size()-p1.getaList().size();
		}
	}
}
