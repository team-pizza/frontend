package com.pizza.android.bas.ObjectsTests;

import org.junit.Test;
import java.util.Vector;
import Objects.Group;
import Objects.Account;
import Objects.Database;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Title: DatabaseTest
 * Description: This JUnit Test Class is used to test the Database class and its functions
 * @author Paige Yon
 */
public class DatabaseTest {
    /**
     * This function, testDatabase, is used to test the constructor of the Database class
     */
    @Test
    void testDatabase() {
        // Initializes Test Data
        Account mainTestUser = new Account("User");
        Group mainTestGroup = new Group(mainTestUser, "TestGroup");

        // Begins Testing
        Database testData = new Database();
        testData.addGroup(mainTestGroup);
        testData.addUser(mainTestUser);

        // Checks Results; Ensures that each object is in the Database
        assertNotEquals(-1, testData.returnAccountList().indexOf(mainTestUser));
        assertNotEquals(-1, testData.returnGroupList().indexOf(mainTestGroup));
    }

    /**
     * This function, testReturnGroupList, is used to test the Database's returnGroupList function
     */
    @Test
    void testReturnGroupList() {
        // Initializes Test Data
        Account mainTestUser = new Account("User");
        Group mainTestGroup1 = new Group(mainTestUser, "TestGroup1");
        Group mainTestGroup2 = new Group(mainTestUser, "TestGroup2");
        Group mainTestGroup3 = new Group(mainTestUser, "TestGroup3");

        // Creates Group List for expected outcome
        Vector<Group> groups = new Vector<Group>();
        groups.addElement(mainTestGroup1);
        groups.addElement(mainTestGroup2);
        groups.addElement(mainTestGroup3);

        // Begins Testing
        Database testData = new Database();
        testData.setGroupList(groups);

        // Checks Results
        assertEquals(groups, testData.returnGroupList());
    }

    /**
     * This function, testAddGroup, is used to test the Database's addGroup function
     */
    @Test
    void testAddGroup() {
        // Initializes Test Data
        Account mainTestUser = new Account("User");
        Group mainTestGroup1 = new Group(mainTestUser, "TestGroup1");
        Group mainTestGroup2 = new Group(mainTestUser, "TestGroup2");
        Group mainTestGroup3 = new Group(mainTestUser, "TestGroup3");

        // Creates Group List for expected outcome
        Vector<Group> groups = new Vector<Group>();
        groups.addElement(mainTestGroup1);
        groups.addElement(mainTestGroup2);
        groups.addElement(mainTestGroup3);

        // Begins Testing
        Database testData = new Database();
        testData.addGroup(mainTestGroup1);
        testData.addGroup(mainTestGroup2);
        testData.addGroup(mainTestGroup3);

        // Checks Results
        assertEquals(groups, testData.returnGroupList());
    }

    /**
     * This function, testReturnGroup, is used to test the Database's returnGroup function
     */
    @Test
    void testReturnGroup() {
        // Initializes Test Data using empty Group objects
        Group mainTestGroup1= new Group();
        Group mainTestGroup2 = new Group();
        Group mainTestGroup3 = new Group();

        // Creates Group List for expected outcome
        Vector<Group> groups = new Vector<Group>();
        groups.addElement(mainTestGroup1);
        groups.addElement(mainTestGroup2);
        groups.addElement(mainTestGroup3);

        // Begins Testing
        Database testData = new Database();
        testData.setGroupList(groups);

        // Checks Results; Makes sure it can return each Account object when called
        assertEquals(mainTestGroup1, testData.returnGroup(mainTestGroup1));
        assertEquals(mainTestGroup2, testData.returnGroup(mainTestGroup2));
        assertEquals(mainTestGroup3, testData.returnGroup(mainTestGroup3));
    }

    /**
     * This function, testUpdateGroup, is used to test the Database's updateGroup function
     */
    @Test
    void testUpdateGroup() {
        // Initializes Test Data
        Account mainTestUser = new Account("User");
        Group mainTestGroup = new Group(mainTestUser, "TestGroup");
        Account mainTestUser1 = new Account("TestUser1");
        Account mainTestUser2 = new Account("TestUser2");
        Account mainTestUser3 = new Account("TestUser3");

        // Sets up Database
        Database testData = new Database();
        testData.addUser(mainTestUser);
        testData.addUser(mainTestUser1);
        testData.addUser(mainTestUser2);
        testData.addUser(mainTestUser3);
        testData.addGroup(mainTestGroup);

        // Begins Testing
        mainTestGroup.removeGroupMember(mainTestUser2);
        testData.updateGroup(mainTestGroup);

        // Checks Results; Ensures that MainTestGroup no longer has mainTestUser2
        // Also checks to make sure user exists in database still
        assertEquals(-1, testData.returnGroup(mainTestGroup).returnMemberList().indexOf(mainTestUser2));
        assertNotEquals(-1, testData.returnAccountList().indexOf(mainTestUser2));
    }


