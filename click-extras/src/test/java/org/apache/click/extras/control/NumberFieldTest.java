package org.apache.click.extras.control;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.apache.click.MockContext;
import org.apache.click.control.Form;
import org.apache.click.servlet.MockRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.MVEL;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NumberFieldTest {

  Locale defaultLocale;

  @Before public void setUp () {
    defaultLocale = Locale.getDefault();
    Locale.setDefault(Locale.US);
  }

  @After public void tearDown () {
    Locale.setDefault(defaultLocale);
  }

  @Test public void testFormat() {
    MockContext.initContext(Locale.US);

    Number decNum = 2.56f;

    NumberField engF = new NumberField("en");

    assertNull(engF.getPattern());
    engF.setPattern("#.00");
    assertEquals("#.00", engF.getPattern());
    engF.setPattern(null);
    assertNull(engF.getPattern());

    engF.setValue("some Text");
    assertEquals("some Text", engF.getValue());
    assertNull(engF.getNumber());

    engF.setValue("12.456,5656");
    assertEquals("12.456,5656", engF.getValue());
    assertEquals(12.456, engF.getNumber());

    engF.setNumber(decNum);
    assertEquals("2.56", engF.getValue());
    assertEquals(2.56d, engF.getNumber().doubleValue(), 0);

    engF.setValue("123.6");
    assertEquals(123.6d, engF.getNumber().doubleValue(), 0);
    assertEquals(engF.getNumber(), engF.getValueObject());

    engF.setPattern("0");
    engF.setNumber(123.6f);
    assertEquals("124", engF.getValue());
    assertEquals(124, engF.getNumber().intValue());

    engF.setValue("123.6");
    assertEquals("123.6", engF.getValue());
    assertEquals(123.6f, engF.getNumber().floatValue(), 0);

    engF.setPattern("0.00");
    engF.setNumber(123.6f);
    assertEquals("123.60", engF.getValue());
    assertEquals(123.6f, engF.getNumber().floatValue(), 0);

    engF.setValue("12.223");
    assertEquals(12.223f, engF.getNumber().floatValue(), 0);

    //keeps the pattern
    engF.setNumberFormat(NumberFormat.getInstance(Locale.GERMAN));
    engF.setNumber(decNum);
    assertEquals("2,56", engF.getValue());
    engF.setValue("3456,134");
    assertEquals(3456.134f, engF.getNumber().floatValue(), 0);

    MockContext.initContext(Locale.GERMANY);

    NumberField germanF = new NumberField("de");

    germanF.setNumber(decNum);
    assertEquals("2,56", germanF.getValue());
    germanF.setValue("3.456,134");
    assertEquals(3456.134f, germanF.getNumber().floatValue(), 0);
  }

  @Test public void testOnProcess() {
    MockContext mockContext = MockContext.initContext(Locale.US);
    MockRequest req = mockContext.getMockRequest();
    Map<String, String[]> params = req.getParameterMap();

    NumberField engF = new NumberField("en");
    engF.setPattern("#,##0.00");

    engF.setValidate(false);
    params.put("en", new String[]{"no number"});
    assertTrue(engF.onProcess());
    assertEquals("no number", engF.getValue());
    assertTrue(engF.isValid());
    assertNull(engF.getNumber());
    engF.validate();
    assertFalse(engF.isValid());

    engF = new NumberField("en");
    engF.setPattern("#,##0.00");
    params.put("en", new String[]{"12.3"});

    engF.setValidate(false);
    assertTrue(engF.onProcess());
    assertEquals("12.3", engF.getValue());
    assertEquals(12.3f, engF.getNumber().floatValue(), 0);
    engF.validate();
    assertEquals("12.30", engF.getValue());

    engF = new NumberField("en");
    engF.setPattern("#,##0.00");
    params.put("en", new String[]{"12.3"});

    assertTrue(engF.onProcess());
    assertEquals("12.30", engF.getValue());
    assertEquals("12.3", req.getParameter(engF.getName()));

    params.put("en", new String[]{"some value"});
    assertTrue(engF.onProcess());
    assertEquals("some value", engF.getValue());
    assertNull(engF.getNumber());
    assertEquals("some value", req.getParameter(engF.getName()));
  }

  @Test public void testValidate() {
    MockContext mockContext = MockContext.initContext(Locale.US);
    MockRequest req = mockContext.getMockRequest();
    Map<String, String[]> params = req.getParameterMap();

    NumberField engF = new NumberField("en");
    engF.setPattern("0");

    engF.setMaxValue(100);
    engF.setMinValue(1);
    engF.setRequired(true);

    params.put("en", new String[]{"2.23"});
    assertTrue(engF.onProcess());
    assertTrue(engF.isValid());
    assertEquals("2", engF.getValue());

    engF.setValue("123,45");
    engF.validate();
    assertFalse(engF.isValid());
    assertEquals("123,45", engF.getValue());

    engF.setValue("-12");
    engF.validate();
    assertFalse(engF.isValid());
    assertEquals("-12", engF.getValue());

    engF = new NumberField("en");
    engF.setPattern("0");

    // Test required + blank value
    engF.setRequired(true);
    params.put("en", new String[]{""});

    assertTrue(engF.onProcess());
    assertFalse(engF.isValid());
    assertEquals(0, engF.getValue().length());

    engF.setValue("");
    assertFalse(engF.isValid());
    assertEquals("", engF.getValue());

    engF.setValue("some text");
    assertFalse(engF.isValid());
    assertEquals("some text", engF.getValue());
  }


  /**
   * Test that the fix for number->BigDecimal conversion work.
   *
   * CLK-694.
   */
  @Test public void testFormCopyBigDecimal() {
    MockContext.initContext(Locale.US);

    Form form = new Form("form");

    NumberField bigDecimalField = new NumberField("bigDecimalField");
    NumberField bigIntegerField = new NumberField("bigIntegerField");

    // Specify a very large value
    final String bigValue = Long.toString(Long.MIN_VALUE);
    bigDecimalField.setValue(bigValue);
    assertEquals(bigValue, bigDecimalField.getValue());
    assertEquals(bigValue, bigDecimalField.getNumber().toString());
    assertEquals(bigValue, bigDecimalField.getValueObject().toString());
    form.add(bigDecimalField);

    bigIntegerField.setValue(bigValue);
    assertEquals(bigValue, bigIntegerField.getValue());
    form.add(bigIntegerField);

    MyObj obj = new MyObj();
    form.copyTo(obj);

    assertEquals(obj.bigDecimalField.getClass() + "=" + obj.bigDecimalField.longValue(),
        bigValue, obj.bigDecimalField.toString());
    assertEquals(bigValue, obj.bigIntegerField.toString());
  }

  static boolean isTestHard (){
    // wtf? doesn't work under gradle?
    return System.getProperty("test.hard") != null;
  }

  /**
   * Test that Field->BigInteger conversion works.
   */
  @Test public void testFormCopyBigInteger() {
    MockContext.initContext(Locale.US);

    Form form = new Form("form");

    NumberField bigDecimalField = new NumberField("bigDecimalField");
    NumberField bigIntegerField = new NumberField("bigIntegerField");
    NumberField longField = new NumberField("longField");// there is a specialized LongField!

    // very large value      9223372036854775808
    final String bigValue = "999999999999999999";
    bigDecimalField.setValue(bigValue);
    form.add(bigDecimalField);
    System.out.println(bigDecimalField.getValueObject().getClass());
    assertTrue(bigDecimalField.getValueObject() instanceof Long);// wtf?
    long d = (Long) bigDecimalField.getValueObject();
    assertEquals(bigDecimalField.getValueObject().getClass() + " = " + bigDecimalField.getValueObject(), bigValue,
        bigDecimalField.getValueObject().toString());
    assertEquals(bigDecimalField.getValueObject().getClass() + " = " + bigDecimalField.getValueObject(), bigValue, "" + d);
    //
    bigIntegerField.setValue(bigValue);
    form.add(bigIntegerField);
    //
    longField.setValue(bigValue);
    form.add(longField);

    MyObj obj = new MyObj();
    form.copyTo(obj);

    assertEquals(bigValue, obj.bigIntegerField.toString());
    assertEquals(bigValue, "" + obj.longField);
    if (isTestHard()){
      assertEquals(bigValue, obj.bigDecimalField.toString());// todo MVEL bug: https://github.com/mvel/mvel/issues/313
    }
  }

  @Test public void testMvelPure () {
    final String bigValue = "999999999999999999";
    MyObj obj = new MyObj();

    final String expr = "obj.bigDecimalField = bigValue; obj.bigIntegerField = bigValue";
    MVEL.eval(expr, ImmutableMap.of("obj", obj, "bigValue", bigValue));

    assertEquals(bigValue, obj.bigIntegerField.toString());
    assertEquals(bigValue, obj.bigDecimalField.toString());

    obj.bigDecimalField = new BigDecimal(Long.parseLong(bigValue));
    assertEquals(bigValue, obj.bigDecimalField.toString());

    MVEL.eval(expr, ImmutableMap.of("obj", obj, "bigValue", Long.valueOf(bigValue)));

    assertEquals(bigValue, obj.bigIntegerField.toString());
    if (isTestHard()){
      assertEquals(bigValue, obj.bigDecimalField.toString());// todo MVEL bug: https://github.com/mvel/mvel/issues/313
    }
  }


  @Test public void testBugReport () {
    final String bigValue = "999999999999999999";
    MyObj obj = new MyObj();
    final String expr = "obj.bigDecimalField = bigValue";

    MVEL.eval(expr, ImmutableMap.of("obj", obj, "bigValue", bigValue));
    assertEquals(bigValue, obj.bigDecimalField.toString());// OK

    assertEquals(bigValue, new BigDecimal(Long.valueOf(bigValue).toString()).toString());

    MVEL.eval(expr, ImmutableMap.of("obj", obj, "bigValue", Long.valueOf(bigValue)));
    if (isTestHard()){
      assertEquals(bigValue, obj.bigDecimalField.toString());// Failure todo MVEL bug: https://github.com/mvel/mvel/issues/313
      // Expected :999999999999999999
      // Actual   :1000000000000000000
    }
  }

  /** POJO for testing of copying values between Fields and domain objects */
  @Setter @Getter
  public static class MyObj {
    BigDecimal bigDecimalField;
    BigInteger bigIntegerField;
    long longField;
  }
}