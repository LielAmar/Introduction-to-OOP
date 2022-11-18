import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ChatterBot Class requested in the OOP course, HUJI, Winter 2021-2022 Semester.
 * @author Erel Debel.
 */
class ChatterBotTest {
	static final String BOT_NAME = "Rob";
	static final String REQUEST_PREFIX = "say ";
	static final String LEGAL_STATEMENT = "1";
	static final String ILLEGAL_STATEMENT = "2";

	String[] responses = {
			"A: mixed <phrase>,<request>",
			"B: phrase <phrase>,<phrase>",
			"C: request <request>,<request>"
	};
	String[] legalRequestResponses = {
			"A: mixed 1,<request>",
			"B: phrase 1,1",
			"C: request <request>,<request>"
	};
	String[] illegalRequestResponses = {
			"A: mixed <phrase>,2",
			"B: phrase <phrase>,<phrase>",
			"C: request 2,2"
	};
	ChatterBot bot = new ChatterBot(BOT_NAME, responses, responses);

	@org.junit.jupiter.api.Test
	void arrayDeepCopy() {
		this.responses = new String[]{""};
		String responseToLegalRequest = bot.replyTo(REQUEST_PREFIX);
		assertNotEquals("", responseToLegalRequest);
		String responseToIllegalRequest = bot.replyTo("");
		assertNotEquals("", responseToIllegalRequest);
	}

	@org.junit.jupiter.api.Test
	void getName() {
		assertEquals(BOT_NAME, bot.getName());
	}

	@org.junit.jupiter.api.Test
	void nameAndArraysAreNotStatic() {
		ChatterBot bot2 = new ChatterBot("bob", new String[]{""}, new String[]{""});
		arrayDeepCopy();
		getName();
	}

	@org.junit.jupiter.api.Test
	void replyToLegal() {
		boolean[] responsesUsed = {false, false, false};
		boolean illegalResponse;
		for (int i = 0; i < 10000; i++) {
			String response = bot.replyTo(REQUEST_PREFIX + LEGAL_STATEMENT);
			illegalResponse = true;
			for (int j = 0; j < responsesUsed.length; j++) {
				if (response.equals(legalRequestResponses[j])) {
					responsesUsed[j] = true;
					illegalResponse = false;
				}
			}
			if (illegalResponse) {
				fail("\nBot's response was: \"" + response + "\"\nExpected one of the following:\n\"" +
						legalRequestResponses[0] + "\"\n\"" + legalRequestResponses[1] + "\"\n\"" +
						legalRequestResponses[2] + "\"");
			}
		}
		for (boolean responseUsed : responsesUsed) {
			assertTrue(responseUsed);
		}
	}

	@org.junit.jupiter.api.Test
	void replyToIllegal() {
		boolean[] responsesUsed = {false, false, false};
		boolean illegalResponse;
		for (int i = 0; i < 10000; i++) {
			illegalResponse = true;
			String response = bot.replyTo(ILLEGAL_STATEMENT);
			for (int j = 0; j < responsesUsed.length; j++) {
				if (response.equals(illegalRequestResponses[j])) {
					responsesUsed[j] = true;
					illegalResponse = false;
				}
			}
			if (illegalResponse) {
				fail("\nBot's response was: \"" + response + "\"\nExpected one of the following:\n\"" +
						illegalRequestResponses[0] + "\"\n\"" + illegalRequestResponses[1] + "\"\n\"" +
						illegalRequestResponses[2] + "\"");
			}
		}
		for (boolean responseUsed : responsesUsed) {
			assertTrue(responseUsed);
		}
	}

//	@org.junit.jupiter.api.Test
//	void replacePlaceholderInARandomPattern() {
//		String replaced = bot.replacePlaceholderInARandomPattern(
//				new String[]{"abc"}, "bc", "a");
//		assertEquals("aa", replaced);
//		replaced = bot.replacePlaceholderInARandomPattern(
//				new String[]{"abc"}, "aa", "b");
//		assertEquals("abc", replaced);
//		replaced = bot.replacePlaceholderInARandomPattern(
//				new String[]{"aaa"}, "a", "b");
//		assertEquals("bbb", replaced);
//		replaced = bot.replacePlaceholderInARandomPattern(
//				new String[]{"cbbc"}, "c", "a");
//		assertEquals("abba", replaced);
//	}
}