package tk.rcoleyprogramming.comicbookcollector;

import android.provider.BaseColumns;

/**
 * Created by Ryan on 5/26/2015.
 */
public final class ComicBookTableStructure {

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ComicBookEntry.tableName;
    private static final String TEXT_TYPE = " VARCHAR(255)";
    private static final String COMMA_SEP = ", ";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ComicBookEntry.tableName + " (" +
                    ComicBookEntry._ID + " INTEGER PRIMARY KEY, " +
                    ComicBookEntry.series + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.issueNumber + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.issueTitle + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.publisher + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.coverDateMonth + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.coverDateYear + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.coverPrice + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.condition + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.storageMethod + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.comicLocation + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.pricePaid + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.writer + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.penciller + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.inker + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.colorist + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.letterer + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.editor + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.coverArtist + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.readUnread + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.dateAcquired + TEXT_TYPE + COMMA_SEP +
                    ComicBookEntry.locationAcquired + TEXT_TYPE + " )";
    String[] categories = {"Title", "Issue Number", "Issue Name", "Publisher", "Cover Date Month",
            "Cover Date Year", "Cover Price", "Condition", "Storage Method", "Storage Location", "Price Paid",
            "Writer(s)", "Penciller(s)", "Inker(s)", "Colorist(s)", "Letterer(s)", "Editor(s)",
            "Cover Artist(s)", "Read/Unread", "Date Acquired", "Location Acquired"};

    public ComicBookTableStructure() {
    }

    public static abstract class ComicBookEntry implements BaseColumns {
        public static final String tableName = "comic_books";
        public static final String series = "series_name";
        public static final String issueNumber = "issue_number";
        public static final String issueTitle = "issue_title";
        public static final String publisher = "publisher";
        public static final String coverDateMonth = "cover_date_month";
        public static final String coverDateYear = "cover_date_year";
        public static final String coverPrice = "cover_price";
        public static final String condition = "condition";
        public static final String storageMethod = "storage_method";
        public static final String pricePaid = "price_paid";
        public static final String writer = "writer";
        public static final String penciller = "penciller";
        public static final String inker = "inker";
        public static final String colorist = "colorist";
        public static final String letterer = "letterer";
        public static final String editor = "editor";
        public static final String coverArtist = "cover_artist";
        public static final String readUnread = "read_unread";
        public static final String dateAcquired = "date_acquired";
        public static final String locationAcquired = "location_acquired";
        public static final String comicLocation = "comic_location";
    }

}
