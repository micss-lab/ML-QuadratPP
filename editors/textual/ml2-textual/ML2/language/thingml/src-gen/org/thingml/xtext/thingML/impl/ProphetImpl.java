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

import org.thingml.xtext.thingML.Prophet;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Prophet</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.ProphetImpl#getGrowth <em>Growth</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.ProphetImpl#getSeasonality_mode <em>Seasonality mode</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.ProphetImpl#getSeasonality_prior_scale <em>Seasonality prior scale</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.ProphetImpl#getHolidays_prior_scale <em>Holidays prior scale</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.ProphetImpl#getChangepoint_prior_scale <em>Changepoint prior scale</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.ProphetImpl#getInterval_width <em>Interval width</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProphetImpl extends hybrid_ModelAlgorithmImpl implements Prophet
{
  /**
   * The default value of the '{@link #getGrowth() <em>Growth</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGrowth()
   * @generated
   * @ordered
   */
  protected static final String GROWTH_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getGrowth() <em>Growth</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGrowth()
   * @generated
   * @ordered
   */
  protected String growth = GROWTH_EDEFAULT;

  /**
   * The default value of the '{@link #getSeasonality_mode() <em>Seasonality mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSeasonality_mode()
   * @generated
   * @ordered
   */
  protected static final String SEASONALITY_MODE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSeasonality_mode() <em>Seasonality mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSeasonality_mode()
   * @generated
   * @ordered
   */
  protected String seasonality_mode = SEASONALITY_MODE_EDEFAULT;

  /**
   * The default value of the '{@link #getSeasonality_prior_scale() <em>Seasonality prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSeasonality_prior_scale()
   * @generated
   * @ordered
   */
  protected static final double SEASONALITY_PRIOR_SCALE_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getSeasonality_prior_scale() <em>Seasonality prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSeasonality_prior_scale()
   * @generated
   * @ordered
   */
  protected double seasonality_prior_scale = SEASONALITY_PRIOR_SCALE_EDEFAULT;

  /**
   * The default value of the '{@link #getHolidays_prior_scale() <em>Holidays prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHolidays_prior_scale()
   * @generated
   * @ordered
   */
  protected static final double HOLIDAYS_PRIOR_SCALE_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getHolidays_prior_scale() <em>Holidays prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHolidays_prior_scale()
   * @generated
   * @ordered
   */
  protected double holidays_prior_scale = HOLIDAYS_PRIOR_SCALE_EDEFAULT;

  /**
   * The default value of the '{@link #getChangepoint_prior_scale() <em>Changepoint prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChangepoint_prior_scale()
   * @generated
   * @ordered
   */
  protected static final double CHANGEPOINT_PRIOR_SCALE_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getChangepoint_prior_scale() <em>Changepoint prior scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChangepoint_prior_scale()
   * @generated
   * @ordered
   */
  protected double changepoint_prior_scale = CHANGEPOINT_PRIOR_SCALE_EDEFAULT;

  /**
   * The default value of the '{@link #getInterval_width() <em>Interval width</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInterval_width()
   * @generated
   * @ordered
   */
  protected static final double INTERVAL_WIDTH_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getInterval_width() <em>Interval width</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInterval_width()
   * @generated
   * @ordered
   */
  protected double interval_width = INTERVAL_WIDTH_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProphetImpl()
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
    return ThingMLPackage.eINSTANCE.getProphet();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getGrowth()
  {
    return growth;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setGrowth(String newGrowth)
  {
    String oldGrowth = growth;
    growth = newGrowth;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.PROPHET__GROWTH, oldGrowth, growth));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getSeasonality_mode()
  {
    return seasonality_mode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSeasonality_mode(String newSeasonality_mode)
  {
    String oldSeasonality_mode = seasonality_mode;
    seasonality_mode = newSeasonality_mode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.PROPHET__SEASONALITY_MODE, oldSeasonality_mode, seasonality_mode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getSeasonality_prior_scale()
  {
    return seasonality_prior_scale;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSeasonality_prior_scale(double newSeasonality_prior_scale)
  {
    double oldSeasonality_prior_scale = seasonality_prior_scale;
    seasonality_prior_scale = newSeasonality_prior_scale;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.PROPHET__SEASONALITY_PRIOR_SCALE, oldSeasonality_prior_scale, seasonality_prior_scale));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getHolidays_prior_scale()
  {
    return holidays_prior_scale;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setHolidays_prior_scale(double newHolidays_prior_scale)
  {
    double oldHolidays_prior_scale = holidays_prior_scale;
    holidays_prior_scale = newHolidays_prior_scale;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.PROPHET__HOLIDAYS_PRIOR_SCALE, oldHolidays_prior_scale, holidays_prior_scale));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getChangepoint_prior_scale()
  {
    return changepoint_prior_scale;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setChangepoint_prior_scale(double newChangepoint_prior_scale)
  {
    double oldChangepoint_prior_scale = changepoint_prior_scale;
    changepoint_prior_scale = newChangepoint_prior_scale;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.PROPHET__CHANGEPOINT_PRIOR_SCALE, oldChangepoint_prior_scale, changepoint_prior_scale));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getInterval_width()
  {
    return interval_width;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setInterval_width(double newInterval_width)
  {
    double oldInterval_width = interval_width;
    interval_width = newInterval_width;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.PROPHET__INTERVAL_WIDTH, oldInterval_width, interval_width));
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
      case ThingMLPackage.PROPHET__GROWTH:
        return getGrowth();
      case ThingMLPackage.PROPHET__SEASONALITY_MODE:
        return getSeasonality_mode();
      case ThingMLPackage.PROPHET__SEASONALITY_PRIOR_SCALE:
        return getSeasonality_prior_scale();
      case ThingMLPackage.PROPHET__HOLIDAYS_PRIOR_SCALE:
        return getHolidays_prior_scale();
      case ThingMLPackage.PROPHET__CHANGEPOINT_PRIOR_SCALE:
        return getChangepoint_prior_scale();
      case ThingMLPackage.PROPHET__INTERVAL_WIDTH:
        return getInterval_width();
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
      case ThingMLPackage.PROPHET__GROWTH:
        setGrowth((String)newValue);
        return;
      case ThingMLPackage.PROPHET__SEASONALITY_MODE:
        setSeasonality_mode((String)newValue);
        return;
      case ThingMLPackage.PROPHET__SEASONALITY_PRIOR_SCALE:
        setSeasonality_prior_scale((Double)newValue);
        return;
      case ThingMLPackage.PROPHET__HOLIDAYS_PRIOR_SCALE:
        setHolidays_prior_scale((Double)newValue);
        return;
      case ThingMLPackage.PROPHET__CHANGEPOINT_PRIOR_SCALE:
        setChangepoint_prior_scale((Double)newValue);
        return;
      case ThingMLPackage.PROPHET__INTERVAL_WIDTH:
        setInterval_width((Double)newValue);
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
      case ThingMLPackage.PROPHET__GROWTH:
        setGrowth(GROWTH_EDEFAULT);
        return;
      case ThingMLPackage.PROPHET__SEASONALITY_MODE:
        setSeasonality_mode(SEASONALITY_MODE_EDEFAULT);
        return;
      case ThingMLPackage.PROPHET__SEASONALITY_PRIOR_SCALE:
        setSeasonality_prior_scale(SEASONALITY_PRIOR_SCALE_EDEFAULT);
        return;
      case ThingMLPackage.PROPHET__HOLIDAYS_PRIOR_SCALE:
        setHolidays_prior_scale(HOLIDAYS_PRIOR_SCALE_EDEFAULT);
        return;
      case ThingMLPackage.PROPHET__CHANGEPOINT_PRIOR_SCALE:
        setChangepoint_prior_scale(CHANGEPOINT_PRIOR_SCALE_EDEFAULT);
        return;
      case ThingMLPackage.PROPHET__INTERVAL_WIDTH:
        setInterval_width(INTERVAL_WIDTH_EDEFAULT);
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
      case ThingMLPackage.PROPHET__GROWTH:
        return GROWTH_EDEFAULT == null ? growth != null : !GROWTH_EDEFAULT.equals(growth);
      case ThingMLPackage.PROPHET__SEASONALITY_MODE:
        return SEASONALITY_MODE_EDEFAULT == null ? seasonality_mode != null : !SEASONALITY_MODE_EDEFAULT.equals(seasonality_mode);
      case ThingMLPackage.PROPHET__SEASONALITY_PRIOR_SCALE:
        return seasonality_prior_scale != SEASONALITY_PRIOR_SCALE_EDEFAULT;
      case ThingMLPackage.PROPHET__HOLIDAYS_PRIOR_SCALE:
        return holidays_prior_scale != HOLIDAYS_PRIOR_SCALE_EDEFAULT;
      case ThingMLPackage.PROPHET__CHANGEPOINT_PRIOR_SCALE:
        return changepoint_prior_scale != CHANGEPOINT_PRIOR_SCALE_EDEFAULT;
      case ThingMLPackage.PROPHET__INTERVAL_WIDTH:
        return interval_width != INTERVAL_WIDTH_EDEFAULT;
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
    result.append(" (growth: ");
    result.append(growth);
    result.append(", seasonality_mode: ");
    result.append(seasonality_mode);
    result.append(", seasonality_prior_scale: ");
    result.append(seasonality_prior_scale);
    result.append(", holidays_prior_scale: ");
    result.append(holidays_prior_scale);
    result.append(", changepoint_prior_scale: ");
    result.append(changepoint_prior_scale);
    result.append(", interval_width: ");
    result.append(interval_width);
    result.append(')');
    return result.toString();
  }

} //ProphetImpl
