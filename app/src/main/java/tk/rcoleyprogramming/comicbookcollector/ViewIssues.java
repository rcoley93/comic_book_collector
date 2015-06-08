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
import java.util.List;


public class ViewIssues extends ActionBarActivity {
    String strSeries, strPublisher;
    ComicBookDatabaseHelper cbdbHelper = new ComicBookDatabaseHelper(this);
    private ArrayList<ComicListElement> aList;
    private MyAdapter aa;

    @Override
    protected void onResume() {
        super.onResume();
        getListOfComics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_issues);

        Intent i = getIntent();
        strSeries = i.getStringExtra("series");
        strPublisher = i.getStringExtra("publisher");

        //setup the list view
        aList = new ArrayList<ComicListElement>();
        aa = new MyAdapter(this, R.layout.comic_list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();

        //get the list of comics
        getListOfComics();
    }

    private void getListOfComics() {
        aList.clear();
        //get the database
        SQLiteDatabase db = cbdbHelper.getReadableDatabase();

        //set the query settings
        String[] strWantedValues = {ComicBookTableStructure.ComicBookEntry.issueNumber, ComicBookTableStructure.ComicBookEntry._ID, ComicBookTableStructure.ComicBookEntry.issueTitle};
        String strSortOrder = ComicBookTableStructure.ComicBookEntry.issueNumber + " ASC";
        String strWhere = ComicBookTableStructure.ComicBookEntry.series + " LIKE ?";
        String[] strWhereArgs = {strSeries};

        //get the results
        Cursor c = db.query(ComicBookTableStructure.ComicBookEntry.tableName, strWantedValues, strWhere, strWhereArgs, null, null, strSortOrder);

        //finish the activity if there are no more comics in the series
        if(c.getCount() == 0) this.finish();


        //parse the results
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            ComicListElement cle = new ComicListElement();

            //get the series name
            int intID = c.getInt(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry._ID));
            String intIssueNumber = c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.issueNumber));
            String strTitle = c.getString(c.getColumnIndexOrThrow(ComicBookTableStructure.ComicBookEntry.issueTitle));

            //set the values
            cle.id = intID;
            cle.issueNumber = intIssueNumber;
            cle.title = strTitle;

            aList.add(cle);
        }

        //close the db and cursor
        c.close();
        db.close();

        //update the list view
        aa.notifyDataSetChanged();
    }

    private class MyAdapter extends ArrayAdapter<ComicListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ComicListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
            this.context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            final ComicListElement w = getItem(position);

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
            TextView tvTitle = (TextView) newView.findViewById(R.id.itemComicTitle);
            TextView tvIssuesNumber = (TextView) newView.findViewById(R.id.itemIssueNumber);

            tvTitle.setText(w.title);
            tvIssuesNumber.setText(w.issueNumber);

            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewIssues.this, ViewComic.class);
                    intent.putExtra("extra_data", true);
                    intent.putExtra("id", w.id);
                    startActivity(intent);
                }

            });

            return newView;
        }
    }

    public void addComic(View v) {
        Intent i = new Intent(ViewIssues.this, AddComic.class);
        i.putExtra("extra_data", true);
        i.putExtra("series", strSeries);
        i.putExtra("publisher", strPublisher);
        startActivity(i);

    }
}
