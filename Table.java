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
		AllPlayers[pos]=p;
	}

	public Player[] get_player(){
		return AllPlayers;
	}
	
	public void set_dealer(Dealer d){
		dealer=d;
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
		return dealerCard.get(1);
	}

	
	private void distribute_cards_to_dealer_and_players(){
		ArrayList<Card> playerCard=new ArrayList<Card>();//存放每一個player所拿到的card
		for(int i=0; i<AllPlayers.length;i++){
			if(AllPlayers[i]==null){
				continue;
			}
			playerCard.add(AllCards.getOneCard(true)); 
			playerCard.add(AllCards.getOneCard(true));
			AllPlayers[i].setOneRoundCard(playerCard); //將所拿到的兩張牌到setOneRoundCard裡
			while(AllPlayers[i].hit_me(this)){ //當滿足hit_me==true時，即須要在getOneCard
				playerCard.add(AllCards.getOneCard(true));
				AllPlayers[i].setOneRoundCard(playerCard);
			}
			playerCard=new ArrayList<Card>(); //將此playerCard在一次實體化，供下一個player可有新的空間使用
		}
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
					System.out.println("Hit! "+p.getName()+ "’s cards now: ");
					for(Card c:p.getOneRoundCard()){
						c.printCard();
					}
					System.out.println(p.getName()+"'s hit is over!");
				}
				else 
				{
					System.out.println(p.getName()+", Pass hit!");
					System.out.println(p.getName()+", Final Card:");
					for(Card c : p.getOneRoundCard()){
						c.printCard();
					}
					System.out.println(p.getName()+"'s hit is over!");
				}
			}while(hit); //使用do-while迴圈是此動作至少會做一次	
		}
		
	}
	
	private void ask_dealer_about_hits(){
		boolean hit=false;
		do{
			hit=dealer.hit_me(this);
			if(hit) //當hit=true時
			{
				dealerCard.add(AllCards.getOneCard(false));
				dealer.setOneRoundCard(dealerCard);
			}
			else
			{
				System.out.println("Dealer's hit is over!");
			}
		}while(hit);
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
		//當計算完畢bet時，記得將array裡bet的值歸0
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
