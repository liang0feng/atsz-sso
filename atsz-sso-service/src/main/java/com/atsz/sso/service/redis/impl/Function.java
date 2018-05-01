package com.atsz.sso.service.redis.impl;

public interface Function<E,T> {

    public T callback(E e);
}
