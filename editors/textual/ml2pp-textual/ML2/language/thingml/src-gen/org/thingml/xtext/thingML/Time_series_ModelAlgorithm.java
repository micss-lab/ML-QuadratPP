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
 * A representation of the model object '<em><b>Time series Model Algorithm</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.Time_series_ModelAlgorithm#getForecasting_plots <em>Forecasting plots</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getTime_series_ModelAlgorithm()
 * @model
 * @generated
 */
public interface Time_series_ModelAlgorithm extends DataAnalyticsModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Forecasting plots</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.ForecastingPlots}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Forecasting plots</em>' attribute.
   * @see org.thingml.xtext.thingML.ForecastingPlots
   * @see #setForecasting_plots(ForecastingPlots)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getTime_series_ModelAlgorithm_Forecasting_plots()
   * @model
   * @generated
   */
  ForecastingPlots getForecasting_plots();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Time_series_ModelAlgorithm#getForecasting_plots <em>Forecasting plots</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Forecasting plots</em>' attribute.
   * @see org.thingml.xtext.thingML.ForecastingPlots
   * @see #getForecasting_plots()
   * @generated
   */
  void setForecasting_plots(ForecastingPlots value);

} // Time_series_ModelAlgorithm
