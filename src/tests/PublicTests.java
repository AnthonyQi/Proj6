package tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import systemImp.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class PublicTests {
	
	//-------------ChainedHashMap Tests 
	
	/**
	 * Tests the put, containsKey, and toString methods of ChainedHashMap.
	 */
	@Test
	public void testPutContainsKeyToString() {
	    // Create a ChainedHashMap to test
	    ChainedHashMap map = new ChainedHashMap();

	    /**
	     * Test put method:
	     * Adds key-value pairs (1="Apple", 2="Banana", 3="Cherry", 4="Date").
	     * Ensures keys are correctly inserted into the map.
	     */
	    map.put(1000, "Apple");
	    map.put(2000, "Banana");
	    map.put(3000, "Cherry");
	    map.put(4000, "Date");

	    /**
	     * Test containsKey method:
	     * Verifies that the map correctly recognizes existing keys.
	     * Ensures that a non-existent key returns false.
	     */
	    assertTrue("Map should contain key 1000", map.containsKey(1000));
	    assertTrue("Map should contain key 2000", map.containsKey(2000));
	    assertTrue("Map should contain key 3000", map.containsKey(3000));
	    assertTrue("Map should contain key 4000", map.containsKey(4000));
	    assertFalse("Map should not contain key 5000", map.containsKey(5000));

	    /**
	     * Test toString method:
	     * Captures the string representation of the map.
	     * Compares it with an expected output stored in "pubTest02.txt".
	     */
	    String result = map.toString() + "\n";
	    assertTrue(TestsSupport.isCorrect("pubTest01.txt", result));
	}

    
    
    @Test
    public void testPutWithToStringEvery10Entries() {
        // Create a ChainedHashMap to test
        ChainedHashMap map = new ChainedHashMap();
        StringBuilder log = new StringBuilder();

        /**
         * Test put method with 100 entries:
         * Inserts 100 key-value pairs (keys: 1000-1099, values: "Value1000"-"Value1099").
         * Every 10 insertions, calls toString and appends it to log with "-----".
         */
        for (int i = 0; i < 100; i++) {
            int key = 1000 + i; // Generate a 4-digit key
            map.put(key, "Value" + key);

            // Every 10 insertions, log the map's state
            if ((i + 1) % 10 == 0) {
                log.append(map.toString()).append("\n-----------------------------\n");
            }
        }

        /**
         * Validate the final log output:
         * Ensures that intermediate states are correctly logged every 10 insertions.
         */
        assertTrue(TestsSupport.isCorrect("pubTest02.txt", log.toString()));
    }

    
    
    

    @Test
    public void testIterator() {
        // Create a ChainedHashMap to test
        ChainedHashMap map = new ChainedHashMap();

        /**
         * Test iterator on an empty map:
         * Ensures that an empty map's iterator has no next element.
         */
        Iterator<ChainedHashMap.Entry> iterEmpty = map.iterator();
        assertFalse("Iterator should have no elements in an empty map", iterEmpty.hasNext());

        /**
         * Add key-value pairs:
         * Ensures iterator can traverse all elements in the map.
         */
        map.put(1000, "Apple");
        map.put(2000, "Banana");
        map.put(3000, "Cherry");
        map.put(4000, "Date");

        /**
         * Test iterator traversal:
         * Checks that all elements can be iterated over.
         */
        Iterator<ChainedHashMap.Entry> iter = map.iterator();
        HashSet<Integer> seenKeys = new HashSet<>();
        
        while (iter.hasNext()) {
            ChainedHashMap.Entry entry = iter.next();
            seenKeys.add(entry.getKey());
        }

        // Verify all keys are iterated over exactly once
        assertEquals("Iterator should traverse all keys", Set.of(1000, 2000, 3000, 4000), seenKeys);

        /**
         * Test iterator throws NoSuchElementException when exceeding elements.
         */
        try {
            iter.next();
            fail("Calling next() beyond elements should throw NoSuchElementException");
        } catch (NoSuchElementException expected) {
            // Expected behavior
        }
    }

    @Test
    public void testChainedHashMapOperations() {
        // Create a ChainedHashMap to test
        ChainedHashMap map = new ChainedHashMap();

        /**
         * Test put method:
         * Adds 10 key-value pairs.
         */
        map.put(1000, "Apple");
        map.put(2000, "Banana");
        map.put(3000, "Cherry");
        map.put(4000, "Date");
        map.put(5000, "Elderberry");
        map.put(6000, "Fig");
        map.put(7000, "Grape");
        map.put(8000, "Honeydew");
        map.put(9000, "Iced Tea");
        map.put(9999, "Jackfruit");

        // Ensure all elements are added
        assertEquals("Map should have 10 elements", 10, map.getSize());

        /**
         * Test get method:
         * Ensures values are correctly retrieved.
         */
        assertEquals("Value for key 1000 should be Apple", "Apple", map.get(1000));
        assertEquals("Value for key 2000 should be Banana", "Banana", map.get(2000));
        assertEquals("Value for key 3000 should be Cherry", "Cherry", map.get(3000));
        assertEquals("Value for key 9999 should be Jackfruit", "Jackfruit", map.get(9999));

        // Ensure get returns null for non-existent key
        assertNull("Key 6789 should not exist", map.get(6789));

        /**
         * Test containsKey method:
         * Ensures map correctly recognizes existing and non-existing keys.
         */
        assertTrue("Map should contain key 4000", map.containsKey(4000));
        assertTrue("Map should contain key 9000", map.containsKey(9000));
        assertFalse("Map should not contain key 6789", map.containsKey(6789));

  
        /**
         * Test remove method:
         * Removes an element and verifies the map updates correctly.
         */
        map.remove(5000);
        assertFalse("Key 5000 should be removed", map.containsKey(5000));
        assertNull("Key 5000 should return null after removal", map.get(5000));

        // Ensure size is updated correctly after removal
        assertEquals("Map should have 10 - 1 = 9 elements", 9, map.getSize());

        /**
         * Test toString method:
         * Compares output with expected results.
         */
        String result = map.toString() + "\n";
        //System.out.print(result);
        assertTrue("toString output should match expected result", TestsSupport.isCorrect("pubTest03.txt", result));
    }

	//-------------HashSet Tests 
    
    @Test
    public void testPutAndHashCodeToString() {
        // Create a  MyHashSet to test
        MyHashSet set = new MyHashSet();

        /**
         * Test add method:
         * Adds 10 words with 4 to 5 letters to the set.
         * Ensures that all elements are correctly inserted into the set.
         */
        set.add("apple");
        set.add("banjo");
        set.add("cat");
        set.add("date");
        set.add("eagle");
        set.add("fish");
        set.add("goose");
        set.add("hawk");
        set.add("ibis");
        set.add("joe");

        /**
         * Test myHashCode method:
         * Calls myHashCode on a couple of words and outputs the hash codes.
         */
        StringBuilder sb = new StringBuilder();
        sb.append("Hash Code for 'Apple': ").append(set.myHashCode("apple")).append("\n");
        sb.append("Hash Code for 'Banjo': ").append(set.myHashCode("banjo")).append("\n");

        /**
         * Test toString method:
         * Captures the string representation of the set.
         * Appends the hash codes of a couple of elements.
         */
        String result = set.toString() + "\n" + sb.toString();
        System.out.print(result);

        // Write the result to a file for validation
        assertTrue(TestsSupport.isCorrect("pubTest04.txt", result));
    }
    
    @Test
    public void testInsertRemoveContainsAndToString() {
        // Create a MyHashSet to test
        MyHashSet set = new MyHashSet();

        /**
         * Test add method:
         * Adds a few words to the set.
         * Ensures that the elements are correctly inserted into the set.
         */
        set.add("apple");
        set.add("bat");
        set.add("cake");

        /**
         * Test contains method:
         * Verifies that the set contains the inserted elements.
         * Ensures that non-inserted elements return false.
         */
        assertTrue("Set should contain 'apple'", set.contains("apple"));
        assertTrue("Set should contain 'bat'", set.contains("bat"));
        assertTrue("Set should contain 'cake'", set.contains("cake"));
        assertFalse("Set should not contain 'dog'", set.contains("dog"));

        /**
         * Test toString before removal:
         * Captures the string representation of the set before removing elements.
         */
        String beforeRemove = set.toString() + "\n";
        System.out.print(beforeRemove);
        /**
         * Test remove method:
         * Removes some elements from the set and verifies the removal.
         */
        set.remove("bat");
        set.remove("cake");

        /**
         * Test toString after removal:
         * Captures the string representation of the set after removing elements.
         */
        String afterRemove = set.toString() + "\n";
        System.out.print(afterRemove);
        /**
         * Test contains method after removal:
         * Ensures that removed elements no longer exist in the set.
         * Verifies that other elements are still present.
         */
        assertFalse("Set should not contain 'bat' after removal", set.contains("bat"));
        assertFalse("Set should not contain 'cake' after removal", set.contains("cake"));
        assertTrue("Set should still contain 'apple'", set.contains("apple"));

        set.add("walk");
        set.add("play");
        String afterAdd = set.toString() + "\n";
        System.out.print(afterAdd);

        /**
         * Write the results to a file for validation:
         * Write both before and after states of the set, along with other test results.
         */
        String result = "Before Removal:\n" + beforeRemove + "After Removal:\n" + afterRemove;
        result += "After Add:\n" + afterAdd;
        System.out.print(result);
        assertTrue(TestsSupport.isCorrect("pubTest05.txt", result));
    }

	 
}

	
