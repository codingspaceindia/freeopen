package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "node")
@Getter
@Setter
public class Node {
    @Id
    String id;
    String userId;
    Node left, right;

    public Node(String userId) {
        this.userId = userId;
        this.left = this.right = null;
    }
}
