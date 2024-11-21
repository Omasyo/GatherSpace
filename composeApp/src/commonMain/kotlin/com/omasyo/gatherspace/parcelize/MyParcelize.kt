package com.omasyo.gatherspace.parcelize

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
// No `expect` keyword here
annotation class MyParcelize()

expect interface MyParcelable