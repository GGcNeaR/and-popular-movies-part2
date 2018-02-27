package com.udacity.and.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final String MOVIE_EXTRA = "MOVIE_EXTRA";

    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;

    private String originalLanguage;
    private String originalTitle;

    private int[] genreIds;

    private int voteCount;
    private double voteAverage;
    private double popularity;
    private String backdropPath;
    private boolean isAdult;

    public Movie(int id, String title, String overview, String posterPath, String releaseDate,
                 String originalLanguage, String originalTitle, int[] genreIds,
                 int voteCount, double voteAverage, double popularity,
                 String backdropPath, boolean isAdult) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.backdropPath = backdropPath;
        this.isAdult = isAdult;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public boolean isAdult() {
        return isAdult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeInt(genreIds.length);
        dest.writeIntArray(genreIds);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
        dest.writeString(backdropPath);
        dest.writeByte((byte)(isAdult ? 1 : 0));
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.genreIds = new int[in.readInt()];
        in.readIntArray(this.genreIds);
        this.voteCount = in.readInt();
        this.voteAverage = in.readDouble();
        this.popularity = in.readDouble();
        this.backdropPath = in.readString();
        this.isAdult = in.readByte() != 0;
    }
}
