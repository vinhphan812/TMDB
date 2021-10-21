//change package to your project package
package <package_name>;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Entertainment contain basic property of the <b>movie</b>, <b>tv</b> and <b>person</b>
 */
public class Entertainment {
    @SerializedName("id")
    public String id;
    @SerializedName("vote_count")
    public int vote_count;
    @SerializedName("poster_path")
    public String poster_path;
    @SerializedName("overview")
    public String overview;
    @SerializedName("backdrop_path")
    public String backdrop_path;
    @SerializedName("original_language")
    public String original_language;
    @SerializedName("media_type")
    public String media_type = "";
    @SerializedName("genre_ids")
    public int[] genre_ids;
    @SerializedName("vote_average")
    public double vote_average;
    @SerializedName("popularity")
    public double popularity;
}

class MovieItem extends Entertainment {
    @SerializedName("title")
    public String title;
    @SerializedName("original_title")
    public String original_title;
    @SerializedName("release_date")
    public String release_date;
    @SerializedName("video")
    public Boolean video;
    @SerializedName("adult")
    public Boolean adult;

    public String toString() {
        return String.format("id: %s, title: %s, original_title: %s, media_type: %s", id, title, original_title, media_type);
    }
}

class TVItem extends Entertainment {
    @SerializedName("name")
    public String name;
    @SerializedName("original_name")
    public String original_name;
    @SerializedName("first_air_date")
    public String first_air_date;
    @SerializedName("origin_country")
    public String[] origin_country;

    @Override
    public String toString() {
        return String.format("id: %s, title: %s, original_title: %s, media_type: %s", id, name, original_name, media_type);
    }
}

class PersonItem extends Entertainment {
    @SerializedName("adult")
    public boolean adult;
    /*
     * gender option
     * 0 - Not specified
     * 1 - Female
     * 2 - Male
     * */
    @SerializedName("gender")
    public int gender;
    /*
     * include: MovieItem, TVItem
     * using Factory class to create List
     * */
    public List<Entertainment> known_for;

    @SerializedName("known_for_department")
    public String known_for_department;
    @SerializedName("name")
    public String name;
    @SerializedName("profile_path")
    public String profile_path;
}