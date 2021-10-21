//change package to your project package
package <package_name>;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Factory</b> contain method convert JsonArray to <strong>Entertainment List</strong> (<i>MovieItem</i>, <i>TVItem</i>, <i>PersonItem</i>)
 *
 */
public class Factory {
    static Gson gson = new Gson();
    public Factory() {
    }

    /**
     * @param jsonArray is a Json Object Array contain: movie and tv
     * @return <b>Entertainment</b> List contain: <i>MovieItem</i>, <i>TVItem</i>
     */
    static public List<Entertainment> MvAndTv(JsonArray jsonArray) {

        List<Entertainment> list = new ArrayList<>();

        for (JsonElement item : jsonArray) {
            JsonObject obj = item.getAsJsonObject();
            String type = obj.get("media_type").getAsString();

            boolean checkType = type.equals("tv") || !obj.has("title");
            list.add(gson.fromJson(obj, checkType ? TVItem.class : MovieItem.class));
        }
        return list;
    }

    /**
     * @param JsonArray is a Json Object Array contain: movie, tv and person.
     * @return <b>Entertainment</b> List contain: <i>MovieItem</i>, <i>TVItem</i> and <i>PersonItem</i>
     */
    static public List<Entertainment> MultiSearchList(JsonArray JsonArray) {
        List<Entertainment> list = new ArrayList<>();

        for (JsonElement item : JsonArray) {
            JsonObject obj = item.getAsJsonObject();
            String type = obj.get("media_type").getAsString();
            list.add(gson.fromJson(obj, type.equals("tv") ? TVItem.class : type.equals("movie") ? MovieItem.class : PersonItem.class));
        }

        return list;
    }
}
