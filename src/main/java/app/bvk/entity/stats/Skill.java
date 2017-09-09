package app.bvk.entity.stats;

public class Skill
{

    private int value;
    private boolean hasProficiency;

    public Skill(final int value, final boolean hasProficiency)
    {
        this.value = value;
        this.hasProficiency = hasProficiency;
    }

    public boolean isHasProficiency()
    {
        return hasProficiency;
    }

    public void setHasProficiency(boolean hasProficiency)
    {
        this.hasProficiency = hasProficiency;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

}
