package com.qualiycode.utils.pair;

/**
 * This is a generic pair class which contains two parameters
 * 
 * @author Eli Rozenfeld
 *
 * @param <F> Type of first parameter
 * @param <S> Type of second parameter
 */
public class Pair<F, S> {
    private F first; //first member of pair
    private S second; //second member of pair

    public Pair() {}
    
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @param first - the first object in the pair
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * @param second - the second object in the pair
     */
    public void setSecond(S second) {
        this.second = second;
    }

    /**
     * @return the first object in the pair
     */
    public F getFirst() {
        return first;
    }

    /**
     * @return the second object in the pair
     */
    public S getSecond() {
        return second;
    }
    
    /**
     * This function compair this Pair to another Pair
     * @param otherPair - the Pair to compare with this one
     * @return true if this Pair objects equals to the otherPair objects
     */
    public boolean isPairsTheSame(Pair<F, S> otherPair){
    	return first.equals(otherPair.getFirst()) && second.equals(otherPair.second);
    }
    
    @Override
	public String toString(){
    	return first.toString() + " " + second.toString();
    }
}