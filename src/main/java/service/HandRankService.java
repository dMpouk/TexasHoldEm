package service;

import api.RuleEngineApi;
import domain.HandRank;
import domain.Player;

public class HandRankService {

  private RuleEngineApi ruleEngine = new RuleEngine();

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
    handRank = (handRank == null) ? hasHighCard(selectedPlayer) : handRank;

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
    return ruleEngine.hasThreeOfAKind(selectedPlayer);
  }

  private HandRank hasTwoPair(Player selectedPlayer) {
    return ruleEngine.hasTwoPair(selectedPlayer);
  }

  private HandRank hasOnePair(Player selectedPlayer, Character... excludedFace) {
    return ruleEngine.hasOnePair(selectedPlayer, excludedFace);
  }

  private HandRank hasHighCard(Player selectedPlayer) {
    return ruleEngine.hasHighCard(selectedPlayer);
  }

}
