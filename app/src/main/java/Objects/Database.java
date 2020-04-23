package Objects;

import java.util.Vector;

/**
 * Title: Database (Temp)
 * @author Paige Yon
 * Description: This class is used as a template for the project's database class that will be implemented into the front-end development of B.A.S.
 * It currently holds a vector array of Groups and a vector array of Accounts.
 */
public class Database {
    // Holds  vector array of known Groups
    private Vector<Group> groupList;
    // Holds vector array of known Users
    private Vector<Account> userList;

    /**
     * This is the constructor of the Database class
     * It initializes a empty Vector<Group> array for grouplist and an empty Vector<Account> for userlist
     */
    public Database(){
        groupList = new Vector<Group>();
        userList = new Vector<Account>();
    }

    /**
     * This function, returnGroupList, returns a list of known Groups in the database
     * @return groupList | Vector<Group>
     */
    public Vector<Group> returnGroupList() {
        return groupList;
    }

    /**
     * This function, returnAccountList, returns a list of known Users in the database
     * @return userList | Vector<Account>
     */
    public Vector<Account> returnAccountList() {
        return userList;
    }

    /**
     *  This function, setGroupList, is used mainly to populate an empty group list with a large list of Groups (newGroupList)
     *  It can also be used to quickly reset the database's list of Groups
     * @param newGroupList | Vector<Group>
     */
    public void setGroupList(Vector<Group> newGroupList) {
        groupList.clear();
        groupList.trimToSize();
        // Possibly reinstate statement if we raise language past 7: newGroupList.forEach(group -> groupList.addElement(group));
        for (int i = 0; i < newGroupList.size(); i++){
            groupList.addElement(newGroupList.elementAt(i));
        }
    }

    /**
     *  This function, setAccountList, is used mainly to populate an empty account list with a large list of Account (newUserList)
     *  It can also be used to quickly reset the database's list of Accounts
     * @param newUserList | Vector<Account>
     */
    public void setAccountList(Vector<Account> newUserList) {
        userList.clear();
        userList.trimToSize();
        // Possibly reinstate statement if we raise language past 7: newUserList.forEach(user -> userList.addElement(user));
        for (int i = 0; i < newUserList.size(); i++){
            userList.addElement(newUserList.elementAt(i));
        }
    }

    /**
     * This function, addGroup, is used to add a requested Group to the database
     * @param group | Group
     */
    public void addGroup(Group group) {
        groupList.addElement(group);
        groupList.trimToSize();
    }

    /**
     * This function, updateGroup, is used to update the inputed Group's information in the database
     * If the Group does not exist, an error is thrown
     * @param updatedGroup | Group
     */
    public void updateGroup(Group updatedGroup) {
        // If the Group is not found in the list, index = -1
        // Otherwise, index equals the position of the first found instance
        int index = this.returnGroupList().indexOf(updatedGroup);
        // If Group is not found, this branch is used
        if (index == -1) {
            // When transferred to Front-End, this will be turned into a custom exception/toast message
            System.out.println("Error: Group Does Not Exist in Database");
        }else { // User's information is updated
            groupList.set(index, updatedGroup);
        }
    }

    /**
     * This function, removeGroup, is used to remove a requested Group from the database
     * @param group | Group
     */
    public void removeGroup(Group group) {
        groupList.removeElement(group);
        groupList.trimToSize();
    }

    /**
     * This function, returnGroup, is used to return a Group's information from the database list
     * It is currently used more for testing purposes
     * @param requested | Group
     * @return requestedGroup | Group
     */
    public Group returnGroup(Group requested) {
        // If the Group is not found in the list, index = -1
        // Otherwise, index equals the position of the first found instance
        int index = groupList.indexOf(requested);
        if (index == -1) {
            // When transferred to Front-End, this will be turned into a custom exception/toast message
            System.out.println("Error: User Does Not Exist in Database");
            return null;
        }else { // Returns requested Group data
            return groupList.elementAt(index);
        }
    }

    /**
     * This function, addAccount, is used to add a requested Account to the database
     * @param user | Account
     */
    public void addUser(Account user) {
        userList.addElement(user);
        userList.trimToSize();
    }

    /**
     * This function, addAccount, is used to remove a requested Account to the database
     * @param user | Account
     */
    public void removeUser(Account user) {
        userList.removeElement(user);
        userList.trimToSize();
    }


    /**
     * This function, updateUser, is used to update the inputed Account's information in the database
     * If the account does not exist, an error is thrown
     * @param updatedUser | Account
     */
    public void updateUser(Account updatedUser) {
        // If the Account is not found in the list, index = -1
        // Otherwise, index equals the position of the first found instance
        int index = this.returnAccountList().indexOf(updatedUser);
        // If Account is not found, this branch is used
        if (index == -1) {
            // When transferred to Front-End, this will be turned into a custom exception/toast message
            System.out.println("Error: User Does Not Exist in Database");
        }else { // User's information is updated
            userList.set(index, updatedUser);
        }
    }

    /**
     * This function, returnUser, is used to return a user's account information from the database list
     * It is currently used more for testing purposes
     * @param requested | Account
     * @return requestedAccount | Account
     */
    public Account returnUser(Account requested) {
        // If the Account is not found in the list, index = -1
        // Otherwise, index equals the position of the first found instance
        int index = userList.indexOf(requested);
        if (index == -1) {
            // When transferred to Front-End, this will be turned into a custom exception/toast message
            System.out.println("Error: User Does Not Exist in Database");
            return null;
        }else { // Returns requested user account
            return userList.elementAt(index);
        }
    }

    /**
     * This override function, returnUser, is used to return a user's account information from the database list using an email
     * It is currently used more for testing purposes
     * @param email | String
     * @return requestedAccount | Account
     */
    public Account returnUser(String email) {
        for(int i =0; i < this.userList.size(); i++) {
            if(this.userList.elementAt(i).returnName().equals(email)) {
                return this.userList.elementAt(i);
            }
        }
        return null;
    }
}
