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
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.thingml.xtext.thingML.BooleanLiteral;
import org.thingml.xtext.thingML.DoubleLiteral;
import org.thingml.xtext.thingML.Eigen_Solver;
import org.thingml.xtext.thingML.IntegerLiteral;
import org.thingml.xtext.thingML.Laplacian_Embedding_Label_Assign_Strategy;
import org.thingml.xtext.thingML.SpectralClustering;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spectral Clustering</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getN_clusters <em>Nclusters</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getEigen_solver <em>Eigen solver</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getN_components <em>Ncomponents</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getRandom_state <em>Random state</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getN_init <em>Ninit</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getGamma <em>Gamma</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getAffinity <em>Affinity</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getN_neighbors <em>Nneighbors</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getEigen_tol <em>Eigen tol</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getAssign_labels <em>Assign labels</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getDegree <em>Degree</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getCoef0 <em>Coef0</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getKernel_params <em>Kernel params</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getN_jobs <em>Njobs</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.SpectralClusteringImpl#getVerbose <em>Verbose</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpectralClusteringImpl extends ML2_ModelAlgorithmImpl implements SpectralClustering
{
  /**
   * The cached value of the '{@link #getN_clusters() <em>Nclusters</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_clusters()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_clusters;

  /**
   * The default value of the '{@link #getEigen_solver() <em>Eigen solver</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEigen_solver()
   * @generated
   * @ordered
   */
  protected static final Eigen_Solver EIGEN_SOLVER_EDEFAULT = Eigen_Solver.NOT_SET;

  /**
   * The cached value of the '{@link #getEigen_solver() <em>Eigen solver</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEigen_solver()
   * @generated
   * @ordered
   */
  protected Eigen_Solver eigen_solver = EIGEN_SOLVER_EDEFAULT;

  /**
   * The cached value of the '{@link #getN_components() <em>Ncomponents</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_components()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_components;

  /**
   * The cached value of the '{@link #getRandom_state() <em>Random state</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRandom_state()
   * @generated
   * @ordered
   */
  protected IntegerLiteral random_state;

  /**
   * The cached value of the '{@link #getN_init() <em>Ninit</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_init()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_init;

  /**
   * The cached value of the '{@link #getGamma() <em>Gamma</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGamma()
   * @generated
   * @ordered
   */
  protected DoubleLiteral gamma;

  /**
   * The default value of the '{@link #getAffinity() <em>Affinity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAffinity()
   * @generated
   * @ordered
   */
  protected static final String AFFINITY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAffinity() <em>Affinity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAffinity()
   * @generated
   * @ordered
   */
  protected String affinity = AFFINITY_EDEFAULT;

  /**
   * The cached value of the '{@link #getN_neighbors() <em>Nneighbors</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_neighbors()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_neighbors;

  /**
   * The cached value of the '{@link #getEigen_tol() <em>Eigen tol</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEigen_tol()
   * @generated
   * @ordered
   */
  protected DoubleLiteral eigen_tol;

  /**
   * The default value of the '{@link #getAssign_labels() <em>Assign labels</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAssign_labels()
   * @generated
   * @ordered
   */
  protected static final Laplacian_Embedding_Label_Assign_Strategy ASSIGN_LABELS_EDEFAULT = Laplacian_Embedding_Label_Assign_Strategy.NOT_SET;

  /**
   * The cached value of the '{@link #getAssign_labels() <em>Assign labels</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAssign_labels()
   * @generated
   * @ordered
   */
  protected Laplacian_Embedding_Label_Assign_Strategy assign_labels = ASSIGN_LABELS_EDEFAULT;

  /**
   * The cached value of the '{@link #getDegree() <em>Degree</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDegree()
   * @generated
   * @ordered
   */
  protected DoubleLiteral degree;

  /**
   * The cached value of the '{@link #getCoef0() <em>Coef0</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCoef0()
   * @generated
   * @ordered
   */
  protected DoubleLiteral coef0;

  /**
   * The default value of the '{@link #getKernel_params() <em>Kernel params</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKernel_params()
   * @generated
   * @ordered
   */
  protected static final String KERNEL_PARAMS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getKernel_params() <em>Kernel params</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKernel_params()
   * @generated
   * @ordered
   */
  protected String kernel_params = KERNEL_PARAMS_EDEFAULT;

  /**
   * The cached value of the '{@link #getN_jobs() <em>Njobs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_jobs()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_jobs;

  /**
   * The cached value of the '{@link #getVerbose() <em>Verbose</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVerbose()
   * @generated
   * @ordered
   */
  protected BooleanLiteral verbose;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SpectralClusteringImpl()
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
    return ThingMLPackage.eINSTANCE.getSpectralClustering();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getN_clusters()
  {
    return n_clusters;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetN_clusters(IntegerLiteral newN_clusters, NotificationChain msgs)
  {
    IntegerLiteral oldN_clusters = n_clusters;
    n_clusters = newN_clusters;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS, oldN_clusters, newN_clusters);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setN_clusters(IntegerLiteral newN_clusters)
  {
    if (newN_clusters != n_clusters)
    {
      NotificationChain msgs = null;
      if (n_clusters != null)
        msgs = ((InternalEObject)n_clusters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS, null, msgs);
      if (newN_clusters != null)
        msgs = ((InternalEObject)newN_clusters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS, null, msgs);
      msgs = basicSetN_clusters(newN_clusters, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS, newN_clusters, newN_clusters));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Eigen_Solver getEigen_solver()
  {
    return eigen_solver;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setEigen_solver(Eigen_Solver newEigen_solver)
  {
    Eigen_Solver oldEigen_solver = eigen_solver;
    eigen_solver = newEigen_solver == null ? EIGEN_SOLVER_EDEFAULT : newEigen_solver;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_SOLVER, oldEigen_solver, eigen_solver));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getN_components()
  {
    return n_components;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetN_components(IntegerLiteral newN_components, NotificationChain msgs)
  {
    IntegerLiteral oldN_components = n_components;
    n_components = newN_components;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS, oldN_components, newN_components);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setN_components(IntegerLiteral newN_components)
  {
    if (newN_components != n_components)
    {
      NotificationChain msgs = null;
      if (n_components != null)
        msgs = ((InternalEObject)n_components).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS, null, msgs);
      if (newN_components != null)
        msgs = ((InternalEObject)newN_components).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS, null, msgs);
      msgs = basicSetN_components(newN_components, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS, newN_components, newN_components));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getRandom_state()
  {
    return random_state;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetRandom_state(IntegerLiteral newRandom_state, NotificationChain msgs)
  {
    IntegerLiteral oldRandom_state = random_state;
    random_state = newRandom_state;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE, oldRandom_state, newRandom_state);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRandom_state(IntegerLiteral newRandom_state)
  {
    if (newRandom_state != random_state)
    {
      NotificationChain msgs = null;
      if (random_state != null)
        msgs = ((InternalEObject)random_state).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE, null, msgs);
      if (newRandom_state != null)
        msgs = ((InternalEObject)newRandom_state).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE, null, msgs);
      msgs = basicSetRandom_state(newRandom_state, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE, newRandom_state, newRandom_state));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getN_init()
  {
    return n_init;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetN_init(IntegerLiteral newN_init, NotificationChain msgs)
  {
    IntegerLiteral oldN_init = n_init;
    n_init = newN_init;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NINIT, oldN_init, newN_init);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setN_init(IntegerLiteral newN_init)
  {
    if (newN_init != n_init)
    {
      NotificationChain msgs = null;
      if (n_init != null)
        msgs = ((InternalEObject)n_init).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NINIT, null, msgs);
      if (newN_init != null)
        msgs = ((InternalEObject)newN_init).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NINIT, null, msgs);
      msgs = basicSetN_init(newN_init, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NINIT, newN_init, newN_init));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getGamma()
  {
    return gamma;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetGamma(DoubleLiteral newGamma, NotificationChain msgs)
  {
    DoubleLiteral oldGamma = gamma;
    gamma = newGamma;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA, oldGamma, newGamma);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setGamma(DoubleLiteral newGamma)
  {
    if (newGamma != gamma)
    {
      NotificationChain msgs = null;
      if (gamma != null)
        msgs = ((InternalEObject)gamma).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA, null, msgs);
      if (newGamma != null)
        msgs = ((InternalEObject)newGamma).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA, null, msgs);
      msgs = basicSetGamma(newGamma, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA, newGamma, newGamma));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getAffinity()
  {
    return affinity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setAffinity(String newAffinity)
  {
    String oldAffinity = affinity;
    affinity = newAffinity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__AFFINITY, oldAffinity, affinity));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getN_neighbors()
  {
    return n_neighbors;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetN_neighbors(IntegerLiteral newN_neighbors, NotificationChain msgs)
  {
    IntegerLiteral oldN_neighbors = n_neighbors;
    n_neighbors = newN_neighbors;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS, oldN_neighbors, newN_neighbors);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setN_neighbors(IntegerLiteral newN_neighbors)
  {
    if (newN_neighbors != n_neighbors)
    {
      NotificationChain msgs = null;
      if (n_neighbors != null)
        msgs = ((InternalEObject)n_neighbors).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS, null, msgs);
      if (newN_neighbors != null)
        msgs = ((InternalEObject)newN_neighbors).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS, null, msgs);
      msgs = basicSetN_neighbors(newN_neighbors, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS, newN_neighbors, newN_neighbors));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getEigen_tol()
  {
    return eigen_tol;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEigen_tol(DoubleLiteral newEigen_tol, NotificationChain msgs)
  {
    DoubleLiteral oldEigen_tol = eigen_tol;
    eigen_tol = newEigen_tol;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL, oldEigen_tol, newEigen_tol);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setEigen_tol(DoubleLiteral newEigen_tol)
  {
    if (newEigen_tol != eigen_tol)
    {
      NotificationChain msgs = null;
      if (eigen_tol != null)
        msgs = ((InternalEObject)eigen_tol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL, null, msgs);
      if (newEigen_tol != null)
        msgs = ((InternalEObject)newEigen_tol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL, null, msgs);
      msgs = basicSetEigen_tol(newEigen_tol, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL, newEigen_tol, newEigen_tol));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Laplacian_Embedding_Label_Assign_Strategy getAssign_labels()
  {
    return assign_labels;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setAssign_labels(Laplacian_Embedding_Label_Assign_Strategy newAssign_labels)
  {
    Laplacian_Embedding_Label_Assign_Strategy oldAssign_labels = assign_labels;
    assign_labels = newAssign_labels == null ? ASSIGN_LABELS_EDEFAULT : newAssign_labels;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__ASSIGN_LABELS, oldAssign_labels, assign_labels));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getDegree()
  {
    return degree;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDegree(DoubleLiteral newDegree, NotificationChain msgs)
  {
    DoubleLiteral oldDegree = degree;
    degree = newDegree;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE, oldDegree, newDegree);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setDegree(DoubleLiteral newDegree)
  {
    if (newDegree != degree)
    {
      NotificationChain msgs = null;
      if (degree != null)
        msgs = ((InternalEObject)degree).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE, null, msgs);
      if (newDegree != null)
        msgs = ((InternalEObject)newDegree).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE, null, msgs);
      msgs = basicSetDegree(newDegree, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE, newDegree, newDegree));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getCoef0()
  {
    return coef0;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCoef0(DoubleLiteral newCoef0, NotificationChain msgs)
  {
    DoubleLiteral oldCoef0 = coef0;
    coef0 = newCoef0;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__COEF0, oldCoef0, newCoef0);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCoef0(DoubleLiteral newCoef0)
  {
    if (newCoef0 != coef0)
    {
      NotificationChain msgs = null;
      if (coef0 != null)
        msgs = ((InternalEObject)coef0).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__COEF0, null, msgs);
      if (newCoef0 != null)
        msgs = ((InternalEObject)newCoef0).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__COEF0, null, msgs);
      msgs = basicSetCoef0(newCoef0, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__COEF0, newCoef0, newCoef0));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getKernel_params()
  {
    return kernel_params;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setKernel_params(String newKernel_params)
  {
    String oldKernel_params = kernel_params;
    kernel_params = newKernel_params;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__KERNEL_PARAMS, oldKernel_params, kernel_params));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getN_jobs()
  {
    return n_jobs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetN_jobs(IntegerLiteral newN_jobs, NotificationChain msgs)
  {
    IntegerLiteral oldN_jobs = n_jobs;
    n_jobs = newN_jobs;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS, oldN_jobs, newN_jobs);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setN_jobs(IntegerLiteral newN_jobs)
  {
    if (newN_jobs != n_jobs)
    {
      NotificationChain msgs = null;
      if (n_jobs != null)
        msgs = ((InternalEObject)n_jobs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS, null, msgs);
      if (newN_jobs != null)
        msgs = ((InternalEObject)newN_jobs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS, null, msgs);
      msgs = basicSetN_jobs(newN_jobs, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS, newN_jobs, newN_jobs));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BooleanLiteral getVerbose()
  {
    return verbose;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVerbose(BooleanLiteral newVerbose, NotificationChain msgs)
  {
    BooleanLiteral oldVerbose = verbose;
    verbose = newVerbose;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE, oldVerbose, newVerbose);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVerbose(BooleanLiteral newVerbose)
  {
    if (newVerbose != verbose)
    {
      NotificationChain msgs = null;
      if (verbose != null)
        msgs = ((InternalEObject)verbose).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE, null, msgs);
      if (newVerbose != null)
        msgs = ((InternalEObject)newVerbose).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE, null, msgs);
      msgs = basicSetVerbose(newVerbose, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE, newVerbose, newVerbose));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS:
        return basicSetN_clusters(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS:
        return basicSetN_components(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE:
        return basicSetRandom_state(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__NINIT:
        return basicSetN_init(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA:
        return basicSetGamma(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS:
        return basicSetN_neighbors(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL:
        return basicSetEigen_tol(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE:
        return basicSetDegree(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__COEF0:
        return basicSetCoef0(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS:
        return basicSetN_jobs(null, msgs);
      case ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE:
        return basicSetVerbose(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS:
        return getN_clusters();
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_SOLVER:
        return getEigen_solver();
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS:
        return getN_components();
      case ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE:
        return getRandom_state();
      case ThingMLPackage.SPECTRAL_CLUSTERING__NINIT:
        return getN_init();
      case ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA:
        return getGamma();
      case ThingMLPackage.SPECTRAL_CLUSTERING__AFFINITY:
        return getAffinity();
      case ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS:
        return getN_neighbors();
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL:
        return getEigen_tol();
      case ThingMLPackage.SPECTRAL_CLUSTERING__ASSIGN_LABELS:
        return getAssign_labels();
      case ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE:
        return getDegree();
      case ThingMLPackage.SPECTRAL_CLUSTERING__COEF0:
        return getCoef0();
      case ThingMLPackage.SPECTRAL_CLUSTERING__KERNEL_PARAMS:
        return getKernel_params();
      case ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS:
        return getN_jobs();
      case ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE:
        return getVerbose();
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
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS:
        setN_clusters((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_SOLVER:
        setEigen_solver((Eigen_Solver)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS:
        setN_components((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE:
        setRandom_state((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NINIT:
        setN_init((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA:
        setGamma((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__AFFINITY:
        setAffinity((String)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS:
        setN_neighbors((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL:
        setEigen_tol((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__ASSIGN_LABELS:
        setAssign_labels((Laplacian_Embedding_Label_Assign_Strategy)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE:
        setDegree((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__COEF0:
        setCoef0((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__KERNEL_PARAMS:
        setKernel_params((String)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS:
        setN_jobs((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE:
        setVerbose((BooleanLiteral)newValue);
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
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS:
        setN_clusters((IntegerLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_SOLVER:
        setEigen_solver(EIGEN_SOLVER_EDEFAULT);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS:
        setN_components((IntegerLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE:
        setRandom_state((IntegerLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NINIT:
        setN_init((IntegerLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA:
        setGamma((DoubleLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__AFFINITY:
        setAffinity(AFFINITY_EDEFAULT);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS:
        setN_neighbors((IntegerLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL:
        setEigen_tol((DoubleLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__ASSIGN_LABELS:
        setAssign_labels(ASSIGN_LABELS_EDEFAULT);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE:
        setDegree((DoubleLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__COEF0:
        setCoef0((DoubleLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__KERNEL_PARAMS:
        setKernel_params(KERNEL_PARAMS_EDEFAULT);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS:
        setN_jobs((IntegerLiteral)null);
        return;
      case ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE:
        setVerbose((BooleanLiteral)null);
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
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCLUSTERS:
        return n_clusters != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_SOLVER:
        return eigen_solver != EIGEN_SOLVER_EDEFAULT;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NCOMPONENTS:
        return n_components != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__RANDOM_STATE:
        return random_state != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__NINIT:
        return n_init != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__GAMMA:
        return gamma != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__AFFINITY:
        return AFFINITY_EDEFAULT == null ? affinity != null : !AFFINITY_EDEFAULT.equals(affinity);
      case ThingMLPackage.SPECTRAL_CLUSTERING__NNEIGHBORS:
        return n_neighbors != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__EIGEN_TOL:
        return eigen_tol != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__ASSIGN_LABELS:
        return assign_labels != ASSIGN_LABELS_EDEFAULT;
      case ThingMLPackage.SPECTRAL_CLUSTERING__DEGREE:
        return degree != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__COEF0:
        return coef0 != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__KERNEL_PARAMS:
        return KERNEL_PARAMS_EDEFAULT == null ? kernel_params != null : !KERNEL_PARAMS_EDEFAULT.equals(kernel_params);
      case ThingMLPackage.SPECTRAL_CLUSTERING__NJOBS:
        return n_jobs != null;
      case ThingMLPackage.SPECTRAL_CLUSTERING__VERBOSE:
        return verbose != null;
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
    result.append(" (eigen_solver: ");
    result.append(eigen_solver);
    result.append(", affinity: ");
    result.append(affinity);
    result.append(", assign_labels: ");
    result.append(assign_labels);
    result.append(", kernel_params: ");
    result.append(kernel_params);
    result.append(')');
    return result.toString();
  }

} //SpectralClusteringImpl
