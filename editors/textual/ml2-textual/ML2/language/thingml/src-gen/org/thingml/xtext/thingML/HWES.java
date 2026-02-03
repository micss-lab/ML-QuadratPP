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
 * A representation of the model object '<em><b>HWES</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.HWES#getTrend <em>Trend</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.HWES#getDamped <em>Damped</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.HWES#getSeasonal <em>Seasonal</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.HWES#getSeasonal_periods <em>Seasonal periods</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.HWES#getInitialization_method <em>Initialization method</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.HWES#getUse_boxcox <em>Use boxcox</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.HWES#getRemove_bias <em>Remove bias</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.HWES#getOptimized <em>Optimized</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES()
 * @model
 * @generated
 */
public interface HWES extends statistical_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Trend</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trend</em>' attribute.
   * @see #setTrend(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES_Trend()
   * @model
   * @generated
   */
  String getTrend();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.HWES#getTrend <em>Trend</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trend</em>' attribute.
   * @see #getTrend()
   * @generated
   */
  void setTrend(String value);

  /**
   * Returns the value of the '<em><b>Damped</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Damped</em>' containment reference.
   * @see #setDamped(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES_Damped()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getDamped();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.HWES#getDamped <em>Damped</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Damped</em>' containment reference.
   * @see #getDamped()
   * @generated
   */
  void setDamped(BooleanLiteral value);

  /**
   * Returns the value of the '<em><b>Seasonal</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonal</em>' attribute.
   * @see #setSeasonal(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES_Seasonal()
   * @model
   * @generated
   */
  String getSeasonal();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.HWES#getSeasonal <em>Seasonal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonal</em>' attribute.
   * @see #getSeasonal()
   * @generated
   */
  void setSeasonal(String value);

  /**
   * Returns the value of the '<em><b>Seasonal periods</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Seasonal periods</em>' containment reference.
   * @see #setSeasonal_periods(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES_Seasonal_periods()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getSeasonal_periods();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.HWES#getSeasonal_periods <em>Seasonal periods</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Seasonal periods</em>' containment reference.
   * @see #getSeasonal_periods()
   * @generated
   */
  void setSeasonal_periods(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Initialization method</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initialization method</em>' attribute.
   * @see #setInitialization_method(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES_Initialization_method()
   * @model
   * @generated
   */
  String getInitialization_method();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.HWES#getInitialization_method <em>Initialization method</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Initialization method</em>' attribute.
   * @see #getInitialization_method()
   * @generated
   */
  void setInitialization_method(String value);

  /**
   * Returns the value of the '<em><b>Use boxcox</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Use boxcox</em>' containment reference.
   * @see #setUse_boxcox(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES_Use_boxcox()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getUse_boxcox();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.HWES#getUse_boxcox <em>Use boxcox</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Use boxcox</em>' containment reference.
   * @see #getUse_boxcox()
   * @generated
   */
  void setUse_boxcox(BooleanLiteral value);

  /**
   * Returns the value of the '<em><b>Remove bias</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remove bias</em>' containment reference.
   * @see #setRemove_bias(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES_Remove_bias()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getRemove_bias();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.HWES#getRemove_bias <em>Remove bias</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remove bias</em>' containment reference.
   * @see #getRemove_bias()
   * @generated
   */
  void setRemove_bias(BooleanLiteral value);

  /**
   * Returns the value of the '<em><b>Optimized</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Optimized</em>' containment reference.
   * @see #setOptimized(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getHWES_Optimized()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getOptimized();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.HWES#getOptimized <em>Optimized</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Optimized</em>' containment reference.
   * @see #getOptimized()
   * @generated
   */
  void setOptimized(BooleanLiteral value);

} // HWES
