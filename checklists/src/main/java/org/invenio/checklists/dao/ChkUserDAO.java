package org.invenio.checklists.dao;

import java.util.List;
import org.invenio.checklists.orm.ChkUser;

/**
 *
 * @author avillalobos
 */
public interface ChkUserDAO {
    
    public ChkUser save(ChkUser user);
    public ChkUser get(String id);
    public ChkUser getByEmail(String email);
    public ChkUser getByToken(String token);
    public List<ChkUser> get();
    public ChkUser update(ChkUser user);
    public void delete(ChkUser user);

}
