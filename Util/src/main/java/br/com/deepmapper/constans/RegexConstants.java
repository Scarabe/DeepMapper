package br.com.deepmapper.constans;

public class RegexConstants {
	public static final String onionRegex;

	static{
		onionRegex = "(?i)(http:\\/\\/).{16}?(?:\\.onion)";
	}
}
