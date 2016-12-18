package org.books.persistence.repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class that defines the common operations that can perform each repository.
 *
 * @param <T> The given entity to perform the operations on.
 */
public abstract class AbstractRepository<T, PK> {

	@PersistenceContext(name = "bookstore")
	protected EntityManager em;

	private Class<T> entityClass;

	protected AbstractRepository(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/**
	 * Persist the entity instance object into the database.
	 *
	 * @param t the entity instance to persist.
	 */
	public void persist(T t) {

		this.em.persist(t);
	}

	/**
	 * Retrieve an entity instance object that was previously persisted to the database using the indicated id as primary key.
	 *
	 * @param id the primary key to retrieve the entity instance.
	 * @return the entity object found.
	 */
	public T find(PK id) {

		return this.em.find(entityClass, id);

	}

	public T find(PK id, LockModeType lockModeType) {
		return this.em.find(entityClass, id, lockModeType);
	}

	/**
	 * Save changes made to a persistent object.
	 *
	 * @param t the transient entity to update.
	 * @return the old state of the object before the update.
	 */
	public T update(T t) {

		return this.em.merge(t);
	}

	public void flush() {
		em.flush();
	}

	public void lock(T t, LockModeType lockModeType) {
		em.lock(t, lockModeType);
	}

	/**
	 * Remove an object from persistent storage in the database.
	 *
	 * @param item the persistent object to remove.
	 */
	public void delete(T item) {
		this.em.remove(item);
	}

	public List<T> findByNamedQuery(String queryName) {
		return findByNamedQuery(entityClass, queryName);
	}

	public List<T> findByNamedQuery(String queryName, Map<String, Object> parameters) {
		return findByNamedQuery(entityClass, queryName, parameters);
	}

	public <X> List<X> findByNamedQuery(Class<X> clazz, String queryName) {
		TypedQuery<X> q = em.createNamedQuery(queryName, clazz);

		return q.getResultList();
	}

	public <X> List<X> findByNamedQuery(Class<X> clazz, String queryName, Map<String, Object> parameters) {
		TypedQuery<X> q = em.createNamedQuery(queryName, clazz);

		for (String key : parameters.keySet()) {
			q.setParameter(key, parameters.get(key));
		}

		return q.getResultList();
	}
}
