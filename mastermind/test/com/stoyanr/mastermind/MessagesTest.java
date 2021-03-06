/*
 * $Id: $
 *
 * Copyright 2012 Stoyan Rachev (stoyanr@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stoyanr.mastermind;

import org.junit.Test;

import static com.stoyanr.mastermind.Messages.*;
import static org.junit.Assert.assertEquals;

public class MessagesTest
{
    private static final int KEY = 10;
    
    private static final String M_WRONG_MESSAGE = "Wrong message:";

    @Test
    public final void testMsg()
    {
        final String expected = "!" + PACKAGE + "." + Integer.toString(KEY) + "!";
        assertEquals(M_WRONG_MESSAGE, expected, msg(KEY));
    }

}
