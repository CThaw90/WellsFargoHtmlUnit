package com.bartershock.bot;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class BarterBot {

	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		System.out.println("Starting Bot Crawler");
		// System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
		
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		WebClientOptions webClientOptions = webClient.getOptions();
		CookieManager cookieManager = webClient.getCookieManager();
		
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		webClientOptions.setJavaScriptEnabled(false);
		webClientOptions.setCssEnabled(false);
		webClientOptions.setRedirectEnabled(true);
		webClientOptions.setUseInsecureSSL(true);
		cookieManager.setCookiesEnabled(true);
		
		final HtmlPage page = webClient.getPage("https://wellsfargo.com");
		final HtmlForm form = (HtmlForm) page.getElementById("frmSignon");
		final HtmlSubmitInput submit = (HtmlSubmitInput) form.getInputByName("btnSignon");
		final DomElement userElement = page.getElementById("userid");
		final HtmlTextInput username = (HtmlTextInput) form.getInputByName(userElement.getAttribute("name"));
		final HtmlPasswordInput password = (HtmlPasswordInput) form.getInputByName("j_password");
		
		username.setValueAttribute("username");
		password.setValueAttribute("password");
		
//		System.out.println(username.asXml());
//		System.out.println(password.asXml());
//		System.out.println(submit.asXml());
//		System.out.println(form.asXml());
		
		webClient.setWebConnection(new HttpWebConnection (webClient) {
			public WebResponse getResponse(WebRequest request) throws IOException {
				if (request.getHttpMethod().equals(HttpMethod.POST)) {
					System.out.println("HTTP METHOD: " + request.getHttpMethod());
					System.out.println("URL: " + request.getUrl());
					System.out.println("ENCODING: " + request.getEncodingType());
					System.out.println("REQUEST BODY: " + request.getRequestBody());
					System.out.println("URL CREDS: " + request.getUrlCredentials());
					System.out.println("ADDITIONAL HEADERS: " + request.getAdditionalHeaders());
					System.out.println("REQUEST PARAMS: " + request.getRequestParameters());
				}
				
				return super.getResponse(request);
			}
		});
		
		Page responsePage = submit.click();
//		WebRequest webRequest = responsePage.getWebResponse().getWebRequest();
//		System.out.println(webRequest.getHttpMethod());
//		System.out.println(webRequest.getEncodingType());
//		System.out.println(webRequest.getRequestBody());
//		System.out.println(responsePage.getWebResponse().getWebRequest());
		final HtmlPage resultPage = HTMLParser.parseHtml(responsePage.getWebResponse(), webClient.getCurrentWindow());
		System.out.println(resultPage.getUrl());
		webClient.close();
	}
}
