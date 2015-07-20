package tk.rcoleyprogramming.comicbookcollector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ryan on 5/28/2015.
 */
public class ComicBookDatabaseHelper extends SQLiteOpenHelper {
    public static final int DB_Version = 5;
    public static final String DB_Name = "ComicBooks.db";

    public ComicBookDatabaseHelper(Context c) {
        super(c, DB_Name, null, DB_Version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ComicBookTableStructure.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ComicBookTableStructure.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
