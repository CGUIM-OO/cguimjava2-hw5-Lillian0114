/**
 * Description: First, writing the class, Card, and int the field rank.
 * Set the enum type, Suit.
 * Furthermore, writing the constructors to int rank and Suit the suit.  
 */

public class Card {
	enum Suit{Club,Diamond,Heart,Spade};
	private Suit suit; //Definition: 1~4, Clubs=1, Diamonds=2, Hearts=3, Spades=4
	private int rank; //1~13
	/**
	 * @param s suit
	 * @param r rank
	 */

	public Card(Suit s,int r){
		suit=s;
		rank=r;
	}	
	
	/**
	 * Next, printCard, getSuit and getRank is the method.
	 * Create the array to present the rank and enum the Suit conveniently. 
	 * The first index of array is 0, so we should subtract 1 when we want to get the String from array.
	 * In the end println the card.
	 */
	public void printCard(){

		if(rank==1)
			System.out.println(suit.toString()+" Ace");
		else
		System.out.println(suit.toString()+" "+rank);
	}
	
	public Suit getSuit(){
		return suit;
	}
	public int getRank(){
		return rank;
	}
}


