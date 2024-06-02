package org.example.connection;


import com.example.demo_battleship.model.Game;
import org.springframework.web.client.RestTemplate;
import com.example.demo_battleship.model.Player;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HttpClient {

    public static String sendPostRequest(String urlString, String jsonInputString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
    public static String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
    public static int getPlayerId(String playerName) throws Exception {
        String urlString = "http://localhost:8080/api/players/take_Id/" + playerName;
        String response = sendGetRequest(urlString);
        return Integer.parseInt(response);
    }
    public static int getPlayerIdWithPlayerTeamId(int playerTeamId) throws Exception {
        String urlString = "http://localhost:8080/api/players/take_id_by_playerTeamId/" + playerTeamId;
        String response = sendGetRequest(urlString);
        return Integer.parseInt(response);
    }

    public static Iterable<Player> getPlayersList() {
        RestTemplate restTemplate = new RestTemplate();
        Player[] playersArray = restTemplate.getForObject("http://localhost:8080/api/players" + "/list", Player[].class);
        return Arrays.asList(playersArray);

    }

    public static Iterable<Game> getGameList() {
        RestTemplate restTemplate = new RestTemplate();
        Game[] gamesArray = restTemplate.getForObject("http://localhost:8080/api/games" + "/list", Game[].class);
        return Arrays.asList(gamesArray);
    }

}
