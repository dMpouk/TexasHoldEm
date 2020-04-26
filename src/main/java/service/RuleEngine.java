package service;

import domain.Card;
import domain.HandRank;
import domain.Player;
import enums.HandRankCategoryEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleEngine {

  private static final char ACE = 'A';
  private static final char MAX_CARDS = 7;

  private RuleEngineService ruleEngineService = new RuleEngineService();

  public HandRank hasFolded(Player selectedPlayer) {
    if (selectedPlayer.getAllCardsSorted().size() < MAX_CARDS) {
      HandRank handRank = new HandRank();
      handRank.setHandRankCategory(HandRankCategoryEnum.FOLDED);
      return handRank;
    }
    return null;
  }

  public HandRank hasRoyalFlush(Player selectedPlayer) {
    HandRank straightFlushHandRank = hasStraightFlush(selectedPlayer);
    if (straightFlushHandRank != null && straightFlushHandRank.getAt().getFace() == ACE) {
      HandRank handRank = straightFlushHandRank;
      handRank.setHandRankCategory(HandRankCategoryEnum.ROYAL_FLUSH);
      return handRank;
    }
    return null;
  }

  public HandRank hasStraightFlush(Player selectedPlayer) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    int sequence = 1;
    for (int i = 0; i < sortedCards.size(); i++) {
      Card selectedCard = sortedCards.get(i);
      Card currentCard = selectedCard;
      for (int j = i + 1; j < sortedCards.size(); j++) {
        if (((sortedCards.get(j).getFaceNumericRepresentation() + 1)
                    == currentCard.getFaceNumericRepresentation())
            && ((sortedCards.get(j).getSuit()) == currentCard.getSuit())) {
          sequence++;
          currentCard = sortedCards.get(j);
        } else {
          break;
        }
      }
      sequence = ruleEngineService.increaseSequenceIfSuitedWildcard(sortedCards, sequence, currentCard);
      if (sequence > 4) {
        return ruleEngineService.createHandRank(HandRankCategoryEnum.STRAIGHT_FLUSH, selectedCard, null, null);
      } else {
        sequence = 1;
      }
    }
    return null;
  }

  public HandRank hasFourOfAKind(Player selectedPlayer) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();
    Card sameFacesResult = ruleEngineService.findSameFaces(4, sortedCards);
    return (sameFacesResult != null) ? ruleEngineService.createHandRank(HandRankCategoryEnum.FOUR_OF_A_KIND, sameFacesResult, null, sortedCards) : null;
  }

  public HandRank hasFullHouse(Player selectedPlayer) {
    HandRank threeOfAKindHandRank = hasThreeOfAKind(selectedPlayer);
    HandRank onePairHandRank =
        (threeOfAKindHandRank != null)
            ? hasOnePair(selectedPlayer, threeOfAKindHandRank.getAt().getFace())
            : null;

    if (threeOfAKindHandRank != null && onePairHandRank != null) {
      return ruleEngineService.createHandRank(HandRankCategoryEnum.FULL_HOUSE, threeOfAKindHandRank.getAt(), onePairHandRank.getAt(), null);
    }
    return null;
  }

  public HandRank hasFlush(Player selectedPlayer) {
    StringBuilder suitString = new StringBuilder();
    selectedPlayer.getAllCardsSorted().forEach(card -> suitString.append(card.getSuit()));

    Map<Character, Long> suitCounters = new HashMap<>();
    List<Long> counters = new ArrayList<>();
    ruleEngineService.countSuits(suitString, suitCounters, counters);

    counters.sort(Collections.reverseOrder());
    long highestCounter = counters.get(0);
    if (highestCounter > 4) {
      char mostFrequentSuit =
          suitCounters.entrySet().stream()
              .filter(entry -> entry.getValue() == highestCounter)
              .findFirst()
              .get()
              .getKey();
      Card highestCard =
          selectedPlayer.getAllCardsSorted().stream()
              .filter(card -> card.getSuit() == mostFrequentSuit)
              .findFirst()
              .get();
      return ruleEngineService.createHandRank(HandRankCategoryEnum.FLUSH, highestCard, null, null);
    }
    return null;
  }

  public HandRank hasStraight(Player selectedPlayer) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    int sequence = 1;
    for (int i = 0; i < sortedCards.size(); i++) {
      Card selectedCard = sortedCards.get(i);
      Card currentCard = selectedCard;
      for (int j = i + 1; j < sortedCards.size(); j++) {
        if (((sortedCards.get(j).getFaceNumericRepresentation() + 1)
            == currentCard.getFaceNumericRepresentation())) {
          sequence++;
          currentCard = sortedCards.get(j);
        } else {
          break;
        }
      }
      sequence = ruleEngineService.increaseSequenceIfWildcard(sortedCards, sequence);
      if (sequence > 4) {
        return ruleEngineService.createHandRank(HandRankCategoryEnum.STRAIGHT, selectedCard, null, null);
      } else {
        sequence = 1;
      }
    }
    return null;
  }

  public HandRank hasThreeOfAKind(Player selectedPlayer) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();
    Card sameFacesResult = ruleEngineService.findSameFaces(3, sortedCards);
    return (sameFacesResult != null) ? ruleEngineService.createHandRank(HandRankCategoryEnum.THREE_OF_A_KIND, sameFacesResult, null, sortedCards) : null;
  }

  public HandRank hasTwoPair(Player selectedPlayer) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    List<Card> pairs = ruleEngineService.searchForPairs(sortedCards);
    if (pairs.size() > 1) {
      pairs.sort(Card::compareTo);
      return ruleEngineService.createHandRank(HandRankCategoryEnum.TWO_PAIR, pairs.get(0), pairs.get(1), sortedCards);
    }
    return null;
  }

  public HandRank hasOnePair(Player selectedPlayer, Character... excludedFace) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();
    List<Card> filteredCards = new ArrayList<>(sortedCards);
    filteredCards = ruleEngineService.removeExcludedFaces(sortedCards, filteredCards, excludedFace);

    List<Card> sortedPairs = ruleEngineService.searchForPairs(filteredCards);
    if (!sortedPairs.isEmpty()) {
      return ruleEngineService.createHandRank(HandRankCategoryEnum.ONE_PAIR, sortedPairs.get(0), null, filteredCards);
    }
    return null;
  }

  public HandRank hasHighCard(Player selectedPlayer) {
    return ruleEngineService.createHandRank(HandRankCategoryEnum.HIGH_CARD, selectedPlayer.getAllCardsSorted().get(0), null, selectedPlayer.getAllCardsSorted());
  }
}
