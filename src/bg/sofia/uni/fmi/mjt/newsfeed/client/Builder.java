package bg.sofia.uni.fmi.mjt.newsfeed.client;

import java.util.List;

public class Builder {
    final String apiKey;
    List<String> keywords;

    String country;
    String category;
    String sources;

    int pageSize;

    Builder(String apiKey, String... keywords) {
        this.apiKey = apiKey;
        this.keywords = List.of(keywords);
    }

    public Builder setCountry(String country) {
        if (this.sources == null) {
            this.country = country;
        }
        return this;
    }

    public Builder setCategory(String category) {
        if (this.sources == null) {
            this.category = category;
        }
        return this;
    }

    public Builder setSources(String sources) {
        if (this.country == null && this.category == null) {
            this.sources = sources;
        }
        return this;
    }

    public Builder setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Query build() {
        return new Query(this);
    }
}

