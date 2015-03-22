import java.io.IOException;

/*
 * Tester class for the Logger classes in MyLogger.java
 */
public class MyLoggerTester {

	public static void main(String[] args) throws IOException {
		ConsoleLogger cl = new ConsoleLogger();
		cl.log(1, "Hello");
		FileLogger fl = new FileLogger();
		fl.log(2, "World");
		HTTPLogger hl = new HTTPLogger();
		hl.printLog(3, "Half-Life");
	}

}
