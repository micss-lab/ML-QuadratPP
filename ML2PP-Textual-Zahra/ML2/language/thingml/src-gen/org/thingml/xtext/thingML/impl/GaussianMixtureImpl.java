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
import org.thingml.xtext.thingML.Covariance_Type;
import org.thingml.xtext.thingML.DoubleLiteral;
import org.thingml.xtext.thingML.GaussianMixture;
import org.thingml.xtext.thingML.Init_Params_Methods;
import org.thingml.xtext.thingML.IntegerLiteral;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Gaussian Mixture</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getN_components <em>Ncomponents</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getCovariance_type <em>Covariance type</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getTol <em>Tol</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getReg_covar <em>Reg covar</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getMax_iter <em>Max iter</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getN_init <em>Ninit</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getInit_params <em>Init params</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getWeights_init <em>Weights init</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getMeans_init <em>Means init</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getPrecisions_init <em>Precisions init</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getRandom_state <em>Random state</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getWarm_start <em>Warm start</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getVerbose <em>Verbose</em>}</li>
 *   <li>{@link org.thingml.xtext.thingML.impl.GaussianMixtureImpl#getVerbose_interval <em>Verbose interval</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GaussianMixtureImpl extends ML2_ModelAlgorithmImpl implements GaussianMixture
{
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
   * The default value of the '{@link #getCovariance_type() <em>Covariance type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCovariance_type()
   * @generated
   * @ordered
   */
  protected static final Covariance_Type COVARIANCE_TYPE_EDEFAULT = Covariance_Type.NOT_SET;

  /**
   * The cached value of the '{@link #getCovariance_type() <em>Covariance type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCovariance_type()
   * @generated
   * @ordered
   */
  protected Covariance_Type covariance_type = COVARIANCE_TYPE_EDEFAULT;

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
   * The cached value of the '{@link #getReg_covar() <em>Reg covar</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReg_covar()
   * @generated
   * @ordered
   */
  protected DoubleLiteral reg_covar;

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
   * The cached value of the '{@link #getN_init() <em>Ninit</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getN_init()
   * @generated
   * @ordered
   */
  protected IntegerLiteral n_init;

  /**
   * The default value of the '{@link #getInit_params() <em>Init params</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInit_params()
   * @generated
   * @ordered
   */
  protected static final Init_Params_Methods INIT_PARAMS_EDEFAULT = Init_Params_Methods.NOT_SET;

  /**
   * The cached value of the '{@link #getInit_params() <em>Init params</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInit_params()
   * @generated
   * @ordered
   */
  protected Init_Params_Methods init_params = INIT_PARAMS_EDEFAULT;

  /**
   * The default value of the '{@link #getWeights_init() <em>Weights init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWeights_init()
   * @generated
   * @ordered
   */
  protected static final String WEIGHTS_INIT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getWeights_init() <em>Weights init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWeights_init()
   * @generated
   * @ordered
   */
  protected String weights_init = WEIGHTS_INIT_EDEFAULT;

  /**
   * The default value of the '{@link #getMeans_init() <em>Means init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMeans_init()
   * @generated
   * @ordered
   */
  protected static final String MEANS_INIT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMeans_init() <em>Means init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMeans_init()
   * @generated
   * @ordered
   */
  protected String means_init = MEANS_INIT_EDEFAULT;

  /**
   * The default value of the '{@link #getPrecisions_init() <em>Precisions init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrecisions_init()
   * @generated
   * @ordered
   */
  protected static final String PRECISIONS_INIT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPrecisions_init() <em>Precisions init</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrecisions_init()
   * @generated
   * @ordered
   */
  protected String precisions_init = PRECISIONS_INIT_EDEFAULT;

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
   * The cached value of the '{@link #getWarm_start() <em>Warm start</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWarm_start()
   * @generated
   * @ordered
   */
  protected BooleanLiteral warm_start;

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
   * The cached value of the '{@link #getVerbose_interval() <em>Verbose interval</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVerbose_interval()
   * @generated
   * @ordered
   */
  protected IntegerLiteral verbose_interval;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GaussianMixtureImpl()
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
    return ThingMLPackage.eINSTANCE.getGaussianMixture();
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS, oldN_components, newN_components);
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
        msgs = ((InternalEObject)n_components).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS, null, msgs);
      if (newN_components != null)
        msgs = ((InternalEObject)newN_components).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS, null, msgs);
      msgs = basicSetN_components(newN_components, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS, newN_components, newN_components));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Covariance_Type getCovariance_type()
  {
    return covariance_type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCovariance_type(Covariance_Type newCovariance_type)
  {
    Covariance_Type oldCovariance_type = covariance_type;
    covariance_type = newCovariance_type == null ? COVARIANCE_TYPE_EDEFAULT : newCovariance_type;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__COVARIANCE_TYPE, oldCovariance_type, covariance_type));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__TOL, oldTol, newTol);
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
        msgs = ((InternalEObject)tol).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__TOL, null, msgs);
      if (newTol != null)
        msgs = ((InternalEObject)newTol).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__TOL, null, msgs);
      msgs = basicSetTol(newTol, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__TOL, newTol, newTol));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DoubleLiteral getReg_covar()
  {
    return reg_covar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetReg_covar(DoubleLiteral newReg_covar, NotificationChain msgs)
  {
    DoubleLiteral oldReg_covar = reg_covar;
    reg_covar = newReg_covar;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR, oldReg_covar, newReg_covar);
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
  public void setReg_covar(DoubleLiteral newReg_covar)
  {
    if (newReg_covar != reg_covar)
    {
      NotificationChain msgs = null;
      if (reg_covar != null)
        msgs = ((InternalEObject)reg_covar).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR, null, msgs);
      if (newReg_covar != null)
        msgs = ((InternalEObject)newReg_covar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR, null, msgs);
      msgs = basicSetReg_covar(newReg_covar, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR, newReg_covar, newReg_covar));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER, oldMax_iter, newMax_iter);
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
        msgs = ((InternalEObject)max_iter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER, null, msgs);
      if (newMax_iter != null)
        msgs = ((InternalEObject)newMax_iter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER, null, msgs);
      msgs = basicSetMax_iter(newMax_iter, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER, newMax_iter, newMax_iter));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__NINIT, oldN_init, newN_init);
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
        msgs = ((InternalEObject)n_init).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__NINIT, null, msgs);
      if (newN_init != null)
        msgs = ((InternalEObject)newN_init).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__NINIT, null, msgs);
      msgs = basicSetN_init(newN_init, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__NINIT, newN_init, newN_init));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Init_Params_Methods getInit_params()
  {
    return init_params;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setInit_params(Init_Params_Methods newInit_params)
  {
    Init_Params_Methods oldInit_params = init_params;
    init_params = newInit_params == null ? INIT_PARAMS_EDEFAULT : newInit_params;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__INIT_PARAMS, oldInit_params, init_params));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getWeights_init()
  {
    return weights_init;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setWeights_init(String newWeights_init)
  {
    String oldWeights_init = weights_init;
    weights_init = newWeights_init;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__WEIGHTS_INIT, oldWeights_init, weights_init));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getMeans_init()
  {
    return means_init;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMeans_init(String newMeans_init)
  {
    String oldMeans_init = means_init;
    means_init = newMeans_init;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__MEANS_INIT, oldMeans_init, means_init));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getPrecisions_init()
  {
    return precisions_init;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setPrecisions_init(String newPrecisions_init)
  {
    String oldPrecisions_init = precisions_init;
    precisions_init = newPrecisions_init;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__PRECISIONS_INIT, oldPrecisions_init, precisions_init));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE, oldRandom_state, newRandom_state);
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
        msgs = ((InternalEObject)random_state).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE, null, msgs);
      if (newRandom_state != null)
        msgs = ((InternalEObject)newRandom_state).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE, null, msgs);
      msgs = basicSetRandom_state(newRandom_state, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE, newRandom_state, newRandom_state));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START, oldWarm_start, newWarm_start);
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
        msgs = ((InternalEObject)warm_start).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START, null, msgs);
      if (newWarm_start != null)
        msgs = ((InternalEObject)newWarm_start).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START, null, msgs);
      msgs = basicSetWarm_start(newWarm_start, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START, newWarm_start, newWarm_start));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE, oldVerbose, newVerbose);
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
        msgs = ((InternalEObject)verbose).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE, null, msgs);
      if (newVerbose != null)
        msgs = ((InternalEObject)newVerbose).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE, null, msgs);
      msgs = basicSetVerbose(newVerbose, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE, newVerbose, newVerbose));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IntegerLiteral getVerbose_interval()
  {
    return verbose_interval;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVerbose_interval(IntegerLiteral newVerbose_interval, NotificationChain msgs)
  {
    IntegerLiteral oldVerbose_interval = verbose_interval;
    verbose_interval = newVerbose_interval;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL, oldVerbose_interval, newVerbose_interval);
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
  public void setVerbose_interval(IntegerLiteral newVerbose_interval)
  {
    if (newVerbose_interval != verbose_interval)
    {
      NotificationChain msgs = null;
      if (verbose_interval != null)
        msgs = ((InternalEObject)verbose_interval).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL, null, msgs);
      if (newVerbose_interval != null)
        msgs = ((InternalEObject)newVerbose_interval).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL, null, msgs);
      msgs = basicSetVerbose_interval(newVerbose_interval, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL, newVerbose_interval, newVerbose_interval));
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
      case ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS:
        return basicSetN_components(null, msgs);
      case ThingMLPackage.GAUSSIAN_MIXTURE__TOL:
        return basicSetTol(null, msgs);
      case ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR:
        return basicSetReg_covar(null, msgs);
      case ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER:
        return basicSetMax_iter(null, msgs);
      case ThingMLPackage.GAUSSIAN_MIXTURE__NINIT:
        return basicSetN_init(null, msgs);
      case ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE:
        return basicSetRandom_state(null, msgs);
      case ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START:
        return basicSetWarm_start(null, msgs);
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE:
        return basicSetVerbose(null, msgs);
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL:
        return basicSetVerbose_interval(null, msgs);
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
      case ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS:
        return getN_components();
      case ThingMLPackage.GAUSSIAN_MIXTURE__COVARIANCE_TYPE:
        return getCovariance_type();
      case ThingMLPackage.GAUSSIAN_MIXTURE__TOL:
        return getTol();
      case ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR:
        return getReg_covar();
      case ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER:
        return getMax_iter();
      case ThingMLPackage.GAUSSIAN_MIXTURE__NINIT:
        return getN_init();
      case ThingMLPackage.GAUSSIAN_MIXTURE__INIT_PARAMS:
        return getInit_params();
      case ThingMLPackage.GAUSSIAN_MIXTURE__WEIGHTS_INIT:
        return getWeights_init();
      case ThingMLPackage.GAUSSIAN_MIXTURE__MEANS_INIT:
        return getMeans_init();
      case ThingMLPackage.GAUSSIAN_MIXTURE__PRECISIONS_INIT:
        return getPrecisions_init();
      case ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE:
        return getRandom_state();
      case ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START:
        return getWarm_start();
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE:
        return getVerbose();
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL:
        return getVerbose_interval();
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
      case ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS:
        setN_components((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__COVARIANCE_TYPE:
        setCovariance_type((Covariance_Type)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__TOL:
        setTol((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR:
        setReg_covar((DoubleLiteral)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER:
        setMax_iter((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__NINIT:
        setN_init((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__INIT_PARAMS:
        setInit_params((Init_Params_Methods)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__WEIGHTS_INIT:
        setWeights_init((String)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__MEANS_INIT:
        setMeans_init((String)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__PRECISIONS_INIT:
        setPrecisions_init((String)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE:
        setRandom_state((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START:
        setWarm_start((BooleanLiteral)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE:
        setVerbose((IntegerLiteral)newValue);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL:
        setVerbose_interval((IntegerLiteral)newValue);
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
      case ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS:
        setN_components((IntegerLiteral)null);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__COVARIANCE_TYPE:
        setCovariance_type(COVARIANCE_TYPE_EDEFAULT);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__TOL:
        setTol((DoubleLiteral)null);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR:
        setReg_covar((DoubleLiteral)null);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER:
        setMax_iter((IntegerLiteral)null);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__NINIT:
        setN_init((IntegerLiteral)null);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__INIT_PARAMS:
        setInit_params(INIT_PARAMS_EDEFAULT);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__WEIGHTS_INIT:
        setWeights_init(WEIGHTS_INIT_EDEFAULT);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__MEANS_INIT:
        setMeans_init(MEANS_INIT_EDEFAULT);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__PRECISIONS_INIT:
        setPrecisions_init(PRECISIONS_INIT_EDEFAULT);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE:
        setRandom_state((IntegerLiteral)null);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START:
        setWarm_start((BooleanLiteral)null);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE:
        setVerbose((IntegerLiteral)null);
        return;
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL:
        setVerbose_interval((IntegerLiteral)null);
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
      case ThingMLPackage.GAUSSIAN_MIXTURE__NCOMPONENTS:
        return n_components != null;
      case ThingMLPackage.GAUSSIAN_MIXTURE__COVARIANCE_TYPE:
        return covariance_type != COVARIANCE_TYPE_EDEFAULT;
      case ThingMLPackage.GAUSSIAN_MIXTURE__TOL:
        return tol != null;
      case ThingMLPackage.GAUSSIAN_MIXTURE__REG_COVAR:
        return reg_covar != null;
      case ThingMLPackage.GAUSSIAN_MIXTURE__MAX_ITER:
        return max_iter != null;
      case ThingMLPackage.GAUSSIAN_MIXTURE__NINIT:
        return n_init != null;
      case ThingMLPackage.GAUSSIAN_MIXTURE__INIT_PARAMS:
        return init_params != INIT_PARAMS_EDEFAULT;
      case ThingMLPackage.GAUSSIAN_MIXTURE__WEIGHTS_INIT:
        return WEIGHTS_INIT_EDEFAULT == null ? weights_init != null : !WEIGHTS_INIT_EDEFAULT.equals(weights_init);
      case ThingMLPackage.GAUSSIAN_MIXTURE__MEANS_INIT:
        return MEANS_INIT_EDEFAULT == null ? means_init != null : !MEANS_INIT_EDEFAULT.equals(means_init);
      case ThingMLPackage.GAUSSIAN_MIXTURE__PRECISIONS_INIT:
        return PRECISIONS_INIT_EDEFAULT == null ? precisions_init != null : !PRECISIONS_INIT_EDEFAULT.equals(precisions_init);
      case ThingMLPackage.GAUSSIAN_MIXTURE__RANDOM_STATE:
        return random_state != null;
      case ThingMLPackage.GAUSSIAN_MIXTURE__WARM_START:
        return warm_start != null;
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE:
        return verbose != null;
      case ThingMLPackage.GAUSSIAN_MIXTURE__VERBOSE_INTERVAL:
        return verbose_interval != null;
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
    result.append(" (covariance_type: ");
    result.append(covariance_type);
    result.append(", init_params: ");
    result.append(init_params);
    result.append(", weights_init: ");
    result.append(weights_init);
    result.append(", means_init: ");
    result.append(means_init);
    result.append(", precisions_init: ");
    result.append(precisions_init);
    result.append(')');
    return result.toString();
  }

} //GaussianMixtureImpl
