package com.codingspace.freecoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.codingspace.freecoin.controller.UserController;
import com.codingspace.freecoin.model.User;

@SpringBootApplication
@EnableScheduling
@Configuration
public class FreecoinApplication {
	@Autowired
	UserController userController;

	public static void main(String[] args) {
		SpringApplication.run(FreecoinApplication.class, args);
	}

	@Scheduled(cron="0 0 0 * * *",zone = "GMT+5:30") //https://crontab.guru/
	void DailyRunAtMidnight()
	{
		User user = new User();
		System.out.println("Ran Scheduler");
		userController.add365DaysDeductionTransaction();
		userController.DistributeRoi();
		userController.checkUserActive();
		userController.getTopupTransactionsRecord();
		userController.newPairMatching();
		// userController.runPairMatch();
		userController.runWalletUpdate();
		userController.userConverter();
		userController.authConverter();
	}

}
