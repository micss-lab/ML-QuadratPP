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
 * A representation of the model object '<em><b>Label Spreading</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.LabelSpreading#getKernel <em>Kernel</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.LabelSpreading#getGamma <em>Gamma</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.LabelSpreading#getN_neighbors <em>Nneighbors</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.LabelSpreading#getAlpha <em>Alpha</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.LabelSpreading#getMax_iter <em>Max iter</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.LabelSpreading#getTol <em>Tol</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.LabelSpreading#getN_jobs <em>Njobs</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getLabelSpreading()
 * @model
 * @generated
 */
public interface LabelSpreading extends ML2_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Kernel</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Label_Propagation_Kernel}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Kernel</em>' attribute.
   * @see org.thingml.xtext.thingML.Label_Propagation_Kernel
   * @see #setKernel(Label_Propagation_Kernel)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getLabelSpreading_Kernel()
   * @model
   * @generated
   */
  Label_Propagation_Kernel getKernel();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.LabelSpreading#getKernel <em>Kernel</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Kernel</em>' attribute.
   * @see org.thingml.xtext.thingML.Label_Propagation_Kernel
   * @see #getKernel()
   * @generated
   */
  void setKernel(Label_Propagation_Kernel value);

  /**
   * Returns the value of the '<em><b>Gamma</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Gamma</em>' containment reference.
   * @see #setGamma(DoubleLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getLabelSpreading_Gamma()
   * @model containment="true"
   * @generated
   */
  DoubleLiteral getGamma();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.LabelSpreading#getGamma <em>Gamma</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Gamma</em>' containment reference.
   * @see #getGamma()
   * @generated
   */
  void setGamma(DoubleLiteral value);

  /**
   * Returns the value of the '<em><b>Nneighbors</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Nneighbors</em>' containment reference.
   * @see #setN_neighbors(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getLabelSpreading_N_neighbors()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getN_neighbors();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.LabelSpreading#getN_neighbors <em>Nneighbors</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Nneighbors</em>' containment reference.
   * @see #getN_neighbors()
   * @generated
   */
  void setN_neighbors(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Alpha</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Alpha</em>' containment reference.
   * @see #setAlpha(DoubleLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getLabelSpreading_Alpha()
   * @model containment="true"
   * @generated
   */
  DoubleLiteral getAlpha();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.LabelSpreading#getAlpha <em>Alpha</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Alpha</em>' containment reference.
   * @see #getAlpha()
   * @generated
   */
  void setAlpha(DoubleLiteral value);

  /**
   * Returns the value of the '<em><b>Max iter</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Max iter</em>' containment reference.
   * @see #setMax_iter(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getLabelSpreading_Max_iter()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getMax_iter();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.LabelSpreading#getMax_iter <em>Max iter</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Max iter</em>' containment reference.
   * @see #getMax_iter()
   * @generated
   */
  void setMax_iter(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Tol</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tol</em>' containment reference.
   * @see #setTol(DoubleLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getLabelSpreading_Tol()
   * @model containment="true"
   * @generated
   */
  DoubleLiteral getTol();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.LabelSpreading#getTol <em>Tol</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Tol</em>' containment reference.
   * @see #getTol()
   * @generated
   */
  void setTol(DoubleLiteral value);

  /**
   * Returns the value of the '<em><b>Njobs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Njobs</em>' containment reference.
   * @see #setN_jobs(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getLabelSpreading_N_jobs()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getN_jobs();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.LabelSpreading#getN_jobs <em>Njobs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Njobs</em>' containment reference.
   * @see #getN_jobs()
   * @generated
   */
  void setN_jobs(IntegerLiteral value);

} // LabelSpreading
