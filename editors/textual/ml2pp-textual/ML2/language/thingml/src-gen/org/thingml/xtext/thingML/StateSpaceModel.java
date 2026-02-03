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
 * A representation of the model object '<em><b>State Space Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getTransition_matrix <em>Transition matrix</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getSelection_matrix <em>Selection matrix</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getState_cov <em>State cov</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getObs_cov <em>Obs cov</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getInitial_state_mean <em>Initial state mean</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getInitial_state_cov <em>Initial state cov</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getNobs <em>Nobs</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getObs_intercept <em>Obs intercept</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getState_intercept <em>State intercept</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getObs <em>Obs</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.StateSpaceModel#getObs_offset <em>Obs offset</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel()
 * @model
 * @generated
 */
public interface StateSpaceModel extends statistical_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Transition matrix</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Transition matrix</em>' attribute.
   * @see #setTransition_matrix(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Transition_matrix()
   * @model
   * @generated
   */
  String getTransition_matrix();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getTransition_matrix <em>Transition matrix</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Transition matrix</em>' attribute.
   * @see #getTransition_matrix()
   * @generated
   */
  void setTransition_matrix(String value);

  /**
   * Returns the value of the '<em><b>Selection matrix</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Selection matrix</em>' attribute.
   * @see #setSelection_matrix(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Selection_matrix()
   * @model
   * @generated
   */
  String getSelection_matrix();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getSelection_matrix <em>Selection matrix</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Selection matrix</em>' attribute.
   * @see #getSelection_matrix()
   * @generated
   */
  void setSelection_matrix(String value);

  /**
   * Returns the value of the '<em><b>State cov</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>State cov</em>' attribute.
   * @see #setState_cov(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_State_cov()
   * @model
   * @generated
   */
  String getState_cov();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getState_cov <em>State cov</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>State cov</em>' attribute.
   * @see #getState_cov()
   * @generated
   */
  void setState_cov(String value);

  /**
   * Returns the value of the '<em><b>Obs cov</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Obs cov</em>' attribute.
   * @see #setObs_cov(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Obs_cov()
   * @model
   * @generated
   */
  String getObs_cov();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getObs_cov <em>Obs cov</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Obs cov</em>' attribute.
   * @see #getObs_cov()
   * @generated
   */
  void setObs_cov(String value);

  /**
   * Returns the value of the '<em><b>Initial state mean</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initial state mean</em>' attribute.
   * @see #setInitial_state_mean(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Initial_state_mean()
   * @model
   * @generated
   */
  String getInitial_state_mean();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getInitial_state_mean <em>Initial state mean</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Initial state mean</em>' attribute.
   * @see #getInitial_state_mean()
   * @generated
   */
  void setInitial_state_mean(String value);

  /**
   * Returns the value of the '<em><b>Initial state cov</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initial state cov</em>' attribute.
   * @see #setInitial_state_cov(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Initial_state_cov()
   * @model
   * @generated
   */
  String getInitial_state_cov();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getInitial_state_cov <em>Initial state cov</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Initial state cov</em>' attribute.
   * @see #getInitial_state_cov()
   * @generated
   */
  void setInitial_state_cov(String value);

  /**
   * Returns the value of the '<em><b>Nobs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Nobs</em>' containment reference.
   * @see #setNobs(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Nobs()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getNobs();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getNobs <em>Nobs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Nobs</em>' containment reference.
   * @see #getNobs()
   * @generated
   */
  void setNobs(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Obs intercept</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Obs intercept</em>' attribute.
   * @see #setObs_intercept(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Obs_intercept()
   * @model
   * @generated
   */
  String getObs_intercept();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getObs_intercept <em>Obs intercept</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Obs intercept</em>' attribute.
   * @see #getObs_intercept()
   * @generated
   */
  void setObs_intercept(String value);

  /**
   * Returns the value of the '<em><b>State intercept</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>State intercept</em>' attribute.
   * @see #setState_intercept(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_State_intercept()
   * @model
   * @generated
   */
  String getState_intercept();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getState_intercept <em>State intercept</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>State intercept</em>' attribute.
   * @see #getState_intercept()
   * @generated
   */
  void setState_intercept(String value);

  /**
   * Returns the value of the '<em><b>Obs</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Obs</em>' attribute.
   * @see #setObs(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Obs()
   * @model
   * @generated
   */
  String getObs();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getObs <em>Obs</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Obs</em>' attribute.
   * @see #getObs()
   * @generated
   */
  void setObs(String value);

  /**
   * Returns the value of the '<em><b>Obs offset</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Obs offset</em>' attribute.
   * @see #setObs_offset(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getStateSpaceModel_Obs_offset()
   * @model
   * @generated
   */
  String getObs_offset();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.StateSpaceModel#getObs_offset <em>Obs offset</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Obs offset</em>' attribute.
   * @see #getObs_offset()
   * @generated
   */
  void setObs_offset(String value);

} // StateSpaceModel