    /**
     * This function, testRemoveGroup, is used to test the Database's removeGroup function
     */
    @Test
    void testRemoveGroup() {
        // Initializes Test Data
        Account mainTestUser = new Account("User");
        Group mainTestGroup1 = new Group(mainTestUser, "TestGroup1");
        Group mainTestGroup2 = new Group(mainTestUser, "TestGroup2");
        Group mainTestGroup3 = new Group(mainTestUser, "TestGroup3");

        // Creates Group List for expected outcome
        Vector<Group> groups = new Vector<Group>();
        groups.addElement(mainTestGroup1);
        groups.addElement(mainTestGroup2);
        groups.addElement(mainTestGroup3);

        // Begins Testing
        Database testData = new Database();
        testData.setGroupList(groups);
        testData.removeGroup(mainTestGroup2);

        // Checks Results; Makes sure TestGroup2 is removed
        assertNotEquals(testData.returnGroupList(), groups);
        assertNotEquals(-1, testData.returnGroupList().indexOf(mainTestGroup1));
        assertNotEquals(-1, testData.returnGroupList().indexOf(mainTestGroup3));
        assertEquals(-1, testData.returnGroupList().indexOf(mainTestGroup2));
    }

    /**
     * This function, testReturnAccountList, is used to test the Database's returnAccountList function
     */
    @Test
    void testReturnAccountList() {
        // Initializes Test Data
        Account mainTestUser1 = new Account("TestUser1");
        Account mainTestUser2 = new Account("TestUser2");
        Account mainTestUser3 = new Account("TestUser3");

        // Creates Account List for expected outcome
        Vector<Account> users = new Vector<Account>();
        users.addElement(mainTestUser1);
        users.addElement(mainTestUser2);
        users.addElement(mainTestUser3);

        // Begins Testing
        Database testData = new Database();
        testData.setAccountList(users);

        // Checks Results
        assertEquals(users, testData.returnAccountList());
    }

    /**
     * This function, testAddUser, is used to test the Database's addUser function
     */
    @Test
    void testAddUser() {
        // Initializes Test Data
        Account mainTestUser1 = new Account("TestUser1");
        Account mainTestUser2 = new Account("TestUser2");
        Account mainTestUser3 = new Account("TestUser3");

        // Creates Account List for expected outcome
        Vector<Account> users = new Vector<Account>();
        users.addElement(mainTestUser1);
        users.addElement(mainTestUser2);
        users.addElement(mainTestUser3);

        // Begins Testing
        Database testData = new Database();
        testData.addUser(mainTestUser1);
        testData.addUser(mainTestUser2);
        testData.addUser(mainTestUser3);

        // Checks Results
        assertEquals(users, testData.returnAccountList());
    }

    /**
     * This function, testReturnUser, is used to test the Database's returnUser function
     */
    @Test
    void testReturnUser() {
        // Initializes Test Data
        Account mainTestUser1 = new Account("TestUser1");
        Account mainTestUser2 = new Account("TestUser2");
        Account mainTestUser3 = new Account("TestUser3");

        // Creates Account List for expected outcome
        Vector<Account> users = new Vector<Account>();
        users.addElement(mainTestUser1);
        users.addElement(mainTestUser2);
        users.addElement(mainTestUser3);

        // Begins Testing
        Database testData = new Database();
        testData.setAccountList(users);

        // Checks Results; Makes sure it can return each Account object when called
        assertEquals(mainTestUser1, testData.returnUser(mainTestUser1));
        assertEquals(mainTestUser2, testData.returnUser(mainTestUser2));
        assertEquals(mainTestUser3, testData.returnUser(mainTestUser3));
    }

    /**
     * This function, testRemoveUser, is used to test the Database's removeUser function
     */
    @Test
    void testRemoveUser() {
        // Initializes Test Data
        Account mainTestUser1 = new Account("TestUser1");
        Account mainTestUser2 = new Account("TestUser2");
        Account mainTestUser3 = new Account("TestUser3");

        // Creates Account List for expected outcome
        Vector<Account> users = new Vector<Account>();
        users.addElement(mainTestUser1);
        users.addElement(mainTestUser2);
        users.addElement(mainTestUser3);

        // Begins Testing
        Database testData = new Database();
        testData.setAccountList(users);
        testData.removeUser(mainTestUser2);

        // Checks Results; Makes sure User2 is removed
        assertNotEquals(testData.returnGroupList(), users);
        assertNotEquals(-1, testData.returnAccountList().indexOf(mainTestUser1));
        assertNotEquals(-1, testData.returnAccountList().indexOf(mainTestUser3));
        assertEquals(-1, testData.returnAccountList().indexOf(mainTestUser2));
    }

    /**
     * This function, testUpdateUser, is used to test the Database's updateUser function
     */
    @Test
    void testUpdateUser() {
        // Initializes Test Data
        Account mainTestUser = new Account("User");
        Group mainTestGroup = new Group(mainTestUser, "TestGroup");
        Group mainTestGroup2 = new Group(mainTestUser, "mainTestGroup2");

        // Sets up Database
        Database testData = new Database();
        testData.addUser(mainTestUser);

        // Begins Testing
        mainTestUser.removeGroup(mainTestGroup);
        testData.updateUser(mainTestUser);

        // Checks Results; Ensures that MainTestUser no longer has mainTestGroup
        assertNotEquals(-1, testData.returnUser(mainTestUser).returnGroupList().indexOf(mainTestGroup2));
        assertEquals(-1, testData.returnUser(mainTestUser).returnGroupList().indexOf(mainTestGroup));
    }


}