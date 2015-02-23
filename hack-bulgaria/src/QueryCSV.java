import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QueryCSV {
	
	public static ArrayList<ArrayList<String>> readFile (Path file, ArrayList<ArrayList<String>> CSV) {
		String line = new String();
		//Depends on the CSV format - ", " or " , " could also be used.
		String cvsSplitBy = ",";
		
		Charset charset = Charset.forName("UTF-8");
		try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
			while ((line = reader.readLine()) != null) {
				ArrayList<String> inner = new ArrayList<>();
				String[] words = line.split(cvsSplitBy);
				for (int i=0; i<words.length; i++)
					inner.add(words[i]);
				CSV.add(inner);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		return CSV;
	}
	
	//Display the columns, which names are listed in columnsNames if there are such
	public static void select(ArrayList<ArrayList<String>> CSV, String columnsNames) {
		ArrayList<Integer> indexes = new ArrayList<>();
		String print = new String();
		
		for (int i=0; i<CSV.get(0).size(); i++)
			if (columnsNames.contains(CSV.get(0).get(i)))
				indexes.add(i);
		if (!indexes.isEmpty())
			for (int i=0; i<CSV.size(); i++) { 
				for(int j=0; j<indexes.size(); j++)
					print += CSV.get(i).get(indexes.get(j))+", ";
				System.out.println(print);
				print="";
			}
		if (indexes.isEmpty())
			System.out.println("No match found!");
	}
	
	//Overloading with limit of shown rows
	public static void select(ArrayList<ArrayList<String>> CSV, String columnsNames, int limit) {
		ArrayList<Integer> indexes = new ArrayList<>();
		String print = new String();
		
		for (int i=0; i<CSV.get(0).size(); i++)
			if (columnsNames.contains(CSV.get(0).get(i)))
				indexes.add(i);
		if (!indexes.isEmpty())
			for (int i=0; i<CSV.size()&& i<limit+1; i++) { 
				for(int j=0; j<indexes.size(); j++)
					print += CSV.get(i).get(indexes.get(j))+", ";
				System.out.println(print);
				print="";
			}
		if (indexes.isEmpty())
			System.out.println("No match found!");
	}
	
	//Searches for a string in a cell and outputs the given rows
	public static void find(ArrayList<ArrayList<String>> CSV, String find) {
		for (int i=0; i<CSV.size(); i++) {
			for (int j=0; j<CSV.get(i).size(); j++)
				if (CSV.get(i).get(j).contains(find)) {
					System.out.println(CSV.get(i));
					break;
				}
		}
	}
	
	//Sums the integers in the selected column, if there is no such column name or
	//column does not contain integers, error messages are output.
	public static int sum(ArrayList<ArrayList<String>> CSV, String columnName) {
		int temp=0, index = -1, sum=0;
		boolean containsInts = false;
		for (int i=0; i<CSV.get(0).size(); i++)
			if(columnName.equals(CSV.get(0).get(i)))
				index = i;
		
		if (index != -1)
			for (int i=0; i<CSV.size(); i++) {
				if (isInt(CSV.get(i).get(index))) {
					temp = Integer.parseInt(CSV.get(i).get(index));
					containsInts = true;
				}
				sum+=temp;
				temp=0;
			}
		
		if(index==-1) {
			System.err.println("There is no such column!");
			return -1;
		}
		
		if(!containsInts){
			System.err.println("Column does not contain integers!");
			return -1;
		}
		
		return sum;
	}
	
	//Shows the columns names	
	public static void show(ArrayList<ArrayList<String>> CSV) {
		for (int i=0; i<CSV.get(0).size(); i++)
			System.out.print(CSV.get(0).get(i) + ", ");
	}
	
	public static boolean isInt(String str) {
		try {
			int d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		Path file = Paths.get("test.csv");
		ArrayList<ArrayList<String>> CSV = new ArrayList<>();
		CSV = readFile(file, CSV);
		select(CSV, "Year Model", 1);
		find(CSV, "Ford");
		System.out.println(sum(CSV, "Year"));
		show(CSV);
		
	}

}
