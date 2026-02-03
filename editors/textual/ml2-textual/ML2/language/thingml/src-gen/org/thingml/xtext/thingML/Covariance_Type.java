/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 */
package org.thingml.xtext.thingML;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Covariance Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getCovariance_Type()
 * @model
 * @generated
 */
public enum Covariance_Type implements Enumerator
{
  /**
   * The '<em><b>NOT SET</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NOT_SET_VALUE
   * @generated
   * @ordered
   */
  NOT_SET(0, "NOT_SET", "NOT_SET"),

  /**
   * The '<em><b>FULL</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #FULL_VALUE
   * @generated
   * @ordered
   */
  FULL(1, "FULL", "\'full\'"),

  /**
   * The '<em><b>TIED</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TIED_VALUE
   * @generated
   * @ordered
   */
  TIED(2, "TIED", "\'tied\'"),

  /**
   * The '<em><b>DIAG</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #DIAG_VALUE
   * @generated
   * @ordered
   */
  DIAG(3, "DIAG", "\'diag\'"),

  /**
   * The '<em><b>SPHERICAL</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SPHERICAL_VALUE
   * @generated
   * @ordered
   */
  SPHERICAL(4, "SPHERICAL", "\'spherical\'");

  /**
   * The '<em><b>NOT SET</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NOT_SET
   * @model
   * @generated
   * @ordered
   */
  public static final int NOT_SET_VALUE = 0;

  /**
   * The '<em><b>FULL</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #FULL
   * @model literal="\'full\'"
   * @generated
   * @ordered
   */
  public static final int FULL_VALUE = 1;

  /**
   * The '<em><b>TIED</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TIED
   * @model literal="\'tied\'"
   * @generated
   * @ordered
   */
  public static final int TIED_VALUE = 2;

  /**
   * The '<em><b>DIAG</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #DIAG
   * @model literal="\'diag\'"
   * @generated
   * @ordered
   */
  public static final int DIAG_VALUE = 3;

  /**
   * The '<em><b>SPHERICAL</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SPHERICAL
   * @model literal="\'spherical\'"
   * @generated
   * @ordered
   */
  public static final int SPHERICAL_VALUE = 4;

  /**
   * An array of all the '<em><b>Covariance Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final Covariance_Type[] VALUES_ARRAY =
    new Covariance_Type[]
    {
      NOT_SET,
      FULL,
      TIED,
      DIAG,
      SPHERICAL,
    };

  /**
   * A public read-only list of all the '<em><b>Covariance Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<Covariance_Type> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Covariance Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Covariance_Type get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Covariance_Type result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Covariance Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Covariance_Type getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Covariance_Type result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Covariance Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Covariance_Type get(int value)
  {
    switch (value)
    {
      case NOT_SET_VALUE: return NOT_SET;
      case FULL_VALUE: return FULL;
      case TIED_VALUE: return TIED;
      case DIAG_VALUE: return DIAG;
      case SPHERICAL_VALUE: return SPHERICAL;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private Covariance_Type(int value, String name, String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    return literal;
  }
  
} //Covariance_Type
