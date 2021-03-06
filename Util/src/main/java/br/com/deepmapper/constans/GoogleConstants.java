package br.com.deepmapper.constans;

public class GoogleConstants {
	public static final String googleLink = "https://google.com/";
	public static final String searchBtnName = "btnK";
	public static final String inputSerchName = "q";
	public static final String linksTable = "res";
	public static final String xpathGLinks = "//h3[@class='r']/a";
	public static final String naviBar = "navcnt";
	public static final String googleLinkPlusPage = "&start=";
	public static final int googleMaxSearch = 990;

	private String serchContent = "Onion links";
	public String getSerchContent() {
		return serchContent;
	}
	public void setSerchContent(String serchContent) {
		this.serchContent = serchContent;
	}
}
