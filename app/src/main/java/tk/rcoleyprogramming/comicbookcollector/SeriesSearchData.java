package tk.rcoleyprogramming.comicbookcollector;

/**
 * Created by Ryan on 7/28/2015.
 */
public class SeriesSearchData {
    private Responses response;

    public static class Responses {
        private String error;
        private int limit, offset, number_of_page_results, number_of_total_results, status_code;
        private Results results;
        private float version;

        public String getError() {
            return this.error;
        }
    }

    public static class Results {
        private Volumes[] volume;
    }

    public static class Volumes {
        String name, resource_type;
        Publishers publisher;
        private int count_of_issues, id;
    }

    public static class Publishers {
        private int id;
        private String api_detail_url, name;
    }
}
