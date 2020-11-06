package com.librarymanagement;

import com.google.appengine.api.datastore.Blob;

public interface User {
	 public void setId(long id);
	 public void setName(String name);
	 public void setPassword(String password);
	 public void setProfilePic(Blob image);
//	 public void setBook(Long bookId);
	 public long getId();
	 public String getName();
	 public String getPassword();
	 public Blob getProfilePic();
//	 public ArrayList<Long> getAllBooksId();
}
