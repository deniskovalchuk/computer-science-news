package cs.petrsu.ru.imitnews.remote;

import org.jsoup.nodes.Document;

/**
 * Created by Kovalchuk Denis on 17.12.16.
 * Email: deniskk25@gmail.com
 */

public class LoadedDataInspector {
    private static boolean failLoad = true;

    private LoadedDataInspector() {

    }

    public static boolean isFailLoad() {
        return  failLoad;
    }

    public static boolean isFailLoad(Document data) {
        return failLoad = data == null;
    }
}
