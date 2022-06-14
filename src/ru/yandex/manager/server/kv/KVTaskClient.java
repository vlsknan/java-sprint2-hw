package ru.yandex.manager.server.kv;

import com.google.gson.Gson;
import ru.yandex.manager.server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private HttpClient client;
    private Gson gson;
    private String kvServerUrl;
    private String api_token;

    public KVTaskClient(String kvServerUrl) {
        client = HttpClient.newHttpClient();
        gson = HttpTaskServer.getGson();
        this.kvServerUrl = kvServerUrl;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(kvServerUrl + "/register"))
                .GET()
                .build();

        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                this.api_token = response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (NullPointerException | InterruptedException | IOException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + kvServerUrl
                    + "' возникла ошибка.\n" + "Проверьте, пожалуйста, адрес и повторите попытку." );
        }
    }

    // сохранить на сервер
    public void put(String key, String json) {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(kvServerUrl + "/save/" + key + "?API_TOKEN=" + api_token))
                .POST(body)
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (NullPointerException | InterruptedException | IOException e) {
            System.out.println("Во время выполнения PUT запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    // загрузить с сервера
    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(kvServerUrl + "/load/" + key + "?API_TOKEN=" + api_token))
                .GET()
                .build();
        String value = null;
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                value = response.body();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (NullPointerException | InterruptedException | IOException e) {
            System.out.println("Во время выполнения LOAD запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return value;
    }
}

