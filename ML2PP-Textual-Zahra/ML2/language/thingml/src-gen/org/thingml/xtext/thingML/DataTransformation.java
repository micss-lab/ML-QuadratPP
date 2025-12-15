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
 * A representation of the literals of the enumeration '<em><b>Data Transformation</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getDataTransformation()
 * @model
 * @generated
 */
public enum DataTransformation implements Enumerator
{
  /**
   * The '<em><b>LOG TRANSFORMATION</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LOG_TRANSFORMATION_VALUE
   * @generated
   * @ordered
   */
  LOG_TRANSFORMATION(0, "LOG_TRANSFORMATION", "LogTransformation"),

  /**
   * The '<em><b>DIFFERENCING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #DIFFERENCING_VALUE
   * @generated
   * @ordered
   */
  DIFFERENCING(1, "DIFFERENCING", "Differencing"),

  /**
   * The '<em><b>NORMALIZATION</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NORMALIZATION_VALUE
   * @generated
   * @ordered
   */
  NORMALIZATION(2, "NORMALIZATION", "Normalization"),

  /**
   * The '<em><b>STANDARDIZATION</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STANDARDIZATION_VALUE
   * @generated
   * @ordered
   */
  STANDARDIZATION(3, "STANDARDIZATION", "Standardization"),

  /**
   * The '<em><b>SCALING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SCALING_VALUE
   * @generated
   * @ordered
   */
  SCALING(4, "SCALING", "Scaling"),

  /**
   * The '<em><b>SMOOTHING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SMOOTHING_VALUE
   * @generated
   * @ordered
   */
  SMOOTHING(5, "SMOOTHING", "Smoothing"),

  /**
   * The '<em><b>AGGREGATION</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AGGREGATION_VALUE
   * @generated
   * @ordered
   */
  AGGREGATION(6, "AGGREGATION", "Aggregation"),

  /**
   * The '<em><b>DOWNSAMPLING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #DOWNSAMPLING_VALUE
   * @generated
   * @ordered
   */
  DOWNSAMPLING(7, "DOWNSAMPLING", "Downsampling"),

  /**
   * The '<em><b>UPSAMPLING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #UPSAMPLING_VALUE
   * @generated
   * @ordered
   */
  UPSAMPLING(8, "UPSAMPLING", "Upsampling");

  /**
   * The '<em><b>LOG TRANSFORMATION</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LOG_TRANSFORMATION
   * @model literal="LogTransformation"
   * @generated
   * @ordered
   */
  public static final int LOG_TRANSFORMATION_VALUE = 0;

  /**
   * The '<em><b>DIFFERENCING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #DIFFERENCING
   * @model literal="Differencing"
   * @generated
   * @ordered
   */
  public static final int DIFFERENCING_VALUE = 1;

  /**
   * The '<em><b>NORMALIZATION</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NORMALIZATION
   * @model literal="Normalization"
   * @generated
   * @ordered
   */
  public static final int NORMALIZATION_VALUE = 2;

  /**
   * The '<em><b>STANDARDIZATION</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STANDARDIZATION
   * @model literal="Standardization"
   * @generated
   * @ordered
   */
  public static final int STANDARDIZATION_VALUE = 3;

  /**
   * The '<em><b>SCALING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SCALING
   * @model literal="Scaling"
   * @generated
   * @ordered
   */
  public static final int SCALING_VALUE = 4;

  /**
   * The '<em><b>SMOOTHING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SMOOTHING
   * @model literal="Smoothing"
   * @generated
   * @ordered
   */
  public static final int SMOOTHING_VALUE = 5;

  /**
   * The '<em><b>AGGREGATION</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AGGREGATION
   * @model literal="Aggregation"
   * @generated
   * @ordered
   */
  public static final int AGGREGATION_VALUE = 6;

  /**
   * The '<em><b>DOWNSAMPLING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #DOWNSAMPLING
   * @model literal="Downsampling"
   * @generated
   * @ordered
   */
  public static final int DOWNSAMPLING_VALUE = 7;

  /**
   * The '<em><b>UPSAMPLING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #UPSAMPLING
   * @model literal="Upsampling"
   * @generated
   * @ordered
   */
  public static final int UPSAMPLING_VALUE = 8;

  /**
   * An array of all the '<em><b>Data Transformation</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final DataTransformation[] VALUES_ARRAY =
    new DataTransformation[]
    {
      LOG_TRANSFORMATION,
      DIFFERENCING,
      NORMALIZATION,
      STANDARDIZATION,
      SCALING,
      SMOOTHING,
      AGGREGATION,
      DOWNSAMPLING,
      UPSAMPLING,
    };

  /**
   * A public read-only list of all the '<em><b>Data Transformation</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<DataTransformation> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Data Transformation</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static DataTransformation get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      DataTransformation result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Data Transformation</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static DataTransformation getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      DataTransformation result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Data Transformation</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static DataTransformation get(int value)
  {
    switch (value)
    {
      case LOG_TRANSFORMATION_VALUE: return LOG_TRANSFORMATION;
      case DIFFERENCING_VALUE: return DIFFERENCING;
      case NORMALIZATION_VALUE: return NORMALIZATION;
      case STANDARDIZATION_VALUE: return STANDARDIZATION;
      case SCALING_VALUE: return SCALING;
      case SMOOTHING_VALUE: return SMOOTHING;
      case AGGREGATION_VALUE: return AGGREGATION;
      case DOWNSAMPLING_VALUE: return DOWNSAMPLING;
      case UPSAMPLING_VALUE: return UPSAMPLING;
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
  private DataTransformation(int value, String name, String literal)
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
  
} //DataTransformation
