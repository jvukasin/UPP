package com.naucnacentrala.NaucnaCentrala.ESConfig;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.naucnacentrala.NaucnaCentrala.repository.elasticSearch")
public class Config extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration=ClientConfiguration.builder()
                .connectedTo("localhost:9200", "localhost:9201")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Override
    public ElasticsearchOperations elasticsearchOperations() {
        return super.elasticsearchOperations();
    }
}
