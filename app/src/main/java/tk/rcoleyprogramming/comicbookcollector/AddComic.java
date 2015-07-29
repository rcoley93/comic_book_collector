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
    long intID = -1;
    //all of the views on the page
    EditText etSeries, etPublisher, etIssueNumber, etIssueTitle, etPricePaid,
            etWriter, etPenciller, etInker, etColorist, etLetterer, etEditor,
            etCoverArtist, etLocationAcquired, etCoverPrice, etComicLocation, etDateAquiredYear, etCoverDateYear;
    Spinner spGrade, spStorageMethod, spReadUnread, spCoverDateMonth;
    ComicBook oldComic = null;

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
            intID = i.getLongExtra("id",-1);
            if(intID == -1){
                //add comic in series
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
                //update comic
                oldComic = ComicBook.findById(ComicBook.class,intID);
                String[] values = oldComic.getAllDetails();

                etSeries.setText(values[0]);
                etIssueNumber.setText(values[1]);
                etIssueTitle.setText(values[2]);
                etPublisher.setText(values[3]);
                setSpinner(spCoverDateMonth, values[4], 4);
                etCoverDateYear.setText(values[5]);
                etCoverPrice.setText(values[6]);
                setSpinner(spGrade, values[7], 1);
                setSpinner(spStorageMethod, values[8], 2);
                etPricePaid.setText(values[9]);
                etWriter.setText(values[10]);
                etPenciller.setText(values[11]);
                etInker.setText(values[12]);
                etColorist.setText(values[13]);
                etLetterer.setText(values[14]);
                etEditor.setText(values[15]);
                etCoverArtist.setText(values[16]);
                setSpinner(spReadUnread, values[17], 3);
                etDateAquiredYear.setText(values[18]);
                etLocationAcquired.setText(values[19]);
                etComicLocation.setText(values[20]);

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
                    if (str[j].contains(s)) sp.setSelection(j);
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

        ComicBook newComic = new ComicBook(etSeries.getText().toString(),etIssueNumber.getText().toString(),
                etIssueTitle.getText().toString(), etPublisher.getText().toString(),spCoverDateMonth.getSelectedItem().toString(),
                etCoverDateYear.getText().toString(), etCoverPrice.getText().toString(), spGrade.getSelectedItem().toString(),
                spStorageMethod.getSelectedItem().toString(), etPricePaid.getText().toString(), etWriter.getText().toString(),
                etPenciller.getText().toString(), etInker.getText().toString(), etColorist.getText().toString(), etLetterer.getText().toString(),
                etEditor.getText().toString(),etCoverArtist.getText().toString(),spReadUnread.getSelectedItem().toString(),
                etDateAquiredYear.getText().toString(), etLocationAcquired.getText().toString(), etComicLocation.getText().toString());

        if (oldComic != null) {
            long oldID = oldComic.getId();
            newComic.setId(oldID);
            oldComic = newComic;
            oldComic.save();
        } else {
            newComic.save();
        }

        if (oldComic != null) Toast.makeText(this, "The comic book was added to the collection!", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "The comic book was updated in the collection!", Toast.LENGTH_SHORT).show();

        finish();
    }
}
