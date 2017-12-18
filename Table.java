import java.util.ArrayList;

public class Table {
	private Deck AllCards; //存放所有的牌
	private Player[] AllPlayers; //存放所有的玩家
	private Dealer dealer; //存放一個莊家
	public static final int MAXPLAYER=4; //最多一張牌桌能坐幾個人
	private int[] pos_betArray ; //存放每個玩家在一局下的注
	ArrayList<Card> dealerCard=new ArrayList<Card>(); //存放dealer所拿到的card
	
	public Table(int nDeck){
		dealer=new Dealer();
		AllCards= new Deck(nDeck);
		AllPlayers=new Player[MAXPLAYER]; //設定所有玩家長度最長為4
		pos_betArray=new int[MAXPLAYER];  //設定每一局玩家所下的注的長度最長為4
	}
	
	public void set_player(int pos,Player p){
		AllPlayers[pos]=p; //將Player放到牌桌上
	}

	public Player[] get_player(){
		return AllPlayers; //回傳所有在牌桌上的player
	}
	
	public void set_dealer(Dealer d){
		dealer=d; //將Dealer放到牌桌上
	}
	
	private void ask_each_player_about_bets(){
		for(int i=0; i<AllPlayers.length;i++){
			if(AllPlayers[i]==null){ //有可能玩家小於4人，所以當玩家為null時 就continue，查看下一個位置是否有坐玩家
				continue;
			}
			AllPlayers[i].sayHello();
			AllPlayers[i].makeBet();
			pos_betArray[i]=AllPlayers[i].makeBet();
		}
	}
	
	public Card get_face_up_card_of_dealer(){
		return dealerCard.get(1); //回傳dealer打開的那張牌，也就是第二張牌
	}

	
	private void distribute_cards_to_dealer_and_players(){
		ArrayList<Card> playerCard=new ArrayList<Card>();//存放每一個player所拿到的card
		for(int i=0; i<AllPlayers.length;i++){
			if(AllPlayers[i]==null){
				continue;
			}
			//發兩張打開的牌給玩家
			playerCard.add(AllCards.getOneCard(true)); 
			playerCard.add(AllCards.getOneCard(true));
			AllPlayers[i].setOneRoundCard(playerCard); //將所拿到的兩張牌到setOneRoundCard裡
			playerCard=new ArrayList<Card>(); //將此playerCard在一次實體化，供下一個player可有新的空間使用
		}
		//再一張蓋著的牌，以及一張打開的牌給莊家
		dealerCard.add(AllCards.getOneCard(false));
		dealerCard.add(AllCards.getOneCard(true));
		dealer.setOneRoundCard(dealerCard);
		System.out.print("Dealer's face up card is ");
		dealerCard.get(1).printCard(); //開的牌亦即第二張牌
	}
	
	private void ask_each_player_about_hits(){
		for(Player p : AllPlayers){
			if(p==null){
				continue;
			}
			boolean hit=false;
			do{
				hit=p.hit_me(this);
				if(hit) //如果hit=true及要牌
				{
					System.out.print("Hit! ");
					p.getOneRoundCard().add(AllCards.getOneCard(true));
					System.out.println(p.getName()+"'s Cards now:");
					for(Card c:p.getOneRoundCard()){
						c.printCard();
					}
					if(p.getTotalValue()>21){ //如果爆了，就要跳出迴圈，所以hit要等於false
						hit=false;
					}
				}
			}while(hit); //使用do-while迴圈是此動作至少會做一次	
			System.out.println(p.getName()+", Pass hit!");
			System.out.println(p.getName()+", Final Card:");
			for(Card c : p.getOneRoundCard()){
				c.printCard();
			}
			System.out.println(p.getName()+"'s hit is over!");
		}
		
	}
	
	private void ask_dealer_about_hits(){
		boolean hit=false;
		do{
			hit=dealer.hit_me(this); //this
			if(hit){
				System.out.print("Hit! ");
				dealer.getOneRoundCard().add(AllCards.getOneCard(true));
				System.out.println("dealer's Cards now:");
				for(Card c : dealer.getOneRoundCard()){
					c.printCard();
				}
				if(dealer.getTotalValue()>21){
					hit=false;
				}
			}
		}while(hit);
		System.out.println("Dealer's  hit is over!");
		System.out.println("dealer, Final Card:");
		for(Card c : dealer.getOneRoundCard()){
			c.printCard();
		}
	}
	
	private void calculate_chips(){
		System.out.println("Dealer's card value is "+dealer.getTotalValue()+", Cards: ");
		dealer.printAllCard();
		for(int i=0; i<AllPlayers.length;i++){
			if(AllPlayers[i]==null){
				continue;
			}
			System.out.print(AllPlayers[i].getName()+" card value is "+AllPlayers[i].getTotalValue());
			// player贏的情況有可能為:1.player的點數大於dealer的點數，且player和dealer的點數皆<=21；2.dealer的點數>21
			if(AllPlayers[i].getTotalValue()>dealer.getTotalValue()&&AllPlayers[i].getTotalValue()<=21&&dealer.getTotalValue()<=21||AllPlayers[i].getTotalValue()<=21&&dealer.getTotalValue()>21){
				AllPlayers[i].increaseChips(pos_betArray[i]);
				System.out.println(", Get "+AllPlayers[i].makeBet()+" Chips, the Chips now is: "+AllPlayers[i].getCurrentChips());	
			}
			//兩方平手的情況有可能為:1.player的點數=dealer的點數；2.dealer和player的點數皆>21
			else if(AllPlayers[i].getTotalValue()>21&&dealer.getTotalValue()>21||AllPlayers[i].getTotalValue()==dealer.getTotalValue() ){
				AllPlayers[i].increaseChips(0);
				System.out.println(", chips have no change! The Chips now is: "+AllPlayers[i].getCurrentChips());
			}
			else{
				AllPlayers[i].increaseChips(-pos_betArray[i]);
				System.out.println(", Loss "+AllPlayers[i].makeBet()+" Chips, the Chips now is: "+AllPlayers[i].getCurrentChips());
			}
		}
		//當一局玩完時，記得將array裡bet的值歸0，方便下一局下注可重新使用
		for(int i=0; i<pos_betArray.length; i++){
			pos_betArray[i] = 0;
		}
		//當一局玩完後，dealerCard需要清空，方便下一局使用
		dealerCard.clear();
	}
	
	public int[] get_players_bet(){
		return pos_betArray;
	}
	
	public void play(){
		ask_each_player_about_bets();
		distribute_cards_to_dealer_and_players();
		ask_each_player_about_hits();
		ask_dealer_about_hits();
		calculate_chips();
	}
	
}
