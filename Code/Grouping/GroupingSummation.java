
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;

public class GroupingSummation
{

	// Hashtable for the index of OTUs in X file.
	static public Hashtable<String, Integer> OTUHashtable = new Hashtable<String, Integer>(); 
	
	//Hashtable for groups
	static public Hashtable<String, String> GroupHashtable = new Hashtable<String, String>(); 
	static public ArrayList<SmartScanGrouping> Groups = new ArrayList<SmartScanGrouping>();
	static public ArrayList<ArrayList<Double>> FinalData = new ArrayList<ArrayList<Double>>();
	static public ArrayList<String> classType = new ArrayList<String>();
    static public String SMARTscan = "";
    static public String Xfile = "";
    static public String Yfile = "";
	
	
	public static void main(String args[]) 
	{
		ArrayList<String> BrnchesArrayList = new ArrayList<String>();
        SMARTscan = args[1];
        Xfile = args[2];
        Yfile = args[3];

		applyGrouping(args[0]);
        
		
    }

	public static void FillOTUHashtable(ArrayList<String> OTUNames)
	{
		for(int i=0; i< OTUNames.size() ; i++)
		{
			OTUHashtable.put(OTUNames.get(i), i);
		}

	}
	

	
	public static void applyGrouping(String OutputFileName)
	{
		ArrayList<ArrayList<String>> XArray = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<Double>> FloatXarray = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<String>> GroupArray = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> YArray = new ArrayList<ArrayList<String>>();
		
		// Read Y file
		classType = ReadData2(Yfile);
		classType.remove(0);
		
		// Read the X file
		XArray = ReadData(Xfile);
		FillOTUHashtable(XArray.get(0));
		
		//Read Group Info
		GroupArray = ReadData(SMARTscan);
		FillGroupHashtable(GroupArray);
		FillGroupInfo(GroupArray);
		FloatXarray = FillFloatXarray(XArray);
		
		FinalData = SumValueOfGroupMember(FloatXarray);
		String stream = WriteFinalFile( );
		WriteFile(stream,  "../Output/" +OutputFileName );
				
	}
	
	
	public static String WriteFinalFile( )
	{
		String stream = "";
		
		for(int i=0; i< FinalData.size() ; i++ )
		{
			 for(int j=0; j < FinalData.get(i).size() ;j++ )
			 {
				 stream = stream + FinalData.get(i).get(j) +",";
			 }
			 if (classType.get(i).equals("0") )
			 {
				 stream = stream + "NONE";
			 }
			 else
			 {
				 stream = stream + "HIGH";
			 }
			 stream = stream + "\n";
		}
		return stream;
		
	}
	
	
	public static String WriteFinalFile2( ArrayList<ArrayList<String>> FinalData2)
	{
		String stream = "";
		
		for(int i=0; i< FinalData2.size() ; i++ )
		{
			if(i == 164)
			{
				int a = 0;
			}
			 for(int j=0; j < FinalData2.get(i).size() ;j++ )
			 {
				 stream = stream + FinalData2.get(i).get(j) +",";
			 }
			 if (classType.get(i).equals("0") )
			 {
				 stream = stream + "NONE";
			 }
			 else
			 {
				 stream = stream + "HIGH";
			 }
			 stream = stream + "\n";
		}
		return stream;
		
	}
	
	
	public static ArrayList<ArrayList<Double>> FillFloatXarray(ArrayList<ArrayList<String>> Data)
	{
		ArrayList<ArrayList<Double>> FloatXarray = new ArrayList<ArrayList<Double>>();
		
		for(int i=1; i < Data.size() ; i++)
		{
			ArrayList<Double> temp= new ArrayList<Double>();
			for ( int j=0; j< Data.get(i).size() ; j++)
			{
				
				Double abundance = Double.parseDouble(Data.get(i).get(j));
				
				temp.add(abundance);
				
			}
			FloatXarray.add(temp);
		}
		
		return FloatXarray;
	}
	
