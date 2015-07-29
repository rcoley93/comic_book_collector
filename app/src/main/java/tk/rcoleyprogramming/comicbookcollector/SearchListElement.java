package tk.rcoleyprogramming.comicbookcollector;

/**
 * Created by Ryan on 5/30/2015.
 */
public class SearchListElement {

    String title, publisher, series, issueNumber;
    long id;

    SearchListElement(long id, String[] values) {
        this.id = id;
        this.title = values[2];
        this.publisher = values[3];
        this.series = values[0];
        this.issueNumber = values[1];
    }
}
