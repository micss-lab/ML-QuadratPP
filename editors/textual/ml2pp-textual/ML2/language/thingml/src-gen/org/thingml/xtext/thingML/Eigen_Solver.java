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
 * A representation of the literals of the enumeration '<em><b>Eigen Solver</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getEigen_Solver()
 * @model
 * @generated
 */
public enum Eigen_Solver implements Enumerator
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
   * The '<em><b>AUTO</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AUTO_VALUE
   * @generated
   * @ordered
   */
  AUTO(1, "AUTO", "\'auto\'"),

  /**
   * The '<em><b>ARPACK</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ARPACK_VALUE
   * @generated
   * @ordered
   */
  ARPACK(2, "ARPACK", "\'arpack\'"),

  /**
   * The '<em><b>LOBPCG</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LOBPCG_VALUE
   * @generated
   * @ordered
   */
  LOBPCG(3, "LOBPCG", "\'lobpcg\'"),

  /**
   * The '<em><b>AMG</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AMG_VALUE
   * @generated
   * @ordered
   */
  AMG(4, "AMG", "\'amg\'");

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
   * The '<em><b>AUTO</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AUTO
   * @model literal="\'auto\'"
   * @generated
   * @ordered
   */
  public static final int AUTO_VALUE = 1;

  /**
   * The '<em><b>ARPACK</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ARPACK
   * @model literal="\'arpack\'"
   * @generated
   * @ordered
   */
  public static final int ARPACK_VALUE = 2;

  /**
   * The '<em><b>LOBPCG</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LOBPCG
   * @model literal="\'lobpcg\'"
   * @generated
   * @ordered
   */
  public static final int LOBPCG_VALUE = 3;

  /**
   * The '<em><b>AMG</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AMG
   * @model literal="\'amg\'"
   * @generated
   * @ordered
   */
  public static final int AMG_VALUE = 4;

  /**
   * An array of all the '<em><b>Eigen Solver</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final Eigen_Solver[] VALUES_ARRAY =
    new Eigen_Solver[]
    {
      NOT_SET,
      AUTO,
      ARPACK,
      LOBPCG,
      AMG,
    };

  /**
   * A public read-only list of all the '<em><b>Eigen Solver</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<Eigen_Solver> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Eigen Solver</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Eigen_Solver get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Eigen_Solver result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Eigen Solver</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Eigen_Solver getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Eigen_Solver result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Eigen Solver</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Eigen_Solver get(int value)
  {
    switch (value)
    {
      case NOT_SET_VALUE: return NOT_SET;
      case AUTO_VALUE: return AUTO;
      case ARPACK_VALUE: return ARPACK;
      case LOBPCG_VALUE: return LOBPCG;
      case AMG_VALUE: return AMG;
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
  private Eigen_Solver(int value, String name, String literal)
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
  
} //Eigen_Solver
