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
 * A representation of the literals of the enumeration '<em><b>KNN Algorithms</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getKNN_Algorithms()
 * @model
 * @generated
 */
public enum KNN_Algorithms implements Enumerator
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
   * The '<em><b>BALL TREE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BALL_TREE_VALUE
   * @generated
   * @ordered
   */
  BALL_TREE(2, "BALL_TREE", "\'ball_tree\'"),

  /**
   * The '<em><b>KD TREE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #KD_TREE_VALUE
   * @generated
   * @ordered
   */
  KD_TREE(3, "KD_TREE", "\'kd_tree\'"),

  /**
   * The '<em><b>BRUTE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BRUTE_VALUE
   * @generated
   * @ordered
   */
  BRUTE(4, "BRUTE", "\'brute\'");

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
   * The '<em><b>BALL TREE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BALL_TREE
   * @model literal="\'ball_tree\'"
   * @generated
   * @ordered
   */
  public static final int BALL_TREE_VALUE = 2;

  /**
   * The '<em><b>KD TREE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #KD_TREE
   * @model literal="\'kd_tree\'"
   * @generated
   * @ordered
   */
  public static final int KD_TREE_VALUE = 3;

  /**
   * The '<em><b>BRUTE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BRUTE
   * @model literal="\'brute\'"
   * @generated
   * @ordered
   */
  public static final int BRUTE_VALUE = 4;

  /**
   * An array of all the '<em><b>KNN Algorithms</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final KNN_Algorithms[] VALUES_ARRAY =
    new KNN_Algorithms[]
    {
      NOT_SET,
      AUTO,
      BALL_TREE,
      KD_TREE,
      BRUTE,
    };

  /**
   * A public read-only list of all the '<em><b>KNN Algorithms</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<KNN_Algorithms> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>KNN Algorithms</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static KNN_Algorithms get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      KNN_Algorithms result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>KNN Algorithms</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static KNN_Algorithms getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      KNN_Algorithms result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>KNN Algorithms</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static KNN_Algorithms get(int value)
  {
    switch (value)
    {
      case NOT_SET_VALUE: return NOT_SET;
      case AUTO_VALUE: return AUTO;
      case BALL_TREE_VALUE: return BALL_TREE;
      case KD_TREE_VALUE: return KD_TREE;
      case BRUTE_VALUE: return BRUTE;
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
  private KNN_Algorithms(int value, String name, String literal)
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
  
} //KNN_Algorithms
