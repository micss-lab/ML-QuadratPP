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
 * A representation of the literals of the enumeration '<em><b>Preprocessing Plots</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getPreprocessingPlots()
 * @model
 * @generated
 */
public enum PreprocessingPlots implements Enumerator
{
  /**
   * The '<em><b>LINE PLOT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LINE_PLOT_VALUE
   * @generated
   * @ordered
   */
  LINE_PLOT(0, "LINE_PLOT", "LINE_PLOT"),

  /**
   * The '<em><b>HISTOGRAM</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #HISTOGRAM_VALUE
   * @generated
   * @ordered
   */
  HISTOGRAM(1, "HISTOGRAM", "HISTOGRAM"),

  /**
   * The '<em><b>BOX PLOT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BOX_PLOT_VALUE
   * @generated
   * @ordered
   */
  BOX_PLOT(2, "BOX_PLOT", "BOX_PLOT"),

  /**
   * The '<em><b>SCATTER PLOT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SCATTER_PLOT_VALUE
   * @generated
   * @ordered
   */
  SCATTER_PLOT(3, "SCATTER_PLOT", "SCATTER_PLOT"),

  /**
   * The '<em><b>HEAT MAP</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #HEAT_MAP_VALUE
   * @generated
   * @ordered
   */
  HEAT_MAP(4, "HEAT_MAP", "HEAT_MAP"),

  /**
   * The '<em><b>AUTOCORRELATION PLOT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AUTOCORRELATION_PLOT_VALUE
   * @generated
   * @ordered
   */
  AUTOCORRELATION_PLOT(5, "AUTOCORRELATION_PLOT", "AUTOCORRELATION_PLOT"),

  /**
   * The '<em><b>LAG PLOT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LAG_PLOT_VALUE
   * @generated
   * @ordered
   */
  LAG_PLOT(6, "LAG_PLOT", "LAG_PLOT"),

  /**
   * The '<em><b>VIOLIN PLOT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VIOLIN_PLOT_VALUE
   * @generated
   * @ordered
   */
  VIOLIN_PLOT(7, "VIOLIN_PLOT", "VIOLIN_PLOT"),

  /**
   * The '<em><b>CORRELATION MATRIX PLOT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CORRELATION_MATRIX_PLOT_VALUE
   * @generated
   * @ordered
   */
  CORRELATION_MATRIX_PLOT(8, "CORRELATION_MATRIX_PLOT", "CORRELATION_MATRIX_PLOT"),

  /**
   * The '<em><b>PAIR PLOT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PAIR_PLOT_VALUE
   * @generated
   * @ordered
   */
  PAIR_PLOT(9, "PAIR_PLOT", "PAIR_PLOT"),

  /**
   * The '<em><b>CLASS IMBALANCE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CLASS_IMBALANCE_VALUE
   * @generated
   * @ordered
   */
  CLASS_IMBALANCE(10, "CLASS_IMBALANCE", "CLASS_IMBALANCE"),

  /**
   * The '<em><b>MULTIVARIATE DISTRIBUTION</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MULTIVARIATE_DISTRIBUTION_VALUE
   * @generated
   * @ordered
   */
  MULTIVARIATE_DISTRIBUTION(11, "MULTIVARIATE_DISTRIBUTION", "MULTIVARIATE_DISTRIBUTION");

  /**
   * The '<em><b>LINE PLOT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LINE_PLOT
   * @model
   * @generated
   * @ordered
   */
  public static final int LINE_PLOT_VALUE = 0;

  /**
   * The '<em><b>HISTOGRAM</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #HISTOGRAM
   * @model
   * @generated
   * @ordered
   */
  public static final int HISTOGRAM_VALUE = 1;

  /**
   * The '<em><b>BOX PLOT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BOX_PLOT
   * @model
   * @generated
   * @ordered
   */
  public static final int BOX_PLOT_VALUE = 2;

