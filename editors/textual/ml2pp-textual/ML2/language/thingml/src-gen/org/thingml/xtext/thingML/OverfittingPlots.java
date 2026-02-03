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
 * A representation of the literals of the enumeration '<em><b>Overfitting Plots</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getOverfittingPlots()
 * @model
 * @generated
 */
public enum OverfittingPlots implements Enumerator
{
  /**
   * The '<em><b>TRAINING LOSS</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TRAINING_LOSS_VALUE
   * @generated
   * @ordered
   */
  TRAINING_LOSS(0, "TRAINING_LOSS", "TRAINING_LOSS"),

  /**
   * The '<em><b>VALIDATION LOSS</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VALIDATION_LOSS_VALUE
   * @generated
   * @ordered
   */
  VALIDATION_LOSS(1, "VALIDATION_LOSS", "VALIDATION_LOSS"),

  /**
   * The '<em><b>LEARNING CURVE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LEARNING_CURVE_VALUE
   * @generated
   * @ordered
   */
  LEARNING_CURVE(2, "LEARNING_CURVE", "LEARNING_CURVE"),

  /**
   * The '<em><b>BIAS VARIANCE TRADEOFF</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BIAS_VARIANCE_TRADEOFF_VALUE
   * @generated
   * @ordered
   */
  BIAS_VARIANCE_TRADEOFF(3, "BIAS_VARIANCE_TRADEOFF", "BIAS_VARIANCE_TRADEOFF");

  /**
   * The '<em><b>TRAINING LOSS</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TRAINING_LOSS
   * @model
   * @generated
   * @ordered
   */
  public static final int TRAINING_LOSS_VALUE = 0;

  /**
   * The '<em><b>VALIDATION LOSS</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VALIDATION_LOSS
   * @model
   * @generated
   * @ordered
   */
  public static final int VALIDATION_LOSS_VALUE = 1;

  /**
   * The '<em><b>LEARNING CURVE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LEARNING_CURVE
   * @model
   * @generated
   * @ordered
   */
  public static final int LEARNING_CURVE_VALUE = 2;

  /**
   * The '<em><b>BIAS VARIANCE TRADEOFF</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BIAS_VARIANCE_TRADEOFF
   * @model
   * @generated
   * @ordered
   */
  public static final int BIAS_VARIANCE_TRADEOFF_VALUE = 3;

  /**
   * An array of all the '<em><b>Overfitting Plots</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final OverfittingPlots[] VALUES_ARRAY =
    new OverfittingPlots[]
    {
      TRAINING_LOSS,
      VALIDATION_LOSS,
      LEARNING_CURVE,
      BIAS_VARIANCE_TRADEOFF,
    };

  /**
   * A public read-only list of all the '<em><b>Overfitting Plots</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<OverfittingPlots> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Overfitting Plots</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static OverfittingPlots get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      OverfittingPlots result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Overfitting Plots</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static OverfittingPlots getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      OverfittingPlots result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Overfitting Plots</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static OverfittingPlots get(int value)
  {
    switch (value)
    {
      case TRAINING_LOSS_VALUE: return TRAINING_LOSS;
      case VALIDATION_LOSS_VALUE: return VALIDATION_LOSS;
      case LEARNING_CURVE_VALUE: return LEARNING_CURVE;
      case BIAS_VARIANCE_TRADEOFF_VALUE: return BIAS_VARIANCE_TRADEOFF;
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
  private OverfittingPlots(int value, String name, String literal)
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
  
} //OverfittingPlots
