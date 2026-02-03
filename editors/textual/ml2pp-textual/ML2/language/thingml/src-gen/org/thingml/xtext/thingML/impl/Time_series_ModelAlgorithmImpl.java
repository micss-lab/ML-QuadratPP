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
package org.thingml.xtext.thingML.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.thingml.xtext.thingML.ForecastingPlots;
import org.thingml.xtext.thingML.ThingMLPackage;
import org.thingml.xtext.thingML.Time_series_ModelAlgorithm;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time series Model Algorithm</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.Time_series_ModelAlgorithmImpl#getForecasting_plots <em>Forecasting plots</em>}</li>
 * </ul>
 *
 * @generated
 */
public class Time_series_ModelAlgorithmImpl extends DataAnalyticsModelAlgorithmImpl implements Time_series_ModelAlgorithm
{
  /**
   * The default value of the '{@link #getForecasting_plots() <em>Forecasting plots</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getForecasting_plots()
   * @generated
   * @ordered
   */
  protected static final ForecastingPlots FORECASTING_PLOTS_EDEFAULT = ForecastingPlots.FORECAST_VS_ACTUAL;

  /**
   * The cached value of the '{@link #getForecasting_plots() <em>Forecasting plots</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getForecasting_plots()
   * @generated
   * @ordered
   */
  protected ForecastingPlots forecasting_plots = FORECASTING_PLOTS_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Time_series_ModelAlgorithmImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ThingMLPackage.eINSTANCE.getTime_series_ModelAlgorithm();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ForecastingPlots getForecasting_plots()
  {
    return forecasting_plots;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setForecasting_plots(ForecastingPlots newForecasting_plots)
  {
    ForecastingPlots oldForecasting_plots = forecasting_plots;
    forecasting_plots = newForecasting_plots == null ? FORECASTING_PLOTS_EDEFAULT : newForecasting_plots;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.TIME_SERIES_MODEL_ALGORITHM__FORECASTING_PLOTS, oldForecasting_plots, forecasting_plots));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ThingMLPackage.TIME_SERIES_MODEL_ALGORITHM__FORECASTING_PLOTS:
        return getForecasting_plots();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ThingMLPackage.TIME_SERIES_MODEL_ALGORITHM__FORECASTING_PLOTS:
        setForecasting_plots((ForecastingPlots)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ThingMLPackage.TIME_SERIES_MODEL_ALGORITHM__FORECASTING_PLOTS:
        setForecasting_plots(FORECASTING_PLOTS_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ThingMLPackage.TIME_SERIES_MODEL_ALGORITHM__FORECASTING_PLOTS:
        return forecasting_plots != FORECASTING_PLOTS_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (forecasting_plots: ");
    result.append(forecasting_plots);
    result.append(')');
    return result.toString();
  }

} //Time_series_ModelAlgorithmImpl
