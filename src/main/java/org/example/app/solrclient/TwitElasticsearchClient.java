package org.example.app.solrclient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.example.app.entity.Twit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TwitElasticsearchClient {

    private final String HOSTNAME;

    private final int PORT;

    private final ElasticsearchClient client = init();

    public TwitElasticsearchClient(@Value("${elastic.hostname}")String hostname, @Value("${elastic.port}")int port) {
        this.HOSTNAME = hostname;
        this.PORT = port;
    }

    public ElasticsearchClient init () {
        RestClient restClient = RestClient.builder(
                        new HttpHost("localhost", 9200)
                )
                .build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Value("${elastic.index}")
    private String INDEX;
    
    @SneakyThrows
    public void addDoc(Twit twit) {
        JsonData jsonData = JsonData.fromJson("{\"id\":\"" + twit.getId()
                + "\",\"content\":\"" + twit.getContent() + "\"}");
        IndexResponse response = client.index(i -> i
                .index(INDEX)
                .document(jsonData)
        );
    }

    @SneakyThrows
    public List<String> findListIdByContent(String userQuery) {
        SearchResponse<JsonData> response = client.search(s -> s
                        .index(INDEX)
                        .query(q -> q
                                .match(t -> t
                                        .field("content")
                                        .query(userQuery)
                                )
                        ),
                JsonData.class
        );
        List<Hit<JsonData>> hits = response.hits().hits();
        return hits.stream()
                .map(hit -> hit.source().toJson().asJsonObject().get("id").toString().replaceAll("\"", ""))
                .collect(Collectors.toList());
    }
}
