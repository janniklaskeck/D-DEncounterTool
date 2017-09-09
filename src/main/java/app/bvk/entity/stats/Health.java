package app.bvk.entity.stats;

public class Health
{
    private int maxHealth;
    private int currentHealth;
    private int temporaryHealth;

    public Health(final int maxHealth, final int currentHealth, final int temporaryHealth)
    {
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.temporaryHealth = temporaryHealth;
    }

    public int getMaxHealth()
    {
        return this.maxHealth;
    }

    public void setMaxHealth(final int maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth()
    {
        return this.currentHealth;
    }

    public void setCurrentHealth(final int currentHealth)
    {
        this.currentHealth = currentHealth;
    }

    public int getTemporaryHealth()
    {
        return this.temporaryHealth;
    }

    public void setTemporaryHealth(final int temporaryHealth)
    {
        this.temporaryHealth = temporaryHealth;
    }
}
