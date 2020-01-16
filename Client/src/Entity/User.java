package Entity;

import java.io.Serializable;
import java.util.EnumSet;

/**
 *  entity containing all of the users details Also enforces
 *         some of the story's constraints
 *
 */
public class User implements Serializable {

	// enums*********************************************
	public enum icmPermission {
		informationTechnologiesDepartmentManager,
		inspector,
		estimator, executionLeader, examiner,
		changeControlCommitteeChairman,
		changeControlCommitteeMember
	}

	// will be used to determine who can be allocated icmPermission
	public enum collegeStatus {
		student, informationEngineer, lecturer
	}

	// Variables
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private EnumSet<icmPermission> Permissions = null;
	//private icmPermission[] Permissions;
	private collegeStatus collegeStatus;
// Contractors

	/**
	 * creates user entity while enforcing constraints
	 *
	 * @param userName      ?
	 * @param password      ?
	 * @param firstName     ?
	 * @param lastName      ?
	 * @param email         ?
	 * @param collegeStatus -(enum-collegeStatus)
	 * @param Permissions   - (enum-icmPermission)
	 */
	public User(String userName, String password, String firstName,
				String lastName, String email, collegeStatus collegeStatus,
				EnumSet<icmPermission> Permissions) {
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
	 * @throws IllegalArgumentException when can not update the Permissions
	 */

	public void updatePermissions(EnumSet<icmPermission> Permissions) {
		try {
			if (Permissions == null) throw new IllegalArgumentException("Permissions shold not be null");
			if ((collegeStatus == collegeStatus.student || collegeStatus == collegeStatus.lecturer) && (!Permissions.isEmpty()))
				throw new IllegalArgumentException(collegeStatus.name() + " cannot have icmPermissions\n");
			this.Permissions = Permissions;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}


	// TODO test this method

	/**
	 * adding a Permission to user entity
	 *
	 * @param Permission ?
	 */
	public void addPermission(icmPermission Permission) {

		try {
			if (collegeStatus == collegeStatus.student || collegeStatus == collegeStatus.lecturer)
				throw new IllegalArgumentException(collegeStatus.name() + " cannot have icmPermissions\n");
			if (Permission == null)
				throw new IllegalArgumentException("null  is not a Permission\n");
			if (this.Permissions == null) {
				EnumSet<icmPermission> all = EnumSet.allOf(icmPermission.class);
				this.Permissions = EnumSet.complementOf(all);
				this.Permissions.add(Permission);
				return;
			}
			if (this.Permissions.contains(Permission))
				return;
			this.Permissions.add(Permission);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}


	} // END of addPermmision()


	/**
	 * removes the Permission from the user
	 *
	 * @param Permission ?
	 */
	public void removePermission(icmPermission Permission) {
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

	public EnumSet<icmPermission> getICMPermissions() {
		return Permissions;
	}

	public String getICMPermissionString() {
		if (Permissions != null && Permissions.size() > 0) {
			StringBuilder permissions = new StringBuilder();
			Permissions.forEach(permission ->
					permissions.append(permission.name()).append(", "));
			permissions.delete(permissions.length() - 2, permissions.length());
			permissions.trimToSize();
			return String.valueOf(permissions);
		} else return "none";
	}

	public collegeStatus getCollegeStatus() {
		return collegeStatus;
	}

	@Override
	public String toString() {
		return userName;
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
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
