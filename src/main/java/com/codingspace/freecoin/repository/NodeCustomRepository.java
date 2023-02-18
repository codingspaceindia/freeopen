package com.codingspace.freecoin.repository;

import java.util.List;

import com.codingspace.freecoin.model.Node;

public interface NodeCustomRepository {
    public Node getByUserId(String userId);

    public Node updateNodeLeft(Node parentNode, Node childNode);

    public Node updateNodeRight(Node parentNode, Node childNode);

    public List<Node> getAllNodes();

    public List<Node> getPlacementParentUserId(String userId);
}
