package com.example.management_system.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import java.io.InputStreamReader;
import java.util.Collections;

public class GmailConfig {
    public static Gmail getGmailService() throws Exception {
        var JSON_FACTORY = GsonFactory.getDefaultInstance();
        var CREDENTIALS_FOLDER = new java.io.File("credentials");
        var inputStream = GmailConfig.class.getResourceAsStream("/client_secret.json");
        var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));

        var flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                Collections.singletonList("https://www.googleapis.com/auth/gmail.send")
        ).setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                .setAccessType("offline")
                .build();

        var receiver = new LocalServerReceiver.Builder().setPort(8889).build();
        var credentials = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credentials)
                .setApplicationName("Mandela Factory Outlet")
                .build();
    }
}
