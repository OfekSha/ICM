package Entity;


/**
 * @author Yonathan -TODO: test it 
 *
 */
public class User {
	
	// enums*********************************************
	enum ICMPermissions{
		informationTecnologiesDeparmentManger,
		inspector,
		estimator,
		exeutionLeader,
		examiner,
		changeControlCommitteeChairman
	}
	
	// wil be used to determan who can be allocted ICMPermissions
	enum Job{
		student,
		informationEngineer
		
	}
	
	
	//Variables
private String userName;
private String password;
private String firstName;
private String lastName;
private String email ;
private ICMPermissions[] Permissions;
private Job job ;
public boolean logedIn;


// Contractors



/** 
 * creates user entity while enforcing constraints
 * 
 * @param userName
 * @param password
 * @param firstName
 * @param lastName
 * @param email
 * @param job			-(enum-Job)
 * @param Permissions  -  (enum-ICMPermissions)if the user is is a student send null
 * @param logedIn		- represents if the user is logged in
 */
public User(String userName ,String password,String firstName,String lastName ,String email,Job job,ICMPermissions[] Permissions,boolean logedIn) {
	
	this.job=job;
	if (!updatePremiisions(Permissions))  throw new IllegalArgumentException("Students dont have Permissions in the system");
	this.userName =userName;
	this.password =password;
	this.firstName =firstName;
	this.lastName =lastName;
	this.email =email;
	this.logedIn =logedIn;
} 


// input 



/**
 * updatedes the user entity Permissions while   enforcing constraints
 * @param Permissions - array of the Permissions the user has
 * @return				- returns  true if the update was done
 */
public  boolean updatePremiisions(ICMPermissions[] Permissions) {
	if(Permissions!=null && job==Job.student) return false;
	 this.Permissions=Permissions;
	 return true;
	
}

public void updateLoging(boolean logedIn) {
	this.logedIn=logedIn;
}


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

public ICMPermissions[] getICMPermissions() {
	return Permissions;
}

public Job getJob() {
	return job;
	}
public boolean getlogedIn() {
	return logedIn;
}




}//End of User
