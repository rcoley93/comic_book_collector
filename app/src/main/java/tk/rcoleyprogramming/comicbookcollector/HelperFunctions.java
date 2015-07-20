package tk.rcoleyprogramming.comicbookcollector;

/**
 * Created by Ryan on 7/18/2015.
 */
public class HelperFunctions {
    public static String getDate(String s, String s1) {
        switch (s) {
            case " January":
                return "1/1/" + s1;
            case " February":
                return "2/1/" + s1;
            case " March":
                return "3/1/" + s1;
            case " April":
                return "4/1/" + s1;
            case " May":
                return "5/1/" + s1;
            case " June":
                return "6/1/" + s1;
            case " July":
                return "7/1/" + s1;
            case " August":
                return "8/1/" + s1;
            case " September":
                return "9/1/" + s1;
            case " October":
                return "10/1/" + s1;
            case " November":
                return "11/1/" + s1;
            case " December":
                return "12/1/" + s1;
        }
        return s + " " + s1;
    }


    public static String getGrade(String str) {
        switch (str) {
            case "Near Mint":
            case "Near Mint/Mint":
            case "Mint":
            case "NM Near Mint":
                return "NM Near Mint";
            case "Very Fine/Near Mint":
            case "Very Fine":
            case "VF Very Fine":
                return "VF Very Fine";
            case "Fine/Very Fine":
            case "Fine":
            case "FN Fine":
                return "FN Fine";
            case "Very Good/Fine":
            case "Very Good":
            case "VG Very Good":
                return "VG Very Good";
            case "Good/Very Good":
            case "Good":
            case "GD Good":
                return "GD Good";
            case "Fair/Good":
            case "Fair":
            case "FR Fair":
                return "FR Fair";
            case "Poor":
            case "PR Poor":
                return "PR Poor";
        }
        return "Unknown";
    }

    public static String getStorage(String str) {
        switch (str) {
            case "Bagged/Boarded":
                return str;
            case "Bagged":
                return str;
        }
        return "None";
    }

    public static String getReadUnread(String str) {
        if (str.equals("read")) return "Read";
        return "Unread";
    }
}
