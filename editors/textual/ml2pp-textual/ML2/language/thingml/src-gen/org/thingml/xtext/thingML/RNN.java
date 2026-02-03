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
 * A representation of the model object '<em><b>RNN</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.RNN#getUnits <em>Units</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.RNN#getActivation <em>Activation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.RNN#getReturn_sequences <em>Return sequences</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.RNN#getRegularization <em>Regularization</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.RNN#getHidden_layer_sizes <em>Hidden layer sizes</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.RNN#getHidden_layers <em>Hidden layers</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.RNN#getRate <em>Rate</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getRNN()
 * @model
 * @generated
 */
public interface RNN extends Deep_learning_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Units</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Units</em>' containment reference.
   * @see #setUnits(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getRNN_Units()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getUnits();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.RNN#getUnits <em>Units</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Units</em>' containment reference.
   * @see #getUnits()
   * @generated
   */
  void setUnits(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Activation</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Activation}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Activation</em>' attribute.
   * @see org.thingml.xtext.thingML.Activation
   * @see #setActivation(Activation)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getRNN_Activation()
   * @model
   * @generated
   */
  Activation getActivation();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.RNN#getActivation <em>Activation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Activation</em>' attribute.
   * @see org.thingml.xtext.thingML.Activation
   * @see #getActivation()
   * @generated
   */
  void setActivation(Activation value);

  /**
   * Returns the value of the '<em><b>Return sequences</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Return sequences</em>' containment reference.
   * @see #setReturn_sequences(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getRNN_Return_sequences()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getReturn_sequences();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.RNN#getReturn_sequences <em>Return sequences</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Return sequences</em>' containment reference.
   * @see #getReturn_sequences()
   * @generated
   */
  void setReturn_sequences(BooleanLiteral value);

  /**
   * Returns the value of the '<em><b>Regularization</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Regularization}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Regularization</em>' attribute.
   * @see org.thingml.xtext.thingML.Regularization
   * @see #setRegularization(Regularization)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getRNN_Regularization()
   * @model
   * @generated
   */
  Regularization getRegularization();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.RNN#getRegularization <em>Regularization</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Regularization</em>' attribute.
   * @see org.thingml.xtext.thingML.Regularization
   * @see #getRegularization()
   * @generated
   */
  void setRegularization(Regularization value);

  /**
   * Returns the value of the '<em><b>Hidden layer sizes</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hidden layer sizes</em>' attribute.
   * @see #setHidden_layer_sizes(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getRNN_Hidden_layer_sizes()
   * @model
   * @generated
   */
  String getHidden_layer_sizes();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.RNN#getHidden_layer_sizes <em>Hidden layer sizes</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hidden layer sizes</em>' attribute.
   * @see #getHidden_layer_sizes()
   * @generated
   */
  void setHidden_layer_sizes(String value);

  /**
   * Returns the value of the '<em><b>Hidden layers</b></em>' containment reference list.
   * The list contents are of type {@link org.thingml.xtext.thingML.IntegerLiteral}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hidden layers</em>' containment reference list.
   * @see org.thingml.xtext.thingML.ThingMLPackage#getRNN_Hidden_layers()
   * @model containment="true"
   * @generated
   */
  EList<IntegerLiteral> getHidden_layers();

  /**
   * Returns the value of the '<em><b>Rate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rate</em>' attribute.
   * @see #setRate(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getRNN_Rate()
   * @model
   * @generated
   */
  double getRate();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.RNN#getRate <em>Rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rate</em>' attribute.
   * @see #getRate()
   * @generated
   */
  void setRate(double value);

} // RNN
