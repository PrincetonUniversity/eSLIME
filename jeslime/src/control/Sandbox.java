package control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import structural.identifiers.Coordinate;

public class Sandbox {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String toParse2 = "(1, 22 | 7 | 3.0)";
		String toParse3 = "(1, 22, 3 | 4 | 0.3)";
		
		//Non-temporal
		//String pStr = ("\\((\\d+), (\\d+)(, (\\d+))? \\| (\\d+)\\)");
		
		String pStr = ("\\((\\d+), (\\d+)(, (\\d+))? \\| (\\d+) \\| (\\d+\\.\\d+)\\)");

		Pattern pattern = Pattern.compile(pStr);
		Matcher matcher = pattern.matcher(toParse2);
		matcher.find();
		for (int i = 0; i <= matcher.groupCount(); i++) {
			System.out.println(matcher.group(i));
		}
	}
}
