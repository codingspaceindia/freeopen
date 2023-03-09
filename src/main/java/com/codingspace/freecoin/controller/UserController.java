package com.codingspace.freecoin.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import com.codingspace.freecoin.model.*;
import com.codingspace.freecoin.model.reqres.SaveUserReq;
import com.codingspace.freecoin.repository.AuthRepository;
import com.codingspace.freecoin.repository.NodeRepository;
import com.codingspace.freecoin.repository.UserRepository;
import com.codingspace.freecoin.repository.WalletRepository;
import com.codingspace.freecoin.service.Mapping;
import com.codingspace.freecoin.service.UserService;
import com.codingspace.freecoin.validators.UserValidator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Creates new user. Create wallet for new user with 1.5 joiningBonus & create
 * node document in db Add 1.5 for parent ref id
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    UserValidator userValidator;

    @Autowired
    UserService userService;

	@Autowired
	Mapping mapping;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    JavaMailSender javaMailSender;

	@Autowired
	TransferWalletTransactions transferWalletTransactions;

	@Autowired
	MongoTemplate mongoTemplate;

    DecimalFormat df = new DecimalFormat("####0.00");


//-----------------------------Temporary Api Calls--------------------------



    @DeleteMapping("/temporaryDeleteApi")
    public void testDelete()
    {
    	List<Wallet> wallet = walletRepository.findAll();
    	for(Wallet w:wallet)
    	{
    		List<Transactions> transactionList = w.getTransactions();
    		for(Transactions transactions:transactionList)
    		{
    			transactions.setProcessed(false);
    			System.out.println(transactions.isProcessed());
    		}
    		walletRepository.SaveWallet(w);
    	}
    }

    @DeleteMapping("/extraTopupDelete")
    public void deleteTReq()
    {
        List<TopupRequest> topupRequestL = new ArrayList<>();
    	String userId = "61ae3a07706e8543bc32956d";
    	int count =0;
        for (TopupRequest topupRequest : topupRequestL)
        {
        	if(topupRequest.getUserId().equals(userId))
        	{
        		count+=1;

        	}
       	}
    }



    @DeleteMapping("/temporaryDeleteApiHalil")
    public void Withdraw()
    {
    	String userId = "61b0cb4211de1b30cf0a03c8";
    	List<UserStatus> userStatus = walletRepository.getAllUserStatus();
    	for(UserStatus user:userStatus)
        {

			if(user.getUserId().equals(userId))
			{
				System.out.println("Found");
				System.out.println(user.getName()+" "+user.getActive());
				/*
				user.setUserId("");
				user.setActive(false);
				user.setRefId("");
				user.setName("Test");
				*/
				walletRepository.createUserStatus(user);
			}
        }
    }


    @DeleteMapping("/temporaryDeleteApi/{userId}")
    public void testDelete2(@PathVariable(value = "userId") String userId)
    {
    	Wallet wallet = walletRepository.getByUserId(userId);

    		List<Transactions> transactionList = wallet.getTransactions();
    		for(Transactions transactions:transactionList)
    		{
    			transactions.setProcessed(false);
    			System.out.println(transactions.isProcessed());
    		}
    		walletRepository.SaveWallet(wallet);

    }

    @PutMapping("/convertAuth")
    public void authConverter()
    {
    	List<Auth> authDocs = walletRepository.getAllAuthDocs();
    	for(Auth auth:authDocs)
    	{
    		if(auth.getUserId()!=null)
    		{
    			User u = userRepository.getById(auth.getUserId());
    			System.out.println(u.getRefId());
    			auth.setEmail(u.getMailId());
    			auth.setName(u.getName());
    			auth.setRefId(u.getRefId());
    			walletRepository.createAuth(auth);
    		}
    	}
    }

    public void authConverterUser(String userId)
    {
    	List<Auth> authDocs = walletRepository.getAllAuthDocs();
    	for(Auth auth:authDocs)
    	{
    		if(auth.getUserId()!=null&&auth.getUserId().equals(userId))
    		{
    			User u = userRepository.getById(auth.getUserId());
    			System.out.println(u.getRefId());
    			auth.setEmail(u.getMailId());
    			auth.setName(u.getName());
    			auth.setRefId(u.getRefId());
    			walletRepository.createAuth(auth);
    		}
    	}
    }

    /*
    @PutMapping("/convertNode")
    public void nodeConverter()
    {
    	List<Node> authDocs = walletRepository.g;
    	for(Node auth:authDocs)
    	{
    		if(auth.getUserId()!=null)
    		{

    		}
    	}
    }


    userL=userRepository.findAll();
    	for(User usr:userL)
    	{
    		if(usr!=null&&usr.getId()!=null)
    		{
    			getPairMatchingS(usr.getId());
    		}
    	}

    if(userNode.getLeft()!=null && userNode.getRight()!=null)
    	{
    		pairMatchLeft(userNode.getLeft().getUserId());
    		pairMatchRight(userNode.getRight().getUserId());
    	}
    	if(userNode.getLeft()!=null && userNode.getRight()==null)
    	{
    		pairMatchLeft(userNode.getLeft().getUserId());
    	}
    	if(userNode.getLeft()==null && userNode.getRight()!=null)
    	{
    		pairMatchRight(userNode.getRight().getUserId());
    	}
    	response.put("leftChildSize",allLeftUsers.size());
    	response.put("rightChildSize",allRightUsers.size());
    	response.put("totalStacking",getUSDValueForCoins(pairMatchResolver()));
    	response.put("leftBusinessAmount",df.format(getLeftBusiness()));
    	response.put("rightBusinessAmount",df.format(getRightBusiness()));
    	response.put("activeRightChildSize",pairMatchRightUsers.size());
    	response.put("activeLeftChildSize",pairMatchLeftUsers.size());
    	return ResponseEntity.ok().body(response);
    }
    */

    @PutMapping("/convertUser")
    public void userConverter()
    {
    	List<User> user = userRepository.getAllUsers();
    	for(User u:user)
    	{
    		if(u.getParentId()!=null)
    		{
    			User usr = userRepository.getById(u.getParentId());
    			u.setNameOfParent(usr.getName());
    			System.out.println(u.getRefId());
    			userRepository.createUser(u);
    		}
    	}
    }

    public void userConverterSingle(String userId)
    {
    	List<User> user = userRepository.getAllUsers();
    	for(User u:user)
    	{
    		if(u.getParentId()!=null&&u.getId().equals(userId))
    		{
    			User usr = userRepository.getById(u.getParentId());
    			u.setNameOfParent(usr.getName());
    			System.out.println(u.getRefId());
    			userRepository.createUser(u);
    		}
    	}
    }



    @PostMapping("/createPreviousBalance")
    public void createPreviousBalance()
    {
    	List<User> user = userRepository.getAllUsers();
    	List<PreviousBalance> previousBalance = walletRepository.getAllPreviousBalance();
    	List<User> notCreatedList = userRepository.getAllUsers();
    	List<User> CreatedList = new ArrayList<>();
    	double contains =0;
    	double create =0;
    	double addedtolist =0;
    	for(User u:user)
    	{
    		for(PreviousBalance p :previousBalance)
    		{
    			if(u.getRefId().equals(p.getRefId()))
    			{
    				contains+=1;
    				CreatedList.add(u);
    			}
    		}
    	}
    	for(User u :user)
    	{
    		for(User usr:CreatedList)
    		{
    			if(u.getRefId().equals(usr.getRefId()))
    			{
    				notCreatedList.remove(usr);
    			}
    		}
    	}
    	int id=0;
    	for(User u :notCreatedList)
    	{
			create += 1;
    		PreviousBalance newPreviousBalance = new PreviousBalance();
//    		newPreviousBalance.setId(u.getId()+id);
    		newPreviousBalance.setRefId(u.getRefId());
    		newPreviousBalance.setName(u.getName());
            newPreviousBalance.setAvailableBalance(0);
            newPreviousBalance.setEarningBalance(0);
            newPreviousBalance.setStacking(0);
            newPreviousBalance.setUserId(u.getId());
            userRepository.setPreviousBalanceByUserId(newPreviousBalance);
            id+=1;
			System.out.println(u.getRefId());
    	}
		System.out.println("contains : " + contains);
		System.out.println("Created : " + create);
		System.out.println("id" + id);

    }

    @PutMapping("/convertWithdrawRequest")
    public void withdrawConverter()
    {
    	List<WithdrawRequest> withdrawRequest = walletRepository.getAllWithdrawRequest();
    	for(WithdrawRequest withdraws:withdrawRequest)
    	{
    		withdraws.setRefId(getRefId(withdraws));
    		System.out.println(withdraws.getUserId()+" "+withdraws.getRefId());
            if(withdraws.getUserId()!= null)
                withdraws.setName(userRepository.getById(withdraws.getId()).getName());
    	    else
                withdraws.setName(userRepository.getByRefId(withdraws.getRefId()).getName());
    		walletRepository.createWithdrawRequest(withdraws);
    	}
    }

    @PutMapping("/convertPreviousBalance")
    public void previousBalanceConverter()
    {
    	List<PreviousBalance> previousBalance = walletRepository.getAllPreviousBalance();
    	for(PreviousBalance prevBalance:previousBalance)
    	{
    		prevBalance.setRefId(getRefId(prevBalance));
    		prevBalance.setName(getName(prevBalance));
    		System.out.println(prevBalance.getUserId()+" "+prevBalance.getRefId()+" "+prevBalance.getName());
    		walletRepository.createPreviousBalance(prevBalance);
    	}
    }

    @DeleteMapping("/deleteDummyTransactions/{userId}")
    public Boolean deleteDummyTransactions(@PathVariable(value = "userId") String userId)
    {
        Wallet wallet = walletRepository.getByUserId(userId);
        List<Transactions> transactionList = wallet.getTransactions();
        int LeftChildCount =0;
        int RightChildCount =0;
        for(Transactions transaction : transactionList)
        {
        	TransactionType tType = transaction.getType();
        	String tMsg = transaction.getMessage();

        	if (tType.equals(TransactionType.C))
        	{
                String[] split = tMsg.split("/");

                if (split[0].equals("NEW_LEFT_CHILD_JOIN"))
                {
                	LeftChildCount+=1;
                	if(LeftChildCount>1)
                	{
                		System.out.println(transaction.getAmount()+"  "+transaction.getMessage());
                		transaction.setAmount(0);
                		System.out.println("Amount After: "+" "+transaction.getAmount());
                		walletRepository.createTransaction(transaction);
                	}

                }
                else if (split[0].equals("NEW_RIGHT_CHILD_JOIN"))
                {
                	RightChildCount+=1;
                	if(RightChildCount>1)
                	{
                		System.out.println(transaction.getAmount()+"  "+transaction.getMessage());
                		transaction.setAmount(0);
                		System.out.println("Amount After: "+" "+transaction.getAmount());
                		walletRepository.createTransaction(transaction);
                	}
                }
        	}

        }
        return true;

    }

    @GetMapping("runFunctionForUser/{userId}")
    public ResponseEntity<Map<String, Long>> setUserWalletData(@PathVariable(value = "userId")String userId)
    {
        Wallet wallet = walletRepository.getByUserId(userId);
        Map<String, Long> response = new HashMap<>();


        double curOfferWallet = wallet.getOfferWallet();
        double curStackingWallet = wallet.getStackingWallet();
        double curEarningWallet = wallet.getEarningsWallet();

        List<Transactions> transactionList = wallet.getTransactions();
        for(Transactions transactions :transactionList)
        {
        	if(transactions.getMessage().equals("USER_WITHDRAW"))
        	{
        		transactions.setMessage("FAILED_TRANSACTION");
        		transactions.setAmount(0);
        		transactions.setProcessed(true);
        	}
        }

        /*
        System.out.println(curOfferWallet+"****"+curStackingWallet+"****"+curEarningWallet);


        wallet.setEarningsWallet(curEarningWallet+2300000);
        wallet.setOfferWallet(curOfferWallet+2300000);
        wallet.setStackingWallet(curStackingWallet+2300000);

        System.out.println((curOfferWallet+2300000)+"****"+(curStackingWallet+2300000)+"****"+(curEarningWallet+2300000));
        */

        /*
        wallet.setEarningsWallet(0);
        wallet.setOfferWallet(0);
        wallet.setStackingWallet(0);
        wallet.setTotalStacking(0);
        wallet.setRoiAmount(0);
        */

        walletRepository.SaveWallet(wallet);

        return ResponseEntity.ok().body(response);

    }
//----------------------------------END------------------------------

