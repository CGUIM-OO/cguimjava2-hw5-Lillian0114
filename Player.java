//import java.util.ArrayList;

//import java.util.ArrayList;

public class Player extends Person {
	private String name; //玩家姓名
	private int chips; //玩家有的籌碼
	private int bet; //玩家此局下注的籌碼
	
	
	public Player(String name, int chips){
		this.name=name; //因為local variable與instance variable一樣(shadowing)，故當要呼叫instance variable時，需要用this關鍵字
		this.chips=chips;
	}
	public String getName(){
		return name;
	}
	public int getCurrentChips(){
		return chips; //回傳玩家現有籌碼
	}
	public void sayHello(){
		System.out.println("Hello, I am " + name + ".");
		System.out.println("I have " + chips + " chips.");
	}
	
	
	public void increaseChips (int diff){   //玩家籌碼變動
		chips+=diff; //將傳進來的diff參數加入chips裡
	}
	
	public int makeBet(){
		if(chips==0){
			System.out.println("您已沒籌碼，無法繼續下注了喔!");
			bet=0; //因為已經沒有籌碼了，所以也無法下注，故bet=0
		}
		else{
			bet=1;
		}
		return bet;
	}
	
	@Override 
	//因為person的hit_me為abstract method，所以當Player繼承Person時就要再寫一次有body的method
	public boolean hit_me(Table table) {
		boolean ans=false;
		if (getTotalValue() == 17 && hasAce()) {
			ans= true;
		} 
		else if(getTotalValue()<=16){
			ans=true;
		}
		else if(getTotalValue()>17){
			ans=false;
		}
		else if (getTotalValue() >= 21){
				ans= false;
		}
		return ans;
	}
}