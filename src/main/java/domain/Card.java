package domain;

public class Card implements Comparable<Card> {

  private char face;
  private char suit;

  public Card(char face, char suit) {
    this.face = face;
    this.suit = suit;
  }

  public char getFace() {
    return face;
  }

  public int getFaceNumericRepresentation() {
    return replaceLetterFaces(this.face);
  }

  public char getSuit() {
    return suit;
  }

  @Override
  public int compareTo(Card other) {
    int thisFace = replaceLetterFaces(this.face);
    int otherFace = replaceLetterFaces(other.face);

    if (thisFace > otherFace) {
      return 1;
    } else if (thisFace < otherFace) {
      return -1;
    }
    return 0;
  }

  private int replaceLetterFaces(char face) {
    switch (face) {
      case 'A':
        return 14;
      case 'K':
        return 13;
      case 'Q':
        return 12;
      case 'J':
        return 11;
      case 'T':
        return 10;
      default:
        return Integer.parseInt(String.valueOf(face));
    }
  }
}
