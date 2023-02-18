package com.codingspace.freecoin.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
class Id{
    @JsonProperty("$oid") 
    public String oid;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
    
}
 class UserId{
    public String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
 
 class Left extends UserId {
	 
 }
 
 class Right extends UserId {
	 
 }

public class Root{
    public Id _id;
    public String userId;
    public String _class;
    public Left left;
    public Right right;
	public Id get_id() {
		return _id;
	}
	public void set_id(Id _id) {
		this._id = _id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	public Left getLeft() {
		return left;
	}
	
	public Right getRight() {
		return right;
	}
	public void setRight(Right right) {
		this.right = right;
	}
	public void setLeft(Left left) {
		this.left = left;
	}
}