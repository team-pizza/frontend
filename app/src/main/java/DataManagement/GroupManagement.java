package DataManagement;

import Objects.Group;
import java.util.Vector;
import Objects.Account;
import Objects.Database;

/**
 * Title: GroupManagement
 * Description: The class is used to manage Group data sent from the database
 * Currently this class can delete the Group data from the database, invite new members to a Group, and have a member leave the group
 * @author Paige Yon
 */
public class GroupManagement {
    // Holds the requested group for data manipulation
    private Group group;
    // Holds the database where the group information comes from
    private Database database;

    /**
     * This is the public constructor of the Group Management class
     * The requested group to manage is passed in along with the database data
     * @param request | Group
     * @param data | Database
     */
    public GroupManagement(Group request, Database data){
        group = request;
        database = data;
    }

    /**
     * This function, returnGroup, is used to return the Group object currently in GroupManagement object
     * It is used mainly for testing purposes
     * @return group | Group
     */
    public Group returnGroup() {
        return group;
    }

    /**
     * This function, setGroup, is used to change the Group object within the GroupManagement object using the param Group object
     * This ensures the GroupManagement object can be re-used
     * @param newGroup
     */
    public void setGroup(Group newGroup) {
        group = newGroup;
    }

    /**
     * This function, DeleteGroup, is used to delete a group from the database and its member's group lists
     * The request must be from the Group's owner's account. Otherwise, the request will be denied.
     * If the requested group is not currently in the manager, the setGroup function is called.
     * @param requestor | Account
     * @param requestedGroup | Group
     */
    public void deleteGroup(Account requestor, Group requestedGroup) {
        // Sets the GroupManagement object's group to the requested group if the param object and internal Group object are not a match
        if(!requestedGroup.returnGroupName().contentEquals(group.returnGroupName())) {
            this.setGroup(requestedGroup);
        }

        // If the CheckOwner function returns true, the request is permitted
        if(this.CheckOwner(requestor.returnName())==true) {
            // Calls the deleteMembers function to grab the users' information from the database and remove the group to be deleted from their lists
            this.deleteMembers(database, group);
            // Calls the deleteGroupData function to delete the group's information from the database
            this.deleteGroupData(database, group);
            // Sets the manager's internal group to null as the group data has finished being deleted
            this.setGroup(null);
        }
        // If the request is denied, this branch is used
        else {
            // When transferred to Front-End, this will be turned into a custom exception/toast message
            System.out.println("Error: User does not have Admin permission. Request Denied ");
        }
    }

    /**
     * This function, leaveGroup, is used for the requested member to leave the inputed Group
     * If the request is from the Group's owner's account, a new Owner will be chosen. Otherwise, only the requested will be affected.
     * For the prototype purposes, the response is inputed manually.
     * @param requestor | Account
     * @param requestedGroup | Group
     * @param response | char
     */
    public void leaveGroup(Account requestor, Group requestedGroup, char response) {
        // Sets the GroupManagement object's group to the requested group if the param object and internal Group object are not a match
        if(!requestedGroup.returnGroupName().contentEquals(group.returnGroupName())) {
            this.setGroup(requestedGroup);
        }

        // For this console prototype only, the input is received through the scanner
        System.out.println("Are you sure you want to leave " + this.group.returnGroupName() + "?");
        char results = response;

        if (results == 'Y' || results == 'y') {
            if(this.CheckOwner(requestor.returnName())==true) {
                //If the owner is the only member in the group, the group is deleted
                if(this.group.returnMemberList().size() == 1) {
                    this.deleteGroup(requestor, requestedGroup);
                }
                else {	// Plans later to have the owner pass on the ownership manually
                    // If the Account is not found in the list, index = -1
                    // Otherwise, index equals the position of the first found instance
                    int index = this.group.returnMemberList().indexOf(requestor);
                    int groupSize = this.group.returnMemberList().size();
                    // If the Account is the last in the vector array, the account before it is chosen as the new owner
                    if(index == groupSize - 1) {
                        Account newOwner = this.group.returnMemberList().elementAt(groupSize - 2);
                        this.group.setOwner(newOwner.returnName());
                    }
                    else {// Otherwise the account after the requestor in the list is chosen
                        Account newOwner = this.group.returnMemberList().elementAt(index + 1);
                        this.group.setOwner(newOwner.returnName());
                    }
                    requestor.removeGroup(requestedGroup);
                    this.group.removeGroupMember(requestor);
                    this.database.updateUser(requestor);
                    this.database.updateGroup(requestedGroup);
                }
            }
            else {
                requestor.removeGroup(requestedGroup);
                this.group.removeGroupMember(requestor);
                this.database.updateUser(requestor);
                this.database.updateGroup(requestedGroup);
            }
            System.out.println("You have left " + requestedGroup.returnGroupName() + ".");
        }
        else {
            System.out.println("Request cancelled.");
        }
    }

