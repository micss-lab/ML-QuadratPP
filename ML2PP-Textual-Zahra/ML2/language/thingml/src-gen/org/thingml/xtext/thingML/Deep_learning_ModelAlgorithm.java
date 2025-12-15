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
 * A representation of the model object '<em><b>Deep learning Model Algorithm</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getOptimizer <em>Optimizer</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getBatch_size <em>Batch size</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getEpochs <em>Epochs</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getMetrics <em>Metrics</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getEarly_stopping <em>Early stopping</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getOverfitting_Plots <em>Overfitting Plots</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getDeep_learning_ModelAlgorithm()
 * @model
 * @generated
 */
public interface Deep_learning_ModelAlgorithm extends Time_series_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Optimizer</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Optimizer}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Optimizer</em>' attribute.
   * @see org.thingml.xtext.thingML.Optimizer
   * @see #setOptimizer(Optimizer)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDeep_learning_ModelAlgorithm_Optimizer()
   * @model
   * @generated
   */
  Optimizer getOptimizer();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getOptimizer <em>Optimizer</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Optimizer</em>' attribute.
   * @see org.thingml.xtext.thingML.Optimizer
   * @see #getOptimizer()
   * @generated
   */
  void setOptimizer(Optimizer value);

  /**
   * Returns the value of the '<em><b>Batch size</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Batch size</em>' containment reference.
   * @see #setBatch_size(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDeep_learning_ModelAlgorithm_Batch_size()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getBatch_size();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getBatch_size <em>Batch size</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Batch size</em>' containment reference.
   * @see #getBatch_size()
   * @generated
   */
  void setBatch_size(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Epochs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Epochs</em>' containment reference.
   * @see #setEpochs(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDeep_learning_ModelAlgorithm_Epochs()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getEpochs();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getEpochs <em>Epochs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Epochs</em>' containment reference.
   * @see #getEpochs()
   * @generated
   */
  void setEpochs(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Metrics</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Metrics}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Metrics</em>' attribute.
   * @see org.thingml.xtext.thingML.Metrics
   * @see #setMetrics(Metrics)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDeep_learning_ModelAlgorithm_Metrics()
   * @model
   * @generated
   */
  Metrics getMetrics();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getMetrics <em>Metrics</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Metrics</em>' attribute.
   * @see org.thingml.xtext.thingML.Metrics
   * @see #getMetrics()
   * @generated
   */
  void setMetrics(Metrics value);

  /**
   * Returns the value of the '<em><b>Early stopping</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.EarlyStopping}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Early stopping</em>' attribute.
   * @see org.thingml.xtext.thingML.EarlyStopping
   * @see #setEarly_stopping(EarlyStopping)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDeep_learning_ModelAlgorithm_Early_stopping()
   * @model
   * @generated
   */
  EarlyStopping getEarly_stopping();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getEarly_stopping <em>Early stopping</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Early stopping</em>' attribute.
   * @see org.thingml.xtext.thingML.EarlyStopping
   * @see #getEarly_stopping()
   * @generated
   */
  void setEarly_stopping(EarlyStopping value);

  /**
   * Returns the value of the '<em><b>Overfitting Plots</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.OverfittingPlots}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Overfitting Plots</em>' attribute.
   * @see org.thingml.xtext.thingML.OverfittingPlots
   * @see #setOverfitting_Plots(OverfittingPlots)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getDeep_learning_ModelAlgorithm_Overfitting_Plots()
   * @model
   * @generated
   */
  OverfittingPlots getOverfitting_Plots();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.Deep_learning_ModelAlgorithm#getOverfitting_Plots <em>Overfitting Plots</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Overfitting Plots</em>' attribute.
   * @see org.thingml.xtext.thingML.OverfittingPlots
   * @see #getOverfitting_Plots()
   * @generated
   */
  void setOverfitting_Plots(OverfittingPlots value);

} // Deep_learning_ModelAlgorithm
