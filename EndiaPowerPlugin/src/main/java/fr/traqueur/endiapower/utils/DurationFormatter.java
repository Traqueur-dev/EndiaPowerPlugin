package fr.traqueur.endiapower.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to format the duration of a time in a readable format.
 */
public class DurationFormatter {
	private static final long oneMinute = TimeUnit.MINUTES.toMillis(1);
	private static final long oneHour = TimeUnit.HOURS.toMillis(1);
	private static final long oneDay = TimeUnit.DAYS.toMillis(1);
	public static ThreadLocal<DecimalFormat> remainingSeconds = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
	public static ThreadLocal<DecimalFormat> remainingSecondsTrailing = ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));

	public static String getRemaining(long millis, boolean milliseconds) {
		return DurationFormatter.getRemaining(millis, milliseconds, true);
	}

	public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
		if (milliseconds && duration < oneMinute) {
			return String.valueOf(
					(trail ? remainingSecondsTrailing : remainingSeconds).get().format((double) duration * 0.001))
					+ 's';
		}
		if (duration >= oneDay) {
			return DurationFormatUtils.formatDuration(duration, "dd-HH:mm:ss");
		}
		return DurationFormatUtils.formatDuration(duration,
				(duration >= oneHour ? "HH:" : "") + "mm:ss");
	}

}