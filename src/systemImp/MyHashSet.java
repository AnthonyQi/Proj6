package systemImp;

/**
 * A simple implementation of a HashSet using open addressing with linear probing.<br>
 * This HashSet works specifically with Strings.<br>
 * 
 * It enforces the following constraints:<br>
 * - Strings must not be NULL, no longer than 5 characters, and only use a to z, otherwise an exception is thrown.<br>
 * - A custom hash code function uses hash codes based on the prime number 19.<br>
 * - The initial size of the table is 2, and it dynamically resizes to the next prime when necessary.<br>
 * 
 * <p>
 * The set uses open addressing and linear probing for collision resolution.<br>
 * When the load factor is >= a customized threshold (default is 0.75), the table is rehashed to a larger prime size.</p>
 * 
 * <p>
 * This class ensures that the set operates efficiently and does not allow duplicate entries.
 * </p>
 */
public class MyHashSet {

    private static final String DELETED = "DELETED";  // Sentinel for deleted entries
    private static final String EMPTY = null;         // Sentinel for empty entries
    
    // Array of primes for resizing the hash table
    // These primes are chosen to ensure that the table size grows by a "double prime" strategy.
    private static final int[] PRIMES = {2, 5, 11, 23, 47, 97, 197, 397, 797, 1597};
    
    private String[] table;      // The actual table where the strings are stored
    private int size;            // The current number of elements in the set
    private int primeIndex;      // Index of the current prime number used for table size
    private double loadFactorThreshold;  // The threshold at which to rehash the table
    
    public MyHashSet() {
    	table = new String[2];
    	this.size = 0;
    	this.primeIndex = 0;
    	this.loadFactorThreshold = 0.75;
    }
    
    public void setLoadFactorThreshold(double threshold) {
    	//makes sure the value is within 0.00001 due to comparing doubles
    	boolean ff = Math.abs(0.45 - threshold) < 0.00001;
    	boolean s = Math.abs(0.60 - threshold) < 0.00001;
    	boolean sf = Math.abs(0.75 - threshold) < 0.00001;
    	boolean ef = Math.abs(0.85 - threshold) < 0.00001;
    	//if else to set correct defined value
    	if(ff) {
    	    this.loadFactorThreshold = 0.45;
    	} else if(s) {
    	    this.loadFactorThreshold = 0.60;
    	} else if(sf) {
    	    this.loadFactorThreshold = 0.75;
    	} else if(ef) {
    	    this.loadFactorThreshold = 0.85;
    	} else {
    	    throw new IllegalArgumentException
    	    ("Not a predefined threshold value");
    	}
    }
    
    //calculate the load factor by getting the size instance variable
    //and dividing it with the length of the table[]
    public double getLoadFactor() {
    	double loadFactor = size / (double) table.length;
    	return loadFactor;
    }
    
    public String toString() {
    	StringBuilder log = new StringBuilder();
    	for(int i = 0; i < table.length; i++) {
    		log.append("Index " + i + ": ").append(table[i] + "\n");
    	}
    	return log.toString();
    }
    
    private boolean isValidKey(String key) {
    	//checks if key is null or all and upper case letters
    	//and the key is less than to 5 characters long but not empty
    	if(key == EMPTY || !key.equals(key.toLowerCase())
    			|| key.length() > 5 || key.length() <= 0) {
            return false;
        }
    	//loop through string to check if the characters are 
    	//letters of alphabet
    	for(char c : key.toCharArray()) {
    	    if(c < 'a' || c > 'z') {
    	        return false;
    	    }
    	}
        return true;
    }

    public int myHashCode(String key) {
    	if(!isValidKey(key)) {
    		throw new IllegalArgumentException("Invalid Key");
    	}
    	int prime = 19;
    	//calculate the first one
    	int hash = (int) key.charAt(0);
    	//calculate the rest of the key
    	for(int i = 1; i < key.length(); i++) {
    	    hash = (hash * prime) + (int) key.charAt(i);
    	}
    	return hash;
    }
    
    private int hash(String key) {
    	return myHashCode(key) % table.length;
    }
    
    public void add(String key) {
    	if(!isValidKey(key)) {
    		throw new IllegalArgumentException("Invalid Key");
    	}
        int index = hash(key);
        int originalIndex = index;
        int firstDelete = -1;
    	while(true) {
    		//checks if table is null at index
    		if(table[index] == EMPTY) {
    			//checks if firstDelete index has changed at all
    			if(firstDelete != -1) {
    				//set to first delete
    				table[firstDelete] = key;	
    			} else {
    				//set to index of key if not
    				table[index] = key;
    			}
    			//increase size
    			size++;
    			//break if added
    			break;
    		} 
    		//checks if key is equal to the table at the certain index
    		//returns when no space left
    		else if(key.equals(table[index])){
    			return;
    		} 
    		//checks if the string at index says DELETED
    		//or if firstDelete is not changed
    		else if(table[index].equals(DELETED) && firstDelete == -1) {
    			//sets firstDelete index to the current index
    			firstDelete = index;
    		}
    		//sets the next index to the next indices modulus table.length to
    		//get the correct indices for each string of different lengths
            index = (index + 1) % table.length;
            if(index == originalIndex) {
                throw new IllegalStateException("table is full");
            }
    	}
    	//check for rehash
    	if(getLoadFactor() > loadFactorThreshold) {
            rehash();
        }
    }
    
    public void remove(String key) {
    	if(!isValidKey(key)) {
    		throw new IllegalArgumentException("Invalid Key");
    	}
    	int index = hash(key);
        int originalIndex = index;

        while(table[index] != EMPTY) {
            if(key.equals(table[index])) {
                table[index] = DELETED;
                size--;
                break;
            }
            index = (index + 1) % table.length;
            if(index == originalIndex) {
            	//return once entire table has been looped
                break;
            }
        }
    }


    private void rehash() {
    	//check the next prime. if max then throw exception
    	if(PRIMES[primeIndex] == 1597) {
    		throw new IllegalStateException("Max size");
    	}
    	primeIndex++;
    	int newSize = PRIMES[primeIndex];
    	//create temporary string[]
    	String[] oldTable = table;
    	//switch to the new table first
        table = new String[newSize];
        //reset size because add() will increment it again
        size = 0;
        for(String key : oldTable) {
            if(key != EMPTY && !key.equals(DELETED)) {
            	//now add goes into the new table
                add(key);
            }
        }
    }

    public int size() {
    	return this.size;
    }
    
    public boolean contains(String key) {
    	if(!isValidKey(key)) {
    		throw new IllegalArgumentException("Invalid Key");
    	}
    	int index = hash(key);
        int originalIndex = index;

        while(table[index] != EMPTY) {
            if(key.equals(table[index])) {
                return true;
            }
            index = (index + 1) % table.length;
            if(index == originalIndex) {
            	//return once entire table has been looped
                return false;
            }
        }
        return false;
    }

    public boolean containsAll(String[] keys) {
    	if(keys == null) {
    		return false;
    	}
    	for(String key: keys) {
        	if(!isValidKey(key) || !contains(key)) {
        		return false;
        	}
        }
        return true;
    }
}

