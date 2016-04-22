/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.sparql.algebra.walker;

import java.util.Objects ;

import org.apache.jena.sparql.algebra.* ;
import org.apache.jena.sparql.core.VarExprList ;
import org.apache.jena.sparql.expr.* ;

/** Walk algebra, {@link Op}s and {@link Expr}s. */
public class Walker {

    /** Walk visiting every {@link Op} with an {@link OpVisitor},
     * including inside expressions.
     */
    public static void walk(Op op, OpVisitor opVisitor) {
        Objects.requireNonNull(opVisitor) ;
        walk(op, opVisitor, null);
    }
    
    /** Walk visiting every {@link Op} and every {@link Expr},
     *  starting from an {@link Op}.
     */
    public static void walk(Op op, OpVisitor opVisitor, ExprVisitor exprVisitor) {
        walk(op, opVisitor, exprVisitor, null, null) ; 
    }
    
    /** Walk visiting every {@link Op} and every {@link Expr},
     *  starting from an {@link Op}.
     */
    public static void walk(Op op, OpVisitor opVisitor, ExprVisitor exprVisitor, OpVisitor beforeVisitor, OpVisitor afterVisitor) {
        if ( op == null )
            return ;
        createWalker(opVisitor, exprVisitor, beforeVisitor, afterVisitor).walk(op);
    }

    /** Walk visiting every {@link Expr} with an {@link ExprVisitor},
     * including inside any {@link Op} in expressions.
     */
    public static void walk(Expr expr, ExprVisitor exprVisitor) {
        Objects.requireNonNull(exprVisitor) ;
        walk(expr, null, exprVisitor);
    }
    
    /** Walk visiting every {@link Op} and every {@link Expr},
     *  starting from an {@link Expr}.
     */
    public static void walk(Expr expr, OpVisitor opVisitor, ExprVisitor exprVisitor) {
        walk(expr, opVisitor, exprVisitor, null, null) ;
    }
    
    /** Walk visiting every {@link Op} and every {@link Expr},
     *  starting from an {@link Expr}.
     */
    public static void walk(Expr expr, OpVisitor opVisitor, ExprVisitor exprVisitor, OpVisitor beforeVisitor, OpVisitor afterVisitor) {
        if ( expr == null )
            return ;
        Objects.requireNonNull(expr) ;
        createWalker(opVisitor, exprVisitor, beforeVisitor,afterVisitor).walk(expr);
    }
    

    /** Walk visiting every {@link Expr} with an {@link ExprVisitor},
     * including inside any {@link Op} in expressions.
     */
    public static void walk(ExprList exprList, ExprVisitor exprVisitor) {
       walk(exprList, null, exprVisitor);
    }
    
    /** Walk visiting every {@link Op} and every {@link Expr},
     *  starting from an {@link ExprList}.
     */
    public static void walk(ExprList exprList, OpVisitor opVisitor, ExprVisitor exprVisitor) {
        if ( exprList == null )
            return ;
        Objects.requireNonNull(exprVisitor) ;
        exprList.forEach(e->walk(e,opVisitor, exprVisitor)) ;
    }

    public static void walk(VarExprList varExprList, ExprVisitor exprVisitor) {
        Objects.requireNonNull(exprVisitor) ;
        walk(varExprList, null, exprVisitor);
     }
     
     public static void walk(VarExprList varExprList, OpVisitor opVisitor, ExprVisitor exprVisitor) {
         if ( varExprList == null )
             return ;
         varExprList.forEach((v,e)->walk(e,opVisitor, exprVisitor)) ;
     }
 
    private static OpVisitor   nullOpVisitor   = new OpVisitorBase() ;
    private static ExprVisitor nullExprVisitor = new ExprVisitorBase() ;
     
    public static WalkerVisitor createWalker(OpVisitor opVisitor, ExprVisitor exprVisitor) {
        return createWalker(opVisitor, exprVisitor, null, null) ;
    }
    
    public static WalkerVisitor createWalker(OpVisitor opVisitor, ExprVisitor exprVisitor, OpVisitor beforeVisitor, OpVisitor afterVisitor) {
        
        if ( opVisitor == null )
            opVisitor = nullOpVisitor ;
        if ( exprVisitor == null )
            exprVisitor = new ExprVisitorBase() ;
        return new WalkerVisitor(opVisitor, exprVisitor, beforeVisitor, afterVisitor)  ;
    }
    
    /** Transform an {@link Op}. */
    public static Op transform(Op op, Transform opTransform, ExprTransform exprTransform) {
        ApplyTransformVisitor v = createTransformer(opTransform, exprTransform) ;
        return transform(op, v) ;
    }
    
    /** Transform an {@link Op}. */
    public static Op transform(Op op, Transform opTransform, ExprTransform exprTransform, OpVisitor beforeVisitor, OpVisitor afterVisitor) {
        ApplyTransformVisitor v = createTransformer(opTransform, exprTransform) ;
        return transform(op, v, beforeVisitor, afterVisitor) ;
    }

    /** Transform an {@link Expr}. */
    public static Expr transform(Expr expr, Transform opTransform, ExprTransform exprTransform) {
        ApplyTransformVisitor v = createTransformer(opTransform, exprTransform) ;
        return transform(expr, v) ;
    }

    /** Transform an {@link Op}. */
    public static Op transform(Op op, ApplyTransformVisitor v) {
        return transform(op, v, null, null) ;
    }

    /** Transform an {@link Op}. */
    public static Op transform(Op op, ApplyTransformVisitor v, OpVisitor beforeVisitor, OpVisitor afterVisitor) {
        walk(op, v, v, beforeVisitor, afterVisitor) ;
        return v.opResult() ;
    }

    /** Transform an {@link Expr}. */
    public static Expr transform(Expr expr, ApplyTransformVisitor v) {
        walk(expr, v) ;
        return v.exprResult() ;
    }

    /** Transform an {@link Expr}. */
    public static Expr transform(Expr expr, ApplyTransformVisitor v, OpVisitor beforeVisitor, OpVisitor afterVisitor) {
        walk(expr, v, v, beforeVisitor, afterVisitor) ;
        return v.exprResult() ;
    }

    /** Transform an algebra expression */
    public static Op transform(Op op, Transform transform) {
       return transform(op, transform, null) ;
    }
    
    /** Transform an expression */
    public static Expr transform(Expr expr, ExprTransform exprTransform) {
        return transform(expr, null, exprTransform) ;
    }
        
    private static Transform     nullOpTransform   = new TransformBase() ;
    private static ExprTransform nullExprTransform = new ExprTransformBase() ;
    
    public static ApplyTransformVisitor createTransformer(Transform opTransform, ExprTransform exprTransform) {
        return createTransformer(opTransform, exprTransform, null, null) ;
    }
 
    public static ApplyTransformVisitor createTransformer(Transform opTransform, ExprTransform exprTransform, OpVisitor beforeVisitor, OpVisitor afterVisitor) {
        if ( opTransform == null )
            opTransform = nullOpTransform ;
        if ( exprTransform == null )
            exprTransform = nullExprTransform ;
        return new ApplyTransformVisitor(opTransform, exprTransform, null, null) ;
    }
}

