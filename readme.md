# Simple Sync Shim for Azure Cosmos DB Java client

The Azure Cosmos DB Java library [azure-cosmosdb](https://mvnrepository.com/artifact/com.microsoft.azure/azure-cosmosdb) 
doesn't currently support sync APIs which can cause some friction in trying to use it. This repo is a simple sample of building 
a sync shim around the SDK.

Cosmos DB will eventually support Sync APIs and Async APIs in the azure-cosmosdb library, at which point I'll update this repo.

## Using this client

This client isn't published to maven, so you'll need to grab the source code and build it yourself.

```java
        ConnectionPolicy connectionPolicy = new ConnectionPolicy();
        connectionPolicy.setConnectionMode(ConnectionMode.Direct);
        AsyncDocumentClient asyncDocumentClient = new AsyncDocumentClient.Builder()
                .withServiceEndpoint("")
                .withMasterKeyOrResourceToken("")
                .withConnectionPolicy(connectionPolicy)
                .build();

        // Create our sync shim client that uses the async client
        SyncShimCosmosClient client = new SyncShimCosmosClient(asyncDocumentClient);

        // Read a document
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setPartitionKey(new PartitionKey("fizzbuzz"));
        client.readDocument("/dbs/foo/colls/bar/docs/baz", requestOptions);

```

## Support

This client isn't officially supported and intended more as sample code. 

## License

[MIT](./LICENSE)