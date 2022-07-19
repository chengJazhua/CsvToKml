
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;


public class CSVToKMLConverter
{
	// TODO - Point to appropriate file location	

	private ArrayList<Point2D.Double> point1 = new ArrayList<Point2D.Double>();
	private ArrayList<Point2D.Double> point2 = new ArrayList<Point2D.Double>();
	private ArrayList<Point2D.Double> point3 = new ArrayList<Point2D.Double>();
	private ArrayList<Point2D.Double> point4 = new ArrayList<Point2D.Double>();
	private ArrayList<Point2D.Double> point5 = new ArrayList<Point2D.Double>();
	private ArrayList<Point2D.Double> point6 = new ArrayList<Point2D.Double>();


	private String InputFile;
	private String date;
	private int timeMin;
	private int timeHour;
	private double[] values;
	
	public CSVToKMLConverter() {
		InputFile = "";
		timeMin = 0;
	}
	
	public CSVToKMLConverter(String input, int timeInMin, String date, double[] conValues) {
		InputFile = input;
		timeMin = timeInMin%60;
		timeHour = timeInMin/60;
		if (timeHour >= 24) {
			timeHour = timeHour%24;
		}
		this.date = date;
		values = conValues;
	}
	public String create()
	{
		
		GridData gridData = new GridData();
		
		gridData.readFromCSVFile(InputFile);
		
		double[] lat = gridData.getLatitudes();
		double[] longs = gridData.getLongitudes();
		double[][] data = gridData.getGridValues();
		sortData(data, longs, lat);
		String file = "";
		
		String lookAt = "<LookAt>\n" + "<longitude>\n" +    longs[longs.length/2] + "</longitude>\n" +
				"<latitude>\n" +   lat[lat.length/2] + "</latitude>\n" +
			      "<altitude>\n" + "0" + "</altitude>\n"  + "<range>\n 10000 </range>\n" + "<altitudeMode>\n relativeToGround </altitudeMode>\n" +  "</LookAt>\n" ;
		
		file += lookAt;
		
		for (int i = 0; i < longs.length; i++) {
			for (int j = 0; j < lat.length; j++) {
							}
		}
		
		for (int i = 5; i >= 0; i--) {
			
			if (i == 0 && point1.size() > 1) {
				file += startPoly();
				point1 = QuickHull.quickHull(point1);
				quickSort(point1, 0, point1.size()-1, point1.get(0));
				quickSort(point1, 0, point1.size()-1, point1.get(1));
				file += addCoords(point1);
				file += endPoly("#ddff2d00");

			}
			else if (i == 1 && point2.size() > 1) {
				file += startPoly();
				point2 = QuickHull.quickHull(point2);
				quickSort(point2, 0, point2.size()-1, point2.get(0));
				quickSort(point2, 0, point2.size()-1, point2.get(1));
				file += addCoords(point2);
				file += endPoly("#ddf9b903");
			}
			else if (i == 2 && point3.size() > 1) {
								file += startPoly();
				point3 = QuickHull.quickHull(point3);
				quickSort(point3, 0, point3.size()-1, point3.get(0));
				quickSort(point3, 0, point3.size()-1, point3.get(1));
				file += addCoords(point3);
				file += endPoly("#ddf1f50d");
				
			}
			else if (i == 3 && point4.size() > 1) {
							file += startPoly();
				point4 = QuickHull.quickHull(point4);
				quickSort(point4, 0, point4.size()-1, point4.get(0));
				quickSort(point4, 0, point4.size()-1, point4.get(1));
				file += addCoords(point4);
				file += endPoly("#ddc7f50d");
				
			}
			else if (i == 4 && point5.size() > 1) {
			
				file += startPoly();
				point5 = QuickHull.quickHull(point5);
				quickSort(point5, 0, point5.size()-1, point5.get(0));
				quickSort(point5, 0, point5.size()-1, point5.get(1));
				file += addCoords(point5);
				file += endPoly("#dd7af50d");
				
			}
			else {
				if (point6.size() > 1) {
										
					file += startPoly();
					point6 = QuickHull.quickHull(point6);
					quickSort(point6, 0, point6.size()-1, point6.get(0));
					quickSort(point6, 0, point6.size()-1, point6.get(1));
					file += addCoords(point6);
					file += endPoly("#dd0000ff");
					
				}
				
			}
				
			
		}
		

		
		return file;
		
	}
	
