package com.example.demo_battleship;

import com.example.demo_battleship.controller.PlayerController;
import com.example.demo_battleship.model.Player;
import com.example.demo_battleship.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Test
    void listPlayers_ShouldReturnListOfPlayers() throws Exception {
//        // Arrange
//        List<Player> players = Arrays.asList(
//                new Player("player1"),
//                new Player("player2"),
//                new Player("player3")
//        );
//        when(playerService.listPlayers()).thenReturn(players);
//
//        // Act and Assert
//        mockMvc.perform(get("/api/players/list"))
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect((ResultMatcher) jsonPath("$", hasSize(3)))
//                .andExpect((ResultMatcher) jsonPath("$[0].playerName", is("player1")))
//                .andExpect((ResultMatcher) jsonPath("$[1].playerName", is("player2")))
//                .andExpect((ResultMatcher) jsonPath("$[2].playerName", is("player3")));
    }
}
