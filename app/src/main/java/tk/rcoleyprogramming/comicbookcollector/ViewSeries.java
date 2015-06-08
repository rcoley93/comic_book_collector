package tk.rcoleyprogramming.comicbookcollector;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ViewSeries extends ActionBarActivity {

    ComicBookDatabaseHelper cbdbHelper = new ComicBookDatabaseHelper(this);
    private ArrayList<SeriesListElement> aList;
    private MyAdapter aa;
    String strPublisher = null;

    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_view_series);


        /*Intent i = getIntent();
        strPublisher = i.getStringExtra("publisher");*/


        //setup the list view
        aList = new ArrayList<SeriesListElement>();
        aa = new MyAdapter(this, R.layout.series_list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();

        //get the list of comics
        getListOfSeries();
    }

    private void getListOfSeries() {
        aList.clear();
        //get the database
        SQLiteDatabase db = cbdbHelper.getReadableDatabase();

        //set the query
        String strQuery = "SELECT DISTINCT " + ComicBookTableStructure.ComicBookEntry.series + "," + ComicBookTableStructure.ComicBookEntry.publisher + " FROM " + ComicBookTableStructure.ComicBookEntry.tableName;
        //if(strPublisher != null && strPublisher != "") strQuery += " WHERE " + ComicBookTableStructure.ComicBookEntry.series + "='" + strPublisher+"'";

        //get the results
        Cursor c = db.rawQuery(strQuery, null);

        //parse the results
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            //get the series name
            String strSeries = c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.series));
            String strPublisher = c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.publisher));

            //get number of issues in the series
            strQuery = "SELECT * FROM " + ComicBookTableStructure.ComicBookEntry.tableName + " WHERE " + ComicBookTableStructure.ComicBookEntry.series + " LIKE \"" + strSeries + "\"";
            Cursor cCount = db.rawQuery(strQuery, null);

            //get the result
            int intCount = cCount.getCount();
            cCount.close();

            aList.add(new SeriesListElement(strSeries, strPublisher, intCount));
        }

        //close the db and cursor
        c.close();
        db.close();
        //update the list view
        aa.notifyDataSetChanged();
    }

    private class MyAdapter extends ArrayAdapter<SeriesListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, ArrayList<SeriesListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
            this.context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            final SeriesListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView tvSeries = (TextView) newView.findViewById(R.id.itemSeriesTitle);
            TextView tvIssuesNumber = (TextView) newView.findViewById(R.id.itemNumberOfIssues);
            TextView tvPublisher = (TextView) newView.findViewById(R.id.itemPublisher);

            String strLabel = (w.issueNumber < 2) ? " issue" : " issues";
            tvSeries.setText(w.series);
            tvIssuesNumber.setText(String.valueOf(w.issueNumber) + strLabel);
            tvPublisher.setText(w.publisher);

            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewSeries.this, ViewIssues.class);
                    intent.putExtra("series", w.series);
                    intent.putExtra("publisher", w.publisher);
                    startActivity(intent);
                }

            });

            return newView;
        }
    }

}
