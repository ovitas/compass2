package no.ovitas.compass2.kb.store.dao.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import no.ovitas.compass2.config.settings.SubPlugin;
import no.ovitas.compass2.exception.CompassErrorID;
import no.ovitas.compass2.exception.CompassException;
import no.ovitas.compass2.kb.store.dao.GenericDao;
import no.ovitas.compass2.kb.store.model.BaseObject;
import no.ovitas.compass2.kb.store.model.ScopeEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.ArrayList;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * 
 * <p>
 * To register this class in your Spring context file, use the following XML.
 * 
 * <pre>
 *      &lt;bean id="fooDao" class="no.ovitas.compass2.kb.store.dao.hibernate.GenericDaoJpaHibernate"&gt;
 *          &lt;constructor-arg value="no.ovitas.compass2.kb.store.model.Foo"/&gt;
 *          &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 * 
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * @param <T>
 *            a type variable
 * @param <PK>
 *            the primary key for that type
 */
public class GenericDaoJpa<T, PK extends Serializable> implements
		GenericDao<T, PK> {
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());

	public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";

	/**
	 * Entity manager, injected by Spring using @PersistenceContext annotation
	 * on setEntityManager()
	 */
	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
	protected EntityManager entityManager;
	private Class<T> persistentClass;

	protected TransactionManager transactionManager;

	/**
	 * Constructor that takes in a class to see which type of entity to persist.
	 * Use this constructor when subclassing or using dependency injection.
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 */
	public GenericDaoJpa(final Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	/**
	 * Constructor that takes in a class to see which type of entity to persist.
	 * Use this constructor when subclassing or using dependency injection.
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 * @param entityManager
	 *            the configured EntityManager for JPA implementation.
	 */
	public GenericDaoJpa(final Class<T> persistentClass,
			EntityManager entityManager) {
		this.persistentClass = persistentClass;
		this.entityManager = entityManager;
	}

	/**
	 * This is a setter method for transactionManager.
	 * 
	 * @param transactionManager
	 *            the transactionManager to set
	 */
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		try {
			return this.entityManager.createQuery(
					"select obj from " + this.persistentClass.getName()
							+ " obj").getResultList();
		} catch (Exception e) {
			throw new CompassException(CompassErrorID.KB_SQL_QUERY_ERROR,
					"Error occured when search in database!", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAllDistinct() {
		Collection result = new LinkedHashSet(getAll());
		return new ArrayList(result);
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(PK id) {
		T entity;
		try {
			entity = this.entityManager.find(this.persistentClass, id);

			if (entity == null) {
				String msg = "Uh oh, '" + this.persistentClass
						+ "' object with id '" + id + "' not found...";
				log.warn(msg);
				throw new EntityNotFoundException(msg);
			}
		} catch (Exception e) {
			throw new CompassException(CompassErrorID.KB_SQL_QUERY_ERROR,
					"Error occured when search in database!", e);
		}

		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists(PK id) {
		T entity = this.entityManager.find(this.persistentClass, id);
		return entity != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public T save(T object) {
		try {
			return this.entityManager.merge(object);
		} catch (Exception e) {
			throw new CompassException(CompassErrorID.KB_SAVE_ERROR,
					"Error occured when save entity!", e);
		}
	}

	public T persist(T object) {
		try {
			this.entityManager.persist(object);
		} catch (Exception e) {
			throw new CompassException(CompassErrorID.KB_SAVE_ERROR,
					"Error occured when save entity!", e);
		}
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(PK id) {
		this.entityManager.remove(this.get(id));
	}

	public void persistAll(Collection<T> collection) {
		for (T t : collection) {
			entityManager.persist(t);
		}

	}

}
