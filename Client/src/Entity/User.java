package Entity;

public class User {
	enum rule{
		InformationTecnologiesDeparmentManger,
		Inspector,
		Estimator,
		ExeutionLeader,
		Examiner,
		ChangeControlCommitteeChairman
	}
	
	
public String userName;
public String password;
public String firstName;
public String lastName;
public String email ;
public rule[] Permissions;

public boolean logedIn;

}//End of User