	public String createLines(String color, ArrayList<Point2D.Double> points) {
		String lines = "<Placemark>\n" + "<visibility>1</visibility>\n" + "<Style>\n" + "<geomColor>\n " + color
				+ " </geomColor>\n" + "</Style>\n" + "<LineString>\n" +"<coordinates>\n";
		lines += addCoords(points);
		lines += "</coordinates>\n" + "</LineString>\n" + timeStamp() + "</Placemark>\n";
		return lines;
	}
	
	public String startPoly() {
		String polyStart = "<Placemark>\n" + "<Polygon>\n"
				+ "<altitudeMode> clampedToGround </altitudeMode>\n" + "<outerBoundaryIs>\n" + "<LinearRing>\n" + "<coordinates>\n";
		return polyStart;
	}
	
	public String endPoly(String color) {
		
		String polyEnd = "</coordinates>\n" + "</LinearRing>\n" + "</outerBoundaryIs>\n"
				+ "</Polygon>\n"  ;
		String polyStyle = "<Style>\n" + "<PolyStyle>\n" + "<color>" + color + "</color>" 
				+ "<outline> 0 </outline>\n" +"<fill>1</fill>" +  "</PolyStyle>\n" + "</Style>\n";
		String timeStamp = timeStamp();
		return  polyEnd + polyStyle +  timeStamp + "</Placemark>\n";
	}
	
	public String dataColor(double data) {
		
		String color = "";
		if (data > values[0]) 
			color = "#ddff2d00";
		
		else if (data > values[1]) 
			color = "#ddf9b903";
			
		else if (data > values[2]) 
			color = "#ddf1f50d";
			
		else if (data > values[3]) 
			color = "#ddc7f50d";
			
		else if (data > values[4])
			color = "#dd7af50d"; 
		else if (data > values[5])
			color = "dd0000ff";
		
		return color;
			
		
	}
	
	
	public String ScreenOverlay() {
		String screen = "<ScreenOverlay>\n" + "<Icon>\n" + "<href>\n" + "C:\\Users\\chengj\\Documents\\testpic.png"
				+ "</href>\n" + "</Icon>\n";
		screen += "<overlayXY x=\"0\" y=\"0.1\" xunits=\"fraction\" yunits=\"fraction\"/>\n" + 
				"<screenXY x=\"0\" y=\"0.1\" xunits=\"fraction\" yunits=\"fraction\"/>\n";
		screen += "<size x=\"100\" y=\"100\" xunits=\"pixels\" yunits=\"pixels\"/>\n" + "</ScreenOverlay>\n";
		return screen;
	}
	
	public String addCoords(Point2D.Double[] points) {
		String coords = "";
		if (points.length != 0) {
			for (int i = 0; i < points.length; i++) {	
				String[] cut = points[i].toString().split("\\[");
				String[] cutAgain = cut[1].split(" ");
				coords += cutAgain[0] + cutAgain[1].substring(0, cutAgain[1].length()-1) + " ";
				
			}
			String[] cut = points[0].toString().split("\\[");
			String[] cutAgain = cut[1].split(" ");
			coords += cutAgain[0] + cutAgain[1].substring(0, cutAgain[1].length()-1) + " ";
		}
		return coords;
	}
	
	public String addCoords(ArrayList<Point2D.Double> points) {
		if (!points.isEmpty()) {
			String coords = "";
		
		for (int i = 0; i < points.size(); i++) {
	
			String[] cut = points.get(i).toString().split("\\[");
			String[] cutAgain = cut[1].split(" ");
			coords += cutAgain[0] + cutAgain[1].substring(0, cutAgain[1].length()-1) + " ";
			
		}
		String[] cut = points.get(0).toString().split("\\[");
		String[] cutAgain = cut[1].split(" ");
		coords += cutAgain[0] + cutAgain[1].substring(0, cutAgain[1].length()-1) + " ";
		return coords;
		}
		return "";
	}
	
