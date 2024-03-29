package com.cab.booking.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cab.booking.IBookingDetails;
import com.cab.booking.IUser;
import com.cab.booking.IUserManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager implements IUserManager {

	private static UserManager INSTANCE;
	
	private final List<IUser> allUsers;
	
	private final Map<String, List<IBookingDetails>> bookingHistory;
	
	private UserManager()
	{
		this.allUsers = new ArrayList<IUser>();
		this.bookingHistory = new HashMap<String, List<IBookingDetails>>();
	}
	
	/*
	 * Singleton instance
	 */
	public static synchronized UserManager getInstance()
	{
		if(UserManager.INSTANCE == null)
		{
			UserManager.INSTANCE = new UserManager();
		}
		return UserManager.INSTANCE;
	}
	
	@Override
	public void listAllRegisteredUsers() {
		System.out.println("List of all registered users in the sytem:");
		for(IUser user : allUsers)
		{
			System.out.println("\t" + user.getUserID() + "\t - \t" + user.getEmailID());
		}
	}
	
	@Override
	public void showBookingHistory(IUser user) {
		System.out.println("Booking History for " + user.getUserID() + ":");
		for(IBookingDetails detail : this.bookingHistory.get(user.getUserID()))
		{
			System.out.println("\tOn: " + detail.getBookingDate());
			System.out.println("\t\tBooking ID: " + detail.getBookingID());
			System.out.println("\t\tCab Number: " + detail.getCabNo());
		}
	}

	@Override
	public boolean addUser(IUser user) {
		for(IUser extUser : this.allUsers)
		{
			if(user.getUserID().equals(extUser))
			{
				return false;
			}
		}
                
            try {
                FileOutputStream fileOut = new FileOutputStream("Users.txt");
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(user);
                objectOut.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                
		return this.allUsers.add(user);
	}

	@Override
	public boolean deleteUser(IUser user) {
		this.bookingHistory.remove(user.getUserID());
		if(this.allUsers.remove(user))
		{
			System.out.println("Profile deleted successfully!");
			return true;
		}
		else
		{
			System.out.println("Oops! Something went wrong. Please try again.");
			return false;
		}
	}
	
	public void editUserInfo(IUser user)
	{
		int index = this.allUsers.indexOf(user);
		this.allUsers.get(index).updateEmailID(user.getEmailID());
		this.allUsers.get(index).updateFullName(user.getFullName());
		System.out.println("Updated the user information of " + user.getUserID() + ".");
	}

	@Override
	public boolean saveBookingHistory(String userID, IBookingDetails booking) {
		if(this.bookingHistory.get(userID) == null)
		{
			List<IBookingDetails> bookingList = new ArrayList<IBookingDetails>();
			bookingList.add(booking);
			this.bookingHistory.put(userID, bookingList);
			return true;
		}
		else
		{
			return this.bookingHistory.get(userID).add(booking);
		}
	}

	@Override
	public void showUserInfo(String userID) {
		System.out.println("User Details:");
		for(IUser user : this.allUsers)
		{
			if(user.getUserID().equals(userID))
			{
				System.out.println("\tUser ID: " + user.getUserID());
				System.out.println("\tEmail ID: " + user.getEmailID());
				System.out.println("\tFull Name: " + user.getFullName());
			}
		}
	}

	@Override
	public boolean authenticate(String userID, String password) {
		for(IUser user : this.allUsers)
		{
			if(user.getUserID().equals(userID))
			{
				return user.getPassword().equals(password);
			}
		}
		return false;
	}

	@Override
	public IUser getUser(String userID) {
		IUser usrToReturn = null;
		for(IUser user : this.allUsers)
		{
			if(user.getUserID().equals(userID))
			{
				usrToReturn = user;
				break;
			}
		}		
		return usrToReturn;
	}

}
