package com.lendup.persistence;

import java.util.List;

import com.lendup.twiliocaller.models.Caller;

/**
 * implementation of {@link CallerDbPeristence}
 * 
 * @author harishs
 * 
 */
public class CallerDbPersistenceImpl implements CallerDbPeristence {

    @Override
    public void saveInDb(Caller caller) {
	caller.save();
    }

    /**
     * Returns a List of all the records stored in the table
     */
    @Override
    public List<Caller> getList() {
	return Caller.find.findList();
    }

    /**
     * allows querying a table by id (primary key)
     */
    @Override
    public Caller findById(long id) {
	return Caller.find.byId(id);
    }

}
