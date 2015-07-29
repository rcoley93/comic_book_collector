package tk.rcoleyprogramming.comicbookcollector;

/**
 * Created by Ryan on 5/28/2015.
 */
public class SeriesListElement {
    long issueCount;
    String series, publisher;

    SeriesListElement() {
    }

    SeriesListElement(String s, String p, long i) {
        this.series = s;
        this.issueCount = i;
        this.publisher = p;
    }

    SeriesListElement(long count, String[] values) {
        this.issueCount = count;
        this.publisher = values[1];
        this.series = values[0];

    }

    public String toString() {
        return "The title of the series is " + series + " and has " + issueCount + " issues!";
    }
}
