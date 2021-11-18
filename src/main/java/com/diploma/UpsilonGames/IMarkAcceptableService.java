package com.diploma.UpsilonGames;

public interface IMarkAcceptableService<T>{
    public T findById(long id);
    public boolean existsById(long id);
}