//---------------------Functional Methods & APIs--------------------------

    @GetMapping("/getUserStatus")
    public List<UserStatus> test()
    {

    	List<UserStatus> previousBalanceList = walletRepository.getAllUserStatus();
    	return previousBalanceList;
    }

    public String getRefId(TopupRequest topupRequest)
    {
        User u = new User();
        u=userRepository.getById(topupRequest.getUserId());
        String userId = u.getRefId();
        return userId;
    }

    public String getRefId(WithdrawRequest withdrawRequest)
    {
        User u = new User();
        u=userRepository.getById(withdrawRequest.getUserId());
        String userId = u.getRefId();
        return userId;
    }

    public String getRefId(PreviousBalance previousBalance)
    {
        User u = new User();
        u=userRepository.getById(previousBalance.getUserId());
        String userId = u.getRefId();
        return userId;
    }

    public String getName(PreviousBalance previousBalance)
    {
        User u = new User();
        u=userRepository.getById(previousBalance.getUserId());
        String name = u.getName();
        return name;
    }



    @GetMapping("/getUserStackingDate/{userId}")
    public Date getStackingDate(@PathVariable(value = "userId")String userId)
    {
    	List<TopupRequest> topupRequestList = walletRepository.getAllAcceptedTopupRequest();
    	List<Date> dates = new ArrayList<>();
    	for(TopupRequest topupRequest:topupRequestList)
    	{
    		if(topupRequest.getUserId().equals(userId))
    		{
    			dates.add(topupRequest.getRespondedAt());
    			System.out.println(topupRequest.getRespondedAt());
    		}
    	}
    	//Optional<Date> minDate = dates.stream().min(Comparator.naturalOrder());
    	Date minDate = Collections.min(dates);
    	System.out.println("Final Date: "+minDate);
    	return minDate;

    }

    @PutMapping("/runWalletUpdate")
    public void runWalletUpdate()
    {
    	List<User> userList = userRepository.findAll();
    	for(User user:userList)
    	{
    		if(walletRepository.getByUserId(user.getId())!=null)
    		{
    			setWalletData(user.getId());
    			System.out.println(user.getRefId());
    		}
    	}
    }

    @PutMapping("/runWalletReset")
    public void runWalletReset()
    {
    	List<User> userList = userRepository.findAll();
    	for(User user:userList)
    	{
    		if(walletRepository.getByUserId(user.getId())!=null)
    		{
    			resetWalletData(user.getId());
    			System.out.println(user.getRefId());
    		}
    	}
    }

    @PutMapping("/runWalletSetdata")
    public void runWalletSetdata()
    {
    	List<User> userList = userRepository.findAll();
    	for(User user:userList)
    	{
    		if(walletRepository.getByUserId(user.getId())!=null)
    		{
    			setDataFunction(user.getId());
    			System.out.println(user.getRefId());
    		}
    	}
    }

    @GetMapping("/setWalletFunction/{userId}")
    public ResponseEntity<Map<String, Long>> setDataFunction(@PathVariable(value = "userId")String userId)
    {
        Wallet wallet = walletRepository.getByUserId(userId);

        double offerWallet = 0;
        double stackingWallet = 0;
        double earningWallet = 0;
        double bonusWallet = 0;
        double totalStacking =0;
        double count =0;
        Map<String, Long> response = new HashMap<>();
        List<Transactions> transactionList = wallet.getTransactions();
        int transactionCount =0;
        int LeftChildCount =0;
        int RightChildCount =0;
        for(Transactions transaction : transactionList)
        {
        	TransactionType tType = transaction.getType();
        	String tMsg = transaction.getMessage();

        	if (tType.equals(TransactionType.C))
        	{
                String[] split = tMsg.split("/");
                if(split[0].equals("NEW_RIGHT_CHILD_JOIN"))
        		{
        		transaction.setMessage("RIGHT_CHILD_JOIN_PROCESSED");
        		transaction.setProcessed(true);
        		System.out.println("Plop");
        		}
                if(split[0].equals("NEW_LEFT_CHILD_JOIN"))
        		{
        		transaction.setMessage("LEFT_CHILD_JOIN_PROCESSED");
        		transaction.setProcessed(true);
        		System.out.println("Plip");
        		}
        	}
        }


        System.out.println("Processing.....");
        /*
        double curOfferWallet = wallet.getOfferWallet();
        double curStackingWallet = wallet.getStackingWallet();
        double curEarningWallet = wallet.getEarningsWallet();
        double curtotalStacking =wallet.getTotalStacking();
        double curRoiAmount = wallet.getRoiAmount();
        */

        //wallet.setEarningsWallet(1400000);
        //wallet.setOfferWallet(2100000);
        //wallet.setStackingWallet(592000);
        //wallet.setTotalStacking(10000000);
        //wallet.setRoiAmount(25900);


        walletRepository.SaveWallet(wallet);


        return ResponseEntity.ok().body(response);

    }

    @PutMapping("/setDataFunction")
    public ResponseEntity<String> setDataForWallet(@RequestBody Wallet wallet)
    {
        walletRepository.SaveWallet(wallet);
        return ResponseEntity.ok().body("User Added"+wallet.getUserId());
    }

    @GetMapping("runResetFunction/{userId}")
    public ResponseEntity<Map<String, Long>> resetWalletData(@PathVariable(value = "userId")String userId)
    {
        Wallet wallet = walletRepository.getByUserId(userId);
        double offerWallet = 0;
        double stackingWallet = 0;
        double earningWallet = 0;
        double bonusWallet = 0;
        double totalStacking =0;
        double roiAmount =0;
        Map<String, Long> response = new HashMap<>();
        List<Transactions> transactionList = wallet.getTransactions();
        int transactionCount =0;
        int LeftChildCount =0;
        int RightChildCount =0;
        for(Transactions transaction : transactionList)
        {
        	TransactionType tType = transaction.getType();
        	String tMsg = transaction.getMessage();

        	if (tType.equals(TransactionType.C))
        	{
                String[] split = tMsg.split("/");

                // Active Wallet Functions
                if (split[0].equals("NEW_JOIN"))
                {
                	//offerwallet
                	offerWallet=offerWallet+transaction.getAmount();
                	double amount = transaction.getAmount();
            		//transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(false);
                }
                else if (split[0].equals("TOPUP_DONE"))
                {
                	//offerwallet
                	totalStacking = totalStacking+transaction.getAmount();
                	double amount = transaction.getAmount();
                	roiAmount += transaction.getFcAmount()*0.0037;
            		//transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(false);
                }
                else if (split[0].equals("TOPUP_SELF_RETURN"))
                {
                	//offerWallet
                	if(transactionCount==0)
                	{
                		offerWallet=offerWallet+transaction.getAmount(); //firsttimeonly
                	}
                	double amount = transaction.getAmount();
            		//transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transactionCount=transactionCount+1;
                	transaction.setProcessed(false);
                }
                else if (split[0].equals("NEW_RIGHT_CHILD_JOIN"))
                {
                	//offerwallet
                	RightChildCount+=1;
                	if(RightChildCount<2)
                	{
                		offerWallet=offerWallet+transaction.getAmount();
                	}
                	double amount = transaction.getAmount();
            		//transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(false);
                }
                else if (split[0].equals("NEW_LEFT_CHILD_JOIN"))
                {
                	//offerwallet
                	LeftChildCount+=1;
                	if(LeftChildCount<2)
                	{
                		offerWallet=offerWallet+transaction.getAmount();
                	}
                	double amount = transaction.getAmount();
            		//transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(false);
                }
                else if (split[0].equals("CHILD_TOPUP_DONE"))
                {
                	//earningswallet
                	earningWallet=earningWallet+transaction.getAmount();
                	double amount = transaction.getAmount();
            		//transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(false);
                }
                else if (split[0].equals("BUSINESS_MATCH"))
                {
                	//earningswallet
                	earningWallet=earningWallet+transaction.getAmount();
                	double amount = transaction.getAmount();
            		//transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(false);
                }
                else if (split[0].equals("ROI"))
                {
                	//stackingwallet
                	stackingWallet=stackingWallet+transaction.getFcAmount();
                	transaction.setProcessed(false);
                }
                else
                {
                	transaction.setProcessed(true);
                }
        	}

        }


        System.out.println("Processing.....");

        double curOfferWallet = wallet.getOfferWallet();
        double curStackingWallet = wallet.getStackingWallet();
        double curEarningWallet = wallet.getEarningsWallet();
        double curtotalStacking =wallet.getTotalStacking();
        double curRoiAmount = wallet.getRoiAmount();


        wallet.setEarningsWallet(0);
        wallet.setOfferWallet(0);
        wallet.setStackingWallet(0);
        wallet.setTotalStacking(0);
        wallet.setRoiAmount(0);


        walletRepository.SaveWallet(wallet);


        return ResponseEntity.ok().body(response);

    }


    @GetMapping("runFunction/{userId}")
    public ResponseEntity<Map<String, Long>> setWalletData(@PathVariable(value = "userId")String userId)
    {
        Wallet wallet = walletRepository.getByUserId(userId);
        double offerWallet = 0;
        double stackingWallet = 0;
        double earningWallet = 0;
        double bonusWallet = 0;
        double totalStacking =0;
        double roiAmount =0;
        Map<String, Long> response = new HashMap<>();
        List<Transactions> transactionList = wallet.getTransactions();
        int transactionCount =0;
        int LeftChildCount =0;
        int RightChildCount =0;
        int processedRightChildJoinCount =0;
        int processedLeftChildJoinCount =0;
        for(Transactions transaction : transactionList)
        {
        	TransactionType tType = transaction.getType();
        	String tMsg = transaction.getMessage();

        	if (tType.equals(TransactionType.C)&&transaction.isProcessed()==false)
        	{
                String[] split = tMsg.split("/");

                // Active Wallet Functions
                if (split[0].equals("NEW_JOIN"))
                {
                	//offerwallet
                	offerWallet=offerWallet+transaction.getAmount();
                	double amount = transaction.getAmount();
            		transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(true);
                }
                else if (split[0].equals("TOPUP_DONE"))
                {
                	//offerwallet
                	totalStacking = totalStacking+transaction.getAmount();
                	double amount = transaction.getAmount();
                	transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	roiAmount += transaction.getFcAmount()*0.0037;
                	transaction.setProcessed(true);
                }
                else if (split[0].equals("TOPUP_SELF_RETURN"))
                {
                	//offerWallet
			        int processedCount=0;
                	for(Transactions topupTrans : transactionList)
                	{
                		String childMsg = topupTrans.getMessage();
                		if(childMsg.equals("TOPUP_SELF_RETURN_PROCESSED"))
                		{
                			processedCount+=1;
                		}
                	}
                	if(processedCount==0)
                	{
                		offerWallet=offerWallet+transaction.getAmount(); //firsttimeonly
                	}
                	double amount = transaction.getAmount();
            		transaction.setFcAmount(getCoinValueForUSDLocal(amount));
			transaction.setMessage("TOPUP_SELF_RETURN_PROCESSED");
                	transaction.setProcessed(true);
                }
                else if (split[0].equals("NEW_RIGHT_CHILD_JOIN"))
                {
                	//offerwallet
                	for(Transactions CJRTransaction : transactionList)
                	{
                		String childMsg = CJRTransaction.getMessage();
                		if(childMsg.equals("RIGHT_CHILD_JOIN_PROCESSED"))
                		{
                			processedRightChildJoinCount+=1;
                		}
                	}

                	RightChildCount+=1;
                	if(RightChildCount<2&&processedRightChildJoinCount==0)
                	{
                		offerWallet=offerWallet+transaction.getAmount();
                		transaction.setMessage("RIGHT_CHILD_JOIN_PROCESSED");
                	}
                	double amount = transaction.getAmount();
            		transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(true);
                }
                else if (split[0].equals("NEW_LEFT_CHILD_JOIN"))
                {
                	//offerwallet
                	for(Transactions CJLTransaction : transactionList)
                	{
                		String childMsg = CJLTransaction.getMessage();
                		if(childMsg.equals("LEFT_CHILD_JOIN_PROCESSED"))
                		{
                			processedLeftChildJoinCount+=1;
                		}
                	}
                	LeftChildCount+=1;
                	if(LeftChildCount<2&&processedLeftChildJoinCount==0)
                	{
                		offerWallet=offerWallet+transaction.getAmount();
                		transaction.setMessage("LEFT_CHILD_JOIN_PROCESSED");
                	}
                	double amount = transaction.getAmount();
            		transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(true);
                }
                else if (split[0].equals("CHILD_TOPUP_DONE"))
                {
                	//earningswallet
                	earningWallet=earningWallet+transaction.getAmount();
                	double amount = transaction.getAmount();
            		transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(true);
                }
                else if (split[0].equals("BUSINESS_MATCH"))
                {
                	//earningswallet
                	earningWallet=earningWallet+transaction.getAmount();
                	double amount = transaction.getAmount();
            		transaction.setFcAmount(getCoinValueForUSDLocal(amount));
                	transaction.setProcessed(true);
                }
                else if (split[0].equals("ROI"))
                {
                	//stackingwallet
                	stackingWallet=stackingWallet+transaction.getFcAmount();
                	transaction.setProcessed(true);
                }
                else
                {
                	transaction.setProcessed(false);
                }
        	}

        }


        System.out.println(getCoinValueForUSDLocal(earningWallet)+"****"+getCoinValueForUSDLocal(offerWallet)+"****"+getCoinValueForUSDLocal(stackingWallet)+"****"+getCoinValueForUSDLocal(totalStacking));

        double curOfferWallet = wallet.getOfferWallet();
        double curStackingWallet = wallet.getStackingWallet();
        double curEarningWallet = wallet.getEarningsWallet();
        double curtotalStacking =wallet.getTotalStacking();
        double curRoiAmount = wallet.getRoiAmount();

        wallet.setEarningsWallet(curEarningWallet+getCoinValueForUSDLocal(earningWallet));
        wallet.setOfferWallet(curOfferWallet+getCoinValueForUSDLocal(offerWallet));
        wallet.setStackingWallet(curStackingWallet+stackingWallet);
        wallet.setTotalStacking(curtotalStacking+getCoinValueForUSDLocal(totalStacking));
        wallet.setRoiAmount(curRoiAmount+roiAmount);

        walletRepository.SaveWallet(wallet);
        return ResponseEntity.ok().body(response);

    }
