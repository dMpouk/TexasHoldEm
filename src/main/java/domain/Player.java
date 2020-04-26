package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Player implements Comparable<Player> {

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

  @Override
  public int compareTo(Player other) {
    return Comparator.comparing(Player::getHandRank).compare(this, other);
  }
}
