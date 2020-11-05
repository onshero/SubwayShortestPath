package control;



import java.util.ArrayList;
import java.util.List;

import model.BeanLine;
import model.BeanStation;

public class LineManager {
	
	//存储相邻线路
	public boolean addConnectedLines(List<BeanLine> lines, BeanLine line) {
		return line.getConnectedLine().addAll(lines);
	}
	
	//查找如何从一条线路到另一条线路
	public List<List<BeanLine>> LineToLine(BeanLine start, BeanLine end, int n){
		List<List<BeanLine>> array = new ArrayList<>();
		if(n==0) return array;
		if(start.getId()==end.getId()) {
			List<BeanLine> aLines = new ArrayList<>();
			aLines.add(start);
			array.add(aLines);
			return array;
		}
		for(BeanLine line: start.getConnectedLine()) {
			if(line!=start) {
				List<List<BeanLine>> l2l = LineToLine(line, end,n-1);
				if(l2l.isEmpty()) continue;
				for(List<BeanLine> aLines: l2l) {
					if(aLines.contains(start)) l2l.remove(aLines);
					else{
						aLines.add(start);
						if(!array.contains(aLines)) array.add(aLines);
					}
				}
			}
		}
		return array;
	}
	
	
}
