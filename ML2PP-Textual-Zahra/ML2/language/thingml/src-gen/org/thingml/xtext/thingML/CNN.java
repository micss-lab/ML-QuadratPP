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
 * A representation of the model object '<em><b>CNN</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getUnits <em>Units</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getHidden_layer_sizes <em>Hidden layer sizes</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getHidden_layers <em>Hidden layers</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getInput_activation <em>Input activation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getHidden_activation <em>Hidden activation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getOutput_activation <em>Output activation</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getRegularization <em>Regularization</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getDropout <em>Dropout</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.CNN#getRate <em>Rate</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN()
 * @model
 * @generated
 */
public interface CNN extends Deep_learning_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Units</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Units</em>' containment reference.
   * @see #setUnits(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Units()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getUnits();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.CNN#getUnits <em>Units</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Units</em>' containment reference.
   * @see #getUnits()
   * @generated
   */
  void setUnits(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Hidden layer sizes</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hidden layer sizes</em>' attribute.
   * @see #setHidden_layer_sizes(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Hidden_layer_sizes()
   * @model
   * @generated
   */
  String getHidden_layer_sizes();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.CNN#getHidden_layer_sizes <em>Hidden layer sizes</em>}' attribute.
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
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Hidden_layers()
   * @model containment="true"
   * @generated
   */
  EList<IntegerLiteral> getHidden_layers();

  /**
   * Returns the value of the '<em><b>Input activation</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Activation}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Input activation</em>' attribute.
   * @see org.thingml.xtext.thingML.Activation
   * @see #setInput_activation(Activation)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Input_activation()
   * @model
   * @generated
   */
  Activation getInput_activation();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.CNN#getInput_activation <em>Input activation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Input activation</em>' attribute.
   * @see org.thingml.xtext.thingML.Activation
   * @see #getInput_activation()
   * @generated
   */
  void setInput_activation(Activation value);

  /**
   * Returns the value of the '<em><b>Hidden activation</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Activation}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hidden activation</em>' attribute.
   * @see org.thingml.xtext.thingML.Activation
   * @see #setHidden_activation(Activation)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Hidden_activation()
   * @model
   * @generated
   */
  Activation getHidden_activation();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.CNN#getHidden_activation <em>Hidden activation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hidden activation</em>' attribute.
   * @see org.thingml.xtext.thingML.Activation
   * @see #getHidden_activation()
   * @generated
   */
  void setHidden_activation(Activation value);

  /**
   * Returns the value of the '<em><b>Output activation</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Activation}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Output activation</em>' attribute.
   * @see org.thingml.xtext.thingML.Activation
   * @see #setOutput_activation(Activation)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Output_activation()
   * @model
   * @generated
   */
  Activation getOutput_activation();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.CNN#getOutput_activation <em>Output activation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Output activation</em>' attribute.
   * @see org.thingml.xtext.thingML.Activation
   * @see #getOutput_activation()
   * @generated
   */
  void setOutput_activation(Activation value);

  /**
   * Returns the value of the '<em><b>Regularization</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Regularization}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Regularization</em>' attribute.
   * @see org.thingml.xtext.thingML.Regularization
   * @see #setRegularization(Regularization)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Regularization()
   * @model
   * @generated
   */
  Regularization getRegularization();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.CNN#getRegularization <em>Regularization</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Regularization</em>' attribute.
   * @see org.thingml.xtext.thingML.Regularization
   * @see #getRegularization()
   * @generated
   */
  void setRegularization(Regularization value);

  /**
   * Returns the value of the '<em><b>Dropout</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Dropout</em>' attribute.
   * @see #setDropout(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Dropout()
   * @model
   * @generated
   */
  double getDropout();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.CNN#getDropout <em>Dropout</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Dropout</em>' attribute.
   * @see #getDropout()
   * @generated
   */
  void setDropout(double value);

  /**
   * Returns the value of the '<em><b>Rate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rate</em>' attribute.
   * @see #setRate(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getCNN_Rate()
   * @model
   * @generated
   */
  double getRate();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.CNN#getRate <em>Rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rate</em>' attribute.
   * @see #getRate()
   * @generated
   */
  void setRate(double value);

} // CNN
