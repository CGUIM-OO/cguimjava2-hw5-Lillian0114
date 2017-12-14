import java.util.ArrayList;
import java.util.Random;

public class Deck {
	private ArrayList<Card> cards;
	public int nUsed;
	public ArrayList<Card> usedCard;
	private ArrayList<Card> openCard;
	
	/**
	 * @param nDeck how many deck you input
	 * 
	 * Description:
	 * Create arraylists, usedCard and cards, that we can use conveniently after.
	 * First, we can use nested loops to put the card into "cards".
	 * quantity is the count of Deck(nDeck).
	 * x  is the code of rank. Use enhanced for loop to present the suit.
	 * y  is the code of suit. Use for loop to present the rank.
	 * When the quantity is 1 and the program will run x = 1, and then the program will run y=1 from y=13 and so on the next program.
	 * card is the field to add card into "cards".
	 * Last, we should call the shuffle method to make every card is be shaffled.
	 */
	public Deck(int nDeck){
		usedCard=new ArrayList<Card>();
		cards=new ArrayList<Card>();
		openCard=new ArrayList<Card>();
		for(int quantity=1; quantity<=nDeck; quantity++){
			for(Card.Suit x : Card.Suit.values()){
				for(int y=1; y<=13; y++){
					Card card = new Card(x,y);
					cards.add(card); 
				}
			} 
		}
		shuffle();
	}	
	
	/**
	 * Afterward, we can do the printDeck method. Create "allcards" and get the card from "cards". 
	 * In the end, we can reuse the printcard method. 
	 * Create the field, allcards, that let all of the Card be printed.
	 */
	public void printDeck(){ 
		for(int i = 0; i < cards.size(); i++) {   
			Card allcards = cards.get(i);
			allcards.printCard();
		}  
	}
	
	public ArrayList<Card> getAllCards(){
		return cards;
	}
	
	/**
	 * shuffle should let all cards recover, so we should put usedCard into cards.
	 * Random to get any card and put this into new position. SO, we should use the for loop.
	 * Because we want to exchange the i position card and j position card, 
	 * we should Card the temp to let one card can be seted temporary.
	 * Last, we want to reuse the UsedCard method and nUsed field, we need to initialize those again. 
	 */
	public void shuffle(){
		cards.addAll(usedCard);
		Random rnd = new Random(); 
		for(int i=0;i<cards.size();i++){
			int j = rnd.nextInt(cards.size());
			Card temp=cards.get(i);
			cards.set(i, cards.get(j));
			cards.set(j, temp);
		}
		usedCard.clear();
		nUsed=0;
		openCard.clear(); //當洗牌時要重置 ArrayList openCard，因為會把所有牌收回去洗牌，所以"明牌"也須收回。
	}
	
	/**
	 * We should create the oneCard field first.
	 * And if someone call the getOneCard method, we need to know weather there have cards in the deck.
	 * So, if there don't have cards, we should call the shuffle method. Next, use recursive to call the getOneCard.
	 * if there still are cards in deck, we will get the first card to player.
	 * So we should remove the first card in allCards arraylist after we put this card in useCard arraylist.
	 * In the end, we will return oneCard.
	 * @return oneCard ( when someone want newCard)
	 */
	public Card getOneCard(boolean isOpened){ 
		Card oneCard = null;
		if(cards.size()==0){
			shuffle();
			getOneCard(isOpened); //利用遞迴的方式，將isOpened傳入getOneCard方法裡。
		}
		else{
			oneCard= cards.get(0);
			usedCard.add(oneCard);
			if(isOpened==true){ 
				openCard.add(oneCard); /*拿到一張牌，加入isOpened參數，決定發出去的牌是開著還是蓋起來的，如果參數isOpened為true，
							         代表此牌為明牌，故需要再openCard的ArrayList加入此oneCard。*/
			}
			cards.remove(0);
			nUsed++;
			}
		return oneCard;
	}
	
	public ArrayList<Card> getOpenedCard(){
		return openCard; //回傳此副牌中所有打開過的牌。
	}
	
}
