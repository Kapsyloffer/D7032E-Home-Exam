package Rewrite;
public class Card
{
    public enum CardType 
	{
		//Dessa är speciella. Hantera instantly.
		ExplodingKitten,
		Defuse,
		//Not these tho
		Attack,
		Favor,
		Nope,
		Shuffle,
		Skip,
		SeeTheFuture,
		HairyPotatoCat,
		Cattermelon,
		RainbowRalphingCat,
		TacoCat,
		OverweightBikiniCat
	}
    private CardType cType;

    public Card(CardType t)
    {
        this.cType = t;
    }

    public void Action()
    {
		new CardAction(this.cType);
    }

    public CardType getType()
    {
        return cType;
    }
}
