/* In a programming language of your choice, implement the following function/method:

	maskOutWords(words, text)
	If you read type declarations well, here it is:

	maskOutWords :: [String] -> String -> String
	Where: 
	words is a list of words (strings)
	text is a string, that may contain newlines - \n
	The function should return a new text, where each matching word from words is replaced by the same number of characters *.
 */

import java.util.ArrayList;
import java.util.List;


public class MaskOutWords {

	public static String mask(List<String> words, String text ) {
		String stars = new String();
		for (int i=0; i<words.size(); i++) {
			if (text.contains(words.get(i))) {
				for (int j=0; j<words.get(i).length(); j++)
					stars+="*";
				text = text.replace(words.get(i), stars);
				stars = "";
			}
		}
		
		return text;
	}
	
	public static void main(String[] args) {
		List<String> words = new ArrayList<String>();
		words.add("PHP");
		words.add("java");
		String text = "PHP is not like java. php is more like JAVAscript.\nPHP java.";
		System.out.println(mask(words, text));

	}

}
