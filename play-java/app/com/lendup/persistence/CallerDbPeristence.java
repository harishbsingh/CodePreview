package com.lendup.persistence;

import java.util.List;

/**
 * Interface that defines persistence operations
 */
import com.lendup.twiliocaller.models.Caller;

public interface CallerDbPeristence {

    public void saveInDb(Caller caller);

    public List<Caller> getList();

    public Caller findById(long id);
}
