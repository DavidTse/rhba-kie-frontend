package com.syngenta.sadie.kieserverclient.service;

import com.syngenta.dda.facts.DeriveSeason;
import org.drools.core.command.runtime.rule.GetObjectsCommand;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieServiceResponse;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RulesDao {

	private static final Logger logger = LoggerFactory.getLogger(RulesDao.class);

	@Value("${rhdm.execution.server.url}")
	private String SERVER_URL;

	@Value("${rhdm.execution.server.username}")
	private String USER;

	@Value("${rhdm.execution.server.password}")
	private String PASSWORD;

	@Value("${rhdm.execution.server.containerId}")
	private String CONTAINER_ID;

	@Value("${rhdm.execution.server.session}")
	private String SESSION_NAME;

	@Value("${rhdm.execution.server.outIdentifier}")
	private String OUT_IDENTIFIER;

	public Response executeRules(Payload payload) {

		Response response = new Response();
		// KieServicesConfiguration kieServicesConfiguration =
		// KieServicesFactory.newRestConfiguration(SERVER_URL, USER, PASSWORD);

		KieServicesConfiguration kieServicesConfiguration = KieServicesFactory.newRestConfiguration(
				"http://sadie-rules-kieserver-dda-sadie-rules.apps.usae-2.syngentaaws.org:80/services/rest/server",
				"adminUser", "syngenta1!");

		kieServicesConfiguration.setMarshallingFormat(MarshallingFormat.JSON);
		kieServicesConfiguration.setTimeout(60000L);
		Set<Class<?>> classes = new HashSet<>();
		classes.add(DeriveSeason.class);
		kieServicesConfiguration.addExtraClasses(classes);

		KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(kieServicesConfiguration);

		ServiceResponse<ExecutionResults> rulesResponse = getRulesResponse(payload, kieServicesClient);

		@SuppressWarnings("unchecked")
		List<DeriveSeason> seasonList = (List<DeriveSeason>) rulesResponse.getResult().getValue("seasons");
		seasonList.forEach(response::addDerivedSeason);
		return response;
	}

	private ServiceResponse<ExecutionResults> getRulesResponse(Payload payload, KieServicesClient kieServicesClient) {

		GetObjectsCommand getObjectsCommand = new GetObjectsCommand();
		getObjectsCommand.setOutIdentifier("seasons");
		List<Command<?>> commands = new ArrayList<>();
		KieCommands commandsFactory = KieServices.Factory.get().getCommands();
		commands.add(commandsFactory.newInsertElements(payload.getDeriveSeasons()));
		commands.add(commandsFactory.newFireAllRules());
		commands.add(getObjectsCommand);
		BatchExecutionCommand batchExecution = commandsFactory.newBatchExecution(commands, "SyngentaStatelessSession");
		RuleServicesClient ruleServicesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
		//ServiceResponse<ExecutionResults> serviceResponse = ruleServicesClient
		//		.executeCommandsWithResults("RuleProjectDecisionCentralExample", batchExecution);
		
		ServiceResponse<ExecutionResults> serviceResponse = ruleServicesClient
				.executeCommandsWithResults(CONTAINER_ID, batchExecution);
		

		if (serviceResponse.getType() == KieServiceResponse.ResponseType.SUCCESS) {
			logger.debug("Commands executed with success! Response: " + serviceResponse.getResult());
		} else {
			logger.error("Error executing rules: ", serviceResponse.getMsg());
		}
		return serviceResponse;
	}

}

