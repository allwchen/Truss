import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class TrussWork {

	private static final char UNICODE_REPLACEMENT = '\uFFFD';
	private static final char CSV_DELIMITER = ',';
	private static final char CSV_QUOTE = '"';
	private static final String INPUT_DATE_FORMAT = "M/d/yy h:mm:ss a";
	private static final String OUTPUT_DATE_FORMAT = "YYYY-MM-dd'T'HH:mm:ssX";
	private static final String DURATION_FORMAT = "H:mm:ss.SSS";
	private static final String DURATION_ZERO = "0:00:00.000";
	private static final String DEFAULT_CSV_FILE = "sample.csv";

	public static void main(String args[]) {
		
	String csvInputFile;// = "sample.csv";
        String csvOutputFile = "output.csv";
        Scanner scanner;
        List<String> line;
        String lineToWrite;
	BufferedWriter bw = null;
	FileWriter fw = null;
	boolean header = true;
        
        String inputTimestamp;
        String inputAddress;
        String inputZip;
        String inputFullname;
        String inputFooDuration;
        String inputBarDuration;
        String inputTotalDuration;
        String inputNotes;
        
        String outputTimestamp;
        String outputAddress;
        String outputZip;
        String outputFullname;
        String outputFooDuration;
        String outputBarDuration;
        String outputTotalDuration;
        String outputNotes;
        
        LocalDateTime localDt;
        ZonedDateTime pacific_time;
        ZonedDateTime eastern_time;
        ZoneId pacificZoneId = ZoneId.of("America/Los_Angeles");
        ZoneId easternZoneId = ZoneId.of("America/New_York");
		DateTimeFormatter format = DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT);
		
		Duration fooDuration;
		Duration barDuration;
		String[] fooArray;
		String[] barArray;
		Duration totalDuration;
		
		csvInputFile = DEFAULT_CSV_FILE;
		for (int i=0; i<args.length; i++) {
			if (i==0) {
				csvInputFile = args[i];
				System.out.println("Reading file " + csvInputFile);
			}
		}
                
        try {
	        scanner = new Scanner(new File(csvInputFile));
	        fw = new FileWriter(csvOutputFile);
	        bw = new BufferedWriter(fw);
	        while (scanner.hasNext()) {
System.out.println("scanner had next");
    	        line = readLine(scanner.nextLine());
        	    
        		inputTimestamp 		= line.get(0);
        		inputAddress 		= line.get(1);
        		inputZip 			= line.get(2);
        		inputFullname 		= line.get(3);
        		inputFooDuration 	= line.get(4);
        		inputBarDuration 	= line.get(5);
        		inputTotalDuration 	= line.get(6);
        		inputNotes 			= line.get(7);
				
				System.out.println("Timestamp = " + inputTimestamp);
                System.out.println("Address = " + inputAddress);
                System.out.println("ZIP = " + inputZip);
                System.out.println("Fullname = " + inputFullname);
                System.out.println("FooDuration = " + inputFooDuration);
                System.out.println("BarDuration = " + inputBarDuration);
	            System.out.println("TotalDuration = " + inputTotalDuration);
   	        	System.out.println("Notes = " + inputNotes);
        		
        		if (header == false) {
        			//System.out.println("inputTimestamp = " + inputTimestamp);
        			//System.out.println("input date format: " + INPUT_DATE_FORMAT);
        			localDt = LocalDateTime.parse(inputTimestamp, DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT));
	    			//System.out.println("pacific time: " + format.format(ldt));
	    			//pacific_time = ZonedDateTime.parse(inputTimestamp, DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT));
	    			pacific_time = localDt.atZone(pacificZoneId);
	    			//System.out.println("pacific time: " + pacific_time /*DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT).format(pacific_time)*/);
	    			eastern_time = pacific_time.withZoneSameInstant(easternZoneId);
	    			//System.out.println("eastern time: " + eastern_time /*DateTimeFormatter.ofPattern(OUTPUT_DATE_FORMAT).format(eastern_time)*/);
	    			outputTimestamp = format.format(eastern_time);
	    			
    				//System.out.println("Timestamp = " + inputTimestamp + " ==> " + outputTimestamp + "\n");
    			
        			outputAddress 		= inputAddress;
        			if (outputAddress.contains(",")) {
        				outputAddress = "\"" + outputAddress + "\"";
        			}
        			
	        		outputZip 			= String.format("%05d", Integer.parseInt(inputZip));
	        		
    	    		outputFullname 		= inputFullname.toUpperCase();
        			
	                outputFooDuration 	= addDurations(inputFooDuration, DURATION_ZERO.toString());
        			outputBarDuration 	= addDurations(inputBarDuration, DURATION_ZERO.toString());
	                
	                outputTotalDuration = addDurations(inputFooDuration, inputBarDuration);
    	            
        			outputNotes 		= inputNotes;
    	            if (outputNotes.contains(",")) {
        				outputNotes = "\"" + outputNotes + "\"";
        			}
        		} else {
        			outputTimestamp		= inputTimestamp;
        			outputAddress 		= inputAddress;
	        		outputZip 			= inputZip;
    	    		outputFullname 		= inputFullname;
        			outputFooDuration 	= inputFooDuration;
        			outputBarDuration 	= inputBarDuration;
        			outputTotalDuration = inputTotalDuration;
        			outputNotes 		= inputNotes;

        			header = false;
        		}
   	        	
   	        	lineToWrite = outputTimestamp + "," 
   	        				  + outputAddress + "," 
   	        				  + outputZip + "," 
   	        				  + outputFullname + "," 
   	        				  + outputFooDuration + ","
   	        				  + outputBarDuration + ","
   	        				  + outputTotalDuration + ","
   	        				  + outputNotes + "\n";
   	        	bw.write(lineToWrite);
        	}
System.out.println("finished while loop");
        	scanner.close();
System.out.println("closed scanner");
    	} catch (FileNotFoundException e) {
    		//e.printStackTrace();
    		System.out.println("File not found: " + csvInputFile);
    	} catch (Exception e) {
    		System.out.println("Other exception with file: " + csvInputFile);
			e.printStackTrace();
    	} finally {
    		if (bw != null) {
    			try {
    				bw.close();
    			} catch (Exception e) {
    			}
    		}
    		if (fw != null) {
    			try {
    				fw.close();
    			} catch (Exception e) {
    			}
    		}
    		System.out.println("All done!");
    	}
	}
	
	public static List<String> readLine(String csvLine) {
		return readLine(csvLine, CSV_DELIMITER, CSV_QUOTE);
	}
	
	public static List<String> readLine(String csvLine, char delimiters) {
		return readLine(csvLine, delimiters, CSV_QUOTE);
	}
	
	public static List<String> readLine(String csvLine, char delimiters, char quoteChar) {
		List<String> line = new ArrayList<>();

		if (csvLine == null && csvLine.isEmpty()) { // empty string
			return line;
		}
		
		if (quoteChar == ' ') {
			quoteChar = CSV_QUOTE;
		}
		if (delimiters == ' ') {
			delimiters = CSV_DELIMITER;
		}
		
		StringBuffer current = new StringBuffer();
		boolean inQuotes = false;
		boolean etc = false;
		
		char[] chars = csvLine.toCharArray();
		for (char ch : chars) {
			if (inQuotes) {
				if (ch == quoteChar) {
					inQuotes = false;
				} else {
					if (Character.isSurrogate(ch) /*Character.isHighSurrogate(ch) || Character.isHighSurrogate(ch)*/) {
						current.append(UNICODE_REPLACEMENT);
					} else {
						current.append(ch);
					}
				}
			} else {
				if (ch == quoteChar) {
					inQuotes = true;
				} else if (ch == delimiters) {
					line.add(current.toString());
					current = new StringBuffer();
				} else if (ch == '\r') {
					continue;
				} else if (ch == '\n') {
					break;
				} else {
					if (Character.isSurrogate(ch) /*Character.isHighSurrogate(ch) || Character.isHighSurrogate(ch)*/) {
						current.append(UNICODE_REPLACEMENT);
					} else {
						current.append(ch);
					}
				}
			}
		}
		line.add(current.toString());

		return line;
	}
	
	public static String addDurations (String dur1String, String dur2String) {
		String[] array1, array2;
		Duration d1, d2, dtotal;
		double duration_time;
		
	    array1 = dur1String.split(":");
        array2 = dur2String.split(":");
        			
        d1 = Duration.parse("PT"+array1[0]+"H"+array1[1]+"M"+array1[2]+"S");
        d2 = Duration.parse("PT"+array2[0]+"H"+array2[1]+"M"+array2[2]+"S");
        
        dtotal = d1.plus(d2);
		duration_time = (dtotal.toMillis()) / 1000.00;
		
		return "" + duration_time;
	}
}
