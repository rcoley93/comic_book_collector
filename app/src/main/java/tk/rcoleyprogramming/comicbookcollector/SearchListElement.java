package tk.rcoleyprogramming.comicbookcollector;

/**
 * Created by Ryan on 5/30/2015.
 */
public class SearchListElement {
    SearchListElement() {
    }

    SearchListElement(String t, String p, String s, int in, int id) {
        this.title = t;
        this.publisher = p;
        this.series = s;
        this.issueNumber = in;
        this.id = id;
    }

    String title, publisher, series;
    int issueNumber, id;
}
