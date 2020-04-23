package com.pizza.android.bas.ObjectsTests;

import java.util.Vector;
import Objects.Account;
import Objects.Group;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Title: AccountTest
 * Description: This JUnit Test Class is used to test the Account class and its functions
 * @author Paige Yon
 */
public class AccountTest {
    /**
     * This function, testAccount, is used to test the constructor and returnName functions of the Account class
     */
    @Test
    public void testAccount() {
        // Initializes Test Data
        Account test = new Account("Success");
        // Checks Results
        assertEquals("Success", test.returnName());
    }

    /**
     * This function, testSetUserName, is used to test the Account's setUserName function
     */
    @Test
    public void testSetUserName() {
        // Initializes Test Data
        Account test = new Account("Fail");
        // Begins Testing
        test.setUserName("Success");

        // Checks Results
        assertEquals("Success", test.returnName());
        assertEquals(0, test.returnGroupList().size());
    }

    /**
     * This function, testReturnGroupList, is used to test the Account's returnGroupList function
     */
    @Test
    public void testReturnGroupList() {
        // Initializes test data
        Account test = new Account("Success");
        Vector<Group> testList = new Vector<Group>();
        testList.addElement(new Group(test, "Test1"));

        // Begins Testing
        test.setGroupList(testList);

        // Checks Results
        assertEquals(testList, test.returnGroupList());
    }

    /**
     * This function, testAddGroup, is used to test the Account's addGroup function
     */
    @Test
    public void testAddGroup() {
        // Initializes Test Data
        Account test = new Account("Success");
        Group testGroup = new Group();
        Vector<Group> testList = new Vector<Group>();
        testList.addElement(testGroup);

        // Begins Testing
        test.addGroup(testGroup);

        // Checks Results
        assertEquals(testList, test.returnGroupList());
    }

    /**
     * This function, testRemoveGroup, is used to test the Account's removeGroup function
     */
    @Test
    public void testRemoveGroup() {
        // Initializes Test Data
        Account test = new Account("Success");
        Vector<Group> testList = new Vector<Group>();
        Group testGroup1 = new Group(test, "Test1");
        Group testGroup2 = new Group(test, "Test2");
        Group testGroup3 = new Group(test, "Test3");

        // Adds groups to Vector array
        testList.addElement(testGroup1);
        testList.addElement(testGroup2);
        testList.addElement(testGroup3);

        // Begins Test
        test.setGroupList(testList);
        test.removeGroup(testGroup2);

        // Checks Results; Must remove testGroup2 from list
        assertNotEquals(test.returnGroupList(), testList);
        assertNotEquals(-1, test.returnGroupList().indexOf(testGroup1));
        assertNotEquals(-1, test.returnGroupList().indexOf(testGroup3));
        assertEquals(-1, test.returnGroupList().indexOf(testGroup2));
    }

}