package me.eeshe.gtmobs.models;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class ExpiringSet<T> implements Set<T> {

  private final Cache<T, Object> cache;
  private static final Object PRESENT = new Object();

  public ExpiringSet(long duration, TimeUnit unit) {
    this.cache = CacheBuilder.newBuilder()
        .expireAfterWrite(duration, unit)
        .build();
  }

  public static <T> ExpiringSet<T> create(Duration duration) {
    return new ExpiringSet<>(duration.toMillis(), TimeUnit.MILLISECONDS);
  }

  @Override
  public boolean add(T element) {
    boolean present = cache.getIfPresent(element) == PRESENT;
    cache.put(element, PRESENT);
    return present;
  }

  @Override
  public boolean addAll(Collection<? extends T> elements) {
    boolean hasAll = cache.asMap().keySet().containsAll(elements);
    elements.forEach(element -> cache.put(element, PRESENT));
    return hasAll;
  }

  @Override
  public void clear() {
    cache.invalidateAll();
  }

  @Override
  public Iterator<T> iterator() {
    return cache.asMap().keySet().iterator();
  }

  @Override
  public boolean remove(Object element) {
    boolean present = cache.getIfPresent(element) == PRESENT;
    cache.invalidate(element);
    return present;
  }

  @Override
  public boolean removeAll(Collection<?> elements) {
    boolean hasAll = cache.asMap().keySet().containsAll(elements);
    cache.invalidateAll(elements);
    return hasAll;
  }

  @Override
  public boolean retainAll(Collection<?> elements) {
    int previousSize = size();
    Set<T> keysToRemove = new HashSet<>(cache.asMap().keySet());
    keysToRemove.removeAll(elements);
    cache.invalidateAll(keysToRemove);
    return size() != previousSize;
  }

  @Override
  public boolean contains(Object element) {
    return cache.getIfPresent(element) == PRESENT;
  }

  @Override
  public boolean containsAll(Collection<?> elements) {
    return cache.asMap().keySet().containsAll(elements);
  }

  @Override
  public int size() {
    return (int) cache.size();
  }

  @Override
  public boolean isEmpty() {
    return cache.size() == 0;
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    cache.asMap().keySet().forEach(action);
  }

  public Stream<T> parallelStream() {
    return cache.asMap().keySet().parallelStream();
  }

  @Override
  public boolean removeIf(Predicate<? super T> filter) {
    return cache.asMap().keySet().removeIf(filter);
  }

  public Stream<T> stream() {
    return cache.asMap().keySet().stream();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null || getClass() != other.getClass())
      return false;
    ExpiringSet<?> that = (ExpiringSet<?>) other;
    return Objects.equals(cache.asMap(), that.cache.asMap());
  }

  @Override
  public int hashCode() {
    return cache.asMap().hashCode();
  }

  @Override
  public Object[] toArray() {
    return cache.asMap().keySet().toArray();
  }

  @Override
  public <E> E[] toArray(E[] a) {
    return cache.asMap().keySet().toArray(a);
  }
}
