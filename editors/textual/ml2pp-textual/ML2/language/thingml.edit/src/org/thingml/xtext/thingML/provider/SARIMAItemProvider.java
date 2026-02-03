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

import org.thingml.xtext.thingML.SARIMA;
import org.thingml.xtext.thingML.ThingMLFactory;
import org.thingml.xtext.thingML.ThingMLPackage;

/**
 * This is the item provider adapter for a {@link org.thingml.xtext.thingML.SARIMA} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SARIMAItemProvider extends statistical_ModelAlgorithmItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SARIMAItemProvider(AdapterFactory adapterFactory) {
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

			addTrendPropertyDescriptor(object);
			addStart_paramsPropertyDescriptor(object);
			addMethodPropertyDescriptor(object);
			addSolverPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Trend feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTrendPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SARIMA_trend_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SARIMA_trend_feature", "_UI_SARIMA_type"),
				 ThingMLPackage.eINSTANCE.getSARIMA_Trend(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start params feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStart_paramsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SARIMA_start_params_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SARIMA_start_params_feature", "_UI_SARIMA_type"),
				 ThingMLPackage.eINSTANCE.getSARIMA_Start_params(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Method feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMethodPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SARIMA_method_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SARIMA_method_feature", "_UI_SARIMA_type"),
				 ThingMLPackage.eINSTANCE.getSARIMA_Method(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Solver feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSolverPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SARIMA_solver_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SARIMA_solver_feature", "_UI_SARIMA_type"),
				 ThingMLPackage.eINSTANCE.getSARIMA_Solver(),
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
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_P());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_Differencing());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_Q());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_SeasonalP());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_SeasonalD());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_SeasonalQ());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_Seasonality());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_Transparams());
			childrenFeatures.add(ThingMLPackage.eINSTANCE.getSARIMA_Maxiter());
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
	 * This returns SARIMA.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SARIMA"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SARIMA)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_SARIMA_type") :
			getString("_UI_SARIMA_type") + " " + label;
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

		switch (notification.getFeatureID(SARIMA.class)) {
			case ThingMLPackage.SARIMA__TREND:
			case ThingMLPackage.SARIMA__START_PARAMS:
			case ThingMLPackage.SARIMA__METHOD:
			case ThingMLPackage.SARIMA__SOLVER:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ThingMLPackage.SARIMA__P:
			case ThingMLPackage.SARIMA__DIFFERENCING:
			case ThingMLPackage.SARIMA__Q:
			case ThingMLPackage.SARIMA__SEASONAL_P:
			case ThingMLPackage.SARIMA__SEASONAL_D:
			case ThingMLPackage.SARIMA__SEASONAL_Q:
			case ThingMLPackage.SARIMA__SEASONALITY:
			case ThingMLPackage.SARIMA__TRANSPARAMS:
			case ThingMLPackage.SARIMA__MAXITER:
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
				(ThingMLPackage.eINSTANCE.getSARIMA_P(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getSARIMA_Differencing(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getSARIMA_Q(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getSARIMA_SeasonalP(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getSARIMA_SeasonalD(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getSARIMA_SeasonalQ(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getSARIMA_Seasonality(),
				 ThingMLFactory.eINSTANCE.createIntegerLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getSARIMA_Transparams(),
				 ThingMLFactory.eINSTANCE.createBooleanLiteral()));

		newChildDescriptors.add
			(createChildParameter
				(ThingMLPackage.eINSTANCE.getSARIMA_Maxiter(),
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
			childFeature == ThingMLPackage.eINSTANCE.getSARIMA_P() ||
			childFeature == ThingMLPackage.eINSTANCE.getSARIMA_Differencing() ||
			childFeature == ThingMLPackage.eINSTANCE.getSARIMA_Q() ||
			childFeature == ThingMLPackage.eINSTANCE.getSARIMA_SeasonalP() ||
			childFeature == ThingMLPackage.eINSTANCE.getSARIMA_SeasonalD() ||
			childFeature == ThingMLPackage.eINSTANCE.getSARIMA_SeasonalQ() ||
			childFeature == ThingMLPackage.eINSTANCE.getSARIMA_Seasonality() ||
			childFeature == ThingMLPackage.eINSTANCE.getSARIMA_Maxiter();

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
