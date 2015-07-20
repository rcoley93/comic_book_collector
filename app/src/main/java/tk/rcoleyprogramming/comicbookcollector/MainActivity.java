package tk.rcoleyprogramming.comicbookcollector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import org.apache.poi.ss.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class MainActivity extends ActionBarActivity {

    //set up the db
    ComicBookDatabaseHelper cbdbHelper = new ComicBookDatabaseHelper(MainActivity.this);
    TextView tvComics, tvSeries, tvPublishers, tvRead, tvBagged, tvBoarded;
    TextView tvSeries1, tvIssueNumber1, tvSeries2, tvIssueNumber2, tvSeries3, tvIssueNumber3, tvSeries4, tvIssueNumber4, tvSeries5, tvIssueNumber5;
    TextView[] tvRecentlyAdded = {tvSeries1, tvIssueNumber1, tvSeries2, tvIssueNumber2, tvSeries3, tvIssueNumber3, tvSeries4, tvIssueNumber4, tvSeries5, tvIssueNumber5};

    //alert dialog
    AlertDialog.Builder dialogBuildExport;
    AlertDialog dialogExport;

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

        switch (id) {
            case R.id.action_import:
                Intent intent = new Intent(MainActivity.this, importComics.class);
                startActivity(intent);
                return true;
            case R.id.action_delete_all:
                SQLiteDatabase db = cbdbHelper.getWritableDatabase();
                db.execSQL(ComicBookTableStructure.SQL_DELETE_ENTRIES);
                db.execSQL(ComicBookTableStructure.SQL_CREATE_ENTRIES);
                db.close();
                getRecentComics();
                getStatistics();
                return true;
            case R.id.action_about:
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("About");
                alertDialog.setMessage("Thanks!\nBat and Magazine icon designed by Freepik\nDatabase icon designed by Designmodo\n File Picker Library coded by evandrocontato");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;
            case R.id.action_export:
                createDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_export_comics);
        dialog.setTitle("Export Comics");

        Button dialogButton = (Button) dialog.findViewById(R.id.btnExport);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spnExport = (Spinner) dialog.findViewById(R.id.export_type);
                if (sdEnabled()) {
                    exportComicBooks ec = new exportComicBooks(spnExport.getSelectedItem().toString());
                    ec.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Error! Unable to write to external storage!", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
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

    private boolean sdEnabled() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) return false;
        return true;
    }

    public void createExportDialog() {
        dialogBuildExport = new AlertDialog.Builder(MainActivity.this);
        dialogBuildExport.setTitle("Exporting.....");
        dialogBuildExport.setMessage("Exporting Comics!");

        dialogExport = dialogBuildExport.create();
        dialogExport.show();
    }

    public void setExportDialogMessage(String str) {
        dialogExport.setMessage(str);
    }

    public void dismissDialog() {
        dialogExport.dismiss();
    }

    private class exportComicBooks extends AsyncTask<String, String, String> {

        ComicBookDatabaseHelper cbdbHelper;
        private String exportType;

        exportComicBooks(String strExportType) {
            exportType = strExportType;
            cbdbHelper = new ComicBookDatabaseHelper(getApplicationContext());
        }

        @Override
        protected String doInBackground(String... strings) {
            final String[] categories = {"Title", "Issue Number", "Issue Name", "Publisher", "Cover Date Month",
                    "Cover Date Year", "Cover Price", "Condition", "Storage Method", "Storage Location", "Price Paid",
                    "Writer(s)", "Penciller(s)", "Inker(s)", "Colorist(s)", "Letterer(s)", "Editor(s)",
                    "Cover Artist(s)", "Read/Unread", "Date Acquired", "Location Acquired"};

            SQLiteDatabase dbComics = cbdbHelper.getReadableDatabase();

            String strQuery = "SELECT * FROM " + ComicBookTableStructure.ComicBookEntry.tableName;
            Cursor c = dbComics.rawQuery(strQuery, null);
            c.moveToFirst();
            int totalComics = c.getCount(), currentComic = 0;

            if (c.getCount() == 0) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error! There are no Comics to export!", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            switch (exportType) {
                case "CSV":
                    String csv = "";

                    Storage storageCSV = SimpleStorage.getExternalStorage();
                    storageCSV.createDirectory("Exports");
                    storageCSV.createFile("Exports", "collection.csv", "");

                    for (int i = 0; i < categories.length; i++) {
                        if (i != categories.length - 1) {
                            csv += categories[i] + ",";
                        } else {
                            csv += categories[i];
                        }
                    }

                    storageCSV.appendFile("Exports", "collection.csv", csv);
                    csv = "";

                    do {
                        for (int i = 1; i < c.getColumnCount(); i++) {
                            csv += "\"";
                            if (i != c.getColumnCount() - 1) csv += c.getString(i) + "\",";
                            else csv += c.getString(i) + "\"";
                        }
                        storageCSV.appendFile("Exports", "collection.csv", csv);
                        csv = "";
                        publishProgress(Integer.toString(++currentComic), Integer.toString(totalComics));
                    } while (c.moveToNext());
                    break;
                case "HTML":
                case "PDF":
                    String htmlHeader = "<!DOCTYPE HTML>\n" +
                            "<html>\n" +
                            "\t<head>\n" +
                            "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf=8\" />\n" +
                            "\t\t<title>Comic Book Collection</title>\n" +
                            "\t</head>\n" +
                            "\t<body>\n" +
                            "\t\t<center><h1>Comic Book Collection</h1></center><br />\n" +
                            "\t\t<table border=\"1\">\n" +
                            "\t\t\t<tr>";
                    String htmlFooter = "\t\t</table>\n\t</body>\n</html>";

                    Storage storageHTML = SimpleStorage.getExternalStorage();
                    storageHTML.createDirectory("Exports");
                    storageHTML.createFile("Exports", "collection.html", htmlHeader);

                    for (int i = 0; i < categories.length; ++i) {
                        String str = "\t\t\t\t<th>" + categories[i] + "</th>";
                        storageHTML.appendFile("Exports", "collection.html", str);
                    }

                    storageHTML.appendFile("Exports", "collection.html", "\t\t\t</tr>");

                    do {
                        String html = "\t\t\t<tr>";
                        storageHTML.appendFile("Exports", "collection.html", html);
                        for (int i = 1; i < c.getColumnCount(); i++) {
                            html = "\t\t\t\t<td>" + c.getString(i) + "</td>";
                            storageHTML.appendFile("Exports", "collection.html", html);
                        }
                        storageHTML.appendFile("Exports", "collection.html", "\t\t\t</tr>");
                        publishProgress(Integer.toString(++currentComic), Integer.toString(totalComics));
                    } while (c.moveToNext());

                    storageHTML.appendFile("Exports", "collection.html", htmlFooter);

                    if (exportType.equals("PDF")) {
                        File sdCard = Environment.getExternalStorageDirectory();
                        File dir = new File(sdCard.getAbsolutePath() + "/Exports");
                        File pdf = new File(dir, "collection.pdf");
                        File html = new File(dir, "collection.html");

                        Document document = new Document(PageSize.A1, 75, 75, 50, 50);

                        try {
                            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdf));

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    setExportDialogMessage("Saving PDF!");
                                }
                            });

                            document.open();

                            try {
                                XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(html));
                            } catch (IOException ex) {
                                Log.wtf("PDF", "FILE IO EXCEPTION! " + ex.toString());
                            }

                            document.close();
                        } catch (FileNotFoundException ex) {
                            Log.wtf("PDF", "FILE NOT FOUND! " + ex.toString());
                        } catch (DocumentException ex) {
                            Log.wtf("PDF", "Document Exception! " + ex.toString());
                        }
                        storageHTML.deleteFile("Exports", "collection.html");
                    }
                    break;
                case "Excel":
                    //set up the workbook
                    Workbook wb = new HSSFWorkbook();

                    //set up the sheet
                    final Sheet sheet = wb.createSheet("Comic Book Collection");
                    PrintSetup printSetup = sheet.getPrintSetup();
                    printSetup.setLandscape(true);
                    //the following three statements are required only for HSSF
                    sheet.setAutobreaks(true);
                    printSetup.setFitHeight((short) 1);
                    printSetup.setFitWidth((short) 1);

                    //set up the styles
                    //title
                    CellStyle title = wb.createCellStyle();
                    Font titleFont = wb.createFont();
                    titleFont.setFontHeightInPoints((short) 36);
                    titleFont.setBold(true);
                    title.setFont(titleFont);
                    title.setAlignment(CellStyle.ALIGN_CENTER);
                    //header
                    CellStyle header = wb.createCellStyle();
                    Font headerFont = wb.createFont();
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setBold(true);
                    header.setFont(headerFont);
                    header.setAlignment(CellStyle.ALIGN_CENTER);
                    //values
                    CellStyle values = wb.createCellStyle();
                    Font valuesFont = wb.createFont();
                    valuesFont.setFontHeightInPoints((short) 12);
                    values.setFont(valuesFont);

                    //set up the title
                    Row row = sheet.createRow(0);
                    row.setHeightInPoints((float) 46.50);
                    Cell cell = row.createCell(0);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 20));
                    cell.setCellValue("Comic Book Collection");
                    cell.setCellStyle(title);


                    //create the header
                    row = sheet.createRow(1);

                    for (int i = 0; i < categories.length; ++i) {
                        cell = row.createCell(i);
                        cell.setCellValue(categories[i]);
                        cell.setCellStyle(header);
                    }

                    //add the rest of the values
                    int rowCount = 2;

                    do {
                        row = sheet.createRow(rowCount++);

                        for (int i = 1; i < c.getColumnCount(); i++) {
                            cell = row.createCell(i - 1);
                            cell.setCellStyle(values);
                            cell.setCellValue(c.getString(i));
                        }

                        publishProgress(Integer.toString(++currentComic), Integer.toString(totalComics));
                    } while (c.moveToNext());


                    /*for(int i = 0;i<categories.length;++i){
                        sheet.autoSizeColumn(i);
                    }*/

                    runOnUiThread(new Runnable() {
                        public void run() {
                            setExportDialogMessage("Saving Excel file!");
                        }
                    });

                    //save file
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdCard.getAbsolutePath() + "/Exports");
                    dir.mkdir();
                    File xls = new File(dir, "collection.xls");
                    try {
                        FileOutputStream out = new FileOutputStream(xls);
                        wb.write(out);
                        out.close();
                    } catch (FileNotFoundException ex) {
                        Log.wtf("EXCEL", "FILE NOT FOUND! " + ex.toString());
                    } catch (IOException ex) {
                        Log.wtf("EXCEL", "IO ERROR! " + ex.toString());
                    }
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            Toast.makeText(getApplicationContext(), "Completed Export of Comics!", Toast.LENGTH_LONG).show();
            dismissDialog();
        }

        @Override
        protected void onPreExecute() {
            createExportDialog();
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
            setExportDialogMessage("Exported Comic " + text[0] + " of " + text[1] + "!");
        }
    }
}
