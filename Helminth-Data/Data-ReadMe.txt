-Data description:
As mentioned in the paper (Application of Taxonomic Modeling to Microbiome Data Mining for Detection of Helminth Infection in Global Populations), this data set are microbiome count data from 16s rRNA sequencing of stool samples from helminth-infected (‘High’ class) and helminth-non-infected (‘None’ class) individuals in two countries, Indonesia and Liberia. The data was produced by the helminth genomics group at the McDonnell Genome Institute in collaboration with the laboratory of Drs.Peter U. Fischer, Maria Yazdanbakhsh and Taniawati Supali.

This Folder contains three files:  Indonesia.csv, Liberia.csv, and Data-Mapper.txt.
—- Indonesia.csv is the Indonesia dataset. 
—- Liberis.csv is the Liberia dataset.
—- Data-Mapper.txt is the file the taxonomic tree is extracted from. 
—- smt.output.csv is the grouping file resulted by applying the SMARt-scan (Available at https://dsgweb.wustl.edu/qunyuan/)software/smartscan/). The prepared file is just a sample file to show how it is used in the supplementary codes.

In both of the datasets, Indonesia.csv and Liberia.csv, Each row is an individual’s sample. The first column, #Sample, is the Sample ID; the second column, @Class, is the class of sample; and the rest of the columns are the anonymous variables (taxa). List of specific taxa and the complete cohort information will be published as a separate study and the (#Sample) Sample ID will enable to cross-link the information among the studies. 

In Data-Mapper.txt file, each row indicates the taxonomic information of an extracted taxa from the helminth datasets. In each row, the first column is the anonymous taxa and the second column is the anonymous taxonomy branch of the taxa. As we mentioned, the real taxonomic information can be extracted from the another paper publishing the data. It should be noted that the Data-Mapper.txt file is just a template file (with no real data), prepared for running the codes provided in the Code supplementaries.   

More information about the datasets can be find in the section ‘2.1. Microbiome Data’ of the paper. 