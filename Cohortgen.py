### Quick and dirty cohort data generator 
###
### This python script creates additional rows required for Cohort analysis for churned customers .  
### The customer data in not the format required for Cohort analysis  - for ex. formats exist for e-commerce churn data  
### The logic takes into account group of customers and follows them through till they churn ( churn ) 
### till churn year they are 0 but the year when they churn they are flagged as 1 , additionally duration year is also calculated   
###  bad practice of using raw subscripts !!  but change later 
###  Position 8 -  RegYear 
###  Position 9 -  Current Year usually, will be Termination Year for cases where customer churned (FOR , SUR , LPS) type codes 
###  Position 14 - 15th column and 14th index where duration year is stored 
###  Position 15 - 16th column and 15th index where churn is stored 

import time
import csv
#timestr = time.strftime("%Y%m%d-%H%M%S")
timestr = time.strftime("%H%M%S")
inpath="/home/li/users/sdey/churn/outputs/data/EdaDataset.csv"
print timestr
outpathraw="/home/li/users/sdey/churn/outputs/data/CohortDataset_"
outpath = outpathraw + timestr + "longv1.csv"
#inpath="testeda.csv"                    # Test sample file 

def Writecsv(record):                   # Write Out record 

    writer.writerow(record)
    return   

def CreateCohort(row):    						 # Function to calculate year wise cohorts to account for non churn years before churn for churnd cases and  
												 # yearwise cohorts to cover full duration for non churn policies

                          
    if row[8]=='NULL' :   
       return             						 # Skip cases when Reg year is NULL 

    if int(row[8]) < 2009 or int(row[8]) > 2016  :   
       return             	                     # Skip cases before 2009 and after 2016                  

    if row[9] in ("2008","2017","2044"):         # Skip cases with weird termination dates   
	   return
	   
    row.extend([0,0])                          # In order , duration , churn , inforce , total
    DurationYear = 0                             # Initialize Duration year variable 
#   storestatus = None                           # Initialize policy status variable	
    
    if row[9]=='NULL' :
            year=int(row[8])
            row[8] = year                        # cast to integer  
            regyear = year 
            while(year<=2016):
                churnvalue = 0                   #Set churn to False 
                row[9] = year                    #Set the column to current year 
                DurationYear = year-regyear+1                 
                year = year + 1
                row[14]=DurationYear             # Update 15 column with churn value 
                row[15]=churnvalue               # Update 16 column with churn value 
                Writecsv(row[0:16])
 #              print("record1")

    elif row[7] in ("FOR","SUR","LPS","DTH","LF","PUR","RPL","TC","TE","NTO"):          # Check all types of churn categories  
            
#          print("recStart")
#          row.extend([0,0])                     # Create the 16 and 17th columns to hold the churn variable and duration Year
#          print(len(row))
           year=int(row[8])
           regyear = year 
           storestatus = row[7]  
           row[8] = year 
           churnyear = int(row[9])  
#          print("year,churnyear",year,churnyear)

           while(year<churnyear):
               churnvalue = 0  
               row[7] = None
               row[9] = year 
               DurationYear = year-regyear+1
               year=year + 1  
 #             print("year,churnyear,churnvalue",year,churnyear,churnvalue)
               row[14]=DurationYear
               row[15]=churnvalue                # Write the 16 column with churn value 
               Writecsv(row[0:16])
 
           if year == churnyear:
#               print("year=churnyear")
                row[7] = storestatus
                if row[7] in ("DTH","LF","PUR","RPL","TC","TE","NTO"):
                    churnvalue = 0              
                else:
                    churnvalue = 1                # Set churn only for surrender,churn and forfeiture  status codes 
                DurationYear = year-regyear+1  
                row[14] = DurationYear
                row[15] = churnvalue             # Update the churn flag to churnd 
                row[9] = churnyear               # Update the Current year as churn year       
#               print(row)
                Writecsv(row[0:16])
    return

def main():    
    global writer 
    with open(inpath, "rb") as infile , open(outpath, "wb") as outfile:

        reader = csv.reader(infile)                         #Set up the reader
        header = next(reader, None)                         #Read the header
        header.extend(['DurationYr','churn'])               #Create DurationYr column and churn flag column 
        
        writer = csv.writer(outfile)                        #Set up the writer   
   
        if header:
            writer.writerow(header)                         #Write out the header  
    
        for row in reader:
            CreateCohort(row)                               # process each row and call the create cohort data


if __name__ == "__main__": main()        
