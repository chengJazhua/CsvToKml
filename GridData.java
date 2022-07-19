package com.leidos.hats.grids;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridData
{
	private double[] latitudes;
	private double[] longitudes;
	
	private double[][] gridValues;

	public void readFromCSVFile(String csvFilePath)
	{
		File csvFile = new File(csvFilePath);
		
		if(!csvFile.isFile())
		{
			System.err.println("Could not find file " + csvFilePath);
		}
		
		try(FileReader fileReader = new FileReader(csvFile); BufferedReader br = new BufferedReader(fileReader))
		{
			for(int i = 0; i < 3; i++) // Throw away the first three non-data lines
				br.readLine();
			
			List<List<Double>> valueArray = new ArrayList<>(); 
			
			// Read in the longitude values
			String nextline = br.readLine();
			
			String[] splitLongitudes = nextline.split(",");
			
			longitudes = new double[splitLongitudes.length - 1]; // Account for the first token in splitLongitudes being the "LATITUDES" label, not being a longitude value
			
			for(int i = 1; i < splitLongitudes.length; i++)
				longitudes[i - 1] = new Double(splitLongitudes[i]);
			
			nextline = br.readLine();
			
			List<Double> latitudeListArray = new ArrayList<>();
			
			while(nextline != null)
			{				
				String[] splitLatLine = nextline.split(",");
				
				latitudeListArray.add(new Double(splitLatLine[0]));
				
				List<Double> valuesAtLat = new ArrayList<>();
				
				for(int i = 1; i < splitLatLine.length; i++)
				{
					Double value = new Double(splitLatLine[i]);
					valuesAtLat.add(value);
				}
				
				valueArray.add(valuesAtLat);
				
				nextline = br.readLine();
			}
			
			latitudes = new double[latitudeListArray.size()];
			
			for(int i = 0; i < latitudes.length; i++)
			{
				latitudes[i] = latitudeListArray.get(i);
			}
			
			gridValues = new double[latitudes.length][longitudes.length];
			
			for(int i = 0; i < valueArray.size(); i++)
			{
				List<Double> valuesAtLat = valueArray.get(i);
				
				for(int j = 0; j < valuesAtLat.size(); j++)
					gridValues[i][j] = valuesAtLat.get(j); 
			}
		}
		catch (IOException e)
		{
			System.err.println("Error processing CSV grid from " + csvFile.getAbsolutePath() + ": " + e.getLocalizedMessage());			
		}
	}
	
	public double[] getLatitudes()
	{
		return latitudes;
	}

	public void setLatitudes(double[] latitudes)
	{
		this.latitudes = latitudes;
	}

	public double[] getLongitudes()
	{
		return longitudes;
	}

	public void setLongitudes(double[] longitudes)
	{
		this.longitudes = longitudes;
	}

	public double[][] getGridValues()
	{
		return gridValues;
	}

	public void setGridValues(double[][] gridValues)
	{
		this.gridValues = gridValues;
	}
}
