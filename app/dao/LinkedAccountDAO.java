package dao;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.entity.LinkedAccount;
import models.entity.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

/**
 * The LinkedAccountDAO handle database access for the entity LinkedAccount.
 * 
 * @author Sebastian Sachtleben
 */
public class LinkedAccountDAO extends BaseDAO<Long, LinkedAccount> {
  
  private LinkedAccountDAO() {
    super(Long.class, LinkedAccount.class);
  }

  private static final LinkedAccountDAO instance = new LinkedAccountDAO();

  public static LinkedAccount findByProviderKey(final User user, String key) {
    CriteriaBuilder builder = instance.getCriteriaBuilder();
    CriteriaQuery<LinkedAccount> q = instance.getCriteria();
    q.where(builder.and(builder.equal(instance.getRoot(q).get("user"), user), builder.equal(instance.getRoot(q).get("providerKey"), key)));
    try {
      return JPA.em().createQuery(q).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }  
  
}
