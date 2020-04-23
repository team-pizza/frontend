package com.pizza.android.bas.ObjectsTests;

import org.junit.Test;
import java.util.Vector;
import Objects.Group;
import Objects.Account;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Title: GroupTest
 * Description: This JUnit Test Class is used to test the Group class and its functions
 * @author Paige Yon
 */
public class GroupTest {

    /**
     * This function, testGroup, is used to test the empty constructor of the GroupManagement class
     */
    @Test
    public void testGroup() {
        // Initialize Data
        Group newGroup = new Group();

        // Check Results
        assertEquals("", newGroup.returnGroupName());
        assertEquals("", newGroup.returnOwner());
    }

    /**
     * This function, testGroupAccountString, is used to test the override constructor of the GroupManagement class
     */
    @Test
    public void testGroupAccountString() {
        // Initialize Data
        Account testOwner = new Account("Owner");
        Group newGroup = new Group(testOwner, "TestGroup");

        // Check Results
        assertEquals("TestGroup", newGroup.returnGroupName());
        assertEquals("Owner", newGroup.returnOwner());
        assertNotEquals(-1, newGroup.returnMemberList().indexOf(testOwner));
    }

    /**
     * This function, testReturnGroupName, is used to test the Group class's returnGroupName function
     */
    @Test
    public void testReturnGroupName() {
        // Initialize Data
        Account testOwner = new Account("Owner");

        // Begin Testing
        Group newGroup = new Group(testOwner, "TestGroup");

        // Check Results
        assertEquals("TestGroup", newGroup.returnGroupName());
    }

    /**
     * This function, testReturnOwner, is used to test the Group class's returnOwner function
     */
    @Test
    public void testReturnOwner() {
        // Initialize Data
        Account testOwner = new Account("Owner");

        // Begin Testing
        Group newGroup = new Group(testOwner, "TestGroup");

        // Check Results
        assertEquals("Owner", newGroup.returnOwner());
    }

    /**
     * This function, testReturnGroupMembers, is used to test the Group class's returnMemberList function
     */
    @Test
    public void testReturnGroupMembers() {
        // Initialize Data
        Account testOwner = new Account("Owner");
        Account testMember1 = new Account("Member1");
        Account testMember2 = new Account("Member2");

        // Create Account Vector array for expected outcome
        Vector<Account> expectedOutcome = new Vector<Account>();
        expectedOutcome.addElement(testOwner);
        expectedOutcome.addElement(testMember1);
        expectedOutcome.addElement(testMember2);

        // Begin Testing
        Group newGroup = new Group(testOwner, "TestGroup");
        newGroup.addGroupMember(testMember1);
        newGroup.addGroupMember(testMember2);

        // Check Results
        assertEquals(expectedOutcome, newGroup.returnMemberList());
    }

    /**
     * This function, testSetOwner, is used to test the Group class's setOwner function
     */
    @Test
    public void testSetOwner() {
        // Initialize Data
        Group newGroup = new Group();

        // Begin Testing
        newGroup.setOwner("newOwner");

        // Check Results
        assertEquals("newOwner", newGroup.returnOwner());
    }

    /**
     * This function, testSetGroupNameAllowed, is used to test the Group class's setGroupName function as the group's creator
     */
    @Test
    public void testSetGroupNameAllowed() {
        // Initialize Data
        Account testOwner = new Account("Owner");

        // Begin Testing
        Group newGroup = new Group(testOwner, "TestGroup");
        newGroup.setGroupName("Success", testOwner.returnName());

        // Check Results
        assertEquals("Success", newGroup.returnGroupName());
    }

    /**
     * This function, testSetGroupNameAllowed, is used to test the Group class's setGroupName function as the group's member
     */
    public void testSetGroupNameDenied() {
        // Initialize Data
        Account testOwner = new Account("Owner");
        Account testMember = new Account("Member");

        // Begin Testing
        Group newGroup = new Group(testOwner, "TestGroup");
        newGroup.setGroupName("Success", testMember.returnName());

        // Check Results; Name should not be changed
        assertNotEquals("Success", newGroup.returnGroupName());
    }

    /**
     * This function, testAddGroupMember is used to test the Group class's addGroupMember function
     */
    @Test
    public void testAddGroupMember() {
        // Initialize Data
        Account testOwner = new Account("Owner");
        Account newMember = new Account("Member");

        // Begin Testing
        Group newGroup = new Group(testOwner, "TestGroup");
        newGroup.addGroupMember(newMember);

        // Check results; Ensure new member has group added to their account list as well
        assertNotEquals(-1, newGroup.returnMemberList().indexOf(newMember));
        assertNotEquals(-1, newMember.returnGroupList().indexOf(newGroup));
    }

    /**
     * This function, testRemoveMember, is used to test the Group's removeGroupMember function
     */
    @Test
    public void testRemoveMember() {
        // Initializes Test Data
        Account testOwner = new Account("Owner");
        Group testGroup = new Group(testOwner, "Test");

        // Builds list to use for checking results
        Vector<Account> testList = new Vector<Account>();
        Account testMember1 = new Account("Test1");
        Account testMember2 = new Account("Test2");
        Account testMember3 = new Account("Test3");

        // Adds groups to Vector array
        testList.addElement(testMember1);
        testList.addElement(testMember2);
        testList.addElement(testMember3);

        //Add members to Group
        testGroup.addGroupMember(testMember1);
        testGroup.addGroupMember(testMember2);
        testGroup.addGroupMember(testMember3);

        // Begins Test
        testGroup.removeGroupMember(testMember2);

        // Checks Results; Must remove testGroup2 from list
        assertNotEquals(testGroup.returnMemberList(), testList);
        assertNotEquals(-1, testGroup.returnMemberList().indexOf(testMember1));
        assertNotEquals(-1, testGroup.returnMemberList().indexOf(testMember3));
        assertEquals(-1, testGroup.returnMemberList().indexOf(testMember2));
    }

}
