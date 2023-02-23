import java.util.Locale;

/**
 * A factory for Player type
 */

public class PlayerFactory {

    /**
     * Builds a player from the given string
     *
     * @param playerName   Name of the player to build
     * @return             Built player object
     */
    public Player buildPlayer(String playerName) {
        switch(playerName.toLowerCase(Locale.ENGLISH)) {
            case "human":
                return new HumanPlayer();
            case "whatever":
                return new WhateverPlayer();
            case "clever":
                return new CleverPlayer();
            case "genius":
                return new GeniusPlayer();
            default:
                return null;
        }
    }
}