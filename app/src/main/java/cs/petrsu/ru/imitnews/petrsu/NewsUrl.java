package cs.petrsu.ru.imitnews.petrsu;

import java.util.Calendar;

/**
 * Created by Kovalchuk Denis on 23.12.16.
 * Email: deniskk25@gmail.com
 */

abstract class NewsUrl {
    static final String url = "http://cs.petrsu.ru";
    private static final int firstYear = 2002;
    int currentYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

    abstract public String getUrl();

    public void setPreviousYear() {

    }

    boolean isValidYear() {
        return currentYear >= firstYear;
    }
}
