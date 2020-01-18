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
    private TableColumn<userForTable, String> colUserName;
    private TableColumn<userForTable, String> colFirstName;
    private TableColumn<userForTable, String> colLastName;
    private TableColumn<userForTable, String> colEmail;
    private TableColumn<userForTable, String> colPermissions;
    private TableColumn<userForTable, String> colStatus;

    public UserTableView(TableView<userForTable> tblViewUsers,
                         TableColumn<userForTable, String> colFirstName,
                         TableColumn<userForTable, String> colLastName,
                         TableColumn<userForTable, String> colEmail,
                         TableColumn<userForTable, String> colPermissions) {
        this.tblViewUsers = tblViewUsers;
        this.colFirstName = colFirstName;
        this.colLastName = colLastName;
        this.colEmail = colEmail;
        this.colPermissions = colPermissions;
        initializeTableView();
    }

    /**
     * This method set data into the table.
     * @param users - arrayList of users (Entity.User)
     */
    public void setData(ArrayList<User> users) {
        ArrayList<userForTable> usersForTable = new ArrayList<>();
        users.forEach(u -> {
            if (u.getICMPermissions().size() < 7) {
                usersForTable.add(new userForTable(u));
            }
        });
        tblViewUsers.setItems(FXCollections.observableArrayList(usersForTable));
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
     * This function return the selected user in the table.
     */
    public userForTable onUserClicked() {
        return tblViewUsers.getSelectionModel().getSelectedItem();
    }

    public static class userForTable {
        private final User originalUser;
        private final SimpleStringProperty eMail;
        private final SimpleStringProperty userName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty permissions;
        private final SimpleStringProperty collegeStatus;

        private userForTable(User user) {
            originalUser = user;
            eMail = new SimpleStringProperty(user.getEmail());
            userName = new SimpleStringProperty(user.getUserName());
            lastName = new SimpleStringProperty(user.getLastName());
            firstName = new SimpleStringProperty(user.getFirstName());
            permissions = new SimpleStringProperty(user.getICMPermissionString());
            collegeStatus = new SimpleStringProperty(user.getCollegeStatus().name());
        }

        public User getOriginalUser() {
            return originalUser;
        }

        public String geteMail() {
            return eMail.get();
        }

        public SimpleStringProperty eMailProperty() {
            return eMail;
        }

        public String getUserName() {
            return userName.get();
        }

        public SimpleStringProperty userNameProperty() {
            return userName;
        }

        public String getLastName() {
            return lastName.get();
        }

        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }

        public String getFirstName() {
            return firstName.get();
        }

        public SimpleStringProperty firstNameProperty() {
            return firstName;
        }

        public String getPermissions() {
            return permissions.get();
        }

        public SimpleStringProperty permissionsProperty() {
            return permissions;
        }

        public String getCollegeStatus() {
            return collegeStatus.get();
        }

        public SimpleStringProperty collegeStatusProperty() {
            return collegeStatus;
        }
    }
}