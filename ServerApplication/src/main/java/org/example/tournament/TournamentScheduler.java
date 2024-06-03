package org.example.tournament;

import java.util.*;

public class TournamentScheduler {
    private final int n; // numar de jucatori
    private final int p; // numar maxim de meciuri pe zi
    private final int d; // numar maxim de zile
    private List<Player> players; // lista de jucatori
    private final Map<String, Integer> matchResults; // rezultatele meciurilor

    public TournamentScheduler(int n, int p, int d) {
        this.n = n;
        this.p = p;
        this.d = d;
        this.matchResults = new HashMap<>();
        initializePlayers();
    }

    private void initializePlayers() {
        players = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Player player = new Player(i);
            // setam ca fiecare jucator sa joace un meci cu ceilalti n-1
            for (int j = 0; j < n; j++) {
                if (j != i) { // nu il luam pe el insusi
                    player.addMatch(j); // adaugam id-ul celuilalt jucator cu care trebuie sa joace
                }
            }
            players.add(player);
        }
        //Collections.shuffle(players); // randomizam ordinea jucatorilor
    }

    public void scheduleTournament() {
        int totalMatches = n * (n - 1) / 2;
        int totalSlots = d * p;

        if (totalMatches > totalSlots) {
            System.out.println("the tournament cannot be scheduled with the given constraints.");
            return;
        }

        // creez o lista de indici si o amestec
        List<Integer> playerIndices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            playerIndices.add(i);
        }
        Collections.shuffle(playerIndices);

        List<List<Integer>> scheduleDays = new ArrayList<>();
        for (int i = 0; i < d; i++) {
            scheduleDays.add(new ArrayList<>()); // adaug noi sloturi de meciuri pentru fiecare zi
        }

        int matchIndex = 0;
        Queue<Integer> playerQueue = new LinkedList<>(playerIndices);

        Random random = new Random(); // pentru generarea rezultatelor aleatorii

        for (int dayIndex = 0; dayIndex < d; dayIndex++) {
            int matchesScheduledForDay = 0;

            while (!playerQueue.isEmpty() && matchesScheduledForDay < p) {
                int playerIndex = playerQueue.poll();
                Player player1 = players.get(playerIndex);
                Set<Integer> matchesRemaining = player1.getMatchesRemaining();
                if (matchesRemaining.isEmpty()) {
                    continue;
                }

                Iterator<Integer> iterator = matchesRemaining.iterator();
                int matchId = iterator.next();
                iterator.remove();

                Player player2 = players.get(matchId);
                player2.getMatchesRemaining().remove(player1.getId());

                scheduleDays.get(dayIndex).add(player1.getId());
                scheduleDays.get(dayIndex).add(player2.getId());

                // genereaza rezultatul meciului
                boolean player1Wins = random.nextBoolean();
                if (player1Wins) {
                    matchResults.put(player1.getId() + "-" + player2.getId(), player1.getId());
                } else {
                    matchResults.put(player1.getId() + "-" + player2.getId(), player2.getId());
                }

                matchesScheduledForDay++;
                matchIndex++;

                if (matchIndex == totalMatches) {
                    break;
                }

                // reinserez player2 inapoi in coada daca mai are meciuri de jucat
                if (!player2.getMatchesRemaining().isEmpty()) {
                    playerQueue.offer(matchId);
                }

                // reinserez player1 inapoi in coada daca mai are meciuri de jucat
                if (!player1.getMatchesRemaining().isEmpty()) {
                    playerQueue.offer(playerIndex);
                }
            }

            if (matchIndex == totalMatches) {
                break;
            }
        }

        printSchedule(scheduleDays);
        List<Integer> sequence = findWinningSequence();
        System.out.println("winning sequence:");
        for (int player : sequence) {
            System.out.print("P" + (player + 1) + " -> ");
        }
    }

    private void printSchedule(List<List<Integer>> scheduleDays) {
        for (int dayIndex = 0; dayIndex < scheduleDays.size(); dayIndex++) {
            List<Integer> daySchedule = scheduleDays.get(dayIndex);
            System.out.println("day " + (dayIndex + 1) + " matches:");
            for (int i = 0; i < daySchedule.size(); i += 2) {
                int player1 = daySchedule.get(i);
                int player2 = daySchedule.get(i + 1);
                System.out.println("match between player " + (player1 + 1) + " and player " + (player2 + 1) + " | winner is " + (matchResults.get(player1 + "-" + player2) + 1));
            }
        }
    }

    private List<Integer> findWinningSequence() {
        // creez un graf de victorii
        Map<Integer, Set<Integer>> victoryGraph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            victoryGraph.put(i, new HashSet<>());
        }

        // populez graful cu rezultate
        // mapez de forma : winner -> loser
        for (Map.Entry<String, Integer> entry : matchResults.entrySet()) {
            String[] players = entry.getKey().split("-");
            int winner = entry.getValue();
            //parsez stringul in int
            int loser = Integer.parseInt(players[0]) == winner ? Integer.parseInt(players[1]) : Integer.parseInt(players[0]);
            victoryGraph.get(winner).add(loser);
        }
        System.out.println("victory graph: " + victoryGraph + " lA INDEX aduna +1 pt a obt id ul playerului");

        // caut secventa
        boolean[] visited = new boolean[n];
        List<Integer> sequence = new ArrayList<>();
        //parcurg fiecare nod pt a gasi o secv castigatoare
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                List<Integer> result = findSequenceUtil(i, victoryGraph, visited, sequence);
                if (!result.isEmpty()) {
                    return result; // Daca am gasit o secventa castigatoare, o returnam
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Integer> findSequenceUtil(int current, Map<Integer, Set<Integer>> graph, boolean[] visited, List<Integer> sequence) {
        //vizitez nodul curent
        visited[current] = true;
        //il adaug in secventa
        sequence.add(current);

        //daca secv = cu nr de playeri,termin
        if (sequence.size() == graph.size()) {
            return new ArrayList<>(sequence); // Daca am gasit o secventa completa, o returnam
        }

        // Parcurgem vecinii nodului curent
        for (int neighbor : graph.get(current)) {
            boolean allNeighborsDefeated = true;

            // verific daca vecinul a fost deja vizitat si a fost invins de nodurile din stanga secventei
            for (int defeatedBy : sequence) {
                if (!visited[neighbor] && graph.get(neighbor).contains(defeatedBy)) {
                    allNeighborsDefeated = false;
                    break;
                }
            }
            // daca toti vecinii sunt invinsi de nodurile din stanga secventei, continuam cautarea
            if (allNeighborsDefeated) {
                List<Integer> result = findSequenceUtil(neighbor, graph, visited, sequence);
                if (!result.isEmpty()) {
                    return result; // daca gasesc o secventa castigatoare o returnez
                }
            }
        }

        // Daca ajung aici, inseamna ca nu am putut gasi o secventa valida pornind de la nodul curent
        visited[current] = false;
        sequence.remove(sequence.size() - 1);
        return Collections.emptyList(); // daca nu am gasit nicio secventa castigatoare returnez o lista goala
    }


    public static void main(String[] args) {
        TournamentScheduler scheduler = new TournamentScheduler(4, 3, 2);
        scheduler.scheduleTournament();
    }
}