    /**
     * This function, inviteNewMember, is used for the requested member to invite a new member to a Group using their email
     * If the email is tied to an existing account, that account is updated to include the Group in their list upon receiving a confirmation from the account about joining
     * If the email is tied to an existing member, the request is disbanded.
     * If the email is not tied to an existing member or account, a new account is created upon the invited saying yes.
     * If the invited say no, the request is disbanded.
     * For the prototype purposes, the response is inputed manually.
     * @param requestor | Account
     * @param requestedGroup | Group
     * @param newMemberEmail | String
     * @param response | char
     */
    public void inviteNewMember(Account requestor, Group requestedGroup, String newMemberEmail, char response) {
        // Sets the GroupManagement object's group to the requested group if the param object and internal Group object are not a match
        if(!requestedGroup.returnGroupName().contentEquals(group.returnGroupName())) {
            this.setGroup(requestedGroup);
        }

        Account newMember = this.database.returnUser(newMemberEmail);
        if(newMember == null) {
            System.out.println("Does " + newMemberEmail + "accept " + this.group.returnGroupName() + " invitation?");
            completeInvitationProcess(this.group, newMemberEmail, newMember, this.database, response);
        }
        else {
            int index = this.group.returnMemberList().indexOf(newMember);
            if(index != -1) {
                System.out.println("Error: The requested user is already a member. ");
            }
            else {
                System.out.println(newMemberEmail + " is invited to join " + requestedGroup.returnGroupName() + ".");
                System.out.println("Does " + newMemberEmail + "accept " + this.group.returnGroupName() + " invitation?");
                completeInvitationProcess(this.group, newMemberEmail, newMember, this.database, response);
            }
        }
    }

    /* * * Private Functions * * */
    /*
     * This private function, completedInviationProcess, is used to completed the Invitation process for new Accounts
     */
    // Possibly for the console prototype only
    private void completeInvitationProcess(Group requestedGroup, String newMemberEmail, Account newMember, Database data, char response) {
        char results = response;
        if (results == 'Y' || results == 'y') {
            if(newMember != null) {
                requestedGroup.addGroupMember(newMember);
            }
            else {
                // In the future, there will be a verification method for creating new Account
                System.out.println("Registering your email with database...");
                System.out.println("Creating account...");
                newMember = new Account(newMemberEmail);
                requestedGroup.addGroupMember(newMember);
                data.addUser(newMember);
                data.updateGroup(requestedGroup);
            }
            System.out.println(newMemberEmail + " is a now a member of the " + requestedGroup.returnGroupName() + " Group.");
        }
    }


    /*
     * This private function, CheckOwner, is used to check in the passed in gmail string to the Group's current owner string
     * Returns true if the strings are a match
     * Returns false if the strings are not a match
     */
    private boolean CheckOwner(String requestor){
        // If the group owner's string matches the requestor's string, this branch is used
        if(group.returnOwner().contentEquals(requestor)) {
            return true;
        }
        // If the information is not a match, this branch is used
        else {
            return false;
        }
    }

    /*
     * This private function, deleteMembers, grabs the Group's members information from the database
     * It then removes the group from each member's list and updates their entries in the database
     */
    // Will have to change when integrated into the application to access the database
    private void deleteMembers(Database data, Group group) {
        // Holds the Group Members info
        Vector<Account> groupMembers = group.returnMemberList();
        // Removes the group from the member's list
        // Possibly reinstate statement if we raise language past 7: groupMembers.forEach(member -> member.removeGroup(group));
        for (int i = 0; i < groupMembers.size(); i++){
            groupMembers.elementAt(i).removeGroup(group);
        }
        // Updates the group's information in the database
        // Possibly reinstate statement if we raise language past 7: groupMembers.forEach(member->data.updateUser(member));
        for (int i = 0; i < groupMembers.size(); i++){
            data.updateUser(groupMembers.elementAt(i));
        }
    }


    /*
     * This private function, deletes the Group's data from the database
     */
    // Will have to change when integrated into the application to access the database
    private void deleteGroupData(Database data, Group group) {
        data.removeGroup(group);
    }
}