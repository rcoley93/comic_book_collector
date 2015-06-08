package tk.rcoleyprogramming.comicbookcollector;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity {
    //set up the db
    ComicBookDatabaseHelper cbdbHelper = new ComicBookDatabaseHelper(MainActivity.this);
    TextView tvComics, tvSeries, tvPublishers, tvRead, tvBagged, tvBoarded;
    TextView tvSeries1, tvIssueNumber1, tvSeries2, tvIssueNumber2, tvSeries3, tvIssueNumber3, tvSeries4, tvIssueNumber4, tvSeries5, tvIssueNumber5;
    TextView[] tvRecentlyAdded = {tvSeries1, tvIssueNumber1, tvSeries2, tvIssueNumber2, tvSeries3, tvIssueNumber3, tvSeries4, tvIssueNumber4, tvSeries5, tvIssueNumber5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");

        tvComics = (TextView) findViewById(R.id.totalComics);
        tvSeries = (TextView) findViewById(R.id.totalSeries);
        tvPublishers = (TextView) findViewById(R.id.totalPublishers);
        tvRead = (TextView) findViewById(R.id.totalRead);
        tvBagged = (TextView) findViewById(R.id.totalBagged);
        tvBoarded = (TextView) findViewById(R.id.totalBoarded);

        tvRecentlyAdded[0] = (TextView) findViewById(R.id.series1);
        tvRecentlyAdded[5] = (TextView) findViewById(R.id.issueNumber1);
        tvRecentlyAdded[1] = (TextView) findViewById(R.id.series2);
        tvRecentlyAdded[6] = (TextView) findViewById(R.id.issueNumber2);
        tvRecentlyAdded[2] = (TextView) findViewById(R.id.series3);
        tvRecentlyAdded[7] = (TextView) findViewById(R.id.issueNumber3);
        tvRecentlyAdded[3] = (TextView) findViewById(R.id.series4);
        tvRecentlyAdded[8] = (TextView) findViewById(R.id.issueNumber4);
        tvRecentlyAdded[4] = (TextView) findViewById(R.id.series5);
        tvRecentlyAdded[9] = (TextView) findViewById(R.id.issueNumber5);

        getStatistics();
        getRecentComics();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();

        getStatistics();
        getRecentComics();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_import) {
            Intent intent = new Intent(MainActivity.this, importComics.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_delete_all) {
            SQLiteDatabase db = cbdbHelper.getWritableDatabase();
            db.execSQL(ComicBookTableStructure.SQL_DELETE_ENTRIES);
            db.execSQL(ComicBookTableStructure.SQL_CREATE_ENTRIES);
            db.close();

            getRecentComics();
            getStatistics();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAction(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnAddComic:
                i = new Intent(MainActivity.this, AddComic.class);
                i.putExtra("extra_data", false);
                startActivity(i);
                break;
            case R.id.btnSearch:
                i = new Intent(MainActivity.this, Search.class);
                startActivity(i);
                break;
            case R.id.btnViewSeries:
                i = new Intent(MainActivity.this, ViewSeries.class);
                startActivity(i);
                break;
        }
    }

    private void getRecentComics() {
        ComicBookDatabaseHelper cbDH = new ComicBookDatabaseHelper(this);
        SQLiteDatabase db = cbDH.getReadableDatabase();

        String strQuery = "SELECT " + ComicBookTableStructure.ComicBookEntry.series + "," + ComicBookTableStructure.ComicBookEntry.issueNumber + " FROM " + ComicBookTableStructure.ComicBookEntry.tableName + " ORDER BY " + ComicBookTableStructure.ComicBookEntry._ID + " DESC LIMIT 5";
        Cursor c = db.rawQuery(strQuery, null);
        c.moveToFirst();

        if(c.getCount() != 0) {
            for (int i = 0; i < 5; i++) {
                c.moveToPosition(i);
                tvRecentlyAdded[i].setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.series)));
                tvRecentlyAdded[i + 5].setText(c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.issueNumber)));
            }
        }else{
            tvRecentlyAdded[0].setText("No Comics have been Added!");
            tvRecentlyAdded[5].setText("");
            for(int i=1;i<5;i++){
                tvRecentlyAdded[i].setText("");
                tvRecentlyAdded[i + 5].setText("");
            }
        }

        c.close();
        db.close();
        cbDH.close();
    }

    private void getStatistics() {
        ComicBookDatabaseHelper cbDH = new ComicBookDatabaseHelper(this);
        SQLiteDatabase db = cbDH.getReadableDatabase();

        String strComicsQuery = "SELECT " + ComicBookTableStructure.ComicBookEntry.issueTitle + " FROM " + ComicBookTableStructure.ComicBookEntry.tableName;
        String strSeriesQuery = "SELECT DISTINCT " + ComicBookTableStructure.ComicBookEntry.series + " FROM " + ComicBookTableStructure.ComicBookEntry.tableName;
        String strPublisherQuery = "SELECT DISTINCT " + ComicBookTableStructure.ComicBookEntry.publisher + " FROM " + ComicBookTableStructure.ComicBookEntry.tableName;
        String strReadQuery = "SELECT " + ComicBookTableStructure.ComicBookEntry.readUnread + " FROM " + ComicBookTableStructure.ComicBookEntry.tableName + " WHERE " + ComicBookTableStructure.ComicBookEntry.readUnread + "='Read'";
        String strBaggedBoardedQuery = "SELECT " + ComicBookTableStructure.ComicBookEntry.storageMethod + " FROM " + ComicBookTableStructure.ComicBookEntry.tableName + " WHERE " + ComicBookTableStructure.ComicBookEntry.storageMethod + "='Bagged/Boarded'";
        String strBaggedQuery = "SELECT " + ComicBookTableStructure.ComicBookEntry.storageMethod + " FROM " + ComicBookTableStructure.ComicBookEntry.tableName + " WHERE " + ComicBookTableStructure.ComicBookEntry.storageMethod + "='Bagged'";

        int intComics, intSeries, intPublishers, intRead, intBagged, intBaggedBoarded;

        Cursor count = db.rawQuery(strComicsQuery, null);
        intComics = count.getCount();

        count = db.rawQuery(strSeriesQuery, null);
        intSeries = count.getCount();

        count = db.rawQuery(strPublisherQuery, null);
        intPublishers = count.getCount();

        count = db.rawQuery(strReadQuery, null);
        intRead = count.getCount();

        count = db.rawQuery(strBaggedBoardedQuery, null);
        intBaggedBoarded = count.getCount();

        count = db.rawQuery(strBaggedQuery, null);
        intBagged = count.getCount();

        intBagged += intBaggedBoarded;

        float fltReadPercent = (((float) intRead) / ((float) intComics))*100;
        float fltBaggedPercent = (((float) intBagged) / ((float) intComics))*100;
        float fltBoardedPercent = (((float) intBaggedBoarded) / ((float) intComics))*100;

        tvComics.setText(String.valueOf(intComics));
        tvPublishers.setText(String.valueOf(intPublishers));
        tvSeries.setText(String.valueOf(intSeries));

        if(Float.isNaN(fltBaggedPercent)) fltBaggedPercent = 0;
        if(Float.isNaN(fltBoardedPercent)) fltBoardedPercent = 0;
        if(Float.isNaN(fltReadPercent)) fltReadPercent = 0;

        tvBagged.setText(String.valueOf(intBagged) + " (" + new DecimalFormat("##.#").format(fltBaggedPercent) + "%)");
        tvBoarded.setText(String.valueOf(intBaggedBoarded) + " (" + new DecimalFormat("##.#").format(fltBoardedPercent) + "%)");
        tvRead.setText(String.valueOf(intRead) + " (" + new DecimalFormat("##.#").format(fltReadPercent) + "%)");

        db.close();
        cbDH.close();
    }
}
