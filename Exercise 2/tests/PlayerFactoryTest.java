import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the RendererFactory Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
class PlayerFactoryTest {
	/**
	 * string representing a human player.
	 */
	public static final String HUMAN = "human";
	public static final String HUMAN1 = "HuMan";

	/**
	 * string representing a random player.
	 */
	public static final String WHATEVER = "whatever";
	public static final String WHATEVER1 = "whAteVer";

	/**
	 * string representing a "clever" player.
	 */
	public static final String CLEVER = "clever";
	public static final String CLEVER1 = "cLeVer";


	/**
	 * string representing a "genius" player.
	 */
	public static final String GENIUS = "genius";
	public static final String GENIUS1 = "geNiuS";

	private static final String HUMAN_PLAYER = "HumanPlayer";
	private static final String WHATEVER_PLAYER = "WhateverPlayer";
	private static final String CLEVER_PLAYER = "CleverPlayer";
	private static final String SNARTYPAMTS_PLAYER = "GeniusPlayer";

	private final PlayerFactory PF = new PlayerFactory();

	/**
	 * Checks that buildPlayer builds according to the correct strings and returns null otherwise.
	 */
	@Test
	void checkBuildPlayer() {
		assertNull(PF.buildPlayer(""));
		assertNull(PF.buildPlayer("player"));

		Player humanPlayer = PF.buildPlayer(HUMAN);
		assertNotNull(humanPlayer);
		assertEquals(HUMAN_PLAYER, humanPlayer.getClass().getSimpleName());

		Player humanPlayer1 = PF.buildPlayer(HUMAN1);
		assertNotNull(humanPlayer1);
		assertEquals(HUMAN_PLAYER, humanPlayer1.getClass().getSimpleName());

		Player whateverPlayer = PF.buildPlayer(WHATEVER);
		assertNotNull(whateverPlayer);
		assertEquals(WHATEVER_PLAYER, whateverPlayer.getClass().getSimpleName());

		Player whateverPlayer1 = PF.buildPlayer(WHATEVER1);
		assertNotNull(whateverPlayer1);
		assertEquals(WHATEVER_PLAYER, whateverPlayer1.getClass().getSimpleName());

		Player cleverPlayer = PF.buildPlayer(CLEVER);
		assertNotNull(cleverPlayer);
		assertEquals(CLEVER_PLAYER, cleverPlayer.getClass().getSimpleName());

		Player cleverPlayer1 = PF.buildPlayer(CLEVER1);
		assertNotNull(cleverPlayer1);
		assertEquals(CLEVER_PLAYER, cleverPlayer1.getClass().getSimpleName());

		Player geniusPlayer = PF.buildPlayer(GENIUS);
		assertNotNull(geniusPlayer);
		assertEquals(SNARTYPAMTS_PLAYER, geniusPlayer.getClass().getSimpleName());

		Player geniusPlayer1 = PF.buildPlayer(GENIUS1);
		assertNotNull(geniusPlayer1);
		assertEquals(SNARTYPAMTS_PLAYER, geniusPlayer1.getClass().getSimpleName());
	}
}