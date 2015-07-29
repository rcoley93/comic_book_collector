package tk.rcoleyprogramming.comicbookcollector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    long lngID;

    @Override
    protected void onResume() {
        super.onResume();
        viewComic();
    }

    public void viewComic() {

        ComicBook comic = ComicBook.findById(ComicBook.class, lngID);

        String strCoverPriceRaw = comic.getAllDetails()[6];
        String strPricePaidRaw = comic.getAllDetails()[9];
        String strIssueNumberRaw = comic.getAllDetails()[1];

        //set all the text views to the appropriate text
        String strPricePaid = (strPricePaidRaw.length() > 5) ? strPricePaidRaw.substring(4) : "";
        String strCoverPrice = (strCoverPriceRaw.length() > 5) ? strCoverPriceRaw.substring(4) : "";
        String strIssueNumber = (strIssueNumberRaw.charAt(0) == '#') ? strIssueNumberRaw.substring(1) : strIssueNumberRaw;

        if (strPricePaid == "") strPricePaid = "Free";
        else if (strPricePaid.length() == 3) strPricePaid += "0";

        if (strCoverPrice == "") strCoverPrice = "Free";
        else if (strCoverPrice.length() == 3) strCoverPrice += "0";

        etSeries.setText(comic.getAllDetails()[0]);
        etIssueNumber.setText(strIssueNumber);
        etIssueTitle.setText(comic.getAllDetails()[2]);
        etPublisher.setText(comic.getAllDetails()[3]);
        etCoverDate.setText(comic.getAllDetails()[4] + " " + comic.getAllDetails()[5]);
        etCoverPrice.setText(strCoverPrice);
        etGrade.setText(HelperFunctions.getGrade(comic.getAllDetails()[7]));
        etStorageMethod.setText(HelperFunctions.getStorage(comic.getAllDetails()[8]));
        etPricePaid.setText(strPricePaid);
        etWriter.setText(comic.getAllDetails()[10]);
        etPenciller.setText(comic.getAllDetails()[11]);
        etInker.setText(comic.getAllDetails()[12]);
        etColorist.setText(comic.getAllDetails()[13]);
        etLetterer.setText(comic.getAllDetails()[14]);
        etEditor.setText(comic.getAllDetails()[15]);
        etCoverArtist.setText(comic.getAllDetails()[16]);
        etReadUnread.setText(HelperFunctions.getReadUnread(comic.getAllDetails()[17]));
        etDateAcquired.setText(comic.getAllDetails()[18]);
        etLocationAcquired.setText(comic.getAllDetails()[19]);
        etComicLocation.setText(comic.getAllDetails()[20]);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comic);

        Intent i = getIntent();
        lngID = i.getLongExtra("id", -1);

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
                ComicBook.findById(ComicBook.class, lngID).delete();
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
            ComicBook newComic = new ComicBook(series, String.valueOf(aRange.get(i)), publisher);
            newComic.save();
            Toast.makeText(getApplicationContext(), series + " issue number " + aRange.get(i) + " was added to the collection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addComic(View v) {
        Intent i = new Intent(ViewComic.this, AddComic.class);
        i.putExtra("extra_data", true);
        i.putExtra("id", lngID);
        startActivity(i);
    }
}
