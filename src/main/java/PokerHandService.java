import domain.Card;
import domain.HandRank;
import domain.Player;
import enums.HandRankCategoryEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PokerHandService {

  private static final String ONE_SPACE = " ";
  private static final String WINNER = "(winner)";

  public String analyze(String allCards) {
    List<Player> players = initializePlayers(allCards);

    decideHandRank(players);

    Player winner = decideWinner(players);

    return buildResult(players, winner);
  }

  private List<Player> initializePlayers(String allCards) {
    String[] playersCards = allCards.split(System.lineSeparator());
    List<Player> players = new ArrayList<>();
    for (String playerCards : playersCards) {
      Player currentPlayer = new Player();
      parsePlayerCards(currentPlayer, playerCards);
      players.add(currentPlayer);
    }
    return players;
  }

  private void decideHandRank(List<Player> players) {
    HandRankService handRankService = new HandRankService();
    for (Player currentPlayer : players) {
      HandRank currentHandRank = handRankService.decideHandRank(currentPlayer);
      currentPlayer.setHandRank(currentHandRank);
    }
  }

  private Player decideWinner(List<Player> players) {
    Comparator<Player> playerComparator = Comparator.reverseOrder();
    List<Player> filtered =
        players.stream()
            .filter(p -> !p.getHandRank().getHandRankCategory().equals(HandRankCategoryEnum.FOLDED))
            .collect(Collectors.toList());
    List<Player> sorted = filtered.stream().sorted(playerComparator).collect(Collectors.toList());
    return sorted.get(0);
  }

  private String buildResult(List<Player> players, Player winner) {
    StringBuilder result = new StringBuilder();
    for (Player currentPlayer : players) {
      result
          .append(getAllCardsPrinted(currentPlayer))
          .append(ONE_SPACE)
          .append(currentPlayer.getHandRank().getHandRankCategory().getDescription())
          .append((currentPlayer.equals(winner) ? ONE_SPACE + WINNER : ""))
          .append(System.lineSeparator());
    }
    return result.toString().trim();
  }

  private void parsePlayerCards(Player currentPlayer, String playerCards) {
    String[] currentLineSeparatedCards = playerCards.trim().split(ONE_SPACE);

    int counter = 0;
    for (String selectedCard : currentLineSeparatedCards) {
      Card card = new Card(selectedCard.charAt(0), selectedCard.charAt(1));
      if (counter < 2) {
        currentPlayer.addHoleCard(card);
      } else {
        currentPlayer.addCommunityCard(card);
      }
      counter++;
    }
  }

  private String getAllCardsPrinted(Player player) {
    List<Card> allPlayerCards = new ArrayList<>(player.getHoleCards());
    allPlayerCards.addAll(player.getCommunityCards());

    StringBuilder result = new StringBuilder();
    int index = 1;
    for (Card card : allPlayerCards) {
      result.append(card.getFace()).append(card.getSuit());
      if (index != allPlayerCards.size()) {
        result.append(ONE_SPACE);
      }
      index++;
    }
    return result.toString();
  }

}
