package tk.rcoleyprogramming.comicbookcollector;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class AddComic extends ActionBarActivity {

    String strPublisher, strSeries;
    int intID = -1;
    //all of the views on the page
    EditText etSeries, etPublisher, etIssueNumber, etIssueTitle, etPricePaid,
            etWriter, etPenciller, etInker, etColorist, etLetterer, etEditor,
            etCoverArtist, etLocationAcquired, etCoverPrice, etComicLocation, etDateAquiredYear, etCoverDateYear;
    Spinner spGrade, spStorageMethod, spReadUnread, spCoverDateMonth;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_comic);

        etSeries = (EditText) findViewById(R.id.series);
        etPublisher = (EditText) findViewById(R.id.publisher);
        etColorist = (EditText) findViewById(R.id.colorist);
        etComicLocation = (EditText) findViewById(R.id.comicLocation);
        etCoverArtist = (EditText) findViewById(R.id.coverArtist);
        etCoverPrice = (EditText) findViewById(R.id.coverPrice);
        etPenciller = (EditText) findViewById(R.id.penciller);
        etPricePaid = (EditText) findViewById(R.id.pricePaid);
        etInker = (EditText) findViewById(R.id.inker);
        etIssueNumber = (EditText) findViewById(R.id.issueNumber);
        etIssueTitle = (EditText) findViewById(R.id.issueTitle);
        etLetterer = (EditText) findViewById(R.id.letterer);
        etLocationAcquired = (EditText) findViewById(R.id.locationAcquired);
        etEditor = (EditText) findViewById(R.id.editor);
        etWriter = (EditText) findViewById(R.id.writer);
        etDateAquiredYear = (EditText) findViewById(R.id.etDateAquiredYear);
        etCoverDateYear = (EditText) findViewById(R.id.etCoverDateYear);

        spGrade = (Spinner) findViewById(R.id.condition);
        spStorageMethod = (Spinner) findViewById(R.id.storageMethod);
        spReadUnread = (Spinner) findViewById(R.id.readUnread);
        spCoverDateMonth = (Spinner) findViewById(R.id.spnCoverDateMonth);

        getInfo();


    }

    private void getInfo() {
        Intent i = getIntent();
        if (i.getBooleanExtra("extra_data", false)) {
            intID = i.getIntExtra("id", -1);
            if(intID == -1){
                strSeries = i.getStringExtra("series");
                strPublisher = i.getStringExtra("publisher");
                etSeries.setText(strSeries);
                etPublisher.setText(strPublisher);
                etSeries.setEnabled(false);
                etSeries.setFocusable(false);
                etPublisher.setEnabled(false);
                etPublisher.setFocusable(false);
                return;
            }else {
                SQLiteDatabase db = new ComicBookDatabaseHelper(this).getReadableDatabase();
                Cursor cursor = db.rawQuery((new StringBuilder()).append("SELECT * FROM comic_books WHERE _id LIKE ").append(String.valueOf(intID)).toString(), null);
                cursor.moveToFirst();
                String s = cursor.getString(cursor.getColumnIndexOrThrow("price_paid"));
                String s1 = cursor.getString(cursor.getColumnIndexOrThrow("price_paid"));
                etSeries.setText(cursor.getString(cursor.getColumnIndexOrThrow("series_name")));
                etPublisher.setText(cursor.getString(cursor.getColumnIndexOrThrow("publisher")));
                etIssueNumber.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("issue_number"))));
                etIssueTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("issue_title")));

                setSpinner(spCoverDateMonth, cursor.getString(cursor.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.coverDateMonth)), 4);
                etCoverDateYear.setText(cursor.getString(cursor.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.coverDateYear)));

                etDateAquiredYear.setText(cursor.getString(cursor.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.dateAcquired)));

                etCoverPrice.setText(s1);
                setSpinner(spGrade, cursor.getString(cursor.getColumnIndexOrThrow("condition")), 1);
                setSpinner(spStorageMethod, cursor.getString(cursor.getColumnIndexOrThrow("storage_method")), 2);
                etPricePaid.setText(s);
                etWriter.setText(cursor.getString(cursor.getColumnIndexOrThrow("writer")));
                etPenciller.setText(cursor.getString(cursor.getColumnIndexOrThrow("penciller")));
                etInker.setText(cursor.getString(cursor.getColumnIndexOrThrow("inker")));
                etColorist.setText(cursor.getString(cursor.getColumnIndexOrThrow("colorist")));
                etLetterer.setText(cursor.getString(cursor.getColumnIndexOrThrow("letterer")));
                etEditor.setText(cursor.getString(cursor.getColumnIndexOrThrow("editor")));
                etCoverArtist.setText(cursor.getString(cursor.getColumnIndexOrThrow("cover_artist")));
                etLocationAcquired.setText(cursor.getString(cursor.getColumnIndexOrThrow("location_acquired")));
                setSpinner(spReadUnread, cursor.getString(cursor.getColumnIndexOrThrow("read_unread")), 3);
                etComicLocation.setText(cursor.getString(cursor.getColumnIndexOrThrow("comic_location")));
                cursor.close();
                ((Button) findViewById(R.id.btnAddComic)).setText("Update Comic");
            }
        }
    }

    public void setSpinner(Spinner sp, String s, int i) {
        String[] str;
        switch (i) {
            case 1:
                str = getResources().getStringArray(R.array.comic_grades);
                for (int j = 0; j < str.length; ++j) {
                    if (str[j].equals(s)) sp.setSelection(j);
                }
                break;
            case 2:
                str = getResources().getStringArray(R.array.comic_storage);
                for (int j = 0; j < str.length; ++j) {
                    if (str[j].equals(s)) sp.setSelection(j);
                }
                break;
            case 3:
                if (s.equals("Read")) sp.setSelection(1);
                else sp.setSelection(0);
                break;
            case 4:
                str = getResources().getStringArray(R.array.months);
                for (int j = 0; j < str.length; ++j) {
                    if (str[j].equals(s)) sp.setSelection(j);
                }
                break;
        }
    }

    public void addComic(View v) {
        //first check to see if series,publisher and issue number are filled in
        if (etSeries.getText().toString().equals("")) {
            Toast.makeText(this, "Make sure that the series is filled in!", Toast.LENGTH_LONG).show();
            return;
        } else if (etIssueNumber.getText().toString().equals("")) {
            Toast.makeText(this, "Make sure that the issue number is filled in!", Toast.LENGTH_LONG).show();
            return;
        } else if (etPublisher.getText().toString().equals("")) {
            Toast.makeText(this, "Make sure that the publisher is filled in!", Toast.LENGTH_LONG).show();
            return;
        }

        //set up the db
        ComicBookDatabaseHelper cbdbHelper = new ComicBookDatabaseHelper(this);

        SQLiteDatabase dbComics = cbdbHelper.getWritableDatabase();

        //set all the values
        ContentValues cv = new ContentValues();
        String intIssueNumber = etIssueNumber.getText().toString();

        cv.put(ComicBookTableStructure.ComicBookEntry.colorist, etColorist.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.condition, spGrade.getSelectedItem().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.coverArtist, etCoverArtist.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.coverDateMonth, spCoverDateMonth.getSelectedItem().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.coverDateYear,etCoverDateYear.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.coverPrice, etCoverPrice.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.dateAcquired, etDateAquiredYear.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.editor, etEditor.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.inker, etInker.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.issueTitle, etIssueTitle.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.letterer, etLetterer.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.issueNumber, intIssueNumber);
        cv.put(ComicBookTableStructure.ComicBookEntry.locationAcquired, etLocationAcquired.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.penciller, etPenciller.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.pricePaid, etPricePaid.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.series, etSeries.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.publisher, etPublisher.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.writer, etWriter.getText().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.readUnread, spReadUnread.getSelectedItem().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.storageMethod, spStorageMethod.getSelectedItem().toString());
        cv.put(ComicBookTableStructure.ComicBookEntry.comicLocation, etComicLocation.getText().toString());

        long count = 0;
        //check to see if update
        if (intID != -1) {
            // Which row to update, based on the ID
            String selection = ComicBookTableStructure.ComicBookEntry._ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(intID)};
            count = dbComics.update(ComicBookTableStructure.ComicBookEntry.tableName, cv, selection, selectionArgs);
        } else {
            try {
                //insert the new row
                count = dbComics.insertOrThrow(ComicBookTableStructure.ComicBookEntry.tableName, null, cv);
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

        //check to see if it entered
        if (count == 0) {
            Toast.makeText(this, "Error! Couldn't enter the new comic into the database", Toast.LENGTH_SHORT).show();
        } else {
            if (intID == -1)
                Toast.makeText(this, "The comic book was added to the collection!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "The comic book was updated in the collection!", Toast.LENGTH_SHORT).show();
        }

        dbComics.close();
        finish();
    }
}
