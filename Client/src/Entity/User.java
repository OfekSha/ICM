package Entity;

import java.io.Serializable;
import java.util.EnumSet;

/**
 * @author Yonathan entity containing all of the users details Also enforces
 *         some of the story's constraints
 *
 */
public class User implements Serializable {

	// enums*********************************************
	public enum permissionsICM {
		informationTechnologiesDepartmentManager, inspector, estimator, executionLeader, examiner,
		changeControlCommitteeChairman,changeControlCommitteeMember
	}

	// wil be used to determine who can be allocated permissionsICM
	public enum collegeStatus {
		student, informationEngineer
	}

	// Variables
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private EnumSet<permissionsICM> Permissions = null;
	//private permissionsICM[] Permissions;
	private collegeStatus collegeStatus;
// Contractors

	/**
	 * creates user entity while enforcing constraints
	 *
	 * @param userName    ?
	 * @param password    ?
	 * @param firstName   ?
	 * @param lastName    ?
	 * @param email       ?
	 * @param collegeStatus         -(enum-collegeStatus)
	 * @param Permissions - (enum-permissionsICM)if the user is is a student send null
	 */
	public User(String userName, String password, String firstName, String lastName, String email, collegeStatus collegeStatus,
				EnumSet<permissionsICM> Permissions) {
		this.collegeStatus = collegeStatus;
		updatePermissions(Permissions);
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

// input 

	/**
	 * updates all of the user entity Permissions while enforcing constraints
	 *
	 * @param Permissions - EnumSet of the Permissions the user has
	 * @return - returns true if the update was done
	 */

	//TODO Return value of the method is never used
	public boolean updatePermissions(EnumSet<permissionsICM> Permissions) {
		if (Permissions == null || collegeStatus == collegeStatus.student)
			return false;
		this.Permissions = Permissions;
		return true;
	}



	// TODO test this method

	/**
	 * adding a Permission to user entity [NOT YET TESTED]
	 *
	 * @param Permission doesnt @return - returns true if permission is in the collection
	 */
	public void addPremmision(permissionsICM Permission) {

		if (collegeStatus == collegeStatus.student && Permission == null)
			return;
		//TODO Warning:(93, 34) Condition 'Permission != null' is always 'true' when reached
		if (collegeStatus == collegeStatus.student && Permission != null) {
			try {
				throw new IllegalArgumentException("Students dont have Permissions in the system");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			if (Permission == null)
				throw new IllegalArgumentException("null  is not a Permission");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.exit(0); // must exit -EnumSet cannot receive null
		}

		if (this.Permissions == null) {
			EnumSet<permissionsICM> all = EnumSet.allOf(permissionsICM.class);
			this.Permissions = EnumSet.complementOf(all);
			this.Permissions.add(Permission);
			return;
		}
		if (this.Permissions.contains(Permission))
			return;
		this.Permissions.add(Permission);
	} // END of addPermmision()

	// TODO test this method

	/**
	 * removes the Permission from the user [NOT YET TESTED]
	 *
	 * @param Permission ?
	 */
	public void removePermission(permissionsICM Permission) {
		if (Permission == null || this.Permissions == null)
			return;
		this.Permissions.remove(Permission);
	}// END of removePermission()

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

	public EnumSet<permissionsICM> getICMPermissions() {
		return Permissions;
	}

	public collegeStatus getCollegeStatus() {
		return collegeStatus;
	}


	@Override
	public String toString() {
		return userName;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}
		User u = (User) o;
		return (this.getUserName()).equals(u.getUserName());

	}
}// End of User
