package com.apus.verbs

class Verb(val verb: String, val simple_past: String, val past_participle: String, val spanish: String){
    override fun toString() = "$verb - $simple_past - $past_participle - $spanish"
}