package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ClientRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsClientException;
import bg.sofia.uni.fmi.mjt.newsfeed.dto.Page;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class NewsFeedClient {
    private final HttpClient newsClient;
    private static final int MAX_ANSWERS_PER_SEARCH = 100;

    private static  final int MAX_PAGES = 3;
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "://newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";

    private static final Gson GSON = new Gson();

    public NewsFeedClient(HttpClient newsClient) {
        this.newsClient = newsClient;
    }

    public List<Page> fetchNews(Query apiEndpointQuery) throws NewsClientException {
        int pageNumber = 0;
        int pagesTotal = 0;

        List<Page> pages = new ArrayList<>();
        HttpResponse<String> response;
        while ((pagesTotal + apiEndpointQuery.getPageSize() <= MAX_ANSWERS_PER_SEARCH) && pageNumber <= MAX_PAGES) {
            pageNumber++;
            String queryString = apiEndpointQuery.createQueryString(pageNumber);
            try {
                URI uri = URI.create(API_ENDPOINT_SCHEME + API_ENDPOINT_HOST + API_ENDPOINT_PATH + queryString);
                HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

                response = newsClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                throw new NewsClientException("Couldn't fetch news", e);
            }
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                String body = response.body();
                JsonElement jsonArticles = JsonParser.parseString(body).getAsJsonObject();
                Page page = GSON.fromJson(jsonArticles.toString(), Page.class);
                if (page.getArticles().isEmpty()) {
                    if (pages.isEmpty()) {
                        throw new ClientRequestException("No news found");
                    }
                    return pages;
                }
                pagesTotal += page.getArticles().size();
                pages.add(page);

            } else {
                String body = response.body();
                JsonObject errorObject = JsonParser.parseString(body).getAsJsonObject();
                throw new ClientRequestException(errorObject.get("message").toString());
            }
        }
        return pages;
    }

}
