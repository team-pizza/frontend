package Objects;
import java.util.Vector;


/**
 * Title: Account (Base)
 * @author Paige Yon
 * Description: This class is used as a template for the user account class that will be implemented into the front-end development of B.A.S.
 * It currently holds user's name (google email) and list of groups associated with their account
 */
public class Account {
    // Account variables
    // Represents the user's google email
    private String username;
    // Groups associated with the user's account
    private Vector<Group> groupList;

    /**
     * This is the public constructor of the Account Class
     * To make an account, their gmail string is inputed
     * An empty Vector list of Group is initialized for the new account
     * @param gmail | String
     */
    public Account(String gmail){
        username = gmail;
        groupList = new Vector<Group>();
    }

    /**
     * This function, setUserName, is used to reset the username with the passed-in string
     * It is used mainly in the event of a user changing their email
     * @param name | String
     */
    public void setUserName(String name) {
        username = name;
    }

    /**
     *  This function, setGroupList, is used mainly to populate an empty group list with a large list of Groups (newGroupList)
     *  It can also be used to quickly reset an account's List of Groups
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
     * This function, returnName, returns the username attached to the account's data
     * @return username | string
     */
    public String returnName() {
        return username;
    }

    /**
     * This function, returnGroupList, returns a Vector<Group> array of the account's current groups
     * @return groupList | Vector<Group>
     */
    public Vector<Group> returnGroupList() {
        return groupList;
    }

    /**
     * This function, addGroup, is used to add a group to the user's account Group List
     * @param group | Group
     */
    public void addGroup(Group group) {
        groupList.addElement(group);
        // The list is trimmed to its new size
        groupList.trimToSize();
    }

    /**
     * This function, removeGroup, is used to remove a group from the user's account Group List
     * @param group | Group
     */
    public void removeGroup(Group group) {
        groupList.removeElement(group);
        // The list is trimmed to its new size
        groupList.trimToSize();
    }

}