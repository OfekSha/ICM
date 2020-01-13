package Entity;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.EnumSet;

public class UserTableView {

    public TableView<UserTableView.userForTable> tblViewUsers;
    // table columns:
    public TableColumn<UserTableView.userForTable, String> colUsername;
    public TableColumn<UserTableView.userForTable, String> colFirstname;
    public TableColumn<UserTableView.userForTable, String> colLastname;
    public TableColumn<UserTableView.userForTable, String> colEmail;
    public TableColumn<UserTableView.userForTable, Object> colPermissions;
    public TableColumn<UserTableView.userForTable, Object> colStatus;

    public UserTableView(TableView<UserTableView.userForTable> table,
                            TableColumn<UserTableView.userForTable, String> username,
                            TableColumn<UserTableView.userForTable, String> firstname,
                            TableColumn<UserTableView.userForTable, String> lastname,
                            TableColumn<UserTableView.userForTable, String> email,
                            TableColumn<UserTableView.userForTable, Object> permissions,
                            TableColumn<UserTableView.userForTable, Object> status) {
        tblViewUsers = table;
        colUsername = username;
        colFirstname = firstname;
        colLastname = lastname;
        colEmail = email;
        colPermissions = permissions;
        colStatus = status;
        initializeTableView();
    }

    /**
     * This method set data into the table.
     * @param users - arrayList of users (Entity.User)
     */
    public void setData(ArrayList<User> users) {
        ObservableList<UserTableView.userForTable> tableData;
        tableData = FXCollections.observableArrayList(usersForTableList(users));
        tblViewUsers.setItems(tableData);
    }

    /**
     * This method build new array list from requests that adapt to table view.
     * @param usersList - arrayList of all users (changeRequest)
     * @return newList - arrayList of requirementForTable
     */
    private static ArrayList<UserTableView.userForTable> usersForTableList(ArrayList<User> usersList) {
        ArrayList<UserTableView.userForTable> newList = new ArrayList<>();
        usersList.forEach(u -> newList.add(new userForTable(u)));
        return newList;
    }

    public UserTableView() {
        initializeTableView();
    }

    private void initializeTableView() {
        if (colUsername != null) {
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        }
        if (colFirstname != null) {
            colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        }
        if (colLastname != null) {
            colLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        }
        if (colEmail != null) {
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        }
        if (colPermissions != null) {
            colPermissions.setCellValueFactory(new PropertyValueFactory<>("permissions"));
        }
        if (colStatus != null) {
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }

    /**
     *
     * This function return the selected request in the table.
     * @return requirementForTable - this is special class for table for get the object requirementForTable.getObject();
     */
    public UserTableView.userForTable onRequirementClicked() {
        return tblViewUsers.getSelectionModel().getSelectedItem();
    }

    /**
     *
     * constructor for requirementForTable.
     * use SimpleStringProperty and SimpleObjectProperty
     * very important.
     */
    public static class userForTable {
        private User originalUser; // this is the original request before make it adaptable to table view
        private SimpleStringProperty username;
        private SimpleStringProperty firstname;
        private SimpleStringProperty lastname;
        private SimpleStringProperty email;
        private SimpleObjectProperty<EnumSet<User.permissionsICM>> permission;
        private SimpleObjectProperty<User.collegeStatus> collegeStatus;

        public userForTable(User originalUser, SimpleStringProperty username, SimpleStringProperty firstname, SimpleStringProperty lastname, SimpleStringProperty email, SimpleObjectProperty<EnumSet<User.permissionsICM>> permission, SimpleObjectProperty<User.collegeStatus> collegeStatus) {
            this.originalUser = originalUser;
            this.username = username;
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.permission = permission;
            this.collegeStatus = collegeStatus;
        }

        public userForTable(User originalUser) {
            new userForTable(originalUser,
                    new SimpleStringProperty(originalUser.getUserName()),
                    new SimpleStringProperty(originalUser.getFirstName()),
                    new SimpleStringProperty(originalUser.getLastName()),
                    new SimpleStringProperty(originalUser.getEmail()),
                    new SimpleObjectProperty<>(originalUser.getICMPermissions()),
                    new SimpleObjectProperty<>(originalUser.getCollegeStatus()));
        }

        public User getOriginalUser() {
            return originalUser;
        }

        public void setOriginalUser(User originalUser) {
            this.originalUser = originalUser;
        }

        public String getUsername() {
            return username.get();
        }

        public SimpleStringProperty usernameProperty() {
            return username;
        }

        public void setUsername(String username) {
            this.username.set(username);
        }

        public String getFirstname() {
            return firstname.get();
        }

        public SimpleStringProperty firstnameProperty() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname.set(firstname);
        }

        public String getLastname() {
            return lastname.get();
        }

        public SimpleStringProperty lastnameProperty() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname.set(lastname);
        }

        public String getEmail() {
            return email.get();
        }

        public SimpleStringProperty emailProperty() {
            return email;
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

        public EnumSet<User.permissionsICM> getPermission() {
            return permission.get();
        }

        public SimpleObjectProperty<EnumSet<User.permissionsICM>> permissionProperty() {
            return permission;
        }

        public void setPermission(EnumSet<User.permissionsICM> permission) {
            this.permission.set(permission);
        }

        public User.collegeStatus getCollegeStatus() {
            return collegeStatus.get();
        }

        public SimpleObjectProperty<User.collegeStatus> collegeStatusProperty() {
            return collegeStatus;
        }

        public void setCollegeStatus(User.collegeStatus collegeStatus) {
            this.collegeStatus.set(collegeStatus);
        }
    }
}


