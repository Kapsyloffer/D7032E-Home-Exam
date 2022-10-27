package Cardstuff;

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
    private CardType thisType;

    public Card(CardType t)
    {
        this.thisType = t;
    }

    public void Action() 
    {
		new CardAction(this.thisType);
    }

    public CardType getType()
    {
        return thisType;
    }
}
