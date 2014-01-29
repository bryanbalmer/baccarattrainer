package baccarattrainer;

import com.bryanbalmer.playingcards.*;

/**
 * A generic entity that can be Player or Banker
 * @author Bryan
 */
public class BaccPlayer {
    private Card[] cards;
    private boolean hasInitialCards;
    private boolean hasThirdCard;
    
    public BaccPlayer() {
        cards = new Card[3];
        hasInitialCards = false;
        hasThirdCard = false;
    }
    
    // Each player assigned two cards to start round
    public void setInitialCards(Card card1, Card card2) {
        if (!hasInitialCards) {
            cards[0] = card1;
            cards[1] = card2;
            hasInitialCards = true;
        }
    }
    
    // Third card is assigned if rules dictate
    public void setThirdCard(Card card3) {
        if (!hasThirdCard) {
            cards[2] = card3;
            hasThirdCard = true;
        }
    }
    
    // Clear cards to begin new round
    public void resetCards() {
        cards[0] = null;
        cards[1] = null;
        cards[2] = null;
        hasInitialCards = false;
        hasThirdCard = false;
    }
    
    // Score is determined to be the sum of the cards, mod 10
    // Gives sum of first two cards
    public int initialScore() {
        return ((cardValue(cards[0].getRank()) +
                cardValue(cards[1].getRank())) % 10);
    }
    
    // Gives sum of all cards or first two if there was no third card
    public int finalScore() {
        if (hasThirdCard) {
            return ((this.initialScore() +
                    cardValue(cards[2].getRank())) % 10);
        } else {
            return this.initialScore();
        }
    }
    
    // Takes int 1, 2, or 3 to get card value
    public int getCardValue(int c) {
        return cardValue(cards[--c].getRank());
    }
    
    // Takes int 1, 2, or 3 to get card
    public Card getCard(int c) {
        return cards[--c];
    }
    
    // Returns Baccarat card value for given card rank
    private int cardValue(Rank rank) {
        switch (rank) {
            case ACE:   return 1;
            case TWO:   return 2;
            case THREE: return 3;
            case FOUR:  return 4;
            case FIVE:  return 5;
            case SIX:   return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE:  return 9;
            // The rest of the cards (TEN, JACK, QUEEN, KING) are 0
            default:    return 0;
        }
    }
}
