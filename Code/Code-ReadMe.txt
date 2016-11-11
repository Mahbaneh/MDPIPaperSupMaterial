-Code description: This directory contains supplementary codes used in the paper (Application of Taxonomic Modeling to Microbiome Data Mining for Detection of Helminth Infection in Global Populations), Figure3. These codes can be used freely. This Folder contains three sub-folders: Grouping, Normalization, and Output. 

—-Normalization folder: This folder contains Normalization.java code. By this code, we prepare the helminth files needed as the input of the SMART-scan algorithm, explained in section 2.2.2. “Taxonomic Modeling” of the paper.
——- Normalization.java:
			-Input: 
                              Helminth-Input-File: Is one of the helminth datasets, Indonesia.csv or Libera.csv.
                              Data-Mapper-File: Is the Data-Mapper.txt file.

			-Instructions for running the code: 
                              javac Normalization.java 
                              java Normalization Helminth-Input-File Data-Mapper-File
                              
			-Output: These output files can be found in the Code/Output directory.
                              X.csv: Contains taxonomic information of the samples in the Helminth-Input-File file. In this file, rows are samples and columns are  NORMALIZED taxa.  
                              Y.csv: Contains the class attribute of samples in the Helminth-Input-File file. ‘0’ is for ‘None’ class and ‘1’ is for ‘High’ class.
                              tree.csv: Contains the taxonomic information needed by SMART-scan for building the tree. This file is extracted from the Data-Mapper.txt. In each row of tree.txt we have the taxonomy of a taxa extracted for helminth data.

—-Grouping Folder: This folder contains GroupingSummation.java and SmartScanGrouping.java. By these codes, we  prepare the new dataset resulted from applying the SMART-scan grouping on the X.csv. Thus, we need the groupings which is one of the outputs of the SMART-scan toolkit (Available at https://dsgweb.wustl.edu/qunyuan/software/smartscan/).
——- GroupingSummation.java and SmartScanGrouping.java

			-Input: 
                              SMART-scan-Grouping: The grouping resulted from SMART-scan toolkit (Available at https://dsgweb.wustl.edu/qunyuan/)software/smartscan/).
                              X.csv: Output of Normalization.java.
                              Y.csv: Output of Normalization.java.

                       Instructions for running the code:
                             javac GroupingSummation.java
                             java GroupingSummation Output-File SMART-scan-Grouping X.csv Y.csv

			-Output: 
                              Output-File.csv: Is the new dataset after applying the SMART-scan grouping. Each new feature is the summation of the values of members of a group. In this file, each row is a sample, the last column is the class attribute of the sample and the rest of the columns are new features.



—- Output Folder: All the output of the codes (X.csv, Y.csv, tree.csv, Output-File.csv) will be saved in this folder.  

-Example of running: 
                    
                              javac Normalization.java 
                              java Normalization "../../Helminth-Data/Indonesia.csv" "../../Helminth-Data/Data-Mapper.txt"

                              
                              javac GroupingSummation.java
                              java GroupingSummation "X-After-Grouping.csv" "../../Helminth-Data/smt.output.csv" "../Output/X.csv" "../Output/Y.csv"




 