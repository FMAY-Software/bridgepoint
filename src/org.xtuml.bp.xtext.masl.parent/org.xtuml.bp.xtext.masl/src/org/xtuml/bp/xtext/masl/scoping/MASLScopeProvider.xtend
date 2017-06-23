/*
 * generated by Xtext 2.9.2
 */
package org.xtuml.bp.xtext.masl.scoping

import com.google.inject.Inject
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.resource.EObjectDescription
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.impl.MapBasedScope
import org.eclipse.xtext.scoping.impl.SimpleScope
import org.xtuml.bp.xtext.masl.MASLExtensions
import org.xtuml.bp.xtext.masl.lib.MASLLibraryProvider
import org.xtuml.bp.xtext.masl.masl.behavior.AssignStatement
import org.xtuml.bp.xtext.masl.masl.behavior.AttributeReferential
import org.xtuml.bp.xtext.masl.masl.behavior.BehaviorPackage
import org.xtuml.bp.xtext.masl.masl.behavior.CharacteristicCall
import org.xtuml.bp.xtext.masl.masl.behavior.CodeBlock
import org.xtuml.bp.xtext.masl.masl.behavior.CreateExpression
import org.xtuml.bp.xtext.masl.masl.behavior.Expression
import org.xtuml.bp.xtext.masl.masl.behavior.FindExpression
import org.xtuml.bp.xtext.masl.masl.behavior.ForStatement
import org.xtuml.bp.xtext.masl.masl.behavior.GenerateStatement
import org.xtuml.bp.xtext.masl.masl.behavior.NavigateExpression
import org.xtuml.bp.xtext.masl.masl.behavior.SimpleFeatureCall
import org.xtuml.bp.xtext.masl.masl.behavior.SortOrderFeature
import org.xtuml.bp.xtext.masl.masl.behavior.TerminatorActionCall
import org.xtuml.bp.xtext.masl.masl.behavior.VariableDeclaration
import org.xtuml.bp.xtext.masl.masl.structure.AbstractActionDefinition
import org.xtuml.bp.xtext.masl.masl.structure.AssocRelationshipDefinition
import org.xtuml.bp.xtext.masl.masl.structure.AttributeDefinition
import org.xtuml.bp.xtext.masl.masl.structure.Characteristic
import org.xtuml.bp.xtext.masl.masl.structure.DomainDefinition
import org.xtuml.bp.xtext.masl.masl.structure.ObjectDeclaration
import org.xtuml.bp.xtext.masl.masl.structure.ObjectDefinition
import org.xtuml.bp.xtext.masl.masl.structure.ObjectServiceDefinition
import org.xtuml.bp.xtext.masl.masl.structure.Parameter
import org.xtuml.bp.xtext.masl.masl.structure.RegularRelationshipDefinition
import org.xtuml.bp.xtext.masl.masl.structure.RelationshipEnd
import org.xtuml.bp.xtext.masl.masl.structure.RelationshipNavigation
import org.xtuml.bp.xtext.masl.masl.structure.StateDefinition
import org.xtuml.bp.xtext.masl.masl.structure.StructurePackage
import org.xtuml.bp.xtext.masl.masl.structure.SubtypeRelationshipDefinition
import org.xtuml.bp.xtext.masl.masl.structure.TerminatorDefinition
import org.xtuml.bp.xtext.masl.masl.structure.TransitionOption
import org.xtuml.bp.xtext.masl.masl.structure.TransitionRow
import org.xtuml.bp.xtext.masl.masl.types.StructureComponentDefinition
import org.xtuml.bp.xtext.masl.typesystem.CollectionType
import org.xtuml.bp.xtext.masl.typesystem.EnumType
import org.xtuml.bp.xtext.masl.typesystem.InstanceType
import org.xtuml.bp.xtext.masl.typesystem.MaslType
import org.xtuml.bp.xtext.masl.typesystem.MaslTypeProvider
import org.xtuml.bp.xtext.masl.typesystem.NamedType
import org.xtuml.bp.xtext.masl.typesystem.StructureType
import org.xtuml.bp.xtext.masl.typesystem.TypeOfType
import org.xtuml.bp.xtext.masl.typesystem.TypeParameterResolver

