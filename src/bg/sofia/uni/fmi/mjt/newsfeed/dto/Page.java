package bg.sofia.uni.fmi.mjt.newsfeed.dto;

import java.util.List;

public class Page {
    Article[] articles;

    public Page(Article[] articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return List.of(articles);
    }

    public Article getArticle(int index) {
        return List.of(articles).get(index);
    }

}
