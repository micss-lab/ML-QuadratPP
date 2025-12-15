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
 * A representation of the literals of the enumeration '<em><b>Metrics</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getMetrics()
 * @model
 * @generated
 */
public enum Metrics implements Enumerator
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
   * The '<em><b>MSE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MSE_VALUE
   * @generated
   * @ordered
   */
  MSE(1, "MSE", "\'MSE\'"),

  /**
   * The '<em><b>MAE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MAE_VALUE
   * @generated
   * @ordered
   */
  MAE(2, "MAE", "\'MAE\'"),

  /**
   * The '<em><b>ACCURACY</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ACCURACY_VALUE
   * @generated
   * @ordered
   */
  ACCURACY(3, "ACCURACY", "\'ACCURACY\'"),

  /**
   * The '<em><b>PRECISION</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PRECISION_VALUE
   * @generated
   * @ordered
   */
  PRECISION(4, "PRECISION", "\'PRECISION\'"),

  /**
   * The '<em><b>RECALL</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RECALL_VALUE
   * @generated
   * @ordered
   */
  RECALL(5, "RECALL", "\'RECALL\'"),

  /**
   * The '<em><b>F1 SCORE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #F1_SCORE_VALUE
   * @generated
   * @ordered
   */
  F1_SCORE(6, "F1_SCORE", "\'F1_SCORE\'");

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
   * The '<em><b>MSE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MSE
   * @model literal="\'MSE\'"
   * @generated
   * @ordered
   */
  public static final int MSE_VALUE = 1;

  /**
   * The '<em><b>MAE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MAE
   * @model literal="\'MAE\'"
   * @generated
   * @ordered
   */
  public static final int MAE_VALUE = 2;

  /**
   * The '<em><b>ACCURACY</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ACCURACY
   * @model literal="\'ACCURACY\'"
   * @generated
   * @ordered
   */
  public static final int ACCURACY_VALUE = 3;

  /**
   * The '<em><b>PRECISION</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PRECISION
   * @model literal="\'PRECISION\'"
   * @generated
   * @ordered
   */
  public static final int PRECISION_VALUE = 4;

  /**
   * The '<em><b>RECALL</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RECALL
   * @model literal="\'RECALL\'"
   * @generated
   * @ordered
   */
  public static final int RECALL_VALUE = 5;

  /**
   * The '<em><b>F1 SCORE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #F1_SCORE
   * @model literal="\'F1_SCORE\'"
   * @generated
   * @ordered
   */
  public static final int F1_SCORE_VALUE = 6;

  /**
   * An array of all the '<em><b>Metrics</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final Metrics[] VALUES_ARRAY =
    new Metrics[]
    {
      NOT_SET,
      MSE,
      MAE,
      ACCURACY,
      PRECISION,
      RECALL,
      F1_SCORE,
    };

  /**
   * A public read-only list of all the '<em><b>Metrics</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<Metrics> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Metrics</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Metrics get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Metrics result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Metrics</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Metrics getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Metrics result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Metrics</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Metrics get(int value)
  {
    switch (value)
    {
      case NOT_SET_VALUE: return NOT_SET;
      case MSE_VALUE: return MSE;
      case MAE_VALUE: return MAE;
      case ACCURACY_VALUE: return ACCURACY;
      case PRECISION_VALUE: return PRECISION;
      case RECALL_VALUE: return RECALL;
      case F1_SCORE_VALUE: return F1_SCORE;
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
  private Metrics(int value, String name, String literal)
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
  
} //Metrics
