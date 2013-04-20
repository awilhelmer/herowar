package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.entity.TokenAction;
import models.entity.TokenAction.Type;
import models.entity.User;
import play.db.jpa.JPA;

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

  public static TokenAction findByToken(final String token, final Type type) {
    CriteriaBuilder builder = instance.getCriteriaBuilder();
    CriteriaQuery<TokenAction> q = instance.getCriteria();
    Root<TokenAction> root = instance.getRoot(q);
    q.where(builder.and(builder.equal(root.get("token"), token), builder.equal(root.get("type"), type)));
    try {
      return JPA.em().createQuery(q).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static List<TokenAction> findByTokenAndUserId(final Long userId, final Type type) {
    CriteriaBuilder builder = instance.getCriteriaBuilder();
    CriteriaQuery<TokenAction> q = instance.getCriteria();
    Root<TokenAction> root = instance.getRoot(q);
    q.where(builder.and(builder.equal(root.get("targetUser.id"), userId), builder.equal(root.get("type"), type)));
    return JPA.em().createQuery(q).getResultList();
  }

  public static void deleteByUser(final User u, final Type type) {
    List<TokenAction> actionList = findByTokenAndUserId(u.getId(), type);
    for (TokenAction action : actionList) {
      JPA.em().remove(action);
    }
  }

}
