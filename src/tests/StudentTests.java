package tests;

import static org.junit.Assert.*;
//i saw an earlier piazza post that said it was fine to import any java.util.*
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import systemImp.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class StudentTests {
	
	//Testing ChainedHashMap Class
	
	@Test
	public void testingChainHashMapPutAndGetAndRehash() {
		ChainedHashMap map = new ChainedHashMap();
		//adding some items into the list
		map.put(1000, "1");
		map.put(1000, "!");
		map.put(1000, "q");
		map.put(1000, "10");
		//trying to getting something in a null linked list
		String nullResult = map.get(2000);
		assertEquals(null, nullResult);
		map.put(2000, "heheheha");
		map.put(2000, ":)");
		assertEquals(2, map.getSize());
		//testing if the get method gets the latest one
		String result1 = map.get(1000);
		String answer1 = "10";
		assertEquals(answer1, result1);
		//testing with invalid keys
		int invKey = 10000;
		try {
			map.put(invKey, "Not Valid");
		} catch(Exception e) {
			String err1 = "Key must be 4 digits long";
			assertEquals(err1, e.getMessage());
		}
		//testing with null value
		try  {
			map.put(1000, null);
		} catch(Exception e) {
			String err2 = "No null";
			assertEquals(err2, e.getMessage());
		}
		
		//testing the get if the key doesn't exist
		String result = map.get(1001);
		assertEquals(null, result);
		//testing when resizing/rehashing
		assertEquals(2, map.getSize());
		//resize happens when size/map.length > 3
		//in this case, when we have 6+ items/ map length of 2
		map.put(2001, "old");
		map.put(3002, "1000?");
		map.put(5000, "wow");
		map.put(7000, "this is crazy");
		assertEquals(6, map.getSize());
		assertEquals(2, map.getTableLength());
		//one more
		map.put(9001, "over 9000");
		assertEquals(7, map.getSize());
		assertEquals(5, map.getTableLength());
		//testing rehash
		ChainedHashMap chain = new ChainedHashMap();
		//the first put should map to index 0 before rehash
		//1001 should hash to 53228 % 2 = 0(so index 0), and after rehash
		//1001 should hash to 53228 % 5 = 3 (so index 3)
		chain.put(1001, "1");
		chain.put(2001, "2");
		chain.put(3001, "3");
		chain.put(4001, "4");
		chain.put(5001, "5");
		chain.put(6001, "6");
		System.out.print(chain.toString() + "\n");
		assertEquals(2, chain.getTableLength());
		//where rehashing should trigger overload
		chain.put(7001, "7");
		System.out.print(chain.toString());
		assertEquals(5, chain.getTableLength());
	}
	
	@Test
	public void testingContainsKey() {
		ChainedHashMap map = new ChainedHashMap();
		//testing invalid key
		try {
			map.containsKey(0);
		} catch(Exception e) {
			String err1 = "Key must be 4 digits long";
			assertEquals(err1, e.getMessage());
		}
		map.put(1000, "cheese");
		//testing with valid key
		assertTrue(map.containsKey(1000));
		//testing valid key that doesn't exist
		assertFalse(map.containsKey(1001));
		//testing with valid key not in the same index 
		//of an existing LinkedList
		assertFalse(map.containsKey(2000));
	}
	
	@Test
	public void testingGetValues() {
		ChainedHashMap map = new ChainedHashMap();
		//testing null target
		try {
			map.getValues(null);
		} catch(Exception e) {
			String err1 = "No null";
			assertEquals(err1, e.getMessage());
		}
		//adding some items
		map.put(1000, "dog");
		map.put(2000, "dog");
		map.put(3000, "cat");
		map.put(4000, "cat");
		map.put(5000, "dog");
		map.put(9999, "dat");
		assertEquals(6, map.getSize());
		//testing with one index
		ArrayList<Integer> ans1 = new ArrayList<>();
		ans1.add(9999);
		assertEquals(ans1, map.getValues("dat"));
		//testing with multiple indices
		ArrayList<Integer> ans2 = new ArrayList<>();
		ans2.add(1000);
		ans2.add(2000);
		ans2.add(5000);
		assertEquals(ans2, map.getValues("dog"));
		//testing when no elements match
		ArrayList<Integer> ans3 = new ArrayList<>();
		assertEquals(ans3, map.getValues("heheheha"));
	}
	
	@Test
	public void testingRemove() {
		ChainedHashMap map = new ChainedHashMap();
		//testing normal remove
		map.put(1000, "lol");
		map.remove(1000);
		assertFalse(map.containsKey(1000));
		//testing invalid key
		try {
			map.remove(10);
		} catch(Exception e) {
			String err1 = "Key must be 4 digits long";
			assertEquals(err1, e.getMessage());
		}
		//testing if size correctly updates
		map.put(1000, "T-T");
		map.put(2000, "T=T");
		assertEquals(2, map.getSize());
		map.remove(1000);
		map.remove(2000);
		assertEquals(0, map.getSize());
		//remove with multiple of the same key (should remove the key 1000)
		map.put(1000, "1");
		map.put(1000, "2");
		map.put(1000, "3");
		map.remove(1000);
		assertFalse(map.containsKey(1000));
	}
	
	@Test
	public void testingToStringChainedHashMap() {
		ChainedHashMap map = new ChainedHashMap();
		//testing with empty
		String ans1 = "0 -> \n"
				+ "1 -> \n";
		String res1 = map.toString();
		assertEquals(ans1, res1);
		map.put(1000, "-.-");
		map.put(1001, "-.-");
		map.put(1002, "-.-");
		//testing before rehash
		String ans2 = "0 -> (1001, -.-) \n"
				+ "1 -> (1000, -.-) (1002, -.-) \n";
		String res2 = map.toString();
		assertEquals(ans2, res2);
		//testing toString after rehash
		map.put(1003, "-.-");
		map.put(1004, "-.-");
		map.put(1005, "-.-");
		map.put(1006, "-.-");
		map.put(1007, "-.-");
		//elements added before the rehash have been rehashed with different
		//table.length to find different index
		String ans3 = "0 -> (1002, -.-) (1007, -.-) \n"
				+ "1 -> (1005, -.-) (1000, -.-) \n"
				+ "2 -> (1003, -.-) \n"
				+ "3 -> (1001, -.-) (1006, -.-) \n"
				+ "4 -> (1004, -.-) \n";
		String res3 = map.toString();
		assertEquals(ans3, res3);
	}
	
	@Test
	public void testingIterator() {
		ChainedHashMap map = new ChainedHashMap();

		//testing with empty map
		Iterator<ChainedHashMap.Entry> emptyIter = map.iterator();
		assertFalse(emptyIter.hasNext());

		//testing with one entry
		map.put(1000, "1");
		Iterator<ChainedHashMap.Entry> singleIter = map.iterator();
		assertTrue(singleIter.hasNext());
		singleIter.next();
		assertFalse(singleIter.hasNext());

		//testing with duplicate key (should only be one in the map)
		map.put(1000, "heheheha");
		Iterator<ChainedHashMap.Entry> dupIter = map.iterator();
		int dupCount = 0;
		while (dupIter.hasNext()) {
			dupIter.next();
			dupCount++;
		}
		assertEquals(1, dupCount);

		//add more entries and remove one
		map.put(2000, "2");
		map.put(3000, "3");
		//remove existing key
		map.remove(1000); 
		Iterator<ChainedHashMap.Entry> multiIter = map.iterator();
		int multiCount = 0;
		while (multiIter.hasNext()) {
			multiIter.next();
			multiCount++;
		}
		//should only have 2000 and 3000
		assertEquals(2, multiCount); 

		//make sure exception is thrown when calling next too many times
		Iterator<ChainedHashMap.Entry> overIter = map.iterator();
		while (overIter.hasNext()) {
			overIter.next();
		}
		try {
			overIter.next();
		} catch (Exception e) {
			assertEquals("No more elements", e.getMessage());
		}
	}
	
	//Testing MyHashSet Class
	
	@Test
    public void testConstructor() {
        //test default constructor
        MyHashSet set = new MyHashSet();
        //should start empty
        System.out.println(set.size());
        assertEquals(0, set.size());
        System.out.println(set.toString());
        //should be null
        String ans1 = "Index 0: null\n"
        		+ "Index 1: null\n";
        assertEquals(ans1, set.toString());

        /*since it is a default constructor with no parameters, there won't
         * be any issues with incorrect parameters.
         *testing invalid keys during construction
         */
    	String err = "Invalid Key";
        try {
            set.add("ABCDE");
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }

        try {
            set.add("HELLO");
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }

        try {
            set.add(null);
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }
    }

    @Test
    public void testSetLoadFactorThresholdAndGetLoadFactor() {
        MyHashSet set = new MyHashSet();
        //testing each predefined threshold
        set.setLoadFactorThreshold(0.45);
        set.add("qwert");
        set.add("trewq");
        System.out.print(set.getLoadFactor() + "\n");
        //should be right below 0.45 at 0.4
        //as soon as I add another element, it immediately rehashes
        assertTrue(set.getLoadFactor() < 0.45);
        set.add("asdfg");
        //past the threshold here
        assertEquals(3, set.size());
        //adding more elements before testing for new threshold
        set.setLoadFactorThreshold(0.60);
        set.add("angel");
        set.add("bagel");
        //at the old threshold, it would have reset at adding bagel
        set.add("truce");
        //i believe this is the last element added before rehashing
        System.out.print(set.getLoadFactor() + "\n");
        assertTrue(set.getLoadFactor() < 0.6);

        set.setLoadFactorThreshold(0.75);
        set.add("proud");
        //this is 0.72727272....
        set.add("black");
        System.out.print(set.getLoadFactor() + "\n");
        assertTrue(set.getLoadFactor() < 0.75);

        set.setLoadFactorThreshold(0.85);
        //this is 0.81818181
        set.add("chain");
        System.out.print(set.getLoadFactor() + "\n");
        assertTrue(set.getLoadFactor() < 0.85);
        //adding another element resets the threshold
        set.add("bacon");
        System.out.print(set.getLoadFactor() + "\n");
        //test invalid threshold
        try {
            set.setLoadFactorThreshold(0.5);
        } catch (Exception e) {
        	String err = "Not a predefined threshold value";
        	assertEquals(err, e.getMessage());
        }
        //testing with a double with a small difference
        set.setLoadFactorThreshold(0.45000001);
    }
	
    @Test
    public void testingToStringMyHashSet() {
        MyHashSet set = new MyHashSet();
        //set threshold to 0.85
        set.setLoadFactorThreshold(0.85);
        set.add("flint");
        String res1 = set.toString();
        String ans1 = "Index 0: null\n"
        		+ "Index 1: flint\n";
        assertEquals(ans1, res1);
        set.add("anndd");
        set.add("steel");

        //test toString with first rehash
        String res2 = set.toString();
        System.out.print(res2);
        String ans2 = "Index 0: flint\n"
        		+ "Index 1: null\n"
        		+ "Index 2: anndd\n"
        		+ "Index 3: steel\n"
        		+ "Index 4: null\n";
        assertEquals(ans2, res2);

        //test empty set
        MyHashSet eSet = new MyHashSet();
        String res3 = eSet.toString();
        String ans3 = "Index 0: null\n"
        		+ "Index 1: null\n";
        assertEquals(ans3, res3);
    }
    
    @Test
    public void testHashCode() {
        MyHashSet set = new MyHashSet();

        //testing two valid keys, inverses of each other
        //if they output the same code
        int hash1 = set.myHashCode("apple");
        int hash2 = set.myHashCode("elppa");
        //they should be different but only slightly
        assertTrue(hash1 != hash2);
        /*should all follow the ((((s0*19)+s1)*19)....+s_n) formula
         * no matter how long the string is unless it is more than 5 characters
         * long or empty
         */
        int hash3 = set.myHashCode("abc");
        //a = 97, b = 98, c = 99
        //should end up being (((97*19)+98)*19+99) = 36978
        int ans = 36978;
        assertEquals(ans, hash3);
        //testing invalid keys
        //invalid keys have already been tested, but testing here might be
        //redundant but checks for method faultiness
    	String err = "Invalid Key";
        try {
            set.myHashCode("APPLE");
        } catch (Exception e) {
        	assertEquals(err, e.getMessage());
        }
        
        //testing out an empty key
        try {
            set.myHashCode("");
        } catch (Exception e) {
        	assertEquals(err, e.getMessage());
        }
    }
    
	@Test
    public void testAddAndRemove() {
        MyHashSet set = new MyHashSet();
        //testing with valid keys
        set.add("apple");
        //index 1 at table length of 5
        set.add("elppa");
        set.remove("elppa");
        String ans1 = "Index 0: apple\n"
        		+ "Index 1: DELETED\n"
        		+ "Index 2: null\n"
        		+ "Index 3: null\n"
        		+ "Index 4: null\n";
        String res1 = set.toString();
        assertEquals(ans1, res1);
        //adding some elements, where night should replace the deleted slot
        set.add("chick");
        set.add("night");
        String ans2 = "Index 0: apple\n"
        		+ "Index 1: night\n"
        		+ "Index 2: null\n"
        		+ "Index 3: chick\n"
        		+ "Index 4: null\n";
        String res2 = set.toString();
        assertEquals(ans2, res2);
        //check size and contains after adding
        assertEquals(3, set.size());
        assertTrue(set.contains("apple"));
        assertTrue(set.contains("chick"));
        assertTrue(set.contains("night"));
        //remove one and check
        set.remove("chick");
        assertFalse(set.contains("chick"));
        assertEquals(2, set.size());

        //remove non-existent element
        set.remove("grape");
        assertEquals(2, set.size());
		//with less than 5 letters
        
        set.add("wow");
        //System.out.print(set.toString());
        assertEquals(3, set.size());
        set.remove("wow");
        assertEquals(2, set.size());
        //checking if deleted works and add works
        //System.out.print(set.toString());
        String ans3 = "Index 0: apple\n"
        		+ "Index 1: night\n"
        		+ "Index 2: DELETED\n" //this deleted is "wow"
        		+ "Index 3: DELETED\n" //this deleted is "chick"
        		+ "Index 4: null\n";
        String res3 = set.toString();
        assertEquals(ans3, res3);
        //testing if add goes in the right place
        //adding/removing with invalid keys
        //null key
    	String err = "Invalid Key";
        try {
        	set.add(null);
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }
        //with a mix of special characters
        try {
        	set.add("q.q.q");
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }
        //with a mix of capital letters
        try {
        	set.add("WoWoW");
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }
        //with only special characters
        try {
        	set.add("._._.");
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }
        //with all capital letters
        try {
        	set.add("WWWWW");
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }
        //testing when greater than 5
        try {
        	set.add("eleven");
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }
        //testing with empty
        try {
        	set.add("");
        } catch(Exception e) {
        	assertEquals(err, e.getMessage());
        }
    }

    @Test
    public void testContainsAndContainsAll() {
        MyHashSet set = new MyHashSet();
        //testing normal case
        set.add("dog");
        set.add("cat");
        set.add("apple");

        //test contains
        assertTrue(set.contains("dog"));
        assertTrue(set.contains("cat"));
        assertTrue(set.contains("apple"));
        assertFalse(set.contains("bird"));

        //test containsAll
        //testing valid list of keys
        String[] keys1 = {"dog", "cat", "apple"};
        assertTrue(set.containsAll(keys1));
        

        //testing with one invalid
        String[] keys2 = {"apple", "cat", "dog", "elephant"};
        assertFalse(set.containsAll(keys2));

        //testing with a whole list of valid keys but nonexistent in the list
        String[] keys3 = {"wow", "doggy", "kitty", "zoo"};
        assertFalse(set.containsAll(keys3));
        //test null array for containsAll
        assertFalse(set.containsAll(null));
    }
}