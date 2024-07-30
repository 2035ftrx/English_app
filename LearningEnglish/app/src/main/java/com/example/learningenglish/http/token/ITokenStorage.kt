package com.example.learningenglish.http.token

interface ITokenStorage {

    fun saveToken(token: String)

    fun getToken():String?

    fun cleanToken():Boolean

}