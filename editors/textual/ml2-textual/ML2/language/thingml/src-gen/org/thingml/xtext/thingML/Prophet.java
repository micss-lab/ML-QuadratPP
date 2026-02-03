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
 * A representation of the model object '<em><b>Prophet</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.Prophet#getGrowth <em>Growth</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Prophet#getSeasonality_mode <em>Seasonality mode</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Prophet#getSeasonality_prior_scale <em>Seasonality prior scale</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Prophet#getHolidays_prior_scale <em>Holidays prior scale</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Prophet#getChangepoint_prior_scale <em>Changepoint prior scale</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Prophet#getInterval_width <em>Interval width</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getProphet()
 * @model
 * @generated
 */
public interface Prophet extends hybrid_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Growth</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Growth</em>' attribute.
   * @see #setGrowth(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getProphet_Growth()
   * @model
   * @generated
   */
  String getGrowth();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Prophet#getGrowth <em>Growth</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Growth</em>' attribute.
   * @see #getGrowth()
   * @generated
   */
  void setGrowth(String value);

  /**
   * Returns the value of the '<em><b>Seasonality mode</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonality mode</em>' attribute.
   * @see #setSeasonality_mode(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getProphet_Seasonality_mode()
   * @model
   * @generated
   */
  String getSeasonality_mode();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Prophet#getSeasonality_mode <em>Seasonality mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonality mode</em>' attribute.
   * @see #getSeasonality_mode()
   * @generated
   */
  void setSeasonality_mode(String value);

  /**
   * Returns the value of the '<em><b>Seasonality prior scale</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonality prior scale</em>' attribute.
   * @see #setSeasonality_prior_scale(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getProphet_Seasonality_prior_scale()
   * @model
   * @generated
   */
  double getSeasonality_prior_scale();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Prophet#getSeasonality_prior_scale <em>Seasonality prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonality prior scale</em>' attribute.
   * @see #getSeasonality_prior_scale()
   * @generated
   */
  void setSeasonality_prior_scale(double value);

  /**
   * Returns the value of the '<em><b>Holidays prior scale</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Holidays prior scale</em>' attribute.
   * @see #setHolidays_prior_scale(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getProphet_Holidays_prior_scale()
   * @model
   * @generated
   */
  double getHolidays_prior_scale();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Prophet#getHolidays_prior_scale <em>Holidays prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Holidays prior scale</em>' attribute.
   * @see #getHolidays_prior_scale()
   * @generated
   */
  void setHolidays_prior_scale(double value);

  /**
   * Returns the value of the '<em><b>Changepoint prior scale</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Changepoint prior scale</em>' attribute.
   * @see #setChangepoint_prior_scale(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getProphet_Changepoint_prior_scale()
   * @model
   * @generated
   */
  double getChangepoint_prior_scale();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Prophet#getChangepoint_prior_scale <em>Changepoint prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Changepoint prior scale</em>' attribute.
   * @see #getChangepoint_prior_scale()
   * @generated
   */
  void setChangepoint_prior_scale(double value);

  /**
   * Returns the value of the '<em><b>Interval width</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Interval width</em>' attribute.
   * @see #setInterval_width(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getProphet_Interval_width()
   * @model
   * @generated
   */
  double getInterval_width();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Prophet#getInterval_width <em>Interval width</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Interval width</em>' attribute.
   * @see #getInterval_width()
   * @generated
   */
  void setInterval_width(double value);

} // Prophet
