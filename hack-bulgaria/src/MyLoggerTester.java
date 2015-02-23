/*
 * Tester class for the Logger classes in MyLogger.java
 */
public class MyLoggerTester {

	public static void main(String[] args) {
		ConsoleLogger cl = new ConsoleLogger();
		cl.log(1, "Hello");
		FileLogger fl = new FileLogger();
		fl.log(2, "World");
	}

}