  /**
   * The '<em><b>SCATTER PLOT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SCATTER_PLOT
   * @model
   * @generated
   * @ordered
   */
  public static final int SCATTER_PLOT_VALUE = 3;

  /**
   * The '<em><b>HEAT MAP</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #HEAT_MAP
   * @model
   * @generated
   * @ordered
   */
  public static final int HEAT_MAP_VALUE = 4;

  /**
   * The '<em><b>AUTOCORRELATION PLOT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AUTOCORRELATION_PLOT
   * @model
   * @generated
   * @ordered
   */
  public static final int AUTOCORRELATION_PLOT_VALUE = 5;

  /**
   * The '<em><b>LAG PLOT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LAG_PLOT
   * @model
   * @generated
   * @ordered
   */
  public static final int LAG_PLOT_VALUE = 6;

  /**
   * The '<em><b>VIOLIN PLOT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VIOLIN_PLOT
   * @model
   * @generated
   * @ordered
   */
  public static final int VIOLIN_PLOT_VALUE = 7;

  /**
   * The '<em><b>CORRELATION MATRIX PLOT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CORRELATION_MATRIX_PLOT
   * @model
   * @generated
   * @ordered
   */
  public static final int CORRELATION_MATRIX_PLOT_VALUE = 8;

  /**
   * The '<em><b>PAIR PLOT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PAIR_PLOT
   * @model
   * @generated
   * @ordered
   */
  public static final int PAIR_PLOT_VALUE = 9;

  /**
   * The '<em><b>CLASS IMBALANCE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CLASS_IMBALANCE
   * @model
   * @generated
   * @ordered
   */
  public static final int CLASS_IMBALANCE_VALUE = 10;

  /**
   * The '<em><b>MULTIVARIATE DISTRIBUTION</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MULTIVARIATE_DISTRIBUTION
   * @model
   * @generated
   * @ordered
   */
  public static final int MULTIVARIATE_DISTRIBUTION_VALUE = 11;

  /**
   * An array of all the '<em><b>Preprocessing Plots</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final PreprocessingPlots[] VALUES_ARRAY =
    new PreprocessingPlots[]
    {
      LINE_PLOT,
      HISTOGRAM,
      BOX_PLOT,
      SCATTER_PLOT,
      HEAT_MAP,
      AUTOCORRELATION_PLOT,
      LAG_PLOT,
      VIOLIN_PLOT,
      CORRELATION_MATRIX_PLOT,
      PAIR_PLOT,
      CLASS_IMBALANCE,
      MULTIVARIATE_DISTRIBUTION,
    };

  /**
   * A public read-only list of all the '<em><b>Preprocessing Plots</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<PreprocessingPlots> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Preprocessing Plots</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static PreprocessingPlots get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      PreprocessingPlots result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Preprocessing Plots</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static PreprocessingPlots getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      PreprocessingPlots result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Preprocessing Plots</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static PreprocessingPlots get(int value)
  {
    switch (value)
    {
      case LINE_PLOT_VALUE: return LINE_PLOT;
      case HISTOGRAM_VALUE: return HISTOGRAM;
      case BOX_PLOT_VALUE: return BOX_PLOT;
      case SCATTER_PLOT_VALUE: return SCATTER_PLOT;
      case HEAT_MAP_VALUE: return HEAT_MAP;
      case AUTOCORRELATION_PLOT_VALUE: return AUTOCORRELATION_PLOT;
      case LAG_PLOT_VALUE: return LAG_PLOT;
      case VIOLIN_PLOT_VALUE: return VIOLIN_PLOT;
      case CORRELATION_MATRIX_PLOT_VALUE: return CORRELATION_MATRIX_PLOT;
      case PAIR_PLOT_VALUE: return PAIR_PLOT;
      case CLASS_IMBALANCE_VALUE: return CLASS_IMBALANCE;
      case MULTIVARIATE_DISTRIBUTION_VALUE: return MULTIVARIATE_DISTRIBUTION;
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
  private PreprocessingPlots(int value, String name, String literal)
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
  
} //PreprocessingPlots
