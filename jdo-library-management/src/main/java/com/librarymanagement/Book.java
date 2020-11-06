package com.librarymanagement;

import java.time.LocalDateTime;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;



@PersistenceCapable
public class Book {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	
	@Persistent
	long id;
	
	@Persistent
	 String name;
	
	@Persistent
	 Long barrowerId;
	
	@Persistent
	 String borrowedOn;
	
	@Persistent
	 String dueDate;
	
	Book(){}
	
	public Book(long i,String name,long j,String borrowedOn,String dueDate ){
		this.id = i;
		this.name = name;
		this.barrowerId = j;
		this.borrowedOn = borrowedOn;
		this.dueDate = dueDate;
	}
	
	public LocalDateTime getBorrowedOn() {
		return LocalDateTime.parse(this.borrowedOn);
	}
	
	public LocalDateTime getDueDate() {
		return LocalDateTime.parse(this.dueDate);
	}
	
	@Override
	public String toString() {
		return "Book [bookId=" + id + ", name=" + name + "]";
	}
}
