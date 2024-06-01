package hack.maze.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AzureStorageConfig {

    @Value("${spring.cloud.azure.storage.blob.endpoint}")
    private String azureBaseEndpoint;

    @Value("${azure-sas-token}")
    private String azureSasToken;

    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint(azureBaseEndpoint)
                .sasToken(azureSasToken)
                .buildClient();
    }


}
