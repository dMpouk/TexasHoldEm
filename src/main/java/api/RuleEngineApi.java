package api;

import domain.HandRank;
import domain.Player;

public interface RuleEngineApi {

  HandRank hasFolded(Player selectedPlayer);

  public HandRank hasRoyalFlush(Player selectedPlayer);

  public HandRank hasStraightFlush(Player selectedPlayer);

  public HandRank hasFourOfAKind(Player selectedPlayer);

  public HandRank hasFullHouse(Player selectedPlayer);

  public HandRank hasFlush(Player selectedPlayer);

  public HandRank hasStraight(Player selectedPlayer);

  public HandRank hasThreeOfAKind(Player selectedPlayer);

  public HandRank hasTwoPair(Player selectedPlayer);

  public HandRank hasOnePair(Player selectedPlayer, Character... excludedFace);

  public HandRank hasHighCard(Player selectedPlayer);
}
