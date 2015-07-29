package tk.rcoleyprogramming.comicbookcollector;

import com.orm.SugarRecord;

/**
 * Created by Ryan on 7/28/2015.
 */
public class ComicPublisher extends SugarRecord<ComicPublisher> {
    private String name;
    private int id;

    public ComicPublisher() {
    }

    public ComicPublisher(String name, int id) {
        this.name = name;
        this.id = id;
        this.save();
    }

    public String toString() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        ComicPublisher getComicID = ComicPublisher.find(ComicPublisher)
    }
}