	public String timeStamp() {
		String imageTime = "<TimeStamp>\n" + "<when>" + date;
		if (timeHour < 10)
			imageTime += "0";
		
		imageTime += timeHour + ":";
		if (timeMin < 10)
			imageTime += "0";
		imageTime += timeMin + ":00Z" + "</when>\n" + "</TimeStamp>\n";
		return imageTime;
	}
	
	public void sortData(double[][] data, double[] longs, double[] lat) {
		for (int i = 0; i < data[0].length; i++) {
			for (int j = 0; j < data.length; j++) {
				if (data[j][i] > values[0]) 
					point1.add(new Point2D.Double(longs[i], lat[j]));
				else if (data[j][i] > values[1]) 
					point2.add(new Point2D.Double(longs[i], lat[j]));

				else if (data[j][i] > values[2]) 
					point3.add(new Point2D.Double(longs[i], lat[j]));

				else if (data[j][i] > values[3]) 
					point4.add(new Point2D.Double(longs[i], lat[j]));

				else if (data[j][i] > values[4])
					point5.add(new Point2D.Double(longs[i], lat[j]));
				else if (data[j][i] > values[5])
					point6.add(new Point2D.Double(longs[i], lat[j]));
			}
		}
		
	}
	
		
		
	public boolean pointListContains(ArrayList<Point2D.Double> points, Point2D.Double point ) {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).toString().equals(point.toString()))
				return true;
		}
		return false;
	}
	
	public String locPoint(double[] longs, double[] lat, Point2D.Double point) {
		String[] cut = point.toString().split("\\[");
		String[] cutAgain = cut[1].split(" ");
		String loc = "";
		double x = java.lang.Double.parseDouble(cutAgain[0].substring(0, cutAgain[0].length()-1));
		double y = java.lang.Double.parseDouble(cutAgain[1].substring(0, cutAgain[1].length()-1));
		for (int i = 0; i < longs.length; i++) {
			if (longs[i] == x)
				loc += i;
		}
		for (int i = 0; i < lat.length; i++) {
			if (lat[i] == y)
				loc += " " + i;
		}
		return loc;
	}
	
	
	
	public Point2D.Double findCenter(List<Point2D.Double> points){
		int x = 0;
		int y = 0;
		for (Point2D.Double point : points) {
			x += point.x;
			y += point.y;
		}
		
		Point2D.Double center = new Point2D.Double(0, 0);
		center.x = x / points.size();
		center.y = y / points.size();
		return center;
	}
	
		public List<Point2D.Double> sortPoints(List<Point2D.Double> points ){
		
		return points;
	}

	 
	public void swap(List<Point2D.Double> points, int i, int j)
	{
	    Point2D.Double temp = points.get(i);
	    points.set(i, points.get(j));
	    points.set(j, temp);
	}
	
    public int partition(List<Point2D.Double> points, int low, int high, Point2D.Double center)
	{
	    

	    Point2D.Double pivot = points.get(high);
	    
	    int i = (low - 1);
	 
	    for(int j = low; j <= high - 1; j++)
	    {
	         

	        if (comparePoints(points.get(j), pivot, center) < 0)
	        {
	             
	            i++;
	            swap(points, i, j);
	        }
	    }
	    swap(points, i + 1, high);
	    return (i + 1);
	}
    
    void quickSort(List<Point2D.Double> points, int low, int high, Point2D.Double center)
    {
        if (low < high)
        {
             

            int pi = partition(points, low, high, center);
            quickSort(points, low, pi - 1, center);
            quickSort(points, pi + 1, high, center);
        }
    }
	
	public int comparePoints(Point2D.Double coor1, Point2D.Double coor2, Point2D.Double compPoint) {
		double a1 = (Math.toDegrees(Math.atan2(coor1.x - compPoint.x, coor1.y - compPoint.y)) + 360) % 360;
        double a2 = (Math.toDegrees(Math.atan2(coor2.x - compPoint.x, coor2.y - compPoint.y)) + 360) % 360;
        return (int) (a2 - a1);
	}
	
}

