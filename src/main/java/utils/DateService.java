package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateService {

	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
	}

	public static Date zeroTime(final Date date ){
		return setTime( date, 0, 0, 0, 0 );
	}

	public static Date setTime( final Date date, final int hourOfDay, final int minute, final int second, final int ms ){
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime( date );
		gc.set( Calendar.HOUR_OF_DAY, hourOfDay );
		gc.set( Calendar.MINUTE, minute );
		gc.set( Calendar.SECOND, second );
		gc.set( Calendar.MILLISECOND, ms );
		return gc.getTime();
	}
}