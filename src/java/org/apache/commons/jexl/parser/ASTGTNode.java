/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", "Jexl" and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.jexl.parser;

import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.util.Coercion;

/**
 *  GT : a > b
 *
 *  Follows A.3.6.1 of the JSTL 1.0 specification
 *
 *  @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 *  @author <a href="mailto:proyal@apache.org">Peter Royal</a>
 *  @version $Id: ASTGTNode.java,v 1.3 2003/06/22 19:43:01 proyal Exp $
 */
public class ASTGTNode extends SimpleNode
{
    public ASTGTNode( int id )
    {
        super( id );
    }

    public ASTGTNode( Parser p, int id )
    {
        super( p, id );
    }

    /** Accept the visitor. **/
    public Object jjtAccept( ParserVisitor visitor, Object data )
    {
        return visitor.visit( this, data );
    }

    public Object value( JexlContext jc )
        throws Exception
    {
        /*
         * now get the values
         */

        Object left = ( (SimpleNode)jjtGetChild( 0 ) ).value( jc );
        Object right = ( (SimpleNode)jjtGetChild( 1 ) ).value( jc );

        if( ( left == right ) || ( left == null ) || ( right == null ) )
        {
            return Boolean.FALSE;
        }
        else if( Coercion.isFloatingPoint( left ) || Coercion.isFloatingPoint( right ) )
        {
            double leftDouble = Coercion.coerceDouble( left ).doubleValue();
            double rightDouble = Coercion.coerceDouble( right ).doubleValue();

            return leftDouble > rightDouble
                ? Boolean.TRUE
                : Boolean.FALSE;
        }
        else if( Coercion.isNumberable( left ) || Coercion.isNumberable( right ) )
        {
            long leftLong = Coercion.coerceLong( left ).longValue();
            long rightLong = Coercion.coerceLong( right ).longValue();

            return leftLong > rightLong
                ? Boolean.TRUE
                : Boolean.FALSE;
        }
        else if( left instanceof String || right instanceof String )
        {
            String leftString = left.toString();
            String rightString = right.toString();

            return leftString.compareTo( rightString ) > 0
                ? Boolean.TRUE
                : Boolean.FALSE;
        }
        else if( left instanceof Comparable )
        {
            return ( (Comparable)left ).compareTo( right ) > 0
                ? Boolean.TRUE
                : Boolean.FALSE;
        }
        else if( right instanceof Comparable )
        {
            return ( (Comparable)right ).compareTo( left ) < 0
                ? Boolean.TRUE
                : Boolean.FALSE;
        }

        throw new Exception( "Invalid comparison : GT " );
    }
}
