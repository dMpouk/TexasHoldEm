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
    if (straightFlushHandRank != null
        && straightFlushHandRank.getAt().getFace() == ACE) {
      HandRank handRank = straightFlushHandRank;
      handRank.setHandRankCategory(HandRankCategoryEnum.ROYAL_FLUSH);
      return handRank;
    }
    return null;
  }

  public HandRank hasStraightFlush(Player selectedPlayer) {
    HandRank handRank = new HandRank();
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    int sequence = 1;
    for (int i = 0; i < sortedCards.size(); i++) {
      Card selectedCard = sortedCards.get(i);
      Card currentCard = selectedCard;
      for (int j = i + 1; j < sortedCards.size(); j++) {
        if ((((sortedCards.get(j).getFaceNumericRepresentation() + 1)
            == currentCard.getFaceNumericRepresentation())
            || (sortedCards.get(j).getFaceNumericRepresentation() == 2
            && sortedCards.stream().anyMatch(c -> c.getFace() == ACE)))
            && ((sortedCards.get(j).getSuit()) == currentCard.getSuit())) {
          sequence++;
          currentCard = sortedCards.get(j);
        } else {
          break;
        }
      }
      char currentSuit = currentCard.getSuit();
      if(sortedCards.get(sortedCards.size()-1).getFaceNumericRepresentation() == 2
          && sortedCards.stream().anyMatch(c -> c.getFace() == ACE && c.getSuit() == currentSuit)){
        sequence++;
      }
      if (sequence > 4) {
        handRank.setHandRankCategory(HandRankCategoryEnum.STRAIGHT_FLUSH);
        handRank.setAt(selectedCard);
        return handRank;
      } else {
        sequence = 1;
      }
    }
    return null;
  }

  public HandRank hasFourOfAKind(Player selectedPlayer) {
    HandRank handRank = new HandRank();
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    int sameFaces = 1;
    for (int i = 0; i < sortedCards.size(); i++) {
      Card selectedCard = sortedCards.get(i);
      for (int j = i + 1; j < sortedCards.size(); j++) {
        if (sortedCards.get(j).getFace() == selectedCard.getFace()) {
          sameFaces++;
        }
      }
      if (sameFaces > 3) {
        // TODO : extract
        handRank.setAt(selectedCard);
        handRank.setHandRankCategory(HandRankCategoryEnum.FOUR_OF_A_KIND);
        List<Card> kickers =
            sortedCards.stream()
                .filter(c -> c.getFace() == selectedCard.getFace())
                .collect(Collectors.toList())
                .subList(0, 0);
        handRank.setKickers(kickers);
        return handRank;
      }
      sameFaces = 1;
    }
    return null;
  }

  public HandRank hasFullHouse(Player selectedPlayer) {
    HandRank threeOfAKindHandRank = hasTheeOfAKind(selectedPlayer);
    HandRank onePairHandRank =
        (threeOfAKindHandRank != null)
            ? hasOnePair(selectedPlayer, threeOfAKindHandRank.getAt().getFace())
            : null;

    if (threeOfAKindHandRank != null && onePairHandRank != null) {
      HandRank handRank = new HandRank();
      handRank.setHandRankCategory(HandRankCategoryEnum.FULL_HOUSE);
      handRank.setAt(threeOfAKindHandRank.getAt());
      handRank.setSecond(onePairHandRank.getAt());
      return handRank;
    }
    return null;
  }

  public HandRank hasFlush(Player selectedPlayer) {
    StringBuilder suitString = new StringBuilder();
    selectedPlayer.getAllCardsSorted().forEach(card -> suitString.append(card.getSuit()));

    Map<Character, Long> suitCounters = new HashMap<>();
    List<Long> counters = new ArrayList<>();
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
      HandRank handRank = new HandRank();
      handRank.setHandRankCategory(HandRankCategoryEnum.FLUSH);
      handRank.setAt(highestCard);
      return handRank;
    }

    return null;
  }

  public HandRank hasStraight(Player selectedPlayer) {
    HandRank handRank = new HandRank();
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    int sequence = 1;
    for (int i = 0; i < sortedCards.size(); i++) {
      Card selectedCard = sortedCards.get(i);
      Card currentCard = selectedCard;
      for (int j = i + 1; j < sortedCards.size(); j++) {
        if (((sortedCards.get(j).getFaceNumericRepresentation() + 1)
            == currentCard.getFaceNumericRepresentation())
        ) {
          sequence++;
          currentCard = sortedCards.get(j);
        } else {
          break;
        }
      }
      if(sortedCards.get(sortedCards.size()-1).getFaceNumericRepresentation() == 2
          && sortedCards.stream().anyMatch(c -> c.getFace() == ACE)){
        sequence++;
      }
      if (sequence > 4) {
        handRank.setHandRankCategory(HandRankCategoryEnum.STRAIGHT);
        handRank.setAt(selectedCard);
        return handRank;
      } else {
        sequence = 1;
      }
    }

    return null;
  }

  public HandRank hasTheeOfAKind(Player selectedPlayer) {
    HandRank handRank = new HandRank();
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    int sameFaces = 1;
    for (int i = 0; i < sortedCards.size(); i++) {
      Card selectedCard = sortedCards.get(i);
      for (int j = i + 1; j < sortedCards.size(); j++) {
        if (sortedCards.get(j).getFace() == selectedCard.getFace()) {
          sameFaces++;
        }
      }
      if (sameFaces > 2) {
        // TODO : extract
        handRank.setAt(selectedCard);
        handRank.setHandRankCategory(HandRankCategoryEnum.THREE_OF_A_KIND);
        List<Card> kickers =
            sortedCards.stream()
                .filter(c -> c.getFace() == selectedCard.getFace())
                .collect(Collectors.toList())
                .subList(0, 1);
        handRank.setKickers(kickers);
        return handRank;
      }
      sameFaces = 1;
    }
    return null;
  }

  public HandRank hasTwoPair(Player selectedPlayer) {
    HandRank handRank = new HandRank();
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();

    List<Card> pairs = new ArrayList<>();
    for (int i = 0; i < sortedCards.size(); i++) {
      Card selectedCard = sortedCards.get(i);
      for (int j = i + 1; j < sortedCards.size(); j++) {
        if (sortedCards.get(j).getFace() == selectedCard.getFace()) {
          pairs.add(selectedCard);
        }
      }
    }
    if (pairs.size() > 1) {
      handRank.setHandRankCategory(HandRankCategoryEnum.TWO_PAIR);
      pairs.sort(Card::compareTo);
      handRank.setAt(pairs.get(0));
      handRank.setSecond(pairs.get(1));
      List<Card> kickers =
          sortedCards.stream()
              .filter(c -> c.getFace() != handRank.getAt().getFace())
              .filter(c -> c.getFace() != handRank.getSecond().getFace())
              .collect(Collectors.toList())
              .subList(0, 0);
      handRank.setKickers(kickers);
      return handRank;
    }
    return null;
  }

  public HandRank hasOnePair(Player selectedPlayer, Character... excludedFace) {
    HandRank handRank = new HandRank();
    List<Card> sortedCards = selectedPlayer.getAllCardsSorted();
    List<Card> filteredCards = new ArrayList<>(sortedCards);

    if (excludedFace.length > 0) {
      List<Character> excludedCardsList = Arrays.asList(excludedFace);
      filteredCards =
          sortedCards.stream()
              .filter(card -> !excludedCardsList.contains(card.getFace()))
              .collect(Collectors.toList());
    }

    for (int i = 0; i < filteredCards.size(); i++) {
      Card selectedCard = filteredCards.get(i);
      for (int j = i + 1; j < filteredCards.size(); j++) {
        if (filteredCards.get(j).getFace() == selectedCard.getFace()) {
          handRank.setHandRankCategory(HandRankCategoryEnum.ONE_PAIR);
          handRank.setAt(selectedCard);
          List<Card> kickers =
              filteredCards.stream()
                  .filter(c -> c.getFace() != selectedCard.getFace())
                  .collect(Collectors.toList())
                  .subList(0, 2);
          handRank.setKickers(kickers);
          return handRank;
        }
      }
    }
    return null;
  }
}
