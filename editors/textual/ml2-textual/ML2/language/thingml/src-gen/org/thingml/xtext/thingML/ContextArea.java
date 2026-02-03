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
 * A representation of the literals of the enumeration '<em><b>Context Area</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.thingml.xtext.thingML.ThingMLPackage#getContextArea()
 * @model
 * @generated
 */
public enum ContextArea implements Enumerator
{
  /**
   * The '<em><b>River Flow</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RIVER_FLOW_VALUE
   * @generated
   * @ordered
   */
  RIVER_FLOW(0, "RiverFlow", "RiverFlow"),

  /**
   * The '<em><b>Server Monitoring</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVER_MONITORING_VALUE
   * @generated
   * @ordered
   */
  SERVER_MONITORING(1, "ServerMonitoring", "ServerMonitoring"),

  /**
   * The '<em><b>Io TData Center</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #IO_TDATA_CENTER_VALUE
   * @generated
   * @ordered
   */
  IO_TDATA_CENTER(2, "IoTDataCenter", "IoTDataCenter"),

  /**
   * The '<em><b>Other</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OTHER_VALUE
   * @generated
   * @ordered
   */
  OTHER(3, "Other", "Other");

  /**
   * The '<em><b>River Flow</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RIVER_FLOW
   * @model name="RiverFlow"
   * @generated
   * @ordered
   */
  public static final int RIVER_FLOW_VALUE = 0;

  /**
   * The '<em><b>Server Monitoring</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVER_MONITORING
   * @model name="ServerMonitoring"
   * @generated
   * @ordered
   */
  public static final int SERVER_MONITORING_VALUE = 1;

  /**
   * The '<em><b>Io TData Center</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #IO_TDATA_CENTER
   * @model name="IoTDataCenter"
   * @generated
   * @ordered
   */
  public static final int IO_TDATA_CENTER_VALUE = 2;

  /**
   * The '<em><b>Other</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OTHER
   * @model name="Other"
   * @generated
   * @ordered
   */
  public static final int OTHER_VALUE = 3;

  /**
   * An array of all the '<em><b>Context Area</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final ContextArea[] VALUES_ARRAY =
    new ContextArea[]
    {
      RIVER_FLOW,
      SERVER_MONITORING,
      IO_TDATA_CENTER,
      OTHER,
    };

  /**
   * A public read-only list of all the '<em><b>Context Area</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<ContextArea> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Context Area</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ContextArea get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ContextArea result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Context Area</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ContextArea getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ContextArea result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Context Area</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ContextArea get(int value)
  {
    switch (value)
    {
      case RIVER_FLOW_VALUE: return RIVER_FLOW;
      case SERVER_MONITORING_VALUE: return SERVER_MONITORING;
      case IO_TDATA_CENTER_VALUE: return IO_TDATA_CENTER;
      case OTHER_VALUE: return OTHER;
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
  private ContextArea(int value, String name, String literal)
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
  
} //ContextArea
