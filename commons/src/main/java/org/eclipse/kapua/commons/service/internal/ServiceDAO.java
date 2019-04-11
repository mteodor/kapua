/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.internal;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.kapua.KapuaEntityExistsException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.OrPredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoAttributes;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.Groupable;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embedded;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link ServiceDAO} utility methods.
 *
 * @since 1.0.0
 */
public class ServiceDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceDAO.class);

    private static final AccessInfoService ACCESS_INFO_SERVICE;
    private static final AccessInfoFactory ACCESS_INFO_FACTORY;

    private static final AccessPermissionService ACCESS_PERMISSION_SERVICE;
    private static final AccessRoleService ACCESS_ROLE_SERVICE;

    private static final RoleService ROLE_SERVICE;
    private static final RolePermissionService ROLE_PERMISSION_SERVICE;

    private static final String SQL_ERROR_CODE_CONSTRAINT_VIOLATION = "23505";

    private static final SystemSetting SYSTEM_SETTING = SystemSetting.getInstance();

    private static final String ESCAPE = SYSTEM_SETTING.getString(SystemSettingKey.DB_CHARACTER_ESCAPE, "\\");
    private static final String LIKE = SYSTEM_SETTING.getString(SystemSettingKey.DB_CHARACTER_WILDCARD_ANY, "%");
    private static final String ANY = SYSTEM_SETTING.getString(SystemSettingKey.DB_CHARACTER_WILDCARD_SINGLE, "_");

    private static final String ATTRIBUTE_SEPARATOR = ".";
    private static final String ATTRIBUTE_SEPARATOR_ESCAPED = "\\.";

    static {
        KapuaLocator locator = null;
        try {
            locator = KapuaLocator.getInstance();
        } catch (ExceptionInInitializerError kre) {
            LOG.warn("KapuaLocator not available! Access Group feature may be not supported!", kre);
        }

        if (locator != null) {
            ACCESS_INFO_SERVICE = KapuaLocator.getInstance().getService(AccessInfoService.class);
            ACCESS_INFO_FACTORY = KapuaLocator.getInstance().getFactory(AccessInfoFactory.class);

            ACCESS_PERMISSION_SERVICE = KapuaLocator.getInstance().getService(AccessPermissionService.class);
            ACCESS_ROLE_SERVICE = KapuaLocator.getInstance().getService(AccessRoleService.class);

            ROLE_SERVICE = KapuaLocator.getInstance().getService(RoleService.class);
            ROLE_PERMISSION_SERVICE = KapuaLocator.getInstance().getService(RolePermissionService.class);
        } else {
            ACCESS_INFO_SERVICE = null;
            ACCESS_INFO_FACTORY = null;

            ACCESS_PERMISSION_SERVICE = null;
            ACCESS_ROLE_SERVICE = null;

            ROLE_SERVICE = null;
            ROLE_PERMISSION_SERVICE = null;
        }
    }

    protected ServiceDAO() {
    }

    /**
     * Create {@link KapuaEntity} utility method.<br>
     * This method checks for the constraint violation and, in this case, it throws a specific exception ({@link KapuaEntityExistsException}).
     *
     * @param em     The {@link EntityManager} that holds the transaction.
     * @param entity The {@link KapuaEntity} to be created.
     * @return The persisted {@link KapuaEntity}.
     * @since 1.0.0
     */
    public static <E extends KapuaEntity> E create(EntityManager em, E entity) {
        try {
            em.persist(entity);
            em.flush();
            em.refresh(entity);
        } catch (EntityExistsException e) {
            throw new KapuaEntityExistsException(e, entity.getId());
        } catch (PersistenceException e) {
            if (isInsertConstraintViolation(e)) {
                KapuaEntity entityFound = em.find(entity.getClass(), entity.getId());
                if (entityFound == null) {
                    throw e;
                }
                throw new KapuaEntityExistsException(e, entity.getId());
            } else {
                throw e;
            }
        }

        return entity;
    }

    private static boolean isInsertConstraintViolation(PersistenceException e) {
        Throwable cause = e.getCause();
        while (cause != null && !(cause instanceof SQLException)) {
            cause = cause.getCause();
        }

        if (cause == null) {
            return false;
        }

        SQLException innerExc = (SQLException) cause;
        return SQL_ERROR_CODE_CONSTRAINT_VIOLATION.equals(innerExc.getSQLState());
    }

    /**
     * Find {@link KapuaEntity} utility method
     *
     * @param em       The {@link EntityManager} that holds the transaction.
     * @param clazz    The {@link KapuaEntity} class. This must be the implementing {@code class}.
     * @param scopeId  The {@link KapuaEntity} scopeId of the entity to be found.
     * @param entityId The {@link KapuaEntity} {@link KapuaId} of the entity to be found.
     * @since 1.0.0
     */
    public static <E extends KapuaEntity> E find(EntityManager em, Class<E> clazz, KapuaId scopeId, KapuaId entityId) {
        //
        // Checking existence
        E entityToFind = em.find(clazz, entityId);

        //
        // Return if not null and scopeIds matches
        if (entityToFind != null) {
            if (scopeId == null) {
                return entityToFind;
            } else if (entityToFind.getScopeId() == null) {
                return entityToFind;
            } else if (entityToFind.getScopeId().equals(scopeId)) {
                return entityToFind;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Update {@link KapuaUpdatableEntity} utility method.
     *
     * @param em     The {@link EntityManager} that holds the transaction.
     * @param clazz  The {@link KapuaUpdatableEntity} class. This must be the implementing {@code class}.
     * @param entity The {@link KapuaUpdatableEntity} to be updated
     * @return The updated {@link KapuaUpdatableEntity}.
     * @throws KapuaEntityNotFoundException If the {@link KapuaEntity} does not exists.
     * @since 1.0.0
     */
    public static <E extends KapuaUpdatableEntity> E update(EntityManager em, Class<E> clazz, E entity) throws KapuaEntityNotFoundException {
        //
        // Checking existence
        E entityToUpdate = em.find(clazz, entity.getId());

        //
        // Updating if not null
        if (entityToUpdate != null) {
            AbstractKapuaUpdatableEntity updatableEntity = (AbstractKapuaUpdatableEntity) entity;
            updatableEntity.setCreatedOn(entityToUpdate.getCreatedOn());
            updatableEntity.setCreatedBy(entityToUpdate.getCreatedBy());

            em.merge(entity);
            em.flush();
            em.refresh(entityToUpdate);
        } else {
            throw new KapuaEntityNotFoundException(clazz.getSimpleName(), entity.getId());
        }

        return entityToUpdate;
    }

    /**
     * Delete {@link KapuaEntity} utility method
     *
     * @param em       The {@link EntityManager} that holds the transaction.
     * @param clazz    The {@link KapuaEntity} class. This must be the implementing {@code class}.
     * @param scopeId  The {@link KapuaEntity} scopeId of the entity to be deleted.
     * @param entityId The {@link KapuaEntity} {@link KapuaId} of the entity to be deleted.
     * @throws KapuaEntityNotFoundException If the {@link KapuaEntity} does not exists.
     * @since 1.0.0
     */
    public static <E extends KapuaEntity> void delete(EntityManager em, Class<E> clazz, KapuaId scopeId, KapuaId entityId)
            throws KapuaEntityNotFoundException {
        //
        // Checking existence
        E entityToDelete = find(em, clazz, scopeId, entityId);

        //
        // Deleting if not null and scopeIds matches
        if (entityToDelete != null) {
            em.remove(entityToDelete);
            em.flush();
        } else {
            throw new KapuaEntityNotFoundException(clazz.getSimpleName(), entityId);
        }
    }

    /**
     * Find by fields {@link KapuaEntity} utility method
     *
     * @param em    The {@link EntityManager} that holds the transaction.
     * @param clazz The {@link KapuaEntity} class. This must be the implementing {@code class}.
     * @param name  The {@link KapuaEntity} name of the field from which to search.
     * @param value The value of the field from which to search.
     * @return The {@link KapuaEntity} found, or {@code null} if not found.
     * @since 1.0.0
     */
    public static <E extends KapuaEntity> E findByField(EntityManager em, Class<E> clazz, String name, String value) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> criteriaSelectQuery = cb.createQuery(clazz);

        //
        // FROM
        Root<E> entityRoot = criteriaSelectQuery.from(clazz);

        //
        // SELECT
        criteriaSelectQuery.select(entityRoot);

        // name
        ParameterExpression<String> pName = cb.parameter(String.class, name);
        criteriaSelectQuery.where(cb.equal(entityRoot.get(name), pName));

        //
        // QUERY!
        TypedQuery<E> query = em.createQuery(criteriaSelectQuery);
        query.setParameter(pName.getName(), value);

        List<E> result = query.getResultList();
        E user = null;
        if (result.size() == 1) {
            user = result.get(0);
        }

        return user;
    }

    /**
     * Query {@link KapuaEntity} utility method.
     *
     * @param em                The {@link EntityManager} that holds the transaction.
     * @param interfaceClass    {@link KapuaQuery} result entity interface class
     * @param implementingClass {@link KapuaQuery} result entity implementation class
     * @param resultContainer   The {@link KapuaListResult} in which load the result. It must be empty.
     * @param kapuaQuery        The {@link KapuaQuery} to perform.
     * @return The reference of the {@code resultContainer} parameter. Results are added to the given {@code resultContainer} parameter.
     * @throws KapuaException If filter predicates in the {@link KapuaQuery} are incorrect. See {@link #handleKapuaQueryPredicates(QueryPredicate, Map, CriteriaBuilder, Root, EntityType)}.
     * @since 1.0.0
     */
    public static <I extends KapuaEntity, E extends I, L extends KapuaListResult<I>> L query(EntityManager em,
                                                                                             Class<I> interfaceClass,
                                                                                             Class<E> implementingClass,
                                                                                             L resultContainer,
                                                                                             KapuaQuery<I> kapuaQuery)
            throws KapuaException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> criteriaSelectQuery = cb.createQuery(implementingClass);

        //
        // FROM
        Root<E> entityRoot = criteriaSelectQuery.from(implementingClass);
        EntityType<E> entityType = entityRoot.getModel();

        //
        // SELECT
        criteriaSelectQuery.select(entityRoot).distinct(true);

        // Fetch LAZY attributes if necessary
        if (kapuaQuery.getFetchAttributes() != null) {
            for (String fetchAttribute : kapuaQuery.getFetchAttributes()) {
                entityRoot.fetch(entityType.getSingularAttribute(fetchAttribute), JoinType.LEFT);
            }
        }

        //
        // WHERE
        QueryPredicate kapuaPredicates = kapuaQuery.getPredicate();
        if (kapuaQuery.getScopeId() != null) {

            AndPredicate scopedAndPredicate = kapuaQuery.andPredicate(
                    kapuaQuery.attributePredicate(KapuaEntityAttributes.SCOPE_ID, kapuaQuery.getScopeId())
            );

            // Add existing query predicates
            if (kapuaQuery.getPredicate() != null) {
                scopedAndPredicate.and(kapuaQuery.getPredicate());
            }

            kapuaPredicates = scopedAndPredicate;
        }

        // Manage kapua query predicates to build the where clause.
        Map<ParameterExpression, Object> binds = new HashMap<>();
        Expression<Boolean> expr = handleKapuaQueryPredicates(kapuaPredicates,
                binds,
                cb,
                entityRoot,
                entityRoot.getModel());

        if (expr != null) {
            criteriaSelectQuery.where(expr);
        }

        //
        // ORDER BY
        // Default to the KapuaEntity id if no ordering is specified.
        Order order;
        if (kapuaQuery.getSortCriteria() != null) {
            FieldSortCriteria sortCriteria = (FieldSortCriteria) kapuaQuery.getSortCriteria();

            if (SortOrder.ASCENDING.equals(sortCriteria.getSortOrder())) {
                order = cb.asc(extractAttribute(entityRoot, sortCriteria.getAttributeName()));
            } else {
                order = cb.desc(extractAttribute(entityRoot, sortCriteria.getAttributeName()));
            }

        } else {
            order = cb.asc(entityRoot.get(entityType.getSingularAttribute(KapuaEntityAttributes.ENTITY_ID)));
        }
        criteriaSelectQuery.orderBy(order);

        //
        // QUERY!
        TypedQuery<E> query = em.createQuery(criteriaSelectQuery);

        // Populate query parameters
        binds.forEach(query::setParameter); // Whoah! This is very magic!

        // Set offset
        if (kapuaQuery.getOffset() != null) {
            query.setFirstResult(kapuaQuery.getOffset());
        }

        // Set limit
        if (kapuaQuery.getLimit() != null) {
            query.setMaxResults(kapuaQuery.getLimit() + 1);
        }

        // Finally querying!
        List<E> result = query.getResultList();

        // Check limit exceeded
        if (kapuaQuery.getLimit() != null &&
                result.size() > kapuaQuery.getLimit()) {
            result.remove(kapuaQuery.getLimit().intValue());
            resultContainer.setLimitExceeded(true);
        }

        // Set results
        resultContainer.addItems(result);
        return resultContainer;
    }

    /**
     * Count {@link KapuaEntity} utility method.
     *
     * @param em                The {@link EntityManager} that holds the transaction.
     * @param interfaceClass    {@link KapuaQuery} result entity interface class
     * @param implementingClass {@link KapuaQuery} result entity implementation class
     * @param kapuaQuery        The {@link KapuaQuery} to perform.
     * @return The number of {@link KapuaEntity}es that matched the filter predicates.
     * @throws KapuaException If filter predicates in the {@link KapuaQuery} are incorrect. See {@link #handleKapuaQueryPredicates(QueryPredicate, Map, CriteriaBuilder, Root, EntityType)}.
     * @since 1.0.0
     */
    public static <I extends KapuaEntity, E extends I> long count(EntityManager em,
                                                                  Class<I> interfaceClass,
                                                                  Class<E> implementingClass,
                                                                  KapuaQuery<I> kapuaQuery)
            throws KapuaException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaSelectQuery = cb.createQuery(Long.class);

        //
        // FROM
        Root<E> entityRoot = criteriaSelectQuery.from(implementingClass);

        //
        // SELECT
        criteriaSelectQuery.select(cb.countDistinct(entityRoot));

        //
        // WHERE
        QueryPredicate kapuaPredicates = kapuaQuery.getPredicate();
        if (kapuaQuery.getScopeId() != null) {

            AndPredicate scopedAndPredicate = kapuaQuery.andPredicate();

            AttributePredicate<KapuaId> scopeId = kapuaQuery.attributePredicate(KapuaEntityAttributes.SCOPE_ID, kapuaQuery.getScopeId());
            scopedAndPredicate.and(scopeId);

            if (kapuaQuery.getPredicate() != null) {
                scopedAndPredicate.and(kapuaQuery.getPredicate());
            }

            kapuaPredicates = scopedAndPredicate;
        }

        Map<ParameterExpression, Object> binds = new HashMap<>();
        Expression<Boolean> expr = handleKapuaQueryPredicates(kapuaPredicates,
                binds,
                cb,
                entityRoot,
                entityRoot.getModel());

        if (expr != null) {
            criteriaSelectQuery.where(expr);
        }

        //
        // COUNT!
        TypedQuery<Long> query = em.createQuery(criteriaSelectQuery);

        // Populate query parameters
        binds.forEach(query::setParameter); // Whoah! This is very magic!

        return query.getSingleResult();
    }

    /**
     * Handles {@link QueryPredicate} contained of a {@link KapuaQuery}.
     * <p>
     * It manages different types of {@link QueryPredicate} like:
     * <ul>
     * <li>{@link AttributePredicate}</li>
     * <li>{@link AndPredicate}</li>
     * <li>{@link OrPredicate}</li>
     * </ol>
     * <p>
     * It can be invoked recursively (i.e. to handle {@link AttributePredicate}s of the {@link AndPredicate}.
     *
     * @param queryPredicate     The {@link QueryPredicate} to handle.
     * @param binds              The {@link Map}&lg;{@link String}, {@link Object}&gt; of the query values.
     * @param cb                 The JPA {@link CriteriaBuilder} of the {@link javax.persistence.Query}.
     * @param userPermissionRoot The JPA {@link Root} of the {@link javax.persistence.Query}.
     * @param entityType         The JPA {@link EntityType} of the {@link javax.persistence.Query}.
     * @return The handled {@link Predicate}
     * @throws KapuaException If any problem occurs.
     */
    private static <E> Predicate handleKapuaQueryPredicates(QueryPredicate queryPredicate,
                                                            Map<ParameterExpression, Object> binds,
                                                            CriteriaBuilder cb,
                                                            Root<E> userPermissionRoot,
                                                            EntityType<E> entityType)
            throws KapuaException {
        Predicate predicate = null;
        if (queryPredicate instanceof AttributePredicate) {
            AttributePredicate attributePredicate = (AttributePredicate) queryPredicate;
            predicate = handleAttributePredicate(attributePredicate, binds, cb, userPermissionRoot, entityType);
        } else if (queryPredicate instanceof AndPredicate) {
            AndPredicate andPredicate = (AndPredicate) queryPredicate;
            predicate = handleAndPredicate(andPredicate, binds, cb, userPermissionRoot, entityType);
        } else if (queryPredicate instanceof OrPredicate) {
            OrPredicate orPredicate = (OrPredicate) queryPredicate;
            predicate = handleOrPredicate(orPredicate, binds, cb, userPermissionRoot, entityType);
        }
        return predicate;
    }

    private static <E> Predicate handleAndPredicate(AndPredicate andPredicate,
                                                    Map<ParameterExpression, Object> binds,
                                                    CriteriaBuilder cb,
                                                    Root<E> entityRoot,
                                                    EntityType<E> entityType)
            throws KapuaException {

        Predicate[] jpaAndPredicates =
                handlePredicate(
                        andPredicate.getPredicates(),
                        binds,
                        cb,
                        entityRoot,
                        entityType);

        return cb.and(jpaAndPredicates);

    }

    private static <E> Predicate handleOrPredicate(OrPredicate orPredicate,
                                                   Map<ParameterExpression, Object> binds,
                                                   CriteriaBuilder cb,
                                                   Root<E> entityRoot,
                                                   EntityType<E> entityType)
            throws KapuaException {

        Predicate[] jpaOrPredicates =
                handlePredicate(
                        orPredicate.getPredicates(),
                        binds,
                        cb,
                        entityRoot,
                        entityType);

        return cb.or(jpaOrPredicates);
    }

    private static <E> Predicate[] handlePredicate(List<QueryPredicate> orPredicates, Map<ParameterExpression, Object> binds, CriteriaBuilder cb, Root<E> entityRoot, EntityType<E> entityType) throws KapuaException {
        Predicate[] jpaOrPredicates = new Predicate[orPredicates.size()];

        for (int i = 0; i < orPredicates.size(); i++) {
            Predicate expr = handleKapuaQueryPredicates(orPredicates.get(i), binds, cb, entityRoot, entityType);
            jpaOrPredicates[i] = expr;
        }
        return jpaOrPredicates;
    }

    private static <E> Predicate handleAttributePredicate(AttributePredicate attrPred,
                                                          Map<ParameterExpression, Object> binds,
                                                          CriteriaBuilder cb,
                                                          Root<E> entityRoot,
                                                          EntityType<E> entityType)
            throws KapuaException {
        Predicate expr;
        String attrName = attrPred.getAttributeName();

        // Parse attributes
        Object attrValue = attrPred.getAttributeValue();
        if (attrValue instanceof KapuaId && !(attrValue instanceof KapuaEid)) {
            attrValue = KapuaEid.parseKapuaId((KapuaId) attrValue);
        }

        // Fields to query properties of sub attributes of the root entity
        Attribute attribute;
        if (attrName.contains(ATTRIBUTE_SEPARATOR)) {
            attribute = entityType.getAttribute(attrName.split(ATTRIBUTE_SEPARATOR_ESCAPED)[0]);
        } else {
            attribute = entityType.getAttribute(attrName);
        }

        if (attrValue instanceof Object[]) {
            Object[] attrValues = (Object[]) attrValue;
            Expression<?> orPredicate = extractAttribute(entityRoot, attrName);

            Predicate[] orPredicates = new Predicate[attrValues.length];
            for (int i = 0; i < attrValues.length; i++) {
                Object value = attrValues[i];

                if (value instanceof KapuaId && !(value instanceof KapuaEid)) {
                    value = new KapuaEid((KapuaId) value);
                }

                orPredicates[i] = cb.equal(orPredicate, value);
            }

            expr = cb.and(cb.or(orPredicates));
        } else {
            String strAttrValue;
            switch (attrPred.getOperator()) {
                case LIKE:
                    strAttrValue = attrValue.toString().replace(LIKE, ESCAPE + LIKE).replace(ANY, ESCAPE + ANY);
                    ParameterExpression<String> pl = cb.parameter(String.class);
                    binds.put(pl, LIKE + strAttrValue + LIKE);
                    expr = cb.like(extractAttribute(entityRoot, attrName), pl);
                    break;

                case STARTS_WITH:
                    strAttrValue = attrValue.toString().replace(LIKE, ESCAPE + LIKE).replace(ANY, ESCAPE + ANY);
                    ParameterExpression<String> psw = cb.parameter(String.class);
                    binds.put(psw, strAttrValue + LIKE);
                    expr = cb.like(extractAttribute(entityRoot, attrName), psw);
                    break;

                case IS_NULL:
                    expr = cb.isNull(extractAttribute(entityRoot, attrName));
                    break;

                case NOT_NULL:
                    expr = cb.isNotNull(extractAttribute(entityRoot, attrName));
                    break;

                case NOT_EQUAL:
                    expr = cb.notEqual(extractAttribute(entityRoot, attrName), attrValue);
                    break;

                case GREATER_THAN:
                    if (attrValue instanceof Comparable && ArrayUtils.contains(attribute.getJavaType().getInterfaces(), Comparable.class)) {
                        Comparable comparableAttrValue = (Comparable) attrValue;
                        Expression<? extends Comparable> comparableExpression = extractAttribute(entityRoot, attrName);
                        expr = cb.greaterThan(comparableExpression, comparableAttrValue);
                    } else {
                        throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "Trying to compare a non-comparable value");
                    }
                    break;

                case GREATER_THAN_OR_EQUAL:
                    if (attrValue instanceof Comparable && ArrayUtils.contains(attribute.getJavaType().getInterfaces(), Comparable.class)) {
                        Expression<? extends Comparable> comparableExpression = extractAttribute(entityRoot, attrName);
                        Comparable comparableAttrValue = (Comparable) attrValue;
                        expr = cb.greaterThanOrEqualTo(comparableExpression, comparableAttrValue);
                    } else {
                        throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "Trying to compare a non-comparable value");
                    }
                    break;

                case LESS_THAN:
                    if (attrValue instanceof Comparable && ArrayUtils.contains(attribute.getJavaType().getInterfaces(), Comparable.class)) {
                        Expression<? extends Comparable> comparableExpression = extractAttribute(entityRoot, attrName);
                        Comparable comparableAttrValue = (Comparable) attrValue;
                        expr = cb.lessThan(comparableExpression, comparableAttrValue);
                    } else {
                        throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "Trying to compare a non-comparable value");
                    }
                    break;
                case LESS_THAN_OR_EQUAL:
                    if (attrValue instanceof Comparable && ArrayUtils.contains(attribute.getJavaType().getInterfaces(), Comparable.class)) {
                        Expression<? extends Comparable> comparableExpression = extractAttribute(entityRoot, attrName);
                        Comparable comparableAttrValue = (Comparable) attrValue;
                        expr = cb.lessThanOrEqualTo(comparableExpression, comparableAttrValue);
                    } else {
                        throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "Trying to compare a non-comparable value");
                    }
                    break;

                case EQUAL:
                default:
                    expr = cb.equal(extractAttribute(entityRoot, attrName), attrValue);
            }
        }
        return expr;
    }

    /**
     * Utility method that selects the correct {@link Root} attribute.<br>
     * This method handles {@link Embedded} attributes and nested {@link KapuaEntity}es up to one level of nesting<br>
     * <p>
     * Filter predicates takes advantage of the dot notation to access {@link Embedded} attributes and nested {@link KapuaEntity}es.
     *
     * @param entityRoot    The {@link Root} entity from which extract the attribute.
     * @param attributeName The full attribute name. It can contain at maximum one '.' separator.
     * @return The {@link Path} expression that matches the given {@code attributeName} parameter.
     * @since 1.0.0
     */
    private static <E, P> Path<P> extractAttribute(Root<E> entityRoot, String attributeName) {

        Path<P> expressionPath;
        if (attributeName.contains(ATTRIBUTE_SEPARATOR)) {
            expressionPath = entityRoot.get(attributeName.split(ATTRIBUTE_SEPARATOR_ESCAPED)[0]).get(attributeName.split(ATTRIBUTE_SEPARATOR_ESCAPED)[1]);
        } else {
            expressionPath = entityRoot.get(attributeName);
        }
        return expressionPath;
    }

    /**
     * Handles the {@link Groupable} property of the {@link KapuaEntity}.
     *
     * @param query              The {@link KapuaQuery} to manage.
     * @param domain             The {@link Domain} inside which the {@link KapuaQuery} param targets.
     * @param groupPredicateName The name of the {@link Group} id field.
     * @since 1.0.0
     */
    protected static void handleKapuaQueryGroupPredicate(KapuaQuery query, Domain domain, String groupPredicateName) throws KapuaException {

        if (ACCESS_INFO_FACTORY != null) {
            KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
            if (kapuaSession != null) {
                try {
                    KapuaId userId = kapuaSession.getUserId();

                    AccessInfoQuery accessInfoQuery = ACCESS_INFO_FACTORY.newQuery(kapuaSession.getScopeId());
                    accessInfoQuery.setPredicate(query.attributePredicate(AccessInfoAttributes.USER_ID, userId));

                    AccessInfoListResult accessInfos = KapuaSecurityUtils.doPrivileged(() -> ACCESS_INFO_SERVICE.query(accessInfoQuery));

                    List<Permission> groupPermissions = new ArrayList<>();
                    if (!accessInfos.isEmpty()) {

                        AccessInfo accessInfo = accessInfos.getFirstItem();
                        AccessPermissionListResult accessPermissions = KapuaSecurityUtils.doPrivileged(() -> ACCESS_PERMISSION_SERVICE.findByAccessInfoId(accessInfo.getScopeId(), accessInfo.getId()));

                        for (AccessPermission ap : accessPermissions.getItems()) {
                            if (checkGroupPermission(domain, groupPermissions, ap.getPermission())) {
                                break;
                            }
                        }

                        AccessRoleListResult accessRoles = KapuaSecurityUtils.doPrivileged(() -> ACCESS_ROLE_SERVICE.findByAccessInfoId(accessInfo.getScopeId(), accessInfo.getId()));

                        for (AccessRole ar : accessRoles.getItems()) {
                            KapuaId roleId = ar.getRoleId();

                            Role role = KapuaSecurityUtils.doPrivileged(() -> ROLE_SERVICE.find(ar.getScopeId(), roleId));

                            RolePermissionListResult rolePermissions = KapuaSecurityUtils.doPrivileged(() -> ROLE_PERMISSION_SERVICE.findByRoleId(role.getScopeId(), role.getId()));

                            for (RolePermission rp : rolePermissions.getItems()) {
                                if (checkGroupPermission(domain, groupPermissions, rp.getPermission())) {
                                    break;
                                }
                            }
                        }
                    }

                    AndPredicate andPredicate = query.andPredicate();
                    if (!groupPermissions.isEmpty()) {
                        int i = 0;
                        KapuaId[] groupsIds = new KapuaEid[groupPermissions.size()];
                        for (Permission p : groupPermissions) {
                            groupsIds[i++] = p.getGroupId();
                        }
                        andPredicate.and(query.attributePredicate(groupPredicateName, groupsIds));
                    }

                    if (query.getPredicate() != null) {
                        andPredicate.and(query.getPredicate());
                    }

                    query.setPredicate(andPredicate);
                } catch (Exception e) {
                    throw KapuaException.internalError(e, "Error while grouping!");
                }
            }
        } else {
            LOG.warn("'Access Group Permission' feature is disabled");
        }
    }

    private static boolean checkGroupPermission(Domain domain, List<Permission> groupPermissions, Permission p) {
        if ((p.getDomain() == null || domain.getName().equals(p.getDomain())) &&
                (p.getAction() == null || Actions.read.equals(p.getAction()))) {
            if (p.getGroupId() == null) {
                groupPermissions.clear();
                return true;
            } else {
                groupPermissions.add(p);
            }
        }
        return false;
    }
}
