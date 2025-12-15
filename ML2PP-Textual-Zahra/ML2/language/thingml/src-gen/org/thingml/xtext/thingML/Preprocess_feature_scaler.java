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
 * A representation of the literals of the enumeration '<em><b>Preprocess feature scaler</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getPreprocess_feature_scaler()
 * @model
 * @generated
 */
public enum Preprocess_feature_scaler implements Enumerator
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
   * The '<em><b>OFF</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OFF_VALUE
   * @generated
   * @ordered
   */
  OFF(1, "OFF", "OFF"),

  /**
   * The '<em><b>STANDARD SCALER</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STANDARD_SCALER_VALUE
   * @generated
   * @ordered
   */
  STANDARD_SCALER(2, "STANDARD_SCALER", "StandardScaler"),

  /**
   * The '<em><b>MIN MAX SCALER</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MIN_MAX_SCALER_VALUE
   * @generated
   * @ordered
   */
  MIN_MAX_SCALER(3, "MIN_MAX_SCALER", "MinMaxScaler"),

  /**
   * The '<em><b>ROBUST SCALER</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ROBUST_SCALER_VALUE
   * @generated
   * @ordered
   */
  ROBUST_SCALER(4, "ROBUST_SCALER", "RobustScaler");

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
   * The '<em><b>OFF</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OFF
   * @model
   * @generated
   * @ordered
   */
  public static final int OFF_VALUE = 1;

  /**
   * The '<em><b>STANDARD SCALER</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STANDARD_SCALER
   * @model literal="StandardScaler"
   * @generated
   * @ordered
   */
  public static final int STANDARD_SCALER_VALUE = 2;

  /**
   * The '<em><b>MIN MAX SCALER</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MIN_MAX_SCALER
   * @model literal="MinMaxScaler"
   * @generated
   * @ordered
   */
  public static final int MIN_MAX_SCALER_VALUE = 3;

  /**
   * The '<em><b>ROBUST SCALER</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ROBUST_SCALER
   * @model literal="RobustScaler"
   * @generated
   * @ordered
   */
  public static final int ROBUST_SCALER_VALUE = 4;

  /**
   * An array of all the '<em><b>Preprocess feature scaler</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final Preprocess_feature_scaler[] VALUES_ARRAY =
    new Preprocess_feature_scaler[]
    {
      NOT_SET,
      OFF,
      STANDARD_SCALER,
      MIN_MAX_SCALER,
      ROBUST_SCALER,
    };

  /**
   * A public read-only list of all the '<em><b>Preprocess feature scaler</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<Preprocess_feature_scaler> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Preprocess feature scaler</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Preprocess_feature_scaler get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Preprocess_feature_scaler result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Preprocess feature scaler</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Preprocess_feature_scaler getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Preprocess_feature_scaler result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Preprocess feature scaler</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Preprocess_feature_scaler get(int value)
  {
    switch (value)
    {
      case NOT_SET_VALUE: return NOT_SET;
      case OFF_VALUE: return OFF;
      case STANDARD_SCALER_VALUE: return STANDARD_SCALER;
      case MIN_MAX_SCALER_VALUE: return MIN_MAX_SCALER;
      case ROBUST_SCALER_VALUE: return ROBUST_SCALER;
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
  private Preprocess_feature_scaler(int value, String name, String literal)
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
  
} //Preprocess_feature_scaler
