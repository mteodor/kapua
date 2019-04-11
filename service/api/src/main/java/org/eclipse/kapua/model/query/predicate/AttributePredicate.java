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
package org.eclipse.kapua.model.query.predicate;

/**
 * {@link AttributePredicate} definition.
 *
 * @param <T> Attribute value type.
 * @since 1.0.0
 */
public interface AttributePredicate<T> extends QueryPredicate {

    /**
     * {@link AttributePredicate}s operators
     * <p>
     * Determines how the values of the result set are compared with the given {@link AttributePredicate#getAttributeValue()}
     *
     * @since 1.0.0
     */
    enum Operator {
        /**
         * {@link #EQUAL} {@link Operator}
         * <p>
         * Matches results with the same value.
         *
         * @since 1.0.0
         */
        EQUAL,

        /**
         * {@link #NOT_EQUAL} {@link Operator}
         * <p>
         * Matches results with not the same value.
         *
         * @since 1.0.0
         */
        NOT_EQUAL,

        /**
         * {@link #IS_NULL} {@link Operator}
         * <p>
         * Matches results with value {@code null}.
         *
         * @since 1.0.0
         */
        IS_NULL,
        /**
         * {@link #NOT_NULL} {@link Operator}
         * <p>
         * Matches results with value NOT {@code null}.
         *
         * @since 1.0.0
         */
        NOT_NULL,

        /**
         * {@link #STARTS_WITH} {@link Operator}
         * <p>
         * Matches results with value that starts with the given value.
         * To be used with {@link String} {@link org.eclipse.kapua.model.KapuaEntityAttributes}.
         *
         * @since 1.0.0
         */
        STARTS_WITH,
        /**
         * {@link #LIKE} {@link Operator}
         * <p>
         * Matches results with value that are like (in SQL fashion) the given value.
         * To be used with {@link String} {@link org.eclipse.kapua.model.KapuaEntityAttributes}.
         * <p>
         * If you want to match only the beginning of the {@link String} please consider using {@link #STARTS_WITH}.
         *
         * @since 1.0.0
         */
        LIKE,

        /**
         * {@link #GREATER_THAN} {@link Operator}
         * <p>
         * Matches result with value that is greater but not equal.
         * To be used with {@link Comparable} types.
         *
         * @since 1.0.0
         */
        GREATER_THAN,
        /**
         * {@link #GREATER_THAN_OR_EQUAL} {@link Operator}
         * <p>
         * Matches result with value that is greater or equal.
         * To be used with {@link Comparable} types.
         *
         * @since 1.0.0
         */
        GREATER_THAN_OR_EQUAL,
        /**
         * {@link #LESS_THAN} {@link Operator}
         * <p>
         * Matches result with value that is less but not equal.
         * To be used with {@link Comparable} types.
         *
         * @since 1.0.0
         */
        LESS_THAN,
        /**
         * {@link #LESS_THAN_OR_EQUAL} {@link Operator}
         * <p>
         * Matches result with value that is less or equal.
         * To be used with {@link Comparable} types.
         *
         * @since 1.0.0
         */
        LESS_THAN_OR_EQUAL
    }

    /**
     * Gets the name of the {@link org.eclipse.kapua.model.KapuaEntityAttributes} to compare.
     *
     * @return The name name of the {@link org.eclipse.kapua.model.KapuaEntityAttributes} to compare.
     * @since 1.0.0
     */
    String getAttributeName();

    /**
     * Gets the value to compare the results.
     *
     * @return The value to compare the results.
     * @since 1.0.0
     */
    T getAttributeValue();

    /**
     * Get the {@link Operator} used to compare results.
     *
     * @return The {@link Operator} used to compare results.
     * @since 1.0.0
     */
    Operator getOperator();
}
