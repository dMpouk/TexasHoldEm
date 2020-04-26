package service;

import domain.Card;
import domain.HandRank;
import enums.HandRankCategoryEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleEngineService {

  private static final char HEART_SUIT = 'h';
  private static final char DIAMOND_SUIT = 'd';
  private static final char CLUB_SUIT = 'c';
  private static final char SPADE_SUIT = 's';
  private static final char ACE = 'A';

  public HandRank createHandRank(
      HandRankCategoryEnum handRankCategory,
      Card selectedCard,
      Card secondCard,
      List<Card> cards) {
    HandRank handRank = new HandRank();
    handRank.setHandRankCategory(handRankCategory);
    handRank.setAt(selectedCard);
    handRank.setSecond(secondCard);
    if (cards != null) {
      List<Card> kickers =
          cards.stream()
              .filter(c -> c.getFace() != selectedCard.getFace())
              .filter(c -> secondCard == null || c.getFace() != secondCard.getFace())
              .collect(Collectors.toList())
              .subList(handRankCategory.getMinKickerIndex(), handRankCategory.getMaxKickerIndex());
      handRank.setKickers(kickers);
    }
    return handRank;
  }

  private boolean canUseWildCard(List<Card> sortedCards) {
    return sortedCards.get(sortedCards.size() - 1).getFaceNumericRepresentation() == 2;
  }

  private boolean containsSuitedAce(List<Card> sortedCards, char currentSuit) {
    return sortedCards.stream().anyMatch(c -> c.getFace() == ACE && c.getSuit() == currentSuit);
  }

  public void countSuits(
      StringBuilder suitString, Map<Character, Long> suitCounters, List<Long> counters) {
    long count = suitString.chars().filter(c -> c == HEART_SUIT).count();
    counters.add(count);
    suitCounters.put(HEART_SUIT, count);
    count = suitString.chars().filter(c -> c == CLUB_SUIT).count();
    counters.add(count);
    suitCounters.put(CLUB_SUIT, count);
    count = suitString.chars().filter(c -> c == DIAMOND_SUIT).count();
    counters.add(count);
    suitCounters.put(DIAMOND_SUIT, count);
    count = suitString.chars().filter(c -> c == SPADE_SUIT).count();
    counters.add(count);
    suitCounters.put(SPADE_SUIT, count);
  }

  public int increaseSequenceIfWildcard(List<Card> sortedCards, int sequence) {
    if (canUseWildCard(sortedCards)
        && hasAce(sortedCards)) {
      sequence++;
    }
    return sequence;
  }

  public int increaseSequenceIfSuitedWildcard(List<Card> sortedCards, int sequence,
      Card currentCard) {
    char currentSuit = currentCard.getSuit();
    if (canUseWildCard(sortedCards)
        && containsSuitedAce(sortedCards, currentSuit)) {
      sequence++;
    }
    return sequence;
  }

  private boolean hasAce(List<Card> sortedCards) {
    return sortedCards.stream().anyMatch(c -> c.getFace() == ACE);
  }

  public Card findSameFaces(int numberOfSameFaces, List<Card> cards){
    int sameFaces = 1;
    for (int i = 0; i < cards.size(); i++) {
      Card selectedCard = cards.get(i);
      for (int j = i + 1; j < cards.size(); j++) {
        if (cards.get(j).getFace() == selectedCard.getFace()) {
          sameFaces++;
        }
      }
      if (sameFaces >= numberOfSameFaces) {
        return selectedCard;
      }
      sameFaces = 1;
    }
    return null;
  }

  public List<Card> removeExcludedFaces(
      List<Card> sortedCards, List<Card> filteredCards, Character[] excludedFace) {
    if (excludedFace.length > 0) {
      List<Character> excludedCardsList = Arrays.asList(excludedFace);
      filteredCards =
          sortedCards.stream()
              .filter(card -> !excludedCardsList.contains(card.getFace()))
              .collect(Collectors.toList());
    }
    return filteredCards;
  }

  public List<Card> searchForPairs(List<Card> sortedCards) {
    List<Card> pairs = new ArrayList<>();
    for (int i = 0; i < sortedCards.size(); i++) {
      Card selectedCard = sortedCards.get(i);
      for (int j = i + 1; j < sortedCards.size(); j++) {
        if (sortedCards.get(j).getFace() == selectedCard.getFace()) {
          pairs.add(selectedCard);
        }
      }
    }
    return pairs;
  }

}
