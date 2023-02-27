package com.syngenta.sadie.kieserverclient.service;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

 
public class Worker implements Runnable{
	
	
    KieserverClientService service;
	
	public Worker(KieserverClientService service) {
		this.service=service;
	}

	public void run() {
		try {
	
			service.runDataFile("D:/Users/s955915/Documents/syngenta/data-20000.txt", 20000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException | JSONException e) {
			e.printStackTrace();
		}
		
	}
}
