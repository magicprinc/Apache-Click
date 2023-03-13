package org.apache.click.extras.spring;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.apache.click.service.PropertyServiceBase;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.concurrent.TimeUnit;

public class SPELPropertyService extends PropertyServiceBase {
  protected final Cache<String,Expression> EXPRESSION_CACHE = Caffeine.newBuilder()
      .maximumSize(10_000)
      .expireAfterAccess(1, TimeUnit.HOURS)
      .build();


  @Override public void onDestroy (){
    super.onDestroy();
    EXPRESSION_CACHE.invalidateAll();

  }


  @Override public void setValue (Object target, String name, Object value){
    val expression = cacheOrParse(target, name.trim());

    val context = new StandardEvaluationContext(target);// this object cannot be changed after the context object is created
    context.addPropertyAccessor(new ReflectivePropertyAccessor());
    context.addPropertyAccessor(new MapAccessor());

    expression.setValue(context, value);
  }


  protected Expression cacheOrParse (Object root, String name){
    return EXPRESSION_CACHE.asMap()
        .computeIfAbsent(
            distinctClassName(root)+'#'+name,
            k->new SpelExpressionParser(new SpelParserConfiguration(true, true)).parseExpression(name));
  }
}