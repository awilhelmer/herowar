package dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import models.entity.LinkedAccount;
import models.entity.User;
import play.db.jpa.JPA;

import com.ssachtleben.play.plugin.auth.models.Identity;

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

	public static LinkedAccount find(final String key, final String userId) {
		CriteriaBuilder builder = instance.getCriteriaBuilder();
		CriteriaQuery<LinkedAccount> q = instance.getCriteria();
		Root<LinkedAccount> root = instance.getRoot(q);
		q.where(builder.and(builder.equal(root.get("providerKey"), key), builder.equal(root.get("providerUserId"), userId)));
		try {
			return JPA.em().createQuery(q).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static LinkedAccount findByUsername(String key, String username) {
		List<LinkedAccount> accounts = JPA
				.em()
				.createQuery(
						String.format("FROM %s la WHERE la.providerKey = :key AND la.user.username = :username", LinkedAccount.class.getSimpleName()))
				.setParameter("key", key).setParameter("username", username).getResultList();
		if (accounts != null && accounts.size() > 0) {
			return accounts.get(0);
		}
		return null;
	}

	public static LinkedAccount create(final Identity identity, final User user) {
		LinkedAccount acc = new LinkedAccount();
		acc.setProviderKey(identity.provider());
		acc.setProviderUserId(identity.id());
		acc.setUser(user);
		JPA.em().persist(acc);
		return acc;
	}
}
