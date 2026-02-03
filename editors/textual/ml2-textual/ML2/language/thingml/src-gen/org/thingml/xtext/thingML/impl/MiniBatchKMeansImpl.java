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
import org.thingml.xtext.thingML.IntegerLiteral;
import org.thingml.xtext.thingML.KMeans_init_enum;
import org.thingml.xtext.thingML.MiniBatchKMeans;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mini Batch KMeans</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getN_clusters <em>Nclusters</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getInit_enum <em>Init enum</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getInit_str <em>Init str</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getMax_iter <em>Max iter</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getBatch_size <em>Batch size</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getVerbose <em>Verbose</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getCompute_labels <em>Compute labels</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getRandom_state <em>Random state</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getTol <em>Tol</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getMax_no_improvement <em>Max no improvement</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getInit_size <em>Init size</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getN_init <em>Ninit</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.MiniBatchKMeansImpl#getReassignment_ratio <em>Reassignment ratio</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MiniBatchKMeansImpl extends ML2_ModelAlgorithmImpl implements MiniBatchKMeans
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
   * The default value of the '{@link #getInit_enum() <em>Init enum</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInit_enum()
   * @generated
   * @ordered
   */
  protected static final KMeans_init_enum INIT_ENUM_EDEFAULT = KMeans_init_enum.NOT_SET;

  /**
   * The cached value of the '{@link #getInit_enum() <em>Init enum</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInit_enum()
   * @generated
   * @ordered
   */
  protected KMeans_init_enum init_enum = INIT_ENUM_EDEFAULT;

  /**
   * The default value of the '{@link #getInit_str() <em>Init str</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInit_str()
   * @generated
   * @ordered
   */
  protected static final String INIT_STR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getInit_str() <em>Init str</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInit_str()
   * @generated
   * @ordered
   */
  protected String init_str = INIT_STR_EDEFAULT;

  /**
   * The cached value of the '{@link #getMax_iter() <em>Max iter</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax_iter()
   * @generated
   * @ordered
   */
  protected IntegerLiteral max_iter;

  /**
   * The cached value of the '{@link #getBatch_size() <em>Batch size</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBatch_size()
   * @generated
   * @ordered
   */
  protected IntegerLiteral batch_size;

  /**
   * The cached value of the '{@link #getVerbose() <em>Verbose</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVerbose()
   * @generated
   * @ordered
   */
  protected IntegerLiteral verbose;

  /**
   * The cached value of the '{@link #getCompute_labels() <em>Compute labels</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCompute_labels()
   * @generated
   * @ordered
   */
  protected BooleanLiteral compute_labels;

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
   * The cached value of the '{@link #getTol() <em>Tol</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTol()
   * @generated
   * @ordered
   */
  protected DoubleLiteral tol;

  /**
   * The cached value of the '{@link #getMax_no_improvement() <em>Max no improvement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax_no_improvement()
   * @generated
   * @ordered
   */
  protected IntegerLiteral max_no_improvement;

  /**
   * The cached value of the '{@link #getInit_size() <em>Init size</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInit_size()
   * @generated
   * @ordered
   */
  protected IntegerLiteral init_size;

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
   * The cached value of the '{@link #getReassignment_ratio() <em>Reassignment ratio</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReassignment_ratio()
   * @generated
   * @ordered
   */
  protected DoubleLiteral reassignment_ratio;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MiniBatchKMeansImpl()
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
    return ThingMLPackage.eINSTANCE.getMiniBatchKMeans();
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS, oldN_clusters, newN_clusters);
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
        msgs = ((InternalEObject)n_clusters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS, null, msgs);
      if (newN_clusters != null)
        msgs = ((InternalEObject)newN_clusters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS, null, msgs);
      msgs = basicSetN_clusters(newN_clusters, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS, newN_clusters, newN_clusters));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public KMeans_init_enum getInit_enum()
  {
    return init_enum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setInit_enum(KMeans_init_enum newInit_enum)
  {
    KMeans_init_enum oldInit_enum = init_enum;
    init_enum = newInit_enum == null ? INIT_ENUM_EDEFAULT : newInit_enum;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__INIT_ENUM, oldInit_enum, init_enum));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getInit_str()
  {
    return init_str;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setInit_str(String newInit_str)
  {
    String oldInit_str = init_str;
    init_str = newInit_str;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__INIT_STR, oldInit_str, init_str));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getMax_iter()
  {
    return max_iter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMax_iter(IntegerLiteral newMax_iter, NotificationChain msgs)
  {
    IntegerLiteral oldMax_iter = max_iter;
    max_iter = newMax_iter;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER, oldMax_iter, newMax_iter);
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
  public void setMax_iter(IntegerLiteral newMax_iter)
  {
    if (newMax_iter != max_iter)
    {
      NotificationChain msgs = null;
      if (max_iter != null)
        msgs = ((InternalEObject)max_iter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER, null, msgs);
      if (newMax_iter != null)
        msgs = ((InternalEObject)newMax_iter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER, null, msgs);
      msgs = basicSetMax_iter(newMax_iter, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER, newMax_iter, newMax_iter));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getBatch_size()
  {
    return batch_size;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBatch_size(IntegerLiteral newBatch_size, NotificationChain msgs)
  {
    IntegerLiteral oldBatch_size = batch_size;
    batch_size = newBatch_size;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE, oldBatch_size, newBatch_size);
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
  public void setBatch_size(IntegerLiteral newBatch_size)
  {
    if (newBatch_size != batch_size)
    {
      NotificationChain msgs = null;
      if (batch_size != null)
        msgs = ((InternalEObject)batch_size).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE, null, msgs);
      if (newBatch_size != null)
        msgs = ((InternalEObject)newBatch_size).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE, null, msgs);
      msgs = basicSetBatch_size(newBatch_size, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE, newBatch_size, newBatch_size));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getVerbose()
  {
    return verbose;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVerbose(IntegerLiteral newVerbose, NotificationChain msgs)
  {
    IntegerLiteral oldVerbose = verbose;
    verbose = newVerbose;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE, oldVerbose, newVerbose);
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
  public void setVerbose(IntegerLiteral newVerbose)
  {
    if (newVerbose != verbose)
    {
      NotificationChain msgs = null;
      if (verbose != null)
        msgs = ((InternalEObject)verbose).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE, null, msgs);
      if (newVerbose != null)
        msgs = ((InternalEObject)newVerbose).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE, null, msgs);
      msgs = basicSetVerbose(newVerbose, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE, newVerbose, newVerbose));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BooleanLiteral getCompute_labels()
  {
    return compute_labels;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCompute_labels(BooleanLiteral newCompute_labels, NotificationChain msgs)
  {
    BooleanLiteral oldCompute_labels = compute_labels;
    compute_labels = newCompute_labels;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS, oldCompute_labels, newCompute_labels);
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
  public void setCompute_labels(BooleanLiteral newCompute_labels)
  {
    if (newCompute_labels != compute_labels)
    {
      NotificationChain msgs = null;
      if (compute_labels != null)
        msgs = ((InternalEObject)compute_labels).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS, null, msgs);
      if (newCompute_labels != null)
        msgs = ((InternalEObject)newCompute_labels).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS, null, msgs);
      msgs = basicSetCompute_labels(newCompute_labels, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS, newCompute_labels, newCompute_labels));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE, oldRandom_state, newRandom_state);
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
        msgs = ((InternalEObject)random_state).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE, null, msgs);
      if (newRandom_state != null)
        msgs = ((InternalEObject)newRandom_state).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE, null, msgs);
      msgs = basicSetRandom_state(newRandom_state, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE, newRandom_state, newRandom_state));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getTol()
  {
    return tol;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTol(DoubleLiteral newTol, NotificationChain msgs)
  {
    DoubleLiteral oldTol = tol;
    tol = newTol;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__TOL, oldTol, newTol);
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
  public void setTol(DoubleLiteral newTol)
  {
    if (newTol != tol)
    {
      NotificationChain msgs = null;
      if (tol != null)
        msgs = ((InternalEObject)tol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__TOL, null, msgs);
      if (newTol != null)
        msgs = ((InternalEObject)newTol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__TOL, null, msgs);
      msgs = basicSetTol(newTol, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__TOL, newTol, newTol));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getMax_no_improvement()
  {
    return max_no_improvement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMax_no_improvement(IntegerLiteral newMax_no_improvement, NotificationChain msgs)
  {
    IntegerLiteral oldMax_no_improvement = max_no_improvement;
    max_no_improvement = newMax_no_improvement;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT, oldMax_no_improvement, newMax_no_improvement);
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
  public void setMax_no_improvement(IntegerLiteral newMax_no_improvement)
  {
    if (newMax_no_improvement != max_no_improvement)
    {
      NotificationChain msgs = null;
      if (max_no_improvement != null)
        msgs = ((InternalEObject)max_no_improvement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT, null, msgs);
      if (newMax_no_improvement != null)
        msgs = ((InternalEObject)newMax_no_improvement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT, null, msgs);
      msgs = basicSetMax_no_improvement(newMax_no_improvement, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT, newMax_no_improvement, newMax_no_improvement));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getInit_size()
  {
    return init_size;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetInit_size(IntegerLiteral newInit_size, NotificationChain msgs)
  {
    IntegerLiteral oldInit_size = init_size;
    init_size = newInit_size;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE, oldInit_size, newInit_size);
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
  public void setInit_size(IntegerLiteral newInit_size)
  {
    if (newInit_size != init_size)
    {
      NotificationChain msgs = null;
      if (init_size != null)
        msgs = ((InternalEObject)init_size).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE, null, msgs);
      if (newInit_size != null)
        msgs = ((InternalEObject)newInit_size).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE, null, msgs);
      msgs = basicSetInit_size(newInit_size, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE, newInit_size, newInit_size));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__NINIT, oldN_init, newN_init);
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
        msgs = ((InternalEObject)n_init).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__NINIT, null, msgs);
      if (newN_init != null)
        msgs = ((InternalEObject)newN_init).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__NINIT, null, msgs);
      msgs = basicSetN_init(newN_init, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__NINIT, newN_init, newN_init));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getReassignment_ratio()
  {
    return reassignment_ratio;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetReassignment_ratio(DoubleLiteral newReassignment_ratio, NotificationChain msgs)
  {
    DoubleLiteral oldReassignment_ratio = reassignment_ratio;
    reassignment_ratio = newReassignment_ratio;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO, oldReassignment_ratio, newReassignment_ratio);
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
  public void setReassignment_ratio(DoubleLiteral newReassignment_ratio)
  {
    if (newReassignment_ratio != reassignment_ratio)
    {
      NotificationChain msgs = null;
      if (reassignment_ratio != null)
        msgs = ((InternalEObject)reassignment_ratio).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO, null, msgs);
      if (newReassignment_ratio != null)
        msgs = ((InternalEObject)newReassignment_ratio).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO, null, msgs);
      msgs = basicSetReassignment_ratio(newReassignment_ratio, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO, newReassignment_ratio, newReassignment_ratio));
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
      case ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS:
        return basicSetN_clusters(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER:
        return basicSetMax_iter(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE:
        return basicSetBatch_size(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE:
        return basicSetVerbose(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS:
        return basicSetCompute_labels(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE:
        return basicSetRandom_state(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__TOL:
        return basicSetTol(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT:
        return basicSetMax_no_improvement(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE:
        return basicSetInit_size(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__NINIT:
        return basicSetN_init(null, msgs);
      case ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO:
        return basicSetReassignment_ratio(null, msgs);
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
      case ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS:
        return getN_clusters();
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_ENUM:
        return getInit_enum();
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_STR:
        return getInit_str();
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER:
        return getMax_iter();
      case ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE:
        return getBatch_size();
      case ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE:
        return getVerbose();
      case ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS:
        return getCompute_labels();
      case ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE:
        return getRandom_state();
      case ThingMLPackage.MINI_BATCH_KMEANS__TOL:
        return getTol();
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT:
        return getMax_no_improvement();
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE:
        return getInit_size();
      case ThingMLPackage.MINI_BATCH_KMEANS__NINIT:
        return getN_init();
      case ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO:
        return getReassignment_ratio();
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
      case ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS:
        setN_clusters((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_ENUM:
        setInit_enum((KMeans_init_enum)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_STR:
        setInit_str((String)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER:
        setMax_iter((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE:
        setBatch_size((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE:
        setVerbose((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS:
        setCompute_labels((BooleanLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE:
        setRandom_state((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__TOL:
        setTol((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT:
        setMax_no_improvement((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE:
        setInit_size((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__NINIT:
        setN_init((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO:
        setReassignment_ratio((DoubleLiteral)newValue);
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
      case ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS:
        setN_clusters((IntegerLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_ENUM:
        setInit_enum(INIT_ENUM_EDEFAULT);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_STR:
        setInit_str(INIT_STR_EDEFAULT);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER:
        setMax_iter((IntegerLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE:
        setBatch_size((IntegerLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE:
        setVerbose((IntegerLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS:
        setCompute_labels((BooleanLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE:
        setRandom_state((IntegerLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__TOL:
        setTol((DoubleLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT:
        setMax_no_improvement((IntegerLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE:
        setInit_size((IntegerLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__NINIT:
        setN_init((IntegerLiteral)null);
        return;
      case ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO:
        setReassignment_ratio((DoubleLiteral)null);
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
      case ThingMLPackage.MINI_BATCH_KMEANS__NCLUSTERS:
        return n_clusters != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_ENUM:
        return init_enum != INIT_ENUM_EDEFAULT;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_STR:
        return INIT_STR_EDEFAULT == null ? init_str != null : !INIT_STR_EDEFAULT.equals(init_str);
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_ITER:
        return max_iter != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__BATCH_SIZE:
        return batch_size != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__VERBOSE:
        return verbose != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__COMPUTE_LABELS:
        return compute_labels != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__RANDOM_STATE:
        return random_state != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__TOL:
        return tol != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__MAX_NO_IMPROVEMENT:
        return max_no_improvement != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__INIT_SIZE:
        return init_size != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__NINIT:
        return n_init != null;
      case ThingMLPackage.MINI_BATCH_KMEANS__REASSIGNMENT_RATIO:
        return reassignment_ratio != null;
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
    result.append(" (init_enum: ");
    result.append(init_enum);
    result.append(", init_str: ");
    result.append(init_str);
    result.append(')');
    return result.toString();
  }

} //MiniBatchKMeansImpl
