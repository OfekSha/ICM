package Entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class UserTableView {

    public TableView<userForTable> tblViewUsers;
    // table columns:
    public TableColumn<userForTable, String> colUserName;
    public TableColumn<userForTable, String> colFirstName;
    public TableColumn<userForTable, String> colLastName;
    public TableColumn<userForTable, String> colEmail;
    public TableColumn<userForTable, String> colPermissions;
    public TableColumn<userForTable, String> colStatus;

    public UserTableView(TableView<userForTable> table,
                         TableColumn<userForTable, String> firstName,
                         TableColumn<userForTable, String> lastName,
                         TableColumn<userForTable, String> eMail,
                         TableColumn<userForTable, String> permissions)
    {
        colEmail = eMail;
        tblViewUsers = table;
        colLastName = lastName;
        colFirstName = firstName;
        colPermissions = permissions;
        initializeTableView();
    }

    public UserTableView(TableView<userForTable> table,
                         TableColumn<userForTable, String> userName,
                         TableColumn<userForTable, String> firstName,
                         TableColumn<userForTable, String> lastName,
                         TableColumn<userForTable, String> eMail,
                         TableColumn<userForTable, String> permissions,
                         TableColumn<userForTable, String> status)
    {
        colEmail = eMail;
        colStatus = status;
        tblViewUsers = table;
        colUserName = userName;
        colLastName = lastName;
        colFirstName = firstName;
        colPermissions = permissions;
        initializeTableView();
    }

    /**
     * This method set data into the table.
     * @param users - arrayList of users (Entity.User)
     */
    public void setData(ArrayList<User> users) {
        tblViewUsers.setItems(
                FXCollections.observableArrayList(usersForTableList(users)));
    }

    /**
     * This method build new array list from requests that adapt to table view.
     * @param usersList - arrayList of all users (changeRequest)
     * @return newList - arrayList of requirementForTable
     */
    private static ArrayList<userForTable> usersForTableList(ArrayList<User> usersList) {
        ArrayList<UserTableView.userForTable> newList = new ArrayList<>();
        usersList.forEach(u -> {
            newList.add(new userForTable(u));
            System.out.println(u.getICMPermissionString());
        });
        return newList;
    }

    private void initializeTableView() {
        if (colEmail != null) {
            colEmail.setCellValueFactory(new PropertyValueFactory<>("eMail"));
        }
        if (colStatus != null) {
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
        if (colUserName != null) {
            colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        }
        if (colLastName != null) {
            colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        }
        if (colFirstName != null) {
            colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        }
        if (colPermissions != null) {
            colPermissions.setCellValueFactory(new PropertyValueFactory<>("permissions"));
        }
    }

    /**
     *
     * This function return the selected user in the table.
     */
    public UserTableView.userForTable onUserClicked() {
        return tblViewUsers.getSelectionModel().getSelectedItem();
    }

    /**
     *
     * constructor for requirementForTable.
     * use SimpleStringProperty and SimpleObjectProperty
     * very important.
     */
    public static class userForTable {
        private User originalUser;
        private SimpleStringProperty eMail;
        private SimpleStringProperty userName;
        private SimpleStringProperty lastName;
        private SimpleStringProperty firstName;
        private SimpleStringProperty permission;
        private SimpleStringProperty collegeStatus;

        public userForTable(User originalUser,
                            SimpleStringProperty userName,
                            SimpleStringProperty firstName,
                            SimpleStringProperty lastName,
                            SimpleStringProperty eMail,
                            SimpleStringProperty permission,
                            SimpleStringProperty collegeStatus) {
            this.eMail = eMail;
            this.userName = userName;
            this.lastName = lastName;
            this.firstName = firstName;
            this.permission = permission;
            this.originalUser = originalUser;
            this.collegeStatus = collegeStatus;
        }

        public userForTable(User user) {
            originalUser = user;
            eMail = new SimpleStringProperty(user.getEmail());
            userName = new SimpleStringProperty(user.getUserName());
            lastName = new SimpleStringProperty(user.getLastName());
            firstName = new SimpleStringProperty(user.getFirstName());
            permission = new SimpleStringProperty(user.getICMPermissionString());
            collegeStatus = new SimpleStringProperty(user.getCollegeStatus().name());
            System.out.println(permission);
        }

        public SimpleStringProperty eMailProperty() {
            return eMail;
        }
        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }
        public SimpleStringProperty userNameProperty() {
            return userName;
        }
        public SimpleStringProperty firstNameProperty() {
            return firstName;
        }
        public SimpleStringProperty permissionProperty() {
            return permission;
        }
        public SimpleStringProperty collegeStatusProperty() {
            return collegeStatus;
        }

        public String geteMail() {
            return eMail.get();
        }
        public String getUserName() {
            return userName.get();
        }
        public String getLastName() {
            return lastName.get();
        }
        public String getFirstName() {
            return firstName.get();
        }
        public User getOriginalUser() {
            return originalUser;
        }
        public String getPermission() {
            return permission.get();
        }
        public String getCollegeStatus() {
            return collegeStatus.get();
        }
    }
}