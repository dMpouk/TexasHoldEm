import static org.junit.jupiter.api.Assertions.assertEquals;

import enums.HandRankCategoryEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TexasHoldEmKataTest {

  private static final String ONE_SPACE = " ";
  private static final String WINNER = "(winner)";

  PokerHandService pokerHandService;

  @BeforeEach
  public void setUp(){
    this.pokerHandService = new PokerHandService();
  }

  @Test
  public void testHighCard(){
      String allCards = new StringBuilder()
          .append("Kc 9s Ts 5d 7d 3c 6d").append(System.lineSeparator())
          .toString();

      assertEquals("Kc 9s Ts 5d 7d 3c 6d" + ONE_SPACE + HandRankCategoryEnum.HIGH_CARD.getDescription() + ONE_SPACE + WINNER , pokerHandService.calculateHandRank(allCards));
  }

  @Test
  public void testOnePair(){
    String cards = new StringBuilder()
        .append("Kc 9s 5s Td 9d 3c 6d").append(System.lineSeparator())
        .toString();

    assertEquals("Kc 9s 5s Td 9d 3c 6d" + ONE_SPACE + HandRankCategoryEnum.ONE_PAIR.getDescription() + ONE_SPACE + WINNER, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testTwoPair(){
    String cards = new StringBuilder()
        .append("Kc 9s Ks Td 9d 3c 6d").append(System.lineSeparator())
        .toString();

    assertEquals("Kc 9s Ks Td 9d 3c 6d" + ONE_SPACE + HandRankCategoryEnum.TWO_PAIR.getDescription() + ONE_SPACE + WINNER , pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testThreeOfAKind(){
    String cards = new StringBuilder()
        .append("Kc 9s Ks Kd Td 3c 6d").append(System.lineSeparator())
        .toString();

    assertEquals("Kc 9s Ks Kd Td 3c 6d" + ONE_SPACE + HandRankCategoryEnum.THREE_OF_A_KIND.getDescription() + ONE_SPACE + WINNER , pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void calcuteHandRankForTwoPlayersAndDecideWinner(){
    String cards = new StringBuilder()
        .append("Kc 9s Ks Kd Td 3c 6d").append(System.lineSeparator())
        .append("9c Ah Ks Kd 9d 3c 6d").append(System.lineSeparator())
        .toString();

    String expected = new StringBuilder()
        .append("Kc 9s Ks Kd Td 3c 6d").append(ONE_SPACE).append(HandRankCategoryEnum.THREE_OF_A_KIND.getDescription()).append(ONE_SPACE).append(WINNER).append(System.lineSeparator())
        .append("9c Ah Ks Kd 9d 3c 6d").append(ONE_SPACE).append(HandRankCategoryEnum.TWO_PAIR.getDescription())
        .toString();
    assertEquals(expected , pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void calcuteHandRankForTwoPlayersThatHaveTheSameRankAndDecideWinnerByKicker(){
    String cards = new StringBuilder()
        .append("Kc 9s Ts Jd 8d 3c 6d").append(System.lineSeparator())
        .append("9c Ah Ts Jd 8d 3c 6d").append(System.lineSeparator())
        .toString();

    String expected = new StringBuilder()
        .append("Kc 9s Ts Jd 8d 3c 6d").append(ONE_SPACE).append(HandRankCategoryEnum.HIGH_CARD.getDescription()).append(System.lineSeparator())
        .append("9c Ah Ts Jd 8d 3c 6d").append(ONE_SPACE).append(HandRankCategoryEnum.HIGH_CARD.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();

    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testFoldOfAPlayer(){
    String cards = new StringBuilder()
        .append("Kc 9s Ts Jd 8d 3c 6d").append(System.lineSeparator())
        .append("9c Ah Ts Jd 8d 3c")
        .toString();
    String expected = new StringBuilder()
        .append("Kc 9s Ts Jd 8d 3c 6d").append(ONE_SPACE).append(HandRankCategoryEnum.HIGH_CARD.getDescription()).append(ONE_SPACE).append(WINNER).append(System.lineSeparator())
        .append("9c Ah Ts Jd 8d 3c")
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testStraight(){
    String cards = new StringBuilder()
        .append("5c 6s 9s Td 8d 3c 7d").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("5c 6s 9s Td 8d 3c 7d").append(ONE_SPACE).append(HandRankCategoryEnum.STRAIGHT.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testFlush(){
    String cards = new StringBuilder()
        .append("5s 6s 9s Td 8d 3s 7s").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("5s 6s 9s Td 8d 3s 7s").append(ONE_SPACE).append(HandRankCategoryEnum.FLUSH.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testFullHouse(){
    String cards = new StringBuilder()
        .append("Ks 9d 9s Kc 8d 3c Ks").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("Ks 9d 9s Kc 8d 3c Ks").append(ONE_SPACE).append(HandRankCategoryEnum.FULL_HOUSE.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testFourOfAKind(){
    String cards = new StringBuilder()
        .append("Ks 9d 9s 9c 8d 9c Ts").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("Ks 9d 9s 9c 8d 9c Ts").append(ONE_SPACE).append(HandRankCategoryEnum.FOUR_OF_A_KIND.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testStraightFlush(){
    String cards = new StringBuilder()
        .append("Ts 9s Js 3c 8s 2c 7s").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("Ts 9s Js 3c 8s 2c 7s").append(ONE_SPACE).append(HandRankCategoryEnum.STRAIGHT_FLUSH.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testRoyalFlush(){
    String cards = new StringBuilder()
        .append("Ts Js Ks As 2c Qs 7d").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("Ts Js Ks As 2c Qs 7d").append(ONE_SPACE).append(HandRankCategoryEnum.ROYAL_FLUSH.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void calculateHandRankForSixPlayersAndDecideWinnerWhileSomePlayersFolded(){
    String cards = new StringBuilder()
        .append("Kc 9s Ks Kd 9d 3c 6d").append(System.lineSeparator())
        .append("9c Ah Ks Kd 9d 3c 6d").append(System.lineSeparator())
        .append("Ac Qc Ks Kd 9d 3c").append(System.lineSeparator())
        .append("9h 5s").append(System.lineSeparator())
        .append("4d 2d Ks Kd 9d 3c 6d").append(System.lineSeparator())
        .append("7s Ts Ks Kd 9d").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("Kc 9s Ks Kd 9d 3c 6d").append(ONE_SPACE).append(HandRankCategoryEnum.FULL_HOUSE.getDescription()).append(ONE_SPACE).append(WINNER).append(System.lineSeparator())
        .append("9c Ah Ks Kd 9d 3c 6d").append(ONE_SPACE).append(HandRankCategoryEnum.TWO_PAIR.getDescription()).append(System.lineSeparator())
        .append("Ac Qc Ks Kd 9d 3c").append(ONE_SPACE).append(System.lineSeparator())
        .append("9h 5s").append(ONE_SPACE).append(System.lineSeparator())
        .append("4d 2d Ks Kd 9d 3c 6d").append(ONE_SPACE).append(HandRankCategoryEnum.FLUSH.getDescription()).append(System.lineSeparator())
        .append("7s Ts Ks Kd 9d")
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testStraightWithAceAsOne(){
    String cards = new StringBuilder()
        .append("Ad 9s 5d 3c 2s 4c Td").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("Ad 9s 5d 3c 2s 4c Td").append(ONE_SPACE).append(HandRankCategoryEnum.STRAIGHT.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

  @Test
  public void testStraightFlushWithAceAsOne(){
    String cards = new StringBuilder()
        .append("As 9s 5s 3s 2s 4s Td").append(System.lineSeparator())
        .toString();
    String expected = new StringBuilder()
        .append("As 9s 5s 3s 2s 4s Td").append(ONE_SPACE).append(HandRankCategoryEnum.STRAIGHT_FLUSH.getDescription()).append(ONE_SPACE).append(WINNER)
        .toString();
    assertEquals(expected, pokerHandService.calculateHandRank(cards));
  }

}
