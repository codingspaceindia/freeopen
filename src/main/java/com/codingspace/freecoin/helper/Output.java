package com.codingspace.freecoin.helper;

import java.util.ArrayList;
import java.util.List;

public class Output {
	
	Root user;
	List<Output> childrens;
	
	public Root getUser() {
		return user;
	}
	public void setUser(Root user) {
		this.user = user;
	}
	public List<Output> getChildrens() {
		if(childrens == null || childrens.isEmpty())
		{
			return new ArrayList<Output>();
		}
		return childrens;
	}
	public void setChildrens(List<Output> childrens) {
		this.childrens = childrens;
	}
	
	

}
