package apps.threedbuttonsexperiment.logger;

/**
 * The Class KeyPressRecord.
 */
public class KeyPressRecord {

	/** The charactor. */
	protected String charactor;

	/** The duration. */
	protected long duration;

	/** The press time. */
	protected long pressTime;

	/**
	 * Instantiates a new key press record.
	 *
	 * @param pressTime
	 *            the press time
	 * @param charactor
	 *            the charactor
	 * @param lastKeyPressTime
	 *            the last key press time
	 */
	public KeyPressRecord(long pressTime, String charactor,
			long lastKeyPressTime) {
		super();
		this.pressTime = pressTime;
		this.charactor = charactor;
		this.duration = pressTime - lastKeyPressTime;
	}

	/**
	 * Gets the charactor.
	 *
	 * @return the charactor
	 */
	public String getCharactor() {
		return charactor;
	}

	/**
	 * Gets the duration time.
	 *
	 * @return the duration time
	 */
	public long getDurationTime() {
		return duration;
	}

	/**
	 * Gets the press time.
	 *
	 * @return the press time
	 */
	public long getPressTime() {
		return pressTime;
	}

}
