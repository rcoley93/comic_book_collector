package tk.rcoleyprogramming.comicbookcollector;

import com.orm.SugarRecord;

/**
 * Created by Ryan on 7/25/2015.
 */
public class ComicBook extends SugarRecord<ComicBook> {
    private String series, issueNumber, issueTitle, coverDateMonth,
            coverDateYear, coverPrice, condition, storageMethod, pricePaid,
            writer, penciller, inker, colorist, letterer, editor,
            coverArtist, readUnread, dateAcquired, locationAcquired, comicLocation;
    private ComicPublisher publisher;

    public ComicBook() {
    }

    public ComicBook(String strSeries, String strIssueNumber, String strIssueTitle, String strPublisher, String strCoverDateMonth,
                     String strCoverDateYear, String strCoverPrice, String strCondition, String strStorageMethod, String strPricePaid,
                     String strWriter, String strPenciller, String strInker, String strColorist, String strLetterer, String strEditor,
                     String strCoverArtist, String strReadUnread, String strDateAcquired, String strLocationAcquired, String strComicLocation) {

        this.series = strSeries;
        this.issueNumber = strIssueNumber;
        this.issueTitle = strIssueTitle;
        this.publisher.setName(strPublisher);
        this.coverDateMonth = strCoverDateMonth;
        this.coverDateYear = strCoverDateYear;
        this.coverPrice = strCoverPrice;
        this.condition = strCondition;
        this.storageMethod = strStorageMethod;
        this.pricePaid = strPricePaid;
        this.writer = strWriter;
        this.penciller = strPenciller;
        this.inker = strInker;
        this.colorist = strColorist;
        this.letterer = strLetterer;
        this.editor = strEditor;
        this.coverArtist = strCoverArtist;
        this.readUnread = strReadUnread;
        this.dateAcquired = strDateAcquired;
        this.locationAcquired = strLocationAcquired;
        this.comicLocation = strComicLocation;

    }

    public ComicBook(String[] values) {
        this.series = values[0];
        this.issueNumber = values[1];
        this.issueTitle = values[2];
        this.publisher.setName(values[3]);
        this.coverDateMonth = (values[4].charAt(0) == ' ') ? values[4].substring(1) : values[4];
        this.coverDateYear = values[5];
        this.coverPrice = values[6];
        this.condition = values[7];
        this.storageMethod = values[8];
        this.comicLocation = values[9];
        this.pricePaid = values[10];
        this.writer = values[11];
        this.penciller = values[12];
        this.inker = values[13];
        this.colorist = values[14];
        this.letterer = values[15];
        this.editor = values[16];
        this.coverArtist = values[17];
        this.readUnread = values[18];
        this.dateAcquired = values[19];
        this.locationAcquired = values[20];
    }

    public ComicBook(String strSeries, String strIssueNumber, String strPublisher) {
        this.publisher.setName(strPublisher);
        this.series = strSeries;
        this.issueNumber = strIssueNumber;
    }

    public String[] getRecentDetails() {
        return new String[]{this.series, this.issueNumber};
    }

    public String[] getComicListElement() {
        return new String[]{this.issueTitle, this.issueNumber};
    }

    public String[] getSeriesListElement() {
        return new String[]{this.series, this.publisher.toString()};
    }

    public String[] getSearchListElement() {
        return new String[]{this.series, this.issueNumber, this.issueTitle, this.publisher.toString()};
    }

    public String[] getAllDetails() {
        return new String[]{
                this.series,            //0
                this.issueNumber,       //1
                this.issueTitle,        //2
                this.publisher.toString(),         //3
                this.coverDateMonth,    //4
                this.coverDateYear,     //5
                this.coverPrice,        //6
                this.condition,         //7
                this.storageMethod,     //8
                this.pricePaid,         //9
                this.writer,            //10
                this.penciller,         //11
                this.inker,             //12
                this.colorist,          //13
                this.letterer,          //14
                this.editor,            //15
                this.coverArtist,       //16
                this.readUnread,        //17
                this.dateAcquired,      //18
                this.locationAcquired,  //19
                this.comicLocation      //20
        };
    }

}
