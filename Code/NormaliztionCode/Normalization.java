import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Normalization
{

	/* In this class we should create three files: X.csv, Y.csv and tree.csv*/
	public static String TaxtaList="";
    public static String TreeBranchSeparator = "_._";
    public static String InputFileAddress;
    public static String DataMapperaddress;
    
	
	public static void main(String args[]) 
	{
        SetFileAddress(args);
		prepareTreeFile();
		prepareXandYFile(InputFileAddress );

    }
	
    public static void SetFileAddress(String args[])
    {
        /* Set File adresses. */
        InputFileAddress = args[0];
        DataMapperaddress = args[1];
    }
	
	public static void prepareTreeFile()
	{
        /* In this Method we prepare the tree.csv which is the input of the SMART-scan toolkit.*/
		ArrayList<String> BrnchesArrayList = new ArrayList<String>();// ArrayList for saving content of the DataMaper.
		BrnchesArrayList = ReadData(DataMapperaddress);// Reading DataMapper
		
		String Tree = "Kingdom,Phylum,Class,Order,Family,Genus";// The header of the tree.csv file.
        
        // Preparing the CSV format of the tree.
		for (int i=0; i< BrnchesArrayList.size() ; i++)
		{
			
			String[] parts1 = BrnchesArrayList.get(i).split("\t");
			String[] parts2 = parts1[1].split(TreeBranchSeparator);
			
			if(i < BrnchesArrayList.size()-1)
			{
				TaxtaList = TaxtaList +  parts2[parts2.length-1] + ","; 
			}
			else if(i == BrnchesArrayList.size()-1)
			{
				TaxtaList = TaxtaList +  parts2[parts2.length-1] ; 
			}
			
			String Line= prepareLineOfCSV(parts2);
			Tree = PrepareCSV( Tree, Line);
			
		}
        // Writing file in tree.csv.
		WriteFile(Tree,  "../Output/tree.csv");
	}
	
	
	public static void WriteFile(String Stream,  String OutputFileName)
	{
        // A method for writing Stream to OutputFileName.
		try 
		{
	          FileWriter fileWriter = new FileWriter(OutputFileName);
	          BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	          bufferedWriter.write(Stream );
	          bufferedWriter.close();

	    }
	    catch(Exception e)
	    {
	          System.out.println("Error while reading file line by line:" + e.getMessage());                      
	    }
		
	}
	
	public static void prepareXandYFile(String filename)
	{
        /* In this method, We normalize the data and extract X.csv and Y.csv as, inputs of the smart-scan. */
		ArrayList<String> BrnchesArrayList = new ArrayList<String>();
        ArrayList<ArrayList<Double>> StringTaxaAbundance = new ArrayList<ArrayList<Double>>();
        
		BrnchesArrayList = ReadData( filename);   // Read the helminth data file.
        ExtractYFile(BrnchesArrayList, filename); // Extract and write Y.csv.
        StringTaxaAbundance = ConvertTaxaValuesToRelativeAbundance(BrnchesArrayList); //Apply the data normalization for each sample (convert abundance to relative abundance).
        String XFile = ConvertArrayListFromDoubleToString(StringTaxaAbundance);  //Convert ArrayList from double to String
        WriteFile(XFile,  "../Output/X.csv"); // Write the X.csv file.
		
	}
	
	
	private static void ExtractYFile(ArrayList<String> BrnchesArrayList, String fileName) 
	{
		String YFile="x";
		
		for (int i=1; i < BrnchesArrayList.size(); i++ )
		{
			String[] splittedComponents = BrnchesArrayList.get(i).split(",");
			if(splittedComponents.length>1)
			{
				YFile = YFile + "\n" +  MappingClass(splittedComponents[1]);
				
			}
		}
		
		WriteFile(YFile,  "../Output/Y.csv" );
	}
	
	public static Integer MappingClass(String classInHelminthformat)
	{
        /* Converting the the sample class from ("NONE", "HIGH") to (0, 1). */
		if(classInHelminthformat.equals("NONE"))
		{
			return 0;
		}
		else if(classInHelminthformat.equals("HIGH"))
		{
			return 1;
		}
		else
		{
			System.out.println("Error in the class tag of the Helminth phenotype.");
			return 0;
		}
	}

	private static ArrayList<ArrayList<Double>>  ConvertTaxaValuesToRelativeAbundance(ArrayList<String> BrnchesArrayList)
	{
		// eliminate string cells
		ArrayList<ArrayList<Double>> brnchesArrayList1=new ArrayList<ArrayList<Double>>();
		brnchesArrayList1 = eliminateStringCells(BrnchesArrayList);
		return brnchesArrayList1;
		
	}

	public static ArrayList<ArrayList<Double>> eliminateStringCells(ArrayList<String> BrnchesArrayList)
	{
        
        /* Eliminating the sample ID from the input file. */
		ArrayList<ArrayList<Double>> brnchesArrayList1=new ArrayList<ArrayList<Double>>();
		
		for (int i=1; i< BrnchesArrayList.size(); i++)
		{
			String[] parts1 = BrnchesArrayList.get(i).split(",");
			ArrayList<Double> TemparrayList=new ArrayList<Double>();
			Double sum=0.0;
			
			for (int j=2; j< parts1.length; j++)
			{
				Double abundance=Double.parseDouble(parts1[j]);
				sum = sum + abundance;
			}
			
			//Convert to relative abundance
			for(int j=2; j< parts1.length; j++)
			{
				Double abundance=Double.parseDouble(parts1[j]);
				TemparrayList.add(abundance/sum) ; 
			}
			brnchesArrayList1.add(TemparrayList);
			
		}
			
		return  brnchesArrayList1;
		
	}

	
	public static  ArrayList<String> ReadData(String fileName)
	{
		
		// Reading files.
		ArrayList<String> TemporaryXvalues = new ArrayList<String>();
		
		try
		{
			FileReader inputFile = new FileReader(fileName);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			String line;
			
			while ((line = bufferReader.readLine()) != null)  
			{

				TemporaryXvalues.add(line);
				
			}
				bufferReader.close();
		}
			       
		catch(Exception e)
		{
			System.out.println("Error while reading file line by line:" + e.getMessage());                      
		}
				
		return 	TemporaryXvalues;	
		
	}
	
	public static String prepareLineOfCSV(String[] LineComponents)
	{
		String CSVFormatOfLine = LineComponents[0];
		
		for(int i=1; i<LineComponents.length; i++)
		{
			CSVFormatOfLine =  CSVFormatOfLine +","+ LineComponents[i];
		}
		
		return CSVFormatOfLine ;
		
	}
	
	public static String PrepareCSV(String CSVParag, String NewLine)
	{
		return CSVParag+"\n"+ NewLine;
	}
    
    public static String ConvertArrayListFromDoubleToString(ArrayList<ArrayList<Double>> StringTaxaAbundance)
    {
        String stream = TaxtaList + "\n";
        for(int i=0; i< StringTaxaAbundance.size(); i++)
        {
            ArrayList<Double> TempArrayList = StringTaxaAbundance.get(i);
            for(int j=0; j< TempArrayList.size()-1 ; j++)
            {
                stream = stream + TempArrayList.get(j).toString() + ",";
            }
            stream = stream + TempArrayList.get(TempArrayList.size()-1).toString();
            stream = stream + "\n";
        }
        return stream;
        
    }
		
}