//----------------------------------END-------------------------------

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody SaveUserReq reqData) {

        Boolean isLeft = reqData.getIsLeft();
        Boolean isRight = reqData.getIsRight();
        double joiningBonus = 1.5;
        Map<String, Object> response = new HashMap<String, Object>();


        if (userRepository.getUserByMobile(reqData.getMobileNumber()) != null) {
        response.put("isUserCreated", false);
        response.put("message", "Mobile number already exists");
        return ResponseEntity.ok().body(response);
        }

        if (userRepository.getUserByEmailId(reqData.getMailId()) != null) {
        response.put("isUserCreated", false);
        response.put("message", "Email ID already exists");
        return ResponseEntity.ok().body(response);
        }



        User user = new User();

        // user.setParentId(reqData.getParentId());
        user.setParentRefId(reqData.getParentRefId());
        user.setName(reqData.getName());
        user.setAddress(reqData.getAddress());
        user.setGender(reqData.getGender());
        user.setMailId(reqData.getMailId());
        user.setCountry(reqData.getCountry());
        user.setCity(reqData.getCity());
        user.setMobileNumber(reqData.getMobileNumber());
        user.setDOB(reqData.getDOB());
        user.setRelation(reqData.getRelation());
        user.setPIN(reqData.getPIN());
        user.setNominee(reqData.getNominee());
        user.setJoiningDate(java.util.Calendar.getInstance().getTime());

        String parentRefId = reqData.getParentRefId();
        User parentData = userRepository.getByRefId(reqData.getParentRefId());

        Node referalParentDocs = nodeRepository.getByUserId(parentData.getId());
        String referalParentRightUserId= null;
        String referalParentLeftUserId =null;

        if(referalParentDocs != null){
            if(referalParentDocs.getLeft() !=null){
                referalParentLeftUserId =referalParentDocs.getLeft().getUserId();
            }

            if(referalParentDocs.getRight() != null){
                referalParentRightUserId =referalParentDocs.getRight().getUserId();
            }
        }

        // check parent data is exists
        if (parentData == null) {
            response.put("message", "Parent data not found");
            response.put("isUserCreated", false);
            return ResponseEntity.badRequest().body(response);
        }

        String parentId = parentData.getId();

        String userRefId;
        for (;;) {
            int temp = userService.getLongRandomNumber();
            String generatedRefId = "FM" + temp;
            User isExists = userRepository.getByRefId(generatedRefId);
            if (isExists == null) {
                userRefId = generatedRefId;
                break;
            }
        }

        user.setRefId(userRefId);
        // user.setRefId(reqData.getUserRefId());
        user.setStatus("A");
        user.setParentId(parentData.getId());
        user.setParentRefId(parentData.getRefId());

        userRepository.save(user);
        String userId = user.getId();

        // String userPassword = reqData.getPassword();
        String userPassword = userService.getLongRandomNumber() + "";
        authRepository.createAuth(userId, userPassword);

        userRepository.createBankDetails(userId);

        // Create node data for user
        Node userNode = new Node(userId);
        nodeRepository.save(userNode);
        Node parentNodeData = nodeRepository.getByUserId(parentData.getId());

        // for left child
        if (isLeft && !isRight) {
            if (parentNodeData.getLeft() != null) {
                parentNodeData = userService.getParentNodeLeft(parentNodeData, userNode);
            }
            parentNodeData.setLeft(userNode);
            nodeRepository.updateNodeLeft(parentNodeData, userNode);
        }
        // for right child
        if (isRight && !isLeft) {
            if (parentNodeData.getRight() != null) {
                parentNodeData = userService.getParentNodeRight(parentNodeData, userNode);
            }
            parentNodeData.setRight(userNode);
            nodeRepository.updateNodeRight(parentNodeData, userNode);
        }

        // add 1.5 in parent wallet
        String parentTransactionMessage = "";
        if (isLeft) {
            parentTransactionMessage = TransactionMessage.NEW_LEFT_CHILD_JOIN + "/" + userId;
        } else {
            parentTransactionMessage = TransactionMessage.NEW_RIGHT_CHILD_JOIN + "/" + userId;
        }

        if(referalParentLeftUserId == null || referalParentRightUserId == null){
            walletRepository.addTransaction(parentId, TransactionType.C, joiningBonus, parentTransactionMessage);
        }

        // Create wallet data for user
        Wallet userWallet = new Wallet(userId, joiningBonus);//argument amount is set to 0 from joiningBonus
        // add 1.5 for joining bonus for new user ---> //**//Joining Bonus was disabled for new users from December 1st, 2022.....Application Edited on 1 Dec 2022 01:50//***//
        String walletMessage = TransactionMessage.NEW_JOIN + "";   //Comment this line to disable joining bonus for new users
        Transactions newTransaction = new Transactions(joiningBonus, TransactionType.C, walletMessage);//Comment this line to disable joining bonus for new users
        ArrayList<Transactions> newTransactionList = new ArrayList<Transactions>(0);
        newTransactionList.add(newTransaction);//Comment this line to disable joining bonus for new users
        userWallet.setTransactions(newTransactionList);
        walletRepository.save(userWallet);
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        setWalletData(userId);
        setWalletData(parentId);
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        //Create Bonus wallet
        BonusWallet bonusWallet = new BonusWallet(userId,0);
        walletRepository.createBonusWallet(bonusWallet);

        response.put("mailId", reqData.getMailId());
        response.put("password", userPassword);
        response.put("isUserCreated", true);
        response.put("userRefId", userRefId);
        response.put("userId", userId);

        // get placement parent refId
        User placementParentData = userRepository.getById(parentNodeData.getUserId());
        if (placementParentData != null) {
            response.put("placementRefId", placementParentData.getRefId());
            response.put("placementName", placementParentData.getName());
            response.put("name", placementParentData.getName());
        }
        authConverterUser(userId);
        userConverterSingle(userId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody SaveUserReq reqData) {

        Boolean isLeft = reqData.getIsLeft();
        Boolean isRight = reqData.getIsRight();
        double joiningBonus = 1.5;
        Map<String, Object> response = new HashMap<String, Object>();

        /*
        if (userRepository.getUserByMobile(reqData.getMobileNumber()) != null)
        response.put("isUserCreated", false);
        response.put("message", "Mobile number already exists");
        return ResponseEntity.ok().body(response);
        }


        if (userRepository.getUserByEmailId(reqData.getMailId()) != null) {
        response.put("isUserCreated", false);
        response.put("message", "Email ID already exists");
        return ResponseEntity.ok().body(response);
        }
        */

        User user = new User();

        // user.setParentId(reqData.getParentId());
        user.setParentRefId(reqData.getParentRefId());
        user.setName(reqData.getName());
        user.setAddress(reqData.getAddress());
        user.setGender(reqData.getGender());
        user.setMailId(reqData.getMailId());
        user.setCountry(reqData.getCountry());
        user.setCity(reqData.getCity());
        user.setMobileNumber(reqData.getMobileNumber());
        user.setJoiningDate(java.util.Calendar.getInstance().getTime());

        String parentRefId = reqData.getParentRefId();
        User parentData = userRepository.getByRefId(reqData.getParentRefId());

        Node referalParentDocs = nodeRepository.getByUserId(parentData.getId());
        String referalParentRightUserId= null;
        String referalParentLeftUserId =null;

        if(referalParentDocs != null){
            if(referalParentDocs.getLeft() !=null){
                referalParentLeftUserId =referalParentDocs.getLeft().getUserId();
            }

            if(referalParentDocs.getRight() != null){
                referalParentRightUserId =referalParentDocs.getRight().getUserId();
            }
        }

        // check parent data is exists
        if (parentData == null) {
            response.put("message", "Parent data not found");
            response.put("isUserCreated", false);
            return ResponseEntity.badRequest().body(response);
        }

        String parentId = parentData.getId();

        String userRefId = reqData.getUserRefId();


        user.setRefId(userRefId);
        // user.setRefId(reqData.getUserRefId());
        user.setStatus("A");
        user.setParentId(parentData.getId());
        user.setParentRefId(parentData.getRefId());

        userRepository.save(user);
        String userId = user.getId();

        // String userPassword = reqData.getPassword();
        String userPassword = reqData.getPassword();
        authRepository.createAuth(userId, userPassword);

        userRepository.createBankDetails(userId);

        // Create node data for user
        Node userNode = new Node(userId);
        nodeRepository.save(userNode);
        Node parentNodeData = nodeRepository.getByUserId(parentData.getId());

        // for left child
        if (isLeft && !isRight) {
            if (parentNodeData.getLeft() != null) {
                parentNodeData = userService.getParentNodeLeft(parentNodeData, userNode);
            }
            parentNodeData.setLeft(userNode);
            nodeRepository.updateNodeLeft(parentNodeData, userNode);
        }
        // for right child
        if (isRight && !isLeft) {
            if (parentNodeData.getRight() != null) {
                parentNodeData = userService.getParentNodeRight(parentNodeData, userNode);
            }
            parentNodeData.setRight(userNode);
            nodeRepository.updateNodeRight(parentNodeData, userNode);
        }

        // add 1.5 in parent wallet
        String parentTransactionMessage = "";
        if (isLeft) {
            parentTransactionMessage = TransactionMessage.NEW_LEFT_CHILD_JOIN + "/" + userId;
        } else {
            parentTransactionMessage = TransactionMessage.NEW_RIGHT_CHILD_JOIN + "/" + userId;
        }

        if(referalParentLeftUserId == null || referalParentRightUserId == null){
            walletRepository.addTransaction(parentId, TransactionType.C, joiningBonus, parentTransactionMessage);
        }

        // Create wallet data for user
        Wallet userWallet = new Wallet(userId,joiningBonus); //argument amount was set to 0 from joiningBonus 
        // add 1.5 for joining bonus for new user ---> //**//Joining Bonus was disabled for new users from December 1st, 2022.....Application Edited on 1 Dec 2022 01:50//***//
        String walletMessage = TransactionMessage.NEW_JOIN + "";//Comment this line to disable joining bonus for new users
        Transactions newTransaction = new Transactions(joiningBonus, TransactionType.C, walletMessage);//Comment this line to disable joining bonus for new users
        ArrayList<Transactions> newTransactionList = new ArrayList<Transactions>(0);
        newTransactionList.add(newTransaction);//Comment this line to disable joining bonus for new users
        userWallet.setTransactions(newTransactionList);
        walletRepository.save(userWallet);
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        setWalletData(parentId);
        setWalletData(userId);
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        //Create Bonus wallet
        BonusWallet bonusWallet = new BonusWallet(userId,0);
        walletRepository.createBonusWallet(bonusWallet);

        response.put("mailId", reqData.getMailId());
        response.put("password", userPassword);
        response.put("isUserCreated", true);
        response.put("userRefId", userRefId);
        response.put("userId", userId);

        // get placement parent refId
        User placementParentData = userRepository.getById(parentNodeData.getUserId());
        if (placementParentData != null) {
            response.put("placementRefId", placementParentData.getRefId());
            response.put("placementName", placementParentData.getName());
            response.put("name", placementParentData.getName());
        }
		return ResponseEntity.ok().body(response);
    }

//---------------------Active User Check------------------------------
    @PostMapping("/userActiveCheck")
    public void checkUserActive()
    {
        List<TopupRequest> topupRequestList= walletRepository.getAllAcceptedTopupRequest();
        List<User> userList = userRepository.getAllUsers();

        	for(User user:userList)
        	{
        		UserStatus userStatus = new UserStatus();
        		for(TopupRequest topups:topupRequestList)
                {
        			if(user.getId().equals(topups.getUserId()))
        			{
        				userStatus.setName(user.getName());
        				userStatus.setRefId(user.getRefId());
        				userStatus.setUserId(user.getId());
        				userStatus.setActive(true);
        				System.out.println(userStatus.getName()+" "+userStatus.getActive());
        				walletRepository.createUserStatus(userStatus);
        			}
                }
        	}
        }


//----------------------------END--------------------------------------

//-----------------Get Transactional Data-----------------------

    @GetMapping("/transactionData/{userId}")
    public ResponseEntity<Map<Transactions,String>> getTransactionData(@PathVariable(value = "userId") String userId)
    {
    	Wallet userWallet = walletRepository.getByUserId(userId);
    	List<Transactions> transactions = userWallet.getTransactions();
    	Map<Transactions,String> response = new HashMap<>();

        for (Transactions transaction : transactions)
        {
            TransactionType tType = transaction.getType();
            String tMsg = transaction.getMessage();

            // Credit Function
            if (tType.equals(TransactionType.C)) {
                String[] split = tMsg.split("/");

                // Active Wallet Functions
                if (split[0].equals("NEW_JOIN")) {
                    response.put(transaction, "New Join Credit");
                } else if (split[0].equals("TOPUP_DONE")) {
                	response.put(transaction, "TopUp");
                } else if (split[0].equals("TOPUP_SELF_RETURN")) {
                	response.put(transaction, "TopUp ROI");
                }

                // Earnings Wallet Functions
                else if (split[0].equals("NEW_RIGHT_CHILD_JOIN") || split[0].equals("NEW_LEFT_CHILD_JOIN")) {
                	response.put(transaction, "Child Join Credit");;
                } else if (split[0].equals("CHILD_TOPUP_DONE")) {
                	response.put(transaction, "Child's Topup Credit");
                } else if (split[0].equals("ROI")) {
                	response.put(transaction, "Daily ROI Credit");
                } else {
                    //System.out.println("Error");
                }
            }
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/stackingTransactionData/{userId}")
    public List<Transactions> getStackingTransactionData(@PathVariable(value = "userId") String userId)
    {
    	Wallet userWallet = walletRepository.getByUserId(userId);
    	List<Transactions> transactions = userWallet.getTransactions();
    	List<Transactions> response = new ArrayList<>();

        for (Transactions transaction : transactions)
        {
            TransactionType tType = transaction.getType();
            String tMsg = transaction.getMessage();

            // Credit Function
            if (tType.equals(TransactionType.C)) {
                String[] split = tMsg.split("/");
                if (split[0].equals("ROI")) {
                	response.add(transaction);
                } else {
                    //System.out.println("Error");
                }
            }
        }

        return response;
    }
    ////
    @GetMapping("/earningsTransactionData/{userId}")
    public List<Transactions> getEarningsTransactionData(@PathVariable(value = "userId") String userId)
    {
    	Wallet userWallet = walletRepository.getByUserId(userId);
    	List<Transactions> transactions = userWallet.getTransactions();
    	List<Transactions> response = new ArrayList<>();

        for (Transactions transaction : transactions)
        {
            TransactionType tType = transaction.getType();
            String tMsg = transaction.getMessage();

            // Credit Function
            if (tType.equals(TransactionType.C)) {
                String[] split = tMsg.split("/");
                if (split[0].equals("CHILD_TOPUP_DONE")) {
                	User u = userRepository.getById(split[1]);
                	String msg = transaction.getMessage();
                	transaction.setMessage(msg+"/"+u.getRefId()+"/"+"Child's Topup Done");
                	response.add(transaction);
                }
                else if(split[0].equals("CHILD_TOPUP_DONE")) {
                	User u = userRepository.getById(split[1]);
                	String msg = transaction.getMessage();
                	transaction.setMessage(msg+"/"+u.getRefId()+"/"+"Child's Topup Done");
                	response.add(transaction);
                }
                else if (split[0].equals("BUSINESS_MATCH"))
                {
                	//earningswallet
                	String msg = transaction.getMessage();
                	transaction.setMessage("_Done"+"/"+""+"/"+"Pair-Matching");
                	response.add(transaction);
                }
            }
        }

        return response;
    }

    @GetMapping("/offerWalletTransactionData/{userId}")
    public List<Transactions> getOfferTransactionData(@PathVariable(value = "userId") String userId)
    {
    	Wallet userWallet = walletRepository.getByUserId(userId);
    	List<Transactions> transactions = userWallet.getTransactions();
    	List<Transactions> response = new ArrayList<>();
    	int LeftChildCount =0;
        int RightChildCount =0;
        int topupCount =0;
	int topupCountP =0;
        for (Transactions transaction : transactions)
        {
            TransactionType tType = transaction.getType();
            String tMsg = transaction.getMessage();
            System.out.println(tMsg);
            // Credit Function
            if (tType.equals(TransactionType.C)) {
                String[] split = tMsg.split("/");
                if (split[0].equals("RIGHT_CHILD_JOIN_PROCESSED")) {
                	RightChildCount+=1;
                	//User u = userRepository.getById(split[1]);
                	if(RightChildCount<2)
                	{
                		String msg = transaction.getMessage();
                    	transaction.setMessage(msg+"/"+""+"/"+"New Right Child Join");
                		response.add(transaction);
                	}

                }
                if (split[0].equals("LEFT_CHILD_JOIN_PROCESSED")) {
                	LeftChildCount+=1;
                	//User u = userRepository.getById(split[1]);
                	if(LeftChildCount<2)
                	{
                		String msg = transaction.getMessage();
                    	transaction.setMessage(msg+"/"+""+"/"+"Left Right Child Join");
                		response.add(transaction);
                	}
                }
                if (split[0].equals("TOPUP_SELF_RETURN")) {
                	topupCount+=1;
                	if(topupCount==1)
                	{
                		response.add(transaction);
                	}
                }
		if(split[0].equals("TOPUP_SELF_RETURN_PROCESSED")){
                	topupCountP+=1;
                	if(topupCount==0&&topupCountP==1)
                	{
        			response.add(transaction);
                	}
        	}
                if (split[0].equals("NEW_JOIN")) {
            		response.add(transaction);
                }
                else {
                    //System.out.println("Error");
                }
            }
        }
        return response;
    }


//----------------------------END-----------------------------

// ------------------ROI Distributor------------------------
    double roiAmount = 0;
    double roiFcAmount =0;
    @PostMapping("/roiTopup")
    public ResponseEntity<String> DistributeRoi() {
        List<Wallet> wallets = walletRepository.getAll();

        for (Wallet wallet : wallets) {
            if (wallet.getUserId()!=null) {
            	    roiFcAmount = wallet.getRoiAmount();
            	    if(roiFcAmount==0)
            	    {
            	    	System.out.println("Inactive Account");
            	    }
            	    else
            	    {
            	    	roiTopup(wallet.getUserId());
                        System.out.println("User Id: "+wallet.getUserId()+" "+"Amount :"+roiFcAmount);
            	    }
            }
        }
        return ResponseEntity.ok().body("ROI Main Function Run");
    }

    @PostMapping("/roiTopupSpecial")
    public ResponseEntity<String> DistributeRoiSpecial() {

            	    roiFcAmount = 37000;
                    roiTopup("61ab93c768f7df34cf87e596");
                    System.out.println("User Id: 61ab93c768f7df34cf87e596"+" "+"Amount :"+roiFcAmount);


        return ResponseEntity.ok().body("ROI Main Function Run");
    }


    @GetMapping("/getUserStatistic/{userId}")
    public ResponseEntity<Map<String,Double>> getRoiAmount(@PathVariable(value = "userId") String userId) {
        List<TopupRequest> topups = new ArrayList<>();
        Map<String,Double> response = new HashMap<>();
        Wallet wallet = walletRepository.getByUserId(userId);
        response.put("roiAmount",wallet.getRoiAmount());
        response.put("stackingAmount",wallet.getTotalStacking());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getUserTopupDate/{userId}")
    public ResponseEntity<Map<String,Date>> getTopupDateAmount(@PathVariable(value = "userId") String userId) {
        List<TopupRequest> topups = new ArrayList<>();
        Map<String,Date> response = new HashMap<>();

        Date stackingDate = null;
        topups = walletRepository.getAllTopupRequest();
        for (TopupRequest topupRequest : topups) {

            if (topupRequest.getUserId().equals(userId)&&topupRequest.getStatus().equals("A")) {
            		stackingDate = topupRequest.getRespondedAt();
            		System.out.println(stackingDate);
            }
        }
        response.put("date",stackingDate);

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<String> roiTopup(@RequestBody String userId) {
        User user = userRepository.getById(userId);
        walletRepository.addRoiTransaction(user.getId(), TransactionType.C, roiAmount,roiFcAmount, TransactionMessage.ROI + "");
        setWalletData(userId);
        return ResponseEntity.ok().body("ROI Topup done");
    }


    @GetMapping("/roiReport/{id}")
    public ResponseEntity<List<Transactions>> getRoiReport(@PathVariable(value = "id") String id) {

        List<Transactions> allTransactions = walletRepository.getByUserId(id).getTransactions();
        List<Transactions> roiTransactions = new ArrayList<Transactions>();

        allTransactions.forEach(eachTransaction -> {
            if (eachTransaction.getMessage().equals(TransactionMessage.TOPUP_SELF_RETURN + "")) {
                roiTransactions.add(eachTransaction);
            }
        });
        return ResponseEntity.ok().body(roiTransactions);
    }

//-----------------------End--------------------------------


// ----------------TOPUP and Withdraw API--------------

    @PostMapping("/topup")
    public ResponseEntity<String> topup(@RequestBody TopupRequest topupRequest) {

        User user = userRepository.getById(topupRequest.getUserId());

        if (topupRequest.getStatus().equals("A")) {
            // log to user's wallet
            walletRepository.addTransaction(user.getId(), TransactionType.C, topupRequest.getAmount(),
                    TransactionMessage.TOPUP_DONE + "");

            // log 10% to parent's wallet
            double credit = 1.5;
            double tenPercent = topupRequest.getAmount()/10;

           // log to 10% to user's wallet due to topup (Self returns)
            walletRepository.addTransaction(user.getId(), TransactionType.C, credit,
                    TransactionMessage.TOPUP_SELF_RETURN + "");

            // log 10% to parent's wallet
            walletRepository.addTransaction(user.getParentId(), TransactionType.C, tenPercent,
                    TransactionMessage.CHILD_TOPUP_DONE + "/" + user.getId());
            setWalletData(user.getId());
            setWalletData(user.getParentId());
//            checkUserActive();

        }  else {
            return ResponseEntity.ok().body("Error");
        }

        return ResponseEntity.ok().body("Topup done");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest withdrawRequest) {

        User user = userRepository.getById(withdrawRequest.getUserId());

        if (withdrawRequest.getStatus().equals("A")) {
            // log to user's wallet
            // log to user's wallet
            double amount=0;
            if(withdrawRequest.getCurrencyType().equals("coin"))//1.withdraw 2.transaction
            {
            	amount=withdrawRequest.getAmount();
            }
            else if(withdrawRequest.getCurrencyType().equals("rupee"))
            {
            	amount=getCoinValueForINRLocal(withdrawRequest.getAmount());
            }
            walletRepository.addWithDrawTransaction(user.getId(), TransactionType.D, amount,
                    TransactionMessage.USER_WITHDRAW + "");
            setWalletData(withdrawRequest.getUserId());
			return ResponseEntity.ok().body("Withdraw done");
        } else if (withdrawRequest.getStatus().equals("P")) {
            return ResponseEntity.badRequest()
                    .body("Withdraw status is pending, Wait for the Admin to review your Request");
        } else if (withdrawRequest.getStatus().equals("R")) {
            return ResponseEntity.badRequest()
                    .body("Withdraw Transaction is Rejected, Contact the Admin to review your Request");
        } else {
            return ResponseEntity.badRequest().body("Transaction Error");
        }
    }

	@PostMapping("/transferWithdraw")
	public ResponseEntity<String> transferWithdraw(@RequestBody WithdrawRequest withdrawRequest) {

        if(withdrawRequest.getUserId()!= null)
            withdrawRequest.setName(userRepository.getById(withdrawRequest.getUserId()).getName());
    	else
            withdrawRequest.setName(userRepository.getByRefId(withdrawRequest.getRefId()).getName());
		withdrawRequest.setEarningsWalletDeduction(0);
		withdrawRequest.setOfferWalletDeduction(0);
		withdrawRequest.setStackingWalletDeduction(0);
		withdrawRequest.setAction("Transfer Withdraw");
		walletRepository.createWithdrawRequest(withdrawRequest);
		walletRepository.acceptWithdrawal(withdrawRequest);
		User user = userRepository.getByRefId(withdrawRequest.getRefId());
		walletRepository.addWithDrawTransaction(user.getId(), TransactionType.D, withdrawRequest.getAmount(),
				TransactionMessage.TRANSFER_WITHDRAW + "");
		setWalletData(withdrawRequest.getUserId());
		PreviousBalance previousBalance = userRepository.getUserPreviousBalanceByUserId(user.getId());
		previousBalance.setAvailableBalance(previousBalance.getAvailableBalance()+withdrawRequest.getAmount());
		userRepository.setPreviousBalanceByUserId(previousBalance);
		return ResponseEntity.ok().body("Transaction Successful");


	}

	@PostMapping("/internalTransfer")
	public ResponseEntity<String> internalTransfer(@RequestBody TransferWalletTransactions transferWalletTransactions){
		User fromUser = userRepository.getByRefId(transferWalletTransactions.getFromRefId());
		User toUser = userRepository.getByRefId(transferWalletTransactions.getToRefId());
		if (fromUser == toUser){
			return ResponseEntity.badRequest().body("Invalid request");
		}
		PreviousBalance fromPreviousBalance = userRepository.getUserPreviousBalanceByUserId(fromUser.getId());
		PreviousBalance toPreviousBalance = userRepository.getUserPreviousBalanceByUserId(toUser.getId());
		if (fromPreviousBalance.getAvailableBalance() < transferWalletTransactions.getAmount()){
			return ResponseEntity.badRequest().body("Insufficient Balance");
		}
		else
			{
				fromPreviousBalance.setAvailableBalance(fromPreviousBalance.getAvailableBalance() - transferWalletTransactions.getAmount());
				toPreviousBalance.setAvailableBalance(toPreviousBalance.getAvailableBalance() + transferWalletTransactions.getAmount());
				userRepository.setPreviousBalanceByUserId(fromPreviousBalance);
				userRepository.setPreviousBalanceByUserId(toPreviousBalance);
				transferWalletTransactions.setName(fromUser.getName());
				transferWalletTransactions.setFromRefId(transferWalletTransactions.getFromRefId());
				transferWalletTransactions.setToRefId(transferWalletTransactions.getToRefId());
				transferWalletTransactions.setFromUserId(fromUser.getId());
				transferWalletTransactions.setToUserId(toUser.getId());
				transferWalletTransactions.setAmount(transferWalletTransactions.getAmount());
				transferWalletTransactions.setRespondedAt(Calendar.getInstance().getTime());
				System.out.println(transferWalletTransactions.getFromRefId());
				System.out.println(transferWalletTransactions.getToRefId());
				System.out.println(transferWalletTransactions.getFromUserId());
				System.out.println(transferWalletTransactions.getToUserId());
				System.out.println(transferWalletTransactions.getAmount());
				System.out.println(transferWalletTransactions.getRespondedAt());
				walletRepository.createTransferWalletTransactions(transferWalletTransactions);
				return ResponseEntity.ok("Transfer Successful");
			}


	}
    // ------------------------END--------------------------------

    //--------------------Bonus Wallet Functions------------------
    @GetMapping("/getBonusWallet/for/{userId}")
    public BonusWallet getBonusWallet(@PathVariable(value = "userId")String userId)
    {
    	return walletRepository.getBonusWalletByUserId(userId);
    }

    @GetMapping("/getBonusWallets")
    public List<BonusWallet> getBonusWallets()
    {
    	return walletRepository.getAllBonusWallets();
    }

    @PostMapping("/bonusWallet/create")
    public BonusWallet createTopupRequest(@RequestBody BonusWallet bonusWallet) {
        return walletRepository.createBonusWallet(bonusWallet);
    }

    @PutMapping("/updateBonusWalletAmount/as/{amount}")
    public ResponseEntity<BonusWallet> updateBonusWallet(@RequestBody BonusWallet bonusWallet, @PathVariable(value = "amount")double amount)
    {
    	walletRepository.updateBonusWalletAmount(bonusWallet, amount);
    	return ResponseEntity.ok().body(bonusWallet);
    }
    //-------------------------END--------------------------------

    //----------------------PanRecord Functions--------------------
    @PostMapping("/panRecord/create")
    public PanRecord createPanrecord(@RequestBody PanRecord panRecord) {
        return walletRepository.createPanRecord(panRecord);
    }

    @GetMapping("/getPanRecord/for/{userId}")
    public PanRecord getPanRecord(@PathVariable(value = "userId")String userId)
    {
    	return walletRepository.getPanRecord(userId);
    }

    @GetMapping("/getPanRecords")
    public List<PanRecord> getPanRecords()
    {
    	return walletRepository.getPanRecords();
    }
    //-------------------------END--------------------------------

  //----------------------SupportRecord Functions--------------------
    @PostMapping("/supportRecord/create")
    public SupportRecord createSupportRecordUser(@RequestBody SupportRecord supportRecord) {
    	User u = userRepository.getByRefId(supportRecord.getRefId());
    	if(u.getMobileNumber()!=null&&supportRecord.getPhoneNumber()==null)
    	{
    		supportRecord.setPhoneNumber(u.getMobileNumber());
    	}
    	if(u.getMailId()!=null&&supportRecord.getEmail()==null)
    	{
    		supportRecord.setEmail(u.getMailId());
    	}
        return walletRepository.createSupportRecord(supportRecord);
    }

    @GetMapping("/getUnprocessedSupportRecords")
    public List<SupportRecord> getSupportRecords()
    {
    	return walletRepository.getAllSupportRecords();
    }

    @GetMapping("/getAcceptedSupportRecords")
    public List<SupportRecord> getAcceptedSupportRecords()
    {
    	return walletRepository.getAllAcceptedSupportRecords();
    }

    @PutMapping("/supportRecord/accept")
    public SupportRecord supportRecordAccept(@RequestBody SupportRecord supportRecord) {
        return walletRepository.acceptSupportQuery(supportRecord);
    }
    //-------------------------END--------------------------------

    // --------------Pair Matching and Business-------------------

    List<User> allLeftUsers = new ArrayList<>();
    List<User> allRightUsers = new ArrayList<>();
    List<User> pairMatchLeftUsers = new ArrayList<>();
    List<User> pairMatchRightUsers = new ArrayList<>();
    List<User> userList;
    List<Node> userNodes;
    List<TopupRequest> topupRequestList;
    List<PreviousBalance> previousBalancelist;
    int leftTreeUserCount=0,rightTreeUserCount=0;
    String userId=null;

    @GetMapping("/PairMatching/{id}")
    public ResponseEntity<Map<String, Object>> getPairMatching(@PathVariable(value = "id")String id)
    {

    	userId=id;
    	Map<String, Object> response = new HashMap<String, Object>();
    	if(userList!=null){userList.clear();}
        if(userNodes!=null){userNodes.clear();}
        if(topupRequestList!=null){topupRequestList.clear();}
        if(previousBalancelist!=null){previousBalancelist.clear();}
        if(allLeftUsers!=null){allLeftUsers.clear();}
        if(allRightUsers!=null){allRightUsers.clear();}
        if(pairMatchLeftUsers!=null){pairMatchLeftUsers.clear();}
        if(pairMatchRightUsers!=null){pairMatchRightUsers.clear();}

        topupRequestList=walletRepository.getAllAcceptedTopupRequest();
        previousBalancelist= walletRepository.getAllPreviousBalance();
    	userList = userRepository.findAll();
    	userNodes= nodeRepository.findAll();
    	Node userNode = null;
    	System.out.println("Here");
		for(Node node:userNodes)
		{
			System.out.println(node.getUserId());
			if(node.getUserId().equals(id))
			{
				userNode = node;
			}
		}

    	if(userNode.getLeft()!=null && userNode.getRight()!=null)
    	{
    		pairMatchLeft(userNode.getLeft().getUserId());
    		pairMatchRight(userNode.getRight().getUserId());
    	}
    	if(userNode.getLeft()!=null && userNode.getRight()==null)
    	{
    		pairMatchLeft(userNode.getLeft().getUserId());
    	}
    	if(userNode.getLeft()==null && userNode.getRight()!=null)
    	{
    		pairMatchRight(userNode.getRight().getUserId());
    	}
    	response.put("leftChildSize",allLeftUsers.size());
    	response.put("rightChildSize",allRightUsers.size());
    	response.put("totalStacking",getUSDValueForCoins(pairMatchResolver()));
    	response.put("leftBusinessAmount",df.format(getLeftBusiness()));
    	response.put("rightBusinessAmount",df.format(getRightBusiness()));
    	response.put("activeRightChildSize",pairMatchRightUsers.size());
    	response.put("activeLeftChildSize",pairMatchLeftUsers.size());
    	return ResponseEntity.ok().body(response);
    }

    public void pairMatchLeft(String id)
    {
    	System.out.println("Processing Left Main Tree...Please Wait");
    	User u = null;
    	for(User user:userList)
    	{
    		if(user.getId().equals(id))
    		{
    			u = user;
    		}
    	}
		allLeftUsers.add(u);
    	Node userNode = nodeRepository.getByUserId(id);
    	if(userNode.getLeft()!=null && userNode.getRight()!=null)
    	{
    		pairMatchLeft(userNode.getLeft().getUserId());
    		pairMatchLeft(userNode.getRight().getUserId());
    	}
    	else if(userNode.getLeft()==null && userNode.getRight()!=null)
    	{
    		pairMatchLeft(userNode.getRight().getUserId());
    	}
    	else if(userNode.getLeft()!=null && userNode.getRight()==null)
    	{
    		pairMatchLeft(userNode.getLeft().getUserId());
    	}
    	else if(userNode.getLeft()==null && userNode.getRight()==null)
    	{
    		return;
    	}
    	return ;
    }

    public void pairMatchRight(String id)
    {
    	System.out.println("Processing Right Main Tree...Please Wait");
    	User u = null;
    	for(User user:userList)
    	{
    		if(user.getId().equals(id))
    		{
    			u = user;
    		}
    	}
		allRightUsers.add(u);
    	Node userNode = nodeRepository.getByUserId(id);
    	if(userNode.getLeft()!=null && userNode.getRight()!=null)
    	{
    		pairMatchRight(userNode.getLeft().getUserId());
    		pairMatchRight(userNode.getRight().getUserId());
    	}
    	else if(userNode.getLeft()==null && userNode.getRight()!=null)
    	{
    		pairMatchRight(userNode.getRight().getUserId());
    	}
    	else if(userNode.getLeft()!=null && userNode.getRight()==null)
    	{
    		pairMatchRight(userNode.getLeft().getUserId());
    	}
    	else if(userNode.getLeft()==null && userNode.getRight()==null)
    	{
    		return;
    	}
    	return;
    }

    public double pairMatchResolver()
    {
    	System.out.println("Pair Matching Started......");
    	double amount=0;
    	double leftAmount=0;
    	double rightAmount=0;

    	for(User user:allLeftUsers)
    	{
    		if(user!=null)
        	{
    			String userid = user.getId();
    			for(TopupRequest topupRequest:topupRequestList)
    			{
    				if(topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(userid))
    				{
    					User u = new User();
    					u=userRepository.getById(topupRequest.getUserId());
    					if(pairMatchLeftUsers.contains(u))
    					{

    					}
    					else
    					{
    						pairMatchLeftUsers.add(u);
    					}
    				}
    			}
        	}
    	}


    	for(User user:allRightUsers)
    	{
    		if(user!=null)
        	{
    			String userid = user.getId();
    			for(TopupRequest topupRequest:topupRequestList)
    			{
    				if(topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(userid))
    				{
    					User u = new User();
    					u=userRepository.getById(topupRequest.getUserId());
    					if(pairMatchRightUsers.contains(u))
    					{

    					}
    					else
    					{
    						pairMatchRightUsers.add(u);
    					}

    				}
    			}
        	}
    	}
    			for(TopupRequest topupRequest:topupRequestList)
    			{
    				for(User users:pairMatchLeftUsers)
    				{
    					if(users!=null)
    		        	{
    						if(topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(users.getId()))
    						{
    							leftAmount=amount+topupRequest.getAmount();
    						}
    		        	}

    				}

    				for(User users:pairMatchRightUsers)
    				{
    					if(users!=null)
    		        	{
    						if(topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(users.getId()))
    						{
    						rightAmount=amount+topupRequest.getAmount();
    						}
    		        	}
    				}
    			}




    			List<PairMatchingRecord> pairMatchingRecordList = new ArrayList<>();
    			pairMatchingRecordList=walletRepository.getPairMatchingRecordByUserId(userId);
    			double pastPairMatchAmount = 0;
    			if(pairMatchingRecordList.size()>0)
    			{
    				PairMatchingRecord recordYesterday=pairMatchingRecordList.get(pairMatchingRecordList.size()-1);
    				for(int i=0;i<=pairMatchingRecordList.size()-1;i++)
    				{
    					pastPairMatchAmount = pastPairMatchAmount+recordYesterday.getPairMatchedAmount();

    				}
    			}

    			if(rightAmount>leftAmount)
    			{

    				amount=(leftAmount)*0.05;
    			}
    			else
    			{
    				amount=(rightAmount)*0.05;
    			}

    			return amount;

    }

    @GetMapping("/getLeftBusiness")
    public double getLeftBusiness() {

        double leftAmount = 0;
        for (User user : allLeftUsers)
        {
        	if(user!=null)
        	{
        		for (TopupRequest topupRequest : topupRequestList)
        		{
        			if (topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(user.getId()))
        			{
                	leftAmount = (leftAmount + topupRequest.getAmount());
        			}
        		}
        	}

        }
        System.out.println("Left Amount "+leftAmount);
        return leftAmount;
    }

    @GetMapping("/getRightBusiness")
    public double getRightBusiness() {
        double rightAmount = 0;
        for (User user : allRightUsers)
        {
        	if(user!=null)
        	{
        		for (TopupRequest topupRequest : topupRequestList) {
        			if (topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(user.getId())) {
        				rightAmount = rightAmount + topupRequest.getAmount();
        			}
        		}
        	}

        }
        System.out.println("Right Amount "+rightAmount);
        return rightAmount;
    }

    //-----------------------------END-------------------------

    //--------------------Scheduled Pair Match--------------------


    @GetMapping("/PairMatchRun")
    public void runPairMatch()
    {
    	List<User> userL = new ArrayList<>();
    	userL=userRepository.findAll();
    	for(User usr:userL)
    	{
    		if(usr!=null&&usr.getId()!=null)
    		{
    			getPairMatchingS(usr.getId());
    		}
    	}
    }

    List<User> allLeftUsersS = new ArrayList<>();
    List<User> allRightUsersS = new ArrayList<>();
    List<User> pairMatchLeftUsersS = new ArrayList<>();
    List<User> pairMatchRightUsersS = new ArrayList<>();
    List<User> userListS;
    List<Node> userNodesS;
    List<TopupRequest> topupRequestListS;
    List<PreviousBalance> previousBalancelistS;
    int leftTreeUserCountS=0,rightTreeUserCountS=0;
    String userIdS=null;

    //@GetMapping("/PairMatchingS/{id}")@PathVariable(value = "id")
    public ResponseEntity<Map<String, Object>> getPairMatchingS(String id)
    {
    	userIdS=id;
    	Map<String, Object> response = new HashMap<String, Object>();
    	if(userListS!=null){userListS.clear();}
        if(userNodesS!=null){userNodesS.clear();}
        if(topupRequestListS!=null){topupRequestListS.clear();}
        if(previousBalancelistS!=null){previousBalancelistS.clear();}
        if(allLeftUsersS!=null){allLeftUsersS.clear();}
        if(allRightUsersS!=null){allRightUsersS.clear();}
        if(pairMatchLeftUsersS!=null){pairMatchLeftUsersS.clear();}
        if(pairMatchRightUsersS!=null){pairMatchRightUsersS.clear();}
        topupRequestListS=walletRepository.getAllAcceptedTopupRequest();
        previousBalancelistS= walletRepository.getAllPreviousBalance();
    	userListS = userRepository.findAll();
    	userNodesS= nodeRepository.findAll();
    	Node userNode = null; //nodeRepository.getByUserId(id);
		for(Node node:userNodesS)
		{
			if(node.getUserId().equals(id))
			{
				userNode = node;
			}
		}

    	if(userNode.getLeft()!=null && userNode.getRight()!=null)
    	{
    		pairMatchLeftS(userNode.getLeft().getUserId());
    		pairMatchRightS(userNode.getRight().getUserId());
    	}
    	if(userNode.getLeft()!=null && userNode.getRight()==null)
    	{
    		pairMatchLeftS(userNode.getLeft().getUserId());
    	}
    	if(userNode.getLeft()==null && userNode.getRight()!=null)
    	{
    		pairMatchRightS(userNode.getRight().getUserId());
    	}
    	response.put("leftChildSize",allLeftUsersS.size());
    	response.put("rightChildSize",allRightUsersS.size());
    	response.put("totalStacking",getUSDValueForCoins(pairMatchResolverS()));
    	response.put("activeRightChildSize",pairMatchRightUsersS.size());
    	response.put("activeLeftChildSize",pairMatchLeftUsersS.size());
    	BusinessRecord businessRecord = new BusinessRecord();
    	businessRecord.setUserId(id);
    	businessRecord.setLeftBusinessAmount(getLeftBusinessS());
    	businessRecord.setRightBusinessAmount(getRightBusinessS());
    	businessRecord.setLeftUsers(allLeftUsersS.size());
    	businessRecord.setLeftActiveUsers(pairMatchLeftUsersS.size());
    	businessRecord.setRightUsers(allRightUsersS.size());
    	businessRecord.setRightActiveUsers(pairMatchRightUsersS.size());
    	businessRecord.setTotalStacking(totalInvestmentFinderS(id));
    	walletRepository.SaveBusinessRecord(businessRecord);
    	System.out.println("Saving for Business Records");
    	System.out.println("----------------------------------------------------------------");
    	return ResponseEntity.ok().body(response);
    }

    public Double 	totalInvestmentFinderS(String userId)
    {
    	Wallet wallet = walletRepository.getByUserId(userId);

        double amount = 0;
        List<Transactions> transactionList = wallet.getTransactions();
        for(Transactions transaction : transactionList)
        {
        	TransactionType tType = transaction.getType();
        	String tMsg = transaction.getMessage();

        	if (tType.equals(TransactionType.C))
        	{
                String[] split = tMsg.split("/");
                if(split[0].equals("TOPUP_DONE"))
        		{
                	amount+=transaction.getAmount();
        		}
        	}
        }
        return amount;
    }

    public void pairMatchLeftS(String id)
    {
//    	System.out.println("Processing Left Main Tree...Scheduler");
    	User u = null;
    	for(User user:userListS)
    	{
    		if(user.getId().equals(id))
    		{
    			u = user;
    		}
    	}
//		u = userRepository.getById(id);
		allLeftUsersS.add(u);
    	Node userNode = nodeRepository.getByUserId(id);
    	if(userNode.getLeft()!=null && userNode.getRight()!=null)
    	{
    		pairMatchLeftS(userNode.getLeft().getUserId());
    		pairMatchLeftS(userNode.getRight().getUserId());
    	}
    	else if(userNode.getLeft()==null && userNode.getRight()!=null)
    	{
    		pairMatchLeftS(userNode.getRight().getUserId());
    	}
    	else if(userNode.getLeft()!=null && userNode.getRight()==null)
    	{
    		pairMatchLeftS(userNode.getLeft().getUserId());
    	}
    	else if(userNode.getLeft()==null && userNode.getRight()==null)
    	{
    		return;
    	}
    	return;
    }

    public void pairMatchRightS(String id)
    {
//    	System.out.println("Processing Right Main Tree...Scheduler");
    	User u = null;
    	for(User user:userListS)
    	{
    		if(user.getId().equals(id))
    		{
    			u = user;
    		}
    	}
//		u = userRepository.getById(id);
		allRightUsersS.add(u);
    	Node userNode = nodeRepository.getByUserId(id);
    	if(userNode.getLeft()!=null && userNode.getRight()!=null)
    	{
    		pairMatchRightS(userNode.getLeft().getUserId());
    		pairMatchRightS(userNode.getRight().getUserId());
    	}
    	else if(userNode.getLeft()==null && userNode.getRight()!=null)
    	{
    		pairMatchRightS(userNode.getRight().getUserId());
    	}
    	else if(userNode.getLeft()!=null && userNode.getRight()==null)
    	{
    		pairMatchRightS(userNode.getLeft().getUserId());
    	}
    	else if(userNode.getLeft()==null && userNode.getRight()==null)
    	{
    		return;
    	}
    	return;
    }

	public void findChildren(String userId, boolean left,String parentId)
	{
		HashMap<String, User> userHashMap = mapping.userMapping();
		if (userId == null) {
			return;
		}
		if (left) allLeftUsersG.add(userHashMap.get(userId));
		else allRightUsersG.add(userHashMap.get(userId));
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		Node userNode = nodeHashMap.get(userId);
		if (userNode.getLeft() != null) findChildren(userNode.getLeft().getUserId(), left, userNode.getUserId());
		if (userNode.getRight() != null)findChildren(userNode.getRight().getUserId(), left, userNode.getUserId());

	}

	public void childrenNodeMap(String childDirection, String currentUserId, String parentId) {
		HashMap<String, User> userHashMap = mapping.userMapping();
		if (currentUserId == null) {
			return;
		}
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		Node userNode = nodeHashMap.get(currentUserId);
		if ( userNode.getLeft() != null )
			childrenNodeMap("left", userNode.getLeft().getUserId(), userNode.getUserId());
		if (userNode != null && userNode.getRight() != null)
			childrenNodeMap("right", userNode.getRight().getUserId(), userNode.getUserId());
		if (childDirection.equals("left")) {
			List<Node> currentUserLeftChildren = new ArrayList<>();
			currentUserLeftChildren.add(userNode);
			if (userNode.getLeft() != null)
//				if (allLeftUsersHashMap.containsKey(userNode.getLeft().getUserId()))
					currentUserLeftChildren.addAll(allLeftUsersHashMap.get(userNode.getUserId()));
			if (userNode.getRight() != null)
//				if (allRightUsersHashMap.containsKey(userNode.getRight().getUserId()))
					currentUserLeftChildren.addAll(allRightUsersHashMap.get(userNode.getUserId()));
			allLeftUsersHashMap.put(parentId, currentUserLeftChildren);
		} else if (childDirection.equals("right")) {
			List<Node> currentUserRightChildren = new ArrayList<>();
			currentUserRightChildren.add(userNode);
			if (userNode != null && userNode.getLeft() != null)
//				if (allLeftUsersHashMap.containsKey(userNode.getLeft().getUserId()))
					currentUserRightChildren.addAll(allLeftUsersHashMap.get(userNode.getUserId()));
			if (userNode != null && userNode.getRight() != null)
//				if (allRightUsersHashMap.containsKey(userNode.getRight().getUserId()))
					currentUserRightChildren.addAll(allRightUsersHashMap.get(userNode.getUserId()));
			allRightUsersHashMap.put(parentId, currentUserRightChildren);
		}
//		System.out.println(parentId + " Left : " + allLeftUsersHashMap);
//		System.out.println(parentId + " Right : " + allRightUsersHashMap);
//		System.out.println(parentId + " Left : " + allLeftUsersHashMap.size());
//		System.out.println(parentId + " Right : "  + allRightUsersHashMap.size());
//		if (parentId.equals("61ef7f3ae5bc551115739467")) {
//		System.out.println("Right : " + allRightUsersHashMap.get("61ef7f3ae5bc551115739467"));
//		System.out.println("Left : " + allLeftUsersHashMap.get("61ef7f3ae5bc551115739467"));
//		}
	}
	public double pairMatchResolverRightGuru(String userId) {
		System.out.println("Pair Matching Started......");
		double amount = 0;
		double leftAmount = 0;
		double rightAmount = 0;
		System.out.println(allRightUsersG.size());
		for (User user : allRightUsersG) {
				String userid = user.getId();
				for (TopupRequest topupRequest : topupRequestListG) {
					if (topupRequest.getUserId() != null && topupRequest.getUserId().equals(userid)) {
						User u = new User();
						u = userRepository.getById(topupRequest.getUserId());
						if (pairMatchRightUsersG.contains(u)) {

						} else {
							pairMatchRightUsersG.add(u);
						}
					}
				}
		}
		return pairMatchRightUsersG.size();
	}

    public double pairMatchResolverS()
    {
    	System.out.println("Pair Matching Started......");
    	double amount=0;
    	double leftAmount=0;
    	double rightAmount=0;
    	for(User user:allLeftUsersS)
    	{
    		if(user!=null)
        	{
    			String userid = user.getId();
    			for(TopupRequest topupRequest:topupRequestListS)
    			{
    				if(topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(userid))
    				{
    					User u = new User();
    					u=userRepository.getById(topupRequest.getUserId());
    					if(pairMatchLeftUsersS.contains(u))
    					{

    					}
    					else
    					{
    						pairMatchLeftUsersS.add(u);
    					}
    				}
    			}
        	}
    	}


    	for(User user:allRightUsersS)
    	{
    		if(user!=null)
        	{
    			String userid = user.getId();
    			for(TopupRequest topupRequest:topupRequestListS)
    			{
    				if(topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(userid))
    				{
    					User u = new User();
    					u=userRepository.getById(topupRequest.getUserId());
    					if(pairMatchRightUsersS.contains(u))
    					{

    					}
    					else
    					{
    						pairMatchRightUsersS.add(u);
    					}

    				}
    			}
        	}
    	}

   			 	leftAmount = getLeftBusinessS();
   			 	rightAmount= getRightBusinessS();

    			List<PairMatchingRecord> pairMatchingRecordList = new ArrayList<>();
    			pairMatchingRecordList=walletRepository.getPairMatchingRecordByUserId(userIdS);
    			double pastPairMatchAmount = 0;
    			if(pairMatchingRecordList.size()>0)
    			{
    				PairMatchingRecord recordYesterday=pairMatchingRecordList.get(pairMatchingRecordList.size()-1);
    				for(int i=0;i<=pairMatchingRecordList.size()-1;i++)
    				{
    					pastPairMatchAmount = pastPairMatchAmount+recordYesterday.getPairMatchedAmount();

    				}
    			}
    			System.out.println(leftAmount+"***"+rightAmount+"***"+pastPairMatchAmount);

    			if(rightAmount>leftAmount)
    			{

    				amount=(leftAmount)*0.05;
    			}
    			else
    			{
    				amount=(rightAmount)*0.05;
    			}



    			PairMatchingRecord pairMatchingRecord = new PairMatchingRecord();
    			pairMatchingRecord.setUserId(userIdS);
    			pairMatchingRecord.setPairMatchedAmount(amount);
    			walletRepository.createPairMatchingRecord(pairMatchingRecord);

    			if(amount>pastPairMatchAmount)
    			{
    				System.out.println("UserId :"+userIdS+" "+"Amount :"+(amount-pastPairMatchAmount));
    				System.out.println("Process Finished");
    				System.out.println("----------------------------------------------------------------");
				double totalInvestment=totalInvestmentFinderS(userIdS);
				double pairMatchAmount=amount-pastPairMatchAmount;
    				if(pairMatchAmount>totalInvestment/2)
    				{
    					pairMatchAmount=totalInvestment/2;
    				}
				if(amount-pastPairMatchAmount>0)
				{
    			        	pairMatchTopup(userIdS,pairMatchAmount);
				}
    				return amount-pastPairMatchAmount;
    			}
    			else if(amount==pastPairMatchAmount)
    			{
    				System.out.println("UserId :"+userIdS+" "+"Amount :"+(amount-pastPairMatchAmount));
    				System.out.println("Process Finished");
    				System.out.println("----------------------------------------------------------------");
    				return amount-pastPairMatchAmount;
    			}
    			else
    			{
    				System.out.println("UserId :"+userIdS+" Check For Errors ");
    				System.out.println("Process Finished");
    				System.out.println("----------------------------------------------------------------");
    				return 0;
    			}

    }


    public double getLeftBusinessS() {

        double leftAmount = 0;
        for (User user : allLeftUsersS)
        {
        	if(user!=null)
        	{
        		for (TopupRequest topupRequest : topupRequestListS)
        		{
        			if (topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(user.getId()))
        			{
                	leftAmount = (leftAmount + topupRequest.getAmount());
        			}
        		}
        	}

        }
        return leftAmount;
    }


    public double getRightBusinessS() {
        double rightAmount = 0;
        for (User user : allRightUsersS)
        {
        	if(user!=null)
        	{
        		for (TopupRequest topupRequest : topupRequestListS) {
        			if (topupRequest.getUserId()!=null&&topupRequest.getUserId().equals(user.getId())) {
        				rightAmount = rightAmount + topupRequest.getAmount();
        			}
        		}
        	}

        }
        return rightAmount;
    }

    public ResponseEntity<String> pairMatchTopup(String userId,double amount) {
        User user = userRepository.getById(userId);
        System.out.println("Transaction Done For: "+userId+" "+"Amount :"+amount);
        walletRepository.addTransaction(user.getId(), TransactionType.C, amount, TransactionMessage.BUSINESS_MATCH + "");
        setWalletData(user.getId());
        System.out.println("----------------------------------------------------------------");
        return ResponseEntity.ok().body("PairMatch Topup done");
    }

	public ResponseEntity<String> pairMatchTopupNew(String userId,double amount) {
		HashMap<String, User> userHashMap = mapping.userMapping();
		User user = userHashMap.get(userId);
		System.out.println("Transaction Done For: "+userId+" "+"Amount :"+amount);
		walletRepository.addTransaction(user.getId(), TransactionType.C, amount, TransactionMessage.BUSINESS_MATCH + "");
		setWalletData(user.getId());
		System.out.println("----------------------------------------------------------------");
		return ResponseEntity.ok().body("PairMatch Topup done");
	}
    // ----------------------------END-------------------------

    // -----------------------Currency Changer-----------------

    @GetMapping("/coinToUsd/{coins}")
    public ResponseEntity<Double> getUSDValueForCoins(@PathVariable(value = "coins") Double coins) {
        Dailyprice dailyPrice = userRepository.getDailyPrice();
        double currentAmount = dailyPrice.getCost();
        double oneUSDValue = currentAmount / 15;
        return ResponseEntity.ok().body(coins/oneUSDValue);
    }


    public Double getUSDValueForCoinsLocal(Double coins) {
        Dailyprice dailyPrice = userRepository.getDailyPrice();
        double currentAmount = dailyPrice.getCost();
        double oneUSDValue = currentAmount / 15;
        return coins/oneUSDValue;
    }

    @GetMapping("/usdToCoin/{usd}")
    public ResponseEntity<Double> getCoinValueForUSD(@PathVariable(value = "usd") Double USD) {
        Dailyprice dailyPrice = userRepository.getDailyPrice();
        double currentAmount = dailyPrice.getCost();
        double oneUSDValue = currentAmount / 15; // USD Value per Coin
        double CoinValue = USD * oneUSDValue;
        return ResponseEntity.ok().body(CoinValue);

    }

    public Double getCoinValueForUSDLocal(Double USD) {
        Dailyprice dailyPrice = userRepository.getDailyPrice();
        double currentAmount = dailyPrice.getCost();
        double oneUSDValue = currentAmount / 15; // USD Value per Coin
        double CoinValue = USD * oneUSDValue;
        return CoinValue;

    }

    @GetMapping("/coinToInr/{coins}")
    public ResponseEntity<Double> getInrValueForCoins(@PathVariable(value = "coins") Double coins) {
        double currentUSDRateInINR = 74.34;
        Dailyprice dailyPrice = userRepository.getDailyPrice();
        double currentAmount = dailyPrice.getCost();
        double INRValue = (currentAmount / 15)/currentUSDRateInINR;
        return ResponseEntity.ok().body(coins/INRValue);
    }

    public Double getInrValueForCoinsLocal(Double coins) {
        double currentUSDRateInINR = 74.34;
        Dailyprice dailyPrice = userRepository.getDailyPrice();
        double currentAmount = dailyPrice.getCost();
        double INRValue = (currentAmount / 15)/currentUSDRateInINR;
        return coins/INRValue;
    }

    @GetMapping("/inrToCoin/{inr}")
    public ResponseEntity<Double> getCoinValueForINR(@PathVariable(value = "inr") Double INR) {
        double currentUSDRateInINR = 74.34;
        Dailyprice dailyPrice = userRepository.getDailyPrice();
        double currentAmount = dailyPrice.getCost();
        double oneINRValue = (currentAmount / 15)/currentUSDRateInINR;
        double CoinValue = INR * oneINRValue;
        return ResponseEntity.ok().body(CoinValue);
    }

    public Double getCoinValueForINRLocal(Double INR) {
        double currentUSDRateInINR = 74.34;
        Dailyprice dailyPrice = userRepository.getDailyPrice();
        double currentAmount = dailyPrice.getCost();
        double oneINRValue = (currentAmount / 15)/currentUSDRateInINR;
        double CoinValue = INR * oneINRValue;
        return CoinValue;
    }

    @GetMapping("/usdToInr/{usd}")
    public ResponseEntity<Double> getUsdToINR(@PathVariable(value = "usd") Double usd) {
        double currentUSDRateInINR = 74.34;
        double INRValue = usd * currentUSDRateInINR;
        return ResponseEntity.ok().body(INRValue);

    }

    @GetMapping("/inrToUsd/{inr}")
    public ResponseEntity<Double> getInrToUsd(@PathVariable(value = "inr") Double inr) {
        double currentUSDRateInINR = 74.34;
        double USDValue = inr / currentUSDRateInINR;
        return ResponseEntity.ok().body(USDValue);

    }

    // --------------------------END-------------------------------

    //--------------------Get By Wallet Type-----------------------
    //Dashboard
    @GetMapping("/getWalletData/for/{userId}")
    public ResponseEntity<Map<String, Object>> getWalletDataByType(@PathVariable(value = "userId") String userId)
    {
        Wallet wallet = walletRepository.getByUserId(userId);
        List<BusinessRecord> BRL = walletRepository.getAllBusinessRecords();
        BusinessRecord br = new BusinessRecord();
        for(BusinessRecord businessRecord:BRL)
        {
        	if(businessRecord.getUserId().equals(userId))
        	{
        		br=businessRecord;
        	}
        }
        double offerWallet = 0;
        double stackingWallet = 0;
        double earningWallet = 0;
        double bonusWallet = 0;
        double pairMatchAmount =0;
        Map<String, Object> response = new HashMap<String, Object>();
        List<Transactions> transactionList = wallet.getTransactions();
        int transactionCount =0;
        int LeftChildCount =0;
        int RightChildCount =0;
        for(Transactions transaction : transactionList)
        {
        	TransactionType tType = transaction.getType();
        	String tMsg = transaction.getMessage();

        	if (tType.equals(TransactionType.C) && !transaction.isProcessed())
        	{
                String[] split = tMsg.split("/");

                // Active Wallet Functions
                if (split[0].equals("NEW_JOIN"))
                {
                	//offerwallet
                	offerWallet=offerWallet+transaction.getAmount();
                }
                else if (split[0].equals("TOPUP_SELF_RETURN"))
                {
                	//offerWallet
                	if(transactionCount==0)
                	{
                		offerWallet=offerWallet+transaction.getAmount(); //firsttimeonly
                	}
                	transactionCount=transactionCount+1;
                }

                // Earnings Wallet Functions
                else if (split[0].equals("NEW_RIGHT_CHILD_JOIN"))
                {
                	//offerwallet
                	RightChildCount+=1;
                	if(RightChildCount<2)
                	{
                		offerWallet=offerWallet+transaction.getAmount();
                	}
                }
                else if (split[0].equals("NEW_LEFT_CHILD_JOIN"))
                {
                	//offerwallet
                	LeftChildCount+=1;
                	if(LeftChildCount<2)
                	{
                		offerWallet=offerWallet+transaction.getAmount();
                	}
                }
                else if (split[0].equals("CHILD_TOPUP_DONE"))
                {
                	//earningswallet
                	earningWallet=earningWallet+transaction.getAmount();
                }
                else if (split[0].equals("BUSINESS_MATCH"))
                {
                	//earningswallet
                	System.out.println("PairMatch");
                	earningWallet=earningWallet+transaction.getAmount();
                	pairMatchAmount=pairMatchAmount+transaction.getAmount();
                }
                else if (split[0].equals("ROI"))
                {
                	//stackingwallet
                	stackingWallet=stackingWallet+transaction.getAmount();
                }
                else
                {
                    System.out.println("Error");
                }
        	}

        }

	List<WithdrawRequest> listWR = walletRepository.getAllWithdrawRequest();
    	double offerWalletDeductionAmount =0;
    	double earningsWalletDeductionAmount =0;
    	double StackingWalletDeductionAmount =0;
    	for(WithdrawRequest wr:listWR)
    	{
//    		if(wr.getUserId().equals(userId)&&wr.getStatus().equals("P"))
    		if(wr.getUserId() == userId && wr.getStatus().equals("P"))
    		{
    			earningsWalletDeductionAmount += wr.getEarningsWalletDeduction();
    			offerWalletDeductionAmount += wr.getOfferWalletDeduction();
    			StackingWalletDeductionAmount += wr.getStackingWalletDeduction();
    		}
    	}

        response.put("offerWalletAmount",offerWallet-offerWalletDeductionAmount);
        response.put("stackingWalletAmount",stackingWallet-StackingWalletDeductionAmount);
        response.put("earningWalletAmount",earningWallet-earningsWalletDeductionAmount);
        response.put("bonusWalletAmount",bonusWallet);
        response.put("businessMatch",pairMatchAmount);
        response.put("offerWalletAmountNew",wallet.getOfferWallet()-offerWalletDeductionAmount);
        response.put("stackingWalletAmountNew",wallet.getStackingWallet()-StackingWalletDeductionAmount);
        response.put("earningWalletAmountNew",wallet.getEarningsWallet()-earningsWalletDeductionAmount);
        response.put("LeftBusinessAmount",br.getLeftBusinessAmount());
        response.put("RightBusinessAmount",br.getRightBusinessAmount());
        response.put("TotalStacking",br.getTotalStacking());
        response.put("LeftActiveUsers",br.getLeftActiveUsers());
        response.put("RightActiveUsers",br.getRightActiveUsers());
        response.put("LeftUsers",br.getLeftUsers());
        response.put("RightUsers",br.getRightUsers());
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/getMasterWalletData/for/{userId}")
    public ResponseEntity<Map<String, Double>> getWalletData(@PathVariable(value = "userId") String userId)
    {
        Wallet wallet = walletRepository.getByUserId(userId);

        Map<String, Double> response = new HashMap<String, Double>();
        response.put("offerWalletAmount",wallet.getOfferWallet());
        response.put("stackingWalletAmount",wallet.getStackingWallet());
        response.put("earningWalletAmount",wallet.getEarningsWallet());
        return ResponseEntity.ok().body(response);

    }



    @GetMapping("/getEarningWalletData/for/{userId}")
    public ResponseEntity<Map<Transactions, String>> getEarningWalletDataByType(@PathVariable(value = "userId") String userId)
    {
        Wallet wallet = walletRepository.getByUserId(userId);
        double earningWallet = 0;
        Map<Transactions, String> response = new HashMap<>();
        List<Transactions> transactionList = wallet.getTransactions();
        for(Transactions transaction : transactionList)
        {
        	TransactionType tType = transaction.getType();
        	String tMsg = transaction.getMessage();

        	if (tType.equals(TransactionType.C))
        	{
                String[] split = tMsg.split("/");

                // Earnings Wallet Functions
                if (split[0].equals("CHILD_TOPUP_DONE"))
                {
                	//earningswallet
                	User u = userRepository.getById(split[1]);
                	earningWallet=earningWallet+transaction.getAmount();
                	response.put(transaction,u.getRefId()+"/"+"Child's Topup Done");
                }
                else if (split[0].equals("BUSINESS_MATCH"))
                {
                	//earningswallet
                	System.out.println("PairMatch");
                	earningWallet=earningWallet+transaction.getAmount();
                	response.put(transaction,"Business Match Amount");
                }
                else
                {
                    //System.out.println("Error");
                }
        	}

        }
        return ResponseEntity.ok().body(response);

    }


    //---------------------------END-------------------------------

    //------------------FreeCoinAddressFunctions-------------------
    @PostMapping("/freeCoinAddress/create")
    public FreecoinAddress createFreeCoinAddress(@RequestBody FreecoinAddress freecoinAddress) {
        return walletRepository.createFreeCoinAddress(freecoinAddress);
    }

    @GetMapping("/getFreeCoinAddress/{userId}")
    public FreecoinAddress getFreeCoinAddressByUserId(@PathVariable(value = "userId") String userId) {
        return walletRepository.getFreeCoinAddressByUserId(userId);
    }

    @PutMapping("/editFreeCoinAddress/{address}")
    public FreecoinAddress editFreeCoinAddress(@RequestBody FreecoinAddress freecoinAddress,@PathVariable(value = "address") String address)
    {
    	freecoinAddress.setFreeCoinAddress(address);
    	return walletRepository.createFreeCoinAddress(freecoinAddress);
    }

    //---------------------------END-------------------------------

    // --------------Withdraw Requests and TOPUP Requests----------

    @GetMapping("/getWithdrawRequests")
    public ResponseEntity<List<WithdrawRequest>> getAllWithdrawRequest() {
        List<WithdrawRequest> withdrawRequests = walletRepository.getAllWithdrawRequest();
        return ResponseEntity.ok().body(withdrawRequests);
    }

    @GetMapping("/getBusinessRecords")
    public ResponseEntity<List<BusinessRecord>> getAllBusinessRecords() {
        List<BusinessRecord> businessRecord = walletRepository.getAllBusinessRecords();
        return ResponseEntity.ok().body(businessRecord);
    }

    @GetMapping("/getBusinessRecords/{userId}")
    public ResponseEntity<BusinessRecord> getUserBusinessRecords(@PathVariable(value = "userId") String userId) {
        List<BusinessRecord> businessRecord = walletRepository.getAllBusinessRecords();
        BusinessRecord userBusinessRecord = new BusinessRecord();
        for(BusinessRecord bs:businessRecord)
        {
        	if(bs.getUserId().equals(userId))
        	{
        		userBusinessRecord=bs;
        	}
        }
        return ResponseEntity.ok().body(userBusinessRecord);
    }

    @GetMapping("/getPendingWithdrawRequests")
    public ResponseEntity<List<WithdrawRequest>> getPendingWithdrawRequest() {
        List<WithdrawRequest> withdrawRequest = walletRepository.getAllWithdrawRequest();
        List<WithdrawRequest> pendingWithdraws = new ArrayList<>();
        for(WithdrawRequest pendingWithdraw:withdrawRequest)
        {
        	if(pendingWithdraw.getStatus().equals("P"))
        	{
        		pendingWithdraws.add(pendingWithdraw);
        	}
        }
        return ResponseEntity.ok().body(pendingWithdraws);
    }

    @GetMapping("/getAcceptedWithdrawRequests")
    public ResponseEntity<List<WithdrawRequest>> getAcceptedWithdrawRequest() {
        List<WithdrawRequest> withdrawRequest = walletRepository.getAllWithdrawRequest();
        List<WithdrawRequest> acceptedWithdraws = new ArrayList<>();
        for(WithdrawRequest acceptedWithdraw:withdrawRequest)
        {
        	if(acceptedWithdraw.getStatus().equals("P"))
        	{
        		acceptedWithdraws.add(acceptedWithdraw);
        	}
        }
        return ResponseEntity.ok().body(acceptedWithdraws);
    }


    @GetMapping("/getTopupRequests")
    public ResponseEntity<List<TopupRequest>> getAllPendingTopupRequest() {
        List<TopupRequest> topupRequests = walletRepository.getAllTopupRequest();
        List<TopupRequest> pendingTopups = new ArrayList<>();
        for(TopupRequest topupRequest:topupRequests)
        {
        	if(topupRequest.getStatus().equals("P"))
        	{
        		pendingTopups.add(topupRequest);
        	}
        }
        return ResponseEntity.ok().body(pendingTopups);
    }

    @PostMapping("/withdrawRequest/create")
    public WithdrawRequest createWithdrawRequest(@RequestBody WithdrawRequest withdrawRequest) {
    	withdrawRequest.setRefId(getRefId(withdrawRequest));
		User user = userRepository.getByRefId(withdrawRequest.getRefId());
    	Wallet userWallet = walletRepository.getByUserId(withdrawRequest.getUserId());
    	double offerWallet=0;
        double earningsWallet=0;
        double stackingWallet=0;
        double availableBalance=0;
        double firstGroupWallet=0;
        double secondGroupWallet=0;
        double offerWalletSA=0;
        double earningsWalletSA=0;
        double stackingWalletSA=0;
		double amount =0;
        if(withdrawRequest.getCurrencyType().equals("coin"))
        {
        	amount=withdrawRequest.getAmount();
        }
        else
        {
        	amount=getCoinValueForINRLocal(withdrawRequest.getAmount());
        }

        	if (userWallet != null) {
        		offerWallet = userWallet.getOfferWallet();
        		earningsWallet = userWallet.getEarningsWallet();
        		stackingWallet = userWallet.getStackingWallet();
        		offerWalletSA = userWallet.getOfferWallet();
        		earningsWalletSA = userWallet.getEarningsWallet();
        		stackingWalletSA = userWallet.getStackingWallet();
        		availableBalance=stackingWallet+earningsWallet+offerWallet;
        		firstGroupWallet = earningsWallet;
        		secondGroupWallet= earningsWallet+offerWallet;
        		double shredValue =0;
        		if(amount>availableBalance)
        		{
        			System.out.println("Balance is Low");
        		}
        		else
        		{
        			System.out.println("ElsePart");
        			if(firstGroupWallet>amount)
        			{
        				earningsWallet = earningsWallet-amount;
        				withdrawRequest.setEarningsWalletDeduction(earningsWalletSA-earningsWallet);
        	        	withdrawRequest.setOfferWalletDeduction(0);
        	        	withdrawRequest.setStackingWalletDeduction(0);
        			}
        			else if(secondGroupWallet>amount)
        			{
        				shredValue = amount-earningsWallet;
        				earningsWallet=0;
        				offerWallet=offerWallet-shredValue;
        				withdrawRequest.setEarningsWalletDeduction(earningsWalletSA-earningsWallet);
        	        	withdrawRequest.setOfferWalletDeduction(offerWalletSA-offerWallet);
        	        	withdrawRequest.setStackingWalletDeduction(0);
        			}
        			else
        			{
        				shredValue = amount-earningsWallet;
        				shredValue = shredValue-offerWallet;
        				stackingWallet = stackingWallet - shredValue;
        				earningsWallet =0;
        				offerWallet =0;
        				withdrawRequest.setEarningsWalletDeduction(earningsWalletSA-earningsWallet);
        	        	withdrawRequest.setOfferWalletDeduction(offerWalletSA-offerWallet);
        	        	withdrawRequest.setStackingWalletDeduction(stackingWalletSA-stackingWallet);
        			}
        		}
        	}
		userWallet.setEarningsWallet(earningsWallet);
		userWallet.setOfferWallet(offerWallet);
		userWallet.setStackingWallet(stackingWallet);
		walletRepository.save(userWallet);
        if(withdrawRequest.getUserId()!= null)
            withdrawRequest.setName(userRepository.getById(withdrawRequest.getUserId()).getName());
    	else
            withdrawRequest.setName(userRepository.getByRefId(withdrawRequest.getRefId()).getName());
    	return walletRepository.createWithdrawRequest(withdrawRequest);
    }

    @PostMapping("/withdrawRequest/fix")
    public void fixWithdrawRequest(@RequestBody WithdrawRequest withdrawRequest) {
    	Wallet userWallet = walletRepository.getByUserId(withdrawRequest.getUserId());
    	double offerWallet=0;
        double earningsWallet=0;
        double stackingWallet=0;
        double availableBalance=0;
        double firstGroupWallet=0;
        double secondGroupWallet=0;
        double offerWalletSA=0;
        double earningsWalletSA=0;
        double stackingWalletSA=0;
        double amount=withdrawRequest.getAmount();

        	if (userWallet != null) {
        		offerWallet = userWallet.getOfferWallet();
        		earningsWallet = userWallet.getEarningsWallet();
        		stackingWallet = userWallet.getStackingWallet();
        		offerWalletSA = userWallet.getOfferWallet();
        		earningsWalletSA = userWallet.getEarningsWallet();
        		stackingWalletSA = userWallet.getStackingWallet();
        		availableBalance=stackingWallet+earningsWallet+offerWallet;
        		firstGroupWallet = earningsWallet;
        		secondGroupWallet= earningsWallet+offerWallet;
        		double shredValue =0;
        		if(amount>availableBalance)
        		{
        			System.out.println("Balance is Low");
        		}
        		else
        		{
        			System.out.println("ElsePart");
        			if(firstGroupWallet>amount)
        			{
        				earningsWallet = earningsWallet-amount;
        				withdrawRequest.setEarningsWalletDeduction(earningsWalletSA-earningsWallet);
        	        	withdrawRequest.setOfferWalletDeduction(0);
        	        	withdrawRequest.setStackingWalletDeduction(0);
        	        	System.out.println(earningsWalletSA-earningsWallet);

        			}
        			else if(secondGroupWallet>amount)
        			{
        				shredValue = amount-earningsWallet;
        				earningsWallet=0;
        				offerWallet=offerWallet-shredValue;
        				withdrawRequest.setEarningsWalletDeduction(earningsWalletSA-earningsWallet);
        	        	withdrawRequest.setOfferWalletDeduction(offerWalletSA-offerWallet);
        	        	withdrawRequest.setStackingWalletDeduction(0);
        	        	System.out.println(earningsWalletSA-earningsWallet);
        	        	System.out.println(offerWalletSA-offerWallet);

        			}
        			else
        			{
        				shredValue = amount-earningsWallet;
        				shredValue = shredValue-offerWallet;
        				stackingWallet = stackingWallet - shredValue;
        				earningsWallet =0;
        				offerWallet =0;
        				withdrawRequest.setEarningsWalletDeduction(earningsWalletSA-earningsWallet);
        	        	withdrawRequest.setOfferWalletDeduction(offerWalletSA-offerWallet);
        	        	withdrawRequest.setStackingWalletDeduction(stackingWalletSA-stackingWallet);
        	        	System.out.println((long)earningsWallet);
        	        	System.out.println((long)offerWallet);
        	        	System.out.println((long)stackingWallet);
        			}
        		}
        	}
        	userWallet.setEarningsWallet(earningsWallet);
        	userWallet.setOfferWallet(offerWallet);
        	userWallet.setStackingWallet(stackingWallet);
        	walletRepository.SaveWallet(userWallet);
    }

    @PostMapping("/topUpRequest/create")
    public TopupRequest createTopupRequest(@RequestBody TopupRequest topupRequest) {
		User user = userRepository.getByRefId(topupRequest.getRefId());
    	topupRequest.setRefId(getRefId(topupRequest));
		topupRequest.setName(user.getName());
        return walletRepository.createTopupRequest(topupRequest);
    }

    @GetMapping("/getAcceptedPendingTopupRequests")
    public ResponseEntity<List<TopupRequest>> getAllAcceptedTopupRequests() {
    	List<TopupRequest> topupRequests = walletRepository.getAllTopupRequest();
        List<TopupRequest> acceptedTopups = new ArrayList<>();
        for(TopupRequest topupRequest:topupRequests)
        {
        	if(topupRequest.getStatus().equals("A"))
        	{
        		acceptedTopups.add(topupRequest);
        	}
        }
        return ResponseEntity.ok().body(acceptedTopups);
    }

    @GetMapping("/getAcceptedTopupRequests/{userId}")
    public ResponseEntity<List<TopupRequest>> getAllTopupRequestsUser(@PathVariable(value = "userId") String userId) {
    	List<TopupRequest> topupRequests = walletRepository.getAllTopupRequest();
        List<TopupRequest> acceptedTopups = new ArrayList<>();
        for(TopupRequest topupRequest:topupRequests)
        {
        	if(topupRequest.getStatus().equals("A")&&topupRequest.getUserId().equals(userId))
        	{
        		acceptedTopups.add(topupRequest);
        	}
        }
        return ResponseEntity.ok().body(acceptedTopups);
    }

    @GetMapping("/getPendingTopupRequests")
    public ResponseEntity<List<TopupRequest>> getPendingTopupRequests() {
    	List<TopupRequest> topupRequests = walletRepository.getAllTopupRequest();
        List<TopupRequest> pendingTopups = new ArrayList<>();
        for(TopupRequest topupRequest:topupRequests)
        {
        	if(topupRequest.getStatus().equals("P"))
        	{
        		pendingTopups.add(topupRequest);
        	}
        }
        return ResponseEntity.ok().body(pendingTopups);
    }

    @PutMapping("/acceptTopupRequest")
    public ResponseEntity<TopupRequest> acceptTopupRequest(@RequestBody TopupRequest topupRequest)
    {
    	walletRepository.acceptTopupRequest(topupRequest);
    	topup(topupRequest);
    	return ResponseEntity.ok().body(topupRequest);
    }

    @PutMapping("/acceptWithdrawRequest")
    public ResponseEntity<WithdrawRequest> acceptWithdrawRequest(@RequestBody WithdrawRequest withdrawRequest)
    {
        if(withdrawRequest.getUserId()!= null)
            withdrawRequest.setName(userRepository.getById(withdrawRequest.getUserId()).getName());
    	else
            withdrawRequest.setName(userRepository.getByRefId(withdrawRequest.getRefId()).getName());
    	withdrawRequest.setEarningsWalletDeduction(0);
    	withdrawRequest.setOfferWalletDeduction(0);
    	withdrawRequest.setStackingWalletDeduction(0);
    	walletRepository.createWithdrawRequest(withdrawRequest);
    	walletRepository.acceptWithdrawal(withdrawRequest);
    	withdraw(withdrawRequest);
    	return ResponseEntity.ok().body(withdrawRequest);

    }

    @GetMapping("/getDeductionAmount/{userId}")
    public ResponseEntity<Map<String, Double>> getDeductionAmount(@PathVariable(value = "userId") String userId)
    {
    	List<WithdrawRequest> withdrawRequestList = walletRepository.getAllWithdrawRequest();
    	Map<String, Double> response = new HashMap<String, Double>();
    	double deductionAmount =0;
    	for(WithdrawRequest withdrawRequest:withdrawRequestList)
    	{
    		if(withdrawRequest.getUserId() == userId &&withdrawRequest.getStatus() == "P")
    		{
    			deductionAmount += withdrawRequest.getEarningsWalletDeduction();
    			deductionAmount += withdrawRequest.getOfferWalletDeduction();
    			deductionAmount += withdrawRequest.getStackingWalletDeduction();
    		}
    	}
    	double inrAmount = getInrValueForCoinsLocal(deductionAmount);
	inrAmount=0;
	deductionAmount =0;
    	response.put("DeductionFcAmount",deductionAmount);
    	response.put("DeductioINRAmount",inrAmount);
    	return ResponseEntity.ok().body(response);
    }

    @PutMapping("/rejectWithdrawRequest")
    public ResponseEntity<WithdrawRequest> rejectWithdrawRequest(@RequestBody WithdrawRequest withdrawRequest)
    {
        if(withdrawRequest.getUserId()!= null)
            withdrawRequest.setName(userRepository.getById(withdrawRequest.getUserId()).getName());
    	else
            withdrawRequest.setName(userRepository.getByRefId(withdrawRequest.getRefId()).getName());
        withdrawRequest.setEarningsWalletDeduction(0);
    	withdrawRequest.setOfferWalletDeduction(0);
    	withdrawRequest.setStackingWalletDeduction(0);
    	walletRepository.createWithdrawRequest(withdrawRequest);
    	walletRepository.rejectWithdrawal(withdrawRequest);
		Wallet wallet = walletRepository.getWalletByUserId(withdrawRequest.getUserId());
		wallet.setStackingWallet(wallet.getStackingWallet() + withdrawRequest.getAmount());
		walletRepository.SaveWallet(wallet);
    	return ResponseEntity.ok().body(withdrawRequest);
    }

    @PutMapping("/rejectTopupRequest")
    public ResponseEntity<TopupRequest> rejectTopupRequest(@RequestBody TopupRequest topupRequest)
    {
    	walletRepository.rejectTransaction(topupRequest);
    	return ResponseEntity.ok().body(topupRequest);
    }

    // ---------------------------END--------------------------------

    @GetMapping("/get")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok().body(allUsers);
    }

    @GetMapping("/get/auth")
    public ResponseEntity<List<Auth>> getAllAuthDocs() {
        List<Auth> authList = walletRepository.getAllAuthDocs();
        return ResponseEntity.ok().body(authList);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getById(@PathVariable(value = "id") String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//-----------------------Previous Balance Functions-------------------------

    @GetMapping("/getUserPreviousBalance/{id}")
    public PreviousBalance getUserPreviousBalance(@PathVariable(value = "id") String id) {
        return userRepository.getUserPreviousBalanceByUserId(id);
    }

    @GetMapping("/getUserAvailableBalance")
    public List<PreviousBalance> getUserAvailableBalance() {
        List<PreviousBalance> previousBalance = walletRepository.getAllPreviousBalance();
        return previousBalance;

    }

    @PutMapping("/setUserAvailableBalance/{id}/{newAmount}")
    public ResponseEntity<Boolean> setUserAvailableBalance(@PathVariable(value = "id") String id,@PathVariable(value = "newAmount") Double newAmount) {
        PreviousBalance previousBalance = userRepository.getUserPreviousBalanceByUserId(id);
        previousBalance.setAvailableBalance(newAmount);
        walletRepository.createPreviousBalance(previousBalance);
        return ResponseEntity.ok().body(true);

    }

    @GetMapping("/getUserWalletBalance/{userId}")
    public double getUserWalletBalance(@PathVariable(value = "userId") String userId) {
        double balance = walletRepository.getWalletBalance(userId);
        return balance;

    }
//----------------------------------END----------------------------------

//-----------------------------Generate Otp-------------------------------
    @PostMapping("/generateOtpFor/{userId}")
    public ResponseEntity<Boolean> generateOtp(@PathVariable(value = "userId") String id) {
    	OTPRecords otpRecords = new OTPRecords();
    	String Otp = otpGenerator();
    	otpRecords.setUserId(id);
    	otpRecords.setOtp(Otp);
    	walletRepository.SaveOtpRecord(otpRecords);
    	sendEmail(id,Otp);
        return ResponseEntity.ok().body(true);

    }

    void sendEmail(String userId,String Otp)
	{
    	User u = userRepository.getById(userId);
    	String Email = u.getMailId();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(Email);
        msg.setSubject("Free Coin One Time Password Authentication");
        msg.setText("Hello, \nYour One Time Password Is : " + Otp);
        javaMailSender.send(msg);

    }

    @GetMapping("/authenticateOtpFor/{userId}/{enteredOtp}")
    public ResponseEntity<Boolean> authenticateOtp(@PathVariable(value = "userId") String id,@PathVariable(value = "enteredOtp") String enteredOtp) {
    	OTPRecords otpRecords = walletRepository.getOtpByUserId(id);
    	if(otpRecords.getOtp().equals(enteredOtp))
    	{
    		return ResponseEntity.ok().body(true);
    	}
    	else
    	{
    		return ResponseEntity.ok().body(false);
    	}
    }

    public String otpGenerator()
    {
    	int min = 111111;
    	int max = 999999;
    	int otpI = (int)(Math.random()*(max-min+1)+min);
    	String Otp = "FMC"+otpI;
    	return Otp;
    }
//---------------------------------END------------------------------------

    @GetMapping("/getByRefId/{refId}")
    public User getByRefId(@PathVariable(value = "refId") String refId) {
        return userRepository.getByRefId(refId);
    }

    @GetMapping("/getChildDetails/left/{id}")
    public ResponseEntity<List<Map<String, Object>>> getLeftDetails(@PathVariable String id) {

        List<String> userIds = userService.getAllLeftSideChilds(id);

        List<User> allUsers = userRepository.getUserByIds(userIds);

        List<Wallet> userWallets = walletRepository.getByUserIds(userIds);

        Map<String, List<Wallet>> grpdWalletByUserId = userWallets.stream()
                .collect(Collectors.groupingBy(Wallet::getUserId));

        List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();

        allUsers.forEach(eachUser -> {
            Map<String, Object> output = new HashMap<String, Object>();

            if (grpdWalletByUserId.get(eachUser.getId()) != null) {
                output.put("name", eachUser.getName());
                output.put("total", grpdWalletByUserId.get(eachUser.getId()).get(0).getAmount());
                output.put("userId", eachUser.getId());
                response.add(output);
            }
        });
        // double totalLeftAmount = 0;
        // allUsers.forEach(eachUserId -> {
        // if (grpdWalletByUserId.get(eachUserId) != null) {
        // totalLeftAmount = totalLeftAmount +
        // grpdWalletByUserId.get(eachUserId).get(0).getAmount();
        // }
        // });
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/getChildDetails/right/{id}")
    public ResponseEntity<List<Map<String, Object>>> getRightDetails(@PathVariable String id) {

        List<String> userIds = userService.getAllRightSideChilds(id);

        List<User> allUsers = userRepository.getUserByIds(userIds);

        List<Wallet> userWallets = walletRepository.getByUserIds(userIds);

        Map<String, List<Wallet>> grpdWalletByUserId = userWallets.stream()
                .collect(Collectors.groupingBy(Wallet::getUserId));

        List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();

        allUsers.forEach(eachUser -> {

            Map<String, Object> output = new HashMap<String, Object>();
            if (grpdWalletByUserId.get(eachUser.getId()) != null) {
                output.put("name", eachUser.getName());
                output.put("total", grpdWalletByUserId.get(eachUser.getId()).get(0).getAmount());
                output.put("userId", eachUser.getId());
                response.add(output);
            }
        });
        // double totalLeftAmount = 0;
        // allUsers.forEach(eachUserId -> {
        // if (grpdWalletByUserId.get(eachUserId) != null) {
        // totalLeftAmount = totalLeftAmount +
        // grpdWalletByUserId.get(eachUserId).get(0).getAmount();
        // }
        // });
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/topupReport/{id}")
    public ResponseEntity<List<Transactions>> getTopupReport(@PathVariable(value = "id") String id) {

        List<Transactions> allTransactions = walletRepository.getByUserId(id).getTransactions();
        List<Transactions> topupTransactions = new ArrayList<Transactions>();

        allTransactions.forEach(eachTransaction -> {
            if (eachTransaction.getMessage().equals(TransactionMessage.TOPUP_DONE + "")) {
                topupTransactions.add(eachTransaction);
            }
        });
        return ResponseEntity.ok().body(topupTransactions);
    }

    @GetMapping("/referalReport/{id}")
    public ResponseEntity<List<Transactions>> getReferalReport(@PathVariable(value = "id") String id) {

        List<Transactions> allTransactions = walletRepository.getByUserId(id).getTransactions();
        List<Transactions> referalTransactions = new ArrayList<Transactions>();

        allTransactions.forEach(eachTransaction -> {

            String transactionFullMessage = eachTransaction.getMessage();
            String[] spilt = transactionFullMessage.split("/");

            Boolean leftCondition = spilt[0].equals(TransactionMessage.NEW_LEFT_CHILD_JOIN + "");
            Boolean rightCondition = spilt[0].equals(TransactionMessage.NEW_RIGHT_CHILD_JOIN + "");

            if (leftCondition || rightCondition) {
                referalTransactions.add(eachTransaction);
            }
        });
        return ResponseEntity.ok().body(referalTransactions);
    }

    @GetMapping("/incomeReport/{id}")
    public ResponseEntity<List<Transactions>> getIncomeReport(@PathVariable(value = "id") String id) {

        List<Transactions> allTransactions = walletRepository.getByUserId(id).getTransactions();
        List<Transactions> topupTransactions = new ArrayList<Transactions>();

        allTransactions.forEach(eachTransaction -> {
            if (eachTransaction.getType().equals(TransactionType.C)) {
                topupTransactions.add(eachTransaction);
            }
        });
        return ResponseEntity.ok().body(topupTransactions);
    }

    @PostMapping("/notice/create")
    public Notice createNotice(@RequestBody Notice newNotice) {
        newNotice.setCreatedAt(new Date());
        return userRepository.createNotice(newNotice);
    }

    @GetMapping("/notice/{id}")
    public Map<NoticeType, List<Notice>> getNoticeByUserId(@PathVariable(value = "id") String id) {
        List<Notice> allNotice = userRepository.getNoticeByUserId(id);

        if (allNotice != null && allNotice.size() > 0) {
            Map<NoticeType, List<Notice>> grpdNoticeByType = allNotice.stream()
                    .collect(Collectors.groupingBy(Notice::getNoticeType));
            return grpdNoticeByType;
        } else {
            return null;
        }
    }

    @GetMapping("/bank/{id}")
    public Bank getBank(@PathVariable(value = "id") String id) {
        return userRepository.getBankDetailsByUserId(id);
    }

    @PostMapping("/bank/update")
    public Boolean createNotice(@RequestBody Bank bank) {
        return userRepository.updateBankDetails(bank) != null;
    }

    @PostMapping("/user/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userRepository.updateUserDetails(user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/chart/{id}")
    public List<Map<String, Object>> getChartData(@PathVariable(value = "id") String id) {

        List<Node> allNodes = nodeRepository.getAllNodes();
        Map<String, List<Node>> grpdNodesByUserId = allNodes.stream().collect(Collectors.groupingBy(Node::getUserId));

        List<User> allUsers = userRepository.getAllUsers();
        Map<String, List<User>> grpdUsersById = allUsers.stream().collect(Collectors.groupingBy(User::getId));

        List<Map<String, Object>> customUserList = new ArrayList<>();
        List<Map<String, Object>> response = new ArrayList<>();

        for (User user : allUsers) {
            Map<String, Object> customUser = new HashMap<>();
            ArrayList<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
            customUser.put("name", user.getName());
            customUser.put("refId", user.getRefId());
            customUser.put("id", user.getId());
            customUser.put("children", childrenList);
            customUserList.add(customUser);
        }

        customUserList.forEach(eachUser -> {
            String userId = (String) eachUser.get("id");
            ArrayList<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();

            if (grpdNodesByUserId.get(userId) != null) {

                Node userNode = grpdNodesByUserId.get(userId).get(0);

                if (userNode.getRight() != null) {
                    String rightUserId = userNode.getRight().getUserId();
                    Map<String, Object> user = new HashMap<String, Object>();
                    user.put("name", grpdUsersById.get(rightUserId).get(0).getName());
                    user.put("refId", grpdUsersById.get(rightUserId).get(0).getRefId());
                    user.put("id", grpdUsersById.get(rightUserId).get(0).getId());

                    customUserList.forEach(innerLoop -> {
                        String currentUserId = (String) innerLoop.get("id");
                        if (currentUserId.equals(rightUserId)) {
                            childrenList.add(innerLoop);
                        }
                    });
                } else {
                    childrenList.add(null);
                }

                if (userNode.getLeft() != null) {
                    String leftUserId = userNode.getLeft().getUserId();
                    Map<String, Object> user = new HashMap<String, Object>();
                    user.put("name", grpdUsersById.get(leftUserId).get(0).getName());
                    user.put("refId", grpdUsersById.get(leftUserId).get(0).getRefId());
                    user.put("id", grpdUsersById.get(leftUserId).get(0).getId());

                    customUserList.forEach(innerLoop -> {
                        String currentUserId = (String) innerLoop.get("id");
                        if (currentUserId.equals(leftUserId)) {
                            childrenList.add(innerLoop);
                        }
                    });
                } else {
                    childrenList.add(null);
                }
                eachUser.put("children", childrenList);
            }
        });

        customUserList.forEach(eachUser -> {
            if (id.equals(eachUser.get("id"))) {
                response.add(eachUser);
            }
        });

        return response;
    }

    @PostMapping("/dailyprice/create")
    public Boolean createDailyPrice(@RequestBody Dailyprice dailypriceReq) {
        return userRepository.createDailyPrice(dailypriceReq);
    }

    @GetMapping("/dailyprice")
    public Dailyprice getDailyPrice() {
        return userRepository.getDailyPrice();
    }

    @PostMapping("/dailyprice/change")
    public Boolean changeDailyPrice(@RequestBody Dailyprice dailypriceReq) {
        return userRepository.changeDailyPrice(dailypriceReq);
    }

    @GetMapping("/allUsersList")
    public List<User> getAllUsersList() {
        return userRepository.getAllUsers();
    }

    @GetMapping("/allNodesList")
    public List<Node> getAllNodesList() {
        return nodeRepository.getAllNodes();
    }

    @GetMapping("/allWalletList")
    public List<Wallet> getUserWalletList() {
        return walletRepository.getAllWalletDocs();
    }

    @GetMapping("/allPreviousBalanceList")
    public List<PreviousBalance> getUserPreviousBalanceList() {
        return walletRepository.getAllPreviousBalance();
    }

    @PutMapping("/updateJoiningDate")
    public ResponseEntity<User> updateJoiningDateByRefId(@RequestParam String refId, @RequestParam String joiningDate) {
        User user = null;
        if (isEmpty(refId) && isEmpty(joiningDate)) {
            Date userJoinDate = null;
            try {
                userJoinDate = new SimpleDateFormat("dd-MM-yyyy").parse(joiningDate);
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body(user);
            }
            user = userRepository.updateUserJoiningDate(refId, userJoinDate);
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.badRequest().body(user);
    }

    // Mock API to update joining date - can be removed later
    @GetMapping("/mockUserJoinDate")
    public Boolean mockJoinDate() {
        Boolean isSuccess = false;
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("datafreeminers.json")) {
            Object obj = jsonParser.parse(reader);
            JSONObject users = (JSONObject) obj;
            JSONArray userList = (JSONArray) users.get("Sheet1");
            userList.forEach(user -> parseUserObject((JSONObject) user));
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    private boolean isEmpty(String value) {
        if (value == null || value == "null" || value == "")
            return true;
        return false;
    }

    private void parseUserObject(JSONObject User) {
        String refId = (String) User.get("New id");
        String joiningDate = (String) User.get("Join date");
        updateJoiningDateByRefId(refId, joiningDate);
    }

    @GetMapping("/wallet/classify/{id}")
    public Map<String, Object> classifyWallet(@PathVariable(value = "id") String id) {
        System.out.println("userId " + id);
        Wallet userWallet = walletRepository.getByUserId(id);
        Map<String, Object> response = new HashMap<String, Object>();
        double activeWallet = 0, earningsWallet = 0;

        List<Transactions> transactions = userWallet.getTransactions();

        for (Transactions transaction : transactions) {
            TransactionType tType = transaction.getType();
            String tMsg = transaction.getMessage();

            // Credit Function
            if (tType.equals(TransactionType.C)) {
                String[] split = tMsg.split("/");

                // Active Wallet Functions
                if (split[0].equals("NEW_JOIN")) {
                    activeWallet = activeWallet + transaction.getAmount();
                } else if (split[0].equals("TOPUP_DONE")) {
                    activeWallet = activeWallet + transaction.getAmount();
                } else if (split[0].equals("TOPUP_SELF_RETURN")) {
                    activeWallet = activeWallet + transaction.getAmount();
                }

                // Earnings Wallet Functions
                else if (split[0].equals("NEW_RIGHT_CHILD_JOIN") || split[0].equals("NEW_LEFT_CHILD_JOIN")) {
                    earningsWallet = earningsWallet + transaction.getAmount();
                } else if (split[0].equals("CHILD_TOPUP_DONE")) {
                    earningsWallet = earningsWallet + transaction.getAmount();
                } else {
                    //System.out.println("Error");
                }
            }
        }

        response.put("activeWallet", activeWallet);
        response.put("earningsWallet", earningsWallet);
        return response;
    }

    @GetMapping("/allTransactions")
    public List<Transactions> getAllTransactions()
    {
    	return walletRepository.getAllTransactions();
    }

    @GetMapping("/getPlacementDetails/{id}")
    public Map<String, Object> getPlacementDetails(@PathVariable(value = "id") String id) {

        Map<String, Object> response = new HashMap<String, Object>();
        String userMailId = userRepository.getById(id).getMailId();
        String userPassword = authRepository.getAuthByUserId(id).getPassword();

        // List<Node> result = nodeRepository.getPlacementParentUserId(id);

        // if (result != null) {
        // String placementParentId = result.get(0).getUserId();
        // User parentData = userRepository.getById(placementParentId);
        // String placementParentName = parentData.getName();
        // String placementParentRefId = parentData.getRefId();

        // response.put("placementParentRefId", placementParentRefId);
        // response.put("placementParentName", placementParentName);
        // }

        response.put("userPassword", userPassword);
        response.put("userMailId", userMailId);
        return response;
    }

    @PostMapping("/createBalanceTest")
    public PreviousBalance setBalanceAndJoinDate(@RequestBody Map<String, Object> reqData)
            throws java.text.ParseException {

        /**
         * { "userId": "619760f7f2a57786bbdb96bc", "availableBalance": 1000000,
         * "earningBalance": 0, "stacking": 0, "stackingAt":
         * "2021-11-26T17:40:28.990Z",
         * "joiningDate": "2021-11-26T17:40:28.990Z" },
         */

        double availableBalance = Double.valueOf((String) reqData.get("availableBalance"));
        double earningBalance = Double.valueOf((String) reqData.get("earningBalance"));
        double stacking = Double.valueOf((String) reqData.get("stacking"));

        Date stackingAt = new SimpleDateFormat("yyyy-MM-dd").parse((String) reqData.get("stackingAt"));
        String userId=null;
        List<User> users = new ArrayList<>();
        users=userRepository.findAll();
        for(User user:users)
        {
        	if(user.getRefId().equals(reqData.get("userId")))
        	{
        		userId = user.getId();
        	}
        }

        PreviousBalance newPreviousBalance = new PreviousBalance();

        newPreviousBalance.setAvailableBalance(availableBalance);
        newPreviousBalance.setEarningBalance(earningBalance);
        newPreviousBalance.setStacking(stacking);
        newPreviousBalance.setUserId(userId);
        if (stacking > 0) {
            newPreviousBalance.setStackingAt(stackingAt);
        }
        return userRepository.setPreviousBalanceByUserId(newPreviousBalance);
    }

	@PostMapping("/add300000ToParentId")
	public String add300000ToParentId(@RequestBody List<RefId> refIdList){
		for (RefId refId: refIdList ){
			User user = userRepository.getByRefId(refId.getRefId());
			PreviousBalance previousBalance = userRepository.getUserPreviousBalanceByUserId(user.getParentId());
			if (previousBalance != null) {
				previousBalance.setAvailableBalance(previousBalance.getAvailableBalance() + 3000000);
				userRepository.setPreviousBalanceByUserId(previousBalance);
			} else{
				System.out.println(user.getParentId());
			}

		}
		return "ok";
	}

	@PostMapping("/roiAdjustment")
	public List<Transactions> roiAdjustmentEntireWallet(){ 
	// public List<Transactions> roiAdjustmentSingleWallet(@RequestBody User user){ 
        // HashMap<String,Wallet> walletHashMap = mapping.walletHashMap();
        // Wallet wallet = walletHashMap.get(user.getId());
		List<Transactions> transactionsList = new ArrayList<>();
        // List<Wallet> balWallets = walletRepository.getAll();
        List<Wallet> walletList = new ArrayList<>();
		int numOfTotalTrans = 0;
		int numOfTotalWallets = 0;
        int numOfNewWallets = 0;

            /*For Single wallet */

		// for (Transactions transactions : wallet.getTransactions()) 
        // {
		// 	if (transactions.getMessage().equals("ROI") && transactions.getTransactionAt().getDate() == 6 && transactions.getTransactionAt().getMonth() == 1 
        // && transactions.getTransactionAt().getYear() == 123)
        //      {
		// 		transactionsList.add(transactions);
		// 		numOfTotalTrans += 1;
		// 		System.out.println(transactions);
        // //         wallet.setStackingWallet(wallet.getStackingWallet()-transactions.getFcAmount());
		// // 		// wallet.setStackingWallet(wallet.getStackingWallet() + transactions.getFcAmount());
		// // 		wallet.getTransactions().remove(transactions);
		// // 		// transactions.getTransactionAt().setDate(5);
		// // 		walletRepository.SaveWallet(wallet);
		// // 		// System.out.println(transactions);
		// 		break;  
		// 	}
		// }

                /* For entire Wallet */

        int count = 0, j = 0;
		for (Wallet wallet : walletRepository.getAll())
		{
            int i = 0;
            numOfTotalWallets++;
            // if(wallet.getRoiAmount() == 0) {
            //     walletList.add(wallet);
            //     continue;
            // }
			for (Transactions transactions : wallet.getTransactions())
			{
                // if (transactions.getMessage().equals("ROI") && transactions.getTransactionAt().getDate() == 6 && transactions.getTransactionAt().getMonth() == 1 
                // && transactions.getTransactionAt().getYear() == 123)
				// {
			    //     walletList.add(wallet);
                // }
				if (transactions.getMessage().equals("ROI") && transactions.getTransactionAt().getDate() == 2 && transactions.getTransactionAt().getMonth() == 2 
                && transactions.getTransactionAt().getYear() == 123)
				{
                    // i++;j++;
                    // if(i>1){ 
                        walletList.add(wallet);
                        transactionsList.add(transactions);
                        numOfTotalTrans++;
                        System.out.println(wallet.getUserId());
                        // wallet.setStackingWallet(wallet.getStackingWallet()-transactions.getFcAmount());
                        // // transactions.getTransactionAt().setDate(6);
                        // wallet.getTransactions().remove(transactions);
                        // walletRepository.SaveWallet(wallet);
                        break;
                    // }
				}
                // balWallets.removeAll(walletList);
                
			}
            // break;

		}
		System.out.println("NumofTrans : "  + numOfTotalTrans);
		System.out.println("NumofWallet : "  + numOfTotalWallets);
		System.out.println("Numof new Wallet : "  + numOfNewWallets);
		// System.out.println("Numof Bal Wallet : "  + balWallets.size());
		System.out.println("Number of wallets condition : "  + walletList.size());
        System.out.println("Wallet met condition :" + j);
		return transactionsList;
    }

	@PostMapping("/pairMatchTest1")
	public Double pairMatchTest1(@RequestBody User user){
		double amount = 0;
		int count = 0;
		List<Wallet> walletList = walletRepository.findAll();
		for(Wallet wallet : walletList){
			count +=1;
			List<Transactions> transactionsList = walletRepository.getByUserId(wallet.getId()).getTransactions();
			for(Transactions transaction : transactionsList)
			{
				TransactionType tType = transaction.getType();
				String tMsg = transaction.getMessage();

				if (tType.equals(TransactionType.C))
				{
					String[] split = tMsg.split("/");
					if(split[0].equals("TOPUP_DONE"))
					{
						amount+=transaction.getAmount();
					}
				}
			}
		}
		System.out.println("Count : " + count);
		return amount;
	}

	List<User> allRightUsersG = new ArrayList<>();
	List<User> allLeftUsersG = new ArrayList<>();
	HashMap<String, List<Node>> allLeftUsersHashMap = new HashMap<>();
	HashMap<String, List<Node>> allRightUsersHashMap = new HashMap<>();
	List<User> pairMatchLeftUsersG = new ArrayList<>();
	List<User> pairMatchRightUsersG = new ArrayList<>();
	List<TopupRequest> topupRequestListG = new ArrayList<>();
	double leftAmount = 0;
	double rightAmount = 0;

	@PostMapping("/findingChildren")
	public ResponseEntity<String> findingChildren(@RequestBody User user){
		allLeftUsersG.clear();
		allRightUsersG.clear();
//		topupRequestListG = walletRepository.getAllAcceptedTopupRequest();
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		Node userNode = nodeHashMap.get(user.getId());
		if (userNode.getLeft() != null) findChildren(userNode.getLeft().getUserId(), true, user.getId());
		if (userNode.getRight() != null) findChildren(userNode.getRight().getUserId(), false, user.getId());
		System.out.println("allLeftUsersGuru : " + allLeftUsersG.size());
		System.out.println("allRightUsersGuru : " + allRightUsersG.size());
		return ResponseEntity.ok("Done finding Children");
	}

    @PostMapping("/findingChildrenMapByUserId")
	public ResponseEntity<String> findingChildrenMapByUserId(@RequestBody User user1){
		HashMap<String, User> userHashMap = mapping.userMapping();
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		User user = userHashMap.get(user1.getId());
		System.out.println(user);
		Node userNode = nodeHashMap.get(user.getId());
		if (userNode != null && userNode.getLeft() != null)
			childrenNodeMap("left", userNode.getLeft().getUserId(), userNode.getUserId());
		if (userNode != null && userNode.getRight() != null)
			childrenNodeMap("right", userNode.getRight().getUserId(), userNode.getUserId());
		if (allLeftUsersHashMap.containsKey(user.getId()))
			System.out.println("Size of left users : " + allLeftUsersHashMap.get(user.getId()).size());
		else
			System.out.println("Size of left users : 0");
		if (allRightUsersHashMap.containsKey(user.getId()))
			System.out.println("Size of Right users : " + allRightUsersHashMap.get(user.getId()).size());
		else
			System.out.println("Size of Right users : 0");
        System.out.println("findingChildrenMapByUserId is Done" + "\n\n*******************\n");
		return ResponseEntity.ok("Done findingChildrenMap");

	}

	@PostMapping("/findingChildrenMap")
	public ResponseEntity<String> findingChildrenMap(){
		HashMap<String, User> userHashMap = mapping.userMapping();
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		User user = userHashMap.get("619760f7f2a57786bbdb96bc");
		System.out.println(user);
		Node userNode = nodeHashMap.get(user.getId());
		if (userNode != null && userNode.getLeft() != null)
			childrenNodeMap("left", userNode.getLeft().getUserId(), userNode.getUserId());
		if (userNode != null && userNode.getRight() != null)
			childrenNodeMap("right", userNode.getRight().getUserId(), userNode.getUserId());
		if (allLeftUsersHashMap.containsKey(user.getId()))
			System.out.println("Size of left users : " + allLeftUsersHashMap.get(user.getId()).size());
		else
			System.out.println("Size of left users : 0");
		if (allRightUsersHashMap.containsKey(user.getId()))
			System.out.println("Size of Right users : " + allRightUsersHashMap.get(user.getId()).size());
		else
			System.out.println("Size of Right users : 0");
        System.out.println("findingChildrenMap is Done" + "\n\n*******************\n");
		return ResponseEntity.ok("Done findingChildrenMap");
	}
    
	@PostMapping("/findingChildrenByUserId")
	public ResponseEntity<String> findingChildrenByUserId(@RequestBody User user){
		HashMap<String, User> userHashMap = mapping.userMapping();
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		findingChildrenMapByUserId(user);
		return ResponseEntity.ok("Done");
	}

	@PostMapping("/getTopupTransactionsRecord")
	public ResponseEntity getTopupTransactionsRecord() {
		int count = 0;
//		User user1 = userRepository.getById(user.getId());
		List<TopupRequest> topupRequestList1= walletRepository.getAllAcceptedTopupRequest();
		for (User user : userRepository.getAllUsers()) {
			count += 1;
			double amount = 0;
			List<TopupRequest> topupRequestList = new ArrayList<>();
			for (TopupRequest topupRequest : topupRequestList1) {
				if (topupRequest.getUserId().equals(user.getId())) {
//				if (transactions.getType().equals(TransactionType.C) && transactions.getMessage().equals("TOPUP_DONE")) {
					amount += topupRequest.getAmount();
					topupRequestList.add(topupRequest);
//					amount += transactions.getAmount();
//					TopupTransactionsRecord topupTransactionsRecord = new TopupTransactionsRecord();
//					topupTransactionsRecord.setUserId(wallet.getUserId());
//					topupTransactionsRecord.setTopup_Transactions(topupTransactions);
//					topupTransactionsRecord.setTotalTopUpStackingAmount(amount);
//					walletRepository.createTopupTransactionsRecord(topupTransactionsRecord);
					TopupTransactionsRecord topupTransactionsRecord = new TopupTransactionsRecord();
					topupTransactionsRecord.setUserId(user.getId());
					topupTransactionsRecord.setTopup_Transactions(topupRequestList);
					topupTransactionsRecord.setTotalTopUpStackingAmount(amount);
					walletRepository.createTopupTransactionsRecord(topupTransactionsRecord);
					System.out.println("UserID : " + topupTransactionsRecord.getUserId());
					System.out.println("Topup Transaction size : " + topupTransactionsRecord.getTopup_Transactions().size());
					System.out.println("Amount : " + amount);
					System.out.println("Count : " + count);
				}
			}
//			return ResponseEntity.ok("Done TopupTrans");

//			System.out.println("UserID : " + top.getUserId());
//			System.out.println("Topup Transaction size : " + topupTransactionsRecord.getTopup_Transactions().size());
//			System.out.println("Amount : " + amount);
//			System.out.println("Count : " + count);
//			if(count>3)
//			return ResponseEntity.ok("Done Top up");
		}
		return ResponseEntity.ok("Done TopupTrans");
	}

	public HashMap<String, Object> runPairMatchNew(User user){
		HashMap<String, User> userHashMap = mapping.userMapping();
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		HashMap<String, TopupTransactionsRecord> topupTransactionsRecordHashMap = mapping.topupTransactionsRecordHashMapping();
		HashMap<String, Double> pastPairMatchAmountHashMap = mapping.pastPairMatchAmountMap();
		HashMap<String,Object> response = new HashMap<>();
		if(allLeftUsersHashMap.containsKey(user.getId()) || allRightUsersHashMap.containsKey(user.getId())) {
			double userTopupStacking = 0;
			if(topupTransactionsRecordHashMap.containsKey(user.getId()))
			userTopupStacking = topupTransactionsRecordHashMap.get(user.getId()).getTotalTopUpStackingAmount();
			int leftCount = 0, rightCount = 0;
			double totalLeftAmount = 0, totalRightAmount = 0, currentPairMatchAmount = 0, pairMatchAmount;
			double pastPairMatchAmount = 0;

			List<Node> allLeftUsersNodeList = allLeftUsersHashMap.get(user.getId());
			List<Node> allRightUsersNodeList = allRightUsersHashMap.get(user.getId());
			if(allLeftUsersNodeList!=null)
			for (Node node : allLeftUsersNodeList)
				if (topupTransactionsRecordHashMap.containsKey(node.getUserId())) {
					leftCount += 1;
					totalLeftAmount += topupTransactionsRecordHashMap.get(node.getUserId()).getTotalTopUpStackingAmount();
				}
			if(allRightUsersNodeList!=null)
			for (Node node : allRightUsersNodeList)
				if (topupTransactionsRecordHashMap.containsKey(node.getUserId())) {
					rightCount += 1;
					totalRightAmount += topupTransactionsRecordHashMap.get(node.getUserId()).getTotalTopUpStackingAmount();
				}
			if (totalLeftAmount <= totalRightAmount)
				pairMatchAmount = totalLeftAmount * 0.05;
			else
				pairMatchAmount = totalRightAmount * 0.05;


			if (pastPairMatchAmountHashMap.containsKey(user.getId()))
				pastPairMatchAmount = pastPairMatchAmountHashMap.get(user.getId());

			if (pairMatchAmount > pastPairMatchAmount) {
				currentPairMatchAmount = pairMatchAmount - pastPairMatchAmount;
				if (currentPairMatchAmount > userTopupStacking/2)
					currentPairMatchAmount = userTopupStacking/2;
				if (topupTransactionsRecordHashMap.containsKey(user.getId()))
					pairMatchTopupNew(user.getId(),currentPairMatchAmount);
			}
			pastPairMatchAmountHashMap.put(user.getId(),pairMatchAmount);
			PairMatchingRecord pairMatchingRecord = new PairMatchingRecord();
			pairMatchingRecord.setUserId(user.getId());
			pairMatchingRecord.setPairMatchedAmount(pairMatchAmount);
			walletRepository.createPairMatchingRecord(pairMatchingRecord);
			BusinessRecord businessRecord = new BusinessRecord();
			businessRecord.setUserId(user.getId());
			businessRecord.setLeftBusinessAmount(totalLeftAmount);
			businessRecord.setRightBusinessAmount(totalRightAmount);
			if(allLeftUsersNodeList == null)
				businessRecord.setLeftUsers(0);
			else
			businessRecord.setLeftUsers(allLeftUsersNodeList.size());
			businessRecord.setLeftActiveUsers(leftCount);
			if(allRightUsersNodeList == null)
				businessRecord.setRightUsers(0);
			else
			businessRecord.setRightUsers(allRightUsersNodeList.size());
			businessRecord.setRightActiveUsers(rightCount);
			businessRecord.setTotalStacking(userTopupStacking);
			walletRepository.SaveBusinessRecord(businessRecord);
			System.out.println("User Id : " + user.getId());
			System.out.println("left active Count : " + leftCount);
			System.out.println("Right active Count : " + rightCount);
			System.out.println("Total left Business : " + totalLeftAmount);
			System.out.println("Total Right Business : " + totalRightAmount);
			System.out.println("Pair Match Amount : " + pairMatchAmount);
			System.out.println("Current Pair match Amount: " + currentPairMatchAmount);
			System.out.println("Past Pair match Amount : " + pastPairMatchAmount);
			System.out.println("User Stacking topup : " + userTopupStacking);
		}

		return response;
	}
    
	@PostMapping("/newPairMatchByUserId")
	public ResponseEntity<String> newPairMatchByUserId(@RequestBody User user){
        mapping.clearAllMap();
		HashMap<String, User> userHashMap = mapping.userMapping();
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		HashMap<String, TopupTransactionsRecord> topupTransactionsRecordHashMap = mapping.topupTransactionsRecordHashMapping();
		HashMap<String, Double> pastPairMatchAmountHashMap = mapping.pastPairMatchAmountMap();
		findingChildrenMapByUserId(user);
		runPairMatchNew(user);
		return ResponseEntity.ok("Done newPairMatchByUserId");
	}

	@PostMapping("/newPairMatching")
	public ResponseEntity<String> newPairMatching(){
        mapping.clearAllMap();
		HashMap<String, User> userHashMap = mapping.userMapping();
		HashMap<String, Node> nodeHashMap = mapping.nodeMapping();
		HashMap<String, TopupTransactionsRecord> topupTransactionsRecordHashMap = mapping.topupTransactionsRecordHashMapping();
		HashMap<String, Double> pastPairMatchAmountHashMap = mapping.pastPairMatchAmountMap();
		findingChildrenMap();
		for(Map.Entry<String,User> user1 : userHashMap.entrySet() ) {
				runPairMatchNew(user1.getValue());
			}
		return ResponseEntity.ok("Pair Match Done Successfully");
	}

	@PostMapping("/demo1NotWorking") //TODO 
	public Double topupInternalTransferNotWorking(@RequestBody TopupRequest topupRequest){
		double fcAmount = 0;
		createTopupRequest(topupRequest);
		acceptTopupRequest(topupRequest);
		TransferWalletTransactions transferWalletTransactions = new TransferWalletTransactions();
		transferWalletTransactions.setFromUserId(topupRequest.getUserId());
		transferWalletTransactions.setFromRefId(topupRequest.getRefId());
		transferWalletTransactions.setToUserId("62f93fd489e9e602991cd354");
		transferWalletTransactions.setToRefId("FM8627446");
		transferWalletTransactions.setAmount(getCoinValueForUSDLocal(topupRequest.getAmount()));
		internalTransfer(transferWalletTransactions);
		return getCoinValueForUSDLocal(topupRequest.getAmount());
	}

	@GetMapping("demo9/{userId}")
	public List<TransferWalletTransactions> getTopupTransferWalletTransactionsByFromUserId(@PathVariable (value = "userId") String userId ){
        return walletRepository.getTransferWalletTransactionsByFromUserId(userId);
	}

	@PostMapping("/demo3")
	public List<TransferWalletTransactions> getAllTransferWalletTransactions(){
        return walletRepository.getAllTransferWalletTransactions();
	}

    @GetMapping("/getCreditedTransferWalletTransactions/{userId}")
    public List<TransferWalletTransactions> getCreditedTransferWalletTransactions(@PathVariable(value = "userId") String userId) {
        return walletRepository.getallCreditedTransferWalletTransactionsByUserId(userId);
    }
    

	@PutMapping("/setTopupRequestName")
	public ResponseEntity<String> setTopupRequestName(){
		HashMap<String,User> userHashMap = mapping.userMapping();
        int count =0;
		for (TopupRequest topupRequest: walletRepository.getAllTopupRequest()){
			if(topupRequest.getUserId()!=null) {
				topupRequest.setName(userHashMap.get(topupRequest.getUserId()).getName());
				walletRepository.createTopupRequest(topupRequest);
                count++;
			}
		}
        System.out.println(count);
		return ResponseEntity.ok("ok");
	}

    @PutMapping("/setWithdrawRequestName")
	public ResponseEntity<String> setWithdrawRequestName(){
		HashMap<String,User> userHashMap = mapping.userMapping();
        int count =0;
		for (WithdrawRequest withdrawRequest: walletRepository.getAllWithdrawRequest()){
			if(withdrawRequest.getUserId()!=null && withdrawRequest.getName() == null) {
                if(withdrawRequest.getUserId()!= null)
                    withdrawRequest.setName(userHashMap.get(withdrawRequest.getUserId()).getName());
    	        else
                    withdrawRequest.setName(userHashMap.get(withdrawRequest.getRefId()).getName());
                walletRepository.createWithdrawRequest(withdrawRequest);
                count++;
			}
		}
        System.out.println(count);
		return ResponseEntity.ok("ok");
	}

    @PutMapping("/setTransferWalletTransactionsName")
	public ResponseEntity<String> setTransferWalletTransactionsName(){
		HashMap<String,User> userHashMap = mapping.userMapping();
        int count =0;
		for (TransferWalletTransactions transferWalletTransactions: walletRepository.getAllTransferWalletTransactions()){
			if(transferWalletTransactions.getFromUserId()!=null && transferWalletTransactions.getName() == null) {
                if(transferWalletTransactions.getFromUserId()!= null)
                    transferWalletTransactions.setName(userHashMap.get(transferWalletTransactions.getFromUserId()).getName());
    	        else
                    transferWalletTransactions.setName(userHashMap.get(transferWalletTransactions.getFromRefId()).getName());
                walletRepository.createTransferWalletTransactions(transferWalletTransactions);
                count++;
			}
		}
        System.out.println(count);
		return ResponseEntity.ok("ok");
	}

	@GetMapping("/getTopUpTransactionbyuserid")
	public TopupTransactionsRecord getTopUpTransactionbyuserid(@RequestBody User user){
		System.out.println(walletRepository.getTopupTransactionsRecordByUserId(user.getId()));
		return walletRepository.getTopupTransactionsRecordByUserId(user.getId());
	}

    @GetMapping("getBusinessMatch")
    public Transactions getBusinessMatch(){
        Transactions transactions = new Transactions();
        for (Wallet wallet : walletRepository.getAll()){
            for(Transactions transaction : wallet.getTransactions()){
                if(transaction.getMessage().equals("BUSINESS_MATCH") && transaction.getTransactionAt().getDate() == 2 && transaction.getTransactionAt().getMonth() == 9 && transaction.getTransactionAt().getYear() == 122){
                    System.out.println(wallet.getUserId());
                    System.out.println(transaction);
                    return transaction;
                }
            }
        }
        return transactions;
    }

    @GetMapping("/testingDisplay")
    public String testingDisplay(){
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.set(Calendar.YEAR, 2021);
        date1.set(Calendar.DATE, 9);
        // date1.set(Calendar.MONTH, 1);
        LocalDate localDate1 = LocalDate.of(date1.get(Calendar.YEAR)+1900, date1.get(Calendar.MONTH)+1, date1.get(Calendar.DAY_OF_MONTH));
        LocalDate localDate2 = LocalDate.of(date2.get(Calendar.YEAR)+1900, date2.get(Calendar.MONTH)+1, date2.get(Calendar.DAY_OF_MONTH));
        System.out.println(localDate1);
        System.out.println(date1.getTime());
        System.out.println(date2.getTime());
        Period diff = Period.between(localDate1, localDate2);
        System.out.println("Years:"+diff.getYears()+", Months:"+diff.getMonths()+", Days:"+ diff.getDays());
        return "Hello";
    }

    @PutMapping("/addingIs365Days")
    public ResponseEntity<String> addingIs365Days(){
        List<TopupRequest> topupRequests = walletRepository.getAllAcceptedTopupRequest();
        int count = 0;
        for(TopupRequest topupRequest : topupRequests){
            if(topupRequest.getIs365Days())
            continue;
            Date respondedDate = topupRequest.getRespondedAt();
            Date todayDate = Calendar.getInstance().getTime();
            LocalDate respondedLocalDate = LocalDate.of(respondedDate.getYear()+1900, respondedDate.getMonth()+1,respondedDate.getDate());
            LocalDate todayLocalDate = LocalDate.of(todayDate.getYear()+1900, todayDate.getMonth()+1,todayDate.getDate());
            Period diffBwDate = Period.between(respondedLocalDate, todayLocalDate);
            if (diffBwDate.getYears() >= 1 ){
                count++;
                topupRequest.setIs365Days(true);
                System.out.println("Count : " + count);
            } 
            else topupRequest.setIs365Days(false);
            mongoTemplate.save(topupRequest);
        }
        return ResponseEntity.ok("Done");
    }
    @PutMapping("/addingIs365Reduction")
    public void add(){
        int count = 0;
        List<TopupRequest> topupRequests = walletRepository.getAllTopupRequest();
        for(TopupRequest topupRequest:topupRequests){
            // if(topupRequest.getIs365Reduction().equals(null)){
                // if(topupRequest.getUserId().equals("6232ee4c90acbd7c34771a60")){
                count++;
                // System.out.println(topupRequest);
                // break;
                // topupRequest.setIs365Reduction(false);
                mongoTemplate.save(topupRequest);
            // }
        }
        System.out.println("Count : " + count);
    }

    @PutMapping("/add365DaysDeductionTransaction")
    public ResponseEntity<String> add365DaysDeductionTransaction(){
        addingIs365Days();
        int count = 0;
        List<TopupRequest> topupRequests = walletRepository.getAllAcceptedTopupRequest();
        for(TopupRequest topupRequest : topupRequests){
            if(topupRequest.getIs365Days().equals(true) && topupRequest.getIs365Reduction().equals(false)){
                count++;
                System.out.println(topupRequest.getUserId());
                Wallet wallet = walletRepository.getByUserId(topupRequest.getUserId());;
                for(Transactions orgTransaction:wallet.getTransactions()){
                    if(orgTransaction.getMessage().equals("TOPUP_DONE") && orgTransaction.getTransactionAt().getYear() == topupRequest.getRespondedAt().getYear() && 
                    orgTransaction.getTransactionAt().getMonth() == topupRequest.getRespondedAt().getMonth() &&
                    orgTransaction.getTransactionAt().getDate() == topupRequest.getRespondedAt().getDate())
                    {
                    Transactions newTransaction = new Transactions(orgTransaction.getAmount(), TransactionType.D, "365_DAYS_DONE", orgTransaction.getFcAmount());
                    newTransaction.setProcessed(true);
                    newTransaction.setTopupDoneDate(orgTransaction.getTransactionAt());
                    walletRepository.add365DaysDeductionTransaction(topupRequest.getUserId(), newTransaction);
                    topupRequest.setIs365Reduction(true);
                    mongoTemplate.save(topupRequest);
                    break;
                    }
                }
            }
            // break;
        }
        System.out.println("Count : " + count);
        return ResponseEntity.ok("Done");
    }

    @PutMapping("/adjusting")
    public String adjusting(){
        List<TopupRequest> topupRequests = walletRepository.getAllAcceptedTopupRequest();
        int count = 0, count2 = 0;
        for(TopupRequest topupRequest : topupRequests){
            if(topupRequest.getIs365Days() && topupRequest.getIs365Days()){
                count++;
                Wallet wallet = walletRepository.getByUserId(topupRequest.getUserId());
                if(wallet.getRoiAmount()<0){
                    wallet.setRoiAmount(0);
                    mongoTemplate.save(wallet);
                    continue;
                }
                for(Transactions transaction:wallet.getTransactions()){
                    if(transaction.getMessage().equals("365_DAYS_DONE")){
                        count2++;
                        // System.out.println(transaction);
                        // wallet.setRoiAmount(wallet.getRoiAmount()-transaction.getFcAmount()*0.0037);
                        // wallet.setRoiAmount(wallet.getRoiAmount()-transaction.getAmount());
                        System.out.println(transaction.getFcAmount()*0.0037);
                        // mongoTemplate.save(wallet);
                        break;
                    }
                }
                // break;
            }
        }
        System.out.println("count : " + count);
        System.out.println("count2 : " + count2);
        return "Done";
    }

    @GetMapping(value="/getAllTopupRequest")
    public List<TopupRequest> getAllTopupRequest() {
        return walletRepository.getAllTopupRequest();
    }

    @GetMapping(value = "/getUserName/{refId}") 
    public String getUserName(@PathVariable(value = "refId") String refId){
        return userRepository.getByRefId(refId).getName();
    }

    @PostMapping(value="internalSelfTopup")
    public ResponseEntity<String> internalSelfTopup(@RequestBody TopupRequest topupRequest) {
        User user = userRepository.getById(topupRequest.getUserId());
        PreviousBalance previousBalance = walletRepository.getPreviousBalanceById(user.getId());
        double amount = getCoinValueForUSDLocal(topupRequest.getAmount());
        if (previousBalance.getAvailableBalance() < amount){
            return ResponseEntity.badRequest().body("Insufficient Balance");
        }
        else{
            createTopupRequest(topupRequest);
            previousBalance.setAvailableBalance(previousBalance.getAvailableBalance() - amount);
            userRepository.setPreviousBalanceByUserId(previousBalance);
            acceptTopupRequest(topupRequest);
            return ResponseEntity.ok("Topup Done");
        }
    }

    @PostMapping(value = "/sendMailViaFreeminer")
    public ResponseEntity<String> sendMailViaFreeminer(@RequestBody Map<String, String> mailDetails){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailDetails.get("toMailId"));
        mail.setSubject(mailDetails.get("subject"));
        // mail.setText("<h1>This is actual message embedded in TML tags</h1>");
        mail.setText(mailDetails.get("mailContent"));
        javaMailSender.send(mail);
        return ResponseEntity.ok("Mail Sent");
    }

    @PostMapping(value = "/createFmdc")
    public Fmdc createFmdc(@RequestBody Fmdc fmdc){
        User user = userRepository.getByRefId(fmdc.getRefId());
        fmdc.setUserName(user.getName());
        return userRepository.createFmdc(fmdc);
    }
    
    @GetMapping(value = "/getAllFmdcs")
    public List<Fmdc> getAllFmdcRecords(){
        return userRepository.getAllFmdc();
    }


}

