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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;


public class Search extends ActionBarActivity {
    EditText etSearchQuery;
    Spinner spCategory;
    private ArrayList<SearchListElement> aList;
    private MyAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearchQuery = (EditText) findViewById(R.id.searchQuery);
        spCategory = (Spinner) findViewById(R.id.category);

        //setup the list view
        aList = new ArrayList<SearchListElement>();
        aa = new MyAdapter(this, R.layout.search_list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    public void search(View v) {
        aList.clear();

        String strCategory = spCategory.getSelectedItem().toString();
        String strSearchType, strWhere;

        if (strCategory.equals("Title")) strSearchType = "issue_title";
        else strSearchType = "issue_number";
        strWhere = "'%" + etSearchQuery.getText().toString() + "%'";
        String strQuery = "SELECT * FROM " + ComicBook.getTableName(ComicBook.class) + " WHERE " + strSearchType + " LIKE " + strWhere;

        List<ComicBook> comics = ComicBook.findWithQuery(ComicBook.class, strQuery, null);

        for (int i = 0; i < comics.size(); ++i) {

            ComicBook current = comics.get(i);

            aList.add(new SearchListElement(current.getId(), current.getSearchListElement()));

        }

        aa.notifyDataSetChanged();

    }

    private class MyAdapter extends ArrayAdapter<SearchListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<SearchListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
            this.context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            final SearchListElement w = getItem(position);

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
            TextView tvTitle = (TextView) newView.findViewById(R.id.title);
            TextView tvIssuesNumber = (TextView) newView.findViewById(R.id.issueNumber);
            TextView tvPublisher = (TextView) newView.findViewById(R.id.publisher);
            TextView tvSeries = (TextView) newView.findViewById(R.id.series);

            tvTitle.setText(w.title);
            tvIssuesNumber.setText(String.valueOf(w.issueNumber));
            tvPublisher.setText(w.publisher);
            tvSeries.setText(w.series);

            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Search.this, ViewComic.class);
                    intent.putExtra("extra_data", true);
                    intent.putExtra("id", w.id);
                    startActivity(intent);
                }

            });

            return newView;
        }
    }
}
