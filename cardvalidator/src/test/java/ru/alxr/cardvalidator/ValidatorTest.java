package ru.alxr.cardvalidator;

import org.junit.Test;
import ru.alxr.cardvalidator.impl.DefaultCardInfoParser;
import ru.alxr.cardvalidator.impl.DefaultValidator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidatorTest {

    private DefaultValidator validator = new DefaultValidator(new DefaultCardInfoParser());

    @Test
    public void addition_isCorrect() {
        assert (checkWrongNumber("4929804463622138"));
        assert (checkWrongNumber("5212132012291762"));


        assert (checkValidNumber("4929804463622139"));
        assert (checkValidNumber("6762765696545485"));
        assert (checkValidNumber("6210948000000029"));
    }

    private boolean checkWrongNumber(String source) {
        try {
            validator.commonCheck(source);
            System.out.println("Failed with " + source);
            return false;
        } catch (Exception e) {
            System.out.println("Passed with " + source);
            return true;
        }
    }

    private boolean checkValidNumber(String source) {
        try {
            validator.commonCheck(source);
            System.out.println("Passed with " + source);
            return true;
        } catch (Exception e) {
            System.out.println("Failed with " + source);
            return false;
        }
    }

}