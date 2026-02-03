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
 * A representation of the literals of the enumeration '<em><b>Ensemble Methods</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getEnsembleMethods()
 * @model
 * @generated
 */
public enum EnsembleMethods implements Enumerator
{
  /**
   * The '<em><b>OFF</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OFF_VALUE
   * @generated
   * @ordered
   */
  OFF(0, "OFF", "OFF"),

  /**
   * The '<em><b>BAGGING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BAGGING_VALUE
   * @generated
   * @ordered
   */
  BAGGING(1, "BAGGING", "BAGGING"),

  /**
   * The '<em><b>BOOSTING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BOOSTING_VALUE
   * @generated
   * @ordered
   */
  BOOSTING(2, "BOOSTING", "BOOSTING"),

  /**
   * The '<em><b>STACKING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STACKING_VALUE
   * @generated
   * @ordered
   */
  STACKING(3, "STACKING", "STACKING");

  /**
   * The '<em><b>OFF</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OFF
   * @model
   * @generated
   * @ordered
   */
  public static final int OFF_VALUE = 0;

  /**
   * The '<em><b>BAGGING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BAGGING
   * @model
   * @generated
   * @ordered
   */
  public static final int BAGGING_VALUE = 1;

  /**
   * The '<em><b>BOOSTING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BOOSTING
   * @model
   * @generated
   * @ordered
   */
  public static final int BOOSTING_VALUE = 2;

  /**
   * The '<em><b>STACKING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STACKING
   * @model
   * @generated
   * @ordered
   */
  public static final int STACKING_VALUE = 3;

  /**
   * An array of all the '<em><b>Ensemble Methods</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final EnsembleMethods[] VALUES_ARRAY =
    new EnsembleMethods[]
    {
      OFF,
      BAGGING,
      BOOSTING,
      STACKING,
    };

  /**
   * A public read-only list of all the '<em><b>Ensemble Methods</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<EnsembleMethods> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Ensemble Methods</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static EnsembleMethods get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      EnsembleMethods result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Ensemble Methods</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static EnsembleMethods getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      EnsembleMethods result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Ensemble Methods</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static EnsembleMethods get(int value)
  {
    switch (value)
    {
      case OFF_VALUE: return OFF;
      case BAGGING_VALUE: return BAGGING;
      case BOOSTING_VALUE: return BOOSTING;
      case STACKING_VALUE: return STACKING;
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
  private EnsembleMethods(int value, String name, String literal)
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
  
} //EnsembleMethods
