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
import org.thingml.xtext.thingML.IntegerLiteral;
import org.thingml.xtext.thingML.RFR;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RFR</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getN_estimators <em>Nestimators</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getCriterion <em>Criterion</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getMax_depth <em>Max depth</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getMin_samples_split <em>Min samples split</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getMin_samples_leaf <em>Min samples leaf</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getMin_weight_fraction_leaf <em>Min weight fraction leaf</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getMax_features <em>Max features</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getMax_leaf_nodes <em>Max leaf nodes</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getMin_impurity_decrease <em>Min impurity decrease</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getBootstrap <em>Bootstrap</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getOob_score <em>Oob score</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getN_jobs <em>Njobs</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getRandom_state <em>Random state</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getVerbose <em>Verbose</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.RFRImpl#getWarm_start <em>Warm start</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RFRImpl extends machine_learning_ModelAlgorithmImpl implements RFR
{
  /**
   * The cached value of the '{@link #getN_estimators() <em>Nestimators</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_estimators()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_estimators;

  /**
   * The default value of the '{@link #getCriterion() <em>Criterion</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCriterion()
   * @generated
   * @ordered
   */
  protected static final String CRITERION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getCriterion() <em>Criterion</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCriterion()
   * @generated
   * @ordered
   */
  protected String criterion = CRITERION_EDEFAULT;

  /**
   * The cached value of the '{@link #getMax_depth() <em>Max depth</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax_depth()
   * @generated
   * @ordered
   */
  protected IntegerLiteral max_depth;

  /**
   * The cached value of the '{@link #getMin_samples_split() <em>Min samples split</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMin_samples_split()
   * @generated
   * @ordered
   */
  protected IntegerLiteral min_samples_split;

  /**
   * The cached value of the '{@link #getMin_samples_leaf() <em>Min samples leaf</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMin_samples_leaf()
   * @generated
   * @ordered
   */
  protected IntegerLiteral min_samples_leaf;

  /**
   * The default value of the '{@link #getMin_weight_fraction_leaf() <em>Min weight fraction leaf</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMin_weight_fraction_leaf()
   * @generated
   * @ordered
   */
  protected static final double MIN_WEIGHT_FRACTION_LEAF_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getMin_weight_fraction_leaf() <em>Min weight fraction leaf</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMin_weight_fraction_leaf()
   * @generated
   * @ordered
   */
  protected double min_weight_fraction_leaf = MIN_WEIGHT_FRACTION_LEAF_EDEFAULT;

  /**
   * The default value of the '{@link #getMax_features() <em>Max features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax_features()
   * @generated
   * @ordered
   */
  protected static final String MAX_FEATURES_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMax_features() <em>Max features</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax_features()
   * @generated
   * @ordered
   */
  protected String max_features = MAX_FEATURES_EDEFAULT;

  /**
   * The cached value of the '{@link #getMax_leaf_nodes() <em>Max leaf nodes</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax_leaf_nodes()
   * @generated
   * @ordered
   */
  protected IntegerLiteral max_leaf_nodes;

  /**
   * The default value of the '{@link #getMin_impurity_decrease() <em>Min impurity decrease</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMin_impurity_decrease()
   * @generated
   * @ordered
   */
  protected static final double MIN_IMPURITY_DECREASE_EDEFAULT = 0.0;

  /**
   * The cached value of the '{@link #getMin_impurity_decrease() <em>Min impurity decrease</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMin_impurity_decrease()
   * @generated
   * @ordered
   */
  protected double min_impurity_decrease = MIN_IMPURITY_DECREASE_EDEFAULT;

  /**
   * The cached value of the '{@link #getBootstrap() <em>Bootstrap</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBootstrap()
   * @generated
   * @ordered
   */
  protected BooleanLiteral bootstrap;

  /**
   * The cached value of the '{@link #getOob_score() <em>Oob score</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOob_score()
   * @generated
   * @ordered
   */
  protected BooleanLiteral oob_score;

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
   * The cached value of the '{@link #getRandom_state() <em>Random state</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRandom_state()
   * @generated
   * @ordered
   */
  protected IntegerLiteral random_state;

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
   * The cached value of the '{@link #getWarm_start() <em>Warm start</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWarm_start()
   * @generated
   * @ordered
   */
  protected BooleanLiteral warm_start;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RFRImpl()
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
    return ThingMLPackage.eINSTANCE.getRFR();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getN_estimators()
  {
    return n_estimators;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetN_estimators(IntegerLiteral newN_estimators, NotificationChain msgs)
  {
    IntegerLiteral oldN_estimators = n_estimators;
    n_estimators = newN_estimators;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__NESTIMATORS, oldN_estimators, newN_estimators);
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
  public void setN_estimators(IntegerLiteral newN_estimators)
  {
    if (newN_estimators != n_estimators)
    {
      NotificationChain msgs = null;
      if (n_estimators != null)
        msgs = ((InternalEObject)n_estimators).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__NESTIMATORS, null, msgs);
      if (newN_estimators != null)
        msgs = ((InternalEObject)newN_estimators).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__NESTIMATORS, null, msgs);
      msgs = basicSetN_estimators(newN_estimators, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__NESTIMATORS, newN_estimators, newN_estimators));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getCriterion()
  {
    return criterion;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCriterion(String newCriterion)
  {
    String oldCriterion = criterion;
    criterion = newCriterion;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__CRITERION, oldCriterion, criterion));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getMax_depth()
  {
    return max_depth;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMax_depth(IntegerLiteral newMax_depth, NotificationChain msgs)
  {
    IntegerLiteral oldMax_depth = max_depth;
    max_depth = newMax_depth;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MAX_DEPTH, oldMax_depth, newMax_depth);
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
  public void setMax_depth(IntegerLiteral newMax_depth)
  {
    if (newMax_depth != max_depth)
    {
      NotificationChain msgs = null;
      if (max_depth != null)
        msgs = ((InternalEObject)max_depth).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__MAX_DEPTH, null, msgs);
      if (newMax_depth != null)
        msgs = ((InternalEObject)newMax_depth).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__MAX_DEPTH, null, msgs);
      msgs = basicSetMax_depth(newMax_depth, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MAX_DEPTH, newMax_depth, newMax_depth));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getMin_samples_split()
  {
    return min_samples_split;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMin_samples_split(IntegerLiteral newMin_samples_split, NotificationChain msgs)
  {
    IntegerLiteral oldMin_samples_split = min_samples_split;
    min_samples_split = newMin_samples_split;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MIN_SAMPLES_SPLIT, oldMin_samples_split, newMin_samples_split);
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
  public void setMin_samples_split(IntegerLiteral newMin_samples_split)
  {
    if (newMin_samples_split != min_samples_split)
    {
      NotificationChain msgs = null;
      if (min_samples_split != null)
        msgs = ((InternalEObject)min_samples_split).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__MIN_SAMPLES_SPLIT, null, msgs);
      if (newMin_samples_split != null)
        msgs = ((InternalEObject)newMin_samples_split).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__MIN_SAMPLES_SPLIT, null, msgs);
      msgs = basicSetMin_samples_split(newMin_samples_split, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MIN_SAMPLES_SPLIT, newMin_samples_split, newMin_samples_split));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getMin_samples_leaf()
  {
    return min_samples_leaf;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMin_samples_leaf(IntegerLiteral newMin_samples_leaf, NotificationChain msgs)
  {
    IntegerLiteral oldMin_samples_leaf = min_samples_leaf;
    min_samples_leaf = newMin_samples_leaf;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MIN_SAMPLES_LEAF, oldMin_samples_leaf, newMin_samples_leaf);
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
  public void setMin_samples_leaf(IntegerLiteral newMin_samples_leaf)
  {
    if (newMin_samples_leaf != min_samples_leaf)
    {
      NotificationChain msgs = null;
      if (min_samples_leaf != null)
        msgs = ((InternalEObject)min_samples_leaf).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__MIN_SAMPLES_LEAF, null, msgs);
      if (newMin_samples_leaf != null)
        msgs = ((InternalEObject)newMin_samples_leaf).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__MIN_SAMPLES_LEAF, null, msgs);
      msgs = basicSetMin_samples_leaf(newMin_samples_leaf, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MIN_SAMPLES_LEAF, newMin_samples_leaf, newMin_samples_leaf));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getMin_weight_fraction_leaf()
  {
    return min_weight_fraction_leaf;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMin_weight_fraction_leaf(double newMin_weight_fraction_leaf)
  {
    double oldMin_weight_fraction_leaf = min_weight_fraction_leaf;
    min_weight_fraction_leaf = newMin_weight_fraction_leaf;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MIN_WEIGHT_FRACTION_LEAF, oldMin_weight_fraction_leaf, min_weight_fraction_leaf));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getMax_features()
  {
    return max_features;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMax_features(String newMax_features)
  {
    String oldMax_features = max_features;
    max_features = newMax_features;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MAX_FEATURES, oldMax_features, max_features));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getMax_leaf_nodes()
  {
    return max_leaf_nodes;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMax_leaf_nodes(IntegerLiteral newMax_leaf_nodes, NotificationChain msgs)
  {
    IntegerLiteral oldMax_leaf_nodes = max_leaf_nodes;
    max_leaf_nodes = newMax_leaf_nodes;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MAX_LEAF_NODES, oldMax_leaf_nodes, newMax_leaf_nodes);
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
  public void setMax_leaf_nodes(IntegerLiteral newMax_leaf_nodes)
  {
    if (newMax_leaf_nodes != max_leaf_nodes)
    {
      NotificationChain msgs = null;
      if (max_leaf_nodes != null)
        msgs = ((InternalEObject)max_leaf_nodes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__MAX_LEAF_NODES, null, msgs);
      if (newMax_leaf_nodes != null)
        msgs = ((InternalEObject)newMax_leaf_nodes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__MAX_LEAF_NODES, null, msgs);
      msgs = basicSetMax_leaf_nodes(newMax_leaf_nodes, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MAX_LEAF_NODES, newMax_leaf_nodes, newMax_leaf_nodes));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public double getMin_impurity_decrease()
  {
    return min_impurity_decrease;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMin_impurity_decrease(double newMin_impurity_decrease)
  {
    double oldMin_impurity_decrease = min_impurity_decrease;
    min_impurity_decrease = newMin_impurity_decrease;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__MIN_IMPURITY_DECREASE, oldMin_impurity_decrease, min_impurity_decrease));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BooleanLiteral getBootstrap()
  {
    return bootstrap;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBootstrap(BooleanLiteral newBootstrap, NotificationChain msgs)
  {
    BooleanLiteral oldBootstrap = bootstrap;
    bootstrap = newBootstrap;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__BOOTSTRAP, oldBootstrap, newBootstrap);
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
  public void setBootstrap(BooleanLiteral newBootstrap)
  {
    if (newBootstrap != bootstrap)
    {
      NotificationChain msgs = null;
      if (bootstrap != null)
        msgs = ((InternalEObject)bootstrap).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__BOOTSTRAP, null, msgs);
      if (newBootstrap != null)
        msgs = ((InternalEObject)newBootstrap).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__BOOTSTRAP, null, msgs);
      msgs = basicSetBootstrap(newBootstrap, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__BOOTSTRAP, newBootstrap, newBootstrap));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BooleanLiteral getOob_score()
  {
    return oob_score;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOob_score(BooleanLiteral newOob_score, NotificationChain msgs)
  {
    BooleanLiteral oldOob_score = oob_score;
    oob_score = newOob_score;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__OOB_SCORE, oldOob_score, newOob_score);
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
  public void setOob_score(BooleanLiteral newOob_score)
  {
    if (newOob_score != oob_score)
    {
      NotificationChain msgs = null;
      if (oob_score != null)
        msgs = ((InternalEObject)oob_score).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__OOB_SCORE, null, msgs);
      if (newOob_score != null)
        msgs = ((InternalEObject)newOob_score).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__OOB_SCORE, null, msgs);
      msgs = basicSetOob_score(newOob_score, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__OOB_SCORE, newOob_score, newOob_score));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__NJOBS, oldN_jobs, newN_jobs);
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
        msgs = ((InternalEObject)n_jobs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__NJOBS, null, msgs);
      if (newN_jobs != null)
        msgs = ((InternalEObject)newN_jobs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__NJOBS, null, msgs);
      msgs = basicSetN_jobs(newN_jobs, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__NJOBS, newN_jobs, newN_jobs));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__RANDOM_STATE, oldRandom_state, newRandom_state);
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
        msgs = ((InternalEObject)random_state).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__RANDOM_STATE, null, msgs);
      if (newRandom_state != null)
        msgs = ((InternalEObject)newRandom_state).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__RANDOM_STATE, null, msgs);
      msgs = basicSetRandom_state(newRandom_state, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__RANDOM_STATE, newRandom_state, newRandom_state));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__VERBOSE, oldVerbose, newVerbose);
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
        msgs = ((InternalEObject)verbose).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__VERBOSE, null, msgs);
      if (newVerbose != null)
        msgs = ((InternalEObject)newVerbose).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__VERBOSE, null, msgs);
      msgs = basicSetVerbose(newVerbose, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__VERBOSE, newVerbose, newVerbose));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BooleanLiteral getWarm_start()
  {
    return warm_start;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetWarm_start(BooleanLiteral newWarm_start, NotificationChain msgs)
  {
    BooleanLiteral oldWarm_start = warm_start;
    warm_start = newWarm_start;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__WARM_START, oldWarm_start, newWarm_start);
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
  public void setWarm_start(BooleanLiteral newWarm_start)
  {
    if (newWarm_start != warm_start)
    {
      NotificationChain msgs = null;
      if (warm_start != null)
        msgs = ((InternalEObject)warm_start).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__WARM_START, null, msgs);
      if (newWarm_start != null)
        msgs = ((InternalEObject)newWarm_start).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.RFR__WARM_START, null, msgs);
      msgs = basicSetWarm_start(newWarm_start, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.RFR__WARM_START, newWarm_start, newWarm_start));
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
      case ThingMLPackage.RFR__NESTIMATORS:
        return basicSetN_estimators(null, msgs);
      case ThingMLPackage.RFR__MAX_DEPTH:
        return basicSetMax_depth(null, msgs);
      case ThingMLPackage.RFR__MIN_SAMPLES_SPLIT:
        return basicSetMin_samples_split(null, msgs);
      case ThingMLPackage.RFR__MIN_SAMPLES_LEAF:
        return basicSetMin_samples_leaf(null, msgs);
      case ThingMLPackage.RFR__MAX_LEAF_NODES:
        return basicSetMax_leaf_nodes(null, msgs);
      case ThingMLPackage.RFR__BOOTSTRAP:
        return basicSetBootstrap(null, msgs);
      case ThingMLPackage.RFR__OOB_SCORE:
        return basicSetOob_score(null, msgs);
      case ThingMLPackage.RFR__NJOBS:
        return basicSetN_jobs(null, msgs);
      case ThingMLPackage.RFR__RANDOM_STATE:
        return basicSetRandom_state(null, msgs);
      case ThingMLPackage.RFR__VERBOSE:
        return basicSetVerbose(null, msgs);
      case ThingMLPackage.RFR__WARM_START:
        return basicSetWarm_start(null, msgs);
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
      case ThingMLPackage.RFR__NESTIMATORS:
        return getN_estimators();
      case ThingMLPackage.RFR__CRITERION:
        return getCriterion();
      case ThingMLPackage.RFR__MAX_DEPTH:
        return getMax_depth();
      case ThingMLPackage.RFR__MIN_SAMPLES_SPLIT:
        return getMin_samples_split();
      case ThingMLPackage.RFR__MIN_SAMPLES_LEAF:
        return getMin_samples_leaf();
      case ThingMLPackage.RFR__MIN_WEIGHT_FRACTION_LEAF:
        return getMin_weight_fraction_leaf();
      case ThingMLPackage.RFR__MAX_FEATURES:
        return getMax_features();
      case ThingMLPackage.RFR__MAX_LEAF_NODES:
        return getMax_leaf_nodes();
      case ThingMLPackage.RFR__MIN_IMPURITY_DECREASE:
        return getMin_impurity_decrease();
      case ThingMLPackage.RFR__BOOTSTRAP:
        return getBootstrap();
      case ThingMLPackage.RFR__OOB_SCORE:
        return getOob_score();
      case ThingMLPackage.RFR__NJOBS:
        return getN_jobs();
      case ThingMLPackage.RFR__RANDOM_STATE:
        return getRandom_state();
      case ThingMLPackage.RFR__VERBOSE:
        return getVerbose();
      case ThingMLPackage.RFR__WARM_START:
        return getWarm_start();
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
      case ThingMLPackage.RFR__NESTIMATORS:
        setN_estimators((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.RFR__CRITERION:
        setCriterion((String)newValue);
        return;
      case ThingMLPackage.RFR__MAX_DEPTH:
        setMax_depth((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.RFR__MIN_SAMPLES_SPLIT:
        setMin_samples_split((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.RFR__MIN_SAMPLES_LEAF:
        setMin_samples_leaf((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.RFR__MIN_WEIGHT_FRACTION_LEAF:
        setMin_weight_fraction_leaf((Double)newValue);
        return;
      case ThingMLPackage.RFR__MAX_FEATURES:
        setMax_features((String)newValue);
        return;
      case ThingMLPackage.RFR__MAX_LEAF_NODES:
        setMax_leaf_nodes((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.RFR__MIN_IMPURITY_DECREASE:
        setMin_impurity_decrease((Double)newValue);
        return;
      case ThingMLPackage.RFR__BOOTSTRAP:
        setBootstrap((BooleanLiteral)newValue);
        return;
      case ThingMLPackage.RFR__OOB_SCORE:
        setOob_score((BooleanLiteral)newValue);
        return;
      case ThingMLPackage.RFR__NJOBS:
        setN_jobs((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.RFR__RANDOM_STATE:
        setRandom_state((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.RFR__VERBOSE:
        setVerbose((BooleanLiteral)newValue);
        return;
      case ThingMLPackage.RFR__WARM_START:
        setWarm_start((BooleanLiteral)newValue);
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
      case ThingMLPackage.RFR__NESTIMATORS:
        setN_estimators((IntegerLiteral)null);
        return;
      case ThingMLPackage.RFR__CRITERION:
        setCriterion(CRITERION_EDEFAULT);
        return;
      case ThingMLPackage.RFR__MAX_DEPTH:
        setMax_depth((IntegerLiteral)null);
        return;
      case ThingMLPackage.RFR__MIN_SAMPLES_SPLIT:
        setMin_samples_split((IntegerLiteral)null);
        return;
      case ThingMLPackage.RFR__MIN_SAMPLES_LEAF:
        setMin_samples_leaf((IntegerLiteral)null);
        return;
      case ThingMLPackage.RFR__MIN_WEIGHT_FRACTION_LEAF:
        setMin_weight_fraction_leaf(MIN_WEIGHT_FRACTION_LEAF_EDEFAULT);
        return;
      case ThingMLPackage.RFR__MAX_FEATURES:
        setMax_features(MAX_FEATURES_EDEFAULT);
        return;
      case ThingMLPackage.RFR__MAX_LEAF_NODES:
        setMax_leaf_nodes((IntegerLiteral)null);
        return;
      case ThingMLPackage.RFR__MIN_IMPURITY_DECREASE:
        setMin_impurity_decrease(MIN_IMPURITY_DECREASE_EDEFAULT);
        return;
      case ThingMLPackage.RFR__BOOTSTRAP:
        setBootstrap((BooleanLiteral)null);
        return;
      case ThingMLPackage.RFR__OOB_SCORE:
        setOob_score((BooleanLiteral)null);
        return;
      case ThingMLPackage.RFR__NJOBS:
        setN_jobs((IntegerLiteral)null);
        return;
      case ThingMLPackage.RFR__RANDOM_STATE:
        setRandom_state((IntegerLiteral)null);
        return;
      case ThingMLPackage.RFR__VERBOSE:
        setVerbose((BooleanLiteral)null);
        return;
      case ThingMLPackage.RFR__WARM_START:
        setWarm_start((BooleanLiteral)null);
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
      case ThingMLPackage.RFR__NESTIMATORS:
        return n_estimators != null;
      case ThingMLPackage.RFR__CRITERION:
        return CRITERION_EDEFAULT == null ? criterion != null : !CRITERION_EDEFAULT.equals(criterion);
      case ThingMLPackage.RFR__MAX_DEPTH:
        return max_depth != null;
      case ThingMLPackage.RFR__MIN_SAMPLES_SPLIT:
        return min_samples_split != null;
      case ThingMLPackage.RFR__MIN_SAMPLES_LEAF:
        return min_samples_leaf != null;
      case ThingMLPackage.RFR__MIN_WEIGHT_FRACTION_LEAF:
        return min_weight_fraction_leaf != MIN_WEIGHT_FRACTION_LEAF_EDEFAULT;
      case ThingMLPackage.RFR__MAX_FEATURES:
        return MAX_FEATURES_EDEFAULT == null ? max_features != null : !MAX_FEATURES_EDEFAULT.equals(max_features);
      case ThingMLPackage.RFR__MAX_LEAF_NODES:
        return max_leaf_nodes != null;
      case ThingMLPackage.RFR__MIN_IMPURITY_DECREASE:
        return min_impurity_decrease != MIN_IMPURITY_DECREASE_EDEFAULT;
      case ThingMLPackage.RFR__BOOTSTRAP:
        return bootstrap != null;
      case ThingMLPackage.RFR__OOB_SCORE:
        return oob_score != null;
      case ThingMLPackage.RFR__NJOBS:
        return n_jobs != null;
      case ThingMLPackage.RFR__RANDOM_STATE:
        return random_state != null;
      case ThingMLPackage.RFR__VERBOSE:
        return verbose != null;
      case ThingMLPackage.RFR__WARM_START:
        return warm_start != null;
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
    result.append(" (criterion: ");
    result.append(criterion);
    result.append(", min_weight_fraction_leaf: ");
    result.append(min_weight_fraction_leaf);
    result.append(", max_features: ");
    result.append(max_features);
    result.append(", min_impurity_decrease: ");
    result.append(min_impurity_decrease);
    result.append(')');
    return result.toString();
  }

} //RFRImpl
