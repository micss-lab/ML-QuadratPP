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
package org.thingml.xtext.thingML.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.thingml.xtext.thingML.ThingMLFactory;
import org.thingml.xtext.thingML.ThingMLPackage;
import org.thingml.xtext.thingML.XGBoost;

/**
 * This is the item provider adapter for a {@link org.thingml.xtext.thingML.XGBoost} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class XGBoostItemProvider extends machine_learning_ModelAlgorithmItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XGBoostItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addLearning_ratePropertyDescriptor(object);
			addObjectivePropertyDescriptor(object);
			addBoosterPropertyDescriptor(object);
			addGammaPropertyDescriptor(object);
			addMin_child_weightPropertyDescriptor(object);
			addSubsamplePropertyDescriptor(object);
			addColsample_bytreePropertyDescriptor(object);
			addColsample_bylevelPropertyDescriptor(object);
			addColsample_bynodePropertyDescriptor(object);
			addReg_alphaPropertyDescriptor(object);
			addReg_lambdaPropertyDescriptor(object);
			addScale_pos_weightPropertyDescriptor(object);
			addBase_scorePropertyDescriptor(object);
			addMissingPropertyDescriptor(object);
			addImportance_typePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Learning rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLearning_ratePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_learning_rate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_learning_rate_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Learning_rate(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Objective feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addObjectivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_objective_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_objective_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Objective(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Booster feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBoosterPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_booster_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_booster_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Booster(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Gamma feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGammaPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_gamma_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_gamma_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Gamma(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min child weight feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMin_child_weightPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_min_child_weight_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_min_child_weight_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Min_child_weight(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Subsample feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSubsamplePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_subsample_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_subsample_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Subsample(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Colsample bytree feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addColsample_bytreePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_colsample_bytree_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_colsample_bytree_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Colsample_bytree(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Colsample bylevel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addColsample_bylevelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_colsample_bylevel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_colsample_bylevel_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Colsample_bylevel(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Colsample bynode feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addColsample_bynodePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_colsample_bynode_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_colsample_bynode_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Colsample_bynode(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Reg alpha feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReg_alphaPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_reg_alpha_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_reg_alpha_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Reg_alpha(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Reg lambda feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReg_lambdaPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_reg_lambda_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_reg_lambda_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Reg_lambda(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Scale pos weight feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addScale_pos_weightPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_scale_pos_weight_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_scale_pos_weight_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Scale_pos_weight(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base score feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBase_scorePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_base_score_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_base_score_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Base_score(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Missing feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMissingPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_missing_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_missing_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Missing(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Importance type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addImportance_typePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XGBoost_importance_type_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XGBoost_importance_type_feature", "_UI_XGBoost_type"),
				 ThingMLPackage.eINSTANCE.getXGBoost_Importance_type(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getXGBoost_Max_depth());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getXGBoost_N_estimators());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getXGBoost_Random_state());
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns XGBoost.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/XGBoost"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((XGBoost)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_XGBoost_type") :
			getString("_UI_XGBoost_type") + " " + label;
	}


	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(XGBoost.class)) {
			case ThingMLPackage.XG_BOOST__LEARNING_RATE:
			case ThingMLPackage.XG_BOOST__OBJECTIVE:
			case ThingMLPackage.XG_BOOST__BOOSTER:
			case ThingMLPackage.XG_BOOST__GAMMA:
			case ThingMLPackage.XG_BOOST__MIN_CHILD_WEIGHT:
			case ThingMLPackage.XG_BOOST__SUBSAMPLE:
			case ThingMLPackage.XG_BOOST__COLSAMPLE_BYTREE:
			case ThingMLPackage.XG_BOOST__COLSAMPLE_BYLEVEL:
			case ThingMLPackage.XG_BOOST__COLSAMPLE_BYNODE:
			case ThingMLPackage.XG_BOOST__REG_ALPHA:
			case ThingMLPackage.XG_BOOST__REG_LAMBDA:
			case ThingMLPackage.XG_BOOST__SCALE_POS_WEIGHT:
			case ThingMLPackage.XG_BOOST__BASE_SCORE:
			case ThingMLPackage.XG_BOOST__MISSING:
			case ThingMLPackage.XG_BOOST__IMPORTANCE_TYPE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ThingMLPackage.XG_BOOST__MAX_DEPTH:
			case ThingMLPackage.XG_BOOST__NESTIMATORS:
			case ThingMLPackage.XG_BOOST__RANDOM_STATE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getXGBoost_Max_depth(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getXGBoost_N_estimators(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getXGBoost_Random_state(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == ThingMLPackage.eINSTANCE.getXGBoost_Max_depth() ||
			childFeature == ThingMLPackage.eINSTANCE.getXGBoost_N_estimators() ||
			childFeature == ThingMLPackage.eINSTANCE.getXGBoost_Random_state();

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
