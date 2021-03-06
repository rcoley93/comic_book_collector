package tk.rcoleyprogramming.comicbookcollector;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;


public class importComics extends ActionBarActivity {

    public final int FILE_CHOOSER = 0;

    //alert dialog
    AlertDialog.Builder dialogBuild;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_comics);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_CHOOSER:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getStringExtra("fileSelected");
                    importComicbooks ic = new importComicbooks(FilePath, importComics.this);
                    ic.execute();
                }
                break;

        }
    }

    public void importComic(View v) {
        Intent i = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(".csv");
        i.putExtra("filterFileExtension",extensions);
        startActivityForResult(i, FILE_CHOOSER);
    }

    public void createImportDialog(){
        dialogBuild = new AlertDialog.Builder(importComics.this);
        dialogBuild.setTitle("Importing.....");
        dialogBuild.setMessage("Adding Comics!");

        dialog = dialogBuild.create();
        dialog.show();
    }

    public void setImportDialogMessage(String str){
        dialog.setMessage(str);
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

    private class importComicbooks extends AsyncTask<String, String, String> {

        List<String[]> list = new ArrayList<>();
        Context c;
        private String resp, fileName;

        importComicbooks(String filename, Context context) {
            fileName = filename;
            c = context;
        }

        private void processFile(String file) {
            CSVReader csv;
            try {
                csv = new CSVReader(new InputStreamReader(new FileInputStream(file)));
                for (; ; ) {
                    String[] next = csv.readNext();
                    if (next != null) list.add(next);
                    else break;
                }
            } catch (FileNotFoundException ex) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error: File not found!", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (final IOException ex) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error: I/O exception " + ex.toString() + "!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String[] row = list.get(0);

            if (row.length != 21) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error: Make sure that file is formatted correctly! Categories!", Toast.LENGTH_SHORT).show();
                    }
                });
                return resp;
            } else {
                String[] categories = {"Title", "Issue Number", "Issue Name", "Publisher", "Cover Date Month",
                        "Cover Date Year", "Cover Price", "Condition", "Storage Method", "Storage Location", "Price Paid",
                        "Writer(s)", "Penciller(s)", "Inker(s)", "Colorist(s)", "Letterer(s)", "Editor(s)",
                        "Cover Artist(s)", "Read/Unread", "Date Acquired", "Location Acquired"};
                for (int i = 0; i < 21; ++i) {
                    if (!categories[i].equals(row[i])) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Error! Import File not formatted correctly!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return resp;
                    }
                }
                for (int i = 1; i < list.size(); ++i) {
                    row = list.get(i);
                    for (int j = 0; j < row.length; ++j) {
                        row[j] = row[j].replace("\"", "");
                        row[j].trim();
                    }

                    ComicBook newComic = new ComicBook(row);
                    newComic.save();

                    publishProgress(String.valueOf(i), String.valueOf(list.size() - 1));
                }
            }
            resp = "Added Comics to database!";
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            dismissDialog();
            Toast.makeText(getApplicationContext(), "Completed Import of Comics!", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            createImportDialog();
            processFile(fileName);
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
           setImportDialogMessage("Added Comic " + text[0] + " of " + text[1] + "!");
        }
    }
}
