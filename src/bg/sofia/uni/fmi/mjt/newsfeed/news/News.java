package bg.sofia.uni.fmi.mjt.newsfeed.news;

import bg.sofia.uni.fmi.mjt.newsfeed.dto.Page;

import java.util.List;

public class News {
    private List<Page> pages;

    public News(List<Page> pages) {
        this.pages = pages;
    }

    public Page getPage(int index) {
        return pages.get(index);
    }
}
