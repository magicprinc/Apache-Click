package org.apache.click.extras.control;

import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Чуть чистит ввод пользователя.
 * 24ч часы (без AM/PM)
 */
public class DateFieldEx extends DateField {

	public DateFieldEx (String name) { super(name);}//new

	public DateFieldEx (String name, String label) { super(name, label);}//new

	public DateFieldEx (String name, boolean required) { super(name, required);}//new

	public DateFieldEx (String name, String label, boolean required) { super(name, label, required);}//new

	public DateFieldEx (String name, String label, int size) { super(name, label, size);}//new

	public DateFieldEx (String name, String label, int size, boolean required) { super(name, label, size, required);}//new

	public DateFieldEx () {}//new


	/**
	 * Set the DateField value.
	 *
	 * @param value the DateField value
	 */
	@Override	public void setValue (String value) {
		if (value != null && value.length() > 0) {
			String v = value.trim().replace('/', '.').replace('\\', '.').replace('-', '.');//ru.hack

			try {
				Date parsedDate = getDateFormat().parse(v);

				// Cache date for subsequent retrievals
				date = new Date(parsedDate.getTime());

			} catch (ParseException ignore) {	date = null; }

			super.setValue(v);

		} else {
			date = null;
			super.setValue("");
		}//i
	}//setValue


	@Override protected void addCalenderTranslations (List<Element> headElements) {
		super.addCalenderTranslations(headElements);
		JsScript script = new JsScript();
		script.setId("datefield-js-setup-24h");
		if (!headElements.contains(script)) {
			HtmlStringBuffer buffer = new HtmlStringBuffer(150);
			buffer.append("Date.prototype.getAMPMHour = function() { var hour = this.getHours(); return hour;}\n");
			buffer.append("Date.prototype.getAMPM = function() { return \"\";}");
			script.setContent(buffer.toString());
			headElements.add(script);
		}
	}//addCalenderTranslations

}