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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ARIMA</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA#getP <em>P</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA#getDifferencing <em>Differencing</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA#getQ <em>Q</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA#getTrend <em>Trend</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA#getSeasonalP <em>Seasonal P</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA#getSeasonalD <em>Seasonal D</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA#getSeasonalQ <em>Seasonal Q</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.ARIMA#getSeasonality <em>Seasonality</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA()
 * @model
 * @generated
 */
public interface ARIMA extends statistical_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>P</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>P</em>' containment reference.
   * @see #setP(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_P()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getP();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA#getP <em>P</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>P</em>' containment reference.
   * @see #getP()
   * @generated
   */
  void setP(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Differencing</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Differencing</em>' containment reference.
   * @see #setDifferencing(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_Differencing()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getDifferencing();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA#getDifferencing <em>Differencing</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Differencing</em>' containment reference.
   * @see #getDifferencing()
   * @generated
   */
  void setDifferencing(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Q</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Q</em>' containment reference.
   * @see #setQ(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_Q()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getQ();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA#getQ <em>Q</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Q</em>' containment reference.
   * @see #getQ()
   * @generated
   */
  void setQ(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Trend</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trend</em>' attribute.
   * @see #setTrend(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_Trend()
   * @model
   * @generated
   */
  String getTrend();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA#getTrend <em>Trend</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trend</em>' attribute.
   * @see #getTrend()
   * @generated
   */
  void setTrend(String value);

  /**
   * Returns the value of the '<em><b>Seasonal P</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonal P</em>' containment reference.
   * @see #setSeasonalP(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_SeasonalP()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getSeasonalP();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA#getSeasonalP <em>Seasonal P</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonal P</em>' containment reference.
   * @see #getSeasonalP()
   * @generated
   */
  void setSeasonalP(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Seasonal D</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonal D</em>' containment reference.
   * @see #setSeasonalD(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_SeasonalD()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getSeasonalD();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA#getSeasonalD <em>Seasonal D</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonal D</em>' containment reference.
   * @see #getSeasonalD()
   * @generated
   */
  void setSeasonalD(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Seasonal Q</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonal Q</em>' containment reference.
   * @see #setSeasonalQ(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_SeasonalQ()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getSeasonalQ();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA#getSeasonalQ <em>Seasonal Q</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonal Q</em>' containment reference.
   * @see #getSeasonalQ()
   * @generated
   */
  void setSeasonalQ(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Seasonality</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonality</em>' containment reference.
   * @see #setSeasonality(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getARIMA_Seasonality()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getSeasonality();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.ARIMA#getSeasonality <em>Seasonality</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonality</em>' containment reference.
   * @see #getSeasonality()
   * @generated
   */
  void setSeasonality(IntegerLiteral value);

} // ARIMA
