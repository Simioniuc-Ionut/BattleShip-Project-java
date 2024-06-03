package org.example.connection;

import com.example.demo_battleship.model.Game;
import org.json.JSONObject;
import com.example.demo_battleship.model.Move;
import com.example.demo_battleship.model.Ship;
import org.springframework.web.client.RestTemplate;
import com.example.demo_battleship.model.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class HttpClient {

    public static String sendPostRequest(String urlString, String jsonInputString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
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

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    //players
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

    //game
    public static Iterable<Game> getGameList() {
        RestTemplate restTemplate = new RestTemplate();
        Game[] gamesArray = restTemplate.getForObject("http://localhost:8080/api/games" + "/list", Game[].class);
        return Arrays.asList(gamesArray);
    }

    //ships
    public static void addShip(Integer gameId, Integer playerId, String shipType, String startPosition, String endPosition, boolean isSunk) throws Exception {
        String urlString = "http://localhost:8080/api/ships/addShip/" + gameId + "/" + playerId;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shipType", shipType);
        jsonObject.put("startPosition", startPosition);
        jsonObject.put("endPosition", endPosition);
        jsonObject.put("isSunk", isSunk);
        String jsonInputString = jsonObject.toString();
        sendPostRequest(urlString, jsonInputString);
    }

    public static void sinkShip(Integer gameId, Integer playerId, String shipType) throws Exception {
        String urlString = "http://localhost:8080/api/ships/sinkShip/" + gameId + "/" + playerId + "/" + shipType;
        sendPostRequest(urlString, "");
    }
    //move

    public static void recordMove(Integer gameId, Integer playerId, String move, boolean isHit) throws Exception {
        String urlString = "http://localhost:8080/api/moves/record/" + gameId + "/" + playerId + "?move=" + move + "&isHit=" + isHit;
        sendPostRequest(urlString, "");
    }


    public static Iterable<Move> getMoveList() {
        RestTemplate restTemplate = new RestTemplate();
        Move[] movesArray = restTemplate.getForObject("http://localhost:8080/api/moves" + "/list", Move[].class);
        return Arrays.asList(movesArray);
    }

    public static Iterable<Ship> getShipList() {
        RestTemplate restTemplate = new RestTemplate();
        Ship[] shipsArray = restTemplate.getForObject("http://localhost:8080/api/ships" + "/list", Ship[].class);
        return Arrays.asList(shipsArray);
    }

}
