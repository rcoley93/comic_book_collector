package tk.rcoleyprogramming.comicbookcollector;

/**
 * Created by Ryan on 7/28/2015.
 */
public class IssueSearchData {
    private SeriesSearchData.Responses response;

    public static class Results {
        private Issues issue;
    }

    public static class Issues {
        private String aliases, api_detail_url, cover_date, date_added, date_last_updated, deck,
                description, has_staff_review, name, site_detail_url, store_date;
        private int id, issue_number;
        private Images image;
        private Volumes volume;
    }

    public static class Images {
        private String icon_url, medium_url, screen_url, small_url, super_url, thumb_url, tiny_url;
    }

    public static class Volumes {
        private String api_detail_url, name, site_detail_url;
        private int id;
    }
}
