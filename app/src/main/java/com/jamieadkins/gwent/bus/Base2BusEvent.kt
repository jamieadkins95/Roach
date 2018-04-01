package com.jamieadkins.gwent.bus

open class Base2BusEvent<out A, out B>(val first: A, val second: B)