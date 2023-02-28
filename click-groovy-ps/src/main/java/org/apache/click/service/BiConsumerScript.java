package org.apache.click.service;

import groovy.lang.Script;

import java.util.function.BiConsumer;

@SuppressWarnings("CommentedOutCode")
public abstract class BiConsumerScript extends Script implements BiConsumer<Object,Object> {
/*
  @Override public void accept (Object t, Object v) {
    t.path = v;
  }
*/
}