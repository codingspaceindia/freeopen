package com.codingspace.freecoin.repository;

import java.util.List;

import com.codingspace.freecoin.model.*;

public interface WalletCustomRepository {
    public Wallet addTransaction(String userId, TransactionType type, double amount, String message);
    
    public Wallet addWithDrawTransaction(String userId,TransactionType type,double amount,String message);

//	public Wallet

    public Wallet getByUserId(String userId);
    
    public WithdrawRequest createWithdrawRequest(WithdrawRequest withdrawRequest);

	public TransferWalletTransactions createTransferWalletTransactions(TransferWalletTransactions transferWalletTransactions);

	public List<TransferWalletTransactions> getAllTransferWalletTransactions();

	public List<TransferWalletTransactions> getTransferWalletTransactionsByFromUserId(String fromUserId);

	public List<TransferWalletTransactions> getallCreditedTransferWalletTransactionsByUserId(String userId);

	public TopupTransactionsRecord createTopupTransactionsRecord(TopupTransactionsRecord topupTransactionsRecord);

	public TopupTransactionsRecord createTopupTransactionsRecordByTopUpRequest(TopupRequest topupRequest);

	public TopupTransactionsRecord getTopupTransactionsRecordByUserId(String userId);

	public List<TopupTransactionsRecord> getAllTopupTransactionsRecord();

    public TopupRequest createTopupRequest(TopupRequest topupRequest);
    
    public PreviousBalance getPreviousBalanceById(String userid);
    
    public List<WithdrawRequest> getAllWithdrawRequest();
    
    public List<TopupRequest> getAllTopupRequest();
    
    public List<TopupRequest> getAllAcceptedTopupRequest();

    public List<Wallet> getByUserIds(List<String> userIds);

    public List<Wallet> getAllWalletDocs();

    public List<PreviousBalance> getAllPreviousBalance();
    
    public List<Transactions> getAllTransactions();
    
    public TopupRequest getTopupRequestByUserId(String userId);
    
    public TopupRequest acceptTopupRequest(TopupRequest topupRequest);

	TopupRequest acceptTransaction(TopupRequest topupRequest);

	WithdrawRequest acceptWithdrawal(WithdrawRequest withdrawRequest);

	TopupRequest rejectTransaction(TopupRequest topupRequest);

	WithdrawRequest rejectWithdrawal(WithdrawRequest withdrawRequest);


	List<Wallet> getAll();

	PairMatchingRecord createPairMatchingRecord(PairMatchingRecord pairMatchingRecord);

	List<PairMatchingRecord> getPairMatchingRecordByUserId(String userId);

	List<PairMatchingRecord> getAllPairMatchingRecord();

	Wallet getWalletByUserId(String userId);

	BonusWallet createBonusWallet(BonusWallet bonusWallet);

	BonusWallet getBonusWalletByUserId(String userId);

	BonusWallet updateBonusWalletAmount(BonusWallet bonusWallet, double amount);

	void deleteSomeTopupRequest();

	PreviousBalance createPreviousBalance(PreviousBalance previousBalance);

	Transactions createTransaction(Transactions transactions);

	List<BonusWallet> getAllBonusWallets();

	FreecoinAddress createFreeCoinAddress(FreecoinAddress freecoinAddress);

	FreecoinAddress getFreeCoinAddressByUserId(String userId);

	UserStatus createUserStatus(UserStatus userStatus);

	void deleteUserStatus();

	List<UserStatus> getAllUserStatus();

	void deletePairMatchRecords();

	Wallet SaveWallet(Wallet wallet);

	Wallet addRoiTransaction(String userId, TransactionType type, double amount, double fcAmount, String message);

	Wallet add365DaysDeductionTransaction(String userId, Transactions transaction);

	void deleteSomeWithdrawRequest();

	BusinessRecord SaveBusinessRecord(BusinessRecord businessRecord);

	List<BusinessRecord> getAllBusinessRecords();

	Double getWalletBalance(String userId);

	OTPRecords SaveOtpRecord(OTPRecords otpRecords);

	OTPRecords getOtpByUserId(String userId);

	PanRecord createPanRecord(PanRecord panRecord);

	PanRecord getPanRecord(String userId);

	SupportRecord createSupportRecord(SupportRecord supportRecord);

	List<SupportRecord> getAllSupportRecords();

	SupportRecord acceptSupportQuery(SupportRecord supportRecord);

	List<SupportRecord> getAllAcceptedSupportRecords();

	List<Auth> getAllAuthDocs();

	Auth createAuth(Auth auth);

	List<PanRecord> getPanRecords();







    

}
