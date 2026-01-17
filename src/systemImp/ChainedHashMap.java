package systemImp;

import java.util.LinkedList; 
import java.util.ArrayList; //ONLY use for return value of getValues method
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A HashMap implementation using separate chaining with LinkedLists.<br>
 * 
 * - Stores Integer keys and String values. <br>
 * - Uses a hash function with compression: ((A * key + B) % P) % table.length<br>
 * - Rehashes when the average chain length exceeds a threshold.<br>
 * - Does not allow duplicate keys (updates value instead), and keys must be 4-digit integers from 1000 (inclusive) to 9999 (inclusive).<br>
 * - Does not allows null values.<br>
 * - Uses an initial size of 2 to encourage collisions.<br>
 * - Since keys are 4-digit numbers,  integer overflow is not a concern.<br>
 */
public class ChainedHashMap implements Iterable<ChainedHashMap.Entry> {
    
    /** A large prime number for hashing */
    private static final int P = 104729;
    /** Large prime multiplier for hashing */
    private static final int A = 2347;
    /** Large prime offset for hashing */
    private static final int B = 7919;

    /** Initial size of the hash table */
    private static final int INITIAL_SIZE = 2;
    /** Threshold for rehashing: when avg chain length exceeds this, we rehash */
    private static final int RESIZE_THRESHOLD = 3;
    /** Prime sizes for resizing */
    private static final int[] PRIMES = {2, 5, 11, 23, 47, 97, 197, 397};

    /** The hash table, where each index contains a linked list of entries */
    private LinkedList<Entry>[] table;
    /** The number of key-value pairs stored */
    private int size;
    /** The index of the current prime in PRIMES */
    private int primeIndex;

    /**
     * Entry class for storing key-value pairs in the linked list.<br>
     * Do NOT MAKE ANY CHANGES to  public static class Entry
     */
    public static class Entry {
        int key;
        String value;
        
        Entry(int key, String value) {
            this.key = key;
            this.value = value;
        }
        public int getKey() {  //used for testing only. NOT needed in code you write
            return key;
        }
    }
    
    //instantiates ChainHashMap object
    @SuppressWarnings("unchecked")
	public ChainedHashMap() {
    	table = (LinkedList<Entry>[]) new LinkedList[INITIAL_SIZE];
    	this.primeIndex = 0;
    	this.size = 0;
    }
    
    //private method that calculates the hash
    private int hash(int key) {
    	int hashCode = ((A * key + B) % P) % table.length;
    	return hashCode;
    }
    
    //O(1) time complexity
    public int getSize() {
    	return this.size;
    }
    
    //O(1) time complexity
    public int getTableLength() {
    	return table.length;
    }
    
    //O(1) time complexity
    //just returns true or false based on if both statements are true or false
    private boolean isValidKey(int key) {
    	return key <= 9999 && key >= 1000;
    }
    
    public void put(int key, String value){
        //validate the key and value
        if(!isValidKey(key)){
            throw new IllegalArgumentException("Key must be 4 digits long");
        }
        if(value == null){
            throw new IllegalArgumentException("No null");
        }
        //compute index using the hash function
        int index = hash(key);
        //initialize if index is empty
        if(table[index] == null){
        	table[index] = new LinkedList<>();
        }
        //search for an existing entry with the same key
        for(Entry e : table[index]){
        	if(e.key == key){
        		//key exists, update the value
        		e.value = value;
        		return;
            }
        }
        //key doesn't exist, add a new entry
        table[index].add(new Entry(key, value));
        size++;
        //check if resizing is needed
        if((double)size / table.length > RESIZE_THRESHOLD){
            rehash();
        }
    }

    
    public String get(int key){
        //validate the key
        if(!isValidKey(key)){
            throw new IllegalArgumentException("Key must be 4 digits long");
        }
        //compute index using the hash function
        int index = hash(key);
        //searching through bucket
        if(table[index] != null){
            for(Entry e : table[index]){
                if(e.key == key){
                    //found the entry, return its value
                    return e.value;
                }
            }
        }
        //key not found, return null
        return null;
    }
    
