package app.bvk.utils;

import java.util.Random;
import java.util.regex.Pattern;

public class Dice {

    private Dice() {

    }

    /**
     * Simple dice throw, e.g. 2d6
     * 
     * @param input
     * @throws Exception
     */
    public static int throwDice(String input) throws DiceParseException {
        Random rnd = new Random();
        int result;
        int amount = 0;
        int diceType = 0;
        int extra = 0;
        String[] inputSplit = input.split(Pattern.quote("+"));
        if (inputSplit.length == 2) {
            extra = Integer.parseInt(inputSplit[1].trim());
            input = inputSplit[0].trim();
        }
        inputSplit = input.split("d").length != 1 ? input.split("d") : input.split("D");
        if (inputSplit.length == 1) {
            throw new DiceParseException();
        } else if (inputSplit.length == 2) {
            amount = Integer.parseInt(inputSplit[0]);
            diceType = Integer.parseInt(inputSplit[1]);
        }

        if (amount == 0 || diceType == 0) {
            return 0;
        }
        int diceThrow = rnd.nextInt(diceType - 1) + 1;
        result = amount * diceThrow + extra;
        System.out.println("Amount: " + amount + " DiceType: " + diceType + " Dice Throw: " + diceThrow + " Extra: "
                + extra + " Result: " + result);
        return result;
    }

}
