package dao;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.entity.TokenAction;
import models.entity.User;
import models.entity.TokenAction.Type;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

/**
 * The TokenActionDAO handle database access for the entity TokenAction.
 * 
 * @author Sebastian Sachtleben
 */
public class TokenActionDAO extends BaseDAO<Long, TokenAction> {
  
  private TokenActionDAO() {
    super(Long.class, TokenAction.class);
  }

  private static final TokenActionDAO instance = new TokenActionDAO();

  @Transactional
  public static TokenAction create(final Type type, final String token, final User targetUser) {
    final Date created = new Date();
    final TokenAction ua = new TokenAction();
    ua.setTargetUser(targetUser);
    ua.setToken(token);
    ua.setType(type);
    ua.setCreated(created);
    ua.setExpires(new Date(created.getTime() + TokenAction.VERIFICATION_TIME * 1000));
    JPA.em().persist(ua);
    return ua;
  }  
  
  @Transactional
  public static TokenAction findByToken(final String token, final Type type) {
    CriteriaBuilder builder = instance.getCriteriaBuilder();
    CriteriaQuery<TokenAction> q = instance.getCriteria();
    q.where(builder.and(builder.equal(instance.getRoot(q).get("token"), token), builder.equal(instance.getRoot(q).get("type"), type)));
    try {
      return JPA.em().createQuery(q).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }  
  
}
