package com.syngenta.sadie.kieserverclient.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.JSONException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syngenta.dda.facts.DeriveSeason;
import com.syngenta.sadie.kieserverclient.service.KieserverClientService;
import com.syngenta.sadie.kieserverclient.service.Worker;

@RestController
public class KieserverClientController {

	@Autowired
	KieserverClientService service;
	
	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	@Value("${jdg.cluster.url}")
	private String JDG_SERVER_URL;

	@GetMapping("/hello")
	public String hello() {
		return "Hello! " + JDG_SERVER_URL;
	}

	@GetMapping("/runVDBTest")	
	public String runVDBTest(@RequestParam(name = "batch") String batch) {
		try {			 
			int batchSize = Integer.parseInt(batch);
			List<DeriveSeason> deriveSeasonList = service.runDataVirtualization(batchSize);
			System.out.println("runloadVDBTest::"+batchSize+", "+ deriveSeasonList.size());
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return "load test ended";
	}
	
	@GetMapping("/runVDBLoadTest")	
	public String runLoadVDB(@RequestParam(name = "batch") String batch) {
		try {			 
			int batchSize = Integer.parseInt(batch);
			List<DeriveSeason> deriveSeasonList = service.runDataVirtualization(batchSize);
			service.runloadTest(deriveSeasonList);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "load test ended";
	}

	@GetMapping("/runload-10")
	public String runLoadTiny() {
		try {
			 
			service.runDataFile("D:/Users/s955915/Documents/syngenta/data-10.txt", 10);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "load test ended";
	}

	@GetMapping("/runload1K")
	public String runLoad1K() {
		try {
			
			service.runDataFile("D:/Users/s955915/Documents/syngenta/data-1000.txt", 1000);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException | JSONException e) {
			e.printStackTrace();
		}
		return "load test ended";
	}

	@GetMapping("/runload10K")
	public String runLoad10K() {
		try {
			 
			service.runDataFile("D:/Users/s955915/Documents/syngenta/data-10000.txt", 10000);
			 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException | JSONException e) {
			e.printStackTrace();
		}
		return "load test ended";
	}

	@GetMapping("/runload20K")
	public String runLoad20K() {
		try {
			
			service.runDataFile("D:/Users/s955915/Documents/syngenta/data-20000.txt", 20000);
			
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException | JSONException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "load test ended";
	}

	@GetMapping("/runload40K")
	public String runLoad40K() {
		try {
			
			service.runDataFile("D:/Users/s955915/Documents/syngenta/data-40000.txt", 40000);
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException | JSONException e) {
			e.printStackTrace();
		}
		return "load test ended";
	}
	
	@GetMapping("/runload50K")
	public String runLoad50K() {
		try {
			
			service.runDataFile("D:/Users/s955915/Documents/syngenta/data-50000.txt", 50000);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException | JSONException e) {
			e.printStackTrace();
		}
		return "load test ended";
	}
	
	@GetMapping("/runload100K")
	public String runLoad100K() {
		try {
			service.runDataFile("D:/Users/s955915/Documents/syngenta/data-100000.txt", 100000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException | JSONException e) {
			e.printStackTrace();
		}
		return "load test ended";
	}
	//
	@GetMapping("/runloadMultithreaded")
	public void runLoadMultithreaded() {
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		for (int i = 0; i < 10; i++) {
			futures.add((Future<Integer>) threadPoolTaskExecutor.submit(new Worker(service)));
		}
		//logger.info("******Blocking until job is  completed on runner thread********");
		for (Future<Integer> currFuture : futures) {
			try {
				currFuture.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			//	logger.error("InterruptedException", e);
			} catch (ExecutionException e) {
				e.printStackTrace();
				//logger.error("ExecutionException", e);
			}
		}		
	} 
	//
}
