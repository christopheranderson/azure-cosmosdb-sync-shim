package com.microsoft.azure.syncshim;

import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.RequestOptions;
import com.microsoft.azure.cosmosdb.ResourceResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Observable;
import rx.Scheduler;

public class SyncShimCosmosClient {
    private final AsyncDocumentClient asyncClient;
    private final Scheduler scheduler;

    public SyncShimCosmosClient(AsyncDocumentClient asyncClient) {
        this.asyncClient = asyncClient;
        this.scheduler = rx.schedulers.Schedulers.io();
    }

    public SyncShimCosmosClient(AsyncDocumentClient asyncClient, Scheduler customScheduler) {
        this.asyncClient = asyncClient;
        this.scheduler = customScheduler;
    }

    public ResourceResponse<Document> readDocument(String documentLink, RequestOptions options) {
        final Observable<ResourceResponse<Document>> response = this.asyncClient.readDocument(documentLink, options);
        return response.observeOn(scheduler).toBlocking().single();
    }

    public ResourceResponse<Document> createDocument(String collectionLink, Object document, RequestOptions options, boolean disableAutomaticIdGeneration) {
        final Observable<ResourceResponse<Document>> response = this.asyncClient.createDocument(collectionLink, document, options, disableAutomaticIdGeneration);
        return response.observeOn(scheduler).toBlocking().single();
    }

    public ResourceResponse<Document> replaceDocument(Document document, RequestOptions options) {
        final Observable<ResourceResponse<Document>> response = this.asyncClient.replaceDocument(document, options);
        return response.observeOn(scheduler).toBlocking().single();
    }

    public ResourceResponse<Document> deleteDocument(String documentLink, RequestOptions options) {
        final Observable<ResourceResponse<Document>> response = this.asyncClient.deleteDocument(documentLink, options);
        return response.observeOn(scheduler).toBlocking().single();
    }
}
