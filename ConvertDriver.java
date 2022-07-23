

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
public class ConvertDriver {
	
	public static void main(String[] args) {
		
		File test = new File(""); // add file path to where kml file is to be outputted
		FileWriter fw = null;
		try {
			fw = new FileWriter(test);
		} catch (IOException e) {
			e.printStackTrace();
		}
        BufferedWriter bw = new BufferedWriter(fw);
        
        String content = "";
        String startKML = "<?xml version= \"1.0\" encoding = \"UTF-8\"?>\n" + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">";
        content += startKML + "<Document>\n" ;
        Scanner sc = new Scanner(System.in);
        double[] values = new double[6];
        for (int i = 0; i < 6; i++) {
        	System.out.println("Input value for contour " + (i+1) + ":");
        	while (values[i] <= 0 ) {
        		try {
        			double input = Double.parseDouble(sc.next());
        			values[i] = input; 
        			if (input <= 0)
        				System.out.println("Please input a positive double: ");
        		}	 	
        		catch (NumberFormatException ignore) {
                	System.out.println("Invalid input, try again:");
        		}
        	}
        }
        File folder = new File(""); // add file path to folder of csv files here
        File[] listOfFiles = folder.listFiles();
        int count = 0;
        int timeStart = 12*60;
        for (File file : listOfFiles) {
            if (file.isFile()) {
            	System.out.println(file.getAbsolutePath());
            	count++;
            	CSVToKMLConverter testing = new CSVToKMLConverter(file.getAbsolutePath(), timeStart + readTime(file.getName()), readDate(file.getName()), values);
            	if (count == 1)
            		content += testing.ScreenOverlay();
            	content += testing.create();
            	
            }
        }
		
		
		content += "</Document>\n" + "</kml>";
		
        try {
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readDate(String file) {
		String date = "";
		String[] cut = file.split("-");
		cut[2] = cut[2].substring(0, 2);
		date += "20" + cut[2] + "-";
		switch(cut[1]) {
		case "Jan": date += "01";
		break;
		case "Feb": date += "02";
		break;
		case "Mar": date += "03";
		break;
		case "Apr": date += "04";
		break;
		case "May": date += "05";
		break;
		case "Jun": date += "06";
		break;
		case "Jul": date += "07";
		break;
		case "Aug": date += "08";
		break;
		case "Sep": date += "09";
		break;
		case "Oct": date += "10";
		break;
		case "Nov": date += "11";
		break;
		case "Dec": date += "12";
		break;
		
		

		}
		date += "-" + cut[0] + "T";
		return date;
	}
	public static int readTime(String file) {
		double min = 0;
		String[] cut = file.split("\\(");
		String time = cut[1];
		String[] cutAgain = time.split(" ");
		if (cutAgain[1].contains("min"))
			min = Double.parseDouble(cutAgain[0]);
		else if (cutAgain[1].contains("hr"))
			min = Double.parseDouble(cutAgain[0])*60;
		else 
			min = Double.parseDouble(cutAgain[0])*60*24;

		return (int) min;
	}

}
