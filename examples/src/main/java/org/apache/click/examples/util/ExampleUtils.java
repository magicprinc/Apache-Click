package org.apache.click.examples.util;

import org.apache.click.Context;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Provides examples helper methods.
 */
public class ExampleUtils {

	private static final Map<Currency, String> CURRENCY_SYMBOLS = new HashMap<Currency, String>();

	public static Object getSessionObject(Class aClass) {
		if (aClass == null) {
			throw new IllegalArgumentException("Null class parameter.");
		}
		Object object = getContext().getSessionAttribute(aClass.getName());
		if (object == null) {
			try {
				object = aClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return object;
	}

	public static void setSessionObject(Object object) {
		if (object != null) {
			getContext().setSessionAttribute(object.getClass().getName(), object);
		}
	}

	public static void removeSessionObject(Class aClass) {
		if (getContext().hasSession() && aClass != null) {
			getContext().getSession().removeAttribute(aClass.getName());
		}
	}

	public static String getCurrencySymbol(Currency currency) {
		if(currency == null) {
			return "";
		}

		String symbol = CURRENCY_SYMBOLS.get(currency);
		if(symbol != null) {
			return symbol;
		}

		String currencyCode = currency.getCurrencyCode();

		Locale defaultLocale = Locale.getDefault();
		symbol = currency.getSymbol(defaultLocale);
		if(!symbol.equals(currencyCode)) {
			CURRENCY_SYMBOLS.put(currency, symbol);
			return symbol;
		}

		Locale[] locales = Locale.getAvailableLocales();
		for (Locale locale : locales) {
			symbol = currency.getSymbol(locale);
			if (!symbol.equals(currencyCode)) {
				CURRENCY_SYMBOLS.put(currency, symbol);
				return symbol;
			}
		}

		CURRENCY_SYMBOLS.put(currency, currencyCode);
		return currencyCode;
	}

	private static Context getContext() {
		return Context.getThreadLocalContext();
	}
}