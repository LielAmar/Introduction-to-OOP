import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 * @author Dan Nirel
 */
class ChatterBot {

	public static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
	public static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";
	public static final String REQUEST_PREFIX = "say ";

	private final String name;
	private final Random rand = new Random();
	private final String[] repliesToLegalRequest;
	private final String[] repliesToIllegalRequest;

	/**
	 * A constructor for ChatterBot
	 *
	 * @param name                      Name of the bot
	 * @param repliesToLegalRequest     Possible replies to Legal requests
	 * @param repliesToIllegalRequest   Possible replies to Illegal requests
	 */
	public ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
		this.name = name;
		this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
		this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];

		System.arraycopy(repliesToLegalRequest, 0, this.repliesToLegalRequest, 0, repliesToLegalRequest.length);
		System.arraycopy(repliesToIllegalRequest, 0, this.repliesToIllegalRequest, 0, repliesToIllegalRequest.length);
	}

	/**
	 * @return   Bot's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Generates a reply from the bot to the given statement
	 *
	 * @param statement   Statement to reply to
	 * @return            Generated reply
	 */
	public String replyTo(String statement) {
		if(statement.startsWith(REQUEST_PREFIX)) {
			return this.replacePlaceholderInARandomPattern(
					statement.replaceFirst(REQUEST_PREFIX, ""), // we don’t repeat the request prefix, so delete it from the reply
					REQUESTED_PHRASE_PLACEHOLDER,
					this.repliesToLegalRequest
			);
		}

		return this.replacePlaceholderInARandomPattern(
				statement,
				ILLEGAL_REQUEST_PLACEHOLDER,
				this.repliesToIllegalRequest
		);
	}

	/**
	 * Creates a reply from the given statement, a placeholder and possible replies
	 *
	 * @param statement     Statement to reply to
	 * @param placeholder   Placeholder to use for the statement
	 * @param replies       Possible replies
	 * @return              Generated reply
	 */
	private String replacePlaceholderInARandomPattern(String statement, String placeholder, String[] replies) {
		int randomIndex = this.rand.nextInt(replies.length);

		String reply = replies[randomIndex];

		return reply.replaceAll(placeholder, statement);
	}
}
