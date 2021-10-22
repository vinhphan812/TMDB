# The Movie Database API Call By Java

This is a project call API for subject **android program**.

## Acknowledgements

-    [OkHttp3](https://square.github.io/okhttp/)
-    [Picasso](https://square.github.io/picasso/)
-    [GSON](https://github.com/google/gson)

## Installation

Step 1: Go to `build.grable` in `dependencies` property

```gradle
  dependencies {
    implementation "com.google.code.gson:gson:2.8.8"
    implementation "com.squareup.okhttp3:okhttp:4.9.0"
    implementation "com.squareup.picasso:picasso:2.71828"
  }
```

Step 2: Download source and extract all file in folder `source` into your project.

```
your_project
    ├─── app
    │     ├──── src
    │     └...   ├───main
    │            └...  ├───java <== extract here.
    │                  └───res
    └───...
```

Step 3: You need change the `package name` from `Account.java`, `Detail.java`, `Entertainment.java`, `Factory.java`, `MovieAPI.java`;

```java
    //change package to your project package
    package <package_name>;
```

Step 3: using permission in `AndroidManifest.xml`:

```xml
<manifest ...>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    ...
</mainifest>
```

## Quickview Object

-    **ListData** is an object contain data. Include: list, id and nextPage.

```java
    // <T> all type such as: Entertainment, MovieItem, TVItem,...
    class ListData<T>: {
        nextPage: int;
        list: List<T>;
        id: String;
        // Init default
        ListData();
        // Init with id. Usually used for data saved by id for collation
        ListData(String id);
        // reset nextpage, list and assign id
        SetId(String id);
    }

    // Example:
    ListData<Entertainment> entertainment = new ListData<Entertainment>();

    // Example with id:
    ListData<MovieItem> movies = new ListData<MovieItem>("12345");
```

-    **Entertainment** is a Parent of **MovieItem**, **TVItem** and **PersonItem**.

```java
    // Entertainment Object is a Core.
    class Entertainment: {
        id: String;
        vote_count: int;
        poster_path: String;
        overview: String;
        backdrop_path: String;
        original_language: String;
        media_type: String;
        genre_ids: int[];
        vote_average: double;
        popularity: double;
    }

    // extend Entertainment
    class MovieItem extend Entertainment: {
        title: String;
        original_title: String;
        release_date: String;
        video: Boolean;
        adult: Boolean;
    }

    // extend Entertainment
    class TVItem extend Entertainment: {
        name: String;
        original_name: String;
        first_air_date: String;
        origin_country: String[];
    }

    // extend Entertainment
    class PersonItem extend Entertainment: {
        adult: boolean;
        /*
        * gender option
        * 0 - Not specified
        * 1 - Female
        * 2 - Male
        * */
        gender: int;
        /*
        * known_for containt: movies, tv.
        * include: MovieItem, TVItem
        * using Factory class to create List
        * */
        known_for: List<Entertainment>;
        known_for_department: String;
        name: String;
        profile_path: String;
    }
```

-    **Detail** is a Parent of **MovieDetail**, **TVDetail**.

```java
    // Detail Object is a Core.
    class Detail: {
        backdrop_path: String;
        genres: Genres[];
        homepage: String;
        id: int;
        original_language: String;
        overview: String;
        popularity: double;
        poster_path: String;
        production_companies: Company[];
        production_countries: Country[];
        spoken_languages: Language[];
        status: String;
        tagline: String;
        vote_average: double;
        vote_count: int;

        // Genres is a Category.
        static class Genres: {
            id: int;
            name: String;
        }

        static class Language: {
            english_name: String;
            iso_639_1: String;
            name: String;
        }

        static class Country: {
            iso_3166_1: String;
            name: String;
        }

        static class Company: {
            id: int;
            logo_path: String;
            name: String;
            origin_country: String;
        }
    }

    // MovieDetail extend Detail
    class MovieItem extend Detail: {
        adult: boolean;
        budget: int;
        imdb_id: String;
        original_title: String;
        release_date: String;
        runtime: int;
        title: String;
        video: boolean;
    }

    // TVDetail extend Detail
    class TVItem extend Detail: {
        created_by: Person[];
        episode_run_time: int[];
        first_air_date: String;
        in_production: boolean;
        languages: String[];
        last_air_date: String;
        last_episode_to_air: LastEpisode;
        name: String;
        networks: Networks[];
        number_of_episodes: int;
        number_of_session: int;
        origin_country: String[];
        original_name: String;
        seasons: Season[];
        type: String;
        static class Season: {
            air_date: String;
            episode_count: int;
            id: int;
            name: String;
            overview: String;
            poster_path: String;
            season_number: int;
        }

        static class Networks: {
            name: String;
            id: int;
            logo_path: String;
            origin_country: String;
        }

        static class LastEpisode: {
            air_date: String;
            episode_number: int;
            id: int;
            name: String;
            overview: String;
            production_code: String;
            season_number: int;
            still_path: String;
            vote_average: double;
            vote_count: int;
        }

        static class Person: {
            id: int;
            credit_id: String;
            name: String;
            gender: byte;
            profile_path: String;
        }
    }
```

-    **Ekip** is a Parent of **Cast** and **Crew**.

```java
    // Ekip Object is a Core.
    class Ekip: {
        adult: boolean;
        gender: int;
        /*
        * gender option
        * 0 - Not specified
        * 1 - Female
        * 2 - Male
        * */
        id: String;
        known_for_department: String;
        name: String;
        original_name: String;
        popularity: double;
        profile_path: String;
        credit_id: String;
        order: int;
    }
    // Cast extend Ekip
    class Cast extends Ekip: {
        character: String;
    }
    // Crew extend Ekip
    class Crew extends Ekip: {
        department: String;
        job: String;
    }
```

-    **Account** is a Object contain info user.

```java
    class Account: {
        id: String;
        name: String;
        iso_639_1: String;
        iso_3166_1: String;
        include_adult: boolean;
        username: String;
        avatar: Avatar;
        static class Avatar: {
            tmdb: TMDB;
            static class TMDB: {
                avatar_path: String;
            }
        }
    }
```

### Other Object

-    Language from Detail Object `Detail.Language`

```java
    class Language {
        english_name: String;
        iso_639_1: String;
        name: String;
    }
```

-    Country from Detail Object `Detail.Country`

```java
    class Country{
        iso_3166_1: String;
        name: String;
    }
```

-    Company from Detail Object `Detail.Company`

```java
    class Company{
        id: int;
        logo_path: String;
        name: String;
        origin_country: String;
    }
```

## Overview

-    [`MovieAPI`](#MovieAPI)

| #   | Method                                                                         | **Return Type**         | Description                                  |
| :-- | :----------------------------------------------------------------------------- | :---------------------- | :------------------------------------------- |
| 1   | [MovieAPI(String APIKEY)](#Init-API-Object)                                    | void                    | Init API with **APIKEY**.                    |
| 2   | [MovieAPI(String APIKEY, String SessionID)](#Init-API-Object)                  | void                    | Init API with Account by **session_id**.     |
| 3   | [getTrending()](#Trending)                                                     | List\<_Entertainment_\> | Trending list contain: **TV** and **Movie**. |
| 4   | [getGenresById(int id, boolean type)](#Genres-by-id)                           | _Detail.Genres_         | **Genres** from id.                          |
| 5   | [getTopRatedMovie()](#Top-Rated-Movie,-TV)                                     | List\<_MovieItem_\>     | Top Rated Movie.                             |
| 6   | [getTopRatedTV()](#Top-Rated-Movie,-TV)                                        | List\<_TVItem_\>        | Top Rated TV show.                           |
| 7   | [getPopularMovie()](#Top-Rated-Movie,-TV)                                      | List\<_MovieItem_\>     | Popular Movie.                               |
| 8   | [getPopularTV()](#Popular-Movie,-TV)                                           | List\<_TVItem_\>        | Popular TV show.                             |
| 9   | [getUpcoming()](#Popular-Movie,-TV)                                            | List\<_MovieItem_\>     | Upcoming Movie.                              |
| 10  | [search()](#Multi-Search)                                                      | List\<_Entertainment_\> | Contain: **Movie**, **TV**, **Person**.      |
| 11  | [getCredit()](#Credit-Movie,-TV)                                               | Object[]                | Array Object include: _Casts_, _Crews_       |
| 12  | [getSimilarMovie()](#Similar-Movie,-TV)                                        | List\<_MovieItem_\>     | Similar List of a movie.                     |
| 13  | [getSimilarMovie()](#Similar-Movie,-TV)                                        | List\<_TVItem_\>        | Similar List of a tv show.                   |
| 14  | [Login()](#Login-with-the-movie-db-account)                                    | String                  | \<status\>\|\<session_id\>.                  |
| 15  | [getDetailAccount()](#Detail-account)                                          | Account                 | Info Account TMDB.                           |
| 16  | [getMyFavoritesMovie()](#My-Favorites-Movie,-TV)                               | List\<_MovieItem_\>     | Your Favorites Movie.                        |
| 17  | [getMyFavoritesTV()](#My-Favorites-Movie,-TV)                                  | List\<_TVItem_\>        | Your Favorites TV.                           |
| 18  | [MarkAsWatchlist(String id, boolean isMark, boolean type)](#Mark-As-Watchlist) | String                  | \<status\>\|\<mesage\>.                      |
| 19  | [Rating(String id, boolean type, double value)](#Rating-Movie,-TV)             | String                  | \<status\>\|\<mesage\>.                      |
| 20  | [DeleteRating(String id, boolean type)](#Rating-Movie,-TV)                     | String                  | \<status\>\|\<mesage\>.                      |
| 21  | [getRatedMovie()](#Mark-As-Watchlist)                                          | List\<_MovieItem_\>     | Your Rated Movies.                           |
| 22  | [getRatedTV()](#Mark-As-Watchlist)                                             | List\<_TVItem_\>        | Your Rated TV show.                          |
| 23  | [getWatchlistMovie()](#My-Watchlist-Movie,-TV)                                 | List\<_MovieItem_\>     | Your Watchlist Movies.                       |
| 24  | [getWatchlistTV()](#My-Watchlist-Movie,-TV)                                    | List\<_TVItem_\>        | Your Watchlist TV show.                      |

-    [`ImageAPI`](#ImageAPI)

| #   | Method                                                     | **Return Type** | Description                           |
| :-- | :--------------------------------------------------------- | :-------------- | :------------------------------------ |
| 1   | `get(String path, int option, ImageView image)`            | void            | load image for ImageView.             |
| 2   | `loadBackground(String path, int option, View background)` | void            | load image for background View.       |
| 3   | `getCorner(String path, int option, ImageView image)`      | void            | load image mark rounded for ImageView |
| 4   | `getCicle(String path, int option, ImageView image)`       | void            | load image mark cicle for ImageView   |

## MovieAPI

-    Instructions for using all **MovieAPI** methods.

### Init API Object

```java
    // APIKEY is a key create in https://www.themoviedb.org/
    MovieAPI api = new MovieAPI(APIKEY);
    // APIKEY, session_id(Account)
    MovieAPI api = new MovieAPI(APIKEY, SessionID);
    // api.isAccess == true => valid APIKEY and SessionID (if any)
    System.out.println(api.isAccess);
```

### Trending

-    list trending contain: MovieItem and TVItem Object.
-    All data save into `api.trending`.

```java
    List<Entertainment> list = api.getTrending();

    // or get All List<T> from MovieAPI
    List<Entertainment> dataTrending = api.trending;

    // check MovieItem or TVItem, then convert to Object
    for(Entertainment item: list)
    {
      if(item.media_type == "movie")
          MovieItem movie = (MovieItem) item;
      else
          TVItem movie = (TVItem) item;
    }
```

### Genres by id

-    Genres (**Category**)
-    `type` true is a movie, false is a TV.

```java
    // Movie
    int movie_id = 28;
    Detail.Genres genres = api.getGenresById(movie_id, true);
    System.out.println(genres.name); // => Phim Hành Động

    // TV
    int tv_id = 16;
    Detail.Genres genres = api.getGenresById(tv_id, false);
    System.out.println(genres.name); // => Phim Hoạt Hình
```

### Top Rated Movie, TV

-    return top rated list.
-    All top rated data save into `api.top_rated_movie` and `api.top_rated_tv`.

```java
    // Movie
    List<MovieItem> topMovie = api.getTopRatedMovie();
    System.out.println("Top Rated Movie size: " + topMovie.size());

    // TV
    List<TVItem> topTV = api.getTopRatedTV();
    System.out.println("Top Rated Movie size: " + topTV.size());
```

### Popular Movie, TV

-    return popular list.
-    All popular data save into `api.popular_movie` and `api.popular_tv`.

```java
    // Movie
    List<MovieItem> popularMovie = api.getPopularMovie();
    System.out.println("Popular Movie size: " + popularMovie.size());

    // TV
    List<TVItem> popularTV = api.getPopularTV();
    System.out.println("Popular Movie size: " + popularTV.size());
```

### Upcoming Movie

-    return the comming soon movie.
-    All comming soon movie data save into `api.upcoming`.

```java
    List<MovieItem> upcoming = api.getUpcoming();
    System.out.println("Upcoming Movie size: " + upcoming.size());
```

### Multi Search

-    Search with text. result may contain: **MovieItem**, **TVItem** and **PersonItem**.

```java

    List<Entertainment> result = api.search("vincenzo");
    System.out.println("Search size: " + result.size());

```

### Credit Movie, TV

-    return _cast_ and _crew_ list.

```java
    // Movie
    Object[] creditMovie = api.getCredit(movie_id, true);
    List<Cast> castMovie = (List<Cast>) creditMovie[0];
    List<Crew> crewMovie = (List<Crew>) creditMovie[1];

    // TV
    Object[] creditTV = api.getCredit(tv_id, true);
    List<Cast> castTV = (List<Cast>) creditTV[0];
    List<Crew> crewTV = (List<Crew>) creditTV[1];
```

### Detail Movie, TV

-    Get detail by id. return **MovieDetail** and **TVDetail**'.

```java
    // Movie
    MovieDetail movie = api.getDetailMovie(movie_id);
    System.out.println(movie);

    // TV
    TVDetail tv = api.getDetailTV(tv_id);
    System.out.println(tv);
```

### Similar Movie, TV

-    All similar data save into `api.similar_movie` and `api.similar_tv`.

```java
    // Movie
    List<MovieItem> similarMovie = api.getSimilarMovie(movie_id);
    System.out.println("Similar Movie size: " + similarMovie.size());

    //TV
    List<TVItem> similarTV = api.getSimilarTV(TV_id);
    System.out.println("Similar TV size: " + similarTV.size());
```

## Account Methods

### Login with the movie db account

-    Using account TMDB login to API. save session into `api.session_id`.

```java
    String user = "hihihi",
        pass = "********";
    String status = api.Login(user, pass);
    // success => status = "true|<session_id>";
    // fail => status = "false|<status_message>";
```

### Detail account

-    info account TMDB. data save into `api.account`.

```java
    Account account = api.getDetailAccount();
```

### My Favorites Movie, TV

-    favorites list of account. data save into `api.my_favorite_movie` and `api.my_favorite_tv`.

```java
    // Movie
    List<MovieItem> fivoriteMovie = api.getMyFavoritesMovie();

    // TV
    List<TVItem> fivoriteTV = api.getMyFavoritesTV();
```

### Mark As Favorites

-    mark and not mark favorite movies and tv.
-    `type`: _true_ is a movie, _false_ is a tv.

```java
    String movie_id = "123";
    // type: true is a movie, false is a tv
    boolean type_movie = true;

    // Mark in Favorite: set isMark = true
    String statusMark = api.MarkAsFavorite(movie_id, mark, type_movie);
    // status => <success>|<status_message>

    // Demark in Favorite: set isMark = false
    String statusDemark = api.MarkAsFavorite(movie_id, false, type_movie);
    // status => <success>|<status_message>
```

### Mark As Watchlist

-    `id`: is a movie_id or tv_id.
-    `type`: _true_ is a movie, _false_ is a tv.
-    `isMark`: _true_ is a mark, _false_ is a demark.

```java
    String movie_id = "123";
    // type: true is a movie, false is a tv
    boolean type_movie = true;

    // Mark in Favorite: set isMark = true
    String statusMark = api.MarkAsWatchlist(movie_id, true, type_movie);
    // status => <success>|<status_message>

    // Demark in Favorite: set isMark = false
    String statusDemark = api.MarkAsWatchlist(movie_id, false, type_movie);
    // status => <success>|<status_message>
```

### Rating Movie, TV

-    User ratings for movies, tv.

-    `id`: is a movie_id or tv_id.

-    `type`: _true_ is a movie, _false_ is a tv.

-    `ratingValue`: value type is double.

```java
    String movie_id = "123";
    // type: true is a movie, false is a tv
    boolean type_movie = true;

    double ratingValue = 8.5;

    // Rating
    String status_movie = api.Rating(movie_id, type_movie, ratingValue);
    // status => <success>|<status_message>

    // Delete Rating
    String status_delete_movie = api.DeleteRating(movie_id, type);
    // status => <success>|<status_message>
```

### My Rated Movie, TV

-    My Rated list. data save into `api.my_rated_movie` and `api.my_rated_tv`

```java
    // Movie
    List<MovieItem> ratedMoive = getRatedMovie();

    // TV
    List<TVItem> ratedTV = getRatedTV()
```

### My Watchlist Movie, TV

-    My Watchlist. data save into `api.watchlist_movie` and `api.my_watchlist_tv`

```java
    // Movie
    List<MovieItem> watchMV = getWatchlistMovie();

    // TV
    List<TVItem> watchTV = getWatchlistMovie();
```

## ImageAPI

-    **Optional**: [0 - 6]

**Convention Table**:

| Option | Value      | Description       |
| :----- | :--------- | :---------------- |
| 0      | `w92`      | **MINI** size     |
| 1      | `w185`     | **SMALL** size    |
| 2      | `w300`     | **MEDIUM** size   |
| 3      | `w500`     | **LARGE** size    |
| 4      | `w780`     | **XLARGE** size   |
| 5      | `w1280`    | **XXLARGE** size  |
| 6      | `original` | **ORIGINAL** size |

### Image In ImageView

```java
    String poster_path = "/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg";

    ImageView imageview = findViewById(R.id.imageView);

    // optional 2 is a MEDIUM size.
    ImageAPI.get(poster_path, 2, imageview);
```

### Image In Background View

```java
    String poster_path = "/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg";

    View view = findViewById(R.id.backgroundView);

    // optional 3 is a LARGE size.
    ImageAPI.loadBackground(poster_path, 3, view);

```

### Image Rounded In ImageView

```java
    String poster_path = "/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg";

    ImageView imageview = findViewById(R.id.imageView);

      // optional 2 is a MEDIUM size.
    ImageAPI.getCorner(poster_path, 2, imageview);
```

### Image Circle In ImageView

```java
    String poster_path = "/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg";

    ImageView imageview = findViewById(R.id.imageView);
      // optional 1 is a SMALL size.
    ImageAPI.getCircle(poster_path, 1, imageview);
```

## Features

-    Trending, upComing, Top Rating TV, Movie.
-    Detail info movie and tv, Similar movie, tv.
-    Account: Rating, Add Watchlist, add Favorites.
-    get my watchlist, my favorites.
-    Image convert to: default, rounded, cirlce, load for background.

## Authors

-    [@vinhphan812](https://facebook.com/vinhphan812)
