//change package to your project package
package <package_name>;

import static java.lang.Thread.sleep;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MovieAPI{
    public Account account;

    public String request_token, session_id;
    public ListData<Entertainment> trending, search;
    public Boolean isAccess;

    public ListData<MovieItem> top_rated_movie, similar_movie, popular_movie, upcoming, my_favorite_movie, my_rated_movie, my_watchlist_movie;

    public ListData<TVItem> top_rated_tv, similar_tv, popular_tv, my_favorite_tv, my_rated_tv, my_watchlist_tv;

    public Detail.Genres[] genres_movie, genres_tv;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String APIKEY;

    final private Gson gson = new Gson();

    final private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * <i>constructor build a Object</i>
     *
     * @param APIKEY from the movie db api
     */
    public MovieAPI(String APIKEY) {
        this.APIKEY = APIKEY;
        isAccess = checkAPIKey();
        if (isAccess)
            Init();
    }
    
    private Boolean checkSession() {
        getDetailAccount();
        System.out.println("account " + account);
        return account != null;
    }

    public MovieAPI(String APIKEY, String session_id) {
        this.APIKEY = APIKEY;
        this.session_id = session_id;
        isAccess = checkAPIKey() && checkSession();

        if (isAccess)
            Init();
    }

    private void Init() {
        //region Variable basic
        search = new ListData<>();
        trending = new ListData<>();
        top_rated_movie = new ListData<>();
        top_rated_tv = new ListData<>();
        similar_movie = new ListData<>();
        similar_tv = new ListData<>();
        popular_movie = new ListData<>();
        popular_tv = new ListData<>();
        upcoming = new ListData<>();
        genres_movie = getGenres(true);
        genres_tv = getGenres(false);
        //endregion

        //region Variable with account
        my_favorite_movie = new ListData<>();
        my_favorite_tv = new ListData<>();
        my_rated_movie = new ListData<>();
        my_rated_tv = new ListData<>();
        my_watchlist_movie = new ListData<>();
        my_watchlist_tv = new ListData<>();

        //endregion
    }

    //region Request Methods

    /**
     * Make <b>GET</b> request with <strong>path</strong>
     * <p><strong>EXAMPLE PATH</strong> <b>/movie/547565</b></p>
     *
     * @param path is a string pathname
     * @return Response JSON String is a result from server
     */
    private String requestServer(String path) {
        return requestServer(path, "", null);
    }

    /**
     * make <b>GET</b> request with <strong>path</strong> and <strong>params</strong>
     *
     * @param path   is a pathname
     * @param params is a <b>String Parameter</b>
     * @return Response JSON String is a result from server
     */
    private String requestServer(String path, String params) {
        return requestServer(path, params, null);
    }

    /**
     * make <b>POST</b> request with <strong>path</strong> and <strong>JSON body</strong>
     *
     * @param path is a pathname.
     * @param body is a JSON string
     * @return Response JSON string is a result from server
     */
    private String requestServer(String path, RequestBody body) {
        return requestServer(path, "", body);
    }

    /**
     * Make HTTP/HTTPS request to the movie api <strong>https://api.themoviedb.org/3<b>{{path}}</b>?<b>{{params}}</b></strong>
     * <p><strong>EXAMPLE PATH</strong> <b>/authentication/session/new</b></p>
     *
     * @param path   is a pathname
     * @param params is a <b>String Parameter</b>
     * @param body   is a JSON send to server
     * @return Response JSON String is a result from server
     */
    @Nullable
    private String requestServer(String path, String params, RequestBody body) {
        // declare responseString and status response for request
        final String[] res = {null};
        final boolean[] status = {false};

        //not access => error
        if (!isAccess) return null;

        // make url with APIKEY & language
        String HOST = "https://api.themoviedb.org/3";
        String url = HOST + path + "?language=vi&api_key=" + APIKEY + (params.isEmpty() ? "" : "&" + params) + (session_id == null ? "" : "&session_id=" + session_id);

        System.out.println(url);
        Request request = new Request.Builder().method(body == null ? "GET" : "POST", body).url(url).build();

        // send request and listen event response
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println(e);
                status[0] = true;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    res[0] = responseBody.string();
                    status[0] = true;
                }
            }
        });

        // await response from server (tired)
        while (true) {
            if (status[0]) break;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        return res[0];
    }

    private String deleteRequest(String path) {
        // declare responseString and status response for request
        final String[] res = {null};
        final boolean[] status = {false};

        //not access => error
        if (!isAccess) return null;

        // make url with APIKEY & language
        String HOST = "https://api.themoviedb.org/3";
        String url = HOST + path + "?language=vi&api_key=" + APIKEY + (session_id == null ? "" : "&session_id=" + session_id);

        System.out.println(url);
        Request request = new Request.Builder().method("DELETE", null).url(url).build();

        // send request and listen event response
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println(e);
                status[0] = true;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    res[0] = responseBody.string();
                    status[0] = true;
                }
            }
        });

        // await response from server (tired)
        while (true) {
            if (status[0]) break;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        return res[0];

    }
    //endregion

    /**
     * Validate <i>APIKEY</i>
     *
     * @return boolean indicates valid
     */
    private Boolean checkAPIKey() {
        // bypass requestServer
        isAccess = true;
        String response = requestServer("/authentication/guest_session/new");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (!json.get("success").getAsBoolean()) return false;
        //session_id = json.get("guest_session_id").getAsString();
        return true;
    }

    /**
     * NOTE: <strong>All requested data is stored in <b>genres_movie</b> or <b>genres_tv</b> variable of object's</strong>
     *
     * @param type <i>true</i> is a <b>movie</b>, <i>false</i> is a <b>tv</b>
     * @return Array <i>Genres</i> (Category)
     */
    private Detail.Genres[] getGenres(boolean type) {
        String response = requestServer(String.format("/genre/%s/list", type ? "movie" : "tv"));

        if (response == null) return null;

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        return gson.fromJson(jsonObject.get("genres"), Detail.Genres[].class);
    }

    /**
     * <p><i>Get trending list</i></p>
     * NOTE: <strong>All requested data from <i>first page</i> to <i>n page</i> is stored in <b>trending</b> variable of Object's</strong>
     *
     * @return List of Entertainment <b>trending</b> contain <b>MovieItem</b> and <b>TVItem</b>
     */
    public List<Entertainment> getTrending() {
        String response = requestServer("/trending/all/day");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        JsonArray result = json.get("results").getAsJsonArray();

        trending.nextPage++;
        trending.list.addAll(Factory.MvAndTv(result));

        return trending.list;
    }

    //region Detail Methods

    /**
     * <p><i>Get detail tv from tv id</i></p>
     *
     * @param id   is a <i>movie_id</i> or <i>tv_id</i>
     * @param type <i>true</i> is a <b>movie</b>, <i>false</i> is a <b>tv</b>
     * @return <i>Detail</i> then can parse to <i>MovieDetail</i> or <i>TVDetail</i>
     */
    private Detail getDetail(String id, boolean type) {
        String response = requestServer(String.format("/%s/%s", type ? "movie" : "tv", id));

        if (response == null) return null;

        return gson.fromJson(response, type ? MovieDetail.class : TVDetail.class);
    }

    /**
     * <i>Get detail movie from movie id</i>
     *
     * @param id is a <i>movie_id</i>
     * @return <i>MovieDetail</i> Object
     */
    public MovieDetail getDetailMovie(String id) {
        return (MovieDetail) getDetail(id, true);
    }

    /**
     * <i>Get detail tv show</i>
     *
     * @param id is a tv_id
     * @return <i>TVDetail</i> Object
     */
    public TVDetail getDetailTV(String id) {
        return (TVDetail) getDetail(id, false);
    }
    //endregion

    //region Top Rated Methods

    /**
     * <p><i>Get list movie top rated</i></p>
     * NOTE: <strong>All requested data from <i>first page</i> to <i>n page</i> is stored in <b>list_movie</b> variable of Object's</strong>
     *
     * @return List of <i>MovieItem</i>
     */
    public List<MovieItem> getTopRatedMovie() {
        String response = requestServer("/movie/top_rated", "page=" + top_rated_movie.nextPage);

        if (response == null) return null;

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        Type listType = new TypeToken<List<MovieItem>>() {
        }.getType();

        top_rated_movie.nextPage++;

        top_rated_movie.list.addAll(gson.fromJson(jsonObject.get("results"), listType));

        return top_rated_movie.list;
    }

    /**
     * <p><i>Get list tv top rated</i></p>
     * NOTE: <strong>All requested data from <i>first page</i> to <i>n page</i> is stored in <b>list_tv</b> variable of Object's</strong>
     *
     * @return List of <i>TVItem</i>
     */
    public List<TVItem> getTopRatedTV() {
        String response = requestServer("/tv/top_rated", "page=" + top_rated_tv.nextPage);

        if (response == null) return null;

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        Type listType = new TypeToken<List<TVItem>>() {
        }.getType();

        top_rated_tv.nextPage++;

        top_rated_tv.list.addAll(gson.fromJson(jsonObject.get("results"), listType));

        return top_rated_tv.list;
    }
    //endregion

    //region Similar Methods

    /**
     * <p><i>Get movie similar from movie id</i></p>
     * NOTE: <strong>All requested data from <i>first page</i> to <i>n page</i> is stored in <b>similar_movie</b> variable of Object's</strong>
     *
     * @param id is a <i>movie id</i>
     * @return List of <i>MovieItem</i>
     */
    public List<MovieItem> getSimilarMovie(String id) {
        String response = requestServer("/movie/" + id + "/similar", "page=" + similar_movie.nextPage);
        if (response == null) return null;

        if (similar_movie.id.equals(id))
            similar_movie.setId(id);
        else
            similar_movie.nextPage++;

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        Type listType = new TypeToken<List<Movie>>() {
        }.getType();

        similar_movie.list.addAll(gson.fromJson(jsonObject.get("results"), listType));

        return similar_movie.list;
    }

    /**
     * <p><i>Get list tv similar from tv id</i></p>
     * NOTE: <strong>All requested data from <i>first page</i> to <i>n page</i> is stored in <b>similar_tv</b> variable of Object's</strong>
     *
     * @param id is a <i>tv id</i>
     * @return List of <i>TVItem</i>
     */
    public List<TVItem> getSimilarTV(String id) {
        String response = requestServer("/tv/" + id + "/similar", "page=" + similar_tv.nextPage);
        if (response == null) return null;

        if (similar_tv.id.equals(id))
            similar_tv.nextPage++;
        else
            similar_tv.setId(id);

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        Type listType = new TypeToken<List<TVItem>>() {
        }.getType();
        similar_tv.list.addAll(gson.fromJson(jsonObject.get("results"), listType));
        return similar_tv.list;
    }
    //endregion

    //region Popular Methods

    /**
     * <i>Get list movie popular</i>
     * NOTE: <strong>All requested data from <i>first page</i> to <i>n page</i> is stored in <b>popular_movie</b> variable of Object's</strong>
     *
     * @return List of <i>MovieItem</i>
     */
    public List<MovieItem> getPopularMovie() {
        String response = requestServer("/movie/popular", "page=" + popular_movie.nextPage);
        if (response == null) return null;

        popular_movie.nextPage++;

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        Type listType = new TypeToken<List<MovieItem>>() {
        }.getType();
        popular_movie.list.addAll(gson.fromJson(jsonObject.get("results"), listType));
        return popular_movie.list;
    }

    /**
     * <p><i>Get list tv popular</i></p>
     * NOTE: <strong>All requested data from <i>first page</i> to <i>n page</i> is stored in <b>popular_tv</b> variable of Object's</strong>
     *
     * @return List of <i>TVItem</i>
     */
    public List<TVItem> getPopularTV() {
        String response = requestServer("/tv/popular", "page=" + popular_tv.nextPage);
        if (response == null) return null;

        popular_tv.nextPage++;

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        Type listType = new TypeToken<List<TVItem>>() {
        }.getType();
        popular_tv.list.addAll(gson.fromJson(jsonObject.get("results"), listType));
        return popular_tv.list;
    }
    //endregion

    //region UpComing methods

    /**
     * <p><i>Get list movie upcoming</i></p>
     * NOTE: <strong>All requested data from <i>first page</i> to <i>n page</i> is stored in <b>upcoming</b> variable of Object's</strong>
     *
     * @return List of <i>MovieItem</i>
     */
    public List<MovieItem> getUpcoming() {
        String response = requestServer("/movie/upcoming");
        if (response == null) return null;

        upcoming.nextPage++;

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        Type listType = new TypeToken<List<MovieItem>>() {
        }.getType();

        upcoming.list.addAll(gson.fromJson(jsonObject.get("results"), listType));

        return upcoming.list;
    }
    //endregion

    /**
     * <p><i>Search movie, tv and person</i></p>
     * NOTE: <strong>All requested data from first page to <i>n</i> page is stored in object's search variable</strong>
     *
     * @param query is a search string movie, tv and person
     * @return <b>Entertainment</b> List include: movie, tv and person
     */
    public List<Entertainment> search(String query) {
        String response = requestServer("/search/multi", "include_adult=true&page=" + search.nextPage + "&query=" + query);

        if (response == null) return null;

        if (search.id.equals(query))
            search.setId(query);
        else
            search.nextPage++;

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        search.list.addAll(Factory.MultiSearchList(jsonObject.get("results").getAsJsonArray()));

        return search.list;
    }

    /**
     * <p><i>Get credit movie or tv</i></p>
     * <b>Example</b>
     * <pre>{@code
     *  Object[] credit = api.getCredit(movie_id, true);
     *  List<Cast> cast = (List<Cast>) credit[0];
     *  List<Crew> crew = (List<Crew>) credit[1];
     *  }</pre>
     *
     * @param id   is a <b>movie_id</b> or <b>tv_id</b>
     * @param type true will get credit <b>movie</b> else <b>tv</b>
     * @return Object[] => <p>First item is a <b>List Cast</b>.<p> Second item is a <b>List Crew</b>.
     */
    public Object[] getCredit(String id, boolean type) {
        String response = requestServer(String.format("/%s/%s/credits", type ? "movie" : "tv", id));

        if (response == null) return null;
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        JsonArray cast = json.get("cast").getAsJsonArray();
        JsonArray crew = json.get("crew").getAsJsonArray();

        Type castType = new TypeToken<List<Cast>>() {
        }.getType();
        Type crewType = new TypeToken<List<Crew>>() {
        }.getType();
        System.out.println("cast");
        System.out.println(cast);
        List<Cast> casts = gson.fromJson(cast, castType);
        List<Crew> crews = gson.fromJson(crew, crewType);

        Object[] obj = new Object[2];
        obj[0] = casts;
        obj[1] = crews;
        return obj;
    }

    /**
     * @param id is a id genres.
     * @param type true is a movie, false is a tv.
     * @return
     */
    public Detail.Genres getGenresById(int id, boolean type) {
        Detail.Genres[] genres = type ? genres_movie : genres_tv;
        if (genres == null) return null;

        for (Detail.Genres item : genres)
            if (item.id == id) return item;
        return null;
    }

    //region User Methods

    /**
     * NOTE: <strong>All requested data is stored in <b>request_token</b> variable</strong>
     *
     * @return string request token
     */
    @Nullable
    private String getRequestToken() {
        String response = requestServer("/authentication/token/new");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (!json.get("success").getAsBoolean()) return null;
        request_token = json.get("request_token").getAsString();
        return request_token;
    }

    /**
     * <p>Create session from successfully logged in token</p>
     * NOTE: <strong>All requested data is stored in <b>session_id</b> variable</strong>
     *
     * @return New session id from logged in token
     */
    @Nullable
    private String createSession() {
        String jsonBody = String.format("{\"request_token\": \"%s\"}", request_token);
        String response = requestServer("/authentication/session/new", RequestBody.create(JSON, jsonBody));

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        boolean isSuccess = json.get("success").getAsBoolean();
        if (!isSuccess) return false + "|" + json.get("status_message").getAsString();

        session_id = json.get("session_id").getAsString();
        getDetailAccount();

        return true + "|" + session_id;
    }

    /**
     * Login with account the movie <b>https://www.themoviedb.org/</b>
     *
     * @param user is a username
     * @param pass is a password
     * @return true | false (success | fail)
     */
    public String Login(String user, String pass) {
        getRequestToken();

        String jsonBody = String.format("{\"username\":\"%s\", \"password\":\"%s\", \"request_token\": \"%s\"}", user, pass, request_token);
        String response = requestServer("/authentication/token/validate_with_login", RequestBody.create(JSON, jsonBody));

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        boolean isSuccess = json.get("success").getAsBoolean();

        if (!isSuccess) return false + "|" + json.get("status_message").getAsString();

        return createSession();
    }

    /**
     * <p>Get detail info of user</p>
     *
     * @return detail info account includes: name, id, username, avatar,...
     */
    public Account getDetailAccount() {
        String response = requestServer("/account");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (json.has("success")) return null;

        account = gson.fromJson(json, Account.class);

        return account;
    }

    /**
     * Get my favorite movies
     *
     * @return Movie list has been added to favorites
     */
    public List<MovieItem> getMyFavoritesMovie() {
        if (account == null) {
            if (session_id == null) return null;
            else getDetailAccount();
        }

        String response = requestServer("/account/" + account.id + "/favorite/movies");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (!json.has("success")) {
            Type listType = new TypeToken<List<MovieItem>>() {
            }.getType();

            my_watchlist_movie.nextPage++;
            my_favorite_movie.list.addAll(gson.fromJson(json.get("results"), listType));

            return my_favorite_movie.list;
        }
        return null;
    }

    /**
     * Get my favorite tv show.
     *
     * @return TV Show list has been added to favorites
     */
    public List<TVItem> getMyFavoritesTV() {
        if (account == null) {
            if (session_id == null) return null;
            else getDetailAccount();
        }

        String response = requestServer("/account/" + account.id + "/favorite/tv");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (!json.has("success")) {
            Type listType = new TypeToken<List<TVItem>>() {
            }.getType();
            my_watchlist_tv.nextPage++;
            my_favorite_tv.list.addAll(gson.fromJson(json.get("results"), listType));

            return my_favorite_tv.list;
        }
        return null;
    }


    /**
     * <p>Add movie, tv to favorites</p>
     *
     * @param id     is a <strong>movie</strong> or <strong>tv</strong> id.
     * @param isMark marked or unmarked favorites
     * @param type   is a media type. <strong>movie</strong> is true, <strong>tv</strong> is false
     */
    public String MarkAsFavorites(String id, boolean isMark, boolean type) {
        if (account == null) {
            if (session_id == null) return null;
            else getDetailAccount();
        }

        String jsonBody = String.format("{\"media_type\": \"%s\", \"media_id\": \"%s\", \"favorite\":%b}", type ? "movie" : "tv", id, isMark);

        String response = requestServer("/account/" + account.id + "/favorite", RequestBody.create(JSON, jsonBody));

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        return json.get("success") + "|" + json.get("status_message").getAsString();
    }

    /**
     * <p>Add movie, tv to watchlist</p>
     *
     * @param id     is a <strong>movie</strong> or <strong>tv</strong> id.
     * @param isMark marked or unmarked favorites
     * @param type   is a media type. <strong>movie</strong> is true, <strong>tv</strong> is false
     */
    public String MarkAsWatchlist(String id, boolean isMark, boolean type) {
        if (account == null) {
            if (session_id == null) return null;
            else getDetailAccount();
        }

        String jsonBody = String.format("{\"media_type\": \"%s\", \"media_id\": \"%s\", \"watchlist\":%b}", type ? "movie" : "tv", id, isMark);

        String response = requestServer("/account/" + account.id + "/watchlist", RequestBody.create(JSON, jsonBody));

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        return json.get("success") + "|" + json.get("status_message").getAsString();
    }

    /**
     * @param id    is a <strong>movie</strong> or <strong>tv</strong> id
     * @param type  is a media type. <strong>movie</strong> is true, <strong>tv</strong> is false
     * @param value range from 0.5 to 10
     * @return String value {@code <success>|<status_message>}
     */
    public String Rating(String id, boolean type, double value) {

        String jsonBody = String.format("{\"value\": %f}", value);
        String response = requestServer(String.format("/%s/%s/rating", type ? "movie" : "tv", id), RequestBody.create(JSON, jsonBody));

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        return json.get("success") + "|" + json.get("status_message").getAsString();
    }

    /**
     * @param id   is a <strong>movie</strong> or <strong>tv</strong> id
     * @param type is a media type. <strong>movie</strong> is true, <strong>tv</strong> is false
     * @return String value {@code <success>|<status_message>}
     */
    public String DeleteRating(String id, boolean type) {
        String response = deleteRequest(String.format("/%s/%s/rating", type ? "movie" : "tv", id));

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        return json.get("success") + "|" + json.get("status_message").getAsString();
    }


    /**
     * Get my rated movies
     *
     * @return my movie list is rated. if false return null
     */
    public List<MovieItem> getRatedMovie() {
        if (account == null) {
            if (session_id == null) return null;
            else getDetailAccount();
        }
        String response = requestServer("/account/" + account.id + "/rated/movies");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (json.has("success"))
            return null;
        Type listType = new TypeToken<List<MovieItem>>() {
        }.getType();
        my_rated_movie.nextPage++;

        my_rated_movie.list.addAll(gson.fromJson(json.get("results"), listType));

        return my_rated_movie.list;
    }


    /**
     * Get my rated tv
     *
     * @return my tv list is rated. if false return null
     */
    public List<TVItem> getRatedTV() {
        if (account == null) {
            if (session_id == null) return null;
            else getDetailAccount();
        }
        String response = requestServer("/account/" + account.id + "/rated/tv");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (json.has("success"))
            return null;

        Type listType = new TypeToken<List<TVItem>>() {
        }.getType();

        my_rated_tv.nextPage++;
        my_rated_tv.list.addAll(gson.fromJson(json.get("results"), listType));

        return my_rated_tv.list;
    }

    /**
     * Get movies in my added watchlist
     *
     * @return movies list added to watchlist
     */
    public List<MovieItem> getWatchListMovie() {
        if (account == null) {
            if (session_id == null) return null;
            else getDetailAccount();
        }

        String response = requestServer("/account/" + account.id + "/watchlist/movies");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (json.has("success")) {
            return null;
        }

        Type listType = new TypeToken<List<MovieItem>>() {
        }.getType();

        my_watchlist_movie.nextPage++;
        my_watchlist_movie.list.addAll(gson.fromJson(json.get("results"), listType));

        return my_watchlist_movie.list;
    }

    /**
     * Get tv in my added watchlist
     *
     * @returns Get tv in my added watchlist
     */
    public List<TVItem> getWatchListTV() {
        if (account == null) {
            if (session_id == null) return null;
            else getDetailAccount();
        }

        String response = requestServer("/account/" + account.id + "/watchlist/tv");

        if (response == null) return null;

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (json.has("success")) {
            return null;
        }

        Type listType = new TypeToken<List<TVItem>>() {
        }.getType();

        my_watchlist_tv.nextPage++;
        my_watchlist_tv.list.addAll(gson.fromJson(json.get("results"), listType));

        return my_watchlist_tv.list;
    }
    //endregion
}

