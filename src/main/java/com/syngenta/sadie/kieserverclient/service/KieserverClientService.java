package com.syngenta.sadie.kieserverclient.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

import java.util.StringTokenizer;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syngenta.dda.facts.DeriveSeason;

@Service
public class KieserverClientService {

	public void runDataFile(String dataFileName, int number) throws IOException, ParseException, JSONException {

		long startTime = System.nanoTime();
		System.out.println("Test started " + startTime);
		List<DeriveSeason> deriveSeasonList = parseCountryCSVFile(dataFileName);
		createJSONStringFromList(deriveSeasonList); //to store the json array from
		// deriveSeasonList java list

		// create the paylod
		Payload payload = new Payload();
		payload.setDeriveSeasons(deriveSeasonList);

		// execute the rules against the payload
		RulesDao dao = new RulesDao();
		Response response = dao.executeRules(payload);

		// end of kieserver
		long endTime = System.nanoTime();

		// print the response
		List<DeriveSeason> evaluatedSeasonList = response.getSeasons();

		// print statistics
		System.out.println("------------------------------------");
		System.out.println(dataFileName);

		long totalTime = endTime - startTime;
		double seconds = (double) totalTime / 1_000_000_000.0;
		double milliseconds = (double) totalTime / 1_000_000.0;
		System.out.println(totalTime);
		System.out.println("milliseconds : " + milliseconds);
		System.out.println("seconds : " + seconds);
		System.out.println("per request in milliseconds : " + milliseconds / number);
		System.out.println("------------------------------------");
		System.out.println("Test Ended at " + endTime);
	}
	
	public List<DeriveSeason> runDataVirtualization(int number) throws SQLException {
		
		List<DeriveSeason> deriveSeasonList = new ArrayList<>();
		//TODO add codes
		
		return deriveSeasonList;
	}

	public void runloadTest(List<DeriveSeason> deriveSeasonList) throws IOException, ParseException, JSONException {

		long startTime = System.nanoTime();
		System.out.println("Test started " + startTime);
		
		int number = deriveSeasonList.size();
		createJSONStringFromList(deriveSeasonList); //to store the json array from
		// deriveSeasonList java list

		// create the paylod
		Payload payload = new Payload();
		payload.setDeriveSeasons(deriveSeasonList);

		// execute the rules against the payload
		RulesDao dao = new RulesDao();
		Response response = dao.executeRules(payload);

		// end of kieserver
		long endTime = System.nanoTime();

		// print the response
		List<DeriveSeason> evaluatedSeasonList = response.getSeasons();

		// print statistics
		System.out.println("------------------------------------");
		System.out.println("Load Test");

		long totalTime = endTime - startTime;
		double seconds = (double) totalTime / 1_000_000_000.0;
		double milliseconds = (double) totalTime / 1_000_000.0;
		System.out.println(totalTime);
		System.out.println("milliseconds : " + milliseconds);
		System.out.println("seconds : " + seconds);
		System.out.println("per request in milliseconds : " + milliseconds / number);
		System.out.println("------------------------------------");
		System.out.println("Test Ended at " + endTime);
	}

	// ================== private methods =========================
	/**
	 * Utility method to parse csv file to get country, month and year
	 * 
	 * @param csvFile
	 * @return
	 * @throws IOException
	 */
	public List<DeriveSeason> parseCountryCSVFile(String csvFile) throws IOException {

		Scanner scanner = new Scanner(new File(csvFile));
		List<DeriveSeason> deriveSeasonList = new ArrayList<>();
		while (scanner.hasNext()) {
			DeriveSeason deriveSeason = createDeriveSeasonRequest(scanner.nextLine());
			deriveSeasonList.add(deriveSeason);
		}
		scanner.close();

		return deriveSeasonList;
	}

	/**
	 * Utility method to create DeriveSession object from the string This is one row
	 * from the data file. It has three items country, month and year
	 * 
	 * @param line
	 * @return
	 */
	public DeriveSeason createDeriveSeasonRequest(String line) {
		StringTokenizer token = new StringTokenizer(line, ",");
		DeriveSeason deriveSeason = new DeriveSeason();
		if (token.countTokens() == 3) {
			deriveSeason.setCountry(token.nextToken());
			deriveSeason.setMonth(token.nextToken());
			deriveSeason.setHarvestYear(new Integer(token.nextToken()));
		}
		return deriveSeason;
	}

	/**
	 * Utility method to generate json data
	 * 
	 * @param deriveSeasonList
	 */
	public void createJSONStringFromList(List<DeriveSeason> deriveSeasonList) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File("D:\\Users\\s966694\\Documents\\syngenta\\deriveSeasonJSON.txt"),
					deriveSeasonList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