import static org.eclipse.xtext.scoping.Scopes.*

import static extension org.eclipse.xtext.EcoreUtil2.*
import org.xtuml.bp.xtext.masl.typesystem.BuiltinType
import org.xtuml.bp.xtext.masl.masl.behavior.Equality
import org.xtuml.bp.xtext.masl.masl.behavior.RelationalExp
import org.xtuml.bp.xtext.masl.masl.behavior.CaseAlternative
import org.xtuml.bp.xtext.masl.masl.behavior.CaseStatement

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
class MASLScopeProvider extends AbstractMASLScopeProvider {

	@Inject extension StructurePackage structurePackage
	@Inject extension BehaviorPackage 
	@Inject extension MASLExtensions
	@Inject extension MaslTypeProvider
	@Inject extension ProjectScopeIndexProvider
	@Inject extension TypeParameterResolver

	override getScope(EObject context, EReference reference) {
		switch reference {
			case featureCall_Feature:
				return context.featureScope
			case generateStatement_Event: {
				if(context instanceof GenerateStatement) {
					val contextObject = context.object?:context.containerObject
					if(contextObject != null)	 				
						return createObjectScope(contextObject, [events], super.getScope(context, reference))
				}
			}
			case createArgument_CurrentState: {
				val contextObject = context.getContainerOfType(CreateExpression)?.object
				if(contextObject != null) 
					return createObjectScope(contextObject, [states])				
			}
			case createArgument_Attribute: {
				val contextObject = context.getContainerOfType(CreateExpression)?.object
				if(contextObject != null) 
					return createObjectScope(contextObject, [attributes])				
			}
			case attributeReferential_Attribute: {
				if(context instanceof AttributeReferential) 
					return createObjectScope(context.referredObject, [attributes])
			}
			case transitionRow_Start: {
				if(context instanceof TransitionRow) 
					return createObjectScope(context.getContainerOfType(ObjectDefinition), [states])
			}
			case transitionOption_Event: {
                if(context instanceof TransitionOption) {
                    val contextObject = context?.eventObject
                    if(contextObject != null)
                        return createObjectScope(contextObject, [events]) 
                }
            }
			case transitionOption_EndState: {
				if(context instanceof TransitionOption) 
					return createObjectScope(context.getContainerOfType(ObjectDefinition), [states])		
			}
			case terminatorActionCall_TerminatorAction: {
				if(context instanceof TerminatorActionCall) {
					val receiver = context?.receiver
					if(receiver instanceof SimpleFeatureCall) {
						val feature = receiver.feature
						if(feature instanceof TerminatorDefinition) {
							return scopeFor(feature.services)		
						}						
					}
				} 
			}
			case characteristicCall_Characteristic: {
				if(context instanceof CharacteristicCall) 
					return createCharacteristicScope(context)
			}
			case structurePackage.relationshipNavigation_ObjectOrRole: {
				if(context instanceof RelationshipNavigation) {
					val receiverType = context.receiver.maslType.componentType
					val relationShip = context.relationship
					switch relationShip {
						RegularRelationshipDefinition: {
							val referrables = newArrayList()
							for(end: #[relationShip.forwards, relationShip.backwards]) {
								if(receiverType == BuiltinType.MISSING_TYPE || end.from.maslType.componentType == receiverType) {
									referrables += EObjectDescription.create(
										QualifiedName.create(end.name), end)
									referrables += EObjectDescription.create(
										QualifiedName.create(end.to.name), end)
									referrables += qualifiedDescription(end)
								}
							}
							return new SimpleScope(referrables)
						}
						AssocRelationshipDefinition: {
							val referrables = newArrayList()
							val receiverIsAssocObject = relationShip.object.maslType.componentType == receiverType
							for(end: #[relationShip.forwards, relationShip.backwards]) {
								if(receiverType == BuiltinType.MISSING_TYPE || end.from.maslType.componentType == receiverType || receiverIsAssocObject) {
									referrables += EObjectDescription.create(
										QualifiedName.create(end.name), end)
									referrables += EObjectDescription.create(
										QualifiedName.create(end.to.name), end)
									referrables += qualifiedDescription(end)
								}
							}
							referrables += EObjectDescription.create(
								QualifiedName.create(relationShip.object.name), relationShip.object)
							referrables += EObjectDescription.create(
								QualifiedName.create(relationShip.forwards.name + '.' + relationShip.object.name), relationShip.object
							)
							referrables += EObjectDescription.create(
								QualifiedName.create(relationShip.backwards.name + '.' + relationShip.object.name), relationShip.object
							)
							return new SimpleScope(referrables)
						}
						SubtypeRelationshipDefinition:
							return scopeFor(relationShip.subtypes + #[relationShip.supertype]) 
							
					}
 				}
			}
		}
		super.getScope(context, reference)
	}
	
	private def qualifiedDescription(RelationshipEnd end) {
		EObjectDescription.create(QualifiedName.create(end.name + '.' + end.to.name), end)
	}
	
	private def <T extends EObject> createObjectScope(ObjectDefinition object, (ObjectDefinition)=>Iterable<? extends T> reference, IScope parentScope) {
		if(object == null || object.eIsProxy)
			return IScope.NULLSCOPE
		val index = object.index
		scopeFor(object
			.getAllSupertypes(index)
			.map[reference.apply(it)]
			.flatten, parentScope)
	}
	
	private def <T extends EObject> createObjectScope(ObjectDefinition object, (ObjectDefinition)=>Iterable<? extends T> reference) {
		createObjectScope(object, reference, IScope.NULLSCOPE)
	}

	private def <T extends EObject> createObjectScope(ObjectDeclaration object, (ObjectDefinition)=>Iterable<? extends T> reference, IScope parentScope) {
		if(object == null || object.eIsProxy)
			return IScope.NULLSCOPE
		val index = object.index
		val definition = object.getObjectDefinition(index)
		scopeFor(definition
			.getAllSupertypes(index)
			.map[reference.apply(it)]
			.flatten, parentScope)
	}

	private def <T extends EObject> createObjectScope(ObjectDeclaration object, (ObjectDefinition)=>Iterable<? extends T> reference) {
		createObjectScope(object, reference, IScope.NULLSCOPE)
	}
	
	private def dispatch IScope getFeatureScope(SortOrderFeature call) {
		call.getContainerOfType(NavigateExpression).maslType.componentType.typeFeatureScope
	}
	
	private def dispatch IScope getFeatureScope(SimpleFeatureCall call) {
		if(call.receiver == null) {
			val localFeatureScope = call.getLocalSimpleFeatureScope(delegate.getScope(call, featureCall_Feature), call.eContainmentFeature, false) 
			val parent = call.eContainer
			switch parent {
				AttributeDefinition case call == parent.defaultValue: 
					return getEnumDisambiguationScope(parent.type.maslType, localFeatureScope)
				VariableDeclaration case call == parent.expression: 
					return getEnumDisambiguationScope(parent.type.maslType, localFeatureScope)
				AssignStatement case call == parent.rhs: 
					return getEnumDisambiguationScope(parent.lhs.maslType, localFeatureScope)
				StructureComponentDefinition case call == parent.defaultValue: 
					return getEnumDisambiguationScope(parent.type.maslType, localFeatureScope)
				CaseAlternative case parent.choices.contains(call):
					return getEnumDisambiguationScope((parent.eContainer as CaseStatement).value.maslType, localFeatureScope)
			}
			return localFeatureScope
		} else {
			val type = call.receiver.maslType
			return getTypeFeatureScope(type)
		}
	}
	
	private def IScope getEnumDisambiguationScope(MaslType maslType, IScope parent) {
		val enumType = maslType.expectedEnumType
		if(enumType !== null) {
			val domainName = enumType.enumType.domainName
			return MapBasedScope.createScope(parent, enumType.enumType.enumerators.map[
				#[
					EObjectDescription.create(QualifiedName.create(name), it),
					EObjectDescription.create(QualifiedName.create(domainName, name), it)
				]
			].flatten)
		} else {
			return parent
		}
	} 
	
