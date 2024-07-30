package com.example.learningenglish.user

sealed interface ILoginScreen {

    data object Register : ILoginScreen

    data object Login : ILoginScreen

    data object AdminLogin : ILoginScreen
}