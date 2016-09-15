package goeuro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import PlaceResponse.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.google.gson.Gson;

public class Main {
	private static String url = "http://api.goeuro.com/api/v2/position/suggest/en/";
	
	public static void main(String[] args) {
		if(args.length>0){
			url+=args[0];
		}
		HttpClient client = new HttpClient();
	    GetMethod method = new GetMethod(url);

	    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
	        	new DefaultHttpMethodRetryHandler(3, false));

	    try {
	      // Execute the method.
	      int statusCode = client.executeMethod(method);
	      if (statusCode != HttpStatus.SC_OK || statusCode==400) {
	        System.err.println("Method failed: " + method.getStatusLine());
	      } else {
		      InputStream responseBodyStream = method.getResponseBodyAsStream();
		      String responseBodyString = 
		    		  getStringFromInputStream(responseBodyStream);
		      Gson gson = new Gson();
		      Place[] places = gson.fromJson(responseBodyString, Place[].class);
		      writeToCSV(places);
	      } 
	  

	    } catch (HttpException e) {
	      System.err.println("Fatal protocol violation: " + e.getMessage());
	      e.printStackTrace();
	    } catch (IOException e) {
	      System.err.println("Fatal transport error: " + e.getMessage());
	      e.printStackTrace();
	    } finally {
	      // Release the connection.
	      method.releaseConnection();
	    }  	
	}
	
	private static final String CSV_SEPARATOR = ",";
    private static void writeToCSV(Place[] places)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("places.csv"), "UTF-8"));
            for (Place place : places)
            {
                StringBuffer oneLine = new StringBuffer();
                oneLine.append(place.getId() ==0L ?
                		0L : place.getId());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(place.getName() == null? "" : place.getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(place.getType() == null ? "" : place.getType());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(place.getGeoPosition().getLatitude() == 0.0d ?
                		0.0d : place.getGeoPosition().getLatitude());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(place.getGeoPosition().getLongitude() == 0.0d ?
                		0.0d : place.getGeoPosition().getLongitude());
                oneLine.append(CSV_SEPARATOR);
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) {
        	System.out.println("UnsupportedEncodingException " + e.getMessage());
        }
        catch (FileNotFoundException e){
        	System.out.println("FileNotFoundException " + e.getMessage());
        }
        catch (IOException e){
        	System.out.println("IOException " + e.getMessage());
        }
    }
    
    private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
