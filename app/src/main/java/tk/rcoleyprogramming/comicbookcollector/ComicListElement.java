package tk.rcoleyprogramming.comicbookcollector;

/**
 * Created by Ryan on 5/29/2015.
 */
public class ComicListElement {
    long id;
    String title, issueNumber;

    ComicListElement() {
    }

    ComicListElement(long intId, String[] values) {
        id = intId;
        title = values[0];
        issueNumber = values[1];
    }
}
