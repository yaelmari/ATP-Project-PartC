package View;

public class InputValidity {
    public static int isPositiveNumber(String str)
    {
        int result = -1;

        try {
            int number = Integer.parseInt(str);
            if (number > 0) {
                result = number;
            }
        } catch (NumberFormatException e) {
            // The input is not a valid integer
            result = -1;
        }

        return result;
    }
}
