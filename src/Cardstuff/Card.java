package Cardstuff;

public class Card
{
    private String cType;

    public Card(String t)
    {
        this.cType = t;
    }

    public void Action() 
    {
		new CardAction(this.cType);
    }

    public String getType()
    {
        return cType;
    }
}
