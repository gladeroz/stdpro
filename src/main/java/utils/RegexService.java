package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexService {

	private RegexService(){}

	public static Matcher get(String regex, String value){
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value);
	}
}
