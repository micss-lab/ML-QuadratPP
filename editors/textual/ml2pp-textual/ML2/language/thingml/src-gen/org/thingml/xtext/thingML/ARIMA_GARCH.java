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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ARIMA GARCH</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA_GARCH#getP <em>P</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA_GARCH#getD <em>D</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA_GARCH#getQ <em>Q</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA_GARCH#getSeasonal_order <em>Seasonal order</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA_GARCH#getSeasonal_params <em>Seasonal params</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA_GARCH#getTrend <em>Trend</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA_GARCH#getGarch_order <em>Garch order</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA_GARCH#getGarch_params <em>Garch params</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH()
 * @model
 * @generated
 */
public interface ARIMA_GARCH extends hybrid_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>P</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>P</em>' containment reference.
   * @see #setP(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH_P()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getP();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA_GARCH#getP <em>P</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>P</em>' containment reference.
   * @see #getP()
   * @generated
   */
  void setP(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>D</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>D</em>' containment reference.
   * @see #setD(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH_D()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getD();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA_GARCH#getD <em>D</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>D</em>' containment reference.
   * @see #getD()
   * @generated
   */
  void setD(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Q</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Q</em>' containment reference.
   * @see #setQ(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH_Q()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getQ();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA_GARCH#getQ <em>Q</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Q</em>' containment reference.
   * @see #getQ()
   * @generated
   */
  void setQ(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Seasonal order</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonal order</em>' attribute.
   * @see #setSeasonal_order(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH_Seasonal_order()
   * @model
   * @generated
   */
  String getSeasonal_order();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA_GARCH#getSeasonal_order <em>Seasonal order</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonal order</em>' attribute.
   * @see #getSeasonal_order()
   * @generated
   */
  void setSeasonal_order(String value);

  /**
   * Returns the value of the '<em><b>Seasonal params</b></em>' containment reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.IntegerLiteral}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonal params</em>' containment reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH_Seasonal_params()
   * @model containment="true"
   * @generated
   */
  EList<IntegerLiteral> getSeasonal_params();

  /**
   * Returns the value of the '<em><b>Trend</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trend</em>' attribute.
   * @see #setTrend(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH_Trend()
   * @model
   * @generated
   */
  String getTrend();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA_GARCH#getTrend <em>Trend</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trend</em>' attribute.
   * @see #getTrend()
   * @generated
   */
  void setTrend(String value);

  /**
   * Returns the value of the '<em><b>Garch order</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Garch order</em>' attribute.
   * @see #setGarch_order(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH_Garch_order()
   * @model
   * @generated
   */
  String getGarch_order();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA_GARCH#getGarch_order <em>Garch order</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Garch order</em>' attribute.
   * @see #getGarch_order()
   * @generated
   */
  void setGarch_order(String value);

  /**
   * Returns the value of the '<em><b>Garch params</b></em>' containment reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.IntegerLiteral}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Garch params</em>' containment reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_GARCH_Garch_params()
   * @model containment="true"
   * @generated
   */
  EList<IntegerLiteral> getGarch_params();

} // ARIMA_GARCH
