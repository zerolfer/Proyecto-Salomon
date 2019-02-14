package algorithms.vnsPablo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

public class Traces {
    public static final Logger logger = Logger.getLogger("MyLog");  
    public static Date date;
    
    private  Traces instance = null;

	   public   Traces() throws IOException {
	        // This block configure the logger with handler and formatter  
		    FileHandler fh; 
	        try {
	        	
	        	date = new Date();
	        	
	        	boolean success = (new File(date.toString())).mkdirs();
	        	if (!success) {
	        	    // Directory creation failed
	        	}
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
			
							
				fh = new FileHandler(dateFormat.format(date)+"/result.log");
				
		        logger.addHandler(fh);
		        SimpleFormatter formatter = new SimpleFormatter();  
		        fh.setFormatter(formatter);  
		        logger.addHandler(fh);
		        logger.setUseParentHandlers(false);
		        logger.setLevel(Level.FINE);
		        //formatter.format="%$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n";
	        
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        try {  



			        // the following statement is used to log any messages
			    	FileWriter pw = new FileWriter(date+"/data.csv",true);
			    	pw.append(  "fFunction,iterations,time,totalFitnes, neighborhood,laps, nlSize, explored\n");
			    	pw.close();
			        //logger.info(fh.for ", " +time+", "+fitness + ", " +neighborhood);
 
		       

		    } catch (SecurityException e) {  
		        e.printStackTrace();  
		    } 

     }
	 
		   
	   
	public void TraceFileCsv(String fFunction, int iterations, long time, double fitness, int neighborhood, int laps,int nlSize, int explored) throws IOException 
	{
 
		    

		    try {  



		        // the following statement is used to log any messages
		    	FileWriter pw = new FileWriter(date+"/data.csv",true);
		    	
		    	pw.append(fFunction+", "+iterations+", "+time + ", "+fitness + ", " +neighborhood+ ", " +laps+ ", " +nlSize+ ", " +explored +"\n");
		    	pw.close();
		        //logger.info(fh.for ", " +time+", "+fitness + ", " +neighborhood);

		    } catch (SecurityException e) {  
		        e.printStackTrace();  
		    }  
	}

}
