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
 * A representation of the model object '<em><b>Spectral Clustering</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getN_clusters <em>Nclusters</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getEigen_solver <em>Eigen solver</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getN_components <em>Ncomponents</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getRandom_state <em>Random state</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getN_init <em>Ninit</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getGamma <em>Gamma</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getAffinity <em>Affinity</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getN_neighbors <em>Nneighbors</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getEigen_tol <em>Eigen tol</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getAssign_labels <em>Assign labels</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getDegree <em>Degree</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getCoef0 <em>Coef0</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getKernel_params <em>Kernel params</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getN_jobs <em>Njobs</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.SpectralClustering#getVerbose <em>Verbose</em>}</li>
 * </ul>
 *
 * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering()
 * @model
 * @generated
 */
public interface SpectralClustering extends ML2_ModelAlgorithm
{
  /**
   * Returns the value of the '<em><b>Nclusters</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Nclusters</em>' containment reference.
   * @see #setN_clusters(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_N_clusters()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getN_clusters();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getN_clusters <em>Nclusters</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Nclusters</em>' containment reference.
   * @see #getN_clusters()
   * @generated
   */
  void setN_clusters(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Eigen solver</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Eigen_Solver}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Eigen solver</em>' attribute.
   * @see org.thingml.xtext.thingML.Eigen_Solver
   * @see #setEigen_solver(Eigen_Solver)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Eigen_solver()
   * @model
   * @generated
   */
  Eigen_Solver getEigen_solver();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getEigen_solver <em>Eigen solver</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Eigen solver</em>' attribute.
   * @see org.thingml.xtext.thingML.Eigen_Solver
   * @see #getEigen_solver()
   * @generated
   */
  void setEigen_solver(Eigen_Solver value);

  /**
   * Returns the value of the '<em><b>Ncomponents</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ncomponents</em>' containment reference.
   * @see #setN_components(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_N_components()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getN_components();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getN_components <em>Ncomponents</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ncomponents</em>' containment reference.
   * @see #getN_components()
   * @generated
   */
  void setN_components(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Random state</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Random state</em>' containment reference.
   * @see #setRandom_state(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Random_state()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getRandom_state();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getRandom_state <em>Random state</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Random state</em>' containment reference.
   * @see #getRandom_state()
   * @generated
   */
  void setRandom_state(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Ninit</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ninit</em>' containment reference.
   * @see #setN_init(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_N_init()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getN_init();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getN_init <em>Ninit</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ninit</em>' containment reference.
   * @see #getN_init()
   * @generated
   */
  void setN_init(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Gamma</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Gamma</em>' containment reference.
   * @see #setGamma(DoubleLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Gamma()
   * @model containment="true"
   * @generated
   */
  DoubleLiteral getGamma();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getGamma <em>Gamma</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Gamma</em>' containment reference.
   * @see #getGamma()
   * @generated
   */
  void setGamma(DoubleLiteral value);

  /**
   * Returns the value of the '<em><b>Affinity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Affinity</em>' attribute.
   * @see #setAffinity(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Affinity()
   * @model
   * @generated
   */
  String getAffinity();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getAffinity <em>Affinity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Affinity</em>' attribute.
   * @see #getAffinity()
   * @generated
   */
  void setAffinity(String value);

  /**
   * Returns the value of the '<em><b>Nneighbors</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Nneighbors</em>' containment reference.
   * @see #setN_neighbors(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_N_neighbors()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getN_neighbors();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getN_neighbors <em>Nneighbors</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Nneighbors</em>' containment reference.
   * @see #getN_neighbors()
   * @generated
   */
  void setN_neighbors(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Eigen tol</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Eigen tol</em>' containment reference.
   * @see #setEigen_tol(DoubleLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Eigen_tol()
   * @model containment="true"
   * @generated
   */
  DoubleLiteral getEigen_tol();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getEigen_tol <em>Eigen tol</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Eigen tol</em>' containment reference.
   * @see #getEigen_tol()
   * @generated
   */
  void setEigen_tol(DoubleLiteral value);

  /**
   * Returns the value of the '<em><b>Assign labels</b></em>' attribute.
   * The literals are from the enumeration {@link org.thingml.xtext.thingML.Laplacian_Embedding_Label_Assign_Strategy}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Assign labels</em>' attribute.
   * @see org.thingml.xtext.thingML.Laplacian_Embedding_Label_Assign_Strategy
   * @see #setAssign_labels(Laplacian_Embedding_Label_Assign_Strategy)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Assign_labels()
   * @model
   * @generated
   */
  Laplacian_Embedding_Label_Assign_Strategy getAssign_labels();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getAssign_labels <em>Assign labels</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Assign labels</em>' attribute.
   * @see org.thingml.xtext.thingML.Laplacian_Embedding_Label_Assign_Strategy
   * @see #getAssign_labels()
   * @generated
   */
  void setAssign_labels(Laplacian_Embedding_Label_Assign_Strategy value);

  /**
   * Returns the value of the '<em><b>Degree</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Degree</em>' containment reference.
   * @see #setDegree(DoubleLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Degree()
   * @model containment="true"
   * @generated
   */
  DoubleLiteral getDegree();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getDegree <em>Degree</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Degree</em>' containment reference.
   * @see #getDegree()
   * @generated
   */
  void setDegree(DoubleLiteral value);

  /**
   * Returns the value of the '<em><b>Coef0</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Coef0</em>' containment reference.
   * @see #setCoef0(DoubleLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Coef0()
   * @model containment="true"
   * @generated
   */
  DoubleLiteral getCoef0();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getCoef0 <em>Coef0</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Coef0</em>' containment reference.
   * @see #getCoef0()
   * @generated
   */
  void setCoef0(DoubleLiteral value);

  /**
   * Returns the value of the '<em><b>Kernel params</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Kernel params</em>' attribute.
   * @see #setKernel_params(String)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Kernel_params()
   * @model
   * @generated
   */
  String getKernel_params();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getKernel_params <em>Kernel params</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Kernel params</em>' attribute.
   * @see #getKernel_params()
   * @generated
   */
  void setKernel_params(String value);

  /**
   * Returns the value of the '<em><b>Njobs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Njobs</em>' containment reference.
   * @see #setN_jobs(IntegerLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_N_jobs()
   * @model containment="true"
   * @generated
   */
  IntegerLiteral getN_jobs();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getN_jobs <em>Njobs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Njobs</em>' containment reference.
   * @see #getN_jobs()
   * @generated
   */
  void setN_jobs(IntegerLiteral value);

  /**
   * Returns the value of the '<em><b>Verbose</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Verbose</em>' containment reference.
   * @see #setVerbose(BooleanLiteral)
   * @see org.thingml.xtext.thingML.ThingMLPackage#getSpectralClustering_Verbose()
   * @model containment="true"
   * @generated
   */
  BooleanLiteral getVerbose();

  /**
   * Sets the value of the '{@link org.thingml.xtext.thingML.SpectralClustering#getVerbose <em>Verbose</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Verbose</em>' containment reference.
   * @see #getVerbose()
   * @generated
   */
  void setVerbose(BooleanLiteral value);

} // SpectralClustering
