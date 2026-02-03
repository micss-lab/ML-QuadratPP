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
 * A representation of the literals of the enumeration '<em><b>Rate</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getRate()
 * @model
 * @generated
 */
public enum Rate implements Enumerator
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
   * The '<em><b>CONSTANT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CONSTANT_VALUE
   * @generated
   * @ordered
   */
  CONSTANT(1, "CONSTANT", "\'constant\'"),

  /**
   * The '<em><b>TIME BASED</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TIME_BASED_VALUE
   * @generated
   * @ordered
   */
  TIME_BASED(2, "TIME_BASED", "\'time_based\'"),

  /**
   * The '<em><b>STEP</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STEP_VALUE
   * @generated
   * @ordered
   */
  STEP(3, "STEP", "\'step\'"),

  /**
   * The '<em><b>EXPONENTIAL</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #EXPONENTIAL_VALUE
   * @generated
   * @ordered
   */
  EXPONENTIAL(4, "EXPONENTIAL", "\'exponential\'");

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
   * The '<em><b>CONSTANT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CONSTANT
   * @model literal="\'constant\'"
   * @generated
   * @ordered
   */
  public static final int CONSTANT_VALUE = 1;

  /**
   * The '<em><b>TIME BASED</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TIME_BASED
   * @model literal="\'time_based\'"
   * @generated
   * @ordered
   */
  public static final int TIME_BASED_VALUE = 2;

  /**
   * The '<em><b>STEP</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STEP
   * @model literal="\'step\'"
   * @generated
   * @ordered
   */
  public static final int STEP_VALUE = 3;

  /**
   * The '<em><b>EXPONENTIAL</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #EXPONENTIAL
   * @model literal="\'exponential\'"
   * @generated
   * @ordered
   */
  public static final int EXPONENTIAL_VALUE = 4;

  /**
   * An array of all the '<em><b>Rate</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final Rate[] VALUES_ARRAY =
    new Rate[]
    {
      NOT_SET,
      CONSTANT,
      TIME_BASED,
      STEP,
      EXPONENTIAL,
    };

  /**
   * A public read-only list of all the '<em><b>Rate</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<Rate> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Rate</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Rate get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Rate result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Rate</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Rate getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Rate result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Rate</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Rate get(int value)
  {
    switch (value)
    {
      case NOT_SET_VALUE: return NOT_SET;
      case CONSTANT_VALUE: return CONSTANT;
      case TIME_BASED_VALUE: return TIME_BASED;
      case STEP_VALUE: return STEP;
      case EXPONENTIAL_VALUE: return EXPONENTIAL;
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
  private Rate(int value, String name, String literal)
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
  
} //Rate
