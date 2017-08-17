package app.bvk.utils;

import java.util.Random;
import java.util.regex.Pattern;

public class Dice
{

    private Dice()
    {

    }

    /**
     * Simple dice throw, e.g. 2d6
     *
     * @param input
     * @throws Exception
     */
    public static int throwDice(final String input) throws DiceParseException
    {
        final Random rnd = new Random();
        int result;
        int amount = 0;
        int diceType = 0;
        int extra = 0;
        String[] inputSplit = input.split(Pattern.quote("+"));
        String newInput = "";
        if (inputSplit.length == 2)
        {
            extra = Integer.parseInt(inputSplit[1].trim());
            newInput = inputSplit[0].trim();
        }
        inputSplit = newInput.split("d").length != 1 ? newInput.split("d") : newInput.split("D");
        if (inputSplit.length == 1)
        {
            throw new DiceParseException();
        }
        else if (inputSplit.length == 2)
        {
            amount = Integer.parseInt(inputSplit[0]);
            diceType = Integer.parseInt(inputSplit[1]);
        }

        if (amount == 0 || diceType == 0)
        {
            return 0;
        }
        final int diceThrow = rnd.nextInt(diceType - 1) + 1;
        result = amount * diceThrow + extra;
        return result;
    }

}
