package br.com.deepmapper.constans;

public class RegexConstants {
	private String onionRegex = "(?i)(http:\\/\\/).{16}?(?:\\.onion)";

	public String getOnionRegex() {
		return onionRegex;
	}

	public void setOnionRegex(String onionRegex) {
		this.onionRegex = onionRegex;
	}
}
