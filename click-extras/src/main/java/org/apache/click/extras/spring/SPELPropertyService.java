package org.apache.click.extras.spring;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.apache.click.service.PropertyService;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SPELPropertyService implements PropertyService {
  protected final Cache<String,Expression> EXPRESSION_CACHE =
      Caffeine.newBuilder().maximumSize(10_000).expireAfterAccess(1, TimeUnit.HOURS).build();


  @Override public void onInit (ServletContext servletContext) throws IOException {}

  @Override public void onDestroy () {}


  @Override public Object getValue (Object source, String name) {
    // Expression expression = ExpressionParser expressionParser
    val expression = cacheOrParse(source, name.trim());

    val context = new StandardEvaluationContext(source);// object cannot be changed after the context object is created
    context.addPropertyAccessor(new ReflectivePropertyAccessor());
    context.addPropertyAccessor(new MapAccessor());

    return expression.getValue(context);
  }


  @Override public void setValue (Object target, String name, Object value){
    val expression = cacheOrParse(target, name.trim());

    val context = new StandardEvaluationContext(target);// object cannot be changed after the context object is created
    context.addPropertyAccessor(new ReflectivePropertyAccessor());
    context.addPropertyAccessor(new MapAccessor());

    expression.setValue(context, value);
  }


  protected Expression cacheOrParse (Object root, String name){
    return EXPRESSION_CACHE.asMap().computeIfAbsent(PropertyService.distinctClassName(root)+'#'+name,
        k->new SpelExpressionParser(new SpelParserConfiguration(true, true)).parseExpression(name));
  }
}