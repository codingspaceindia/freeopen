package com.codingspace.freecoin.repository;

import java.util.Calendar;
import java.util.List;

import com.codingspace.freecoin.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

public class WalletCustomRepositoryImpl implements WalletCustomRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Wallet getByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, Wallet.class);
    }
    
    @Override
    public List<Wallet> getAll() {

        return mongoTemplate.findAll(Wallet.class);
    }

    @Override
    public Wallet addTransaction(String userId, TransactionType type, double amount, String message) {

        Wallet userWallet = this.getByUserId(userId);
        double currentAmount = userWallet.getAmount();

        if (type == TransactionType.C) {
            currentAmount = currentAmount + amount;
        } else {
            currentAmount = currentAmount - amount;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.push("transactions", new Transactions(amount, type, message));
        update.set("amount", currentAmount);
        return mongoTemplate.findAndModify(query, update, Wallet.class);
    }
    
    @Override
    public Wallet addRoiTransaction(String userId, TransactionType type, double amount,double fcAmount, String message) {

        Wallet userWallet = this.getByUserId(userId);
        double currentAmount = userWallet.getAmount();

        if (type == TransactionType.C) {
            currentAmount = currentAmount + amount;
        } else {
            currentAmount = currentAmount - amount;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.push("transactions", new Transactions(amount, type, message, fcAmount));
        update.set("amount", currentAmount);
        return mongoTemplate.findAndModify(query, update, Wallet.class);
    }

    @Override
    public Wallet add365DaysDeductionTransaction(String userId , Transactions transaction) {
        Wallet wallet = this.getByUserId(userId);
        double newTotalStacking = wallet.getTotalStacking() - transaction.getFcAmount();
        double newRoiAmount = 0;
        if(wallet.getRoiAmount()>transaction.getFcAmount()*0.0037){
            newRoiAmount = wallet.getRoiAmount() - transaction.getFcAmount()*0.0037;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.push("transactions", transaction);
        update.set("totalStacking", newTotalStacking);
        update.set("roiAmount", newRoiAmount);
        return mongoTemplate.findAndModify(query, update, Wallet.class);
    }
    
    @Override
    public TopupRequest acceptTransaction(TopupRequest topupRequest) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(topupRequest.getUserId()));
        Update update = new Update();
        update.set("status", "A");
        return mongoTemplate.findAndModify(query, update, TopupRequest.class);
    }
    
    
    @Override
    public WithdrawRequest acceptWithdrawal(WithdrawRequest withdrawRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(withdrawRequest.getUserId()));
        withdrawRequest.setStatus("A");
        withdrawRequest.setRespondedAt(Calendar.getInstance().getTime());
        return mongoTemplate.save(withdrawRequest);
    }
    
    @Override
    public Wallet addWithDrawTransaction(String userId,TransactionType type,double amount,String message)
    {
    	Wallet userWallet = this.getByUserId(userId);
    	double offerWallet=0;
        double earningsWallet=0;
        double stackingWallet=0;
        double availableBalance;
        double firstGroupWallet;
        double secondGroupWallet;

        	if (userWallet != null) {
        		offerWallet = userWallet.getOfferWallet();
        		earningsWallet = userWallet.getEarningsWallet();
        		stackingWallet = userWallet.getStackingWallet();
        		availableBalance=stackingWallet+earningsWallet+offerWallet;
        		firstGroupWallet = earningsWallet;
        		secondGroupWallet= earningsWallet+offerWallet;
        		double shredValue ;
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
        			}
        			else if(secondGroupWallet>amount)
        			{
        				shredValue = amount-earningsWallet;
        				earningsWallet=0;
        				offerWallet=offerWallet-shredValue;
        			}
        			else
        			{
        				shredValue = amount-earningsWallet;
        				shredValue = shredValue-offerWallet;
        				stackingWallet = stackingWallet - shredValue;
        				earningsWallet =0;
        				offerWallet =0;
        			}
        		}
        	}
        
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.push("transactions", new Transactions(amount, type, message));
        if (message.equals("USER_WITHDRAW")){
            System.out.println("if block");
        }
        else {
            update.set("offerWallet", offerWallet);
            update.set("earningsWallet", earningsWallet);
            update.set("stackingWallet", stackingWallet);
        }
        return mongoTemplate.findAndModify(query, update, Wallet.class);
    }
    
    @Override
    public Double getWalletBalance(String userId)
    {
    	Wallet userWallet = this.getByUserId(userId);
    	double offerWallet;
        double earningsWallet;
        double stackingWallet;
        double availableBalance = 0;
        
        if (userWallet != null) {
        	offerWallet = userWallet.getOfferWallet();
        	earningsWallet = userWallet.getEarningsWallet();
        	stackingWallet = userWallet.getStackingWallet();
        	availableBalance = offerWallet+earningsWallet+stackingWallet;
        }
        return availableBalance;
    }
    
    @Override
    public TopupRequest acceptTopupRequest(TopupRequest topupRequest)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(topupRequest.getUserId()));
        topupRequest.setStatus("A");
        topupRequest.setRespondedAt(Calendar.getInstance().getTime());
        return mongoTemplate.save(topupRequest);       
    }
    
    @Override
    public TopupRequest rejectTransaction(TopupRequest topupRequest) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(topupRequest.getUserId()));
        topupRequest.setStatus("R");
        return mongoTemplate.save(topupRequest);
    }

    @Override
    public WithdrawRequest rejectWithdrawal(WithdrawRequest withdrawRequest) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(withdrawRequest.getUserId()));
        withdrawRequest.setStatus("R");
        return mongoTemplate.save(withdrawRequest);
    }
    
    @Override
    public List<Wallet> getByUserIds(List<String> userIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userIds));
        return mongoTemplate.find(query, Wallet.class);
    }
    
    @Override
    public Wallet getWalletByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userId));
        return mongoTemplate.findOne(query, Wallet.class);
    }

    @Override
    public List<Wallet> getAllWalletDocs() {
        return mongoTemplate.findAll(Wallet.class);
    }
    
    @Override
    public List<Auth> getAllAuthDocs() {
        return mongoTemplate.findAll(Auth.class);
    }
    
    @Override
    public Auth createAuth(Auth auth) {
        return mongoTemplate.save(auth);
    }
    
    @Override
    public WithdrawRequest createWithdrawRequest(WithdrawRequest withdrawRequest) {
        return mongoTemplate.save(withdrawRequest);
    }

    @Override
    public TransferWalletTransactions createTransferWalletTransactions(TransferWalletTransactions transferWalletTransactions){
        return mongoTemplate.save(transferWalletTransactions);
    }

    @Override
    public List<TransferWalletTransactions> getAllTransferWalletTransactions(){
        return mongoTemplate.findAll(TransferWalletTransactions.class);
    }



    @Override
    public TopupTransactionsRecord createTopupTransactionsRecord(TopupTransactionsRecord topupTransactionsRecord){
        return mongoTemplate.save(topupTransactionsRecord);
    }

    @Override
    public TopupTransactionsRecord createTopupTransactionsRecordByTopUpRequest(TopupRequest topupRequest){
        TopupTransactionsRecord topupTransactionsRecord = this.getTopupTransactionsRecordByUserId(topupRequest.getUserId());
        topupTransactionsRecord.setUserId(topupRequest.getUserId());
        return mongoTemplate.save(topupTransactionsRecord);
    }

    @Override
    public TopupTransactionsRecord getTopupTransactionsRecordByUserId(String userId){
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userId));
        return mongoTemplate.findOne(query,TopupTransactionsRecord.class);
    }

    @Override
    public List<TopupTransactionsRecord> getAllTopupTransactionsRecord(){
        return mongoTemplate.findAll(TopupTransactionsRecord.class);
    }

    @Override
    public UserStatus createUserStatus(UserStatus userStatus) {
        return mongoTemplate.save(userStatus);
    }
    
    @Override
    public PanRecord createPanRecord(PanRecord panRecord) {
        return mongoTemplate.save(panRecord);
    }
    
    @Override
    public PanRecord getPanRecord(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userId));
        return mongoTemplate.findOne(query, PanRecord.class);
    }
    
    @Override
    public List<PanRecord> getPanRecords() {

        return mongoTemplate.findAll(PanRecord.class);
    }
    
    @Override
    public SupportRecord createSupportRecord(SupportRecord supportRecord) {
        return mongoTemplate.save(supportRecord);
    }
    
    @Override
    public List<SupportRecord> getAllSupportRecords() {
    	Query query = new Query();
        query.addCriteria(Criteria.where("isProcessed").is("false"));
        return mongoTemplate.find(query, SupportRecord.class);
    }
    
    @Override
    public List<SupportRecord> getAllAcceptedSupportRecords() {
    	Query query = new Query();
        query.addCriteria(Criteria.where("isProcessed").is("true"));
        return mongoTemplate.find(query, SupportRecord.class);
    }
    
    @Override
    public SupportRecord acceptSupportQuery(SupportRecord supportRecord) {

        Query query = new Query();
        query.addCriteria(Criteria.where("SupportId").is(supportRecord.getSupportId()));
        supportRecord.setIsProcessed("true");
        return mongoTemplate.save(supportRecord);
    }
    
    @Override
    public List<UserStatus> getAllUserStatus() {
        return mongoTemplate.findAll(UserStatus.class);
    }
    
    @Override
    public void deleteUserStatus() {
        mongoTemplate.dropCollection(UserStatus.class);
    }
    
    @Override
    public Wallet SaveWallet(Wallet wallet)
    {
    	return mongoTemplate.save(wallet);
    }
    
    @Override
    public OTPRecords SaveOtpRecord(OTPRecords otpRecords)
    {
    	return mongoTemplate.save(otpRecords);
    }
    
    @Override
    public OTPRecords getOtpByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userId));
        return mongoTemplate.findOne(query, OTPRecords.class);
    }
    
    @Override
    public BusinessRecord SaveBusinessRecord(BusinessRecord businessRecord)
    {
    	return mongoTemplate.save(businessRecord);
    }

    @Override
    public List<BusinessRecord> getAllBusinessRecords() {
        return mongoTemplate.findAll(BusinessRecord.class);
    }
    
    @Override
    public void deletePairMatchRecords() {
        mongoTemplate.dropCollection(PairMatchingRecord.class);
    }
    
    @Override
    public TopupRequest createTopupRequest(TopupRequest topupRequest)
    {
    	return mongoTemplate.save(topupRequest);
    }

    @Override
    public List<TransferWalletTransactions> getTransferWalletTransactionsByFromUserId(String fromUserId){
        Query query = new Query();
        query.addCriteria(Criteria.where("fromUserId").in(fromUserId));
        return mongoTemplate.find(query, TransferWalletTransactions.class);
    }

    @Override
	public List<TransferWalletTransactions> getallCreditedTransferWalletTransactionsByUserId(String userId){
        Query query = new Query();
        query.addCriteria(Criteria.where("toUserId").in(userId));
        return mongoTemplate.find(query, TransferWalletTransactions.class);
    }

    @Override
    public BonusWallet createBonusWallet(BonusWallet bonusWallet)
    {
    	return mongoTemplate.save(bonusWallet);
    }  
    
    @Override
    public BonusWallet getBonusWalletByUserId(String userId) {
    	Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userId));
        return mongoTemplate.findOne(query, BonusWallet.class);
    }
    
    @Override
    public FreecoinAddress getFreeCoinAddressByUserId(String userId) {
    	Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userId));
        return mongoTemplate.findOne(query, FreecoinAddress.class);
    }
    
    @Override
    public BonusWallet updateBonusWalletAmount(BonusWallet bonusWallet,double amount) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(bonusWallet.getUserId()));
        bonusWallet.setAmount(amount);
        return mongoTemplate.save(bonusWallet);
    }
    
    @Override
    public PairMatchingRecord createPairMatchingRecord(PairMatchingRecord pairMatchingRecord)
    {
    	return mongoTemplate.save(pairMatchingRecord);
    }
    
    @Override
    public Transactions createTransaction(Transactions transactions)
    {
    	return mongoTemplate.save(transactions);
    }
    
    @Override
    public FreecoinAddress createFreeCoinAddress(FreecoinAddress freecoinAddress)
    {
    	return mongoTemplate.save(freecoinAddress);
    }

    
    @Override
    public PreviousBalance createPreviousBalance(PreviousBalance previousBalance)
    {
    	return mongoTemplate.save(previousBalance);
    }
    
    @Override
    public List<PairMatchingRecord> getPairMatchingRecordByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userId));
        return mongoTemplate.find(query, PairMatchingRecord.class);
    }

    @Override
    public 	List<PairMatchingRecord> getAllPairMatchingRecord(){
        return mongoTemplate.findAll(PairMatchingRecord.class);
    }


    @Override
    public List<WithdrawRequest> getAllWithdrawRequest() {
        return mongoTemplate.findAll(WithdrawRequest.class);
    }

    @Override
    public List<TopupRequest> getAllTopupRequest() {
        return mongoTemplate.findAll(TopupRequest.class);
    }

    @Override
    public void deleteSomeTopupRequest() {
    	Query query = new Query();
        query.addCriteria(Criteria.where("amount").gt(15));
        mongoTemplate.remove(query, TopupRequest.class);
    }
   
    @Override
    public void deleteSomeWithdrawRequest() {
    	Query query = new Query();
        query.addCriteria(Criteria.where("userId").is("61ab93d768f7df34cf87e61a"));
        mongoTemplate.remove(query, WithdrawRequest.class);
    }
        
    @Override
    public List<TopupRequest> getAllAcceptedTopupRequest() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("A"));
        return mongoTemplate.find(query, TopupRequest.class);
    }    

    @Override
    public List<PreviousBalance> getAllPreviousBalance() {
        return mongoTemplate.findAll(PreviousBalance.class);
    }
    
    @Override
    public List<BonusWallet> getAllBonusWallets() {
        return mongoTemplate.findAll(BonusWallet.class);
    }
    
    @Override
    public PreviousBalance getPreviousBalanceById(String userid) {
    	Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userid));
        return mongoTemplate.findOne(query, PreviousBalance.class);
    }
    
    @Override
    public List<Transactions> getAllTransactions() {
        return mongoTemplate.findAll(Transactions.class);
    }
    

	@Override
	public TopupRequest getTopupRequestByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
		return mongoTemplate.findOne(query, TopupRequest.class);
	}
}
