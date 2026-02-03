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
 * A representation of the model object '<em><b>Self Training Classifier</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getBase_estimator <em>Base estimator</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getCriterion <em>Criterion</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getK_best <em>Kbest</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getMax_iter <em>Max iter</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getVerbose <em>Verbose</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getSelfTrainingClassifier()
 * @model
 * @generated
 */
public interface SelfTrainingClassifier extends ML2_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Base estimator</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Base estimator</em>' containment reference.
   * @see #setBase_estimator(ClassifierWithPredictionCertaintyProbabilities)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSelfTrainingClassifier_Base_estimator()
   * @model containment="true"
   * @generated
   */
  ClassifierWithPredictionCertaintyProbabilities getBase_estimator();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getBase_estimator <em>Base estimator</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Base estimator</em>' containment reference.
   * @see #getBase_estimator()
   * @generated
   */
  void setBase_estimator(ClassifierWithPredictionCertaintyProbabilities value);

  /**
   * Returns the value of the '<em><b>Threshold</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Threshold</em>' containment reference.
   * @see #setThreshold(DoubleLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSelfTrainingClassifier_Threshold()
   * @model containment="true"
   * @generated
   */
  DoubleLiteral getThreshold();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getThreshold <em>Threshold</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' containment reference.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(DoubleLiteral value);

  /**
   * Returns the value of the '<em><b>Criterion</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Self_Training_Criterion}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Criterion</em>' attribute.
   * @see org.thingml.xtext.thingML.Self_Training_Criterion
   * @see #setCriterion(Self_Training_Criterion)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSelfTrainingClassifier_Criterion()
   * @model
   * @generated
   */
  Self_Training_Criterion getCriterion();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getCriterion <em>Criterion</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Criterion</em>' attribute.
   * @see org.thingml.xtext.thingML.Self_Training_Criterion
   * @see #getCriterion()
   * @generated
   */
  void setCriterion(Self_Training_Criterion value);

  /**
   * Returns the value of the '<em><b>Kbest</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Kbest</em>' containment reference.
   * @see #setK_best(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSelfTrainingClassifier_K_best()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getK_best();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getK_best <em>Kbest</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Kbest</em>' containment reference.
   * @see #getK_best()
   * @generated
   */
  void setK_best(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Max iter</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Max iter</em>' containment reference.
   * @see #setMax_iter(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSelfTrainingClassifier_Max_iter()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getMax_iter();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getMax_iter <em>Max iter</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Max iter</em>' containment reference.
   * @see #getMax_iter()
   * @generated
   */
  void setMax_iter(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Verbose</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Verbose</em>' containment reference.
   * @see #setVerbose(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSelfTrainingClassifier_Verbose()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getVerbose();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SelfTrainingClassifier#getVerbose <em>Verbose</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Verbose</em>' containment reference.
   * @see #getVerbose()
   * @generated
   */
  void setVerbose(BooleanLiteral value);

} // SelfTrainingClassifier
