package com.microsoft.azure.syncshim;

import com.microsoft.azure.cosmosdb.*;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.Before;
import org.junit.Test;

public class SyncShimCosmosClientTest {

    private String key;
    private String endpoint;
    private Dotenv dotenv;

    @Before
    public void Before() {
        // Just using .env for convenience, not required for anything
        this.dotenv = Dotenv.load();
        this.endpoint = dotenv.get("TEST_COSMOS_ENDPOINT");
        this.key = dotenv.get("TEST_COSMOS_KEY");
    }

    // This test fails unless you set up a collection ahead of time
    @Test
    public void doSimpleCrud() {
        ConnectionPolicy connectionPolicy = new ConnectionPolicy();
        connectionPolicy.setConnectionMode(ConnectionMode.Direct);
        AsyncDocumentClient asyncDocumentClient = new AsyncDocumentClient.Builder()
                .withServiceEndpoint(this.endpoint)
                .withMasterKeyOrResourceToken(this.key)
                .withConnectionPolicy(connectionPolicy)
                .build();

        // Create our sync shim client that uses the async client
        SyncShimCosmosClient client = new SyncShimCosmosClient(asyncDocumentClient);

        // Create a document
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.setPartitionKey(new PartitionKey("fizzbuzz"));
            Document doc = new Document("{\"id\":\"baz\", \"pk\":\"fizzbuzz\",\"message\":\"Hello world!\"}");
            client.createDocument("/dbs/foo/colls/bar", doc, requestOptions, true);
        }

        // Read a document
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.setPartitionKey(new PartitionKey("fizzbuzz"));
            final ResourceResponse<Document> response = client.readDocument("/dbs/foo/colls/bar/docs/baz", requestOptions);
            System.out.println(response.getResource().toJson());
        }
    }
}
