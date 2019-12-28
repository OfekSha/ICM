package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 * @author Yonathan entity containing all of the users details Also enforces
 *         some of the story's constraints
 *
 */
public class User implements Serializable {

	// enums*********************************************
	public static enum ICMPermissions {
		informationTecnologiesDeparmentManger, inspector, estimator, exeutionLeader, examiner,
		changeControlCommitteeChairman
	}

	// wil be used to determan who can be allocted ICMPermissions
	public enum Job {
		student, informationEngineer

	}

	// Variables
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private EnumSet<ICMPermissions> Permissions = null;
//private ICMPermissions[] Permissions;
	private Job job;
	private boolean logedIn;

// Contractors

	/**
	 * creates user entity while enforcing constraints
	 * 
	 * @param userName
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param job         -(enum-Job)
	 * @param Permissions - (enum-ICMPermissions)if the user is is a student send
	 *                    null
	 * @param logedIn     - represents if the user is logged in
	 */
	public User(String userName, String password, String firstName, String lastName, String email, Job job,
			EnumSet<ICMPermissions> Permissions, boolean logedIn) {
		this.job = job;
		updatePremiisions(Permissions);
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.logedIn = logedIn;
	}

// input 

	/**
	 * updates all of the user entity Permissions while enforcing constraints
	 * 
	 * @param Permissions - EnumSet of the Permissions the user has
	 * @return - returns true if the update was done
	 */
	public boolean updatePremiisions(EnumSet<ICMPermissions> Permissions) {
		if (Permissions == null || job == Job.student)
			return false;
		this.Permissions = Permissions;
		return true;
	}

	public void updateLoging(boolean logedIn) {
		this.logedIn = logedIn;
	}

	// TODO test this method
	/**
	 * adding a Permission to user entity [NOT YET TESTED]
	 * 
	 * @param Permission
	 * @return - returns true if permission is in the collection
	 */
	public void addPremmision(ICMPermissions Permission) {

		if (job == Job.student && Permission == null)
			return;
		else if (job == Job.student && Permission != null) {			
			try {
					throw new IllegalArgumentException("Students dont have Permissions in the system");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}	
			return ;
		}
		try {
			if (Permission == null)
				throw new IllegalArgumentException("null  is not a Permission");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.exit(0); // must exit -EnumSet cannot receive null
		}

		if (this.Permissions == null) {
			EnumSet<ICMPermissions> all = EnumSet.allOf(ICMPermissions.class);
			this.Permissions = EnumSet.complementOf(all);
			this.Permissions.add(Permission);
			return ;
		}
		if (this.Permissions.contains(Permission))
			return ;
		this.Permissions.add(Permission);
		return ;

	} // END of addPremmision()

	// TODO test this method
	/**
	 * removes the Permission from the user [NOT YET TESTED]
	 * 
	 * @param Permission
	 */
	public void removePermission(ICMPermissions Permission) {
		if (Permission == null || this.Permissions == null)
			return;
		if (this.Permissions.contains(Permission))
			this.Permissions.remove(Permission);
	}// END of removePermission()

	/**
	 * changes the log in value to the given value
	 * 
	 * @param bool
	 */
	public void changeLoginStaus(boolean bool) {
		logedIn = bool;
	}// END changeLoginStaus
//output

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public EnumSet<ICMPermissions> getICMPermissions() {
		return Permissions;
	}

	public Job getJob() {
		return job;
	}

	public boolean getlogedIn() {
		return logedIn;
	}

}// End of User
