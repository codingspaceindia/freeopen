package com.codingspace.freecoin.service;

import java.util.*;

import com.codingspace.freecoin.model.Node;
import com.codingspace.freecoin.model.User;
import com.codingspace.freecoin.repository.NodeRepository;
import com.codingspace.freecoin.repository.UserRepository;
import com.codingspace.freecoin.repository.WalletRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    UserRepository userRepository;

    int min = 0;
    int max = 100000;
    String prefix = "FC";

    HashMap<String, User> userMap = new HashMap<>();

    public String getRefIdString(String[] refIds) {
        for (int i = 0; i < refIds.length; i++) {
            String newRefId = generateRefId();
            // if(newRefId == )
        }
        return "Hello";
    }

    private void supportLoop(String newRefId, String[] refIds) {
        for (int i = 0; i < refIds.length; i++) {
            // if(newRefId ==refIds[i])
        }
    }

    public int getLongRandomNumber() {
        int min = 1111111;
        int max = 9999999;
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String generateRefId() {
        int number = (int) (Math.random() * (max - min + 1) + min);
        return prefix + number;
    }

    /**
     * Get extreme bottom left node
     */
    public Node getParentNodeLeft(Node parent, Node newChild) {
        Node pointingNode = parent;
        for (;;) {
            if (pointingNode.getLeft() == null) {
                return pointingNode;
            } else {
                pointingNode = nodeRepository.getByUserId(pointingNode.getLeft().getUserId());
            }
        }
    }

    /**
     * Get extreme bottom right node
     */
    public Node getParentNodeRight(Node parent, Node newChild) {
        Node pointingNode = parent;

        for (;;) {
            if (pointingNode.getRight() == null) {
                return pointingNode;
            } else {
                pointingNode = nodeRepository.getByUserId(pointingNode.getRight().getUserId());
            }
        }
    }

    /**
     * Generate random string for password
     */
    public String generateRandomString(int size) {
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < size) { // length of the random string.
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public List<String> getAllLeftSideChilds(String userId) {

        List<Node> allNodes = nodeRepository.getAllNodes();
        Map<String, List<Node>> grpdNodesByUserId = allNodes.stream().collect(Collectors.groupingBy(Node::getUserId));
        List<String> leftUsersIds = new ArrayList<String>();
        Node pointingNode = grpdNodesByUserId.get(userId).get(0);

        if (pointingNode.getLeft() != null) {
            leftUsersIds.add(pointingNode.getLeft().getUserId());
        }

        for (;;) {
            System.out.println("current user id " + pointingNode.getUserId());
            if (pointingNode.getLeft() != null) {
                String _leftUserId = pointingNode.getLeft().getUserId();
                leftUsersIds.add(_leftUserId);
                pointingNode = grpdNodesByUserId.get(_leftUserId).get(0);
            } else {
                break;
            }
        }
        return leftUsersIds;
    }

    public List<String> getAllRightSideChilds(String userId) {

        List<Node> allNodes = nodeRepository.getAllNodes();
        Map<String, List<Node>> grpdNodesByUserId = allNodes.stream().collect(Collectors.groupingBy(Node::getUserId));
        List<String> rightUsersIds = new ArrayList<String>();
        Node pointingNode = grpdNodesByUserId.get(userId).get(0);

        if (pointingNode.getRight() != null) {
            rightUsersIds.add(pointingNode.getRight().getUserId());
        }

        for (;;) {
            System.out.println("current user id " + pointingNode.getUserId());
            if (pointingNode.getRight() != null) {
                String _rightUserId = pointingNode.getRight().getUserId();
                rightUsersIds.add(_rightUserId);
                pointingNode = grpdNodesByUserId.get(_rightUserId).get(0);
            } else {
                break;
            }
        }
        return rightUsersIds;
    }

    public HashMap<String,User> userMapping(){
        int count = 0;
        if(userMap.isEmpty()) {
            count += 1;
            for (User user : userRepository.findAll()) {
                userMap.put(user.getId(), user);
            }
            System.out.println("displaying count : " + count);
        }
        return userMap;
    }

}
