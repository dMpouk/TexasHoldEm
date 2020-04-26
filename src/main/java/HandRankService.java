import domain.HandRank;
import domain.Player;
import enums.HandRankCategoryEnum;

public class HandRankService {

  //TODO : make it interface
  private RuleEngine ruleEngine = new RuleEngine();

  public HandRank decideHandRank(Player selectedPlayer) {
    HandRank handRank;

    handRank = hasFolded(selectedPlayer);
    handRank = (handRank == null) ? hasRoyalFlush(selectedPlayer) : handRank;
    handRank = (handRank == null) ? hasStraightFlush(selectedPlayer) : handRank;
    handRank = (handRank == null) ? hasFourOfAKind(selectedPlayer) : handRank;
    handRank = (handRank == null) ? hasFullHouse(selectedPlayer) : handRank;
    handRank = (handRank == null) ? hasFlush(selectedPlayer) : handRank;
    handRank = (handRank == null) ? hasStraight(selectedPlayer) : handRank;
    handRank = (handRank == null) ? hasTheeOfAKind(selectedPlayer) : handRank;
    handRank = (handRank == null) ? hasTwoPair(selectedPlayer) : handRank;
    handRank = (handRank == null) ? hasOnePair(selectedPlayer) : handRank;
    if (handRank == null) {
      handRank = new HandRank();
      handRank.setHandRankCategory(HandRankCategoryEnum.HIGH_CARD);
      handRank.setAt(selectedPlayer.getAllCardsSorted().get(0));
      handRank.setKickers(selectedPlayer.getAllCardsSorted().subList(1, 6));
    }
    return handRank;
  }

  private HandRank hasFolded(Player selectedPlayer) {
    return ruleEngine.hasFolded(selectedPlayer);
  }

  private HandRank hasRoyalFlush(Player selectedPlayer) {
    return ruleEngine.hasRoyalFlush(selectedPlayer);
  }

  private HandRank hasStraightFlush(Player selectedPlayer) {
    return ruleEngine.hasStraightFlush(selectedPlayer);
  }

  private HandRank hasFourOfAKind(Player selectedPlayer) {
    return ruleEngine.hasFourOfAKind(selectedPlayer);
  }

  private HandRank hasFullHouse(Player selectedPlayer) {
    return ruleEngine.hasFullHouse(selectedPlayer);
  }

  private HandRank hasFlush(Player selectedPlayer) {
    return ruleEngine.hasFlush(selectedPlayer);
  }

  private HandRank hasStraight(Player selectedPlayer) {
    return ruleEngine.hasStraight(selectedPlayer);
  }

  private HandRank hasTheeOfAKind(Player selectedPlayer) {
    return ruleEngine.hasTheeOfAKind(selectedPlayer);
  }

  private HandRank hasTwoPair(Player selectedPlayer) {
    return ruleEngine.hasTwoPair(selectedPlayer);
  }

  private HandRank hasOnePair(Player selectedPlayer, Character... excludedFace) {
    return ruleEngine.hasOnePair(selectedPlayer, excludedFace);
  }

}
