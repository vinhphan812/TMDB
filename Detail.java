//change package to your project package
package <package_name>;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Detail {
    @SerializedName("backdrop_path")
    public String backdrop_path;
    @SerializedName("genres")
    public Genres[] genres;
    @SerializedName("homepage")
    public String homepage;
    @SerializedName("id")
    public int id;
    @SerializedName("original_language")
    public String original_language;
    @SerializedName("overview")
    public String overview;
    @SerializedName("popularity")
    public double popularity;
    @SerializedName("poster_path")
    public String poster_path;
    @SerializedName("production_companies")
    public Company[] production_companies;
    @SerializedName("production_countries")
    public Country[] production_countries;
    @SerializedName("spoken_languages")
    public Language[] spoken_languages;
    @SerializedName("status")
    public String status;
    @SerializedName("tagline")
    public String tagline;
    @SerializedName("vote_average")
    public double vote_average;
    @SerializedName("vote_count")
    public int vote_count;

    static class Genres {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;

    }

    static class Language {
        @SerializedName("english_name")
        public String english_name;
        @SerializedName("iso_639_1")
        public String iso_639_1;
        @SerializedName("name")
        public String name;
    }

    static class Country{
        @SerializedName("iso_3166_1")
        public String iso_3166_1;
        @SerializedName("name")
        public String name;
    }

    static class Company{
        @SerializedName("id")
        public int id;
        @SerializedName("logo_path")
        public String logo_path;
        @SerializedName("name")
        public String name;
        @SerializedName("origin_country")
        public String origin_country;
    }
}

class MovieDetail extends Detail {
    @SerializedName("adult")
    public boolean adult;
    @SerializedName("budget")
    public int budget;
    @SerializedName("imdb_id")
    public String imdb_id;
    @SerializedName("original_title")
    public String original_title;
    @SerializedName("release_date")
    public String release_date;
    @SerializedName("runtime")
    public int runtime;
    @SerializedName("title")
    public String title;
    @SerializedName("video")
    public boolean video;

    @NonNull
    public String toString(){
        return String.format("id: %d, title: %s, backdrop_path: %s", id, title, backdrop_path);
    }
}

class TVDetail extends Detail {
    @SerializedName("created_by")
    public Person[] created_by;
    @SerializedName("episode_run_time")
    public int[] episode_run_time;
    @SerializedName("first_air_date")
    public String first_air_date;
    @SerializedName("in_production")
    public boolean in_production;
    @SerializedName("languages")
    public String[] languages;
    @SerializedName("last_air_date")
    public String last_air_date;
    @SerializedName("last_episode_to_air")
    public LastEpisode last_episode_to_air;
    @SerializedName("name")
    public String name;
    @SerializedName("networks")
    public Networks[] networks;
    @SerializedName("number_of_episodes")
    public int number_of_episodes;
    @SerializedName("number_of_session")
    public int number_of_session;
    @SerializedName("origin_country")
    public String[] origin_country;
    @SerializedName("original_name")
    public String original_name;
    @SerializedName("seasons")
    public Season[] seasons;
    @SerializedName("type")
    public String type;

    static class Season{
        @SerializedName("air_date")
        public String air_date;
        @SerializedName("episode_count")
        public int episode_count;
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("overview")
        public String overview;
        @SerializedName("poster_path")
        public String poster_path;
        @SerializedName("season_number")
        public int season_number;
    }

    static class Networks{
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public int id;
        @SerializedName("logo_path")
        public String logo_path;
        @SerializedName("origin_country")
        public String origin_country;
    }

    static class LastEpisode{
        @SerializedName("air_date")
        public String air_date;
        @SerializedName("episode_number")
        public int episode_number;
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("overview")
        public String overview;
        @SerializedName("production_code")
        public String production_code;
        @SerializedName("season_number")
        public int season_number;
        @SerializedName("still_path")
        public String still_path;
        @SerializedName("vote_average")
        public double vote_average;
        @SerializedName("vote_count")
        public int vote_count;
    }

    static class Person{
        @SerializedName("id")
        public int id;
        @SerializedName("credit_id")
        public String credit_id;
        @SerializedName("name")
        public String name;
        @SerializedName("gender")
        public byte gender;
        @SerializedName("profile_path")
        public String profile_path;

    }

    public String toString(){
        return String.format("id: %d, title: %s, backdrop_path: %s", id, name, backdrop_path);
    }
}

class Ekip {
    @SerializedName("adult")
    public boolean adult;
    @SerializedName("gender")
    public int gender;
    /*
     * gender option
     * 0 - Not specified
     * 1 - Female
     * 2 - Male
     * */
    @SerializedName("id")
    public String id;
    @SerializedName("known_for_department")
    public String known_for_department;
    @SerializedName("name")
    public String name;
    @SerializedName("original_name")
    public String original_name;
    @SerializedName("popularity")
    public double popularity;
    @SerializedName("profile_path")
    public String profile_path;
    @SerializedName("credit_id")
    public String credit_id;
    @SerializedName("order")
    public int order;
}

class Cast extends Ekip{
    @SerializedName("character")
    public String character;
}

class Crew extends Ekip{
    @SerializedName("department")
    public String department;
    @SerializedName("job")
    public String job;
}