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
 * A representation of the literals of the enumeration '<em><b>Model Evaluation</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getModelEvaluation()
 * @model
 * @generated
 */
public enum ModelEvaluation implements Enumerator
{
  /**
   * The '<em><b>RMSE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RMSE_VALUE
   * @generated
   * @ordered
   */
  RMSE(0, "RMSE", "RMSE"),

  /**
   * The '<em><b>MAE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MAE_VALUE
   * @generated
   * @ordered
   */
  MAE(1, "MAE", "MAE"),

  /**
   * The '<em><b>MSE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MSE_VALUE
   * @generated
   * @ordered
   */
  MSE(2, "MSE", "MSE"),

  /**
   * The '<em><b>RSQUARED</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RSQUARED_VALUE
   * @generated
   * @ordered
   */
  RSQUARED(3, "R_SQUARED", "R_SQUARED"),

  /**
   * The '<em><b>ACCURACY</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ACCURACY_VALUE
   * @generated
   * @ordered
   */
  ACCURACY(4, "ACCURACY", "ACCURACY"),

  /**
   * The '<em><b>PRECISION</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PRECISION_VALUE
   * @generated
   * @ordered
   */
  PRECISION(5, "PRECISION", "PRECISION"),

  /**
   * The '<em><b>RECALL</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RECALL_VALUE
   * @generated
   * @ordered
   */
  RECALL(6, "RECALL", "RECALL"),

  /**
   * The '<em><b>F1 SCORE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #F1_SCORE_VALUE
   * @generated
   * @ordered
   */
  F1_SCORE(7, "F1_SCORE", "F1_SCORE");

  /**
   * The '<em><b>RMSE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RMSE
   * @model
   * @generated
   * @ordered
   */
  public static final int RMSE_VALUE = 0;

  /**
   * The '<em><b>MAE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MAE
   * @model
   * @generated
   * @ordered
   */
  public static final int MAE_VALUE = 1;

  /**
   * The '<em><b>MSE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MSE
   * @model
   * @generated
   * @ordered
   */
  public static final int MSE_VALUE = 2;

  /**
   * The '<em><b>RSQUARED</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RSQUARED
   * @model name="R_SQUARED"
   * @generated
   * @ordered
   */
  public static final int RSQUARED_VALUE = 3;

  /**
   * The '<em><b>ACCURACY</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ACCURACY
   * @model
   * @generated
   * @ordered
   */
  public static final int ACCURACY_VALUE = 4;

  /**
   * The '<em><b>PRECISION</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PRECISION
   * @model
   * @generated
   * @ordered
   */
  public static final int PRECISION_VALUE = 5;

  /**
   * The '<em><b>RECALL</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RECALL
   * @model
   * @generated
   * @ordered
   */
  public static final int RECALL_VALUE = 6;

  /**
   * The '<em><b>F1 SCORE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #F1_SCORE
   * @model
   * @generated
   * @ordered
   */
  public static final int F1_SCORE_VALUE = 7;

  /**
   * An array of all the '<em><b>Model Evaluation</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final ModelEvaluation[] VALUES_ARRAY =
    new ModelEvaluation[]
    {
      RMSE,
      MAE,
      MSE,
      RSQUARED,
      ACCURACY,
      PRECISION,
      RECALL,
      F1_SCORE,
    };

  /**
   * A public read-only list of all the '<em><b>Model Evaluation</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<ModelEvaluation> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Model Evaluation</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ModelEvaluation get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ModelEvaluation result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Model Evaluation</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ModelEvaluation getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ModelEvaluation result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Model Evaluation</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ModelEvaluation get(int value)
  {
    switch (value)
    {
      case RMSE_VALUE: return RMSE;
      case MAE_VALUE: return MAE;
      case MSE_VALUE: return MSE;
      case RSQUARED_VALUE: return RSQUARED;
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
  private ModelEvaluation(int value, String name, String literal)
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
  
} //ModelEvaluation
