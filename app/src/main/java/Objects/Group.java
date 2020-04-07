package Objects;
import java.util.Vector;

/**
 * Title: Group
 * Description: This class is used to create an object, Group, that represents an organization of users
 * The class holds its name, owner, and members
 * @author Paige Yon
 */
public class Group {
    // Holds the group's name
    private String groupName;
    // Holds the gmail for the group's current owner
    private String groupOwner;
    // Holds a list of Account for group members
    private Vector<Account> groupMembers;


    /**
     * This is public constructor for an empty Group object
     * It initializes empty strings for the owner and name as well as an empty group member list
     * It is typically used for testing purposes
     */
    public Group(){
        groupName = "";
        groupOwner = "";
        groupMembers = new Vector<Account>();
    }

    /**
     * This is public override constructor for a Group Object with a initialized name and owner
     * It is used in the event of a user creating a new group
     * @param creator | Account
     * @param name | String
     */
    public Group(Account creator, String name){
        // Sets the group's name and owner to inputted variables
        groupName = name;
        groupOwner = creator.returnName();

        // Initializes group member list with creator as a member
        groupMembers = new Vector<Account>();
        groupMembers.addElement(creator);
        // Adds group to creator's account
        creator.addGroup(this);
    }

    /**
     * This function, returnGroupName, returns the string of the Group's name
     * @return groupName | String
     */
    public String returnGroupName() {
        return groupName;
    }

    /**
     * This function, returnOwner, returns the gmail string of the current Group's owner
     * @return groupOwner | String
     */
    public String returnOwner() {
        return groupOwner;
    }

    /**
     * This function, returnMemberList, returns a Vector<Account> array of the list of group members associated with the Group
     * @return groupMembers | Vector<Account>
     */
    public Vector<Account> returnMemberList() {
        return groupMembers;
    }

    /**
     * This function, setOwner, is used to reset the Group's owner in the event of the current owner retiring from the Group
     * @param newOwner | String
     */
    public void setOwner(String newOwner) {
        groupOwner = newOwner;
    }

    /**
     * This function, setGroupName, is used to rename the Group
     * The requester must be the Group's current owner. Otherwise, the request will be denied
     * @param newName | String
     * @param requester | String
     */
    // It might need to be updated to include the database when transferred to Front-End
    public void setGroupName(String newName, String requester) {
        // If the requester's gmail matches the Group's owner's gmail, this branch is used
        if(requester.contentEquals(groupOwner)) {
            // Renames the Group
            groupName = newName;
        }
        // If the request is denied, this branch is used
        else {
            // When transferred to Front-End, this will be turned into a custom exception/toast message
            System.out.println("Error: User does not have Admin permission. Request Denied ");
        }
    }

    /**
     * This function, addGroupMember, is used to add a new account to the group's member list
     * The Group is then added to the new member's account's group list
     * @param newMember | Account
     */
    public void addGroupMember(Account newMember) {
        // Adds member to group
        groupMembers.addElement(newMember);
        // Adds group to member's list
        newMember.addGroup(this);
    }

    /**
     * This function, removeGroupMember, is used to remove an existing account from the group's member list
     * @param requested | Account
     */
    public void removeGroupMember(Account requested) {
        groupMembers.removeElement(requested);
        // The list is trimmed to its new size
        groupMembers.trimToSize();
    }
}