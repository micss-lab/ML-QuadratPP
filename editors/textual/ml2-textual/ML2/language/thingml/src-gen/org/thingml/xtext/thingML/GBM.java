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
 * A representation of the model object '<em><b>GBM</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getLoss <em>Loss</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getLearning_rate <em>Learning rate</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getN_estimators <em>Nestimators</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getSubsample <em>Subsample</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getCriterion <em>Criterion</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getMin_samples_split <em>Min samples split</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getMin_samples_leaf <em>Min samples leaf</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getMin_weight_fraction_leaf <em>Min weight fraction leaf</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getMax_depth <em>Max depth</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getMin_impurity_decrease <em>Min impurity decrease</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getInit <em>Init</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getRandom_state <em>Random state</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getMax_features <em>Max features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getAlpha <em>Alpha</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getVerbose <em>Verbose</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getMax_leaf_nodes <em>Max leaf nodes</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getWarm_start <em>Warm start</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.GBM#getPresort <em>Presort</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM()
 * @model
 * @generated
 */
public interface GBM extends machine_learning_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Loss</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Loss</em>' attribute.
   * @see #setLoss(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Loss()
   * @model
   * @generated
   */
  String getLoss();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getLoss <em>Loss</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Loss</em>' attribute.
   * @see #getLoss()
   * @generated
   */
  void setLoss(String value);

  /**
   * Returns the value of the '<em><b>Learning rate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Learning rate</em>' attribute.
   * @see #setLearning_rate(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Learning_rate()
   * @model
   * @generated
   */
  double getLearning_rate();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getLearning_rate <em>Learning rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Learning rate</em>' attribute.
   * @see #getLearning_rate()
   * @generated
   */
  void setLearning_rate(double value);

  /**
   * Returns the value of the '<em><b>Nestimators</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Nestimators</em>' containment reference.
   * @see #setN_estimators(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_N_estimators()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getN_estimators();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getN_estimators <em>Nestimators</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Nestimators</em>' containment reference.
   * @see #getN_estimators()
   * @generated
   */
  void setN_estimators(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Subsample</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subsample</em>' attribute.
   * @see #setSubsample(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Subsample()
   * @model
   * @generated
   */
  double getSubsample();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getSubsample <em>Subsample</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Subsample</em>' attribute.
   * @see #getSubsample()
   * @generated
   */
  void setSubsample(double value);

  /**
   * Returns the value of the '<em><b>Criterion</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Criterion</em>' attribute.
   * @see #setCriterion(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Criterion()
   * @model
   * @generated
   */
  String getCriterion();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getCriterion <em>Criterion</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Criterion</em>' attribute.
   * @see #getCriterion()
   * @generated
   */
  void setCriterion(String value);

  /**
   * Returns the value of the '<em><b>Min samples split</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Min samples split</em>' containment reference.
   * @see #setMin_samples_split(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Min_samples_split()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getMin_samples_split();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getMin_samples_split <em>Min samples split</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Min samples split</em>' containment reference.
   * @see #getMin_samples_split()
   * @generated
   */
  void setMin_samples_split(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Min samples leaf</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Min samples leaf</em>' containment reference.
   * @see #setMin_samples_leaf(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Min_samples_leaf()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getMin_samples_leaf();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getMin_samples_leaf <em>Min samples leaf</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Min samples leaf</em>' containment reference.
   * @see #getMin_samples_leaf()
   * @generated
   */
  void setMin_samples_leaf(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Min weight fraction leaf</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Min weight fraction leaf</em>' attribute.
   * @see #setMin_weight_fraction_leaf(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Min_weight_fraction_leaf()
   * @model
   * @generated
   */
  double getMin_weight_fraction_leaf();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getMin_weight_fraction_leaf <em>Min weight fraction leaf</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Min weight fraction leaf</em>' attribute.
   * @see #getMin_weight_fraction_leaf()
   * @generated
   */
  void setMin_weight_fraction_leaf(double value);

  /**
   * Returns the value of the '<em><b>Max depth</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Max depth</em>' containment reference.
   * @see #setMax_depth(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Max_depth()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getMax_depth();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getMax_depth <em>Max depth</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Max depth</em>' containment reference.
   * @see #getMax_depth()
   * @generated
   */
  void setMax_depth(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Min impurity decrease</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Min impurity decrease</em>' attribute.
   * @see #setMin_impurity_decrease(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Min_impurity_decrease()
   * @model
   * @generated
   */
  double getMin_impurity_decrease();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getMin_impurity_decrease <em>Min impurity decrease</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Min impurity decrease</em>' attribute.
   * @see #getMin_impurity_decrease()
   * @generated
   */
  void setMin_impurity_decrease(double value);

  /**
   * Returns the value of the '<em><b>Init</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Init</em>' attribute.
   * @see #setInit(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Init()
   * @model
   * @generated
   */
  String getInit();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getInit <em>Init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Init</em>' attribute.
   * @see #getInit()
   * @generated
   */
  void setInit(String value);

  /**
   * Returns the value of the '<em><b>Random state</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Random state</em>' containment reference.
   * @see #setRandom_state(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Random_state()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getRandom_state();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getRandom_state <em>Random state</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Random state</em>' containment reference.
   * @see #getRandom_state()
   * @generated
   */
  void setRandom_state(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Max features</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Max features</em>' attribute.
   * @see #setMax_features(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Max_features()
   * @model
   * @generated
   */
  String getMax_features();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getMax_features <em>Max features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Max features</em>' attribute.
   * @see #getMax_features()
   * @generated
   */
  void setMax_features(String value);

  /**
   * Returns the value of the '<em><b>Alpha</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Alpha</em>' attribute.
   * @see #setAlpha(double)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Alpha()
   * @model
   * @generated
   */
  double getAlpha();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getAlpha <em>Alpha</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Alpha</em>' attribute.
   * @see #getAlpha()
   * @generated
   */
  void setAlpha(double value);

  /**
   * Returns the value of the '<em><b>Verbose</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Verbose</em>' containment reference.
   * @see #setVerbose(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Verbose()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getVerbose();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getVerbose <em>Verbose</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Verbose</em>' containment reference.
   * @see #getVerbose()
   * @generated
   */
  void setVerbose(BooleanLiteral value);

  /**
   * Returns the value of the '<em><b>Max leaf nodes</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Max leaf nodes</em>' containment reference.
   * @see #setMax_leaf_nodes(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Max_leaf_nodes()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getMax_leaf_nodes();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getMax_leaf_nodes <em>Max leaf nodes</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Max leaf nodes</em>' containment reference.
   * @see #getMax_leaf_nodes()
   * @generated
   */
  void setMax_leaf_nodes(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Warm start</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Warm start</em>' containment reference.
   * @see #setWarm_start(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Warm_start()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getWarm_start();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getWarm_start <em>Warm start</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Warm start</em>' containment reference.
   * @see #getWarm_start()
   * @generated
   */
  void setWarm_start(BooleanLiteral value);

  /**
   * Returns the value of the '<em><b>Presort</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Presort</em>' containment reference.
   * @see #setPresort(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getGBM_Presort()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getPresort();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.GBM#getPresort <em>Presort</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Presort</em>' containment reference.
   * @see #getPresort()
   * @generated
   */
  void setPresort(BooleanLiteral value);

} // GBM
