/**
 * 
 */
package no.ovitas.compass2.kb.store.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ovitas.compass2.kb.store.dao.KnowledgeBaseDao;
import no.ovitas.compass2.kb.store.dao.ScopeDao;
import no.ovitas.compass2.kb.store.model.KnowledgeBaseEntity;
import no.ovitas.compass2.kb.store.model.ScopeEntity;
import no.ovitas.compass2.kb.store.service.ScopeManager;
import no.ovitas.compass2.model.knowledgebase.KnowledgeBaseDescriptor;
import no.ovitas.compass2.model.knowledgebase.Scope;

/**
 * @class ScopeManegerImpl
 * @project compass2-kbstore-jpa-new
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.08.30.
 * 
 */
public class ScopeManagerImpl extends GenericManagerImpl<ScopeEntity, Long>
		implements ScopeManager {

	private ScopeDao scopeDao;
	
	private KnowledgeBaseDao knowledgeBaseDao;

	public ScopeManagerImpl(ScopeDao scopeDao) {
		super(scopeDao);
		this.setScopeDao(scopeDao);
	}

	/**
	 * This is a setter method for scopeDao.
	 * 
	 * @param scopeDao
	 *            the scopeDao to set
	 */
	public void setScopeDao(ScopeDao scopeDao) {
		this.scopeDao = scopeDao;
	}

	/**
	 * This is a getter method for scopeDao.
	 * 
	 * @return the scopeDao
	 */
	public ScopeDao getScopeDao() {
		return scopeDao;
	}

	/**
	 * This is a setter method for knowledgeBaseDao.
	 * @param knowledgeBaseDao the knowledgeBaseDao to set
	 */
	public void setKnowledgeBaseDao(KnowledgeBaseDao knowledgeBaseDao) {
		this.knowledgeBaseDao = knowledgeBaseDao;
	}

	public Map<Scope, Long> addScopes(Set<Scope> scopes,
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		List<ScopeEntity> entities = scopeDao
				.getAllFromKnowledgeBase(knowledgeBaseDescriptor.getId());
		Map<Scope, Long> result = new HashMap<Scope, Long>();

		KnowledgeBaseEntity kbEntity = knowledgeBaseDao.get(knowledgeBaseDescriptor.getId());
		ScopeEntity newScopeEntity;
		boolean exist;
		for (Scope scope : scopes) {
			exist = false;

			for (ScopeEntity scopeEntity : entities) {
				if (scopeEntity.getDisplayName()
						.equals(scope.getDisplayName())) {
					exist = true;
					result.put(scope, scopeEntity.getId());
					break;
				}
			}

			if (!exist) {
				newScopeEntity = createScopeEntity(scope, kbEntity);
				scopeDao.persist(newScopeEntity);
				result.put(scope, newScopeEntity.getId());
			}
		}

		return result;
	}

	private ScopeEntity createScopeEntity(Scope scope,
			KnowledgeBaseEntity kbEntity) {

		ScopeEntity scopeEntity = new ScopeEntity();

		scopeEntity.setDisplayName(scope.getDisplayName());
		scopeEntity.setExternalId(scope.getExternalId());
		scopeEntity.setKnowledgeBase(kbEntity);

		return scopeEntity;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<Scope> getScopesInKnowledgeBase(
			KnowledgeBaseDescriptor knowledgeBaseDescriptor) {
		
		return (Collection)scopeDao.getAllFromKnowledgeBase(knowledgeBaseDescriptor.getId());
	}

}
