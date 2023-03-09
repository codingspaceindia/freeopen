package com.codingspace.freecoin.service;

import com.codingspace.freecoin.model.Node;
import com.codingspace.freecoin.model.PairMatchingRecord;
import com.codingspace.freecoin.model.TopupTransactionsRecord;
import com.codingspace.freecoin.model.User;
import com.codingspace.freecoin.model.Wallet;
import com.codingspace.freecoin.repository.NodeRepository;
import com.codingspace.freecoin.repository.UserRepository;
import com.codingspace.freecoin.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Service
public class Mapping {
    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    HashMap<String, Node> nodeHashMap = new HashMap<>();
    HashMap<String, User> userHashMap = new HashMap<>();
    HashMap<String, Wallet> walletHashMap = new HashMap<>();
    HashMap<String, TopupTransactionsRecord> topupTransactionsRecordHashMap = new HashMap<>();
    HashMap<String, Double> pastPairMatchAmountHashMap = new HashMap<>();

    public void clearAllMap(){
        nodeHashMap.clear();
        userHashMap.clear();
        walletHashMap.clear();
        topupTransactionsRecordHashMap.clear();
        pastPairMatchAmountHashMap.clear();
    }

    public HashMap<String, User> userMapping() {
        if(userHashMap.isEmpty()){
            List<User> userList = userRepository.findAll();
            for (User user : userList) {
                userHashMap.put(user.getId(), user);
            }
        }
        return userHashMap;
    }

    public HashMap<String, Node> nodeMapping() {
        if(nodeHashMap.isEmpty()){
            List<Node> nodeList = nodeRepository.findAll();
            for (Node node : nodeList) {
                nodeHashMap.put(node.getUserId(), node);
            }
        }
        return nodeHashMap;
    }

    public HashMap<String, TopupTransactionsRecord> topupTransactionsRecordHashMapping() {
        if(topupTransactionsRecordHashMap.isEmpty()){
            List<TopupTransactionsRecord> topupTransactionsRecordsList = walletRepository.getAllTopupTransactionsRecord();
            for (TopupTransactionsRecord topupTransactionsRecord : topupTransactionsRecordsList) {
                topupTransactionsRecordHashMap.put(topupTransactionsRecord.getUserId(), topupTransactionsRecord);
            }
        }
        return topupTransactionsRecordHashMap;
    }

    public HashMap<String,Double> pastPairMatchAmountMap(){
        if(pastPairMatchAmountHashMap.isEmpty()){
            List<PairMatchingRecord> pairMatchingRecordList = walletRepository.getAllPairMatchingRecord();
            for (PairMatchingRecord pairMatchingRecord : pairMatchingRecordList)
                pastPairMatchAmountHashMap.put(pairMatchingRecord.getUserId(),pairMatchingRecord.getPairMatchedAmount());
        }
        return pastPairMatchAmountHashMap;
    }

    public HashMap<String,Wallet> walletHashMap() {
        if(walletHashMap.isEmpty()){
            List<Wallet> wallets= walletRepository.getAll();
            for(Wallet wallet : wallets){
                walletHashMap.put(wallet.getUserId(), wallet);
            }
        }
        return walletHashMap;        
    }


}

