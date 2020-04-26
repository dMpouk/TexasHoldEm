package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Player implements Comparable<Player> {

  // TODO : Reconsider combining the two lists
  private List<Card> holeCards;
  private List<Card> communityCards;
  private HandRank handRank;

  public Player() {
    this.holeCards = new ArrayList<>();
    this.communityCards = new ArrayList<>();
  }

  public void addHoleCard(Card card) {
    holeCards.add(card);
  }

  public void addCommunityCard(Card card) {
    communityCards.add(card);
  }

  public List<Card> getHoleCards() {
    return holeCards;
  }

  public List<Card> getCommunityCards() {
    return communityCards;
  }

  public HandRank getHandRank() {
    return handRank;
  }

  public void setHandRank(HandRank handRank) {
    this.handRank = handRank;
  }

  public List<Card> getAllCardsSorted() {
    List<Card> allPlayerCards = new ArrayList<>(this.getHoleCards());
    allPlayerCards.addAll(this.getCommunityCards());

    Comparator<Card> cardComparator = Comparator.reverseOrder();
    return allPlayerCards.stream().sorted(cardComparator).collect(Collectors.toList());
  }

  public String getAllCardsPrinted() {
    List<Card> allPlayerCards = new ArrayList<>(this.getHoleCards());
    allPlayerCards.addAll(this.getCommunityCards());

    StringBuilder result = new StringBuilder();
    int index = 1;
    for (Card card : allPlayerCards) {
      result.append(card.getFace()).append(card.getSuit());
      if (index != allPlayerCards.size()) {
        result.append(" ");
      }
      index++;
    }
    return result.toString();
  }

  @Override
  public int compareTo(Player other) {
//    int result = 0;
//    if (this.getHandRank().getHandRankCategory().getRankCode()
//        > other.getHandRank().getHandRankCategory().getRankCode()) {
//      return 1;
//    } else if (this.getHandRank().getHandRankCategory().getRankCode()
//        == other.getHandRank().getHandRankCategory().getRankCode()) {
//
//      if (this.getHandRank().getAt().getFaceNumericRepresentation()
//          > other.getHandRank().getAt().getFaceNumericRepresentation()) {
//        return 1;
//      } else if (this.getHandRank().getAt().getFaceNumericRepresentation()
//          < other.getHandRank().getAt().getFaceNumericRepresentation()) {
//        return -1;
//      } else{
//
//
//        }
//
//
//      }
//    }
//    return -1;
    return Comparator.comparing(Player::getHandRank).compare(this, other);
  }
}
