/*	Make an interface, called MyLogger with only 1 method - log(level, message)

The two arguments should be:

level - an integer, from 1 to 3.
1 means that your are logging with INFO level.
2 means that you are logging with WARNING level.
3 means that you are logging with PLSCHECKFFS level.
message is a string, that you are logging.
There is a rule of how to make the log message, regardless where you are saving it:

{LOG_LEVEL_STRING}::{TIMESTAMP}::{MESSAGE}
For example, if we log with level = 1, and message = "Hello World", this will produce the following line:

INFO::2015-02-02T01:43:19+00:00::Hello World
The timestamp should be in ISO 8901 format. (NB: Maybe it is meant ISO 8601?)

Make 3 different classes, that implement the interface MyLogger:

ConsoleLogger
The ConsoleLogger should log the messages directly to the console.

FileLogger
The FileLogger should log the messages to a given file.

HTTPLogger
The HTTPLogger shoud log the messages via a POST request to a given HTTP url.
*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

interface MyLogger {
	public void log(int level, String message) throws MalformedURLException, IOException;
}

class ConsoleLogger implements MyLogger {

	@Override
	public void log(int level, String message) {
		String levelStr, log;
		if (level<1 || level>3)
			System.err.println("Error, valid level values are 1,2 and 3.");
		else{
			switch(level) {
			default: levelStr="Error"; break;
			case(1): levelStr="INFO"; break;
			case(2): levelStr="WARNING"; break;
			case(3): levelStr="PLSCHECKFFS"; break;
			}
			// In ISO 8601, as ISO 8901 does not exist.
			Instant time = Instant.now();
			log = levelStr + "::" + time.truncatedTo(ChronoUnit.SECONDS).toString() + "::" + message;
			System.out.println(log);
		}
	}
	
}

class FileLogger implements MyLogger {

	@Override
	public void log(int level, String message) {
		String levelStr, log;
		if (level<1 || level>3)
			System.err.println("Error, valid level values are 1,2 and 3.");
		else{
			switch(level) {
			default: levelStr="Error"; break;
			case(1): levelStr="INFO"; break;
			case(2): levelStr="WARNING"; break;
			case(3): levelStr="PLSCHECKFFS"; break;
			}
			// In ISO 8601, as ISO 8901 does not exist.
			Instant time = Instant.now();
			log = levelStr + "::" + time.truncatedTo(ChronoUnit.SECONDS).toString() + "::" + message;
			
			File file = new File("message.log");
			Charset charset = Charset.forName("UTF-8");
			try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset)) {
			    writer.write(log, 0, log.length());
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			}
		}
	}
	
}

class HTTPLogger implements MyLogger {
	public static String user_agent = "Mozilla/5.0";
	
	public void printLog(int level, String message) throws IOException {
		HTTPLogger http = new HTTPLogger();
		
		System.out.println("\nTesting 2 - Send Http POST request");
		http.log(level, message);
	}
	
	@Override
	public void log(int level, String message) throws IOException {
		
		String levelStr, log;
		if (level<1 || level>3)
			System.err.println("Error, valid level values are 1,2 and 3.");
		else{
			switch(level) {
			default: levelStr="Error"; break;
			case(1): levelStr="INFO"; break;
			case(2): levelStr="WARNING"; break;
			case(3): levelStr="PLSCHECKFFS"; break;
			}
			Instant time = Instant.now();
			log = levelStr + "::" + time.truncatedTo(ChronoUnit.SECONDS).toString() + "::" + message;
			//TODO POST transaction, unfortunately out of my scope of knowledge :)
			String url = "https://selfsolve.apple.com/wcResults.do";
			
			URL obj= new URL(url);

				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", user_agent);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String urlParameters = "sn="+log;
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
	 
		}
	}
	
}