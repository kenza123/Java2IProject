/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;

/**
 * @param <T>
 */
public interface Dao<T> {
    
    public boolean create(T obj);
    public T find (long id);
    public Collection<T> findAll();
    public boolean update (T obj);
    public boolean delete (T obj);
    public void deleteAll();
    public void close();
}
