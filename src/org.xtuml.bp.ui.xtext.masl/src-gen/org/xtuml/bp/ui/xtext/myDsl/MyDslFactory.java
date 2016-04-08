/**
 * generated by Xtext 2.9.1
 */
package org.xtuml.bp.ui.xtext.myDsl;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.xtuml.bp.ui.xtext.myDsl.MyDslPackage
 * @generated
 */
public interface MyDslFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MyDslFactory eINSTANCE = org.xtuml.bp.ui.xtext.myDsl.impl.MyDslFactoryImpl.init();

  /**
   * Returns a new object of class '<em>definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>definition</em>'.
   * @generated
   */
  definition createdefinition();

  /**
   * Returns a new object of class '<em>type Reference</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>type Reference</em>'.
   * @generated
   */
  typeReference createtypeReference();

  /**
   * Returns a new object of class '<em>type Reference With CA</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>type Reference With CA</em>'.
   * @generated
   */
  typeReferenceWithCA createtypeReferenceWithCA();

  /**
   * Returns a new object of class '<em>constrained Array Type Ref</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>constrained Array Type Ref</em>'.
   * @generated
   */
  constrainedArrayTypeRef createconstrainedArrayTypeRef();

  /**
   * Returns a new object of class '<em>array Bounds</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>array Bounds</em>'.
   * @generated
   */
  arrayBounds createarrayBounds();

  /**
   * Returns a new object of class '<em>collection Type Ref</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>collection Type Ref</em>'.
   * @generated
   */
  collectionTypeRef createcollectionTypeRef();

  /**
   * Returns a new object of class '<em>dict Key Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>dict Key Type</em>'.
   * @generated
   */
  dictKeyType createdictKeyType();

  /**
   * Returns a new object of class '<em>dict Value Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>dict Value Type</em>'.
   * @generated
   */
  dictValueType createdictValueType();

  /**
   * Returns a new object of class '<em>relationship Spec</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>relationship Spec</em>'.
   * @generated
   */
  relationshipSpec createrelationshipSpec();

  /**
   * Returns a new object of class '<em>state Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>state Type</em>'.
   * @generated
   */
  stateType createstateType();

  /**
   * Returns a new object of class '<em>parameter List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>parameter List</em>'.
   * @generated
   */
  parameterList createparameterList();

  /**
   * Returns a new object of class '<em>parameter Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>parameter Definition</em>'.
   * @generated
   */
  parameterDefinition createparameterDefinition();

  /**
   * Returns a new object of class '<em>service Visibility</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>service Visibility</em>'.
   * @generated
   */
  serviceVisibility createserviceVisibility();

  /**
   * Returns a new object of class '<em>parameter Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>parameter Type</em>'.
   * @generated
   */
  parameterType createparameterType();

  /**
   * Returns a new object of class '<em>return Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>return Type</em>'.
   * @generated
   */
  returnType createreturnType();

  /**
   * Returns a new object of class '<em>domain Service Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>domain Service Definition</em>'.
   * @generated
   */
  domainServiceDefinition createdomainServiceDefinition();

  /**
   * Returns a new object of class '<em>domain Function Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>domain Function Definition</em>'.
   * @generated
   */
  domainFunctionDefinition createdomainFunctionDefinition();

  /**
   * Returns a new object of class '<em>object Service Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>object Service Definition</em>'.
   * @generated
   */
  objectServiceDefinition createobjectServiceDefinition();

  /**
   * Returns a new object of class '<em>terminator Service Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>terminator Service Definition</em>'.
   * @generated
   */
  terminatorServiceDefinition createterminatorServiceDefinition();

  /**
   * Returns a new object of class '<em>terminator Function Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>terminator Function Definition</em>'.
   * @generated
   */
  terminatorFunctionDefinition createterminatorFunctionDefinition();

  /**
   * Returns a new object of class '<em>object Function Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>object Function Definition</em>'.
   * @generated
   */
  objectFunctionDefinition createobjectFunctionDefinition();

  /**
   * Returns a new object of class '<em>state Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>state Definition</em>'.
   * @generated
   */
  stateDefinition createstateDefinition();

  /**
   * Returns a new object of class '<em>statement List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>statement List</em>'.
   * @generated
   */
  statementList createstatementList();

  /**
   * Returns a new object of class '<em>statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>statement</em>'.
   * @generated
   */
  statement createstatement();

  /**
   * Returns a new object of class '<em>assign Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>assign Statement</em>'.
   * @generated
   */
  assignStatement createassignStatement();

  /**
   * Returns a new object of class '<em>stream Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>stream Value</em>'.
   * @generated
   */
  streamValue createstreamValue();

  /**
   * Returns a new object of class '<em>stream Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>stream Statement</em>'.
   * @generated
   */
  streamStatement createstreamStatement();

  /**
   * Returns a new object of class '<em>call Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>call Statement</em>'.
   * @generated
   */
  callStatement createcallStatement();

  /**
   * Returns a new object of class '<em>exit Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>exit Statement</em>'.
   * @generated
   */
  exitStatement createexitStatement();

  /**
   * Returns a new object of class '<em>return Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>return Statement</em>'.
   * @generated
   */
  returnStatement createreturnStatement();

  /**
   * Returns a new object of class '<em>delay Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>delay Statement</em>'.
   * @generated
   */
  delayStatement createdelayStatement();

  /**
   * Returns a new object of class '<em>delete Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>delete Statement</em>'.
   * @generated
   */
  deleteStatement createdeleteStatement();

  /**
   * Returns a new object of class '<em>erase Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>erase Statement</em>'.
   * @generated
   */
  eraseStatement createeraseStatement();

  /**
   * Returns a new object of class '<em>link Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>link Statement</em>'.
   * @generated
   */
  linkStatement createlinkStatement();

  /**
   * Returns a new object of class '<em>schedule Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>schedule Statement</em>'.
   * @generated
   */
  scheduleStatement createscheduleStatement();

  /**
   * Returns a new object of class '<em>cancel Timer Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>cancel Timer Statement</em>'.
   * @generated
   */
  cancelTimerStatement createcancelTimerStatement();

  /**
   * Returns a new object of class '<em>generate Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>generate Statement</em>'.
   * @generated
   */
  generateStatement creategenerateStatement();

  /**
   * Returns a new object of class '<em>if Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>if Statement</em>'.
   * @generated
   */
  ifStatement createifStatement();

  /**
   * Returns a new object of class '<em>elsif Block</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>elsif Block</em>'.
   * @generated
   */
  elsifBlock createelsifBlock();

  /**
   * Returns a new object of class '<em>else Block</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>else Block</em>'.
   * @generated
   */
  elseBlock createelseBlock();

  /**
   * Returns a new object of class '<em>while Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>while Statement</em>'.
   * @generated
   */
  whileStatement createwhileStatement();

  /**
   * Returns a new object of class '<em>condition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>condition</em>'.
   * @generated
   */
  condition createcondition();

  /**
   * Returns a new object of class '<em>case Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>case Statement</em>'.
   * @generated
   */
  caseStatement createcaseStatement();

  /**
   * Returns a new object of class '<em>case Alternative</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>case Alternative</em>'.
   * @generated
   */
  caseAlternative createcaseAlternative();

  /**
   * Returns a new object of class '<em>choice List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>choice List</em>'.
   * @generated
   */
  choiceList createchoiceList();

  /**
   * Returns a new object of class '<em>case Others</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>case Others</em>'.
   * @generated
   */
  caseOthers createcaseOthers();

  /**
   * Returns a new object of class '<em>for Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>for Statement</em>'.
   * @generated
   */
  forStatement createforStatement();

  /**
   * Returns a new object of class '<em>loop Variable Spec</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>loop Variable Spec</em>'.
   * @generated
   */
  loopVariableSpec createloopVariableSpec();

  /**
   * Returns a new object of class '<em>code Block Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>code Block Statement</em>'.
   * @generated
   */
  codeBlockStatement createcodeBlockStatement();

  /**
   * Returns a new object of class '<em>code Block</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>code Block</em>'.
   * @generated
   */
  codeBlock createcodeBlock();

  /**
   * Returns a new object of class '<em>variable Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>variable Declaration</em>'.
   * @generated
   */
  variableDeclaration createvariableDeclaration();

  /**
   * Returns a new object of class '<em>exception Handler</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>exception Handler</em>'.
   * @generated
   */
  exceptionHandler createexceptionHandler();

  /**
   * Returns a new object of class '<em>other Handler</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>other Handler</em>'.
   * @generated
   */
  otherHandler createotherHandler();

  /**
   * Returns a new object of class '<em>find Condition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Condition</em>'.
   * @generated
   */
  findCondition createfindCondition();

  /**
   * Returns a new object of class '<em>find Logical Or</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Logical Or</em>'.
   * @generated
   */
  findLogicalOr createfindLogicalOr();

  /**
   * Returns a new object of class '<em>find Logical Xor</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Logical Xor</em>'.
   * @generated
   */
  findLogicalXor createfindLogicalXor();

  /**
   * Returns a new object of class '<em>find Logical And</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Logical And</em>'.
   * @generated
   */
  findLogicalAnd createfindLogicalAnd();

  /**
   * Returns a new object of class '<em>find Primary</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Primary</em>'.
   * @generated
   */
  findPrimary createfindPrimary();

  /**
   * Returns a new object of class '<em>find Unary</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Unary</em>'.
   * @generated
   */
  findUnary createfindUnary();

  /**
   * Returns a new object of class '<em>find Comparison</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Comparison</em>'.
   * @generated
   */
  findComparison createfindComparison();

  /**
   * Returns a new object of class '<em>find Name</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Name</em>'.
   * @generated
   */
  findName createfindName();

  /**
   * Returns a new object of class '<em>expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>expression</em>'.
   * @generated
   */
  expression createexpression();

  /**
   * Returns a new object of class '<em>range Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>range Expression</em>'.
   * @generated
   */
  rangeExpression createrangeExpression();

  /**
   * Returns a new object of class '<em>logical Or</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>logical Or</em>'.
   * @generated
   */
  logicalOr createlogicalOr();

  /**
   * Returns a new object of class '<em>logical Xor</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>logical Xor</em>'.
   * @generated
   */
  logicalXor createlogicalXor();

  /**
   * Returns a new object of class '<em>logical And</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>logical And</em>'.
   * @generated
   */
  logicalAnd createlogicalAnd();

  /**
   * Returns a new object of class '<em>equality</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>equality</em>'.
   * @generated
   */
  equality createequality();

  /**
   * Returns a new object of class '<em>relational Exp</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>relational Exp</em>'.
   * @generated
   */
  relationalExp createrelationalExp();

  /**
   * Returns a new object of class '<em>additive Exp</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>additive Exp</em>'.
   * @generated
   */
  additiveExp createadditiveExp();

  /**
   * Returns a new object of class '<em>mult Exp</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>mult Exp</em>'.
   * @generated
   */
  multExp createmultExp();

  /**
   * Returns a new object of class '<em>unary Exp</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>unary Exp</em>'.
   * @generated
   */
  unaryExp createunaryExp();

  /**
   * Returns a new object of class '<em>link Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>link Expression</em>'.
   * @generated
   */
  linkExpression createlinkExpression();

  /**
   * Returns a new object of class '<em>navigate Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>navigate Expression</em>'.
   * @generated
   */
  navigateExpression createnavigateExpression();

  /**
   * Returns a new object of class '<em>extended Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>extended Expression</em>'.
   * @generated
   */
  extendedExpression createextendedExpression();

  /**
   * Returns a new object of class '<em>create Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>create Expression</em>'.
   * @generated
   */
  createExpression createcreateExpression();

  /**
   * Returns a new object of class '<em>create Argument List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>create Argument List</em>'.
   * @generated
   */
  createArgumentList createcreateArgumentList();

  /**
   * Returns a new object of class '<em>create Argument</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>create Argument</em>'.
   * @generated
   */
  createArgument createcreateArgument();

  /**
   * Returns a new object of class '<em>find Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>find Expression</em>'.
   * @generated
   */
  findExpression createfindExpression();

  /**
   * Returns a new object of class '<em>where Clause</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>where Clause</em>'.
   * @generated
   */
  whereClause createwhereClause();

  /**
   * Returns a new object of class '<em>postfix Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>postfix Expression</em>'.
   * @generated
   */
  postfixExpression createpostfixExpression();

  /**
   * Returns a new object of class '<em>postfix No Call Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>postfix No Call Expression</em>'.
   * @generated
   */
  postfixNoCallExpression createpostfixNoCallExpression();

  /**
   * Returns a new object of class '<em>primary Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>primary Expression</em>'.
   * @generated
   */
  primaryExpression createprimaryExpression();

  /**
   * Returns a new object of class '<em>type Name Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>type Name Expression</em>'.
   * @generated
   */
  typeNameExpression createtypeNameExpression();

  /**
   * Returns a new object of class '<em>parenthesised Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>parenthesised Expression</em>'.
   * @generated
   */
  parenthesisedExpression createparenthesisedExpression();

  /**
   * Returns a new object of class '<em>argument List</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>argument List</em>'.
   * @generated
   */
  argumentList createargumentList();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  MyDslPackage getMyDslPackage();

} //MyDslFactory