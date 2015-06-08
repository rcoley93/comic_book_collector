package tk.rcoleyprogramming.comicbookcollector;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ViewComic extends ActionBarActivity {
    TextView etSeries, etPublisher, etIssueNumber, etIssueTitle, etPricePaid,
            etWriter, etPenciller, etInker, etColorist, etLetterer, etEditor,
            etCoverArtist, etLocationAcquired, etCoverPrice, etCoverDate,
            etDateAcquired, etGrade, etStorageMethod, etReadUnread, etComicLocation;

    int intID;

    //start up the database
    ComicBookDatabaseHelper cbdbHelper = new ComicBookDatabaseHelper(this);

    @Override
    protected void onResume() {
        super.onResume();
        viewComic();
    }

    public void viewComic() {

        SQLiteDatabase dbComics = cbdbHelper.getReadableDatabase();

        String strQuery = "SELECT * FROM " + ComicBookTableStructure.ComicBookEntry.tableName + " WHERE " + ComicBookTableStructure.ComicBookEntry._ID + " LIKE " + String.valueOf(intID);
        Cursor c = dbComics.rawQuery(strQuery, null);
        c.moveToFirst();

        //set all the text views to the appropriate text
        String strPricePaid = c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.pricePaid));
        String strCoverPrice = c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.pricePaid));

        if (strPricePaid == "") strPricePaid = "Free";
        else if (strPricePaid.length() == 3) strPricePaid = strPricePaid + "0";

        if (strCoverPrice == "") strCoverPrice = "Free";
        else if (strCoverPrice.length() == 3) strCoverPrice = strCoverPrice + "0";

        etSeries.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.series)));
        etPublisher.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.publisher)));
        etIssueNumber.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.issueNumber)));
        etIssueTitle.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.issueTitle)));
        etCoverDate.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.coverDate)));
        etCoverPrice.setText(strCoverPrice);
        etGrade.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.condition)));
        etStorageMethod.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.storageMethod)));
        etPricePaid.setText(strPricePaid);
        etWriter.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.writer)));
        etPenciller.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.penciller)));
        etInker.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.inker)));
        etColorist.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.colorist)));
        etLetterer.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.letterer)));
        etEditor.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.editor)));
        etCoverArtist.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.coverArtist)));
        etLocationAcquired.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.locationAcquired)));
        etReadUnread.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.readUnread)));
        etDateAcquired.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.dateAcquired)));
        etComicLocation.setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.comicLocation)));

        c.close();
        dbComics.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);

        Intent i = getIntent();
        intID = i.getIntExtra("id", -1);

        etSeries = (TextView) findViewById(R.id.etseries);
        etPublisher = (TextView) findViewById(R.id.etpublisher);
        etIssueNumber = (TextView) findViewById(R.id.etissueNumber);
        etIssueTitle = (TextView) findViewById(R.id.etissueTitle);
        etCoverDate = (TextView) findViewById(R.id.etcoverDate);
        etCoverPrice = (TextView) findViewById(R.id.etcoverPrice);
        etGrade = (TextView) findViewById(R.id.etcondition);
        etStorageMethod = (TextView) findViewById(R.id.etstorageMethod);
        etPricePaid = (TextView) findViewById(R.id.etpricePaid);
        etWriter = (TextView) findViewById(R.id.etwriter);
        etPenciller = (TextView) findViewById(R.id.etpenciller);
        etInker = (TextView) findViewById(R.id.etinker);
        etColorist = (TextView) findViewById(R.id.etcolorist);
        etLetterer = (TextView) findViewById(R.id.etletterer);
        etEditor = (TextView) findViewById(R.id.eteditor);
        etCoverArtist = (TextView) findViewById(R.id.etcoverArtist);
        etLocationAcquired = (TextView) findViewById(R.id.etlocationAcquired);
        etReadUnread = (TextView) findViewById(R.id.etreadUnread);
        etDateAcquired = (TextView) findViewById(R.id.etacquiredDate);
        etComicLocation = (TextView) findViewById(R.id.etComicLocation);

        viewComic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_comic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_delete:
                String strQuery = "DELETE FROM " + ComicBookTableStructure.ComicBookEntry.tableName + " WHERE " + ComicBookTableStructure.ComicBookEntry._ID + "='" + intID + "'";
                SQLiteDatabase db = cbdbHelper.getWritableDatabase();
                db.execSQL(strQuery);
                db.close();
                this.finish();
                return true;
            case R.id.action_add_range:
                addRange();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addRange() {
        LayoutInflater li = LayoutInflater.from(ViewComic.this);
        View v = li.inflate(R.layout.range_prompt, null);

        AlertDialog.Builder prompt = new AlertDialog.Builder(ViewComic.this);

        prompt.setView(v);

        final EditText range = (EditText) v.findViewById(R.id.range);
        range.setHint("ex. 1,2,4-6,8,10");

        prompt.setCancelable(false);

        prompt.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<Integer> aRange = parseRange(range.getText().toString());
                        String strSeries,strPublisher;
                        strSeries = etSeries.getText().toString();
                        strPublisher = etPublisher.getText().toString();
                        if (aRange.size() == 0) {
                            Toast.makeText(getApplicationContext(),"Error! Unable to parse the range! Please try again!",Toast.LENGTH_LONG).show();
                            return;
                        }
                        addComics(aRange,strSeries,strPublisher);
                        dialog.cancel();
                    }
                });

        prompt.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    dialog.cancel();
                }
            });

        AlertDialog ad = prompt.create();

        ad.show();
    }

    private ArrayList<Integer> parseRange(String strRange){
        ArrayList<Integer> result = new ArrayList<Integer>();

        String[] strRangeSplit = strRange.split(",");

        for(int i = 0; i<strRangeSplit.length;++i){
            if(strRangeSplit[i].contains("-")){
                String[] strRangeSplitSplit = strRangeSplit[i].split("-");
                if(strRangeSplitSplit.length!=2){
                    result.clear();
                    return result;
                }
                int start,end;
                try{
                    start = Integer.parseInt(strRangeSplitSplit[0]);
                    end = Integer.parseInt(strRangeSplitSplit[1]);
                }catch(NumberFormatException ex){
                    result.clear();
                    return result;
                }
                for(int j = start; j<=end;++j){
                    result.add(j);
                }
            }else{
                int intAdd;
                try{
                    intAdd = Integer.parseInt(strRangeSplit[i]);
                }catch(NumberFormatException ex){
                    result.clear();
                    return result;
                }
                if(intAdd<0){
                    result.clear();
                    return result;
                }else{
                    result.add(intAdd);
                }
            }
        }

        return result;
    }

    private void addComics(ArrayList<Integer> aRange,String series, String publisher) {
        for(int i = 0;i<aRange.size();++i){
            //set up the db
            ComicBookDatabaseHelper cbdbHelper = new ComicBookDatabaseHelper(getApplicationContext());

            SQLiteDatabase dbComics = cbdbHelper.getWritableDatabase();

            //set all the values
            ContentValues cv = new ContentValues();

            cv.put(ComicBookTableStructure.ComicBookEntry.issueNumber,aRange.get(i));
            cv.put(ComicBookTableStructure.ComicBookEntry.publisher, publisher);
            cv.put(ComicBookTableStructure.ComicBookEntry.series, series);

            long count = 0;

            try {
                //insert the new row
                count = dbComics.insertOrThrow(ComicBookTableStructure.ComicBookEntry.tableName, null, cv);
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }

            if (count == 0) {
                Toast.makeText(getApplicationContext(), "Error! Couldn't enter the new comic into the database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), series + " issue number " + aRange.get(i) + " was added to the collection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addComic(View v) {
        Intent i = new Intent(ViewComic.this, AddComic.class);
        i.putExtra("extra_data", true);
        i.putExtra("id", intID);
        startActivity(i);
    }
}
