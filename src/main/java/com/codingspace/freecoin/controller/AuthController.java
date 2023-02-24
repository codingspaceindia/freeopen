package com.codingspace.freecoin.controller;

import java.util.*;

import com.codingspace.freecoin.model.*;
import com.codingspace.freecoin.model.reqres.AuthValidateReq;
import com.codingspace.freecoin.model.reqres.ChangePasswordReq;
import com.codingspace.freecoin.model.reqres.MailUserIdPassword;
import com.codingspace.freecoin.repository.AuthRepository;
import com.codingspace.freecoin.repository.UserRepository;

import com.codingspace.freecoin.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestBody AuthValidateReq reqBody) {
        Boolean isAuth;
        User user = userRepository.getByRefId(reqBody.getRefId());
        Map<String, Object> response = new HashMap<String, Object>();
        if (user == null) {
            isAuth = false;
        } else {
            isAuth = authRepository.validate(user.getId(), reqBody.getPassword());
        }

        response.put("isAuth", isAuth);
        if (isAuth) {
            User token = user;
            response.put("token", token);
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.ok().body(response);
        }
    }

    @PostMapping("/changePassword")
    public Boolean changePassword(@RequestBody ChangePasswordReq changePasswordReq) {

        Auth response = authRepository.changePassword(changePasswordReq.getUserId(), changePasswordReq.getOldPassword(),
                changePasswordReq.getNewPassword());

        return response != null;
    }

    @GetMapping("/allAuthList")
    public List<Auth> getAll() {
        return authRepository.getAllAuthList();
    }

    @PostMapping("/sendMailUserIdPassword")
    public ResponseEntity<Boolean> sendMailUserIdPassword(@RequestBody MailUserIdPassword mailUserIdPassword) {
        List<Auth> authList = walletRepository.getAllAuthDocs();
        Auth currentUser = null;
        for (int i = 0; i < authList.size(); i++) {
            if (authList.get(i).getRefId().equals(mailUserIdPassword.getRefId())) {
                currentUser = authList.get(i);
                System.out.println(authList.get(i).getRefId() + " : " + authList.get(i).getPassword());
                break;
            }
        }
        if (!currentUser.equals(null)) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(currentUser.getEmail());
            msg.setSubject("Authentication Details");
            msg.setText("RefId : " + currentUser.getRefId() + "\n" + "Password : " + currentUser.getPassword());
            javaMailSender.send(msg);
            System.out.println("\n Mail Sent");
            return ResponseEntity.ok().body(true);
        } else return ResponseEntity.badRequest().body(false);
    }

//    @PostMapping("/testingapi")
//    public List<User> testingapi(@RequestBody List<RefId> refIdList) {
////        return userRepository.getByRefId(authValidateReq.getRefId());
////        User user = userRepository.getByRefId(authValidateReq.getRefId());
////        List<User> userList = userRepository.findAll();
////        List<PreviousBalance> previousBalanceList = walletRepository.getAllPreviousBalance();
////        System.out.println(walletRepository.getPreviousBalanceById(userRepository.getByRefId(authValidateReq.getRefId()).getId()));
//////        return walletRepository.getPreviousBalanceById(userRepository.getByRefId(authValidateReq.getRefId()).getId());
////        PreviousBalance previousBalance = walletRepository.getPreviousBalanceById(userRepository.getByRefId(authValidateReq.getRefId()).getId());
////        previousBalance.setUserId(user.getId());
//
//
////        ___________________________________________________
//
//
//        List<User> userList = new ArrayList<>();
//        for (RefId refId : refIdList) {
//            User user = userRepository.getByRefId(refId.getRefId());
//            PreviousBalance previousBalance = userRepository.getUserPreviousBalanceByUserId(user.getParentId());
//            if (previousBalance != null)
//                continue;
//            else {
//                userList.add(userRepository.getByRefId(user.getRefId()));
//                System.out.println(user.getParentId());
//            }
//        }
//        return userList;
//    }

//    @PostMapping("/authTest1")
////    public List<WithdrawRequest> testApi2(){
//    public List<Wallet> testApi2(@RequestBody User user){
//        int count = 0;
////        for (WithdrawRequest withdrawRequest:walletRepository.getAllWithdrawRequest()){
////            if(withdrawRequest.getUserId().equals(user.getId())){
////                System.out.println(withdrawRequest.getUserId());
////                break;
////            }
////        }
//        for (Wallet wallet : walletRepository.getAll())
//        {
//            count+=1;
//        }
//        return walletRepository.getAll();
//
//    }


}
