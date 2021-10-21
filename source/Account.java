//change package to your project package
package <package_name>;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("iso_639_1")
    public String iso_639_1;
    @SerializedName("iso_3166_1")
    public String iso_3166_1;
    @SerializedName("include_adult")
    public boolean include_adult;
    @SerializedName("username")
    public String username;
    @SerializedName("avatar")
    public Avatar avatar;

    @Override
    public String toString() {
        return String.format("id: %s, name: %s", id, name.isEmpty() ? username : name);
    }
}

class Avatar {
    @SerializedName("tmdb")
    public TMDB tmdb;

    public static class TMDB{
        @SerializedName("avatar_path")
        public String avatar_path;
    }
}

