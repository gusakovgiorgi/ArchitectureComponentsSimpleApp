package com.example.simpleappwithmoderntechnologies.repostiory.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}