package leaderboard;

import com.example.demo_battleship.model.Player;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

// Clasa HttpClient are metoda pt a prolua lista de players
public class HttpClient {
    private static final String BASE_URL = "http://localhost:8080/api/players";

    public static List<Player> getPlayersList() {
        RestTemplate restTemplate = new RestTemplate();
        Player[] playersArray = restTemplate.getForObject(BASE_URL + "/list", Player[].class);
        return Arrays.asList(playersArray);
    }
}
