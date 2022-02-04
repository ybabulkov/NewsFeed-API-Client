package bg.sofia.uni.fmi.mjt.newsfeed.client;

import java.util.List;

public class Query {
    private final String apiKey;
    private final String country;
    private final String category;
    private final String sources;
    private final List<String> keywords;
    private final int pageSize;


    public Query(Builder builder) {
        this.apiKey = builder.apiKey;
        this.category = builder.category;
        this.country = builder.country;
        this.sources = builder.sources;
        this.keywords = builder.keywords;
        this.pageSize = builder.pageSize;
    }

    public static Builder builder(String apiKey, String... keywords) {
        return new Builder(apiKey, keywords);
    }

    public String createQueryString(int page) {
        StringBuilder stringBuilder = new StringBuilder();
        String keywordsString = String.join("+", keywords);

        stringBuilder.append("page=%d&".formatted(page));

        stringBuilder.append("q=%s&".formatted(keywordsString));
        if (country != null) {
            stringBuilder.append("country=%s&".formatted(country));
        }
        if (category != null) {
            stringBuilder.append("category=%s&".formatted(category));
        }
        if (sources != null) {
            stringBuilder.append("sources=%s&".formatted(sources));
        }
        if (pageSize != 0) {
            stringBuilder.append("pageSize=%d&".formatted(pageSize));
        }
        stringBuilder.append("apiKey=%s".formatted(apiKey));
        return "?" + stringBuilder.toString();
    }

    public int getPageSize() {
        return pageSize;
    }
}
