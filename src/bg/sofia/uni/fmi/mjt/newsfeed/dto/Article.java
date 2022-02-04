package bg.sofia.uni.fmi.mjt.newsfeed.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Article {
    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    @SerializedName("publishedAt")
    private String date;
    private String content;


    public Article(Source source, String author, String title, String description,
                   String url, String urlToImage, String date, String content) {

        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.date = date;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(getSource(), article.getSource()) && Objects.equals(getAuthor(), article.getAuthor())
                && Objects.equals(getTitle(), article.getTitle()) && Objects.equals(getDescription(),
                article.getDescription()) && Objects.equals(getUrl(), article.getUrl()) &&
                Objects.equals(getUrlToImage(), article.getUrlToImage()) && Objects.equals(getDate(), article.getDate())
                && Objects.equals(getContent(), article.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(), getAuthor(), getTitle(), getDescription(),
                getUrl(), getUrlToImage(), getDate(), getContent());
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() { return description; }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
