package tk.rcoleyprogramming.comicbookcollector;

/**
 * Created by Ryan on 5/28/2015.
 */
public class SeriesListElement {
    SeriesListElement() {
    }

    SeriesListElement(String s, String p, int i) {
        this.series = s;
        this.issueNumber = i;
        this.publisher = p;
    }

    int issueNumber;
    String series, publisher;

    public String toString() {
        return "The title of the series is " + series + " and has " + issueNumber + " issues!";
    }
}