    //checks first for valid key, to then check the specific key the 
    //linked list is associated with to search through that
    public boolean containsKey(int key) {
        if(!isValidKey(key)) {
            throw new IllegalArgumentException("Key must be 4 digits long");
        }
        //compute index using the hash function
        int index = hash(key);
        //searching through bucket
        if(table[index] != null) {
            for(Entry e : table[index]) {
                if(e.key == key) {
                	//if key is found, then return true
                    return true;
                }
            }
        }
        return false;
    }
    
    public ArrayList<Integer> getValues(String target) {
    	//makes sure the target value isn't null
    	if(target == null) {
    		throw new IllegalArgumentException("No null");
    	}
    	//creates an array list of integers that will hold indices
    	ArrayList<Integer> keys = new ArrayList<Integer>();
    	for(int i = 0; i < table.length; i++) {
    		for(Entry e: table[i]) {
    			if(e.value.equals(target)) {
    				keys.add(e.key);
    			}
    		}
    	}
    	return keys;
    }
    
    public void remove(int key) {
    	//checks key
    	if(!isValidKey(key)) {
    		throw new IllegalArgumentException("Key must be 4 digits long");
    	}
    	//finds index from hash method
    	int index = hash(key);
    	//iterator to loop through
    	Iterator<Entry> iter = table[index].iterator();
    	while(iter.hasNext()) {
    		//finds out if the key is equal
    		Entry e = iter.next();
    		if(e.key == key) {
    			//calls iterator remove method if true
    			iter.remove();
    			size--;
    			//decrease size and break
    			break;
    		}
    	}
    }

    
    @SuppressWarnings("unchecked")
	private void rehash() {
    	if(PRIMES[primeIndex] == 397) {
    		return;
    	}
    	primeIndex+=1;
    	int newSize = PRIMES[primeIndex];
        LinkedList<Entry>[] newTable = 
        		(LinkedList<Entry>[]) new LinkedList[newSize];
        //rehash all existing entries and place them in the new table
        for(int i = 0; i < table.length; i++) {
            if(table[i] != null) {
                //iterate through the linked list at index i
                for(Entry e : table[i]) {
                	int newIndex = ((A * e.key + B) % P) % newSize;
                    if(newTable[newIndex] == null) {
                        newTable[newIndex] = new LinkedList<>();
                    }
                    newTable[newIndex].add(e);
                }
            }
        }
        table = newTable;
    }
    
    public String toString() {
    	StringBuilder log = new StringBuilder();
    	//loops through table indices
    	for(int i = 0; i < table.length; i++) {
    		//appends regardless if its empty or null
            log.append(i).append(" -> ");
            if(table[i] != null && !table[i].isEmpty()) {
                Iterator<Entry> iter = table[i].iterator();
                while(iter.hasNext()) {
                	Entry entry = iter.next();
                    log.append("(").append(entry.key).append(", ").
                    	append(entry.value).append(") ");
                }
                //creates new line
            }
            log.append("\n");
    	}
    	return log.toString();
    }

	@Override
	public Iterator<Entry> iterator() {
		return new Iterator<Entry>() {
			int bucketIndex = 0;
			Iterator<Entry> bIter = null;
			//loops the linkedList iterator while making sure that the index
			//of the table is still in bounds
			public boolean hasNext() {
				while((bIter == null || !bIter.hasNext()) 
						&& bucketIndex < table.length) {
					//create a new bucket equal to the linked list at the index
			        LinkedList<Entry> bucket = table[bucketIndex++];
			        //next checks if the bucket isn't null or isn't empty
			        if(bucket != null && !bucket.isEmpty()) {
			            bIter = bucket.iterator();
			        }
			    }
				//returns true or false based on if the iterator is null or has
				//next
			    return bIter != null && bIter.hasNext();
			}
			//calls hasNext
			public Entry next() {
				if(!hasNext()) {
					throw new NoSuchElementException("No more elements");
				}
				//returns the current and moves to next
				return bIter.next();
			}
		};
	}
}