	private def EnumType getExpectedEnumType(MaslType maslType) {
		switch maslType {
			EnumType:
				return maslType
			CollectionType:
				if(maslType.componentType instanceof EnumType)
					return maslType.componentType as EnumType
			StructureType:
				if(maslType.components.size == 1 && maslType.components.head.type instanceof EnumType)
					return maslType.components.head.type as EnumType
		}
		return null
	}
	
	private def IScope getTypeFeatureScope(MaslType type) {
		switch type {
			InstanceType:
				return type.instance.createObjectScope[attributes + services]
			NamedType: {
				val innerType = type.type
				if (innerType instanceof StructureType)
					return scopeFor(innerType.structureType.components)
			}
			TypeOfType: {
				if(type.type instanceof EnumType)
					return scopeFor((type.type as EnumType).enumType.enumerators)
			} 
			default:
				return IScope.NULLSCOPE
		}
	}

	private def IScope getLocalSimpleFeatureScope(EObject expr, IScope parentScope, EReference containmentFeature, boolean isRightHandSide) {
		if(expr == null)
			return IScope.NULLSCOPE
		val parent = expr.eContainer
		switch expr {
			Equality case containmentFeature == equality_Rhs:
				return parent.getLocalSimpleFeatureScope(parentScope, expr.eContainmentFeature, true)
			RelationalExp case containmentFeature == relationalExp_Rhs:
				return parent.getLocalSimpleFeatureScope(parentScope, expr.eContainmentFeature, true)
			FindExpression case containmentFeature == findExpression_Where && !isRightHandSide: {
				val whereScope = getWhereScope(expr.expression, parentScope)
				if(whereScope != null)
					return whereScope
			}
			NavigateExpression case containmentFeature == navigateExpression_Where && !isRightHandSide: {
				val whereScope = getWhereScope(expr, parentScope)
				if(whereScope != null)
					return whereScope
			}
			CodeBlock:
				return scopeFor(expr.variables, parent.getLocalSimpleFeatureScope(parentScope, expr.eContainmentFeature, isRightHandSide))
			ForStatement:
				return scopeFor(#[expr.variable], parent.getLocalSimpleFeatureScope(parentScope, expr.eContainmentFeature, isRightHandSide))
			ObjectServiceDefinition:
				return getSimpleFeatureScopeForObjectAction(expr.parameters, expr.getObject, parentScope)
			StateDefinition:
				return getSimpleFeatureScopeForObjectAction(expr.parameters, expr.object, parentScope)
			AbstractActionDefinition:
				return scopeFor(expr.parameters, parentScope)
			DomainDefinition:
				return parentScope
		}
		return parent.getLocalSimpleFeatureScope(parentScope, expr.eContainmentFeature, isRightHandSide)
	}
	
	
	private def getWhereScope(Expression expression, IScope parentScope) {
		val maslType = expression.maslType
		val instance = 
			switch maslType {
				InstanceType: 
					 maslType.instance
				CollectionType case maslType.componentType instanceof InstanceType:
					(maslType.componentType as InstanceType).instance
				default:
					null							
			}
		if (instance != null)
			return instance.createObjectScope([attributes + services], expression.eContainer.getLocalSimpleFeatureScope(parentScope, expression.eContainmentFeature, false))
		else 
			return null
	}

	private def getSimpleFeatureScopeForObjectAction(List<Parameter> parameters, ObjectDeclaration context, IScope parentScope) {
		scopeFor(parameters, context.createObjectScope([attributes  + services], parentScope))
	}
	
	private def dispatch IScope getFeatureScope(EObject call) {
		IScope.NULLSCOPE
	}
	
	private def createCharacteristicScope(CharacteristicCall call) {
		val callReceiverType = call.receiver?.maslType
		if(callReceiverType != null) {
			val libResource = call.eResource.resourceSet.getResource(MASLLibraryProvider.MODEL_URI, true)
			val characteristics = libResource.contents.head.getAllContentsOfType(Characteristic)
			val characteristicsForReceiver = characteristics.filter [ c |
				val charReceiverType = if(c.isForValue)
						c.receiverType.maslTypeOfTypeReference
					else
						new TypeOfType(c.receiverType.maslTypeOfTypeReference) 
				return callReceiverType.matchesParameterized(charReceiverType)
			]
			return scopeFor(characteristicsForReceiver)
		} else {
			return IScope.NULLSCOPE
		}
	}
}
