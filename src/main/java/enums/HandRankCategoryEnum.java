package enums;

public enum HandRankCategoryEnum {

  FOLDED(-1, ""),
  HIGH_CARD(0, "High Card"),
  ONE_PAIR(1, "One Pair", 0, 2),
  TWO_PAIR(2, "Two Pair", 0, 0),
  THREE_OF_A_KIND(3, "Three Of A Kind",0, 1),
  STRAIGHT(4, "Straight"),
  FLUSH(5, "Flush"),
  FULL_HOUSE(6, "Full House"),
  FOUR_OF_A_KIND(7, "Four Of A Kind"),
  STRAIGHT_FLUSH(8, "Straight Flush"),
  ROYAL_FLUSH(9, "Royal Flush")
  ;

  HandRankCategoryEnum(int rankCode, String description) {
    this.rankCode = rankCode;
    this.description = description;
  }

  HandRankCategoryEnum(int rankCode, String description, int minKickerIndex, int maxKickerIndex) {
    this.rankCode = rankCode;
    this.description = description;
    this.minKickerIndex = minKickerIndex;
    this.maxKickerIndex = maxKickerIndex;
  }

  int rankCode;
  String description;
  int minKickerIndex;
  int maxKickerIndex;

  public int getRankCode() {
    return rankCode;
  }

  public String getDescription() {
    return description;
  }

  public int getMinKickerIndex() {
    return minKickerIndex;
  }

  public int getMaxKickerIndex() {
    return maxKickerIndex;
  }
}