	public static ArrayList<ArrayList<Double>>  SumValueOfGroupMember(ArrayList<ArrayList<Double>> Data)
	{
		ArrayList<ArrayList<Double>> XData = new ArrayList<ArrayList<Double>>();
		
		for (int i=0; i < Data.size() ; i++)
		{
			ArrayList<Double> temp = new ArrayList<Double>();
			
			for (int j=0; j< Groups.size(); j++)
			{
				Double sum = 0.0;
				for(int k=0; k< Groups.get(j).OTUMemberIndex.size(); k++)
				{
					sum = sum + Data.get(i).get(Groups.get(j).OTUMemberIndex.get(k));
					
				}
				temp.add(sum);
			}
			XData.add(temp);
		}
		return XData;
	}
	
	public static void FillGroupInfo(ArrayList<ArrayList<String>> GroupData)
	{
		Hashtable<String, String> TempHashTable = new Hashtable<String, String>(); 
		for(int i=1; i< GroupData.size() ; i++)
		{
			
			String Key = GroupData.get(i).get(0);
			
			if (!TempHashTable.containsKey(Key))
			{
				//Fill group data
				TempHashTable.put(Key, "");
				int a=0;
				SmartScanGrouping tempGroup = new SmartScanGrouping();
				tempGroup.GroupName = Key;
				String[] MemberName = GroupHashtable.get(Key).split(",");
				
				for (int j =0; j < MemberName.length ; j++)
				{
					tempGroup.OTUMemberNAme.add(MemberName[j]);
					Integer  tempIndex= OTUHashtable.get(MemberName[j]);
					tempGroup.OTUMemberIndex.add(tempIndex);
					
				}
				Groups.add(tempGroup);
				
			}
			
		}
		
	}
	
	public static void FillGroupHashtable(ArrayList<ArrayList<String>> GroupData)
	{
		for(int i=1; i< GroupData.size(); i++ )
		{
			String Key = GroupData.get(i).get(0);
			
			if(GroupHashtable.containsKey(Key))
			{
				String NewStream = GroupHashtable.get(Key) +"," + GroupData.get(i).get(1);
				GroupHashtable.put(Key, NewStream);
			}
			else
			{
				GroupHashtable.put(GroupData.get(i).get(0), GroupData.get(i).get(1));
			}
		}
	}
	
	public static void WriteFile(String Stream,  String OutputFileName)
	{
		try 
		{
	          //Writing Variables
	          FileWriter fileWriter = new FileWriter(OutputFileName);
	          BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	          bufferedWriter.write(Stream );
	          bufferedWriter.close();

	    }
	    catch(Exception e)
	    {
	          System.out.println("Error while readinghile reading  file line by line3:" + e.getMessage() + OutputFileName);
	    }
		
	}
	
	public static  ArrayList<ArrayList<String>> ReadData(String fileName)
	{
		
		//reading data
		ArrayList<ArrayList<String>> Data = new ArrayList<ArrayList<String>>();
		
		try
		{
			FileReader inputFile = new FileReader(fileName);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			String line;
			
			while ((line = bufferReader.readLine()) != null)  
			{
				ArrayList<String> Temporaryvalues = new ArrayList<String>();
				String[] stream = line.split(",");
				
				for (int i=0; i< stream.length; i++)
				{
					Temporaryvalues.add(stream[i]);
									}
				Data.add(Temporaryvalues);
				
			}
			bufferReader.close();

		}
			       
		catch(Exception e)
		{
			System.out.println("Error while reading file line by line1:" + e.getMessage() +fileName);
		}
				
		return 	Data;	
		
	}
	
	
	public static  ArrayList<String> ReadData2(String fileName)
	{
		
		//reading data
		ArrayList<String> Data = new ArrayList<String>();
		
		try
		{
			FileReader inputFile = new FileReader(fileName);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			String line;
			
			while ((line = bufferReader.readLine()) != null)  
			{
				
				Data.add(line);
				
			}
			bufferReader.close();
		}
			       
		catch(Exception e)
		{
			System.out.println(fileName  +" Error while reading file line by line2:" + e.getMessage() );
		}
				
		return 	Data;	
		
	}
	
	
	public static String prepareLineOfCSV(String[] LineComponents)
	{
		String CSVFormatOfLine=LineComponents[0];
		
		for(int i=1; i<LineComponents.length; i++)
		{
			CSVFormatOfLine =  CSVFormatOfLine +","+ LineComponents[i];
		}
		
		return CSVFormatOfLine ;
		
	}
}


