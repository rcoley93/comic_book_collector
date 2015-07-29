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


public class ViewSeries extends ActionBarActivity {

    String strPublisher = null;
    private ArrayList<SeriesListElement> aList;
    private MyAdapter aa;

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

    @Override
    protected void onResume() {
        super.onResume();
        getListOfSeries();
    }

    private void getListOfSeries() {
        aList.clear();

        List<ComicBook> Comics = ComicBook.findWithQuery(ComicBook.class, "SELECT DISTINCT series,publisher FROM " + ComicBook.getTableName(ComicBook.class), null);

        //parse the results
        for (int i = 0; i < Comics.size(); i++) {

            ComicBook comic = Comics.get(i);

            //get number of issues in the series
            String strQuery = "series LIKE \"" + comic.getAllDetails()[0] + "\"";
            Long count = ComicBook.count(ComicBook.class, strQuery, null);

            aList.add(new SeriesListElement(count, comic.getSeriesListElement()));
        }

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

            String strLabel = (w.issueCount < 2) ? " issue" : " issues";
            tvSeries.setText(w.series);
            tvIssuesNumber.setText(String.valueOf(w.issueCount) + strLabel);
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
