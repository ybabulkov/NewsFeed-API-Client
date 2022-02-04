package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ClientRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsClientException;
import bg.sofia.uni.fmi.mjt.newsfeed.dto.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.news.News;
import bg.sofia.uni.fmi.mjt.newsfeed.dto.Page;
import bg.sofia.uni.fmi.mjt.newsfeed.dto.RequestError;
import bg.sofia.uni.fmi.mjt.newsfeed.dto.Source;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsFeedClientTest {

    private static Article newsArticle;
    private static String newsArticleJson;
    private static HttpResponse<String> responseMock;
    private static HttpClient clientMock;

    private static Query query;
    private static NewsFeedClient newsFeedClient;
    private static Gson gson;

    private static final String source = "bbc-news";
    private static final String country = "gb";
    private static final String category = "sports";
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static String[] keywords = {"football"};
    private static final int pageSize = 3;

    @BeforeEach
    public void setupEach() throws IOException, InterruptedException {
        when(clientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(responseMock);
    }

    @BeforeAll
    public static void setupAll() {

        gson = new Gson();
        clientMock = mock(HttpClient.class);
        responseMock = (HttpResponse<String>) mock(HttpResponse.class);

        query = Query.builder(API_KEY, keywords)
                .setCountry(country)
                .setCategory(category)
                .setPageSize(pageSize)
                .build();

        Source newsSource = new Source("10", "bbc news");
        newsArticle = new Article(newsSource,
                "Yoan",
                "Article title",
                "Article description",
                "www.someSite.com",
                "url for picture",
                "11.02.2023",
                "article content");

        Article[] articles = {newsArticle};
        Page page = new Page(articles);
        News news = new News(List.of(page));
        newsArticleJson = gson.toJson(news.getPage(0));

        newsFeedClient = new NewsFeedClient(clientMock);
    }

    @Test
    public void testValidArticle() throws NewsClientException {
        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(responseMock.body()).thenReturn(newsArticleJson);

        News news = new News(newsFeedClient.fetchNews(query));

        Article article = news.getPage(0).getArticle(0);

        assertEquals(newsArticle, article, "Incorrect result for valid article");
        assertEquals(newsArticle.hashCode(), article.hashCode(), "Incorrect result for valid article");
    }

    @Test
    public void testNoArticles() {
        Article[] articles = {};
        Page page = new Page(articles);

        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(responseMock.body()).thenReturn(gson.toJson(page));

        assertThrows(ClientRequestException.class, () -> newsFeedClient.fetchNews(query),
                "Should throw ClientRequestException when there are no articles");

    }

    @Test
    public void testParameterInvalid() {
        RequestError error = new RequestError("400", "parameterInvalid", "You've included a parameter " +
                "in your request which is currently not supported. Check the message property for more details.");

        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        when(responseMock.body()).thenReturn(gson.toJson(error));

        query = Query.builder(API_KEY, keywords)
                .setSources(source)
                .setPageSize(pageSize)
                .build();

        assertThrows(ClientRequestException.class, () ->  newsFeedClient.fetchNews(query),
                "Should throw ClientRequestException");
    }

    @Test
    public void testServerError() {
        RequestError error = new RequestError("500", "Server Error ", "Something went wrong on our side.");

        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
        when(responseMock.body()).thenReturn(gson.toJson(error));

        query = Query.builder(API_KEY, keywords)
                .setSources(source)
                .setCountry(country)
                .setPageSize(pageSize)
                .build();

        assertThrows(ClientRequestException.class, () ->  newsFeedClient.fetchNews(query),
                "Should throw ClientRequestException");
    }

    @Test
    public void testApiKeyMissing () {
        RequestError error = new RequestError("401", "apiKeyMissing ", "Your API key is missing" +
                " from the request. Append it to the request.");

        when(responseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        when(responseMock.body()).thenReturn(gson.toJson(error));

        assertThrows(ClientRequestException.class, () ->  newsFeedClient.fetchNews(query),
                "Should throw ClientRequestException");
    }

    @Test
    public void testInvalidRequest() throws IOException, InterruptedException {
        when(clientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenThrow(new IllegalArgumentException("Exception occurred"));

        assertThrows(NewsClientException.class, () -> newsFeedClient.fetchNews(query),
                "Should throw NewsClientException when request is invalid");
    }

    @Disabled
    @Test
    public void testWithActualAPI() throws NewsClientException {

        newsFeedClient = new NewsFeedClient(HttpClient.newHttpClient());
        final int PAGE_SIZE = 5;
        keywords = new String[] {"football", "transfer"};
        Query queryPageSize5 = Query.builder(API_KEY, keywords)
                .setCountry(country)
                .setCategory(category)
                .setPageSize(PAGE_SIZE)
                .build();
        News news = new News(newsFeedClient.fetchNews(queryPageSize5));
        Article articleFromPage1 = news.getPage(0).getArticle(0);
        System.out.println(articleFromPage1.getTitle());

    }


}