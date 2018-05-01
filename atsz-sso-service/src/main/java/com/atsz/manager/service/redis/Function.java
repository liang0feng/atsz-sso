package com.atsz.manager.service.redis;

public interface Function<E,T> {
	public T callback(E e);
}
