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

  public int getFaceNumericRepresentation(){
    return replaceLetterFaces(this.face);
  }

  public char getSuit() {
    return suit;
  }

  @Override
  public int compareTo(Card other) {
      int thisFace = replaceLetterFaces(this.face);
      int otherFace = replaceLetterFaces(other.face);

      if(thisFace > otherFace){
        return 1;
      }else if(thisFace < otherFace){
        return -1;
      }
      return 0;
  }

  private int replaceLetterFaces(char face) {
    String faceString = String.valueOf(face);
    String filteredString = faceString.replace("A", "14")
        .replace("K", "13")
        .replace("Q", "12")
        .replace("J", "11")
        .replace("T", "10");
    return Integer.parseInt(filteredString);
  }
}
