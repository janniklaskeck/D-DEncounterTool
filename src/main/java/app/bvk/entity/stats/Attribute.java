package app.bvk.entity.stats;

public class Attribute
{
    private int value;

    private boolean hasProficiency;

    public Attribute(final int value, final boolean hasProficiency)
    {
        this.value = value;
        this.hasProficiency = hasProficiency;
    }

    public int getValue()
    {
        return this.value;
    }

    public void setValue(final int value)
    {
        this.value = value;
    }

    public int getModifier()
    {
        return (this.value - 10) / 2;
    }

    public boolean hasProficiency()
    {
        return this.hasProficiency;
    }

    public void setProficiency(final boolean hasProficiency)
    {
        this.hasProficiency = hasProficiency;
    }
}
