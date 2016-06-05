package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started
		List<String> urls = new ArrayList<String>();
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		urls.add(url);
		do{
			url = findUrl(url);
			System.out.println(url);
			if(url == null){
				url = "Error";
			}
			if(urls.contains(url)){
				url = "Error";
			}
			urls.add(url);
		}while(url!= "Error" && !url.equals("https://en.wikipedia.org/wiki/Philosophy"));
		System.out.println(urls);
	}
	private static String findUrl(String url)throws IOException{
		Elements paragraphs = wf.fetchWikipedia(url);
		Element para;
		Iterable<Node> iter;
		int parenthesis = 0;
		for(int i = 0; i < paragraphs.size(); i++){
			para = paragraphs.get(i);
			iter = new WikiNodeIterable(para);
			for (Node node: iter) {
				if (node instanceof TextNode) {
					//System.out.println(node);
					String text = node.attr("text");
					for(int j = 0; j<text.length(); j++){
						if(text.charAt(j) == '(')
							parenthesis++;
						else if(text.charAt(j) == ')')
							parenthesis--;
					}
				}
				else{
					String wikiName = node.attr("href");
					if(wikiName.startsWith("/wiki")){
						String nextUrl = "https://en.wikipedia.org" + node.attr("href");
						if(!nextUrl.equals(url) && parenthesis == 0){
							return nextUrl;
						}
					}
				}
	        }
		}
		return null;
	}
}