/**
 * Load image from <i>THE MOVIE DB API</i> width option size from mini to original.
 * <p>Download image from api <strong>https://image.tmdb.org/t/p/<b>{{size[option]}}{{path}}</b></strong></p>
 * <p><strong><i>Feature:</i></strong></p>
 * <ul>
 *     <li><b>ImageAPI.Get</b> load image. <strong>(ImageView)</strong></li>
 *     <li><b>ImageAPI.loadBackground</b> load background image into view. <strong>(any View)</strong></li>
 *     <li><b>ImageAPI.getCorner</b> transform image was load into circle image. <strong>(ImageView)</strong></li>
 *     <li><b>ImageAPI.getCircle</b> transform image was load into rounded image. <strong>(ImageView)</strong></li>
 * </ul>
 * <p>NOTE: <i>how to use?</i> => <strong>read description method</strong></p>
 */
class ImageAPI {
    final static String HOST = "https://image.tmdb.org/t/p/";

    final String MINI = "w92";
    final String SMALL = "w185";
    final String MEDIUM = "w300";
    final String LARGE = "w500";
    final String XLARGE = "w780";
    final String XXLARGE = "w1280";
    final String ORIGIN = "original";

    /**
     * size[0-6]
     * Optional image size from 0 to 6 corresponds to small to original value.
     */
    static String[] size = {"w92", "w185", "w300", "w500", "w780", "w1280", "original"};

