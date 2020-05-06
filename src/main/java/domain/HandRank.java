package domain;

import enums.HandRankCategoryEnum;
import java.util.Comparator;
import java.util.List;

public class HandRank implements Comparable<HandRank> {

  private HandRankCategoryEnum handRankCategory;
  private Card at;
  //Optional
  private Card second;
  private List<Card> kickers;

  public HandRank() {
  }

  public HandRank(HandRankCategoryEnum handRankCategory, Card at, Card second,
      List<Card> kickers) {
    this.handRankCategory = handRankCategory;
    this.at = at;
    this.second = second;
    this.kickers = kickers;
  }

  public HandRank(HandRankCategoryEnum handRankCategory, Card at,
      List<Card> kickers) {
    this.handRankCategory = handRankCategory;
    this.at = at;
    this.kickers = kickers;
  }

  public HandRankCategoryEnum getHandRankCategory() {
    return handRankCategory;
  }

  public void setHandRankCategory(HandRankCategoryEnum handRankCategory) {
    this.handRankCategory = handRankCategory;
  }

  public Card getAt() {
    return at;
  }

  public void setAt(Card at) {
    this.at = at;
  }

  public Card getSecond() {
    return second;
  }

  public void setSecond(Card second) {
    this.second = second;
  }

  public List<Card> getKickers() {
    return kickers;
  }

  public void setKickers(List<Card> kickers) {
    this.kickers = kickers;
  }

  @Override
  public int compareTo(HandRank other) {
    return Comparator.comparing(HandRank::getHandRankCategory)
        .thenComparing(HandRank::getAt)
        .thenComparing(HandRank::getSecond)
        .thenComparing(kickersComparator())
        .compare(this, other);
  }

  private Comparator<HandRank> kickersComparator() {
    return (o1, o2) -> {
      int result = 0;
      for (int i = 0; i < o1.getKickers().size(); i++) {
        result = o1.getKickers().get(i).compareTo(o2.getKickers().get(i));
        if (result != 0) {
          return result;
        }
      }
      return result;
    };
  }
}
