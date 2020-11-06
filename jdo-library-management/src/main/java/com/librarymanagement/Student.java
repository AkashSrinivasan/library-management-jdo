package com.librarymanagement;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;


@PersistenceCapable
public class Student implements User {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	
	@Persistent
	long id;
	
	@Persistent
	String name;
	
	@Persistent
	String password;
//	ArrayList<Long> allBookId = new ArrayList<Long>();
	
	@Persistent
	Blob profilePic;
	
	Student(){}
	
	Student(long id, String name, String password, Blob profilePic){
		this.id = id;
		this.name = name;
		this.password = password;
		this.profilePic = profilePic;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public void setPassword(String id) {
		// TODO Auto-generated method stub
		this.password = name;
	}

//	@Override
//	public void setBook(Long bookId) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public void setProfilePic(Blob image) {
		// TODO Auto-generated method stub
		this.profilePic = image;
	}

	@Override
	public Blob getProfilePic() {
		// TODO Auto-generated method stub
		return this.profilePic;
	}

//	@Override
//	public ArrayList<Long> getAllBooksId() {
//		// TODO Auto-generated method stub
//		return this.allBookId;
//	}
}
