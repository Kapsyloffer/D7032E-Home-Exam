public class Card 
{
    public enum CardType 
	{
		ExplodingKitten,
		Defuse,
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
        return;
    }

    public CardType getType()
    {
        return cType;
    }
}
