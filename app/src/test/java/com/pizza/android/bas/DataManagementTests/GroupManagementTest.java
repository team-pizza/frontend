package com.pizza.android.bas.DataManagementTests;
import DataManagement.GroupManagement;
import org.junit.Test;
import Objects.Group;
import Objects.Account;
import Objects.Database;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Title: GroupManagementTest
 * Description: This JUnit Test Class is used to test the GroupManagement class and its functions
 * @author Paige Yon
 */
public class GroupManagementTest {

    /**
     * This function, testGroupManagement, is used to test the constructor of the GroupManagement class
     */
    @Test
    public void testGroupManagement() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        Group newGroup = new Group(testOwner, "Test");
        Database data = new Database();

        // Begins Testing
        GroupManagement manager = new GroupManagement(newGroup, data);

        // Checks Results
        assertEquals(newGroup, manager.returnGroup());
    }

    /**
     * This function, testReturnGroup, is used to test the GroupManagement's returnGroup function
     */
    @Test
    // Tests returning the group management's group
    public void testReturnGroup() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        Group group = new Group(testOwner, "Test");
        Database data = new Database();

        // Begins Testing
        GroupManagement manager = new GroupManagement(group, data);

        // Checks Results
        assertEquals(group, manager.returnGroup());
    }

    /**
     * This function, testSetGroup, is used to test the GroupManagement's setGroup function
     */
    @Test
    // Tests setting the group management's group
    public void testSetGroup() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        Group oldGroup = new Group(testOwner, "Test");
        Database data = new Database();
        Group newGroup = new Group();

        // Begins Testing
        GroupManagement manager = new GroupManagement(oldGroup, data);
        manager.setGroup(newGroup);

        // Checks Results
        assertEquals(newGroup, manager.returnGroup());
    }

    /**
     * This function, testDeleteGroupCreator, is used to test the GroupManagement's deleteGroup function as the Group's Creator
     */
    @Test
    // Tests deleting group as creator/owner
    public void testDeleteGroupCreator() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        Account testMember = new Account("Member1");

        Group newGroup = new Group(testOwner, "Test1");
        Group deleteGroup = new Group(testOwner, "DeleteMe");
        deleteGroup.addGroupMember(testMember);

        // Sets up database
        Database data = new Database();
        data.addGroup(newGroup);
        data.addGroup(deleteGroup);
        data.addUser(testOwner);
        data.addUser(testMember);

        // Begins Testing
        GroupManagement manager = new GroupManagement(deleteGroup, data);
        manager.deleteGroup(testOwner, deleteGroup);

        // Checks Results; Ensures deleteGroup's data does not exist in database
        assertEquals(null, manager.returnGroup());
        assertNotEquals(-1, data.returnGroupList().indexOf(newGroup));
        assertEquals(-1, data.returnGroupList().indexOf(deleteGroup));
        // Ensures deleteGroup's data does not exist in the group member's data
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner));
        assertNotEquals(-1, data.returnAccountList().indexOf(testMember));
        assertNotEquals(-1, data.returnUser(testOwner).returnGroupList().indexOf(newGroup));
        assertEquals(-1, data.returnUser(testOwner).returnGroupList().indexOf(deleteGroup));
        assertEquals(-1, data.returnUser(testMember).returnGroupList().indexOf(deleteGroup));
    }

    /**
     * This function, testDeleteNewGroupCreator, is used to test the GroupManagement's deleteGroup function as the Group's Creator
     * It additionally makes the GroupManagement switch group data to manage the requested info
     */
    @Test
    public void testDeleteNewGroupCreator() {
        // Initializes Data
        Account testOwner1 = new Account("Owner");
        Account testOwner2 = new Account("Member1");

        Group newGroup = new Group(testOwner1, "Test1");
        Group deleteGroup = new Group(testOwner2, "DeleteMe");
        deleteGroup.addGroupMember(testOwner1);

        // Sets up database/
        Database data = new Database();
        data.addGroup(newGroup);
        data.addGroup(deleteGroup);
        data.addUser(testOwner1);
        data.addUser(testOwner2);

        // Begins Testing
        GroupManagement manager = new GroupManagement(newGroup, data);
        manager.deleteGroup(testOwner2, deleteGroup);


        // Checks Results; Ensures deleteGroup's data does not exist in database
        assertEquals(null, manager.returnGroup());
        assertNotEquals(-1, data.returnGroupList().indexOf(newGroup));
        assertEquals(-1, data.returnGroupList().indexOf(deleteGroup));
        // Ensures deleteGroup's data does not exist in the group member's data
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner1));
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner2));
        assertNotEquals(-1, data.returnUser(testOwner1).returnGroupList().indexOf(newGroup));
        assertEquals(-1, data.returnUser(testOwner1).returnGroupList().indexOf(deleteGroup));
        assertEquals(-1, data.returnUser(testOwner2).returnGroupList().indexOf(deleteGroup));
    }

    /**
     * This function, testDeleteGroupMember, is used to test the GroupManagement's deleteGroup function as the Group's member
     */
    @Test
    public void testDeleteGroupMember() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        Account testMember = new Account("Member");
        Group newGroup = new Group(testOwner, "Test");
        Database data = new Database();

        //Begins testing
        GroupManagement manager = new GroupManagement(newGroup, data);
        manager.deleteGroup(testMember, newGroup);

        // Checks Results; newGroup should not be deleted
        assertEquals(newGroup, manager.returnGroup());
    }

    /**
     * This function, testDeleteNewGroupMember, is used to test the GroupManagement's deleteGroup function as the Group's member
     * It additionally makes the GroupManagement switch group data to manage the requested info
     */
    @Test
    public void testDeleteNewGroupMember() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        Account testMember = new Account("Member");
        Group oldGroup = new Group(testOwner, "Test");
        Group newGroup = new Group();
        Database data = new Database();

        // Begins Testing
        GroupManagement manager = new GroupManagement(oldGroup, data);
        manager.deleteGroup(testMember, newGroup);

        // Checks Results; newGroup should not be deleted
        assertNotEquals(oldGroup, manager.returnGroup());
        assertEquals(newGroup, manager.returnGroup());
    }

    /**
     * This function, testLeaveGroupCreator, is used to test the GroupManagement's leaveGroup function as the Group's Creator
     * The user's response is yes in this test case.
     */
    @Test
    public void testLeaveGroupCreator() {
        // Initializes Data
        Account testOwner1 = new Account("Owner");
        Account testOwner2 = new Account("Member1");

        Group testGroup = new Group(testOwner1, "Test1");
        testGroup.addGroupMember(testOwner2);

        char response = 'Y';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner1);
        data.addUser(testOwner2);

        // Begins Testing
        GroupManagement manager = new GroupManagement(testGroup, data);
        manager.leaveGroup(testOwner1, testGroup, response);


        // Checks Results; Ensures testGroup still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does not exist in old owner's data and testOwner2 is the new Owner of the group
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner1));
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner2));
        assertEquals(-1, data.returnUser(testOwner1).returnGroupList().indexOf(testGroup));
        assertEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(testOwner1));
        assertEquals(testOwner2.returnName(), testGroup.returnOwner());
    }

    /**
     * This function, testLeaveGroupCreator, is used to test the GroupManagement's leaveGroup function as the Group's Creator
     * The user's response is yes in this test case.
     * It additionally makes the GroupManagement switch group data to manage the requested info
     */
    @Test
    public void testLeaveNewGroupCreator() {
        // Initializes Data
        Account testOwner1 = new Account("Owner");
        Account testOwner2 = new Account("Member1");

        Group holderGroup = new Group(testOwner1, "Test0");
        Group testGroup = new Group(testOwner1, "Test1");
        testGroup.addGroupMember(testOwner2);

        char response = 'Y';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner1);
        data.addUser(testOwner2);

        // Begins Testing
        GroupManagement manager = new GroupManagement(holderGroup, data);
        manager.leaveGroup(testOwner1, testGroup, response);


        // Checks Results; Ensures testGroup still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does not exist in old owner's data and testOwner2 is the new Owner of the group
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner1));
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner2));
        assertEquals(-1, data.returnUser(testOwner1).returnGroupList().indexOf(testGroup));
        assertEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(testOwner1));
        assertEquals(testOwner2.returnName(), testGroup.returnOwner());
    }

    /**
     * This function, testLeaveGroupMembe, is used to test the GroupManagement's leaveGroup function as the Group's member
     * The user's response is yes in this test case.
     */
    @Test
    public void testLeaveGroupMember() {
        // Initializes Data
        Account testOwner1 = new Account("Owner");
        Account testOwner2 = new Account("Member1");

        Group testGroup = new Group(testOwner1, "Test1");
        testGroup.addGroupMember(testOwner2);

        char response = 'Y';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner1);
        data.addUser(testOwner2);

        // Begins Testing
        GroupManagement manager = new GroupManagement(testGroup, data);
        manager.leaveGroup(testOwner2, testGroup, response);


        // Checks Results; Ensures testGroup still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does not exist in old member's data and testOwner1 is the new Owner of the groupassertNotEquals(-1, data.returnAccountList().indexOf(testOwner1));
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner2));
        assertEquals(-1, data.returnUser(testOwner2).returnGroupList().indexOf(testGroup));
        assertEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(testOwner2));
        assertEquals(testOwner1.returnName(), testGroup.returnOwner());
    }

    /**
     * This function, testLeaveNewGroupMember, is used to test the GroupManagement's leaveGroup function as the Group's member
     * The user's response is yes in this test case.
     * It additionally makes the GroupManagement switch group data to manage the requested info
     */
    @Test
    public void testLeaveNewGroupMember() {
        // Initializes Data
        Account testOwner1 = new Account("Owner");
        Account testOwner2 = new Account("Member1");

        Group holderGroup = new Group(testOwner1, "Test0");
        Group testGroup = new Group(testOwner1, "Test1");
        testGroup.addGroupMember(testOwner2);

        char response = 'Y';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner1);
        data.addUser(testOwner2);

        // Begins Testing
        GroupManagement manager = new GroupManagement(holderGroup, data);
        manager.leaveGroup(testOwner2, testGroup, response);


        // Checks Results; Ensures testGroup still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does not exist in old member's data and testOwner1 is the new Owner of the group
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner1));
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner2));
        assertEquals(-1, data.returnUser(testOwner2).returnGroupList().indexOf(testGroup));
        assertEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(testOwner2));
        assertEquals(testOwner1.returnName(), testGroup.returnOwner());
    }

    /**
     * This function, testLeaveGroupCreatorNo, is used to test the GroupManagement's leaveGroup function as the Group's creator
     * The user's response is no in this test case.
     */
    @Test
    public void testLeaveCreatorNo() {
        // Initializes Data
        Account testOwner1 = new Account("Owner");
        Account testOwner2 = new Account("Member1");

        Group testGroup = new Group(testOwner1, "Test1");
        testGroup.addGroupMember(testOwner2);

        char response = 'N';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner1);
        data.addUser(testOwner2);

        // Begins Testing
        GroupManagement manager = new GroupManagement(testGroup, data);
        manager.leaveGroup(testOwner1, testGroup, response);


        // Checks Results; Ensures testGroup still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does exist in the member's data and vise-versa
        // Testowner1 is still the owner of the Group
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner1));
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner2));
        assertNotEquals(-1, data.returnUser(testOwner1).returnGroupList().indexOf(testGroup));
        assertNotEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(testOwner1));
        assertEquals(testOwner1.returnName(), testGroup.returnOwner());

    }

    /**
     * This function, testLeaveGroupMemberNo, is used to test the GroupManagement's leaveGroup function as the Group's member
     * The user's response is no in this test case.
     */
    @Test
    public void testLeaveMemberNo() {
        // Initializes Data
        Account testOwner1 = new Account("Owner");
        Account testOwner2 = new Account("Member1");

        Group testGroup = new Group(testOwner1, "Test1");
        testGroup.addGroupMember(testOwner2);

        char response = 'N';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner1);
        data.addUser(testOwner2);

        // Begins Testing
        GroupManagement manager = new GroupManagement(testGroup, data);
        manager.leaveGroup(testOwner2, testGroup, response);


        // Checks Results; Ensures testGroup still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does exist in the member's data and vise-versa
        // Testowner1 is still the owner of the Group
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner1));
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner2));
        assertNotEquals(-1, data.returnUser(testOwner2).returnGroupList().indexOf(testGroup));
        assertNotEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(testOwner2));
        assertEquals(testOwner1.returnName(), testGroup.returnOwner());
    }

    /**
     * This function,testInviteNewMemberAccept, is used to test the GroupManagement's inviteNewMember function
     * The user's response is yes in this test case
     * The function finds the existing account as the database does not have the user registered with the group
     */
    @Test
    public void testInviteNewUserMemberAccept() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        String testNewEmail = "Member1";
        Account testMember = new Account(testNewEmail);
        Group testGroup = new Group(testOwner, "Test1");
        char response = 'Y';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner);
        data.addUser(testMember);

        // Begins Testing
        GroupManagement manager = new GroupManagement(testGroup, data);
        manager.inviteNewMember(testOwner, testGroup, testNewEmail, response);

        // Checks Results; Ensures testGroup and testMember still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does exist in the new member's data and vise-versa
        // Ensures both users exist in the database
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner));
        assertNotEquals(-1, data.returnAccountList().indexOf(testMember));
        assertNotEquals(-1, data.returnUser(testMember).returnGroupList().indexOf(testGroup));
        assertNotEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(testMember));
    }

    /**
     * This function,testInviteNewMemberAccept, is used to test the GroupManagement's inviteNewMember function
     * The user's response is yes in this test case
     * The function creates a new account as the database does not have the user registered.
     */
    @Test
    public void testInviteNewMemberAccept() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        String testNewEmail = "Member1";
        Group testGroup = new Group(testOwner, "Test1");
        char response = 'Y';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner);

        // Begins Testing
        GroupManagement manager = new GroupManagement(testGroup, data);
        manager.inviteNewMember(testOwner, testGroup, testNewEmail, response);

        // Checks Results; Ensures testGroup and testMember still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does exist in the new member's data and vise-versa
        // Ensures the new user exists in data base and group has added new member to their database
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner));
        assertNotEquals(-1, data.returnUser(testNewEmail));
        assertNotEquals(-1, data.returnUser(testNewEmail).returnGroupList().indexOf(testGroup));
        assertNotEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(data.returnUser(testNewEmail)));
    }

    /**
     * This function, testInviteExistingMemberNo, is used to test the GroupManagement's inviteNewMember function
     * The user's response is yes in this test case, but the member already exists so the request is cancelled
     */
    @Test
    public void testInviteExistingMemberNo() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        String testNewEmail = "Member1";
        Account testMember = new Account(testNewEmail);
        Group testGroup = new Group(testOwner, "Test1");
        testGroup.addGroupMember(testMember);
        char response = 'Y';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner);
        data.addUser(testMember);

        // Begins Testing
        GroupManagement manager = new GroupManagement(testGroup, data);
        manager.inviteNewMember(testOwner, testGroup, testNewEmail, response);

        // Checks Results; Ensures testGroup and testMember still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does exist in the member's data and vise-versa
        // Ensures both users exist in the database
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner));
        assertNotEquals(-1, data.returnAccountList().indexOf(testMember));
        assertNotEquals(-1, data.returnUser(testMember).returnGroupList().indexOf(testGroup));
        assertNotEquals(-1, data.returnGroup(testGroup).returnMemberList().indexOf(testMember));
    }

    /**
     * This function, testInviteNewMemeberNo, is used to test the GroupManagement's inviteNewMember function
     * The user's response is no in this test case.
     */
    @Test
    public void testInviteNewMemeberNo() {
        // Initializes Data
        Account testOwner = new Account("Owner");
        String testNewEmail = "Member1";
        Account testMember = new Account(testNewEmail);
        Group testGroup = new Group(testOwner, "Test1");
        char response = 'N';

        // Sets up database/
        Database data = new Database();
        data.addGroup(testGroup);
        data.addUser(testOwner);
        data.addUser(testMember);

        // Begins Testing
        GroupManagement manager = new GroupManagement(testGroup, data);
        manager.inviteNewMember(testOwner, testGroup, testNewEmail, response);

        // Checks Results; Ensures testGroup and testMember still exists in database
        assertNotEquals(-1, data.returnGroupList().indexOf(testGroup));

        // Ensures testGroup's data does not exist in the member's data and vise-versa
        // Ensures both users exist in the database
        assertNotEquals(-1, data.returnAccountList().indexOf(testOwner));
        assertNotEquals(-1, data.returnAccountList().indexOf(testMember));
        assertNotEquals(1, data.returnUser(testMember).returnGroupList().indexOf(testGroup));
        assertNotEquals(1, data.returnGroup(testGroup).returnMemberList().indexOf(testMember));
    }
}
