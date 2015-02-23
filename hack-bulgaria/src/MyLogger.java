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
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

interface MyLogger {
	public void log(int level, String message);
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
			//TODO POST transaction, unfortunately out of my scope of knowledge :)
		}
	}
	
}