    public ImageAPI() {
    }

    /**
     * Get normal image with optional size, then load it to ImageView.
     *
     * @param path   is a string path image
     * @param option is a size option
     * @param view   is a ImageView need render image
     */
    static public void get(String path, int option, ImageView view) {
        Picasso.get().load(HOST + size[option] + path).into(view);
    }

    /**
     * Get normal image with options size, then load it to background View.
     *
     * @param path       is a string path image
     * @param option     is a size option
     * @param background is a View need render background
     */
    static public void loadBackground(String path, int option, View background) {
        Picasso.get().load(HOST + size[option] + path).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                background.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    /**
     * Get and transform rounded image, then load it to ImageView
     *
     * @param path   is a string path image.
     * @param option is a size option
     * @param view   is a ImageView need render image.
     */
    static public void getCorner(String path, int option, ImageView view) {
        Picasso.get().load("https://image.tmdb.org/t/p/" + size[option] + path).transform(new RoundedTransformation(50, 0)).into(view);
    }

    /**
     * Get and transform circle image, then load it to ImageView
     *
     * @param path   is a string path image.
     * @param option is a size option
     * @param view   is a ImageView need render image.
     */
    static public void getCircle(String path, int option, ImageView view) {
        Picasso.get().load("https://image.tmdb.org/t/p/" + size[option] + path).transform(new CircleTransform()).into(view);
    }


    /**
     * Object transform from normal to rounded image.
     */
    public static class RoundedTransformation implements Transformation {
        private final int radius;
        private final int margin;

        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin,
                    source.getHeight() - margin), radius, radius, paint);

            if (source != output) {
                source.recycle();
            }
            return output;
        }

        @Override
        public String key() {
            return "rounded(r=" + radius + ", m=" + margin + ")";
        }
    }

    /**
     * Object transform from normal to circle image.
     */
    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}


/**
 * ListData is a storage Object contain: <b>nextPage</b>, <b>list</b> and <b>id</b> <strong>(if any)</strong>
 *
 * @param <T> contain: <b>Entertainment</b>, <b>MovieItem</b>, <b>TVItem</b>
 */
class ListData<T> {
    public int nextPage;
    public List<T> list;
    public String id;

    public ListData() {
        list = new ArrayList<T>();
        id = "";
        nextPage = 1;
    }

    public ListData(String id) {
        this.id = id;
        list = new ArrayList<T>();
        nextPage = 1;
    }

    public void setId(String id) {
        this.id = id;
        nextPage = 1;
        list.clear();
    }

    public String toString() {
        return String.format("size: %d, next page: %d", list.size(), nextPage);
    }
}