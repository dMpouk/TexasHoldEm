import domain.Card;
import domain.HandRank;
import domain.Player;
import enums.HandRankCategoryEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleEngine {

  private static final char HEART_SUIT = 'h';
  private static final char DIAMOND_SUIT = 'd';
  private static final char CLUB_SUIT = 'c';
  private static final char SPADE_SUIT = 's';
  private static final char ACE = 'A';
  private static final char MAX_CARDS = 7;

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

      sequence = increaseSequenceIfSuitedWildcard(sortedCards, sequence, currentCard);
      if (sequence > 4) {
        return createHandRank(HandRankCategoryEnum.STRAIGHT_FLUSH, selectedCard, null, null);
      } else {
        sequence = 1;
      }

    }
    return null;
  }

  public HandRank hasFourOfAKind(Player selectedPlayer) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();
    Card sameFacesResult = findSameFaces(4, sortedCards);
    return (sameFacesResult != null) ? createHandRank(HandRankCategoryEnum.FOUR_OF_A_KIND, sameFacesResult, null, sortedCards) : null;
  }

  public HandRank hasFullHouse(Player selectedPlayer) {
    HandRank threeOfAKindHandRank = hasThreeOfAKind(selectedPlayer);
    HandRank onePairHandRank =
        (threeOfAKindHandRank != null)
            ? hasOnePair(selectedPlayer, threeOfAKindHandRank.getAt().getFace())
            : null;

    if (threeOfAKindHandRank != null && onePairHandRank != null) {
      return createHandRank(HandRankCategoryEnum.FULL_HOUSE, threeOfAKindHandRank.getAt(), onePairHandRank.getAt(), null);
    }
    return null;
  }

  public HandRank hasFlush(Player selectedPlayer) {
    StringBuilder suitString = new StringBuilder();
    selectedPlayer.getAllCardsSorted().forEach(card -> suitString.append(card.getSuit()));

    Map<Character, Long> suitCounters = new HashMap<>();
    List<Long> counters = new ArrayList<>();
    countSuits(suitString, suitCounters, counters);

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
      return createHandRank(HandRankCategoryEnum.FLUSH, highestCard, null, null);
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

      sequence = increaseSequenceIfWildcard(sortedCards, sequence);
      if (sequence > 4) {
        return createHandRank(HandRankCategoryEnum.STRAIGHT, selectedCard, null, null);
      } else {
        sequence = 1;
      }
    }

    return null;
  }

  public HandRank hasThreeOfAKind(Player selectedPlayer) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();
    Card sameFacesResult = findSameFaces(3, sortedCards);
    return (sameFacesResult != null) ? createHandRank(HandRankCategoryEnum.THREE_OF_A_KIND, sameFacesResult, null, sortedCards) : null;
  }

  public HandRank hasTwoPair(Player selectedPlayer) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    List<Card> pairs = searchForPairs(sortedCards);
    if (pairs.size() > 1) {
      pairs.sort(Card::compareTo);
      return createHandRank(HandRankCategoryEnum.TWO_PAIR, pairs.get(0), pairs.get(1), sortedCards);
    }
    return null;
  }

  public HandRank hasOnePair(Player selectedPlayer, Character... excludedFace) {
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();
    List<Card> filteredCards = new ArrayList<>(sortedCards);
    filteredCards = removeExcludedFaces(sortedCards, filteredCards, excludedFace);

    List<Card> sortedPairs = searchForPairs(filteredCards);
    if (!sortedPairs.isEmpty()) {
      return createHandRank(HandRankCategoryEnum.ONE_PAIR, sortedPairs.get(0), null, filteredCards);
    }
    return null;
  }

  private HandRank createHandRank(
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

  private void countSuits(
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

  private int increaseSequenceIfWildcard(List<Card> sortedCards, int sequence) {
    if (canUseWildCard(sortedCards)
        && hasAce(sortedCards)) {
      sequence++;
    }
    return sequence;
  }

  private int increaseSequenceIfSuitedWildcard(List<Card> sortedCards, int sequence,
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

  private List<Card> removeExcludedFaces(
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

  private List<Card> searchForPairs(List<Card> sortedCards) {
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
