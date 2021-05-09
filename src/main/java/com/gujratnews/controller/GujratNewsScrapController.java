package com.gujratnews.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class GujratNewsScrapController {

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "news", method = RequestMethod.GET)
	public ResponseEntity<Map<String, List<String>>> welcome() {
		Map<String, List<String>> test = new HashMap<>();

		Document doc;
		try {
			doc = Jsoup.connect("http://epapergujaratsamachar.com/nd/gujaratsamachar.php?issueid=GUJARAT_SUR").get();
			ArrayList<String> st = new ArrayList<>();
			ArrayList<Element> k = doc.getElementsByClass("epaper_list");
			for (Node element : k.get(0).childNodes()) {
				if (element.childNodes() != null && !element.childNodes().isEmpty()) {

					if (element.childNodes().size() > 1 && element.childNodes().get(1) != null
							&& element.childNodes().get(1).childNodes() != null) {
						Node n = element.childNodes().get(1).childNodes().get(0);
						String s = n.attr("src");
						String news[] = s.split("/");
//						System.out.println(news[news.length-1]);

						st.add(news[news.length - 1]);
					}

				}

			}

			test.put("surat", st);
			
			
			String temp = java.time.LocalDate.now().toString();  
			
			System.out.println(temp);
			
			String Date = temp.split("-")[0]+temp.split("-")[1]+temp.split("-")[2];

			String uri = "http://www.enewspapr.com/OutSourcingDatanew.php?operation=getThumbnailDetails&selectedIssueId="
					+ "GUJARAT_AHM_"+Date +"&operation=getThumbnailDetails&selectedIssueId=GUJARAT_AHM_"+Date;
			Object code = null;
			

			String uri1 = "http://www.enewspapr.com/OutSourcingDatanew.php?operation=getThumbnailDetails&selectedIssueId="
					+ "GUJARAT_MUM_"+Date +"&operation=getThumbnailDetails&selectedIssueId=GUJARAT_MUM_"+Date;

			ArrayList<String> ahmd = new ArrayList<>();

			try {
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				headers.add("user-agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
				HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

				Object response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

				JSONObject json = new JSONObject(response);
				code = json.get("body");

				String[] str = code.toString().split(",");
				for (String st1 : str) {
					if (st1.contains("imagename") && !st1.contains("News")) {

						ahmd.add(st1.split(":")[1].trim().replaceAll("\"", ""));

					}
				}
				test.put("ahmedabad", ahmd);
				

			} catch (Exception e) {
				System.out.println(e);
			}
			
			
			
			ArrayList<String> mum = new ArrayList<>();

			try {
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				headers.add("user-agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
				HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

				Object response = restTemplate.exchange(uri1, HttpMethod.GET, entity, String.class);

				JSONObject json = new JSONObject(response);
				code = json.get("body");

				String[] str = code.toString().split(",");
				for (String st1 : str) {
					if (st1.contains("imagename") && !st1.contains("News")) {

						mum.add(st1.split(":")[1].trim().replaceAll("\"", ""));

					}
				}
				test.put("mumbai", mum);
				

			} catch (Exception e) {
				System.out.println(e);
			}



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<Map<String, List<String>>>(test, new HttpHeaders(), HttpStatus.OK);
	}

}
